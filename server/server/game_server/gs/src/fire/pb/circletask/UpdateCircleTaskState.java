package fire.pb.circletask;

import java.util.ArrayList;
import java.util.Calendar;

import fire.pb.PropRole;
import fire.pb.activity.timernpc.TimerNpcService;
import fire.pb.circletask.CircTask;
import fire.pb.circletask.CircTaskClass;
import fire.pb.circletask.CircTaskConf;
import fire.pb.circletask.RefreshDataType;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.hook.RoleHookManager;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.Module;
import fire.pb.mission.SReqMissionCanAccept;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.mission.treasuremap.BaoTuMapManager;
import fire.pb.mission.treasuremap.EventTimerGroupData;
import fire.pb.mission.treasuremap.EventTimerNpcData;
import fire.pb.mission.treasuremap.GiftByEvent;
import fire.pb.talk.MessageMgr;
import fire.pb.util.Misc;


public class UpdateCircleTaskState extends mkdb.Procedure{
	private long roleid;
	private int questid;
	private int state;
	
	private final static int YI_LUN_JIE_SHU = 1;  //一轮结束 
	private final static int BOOS = 2;  //遇到boos了
	private final static int NORMAL = 0;  //正常结束
	
	public UpdateCircleTaskState(long roleid, int questid, int state){
		this.roleid = roleid;
		this.questid = questid;
		this.state = state;
	}
	
