package robot.task;

import java.util.LinkedList;
import java.util.List;

import mkio.Protocol;
import robot.LoginRole;
import robot.manager.NPCMgr;
import robot.ride.PetUtils;
import robot.zhuogui.NpcData;
import robot.zhuogui.ZhuoGuiUtils;
import fire.pb.battle.SSendBattleStart;
import fire.pb.circletask.SRefreshSpecialQuest;
import fire.pb.hook.CSetAutoBattle;
import fire.pb.move.NpcBasic;
import fire.pb.move.SAddUserScreen;
import fire.pb.npc.CNpcService;
import fire.pb.npc.CVisitNpc;
import fire.pb.npc.SNpcShare;
import fire.pb.npc.SVisitNpc;
import fire.pb.team.CQuitTeam;

/**
 * 捉鬼的处理
 * 
 * @author yt
 */
public class ZhuoGuiTask extends Task_RoleBase {

	// 考古任务的npc和坐标
	private int mapid = 1615;

	private int x = 111;

	private int y = 124;

	boolean isFirst = true;

	long needToReLoginTime = System.currentTimeMillis();;

	private List<mkio.Protocol> protocols = new LinkedList<mkio.Protocol>();

	boolean isProcessProtocols = false;

	boolean isReturnData = false;

	long runTime = System.currentTimeMillis();;

	private final TeamTask randomTeamTask;

	private java.util.LinkedList<fire.pb.move.NpcBasic> npcs = null;

	int npcId = -1;

	public ZhuoGuiTask(LoginRole role) {
		super(role);
		randomTeamTask = new TeamTask(role);
		init();
	}

