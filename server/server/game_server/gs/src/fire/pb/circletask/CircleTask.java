package fire.pb.circletask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.locojoy.base.Marshal.OctetsStream;

import fire.log.YYLogger;
import fire.pb.PAddExpProc;
import fire.pb.PropRole;
import fire.pb.activity.award.RewardMgr;
import fire.pb.battle.PNewBattle;
import fire.pb.circletask.CircTask;
import fire.pb.circletask.CircTaskClass;
import fire.pb.circletask.CircTaskConf;
import fire.pb.circletask.CircTaskItemFind;
import fire.pb.circletask.RefreshDataType;
import fire.pb.circletask.SpecialQuestID;
import fire.pb.circletask.SpecialQuestNpcMsg;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.circletask.SpecialQuestType;
import fire.pb.circletask.anye.RoleAnYeTask;
import fire.pb.clan.ClanUtils;
import fire.pb.event.CircleTaskCompleteEvent;
import fire.pb.event.MissionCompleteEvent;
import fire.pb.hook.PSubDpointCntProc;
import fire.pb.hook.RoleHookManager;
import fire.pb.item.Commontext;
import fire.pb.item.EquipItem;
import fire.pb.item.FoodItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;
import fire.pb.item.Pack;
import fire.pb.item.SItemToItem;
import fire.pb.item.Commontext.UseResult;
import fire.pb.main.ConfigManager;
import fire.pb.map.Role;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.npc.NpcServices;
import fire.pb.npc.PReqFortuneWheel;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.ChatChannel;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.SChatItemTips;
import fire.pb.talk.STransChatMessageNotify2Client;
import fire.pb.util.Misc;
import mkdb.Procedure;

public class CircleTask {
	public static final Set<Long> inBattleRoles = Collections.synchronizedSet(new HashSet<Long>());
	public static void addCatchItDstService(final long roleid, final int npcid, java.util.List<Integer> result) {
		xbean.CircleTaskMap quest_map = xtable.Rolecircletask.select(roleid);
		if (quest_map == null)
			return;
		xbean.CircleTaskInfo sqinfo = quest_map.getTaskmap().get(SpecialQuestID.catchIt);
		if (null == sqinfo)
			return;
		if (sqinfo.getQueststate() != SpecialQuestState.UNDONE)
			return;
		
		if (sqinfo.getQuestclass() == CircTaskClass.CircTask_CatchIt) {
			if (sqinfo.getDstnpcid() != npcid)
				return;
			if (sqinfo.getQueststate() == SpecialQuestState.UNDONE)
				result.add(NpcServices.CATCH_IT_Battle);
		}
		return;
	}
	//添加循环任务相关的npc动态服务
	public static void addCTDynamicServices(long roleid, long npckey, java.util.List<Integer> result) {
		for(Integer curtype : CircleTaskManager.getInstance().getCircTaskTypes()) {
			final xbean.CircleTaskInfo sqinfo = selectSpecialQuestInfo(roleid, curtype);
			if (null == sqinfo || sqinfo.getQueststate() != SpecialQuestState.UNDONE)
				continue;
			
			if (sqinfo.getQuestclass() == CircTaskClass.CircTask_Mail) {
				final fire.pb.map.Npc npc = SceneNpcManager.selectNpcByKey(npckey);
				if(null == npc || sqinfo.getDstnpcid() != npc.getNpcID())
					continue;
				if (sqinfo.getQueststate() == SpecialQuestState.UNDONE) {
					//result.add(NpcServices.SEND_MAIL);
					if (sqinfo.getDoneparam() > 0) {
						result.add(sqinfo.getDoneparam());
						CircleTaskManager.logger.error("送信类任务服务id:" + sqinfo.getDoneparam());
					} else {
						CircleTaskManager.logger.error("送信类任务服务id:" + sqinfo.getDoneparam() + ",错误!");
					}
				}
			} else if (sqinfo.getQuestclass() == CircTaskClass.CircTask_ItemFind || 
					sqinfo.getQuestclass() == CircTaskClass.CircTask_PetCatch) {
				//TODO:?是否需要特殊处理,待定
			} else {
				//这里处理非自动完成的循环任务，需要往npc身上挂提交任务的动态服务。（设计方案改变，淘汰）
				
			}
		}
	}
	public static void distributeAward(final long roleid, final int awardid, final xbean.CircleTaskInfo sqinfo, final int IsDbPoint, Map<String, Object> paras) {
		if (paras == null)
			paras = new HashMap<String, Object>(20);
		
		paras.put(RewardMgr.RING, sqinfo.getRound());
		paras.put(RewardMgr.ISDBPOINT, IsDbPoint);
		
		int questType = 0;
		
		switch (sqinfo.getQuestid()) {
		case 1010000:
			questType = PAddExpProc.ZHIYE;
			break;
		case 1030000:
			questType = PAddExpProc.RICHANG;
			break;
		case 1060000:
			questType = PAddExpProc.GONGHUI_RENWU;
			break;
		case 1040000:
			questType = PAddExpProc.KAOGU;
			break;
		case 1050000:
			questType = PAddExpProc.YINGXIONGSILIAN;
			break;
		default:
			break;
		}
		
		fire.pb.activity.award.RewardMgr.getInstance().distributeAllAward(roleid, awardid, paras,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_Task, awardid,
						questType, "循环任务奖励");
	}
	
    public static java.util.Set<Integer> getCTPatrolMap(long roleid) {
		java.util.Set<Integer> ret = new java.util.HashSet<Integer>();
		for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
			final xbean.CircleTaskInfo sqinfo = selectSpecialQuestInfo(roleid, curType);
			if (null == sqinfo)
				continue;
			if (sqinfo.getQuestclass() == CircTaskClass.CircTask_Patrol) {
				if (sqinfo.getQueststate() == SpecialQuestState.UNDONE)
					ret.add(sqinfo.getDstmapid());
			}
		}
		return ret;
	}
	public static boolean getRoleSpecialQuest(long roleid, int questid, int questtype, SChatItemTips sendmsg) {
		final CircleTask sq = new CircleTask(roleid, false);
		xbean.CircleTaskInfo sqinfo = sq.getSpecialQuestInfo(questid);
		if(null == sqinfo)
			return false;
		
		OctetsStream octs = new OctetsStream();
		octs.marshal(sqinfo.getQuestid());
		octs.marshal(sqinfo.getQuesttype());
		octs.marshal(sqinfo.getQueststate());
		octs.marshal(sqinfo.getRound());
		octs.marshal(sqinfo.getSumnum());
		octs.marshal(sqinfo.getDstmapid());
		octs.marshal(sqinfo.getDstnpckey());
		octs.marshal(sqinfo.getDstnpcid());
		octs.marshal(sqinfo.getDstitemid());
		octs.marshal(sqinfo.getDstx());
		octs.marshal(sqinfo.getDsty());
		octs.marshal(sqinfo.getDstitemnum());
		octs.marshal(sqinfo.getBattlenum()); //destitemid2
		if (sqinfo.getQuestclass() == CircTaskClass.CircTask_ItemFind && sqinfo.getDoneparam() > 0) {
			octs.marshal(sqinfo.getDoneparam()); //dstitemidnum2
		} else {
			octs.marshal((int)0);
		}
		octs.marshal((long)0L); //validtime
		octs.marshal((int)0); //islogin
		fire.pb.PropRole prole = new fire.pb.PropRole(roleid, true);
		octs.marshal(prole.getLevel());
		octs.marshal(prole.getSchool());
		sendmsg.tips = octs;
		return true;
	}
	
	public static xbean.CircleTaskInfo getSpecialQuestInfo(long roleid,
			int questid) {
		xbean.CircleTaskMap quests = xtable.Rolecircletask.get(roleid);
		if (null == quests)
			return null;
		return quests.getTaskmap().get(questid);
	}
	
	public static void giveCircleTaskWheel(long roleid, CircTask conf, final fire.pb.map.Npc npc, xbean.CircleTaskInfo sqinfo) {
		if (conf.getIsopenaward() == 1) {
//			sqinfo.setOpenawardnum(sqinfo.getOpenawardnum() + 1);
//			if (sqinfo.getOpenawardnum() >= conf.getOpenawardnum()) {
//				if (conf.getOpenawardid() > 0) {
//					if(npc != null) { 
//					 new PReqFortuneWheel(roleid, npc == null ? 0 :npc.getUniqueID(), npc == null ? 0 :npc.getNpcID(), 0, 0, 0, conf.getOpenawardid(),
//							 SpecialQuestNpcMsg.SPECIAL_QUEST_FORTUNEWHEEL_MSG,null,true,NpcServices.FORTUNE_WHEEL).call();
//					} else {
//						CircleTaskManager.logger.warn("角色[" + roleid + "]循环任务" + conf.getType() + "配置了大转盘！次数:" + conf.getOpenawardnum()
//								 + ",转盘id:" + conf.getOpenawardid() + ".当前次数:" + sqinfo.getOpenawardnum() + ",但npc为null!");
//					}
//				}
//				sqinfo.setOpenawardnum(0);
//			}
			if (sqinfo.getQuesttype() == 1070004){
				if(conf.getOpenawardid() > 0) {
					CircleTaskManager.logger.info("角色[" + roleid + "]循环任务" + conf.getType() + "配置了大转盘id:" + conf.getOpenawardid());
					 new PReqFortuneWheel(roleid, npc == null ? 0 :npc.getUniqueID(), npc == null ? 0 :npc.getNpcID(), 0, 0, 0, conf.getOpenawardid(),
							 SpecialQuestNpcMsg.SPECIAL_QUEST_FORTUNEWHEEL_MSG,null,true,NpcServices.FORTUNE_WHEEL).call();
				}
			}
			CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务" + conf.getType() + "配置了大转盘！次数:" + conf.getOpenawardnum()
					 + ",转盘id:" + conf.getOpenawardid() + ".当前次数:" + sqinfo.getOpenawardnum());
		} else {
			CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务" + conf.getType() + "没有配置了大转盘,不处理.");
		}
	}
	
	public static void giveCTAward(final long roleid, final int roleLevel, final int circletype, final fire.pb.map.Npc npc, xbean.CircleTaskInfo sqinfo, Map<String, Object> paras, boolean teshu) {
		//奖励全部改成走奖励表里的id
		if (sqinfo == null)
			return;
		
		CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务" + circletype + "的" + sqinfo.getQuesttype() + "开始发奖励!");
		
		if (paras == null)
			paras = new HashMap<String, Object>(20);
		
		// 给予奖励
		final CircTask ctaward = CircleTaskManager.getInstance().getCircTask(sqinfo.getId());
		if (ctaward == null) {
			return;
		}	

		int circleMax = CircleTaskManager.getInstance().getCTCircleMax(ctaward);
		if (sqinfo.getSumnum() >= circleMax) {
			if (circletype != 1060000) {
				//发完成20环的提示消息
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ctaward.getMaxnumtips(), null);
			}
		}
				
