package fire.pb.mission;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fire.pb.circletask.CircTask;
import fire.pb.circletask.CircleTask;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.circletask.anye.RoleAnYeTask;
import fire.pb.clan.ClanUtils;
import fire.pb.course.CourseManager;
import fire.pb.event.EnterPetLuckEvent;
import fire.pb.event.Event;
import fire.pb.event.FactionCreateOrJoinEvent;
import fire.pb.event.ForgeDecorationEvent;
import fire.pb.event.JoinCampEvent;
import fire.pb.event.LevelBreakEvent;
import fire.pb.event.LevelContinueEvent;
import fire.pb.event.PetStarEvent;
import fire.pb.event.SetProtectPasswordEvent;
import fire.pb.item.CombineItemEvent;
import fire.pb.item.Commontext;
import fire.pb.item.EnterBingFengInstEvent;
import fire.pb.item.EnterJingYingInstEvent;
import fire.pb.item.EquipMakeEvent;
import fire.pb.item.RoleAddPointEvent;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;
import fire.pb.mission.MissionExeTypes;
import fire.pb.mission.MissionFinTypes;
import fire.pb.mission.MissionStatus;
import fire.pb.mission.SQuestCanAcceptList;
import fire.pb.mission.TrackedMission;
import fire.pb.mission.util.ItemMissionListener;
import fire.pb.mission.util.PetMissionListener;
import fire.pb.npc.SGatherConfig;
import fire.pb.school.School;
import fire.pb.talk.DisplayInfo;
import fire.pb.talk.MessageMgr;
import fire.pb.title.Title;
import fire.pb.util.DateValidate;

public class Module implements ModuleInterface, fire.pb.event.EventHandler {
	public static final String MODULE_NAME = "mission";

	public static final Logger logger = Logger.getLogger("TASK");
	
	public static Module getInstance() {
		return (Module)fire.pb.main.ModuleManager.getInstance().getModuleByName(MODULE_NAME);
	}
	
	private final ItemMissionListener itemmissionlistener = new ItemMissionListener();
	
	private final PetMissionListener petmissionlistener = new PetMissionListener();
	
