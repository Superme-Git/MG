package fire.pb.mission.instance.line;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.log.Logger;
import fire.pb.PropRole;
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
import fire.pb.mission.LineInfo;
import fire.pb.mission.Module;
import fire.pb.mission.SLineTask;
import fire.pb.mission.SLineTaskConfig;
import fire.pb.mission.SLineTaskFight;
import fire.pb.mission.SLinepingji;
import fire.pb.npc.SNpcShare;
import fire.pb.npc.SPingJi;
import fire.pb.scene.DynamicScene;
import fire.pb.scene.SceneManager;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.DateValidate;
import fire.pb.util.Misc;
import fire.pb.util.TaskDlgUtil;
import fire.pb.util.TaskDlgUtil.NpcModel;
import mkdb.Lockeys;

public class LineInstManager implements EventHandler {


	static class SingletonHolder {
		static LineInstManager singleton = new LineInstManager();
	}

	public static LineInstManager getInstance() {
		return SingletonHolder.singleton;
	}

	public static final Logger logger = Logger.getLogger("INSTANCEZONE");

	public static final long Time_Out = 2 * 60 * 60 * 1000;

	public static final long ASK_TIME = 20;
	public static final long DESTROY_INSTANCE = 5;

	// 回到主城地图
	public static final int ZILUOLAN_MAP = 1615;
	public static final int ZILUOLAN_POS_X = 136;
	public static final int ZILUOLAN_POS_Y = 119;

	public static final int MIN_ROLE_SIZE = 3;
	
	public static final int CANNOTDOIT = 0;
	public static final int CANDOIT = 1;
	public static final int ITDONE = 2;

	private Map<Integer, SLineTask> instanceInfo = new HashMap<Integer, SLineTask>();
	private Map<Integer, List<SLineTaskConfig>> configs = new HashMap<Integer, List<SLineTaskConfig>>();
	private Map<Integer, SLineTaskFight> fightConfigs = new HashMap<Integer, SLineTaskFight>();
	private List<Integer> instanceTaskIdList = new ArrayList<Integer>();
	
	private List<Grade> instGradeInfo = new ArrayList<Grade>();
	private List<Integer> npcTaskList = new ArrayList<Integer>();

	// 副本对应的地图信息 key 副本id, value 地图list
	public Map<Integer, List<Integer>> instanceToMapList = new HashMap<Integer, List<Integer>>();

	// 副本询问定时器
	public static Map<Long, ScheduledFuture<?>> askInstFuture = new ConcurrentHashMap<Long, ScheduledFuture<?>>();

	
	public void init() {
		loadData();
		fire.pb.event.Poster.getPoster().listenEvent(this,
				fire.pb.event.LeaveTeamSpecialQuestEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this,
				fire.pb.event.ArriveTeamSpecialQuestEvent.class);
	}

	private LineInstManager() {
	}

