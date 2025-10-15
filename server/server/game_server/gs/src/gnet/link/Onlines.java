package gnet.link;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import mkdb.Procedure.IOnlines;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.state.StateManager;

public class Onlines extends mkio.Manager implements IOnlines {
	static private final Logger logger=Logger.getLogger("SYSTEM");
	
	private static Onlines instance = null;

	public Onlines() {
		instance = this;
	}

	public static Onlines getInstance() {
		return instance;
	}

	private Set<Integer> binds = new HashSet<Integer>();

	@Override
	protected void parse(Element self) throws Exception {
		super.parse(self);
		for (String s : self.getAttribute("bind").split(","))
			binds.add(Integer.valueOf(s));
	}

	/**
	 * 必须在 xdb.startWithNetwork 之前初始化。
	 * @param conf
	 */
	public static void loadConf(String conf) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(conf);
		mkio.Engine.getInstance().register(new mkio.MkioConf(doc.getDocumentElement()));
	}

	//////////////////////////////////////////////////////////////////////////////
	// application interface

	private Map<Long, Role> roles = new HashMap<Long, Role>();

	public Role find(Long roleid) {
		synchronized (roles) {
			return roles.get(roleid);
		}
	}

	public void remove(Long roleid) {
		synchronized (roles) {
			roles.remove(roleid);
			logger.error(new StringBuilder("roleId=").append(roleid).append(" remove"));
			/*Role role = roles.remove(roleid);
			if (null != role) SetLogin挪到User登录时，不然在角色选择和排队时GS收不到LinkBroken
				role.setLogin(gnet.link.SetLogin.eLogout);*/
		}
	}

	public Role find(mkio.Mkio io, int linksid) {
		Link link = find(io);
		if (null != link)
			return link.find(linksid);
		return null;
	}

	/**
	 * 根据从glinkd转发过来的协议，找到Role对象。
	 * 
	 * @param p2
	 * @return 如果p2不是从glinkd转发过来的协议，返回null
	 */
	public Role find(mkio.Protocol p2) {
		if (null != p2.getContext())
			return find(p2.getConnection(), ((Dispatch) p2.getContext()).linksid);
		return null;
	}
	
	//XXX: 写应用逻辑的时候不要直接调用这个方法。或者，必须手动判断下玩家是不是EnterWorld状态
	public long findRoleid(mkio.Protocol p2) {
		Role r=this.find(p2);
		if(r!=null) return r.getRoleid();
		else return -1;
	}

	// override this to create a subclass of Role 
	protected Role newRole(int userid, long roleid) {
		return new Role(userid, roleid);
	}

	public Role insert(Link link, int linksid, int userid, long roleid) {
		logger.error(new StringBuilder("roleId=").append(roleid).append(" insert linksid = ").append(linksid).append(" userid=").append(userid));
		if (null != link) {
			synchronized (roles) {
				/*
				 * XXX 有个问题: 正在处理的协议返回结果时，可能会发送给新的连接。
				 */
				Role old = roles.get(roleid);
				if (null != old) {
					old.linkAttach(link, linksid);
					return old;
				}
				Role role = newRole(userid, roleid);
				roles.put(roleid, role);
				role.linkAttach(link, linksid);
				return role;
			}
		}
		else
			logger.error(new StringBuilder("roleId=").append(roleid).append(" Insert Role : Link not found"));
		return null;
	}
	

	public Role insert(int ip, int port, int linksid, int userid, long roleid) {
		return insert(find(ip, port), linksid, userid, roleid);
	}

	public Role insert(mkio.Protocol p2, long roleid) {
		Dispatch ctx = (Dispatch) p2.getContext();
		return insert(find(p2.getConnection()), ctx.linksid, ctx.userid, roleid);
	}

	/**
	 * 发送协议给当前协议上下文对应的客户端。
	 * @param THIS 当前协议
	 * @param p2   需要发送的协议
	 * @return
	 */
	public boolean sendResponse(mkio.Protocol THIS, mkio.Protocol p2) {
		Dispatch ctx = (Dispatch) THIS.getContext();
		Send msg = new Send();
		msg.linksids.add(ctx.linksid);
		msg.ptype = p2.getType();
		msg.pdata = new OctetsStream().marshal(p2);
		return sendSend(msg, ctx.getConnection());
	}

	/**
	 * 单个目标发送协议
	 */
	public boolean send(Long roleid, mkio.Protocol p2) {
		if(roleid == fire.pb.scene.MapThread.serialVersionUID)
		{//roleid为MapThread.serialVersionUID，表示发送给场景，不是发给某个具体玩家
			return fire.pb.scene.MapThread.getInstance().add(p2);
		}
		Role role = find(roleid);
		if (null != role)
			return role.send(p2);
		return false;
	}
	
	/**
	 * 单个目标发送协议
	 */
	public boolean sendOctets(Long roleid, int ptype, Octets poctets) {
		Role role = find(roleid);
		if (null != role)
			return role.sendOctets(ptype, poctets);
		return false;
	}
	public static long onlinesendp1  = 0;
	public static long onlinesendp2  = 0;
	/**
	 * 单个目标发送协议
	 */
