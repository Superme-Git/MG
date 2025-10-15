package gnet.link;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.PropConf.Sys;
import fire.pb.SSendQueueInfo;
import fire.pb.main.ConfigManager;
import fire.pb.main.JumpQueueUser;
import fire.pb.main.LoginQueueUp;

/**
 * 登录排队逻辑处理
 * 
 * @author liangyanpeng
 *
 */
public class ConnectedUsers extends HashMap<Integer, User> {
	
	static Map<Integer, LoginQueueUp> loginQueueUpMap = ConfigManager.getInstance()
			.getConf(LoginQueueUp.class);
	
	private static int broadcastQueuePeriod = 20000;// 广播客户端刷新排队信息的最小间隔
	
	private static final Logger LOG = Logger.getLogger("SYSTEM");
	
	private static final long serialVersionUID = -2032473210881256513L;
	
	/**
	 * 健康连接数量
	 */
	public int healthConnectedUserNumber = 1;
	/**
	 * 排队阀值
	 */
	public int queueUpNumber = 1;
	/**
	 * 最大连接数(不接受登陆)
	 */
	public int maxConnectedUserNumber = 10;// 
	/**
	 * 排队用户队列
	 */
	private QueueUpQueue normalQueue = new QueueUpQueue(User.STATE_IN_QUEUE);// 
	/**
	 * 优先用户队列
	 */
	private QueueUpQueue priorQueue = new QueueUpQueue(User.STATE_ZERO_QUEUE);// 
	/**
	 * 缓慢进入队列
	 */
	private QueueUpQueue slowQueue = new QueueUpQueue(User.STATE_SLOW_QUEUE);// 
	/**
	 * 断线用户保留two minute,需要定期清空.(当再次登录的时候会进入优先用户队列快速进入)
	 */
	private Map<Integer, Long> priorQueueTickets = new HashMap<Integer, Long>();// 
	/**
	 * 最后缓慢进入等待时间
	 */
	private volatile long lastSequenceWaitTime = 0L;// 
	/**
	 * 最大排队,队列长度
	 */
	int maxQueueSize = 0;
	/**
	 * 0排位票的保存最长时间
	 */
	private static int zeroQueueTicketTime = 120000; // 
	/**
	 * 平均进入速度(默认最小3人/分钟)
	 */
	public int avgLoginSpeed = 3;// 
	/**
	 * 当前Broadcast间隔内登入的人数
	 */
	private int curLoginNum = 0;// 
	/**
	 * 上一次广播时间
	 */
	private long lastBroadcastTime = 0; // 
	/**
	 * 记录最近3次的登入速度
	 */
	private List<Integer> latelyThreeCountSpeeds = new LinkedList<Integer>();// 
	/**
	 * 登录排队限制配置列表
	 */
	private Collection<LoginQueueUp> loginQueueUpLst = null;
	public ConnectedUsers() {
		super();
		initialization();
	}
	
	/**
	 * 初始化常量
	 */
	private void initialization() {
		int confSize = loginQueueUpMap.size();
		LoginQueueUp queueUpNumberConf = loginQueueUpMap.get(confSize);
		LoginQueueUp healthConnectedUserNumberConf = loginQueueUpMap.get(1);
		
		if (queueUpNumberConf == null || healthConnectedUserNumberConf == null) {
			LOG.error("f服务器登录排队配置错误!");
			System.exit(-1);
			return;
		}
		
		loginQueueUpLst = loginQueueUpMap.values();
		
		healthConnectedUserNumber = healthConnectedUserNumberConf.connectusernum;
		queueUpNumber = queueUpNumberConf.connectusernum;
		maxConnectedUserNumber = Sys.MAX_CONNECTED_NUMBER;
		zeroQueueTicketTime = Sys.ZERO_QUEUE_TICKET_TIME;
		broadcastQueuePeriod = Sys.BROADCAST_QUEUE_PERIOD;
		
		maxQueueSize = maxConnectedUserNumber - queueUpNumber;// 排队用户数最大限制
	}