//		final fire.pb.PropRole prole = new fire.pb.PropRole(roleid, false);
		int dayMax = ctaward.awardtime;
		int gaoJiangHuanShu = ctaward.gjhuanshu; //高奖环数
//		int petKey = prole.getFightpetkey();
		//检测双倍点数是否够给予双倍奖励
		short dpoint = RoleHookManager.getInstance().getLastDpoint(roleid);
		if (dpoint >= ctaward.doublepoint && dpoint > 0) { //有双倍点数时的处理
			new PSubDpointCntProc(roleid, ctaward.doublepoint).call();
			
			// 给予双倍奖励
			if (ctaward.getDbappendawardids() != null) {
				//发分级奖励
				int awardid = CircleTaskManager.getInstance().getCircTaskDoubleZong(sqinfo.getId(), roleLevel);
				distributeAward(roleid, awardid, sqinfo, 1, paras);
			}
			
			//给予特殊奖励
			if (ctaward.specialaward > 0 && teshu) {
				distributeAward(roleid, ctaward.specialaward, sqinfo, 1, paras);
			}
			
			if (gaoJiangHuanShu >= sqinfo.getSumnum() && gaoJiangHuanShu > 0) {
				// 给予奖励
				if (ctaward.gjaward > 0) {
					distributeAward(roleid, ctaward.gjaward, sqinfo, 1, paras);
				}
				
				//每轮高奖奖励
				if (sqinfo.getRound() == ctaward.ring) {
					if (ctaward.mlgjitemaward > 0) {
						distributeAward(roleid, ctaward.mlgjitemaward, sqinfo, 1, paras);
					}
				}
			}
			else {
				// 给予普通奖励
				if (ctaward.ptitemaward > 0) {
					distributeAward(roleid, ctaward.ptitemaward, sqinfo, 1, paras);
				}
			}
			
			//每轮双倍点数附加奖励
			if (sqinfo.getRound() == ctaward.ring) {
				if (ctaward.mldbitemaward > 0) {
					distributeAward(roleid, ctaward.mldbitemaward, sqinfo, 1, paras);
				}
			}
			
			//第10环的额外奖励			
			if (sqinfo.getRound() == ctaward.extraitemawardring) {
				if (ctaward.extraitemaward > 0) {
					distributeAward(roleid, ctaward.extraitemaward, sqinfo, 1, paras);
				}
			}
			
			//给每轮的奖励
			CircTaskConf ctc = CircleTaskManager.getCircTaskConf(sqinfo.getQuesttype());
			if (ctc == null) {
				return;
			}
			if (ctc.awardid != 0) {
				paras.put(RewardMgr.RING, sqinfo.getRound());
				paras.put(RewardMgr.ISDBPOINT, 1);
				paras.put(RewardMgr.TASKLV, ctc.tasklevel);
				fire.pb.activity.award.RewardMgr.getInstance().distributeAllAward(roleid, ctc.awardid, paras,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_Task, ctc.awardid, PAddExpProc.OTHER_QUEST, "循环任务奖励");
			}
		}
		else if ((sqinfo.getSumnum() - sqinfo.getCatchitdoubletime()) < dayMax) { //无双倍点数时的处理
			//给予特殊奖励
			if (ctaward.specialaward > 0 && teshu) {
				distributeAward(roleid, ctaward.specialaward, sqinfo, 0, paras);
			}
			
			if (gaoJiangHuanShu >= sqinfo.getSumnum() && gaoJiangHuanShu > 0) {
				// 给予奖励
				if (ctaward.gjaward > 0) {
					distributeAward(roleid, ctaward.gjaward, sqinfo, 0, paras);
				}
				
				//每轮高奖奖励
				if (sqinfo.getRound() == ctaward.ring) {
					if (ctaward.mlgjitemaward > 0) {
						distributeAward(roleid, ctaward.mlgjitemaward, sqinfo, 0, paras);
					}
				}
			}
			else {
				// 给予普通奖励
				if (ctaward.ptitemaward > 0) {
					distributeAward(roleid, ctaward.ptitemaward, sqinfo, 0, paras);
				}
			}
			
			//第10环的额外奖励			
			if (sqinfo.getRound() == ctaward.extraitemawardring) {
				if (ctaward.extraitemaward > 0) {
					distributeAward(roleid, ctaward.extraitemaward, sqinfo, 0, paras);
				}
			}
			
			//给每轮的奖励
			CircTaskConf ctc = CircleTaskManager.getCircTaskConf(sqinfo.getQuesttype());
			if (ctc == null) {
				return;
			}
			if (ctc.awardid != 0) {
				paras.put(RewardMgr.RING, sqinfo.getRound());
				paras.put(RewardMgr.ISDBPOINT, 0);
				paras.put(RewardMgr.TASKLV, ctc.tasklevel);
				fire.pb.activity.award.RewardMgr.getInstance().distributeAllAward(roleid, ctc.awardid, paras,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_Task, ctc.awardid, PAddExpProc.OTHER_QUEST, "循环任务奖励");
			}
		}
		else if (ctaward.shengwangaward > 0) {
			distributeAward(roleid, ctaward.shengwangaward, sqinfo, 0, paras);
		}
		
		//发分级奖励
		int awardLevel = CircleTaskManager.getInstance().getCircTaskZong(sqinfo.getId(), roleLevel);
		if (awardLevel != -1) {
			RewardMgr.getInstance().distributeAllAward(roleid,
					awardLevel,
					paras,
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_Task,
					awardLevel, PAddExpProc.BATTLE_AWARD,
					"循环任务奖励", true);
		}
		
		if (npc != null) {
			giveCircleTaskWheel(roleid, ctaward, npc, sqinfo);
		} else {
			giveCircleTaskWheel(roleid, ctaward, null, sqinfo);
		}
		
		fire.pb.event.Poster.getPoster().dispatchEvent(new MissionCompleteEvent(roleid, sqinfo.getQuesttype()));
		fire.pb.event.Poster.getPoster().dispatchEvent(new CircleTaskCompleteEvent(roleid, sqinfo.getQuestid()));
		//处理运营日志 公会任务
		if(1060000 != sqinfo.getQuestid())
			YYLogger.completeTaskLog(roleid, circletype, YYLogger.SUCC_FLAG, "");
		else
			YYLogger.completeActivityLog(roleid, 118, YYLogger.SUCC_FLAG, "");
	}

	public static void giveTeamLeaderAward(final long roleId, final xbean.CircleTaskInfo sqinfo, final CircTask ctaward, final int questType) {
		if (sqinfo == null)
			return;

//		Bag bag = (Bag)fire.pb.item.Module.getInstance().getItemColumn(
//				roleId, fire.pb.item.BagTypes.BAG, false);	
		
//		final fire.pb.PropRole prole = new fire.pb.PropRole(roleId, false);
		
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put(RewardMgr.IS_LEADER, 1);
		CircleTaskTeamLeaderAward teamleaderAward = CircleTaskManager.getInstance().getTeamLeaderAward(questType);
		if (teamleaderAward == null)
			return;
		int nIndex = 0;
		for (Integer itemid : teamleaderAward.itemIds) {
			//此处修改为给奖励id
			if (teamleaderAward.awardRate.get(nIndex) > Misc.getRatePercent()) {
				
				if (itemid > 0) {
					fire.pb.activity.award.RewardMgr.getInstance().distributeAllAward(roleId, itemid, paras,
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_Task, itemid, PAddExpProc.OTHER_QUEST, "循环任务奖励");
				}
//				BasicItem item = Module.getInstance().getItemManager().genBasicItem(itemid, 1, 0, null, false);
//				
//				if (bag.addItem(item, -1, Constant.ADD_ITEM_REASON, fire.log.YYLogger.COUNTER_TYPE, 0) 
//						!= AddItemResult.SUCC){
//					Message.psendMsgNotifyWhileRollback(roleId, 142338, null);
//					return;
//				}
			}
			
			nIndex ++;
		}
	}
	

	public static boolean hasSpecialQuest(long roleid, int questid) {
		final xbean.CircleTaskInfo sqinfo = selectSpecialQuestInfo(roleid,
				questid);
		if (null == sqinfo)
			return false;
		if (sqinfo.getQueststate() == SpecialQuestState.DONE
				|| sqinfo.getQueststate() == SpecialQuestState.UNDONE)
			return true;
		return false;
	}

	public static int isHaveItemCollect(long roleid) {
		for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
			final xbean.CircleTaskInfo sqinfo = selectSpecialQuestInfo(roleid, curType);
			if (null == sqinfo)
				continue;
			if (sqinfo.getQuestclass() == CircTaskClass.CircTask_ItemCollect) {
				if (sqinfo.getQueststate() == SpecialQuestState.UNDONE)
					return curType;
			}
		}
		return 0;
	}

	public static int isHaveItemFind(long roleid) {
		for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
			final xbean.CircleTaskInfo sqinfo = selectSpecialQuestInfo(roleid, curType);
			if (null == sqinfo)
				continue;
			if (sqinfo.getQuestclass() == CircTaskClass.CircTask_ItemFind) {
				if (sqinfo.getQueststate() == SpecialQuestState.UNDONE)
					return curType;
			}
		}
		return 0;
	}
	
	public static int isHaveKillMonster(long roleid) {
		for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
			final xbean.CircleTaskInfo sqinfo = selectSpecialQuestInfo(roleid, curType);
			if (null == sqinfo)
				continue;
			if (sqinfo.getQuestclass() == CircTaskClass.CircTask_KillMonster) {
				if (sqinfo.getQueststate() == SpecialQuestState.UNDONE)
					return curType;
			}
		}
		return 0;
	}
	
	public static int isHavePatrol(long roleid) {
		for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
			final xbean.CircleTaskInfo sqinfo = selectSpecialQuestInfo(roleid, curType);
			if (null == sqinfo)
				continue;
			if (sqinfo.getQuestclass() == CircTaskClass.CircTask_Patrol) {
				if (sqinfo.getQueststate() == SpecialQuestState.UNDONE)
					return curType;
			}
		}
		return 0;
	}
	
	
	public static void loadCatchItService(long roleid, java.util.List<Integer> result) {
		xbean.CircleTaskMap quest_map = xtable.Rolecircletask.select(roleid);
		if (quest_map == null)
			return;
		xbean.CircleTaskInfo sqinfo = quest_map.getTaskmap().get(SpecialQuestID.catchIt);
		if (null == sqinfo) {
			result.add(NpcServices.CATCH_IT);
			result.add(NpcServices.CATCH_IT_QUERY);
			result.add(NpcServices.CATCH_IT_BJ);
			return;
		}
		
		if (sqinfo.getQueststate() == SpecialQuestState.UNDONE) {
			result.add(NpcServices.CATCH_IT_QUERY);
		}
		else {
//			if (sqinfo.getQueststate() == SpecialQuestState.DONE || sqinfo.getRound() == 0 || sqinfo.getQueststate() == SpecialQuestState.ABANDONED) {
//				
//			}
			result.add(NpcServices.CATCH_IT);
			result.add(NpcServices.CATCH_IT_QUERY);
			result.add(NpcServices.CATCH_IT_BJ);
			return;
		}
		
//		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
//		if (null != team || !team.isTeamLeader(roleid)) {
//		}
		
		return;
	}

	
	public static void loggerCTSchoolQuestOnAbandon(long roleid, xbean.CircleTaskInfo sqinfo){
		fire.pb.PropRole prole = new fire.pb.PropRole(roleid, false);
		switch (sqinfo.getQuestclass()){
		case CircTaskClass.CircTask_Mail:{
			CircleTaskManager.logger.info("循环师门任务放弃:任务id:"+sqinfo.getQuesttype() +", 角色id:"+ roleid + ", 角色姓名:"+ prole.getName() +"角色等级:"+prole.getLevel()+ ", 角色门派ID:"+ prole.getSchool() + ", 送信目的NPCID:" + sqinfo.getDstnpcid() + ", 次数:"+sqinfo.getSumnum() + ", 环数:"+sqinfo.getRound());
		}
		break;
		case CircTaskClass.CircTask_ItemUse:{
			CircleTaskManager.logger.info("循环师门任务放弃:任务id:"+sqinfo.getQuesttype() +", 角色id:"+ roleid + ", 角色姓名:"+ prole.getName() +"角色等级:"+prole.getLevel()+ ", 角色门派ID:"+ prole.getSchool() + ", 目的道具ID:" + sqinfo.getDstitemid()+", 次数:"+sqinfo.getSumnum() + ", 环数:"+sqinfo.getRound());
		}
		break;
		}
		//log
		//doSchoolQuestLog(roleid,sqinfo,RemoteLogID.CTSCHOOL_ABANDONED);
	}

	
	public static void loggerCTSchoolQuestOnTake(long roleid, xbean.CircleTaskInfo sqinfo){
		xbean.Properties prop = xtable.Properties.select(roleid);
		switch (sqinfo.getQuestclass()){
		case CircTaskClass.CircTask_Mail:{
			CircleTaskManager.logger.info("循环师门任务领取:任务id:"+sqinfo.getQuesttype() +", 角色id:"+ roleid + ", 角色姓名:"+ prop.getRolename() +"角色等级:"+prop.getLevel()+ ", 角色门派ID:"+ prop.getSchool() + ", 送信目的NPCID:" + sqinfo.getDstnpcid() + ", 次数:"+sqinfo.getSumnum() + ", 环数:"+sqinfo.getRound());
		}
		break;
		case CircTaskClass.CircTask_ItemUse:{
			CircleTaskManager.logger.info("循环师门任务领取:任务id:"+sqinfo.getQuesttype() +", 角色id:"+ roleid + ", 角色姓名:"+ prop.getRolename() +"角色等级:"+prop.getLevel()+ ", 角色门派ID:"+ prop.getSchool() + ", 目的道具ID:" + sqinfo.getDstitemid()+", 次数:"+sqinfo.getSumnum() + ", 环数:"+sqinfo.getRound());
		}
		break;
		}
	}
	
	public static void loggerSchoolQuestOnTimeout(long roleid, xbean.CircleTaskInfo sqinfo){
		fire.pb.PropRole prole = new fire.pb.PropRole(roleid, false);
		CircleTaskManager.logger.info("师门任务超时:任务id:"+sqinfo.getQuesttype() +", 角色id:"+ roleid + ", 角色姓名:"+ prole.getName() +"角色等级:"+prole.getLevel()+ ", 角色门派ID:"+ prole.getSchool()+ ", 次数:"+sqinfo.getSumnum() + ", 环数:"+sqinfo.getRound());
		
	}

	
	public static xbean.CircleTaskInfo selectSpecialQuestInfo(long roleid,
			int questid) {
		xbean.CircleTaskMap quests = xtable.Rolecircletask.select(roleid);
		if (null == quests)
			return null;
		return quests.getTaskmap().get(questid);
	}

	
	private long roleid;


	
	private xbean.CircleTaskMap questmap;

	
	private xbean.RenXingData renxingdata;
	
	private xbean.CircleTaskCompleteTimes ctcomtimes;

	
	private boolean readonly;
	
	public CircleTask(long roleid, boolean readonly) {
		this.roleid = roleid;
		this.readonly = readonly;
		if (readonly) {
			questmap = xtable.Rolecircletask.select(roleid);
			renxingdata = xtable.Rolerenxingdata.select(roleid);
			ctcomtimes = xtable.Rolecircletaskcomplete.select(roleid);
		}
		else {
			questmap = xtable.Rolecircletask.get(roleid);
			if (questmap == null){
				questmap = xbean.Pod.newCircleTaskMap();
				xtable.Rolecircletask.insert(roleid, questmap);
			}
			renxingdata = xtable.Rolerenxingdata.get(roleid);
			if (renxingdata == null){
				renxingdata = xbean.Pod.newRenXingData();
				xtable.Rolerenxingdata.insert(roleid, renxingdata);
			}
			ctcomtimes = xtable.Rolecircletaskcomplete.get(roleid);
			if (ctcomtimes == null){
				ctcomtimes = xbean.Pod.newCircleTaskCompleteTimes();
				for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
					ctcomtimes.getCircletaskcounts().put(curType, new Integer(0));
				}
				ctcomtimes.setLogtime(Calendar.getInstance().getTimeInMillis());
				xtable.Rolecircletaskcomplete.insert(roleid, ctcomtimes);
			}
		}
	}
	
	
	public void abandonSpecialQuest(int questid) {
		final xbean.CircleTaskInfo sqinfo = getSpecialQuestInfo(questid);
		if (null == sqinfo)
			return;
		
		if (sqinfo.getQuestclass() == CircTaskClass.CircTask_ItemUse) {
			fire.pb.item.Pack bag = (fire.pb.item.Pack)
			fire.pb.item.Module.getInstance().getItemMaps( roleid, 
			fire.pb.item.BagTypes.BAG, false );
		    bag.removeItemById( sqinfo.getDstitemid(), 1, 
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 7, "放弃任务" );
		}
		
		CircleTask.loggerCTSchoolQuestOnAbandon(roleid, sqinfo);
		
		final long now = Calendar.getInstance().getTimeInMillis();
		sqinfo.setLastgiveuptime(now);	
		
		final CircTask circTask = CircleTaskManager.getInstance().getCircTask(sqinfo.getId());
		if (circTask.giveupreset == 1) { //重置环数
			sqinfo.setRound(0);
		}
		else { //不重置环数
			sqinfo.setRound(sqinfo.getRound() - 1);
		}
		
		fire.pb.mission.Module.getInstance().untrackMission( roleid, questid);
		final SRefreshSpecialQuestState sendstate = new SRefreshSpecialQuestState();
		sendstate.questid = questid;
		sendstate.state = SpecialQuestState.ABANDONED;
		Procedure.psendWhileCommit(roleid, sendstate);
	}
	public boolean addCircleTaskCompleteTimes(long roleid, int circletype) {
		if (readonly)
			return false;
		if (!fire.pb.util.DateValidate.inTheSameWeek(Calendar.getInstance().getTimeInMillis(), ctcomtimes.getLogtime())){
			CircleTaskManager.logger.info("角色[" + roleid + "]当前任务[" + circletype + "]特殊循环任务计次数过期(一周),清空");
			clearCircleTaskCompleteTimes();
		}
		Integer count =  ctcomtimes.getCircletaskcounts().get(new Integer(circletype));
		if(null == count || count.equals(0)) {
			CircleTaskManager.logger.info("角色[" + roleid + "]当前任务[" + circletype + "]特殊循环任务计次数=" + 0);
			ctcomtimes.getCircletaskcounts().put(new Integer(circletype), new Integer(1));
			ctcomtimes.setLogtime(Calendar.getInstance().getTimeInMillis());
		} else {
			CircleTaskManager.logger.info("角色[" + roleid + "]当前任务[" + circletype + "]特殊循环任务计次数=" + count);
			ctcomtimes.getCircletaskcounts().put(new Integer(circletype), new Integer(count + 1));
			ctcomtimes.setLogtime(Calendar.getInstance().getTimeInMillis());
		}
		CircleTaskManager.logger.info("角色[" + roleid + "]当前任务[" + circletype + "]特殊循环任务计次数结果:" + ctcomtimes.getCircletaskcounts().get(new Integer(circletype)));
		return true;
	}

	public boolean addRenXingCircTaskCount(long roleid, int circletype) {
		if (readonly)
			return false;
		Integer count =  renxingdata.getRenxinmap().get(new Integer(circletype));
		if(null == count || count.equals(0)) {
			renxingdata.getRenxinmap().put(new Integer(circletype), new Integer(1));
		} else {
			renxingdata.getRenxinmap().put(new Integer(circletype), new Integer(count + 1));
		}
		return true;
	}
	
	public boolean canRenXingCircTask(long roleid, int circletype) {
		final xbean.CircleTaskInfo sqinfo = getSpecialQuestInfo(circletype);
		if (sqinfo != null) {
			return CircleTaskManager.getInstance().canRenXing(sqinfo.getQuesttype());
		}
		return false;
	}
	
	public boolean clearCircleTaskCompleteTimes() {
		if (readonly)
			return false;
		//ctcomtimes.getCircletaskcounts().clear();
		for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
			ctcomtimes.getCircletaskcounts().put(curType, new Integer(0));
		}
		return true;
	}
	
	public void clearSpecialQuestInfo(xbean.CircleTaskInfo sqinfo) {
		if (readonly)
			return;
		sqinfo.setDstitemid(0);
		sqinfo.setDstitemnum(0);
		sqinfo.setDstmapid(0);
		sqinfo.setDstnpcid(0);
		sqinfo.setDstnpckey(0);
		sqinfo.setDstx(0);
		sqinfo.setDsty(0);
		sqinfo.setQueststate(0);
		sqinfo.setQuesttype(0);
		sqinfo.setBattlenum(0);
		sqinfo.setNpcname("");
		sqinfo.setBigaward(0);
		//sqinfo.setEndtime(0);
		sqinfo.setDoneparam(0);
	}
	
	
	public UseResult CTItemUse(int itemid) {
		Role role=fire.pb.map.RoleManager.getInstance().getRoleByID(roleid);
		if (role==null)
			return Commontext.UseResult.FAIL;
		
		CircleTaskManager.logger.info("角色[" + roleid + "]准备使用道具[" + itemid + "]来完成循环任务");
		java.util.Set<Integer> sqtypes = CircleTaskManager.getInstance().getCircTaskTypes();
		for(Integer curtype : sqtypes) {
			final xbean.CircleTaskInfo sqinfo = getSpecialQuestInfo(curtype);
			if (sqinfo == null 
					|| sqinfo.getQuestclass() != CircTaskClass.CircTask_ItemUse
					|| sqinfo.getQueststate() != SpecialQuestState.UNDONE
				)
				continue;
			
			if(sqinfo.getDstitemid() != itemid)
				continue;
			int mapid = sqinfo.getDstmapid();
			//int posx = ctiu.getXpos();
			//int posy = ctiu.getYpos();
			int posx = sqinfo.getDstx();
			int posy = sqinfo.getDsty();
			if (role.getMapId()!=mapid||Math.abs(role.getPos().toGridPos().getX()-posx)>5||Math.abs(role.getPos().toGridPos().getY()-posy)>5){
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 166005, null);
				break;
			}
			
			CircleTaskManager.logger.info("角色[" + roleid + "]使用道具[" + itemid + "]来完成循环任务:" + curtype + "," + sqinfo.getQuestid());
			new UpdateCircleTaskState(roleid, curtype, SpecialQuestState.DONE).call();
			//判断是否需要喊话
			CircTaskItemUse itemUse = CircleTaskManager.getInstance().getCTItemUseData(sqinfo.getSubitemtableid());
			
			mkdb.Procedure proc=new mkdb.Procedure(){
				@Override
				protected boolean process() throws Exception {
					try {
						if (itemUse.getHanhua() > 0) {
							xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
							if (clanInfo == null) {
								return false;
							}
							
							String clanaim = clanInfo.getClanaim();
							fire.pb.message.SStringRes msg = ConfigManager.getInstance()
									.getConf(fire.pb.message.SStringRes.class).get(itemUse.getHanhua());
							if (msg == null) {
								return false;
							}
							
							String msgstring = msg.msg;
							msgstring = msgstring.replaceAll("\\$parameter1\\$", clanInfo.getClanname());
							msgstring = msgstring.replaceAll("\\$parameter2\\$", String.valueOf(clanInfo.getIndex()));
							msgstring = msgstring.replaceAll("\\$parameter3\\$", clanaim);
							final java.util.ArrayList<fire.pb.talk.DisplayInfo> showinfos  = new java.util.ArrayList<fire.pb.talk.DisplayInfo>();
							ChatChannel.getInstance().process(roleid, fire.pb.talk.ChannelType.CHANNEL_CURRENT, msgstring, clanaim, showinfos, 0);
						}
					} catch (Exception e) {
						CircleTaskManager.logger.error(e.getMessage());
						return false;
					}
					return true;
				}
			};
			mkdb.Procedure.pexecuteWhileCommit(proc);
			
			
			
			
			//Message.psendMsgNotifyWhileCommit(roleid, 142299,  null);
			return Commontext.UseResult.SUCC;
		}
		return Commontext.UseResult.FAIL;
	}
	
	
	public boolean dealGaoJiangCircleTask(int circletype, CircTask ct) {
		if(readonly)
			return false;
		int curComTimes = getCircleTaskCompleteTimes(circletype);
		CircleTaskManager.logger.info("角色[" + roleid + "]当前任务[" + circletype + "]特殊循环任务当前记次数:" + curComTimes + ",第一特殊次数:" + ct.gaojiang1quest + ",第二特殊次数:" + ct.gaojiang2quest);
		if(ct.gaojiang1quest > 0 && ct.gaojiang1 == curComTimes) {
			new PAutoAddCircleTask(roleid, ct.gaojiang1quest, 0, false, true).call();
		} else if(ct.gaojiang2quest > 0 && ct.gaojiang2 == curComTimes) {
			new PAutoAddCircleTask(roleid, ct.gaojiang2quest, 0, false, true).call();
		}
		return true;
	}
	
	public boolean exeCircTaskBattle(long roleid, long npckey, int circletype, int npcid) {
		if(circletype == 1080000) {//暗夜马戏团
			xbean.AnYeData aYTD = xtable.Anyemaxituan.select(roleid);
			if (aYTD == null) {
				return false;
			} else {
				for (Map.Entry<Integer, xbean.AnYeTask> e : aYTD.getAnyetasks().entrySet()) {
					if(e.getValue().getKind() == CircTaskClass.CircTask_ChallengeNpc) {
						if(e.getValue().getState() == SpecialQuestState.UNDONE && e.getValue().getDstnpcid() == npcid) {
							final fire.pb.map.Npc npc = SceneNpcManager.selectNpcByKey(npckey);
							if(npc == null || e.getValue().getDstnpcid() != npc.getNpcID())
								continue;
							
							final int aiid = (int)e.getValue().getParam1(); //任务接取时随机的战斗配置id
							CircleTaskManager.logger.debug("玩家[" + roleid + "]" + circletype + "循环类型挑战npc:" + npcid + ",战斗配置:" + aiid);
							new PNewBattle(roleid, aiid, new AnYeChallengeNpcBattleEndHandler(circletype, roleid, npckey, e.getKey())).submit();
							break;
						}
					}
				}
				return true;
			}	
		}
		
		final xbean.CircleTaskInfo sqinfo = getSpecialQuestInfo(circletype);
		if (sqinfo == null)
			return false;
		
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(sqinfo.getId());
		if(ct == null) {
			return false;
		}
		
		if (ct.teamnum > 0) {
			fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
			if (null == team || !team.isTeamLeader(roleid)){
				return false;
			}
			
			// 全队正常状态成员 加锁
			final java.util.List<Long> members = team.getNormalMemberIds();
			if (members.size() < ct.teamnum) {
				STransChatMessageNotify2Client notify = MessageMgr.getMsgNotify(140277, npcid, null);
				gnet.link.Onlines.getInstance().send(roleid, notify);
				return false;
			}
			
			//判断有无等级不足的队员
			List<String> levelApplyNames = new ArrayList<String>(5);
			for (Long mem : team.getNormalMemberIds()) {
				PropRole memberRole = new PropRole(mem, true);
				if(memberRole.getLevel() < ct.levelmin) {
					levelApplyNames.add(memberRole.getName());
				}
			}
			
			//有人等级不足
			if (levelApplyNames.size() > 0) {
				StringBuffer strParam = new StringBuffer();
				int nIndex = 0;
				for (String name : levelApplyNames) {
					if (nIndex != (levelApplyNames.size() - 1)) {
						strParam.append(name).append(",");
					}
					else {
						strParam.append(name);
					}
				}
				
				List<String> paras = new ArrayList<String>(2);
				paras.add(strParam.toString());
				paras.add(String.valueOf(ct.levelmin));
				for (Long mem : team.getNormalMemberIds()) {
					STransChatMessageNotify2Client notify = MessageMgr.getMsgNotify(150523, 0, paras);
					gnet.link.Onlines.getInstance().send(mem, notify);
				}
				
				return false;
			}
		}
		
		if (sqinfo.getQuestclass() == CircTaskClass.CircTask_ChallengeNpc
				&& sqinfo.getDstnpcid() == npcid) {
			//是npc挑战任务，开打！
			final int aiid = sqinfo.getDoneparam(); //活的任务接取时随机的战斗配置id
			CircleTaskManager.logger.debug("玩家[" + roleid + "]" + circletype + "循环类型挑战npc:" + npcid + ",战斗配置:" + aiid);
			new PNewBattle(roleid, aiid, new CircTaskChallengeNpcBattleEndHandler(circletype, roleid, npckey)).submit();
			return true;
		}
		return false;
	}
	
	public xbean.CircleTaskCompleteTimes getCircleTaskCompleteTimes() {
		return ctcomtimes;
	}
	
	public int getCircleTaskCompleteTimes(int circletype) {
		Integer count =  ctcomtimes.getCircletaskcounts().get(new Integer(circletype));
		if(null == count || count.equals(0)) {
			return 0;
		}
		return count.intValue();
		//return ctcomtimes.getCircletaskcounts().get(new Integer(circletype)).intValue();
	}
	
	
	//获得循环类型状态 RECYCLE:未接受,DONE:已完成,UNDONE:已接受
	public int getCircleTaskState(int circletype) {
		xbean.CircleTaskMap quest_map = xtable.Rolecircletask.select(roleid);
		if (null == quest_map) {
			return SpecialQuestState.RECYCLE;
		}
		xbean.CircleTaskInfo questinfo = quest_map.getTaskmap().get(circletype);
		if (null == questinfo) {
			return SpecialQuestState.RECYCLE;
		} else {
			final long now = Calendar.getInstance().getTimeInMillis();
			if (!fire.pb.util.DateValidate.inTheSameDay(questinfo.getTakequesttime(), now)){
				return SpecialQuestState.RECYCLE;
			}
			int dayMax = CircleTaskManager.getInstance().getCTCircleMax(roleid, circletype);
			if (dayMax > 0) {
				if (questinfo.getSumnum() >= dayMax) {
					return SpecialQuestState.DONE;
				}
			}
			if (now <= questinfo.getLastgiveuptime()) {
				return SpecialQuestState.UNDONE;
			}		
			if (questinfo.getQueststate() != SpecialQuestState.SUCCESS && questinfo.getQueststate() != 0) {
				if(questinfo.getQueststate() == SpecialQuestState.ABANDONED)
					return SpecialQuestState.RECYCLE;
				return SpecialQuestState.UNDONE;
			}
			return SpecialQuestState.RECYCLE;
		}
	}
	
	public int getCircTaskPatrolBattleNum(int circletype) {
		final xbean.CircleTaskInfo sqinfo = getSpecialQuestInfo(circletype);
		if (sqinfo != null && sqinfo.getQuestclass() == CircTaskClass.CircTask_Patrol) {
			return sqinfo.getDstitemid();
		} else
			return -1;
	}
	
	
	public xbean.CircleTaskMap getQuestMap() {
		return questmap;
	}
	
	public int getRenXingCircTaskCount(long roleid, int circletype) {
		Integer count =  renxingdata.getRenxinmap().get(new Integer(circletype));
		if(null == count || count.equals(0)) {
			return 0;
		}
		return count.intValue();
	}
	
	
	public xbean.RenXingData getRenXingDate() {
		return renxingdata;
	}
	
	public int getSchoolQuestCurRound(int questid) {
		final xbean.CircleTaskInfo sqinfo = getSpecialQuestInfo(questid);
		if (null == sqinfo)
			return 0;
		if (sqinfo.getQueststate()==SpecialQuestState.UNDONE || sqinfo.getQueststate() == SpecialQuestState.DONE ) 
			return sqinfo.getRound() - 1;
		return sqinfo.getRound();
	}
	
	//通过任务类型，获得玩家身上的循环类型
	public java.util.Set<Integer> getSpecialQuestIdByType(int ctype) {
		java.util.Set<Integer> ret = new java.util.HashSet<Integer>();
		java.util.Set<Integer> cttypes = CircleTaskManager.getInstance().getCircTaskTypes();
		for(Integer curtype : cttypes) {
			final xbean.CircleTaskInfo sqinfo = getSpecialQuestInfo(curtype);
			if(sqinfo != null) {
				if(sqinfo.getQuestclass() == ctype) {
					ret.add(curtype);
				}
			}
		}
		return ret;
	}
	
	
	public xbean.CircleTaskInfo getSpecialQuestInfo(int questid) {
		if (null == questmap)
			return null;
		else
			return questmap.getTaskmap().get(questid);
	}
	
	public int getSpecialQuestState(int questid) {
		final xbean.CircleTaskInfo sqinfo = getSpecialQuestInfo(questid);
		if (sqinfo != null)
			return sqinfo.getQueststate();
		else
			return -1;
	}
	
	public boolean incCircTaskPatrolBattleNum(int circletype) {
		if (readonly)
			return false;
		final xbean.CircleTaskInfo sqinfo = getSpecialQuestInfo(circletype);
		if (sqinfo != null && sqinfo.getQuestclass() == CircTaskClass.CircTask_Patrol) {
			sqinfo.setDstitemid(sqinfo.getDstitemid() + 1);
			return true;
		}
		return false;
	}
	
	public void onItemAdded(final int itemid, final int num) {
		if (readonly)
			return;

		final Pack bag = (Pack) fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.BAG, true);

		RoleAnYeTask rayt = new RoleAnYeTask(roleid);
		for(Map.Entry<Integer, xbean.AnYeTask> e : rayt.getTasks().entrySet()){
			xbean.AnYeTask ayt = e.getValue();
			if (ayt.getState() != SpecialQuestState.DONE && ayt.getState() != SpecialQuestState.UNDONE)
				continue;
			if(ayt.getKind() == CircTaskClass.CircTask_ItemFind) {
				CircleTaskManager.logger.info("角色[" + this.roleid + "]获得" + num + "个物品[" + itemid + "],马戏团任务[" + ayt.getId() + "]需要物品[" + ayt.getDstitemid() + "]!当前任务状态:" + ayt.getState());	
				//final Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.BAG, true);
				int hasnum = bag.getItemsNumByMapping(e.getValue().getDstitemid(), 0);
				if (hasnum >= e.getValue().getDstitemnum()) {
					CircleTaskManager.logger.debug("角色[" + roleid + "]背包里已经有物品id(含映射):" + e.getValue().getDstitemid() + ",数量:" + hasnum);
					if (e.getValue().getState() == SpecialQuestState.UNDONE){
						e.getValue().setState(SpecialQuestState.DONE);
					}
				} else {
					CircleTaskManager.logger.debug("角色[" + roleid + "]背包里没有物品id(含映射):" + e.getValue().getDstitemid() + ",数量:" + hasnum);
					if (e.getValue().getState() == SpecialQuestState.DONE){
						e.getValue().setState(SpecialQuestState.UNDONE);
					}
				}
				rayt.refreshAnYeTasks(e.getKey());
				/*
				if(ayt.getDstitemid() == itemid) {
					int hasnum = bag.getItemNum(itemid, 0);
					CircleTaskManager.logger.info("角色[" + this.roleid + "]马戏团任务id:" + ayt.getId() + "需要物品[" + ayt.getDstitemid() + "],已有物品" + itemid + "个数" + hasnum);
					if (hasnum >= ayt.getDstitemnum()) {
						if (ayt.getState() == SpecialQuestState.UNDONE){
							rayt.doneTask(e.getKey());
						}
					} else {
						if (ayt.getState() == SpecialQuestState.DONE){
							rayt.undoneTask(e.getKey());
						}
					}
					rayt.refreshAnYeTasks(e.getKey());
				} else {
					if(CircleTaskManager.isItem2Item(ayt.getDstitemid(), itemid)){	
						int hasnum = bag.getItemNum(itemid, 0);
						CircleTaskManager.logger.info("角色[" + this.roleid + "]马戏团任务id:" + ayt.getId() + "需要物品[" + ayt.getDstitemid() + "],与已有物品[" + itemid + "](" + hasnum + ")存在映射关系!");
						 if (hasnum >= ayt.getDstitemnum()) {
							if (ayt.getState() == SpecialQuestState.UNDONE){
								rayt.doneTask(e.getKey());
							}
						} else {
							if (ayt.getState() == SpecialQuestState.DONE){
								rayt.undoneTask(e.getKey());
							}
						}
						rayt.refreshAnYeTasks(e.getKey());
					}
				}
				*/
			}
		}
		
		if (questmap == null)
			return;
 		for (Map.Entry<Integer, xbean.CircleTaskInfo> e : questmap.getTaskmap().entrySet()) {
			final xbean.CircleTaskInfo sqinfo = e.getValue();
			if(sqinfo.getQuestclass() == CircTaskClass.CircTask_ItemCollect
					&& sqinfo.getDstitemid() == itemid 
					&& sqinfo.getQueststate() == SpecialQuestState.DONE){
				final ItemMaps questbag =  fire.pb.item.Module.getInstance()
						.getItemMaps(roleid, fire.pb.item.BagTypes.QUEST, false);
				int hasnum = questbag.getItemNum(itemid, 0);
				int hasnum2 = bag.getItemNum(itemid, 0);
				if (hasnum + hasnum2 < sqinfo.getBattlenum()) {
					//new PAbandonSpecialQuestProc(roleid, sqinfo.getQuestid()).call();
				}else{
					boolean ret = new fire.pb.circletask.UpdateCircleTaskState(roleid, e.getKey(), SpecialQuestState.DONE).call();
					if(ret){
						final Pack bag4remove = (Pack) fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.BAG, false);
						if (bag4remove.removeItemById(itemid, hasnum2, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 7,
									"Collect Item For SpecialQuest") != hasnum2){
							CircleTaskManager.logger.debug("调试用消息： 任务 id ：" + sqinfo.getQuestid() + "任务类型:"+sqinfo.getQuesttype()
									+"  普通背包删除道具" + itemid + "出错!!!");
							}
						if (questbag.removeItemById(itemid, hasnum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 7,
								"Collect Item For SpecialQuest") != hasnum){
							CircleTaskManager.logger.debug("调试用消息： 任务 id ：" + sqinfo.getQuestid() + "任务类型:"+sqinfo.getQuesttype()
									+"  任务背包删除道具" + itemid + "出错!!!");
						}
					}
				}
			} else if (sqinfo.getQuestclass() == CircTaskClass.CircTask_ItemFind) {
				int hasnum = bag.getItemsNumByMapping(sqinfo.getDstitemid(), 0);
				if (hasnum >= sqinfo.getDstitemnum()) {
					CircleTaskManager.logger.debug("任务 id:" + sqinfo.getQuestid() + ",任务类型:"+sqinfo.getQuesttype()
						+",角色[" + roleid + "]背包里已经有物品id(含映射):" + sqinfo.getDstitemid() + ",数量:" + hasnum);
					if (sqinfo.getQueststate() == SpecialQuestState.UNDONE){
						new fire.pb.circletask.UpdateCircleTaskState(roleid, e
								.getKey(), SpecialQuestState.DONE).call();		
					}
					CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, false);
				} else {
					CircleTaskManager.logger.debug("任务 id:" + sqinfo.getQuestid() + ",任务类型:"+sqinfo.getQuesttype()
							+",角色[" + roleid + "]背包里没有物品id(含映射):" + sqinfo.getDstitemid() + ",数量:" + hasnum);
					if (sqinfo.getQueststate() == SpecialQuestState.DONE){
						new fire.pb.circletask.UpdateCircleTaskState(roleid, e
								.getKey(), SpecialQuestState.UNDONE).call();
						CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, false);
					}
				}
				
				
