package robot.task;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.h2.util.MathUtils;

import robot.Constant;
import robot.LoginRole;
import robot.RoleBag;
import robot.manager.MissionCfgMgr;
import robot.manager.MissionMgr;
import robot.manager.NPCMgr;
import robot.pos.LogicPos;
import mkio.Protocol;
import fire.pb.battle.SSendBattleEnd;
import fire.pb.battle.SSendBattleStart;
import fire.pb.circletask.CReqGotoPatrol;
import fire.pb.item.CAppendItem;
import fire.pb.item.CPutOnEquip;
import fire.pb.item.EquipItemShuXing;
import fire.pb.item.IDType;
import fire.pb.item.SItemToItem;
import fire.pb.mission.CActiveMissionAIBattle;
import fire.pb.mission.CCommitMission;
import fire.pb.mission.CReqGoto;
import fire.pb.mission.MissionInfo;
import fire.pb.mission.SAcceptMission;
import fire.pb.move.CCheckMove;
import fire.pb.move.CRoleMove;
import fire.pb.move.CRoleStop;
import fire.pb.move.SAddUserScreen;
import fire.pb.move.SRemoveUserScreen;
import fire.pb.move.SRoleEnterScene;
import fire.pb.npc.CVisitNpc;
import fire.pb.npc.SNpcShare;
import fire.pb.npc.SVisitNpc;
import fire.pb.skill.CUpdateInborn;

/**
 * 主线任务
 * 
 * @author 黄兆令
 */
public class ZhuXian extends Task_RoleBase {
	public ZhuXian(LoginRole r) {
		super(r);
		init();
	}

	private void init() {
		// 调整等级
		if (role.getLevel() <= 80) {
			role.sendCommand("//addlevel " + (80 - role.getLevel()) + " equip");
			role.sendCommand("//addjclienthb");
		}
	}

	public static final int GRID_SIZE = 16;// 地图格子大小

	public static final int NPC_TALK = 10; // 点击npc，与npc对话
	public static final int GIVE_MONEY = 11; // 给予金钱
	public static final int GIVE_ITEM = 12; // 给予道具
	public static final int GIVE_PET = 13; // 给予宠物
	public static final int ANSWER_QUESTION = 17; // 答题
	public static final int START_BATTLE = 40; // 开始战斗

	private LogicPos targetpos = null;
	private int npcID;// npc策划数据的编号
	private long npckey;// npc在服务器中的唯一索引
	private int taskid;// 任务id
	private int tasktype;// 任务类型
	private java.util.LinkedList<fire.pb.move.NpcBasic> npcs = null;

	private LogicPos oldpos = null;

	// 比较麻烦的任务直接跳过
	private BlockingQueue<Protocol> protocols = new LinkedBlockingQueue<Protocol>();
	Map<String, String> params;

	// -1 空闲状态 0行走状态 1轻功状态
	private byte state = 0;

	private static final int TASK_CLICK_NPC = 10; // 点击NPC
	private static final int TASK_GIVE_MONEY = 11; // 给予金钱
	private static final int TASK_GIVE_ITEM = 12; // 给予物品
	private static final int TASK_GIVE_PET = 13; // 给予宠物
	private static final int TASK_ANSWER_QUEST = 17; // 回答问题
	private static final int TASK_USE_ITEM = 22; // 使用物品
	private static final int TASK_LOOT_ITEM = 32; // 练功区掉落物品
	private static final int TASK_BATTLES_COUNT = 34; // 练功区战斗计场次
	private static final int TASK_BATTLE_TIMES = 35; // 练功区战斗计次数
	private static final int TASK_BATTLE_NPC = 40; // NPC战斗
	private static final int TASK_NO_CONDITION = 50; // 无条件
	private static final int TASK_TRIGGER_BY_WALK = 54; // 步数触发
	private static final int TASK_TRIGGER_BY_AREA = 56; // 区域触发
	private static final int TASK_LV_LIMIT = 58; // 等级限制任务
	private static final int TASK_SPECIAL = 59; // 特殊任务
	private static final int TASK_PATROL = 60; // 巡逻护送任务
	private static final int TASK_ADD_SKILL = 80; // 学习技能
	private static final int TASK_USE_EQUIP = 90; // 穿戴装备