	public void loadData() {
		configs.clear();
		instanceInfo.clear();
		instGradeInfo.clear();

		// 读取任务配置
		Map<Integer, SLineTaskConfig> map = fire.pb.main.ConfigManager
				.getInstance().getConf(SLineTaskConfig.class);
		if (map != null) {
			for (SLineTaskConfig config : map.values()) {
				int taskType = config.getTasktype();
				List<SLineTaskConfig> taskList = configs.get(taskType);
				if (taskList == null) {
					taskList = new ArrayList<SLineTaskConfig>();
					configs.put(taskType, taskList);
				}
				taskList.add(config);
				npcTaskList.add(config.solonpcid);

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

		Map<Integer, SLineTaskFight> tmp = fire.pb.main.ConfigManager.getInstance().getConf(SLineTaskFight.class);
		if (tmp != null) {
			fightConfigs.putAll(tmp);
		}

		Map<Integer, SLineTask> instanceTask = fire.pb.main.ConfigManager.getInstance().getConf(SLineTask.class);
		if (instanceTask == null) {
			return;
		}
		
		for (SLineTask linetask : instanceTask.values()) {
			instanceInfo.put(linetask.id, linetask);
			
			if (linetask.grade1 != null) {
				String [] str1 = linetask.grade1.split(";");
				if (str1.length == 2) {
					Grade grade = new Grade(1, Integer.valueOf(str1[0]), Integer.valueOf(str1[1]));
					instGradeInfo.add(grade);
				}
			}
			
			if (linetask.grade2 != null) {
				String [] str2 = linetask.grade2.split(";");
				if (str2.length == 2) {
					Grade grade = new Grade(2, Integer.valueOf(str2[0]), Integer.valueOf(str2[1]));
					instGradeInfo.add(grade);
				}
			}
			
			if (linetask.grade3 != null) {
				String [] str3 = linetask.grade3.split(";");
				if (str3.length == 2) {
					Grade grade = new Grade(3, Integer.valueOf(str3[0]), Integer.valueOf(str3[1]));
					instGradeInfo.add(grade);
				}
			}
			
			if (linetask.grade4 != null) {
				String [] str4 = linetask.grade4.split(";");
				if (str4.length == 2) {
					Grade grade = new Grade(4, Integer.valueOf(str4[0]), Integer.valueOf(str4[1]));
					instGradeInfo.add(grade);
				}
			}
		}

	}

	public List<Integer> getInstanceMapId(final int taskType) {
		if (instanceToMapList.containsKey(taskType)) {
			return instanceToMapList.get(taskType);
		}
		return null;
	}

	
	public int getTaskId(int taskType, int step, int order) {
		List<SLineTaskConfig> taskList = configs.get(taskType);
		int taskId = -1;
		if (taskList != null) {
			int index = 0;
			for (SLineTaskConfig config : taskList) {
				if (config.getStep() == step) {
					if (index == order) {
						taskId = config.getTaskid();
						break;
					}
					index++;
				}
			}
			SLineTaskConfig config = taskList.get(order);
			return config.getTaskid();
		}
		return taskId;
	}

	
	public SLineTaskConfig getTaskConfig(int taskType, int step, int order) {
		SLineTaskConfig config = null;
		List<SLineTaskConfig> configs = getStepTask(taskType, step);
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
		List<SLineTaskConfig> taskList = configs.get(taskType);
		int size = 0;
		if (taskList != null) {
			for (SLineTaskConfig config : taskList) {
				if (config.getStep() == step) {
					size++;
				}
			}
		}
		return size;
	}

	public List<SLineTaskConfig> getStepTask(int taskType, int step) {
		List<SLineTaskConfig> taskList = configs.get(taskType);
		List<SLineTaskConfig> stepTaskList = new ArrayList<SLineTaskConfig>();
		for (SLineTaskConfig config : taskList) {
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
		SLineTaskFight cofig = fightConfigs.get(serviceId);
		if (cofig != null) {
			return cofig.getFightid();
		}
		return -1;
	}
	
	
	public SLineTaskFight getFightConfig(final int serviceId) {
		SLineTaskFight cofig = fightConfigs.get(serviceId);
		if (cofig != null) {
			return cofig;
		}
		return null;
	}

	
	public int getInstanceIdByServiceId(final int serviceId) {
		SLineTaskFight cofig = fightConfigs.get(serviceId);
		if (cofig != null) {
			return cofig.getInstancezoneid();
		}
		return -1;
	}

	
	public int getAwardId(int serviceId) {
		SLineTaskFight cofig = fightConfigs.get(serviceId);
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
		List<SLineTaskConfig> taskList = configs.get(taskType);
		if (taskList != null) {
			for (SLineTaskConfig config : taskList) {
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

	
	public void abandonInstanceTask(final long roleid) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				Team team = TeamManager.getTeamByRoleId(roleid);
				if (team != null) {
					List<Long> allRoleIDs = new ArrayList<Long>();
					allRoleIDs.addAll(team.getNormalMemberIds());
					Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoleIDs));
				}
				
				xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.get(roleid);
				if (infoCol == null) {
					return false;
				}

				xbean.InstanceTaskInfo info = infoCol.getInstinfo().get(infoCol.getLastinstanceid());
				if (info == null)
					return false;

				long instanceTaskid = info.getInstanceonlyid();
				xbean.InstanceInfo instInfo = xtable.Instancetask.get(instanceTaskid);
				if (instInfo == null) {
					return false;
				}
				
				// 清理一些数据
				final int questid = instInfo.getCurrenttaskid();
				LineInstManager.logger.info(new StringBuilder().append("abandonInstanceTask:").append(roleid)
						.append(",teamleaderid=:").append(instInfo.getOwnerid())
						.append(",roleids=:").append(instInfo.getRoleids()).append(",taskid:").append(questid));
				
				if (team != null) {
					if (!team.isTeamLeader(roleid)) {
						instInfo.getRoleids().remove(Long.valueOf(roleid));
					}
				}
				else {
					instInfo.getRoleids().remove(Long.valueOf(roleid));
				}
				
				info.setInstanceflag(2);

				// 如果在副本地图内,则不走放弃任务的逻辑
				// if (!mapList.contains(mapId)) {
				refreshTaskState(roleid, questid, SpecialQuestState.INSTANCE_ABANDONED);
				LineInstManager.logger.info(new StringBuilder().append("after abandonInstanceTask")
						.append(",teamleaderid=:").append(instInfo.getOwnerid())
						.append(",roleids=:").append(instInfo.getRoleids()).append(",taskid:").append(questid));

				return true;
			}
		}.call();
	}

	
	public void syncInstanceTask(final long roleid, final long teamleaderid) {
		mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				Team team = TeamManager.getTeamByRoleId(roleid);
				if (team == null) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,	145123, null);
					return false;
				}
				