	public void enterWorldOK(final long roleid) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				new MissionColumn(roleid, false).afterEnterWorld();
				return true;
			}
		}.submit();
	}
	
	public boolean checkGather( long roleid, final SGatherConfig conf ) {
		fire.pb.mission.MissionColumn sml = new fire.pb.mission.MissionColumn(roleid, true);
		for (int mission : conf.tasks) {
			if (sml.getMission(mission) != null ) {
				return true;
			}
		}	
		return false;
	}
	
	@Override
	public void exit() {
	}
	
	public void getCanAcceptMission4TuPo(final long roleid, final java.util.List<Integer> quests, int level) {
		quests.clear();
		for (final QuestCanAcceptList quest : MissionManager.getInstance().getCanAcceptMissionMap().values()) {
			if (UtilHelper.isBranchScenarioMission(quest.id)) {
				if(UtilHelper.isTuPoMission(quest.id)){
					if(quest.任务等级min != level)
						continue;
					if(quest.任务等级min == quest.任务等级max){
						//突破任务
						final Integer roleLevel = xtable.Properties.selectLevel(roleid);
						if (roleLevel == null)
							continue;
						if (roleLevel.intValue() != quest.任务等级min)
							continue;
						if(!quests.contains(quest.id))
							quests.add(quest.id);
					}
				}
			} else {
				getCanAcceptMission(roleid, quests, quest);
			}
		}
		Collections.sort(quests);
	}
	
	public void getCanAcceptMission( final long roleid, final java.util.List<Integer> quests ) {
		quests.clear();
		MissionColumn sml = new MissionColumn( roleid, true );
		//sml.getCanacceptQuest( quests );
		
		for (final QuestCanAcceptList quest : MissionManager.getInstance().getCanAcceptMissionMap().values()) {
			if (UtilHelper.isBranchScenarioMission(quest.id)) {
				if(UtilHelper.isTuPoMission(quest.id)){
					if(quest.任务等级min == quest.任务等级max){
						//突破任务
						final Integer roleLevel = xtable.Properties.selectLevel(roleid);
						if (roleLevel == null)
							continue;
						if (roleLevel.intValue() != quest.任务等级min)
							continue;
						RoleMission task = sml.getMission(quest.id);
						if (task != null) {
							continue;
						}
						if (sml.hasTuPoMission())
							continue;
						int taskline = UtilHelper.getMissionLineid(quest.id);
						if (sml.getMissionByLineid(taskline) != null)
							continue;
						Map<Integer,Integer> tupotips = xtable.Properties.selectTupotips(roleid);
						Integer st = tupotips.get(quest.任务等级min);
						if(st != null && st.intValue() == 1){
							if(!quests.contains(quest.id))
								quests.add(quest.id);
						}
					}
				}
			} else {
				getCanAcceptMission(roleid, quests, quest);
			}
		}
		Collections.sort(quests);
	}
	
	public void getCanAcceptMission4Abandon( final long roleid, final java.util.List<Integer> quests ) {
		quests.clear();
		MissionColumn sml = new MissionColumn( roleid, true );
		//sml.getCanacceptQuest( quests );
		
		for (final QuestCanAcceptList quest : MissionManager.getInstance().getCanAcceptMissionMap().values()) {
			if (UtilHelper.isBranchScenarioMission(quest.id)) {
				if(UtilHelper.isTuPoMission(quest.id)){
					if(quest.任务等级min == quest.任务等级max){
						//突破任务
						final Integer roleLevel = xtable.Properties.selectLevel(roleid);
						if (roleLevel == null)
							continue;
						if (roleLevel.intValue() != quest.任务等级min)
							continue;
						RoleMission task = sml.getMission(quest.id);
						if (task != null && task.getState() != MissionStatus.UNACCEPT) {
							continue;
						}
						Map<Integer,Integer> tupotips = xtable.Properties.selectTupotips(roleid);
						Integer st = tupotips.get(quest.任务等级min);
						if(st != null && st.intValue() == 1){
							if(!quests.contains(quest.id))
								quests.add(quest.id);
						}
					}
				}
			} else {
				getCanAcceptMission(roleid, quests, quest);
			}
		}
		Collections.sort(quests);
	}

	private void getCanAcceptMission(final long roleid, final java.util.List<Integer> quests, final QuestCanAcceptList quest) {
		final Integer roleLevel = xtable.Properties.selectLevel(roleid);
		if (roleLevel == null)
			return;
		if (quest.任务等级min <= roleLevel && roleLevel <= quest.任务等级max) {
			if (UtilHelper.isSpecialQuest(quest.id)) {
				final CircleTask sq = new CircleTask(roleid, true);
				if (sq.getSpecialQuestState(quest.id) == -1) {
					quests.add(quest.id);
				}
				else {
					final xbean.CircleTaskInfo sqinfo = sq.getSpecialQuestInfo(quest.id);
					if (sqinfo.getQueststate() == SpecialQuestState.ABANDONED 
							|| sqinfo.getQueststate() == SpecialQuestState.SUCCESS
							|| sqinfo.getQueststate() == SpecialQuestState.FAIL
							|| sqinfo.getQueststate() == 0) {
						final CircTask ct = CircleTaskManager.getInstance().getCircTask(sqinfo.getId());
						if (ct.totaltime == 0) {
							quests.add(quest.id);
						}
						else {
							final int circle = ct.getCycle();
							final long now = System.currentTimeMillis();
							//判断是否清0的代码放到这里
							switch(circle) {
							case 1: {
								if (!fire.pb.util.DateValidate.inTheSameDay(sqinfo.getTakequesttime(), now)) {
									quests.add(quest.id);
								}
								else {
									//判断次数
									final int sumtime = sqinfo.getSumnum();
									if (sumtime < ct.totaltime) {
										quests.add(quest.id);
									}
								}
								break;
							}
							case 2: {
								if (!fire.pb.util.DateValidate.inTheSameWeek(sqinfo.getTakequesttime(), now)) {
									quests.add(quest.id);
								}
								else {
									//判断次数
									final int sumtime = sqinfo.getSumnum();
									if (sumtime < ct.totaltime) {
										quests.add(quest.id);
									}
								}
								break;
							}
							case 3: {
								if (!fire.pb.util.DateValidate.inTheSameMonth(sqinfo.getTakequesttime(), now)) {
									quests.add(quest.id);
								}
								else {
									//判断次数
									final int sumtime = sqinfo.getSumnum();
									if (sumtime < ct.totaltime) {
										quests.add(quest.id);
									}
								}
								break;
							}
							default:
								break;
							}
						}
						
					}
				}
			}
			else {
				quests.add(quest.id);
			}
		}
	}
	
	public int hasUnfinishedFairylandMission(final long roleid) {
		for (final RoleMission sm : new MissionColumn(roleid, true) ) {
			if ( sm != null && sm.isFairylandQuest() && sm.getState() != MissionStatus.COMMITED )
				return sm.getId();
		}
		return 0;
	}
	
	@Override
	public void init() throws Exception {
		logger.info("task模块初始化开始");
		fire.pb.main.ConfigManager cm = fire.pb.main.ConfigManager.getInstance();
		fire.pb.circletask.CircleTaskManager.getInstance();
		MissionManager.getInstance().init(cm);
		// 监听消息
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.EquipItemEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.UnequipItemEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.SetFightPetEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.LevelupEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.BuyItemEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.EnterWorldEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.OutBattleEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.BattleEndEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.BagItemChange.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.PetColumnChange.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.MissionCompleteEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.CircleTaskCompleteEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.UpdateInbornLevel.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.ReleaseInfoEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.ReleaseJianghuzhaoji.class );
		fire.pb.event.Poster.getPoster().listenEvent( this, fire.pb.event.GetMasterEvent.class );
		fire.pb.event.Poster.getPoster().listenEvent(this, fire.pb.event.EndMinigameEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, fire.pb.event.WinUndeadChallangeEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, fire.pb.event.MoneyChangeEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, fire.pb.event.AddGemToEquip.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, fire.pb.event.RefineEquipEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, fire.pb.event.ReleaseApprenticeInfoEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, fire.pb.event.VisitSite.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, PetStarEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, JoinCampEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, EnterPetLuckEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, SetProtectPasswordEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, CombineItemEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, EquipMakeEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, RoleAddPointEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, ForgeDecorationEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, EnterJingYingInstEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, EnterBingFengInstEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, FactionCreateOrJoinEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, LevelBreakEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, LevelContinueEvent.class);
		//初始化活动日历配置
		fire.pb.mission.activelist.RoleLivenessManager.getInstance().init();
		logger.info("task模块初始化完成");	
		CourseManager.getInstance().Init();
		if(chargeClear) {
			chargeDataClear();
			chargeHistoryDataClear();
			platOrderHistroyClear();
			chargeClear = false;
		}
	}
	
	private boolean chargeClear = true;
	
	private void platOrderHistroyClear() {
		logger.info("开始清理过期平台充值订单历史...");
		Set<String> platorders = new HashSet<String>();
		xtable.Platorderhistroy.getTable().browse(new mkdb.TTable.IWalk<String, Long>() {
			@Override
			public boolean onRecord(String platsn, Long time) {
				platorders.add(platsn);
				return true;
			}
		});
		logger.info("开始清理过期平台充值订单历史...size=" + platorders.size());
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				if (platorders.isEmpty()) {
					return true;
				}
				lock(mkdb.Lockeys.get(xtable.Locks.PLATORDERHISTROY, platorders));
				long now = System.currentTimeMillis();
				for (String platsn : platorders) {
					Long historytime = xtable.Platorderhistroy.select(platsn);
					if (Math.abs(now - historytime.longValue()) >= 1000L * 60 * 60 * 24 * 7){
						xtable.Platorderhistroy.remove(platsn);
					}
				}
				logger.info("完成清理过期无效充值订单.");	
				return true;
			}
		};
		if (mkdb.Transaction.current() == null) {
			proc.submit();
		} else {
			mkdb.Procedure.pexecute(proc);
		}
	}
	
	private void chargeHistoryDataClear() {
		logger.info("开始清理过期无效充值订单历史...");
		Set<Integer> userids = new HashSet<Integer>();
		xtable.Chargehistory.getTable().browse(new mkdb.TTable.IWalk<Integer, xbean.ChargeHistory>() {
			@Override
			public boolean onRecord(Integer userid, xbean.ChargeHistory value) {
				userids.add(userid);
				return true;
			}
		});
		logger.info("开始清理过期无效充值订单历史...size=" + userids.size());
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				if (userids.isEmpty()) {
					return true;
				}
				lock(mkdb.Lockeys.get(xtable.Locks.USERLOCK, userids));
				long now = System.currentTimeMillis();
				for (int  userid : userids) {
					xbean.ChargeHistory ch = xtable.Chargehistory.get(userid);
					Set<Long> delChargeSns = new HashSet<Long>();
					for(java.util.Map.Entry<Long, xbean.ChargeOrder> en : ch.getCharges().entrySet()) {
						if (!fire.pb.util.DateValidate.inTheSameWeek(now, en.getValue().getCreatetime()) && en.getValue().getStatus() == 1){
							delChargeSns.add(en.getKey());
						}
					}
					//logger.info("清理过期无效充值订单历史.userid:" + userid + ",size:" + delChargeSns.size());	
					for(long csn : delChargeSns) {
						ch.getCharges().remove(csn);
					}
				}
				logger.info("完成清理过期无效充值订单历史.");	
				return true;
			}
		};
		if (mkdb.Transaction.current() == null) {
			proc.submit();
		} else {
			mkdb.Procedure.pexecute(proc);
		}
	}
	
	private void chargeDataClear() {
		logger.info("开始清理过期无效充值订单...");
		Set<Long> chargeSns = new HashSet<Long>();
		xtable.Chargeorder.getTable().browse(new mkdb.TTable.IWalk<Long, xbean.ChargeOrder>() {
			@Override
			public boolean onRecord(Long chargeSn, xbean.ChargeOrder value) {
				chargeSns.add(chargeSn);
				return true;
			}
		});
		logger.info("开始清理过期无效充值订单...size=" + chargeSns.size());
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				if (chargeSns.isEmpty()) {
					return true;
				}
				lock(mkdb.Lockeys.get(xtable.Locks.CHARGEORDER, chargeSns));
				long now = System.currentTimeMillis();
				for (long chargeSn : chargeSns) {
					long chargeTime = xtable.Chargeorder.selectCreatetime(chargeSn);
					if (!fire.pb.util.DateValidate.inTheSameWeek(now, chargeTime)){
						xtable.Chargeorder.remove(chargeSn);
					}
				}
				logger.info("完成清理过期无效充值订单.");	
				return true;
			}
		};
		if (mkdb.Transaction.current() == null) {
			proc.submit();
		} else {
			mkdb.Procedure.pexecute(proc);
		}
	}
	
	@Override
	public void onEvent(final Event e) {
		PMissionProc p = new PMissionProc() {
			@Override
			protected boolean missionExecute() {
				if (e instanceof fire.pb.event.EnterWorldEvent) {
					sendScenarioMission(e.getRoleid());
					sendAllCanAcceptMission(e.getRoleid());
					sendAllAcceptedMission(e.getRoleid());
					sendTracked(e.getRoleid());
					return true;
				}
				new MissionColumn(e.getRoleid(), false).onEvent(e);
				if (e instanceof fire.pb.event.BagItemChange) {
					fire.pb.event.BagItemChange event = (fire.pb.event.BagItemChange)e;
					itemmissionlistener.onChanged(event.getRoleid(), event.getItemid());
				} else if (e instanceof fire.pb.event.PetColumnChange) {
					fire.pb.event.PetColumnChange event = (fire.pb.event.PetColumnChange)e;
					petmissionlistener.onPetColumnChange(event.getRoleid(), event.getPetid());
				}else if (e instanceof fire.pb.event.BattleEndEvent) {
				} else if (e instanceof fire.pb.event.SetFightPetEvent) {
					//设置取消宠物参战状态，重新检测下循环任务完成与否
					fire.pb.event.SetFightPetEvent event = (fire.pb.event.SetFightPetEvent)e;
					petmissionlistener.onPetColumnChange(event.getRoleid(), event.getPetid());
				}
				else if (e instanceof fire.pb.event.MissionCompleteEvent) {
					fire.pb.event.MissionCompleteEvent event = (fire.pb.event.MissionCompleteEvent)e;
					int id = event.getMissionID();
					int level = 0;
					if (id == 1120001)
					{
						level = 1;
					}
					else if (id == 1130001)
					{
						level = 2;
					}
					else if (id == 1140001)
					{
						level = 3;
					}
					
					if (level != 0)
					{
						long roleid = event.getRoleid();
						xbean.Properties prop = xtable.Properties.select(roleid);
						if (prop != null)
						{
							School s = School.getSchoolBySchoolid(prop.getSchool());
							int titleID = School.getTitleIdBySchool(level, prop.getSex(), s);
							//给一二三级称谓 by changhao
							Title title = new Title(roleid, false);
							if (!title.roleHaveTitle(titleID))
							{
								Title.addTitle(roleid, titleID, "", -1);
							}						
						}						
					}	
				}

				return true;
			}
		};
		PMissionProc p2 = null;
		if (e instanceof fire.pb.event.EnterWorldEvent) {
			p2 = new PMissionProc() {
				@Override
				protected boolean missionExecute() {
					RoleAnYeTask.sendAllAnYeTask(e.getRoleid());
					return true;
				}
			};
		}
		if (mkdb.Transaction.current() == null){
			p.submit();
			if(p2 != null)
				p2.submit();
		}else{
			p.call();
			if(p2 != null)
				p2.call();
		}		
	}
	
	public Commontext.UseResult onUseMissionItem( final long roleid, final int itemid ) {
		logger.info("角色[" + roleid + "]使用任务道具[" + itemid + "]");
		MissionColumn missionlist = new MissionColumn(roleid, false);
		for (RoleMission mission : missionlist) {
			if (mission.getState() == MissionStatus.UNACCEPT)
				continue;
			if (mission.getConf() == null)
				continue;
			if (UtilHelper.getMissionExeType(mission.getConf().exeIndo.missionType) 
					== MissionExeTypes.USE) {
				MissionConfig conf = mission.getConf();
				if ( conf.exeIndo.useItemID == itemid ) {
					if ( conf.exeIndo.leftPos != 0 || conf.exeIndo.rightPos != 0 || 
							conf.exeIndo.topPos != 0 || conf.exeIndo.bottomPos != 0 ) {
						fire.msp.task.GScenarioQuestUseItemVerifyPos send = 
							new fire.msp.task.GScenarioQuestUseItemVerifyPos();
						send.roleid = roleid;
						send.scenarioquestid = mission.getId();
						send.mapid = conf.exeIndo.mapID;
						send.useitemid = itemid;
						send.left = conf.exeIndo.leftPos;
						send.right = conf.exeIndo.rightPos;
						send.top = conf.exeIndo.topPos;
						send.bottom = conf.exeIndo.bottomPos;
						
						//帮派宣言道具特殊处理
						if(itemid>50275 && itemid<50286){
							xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
							if(clanInfo != null){
								StringBuffer message = new StringBuffer(256);
								message.append("<T t=\"").append(clanInfo.getClanaim()).append("\" c=\"FFFFFFFF\"></T>");
								MessageMgr.sendMsgToRoleAroundScreen(roleid, message.toString(), new ArrayList<DisplayInfo>());
							}
						}
						logger.info("角色[" + roleid + "]使用任务道具[" + itemid + "],有坐标配置需求,转到地图线程处理!");
						fire.pb.GsClient.pSendWhileCommit( send );
						return Commontext.UseResult.AWAIT;
					} else {
						boolean useSucc = fire.msp.task.MScenarioQuestUseItemVerifyPosSucc.dealUseItem(roleid, 
								mission.getId(), itemid);
						logger.info("角色[" + roleid + "]使用任务道具[" + itemid + "],无坐标配置需求,直接处理,结果:" + useSucc);
						if (useSucc)
							return Commontext.UseResult.SUCC;
						else
							return Commontext.UseResult.FAIL;
					}
				}
			}
		}
		return Commontext.UseResult.FAIL;
	}
	
	@Override
	public ReloadResult reload() throws Exception
	{
		fire.pb.main.ConfigManager cm = fire.pb.main.ConfigManager.getInstance();
		MissionManager.getInstance().init(cm);
		return new ReloadResult(true);
	}
	
	private void sendAllAcceptedMission( final long roleid ) {
		sendMasterMission(roleid);
//		sendInstanceMission(roleid);
	}
	
	private void sendAllCanAcceptMission( final long roleid ) {
		SReqMissionCanAccept send = new SReqMissionCanAccept();
		getCanAcceptMission( roleid, send.missions );
		mkdb.Procedure.psendWhileCommit( roleid, send );
	}