	public long needToReLoginTime = 0;
	public int samePointCount = 0;

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);

		// 先处理protocols
		dealProtocols();
		if (state == 0) {
			roleMove();
		} else if (state == 1) {
			checkMove();
		}
		long offLineTime = System.currentTimeMillis() - needToReLoginTime;
		if ((offLineTime > (2 * 60 * 1000)) || (samePointCount > 10)) {
			needToReLoginTime = System.currentTimeMillis();
			role.loginui.onDisconnected();
		}
	}

	private void dealProtocols() {
		synchronized (protocols) {
			for (mkio.Protocol p : protocols) {
				dealProtocol(p);
			}
			protocols.clear();
		}
	}

	private void end() {
		targetpos = null;
		state = -1;
	}

	@Override
	public final void stop() {
		role.loginui.reconnect();
		System.out.println("roleId=" + role.roleId + " 准备重新连接服务器");
	}

	@Override
	public void start() {

	}

	@Override
	public void processProtocol(Protocol p) {
		// if(p.str().indexOf("SRoleMove") == -1)
		// System.out.println("协议名称:" + p.str() + " UserName=" +
		// role.getLoginui().getUserName() + " roleid=" + role.roleId);
		synchronized (protocols) {
			protocols.offer(p);
		}
	}

	/**
	 * 
	 * 处理协议
	 * 
	 * @param p
	 */
	protected void dealProtocol(mkio.Protocol p) {
		needToReLoginTime = System.currentTimeMillis();
		if (p instanceof SAcceptMission) {
			MissionInfo missioninfo = ((SAcceptMission) p).missioninfo;
			if (missioninfo == null) {
				System.out.println("协议数据错误");
				return;
			}
			MissionCfgMgr conf = MissionMgr.getInstance().getTaskConfig(
					missioninfo.missionid);
			if (conf == null) {
				System.out.println("task conf is null.taskid:"
						+ missioninfo.missionid);
				return;
			}

			if (conf.missionTypeString.equals("引导任务")) {
				return;
			}

			// 接受任务的时候end一下
			end();

			mkdb.Trace.info("当前任务id" + missioninfo.missionid + "\t任务名称"
					+ conf.missionName + "\t任务类型"
					+ conf.missionActiveInfo.missionType + " UserName="
					+ role.getLoginui().getUserName() + " roleid="
					+ role.roleId);

			taskid = missioninfo.missionid;

			// 区分任务类型 主线任务180101-199999
			tasktype = conf.missionActiveInfo.missionType;
			switch (tasktype) {
			case TASK_USE_ITEM: // 使用物品
				OnClickCellGoto_Scenario_useItem(missioninfo.missionid);
				break;
			case TASK_PATROL: // 巡逻护送任务
				OnClickCellGoto_Scenario_patrol(missioninfo.missionid);
				break;
			case TASK_NO_CONDITION: // 无条件
			case TASK_ADD_SKILL: // 学习技能
				OnClickUpSkill(missioninfo.missionid);
				break;
			case TASK_USE_EQUIP: // 穿戴装备
				OnClickCellGoto_Scenario_useEquip(conf);
				break;
			case TASK_CLICK_NPC: // 点击NPC
			case TASK_GIVE_MONEY: // 给予金钱
			case TASK_GIVE_ITEM: // 给予物品
			case TASK_GIVE_PET: // 给予宠物
			case TASK_ANSWER_QUEST: // 回答问题
			case TASK_LOOT_ITEM: // 练功区掉落物品
			case TASK_BATTLES_COUNT: // 练功区战斗计场次
			case TASK_BATTLE_TIMES: // 练功区战斗计次数
			case TASK_TRIGGER_BY_WALK: // 步数触发
			case TASK_TRIGGER_BY_AREA: // 区域触发
			case TASK_BATTLE_NPC: // NPC战斗
			case TASK_LV_LIMIT: // 等级限制任务
			case TASK_SPECIAL: // 特殊任务
			default:
				OnClickCellGoto_ScenarioAll(missioninfo.missionid);
				break;
			}
		} else if (p instanceof SRoleEnterScene) {
			role.setScene(((SRoleEnterScene) p).sceneid);
			role.setPos(((SRoleEnterScene) p).destpos.x,
					((SRoleEnterScene) p).destpos.y);
			oldpos = new LogicPos(role.getPosX(), role.getPosY());
			// mkdb.Trace.info("state=" + state + "deal SRoleEnterScene:" +
			// ((SRoleEnterScene)p).destpos.x + "," +
			// ((SRoleEnterScene)p).destpos.y);
		} else if (p instanceof SAddUserScreen) {
			// 添加场景中的npc
			SAddUserScreen sAddUserScreen = (SAddUserScreen) p;
			if (npcs == null) {
				npcs = sAddUserScreen.npclist;
			} else {
				// for(int i = 0;i < sAddUserScreen.npclist.size();i++){
				// for(int k = 0;k < npcs.size();k++){
				// if(npcs.get(k).npckey ==
				// sAddUserScreen.npclist.get(i).npckey){
				// npcs.remove(k);
				// }
				// }
				// npcs.add(sAddUserScreen.npclist.get(i));
				// }
				npcs.addAll(sAddUserScreen.npclist);
			}
		} else if (p instanceof SRemoveUserScreen) {
			// //移除场景中的npc
			// SRemoveUserScreen sRemoveUserScreen=(SRemoveUserScreen)p;
			// for(long npcid:sRemoveUserScreen.npcids){
			// npcs.remove(npcid);
			// }

		} else if (p instanceof SVisitNpc) {
			dealSVisitNpc(taskid);
		} else if (p instanceof SSendBattleStart) {
			// 战斗开始，停止移动
			end();
		} else if (p instanceof SSendBattleEnd) {
			// 战斗结束
		}

	}

	/**
	 * 访问npc类型的任务处理
	 * 
	 * @param taskid
	 */
	public void OnClickCellGoto_ScenarioAll(int taskid) {
		MissionCfgMgr conf = MissionMgr.getInstance().getTaskConfig(taskid);
		if (conf == null) {
			System.out.println("task conf is null.taskid:" + taskid);
			return;
		}
		int mapID = conf.missionActiveInfo.mapID;
		npcID = conf.missionActiveInfo.npcID;

		int nTargetPosX = 0;
		int nTargetPosY = 0;

		// 判断目标地图是否是当前地图，如果不是，用GM命令跳到对应地图
		if (role.getMapId() != mapID) {
			// 在跳地图之前清空npcs
			npcs = null;
			role.sendCommand("//gomap " + mapID);
		}
		if (npcID > 0) {
			SNpcShare npc = NPCMgr.getInstance().getNpcInfoByID(npcID);
			if (npc != null) {
				nTargetPosX = npc.getXPos() * GRID_SIZE;
				nTargetPosY = npc.getYPos() * GRID_SIZE;
				targetpos = new LogicPos(nTargetPosX, nTargetPosY);
				// 设置为开始移动状态
				this.state = 0;
			} else {

			}
		} else {
			// 查找范围
			targetpos = getTargetRandXYWithTaskId(taskid);
			// 设置为开始移动状态
			this.state = 0;
		}
	}

	/**
	 * 处理巡逻任务类型
	 * 
	 * @param taskid
	 */
	public void OnClickCellGoto_Scenario_patrol(int taskid) {
		MissionCfgMgr conf = MissionMgr.getInstance().getTaskConfig(taskid);
		if (conf == null) {
			System.out.println("task conf is null.taskid:" + taskid);
			return;
		}
		int mapID = conf.missionActiveInfo.mapID;
		npcID = conf.missionActiveInfo.npcID;

		// 判断目标地图是否是当前地图，如果不是，用GM命令跳到对应地图
		if (role.getMapId() != mapID) {
			// 在跳地图之前清空npcs
			npcs = null;
			role.sendCommand("//gomap " + mapID);
		}

		CReqGoto cReqGoto = new CReqGoto();
		cReqGoto.mapid = mapID;
		targetpos = getTargetRandXYWithTaskId(taskid);
		// 设置为开始移动状态
		this.state = 0;
		// cReqGoto.xpos=targetpos.getX()/GRID_SIZE;
		// cReqGoto.ypos=targetpos.getY()/GRID_SIZE;
		// role.sendProtocol(cReqGoto);

		// 巡逻
		CReqGotoPatrol cReqGotoPatrol = new CReqGotoPatrol();
		role.sendProtocol(cReqGotoPatrol);
	}

	/**
	 * 处理技能升级的任务类型
	 * 
	 * @param taskid
	 */
	public void OnClickUpSkill(int taskid) {
		// 110001 战士
		// 120001 圣骑士
		// 130001 猎人
		// 140001 德鲁伊
		// 150001 法师
		// 160001 牧师

		// 1111 战士
		// 1211 圣骑士
		// 1311 猎人
		// 1411 德鲁伊
		// 1511 法师
		// 1611 牧师

		CUpdateInborn cUpdateAcupoint = new CUpdateInborn();
		cUpdateAcupoint.id = role.school * 100 + 11;
		cUpdateAcupoint.flag = 0;
		role.sendProtocol(cUpdateAcupoint);
	}

	/**
	 * 使用装备类型的任务
	 * 
	 * @param taskid
	 */
	public void OnClickCellGoto_Scenario_useEquip(MissionCfgMgr conf) {
		int itemId = conf.missionActiveInfo.useItemID;
		RoleBag bag = role.getRoleBag();
		if (!bag.hasItem(itemId)) {
			SItemToItem itemToItemConf = MissionMgr.itemToItemConfMap
					.get(itemId);
			if (itemToItemConf == null) {
				return;
			} else {
				for (int i = 0; i < itemToItemConf.getItemsid().size(); i++) {
					if (bag.hasItem(itemToItemConf.itemsid.get(i)))
						itemId = itemToItemConf.itemsid.get(i);
				}

				if (itemId == conf.missionActiveInfo.useItemID) {
					return;
				}
			}
		}

		EquipItemShuXing equipItemAttr = MissionMgr.equipConfMap.get(itemId);
		if (equipItemAttr == null) {
			return;
		}

		int useEquipKey = bag.getItemKey(itemId);
		int useEquipPos = equipItemAttr.typeid >> 4 & 0xf;
		if (useEquipKey == -1) {
			System.out.println("equip error itemid:" + itemId + " taskid:"
					+ conf.missionId);
			return;
		}

		int mapID = conf.missionActiveInfo.mapID;
		npcID = conf.missionActiveInfo.npcID;
		// 判断目标地图是否是当前地图，如果不是，用GM命令跳到对应地图
		if (role.getMapId() != mapID) {
			// 在跳地图之前清空npcs
			role.sendCommand("//gomap " + mapID);
		}

		// 获得使用物品的坐标
		targetpos = getTargetRandXYWithTaskId(taskid);
		if (targetpos != null) {
			mkdb.Trace.info("taskid:" + taskid + "\t坐标x：" + targetpos.getX()
					+ "\t坐标x：" + targetpos.getY());
		}

		CPutOnEquip cEquipement = new CPutOnEquip(useEquipKey, useEquipPos);

		if (useEquipKey == -1 || useEquipKey == -1) {
			mkdb.Trace.info("装备信息数据错误  " + useEquipKey + "\t");
		}
		mkdb.Trace.info("EquipKey=" + useEquipKey);
		role.sendProtocol(cEquipement);
		// 设置为开始移动状态
		this.state = 0;
	}

	/**
	 * 使用物品类型的任务
	 * 
	 * @param taskid
	 */
	public void OnClickCellGoto_Scenario_useItem(int taskid) {
		MissionCfgMgr conf = MissionMgr.getInstance().getTaskConfig(taskid);
		if (conf == null) {
			System.out.println("task conf is null.taskid:" + taskid);
			return;
		}
		int mapID = conf.missionActiveInfo.mapID;
		npcID = conf.missionActiveInfo.npcID;

		// 判断背包是否有这个道具，如果没有gm添加一个
		int itemcode = conf.missionActiveInfo.useItemID;
		if (role.getTaskItemKey(itemcode) == -1) {
			role.sendCommand("//additem " + itemcode);
		}

		// 判断目标地图是否是当前地图，如果不是，用GM命令跳到对应地图
		if (role.getMapId() != mapID) {
			// 在跳地图之前清空npcs
			// npcs = null;
			role.sendCommand("//gomap " + mapID);
		}

		// 获得使用物品的坐标
		targetpos = getTargetRandXYWithTaskId(taskid);
		if (targetpos != null) {
			mkdb.Trace.info("taskid:" + taskid + "\t坐标x：" + targetpos.getX()
					+ "\t坐标x：" + targetpos.getY());
		}
		// 设置为开始移动状态
		this.state = 0;
	}

	/**
	 * 处理访问npc后的请求逻辑
	 * 
	 * @param taskid
	 */
	public void dealSVisitNpc(int taskid) {
		end();
		MissionCfgMgr conf = MissionMgr.getInstance().getTaskConfig(taskid);
		if (conf == null) {
			mkdb.Trace.info("task conf is null.taskid:" + taskid);
			return;
		}

		// 判断是否是战斗，如果是战斗
		if (conf.missionActiveInfo.missionType == START_BATTLE) {
			CActiveMissionAIBattle cActiveScenarioQuestAIBattle = new CActiveMissionAIBattle();
			cActiveScenarioQuestAIBattle.missionid = taskid;
			cActiveScenarioQuestAIBattle.npckey = npckey;

			role.sendProtocol(cActiveScenarioQuestAIBattle);

		} else {
			// 直接提交完成任务
			CCommitMission cCommitScenarioQuest = new CCommitMission();
			cCommitScenarioQuest.npckey = npckey;
			cCommitScenarioQuest.missionid = taskid;
			role.sendProtocol(cCommitScenarioQuest);
		}

	}

	/**
	 * 根据任务id获得目标点的坐标
	 * 
	 * @param taskid
	 * @return
	 */
	public LogicPos getTargetRandXYWithTaskId(int taskid) {
		MissionCfgMgr conf = MissionMgr.getInstance().getTaskConfig(taskid);
		if (conf == null) {
			mkdb.Trace.info("task conf is null.taskid:" + taskid);
			return null;
		}

		int nWidth = conf.missionActiveInfo.rightPos
				- conf.missionActiveInfo.leftPos;
		nWidth = nWidth - 1;
		if (nWidth < 0) {
			nWidth = 0;
		}
		int nRandWidth = 0;
		if (nWidth > 0) {
			nRandWidth = MathUtils.randomInt(nWidth) / GRID_SIZE;
		}
		int nTargetPosX = conf.missionActiveInfo.leftPos + nRandWidth;

		int nHeight = conf.missionActiveInfo.bottomPos
				- conf.missionActiveInfo.topPos;
		nHeight = nHeight - 1;
		if (nHeight < 0) {
			nHeight = 0;
		}
		int nRandHeight = 0;
		if (nHeight > 0) {
			nRandHeight = MathUtils.randomInt(nHeight) / GRID_SIZE;
		}
		int nTargetPosY = conf.missionActiveInfo.topPos + nRandHeight;

		return new LogicPos(nTargetPosX * GRID_SIZE, nTargetPosY * GRID_SIZE);

	}

	private void roleMove() {
		if (targetpos != null) {
			LogicPos startpos = new LogicPos(role.getPosX(), role.getPosY());

			CRoleMove rolemove = new CRoleMove();
			rolemove.sceneid = role.getSceneID();
			rolemove.srcpos = startpos.toProtocolPos();
			rolemove.destpos = targetpos.toProtocolPos();
			// mkdb.Trace.info("state=" + state + "send CRoleMove:srcpos.x=" +
			// startpos.getX() + ",srcpos.y=" + startpos.getY() +
			// ",targetpos.x=" + targetpos.getX() + ",targetpos.y=" +
			// targetpos.getY());
			role.sendProtocol(rolemove);
			state = 1;
		}
	}

	private void checkMove() {
		if (targetpos != null) {
			LogicPos nowpos = new LogicPos(role.getPosX(), role.getPosY());
			LogicPos nextcheckpos = getNextCheckPos(nowpos, targetpos);
			role.setPos(nextcheckpos.getX(), nextcheckpos.getY());
			CCheckMove checkmove = new CCheckMove();
			checkmove.sceneid = role.getSceneID();
			checkmove.curpos = nextcheckpos.toProtocolPos();
			// mkdb.Trace.info("state=" + state + "send CCheckMove:curpos.x="
			// + nextcheckpos.getX() + ",curpos.y=" + nextcheckpos.getY() +
			// " role=" + role.loginui.getUserName() + " roleid=" +
			// role.roleId);
			role.sendProtocol(checkmove);
			if (nextcheckpos.equals(targetpos)) {
				roleStop();
				if (tasktype == TASK_USE_ITEM) {// 使用道具
					useItemTask(taskid);
				} else if (tasktype == TASK_PATROL) {// 如果是巡逻的任务，没有完成任务，重新随机一个点，继续移动
					if (targetpos.equals(oldpos)) {
						targetpos = getTargetRandXYWithTaskId(taskid);
					} else {
						targetpos = oldpos;
					}
					// 设置为开始移动状态
					this.state = 0;
				} else if (tasktype == TASK_TRIGGER_BY_AREA) {// 到地图上的某个点
					// 直接提交完成任务
					CCommitMission cCommitMission = new CCommitMission();
					cCommitMission.npckey = 0;
					cCommitMission.missionid = taskid;
					role.sendProtocol(cCommitMission);
					end();
				} else {
					// 当前地图是否有当前npc
					if (npcs != null) {
						for (fire.pb.move.NpcBasic npc : npcs) {
							if (npc.id == npcID) {
								npckey = npc.npckey;
								CVisitNpc cvisitnpc = new CVisitNpc(npc.npckey);
								role.sendProtocol(cvisitnpc);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 使用道具的任务
	 * 
	 * @param taskid
	 */
	public void useItemTask(int taskid) {
		MissionCfgMgr conf = MissionMgr.getInstance().getTaskConfig(taskid);
		if (conf == null) {
			mkdb.Trace.info("task conf is null.taskid:" + taskid);
			return;
		}

		try {
			// 使用道具
			CAppendItem cUseItem = new CAppendItem();
			cUseItem.keyinpack = role
					.getTaskItemKey(conf.missionActiveInfo.useItemID);
			cUseItem.idtype = IDType.ROLE;
			cUseItem.id = role.roleId;

			if (cUseItem.keyinpack == -1) {
				mkdb.Trace.info("物品信息数据错误  " + cUseItem.keyinpack + "\t");
			}
			// mkdb.Trace.info("cUseItem.itemkey=" + cUseItem.itemkey);
			role.sendProtocol(cUseItem);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 移动停止
	 */
	private void roleStop() {
		CRoleStop rolestop = new CRoleStop();
		rolestop.pos = new LogicPos(role.getPosX(), role.getPosY())
				.toProtocolPos();
		// mkdb.Trace.info("state=" + state + "send CRoleStop:pos.x=" +
		// rolestop.pos.x + ",pos.y=" + rolestop.pos.y);
		role.sendProtocol(rolestop);
	}

	/**
	 * 获得下一点坐标
	 * 
	 * @param nowpos
	 * @param targetpos
	 * @return
	 */
	private LogicPos getNextCheckPos(LogicPos nowpos, LogicPos targetpos) {
		if (nowpos.equals(targetpos)) {
			samePointCount++;
			// System.out.println("getNextCheckPos Same?");
			return nowpos;
		} else {
			samePointCount = 0;
		}

		int deltax = targetpos.getX() - nowpos.getX();
		int deltay = targetpos.getY() - nowpos.getY();
		int targetx = targetpos.getX();
		int targety = targetpos.getY();
		if (Math.abs(deltax) > Constant.MOVE_SPEED)
			targetx = nowpos.getX()
					+ ((deltax > 0) ? Constant.MOVE_SPEED
							: -Constant.MOVE_SPEED);
		if (Math.abs(deltay) > Constant.MOVE_SPEED)
			targety = nowpos.getY()
					+ ((deltay > 0) ? Constant.MOVE_SPEED
							: -Constant.MOVE_SPEED);
		return new LogicPos(targetx, targety);
	}

}