	// 定时广播排队信息
	synchronized void broadcastQueueInfo(long now) {
		long period = now - lastBroadcastTime;
		if (period < broadcastQueuePeriod)
			return;
		lastBroadcastTime = now;
		
		// 缓慢进入队列
		int sidex = 0;
		for (User user : slowQueue.values()) {
			sidex++;
			user.setSlowQueueIndex(sidex);// 排队位置可能有变化, 更新下
			slowQueue.transferQueueInformation(user);
		}
		
		// 优先队列
		int size = normalQueue.size();
		if (priorQueue.size() > 0) {
			SSendQueueInfo snd = new SSendQueueInfo(0, size, 0);
			send(priorQueue.values(), snd);
		}

		// 正常队列
		avgLoginSpeed = calcAvgLoginSpeed(period);

		int i = 0;
		for (User user : normalQueue.values()) {
			i++;
			SSendQueueInfo snd = new SSendQueueInfo(i, size, ((i / avgLoginSpeed) + 1) * 60);// 以前是分钟现在改成发送秒数, 所以乘以60秒
			user.send(snd);
		}
		// 清理过期票
		List<Integer> timeoutTickets = new ArrayList<Integer>();
		for (Map.Entry<Integer, Long> entry : priorQueueTickets.entrySet()) {
			if ((now - entry.getValue()) > zeroQueueTicketTime)
				timeoutTickets.add(entry.getKey());
		}
		for (int userid : timeoutTickets)
			priorQueueTickets.remove(userid);
	}

	/**
	 * 计算平均登入速度
	 * 
	 * @return 速度值
	 */
	private int calcAvgLoginSpeed(long period) {
		int curspeed = (int) (curLoginNum * (60000L / period));

		latelyThreeCountSpeeds.add(curspeed);
		int totalSpeed = 0;
		for (int speed : latelyThreeCountSpeeds)
			totalSpeed += speed;
		int avgSpeed = totalSpeed / latelyThreeCountSpeeds.size();
		if (latelyThreeCountSpeeds.size() >= 3)
			latelyThreeCountSpeeds.remove(0);
		curLoginNum = 0;
		return Math.max(3, avgSpeed);// 最小1分钟进3人
	}

	/**
	 * 通过userid获取User，可能在登入的账号里，也可能在排队中 User里的getState表示该账户处于的状态
	 * 
	 * @param userid
	 * @return User
	 */
	public User getUserByUserId(int userid) {
		User user = get(userid);
		if (user != null)
			return user;
		user = priorQueue.get(userid);
		if (user != null)
			return user;
		user = normalQueue.get(userid);
		if (user != null)
			return user;

		return null;
	}

	@Override
	public synchronized User get(Object key) {
		return super.get(key);
	}

	/**
	 * 从两个排队队列中获取一个正在排队的User
	 * 
	 * @return null表示队列中无正在排队的玩家
	 */
	public User getUserFromQueue() {
		User user = getUserFromQueue(priorQueue);
		if (user != null)
			return user;

		return getUserFromQueue(normalQueue);
	}

	private User getUserFromQueue(QueueUpQueue queue) {
		if (queue.isEmpty())
			return null;
		long now = System.currentTimeMillis();
		Iterator<User> it = queue.values().iterator();
		while (it.hasNext()) {
			User user = it.next();
			it.remove();
			if (!user.isLinkBroken()) {
				broadcastQueueInfo(now);// 广播队列信息
				return user;
			}
			priorQueueTickets.put(user.getUserid(), now);// 排到他，他却掉线
		}
		return null;
	}
	
	/**
	 * 返回当前连接用户数(已登录+缓慢登录)
	 */
	private synchronized int getConnectUserCount() {
		return this.size() + slowQueue.size();
	}

	/**
	 * @return 返回排队人数
	 */
	public synchronized int getQueueUpNumber() {
		return normalQueue.size() + priorQueue.size();
	}

	int getUserQueueSize(int queueUserState) {
		if (queueUserState == User.STATE_IN_QUEUE)
			return normalQueue.size();
		else if (queueUserState == User.STATE_ZERO_QUEUE)
			return priorQueue.size();
		else
			return 0;
	}

