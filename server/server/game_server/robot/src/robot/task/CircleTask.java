package robot.task;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.h2.util.MathUtils;

import robot.ConfigMgr;
import robot.Constant;
import robot.LoginRole;
import robot.RoleBag;
import robot.manager.MissionCfgMgr;
import robot.manager.MissionMgr;
import robot.manager.NPCMgr;
import robot.pos.LogicPos;
import mkio.Protocol;
import fire.pb.battle.CSendAction;
import fire.pb.battle.CSendRoundPlayEnd;
import fire.pb.battle.OperationType;
import fire.pb.battle.SSendBattleEnd;
import fire.pb.battle.SSendBattleStart;
import fire.pb.battle.SSendRoundPlayEnd;
import fire.pb.battle.SSendRoundScript;
import fire.pb.battle.SSendRoundStart;
import fire.pb.circletask.CircTaskConf;
import fire.pb.circletask.SRefreshSpecialQuest;
import fire.pb.item.CAppendItem;
import fire.pb.item.IDType;
import fire.pb.mission.CActiveMissionAIBattle;
import fire.pb.mission.CCommitMission;
import fire.pb.move.CCheckMove;
import fire.pb.move.CRoleMove;
import fire.pb.move.CRoleStop;
import fire.pb.move.SAddUserScreen;
import fire.pb.move.SRemoveUserScreen;
import fire.pb.move.SRoleEnterScene;
import fire.pb.npc.CNpcService;
import fire.pb.npc.CSubmit2Npc;
import fire.pb.npc.CVisitNpc;
import fire.pb.npc.SNpcShare;
import fire.pb.npc.SVisitNpc;
import fire.pb.npc.SubmitType;
import fire.pb.npc.SubmitUnit;

/**
 * 循环任务 Added By Bill Ye
 */
