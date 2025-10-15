package fire.pb.mission.instance;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.log.Logger;
import fire.pb.PAddExpProc;
import fire.pb.circletask.SRefreshSpecialQuestState;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.event.ArriveTeamSpecialQuestEvent;
import fire.pb.event.Event;
import fire.pb.event.EventHandler;
import fire.pb.event.LeaveTeamSpecialQuestEvent;
import fire.pb.fanpai.PReqCardProc;
import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.ActivityInfo;
import fire.pb.mission.InstanceInfo;
import fire.pb.mission.Module;
import fire.pb.mission.SLandFightConfig;
import fire.pb.mission.SLandPingJi;
import fire.pb.mission.SLandTask;
import fire.pb.mission.SLandTaskConfig;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.npc.SNpcShare;
import fire.pb.npc.SPingJi;
import fire.pb.scene.DynamicScene;
import fire.pb.scene.SceneManager;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.CalendarUtil;
import fire.pb.util.DateValidate;
import fire.pb.util.Misc;
import fire.pb.util.TaskDlgUtil;
import fire.pb.util.TaskDlgUtil.NpcModel;
import mkdb.Procedure;


public class InstanceManager implements EventHandler {

	static class SingletonHolder {
		static InstanceManager singleton = new InstanceManager();
	}

	public static InstanceManager getInstance() {
		return SingletonHolder.singleton;
	}

	public static final Logger logger = Logger.getLogger("TASK");
	
	public static final int ZU_DUI_FU_BEN = 10565;

	public static final int ZU_DUI_FU_BEN_TIMES = 3;
	public static final long Time_Out = 2 * 60 * 60 * 1000;

	public static final long DESTROY_INSTANCE = 10 * 60;

	// 领取任务
	public static final int ZU_DUI_FU_BEN_SERVICE = 460;

	// 回到副本
	public static final int ZU_DUI_FU_BEN_BACK_SERVICE = 1469;

	// 回主城
	public static final int ZILUOLAN_MAP = 1615;
	public static final int ZILUOLAN_POS_X = 102;
	public static final int ZILUOLAN_POS_Y = 104;

	public static final int MIN_ROLE_SIZE = 3;

	public static final int TURN_DAY = 1;
	public static final int TURN_WEEK = 2;
	public static final int TURN_MONTH = 3;

	
	private Map<Integer, SLandTask> instanceInfo = new HashMap<Integer, SLandTask>();
	private Map<Integer, List<SLandTaskConfig>> configs = new HashMap<Integer, List<SLandTaskConfig>>();
	private Map<Integer, SLandFightConfig> fightConfigs = new HashMap<Integer, SLandFightConfig>();
	private List<Integer> instanceTaskIdList = new ArrayList<Integer>();
	private List<SLandPingJi> instancePingjiList = new ArrayList<SLandPingJi>();

	// 任务轮换配置信息, key为组id, value 轮换组信息
	public Map<Integer, TurnClass> instTurnConfigs = new ConcurrentHashMap<Integer, TurnClass>();

	// 副本对应的地图信息 key 副本id, value 地图list
	public Map<Integer, List<Integer>> instanceToMapList = new HashMap<Integer, List<Integer>>();

	public Map<Integer, ServiceConfig> instZoneServiceConfigs = new HashMap<Integer, ServiceConfig>();
	// 副本询问定时器
	public static Map<Long, ScheduledFuture<?>> askInstFuture = new ConcurrentHashMap<Long, ScheduledFuture<?>>();

	
	public void init() {
		loadData();
		setInstanceInfo(TURN_DAY, 0);
		setInstanceInfo(TURN_WEEK, 0);
		setInstanceInfo(TURN_MONTH, 0);

//		fire.pb.event.Poster.getPoster().listenEvent(this,
//				fire.pb.event.LeaveTeamSpecialQuestEvent.class);
//		fire.pb.event.Poster.getPoster().listenEvent(this,
//				fire.pb.event.ArriveTeamSpecialQuestEvent.class);
	}

	private InstanceManager() {
	}

	public void loadData() {
		configs.clear();
		instanceInfo.clear();
		instancePingjiList.clear();

		// 读取任务配置
		Map<Integer, SLandTaskConfig> map = fire.pb.main.ConfigManager
				.getInstance().getConf(SLandTaskConfig.class);
		if (map != null) {
			for (SLandTaskConfig config : map.values()) {
				int taskType = config.getTasktype();
				List<SLandTaskConfig> taskList = configs.get(taskType);
				if (taskList == null) {
					taskList = new ArrayList<SLandTaskConfig>();
					configs.put(taskType, taskList);
				}
				taskList.add(config);

				instanceTaskIdList.add(config.getTaskid());

				List<Integer> mapList = instanceToMapList.get(taskType);
				if (mapList == null) {
					mapList = new ArrayList<Integer>();
					instanceToMapList.put(taskType, mapList);
				}

				if (!mapList.contains(config.getMapid())) {
					mapList.add(config.getMapid());
				}
			}
		}

		Map<Integer, SLandFightConfig> tmp = fire.pb.main.ConfigManager
				.getInstance().getConf(SLandFightConfig.class);
		if (tmp != null) {
			fightConfigs.putAll(tmp);

			for (SLandFightConfig instConfig : tmp.values()) {
				ServiceConfig serv = new ServiceConfig(instConfig);
				instZoneServiceConfigs.put(instConfig.getId(), serv);
			}
		}

		Map<Integer, SLandTask> instanceTask = fire.pb.main.ConfigManager
				.getInstance().getConf(SLandTask.class);
		if (instanceTask == null) {
			return;
		}

		instanceInfo.putAll(instanceTask);

		for (SLandTask mission : instanceTask.values()) {
			if (mission.turngroup == 0)
				continue;

			TurnClass turnClass = instTurnConfigs.get(mission.turngroup);
			if (turnClass == null) {
				turnClass = new TurnClass();
				turnClass.groupid = mission.turngroup;
				turnClass.turntype = mission.turntype;
				instTurnConfigs.put(mission.turngroup, turnClass);
			}

			TurnGroup turnGroup = new TurnGroup(mission.id, mission.activityid,
					mission.activitytypeid, mission.turnid, mission.turntype,
					mission.turngroup, mission.instancezoneid);
			turnClass.groupList.add(turnGroup);
		}

		Map<Integer, SLandPingJi> pingji = fire.pb.main.ConfigManager
				.getInstance().getConf(SLandPingJi.class);
		if (pingji != null) {
			for (SLandPingJi pingji1 : pingji.values()) {
				instancePingjiList.add(pingji1);
			}
		}
	}

