package robot.task;

import java.util.ArrayList;
import java.util.List;

import mkio.Protocol;
import net.sf.json.JSONObject;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import robot.ConfigMgr;
import robot.LoginRole;
import robot.Main;
import robot.clan.ClanMgr;
import robot.protocol.TestUtils;
import robot.protocol.UniqNameUtils;
import robot.ride.RideMgr;
import fire.pb.battle.CPlayPKFightView;
import fire.pb.friends.CBreakOffRelation;
import fire.pb.friends.CGiveGift;
import fire.pb.friends.CRequestAddFriend;
import fire.pb.friends.CStepSpace;
import fire.pb.http.HttpCallBackHandler;

/**
 * 公共测试类
 * 
 * @author yangtao
 * @dateTime 2016年6月4日 下午12:53:08
 * @version 1.0
 */
public class TestTask extends Task_RoleBase {

	private long lastTime = 0l;

	private long num = 0l;

	private int numMod = 1;

	// 好友人数限制
	public static final int FRIEND_NUM_LIMIT = 1000; // 好友人数限制

	// 当前好友数量
	public int currentNum = 0;

	// 好友数量列表
	List<Long> friendList = new ArrayList<Long>();

	// 踩空间角色列表
	List<Long> spaceRoleIdst = new ArrayList<Long>();

	private MoveBattle moveBattle = null;

	private TeamTask randomTeamTask = null;

	public static final String address = ConfigMgr.getInstance().weiBoAddress;

	public static final String func = "/bbs/create";

	/**
	 * 是否按照数量进行下次循环
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月16日 上午10:55:12
	 * @version 1.0
	 */
	boolean isNum = true;

	public TestTask(final LoginRole role) {
		super(role);
		this.init();
	}