	/**
	 * 断线处理
	 * 
	 * @param userid
	 * @param isLinkBroken true 断线处理, false主动离开处理
	 * @return User
	 */
	public synchronized User offline(int userid, boolean isLinkBroken) {
		User user = remove(userid);
		if (user != null) {
			if (isLinkBroken) {
				priorQueueTickets.put(userid, System.currentTimeMillis());
			}
			return user;
		}
		
		user = slowQueue.get(userid);
		if (user != null) {
			if (isLinkBroken)
				user.setLinkBroken(true);
			else {
				slowQueue.remove(userid);
				broadcastQueueInfo(System.currentTimeMillis());
			}
			return user;
		}

		user = priorQueue.get(userid);
		if (user != null) {
			if (isLinkBroken)
				user.setLinkBroken(true);
			else
				priorQueue.remove(userid);
			return user;
		}

		user = normalQueue.get(userid);
		if (user != null) {
			if (isLinkBroken)
				user.setLinkBroken(true);
			else {
				normalQueue.remove(userid);
				broadcastQueueInfo(System.currentTimeMillis());
			}
			return user;
		}
		return null;
	}

	/**
	 * User登录，插入User并发送相应协议
	 * 
	 * @param link
	 * @param linksid
	 * @param userId
	 * @return User
	 */
	private User online(Link link, int linksid, int userId, boolean isGm) {
		LOG.error(new StringBuilder("User登录 UserId = ").append(userId).append(" linksid= ").append(linksid));
		if (null != link) {
			User oldUser = get(userId);
			if (oldUser != null) {
				LOG.error(new StringBuilder("如果在线用户列表中存在，直接返回。UserId = ").append(userId));
				
				remove(userId);
								
//				return oldUser;
			}
			// 已登录
			oldUser = dealOldUserOnline(oldUser, link, linksid, userId);
			if (oldUser != null) {
				return oldUser;
			}
			// 新登录
			return dealNewUserOnline(link, linksid, userId, isGm);
		} else
			LOG.info("Can't found User Link");
		return null;
	}

	/**
	 * @return 返回新登陆用户对象, 或者返回登录失败协议
	 */
	private User dealNewUserOnline(Link link, int linksid, int userid, boolean isAdmin) {
		User user = new User(userid);
		user.linkAttach(link, linksid);
		long now = System.currentTimeMillis();
		
		// 检测是否到达健康上限
		if (this.healthConnectedUserNumber > size()) {
			put(userid, user);
			return user;
		}
		
		// 如果是白名单,直接进入
		if (ConfigManager.getInstance().getConf(JumpQueueUser.class).containsKey(userid)) {
			put(userid, user);
			return user;
		}

		// 游戏管理员,直接进入
		if (isAdmin) {
			put(userid, user);
			return user;
		}
		
		// 检测是否到达排队阀值
		if (getConnectUserCount() >= this.healthConnectedUserNumber && getConnectUserCount() < this.queueUpNumber) {
			// 断线用户直接进入
			Long time = priorQueueTickets.get(userid);
			if (time != null) {
				priorQueueTickets.remove(userid);
				if ((now - time) < zeroQueueTicketTime) {
					put(user.getUserid(), user);
					return user;
				}
			}
			
			slowQueue.put(userid, createSlowUser(user, now));
			broadcastQueueInfo(now);
			return user;
		}
		
		// 达到健康上限,如果是断线保留用户
		Long time = priorQueueTickets.get(userid);
		if (time != null) {
			priorQueueTickets.remove(userid);
			if ((now - time) < zeroQueueTicketTime) {
				priorQueue.put(user.getUserid(), user);
				broadcastQueueInfo(now);
				return user;
			}
		}

		// 检测排队人数是否已满
		if (this.getQueueUpNumber() < maxQueueSize) {
			normalQueue.put(user.getUserid(), user);
			broadcastQueueInfo(now);
			return user;
		}
		
		// 登录失败
		user.setState(User.STATE_FAILED);
		user.send(new SSendQueueInfo(-1, -1, -1));
		return user;
	}