/*	public boolean sendtest(Long roleid, mkio.Protocol p2) {
		long start = System.currentTimeMillis();
		Role role = find(roleid);
		long start1 = System.currentTimeMillis();
		if (null != role)
		{
			boolean b =  role.sendtest(p2);
			long end = System.currentTimeMillis();
			onlinesendp1 += (start1 - start);
			onlinesendp2 += (end - start1);
			return b;
		}
		return false;
	}*/

	/**
	 * 群发，给多个玩家发送协议。
	 * 
	 * 根据玩家所在的link分组发送。
	 * 
	 * @return
	 *     true 全部发送成功。
	 *     false 发送失败（可能部分发送成功）。不报告所有错误，仅记录日志。
	 */
	public boolean send(Set<Long> roleids, mkio.Protocol p2) {
		if(roleids.isEmpty() || p2 == null)
			return false;
		
		Map<Link, HashSet<Integer>> group = new HashMap<Link, HashSet<Integer>>();

		boolean rc = true;
		for (Long roleid : roleids) {
			Role role = find(roleid);
			if (null == role) {
				//logger.debug("send2 role not found , roleid=" + roleid + ", protocol= " + p2.getType());
				rc = false;
				continue;
			}
			Link.Session ls = role.getLinkSession();
			if (null == ls) {
				rc = false;
				//logger.warn("send2 role has broken, roleid=" + roleid);
				continue;
			}
			HashSet<Integer> si = group.get(ls.getLink());
			if (null == si)
				group.put(ls.getLink(), si = new HashSet<Integer>());
			si.add(ls.getSid());
		}

		Send msg = new Send();
		msg.ptype = p2.getType();
		msg.pdata = new OctetsStream().marshal(p2);

		for (Map.Entry<Link, HashSet<Integer>> e : group.entrySet()) {
			msg.linksids.clear();
			if (send(e.getKey(), e.getValue(), msg))
				continue;
			rc = false;
			logger.warn("send2 error, p2=" + Integer.toHexString(p2.getType()) + "link=" + e.getKey());
		}
		return rc;
	}
	
	
	/**
	 * 群发，给多个玩家发送协议。
	 * 
	 * 根据玩家所在的link分组发送。
	 * 
	 * @return
	 *     true 全部发送成功。
	 *     false 发送失败（可能部分发送成功）。不报告所有错误，仅记录日志。
	 */
	public boolean sendOctets(Set<Long> roleids, int ptype, Octets poctets) {
		if(roleids.isEmpty() || poctets == null)
			return false;
		Map<Link, HashSet<Integer>> group = new HashMap<Link, HashSet<Integer>>();

		boolean rc = true;
		for (Long roleid : roleids) {
			Role role = find(roleid);
			if (null == role) {
				logger.debug(new StringBuilder("roleId=").append(roleid).append(" send2 role not found "));
				rc = false;
				continue;
			}
			Link.Session ls = role.getLinkSession();
			if (null == ls) {
				rc = false;
				logger.debug(new StringBuilder("roleId=").append(roleid).append(" send2 role has broken"));
				continue;
			}
			HashSet<Integer> si = group.get(ls.getLink());
			if (null == si)
				group.put(ls.getLink(), si = new HashSet<Integer>());
			si.add(ls.getSid());
		}

		Send msg = new Send();
		msg.ptype = ptype;
		msg.pdata = poctets;

		for (Map.Entry<Link, HashSet<Integer>> e : group.entrySet()) {
			msg.linksids.clear();
			if (send(e.getKey(), e.getValue(), msg))
				continue;
			rc = false;
			logger.warn("send2 error, p2=" + Integer.toHexString(ptype) + "link=" + e.getKey());
		}
		return rc;
	}

	public static final int TRUNK_SEND_COUNT = 300;

	static public boolean sendSend(Send msg, mkio.Mkio mkio) 
	{
		boolean succ = msg.send( mkio);
		return succ;
	}
	
	static public boolean sendProtocl(mkio.Protocol p, mkio.Mkio mkio) 
	{
		return p.send( mkio);
	}
	

	/**
	 * 通过指定的link转发协议。
	 * 
	 * [优化] linksids的类型必须是HashSet，当不需要分组时，可以直接赋给msg.linksids。
	 * 
	 * @param link     指定的link
	 * @param linksids 必须都是玩家在指定link上的会话编号。当数量很大时，会被分成多个协议包发送．
	 * @param msg      其中ptype,pdata在调用前初始化，linksids在函数内部初始化。
	 * @return
	 *     true 全部发送成功。
	 *     false 发送失败（可能部分发送成功）。
	 */
	public boolean send(Link link, HashSet<Integer> linksids, Send msg) {
		if (linksids.size() <= TRUNK_SEND_COUNT) {
			msg.linksids = linksids; // 优化
			return sendSend( msg, link.getXio());
		}
		// trunk send
		boolean rc = true;
		for (Integer linksid : linksids) {
			if (msg.linksids.size() >= TRUNK_SEND_COUNT) {
				rc = rc && sendSend(msg,link.getXio());
				msg.linksids.clear();
			}
			msg.linksids.add(linksid);
		}
		if (false == msg.linksids.isEmpty()) // last trunk
			rc = rc && sendSend( msg, link.getXio());
		msg.linksids.clear();
		return rc;

	}

	/**
	 * 通过指定的link转发协议。
	 * boolean send(Link link, HashSet<Integer> linksids, Send msg)
	 */
	boolean send(Link link, HashSet<Integer> linksids, mkio.Protocol p2) {
		Send msg = new Send();
		msg.ptype = p2.getType();
		msg.pdata = new OctetsStream().marshal(p2);
		return this.send(link, linksids, msg);
	}

	/**
	 * 给所有在线玩家发送协议。
	 */
	public void broadcast(mkio.Protocol p2) {
		broadcast(p2, 0);
	}

	public Collection<Role> getRoles() {
		synchronized (roles) {
			return roles.values();
		}
	}

	public List<Role> getSafeRoles() {
		List<Role> temp = new LinkedList<Role>();
		synchronized (roles) {
			temp.addAll(roles.values());
		}
		return temp;
	}

	/**
	 * 给所有在线玩家发送协议。
	 * @param protocol
	 * @param time 全部广播完成的时间。对于广告类的消息不要求很及时，可以设置较长的广播时间。
	 */
	public void broadcast(mkio.Protocol p2, int timems) {
		Broadcast bc = new Broadcast();
		bc.ptype = p2.getType();
		bc.pdata = new OctetsStream().marshal(p2);
		bc.time = timems;
		for (Link link : this.links()) {
			//if (bc.send(link.getXio()))
			if (sendProtocl(bc,link.getXio()))
				continue;
			logger.warn("broadcast error, p2=" + Integer.toHexString(p2.getType()) + " link=" + link);
		}
		if(logger.isDebugEnabled())
			logger.debug(new StringBuilder("BroadCast : class=").append(p2.getClass().getName())
					.append(" size=").append(bc.pdata.size())
					.append(" time=").append(bc.time)
					.append(" rolenum=").append(roles.size()));
	}

	// ///////////////////////////////////////////////////////////////////////////
	// glinkd 支持的内部操作辅助函数。
	public boolean kick(Long roleid, int error) {
		logger.error(new StringBuilder("roleId=").append(roleid).append(" kick role error=").append(error));
		Role role = find(roleid);
		if (null != role)
			return role.kick(error);
		return false;
	}

	// 绑定单个玩家。
	// 所有玩家默认绑定需要在 OnAddSession 中发送绑定协议。这里不提供辅助操作方法。
	public boolean bind(Long roleid, int pvid) {
		logger.error(new StringBuilder("roleId=").append(roleid).append(" bind  pvid=").append(pvid));
		Role role = find(roleid);
		if (null != role)
			return role.bind(pvid);
		return false;
	}

	public boolean unbind(Long roleid, int pvid) {
		logger.error(new StringBuilder("roleId=").append(roleid).append(" unbind  pvid=").append(pvid));
		Role role = find(roleid);
		if (null != role)
			return role.unbind(pvid);
		return false;
	}

	// ////////////////////////////////////////////////////////////////////////////
	// manager implement
	private Map<InetSocketAddress, Link> links = new HashMap<InetSocketAddress, Link>();
	private Object mutex = new Object();

	public Link[] links() {
		synchronized (mutex) {
			return links.values().toArray(new Link[0]);
		}
	}

	public Link find(int linkid) {
		synchronized (mutex) {
			for (Link link : links.values())
				if (link.getLinkid() == linkid)
					return link;
			return null;
		}
	}

	public Link find(int peerip, int port) {
		synchronized (mutex) {
			return links.get(mkio.Helper.inetSocketAddress(peerip, port));
		}
	}

	public Link find(mkio.Mkio io) {
		synchronized (mutex) {
			return links.get(io.getPeer());
		}
	}

	@Override
	public mkio.Mkio get() {
		synchronized (mutex) {
			if (links.isEmpty())
				return null;
			return links.values().iterator().next().getXio();
		}
	}

	@Override
	public int size() {
		synchronized (mutex) {
			return links.size();
		}
	}

	@Override
	protected void addMkio(mkio.Mkio io) {
		synchronized (mutex) {
			if (null != links.get(io.getPeer())) {
				logger.error("duplicate connection " + io);
				io.close();
				return;
			}
			links.put(io.getPeer(), new Link(io));
		}
		// bind all gs provider protocol to me
		for (Integer pvid : binds) {
			gnet.link.Bind bind = new gnet.link.Bind();
			bind.pvid = pvid;
			sendProtocl(bind, io);
			//bind.send(io);
		}
		// 通知link开始监听
		LinkServerControl lsc = new LinkServerControl();
		lsc.ptype = LinkServerControl.E_START_LISTEN;
		sendProtocl(lsc, io);
		logger.info("Onlines start Listen:" + io);
	}

	@Override
	protected void removeMkio(mkio.Mkio io, Throwable e) {
		Link link;
		synchronized (mutex) {
			link = links.remove(io.getPeer());
		}
		if (null != link) {
			logger.info("Onlines removeXio " + io, e);
			Collection<Role> roles = link.close();
			Handle volatileTemp = handle;
			if (null != volatileTemp) {
				try {
					volatileTemp.onManagerBroken(roles);
				} catch (Throwable ex) {
					logger.error("onManagerBroken " + io, ex);
				}
			}
			
		}
	}

	@Override
	protected void close() {
		super.close();
		Map<InetSocketAddress, Link> tmp;
		synchronized (mutex) {
			tmp = links;
			links = new HashMap<InetSocketAddress, Link>();
		}
		
//		for (Link l : tmp.values()) {
//			LinkServerControl lsc = new LinkServerControl();
//			lsc.ptype = LinkServerControl.E_STOP_LISTEN;
//			sendProtocl(lsc, l.getXio());
//			logger.debug("Onlines stop Listen: " + l.getXio());
//		}
		for (Link l : tmp.values())
			l.close();
	}

	public static interface Handle {
		public void onManagerBroken(Collection<Role> roles);
		public void onLinkBroken(Role role, int reason);

	}

	private volatile Handle handle;

	public void setHandle(Handle handle) {
		this.handle = handle;
	}

	void process(AnnounceLinkId p) {
		Link link = find(p.getConnection());
		if (null == link) {
			logger.error(new StringBuilder("link not found! linkid=").append( p.linkid).append(p.getConnection()));
			return;
		}
		// linkid duplicate-check and set
		synchronized (mutex) {
			if (null != find(p.linkid)) {
				logger.error(new StringBuilder("duplicate linkid found! linkid=").append( p.linkid).append(link.getXio()));
				link.getXio().close();
			} else
				link.setLinkid(p.linkid);
		}
	}

	void process(LinkBroken p) {
		Link link = find(p.getConnection());
		if (null != link) {
			
			//处理Role断线
			Role role = link.find(p.linksid);
			if (null != role) {
				role.linkBroken();
				// volatile 的特性问题，这样写，可以避免判断然后读取。
				// 只要简单读写是安全的，先读到temp中，以后就使用这个快照。
				Handle volatileTemp = handle;
				if (null != volatileTemp) {
					try {
						volatileTemp.onLinkBroken(role, p.reason);
					} catch (Throwable e) {
						logger.error("onLinkBroken " + role, e);
//						logger.error(new StringBuilder("userid=").append(p.userid).append(" Onlines offline "));
					}
				}
			}
			else
				logger.debug("LinkBroken not Role");
			
			//处理User断线
			StateManager.logger.error(new StringBuilder("userid=").append(p.userid).append(" Onlines offline "));
			getConnectedUsers().offline(p.userid, true);
				
		}
	}
	
	
	
	/********************************nobody：添加针对User和排队的方法*********************************/
	
	public ConnectedUsers getConnectedUsers() {
		return users;
	}
	
	private ConnectedUsers users = new ConnectedUsers();//所有已创建连接的用户

}