	/**
	 * 初始化数据满足进入任务的需求
	 */
	private void init() {
		// 调整等级
		if (role.getLevel() < 99) {
			role.sendCommand("//addlevel " + (99 - role.getLevel()) + " equip");
			role.sendCommand("//strong 20000");
		}
		// 随机一个宠物
		int petid = PetUtils.getRandomPetId();
		role.sendCommand("//addpet " + petid + " 99");
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);
		randomTeamTask.run();
		if (role.getTeam() == null)
			return;
		if (!role.getTeam().isLeader(role.roleId)) {
			// System.out.println(role.roleId);
			return;
		}
		// 协议处理
		if (!protocols.isEmpty()) {
			Protocol p = protocols.remove(0);// 每次只处理一个请求
			if ((p instanceof SRefreshSpecialQuest)) {
				isReturnData = true;
				SRefreshSpecialQuest slc = (SRefreshSpecialQuest) p;
				int questid = slc.questid;
				if (questid != 1030000) {
					isReturnData = false;
					return;
				}
				int dstnpcid = slc.dstnpcid;
				npcId = dstnpcid;
				// mkdb.Trace.error("**********捉鬼开始dstnpcid:" + dstnpcid
				// + "---role.roleId" + role.roleId);
				// 这两个npc是动态生成的,只能让他退出队伍重新组队再做此循环任务
				if (dstnpcid == 180011 || dstnpcid == 180001) {
					CQuitTeam cqt = new CQuitTeam();
					role.sendProtocol(cqt);
					return;
				}
				NpcData npcData = ZhuoGuiUtils.getInstance().getData(dstnpcid);
				if (npcData == null) {
					dstnpcid = 180002;
					npcData = ZhuoGuiUtils.getInstance().getData(dstnpcid);
				}
				// 切换到接受藏宝图（考古）的npc地方
				SNpcShare npc = NPCMgr.getInstance().getNpcInfoByID(dstnpcid);
				// mkdb.Trace.error("**********捉鬼开始npc.xPos:" + npc.xPos
				// + "--npc.yPos" + npc.yPos + "----npc.mapid" + npc.mapid
				// + "---role.roleId" + role.roleId);
				role.sendCommand("//goto " + npc.xPos + " " + npc.yPos + " " + npc.mapid);
				// mkdb.Trace.error("**********捉鬼开始npckey:" + npcData.npckey
				// + "--npcData.serviceid" + npcData.serviceid
				// + "---role.roleId" + role.roleId + "------dstnpcid"
				// + dstnpcid);
				// 接受下一个任务
				CNpcService cr = new CNpcService(npcData.npckey, npcData.serviceid);
				role.sendProtocol(cr);
				// 设置自动战斗
				CSetAutoBattle cb = new CSetAutoBattle((byte) 1);
				role.sendProtocol(cb);
				isProcessProtocols = true;
			} else if (p instanceof SAddUserScreen) {
				// 以后维护获取动态npckey和serviceid用
				// 添加场景中的npc
				SAddUserScreen sAddUserScreen = (SAddUserScreen) p;
				if (npcs == null) {
					npcs = sAddUserScreen.npclist;
				} else {
					npcs.addAll(sAddUserScreen.npclist);
				}
				// 以后维护获取动态npckey和serviceid用
				// NpcBasic npcBasic=getNpcBasic(dstnpcid);
				// if(npcBasic!=null){
				// CVisitNpc cVisitNpc = new CVisitNpc(npcBasic.npckey);
				// role.sendProtocol(cVisitNpc);
				// }
				isProcessProtocols = true;
			} else if (p instanceof SVisitNpc) {
				// 以后维护获取动态npckey和serviceid用
				SVisitNpc sVisitNpc = (SVisitNpc) p;
				Long npckey = sVisitNpc.npckey;
				java.util.ArrayList<Integer> servicesList = sVisitNpc.services;
				if (servicesList != null && servicesList.size() > 0) {
					// 先不用
					CNpcService cr = new CNpcService(npckey, servicesList.get(0));
					role.sendProtocol(cr);
					// 设置自动战斗
					CSetAutoBattle cb = new CSetAutoBattle((byte) 1);
					role.sendProtocol(cb);
					isProcessProtocols = true;
				}
			}
			else if (p instanceof SSendBattleStart) {
				role.sendCommand("//full");
			}
		}
		if (isProcessProtocols) {
			isProcessProtocols = false;
			return;
		}
		// 如果一定时间没有消息接受就断线
		long offLineTime = System.currentTimeMillis() - needToReLoginTime;
		if ((offLineTime > (5 * 60 * 1000))) {
			needToReLoginTime = System.currentTimeMillis();
			// mkdb.Trace.error("**********捉鬼任务完成当前轮数，该下一轮了:" + "---role.roleId"
			// + role.roleId);
			isReturnData = false;
			return;
		}
		// 是不是首次
		if (isFirst) {
			isFirst = false;
			// mkdb.Trace.error("**********捉鬼这是首次吗:" + isFirst +
			// "---role.roleId"
			// + role.roleId);
			// 切换到接受（捉鬼）的npc地方
			role.gmgoto(mapid, x, y);
			// 接受任务
			CNpcService cr = new CNpcService(115, 4000);
			role.sendProtocol(cr);
			return;
		}
		if (!isReturnData && System.currentTimeMillis() - runTime > (5 * 1000)) {
			runTime = System.currentTimeMillis();
			// 切换到接受（捉鬼）的npc地方
			role.gmgoto(mapid, x, y);
			// 接受任务
			CNpcService cr = new CNpcService(115, 4000);
			role.sendProtocol(cr);
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
		randomTeamTask.processProtocol(p);
		needToReLoginTime = System.currentTimeMillis();
		// if (p instanceof SRefreshSpecialQuest||p instanceof SAddUserScreen||p instanceof SVisitNpc) {
		// 发送刷新下一个npc怪
		if (p instanceof SRefreshSpecialQuest||p instanceof SSendBattleStart) {
			synchronized (protocols) {
				protocols.add(p);
			}
		}
	}

	/**
	 * 得到npc的信息数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年5月20日 下午4:14:23
	 * @version 1.0
	 * @param npcID
	 * @return
	 */
	public NpcBasic getNpcBasic(long npcID) {
		if (npcs != null) {
			for (fire.pb.move.NpcBasic npc : npcs) {
				if (npc.id == npcID) {
					return npc;
				}
			}
		}
		return null;
	}
}