	public SLandTask getInstanceTask(int taskType) {
		for (SLandTask instanceTask : instanceInfo.values()) {
			if (instanceTask.instancezoneid == taskType)
				return instanceTask;
		}
		return null;
	}

	
	public List<Integer> getInstanceMapId(final int taskType) {
		if (instanceToMapList.containsKey(taskType)) {
			return instanceToMapList.get(taskType);
		}
		return null;
	}

	
	public int getTaskId(int taskType, int step, int order) {
		List<SLandTaskConfig> taskList = configs.get(taskType);
		int taskId = -1;
		if (taskList != null) {
			int index = 0;
			for (SLandTaskConfig config : taskList) {
				if (config.getStep() == step) {
					if (index == order) {
						taskId = config.getTaskid();
						break;
					}
					index++;
				}
			}
			SLandTaskConfig config = taskList.get(order);
			return config.getTaskid();
		}
		return taskId;
	}

	
	public SLandTaskConfig getTaskConfig(int taskType, int step, int order) {
		SLandTaskConfig config = null;
		List<SLandTaskConfig> configs = getStepTask(taskType, step);
		if (configs.size() >= order + 1) {
			config = configs.get(order);
		}
		return config;
	}

	
	public boolean isInstanceCopy(int mapId) {
		if (instanceToMapList == null) {
			return false;
		}

		for (List<Integer> cfg : instanceToMapList.values()) {
			if (cfg.contains(mapId))
				return true;
		}
		return false;
	}
	
	public int getTaskSepSize(int taskType, int step) {
		List<SLandTaskConfig> taskList = configs.get(taskType);
		int size = 0;
		if (taskList != null) {
			for (SLandTaskConfig config : taskList) {
				if (config.getStep() == step) {
					size++;
				}
			}
		}
		return size;
	}

	public List<SLandTaskConfig> getStepTask(int taskType, int step) {
		List<SLandTaskConfig> taskList = configs.get(taskType);
		List<SLandTaskConfig> stepTaskList = new ArrayList<SLandTaskConfig>();
		for (SLandTaskConfig config : taskList) {
			if (config.getStep() == step) {
				if (config.getSteprate() > 0) {
					int nRate = Misc.getRatePercent();
					if (nRate <= config.getSteprate()) {
						stepTaskList.add(config);
						break;
					}
				} else {
					stepTaskList.add(config);
				}
			}
		}
		return stepTaskList;
	}

	
	public int getFightId(int serviceId) {
		SLandFightConfig cofig = fightConfigs.get(serviceId);
		if (cofig != null) {
			return cofig.getFightid();
		}
		return -1;
	}

	
	public ServiceConfig getServiceConfig(final int serviceId) {
		if (instZoneServiceConfigs.containsKey(serviceId))
			return instZoneServiceConfigs.get(serviceId);

		return null;
	}

	
	public int getInstanceIdByServiceId(final int serviceId) {
		SLandFightConfig cofig = fightConfigs.get(serviceId);
		if (cofig != null) {
			return cofig.getInstancezoneid();
		}
		return -1;
	}

	
	public int getAwardId(int serviceId) {
		SLandFightConfig cofig = fightConfigs.get(serviceId);
		if (cofig != null) {
			return cofig.getAward();
		}
		return -1;
	}

