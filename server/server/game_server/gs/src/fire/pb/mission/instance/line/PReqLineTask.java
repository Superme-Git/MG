package fire.pb.mission.instance.line;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.pb.PropRole;
import fire.pb.main.ConfigManager;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.mission.SAnswerInstance;
import fire.pb.mission.SAskIntoInstance;
import fire.pb.mission.SLineTask;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.DateValidate;
import mkdb.Procedure;


public class PReqLineTask extends Procedure {

	private final long roleid;
	private final int lineId;

	public PReqLineTask(long roleid, int taskId) {
		this.roleid = roleid;
		this.lineId = taskId;
	}
	
	@Override
	protected boolean process() throws Exception {
		SLineTask taskCofig = LineInstManager.getInstance().getInstanceTask(lineId);
		if (taskCofig == null) {
			LineInstManager.logger.error("副本数据为空 " + lineId);
			return false;
		}

		// 判断等级
		PropRole pRole = new PropRole(roleid, true);
		if (pRole.getLevel() < taskCofig.getMinlevel()) {
			List<String> paras = new ArrayList<String>(1);
			paras.add(String.valueOf(taskCofig.getMinlevel()));
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 160100,
					paras);
			return false;
		}
		
		//判断是否在队伍中
		Team team = TeamManager.getTeamByRoleId(roleid);
		Role mapRole = RoleManager.getInstance().getRoleByID(roleid);
		if (null == mapRole) {
			LineInstManager.logger.error("角色不在场景内," + lineId);
			return false;
		}
		
		if (mapRole.getMapId() != LineInstManager.ZILUOLAN_MAP) {
			LineInstManager.logger.error("角色所在地图有问题," + lineId);
			return false;
		}

		long nowtime = System.currentTimeMillis();
		List<Long> allRoleIDs = new ArrayList<Long>();
		if (taskCofig.getTeamnum() > 1) {
			// 需要组队
			if (team == null) {
				List<String> paras = new ArrayList<String>(1);
				paras.add(String.valueOf(taskCofig.getTeamnum()));
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 160101, paras);
				return false;
			}