public class CircleTask extends Task_RoleBase {
	public CircleTask(LoginRole r) {
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

	private LogicPos targetpos = null;
	private int npcID;// npc策划数据的编号
	private long npckey;// npc在服务器中的唯一索引
	private int taskid;// 任务id
	private int taskidDetail; // 详细id(有2个ID,一个最后是00结尾,一个是详细的分类)
	private int tasktype;// 任务类型
	private int dstItemTypeId;// 目标道具typeid
	private int dstItemNum; // 目标道具数目
	private java.util.LinkedList<fire.pb.move.NpcBasic> npcs = null;

	private LogicPos oldpos = null;

	private BlockingQueue<Protocol> protocols = new LinkedBlockingQueue<Protocol>();
	Map<String, String> params;

	// -1 空闲状态 0行走状态 1轻功状态
	private byte state = 0;
	public long needToReLoginTime = 0;
	public int samePointCount = 0;

	@Override
	public void run() {
		// 发送心跳包
		Global.DoGlobalTask(this);

		// 先处理protocols
		dealProtocols();
		if (state == 0) {
			roleMove();
		} else if (state == 1) {
			checkMove();
		}

		if ((System.currentTimeMillis() - needToReLoginTime > 2 * 60 * 1000)
				|| (samePointCount > 10)) {
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

	public final int CTASK_DELIVER = 1;
	public final int CTASK_USE_ITEM = 2;
	public final int CTASK_COLLECT_ITEM = 3;
	public final int CTASK_FIND_ITEM = 4;
	public final int CTASK_FIND_PET = 5;
	public final int CTASK_DO_BATTLE = 6;
	public final int CTASK_FIND_BOSS = 7;
	public final int CTASK_KILL_MONSTER = 8;
	public final int CTASK_CHALLENGE = 9;

	/**
	 * 通过类型id获取类型名称
	 */
	public String getCircleTaskTypeById(int cttype) {
		switch (cttype) {
		case 1:
			return "送信拜访";
		case 2:
			return "使用物品";
		case 3:
			return "收集物品";
		case 4:
			return "寻找物品";
		case 5:
			return "寻找宠物";
		case 6:
			return "巡逻战斗";
		case 7:
			return "考古捉鬼";
		case 8:
			return "英雄试炼";
		case 9:
			return "挑战切磋";
		}
		return "未知错误类型";
	}

	/**
	 * 
	 * 处理协议
	 * 
	 * @param p
	 */
	protected void dealProtocol(mkio.Protocol p) {
		needToReLoginTime = System.currentTimeMillis();
		// System.out.println(p.getClass());
		if (p instanceof SRefreshSpecialQuest) {
			SRefreshSpecialQuest msgInfo = (SRefreshSpecialQuest) p;
			taskidDetail = msgInfo.questtype;
			CircTaskConf conf = MissionMgr.circTaskConfMap.get(taskidDetail);
			if (conf == null) {
				System.out.println("无此循环任务相对应的类型");
				return;
			}

			// 接受任务的时候end一下
			end();
			mkdb.Trace.info(new StringBuilder()
					.append("当前任务id:")
					.append(conf.id)
					.append("\t任务名称:")
					.append(conf.getStrtaskname().replace(
							"($Number$/$Number1$)", "")).append("\t任务类型:")
					.append(getCircleTaskTypeById(conf.cttype))
					.append("\tUserName=")
					.append(role.getLoginui().getUserName())
					.append("\troleid=").append(role.roleId));

			taskid = msgInfo.questid;
			taskidDetail = msgInfo.questtype;
			tasktype = conf.cttype;
			switch (tasktype) {
			case CTASK_DELIVER: // 送信拜访类任务-送信类分表
				OnDeliver(conf, msgInfo);
				break;
			case CTASK_USE_ITEM: // 使用物品类任务-使用道具类分表
				OnUseItem(conf, msgInfo);
				break;
			case CTASK_COLLECT_ITEM: // 收集物品类任务-收集物品类分表
				OnCollectItem(conf, msgInfo);
				break;
			case CTASK_FIND_ITEM: // 寻找物品类任务-寻找物品类分表
				OnFindItem(conf, msgInfo);
				break;
			case CTASK_FIND_PET: // 寻找宠物类任务-寻找宠物类分表
				OnFindPet(conf, msgInfo);
				break;
			case CTASK_DO_BATTLE: // 巡逻战斗类任务-巡逻类分表
				OnCollectItem(conf, msgInfo);
				break;
			case CTASK_FIND_BOSS: // 考古捉鬼类任务-明雷类分表
				break;
			case CTASK_KILL_MONSTER: // 英雄试炼类任务-杀怪类分表
				break;
			case CTASK_CHALLENGE: // 挑战切磋类任务-挑战类分表
				break;
			default:
				break;
			}
		} else if (p instanceof SRoleEnterScene) {
			role.setScene(((SRoleEnterScene) p).sceneid);
			role.setPos(((SRoleEnterScene) p).destpos.x,
					((SRoleEnterScene) p).destpos.y);
			oldpos = new LogicPos(role.getPosX(), role.getPosY());
		} else if (p instanceof SAddUserScreen) {
			SAddUserScreen sAddUserScreen = (SAddUserScreen) p;
			if (npcs == null) {
				npcs = sAddUserScreen.npclist;
			} else {
				npcs.addAll(sAddUserScreen.npclist);
			}
		} else if (p instanceof SRemoveUserScreen) {

		} else if (p instanceof SVisitNpc) {
			dealSVisitNpc(taskid);
		} else if (p instanceof SSendBattleStart) {
			role.sendCommand("//full");
			end();
			mkdb.Trace.info("role[" + role.roleId + "] 战斗开始！");
		} else if (p instanceof SSendRoundStart) {
			// mkdb.Trace.info("role[" + role.roleId +
			// "]recv:SSendRoundStart====回合开始！");
			// 简单模拟客户端玩家操作,3秒后反馈给服务端
			mkdb.Executor.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					int skillid = ConfigMgr.getInstance().randSkillByPlayerType(
							role.school);
					CSendAction snd = new CSendAction();
					snd.isrole = 1;
					snd.action.aim = 0;
					if (skillid > 0) {
						snd.action.operationtype = OperationType.ACTION_SKILL;
						snd.action.operationid = skillid;
					} else {
						snd.action.operationtype = OperationType.ACTION_ATTACK;
					}
					role.sendProtocol(snd);

					CSendAction sndpet = new CSendAction();
					sndpet.isrole = 0;
					sndpet.action.aim = 0;
					sndpet.action.operationtype = OperationType.ACTION_ATTACK;
					role.sendProtocol(sndpet);
				}
			}, 5, TimeUnit.SECONDS);
		} else if (p instanceof SSendRoundScript) {
			mkdb.Executor.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					role.sendProtocol(new CSendRoundPlayEnd());
				}
			}, 5, TimeUnit.SECONDS);
		} else if (p instanceof SSendRoundPlayEnd) {

		} else if (p instanceof SSendBattleEnd) {
			state = 1;
			mkdb.Trace.info("role[" + role.roleId + "] 战斗结束！");
		}
	}

	/**
	 * 送信类型的任务
	 */
	public void OnDeliver(CircTaskConf conf, SRefreshSpecialQuest msgInfo) {
		if (conf == null) {
			System.out.println("task conf is null.taskid:" + taskid);
			return;
		}

		// 判断目标地图是否是当前地图，如果不是，用GM命令跳到对应地图
		if (msgInfo.dstmapid != 0 && role.getMapId() != msgInfo.dstmapid) {
			// 在跳地图之前清空npcs
			npcs = null;
			role.sendCommand("//gomap " + msgInfo.dstmapid);
		}

		// 获得使用物品的坐标
		int nTargetPosX = 0, nTargetPosY = 0;
		if (msgInfo.dstnpcid != 0) {
			npcID = msgInfo.dstnpcid;
			npckey = msgInfo.dstnpckey;
			SNpcShare npc = NPCMgr.getInstance().getNpcInfoByID(npcID);
			if (npc != null) {
				if (role.getMapId() != npc.mapid) {
					npcs = null;
					role.sendCommand("//gomap " + npc.mapid);
				}

				nTargetPosX = npc.getXPos() * GRID_SIZE;
				nTargetPosY = npc.getYPos() * GRID_SIZE;
				targetpos = new LogicPos(nTargetPosX, nTargetPosY);
				// 设置为开始移动状态
				this.state = 0;
			}
		}
	}

	/**
	 * 使用物品类型的任务
	 * 
	 * @param taskid
	 */
	public void OnUseItem(CircTaskConf conf, SRefreshSpecialQuest msgInfo) {
		if (conf == null) {
			System.out.println("task conf is null.taskid:" + taskid);
			return;
		}
		int mapID = msgInfo.dstmapid;

		// 判断背包是否有这个道具，如果没有gm添加一个
		dstItemTypeId = msgInfo.dstitemid;
		if (role.getTaskItemKey(dstItemTypeId) == -1) {
			role.sendCommand("//additem " + dstItemTypeId);
		}

		// 判断目标地图是否是当前地图，如果不是，用GM命令跳到对应地图
		if (role.getMapId() != mapID) {
			// 在跳地图之前清空npcs
			// npcs = null;
			role.sendCommand("//gomap " + mapID);
		}

		// 获得使用物品的坐标
		targetpos = new LogicPos(msgInfo.dstx * GRID_SIZE, msgInfo.dsty
				* GRID_SIZE);
		if (targetpos != null) {
			mkdb.Trace.info("taskid:" + taskid + "\t坐标x：" + targetpos.getX()
					+ "\t坐标x：" + targetpos.getY());
		}
		// 设置为开始移动状态
		this.state = 0;
	}

	/**
	 * 收集物品类型任务
	 */
	public void OnCollectItem(CircTaskConf conf, SRefreshSpecialQuest msgInfo) {
		if (conf == null) {
			System.out.println("task conf is null.taskid:" + taskid);
			return;
		}

		// 判断目标地图是否是当前地图，如果不是，用GM命令跳到对应地图
		if (role.getMapId() != msgInfo.dstmapid) {
			// 在跳地图之前清空npcs
			role.sendCommand("//gomap " + msgInfo.dstmapid);
		}

		// 获得使用物品的坐标
		targetpos = new LogicPos(msgInfo.dstx * GRID_SIZE, msgInfo.dsty
				* GRID_SIZE);
		if (targetpos != null) {
			mkdb.Trace.info("taskid:" + taskid + "\t坐标x：" + targetpos.getX()
					+ "\t坐标x：" + targetpos.getY());
		}

		// 设置为开始移动状态
		this.state = 0;
	}

	/**
	 * 寻找物品类型任务
	 * 
	 * @param taskid
	 */
	public void OnFindItem(CircTaskConf conf, SRefreshSpecialQuest msgInfo) {
		if (conf == null) {
			System.out.println("task conf is null.taskid:" + taskid);
			return;
		}

		// 判断背包是否有这个道具，如果没有gm添加一个
		RoleBag bag = role.getRoleBag();
		dstItemTypeId = msgInfo.dstitemid;
		dstItemNum = msgInfo.dstitemnum;
		int bagItemNum = bag.getItemNum(msgInfo.dstitemid);
		if (bagItemNum < dstItemNum) {
			role.sendCommand("//additem " + dstItemTypeId + " "
					+ (dstItemNum - bagItemNum));
		}

		// 判断目标地图是否是当前地图，如果不是，用GM命令跳到对应地图
		if (msgInfo.dstmapid != 0 && role.getMapId() != msgInfo.dstmapid) {
			// 在跳地图之前清空npcs
			npcs = null;
			role.sendCommand("//gomap " + msgInfo.dstmapid);
		}

		// 获得使用物品的坐标
		int nTargetPosX = 0, nTargetPosY = 0;
		if (msgInfo.dstnpcid != 0) {
			npcID = msgInfo.dstnpcid;
			npckey = msgInfo.dstnpckey;
			SNpcShare npc = NPCMgr.getInstance().getNpcInfoByID(npcID);
			if (npc != null) {
				if (role.getMapId() != npc.mapid) {
					npcs = null;
					role.sendCommand("//gomap " + npc.mapid);
				}

				nTargetPosX = npc.getXPos() * GRID_SIZE;
				nTargetPosY = npc.getYPos() * GRID_SIZE;
				targetpos = new LogicPos(nTargetPosX, nTargetPosY);
				// 设置为开始移动状态
				this.state = 0;
			}
		} else if (!(msgInfo.dstx == 0 && msgInfo.dsty == 0)) {
			targetpos = new LogicPos(msgInfo.dstx * GRID_SIZE, msgInfo.dsty
					* GRID_SIZE);
			if (targetpos != null) {
				mkdb.Trace.info("taskid:" + taskid + "\t坐标x：" + targetpos.getX()
						+ "\t坐标x：" + targetpos.getY());
			}
		}
	}

	public void OnFindPet(CircTaskConf conf, SRefreshSpecialQuest msgInfo) {
		if (conf == null) {
			System.out.println("task conf is null.taskid:" + taskid);
			return;
		}

		// gm添加一个
		dstItemTypeId = msgInfo.dstitemid;
		dstItemNum = msgInfo.dstitemnum;
		RoleBag bag = role.getRoleBag();
		if (!bag.isHasPet(dstItemTypeId)) {
			role.sendCommand("//addpet " + dstItemTypeId);
		}

		// 判断目标地图是否是当前地图，如果不是，用GM命令跳到对应地图
		if (msgInfo.dstmapid != 0 && role.getMapId() != msgInfo.dstmapid) {
			// 在跳地图之前清空npcs
			npcs = null;
			role.sendCommand("//gomap " + msgInfo.dstmapid);
		}

		// 获得使用物品的坐标
		int nTargetPosX = 0, nTargetPosY = 0;
		if (msgInfo.dstnpcid != 0) {
			npcID = msgInfo.dstnpcid;
			npckey = msgInfo.dstnpckey;
			SNpcShare npc = NPCMgr.getInstance().getNpcInfoByID(npcID);
			if (npc != null) {
				if (role.getMapId() != npc.mapid) {
					npcs = null;
					role.sendCommand("//gomap " + npc.mapid);
				}

				nTargetPosX = npc.getXPos() * GRID_SIZE;
				nTargetPosY = npc.getYPos() * GRID_SIZE;
				targetpos = new LogicPos(nTargetPosX, nTargetPosY);
				// 设置为开始移动状态
				this.state = 0;
			}
		} else if (!(msgInfo.dstx == 0 && msgInfo.dsty == 0)) {
			targetpos = new LogicPos(msgInfo.dstx * GRID_SIZE, msgInfo.dsty
					* GRID_SIZE);
			if (targetpos != null) {
				mkdb.Trace.info("taskid:" + taskid + "\t坐标x：" + targetpos.getX()
						+ "\t坐标x：" + targetpos.getY());
			}
		}
	}

	/**
	 * 处理访问npc后的请求逻辑
	 * 
	 * @param taskid
	 */
	public void dealSVisitNpc(int taskid) {
		end();
		CircTaskConf conf = MissionMgr.circTaskConfMap.get(taskidDetail);
		if (conf == null) {
			System.out.println("无此循环任务相对应的类型");
			return;
		}

		// 判断是否是战斗，如果是战斗
		switch (tasktype) {
		case CTASK_DELIVER:
			CNpcService cNpcService = new CNpcService(npckey, 100035);
			role.sendProtocol(cNpcService);
			break;
		case CTASK_FIND_BOSS:
			CActiveMissionAIBattle cActiveScenarioQuestAIBattle = new CActiveMissionAIBattle();
			cActiveScenarioQuestAIBattle.missionid = taskid;
			cActiveScenarioQuestAIBattle.npckey = npckey;
			role.sendProtocol(cActiveScenarioQuestAIBattle);
			break;
		default:
			// 直接提交完成任务
			CCommitMission cCommitScenarioQuest = new CCommitMission();
			cCommitScenarioQuest.npckey = npckey;
			cCommitScenarioQuest.missionid = taskid;
			role.sendProtocol(cCommitScenarioQuest);
			break;
		}
	}

	/**
	 * 根据任务id获得目标点的坐标
	 * 
	 * @param taskid
	 * @return
	 */
	public LogicPos getTargetRand(LogicPos p) {
		int n[] = { 5, 10, -5, -10 };
		int randx = MathUtils.randomInt(n.length);
		int randy = MathUtils.randomInt(n.length);
		return new LogicPos(p.getX() + randx * GRID_SIZE, p.getY() + randy
				* GRID_SIZE);
	}

	private void roleMove() {
		if (targetpos != null) {
			LogicPos startpos = new LogicPos(role.getPosX(), role.getPosY());
			CRoleMove rolemove = new CRoleMove();
			rolemove.sceneid = role.getSceneID();
			rolemove.srcpos = startpos.toProtocolPos();
			rolemove.destpos = targetpos.toProtocolPos();
			mkdb.Trace.info("state=" + state + " send CRoleMove:srcpos.x="
					+ startpos.getX() + ",srcpos.y=" + startpos.getY()
					+ ",targetpos.x=" + targetpos.getX() + ",targetpos.y="
					+ targetpos.getY());
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
			mkdb.Trace.info("state=" + state + "send CCheckMove:curpos.x="
					+ nextcheckpos.getX() + ",curpos.y=" + nextcheckpos.getY()
					+ " role=" + role.loginui.getUserName() + " roleid="
					+ role.roleId);
			role.sendProtocol(checkmove);
			if (nextcheckpos.equals(targetpos)) {
				roleStop();
				// 当前地图是否有当前npc
				switch (tasktype) {
				case CTASK_USE_ITEM: // 使用物品类任务-使用道具类分表
					try {
						// 使用道具
						CAppendItem cUseItem = new CAppendItem(
								role.getTaskItemKey(dstItemTypeId),
								IDType.ROLE, role.roleId);
						if (cUseItem.keyinpack == -1) {
							mkdb.Trace.info("物品信息数据错误  " + cUseItem.keyinpack
									+ "\t");
						}
						mkdb.Trace.info("cUseItem.itemkey=" + cUseItem.keyinpack);
						role.sendProtocol(cUseItem);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case CTASK_COLLECT_ITEM: // 收集物品类任务-收集物品类分表
				case CTASK_DO_BATTLE: // 巡逻战斗类任务-巡逻类分表
					if (targetpos.equals(oldpos)) {
						targetpos = getTargetRand(oldpos);
					} else {
						targetpos = oldpos;
					}
					// 设置为开始移动状态
					this.state = 0;
					break;
				case CTASK_FIND_ITEM: // 寻找物品类任务-寻找物品类分表
					if (npcs != null) {
						for (fire.pb.move.NpcBasic npc : npcs) {
							if (npc.id == npcID) {
								ArrayList<SubmitUnit> items = new ArrayList<SubmitUnit>();
								int key = role.getTaskItemKey(dstItemTypeId);
								if (key == -1)
									mkdb.Trace.info("key is null key:" + key);
								items.add(new SubmitUnit(key, dstItemNum));
								CSubmit2Npc csub = new CSubmit2Npc(taskid,
										npc.npckey, SubmitType.ITEM, items);
								role.sendProtocol(csub);
								end();
							}
						}
					}
					break;
				case CTASK_FIND_PET: // 寻找宠物类任务-寻找宠物类分表
					if (npcs != null) {
						for (fire.pb.move.NpcBasic npc : npcs) {
							if (npc.id == npcID) {
								ArrayList<SubmitUnit> items = new ArrayList<SubmitUnit>();
								RoleBag bag = role.getRoleBag();
								if (bag != null) {
									int petkey = bag.getPetKey(dstItemTypeId);
									if (petkey == -1) {
										mkdb.Trace.info("pet is null petkey:"
												+ petkey);
									}
									items.add(new SubmitUnit(petkey, dstItemNum));
									CSubmit2Npc csub = new CSubmit2Npc(taskid,
											npc.npckey, SubmitType.PET, items);
									role.sendProtocol(csub);
									end();
								}

							}
						}
					}
					break;
				case CTASK_FIND_BOSS: // 考古捉鬼类任务-明雷类分表
				case CTASK_KILL_MONSTER: // 英雄试炼类任务-杀怪类分表
				case CTASK_CHALLENGE: // 挑战切磋类任务-挑战类分表
				case CTASK_DELIVER: // 送信拜访类任务-送信类分表
				default:
					if (npcs != null) {
						for (fire.pb.move.NpcBasic npc : npcs) {
							if (npc.id == npcID) {
								npckey = npc.npckey;
								CVisitNpc cvisitnpc = new CVisitNpc(npc.npckey);
								role.sendProtocol(cvisitnpc);
							}
						}
					}
					break;
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