	/**
	 * @return 返回已登录用户, null代表新用户
	 */
	private User dealOldUserOnline(User oldUser, Link link, int linksid, int userid) {
		QueueUpQueue queue = null;
		long now = System.currentTimeMillis();
		
		// 优先队列
		oldUser = priorQueue.get(userid);
		queue = priorQueue;

		// 缓慢进入队列
		if (null == oldUser) {
			oldUser = slowQueue.get(userid);
			queue = slowQueue;
			if (null != oldUser) {// 到时间放行进入
				if (now >= oldUser.getEnterTime()) {
					User user = this.put(userid, oldUser);
//					if (user != null) {
						slowQueue.remove(userid);
//					}
					return oldUser;
				}
			}
		}
		
		// 正常队列
		if (null == oldUser) {
			oldUser = normalQueue.get(userid);
			queue = normalQueue;
		}
		
		// 找到
		if (null != oldUser) {
			oldUser.setLinkBroken(false);
			oldUser.linkAttach(link, linksid);
			if (queue != null) {
				queue.transferQueueInformation(oldUser);
			}
		}
		return oldUser;
	}

	public synchronized User online(mkio.Protocol p2, boolean isGm) {
		Dispatch ctx = (Dispatch) p2.getContext();
		return online(Onlines.getInstance().find(p2.getConnection()), ctx.linksid, ctx.userid, isGm);
	}

	@Override
	public synchronized User put(Integer key, User user) {
		curLoginNum++;
		user.setState(User.STATE_LOGIN);
		user.sendRoleList(false);
		return super.put(key, user);
	}

	/**
	 * 根据现在的登入最大人数刷新队列
	 */
	private void refreshQueue() {
		User newuser = getUserFromQueue();
		if (newuser == null)
			return;

		put(newuser.getUserid(), newuser);
	}

	@Override
	public synchronized User remove(Object key) {
		User olduser = super.remove(key);
		if (olduser == null)
			return null;
		olduser.setLogin(SetLogin.eLogout);
		refreshQueue();// 试图选排队的角色登入，达到登入上限则不能登入
		return olduser;
	}

	/**
	 * 发送协议给多个用户
	 * 
	 * 根据用户所在link分组发送
	 * 
	 * @return true 全部发送成功。 false 发送失败（可能部分发送成功）。不报告所有错误，仅记录日志。
	 */
	public boolean send(Collection<User> users, mkio.Protocol protocol) {
		Map<Link, HashSet<Integer>> linkGroup = new HashMap<Link, HashSet<Integer>>();

		boolean rc = true;
		for (User user : users) {
			Link.Session ls = user.getLinkSession();
			if (null == ls) {
				rc = false;
				continue;
			}
			HashSet<Integer> si = linkGroup.get(ls.getLink());
			if (null == si)
				linkGroup.put(ls.getLink(), si = new HashSet<Integer>());
			si.add(ls.getSid());
		}

		Send msg = new Send();
		msg.ptype = protocol.getType();
		msg.pdata = new OctetsStream().marshal(protocol);

		for (Map.Entry<Link, HashSet<Integer>> e : linkGroup.entrySet()) {
			msg.linksids.clear();
			if (Onlines.getInstance().send(e.getKey(), e.getValue(), msg))
				continue;
			rc = false;
			LOG.warn("协议发送错误 , Protocol=" + Integer.toHexString(protocol.getType()) + "link=" + e.getKey());
		}
		return rc;
	}

	/** 
	 * 创建缓慢进入用户对象
	 * @param user
	 */
	private synchronized User createSlowUser(User user, long now) {
		long enterTime = 0L;
		int connectUserNum = this.getConnectUserCount();
		int idx = 0, len = loginQueueUpLst.size();
		for (LoginQueueUp lqu :loginQueueUpLst) {
			if (idx++ == len)// 最后一个是排队阀值不处理
				break;
			int connectUserNumConf = lqu.connectusernum;
			int timeConf = lqu.time;
			double enterUserNumConf = lqu.enterusernum;
			if (timeConf <= 0 || enterUserNumConf <= 0) {
				continue;
			}
			// 进入等待时间unit:millisecond
			int waitTime = (int) (timeConf / enterUserNumConf * 1000);
			
			if (connectUserNum >= connectUserNumConf) {
				if (this.lastSequenceWaitTime < now) {
					enterTime = lastSequenceWaitTime = now + waitTime; 
				}
				else {
					enterTime = lastSequenceWaitTime += waitTime;
				}
				break;
			}
		}
		
		user.setSlowQueueIndex(slowQueue.size());
		user.setEnterTime(enterTime);
		return user;
	}

}