			if (team.getTeamLeaderId() == roleid) {
				allRoleIDs.addAll(team.getNormalMemberIds());
			} else {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 160191, null);
				return false;
			}

			if (!checkTask(team, taskCofig, allRoleIDs)) {
				LineInstManager.logger.error("队伍中有等级不足的情况," + lineId);
				return false;
			}
			
			xbean.InstanceAskInfo askInst = xtable.Instanceaskinfos.select(team.getTeamId());
			if (askInst != null) {
				if ((nowtime - askInst.getAsktime()) < LineInstManager.ASK_TIME * 1000) { //如果在20秒之内重复询问,则返回
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 160363, null);
					return false;
				}
			}
		} 
		else {
			// 不需要组队
			if (team != null) {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,
						144816, null);
				return false;
			}
			allRoleIDs.add(roleid);
		}

		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoleIDs));

		xbean.InstanceInfoCol teamLeadreInfoCol = null;
		xbean.InstanceTaskInfo teamLeadreTaskInfo = null;
		
		final long currentTime = System.currentTimeMillis();
		
		teamLeadreInfoCol = xtable.Roleinstancetask.get(roleid);
		if (teamLeadreInfoCol == null) {
			teamLeadreInfoCol = xbean.Pod.newInstanceInfoCol();
			xtable.Roleinstancetask.insert(roleid, teamLeadreInfoCol);
		}

		teamLeadreTaskInfo = teamLeadreInfoCol.getInstinfo().get(lineId);
		if (teamLeadreTaskInfo == null) {
			teamLeadreTaskInfo = xbean.Pod.newInstanceTaskInfo();
			teamLeadreInfoCol.getInstinfo().put(lineId, teamLeadreTaskInfo);
		}
		
		int teamLeaderStep = 1;
		
		if (!DateValidate.inTheSameDay(teamLeadreTaskInfo.getAccepttime(), currentTime)) {
			teamLeaderStep = 1;
		}
		else {
			teamLeaderStep = LineInstManager.getStep(teamLeadreInfoCol, lineId);
		}
		
		teamLeadreInfoCol.setLastinstanceid(lineId);
		
		if (taskCofig.afterid != 0) {
			xbean.InstanceTaskInfo afterTaskInfo = teamLeadreInfoCol.getInstinfo().get(taskCofig.afterid);
			
			if (afterTaskInfo == null || afterTaskInfo.getState() < 2) {
				SLineTask afterTaskCofig = LineInstManager.getInstance().getInstanceTask(taskCofig.afterid);
				if (afterTaskCofig != null) {
					List<String> para = new ArrayList<String>(2);
					para.add(afterTaskCofig.taskname);
					para.add(taskCofig.taskname);
					MessageMgr.psendMsgNotify(roleid, 160301, para);
				}
				return false;
			}
		}
		
		if (team != null) {
			xbean.InstanceAskInfo askInfo = xtable.Instanceaskinfos.get(team.getTeamId());
			if (askInfo == null) {
				askInfo = xbean.Pod.newInstanceAskInfo();
				xtable.Instanceaskinfos.insert(team.getTeamId(), askInfo);
			}
			else {
				//清除一下离开队伍的在应答列表里的玩家
				Iterator<Long> iter = askInfo.getAnswerinfo().keySet().iterator();
				while (iter.hasNext()) {
					Long rid = iter.next();
					if (!allRoleIDs.contains(rid)) {
						iter.remove();
					}
				}
			}
			
			//设置提问时间
			askInfo.setAsktime(nowtime);
			boolean reset = false;
			Integer askLineId = askInfo.getInstid();
			if (askLineId == null || askLineId != lineId) {
				askInfo.setInstid(lineId);
				reset = true;
			}
			
			fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(231);
			int commonCnt = Integer.parseInt(common.getValue());
			
			//保存会自动进入副本的角色
			List<Long> autoEnterRoles = new ArrayList<Long>();
			//向每个队员询问是否要进入副本
			for (Long rid : allRoleIDs) {
				xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.select(rid);
				Integer ridCnt = 0;
				
				Integer curStep = 1;
				
				if (infoCol == null) {
					infoCol = xbean.Pod.newInstanceInfoCol();
					xtable.Roleinstancetask.insert(rid, infoCol);
				}
				
				xbean.InstanceTaskInfo instanceInfo = infoCol.getInstinfo().get(lineId);
				if (instanceInfo == null) {
					instanceInfo = xbean.Pod.newInstanceTaskInfo();
					infoCol.getInstinfo().put(lineId, instanceInfo);
				}
				
				if (taskCofig.afterid != 0) {
					xbean.InstanceTaskInfo afterTaskInfo = infoCol.getInstinfo().get(taskCofig.afterid);
					if (afterTaskInfo == null || afterTaskInfo.getState() < 2) {
						SLineTask afterTaskCofig = LineInstManager.getInstance().getInstanceTask(taskCofig.afterid);
						if (afterTaskCofig != null) {
							List<String> para = new ArrayList<String>(3);
							PropRole pteamRole = new PropRole(rid, true);
							para.add(pteamRole.getName());
							para.add(afterTaskCofig.taskname);
							para.add(taskCofig.taskname);
							for (Long mem : team.getNormalMemberIds()) {
								MessageMgr.psendMsgNotify(mem, 160439, para);
							}
						}
						return false;
					}
				}
				
				//判断是否进入副本
				short autoenter = 0;
				
				if (!DateValidate.inTheSameDay(infoCol.getAccepttime(), currentTime)) {
					ridCnt = 0;
					curStep = 1;
				}
				else {
					ridCnt = infoCol.getCounts();
					curStep = LineInstManager.getStep(infoCol, lineId);
				}
				
				if (!reset) { //如果不是新的副本,则判断上一次是否确认过
					if (askInfo.getAnswerinfo().containsKey(rid)) {
						if (askInfo.getAnswerinfo().get(rid) == 1) {
							//上一次是确定的,则自动确认进入
							autoenter = 1;
						}
					}
					else {
						autoenter = 0;
					}
				}
				
				//判断系统配置里是否会自动确认进入
				if (autoenter == 0) {
					Map<Integer, Integer> lineSet = xtable.Properties.selectLineconfigmap(rid);
					if (lineSet.containsKey(taskCofig.minlevel)) {
						autoenter = lineSet.get(taskCofig.minlevel).shortValue();
						if (autoenter > 0)
							autoenter = 1;
					}
				}
				
				//如果是自动进入的,需要给其他队员广播协议
				if (autoenter == 1)
					autoEnterRoles.add(rid);
				
				List<Integer> steplist = LineInstManager.getUndoStep(rid, lineId);
				LinkedList<Integer> tsteplist = new LinkedList<Integer>();
				if (steplist != null)
					tsteplist.addAll(steplist);
				
				if (ridCnt < commonCnt) {
					if (rid == team.getTeamLeaderId()) {
						SAskIntoInstance askInto = new SAskIntoInstance(166008, taskCofig.taskname, commonCnt - ridCnt, 
								teamLeaderStep, teamLeaderStep, curStep, taskCofig.stepnum, tsteplist, 1, autoenter);
						askInfo.getAnswerinfo().put(rid, (int)autoenter);
						mkdb.Procedure.psendWhileCommit(rid, askInto);
					}
					else {
						SAskIntoInstance askInto = new SAskIntoInstance(160210, taskCofig.taskname, commonCnt - ridCnt,
								teamLeaderStep, teamLeaderStep, curStep, taskCofig.stepnum, tsteplist, 1, autoenter);
						askInfo.getAnswerinfo().put(rid, (int)autoenter);
						mkdb.Procedure.psendWhileCommit(rid, askInto);
					}
				}
				else {
					if (rid == team.getTeamLeaderId()) {
						SAskIntoInstance askInto = new SAskIntoInstance(166009, taskCofig.taskname, 0, 
								teamLeaderStep, teamLeaderStep, curStep, taskCofig.stepnum, tsteplist, 1, autoenter);
						askInfo.getAnswerinfo().put(rid, (int)autoenter);
						mkdb.Procedure.psendWhileCommit(rid, askInto);
					}
					else {
						SAskIntoInstance askInto = new SAskIntoInstance(166007, taskCofig.taskname, 0, 
								teamLeaderStep, teamLeaderStep, curStep, taskCofig.stepnum, tsteplist, 1, autoenter);
						askInfo.getAnswerinfo().put(rid, (int)autoenter);
						mkdb.Procedure.psendWhileCommit(rid, askInto);
					}
				}
			}
			
			for (Long rid : autoEnterRoles) {
				//设置为同意
				SAnswerInstance answerInst = new SAnswerInstance();
				answerInst.roleid = rid;
				answerInst.answer = 1;
				mkdb.Procedure.psendWhileCommit(allRoleIDs, answerInst);
			}
			
			//启动定时器,20秒如果没有人应答,给客户端提示谁没有同意进入副本
			ScheduledFuture<?> future = mkdb.Executor.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					new mkdb.Procedure() {
						@Override
						protected boolean process() throws Exception {
							xbean.InstanceAskInfo askInst = xtable.Instanceaskinfos.get(team.getTeamId());
							if (askInst == null)
								return false;
							
							for (Map.Entry<Long, Integer> ret : askInst.getAnswerinfo().entrySet()) {
								if (ret.getValue() == 0) {
									String roleName = xtable.Properties.selectRolename(ret.getKey());
									if (roleName == null)
										return false;
									List<String> para = new ArrayList<String>(1);
									para.add(roleName);
									for (Long rid : team.getAllMemberIds()) {
										MessageMgr.psendMsgNotifyWhileCommit(rid, 160211, para);
									}
								}
							}
//							xtable.Instanceaskinfos.remove(team.getTeamId());
							return true;
						}
						
					}.submit();
				}
			}, LineInstManager.ASK_TIME, TimeUnit.SECONDS);
			LineInstManager.askInstFuture.put(team.getTeamId(), future);
		}
		
		return true;
	}

	
	private boolean checkTask(Team team, SLineTask taskCofig,
			List<Long> allRoleIDs) {
		// 验证等级问题
		List<String> levelException = new ArrayList<String>();
		for (Long mem : allRoleIDs) {
			PropRole pRole = new PropRole(mem, true);
			if (pRole.getLevel() < taskCofig.getMinlevel()) {
				levelException.add(pRole.getName());
			}
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
			for (Long mem : allRoleIDs) {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(mem, 160102, paras);
			}
			
			return false;
		}

		if (allRoleIDs.size() < taskCofig.getTeamnum()) {
			List<String> paras = new ArrayList<String>();
			paras.add(String.valueOf(taskCofig.getTeamnum()));
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 160103,
					paras);
			return false;
		}

		return true;
	}

}