	private void init() {
		// 调整等级
		if (this.role.getLevel() < 1) {
			this.role.sendCommand("//addlevel " + (50 - this.role.getLevel()) + " equip");
		}
		// 切磋
		int protocolsType = Main.getProtocolsType();
		if (protocolsType == 4) {
			changeMapByPK();
			double random = Math.random();
			if (random <= 0.5) {
				randomTeamTask = new TeamTask(role);
			}
		} else {
			// 随机地图
			final int mapId = RideMgr.getInstance().randomInitMap();
			this.role.sendCommand("//gomap " + mapId);
		}
		switch (protocolsType) {
		case 2:
			int iteamId = TestUtils.giftList.get(0);
			this.role.sendCommand("//additem " + iteamId + " 10");
			iteamId = TestUtils.giftList.get(1);
			this.role.sendCommand("//additem " + iteamId + " 10");
			break;
		case 4:
			moveBattle = new MoveBattle(role, true, false);
			break;
		case 6:
			numMod = 1;
			break;
		default:
			break;
		}
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);
		// 使用
		this.startTest();
	}

	private void startTest() {
		if (num > Long.MAX_VALUE)
			num = 0;
		if (isNum) {
			if (num != 0) {
				if (num % numMod != 0) {
					num++;
					return;
				}
			}
			num++;
		} else {
			final Long now = System.currentTimeMillis();
			// 间隔一定的时间处理
			if ((now - this.lastTime) < 3000) {
				return;
			}
			this.lastTime = System.currentTimeMillis();
		}
		if (moveBattle != null)
			moveBattle.run();
		if (randomTeamTask != null)
			randomTeamTask.run();
		Protocol p = null;
		switch (Main.getProtocolsType()) {
		case 1:
			// 公会验证取名
			// if (!UniqName.allocate("clan", clanname)) {
			// 玩家创建角色验证
			String name = ClanMgr.getInstance().getRandomName(8);
			if (!UniqNameUtils.allocate("role", name)) {
				mkdb.Trace.error("**********UniqName已经存在role.roleId" + role.roleId);
				return;
			}
			mkdb.Trace.error("**********测试唯一名存储" + role.roleId);
			break;
		case 2:
			// 赠送礼物
			int value = ClanMgr.getInstance().getRandom(2);
			int iteamId = TestUtils.giftList.get(value);
			Long giveGiftRoleId = TestUtils.getGiveGiftRoleId(role.roleId);
			p = new CGiveGift(giveGiftRoleId, iteamId, (byte) 1, "111", (byte) 1);
			this.role.sendCommand("//additem " + iteamId + " 1");
			break;
		case 3:
			// 好友关系
			p = friend(p);
			break;
		case 4:
			// PK切磋
			p = pKFightView(p);
			break;
		case 5:
			// 踩空间
			p = stepSpace(p);
			break;
		case 6:
			// 留言
			p = spaceCreate(p);
			break;
		default:
			mkdb.Trace.error("**********没有找到对应的协议码：" + role.roleId);
			break;
		}
		if (p != null)
			this.role.sendProtocol(p);
	}

	/**
	 * 留言
	 * 
	 * @author yangtao
	 * @dateTime 2016年7月6日 下午10:19:08
	 * @version 1.0
	 * @param p
	 * @return
	 */
	private Protocol spaceCreate(Protocol p) {
		Long spaceroleid = TestUtils.getFrendId(role.roleId, spaceRoleIdst);
		if (spaceroleid != null) {
//			String content = role.roleId + "发发的发" + spaceroleid + System.currentTimeMillis();
//			// http://192.168.41.5:8803/bbs/create?serverkey=s1&roleid=1&to_roleid=2&content=发发的发&reply_id=0&gift_type=0&is_popularity=0
//			final String buildurl = String
//					.format(
//							"http://%1$s%2$s?serverkey=%3$s&roleid=%4$s&to_roleid=%5$s&content=%6$s&reply_id=%7$s&gift_type=%8$s&is_popularity=%9$s",
//							address, func, ConfigMgr.getInstance().serverid, role.roleId, spaceroleid, content, 0, 0, 0);
			
			
			final HttpGet request = new HttpGet("http://192.168.32.2:8803/bbs/bbs_timeline?serverkey=44&roleid=8204289");
			Main.getHttpClient().execute(request, new SpaceCreateHttpHandler());
			//mkdb.Trace.error("---"+System.currentTimeMillis());
		}
		return null;
	}

	/**
	 * 空间踩一下与留言
	 * 
	 * @author yangtao
	 * @dateTime 2016年7月6日 下午10:11:59
	 * @version 1.0
	 * @param p
	 * @return
	 */
	private Protocol stepSpace(Protocol p) {
		Long spaceroleid = TestUtils.getFrendId(role.roleId, spaceRoleIdst);
		if (spaceroleid != null) {
			spaceRoleIdst.add(spaceroleid);
			p = new CStepSpace(spaceroleid);
		}
		return p;
	}

	/**
	 * PK切磋
	 * 
	 * @author yangtao
	 * @dateTime 2016年7月6日 上午10:31:55
	 * @version 1.0
	 * @param p
	 * @return
	 */
	private Protocol pKFightView(Protocol p) {
		if (num % 10 == 0) {
			// 切换地图
			changeMapByPK();
		}
		int modeltype = 1 + ClanMgr.getInstance().getRandom(3);
		p = new CPlayPKFightView(modeltype, -1, 0);
		return p;
	}

	/**
	 * 切换地图
	 * 
	 * @author yangtao
	 * @dateTime 2016年7月6日 下午2:41:40
	 * @version 1.0
	 */
	private void changeMapByPK() {
		double random = Math.random();
		if (random < 0.4)
			this.role.sendCommand("//goto 44 210 1615");
		else if (random < 0.8)
			this.role.sendCommand("//gomap 1615");
		else {
			final int mapId = RideMgr.getInstance().randomInitMap();
			this.role.sendCommand("//gomap " + mapId);
		}
	}

	/**
	 * 好友关系
	 * 
	 * @author yangtao
	 * @dateTime 2016年7月6日 上午10:29:11
	 * @version 1.0
	 * @param p
	 * @return
	 */
	private Protocol friend(Protocol p) {
		Long giveGiftRoleId;
		// 添加好友与删除好友
		double random = Math.random();
		if (random > 1.0) {
			if (friendList.size() > 0) {
				p = new CBreakOffRelation(friendList.remove(0));
				this.role.sendProtocol(p);
				return null;
			}
		}
		if (currentNum >= FRIEND_NUM_LIMIT) {
			if (friendList.size() > 0)
				p = new CBreakOffRelation(friendList.remove(0));
		} else {
			currentNum++;
			giveGiftRoleId = TestUtils.getFrendId(role.roleId, friendList);
			if (giveGiftRoleId == null)
				return null;
			p = new CRequestAddFriend(giveGiftRoleId);
			friendList.add(giveGiftRoleId);
		}
		return p;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
	}

	@Override
	public void stop() {
		role.loginui.reconnect();
		System.out.println("roleId=" + role.roleId + " 准备重新连接服务器");
		// TODO Auto-generated method stub
	}

	@Override
	public void processProtocol(final Protocol p) {
		if (randomTeamTask != null)
			randomTeamTask.processProtocol(p);
		if (moveBattle != null) {
			moveBattle.processProtocol(p);
		}
		// TODO Auto-generated method stub
	}

	private static class SpaceCreateHttpHandler extends HttpCallBackHandler {

		@Override
		protected boolean process(JSONObject json) {
			//mkdb.Trace.error("收到：---"+System.currentTimeMillis());
			final String errno = json.getString("errno");
			if (errno.isEmpty() || errno.equals("")) {
			}
			return true;
		}
	}
}