				List<Long> allRoleIDs = new ArrayList<Long>();
				allRoleIDs.addAll(team.getNormalMemberIds());
				Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoleIDs));

				if (!team.isTeamLeader(teamleaderid)) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,	145123, null);
					return false;
				}

				xbean.InstanceInfoCol teamLeaderInfoCol = xtable.Roleinstancetask.get(teamleaderid);
				if (teamLeaderInfoCol == null) {
					return false;
				}

				xbean.InstanceTaskInfo teamLeaderInfo = teamLeaderInfoCol.getInstinfo().get(teamLeaderInfoCol.getLastinstanceid());
				if (teamLeaderInfo == null)
					return false;

				long instanceTaskid = teamLeaderInfo.getInstanceonlyid();
				xbean.InstanceInfo instInfo = xtable.Instancetask.get(instanceTaskid);
				if (instInfo == null) {
					return false;
				}

				// 同步队员任务信息
				int lineId = teamLeaderInfoCol.getLastinstanceid();
				xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.get(roleid);
				if (infoCol == null) {
					infoCol = xbean.Pod.newInstanceInfoCol();
					xtable.Roleinstancetask.insert(roleid, infoCol);
				}
				
				xbean.InstanceTaskInfo taskInfo = infoCol.getInstinfo().get(lineId);
				if (taskInfo == null) {
					taskInfo = xbean.Pod.newInstanceTaskInfo();
					infoCol.getInstinfo().put(lineId, taskInfo);
				}
				infoCol.setLastinstanceid(lineId);
				
				xbean.InstanceTimeAwardInfo awardInfo = infoCol.getInstcount().get(lineId);
				if (awardInfo == null) {
					awardInfo = xbean.Pod.newInstanceTimeAwardInfo();
					infoCol.getInstcount().put(lineId, awardInfo);
				}
				
				long currentTime = System.currentTimeMillis();
				if (!DateValidate.inTheSameDay(infoCol.getAccepttime(), currentTime)) {
					infoCol.setCounts(0);
					infoCol.setAccepttime(currentTime);
					infoCol.getInstcount().clear();
				}
				
				if (!DateValidate.inTheSameDay(taskInfo.getAccepttime(), currentTime)) {
					taskInfo.setCounts(0); // 不在同一天则清0
					taskInfo.setCurrentstep(1);
					taskInfo.setCurrenttaskid(0);
					taskInfo.getStepawardtimes().clear();
					taskInfo.setAccepttime(currentTime);
				}
				
				taskInfo.setInstanceonlyid(teamLeaderInfo.getInstanceonlyid());
				taskInfo.setCurrentstep(teamLeaderInfo.getCurrentstep());
				taskInfo.setCurrenttaskid(teamLeaderInfo.getCurrenttaskid());

				// 0=没有任务 1=任务成功 2=放弃任务 3=任务异常结束 4=任务领取 5=任务超时
				taskInfo.setInstanceflag(teamLeaderInfo.getInstanceflag());

				int questid = instInfo.getCurrenttaskid();
				refreshTaskState(roleid, questid, SpecialQuestState.UNDONE);
				instInfo.getRoleids().clear();
				instInfo.getRoleids().addAll(allRoleIDs);

				xbean.InstanceNpcInfo info = null;
				if (instInfo.getNpcs().size() > 0) {
					info = instInfo.getNpcs().get(0);
				}

				if (info == null) {
					LineInstManager.logger.info("Instance info == null   mission.roleids=:"
									+ instInfo.getRoleids() + "taskid:"
									+ questid);
					return false;
				}
				NpcModel npcModel = TaskDlgUtil.createNpcModel(
						info.getNpckey(), info.getNpcid(), info.getMapid(),
						info.getPosx(), info.getPosy());

				TaskDlgUtil.createAndRefreshTaskDlg(roleid, questid, npcModel,
						1, 1, SpecialQuestState.DONE,
						taskInfo.getAccepttime() + LineInstManager.Time_Out
								- System.currentTimeMillis());

				LineInstManager.logger.info(new StringBuilder().append("syncInstanceTask:").append(roleid)
						.append(",teamleaderid=:").append(instInfo.getOwnerid())
						.append(",teamleaderid=:").append(teamleaderid).append(",taskid:")
						.append(questid).append(",step:").append(teamLeaderInfo.getCurrentstep()));
				return true;
			}
		});
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

			xbean.InstanceInfoCol instInfoCol = xtable.Roleinstancetask.get(roleId);
			if (instInfoCol == null) {
				return false;
			}

			xbean.InstanceTaskInfo instTaskInfo = instInfoCol.getInstinfo().get(instanceInfo.getInstanceid());
			if (instTaskInfo == null) {
				return false;
			}