	public Set<Integer> getInstNpcServers() {
		return fightConfigs.keySet();
	}

	
	public boolean isSameDynamicScene(long currentScenceId, long wjltaskId,
			int mapId) {
		DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(
				mapId, wjltaskId);
		if (ds != null) {
			long dsId = ds.getSceneID();
			return currentScenceId == dsId;
		}
		return false;
	}

	
	public int getMapIdByTaskType(final int taskType, final int step) {
		List<SLandTaskConfig> taskList = configs.get(taskType);
		if (taskList != null) {
			for (SLandTaskConfig config : taskList) {
				if (config.getStep() == step) {
					return config.getMapid();
				}
			}
		}
		return -1;
	}

	
	public boolean isInstanceTask(int taskId) {
		return instanceTaskIdList.contains(taskId);
	}

	
	public void abandonInstanceTask(final long roleid, final int questid) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {

				Team team = TeamManager.selectTeamByRoleId(roleid);
				if (team != null) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,
							145123, null);
					return false;
				}

				xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask
						.get(roleid);
				if (infoCol == null) {
					return false;
				}

				xbean.InstanceTaskInfo info = infoCol.getInstinfo()
						.get(questid);
				long instanceTaskid = info.getInstanceonlyid();
				info.setInstanceflag(2);
				xbean.InstanceInfo instInfo = xtable.Instancetask
						.get(instanceTaskid);
				refreshTaskState(roleid, questid, SpecialQuestState.ABANDONED);
				if (instInfo == null) {
					return true;
				}
				instInfo.getRoleids().remove(roleid);

				if (instInfo.getRoleids().size() < MIN_ROLE_SIZE) {
					List<Long> rids = new ArrayList<Long>();
					rids.addAll(instInfo.getRoleids());
					Collections.sort(rids);
					lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, rids));
					lock(mkdb.Lockeys.get(xtable.Locks.ROLEINSTANCETASK, rids));

					for (long rid : instInfo.getRoleids()) {
						xbean.InstanceInfoCol cols = xtable.Roleinstancetask
								.get(rid);
						if (cols == null) {
							return false;
						}

						xbean.InstanceTaskInfo infoo = cols.getInstinfo().get(
								questid);
						infoo.setInstanceflag(2);
						refreshTaskState(rid, questid, SpecialQuestState.FAIL);
					}
				}
				if (instInfo.getRoleids().size() == 0) {
					xtable.Instancetask.remove(instanceTaskid);
				}
				return true;
			}
		}.call();
	}

	
	public void abandonInstanceTask(final long roleid) {
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				logger.debug("abandonInstanceTask: " + roleid);
				Team team = TeamManager.selectTeamByRoleId(roleid);
				if (team != null) {
					return false;
				}

				xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask
						.get(roleid);
				if (infoCol == null) {
					return false;
				}

				xbean.InstanceTaskInfo info = infoCol.getInstinfo().get(
						infoCol.getLastinstanceid());
				if (info == null)
					return false;

				long instanceTaskid = info.getInstanceonlyid();
				xbean.InstanceInfo instInfo = xtable.Instancetask
						.get(instanceTaskid);
				if (instInfo == null) {
					return false;
				}

				Role mapRole = RoleManager.getInstance().getRoleByID(roleid);
				if (mapRole == null) {
					return false;
				}

				// Integer mapId = mapRole.getMapId();
				// List<Integer> mapList =
				// InstanceManager.getInstance().getInstanceMapId(infoCol.getLastinstanceid());

				// 清理一些数据
				final int questid = instInfo.getCurrenttaskid();
				instInfo.getRoleids().remove(roleid);
				info.setInstanceflag(2);

				// 如果在副本地图内,则不走放弃任务的逻辑
				refreshTaskState(roleid, questid, SpecialQuestState.INSTANCE_ABANDONED);

				if (instInfo.getRoleids().size() == 0) {
					xtable.Instancetask.remove(instanceTaskid);
				}
				return true;
			}
		}.call();
	}

	
	public void syncInstanceTask(final long roleid, final long teamleaderid) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				Team team = TeamManager.selectTeamByRoleId(roleid);
				if (team == null) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,
							145123, null);
					return false;
				}

				if (!team.isTeamLeader(teamleaderid)) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,
							145123, null);
					return false;
				}

				xbean.InstanceInfoCol teamLeaderInfoCol = xtable.Roleinstancetask
						.get(teamleaderid);
				if (teamLeaderInfoCol == null) {
					return false;
				}

				xbean.InstanceTaskInfo teamLeaderInfo = teamLeaderInfoCol
						.getInstinfo().get(
								teamLeaderInfoCol.getLastinstanceid());
				if (teamLeaderInfo == null)
					return false;

				long instanceTaskid = teamLeaderInfo.getInstanceonlyid();
				xbean.InstanceInfo instInfo = xtable.Instancetask.get(instanceTaskid);
				if (instInfo == null) {
					return false;
				}

				// 同步队员任务信息
				int instanceZoneId = teamLeaderInfoCol.getLastinstanceid();
				xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask
						.get(roleid);
				if (taskInfo == null) {
					taskInfo = xbean.Pod.newInstanceInfoCol();
					xtable.Roleinstancetask.insert(roleid, taskInfo);
				}
				
				final long currentTime = System.currentTimeMillis();
				if (!DateValidate.inTheSameDay(taskInfo.getAccepttime(), currentTime)) {
					taskInfo.setCounts(0);
					taskInfo.setAccepttime(currentTime);
				}

				xbean.InstanceTaskInfo instanceInfo = taskInfo.getInstinfo().get(instanceZoneId);
				if (instanceInfo == null) {
					instanceInfo = xbean.Pod.newInstanceTaskInfo();
					taskInfo.getInstinfo().put(instanceZoneId, instanceInfo);
				}
				taskInfo.setLastinstanceid(instanceZoneId);

				int defaultTimes = InstanceManager.getInstance()
						.getDayMaxTimes(roleid);
				taskInfo.setAccepttime(teamLeaderInfoCol.getAccepttime());
				if (!DateValidate.inTheSameDay(instanceInfo.getAccepttime(),
						teamLeaderInfo.getAccepttime())) {
					instanceInfo.setCounts(0); // 不在同一天则清0
					instanceInfo.setDefaulttimes(defaultTimes); // 设置一天的默认次数
					instanceInfo.setCurrentstep(0);
					instanceInfo.setCurrenttaskid(0);
				}

				instanceInfo.setAccepttime(teamLeaderInfo.getAccepttime());
				instanceInfo.setInstanceonlyid(teamLeaderInfo
						.getInstanceonlyid());
				instanceInfo.setCurrentstep(teamLeaderInfo.getCurrentstep());
				instanceInfo
						.setCurrenttaskid(teamLeaderInfo.getCurrenttaskid());

				// 0=没有任务 1=任务成功 2=放弃任务 3=任务异常结束 4=任务领取 5=任务超时
				instanceInfo.setInstanceflag(teamLeaderInfo.getInstanceflag());

				int questid = instInfo.getCurrenttaskid();
				refreshTaskState(roleid, questid, SpecialQuestState.UNDONE);
				instInfo.getRoleids().add(roleid);

				xbean.InstanceNpcInfo info = null;
				if (instInfo.getNpcs().size() > 0) {
					info = instInfo.getNpcs().get(0);
				}

				if (info == null) {
					InstanceManager.logger
							.info("Instance info == null   mission.roleids=:"
									+ instInfo.getRoleids() + "taskid:"
									+ questid);
					return false;
				}
				NpcModel npcModel = TaskDlgUtil.createNpcModel(
						info.getNpckey(), info.getNpcid(), info.getMapid(),
						info.getPosx(), info.getPosy());

				TaskDlgUtil.createAndRefreshTaskDlg(roleid, questid, npcModel,
						1, 1, SpecialQuestState.DONE,
						instanceInfo.getAccepttime() + InstanceManager.Time_Out
								- System.currentTimeMillis());

				return true;
			}
		}.call();
	}

	
	public boolean addStep1Task(xbean.InstanceInfo instanceInfo) {
		int currentTaskid = instanceInfo.getCurrenttaskid();
		xbean.InstanceNpcInfo info = null;
		if (instanceInfo.getNpcs().size() > 0) {
			info = instanceInfo.getNpcs().get(0);
		}

		if (info == null) {
			logger.info("Instance info == null   mission.roleids=:"
					+ instanceInfo.getRoleids() + "taskid:" + currentTaskid);
			return false;
		}
		NpcModel npcModel = TaskDlgUtil.createNpcModel(info.getNpckey(),
				info.getNpcid(), info.getMapid(), info.getPosx(),
				info.getPosy());

		// 加上任务追踪 刷新 具体活动的数据
		for (long roleId : instanceInfo.getRoleids()) {

			xbean.InstanceInfoCol instInfoCol = xtable.Roleinstancetask
					.get(roleId);
			if (instInfoCol == null) {
				return false;
			}

			xbean.InstanceTaskInfo instTaskInfo = instInfoCol.getInstinfo()
					.get(instanceInfo.getInstanceid());
			if (instTaskInfo == null) {
				return false;
			}

			instTaskInfo.setCurrentstep(instanceInfo.getCurrentstep());
			instTaskInfo.setCurrenttaskid(instanceInfo.getCurrenttaskid());

			Module.getInstance().trackAccpetMission(roleId, currentTaskid);
			TaskDlgUtil.createAndRefreshTaskDlg(
					roleId,
					currentTaskid,
					npcModel,
					1,
					1,
					SpecialQuestState.DONE,
					instanceInfo.getAccepttime() + Time_Out
							- System.currentTimeMillis());
			logger.debug("Instance: receive mission.roleid:" + roleId + "taskid:"
					+ currentTaskid);
		}
		return true;
	}

	
	public void refreshTaskState(long roleid, int taskid, int state) {
		// 取消任务追踪
		if (state == SpecialQuestState.SUCCESS
				|| state == SpecialQuestState.ABANDONED
				|| state == SpecialQuestState.FAIL) {
			TaskDlgUtil.refreshTaskState(roleid, taskid, state);
		}
		final SRefreshSpecialQuestState sendstate = new SRefreshSpecialQuestState();
		sendstate.questid = taskid;
		sendstate.state = state;
		mkdb.Procedure.psendWhileCommit(roleid, sendstate);
	}

	public int getDayMaxTimes(long roleid) {
		return ZU_DUI_FU_BEN_TIMES;
	}

	
	public void addDynamicServices(long roleid, long npckey, int npcid,
			ArrayList<Integer> result) {
		if (npcid != ZU_DUI_FU_BEN) {
			return;
		}
		xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.select(roleid);
		if (infoCol == null)
			return;

		xbean.InstanceTaskInfo info = infoCol.getInstinfo().get(
				infoCol.getLastinstanceid());

		// 0=没有任务 1=任务成功 2=放弃任务 3=任务异常结束 4=任务领取 5=任务超时
		if (info == null || info.getInstanceflag() <= 2) {
			result.add(ZU_DUI_FU_BEN_SERVICE);
			return;
		}
		if (info.getInstanceflag() >= 4) {
			result.add(ZU_DUI_FU_BEN_BACK_SERVICE);
		}
	}

	
	public int getGradeByRound(int round) {
		for (SLandPingJi config : instancePingjiList) {
			if (config.getMinround() <= round && config.getMaxround() >= round) {
				return config.getId();
			}
		}
		return 4;
	}

	
	public int getAdditionExp(int id) {
		for (SLandPingJi config : instancePingjiList) {
			if (id == config.getId()) {
				return config.getExppersent();
			}
		}
		return 6;
	}

	public int getFanPaiId(final int instanceId, final int grade) {
		SLandTask mission = instanceInfo.get(instanceId);
		if (mission == null)
			return 0;
		if (grade > mission.fanpailist.size())
			return 0;

		return mission.fanpailist.get(grade - 1);
	}

	
	public boolean createInstanceNpc(xbean.InstanceInfo instanceInfo,
			final List<SLandTaskConfig> taskList, final long ownerid) {
		// 刷出npc 先删除原先的Npc
		for (xbean.InstanceNpcInfo npcInfo : instanceInfo.getNpcs()) {
			SceneNpcManager.premoveNpcWhileCommit(npcInfo.getNpckey());
		}
		
		for (xbean.InstanceNpcInfo npcInfo :instanceInfo.getAsistnpcs()) {
			SceneNpcManager.premoveNpcWhileCommit(npcInfo.getNpckey());
		}

		instanceInfo.getNpcs().clear();
		// create 出来战斗npc
		for (SLandTaskConfig config : taskList) {
			int npcId = config.getSolonpcid();
			int leftTopx = config.getLefttopx() * 16;
			int leftTopy = config.getLefttopy() * 16;
			int mapId = config.getMapid();

			DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(
					mapId, ownerid);
			if (ds == null)
				return false;

			long npcKey = SceneNpcManager.getInstance().getNextId();
			SceneNpcManager.createNpcByPos(npcKey, npcId, "", ds.getSceneID(),
					leftTopx, leftTopy, 4, 0);

			xbean.InstanceNpcInfo npcInfo = xbean.Pod.newInstanceNpcInfo();
			npcInfo.setNpckey(npcKey);
			npcInfo.setNpcid(npcId);
			npcInfo.setMapid(config.getMapid());
			npcInfo.setPosx(config.getLefttopx());
			npcInfo.setPosy(config.getLefttopy());
			instanceInfo.getNpcs().add(npcInfo);
		}

		// create出来辅助npc
		instanceInfo.getAsistnpcs().clear();
		for (SLandTaskConfig config : taskList) {
			int npcId1 = config.getDecoratenpc1();
			int pos1x = config.getX1();
			int pos1y = config.getY1();

			int npcId2 = config.getDecoratenpc2();
			int pos2x = config.getX2();
			int pos2y = config.getY2();

			int npcId3 = config.getDecoratenpc3();
			int pos3x = config.getX3();
			int pos3y = config.getY3();

			int mapId = config.getMapid();

			DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(
					mapId, ownerid);
			if (ds == null)
				return false;

			if (npcId1 > 0) {
				long npcKey = SceneNpcManager.getInstance().getNextId();
				SceneNpcManager.createNpcByPos(npcKey, npcId1, "",
						ds.getSceneID(), pos1x * 16, pos1y * 16, 4, 0);
				xbean.InstanceNpcInfo npcInfo = xbean.Pod.newInstanceNpcInfo();
				npcInfo.setNpckey(npcKey);
				npcInfo.setNpcid(npcId1);
				npcInfo.setMapid(config.getMapid());
				npcInfo.setPosx(pos1x);
				npcInfo.setPosy(pos1y);
				instanceInfo.getAsistnpcs().add(npcInfo);
			}

			if (npcId2 > 0) {
				long npcKey = SceneNpcManager.getInstance().getNextId();
				SceneNpcManager.createNpcByPos(npcKey, npcId2, "",
						ds.getSceneID(), pos2x * 16, pos2y * 16, 4, 0);
				xbean.InstanceNpcInfo npcInfo = xbean.Pod.newInstanceNpcInfo();
				npcInfo.setNpckey(npcKey);
				npcInfo.setNpcid(npcId2);
				npcInfo.setMapid(config.getMapid());
				npcInfo.setPosx(pos2x);
				npcInfo.setPosy(pos2y);
				instanceInfo.getAsistnpcs().add(npcInfo);
			}

			if (npcId3 > 0) {
				long npcKey = SceneNpcManager.getInstance().getNextId();
				SceneNpcManager.createNpcByPos(npcKey, npcId3, "",
						ds.getSceneID(), pos3x * 16, pos3y * 16, 4, 0);
				xbean.InstanceNpcInfo npcInfo = xbean.Pod.newInstanceNpcInfo();
				npcInfo.setNpckey(npcKey);
				npcInfo.setNpcid(npcId3);
				npcInfo.setMapid(config.getMapid());
				npcInfo.setPosx(pos3x);
				npcInfo.setPosy(pos3y);
				instanceInfo.getAsistnpcs().add(npcInfo);
			}
		}
		return true;
	}

	public boolean createInstanceNpc(xbean.InstanceInfo instanceInfo,
			int instanceId, int currentStep, long ownerid) {

		List<SLandTaskConfig> taskList = InstanceManager.getInstance()
				.getStepTask(instanceId, currentStep);
		if (taskList.size() == 0) {
			InstanceManager.logger.info("WJL:Exception  没有找到任务 taskType ="
					+ instanceId + "   数量为0");
			return false;
		}
		createInstanceNpc(instanceInfo, taskList, ownerid);
		return true;
	}

	
	public void doNextStep(xbean.InstanceInfo instInfo, final int serviceId,
			final int instanceId, final long instanceOnlyId) {
		int step = instInfo.getCurrentstep();

		ServiceConfig serviceConfig = InstanceManager.getInstance()
				.getServiceConfig(serviceId);
		if (serviceConfig == null)
			return;

		Integer subsave = instInfo.getSubsaves().get(step);
		if (subsave == null)
			subsave = 0;

		subsave = Math.min(serviceConfig.times, subsave + 1);
		instInfo.getSubsaves().put(step, subsave);

		if (subsave >= serviceConfig.times) {
			for (xbean.InstanceNpcInfo info : instInfo.getAsistnpcs()) {
				if (info.getNpckey() > 0) {
					SceneNpcManager.premoveNpcWhileCommit(info.getNpckey());
				}
			}
			instInfo.getAsistnpcs().clear();
		}

			List<SLandTaskConfig> stepTaskList = InstanceManager
					.getInstance().getStepTask(instInfo.getInstanceid(),
							step + 1);
			if (stepTaskList.size() == 0) {
				for (xbean.InstanceNpcInfo npcInfo : instInfo.getNpcs()) {
					SceneNpcManager.premoveNpcWhileCommit(npcInfo.getNpckey());
				}
				InstanceEnd(instInfo, instanceId, instanceOnlyId, 1,
						SpecialQuestState.SUCCESS);
			} else {
				// 本次完成
				instanceEndOneStep(instInfo, SpecialQuestState.SUCCESS);
				InstanceManager.getInstance().createInstanceNpc(instInfo,
						stepTaskList, instanceOnlyId);
				SLandTaskConfig config = stepTaskList.get(0);
				instInfo.setCurrentstep(step + 1);
				instInfo.setCurrenttaskid(config.getTaskid());
				instInfo.setCurrentstate(SpecialQuestState.UNDONE);

				InstanceManager.getInstance().addStep1Task(instInfo);
			}
	}

	
	private void InstanceEnd(xbean.InstanceInfo instInfo, final int instanceId,
			final long instanceOnlyId, final int taskState, final int state) {
		Collection<Long> roleids = instInfo.getRoleids();
		SLandTask taskCofig = InstanceManager.getInstance()
				.getInstanceTask(instanceId);

		for (long roleid : roleids) {
			xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask
					.get(roleid);
			if (taskInfo != null) {
				xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(
						instanceId);
				if (info != null) {
					info.setInstanceflag(taskState);
					info.setCounts(info.getCounts() + 1);
					taskInfo.setCounts(taskInfo.getCounts() + 1);
					if (info.getCounts() < taskCofig.getAwardtime()) {
						info.setState(0);
					} else
						info.setState(2);
				}

				InstanceManager.getInstance().refreshTaskState(roleid,
						instInfo.getCurrenttaskid(), state);
			}

			// 战胜后 给活跃度
			RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
			if (actrole != null)
				actrole.handleActiveLivenessData(taskCofig.getActivitytypeid());
		}

		// 副本任务结束
		if (taskState == 1) {
			for (Long rid : roleids) {
				pingfen(instInfo, rid, instanceId);
			}
		}

		mkdb.Mkdb.executor().schedule(new InstanceEndTimer(instanceOnlyId), 30,
				TimeUnit.SECONDS);
	}

	
	public void pingfen(xbean.InstanceInfo instInfo, final long roleId,
			final int instanceId) {
		SLandTask taskCofig = InstanceManager.getInstance()
				.getInstanceTask(instanceId);
		if (taskCofig == null)
			return;
		
		xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.select(roleId);
		if (infoCol == null)
			return;

		xbean.InstanceTaskInfo info = infoCol.getInstinfo().get(
				infoCol.getLastinstanceid());
		if (info == null)
			return;
		
		if (info.getCounts() > taskCofig.getAwardtime()) {
			return;
		}

		int totalRound = instInfo.getFinalcounts();
		int grade = InstanceManager.getInstance().getGradeByRound(totalRound);
		if (grade < 1) {
			logger.error("grade is null");
			return;
		}

		int persent = InstanceManager.getInstance().getAdditionExp(grade);
		int fanPaiId = InstanceManager.getInstance().getFanPaiId(
				instInfo.getInstanceid(), grade);
		if (fanPaiId == 0) {
			return;
		}

		boolean send = new PReqCardProc(roleId, fanPaiId, 4).call();
		if (send) {
			int expPercent = (info.getTotalexp() * persent) / 100;
			new PAddExpProc(roleId, expPercent, PAddExpProc.OTHER_QUEST,
					"最后经验加成").call();

			SPingJi pingji = new SPingJi();
			pingji.exp = expPercent;
			pingji.grade = (byte) grade;
			mkdb.Procedure.psendWhileCommit(roleId, pingji);
		}
	}

	
	private void instanceEndOneStep(xbean.InstanceInfo instInfo, int state) {
		instInfo.setCurrentstate(state);
		Collection<Long> roleids = instInfo.getRoleids();
		for (long roleid : roleids) {
			InstanceManager.getInstance().refreshTaskState(roleid,
					instInfo.getCurrenttaskid(), state);
		}
	}

	
	public void instanceStart(final long startTime, final long endTime,
			final int activityId, final int baseId) {
		for (SLandTask mission : instanceInfo.values()) {
			if (mission.activityid == activityId) {
				instanceSet(mission.instancezoneid,
						xbean.InstanceActivity.STATE_START, startTime, endTime,
						baseId);
				break;
			}
		}
		// 公告处理 TODO
	}

	
	public void instanceSet(final int instanceZoneId, final int state,
			final long starttime, final long endtime, final int baseid) {
		new Procedure() {
			protected boolean process() {
				xbean.InstanceActivity instanceAct = xtable.Instanceactivitys
						.get(instanceZoneId);
				if (instanceAct == null) {
					instanceAct = xbean.Pod.newInstanceActivity();
					xtable.Instanceactivitys
							.insert(instanceZoneId, instanceAct);
				}

				instanceAct.setInstanceid(instanceZoneId);
				instanceAct.setState(state);

				instanceAct.setStarttime(starttime);
				instanceAct.setEndtime(endtime);
				instanceAct.setBaseid(baseid);

				return true;
			}
		}.call();
	}

	
	public boolean getInstanceSet(final int instanceZoneId) {
		xbean.InstanceActivity instanceAct = xtable.Instanceactivitys
				.select(instanceZoneId);
		if (instanceAct == null) {
			return false;
		}

		if (instanceAct.getState() == xbean.InstanceActivity.STATE_START) {
			long nowTime = System.currentTimeMillis();
			if (nowTime > instanceAct.getStarttime()
					&& nowTime < instanceAct.getEndtime()) {
				return true;
			}
		}

		return false;
	}

	
	public void instanceStop(int activityId) {
		for (SLandTask mission : instanceInfo.values()) {
			if (mission.activityid == activityId) {
				instanceSet(mission.instancezoneid,
						xbean.InstanceActivity.STATE_END, 0, 0, 0);
				break;
			}
		}
	}

	
	public void initInstanceInfo(final int instanceZoneId, final int turnType,
			final int state) {
		if (state == xbean.InstanceActivity.STATE_START) {
			if (turnType == 2) { // 周轮换
				long startTime = System.currentTimeMillis();
				long endTime = CalendarUtil.getEndTimeByStartTime(7);
				instanceSet(instanceZoneId, state, startTime, endTime, 0);
			} else if (turnType == 1) { // 日轮换
				long startTime = System.currentTimeMillis();
				long endTime = CalendarUtil.getEndTimeByStartTime(1);
				instanceSet(instanceZoneId, state, startTime, endTime, 0);
			}
		} else {
			instanceSet(instanceZoneId, state, 0, 0, 0);
		}
	}

	
	public void setInstanceInfo(final int turnType, final int setType) {
		new Procedure() {
			protected boolean process() {
				for (TurnClass turnClass : instTurnConfigs.values()) {
					if (turnClass.turntype != turnType) {
						continue;
					}

					xbean.InstanceTurnGroup instanceTurn = xtable.Instanceturngroups
							.get(turnClass.groupid);
					if (instanceTurn == null) {
						instanceTurn = xbean.Pod.newInstanceTurnGroup();
						instanceTurn.setTurngroup(turnClass.groupid);

						TurnGroup group = turnClass.groupList.get(0);
						instanceTurn.setTurntype(group.turnType);
						instanceTurn.setTurnindex(group.turnId);

						xtable.Instanceturngroups.insert(turnClass.groupid,
								instanceTurn);
						// 设置第一个副本为开启
						initInstanceInfo(group.instanceId, turnType,
								xbean.InstanceActivity.STATE_START);

						// 设置其他副本为未开启
						for (int i = 1; i < turnClass.groupList.size(); i++) {
							TurnGroup gp = turnClass.groupList.get(i);
							initInstanceInfo(gp.instanceId, turnType,
									xbean.InstanceActivity.STATE_UNSTART);
						}
					} else if (setType == 1) {
						int nIndex = instanceTurn.getTurnindex() - 1;
						if (!turnClass.groupList.contains(nIndex))
							nIndex = 0;
						
						TurnGroup group = turnClass.groupList.get(nIndex);
						// 判断当前副本是否结束
						xbean.InstanceActivity instanceAct = xtable.Instanceactivitys
								.select(group.id);
						if (instanceAct == null)
							continue;
						
						long nowTime = System.currentTimeMillis();
						if (nowTime > instanceAct.getStarttime()
								&& nowTime < instanceAct.getEndtime()) {
							// 副本还没有结束
							return false;
						}
						// 设置当前副本为结束
						initInstanceInfo(group.instanceId, turnType,
								xbean.InstanceActivity.STATE_END);

						nIndex++;
						if (nIndex == turnClass.groupList.size()) {
							nIndex = 0;
						}
						group = turnClass.groupList.get(nIndex);
						instanceTurn.setTurntype(group.turnType);
						instanceTurn.setTurnindex(group.turnId);
						// 设置下一个副本为开启
						initInstanceInfo(group.instanceId, turnType,
								xbean.InstanceActivity.STATE_START);
					}
				}
				return true;
			}
		}.call();
	}

	
	public List<ActivityInfo> getInstanceTurnState(final long roleid) {
		List<ActivityInfo> instanceStateMap = new ArrayList<ActivityInfo>();
		xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.select(roleid);
		// int allTimes = getDayMaxTimes(roleid);
		for (SLandTask mission : instanceInfo.values()) {
			if (mission.turntype == 2) {
				int instanceId = mission.instancezoneid;
				int finishTimes = 0;
				int instanceState = 0;
				int state = 0;
				// long startTime = 0;
				// long endTime = 0;
				int activityid = 0;
				int nextid = 0;
				int nextnextid = 0;

				TurnClass turnClass = instTurnConfigs.get(mission.getTurngroup());
				xbean.InstanceTurnGroup instanceTurn = xtable.Instanceturngroups
						.get(turnClass.groupid);
				int nIndex = instanceTurn.getTurnindex() - 1;
				TurnGroup group = turnClass.groupList.get(nIndex);
				activityid = group.activitytypeid;
				nIndex++;
				if (nIndex == turnClass.groupList.size()) {
					nIndex = 0;
				}
				group = turnClass.groupList.get(nIndex);
				nextid = group.activitytypeid;

				nIndex++;
				if (nIndex == turnClass.groupList.size()) {
					nIndex = 0;
				}
				group = turnClass.groupList.get(nIndex);
				nextnextid = group.activitytypeid;

				xbean.InstanceActivity instanceAct = xtable.Instanceactivitys
						.select(mission.instancezoneid);
				if (instanceAct != null) {
					// startTime = instanceAct.getStarttime();
					// endTime = instanceAct.getEndtime();
					instanceState = instanceAct.getState();
				}
				if (taskInfo != null) {
					xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(instanceId);
					if (info != null
							&& DateValidate.inTheSameDay(info.getAccepttime(),
									System.currentTimeMillis())) {
						finishTimes = info.getCounts();
						state = info.getState();
					}
				}

				ActivityInfo instance = new ActivityInfo(activityid, state,
						instanceState, finishTimes, nextid, nextnextid);
				instanceStateMap.add(instance);
			}
		}

		return instanceStateMap;
	}

	
	public Map<Integer, InstanceInfo> getInstanceState(final long roleid) {
		Map<Integer, InstanceInfo> instanceStateMap = new HashMap<Integer, InstanceInfo>();
		xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.get(roleid);
		int allTimes = getDayMaxTimes(roleid);
		for (SLandTask mission : instanceInfo.values()) {
			int instanceId = mission.instancezoneid;
			int finishTimes = 0;
			int instanceState = 0;
			int state = 0;
			long startTime = 0;
			long endTime = 0;

			xbean.InstanceActivity instanceAct = xtable.Instanceactivitys
					.select(mission.instancezoneid);
			if (instanceAct != null) {
				startTime = instanceAct.getStarttime();
				endTime = instanceAct.getEndtime();
				instanceState = instanceAct.getState();
			}
			if (taskInfo != null) {
				xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(
						instanceId);
				if (info != null
						&& DateValidate.inTheSameDay(info.getAccepttime(),
								System.currentTimeMillis())) {
					finishTimes = info.getCounts();
					state = info.getState();
				}
			}

			InstanceInfo instance = new InstanceInfo(mission.id, instanceId,
					state, instanceState, startTime, endTime, finishTimes,
					allTimes);

			instanceStateMap.put(mission.id, instance);
		}

		return instanceStateMap;
	}

	@Override
	public void onEvent(Event e) {
		long rid = e.getRoleid();

		if (e instanceof LeaveTeamSpecialQuestEvent) {
			Role memberRole = RoleManager.getInstance().getRoleByID(rid);
			if (memberRole == null)
				return;
			
			MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(memberRole.getMapId());
			if (cfg == null) {
				return;
			}
			
			if (cfg.dynamic == 0)
				return;
			
			abandonInstanceTask(rid);
		} else if (e instanceof ArriveTeamSpecialQuestEvent) {
			Role memberRole = RoleManager.getInstance().getRoleByID(rid);
			if (memberRole == null)
				return;
			
			MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(memberRole.getMapId());
			if (cfg == null) {
				return;
			}
			
			if (cfg.dynamic == 0)
				return;
			ArriveTeamSpecialQuestEvent teamEvent = (ArriveTeamSpecialQuestEvent) e;
			final long leadreId = teamEvent.getRoleid();
			final long applierId = teamEvent.getApplierRoleId();

			syncInstanceTask(applierId, leadreId);
		}
	}
	
	
	public static boolean visitNpc(long roleId, long npckey, SNpcShare npcshare) {
		// 检查npc身上的服务
		if (mkdb.Transaction.current() != null) {
			new PSendInstNpcService(npcshare.getId(), npckey, roleId).call();
		}
		else {
			new PSendInstNpcService(npcshare.getId(), npckey, roleId).submit();
		}
		return true;
	}
	
	public void sendAllQuest(long roleid) {
		xbean.InstanceInfoCol taskInfoCol = xtable.Roleinstancetask.get(roleid);
		if (taskInfoCol == null)
			return;
		xbean.InstanceTaskInfo taskInfo = taskInfoCol.getInstinfo().get(taskInfoCol.getLastinstanceid());
		if (taskInfo == null)
			return;
		
		xbean.InstanceInfo instInfo = xtable.Instancetask.get(taskInfo.getInstanceonlyid());
		if (instInfo == null)
			return;
		
		xbean.InstanceNpcInfo info = null;
		if (instInfo.getNpcs().size() > 0) {
			info = instInfo.getNpcs().get(0);
		}

		if (info == null) {
			logger.info("Instance info == null   mission.roleids=:"
					+ roleid + "taskid:" + instInfo.getCurrenttaskid());
			return;
		}
		
		NpcModel npcModel = TaskDlgUtil.createNpcModel(info.getNpckey(),
				info.getNpcid(), info.getMapid(), info.getPosx(),
				info.getPosy());
		
		Module.getInstance().trackAccpetMission(roleid, instInfo.getCurrenttaskid());
		TaskDlgUtil.createAndRefreshTaskDlg(
				roleid,
				instInfo.getCurrenttaskid(),
				npcModel,
				1,
				1,
				instInfo.getAccepttime() + Time_Out
						- System.currentTimeMillis());
	}

}