	public void refreshQuestState(final long rId, final CircTaskConf ctc, final CircTask ct) {
		final CircleTask sq = new CircleTask(rId, false);
		if (sq.stepState(questid, state)) {
			xbean.CircleTaskInfo questinfo = sq.getQuestMap().getTaskmap().get(questid);
			if (questinfo == null)
				return;
			
			if (ctc.autocomp == 1)
			{
				CircleTaskManager.getInstance().refreshQuestState(rId, questid, SpecialQuestState.SUCCESS);
				if (questinfo.getQueststate() == SpecialQuestState.DONE) {
					if (ct.doublepoint > 0) { //需要消耗双倍点数的处理
						short dpoint = RoleHookManager.getInstance().getLastDpoint(rId);
						if (dpoint < ct.doublepoint) //无双倍点数的处理
							questinfo.setCatchitsingletime(questinfo.getCatchitsingletime() + 1);
						else
							questinfo.setCatchitdoubletime(questinfo.getCatchitdoubletime() + 1);
					}
					
					questinfo.setSumnum(questinfo.getSumnum() + 1);
					
					if (questinfo.getSumnum() >= (ct.ring * ct.round)) {
						if (questid == 1060000) {
							fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150551, null);
						}
						else {
							fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ct.getMaxnumtips(), null);
						}
					}
					
					if (questinfo.getQuesttype() == ct.specialtask) {
						questinfo.setRound(0);
					}
				}
				sq.clearSpecialQuestInfo(questinfo);
			} 
		}
	}
	
	public boolean process() {
		java.util.List<Long> members = new ArrayList<Long>();
		fire.pb.team.Team team = fire.pb.team.TeamManager.getTeamByRoleId(roleid);
		if (null != team) {
			// 全队正常状态成员 加锁
			members.addAll(team.getNormalMemberIds());
			this.lock(xtable.Locks.ROLELOCK, members);
		}
		else {
			members.add(roleid);
		}
		
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleid, questid);
		if (ct == null) {
			return false;
		}	
		
		int circleMax = CircleTaskManager.getInstance().getCTCircleMax(ct);
		
		final CircleTask sq = new CircleTask(roleid, false);
		xbean.CircleTaskInfo sqinfo = sq.getQuestMap().getTaskmap().get(questid);
		if (null == sqinfo) {
			CircleTaskManager.logger.info("角色[" + roleid + "]循环任务" + questid + "信息错误！");
			return false;
		}
		
		CircTaskConf ctc = CircleTaskManager.getCircTaskConf(sqinfo.getQuesttype());
		if (null == ctc) {
			if (sqinfo.getQueststate() == SpecialQuestState.ABANDONED) {
				CircleTaskManager.logger.error("角色[" + roleid + "]循环任务" + questid + ",任务类型:" + CircTaskClass.CircTask_CatchIt + ",超时放弃,不再发奖");
			}
			CircleTaskManager.logger.info("角色[" + roleid + "]循环任务"+ questid + ",questtype=" + sqinfo.getQuesttype() + "配置null！");
			return false;
		}
		
		if (ctc.cttype == CircTaskClass.CircTask_CatchIt) {
			//如果任务超时,则直接返回false,不再发奖
			if (sqinfo.getQueststate() == SpecialQuestState.ABANDONED) {
				CircleTaskManager.logger.error("角色[" + roleid + "]循环任务" + questid + ",任务类型:" + CircTaskClass.CircTask_CatchIt + ",超时放弃,不再发奖");
				return false;
			}
		}
		
		//判断是否需要组队
		if (ct.teamnum == 0) { //不需要组队
			CircleTaskManager.logger.info("角色[" + roleid + "]StepStateProc任务" + questid +"！");
			if (sq.stepState(questid, state)) {
				if (sqinfo.getQueststate() != SpecialQuestState.DONE) {
					if (sqinfo.getQueststate() == SpecialQuestState.UNDONE) {
						CircleTaskManager.logger.error("角色[" + roleid + "]循环任务" + questid + "状态为未完成！");
						CircleTaskManager.getInstance().refreshQuestState(roleid, questid, SpecialQuestState.UNDONE);
						return true;
					} else {
						CircleTaskManager.logger.error("角色[" + roleid + "]循环任务" + questid + "信息错误！");
						return false;
					}
				}
				
				//检测是否触发特殊事件
				if (sqinfo.getRound() == ct.ring + 1) {
					CircleTaskEventGroup eventGroup = CircleTaskManager.getInstance().getEventGroup(sqinfo.getId());
					if (eventGroup != null) {
						java.util.List<Integer> groupList = eventGroup.getEventList();
						if (groupList != null) {
							for (Integer giftid : groupList) {
								GiftByEvent eventAward = BaoTuMapManager.getInstance().getEventGift(giftid);
								if (eventAward == null) {
									CircleTaskManager.logger.error("触发事件id错误:" + giftid);
								}
								else {
									EventTimerNpcData eventData = BaoTuMapManager.getInstance().getEventTimerNpcData(eventAward.group);
									if (eventData == null) {
										CircleTaskManager.logger.error("事件刷怪组数据读取错误," + eventAward.name);
										return false;
									}
									
									EventTimerGroupData eventGroupData = BaoTuMapManager.getInstance().getEventTimerGroupData(eventData, roleid);
									if (eventGroupData == null) {
										CircleTaskManager.logger.error("事件刷怪组详细数据读取错误," + eventAward.name);
										return false;
									}
									TimerNpcService.getInstance().createTimerNpcByData(eventGroupData, eventAward.noticeId, roleid);
									MessageMgr.psendMsgNotifyWhileCommit(roleid, eventAward.messageId, null);
								}
							}
						}
					}
				}
				//特殊事件处理完毕
				
				if (ctc.cttype == CircTaskClass.CircTask_CatchIt) {
					if(ctc.autocomp == 1) {
						if (sqinfo.getQueststate() == SpecialQuestState.DONE) {
							if (ct.doublepoint > 0) { //需要消耗双倍点数的处理
								short dpoint = RoleHookManager.getInstance().getLastDpoint(roleid);
								if (dpoint < ct.doublepoint) //无双倍点数的处理
									sqinfo.setCatchitsingletime(sqinfo.getCatchitsingletime() + 1);
								else
									sqinfo.setCatchitdoubletime(sqinfo.getCatchitdoubletime() + 1);
							}
							if (ct.getTaskcnt() == 2) {
								sqinfo.setSumnum(sqinfo.getSumnum() + 1);
							}
							sq.clearSpecialQuestInfo(sqinfo);
							CircleTaskManager.getInstance().refreshQuestState(roleid,
								questid, SpecialQuestState.SUCCESS);
						}
						
						try {
							if (sqinfo.getRound() == ct.ring) {
								new mkdb.Procedure() {
								@Override
								protected boolean process() {
										SReqMissionCanAccept send = new SReqMissionCanAccept();
										Module.getInstance().getCanAcceptMission(roleid, send.missions);
										mkdb.Procedure.psend(roleid, send);
										return true;
									}
								}.call();
							}
							else {
								pexecuteWhileCommit(new PAcceptCircTask(roleid, 0, sqinfo.getGivetasknpcid(), questid, false));
//								PAcceptCircTask giveRoleCircTask = new PAcceptCircTask(roleid, 0, sqinfo.getGivetasknpcid(), questid, false);
//								giveRoleCircTask.call();
							}
							
							return true;
						} catch (Exception e) {
							CircleTaskManager.logger.error("上一环完成发送自动循环任务异常：" , e);
						}
					}
				}
				else {
					if(ctc.autocomp == 1) {
						if (sqinfo.getQueststate() == SpecialQuestState.DONE) {
							CircleTaskManager.logger.info("角色[" + roleid + "]循环任务" + questid + ",DONE状态,准备自动完成!");
							final PropRole prole = new PropRole(roleid,true);
							long masterkey = CircleTaskManager.getInstance().getRoleMastaerKey(roleid,prole.getSchool());
							fire.pb.map.Npc npc = SceneNpcManager.selectNpcByKey(masterkey);
							CircleTask.giveCTAward(roleid, prole.getLevel(), questid, npc, sqinfo, null, false);
							//sq.clearSpecialQuestInfo(sqinfo);
							CircleTaskManager.getInstance().refreshQuestState(roleid,
								questid, SpecialQuestState.SUCCESS);
							if (ct.getTaskcnt() == 2) {
								sqinfo.setSumnum(sqinfo.getSumnum() + 1);
								
								if (sqinfo.getSumnum() >= circleMax) {
									if (questid == 1060000) {
										fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150551, null);
									}
									else {
										fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ct.getMaxnumtips(), null);
									}
								}
							}
							RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
							if (actrole != null && ct.getActiveid() > 0){
								final long now = Calendar.getInstance().getTimeInMillis();
								if (sqinfo.getQuestid() == 1050000) {
									if (fire.pb.util.DateValidate.inTheSameDay(sqinfo.getAcceptquesttime(), now))
										actrole.handleActiveLivenessData(ct.getActiveid());
								} else {
									actrole.handleActiveLivenessData(ct.getActiveid());
								}
							}
							
							fire.pb.course.CourseManager.achieveCourse(roleid, fire.pb.course.CourseType.ACTIVE_COURSE, ct.getActiveid(), 0);
							
							sq.addCircleTaskCompleteTimes(roleid, questid);  //为了触发特殊师门
							sq.dealGaoJiangCircleTask(questid, ct);
							if (ctc.getCompletetip() > 0) {
								fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ctc.getCompletetip(), null);
							}
							sq.clearSpecialQuestInfo(sqinfo);
						}
						
						if(ct.gjhuanshu == sqinfo.getSumnum()) {
							CircleTaskManager.logger.info("角色[" + roleid + "]循环任务" + questid + "达到高奖环数 " + ct.gjhuanshu +"！不再自动随机下一环任务.");
							return true;
						}
						try {
							CircleTaskManager.logger.info("角色[" + roleid + "]循环任务" + questid + "开始自动接受下一环！nextquest=" + ctc.nextquest);
							if(questid != 1050000) {//1050000英雄试练类循环不自动接到身上
								if (ctc.nextquest > 0) {
									new PAutoAddCircleTask(roleid, questid, ctc.nextquest).call();
								} else {
									new PAutoAddCircleTask(roleid, questid).call();
								}
							}
						} catch (Exception e) {
							CircleTaskManager.logger.error("上一环完成发送自动循环师门任务异常：" , e);
						}
					}
				}
				
				return true;
			}
		} else if (ct.teamnum > 0) { //需要组队
			if (null == team || !team.isTeamLeader(roleid)) {
				return false;
			}
			
			int TaskState = NORMAL;
			
			xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(roleid);
			if (null == quest_map) {
				return false;
			}
			xbean.CircleTaskInfo teamLeaderQuestInfo = quest_map.getTaskmap().get(questid);
			if (teamLeaderQuestInfo == null)
				return false;
			
			//检测是否触发特殊事件
			if (teamLeaderQuestInfo.getRound() == ct.ring + 1) {
				CircleTaskEventGroup eventGroup = CircleTaskManager.getInstance().getEventGroup(teamLeaderQuestInfo.getId());
				if (eventGroup != null) {
					java.util.List<Integer> groupList = eventGroup.getEventList();
					if (groupList != null) {
						for (Integer giftid : groupList) {
							GiftByEvent eventAward = BaoTuMapManager.getInstance().getEventGift(giftid);
							if (eventAward == null) {
								CircleTaskManager.logger.error("触发事件id错误:" + giftid);
							}
							else {
								EventTimerNpcData eventData = BaoTuMapManager.getInstance().getEventTimerNpcData(eventAward.group);
								if (eventData == null) {
									CircleTaskManager.logger.error("事件刷怪组数据读取错误," + eventAward.name);
									return false;
								}
								
								EventTimerGroupData eventGroupData = BaoTuMapManager.getInstance().getEventTimerGroupData(eventData, roleid);
								if (eventGroupData == null) {
									CircleTaskManager.logger.error("事件刷怪组详细数据读取错误," + eventAward.name);
									return false;
								}
								TimerNpcService.getInstance().createTimerNpcByData(eventGroupData, eventAward.noticeId, roleid);
								MessageMgr.psendMsgNotifyWhileCommit(roleid, eventAward.messageId, null);
							}
						}
					}
				}
			}
			//特殊事件处理完毕
			
			
			if (teamLeaderQuestInfo.getRound() == ct.ring) {
				TaskState = YI_LUN_JIE_SHU;
				//随机是否有特殊任务
				int nRnd = Misc.getRatePercent();
				if (ct.specialtaskrate > nRnd) {
					TaskState = BOOS;
				}
				if (TaskState == YI_LUN_JIE_SHU) {
					final SRefreshQuestData data = new SRefreshQuestData();
					data.questid = questid;
					data.datas.put(RefreshDataType.DEST_ITEM1_NUM, (long)1);
					mkdb.Procedure.psendWhileCommit(roleid, data);
				}
				else if (TaskState == BOOS) {
					final SRefreshQuestData data = new SRefreshQuestData();
					data.questid = questid;
					data.datas.put(RefreshDataType.DEST_ITEM1_NUM, (long)0);
					mkdb.Procedure.psendWhileCommit(roleid, data);
				}
			}
			else if (teamLeaderQuestInfo.getQuesttype() == ct.specialtask) {
				TaskState = YI_LUN_JIE_SHU;
				final SRefreshQuestData data = new SRefreshQuestData();
				data.questid = questid;
				data.datas.put(RefreshDataType.DEST_ITEM1_NUM, (long)1);
				mkdb.Procedure.psendWhileCommit(roleid, data);
			}
			else {
				final SRefreshQuestData data = new SRefreshQuestData();
				data.questid = questid;
				data.datas.put(RefreshDataType.DEST_ITEM1_NUM, (long)0);
				mkdb.Procedure.psendWhileCommit(roleid, data);
			}
			
			if (ct.teamshare == 1) {//组队共享,状态同时更新
				for (Long rid : members) {
					refreshQuestState(rid, ctc, ct);
					if (TaskState == YI_LUN_JIE_SHU) {
						new mkdb.Procedure() {
						@Override
						protected boolean process() {
								SReqMissionCanAccept send = new SReqMissionCanAccept();
								Module.getInstance().getCanAcceptMission(rid, send.missions);
								mkdb.Procedure.psend(rid, send);
								return true;
							}
						}.call();
					}
				}
			}
			else {
				refreshQuestState(roleid, ctc, ct);
				if (TaskState == YI_LUN_JIE_SHU) {
					new mkdb.Procedure() {
					@Override
					protected boolean process() {
							SReqMissionCanAccept send = new SReqMissionCanAccept();
							Module.getInstance().getCanAcceptMission(roleid, send.missions);
							mkdb.Procedure.psend(roleid, send);
							return true;
						}
					}.call();
				}
			}
			
			try 
			{
				switch(TaskState) 
				{
					case BOOS:
					{
						CircleTaskManager.logger.debug("随机到鬼王任务");
						pexecuteWhileCommit(new PAcceptCircTask(roleid, 0, teamLeaderQuestInfo.getGivetasknpcid(), questid, ct.specialtask));
//						PAcceptCircTask giveRoleCircTask = new PAcceptCircTask(roleid, 0, teamLeaderQuestInfo.getGivetasknpcid(), questid, ct.specialtask);
//						giveRoleCircTask.call();
//						if (!giveRoleCircTask.call()) {
//							SpecialQuestManager.getInstance().refresh2role(teamLeaderQuestInfo, roleid, false);
//						}
						return true;
					}
					case NORMAL:
					{
						pexecuteWhileCommit(new PAcceptCircTask(roleid, 0, teamLeaderQuestInfo.getGivetasknpcid(), questid, false));
//						PAcceptCircTask giveRoleCircTask = new PAcceptCircTask(roleid, 0, teamLeaderQuestInfo.getGivetasknpcid(), questid, false);
//						giveRoleCircTask.call();
//						if (!giveRoleCircTask.call()) {
//							SpecialQuestManager.getInstance().refresh2role(teamLeaderQuestInfo, roleid, false);
//						}
						return true;
					}
					case YI_LUN_JIE_SHU:
					{
						return true;
					}
					default:
						break;
				}
				return true;
			} 
			catch (Exception e) {
				CircleTaskManager.logger.error("上一环完成发送自动捉鬼任务异常：" , e);
			}
		}
		
		return true;
	}
}
