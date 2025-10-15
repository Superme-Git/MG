package robot.task;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mkio.Protocol;
import robot.LoginRole;
import robot.chat.ChatIteamMgr;
import robot.manager.MissionMgr;
import robot.ride.RideMgr;
import fire.pb.Item;
import fire.pb.Pet;
import fire.pb.shop.CMarketItemChatShow;
import fire.pb.shop.MarketThreeTable;
import fire.pb.talk.CChatItemTips;
import fire.pb.talk.CTransChatMessage2Serv;
import fire.pb.talk.STransChatMessage2Client;

/**
 * 聊天道具操作
 * 
 * @author yt
 */
public class ChatItemTask extends Task_RoleBase {

	public long lastTime = 0l;

	boolean isProcessProtocols = false;

	// private final MoveBattle moveBattle;
	boolean isCanMove = false;

	private List<mkio.Protocol> protocols = new LinkedList<mkio.Protocol>();

	CMarketItemChatShow cMarketItemChatShow = new CMarketItemChatShow();

	int itemtype = 1;

	long id = 0l;

	int modeltype = 1;

	List<MarketThreeTable> items = null;

	int size = 0;

	int i = 0;

	public ChatItemTask(LoginRole role) {
		super(role);
		// moveBattle = new MoveBattle(role, false);
		init();
	}

	public ChatItemTask(LoginRole role, boolean isChangeMap) {
		super(role);
		// moveBattle = new MoveBattle(role, isChangeMap);
		init();
	}

	public ChatItemTask(LoginRole role, boolean isChangeMap, Boolean isSay) {
		super(role);
		// moveBattle = new MoveBattle(role, isChangeMap, isSay);
		init();
	}

	/**
	 * 初始化数据满足
	 */
	private void init() {
		// 调整等级
		if (role.getLevel() < 50) {
			role.sendCommand("//addlevel " + (50 - role.getLevel()) + " equip");
			role.sendCommand("//addmoney " + 1000000000);
		}
		// // 增加活跃度
		// role.sendCommand("//addhyd 1000");
		// // 获取50个拍卖的道具
		// items = MissionMgr.getInstance().getSomeMarketItemIds(50);
		// size = items.size();
		// 随机地图
		final int mapId = RideMgr.getInstance().randomInitMap();
		this.role.sendCommand("//gomap " + mapId);
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);
//		if (items == null)
//			return;
		// 能移动的时候在调用
		// moveBattle.run();
		// 协议处理
		if (!protocols.isEmpty()) {
			// 解析协议
			jieXiProtocols();
		}
		if (isProcessProtocols) {
			isProcessProtocols = false;
			return;
		}
		// final Long now = System.currentTimeMillis();
		// // 间隔一定的时间处理,因为世界聊天间隔是60秒
		// if ((now - this.lastTime) < 60000) {
		// return;
		// }
		// this.lastTime = System.currentTimeMillis();
		// MarketThreeTable marketThreeTable = items.get(i);
		// if (marketThreeTable == null) {
		// mkdb.Trace.info("marketThreeTable i 没有数据" + role.roleId + "i:" + i);
		// while (marketThreeTable == null) {
		// i++;
		// marketThreeTable = items.get(i);
		// }
		// }
		// int sellItemId = marketThreeTable.id;
		// int type = marketThreeTable.itemtype;
		// String message = "< arg=" + '\"' + "t=1,id=" + sellItemId + ",type=" + type + '\"' + " />";
		// java.util.ArrayList<fire.pb.talk.DisplayInfo> info = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
		// // 发送道具到世界频道
		// CTransChatMessage2Serv cTransChatMessage2Serv = new CTransChatMessage2Serv(5, message, "", info, 0, 0);
		// role.sendProtocol(cTransChatMessage2Serv);
		// // 增加活跃度
		// role.sendCommand("//addhyd 20");
		// i++;
		// if (i >= size) {
		// i = 0;
		// }
		// // 间隔一定的时间处理
		// if (System.currentTimeMillis() - lastTime < 3000) {
		// // mkdb.Trace.error("**********lastTime" + lastTime + "role.roleId"
		// // + role.roleId);
		// return;
		// }
		// lastTime = System.currentTimeMillis();
		// 设置是否发送
		// RedPacketMgr.getInstance().setIsSendPacket();
		// 角色处理
		roleProcess();
	}

	/**
	 * 角色处理
	 */
	private void roleProcess() {
		if (id > 0) {
			cMarketItemChatShow.itemtype = itemtype;
			cMarketItemChatShow.id = id;
			mkdb.Trace.info("请求世界道具显示：" + role.roleId + "---协议号：" + cMarketItemChatShow.getType());
			for (int i = 0; i < 1; i++)
				role.sendProtocol(cMarketItemChatShow);
		}
	}

	/**
	 * 解析协议
	 */
	private void jieXiProtocols() {
		Protocol p = protocols.remove(0);// 每次只处理一个请求
		if (p instanceof STransChatMessage2Client) {
			STransChatMessage2Client sTransChatMessage2Client = (STransChatMessage2Client) p;
			// 解析信息串
			Map<String, String> dataMap = ChatIteamMgr.getInstance().processMessage(sTransChatMessage2Client.message);
			if (dataMap.size() <= 0) {
				return;
			}
			int type = 0;
			if (dataMap.containsKey("type")) {
				type = Integer.parseInt(dataMap.get("type"));
			}
			// 非道具直接过滤掉
			if (type == 0)
				return;
			mkdb.Trace.error("**********进入协议解析:role.roleId" + role.roleId + "---type:" + type);
			if (!dataMap.containsKey("id")) {
				// 针对背包里面的道具和宠物
				CChatItemTips cChatItemTips = new CChatItemTips();
				fire.pb.talk.DisplayInfo displayinfo = new fire.pb.talk.DisplayInfo();
				displayinfo.counterid = Integer.parseInt(dataMap.get("counter"));
				displayinfo.displaytype = type;
				displayinfo.shopid = Long.parseLong(dataMap.get("shopid"));
				displayinfo.uniqid = Integer.parseInt(dataMap.get("key"));
				displayinfo.roleid = sTransChatMessage2Client.roleid;
				displayinfo.teamid = 0l;
				cChatItemTips.displayinfo = displayinfo;
				role.sendProtocol(cChatItemTips);
			} else {
				// 针对拍卖里面的道具和宠物
				CMarketItemChatShow cMarketItemChatShow = new CMarketItemChatShow();
				itemtype = cMarketItemChatShow.itemtype = type;
				id = cMarketItemChatShow.id = Long.parseLong(dataMap.get("id"));
				for (int i = 0; i < 1; i++)
					role.sendProtocol(cMarketItemChatShow);
			}
		}
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	/*
	 * 处理接受到的消息 (non-Javadoc)
	 * @see robot.task.Task_RoleBase#processProtocol(mkio.Protocol)
	 */
	@Override
	public void processProtocol(Protocol p) {
		if (p instanceof STransChatMessage2Client) {
			synchronized (protocols) {
				protocols.add(p);
			}
		}
		// moveBattle.processProtocol(p);
	}
}