//			if (instanceInfo.getOwnerid() == roleId) {
//				instTaskInfo.setCurrentstep(instanceInfo.getCurrentstep());
//			}
			
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
				|| state == SpecialQuestState.FAIL
				|| state == SpecialQuestState.INSTANCE_ABANDONED) {
			TaskDlgUtil.refreshTaskState(roleid, taskid, state);
		}
		final SRefreshSpecialQuestState sendstate = new SRefreshSpecialQuestState();
		sendstate.questid = taskid;
		sendstate.state = state;
		mkdb.Procedure.psendWhileCommit(roleid, sendstate);
	}

	public boolean createInstanceNpc(xbean.InstanceInfo instanceInfo,
			final List<SLineTaskConfig> taskList, final long ownerid) {
		// 刷出npc 先删除原先的Npc
		for (xbean.InstanceNpcInfo npcInfo : instanceInfo.getNpcs()) {
			SceneNpcManager.premoveNpcWhileCommit(npcInfo.getNpckey());
		}
		
		for (xbean.InstanceNpcInfo npcInfo :instanceInfo.getAsistnpcs()) {
			SceneNpcManager.premoveNpcWhileCommit(npcInfo.getNpckey());
		}

		instanceInfo.getNpcs().clear();
		// create 出来战斗npc
		for (SLineTaskConfig config : taskList) {
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
		for (SLineTaskConfig config : taskList) {
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

		List<SLineTaskConfig> taskList = LineInstManager.getInstance()
				.getStepTask(instanceId, currentStep);
		if (taskList.size() == 0) {
			LineInstManager.logger.info("Exception  没有找到任务 taskType ="
					+ instanceId + "   数量为0");
			return false;
		}
		createInstanceNpc(instanceInfo, taskList, ownerid);
		return true;
	}

	
	public void doNextStep(xbean.InstanceInfo instInfo, final int instanceId, final long instanceOnlyId) {
		int step = instInfo.getCurrentstep();

//		Integer subsave = instInfo.getSubsaves().get(step);
//		if (subsave == null)
//			subsave = 0;

//		instInfo.getSubsaves().put(step, subsave);
		
		for (xbean.InstanceNpcInfo info : instInfo.getAsistnpcs()) {
			if (info.getNpckey() > 0) {
				SceneNpcManager.premoveNpcWhileCommit(info.getNpckey());
			}
		}
		
		instInfo.getAsistnpcs().clear();
		List<SLineTaskConfig> stepTaskList = LineInstManager.getInstance().getStepTask(instInfo.getInstanceid(), step + 1);
		if (stepTaskList.size() == 0) {
			for (xbean.InstanceNpcInfo npcInfo : instInfo.getNpcs()) {
				SceneNpcManager.premoveNpcWhileCommit(npcInfo.getNpckey());
			}
			InstanceEnd(instInfo, instanceId, instanceOnlyId, 1, SpecialQuestState.SUCCESS);
		} else {
			// 本次完成
			instanceEndOneStep(instInfo, SpecialQuestState.SUCCESS);
			LineInstManager.getInstance().createInstanceNpc(instInfo,
					stepTaskList, instanceOnlyId);
			SLineTaskConfig config = stepTaskList.get(0);
			instInfo.setCurrentstep(step + 1);
			instInfo.setCurrenttaskid(config.getTaskid());
			instInfo.setCurrentstate(SpecialQuestState.UNDONE);

			LineInstManager.getInstance().addStep1Task(instInfo);
		}
	}
	
	
	public void doNextStep(final int instanceId, final long instanceOnlyId) {
		mkdb.Procedure nextStep = new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.InstanceInfo instInfo = xtable.Instancetask.get(instanceOnlyId);
				if (instInfo == null) {
					return false;
				}
				
				int step = instInfo.getCurrentstep();

//				Integer subsave = instInfo.getSubsaves().get(step);
//				if (subsave == null)
//					subsave = 0;
//
//				instInfo.getSubsaves().put(step, subsave);
				
				for (xbean.InstanceNpcInfo info : instInfo.getAsistnpcs()) {
					if (info.getNpckey() > 0) {
						SceneNpcManager.premoveNpcWhileCommit(info.getNpckey());
					}
				}
				
				instInfo.getAsistnpcs().clear();
				
				for (Long rid : instInfo.getRoleids()) {
					TeamManager.getTeamByRoleId(rid);
					break;
				}
				Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, instInfo.getRoleids()));

				List<SLineTaskConfig> stepTaskList = LineInstManager.getInstance().getStepTask(instInfo.getInstanceid(), step + 1);
				if (stepTaskList.size() == 0) {
					for (xbean.InstanceNpcInfo npcInfo : instInfo.getNpcs()) {
						SceneNpcManager.premoveNpcWhileCommit(npcInfo.getNpckey());
					}
					InstanceEnd(instInfo, instanceId, instanceOnlyId, 1,
							SpecialQuestState.SUCCESS);
				} else {
					// 本次完成
					instanceEndOneStep(instInfo, SpecialQuestState.SUCCESS);
					LineInstManager.getInstance().createInstanceNpc(instInfo,
							stepTaskList, instanceOnlyId);
					SLineTaskConfig config = stepTaskList.get(0);
					instInfo.setCurrentstep(step + 1);
					instInfo.setCurrenttaskid(config.getTaskid());
					instInfo.setCurrentstate(SpecialQuestState.UNDONE);

					LineInstManager.getInstance().addStep1Task(instInfo);
				}
					
				return true;
			}
		};
		
		mkdb.Procedure.pexecuteWhileCommit(nextStep);
	}
	
	public SLineTask getInstanceTask(int taskType) {
		if (instanceInfo.containsKey(taskType))
			return instanceInfo.get(taskType);
		return null;
	}
	
	private void InstanceEnd(xbean.InstanceInfo instInfo, final int instanceId,
			final long instanceOnlyId, final int taskState, final int state) {
		Set<Long> roleids = instInfo.getRoleids();
		
		for (long roleid : roleids) {
			xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.get(roleid);
			if (taskInfo != null) {
				xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(instanceId);
				if (info != null) {
					info.setInstanceflag(taskState);
					info.setState(2);
					
					info.setCurrentstep(1);
				}

				LineInstManager.getInstance().refreshTaskState(roleid, instInfo.getCurrenttaskid(), state);
			}
		}
		
		fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(231);
		int commonCnt = Integer.parseInt(common.getValue());
		// 副本任务结束
		if (taskState == 1) {
			for (Long rid : roleids) {
				xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.select(rid);
				if (infoCol.getCounts() <= commonCnt && infoCol.getFanpai() == 1) {
					pingfen(instInfo, rid, instanceId);
				}
			}
		}
		
		mkdb.Mkdb.executor().schedule(new LineEndTimer(instanceOnlyId), 15,
				TimeUnit.SECONDS);
	}

	private void instanceEndOneStep(xbean.InstanceInfo instInfo, int state) {
		instInfo.setCurrentstate(state);
		Collection<Long> roleids = instInfo.getRoleids();
		for (long roleid : roleids) {
			LineInstManager.getInstance().refreshTaskState(roleid, 
					instInfo.getCurrenttaskid(), state);
		}
	}
	
	public Map<Integer, LineInfo> getLineState(final long roleid) {
		Map<Integer, LineInfo> lineStateMap = new HashMap<Integer, LineInfo>();
		xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.get(roleid);
		
		for (SLineTask mission : instanceInfo.values()) {
			int instanceId = mission.id;
			int state = 0;
			int finishTimes = 0;
			if (taskInfo != null) {
				if (mission.afterid == 0) {
					xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(instanceId);
					if (info == null)
						state = resetState(0, -1);
					else {
						state = resetState(info.getState(), -1);
						finishTimes = info.getCounts();
					}
				}
				else {
					xbean.InstanceTaskInfo afterInfo = taskInfo.getInstinfo().get(mission.afterid);
					if (afterInfo == null)
						state = resetState(0, 0);
					else {
						xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(instanceId);
						if (info == null) {
							state = resetState(0, afterInfo.getState());
						}
						else {
							state = resetState(info.getState(), afterInfo.getState());
							finishTimes = info.getCounts();
						}
					}
				}
			}
			else {
				if (mission.afterid == 0) {
					state = resetState(0, -1);
				}
				else {
					state = resetState(0, 0);
				}
			}
			
			LineInfo instance = new LineInfo(instanceId, state, finishTimes);
			lineStateMap.put(mission.id, instance);
		}

		return lineStateMap;
	}
	
	
	public int resetState(final int state, final int state2) {
		// 角色当天关于该副本的状态 0未完成, 1表示未开启(主要用于一次性副本) 2表示已经完成
		switch(state) {
		case 0:
		{
			if (state2 == 0)
				return 0;
			if (state2 == 2)
				return 1;
			return 1;
		}
		case 2:
		{
			return 2;
		}
		}
		
		//0 未开启; 1 可以进入, 2 已经完成
		return -1;
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
	
	
	public boolean checkLineNpc(int npcid) {
		if (npcTaskList.contains(npcid))
			return true;
		return false;
	}
	
	
	public static boolean visitNpc(long roleId, long npckey, SNpcShare npcshare) {
		// 检查npc身上的服务
		if (mkdb.Transaction.current() != null) {
			new PSendLineNpcService(npcshare.getId(), npckey, roleId).call();
		}
		else {
			new PSendLineNpcService(npcshare.getId(), npckey, roleId).submit();
		}
		return true;
	}
	
	
	public void pingfen(xbean.InstanceInfo instInfo, final long roleId,	final int instanceId) {
		SLineTask taskCofig = LineInstManager.getInstance().getInstanceTask(instanceId);
		if (taskCofig == null)
			return;
		
		xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.select(roleId);
		if (infoCol == null)
			return;

		xbean.InstanceTaskInfo info = infoCol.getInstinfo().get(infoCol.getLastinstanceid());
		if (info == null)
			return;

		int totalRound = instInfo.getFinalcounts();
		int grade = LineInstManager.getInstance().getGradeByRound(totalRound, instanceId);
		if (grade < 1) {
			logger.error("grade is null");
			return;
		}

//		int persent = LineInstManager.getInstance().getAdditionExp(grade);
		int fanPaiId = LineInstManager.getInstance().getFanPaiId(instanceId, grade);
		if (fanPaiId == 0) {
			return;
		}

		boolean send = new PReqCardProc(roleId, fanPaiId, 4).call();
		if (send) {
//			int expPercent = (info.getTotalexp() * persent) / 100;
//			new PAddExpProc(roleId, expPercent, PAddExpProc.OTHER_QUEST, "时光之穴").call();

			SPingJi pingji = new SPingJi();
			pingji.exp = 0;
			pingji.grade = (byte) grade;
			mkdb.Procedure.psendWhileCommit(roleId, pingji);
		}
	}
	
	
	public int getGradeByRound(int round) {
		Map<Integer, SLinepingji> pingji = fire.pb.main.ConfigManager.getInstance().getConf(SLinepingji.class);
		for (SLinepingji config : pingji.values()) {
			if (config.getMinround() <= round && config.getMaxround() >= round) {
				return config.getId();
			}
		}
		return 4;
	}
	
	public int getGradeByRound(int round, int instanceId) {
		for (Grade config : instGradeInfo) {
			if (config.getMinlevel() <= round && config.getMaxlevel() >= round) {
				return config.getGrade();
			}
		}
		return 4;
	}

	
	public int getAdditionExp(int id) {
		Map<Integer, SLinepingji> pingji = fire.pb.main.ConfigManager.getInstance().getConf(SLinepingji.class);
		for (SLinepingji config : pingji.values()) {
			if (id == config.getId()) {
				return config.getExppersent();
			}
		}
		return 6;
	}

	public int getFanPaiId(final int instanceId, final int grade) {
		SLineTask mission = instanceInfo.get(instanceId);
		if (mission == null)
			return 0;
		if (grade > mission.fanpailist.size())
			return 0;

		return mission.fanpailist.get(grade - 1);
	}
	
	/**
	 * 获取队长最小没有获取奖励的关数
	 * @param teamLeadreTaskInfo
	 * @return
	 */
	public static int getStep(xbean.InstanceInfoCol infoCol, final int lineId) {
		xbean.InstanceTimeAwardInfo awardInfo = infoCol.getInstcount().get(lineId);
		if (awardInfo == null)
			return 1;
		
		if (infoCol.getReset() == 1)
			return 1;
		
		fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(231);
		int commonCnt = Integer.parseInt(common.getValue());
		if (infoCol.getCounts() < commonCnt && awardInfo.getStepawardtimes().size() == 0)
			return 1;
		
		if (awardInfo.getStepawardtimes().size() == 0) {
			xbean.InstanceTaskInfo taskInfo = infoCol.getInstinfo().get(lineId);
			if (taskInfo == null)
				return 1;
			
			Integer minVal = Integer.MAX_VALUE;
			Integer minKey = 1;
			
			for (Map.Entry<Integer, Integer> atom : taskInfo.getStepawardtimes().entrySet()) {
				//找到最小奖励次数的关卡
				if (atom.getValue() < minVal) {
					minVal = atom.getValue();
					minKey = atom.getKey();
				}
			}
			return minKey;
		}
		else {
			//有部分关没有拿过奖励,找出最小的关数
			for (int i = 1; i < 6; i ++) {
				Integer atom = awardInfo.getStepawardtimes().get(i);
				if (atom == null || atom == 0)
					return i;
			}
		}
		
		return 1;
	}
	
	/**
	 * 验证进入副本的玩家的等级
	 * @param team
	 * @param lineId
	 * @param roleid
	 * @return
	 */
	public static boolean checkTask(Team team, final int lineId, Long roleid) {
		SLineTask taskCofig = LineInstManager.getInstance().getInstanceTask(lineId);
		if (taskCofig == null) {
			return false;
		}
		// 验证等级问题
		List<String> levelException = new ArrayList<String>();
		
		PropRole pRole = new PropRole(roleid, true);
		if (pRole.getLevel() < taskCofig.getMinlevel()) {
			levelException.add(pRole.getName());
		}
		
		if (!levelException.isEmpty()) {
			StringBuffer strParam = new StringBuffer();
			int nIndex = 0;
			for (String name : levelException) {
				if (nIndex != (levelException.size() - 1)) {
					strParam.append(name).append(",");
				} else {
					strParam.append(name);
				}
			}
			
			List<String> paras = new ArrayList<String>();
			paras.add(strParam.toString());
			paras.add(String.valueOf(taskCofig.getMinlevel()));
			for (Long mem : team.getAllMemberIds()) {
				fire.pb.talk.MessageMgr.sendMsgNotify(mem, 160102, paras);
			}
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * 判断玩家副本状态
	 * @param team
	 * @param roleid
	 * @param lineId
	 * @return
	 */
	public static boolean checkInstState(Team team, final long roleid, final int lineId) {
		SLineTask taskCofig = LineInstManager.getInstance().getInstanceTask(lineId);
		if (taskCofig == null) {
			return false;
		}
		
		xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.select(roleid);
		if (taskInfo == null) {
			taskInfo = xbean.Pod.newInstanceInfoCol();
			xtable.Roleinstancetask.insert(roleid, taskInfo);
		}

		xbean.InstanceTaskInfo instanceInfo = taskInfo.getInstinfo().get(lineId);
		if (instanceInfo == null) {
			instanceInfo = xbean.Pod.newInstanceTaskInfo();
			taskInfo.getInstinfo().put(lineId, instanceInfo);
		}
		
		if (taskCofig.afterid != 0) {
			xbean.InstanceTaskInfo afterTaskInfo = taskInfo.getInstinfo().get(taskCofig.afterid);
			if (afterTaskInfo == null || afterTaskInfo.getState() < 2) {
				SLineTask afterTaskCofig = LineInstManager.getInstance().getInstanceTask(taskCofig.afterid);
				if (afterTaskCofig != null) {
					List<String> para = new ArrayList<String>(3);
					PropRole pRole = new PropRole(roleid, true);
					para.add(pRole.getName());
					para.add(afterTaskCofig.taskname);
					para.add(taskCofig.taskname);
					for (Long mem : team.getNormalMemberIds()) {
						MessageMgr.psendMsgNotify(mem, 160439, para);
					}
				}
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 获取该副本没有打过的关卡
	 * @param roleid
	 * @param instid
	 * @return
	 */
	public static List<Integer> getUndoStep(final long roleid, final int instid) {
		SLineTask taskCofig = LineInstManager.getInstance().getInstanceTask(instid);
		if (taskCofig == null) {
			return null;
		}
		
		xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.select(roleid);
		if (infoCol == null) {
			return null;
		}
		
		xbean.InstanceTaskInfo taskInfo = infoCol.getInstinfo().get(instid);
		if (taskInfo == null) {
			return null;
		}
		
		long currentTime = System.currentTimeMillis();
		
		if (!DateValidate.inTheSameDay(infoCol.getAccepttime(), currentTime)) {
			return null;
		}
		if (!DateValidate.inTheSameDay(taskInfo.getAccepttime(), currentTime)) {
			return null;
		}
		
		xbean.InstanceTimeAwardInfo awardInfo = infoCol.getInstcount().get(instid);
		if (awardInfo == null) {
			return null;
		}
		List<Integer> undostep = new ArrayList<Integer>(5);
		
		for (Integer i = 1; i < taskCofig.stepnum + 1; i ++) {
			if (awardInfo.getStepawardtimes().size() != 0) {
				Integer val = awardInfo.getStepawardtimes().get(i);
				if (val == null)
					undostep.add(i);
				else if (val == 0)
					undostep.add(i);
			}
		}
		
		return undostep;
	}
	
	/**
	 * 检测该关卡是否能拿奖励
	 * @param roleid
	 * @return
	 */
	public static boolean checkAaward(final long roleid) {
		xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.get(roleid);
		if (infoCol == null) {
			return false;
		}

		xbean.InstanceTaskInfo instanceInfo = infoCol.getInstinfo().get(infoCol.getLastinstanceid());
		if (instanceInfo == null) {
			return false;
		}
		
		SLineTask taskCofig = LineInstManager.getInstance().getInstanceTask(infoCol.getLastinstanceid());
		if (taskCofig == null) {
			return false;
		}
		
		fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(231);
		int commonCnt = Integer.parseInt(common.getValue());
		
		xbean.InstanceTimeAwardInfo awardInfo = infoCol.getInstcount().get(infoCol.getLastinstanceid());
		if (awardInfo == null) {
			awardInfo = xbean.Pod.newInstanceTimeAwardInfo();
			infoCol.getInstcount().put(infoCol.getLastinstanceid(), awardInfo);
		}
		
		if ((infoCol.getCounts() < commonCnt && instanceInfo.getAwarding() == 1) || awardInfo.getStepawardtimes().size() > 0) {//奖励
			Integer awardStepTimes = awardInfo.getStepawardtimes().get(instanceInfo.getCurrentstep());
			if (awardStepTimes == null) {
				awardStepTimes = 0;
			}
			
			if (awardInfo.getStepawardtimes().size() != taskCofig.stepnum) {
				//还没有拿到所有关卡奖励
				if (awardStepTimes == 0) {
					return true;
				}
			}
		}
		
		return false;
	}

}