//				if(sqinfo.getDstitemnum() == itemid) {
//					int hasnum = bag.getItemNum(itemid, 0);
//					//stask.setDstitemnum(hasnum);
//					 if (hasnum >= sqinfo.getDstitemnum()) {
//						if (sqinfo.getQueststate() == SpecialQuestState.UNDONE){
//							new fire.pb.circletask.UpdateCircleTaskState(roleid, e
//									.getKey(), SpecialQuestState.DONE).call();
//							//BuffAgent agent = new BuffRoleImpl(roleid);
//							//agent.removeCBuffWithSP(SpecialQuestManager.ITEM_FIND_LEGEND_BUFF);
//						}
//					} else {
//						if (sqinfo.getQueststate() == SpecialQuestState.DONE)
//							new fire.pb.circletask.UpdateCircleTaskState(roleid, e
//									.getKey(), SpecialQuestState.UNDONE).call();
//					}
//					CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, false);
//				} else {
//					if(CircleTaskManager.isItem2Item(sqinfo.getDstitemid(), itemid)){
//						int hasnum = bag.getItemNum(itemid, 0);
//						//stask.setDstitemnum(hasnum);
//						 if (hasnum >= sqinfo.getDstitemnum()) {
//							if (sqinfo.getQueststate() == SpecialQuestState.UNDONE){
//								new fire.pb.circletask.UpdateCircleTaskState(roleid, e
//										.getKey(), SpecialQuestState.DONE).call();
//								//BuffAgent agent = new BuffRoleImpl(roleid);
//								//agent.removeCBuffWithSP(SpecialQuestManager.ITEM_FIND_LEGEND_BUFF);
//							}
//						} else {
//							if (sqinfo.getQueststate() == SpecialQuestState.DONE)
//								new fire.pb.circletask.UpdateCircleTaskState(roleid, e
//										.getKey(), SpecialQuestState.UNDONE).call();
//						}
//						CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, false);
//					}
//				}
			}
		}
	}
	
	
	public void onMoneyAdded(final long money) {
		
	}
	
	
	public void onPetColumnChange(final int petid) {
		if (questmap == null)
			return;
		final PetColumn pc = new PetColumn(roleid, fire.pb.pet.PetColumnTypes.PET, true);
		RoleAnYeTask rayt = new RoleAnYeTask(roleid);
		for(Map.Entry<Integer, xbean.AnYeTask> e : rayt.getTasks().entrySet()){
			xbean.AnYeTask ayt = e.getValue();
			if (ayt.getState() != SpecialQuestState.DONE && ayt.getState() != SpecialQuestState.UNDONE)
				continue;

			if(ayt.getKind() == CircTaskClass.CircTask_PetCatch) {
				int targetnum = pc.getPetNumByMapping(ayt.getDstitemid()); //获取宠物背包里非战斗的目标宠物数量，包含映射表
				if(targetnum >= ayt.getDstitemnum()) {
					if (ayt.getState() == SpecialQuestState.UNDONE){
						ayt.setState(SpecialQuestState.DONE);
					}
				} else {
					if (ayt.getState() == SpecialQuestState.DONE){
						ayt.setState(SpecialQuestState.UNDONE);
					}
				}
				rayt.refreshAnYeTasks(e.getKey());
			}
		}
		
		for (Map.Entry<Integer, xbean.CircleTaskInfo> e : questmap.getTaskmap().entrySet()) {
			final xbean.CircleTaskInfo sqinfo = e.getValue();
			if (sqinfo.getQueststate() != SpecialQuestState.DONE && sqinfo.getQueststate() != SpecialQuestState.UNDONE)
				continue;
			if (sqinfo.getQuesttype() != SpecialQuestType.CatchPet
					&& sqinfo.getQuestclass() != CircTaskClass.CircTask_PetCatch)
				continue;
			
			int targetnum = pc.getPetNumByMapping(sqinfo.getDstitemid()); //获取宠物背包里非战斗的目标宠物数量，包含映射表
			if(targetnum >= sqinfo.getDstitemnum()) {
				if (sqinfo.getQueststate() == SpecialQuestState.UNDONE ){
					new fire.pb.circletask.UpdateCircleTaskState(roleid, e.getKey(), SpecialQuestState.DONE).call();
				}
			} else {
				if (sqinfo.getQueststate() == SpecialQuestState.DONE)
					new fire.pb.circletask.UpdateCircleTaskState(roleid, e.getKey(), SpecialQuestState.UNDONE).call();
			}
			CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, false);
		}
	}
	
	public boolean recycle(int questid) {
		return true;
	}
	
	public boolean sendCTSchoolNpcMail(int npcid) {
		java.util.Set<Integer> mailct = getSpecialQuestIdByType(CircTaskClass.CircTask_Mail);
		for(Integer curtype : mailct) {
			final xbean.CircleTaskInfo sqinfo = getSpecialQuestInfo(curtype);
			if (sqinfo == null || sqinfo.getQueststate() != SpecialQuestState.UNDONE
					|| sqinfo.getDstnpcid() != npcid)
				continue;
			fire.pb.PropRole prole = new fire.pb.PropRole(roleid, false);
			int masterid = fire.pb.circletask.CircleTaskManager.getInstance().getMasterMap().get(prole.getSchool()).masterid;
			sqinfo.setDstnpcid(masterid);
			return new UpdateCircleTaskState(roleid, curtype,
						SpecialQuestState.DONE).call();
		}
		return false;
		
		
		
	}
	
	public boolean sendCTSchoolNpcMail(int npcid, int serviceid) {
		for (Map.Entry<Integer, xbean.CircleTaskInfo> e : questmap.getTaskmap().entrySet()) {
			final xbean.CircleTaskInfo sqinfo = e.getValue();
			if (sqinfo == null)
				continue;
			if (sqinfo.getQuestclass() == CircTaskClass.CircTask_Mail) {
				if(sqinfo.getQueststate() != SpecialQuestState.UNDONE || 
						sqinfo.getDstnpcid() != npcid ||
						sqinfo.getDoneparam() != serviceid)
					continue;
				fire.pb.PropRole prole = new fire.pb.PropRole(roleid, false);
				int masterid = fire.pb.circletask.CircleTaskManager.getInstance().getMasterMap().get(prole.getSchool()).masterid;
				sqinfo.setDstnpcid(masterid);
				return new UpdateCircleTaskState(roleid, sqinfo.getQuestid(), SpecialQuestState.DONE).call();
			}
		}
		return false;
	}
	
	public boolean stepState(int questid, int state) {
		if (readonly)
			return false;
		xbean.CircleTaskInfo sqinfo = getSpecialQuestInfo(questid);
		if (null == sqinfo)
			return false;
		if (sqinfo.getQueststate() != state && 
				sqinfo.getQueststate() == SpecialQuestState.UNDONE) {
			// 变身
			fire.pb.mission.UtilHelper.clearTransformID(roleid, sqinfo.getQuestid());
		}
		sqinfo.setQueststate(state);
		final SRefreshQuestData data = new SRefreshQuestData();
		data.questid = questid;
		data.datas.put(RefreshDataType.STATE, (long)state);
		if (state == SpecialQuestState.DONE){
			//循环任务
			if(CircleTaskManager.getInstance().getCircTaskTypes().contains(sqinfo.getQuestid())) {
				CircTaskConf ctc = CircleTaskManager.getCircTaskConf(sqinfo.getQuesttype());
				if(ctc == null){
					return false;
				}
				if(ctc.autocomp == 0)
				{
					//int npcid = fire.pb.specialquest.SpecialQuestManager.getInstance().getMasterMap().get(prole.getSchool()).masterid;
					int npcid = sqinfo.getSubmitnpc();
					sqinfo.setDstnpckey(0);
					sqinfo.setDstnpcid(npcid);
				}
				else
				{
					//sqinfo.setDstnpckey(0);
					//sqinfo.setDstnpcid(0);
					
					//SpecialQuestManager.getInstance().refreshQuestState(roleid,
					//		questid, SpecialQuestState.SUCCESS);
				}
				data.datas.put(RefreshDataType.DEST_NPD_ID, (long)sqinfo.getDstnpcid());
				data.datas.put(RefreshDataType.DEST_NPD_KEY, (long)sqinfo.getDstnpckey());
			}

			// 帮派日常任务相关目的npc的更新
//			if (sqinfo.getQuestid() == SpecialQuestID.factiondailyquestid){
//				if ( sqinfo.getQuesttype() == SpecialQuestType.FactionPublicize ) {
//					ArrayList<String> params = new ArrayList<String>();
//					xbean.FactionInfo fi = xtable.Factions.select( GradeRelationManager.getInstance().getFactionKeyByRoleid(roleid) );
//					if ( fi != null ) {
//						params.add( fi.getFactionname() );
//						params.add( String.valueOf( GradeRelationManager.getInstance().getFactionKeyByRoleid(roleid)) );
//						params.add( fi.getFactionaim() );
//						params.add( prole.getName() );
//						params.add( String.valueOf( prole.getTitleID() ) );
//						
//					//	fire.pb.talk.Message.getInstance().sendMsgScreen( , msg, showinfos, showkeytype)
//						fire.pb.talk.Message.psendMsgNotifyWhileCommit( roleid, 
//								SpecialQuestNpcMsg.FACTION_PUBLICIZE_MSG, roleid, params);
//					}
//				}
//				int npcid = fire.pb.util.FireProp.getIntValue(SpecialQuestManager.sqprop, "fire.pb.specialquest.factionmanager");
//				sqinfo.setDstnpcid(npcid);
//				data.datas.put(RefreshDataType.DEST_NPD_ID, (long)sqinfo.getDstnpcid());
//				
//			}
		}
		if (state == SpecialQuestState.UNDONE){
//			if (sqinfo.getQuestid() == SpecialQuestID.schoolquestid){
//				if (sqinfo.getQuesttype() == SpecialQuestType.CatchPet
//						|| sqinfo.getQuesttype() == SpecialQuestType.BuyItem)
//					sqinfo.setDstnpcid(0);
//					data.datas.put(RefreshDataType.DEST_NPD_ID, (long)sqinfo.getDstnpcid());
//			}
			
			sqinfo.setDstnpckey(0);
			sqinfo.setDstnpcid(0);
			data.datas.put(RefreshDataType.DEST_NPD_ID, (long)sqinfo.getDstnpcid());
			data.datas.put(RefreshDataType.DEST_NPD_KEY, (long)sqinfo.getDstnpckey());
			CircleTaskManager.getInstance().refreshQuestState(roleid, questid, SpecialQuestState.UNDONE);
		}
		mkdb.Procedure.psendWhileCommit(roleid, data);
		return true;
	}
	
	public boolean submit(int questid, fire.pb.map.Npc npc) {
		final xbean.CircleTaskInfo sqinfo = getSpecialQuestInfo(questid);
		if (null == sqinfo || npc == null)
			return false;
		if (sqinfo.getDstnpcid() != npc.getNpcID())
			return false;
		//这里优先处理新版循环任务提交
		if (sqinfo.getQueststate() == SpecialQuestState.DONE) {
			if (sqinfo.getQuestclass() == CircTaskClass.CircTask_ItemFind) {
				fire.pb.npc.SSubmit2Npc send = new fire.pb.npc.SSubmit2Npc();
				send.questid = questid;
				send.npckey = npc.getUniqueID();
				send.submittype =  fire.pb.npc.SubmitType.ITEM;
				Procedure.psendWhileCommit(roleid, send);
				return true;
			} else if (sqinfo.getQuestclass() == CircTaskClass.CircTask_PetCatch) {
				fire.pb.npc.SSubmit2Npc send = new fire.pb.npc.SSubmit2Npc();
				send.questid = questid;
				send.npckey = npc.getUniqueID();
				send.submittype = fire.pb.npc.SubmitType.PET;
				Procedure.psendWhileCommit(roleid, send);
				return true;
			} else {
				//非自动完成，需要提交的循环任务的通用处理，直接完成该任务
				final int roleLevel = xtable.Properties.selectLevel(roleid);
				giveCTAward(roleid, roleLevel, sqinfo.getQuestid(), npc, sqinfo, null, false);
				//clearSpecialQuestInfo(sqinfo);
				CircleTaskManager.getInstance().refreshQuestState(roleid, questid, SpecialQuestState.SUCCESS);
				final CircTask ctq = CircleTaskManager.getInstance().getCircTask(roleid, sqinfo.getQuestid());
				if (ctq == null) {
					CircleTaskManager.logger.error("角色[" + roleid + "]循环类型:" + sqinfo.getQuestid() + "配置文件错误！");
					return false;
				}
				int circleMax = CircleTaskManager.getInstance().getCTCircleMax(ctq);
				if (ctq.getTaskcnt() == 2) {
					sqinfo.setSumnum(sqinfo.getSumnum() + 1);
					if (sqinfo.getSumnum() >= circleMax) {
						if (questid == 1060000) {
							fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150551, null);
						}
						else {
							fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ctq.getMaxnumtips(), null);
						}
					}
				}
				addCircleTaskCompleteTimes(roleid, questid);
				final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleid, questid);
				if(null == ct)
					return false;
				dealGaoJiangCircleTask(questid, ct);
				
				fire.pb.course.CourseManager.achieveCourse(roleid, fire.pb.course.CourseType.ACTIVE_COURSE, ct.getActiveid(), 0);
				
				RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
				if (actrole != null && ct.getActiveid() > 0){
					actrole.handleActiveLivenessData(ct.getActiveid());
				}
				
				CircTaskConf ctc = CircleTaskManager.getCircTaskConf(sqinfo.getQuesttype());
				if(null == ctc){
					CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务" + sqinfo.getQuesttype() + "配置null！");
					return false;
				}
				if(ctc.getCompletetip() > 0) {
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ctc.getCompletetip(), null);
				}
				clearSpecialQuestInfo(sqinfo);
				if(ct.gjhuanshu == sqinfo.getSumnum()) {
					return true;
				}
				try {
					new PAutoAddCircleTask(roleid, sqinfo.getQuestid()).call();
				} catch (Exception e) {
					CircleTaskManager.logger.error("上一环完成发送自动循环师门任务异常:" , e);
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean submitItemPet(int questid, fire.pb.map.Npc npc,
			java.util.ArrayList<fire.pb.npc.SubmitUnit> things) {
		if (readonly)
			return false;
		if (null == things || things.isEmpty() || npc == null)
			return false;

		final xbean.CircleTaskInfo sqinfo = getSpecialQuestInfo(questid);
		if (sqinfo.getQueststate() != SpecialQuestState.DONE){
			return false;
		}
		
		if (sqinfo.getQuestclass() == CircTaskClass.CircTask_ItemFind) {
			int itemfindid = sqinfo.getGivetasknpcid();//这里获取的是寻找物品子表的索引id
			CircTaskItemFind ctif =  CircleTaskManager.getCircTaskItemFindConf(itemfindid);
			if (null != ctif) {
				if (1 == ctif.needquality) {
					//需要品质，寻找物品(生活技能)
					final fire.pb.PropRole role = new fire.pb.PropRole(roleid, true);
					int roleLevel = role.getLevel();
					//计算出目标品质
					int targetquality = roleLevel * ctif.qualitya / 1000 + ctif.qualityb;
					CircleTaskManager.logger.debug("角色[" + roleid + "]当前计算需求品质为:" + targetquality + ".");
					boolean isdouble = false;
					for (fire.pb.npc.SubmitUnit unit : things) {
						final fire.pb.item.ItemMaps bag = fire.pb.item.Module
								.getInstance().getItemMaps(roleid,
										fire.pb.item.BagTypes.BAG, false);
						int curitemkey = unit.key;
						int curitemnum = unit.num;
						ItemBase targetitem = bag.getItem(curitemkey);
						if(null != targetitem){
							if(targetitem.getItemId() != sqinfo.getDstitemid() &&
									false == CircleTaskManager.isItem2Item(sqinfo.getDstitemid(), targetitem.getItemId())){
								CircleTaskManager.logger.debug("调试用消息： 任务id"+sqinfo.getQuestid()+"任务类型:"+sqinfo.getQuesttype()+"提交物品id不对！");
								return false;
								}
							if(targetitem instanceof EquipItem){
								EquipItem submititem = (EquipItem)targetitem;
								if(submititem.getEquipAttr().getDiamonds().size() > 0){
									fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 166001, null);
									CircleTaskManager.logger.debug("调试用消息： 任务id"+sqinfo.getQuestid()+"任务类型:"+sqinfo.getQuesttype()+"提交装备有镶嵌宝石,禁止提交！");
									return false;
								}
							}
							if(targetitem instanceof FoodItem){
								FoodItem submititem = (FoodItem)targetitem;
								int curquality = submititem.getQuality();
								if(targetquality <= curquality){
									//记录物品品质，加成奖励
									isdouble = true;
								} else {
									isdouble = false;
								}
								if (bag.removeItemWithKey(curitemkey, curitemnum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 7, "Submit Item For SpecialQuest") != curitemnum){
									CircleTaskManager.logger.debug("调试用消息： 任务 id ：" + sqinfo.getQuestid() +"任务类型:"+sqinfo.getQuesttype()+"  删除道具出错!!!");
									return false;
								}
							}
						}					
					}
					//giveSchoolQuestAward(roleid, npc, sqinfo);
					if(isdouble) {
						giveCTAward(roleid, roleLevel, sqinfo.getQuestid(), npc, sqinfo, null, true);
						fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150541, null);
					}
					else {
						giveCTAward(roleid, roleLevel, sqinfo.getQuestid(), npc, sqinfo, null, false);
					}
					//clearSpecialQuestInfo(sqinfo);
					CircleTaskManager.getInstance().refreshQuestState(roleid, questid, SpecialQuestState.SUCCESS);
					addCircleTaskCompleteTimes(roleid, questid);
					final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleid, questid);
					if(null == ct) {
						CircleTaskManager.logger.error("角色[" + roleid + "]循环类型:" + questid + "配置文件错误！");
						return false;
					}
					RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
					if (actrole != null && ct.getActiveid() > 0){
						actrole.handleActiveLivenessData(ct.getActiveid());
					}
					dealGaoJiangCircleTask(questid, ct);
					
					fire.pb.course.CourseManager.achieveCourse(roleid, fire.pb.course.CourseType.ACTIVE_COURSE, ct.getActiveid(), 0);
					
					CircTaskConf ctc = CircleTaskManager.getCircTaskConf(sqinfo.getQuesttype());
					if(null == ctc){
						CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务" + sqinfo.getQuesttype() + "配置null！");
						return false;
					}
					if(ctc.getCompletetip() > 0) {
						fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ctc.getCompletetip(), null);
					}
					clearSpecialQuestInfo(sqinfo);
					if (ct.getTaskcnt() == 2) {
						sqinfo.setSumnum(sqinfo.getSumnum() + 1);
						int circleMax = CircleTaskManager.getInstance().getCTCircleMax(ct);
						if (sqinfo.getSumnum() >= circleMax) {
							if (questid == 1060000) {
								fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150551, null);
							}
							else {
								fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ct.getMaxnumtips(), null);
							}
						}
					}
					if(ct.gjhuanshu == sqinfo.getSumnum()) {
						return true;
					}
					try {
						//new PAddAutoCircTaskSchool(roleid).call();
						new PAutoAddCircleTask(roleid, sqinfo.getQuestid()).call();
					} catch (Exception e) {
						CircleTaskManager.logger.error("上一环完成发送自动循环师门任务异常：" , e);
					}
					return true;				
				} else if (0 == ctif.needquality) {
					//不需要品质，寻找物品
					
					for (fire.pb.npc.SubmitUnit unit : things) {
						final fire.pb.item.ItemMaps bag = fire.pb.item.Module
								.getInstance().getItemMaps(roleid,
										fire.pb.item.BagTypes.BAG, false);
						int curitemkey = unit.key;
						int curitemnum = unit.num;
						ItemBase targetitem = bag.getItem(curitemkey);
						if(null != targetitem){
							if(targetitem.getItemId() != sqinfo.getDstitemid() &&
									false == CircleTaskManager.isItem2Item(sqinfo.getDstitemid(), targetitem.getItemId())){
								CircleTaskManager.logger.debug("调试用消息： 任务id"+sqinfo.getQuestid()+"任务类型:"+sqinfo.getQuesttype()+"提交物品id不对！");
								return false;
							}
							if(targetitem instanceof EquipItem){
								EquipItem submititem = (EquipItem)targetitem;
								if(submititem.getEquipAttr().getDiamonds().size() > 0){
									fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 166001, null);
									CircleTaskManager.logger.debug("调试用消息： 任务id"+sqinfo.getQuestid()+"任务类型:"+sqinfo.getQuesttype()+"提交装备有镶嵌宝石,禁止提交！");
									return false;
								}
							}
							if (bag.removeItemWithKey(curitemkey, curitemnum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 7, "Submit Item For SpecialQuest") != curitemnum){
								CircleTaskManager.logger.debug("调试用消息： 任务 id ：" + sqinfo.getQuestid() +"任务类型:"+sqinfo.getQuesttype()+"  删除道具出错!!!");
								return false;
							}
						}					
					}
					//giveSchoolQuestAward(roleid, npc, sqinfo);
					final int roleLevel = xtable.Properties.selectLevel(roleid);
					if (ctif.teshu == 1)
						giveCTAward(roleid, roleLevel, sqinfo.getQuestid(), npc, sqinfo, null, true);
					else
						giveCTAward(roleid, roleLevel, sqinfo.getQuestid(), npc, sqinfo, null, false);
					//clearSpecialQuestInfo(sqinfo);
					CircleTaskManager.getInstance().refreshQuestState(roleid, questid, SpecialQuestState.SUCCESS);
					addCircleTaskCompleteTimes(roleid, questid);
					final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleid, questid);	
					if (ct == null) {
						CircleTaskManager.logger.error("角色[" + roleid + "]循环类型:" + questid + "配置文件错误！");
						return false;
					}
					RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
					if (actrole != null && ct.getActiveid() > 0){
						actrole.handleActiveLivenessData(ct.getActiveid());
					}
					dealGaoJiangCircleTask(questid, ct);
					
					fire.pb.course.CourseManager.achieveCourse(roleid, fire.pb.course.CourseType.ACTIVE_COURSE, ct.getActiveid(), 0);
					
					CircTaskConf ctc = CircleTaskManager.getCircTaskConf(sqinfo.getQuesttype());
					if(null == ctc){
						CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务" + sqinfo.getQuesttype() + "配置null！");
						return false;
					}
					if(ctc.getCompletetip() > 0) {
						fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ctc.getCompletetip(), null);
					}
					clearSpecialQuestInfo(sqinfo);
					if (ct.getTaskcnt() == 2) {
						sqinfo.setSumnum(sqinfo.getSumnum() + 1);
						int circleMax = CircleTaskManager.getInstance().getCTCircleMax(ct);
						if (sqinfo.getSumnum() >= circleMax) {
							if (questid == 1060000) {
								fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150551, null);
							}
							else {
								fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ct.getMaxnumtips(), null);
							}
						}
					}
					if(ct.gjhuanshu == sqinfo.getSumnum()) {
						return true;
					}
					try {
						new PAutoAddCircleTask(roleid, sqinfo.getQuestid()).call();
					} catch (Exception e) {
						CircleTaskManager.logger.error("上一环完成发送自动循环师门任务异常：" , e);
					}
					return true;
				}
			}
		} else if (sqinfo.getQuestclass() == CircTaskClass.CircTask_PetCatch) {
			final fire.pb.pet.PetColumn petcol = new fire.pb.pet.PetColumn(
					roleid, PetColumnTypes.PET, false);
			int dstpetid = sqinfo.getDstitemid();
			final xbean.PetInfo xpet = petcol.getPetInfo(things.get(0).key);//petcol.getPetInfoById(dstpetid);	  
			if (null == xpet || 
					(xpet.getId() != dstpetid && false == CircleTaskManager.isItem2Item(dstpetid, xpet.getId()))){
				CircleTaskManager.logger.debug("调试用消息： 任务id"+sqinfo.getQuestid()+"任务类型:"+sqinfo.getQuesttype()+"提交宠物出错");
				return false;
			} else {
				if(petcol.removePet(xpet.getKey(),PetColumn.REMOVE_REASON_TASK,questid) < 0)
					return false;
				//giveSchoolQuestAward(roleid, npc,sqinfo);
				final int roleLevel = xtable.Properties.selectLevel(roleid);
				giveCTAward(roleid, roleLevel, sqinfo.getQuestid(), npc, sqinfo, null, false);
				//clearSpecialQuestInfo(sqinfo);
				CircleTaskManager.getInstance().refreshQuestState(roleid, questid, SpecialQuestState.SUCCESS);
				addCircleTaskCompleteTimes(roleid, questid);
				final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleid, questid);
				if (ct == null) {
					CircleTaskManager.logger.error("角色[" + roleid + "]循环类型:" + questid + "配置文件错误！");
					return false;
				}
				RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
				if (actrole != null && ct.getActiveid() > 0){
					actrole.handleActiveLivenessData(ct.getActiveid());
				}
				dealGaoJiangCircleTask(questid, ct);
				
				fire.pb.course.CourseManager.achieveCourse(roleid, fire.pb.course.CourseType.ACTIVE_COURSE, ct.getActiveid(), 0);
				
				CircTaskConf ctc = CircleTaskManager.getCircTaskConf(sqinfo.getQuesttype());
				if(null == ctc){
					CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务" + sqinfo.getQuesttype() + "配置null！");
					return false;
				}
				if(ctc.getCompletetip() > 0) {
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ctc.getCompletetip(), null);
				}
				clearSpecialQuestInfo(sqinfo);
				if (ct.getTaskcnt() == 2) {
					sqinfo.setSumnum(sqinfo.getSumnum() + 1);
					int circleMax = CircleTaskManager.getInstance().getCTCircleMax(ct);
					if (sqinfo.getSumnum() >= circleMax) {
						if (questid == 1060000) {
							fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150551, null);
						}
						else {
							fire.pb.talk.MessageMgr.sendMsgNotify(roleid, ct.getMaxnumtips(), null);
						}
					}
				}
				if(ct.gjhuanshu == sqinfo.getSumnum()) {
					return true;
				}
				try {
					//new PAddAutoCircTaskSchool(roleid).call();
					new PAutoAddCircleTask(roleid, sqinfo.getQuestid()).call();
				} catch (Exception e) {
					CircleTaskManager.logger.error("上一环完成发送自动循环师门任务异常：" , e);
				}
				return true;
			}
		}
		return true;
	}
}