//	private void sendInstanceMission(final long roleid) {
//		fire.pb.mission.instance.InstanceManager.getInstance().sendAllQuest(roleid);
//	}
	
	private void sendMasterMission( final long roleid ) {
		new fire.pb.circletask.PClearCircleTask(roleid, false, true).call();
		fire.pb.circletask.CircleTaskManager.sendAllSpecialQuest( roleid );
	}
	
	private void sendScenarioMission(final long roleid) {
		for (RoleMission sm : new MissionColumn(roleid, false)) {
			if (sm.getState() == MissionStatus.PROCESSING && 
					UtilHelper.getMissionFinType(sm.conf.exeIndo.missionType) == MissionFinTypes.OTHER) {}
			if (sm.getState() == MissionStatus.PROCESSING || sm.getState() == MissionStatus.FINISHED) {
				SAcceptMission send = sm.toProtocol();
				mkdb.Transaction.tsend(roleid, send);
			}
			if (sm instanceof MissionMajorScenario) {
				if (sm.getState() == MissionStatus.COMMITED ) {
					java.util.Set<Integer> posts = MissionManager.getInstance().getPostmissions(
							UtilHelper.getMissionLineid(sm.getId()));
					if(posts != null) {
						for (Integer nextmission : posts) {
							if	(new PAcceptMajorMission(roleid, nextmission, true).call())
								break;
						}
					} else {
					}
				}
			}
		}
	}
	
	private void sendTracked(final long roleid) {
		STrackedMissions send = new STrackedMissions();
		xbean.TrackedMission tt = xtable.Trackedmission.select(roleid);
		if (tt == null)
			return;
		for (Map.Entry<Integer, xbean.Track> e : tt.getQuestids().entrySet()) {
			TrackedMission tq = new TrackedMission();
			tq.acceptdate = e.getValue().getDate();
			send.trackedmissions.put(e.getKey(), tq);
		}
		mkdb.Transaction.tsendWhileCommit(roleid, send);
	}
	
	public void trackAccpetMission( final long roleid, final int questid ) {
		final long accepttime = Calendar.getInstance().getTimeInMillis();
		final mkdb.Procedure p = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				xbean.TrackedMission tt = xtable.Trackedmission.get( roleid );
				if ( tt == null ) {
					tt = xbean.Pod.newTrackedMission();
					xtable.Trackedmission.insert( roleid, tt ); 
				}
				xbean.Track xtrack = xbean.Pod.newTrack();//tt.getQuestids().get( questid );
				xtrack.setDate( accepttime );
				tt.getQuestids().put(questid, xtrack);
				Module.logger.debug("角色[" + roleid + "]追踪任务[" + questid + "].");
				return true;
			}
		};
		if ( mkdb.Transaction.current() == null )
			p.submit();
		else
			p.call();
	}

	public void untrackMission( final long roleid, final int questid ) {
		final mkdb.Procedure p = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				xbean.TrackedMission tt = xtable.Trackedmission.get( roleid );
				if ( tt == null ) {
					return false;
				}
				tt.getQuestids().remove(questid);
				Module.logger.debug("角色[" + roleid + "]取消追踪任务[" + questid + "].");
				return true;
			}
		};
		if ( mkdb.Transaction.current() == null )
			p.submit();
		else
			p.call();
	}

}
