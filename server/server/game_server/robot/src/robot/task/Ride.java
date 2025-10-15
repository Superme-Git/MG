package robot.task;

import java.util.LinkedList;
import java.util.List;

import mkio.Protocol;
import robot.LoginRole;
import robot.clan.ClanMgr;
import robot.ride.RideMgr;
import fire.pb.fushi.spotcheck.CBuySpotCard;
import fire.pb.fushi.spotcheck.CCancelTrading;
import fire.pb.fushi.spotcheck.CRoleTradingRecordView;
import fire.pb.fushi.spotcheck.CRoleTradingView;
import fire.pb.fushi.spotcheck.CSellSpotCard;
import fire.pb.fushi.spotcheck.CTradingSpotCardView;
import fire.pb.fushi.spotcheck.RoleTradingRecord;
import fire.pb.fushi.spotcheck.SRoleTradingView;
import fire.pb.item.CAppendItem;
import fire.pb.item.IDType;

/**
 * 坐骑操作
 * 
 * @author yt
 */
public class Ride extends Task_RoleBase {

	public long lastTime = 0l;

	boolean isProcessProtocols = false;

	boolean isRuneRequest = false;

	private final MoveBattle moveBattle;

	boolean isRide = false;

	private List<mkio.Protocol> protocols = new LinkedList<mkio.Protocol>();

	java.util.ArrayList<fire.pb.fushi.spotcheck.RoleTradingRecord> spotcardinfolist = new java.util.ArrayList<fire.pb.fushi.spotcheck.RoleTradingRecord>();

	int itemId = 0;

	int num = 0;

	public Ride(LoginRole role) {
		super(role);
		moveBattle = new MoveBattle(role, false);
		init();
	}

	public Ride(LoginRole role, boolean isChangeMap, boolean isSay) {
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
		}
		// 随机坐骑
		randomRide();
		// 随机地图
		int mapId = RideMgr.getInstance().randomInitMap();
		role.sendCommand("//gomap " + mapId);
	}

	/**
	 * 随机坐骑
	 * 
	 * @author yangtao
	 * @dateTime 2016年5月23日 上午12:21:24
	 * @version 1.0
	 */
	private void randomRide() {
		// 开始坐骑
		int size = RideMgr.rideItemIdList.size();
		int random = ClanMgr.getInstance().getRandom(size);
		itemId = RideMgr.rideItemIdList.get(random);
		role.sendCommand("//additem " + itemId);
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);
		// 能移动的时候在调用
		moveBattle.run();
		// 使用
		startRide();
	}

	/**
	 * 开始坐骑
	 * 
	 * @author yangtao
	 * @dateTime 2016年5月22日 下午11:01:46
	 * @version 1.0
	 */
	private void startRide() {
		Long now = System.currentTimeMillis();
		// 间隔一定的时间处理
		if (now - lastTime < 3000) {
			// mkdb.Trace.error("**********lastTime" + lastTime + "role.roleId"
			// + role.roleId);
			return;
		}
		lastTime = System.currentTimeMillis();
		if (isRide) {
			return;
		}
		// 如果超过一定的次数就重新买坐骑
		if (num >= 60) {
			randomRide();
			num = 0;
		}
		// 使用道具
		CAppendItem cUseItem = new CAppendItem();
		cUseItem.keyinpack = role.getTaskItemKey(itemId);
		cUseItem.idtype = IDType.ROLE;
		cUseItem.id = role.roleId;
		if (cUseItem.keyinpack == -1) {
			num++;
			return;
		}
		num = 0;
		// mkdb.Trace.info("cUseItem.itemkey=" + cUseItem.itemkey);
		role.sendProtocol(cUseItem);
		isRide = true;
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
