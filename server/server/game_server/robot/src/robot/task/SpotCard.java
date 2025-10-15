package robot.task;

import java.util.LinkedList;
import java.util.List;

import mkio.Protocol;
import robot.LoginRole;
import robot.clan.ClanMgr;
import fire.pb.fushi.spotcheck.CBuySpotCard;
import fire.pb.fushi.spotcheck.CCancelTrading;
import fire.pb.fushi.spotcheck.CRoleTradingRecordView;
import fire.pb.fushi.spotcheck.CRoleTradingView;
import fire.pb.fushi.spotcheck.CSellSpotCard;
import fire.pb.fushi.spotcheck.CTradingSpotCardView;
import fire.pb.fushi.spotcheck.RoleTradingRecord;
import fire.pb.fushi.spotcheck.SRoleTradingView;

/**
 * 符石操作
 * 
 * @author yt
 */
public class SpotCard extends Task_RoleBase {

	public long lastTime = 0l;

	boolean isProcessProtocols = false;

	boolean isRuneRequest = false;

	private final MoveBattle moveBattle;

	boolean isCanMove = false;

	private List<mkio.Protocol> protocols = new LinkedList<mkio.Protocol>();

	java.util.ArrayList<fire.pb.fushi.spotcheck.RoleTradingRecord> spotcardinfolist = new java.util.ArrayList<fire.pb.fushi.spotcheck.RoleTradingRecord>();

	public SpotCard(LoginRole role) {
		super(role);
		moveBattle = new MoveBattle(role, false);
		init();
	}

	public SpotCard(LoginRole role, boolean isChangeMap) {
		super(role);
		moveBattle = new MoveBattle(role, isChangeMap);
		init();
	}

	public SpotCard(LoginRole role, boolean isChangeMap, Boolean isSay) {
		super(role);
		moveBattle = new MoveBattle(role, isChangeMap, isSay);
		init();
	}

	/**
	 * 初始化数据满足
	 */
	private void init() {
		// 调整等级
		if (role.getLevel() < 50) {
			role.sendCommand("//addlevel " + (50 - role.getLevel()) + " equip");
			role.sendCommand("//addgold " + 1000000000);
		}
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);
		// 能移动的时候在调用
		moveBattle.run();
		// 协议处理
		if (!protocols.isEmpty()) {
			// 解析协议
			jieXiProtocols();
		}
		if (isProcessProtocols) {
			// mkdb.Trace.error("**********isProcessProtocols"
			// + isProcessProtocols + "role.roleId" + role.roleId);
			isProcessProtocols = false;
			return;
		}
		Long now = System.currentTimeMillis();
		// 间隔一定的时间处理
		if (now - lastTime < 5000) {
			// mkdb.Trace.error("**********lastTime" + lastTime + "role.roleId"
			// + role.roleId);
			return;
		}
		lastTime = System.currentTimeMillis();
		// 角色处理
		roleProcess();
	}

	/**
	 * 角色处理
	 */
	private void roleProcess() {
		int random = 0;
		int size = spotcardinfolist.size();
		if (size == 0) {
			random = 0;
		}
		random = ClanMgr.getInstance().getRandom(14);
		// mkdb.Trace.error("*********角色处理*random" + random + "role.roleId" + role.roleId);
		if (random >= 0 && random <= 4) {
			int gold = 100 + ClanMgr.getInstance().getRandom(101);
			int fuShiNum = 1 + ClanMgr.getInstance().getRandom(10);
			// 求购符石
			CBuySpotCard cBuySpotCard = new CBuySpotCard(fuShiNum, gold);
			role.sendProtocol(cBuySpotCard);
		} else if (random > 4 && random <= 9) {
			int gold = 100 + ClanMgr.getInstance().getRandom(101);
			int fuShiNum = 1 + ClanMgr.getInstance().getRandom(10);
			// 寄售符石
			CSellSpotCard cSellSpotCard = new CSellSpotCard(fuShiNum, gold);
			role.sendProtocol(cSellSpotCard);
			role.sendCommand("//addfushi 1 50");
		} else if (random > 9 && random <= 10) {
			// 交易区界面
			CTradingSpotCardView cTradingSpotCardView = new CTradingSpotCardView();
			role.sendProtocol(cTradingSpotCardView);
		} else if (random > 10 && random <= 11) {
			// 个人买卖界面
			CRoleTradingView cRoleTradingView = new CRoleTradingView();
			role.sendProtocol(cRoleTradingView);
		} else if (random > 11 && random <= 12) {
			// 个人买卖记录界面
			CRoleTradingRecordView cRoleTradingRecordView = new CRoleTradingRecordView();
			role.sendProtocol(cRoleTradingRecordView);
		} else {
			// 取消订单
			if (size > 0) {
				random = ClanMgr.getInstance().getRandom(size);
				RoleTradingRecord roleTradingRecord = spotcardinfolist.remove(random);
				String tradingid = roleTradingRecord.tradingid;
				CCancelTrading cCancelTrading = new CCancelTrading(tradingid);
				role.sendProtocol(cCancelTrading);
			}
		}
	}

	/**
	 * 解析协议
	 */
	private void jieXiProtocols() {
		// mkdb.Trace.error("**********进入协议解析:role.roleId" + role.roleId);
		Protocol p = protocols.remove(0);// 每次只处理一个请求
		if (p instanceof SRoleTradingView) {
			SRoleTradingView sRoleTradingView = (SRoleTradingView) p;
			spotcardinfolist = new java.util.ArrayList<fire.pb.fushi.spotcheck.RoleTradingRecord>();
			java.util.ArrayList<fire.pb.fushi.spotcheck.RoleTradingRecord> buyspotcardinfolist = sRoleTradingView.buyspotcardinfolist;
			if (buyspotcardinfolist != null && buyspotcardinfolist.size() > 0) {
				spotcardinfolist.addAll(buyspotcardinfolist);
				isProcessProtocols = true;
			}
			java.util.ArrayList<fire.pb.fushi.spotcheck.RoleTradingRecord> sellspotcardinfolist = sRoleTradingView.sellspotcardinfolist;
			if (sellspotcardinfolist != null && sellspotcardinfolist.size() > 0) {
				spotcardinfolist.addAll(sellspotcardinfolist);
				isProcessProtocols = true;
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
		if (p instanceof SRoleTradingView) {
			synchronized (protocols) {
				protocols.add(p);
			}
		}
		moveBattle.processProtocol(p);
	}
}
