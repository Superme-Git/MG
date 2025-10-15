package fire.pb.mission.activelist;

import gnet.link.Onlines;
import mkdb.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.log.YYLogger;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.activity.ActivityConfNew;
import fire.pb.activity.ActivityConfNew4D;
import fire.pb.activity.award.RewardMgr;
import fire.pb.attr.AttrType;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.game.MoneyType;
import fire.pb.main.ConfigManager;
import fire.pb.mission.activelist.SimpleActivityInfo;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.pb.talk.MessageMgr;
import fire.pb.timer.ActivityManager;
import fire.pb.timer.SScheculedActivity;
	
public class RoleLiveness{
	public final static int SCHOOL_QUEST = 1;//师门任务
	public final static int ZUDUIFUBEN = 2;//组队副本
	public final static int BINGFENGWANGZUO = 3;//冰封王座
	public final static int JJC = 4;//竞技场
	
	public final static int LEITAI = 10;//擂台切磋给活跃度
	
	public final static int OFFLINE_HOOK = 99;//离线挂机
	
	public final static int FENG_YAO = 105;//封印古神
	
	public final static int BING_FENG = 119;//冰封王座
	public final static int FUBEN = 120;//精英副本
	public final static int ANYEMAXITUAN = 121;//暗夜马戏团
	public final static int SHARE = 122;//分享
	public final static int SHARE2 = 123;//分享2
	
	public final static int LONG_JUNTUAN = 201;//巨龙军团
	
	public final static int WINNER = 208;//冠军试炼
	
	public final static int ANSWER_QUESTION = 213; //答题 by changhao
	public final static int IMPEXAMVILL = 214;//智力试练全村统考
	public final static int IMPEXAMPROV = 215;//智力试练全省统考
	public final static int IMPEXAMSTATE = 216;//智力试练全国统考
	
	public final static int GONG_HUI_FU_BEN = 218;
	
	public final static int PVP1 = 240;
	public final static int PVP3 = 241;
	public final static int PVP5 = 242;
	public final static int CROSS_PVP3 = 243;
	
	public final static int CLANFIGHT_PRE = 280; //公会战预备 by changhao
	public final static int CLANFIGHT_START = 281; //公会战开始 by changhao

	public static RoleLiveness getRoleLiveness(long roleId){
		return getRoleLiveness(roleId,false);
	}
	
	public static RoleLiveness getRoleLiveness(long roleId, boolean readonly){
		xbean.NewPlayActiveDay xnpad = null;
		if(!readonly)
			xnpad = xtable.Rolesplayactive.get(roleId);
		else
			xnpad = xtable.Rolesplayactive.select(roleId);
		if(xnpad == null){
			if(readonly)
				xnpad = xbean.Pod.newNewPlayActiveDayData();
			else
				xnpad = xbean.Pod.newNewPlayActiveDay();
			xtable.Rolesplayactive.add(roleId, xnpad);
		}
		
		xbean.NewPlayActiveWeek xnpaw = null;
		if(!readonly)
			xnpaw = xtable.Rolesplayactiveweek.get(roleId);
		else
			xnpaw = xtable.Rolesplayactiveweek.select(roleId);
		if(xnpaw == null){
			if(readonly)
				xnpaw = xbean.Pod.newNewPlayActiveWeekData();
			else
				xnpaw = xbean.Pod.newNewPlayActiveWeek();
			xtable.Rolesplayactiveweek.add(roleId, xnpaw);
		}
		return new RoleLiveness(roleId, xnpad, xnpaw, readonly);
	}
	
	public static java.util.TreeMap<Integer, ActivityConfNew> getConfigActivity() {
		java.util.TreeMap<Integer, ActivityConfNew> confs = null;
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) { //普通服
			confs = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.activity.ActivityConfNew.class);
			return confs;
		} else { //点卡服
			java.util.TreeMap<Integer,ActivityConfNew4D> confs4d = ConfigManager.getInstance().getConf(ActivityConfNew4D.class);
			confs = new java.util.TreeMap<Integer, ActivityConfNew>();
			for(java.util.Map.Entry<Integer, ActivityConfNew4D> conf4d : confs4d.entrySet())
			{
				ActivityConfNew tt = new ActivityConfNew();
				tt.id=conf4d.getValue().id ;
				tt.level=conf4d.getValue().level ;
				tt.maxlevel=conf4d.getValue().maxlevel ;
				tt.maxnum=conf4d.getValue().maxnum ;
				tt.resettype=conf4d.getValue().resettype ;
				tt.maxact=conf4d.getValue().maxact ;
				tt.peract=conf4d.getValue().peract ;
				tt.getfoodid1=conf4d.getValue().getfoodid1 ;
				tt.getfoodid2=conf4d.getValue().getfoodid2 ;
				tt.getfoodid3=conf4d.getValue().getfoodid3 ;
				tt.getfoodid4=conf4d.getValue().getfoodid4 ;
				tt.getfoodid5=conf4d.getValue().getfoodid5 ;
				tt.recommend=conf4d.getValue().recommend ;
				tt.yingfu=conf4d.getValue().yingfu ;
				tt.yingfuawardid=conf4d.getValue().yingfuawardid ;
				tt.yingfutimes=conf4d.getValue().yingfutimes ;
				tt.weekrepeat=conf4d.getValue().weekrepeat ;
				confs.put(conf4d.getKey(), tt);
			}
			return confs;
		}
	}

	public static ActivityConfNew getConfigActivity(int type) {
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) { //普通服
			fire.pb.activity.ActivityConfNew activity = 
					fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.activity.ActivityConfNew.class).get(type);
			return activity;
		} else { //点卡服
			fire.pb.activity.ActivityConfNew4D activity4d = 
					fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.activity.ActivityConfNew4D.class).get(type);
			if(activity4d == null)
				return null;
			fire.pb.activity.ActivityConfNew activity = new fire.pb.activity.ActivityConfNew();
			activity.id=activity4d.id ;
			activity.level=activity4d.level ;
			activity.maxlevel=activity4d.maxlevel ;
			activity.maxnum=activity4d.maxnum ;
			activity.resettype=activity4d.resettype ;
			activity.maxact=activity4d.maxact ;
			activity.peract=activity4d.peract ;
			activity.getfoodid1=activity4d.getfoodid1 ;
			activity.getfoodid2=activity4d.getfoodid2 ;
			activity.getfoodid3=activity4d.getfoodid3 ;
			activity.getfoodid4=activity4d.getfoodid4 ;
			activity.getfoodid5=activity4d.getfoodid5 ;
			activity.recommend=activity4d.recommend ;
			activity.yingfu=activity4d.yingfu ;
			activity.yingfuawardid=activity4d.yingfuawardid ;
			activity.yingfutimes=activity4d.yingfutimes ;
			return activity;
		}
	}
	
	public static int getWeekday(long now){
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(now);
		int dayofweek = date.get(Calendar.DAY_OF_WEEK);
		if(dayofweek == Calendar.SUNDAY)
			dayofweek = 7;
		else
			dayofweek--;
		return dayofweek;
	}
	
	public static boolean isToday(int weekday,long now){
		int today = getWeekday(now);
		if(today == weekday)
			return true;
		else
			return false;
	}
	
	private final long roleId;
	private final xbean.NewPlayActiveDay xnpad;
	private final xbean.NewPlayActiveWeek xnpaw;

	private final boolean readonly;

	private RoleLiveness(long roleId, xbean.NewPlayActiveDay xnpad, xbean.NewPlayActiveWeek xnpaw, boolean readonly){
		this.roleId = roleId;
		this.xnpad = xnpad;
		this.xnpaw = xnpaw;
		this.readonly = readonly;
	}

	//加活动计数
	public int addActiveNum(int activeId){
		if(readonly){
			RoleLivenessManager.logger.error("Readonly时，不能调用addActive");
			return -1;
		}
		checkAndResetActiveData();
		
		ActivityConfNew conf = RoleLivenessManager.getInstance().getActivityConfNew(activeId);
		if(null == conf) {
			RoleLivenessManager.logger.error("角色[" + roleId + "]没有找到配置id为:" + activeId + "的活动！");
			return 0;
		}
		if(conf.getResettype() == 1) {
			xbean.PlayActiveData data = xnpad.getActives().get(activeId);
			if(data == null){
				data = xbean.Pod.newPlayActiveData();
				data.setId(activeId);
				data.setCount(0);
				data.setActiveness(0);
				xnpad.getActives().put(activeId, data);	
			}
			data.setCount(data.getCount() + 1);
			xnpad.setTime(Calendar.getInstance().getTimeInMillis());
			
			Integer actCount = xnpad.getActivescount().get(activeId);
			if (actCount == null)
				actCount = 0;
			
			actCount ++;
			xnpad.getActivescount().put(activeId, actCount);
			
			RoleLivenessManager.logger.info("角色[" + roleId + "]加活动[" + activeId + "]计数:" + data.getCount());
			
			// 运营日志 活动计数
			YYLogger.completeActivityLog(roleId, activeId, YYLogger.SUCC_FLAG, "");			
			return data.getCount();
		} else {
			xbean.PlayActiveData data = xnpaw.getActives().get(activeId);
			if(data == null){
				data = xbean.Pod.newPlayActiveData();
				data.setId(activeId);
				data.setCount(0);
				data.setActiveness(0);
				xnpaw.getActives().put(activeId, data);	
			}
			data.setCount(data.getCount() + 1);
			xnpaw.setTime(Calendar.getInstance().getTimeInMillis());
			
			RoleLivenessManager.logger.info("角色[" + roleId + "]加活动[" + activeId + "]计数:" + data.getCount());
			return data.getCount();
		}
	}
	
	//加指定次数活动计数
	public int addActiveNum(int activeId, int count){
		if(readonly){
			RoleLivenessManager.logger.error("Readonly时，不能调用addActive");
			return -1;
		}
		checkAndResetActiveData();
		
		ActivityConfNew conf = RoleLivenessManager.getInstance().getActivityConfNew(activeId);
		if(null == conf) {
			RoleLivenessManager.logger.error("角色[" + roleId + "]没有找到配置id为:" + activeId + "的活动！");
			return 0;
		}
		if(conf.getResettype() == 1) {
			xbean.PlayActiveData data = xnpad.getActives().get(activeId);
			if(data == null){
				data = xbean.Pod.newPlayActiveData();
				data.setId(activeId);
				data.setCount(0);
				data.setActiveness(0);
				xnpad.getActives().put(activeId, data);	
			}
			data.setCount(data.getCount() + count);
			xnpad.setTime(Calendar.getInstance().getTimeInMillis());
			
			Integer actCount = xnpad.getActivescount().get(activeId);
			if (actCount == null)
				actCount = 0;
			
			actCount += count;
			xnpad.getActivescount().put(activeId, actCount);
			
			return data.getCount();
		} else {
			xbean.PlayActiveData data = xnpaw.getActives().get(activeId);
			if(data == null){
				data = xbean.Pod.newPlayActiveData();
				data.setId(activeId);
				data.setCount(0);
				data.setActiveness(0);
				xnpaw.getActives().put(activeId, data);	
			}
			data.setCount(data.getCount() + count);
			xnpaw.setTime(Calendar.getInstance().getTimeInMillis());
			return data.getCount();
		}
	}
	
	//加指定活动的活跃度
	public float addActiveLiveness(int activeId, float count){
		if(readonly){
			RoleLivenessManager.logger.error("Readonly时，不能调用addActiveActiveness");
			return -1;
		}
		checkAndResetActiveData();
		ActivityConfNew conf = RoleLivenessManager.getInstance().getActivityConfNew(activeId);
		if(null == conf) {
			RoleLivenessManager.logger.error("角色[" + roleId + "]没有找到配置id为:" + activeId + "的活动！");
			return 0;
		}
		if(conf.getResettype() == 1){
			xbean.PlayActiveData data = xnpad.getActives().get(activeId);
			if(data == null){
				data = xbean.Pod.newPlayActiveData();
				data.setId(activeId);
				data.setCount(0);
				data.setActiveness(0);
				xnpad.getActives().put(activeId, data);
			}
			data.setActiveness(data.getActiveness() + count);
			xnpad.setTime(Calendar.getInstance().getTimeInMillis());
			RoleLivenessManager.logger.info("角色[" + roleId + "]加活动[" + activeId + "]活跃度" + count+ ",结果:" + data.getActiveness());
			return data.getActiveness();
			}
		else{
			RoleLivenessManager.logger.info("角色[" + roleId + "]加活动[" + activeId + "]活跃度" + 0 + ",resettype=1");
			return 0;
		}
	}
	
	//加活跃度
	public float addLiveness(float addcount){
		if(readonly){
			RoleLivenessManager.logger.error("Readonly时，不能调用addActiveness");
			return 0;
		}
		if(addcount <= 0){
			return 0;
		}
		checkAndResetActiveData();
		xnpad.setActiveness(xnpad.getActiveness() + addcount);
		xnpad.setTime(Calendar.getInstance().getTimeInMillis());
		
		//List<String> parameters = new ArrayList<String>(); 
		//parameters.add(addcount+"");
		//Message.sendMsgNotify(roleId, 145658, parameters);
		
		//计算活力 通过活跃度增加 by changhao
		xbean.Properties prop = xtable.Properties.get(roleId);
		int level = prop.getLevel();
		int energy = prop.getEnergy();
		energy = LiveSkillManager.getInstance().CalcEnergyByActiveNess(energy, level, addcount);
		prop.setEnergy(energy);
		
		SRefreshRoleData refresh = new SRefreshRoleData();
		refresh.datas.put(AttrType.ACTIVENESS, (float)xnpad.getActiveness());
		refresh.datas.put(AttrType.ENERGY, (float)energy);
		if(!refresh.datas.isEmpty())
			mkdb.Procedure.psendWhileCommit(roleId, refresh);
		
		YYLogger.OpTokenGetLog(roleId, YYLoggerTuJingEnum.tujing_Value_itemuseget, MoneyType.MoneyType_Activity,
				(int)addcount, (int)xnpad.getActiveness(), new fire.log.beans.ItemBean());
		
		RoleLivenessManager.logger.info("角色[" + roleId + "]加活跃度:" + addcount);
		return addcount;
	}
	
	//加活跃度(兼容老旧接口)
	public int addActivenessAwardToRole(int addcount, YYLoggerTuJingEnum way){
		if(readonly){
			RoleLivenessManager.logger.error("Readonly时，不能调用addActivenessAwardToRole");
			return 0;
		}
		if(addcount <= 0){
			return 0;
		}
		checkAndResetActiveData();
		xnpad.setActiveness(xnpad.getActiveness() + addcount);
		xnpad.setTime(Calendar.getInstance().getTimeInMillis());
		
		//计算活力 通过活跃度增加 by changhao
		xbean.Properties prop = xtable.Properties.get(roleId);
		int level = prop.getLevel();
		int energy = prop.getEnergy();
		int energybefore = energy;
		energy = LiveSkillManager.getInstance().CalcEnergyByActiveNess(energy, level, addcount);
		prop.setEnergy(energy);
		
		SRefreshRoleData refresh = new SRefreshRoleData();
		refresh.datas.put(AttrType.ACTIVENESS, (float)xnpad.getActiveness());
		refresh.datas.put(AttrType.ENERGY, (float)energy);
		if(!refresh.datas.isEmpty())
			mkdb.Procedure.psendWhileCommit(roleId,refresh);
		
		//运营日志
		fire.log.YYLogger.OpTokenUseLog(roleId, way, MoneyType.MoneyType_Activity,
				energybefore, energy, new fire.log.beans.ItemBean());
		
		return addcount;
	}
	
	/**
	 * GM命令计算当天剩余盈福经验
	 */
	public void calcYingFuExpGM() {
		try {
			if (xnpad == null) {
				RoleLivenessManager.logger.info(new StringBuffer().append("xnpad is null:").append(roleId));
				return;
			}
			
			int days = 0;
			final long now = Calendar.getInstance().getTimeInMillis();
			if (xnpad.getYingfutime() == 0) {
				xnpad.setYingfutime(now);
			}
			
			//判断上次到现在有多少天
			days = 1;

			//计算一共有多少盈福经验
			long yingfuExp = 0;
			
			int roleLevel = xtable.Properties.selectLevel(roleId);
			
			//盈福经验要用到的参数
			Map<String, Object> paras = new HashMap<String, Object>(20);
			paras.put(RewardMgr.RING, 1);
			paras.put(RewardMgr.ISDBPOINT, 0);
			paras.put(RewardMgr.IS_LEADER, 0);
			paras.put(RewardMgr.TEAM_NUM, 1);
			paras.put(RewardMgr.TEAM_LEVEL, roleLevel);
			paras.put(RewardMgr.FUBEN_LEVEL, roleLevel/10*10);
			paras.put(RewardMgr.MAIN_MONSTER_LEVEL, roleLevel);
			
			Map<Integer, ActivityConfNew> activityMap = RoleLiveness.getConfigActivity();
			for (ActivityConfNew activity : activityMap.values()) {
				if (activity.yingfu == 0)
					continue;
				
				if (roleLevel < activity.level)
					continue;
				
				Integer activeCount = xnpad.getActivescount().get(activity.id);
				if (activeCount != null) {
					if (activeCount >= activity.yingfutimes)
						continue;
					int ncount = 0;
					if (days == 1) {
						ncount = activity.yingfutimes - activeCount;
					}
					else {
						ncount = activity.yingfutimes * (days - 1) + (activity.yingfutimes - activeCount);
					}
					
					//计算该角色会获取多少盈福经验
					long exp = RewardMgr.getInstance().getAwardExpdistributeAllAward(roleId, activity.yingfuawardid, paras, true);
					if (exp != 0) {
						yingfuExp += (exp * ncount * RoleLivenessManager.YingFuZheSunXishu / 100);
					}
				}
				else {
					//计算该角色会获取多少盈福经验
					long exp = RewardMgr.getInstance().getAwardExpdistributeAllAward(roleId, activity.yingfuawardid, paras, true);
					if (exp != 0) {
						yingfuExp += (exp * activity.yingfutimes * days * RoleLivenessManager.YingFuZheSunXishu / 100);
					}
				}
			}

			long addYingfu = yingfuExp;
			if (yingfuExp > 0) {
				yingfuExp += xnpad.getYingfuexp();
				if (yingfuExp > RoleLivenessManager.YingFuMax) {
					yingfuExp = RoleLivenessManager.YingFuMax;
				}
				
				xnpad.setYingfuexp(yingfuExp);
				xnpad.setYingfutime(now);
				xnpad.getActivescount().clear();
				
				List<String> para = new ArrayList<String>(2);
				para.add(String.valueOf(addYingfu));
				para.add(String.valueOf(yingfuExp));
				
				MessageMgr.psendSystemMessageToRole(roleId, 160411, para);
				StringBuffer sb = new StringBuffer();
				sb.append("Yingfu added:").append(roleId).append(" :").append(yingfuExp);
				RoleLivenessManager.logger.info(sb.toString());
			}
			else {
				xnpad.setYingfutime(now);
				xnpad.getActivescount().clear();
				
				StringBuffer sb = new StringBuffer();
				sb.append("Yingfu added:").append(roleId).append(" :").append(yingfuExp);
				RoleLivenessManager.logger.info(sb.toString());
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer();
			sb.append("Yingfu failed:").append(roleId).append(" exception:").append(e.toString());
			String str = sb.toString();
			RoleLivenessManager.logger.error(str);
		}
		return;
	}
	
	/**
	 * 计算盈福经验
	 */
	public void calcYingFuExp() {
		try {
			if (xnpad == null) {
				RoleLivenessManager.logger.info(new StringBuffer().append("xnpad is null:").append(roleId));
				return;
			}
			
			int days = 0;
			final long now = Calendar.getInstance().getTimeInMillis();
			if (xnpad.getYingfutime() == 0) {
				xnpad.setYingfutime(now);
			}
			else {
				if (fire.pb.util.DateValidate.inTheSameDay(xnpad.getYingfutime(), now)) {
					// 如果是同一天,则返回
//					PlayActiveManager.logger.info(new StringBuffer().append("YingFu is sameday:").append(roleId));
					return;
				}
			}
			
			//判断上次到现在有多少天
			days = fire.pb.util.DateValidate.getDaysBetween(xnpad.getYingfutime(), now);
			if (days == 0) {
//				PlayActiveManager.logger.info(new StringBuffer().append("YingFu is 0 days:").append(roleId));
				return;
			}

			//计算一共有多少盈福经验
			long yingfuExp = 0;
			
			int roleLevel = xtable.Properties.selectLevel(roleId);
			
			//盈福经验要用到的参数
			Map<String, Object> paras = new HashMap<String, Object>(20);
			paras.put(RewardMgr.RING, 1);
			paras.put(RewardMgr.ISDBPOINT, 0);
			paras.put(RewardMgr.IS_LEADER, 0);
			paras.put(RewardMgr.TEAM_NUM, 1);
			paras.put(RewardMgr.TEAM_LEVEL, roleLevel);
			paras.put(RewardMgr.FUBEN_LEVEL, roleLevel/10*10);
			paras.put(RewardMgr.MAIN_MONSTER_LEVEL, roleLevel);
			
			Map<Integer, Set<Integer>> weekRepeatMap = fire.pb.timer.Module.getWeekRepeatMap();
			for (int i = 0; i < days; i ++) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_YEAR, i - days);
				int dayInWeek = cal.get(Calendar.DAY_OF_WEEK);
				dayInWeek -= 1;
				if (dayInWeek <= 0)
					dayInWeek = 7;
				
				Set<Integer> weekset = weekRepeatMap.get(dayInWeek);
				if (weekset == null)
					continue;
				
				Map<Integer, ActivityConfNew> activityMap = RoleLiveness.getConfigActivity();
				for (ActivityConfNew activity : activityMap.values()) {
					if (!weekset.contains(activity.id))
						continue;
					
					if (activity.yingfu == 0)
						continue;
					
					if (roleLevel < activity.level)
						continue;
					
					Integer activeCount = xnpad.getActivescount().get(activity.id);
					if (activeCount != null) {
						if (activeCount >= activity.yingfutimes)
							continue;
						int ncount = activity.yingfutimes - activeCount;
						/*if (days == 1) {
							ncount = activity.yingfutimes - activeCount;
						}
						else {
							ncount = activity.yingfutimes * (days - 1) + (activity.yingfutimes - activeCount);
						}
						ncount = activity.yingfutimes - activeCount;
						*/
						
						//计算该角色会获取多少盈福经验
						long exp = RewardMgr.getInstance().getAwardExpdistributeAllAward(roleId, activity.yingfuawardid, paras, true);
						if (exp != 0) {
							yingfuExp += (exp * ncount * RoleLivenessManager.YingFuZheSunXishu / 100);
						}
					}
					else {
						//计算该角色会获取多少盈福经验
						long exp = RewardMgr.getInstance().getAwardExpdistributeAllAward(roleId, activity.yingfuawardid, paras, true);
						if (exp != 0) {
//							yingfuExp += (exp * activity.yingfutimes * days * RoleLivenessManager.YingFuZheSunXishu / 100);
							yingfuExp += (exp * activity.yingfutimes * RoleLivenessManager.YingFuZheSunXishu / 100);
						}
					}
				}
			}

			long addYingfu = yingfuExp;
			if (yingfuExp > 0) {
				yingfuExp += xnpad.getYingfuexp();
				if (yingfuExp > RoleLivenessManager.YingFuMax) {
					yingfuExp = RoleLivenessManager.YingFuMax;
				}
				
				xnpad.setYingfuexp(yingfuExp);
				List<String> para = new ArrayList<String>(2);
				para.add(String.valueOf(addYingfu));
				para.add(String.valueOf(yingfuExp));
				
				MessageMgr.psendSystemMessageToRole(roleId, 160411, para);
				StringBuffer sb = new StringBuffer();
				sb.append("Yingfu added:").append(roleId).append(" :").append(yingfuExp);
				RoleLivenessManager.logger.info(sb.toString());
			}
			else {
				StringBuffer sb = new StringBuffer();
				sb.append("Yingfu added:").append(roleId).append(" :").append(yingfuExp);
				RoleLivenessManager.logger.info(sb.toString());
			}
			
			xnpad.setYingfutime(now);
			xnpad.getActivescount().clear();
			
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer();
			sb.append("Yingfu failed:").append(roleId).append(" exception:").append(e.toString());
			String str = sb.toString();
			RoleLivenessManager.logger.error(str);
		}
		return;
	}
	
	//判断是否清理数据,返回true代表数据过期,已经清理,false代表数据未过期,未清理.
	public void checkAndResetActiveData(){
		if(readonly){
			throw new RuntimeException("PlayActiveRole对象在Readonly时,不能执行checkAndResetActiveData.");
		}
		final long now = Calendar.getInstance().getTimeInMillis();
		//final long now2 = System.currentTimeMillis();
		if (!fire.pb.util.DateValidate.inTheSameDay(xnpad.getTime(), now)){
			//不是同一天,清数据
			RoleLivenessManager.logger.info("角色[" + this.roleId + "]上次日活动时间:" + xnpad.getTime() + ",当前时间:" + now + ",不是同一天,清数据！");
			xnpad.setTime(now);
			xnpad.setActiveness(0);
			xnpad.getActives().clear();
			xnpad.getChests().clear();
		}
		if (!fire.pb.util.DateValidate.inTheSameWeek(xnpaw.getTime(), now)){
			RoleLivenessManager.logger.info("角色[" + this.roleId + "]上次周活动时间:" + xnpaw.getTime() + ",当前时间:" + now + ",不是同一周,清数据！");
			//不是同一周,清数据
			xnpaw.setTime(now);
			xbean.PlayActiveData anyeData = xnpaw.getActives().get(ANYEMAXITUAN);
			ActivityConfNew conf = RoleLivenessManager.getInstance().getActivityConfNew(ANYEMAXITUAN);

			xnpaw.getActives().clear();
			if (anyeData != null && anyeData.getCount() < conf.getMaxnum()) {
				RoleLivenessManager.logger.info("角色[" + this.roleId + "]上次暗夜马戏团次数:" + anyeData.getCount() + ",不清数据");
				xnpaw.getActives().put(ANYEMAXITUAN, anyeData);
			}
		}
	}
	
	//处理活动数据,加活动次数,根据配置及推荐度加活跃度
	public boolean handleActiveLivenessData(int activeId) {
		RoleLivenessManager.logger.info("角色[" + roleId + "]准备处理活动[" + activeId + "]的活跃度相关.");
		if(readonly){
			RoleLivenessManager.logger.error("Readonly时，不能调用handleActiveLivenessData");
			return false;
		}
		checkAndResetActiveData();
		ActivityConfNew conf = RoleLivenessManager.getInstance().getActivityConfNew(activeId);
		if(null == conf) {
			RoleLivenessManager.logger.error("角色[" + roleId + "]没有找到配置id为:" + activeId + "的活动！");
			return false;
		}
		if(conf.getResettype() == 1){
			if(conf.getMaxnum() > 0 && getActiveNum(activeId) >= conf.getMaxnum()) {
				RoleLivenessManager.logger.info("角色[" + roleId + "]当前活动:" + activeId + ",次数:" + getActiveNum(activeId) +  ",超过上限！");
				return false;
			}
			//该活动的推荐度
			int recommend = conf.getRecommend();
			RoleLivenessManager.logger.info("角色[" + roleId + "]活动:" + activeId + ",推荐度:" + recommend);
			//给予活跃度
			if(recommend == RoleLivenessManager.getCurrentRecommend()) {
				if(getActiveLiveness(activeId) >= conf.getMaxact() * 2) {
					if(conf.getMaxnum() < 0) //策划大爷说活跃度可以不加，次数到上限还是要记
						addActiveNum(activeId);
					if(conf.getMaxnum() > 0 && getActiveNum(activeId) < conf.getMaxnum())
						addActiveNum(activeId);
					RoleLivenessManager.logger.info("角色[" + roleId + "]当前活动活跃度:" + getActiveLiveness(activeId) + ",超过上限！");
					return false;
				}
				//增加该活动记录次数
				addActiveNum(activeId);
				//推荐活动,活跃度加倍
				float count = new Double(conf.getPeract() * 2).floatValue();
				//如果加上获得的活跃度超过最大值，就只能恰好到最大值，不超过
				if(getActiveLiveness(activeId) + count >= conf.getMaxact() * 2){
					count = conf.getMaxact() * 2 - getActiveLiveness(activeId);
				}
				addLiveness(count);
				addActiveLiveness(activeId, count);
			} else {
				if(getActiveLiveness(activeId) >= conf.getMaxact()) {
					if(conf.getMaxnum() < 0) //策划大爷说活跃度可以不加，次数到上限还是要记
						addActiveNum(activeId);
					if(conf.getMaxnum() > 0 && getActiveNum(activeId) < conf.getMaxnum())
						addActiveNum(activeId);
					RoleLivenessManager.logger.info("角色[" + roleId + "]当前活动活跃度:" + getActiveLiveness(activeId) + ",超过上限！");
					return false;
				}
				//增加该活动记录次数
				addActiveNum(activeId);
				float count = new Double(conf.getPeract()).floatValue();
				//如果加上获得的活跃度超过最大值，就只能恰好到最大值，不超过
				if(getActiveLiveness(activeId) + count >= conf.getMaxact()){
					count = conf.getMaxact() - getActiveLiveness(activeId);
				}
				addLiveness(count);
				addActiveLiveness(activeId, count);
			}
			sendActivityData();
			RoleLivenessManager.logger.info("角色[" + roleId + "]处理活动[" + activeId + "]的活跃度相关数据正常完毕！");
			return true;
		} else {
			if(conf.getMaxnum() > 0 && getActiveNum(activeId) >= conf.getMaxnum()) {
				RoleLivenessManager.logger.info("角色[" + roleId + "]当前活动:" + activeId + ",次数:" + getActiveNum(activeId) +  ",超过上限！");
				return false;
			}
			if(activeId == RoleLiveness.SHARE) {
				//122分享活动
				Map<String, Object>	paras = new HashMap<String, Object>(20);		
				paras.put(RewardMgr.ROLE_LEVEL, xtable.Properties.selectLevel(roleId));	
				fire.pb.activity.award.RewardMgr.getInstance().distributeAllAward(roleId, 5720, paras,
								fire.log.enums.YYLoggerTuJingEnum.tujing_Value_weibo, 5720,
								activeId, "分享奖励");
			} else if(activeId == RoleLiveness.SHARE2) {
				//123分享活动
				Map<String, Object>	paras = new HashMap<String, Object>(20);		
				paras.put(RewardMgr.ROLE_LEVEL, xtable.Properties.selectLevel(roleId));	
				fire.pb.activity.award.RewardMgr.getInstance().distributeAllAward(roleId, 5721, paras,
								fire.log.enums.YYLoggerTuJingEnum.tujing_Value_weibo, 5721,
								activeId, "分享2奖励");
			}
			addActiveNum(activeId);
			sendActivityData();
			RoleLivenessManager.logger.info("角色[" + roleId + "]处理活动[" + activeId + "]的活跃度相关数据正常完毕！");
			return true;
		}
	}
	
	//处理暗夜马戏团活动数据,加活动次数,根据配置及推荐度加活跃度 跨周
	public void handleActiveLivenessAnye(int activeId) {
		RoleLivenessManager.logger.info("角色[" + roleId + "]准备处理暗夜马戏团跨周相关活动[" + activeId + "]的活跃度相关.");
		if(readonly){
			RoleLivenessManager.logger.error("Readonly时，不能调用handleActiveLivenessData");
		}
		
		xbean.PlayActiveData anyeData = xnpaw.getActives().get(ANYEMAXITUAN);
		if (anyeData != null) {
			RoleLivenessManager.logger.info("角色[" + this.roleId + "]上次暗夜马戏团次数:" + anyeData.getCount() + ",清数据");
			anyeData.setCount(0);
			sendActivityData();
		}
	}
	
	
	public long decYingFuExp(long exp) {
		RoleLivenessManager.logger.info(new StringBuffer().append("Yingfu dec:").append(roleId).append(" :").append(exp));
		if (xnpad == null)
			return -1;
		
		long yingfuExp = xnpad.getYingfuexp();
		if (yingfuExp > exp) {
			xnpad.setYingfuexp(yingfuExp - exp);
			RoleLivenessManager.logger.info(new StringBuffer().append("Yingfu shengyu:").append(roleId).append(" :").append(yingfuExp - exp));
			return xnpad.getYingfuexp();
		}
		else {
			xnpad.setYingfuexp(0);
			RoleLivenessManager.logger.info(new StringBuffer().append("Yingfu empty:").append(roleId));
			return 0;
		}
	}
	
	//获取活动次数
	public int getActiveNum(int activeId){
		ActivityConfNew conf = RoleLivenessManager.getInstance().getActivityConfNew(activeId);
		if(null == conf) {
			RoleLivenessManager.logger.error("角色[" + roleId + "]没有找到配置id为:" + activeId + "的活动！");
			return 0;
		}
		if(conf.getResettype() == 1) {
			xbean.PlayActiveData data = xnpad.getActives().get(activeId);
			if(data == null){
				data = xbean.Pod.newPlayActiveData();
				data.setId(activeId);
				data.setCount(0);
				data.setActiveness(0);
				xnpad.getActives().put(activeId, data);	
			}
			return data.getCount();
		} else {
			xbean.PlayActiveData data = xnpaw.getActives().get(activeId);
			if(data == null){
				data = xbean.Pod.newPlayActiveData();
				data.setId(activeId);
				data.setCount(0);
				data.setActiveness(0);
				xnpaw.getActives().put(activeId, data);	
			}
			return data.getCount();
		}
	}
	
	//获取指定活动的活跃度
	public float getActiveLiveness(int activeId){
		ActivityConfNew conf = RoleLivenessManager.getInstance().getActivityConfNew(activeId);
		if(null == conf) {
			RoleLivenessManager.logger.error("角色[" + roleId + "]没有找到配置id为:" + activeId + "的活动！");
			return 0;
		}
		if(conf.getResettype() == 1) {
			xbean.PlayActiveData data = xnpad.getActives().get(activeId);
			if(data == null){
				data = xbean.Pod.newPlayActiveData();
				data.setId(activeId);
				data.setCount(0);
				data.setActiveness(0);
				xnpad.getActives().put(activeId, data);	
			}
			return data.getActiveness();
		} else {
			return 0;
		}
	}
	
	//获取活跃度
	public float getLiveness(){
		return xnpad.getActiveness();
	}
	
	//获取指定宝箱状态
	public int getAwardBoxState(int id){
		Integer state = xnpad.getChests().get(new Integer(id));
		if(null != state)
			return state.intValue();
		else
			return 0;
	}
	
	public xbean.NewPlayActiveDay getNewPlayActiveDay(){
		return xnpad;
	}
	
	public long getRoleId(){
		return roleId;
	}
	
	public long getYingFuExp() {
		if (xnpad == null) {
			RoleLivenessManager.logger.info(new StringBuffer().append("terrible! xnpad is null:").append(roleId));
			return 0;
		}
		//先计算一下了
		calcYingFuExp();
		
		return xnpad.getYingfuexp();
	}
	
	//打开指定的活跃度宝箱
	public boolean openAwardBox(int id){
		if(readonly){
			RoleLivenessManager.logger.error("Readonly时，不能调用openChest");
			return false;
		}
		int cheststate = getAwardBoxState(id);
		if(cheststate == 1) {
			RoleLivenessManager.logger.debug("角色[" + roleId + "]宝箱:" + id + "已经打开,不能再打开！");
			return false;
		} else if (cheststate == 0) {
			//开宝箱,发物品
			int itemid = RoleLivenessManager.getInstance().getChestItemByAct(id, getLiveness());
			if(itemid <= 0){
				RoleLivenessManager.logger.debug("角色[" + roleId + "]宝箱:" + id + ",活跃度不够或宝箱配置错误！");
				return false;
			}
				
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
			if(bag.doAddItem(itemid, 1, 0, 0, "活跃度宝箱奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_huoyuedujiangli, id) != 1){
				MessageMgr.psendMsgNotifyWhileRollback(roleId, 144875, null);
				//fire.pb.talk.Message.sendMsgNotify(roleId, 144875, null);
				return false;
			}
			fire.pb.item.ItemShuXing ia = fire.pb.item.Module.getInstance().getItemManager().getAttr(itemid);
			if (ia != null) {
				ArrayList<String> parameters = new ArrayList<String>();
				parameters.add(String.valueOf(1));
				parameters.add(ia.unit);
				parameters.add(ia.name);
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleId, 141037, parameters);
			}
			
			RoleLivenessManager.logger.info("角色[" + roleId + "]打开宝箱:" + id + ",给予物品:" + itemid + ".");
			
			setChestState(id, 1);
			sendActivityData();
			return true;
		}
		RoleLivenessManager.logger.error("角色[" + roleId + "]宝箱:" + id + "未知状态,数据错误！");
		return false;
	}

	public boolean resetActiveData(){
		RoleLivenessManager.logger.info("角色[" + this.roleId + "]日活动数据重置!readonly=" + readonly);
		if(readonly){
			return false;
		}
		calcYingFuExp();
		
		final long now = Calendar.getInstance().getTimeInMillis();
		if (!fire.pb.util.DateValidate.inTheSameDay(xnpad.getTime(), now)){
			RoleLivenessManager.logger.info("resetActiveData,角色[" + this.roleId + "]上次日活动时间:" + xnpad.getTime() + ",当前时间:" + now + ",不是同一天,清数据！");
			xnpad.setTime(Calendar.getInstance().getTimeInMillis());
			xnpad.setActiveness(0);
			xnpad.getActives().clear();
			xnpad.getChests().clear();
		}
		return true;
	}
	
	public boolean resetActiveWeekData(){
		RoleLivenessManager.logger.info("角色[" + this.roleId + "]周活动数据重置!readonly=" + readonly);
		if(readonly){
			return false;
		}
		final long now = Calendar.getInstance().getTimeInMillis();
		if (!fire.pb.util.DateValidate.inTheSameDay(xnpaw.getTime(), now)){
			RoleLivenessManager.logger.info("resetActiveWeekData,角色[" + this.roleId + "]上次周活动时间:" + xnpaw.getTime() + ",当前时间:" + now + ",不是同一天,清数据！");
		
			xbean.PlayActiveData anyeData = xnpaw.getActives().get(ANYEMAXITUAN);
			ActivityConfNew conf = RoleLivenessManager.getInstance().getActivityConfNew(ANYEMAXITUAN);
			
			xnpaw.setTime(Calendar.getInstance().getTimeInMillis());
			xnpaw.getActives().clear();

			if (anyeData != null && anyeData.getCount() < conf.getMaxnum()) {
				RoleLivenessManager.logger.info("角色[" + this.roleId + "]上次暗夜马戏团次数:" + anyeData.getCount() + ",不清数据");
				xnpaw.getActives().put(ANYEMAXITUAN, anyeData);
			}
		}
		return true;
	}
	
	//发送活动数据
	public void sendActivityData(){
		if(xnpad == null) return;
		SRefreshActivityListFinishTimes snd = new SRefreshActivityListFinishTimes();
		for(xbean.PlayActiveData data : xnpad.getActives().values()){
			SimpleActivityInfo sai = new SimpleActivityInfo(data.getCount(),data.getCount2(), new Float(data.getActiveness()).intValue());
			snd.activities.put(data.getId(), sai);
		}
		if(xnpaw != null){
			for(xbean.PlayActiveData data : xnpaw.getActives().values()){
				SimpleActivityInfo sai = new SimpleActivityInfo(data.getCount(),data.getCount2(), new Float(data.getActiveness()).intValue());
				snd.activities.put(data.getId(), sai);
			}
		}
		snd.activevalue = new Float(xnpad.getActiveness()).intValue();
		for(Map.Entry<Integer, Integer> e : xnpad.getChests().entrySet()){
			snd.chests.put(e.getKey(), e.getValue());
		}
		snd.recommend = RoleLivenessManager.getCurrentRecommend();

		// 客户端不显示GM指令强制关闭的活动
		Map<Integer, SScheculedActivity> activities = ConfigManager.getInstance().getConf(fire.pb.timer.SScheculedActivity.class);
		for (SScheculedActivity activity : activities.values()) {
			if (ActivityManager.getActivitymap().get(activity.getId()) == null) {
				if (snd.closeids.contains(activity.getActivityid()) == false) {
					snd.closeids.add(activity.getActivityid());
				}
			}
		}

		if(Transaction.current() != null){
			mkdb.Procedure.psendWhileCommit(roleId, snd);
		}else{
			Onlines.getInstance().send(roleId, snd);
		}
	}
	
	
	//设置活动计数
	public boolean setActiveNum(int activeId, int count){
		if(readonly){
			RoleLivenessManager.logger.error("Readonly时，不能调用setActive");
			return false;
		}
		checkAndResetActiveData();
		ActivityConfNew conf = RoleLivenessManager.getInstance().getActivityConfNew(activeId);
		if(null == conf) {
			RoleLivenessManager.logger.error("角色[" + roleId + "]没有找到配置id为:" + activeId + "的活动！");
			return false;
		}
		if(conf.getResettype() == 1) {
			xbean.PlayActiveData data = xnpad.getActives().get(activeId);
			if(data == null){
				data = xbean.Pod.newPlayActiveData();
				data.setId(activeId);
				data.setCount(0);
				data.setActiveness(0);
				xnpad.getActives().put(activeId, data);
			}
			data.setCount(count);
			xnpad.setTime(Calendar.getInstance().getTimeInMillis());
			return true;
		} else {
			xbean.PlayActiveData data = xnpaw.getActives().get(activeId);
			if(data == null){
				data = xbean.Pod.newPlayActiveData();
				data.setId(activeId);
				data.setCount(0);
				data.setActiveness(0);
				xnpaw.getActives().put(activeId, data);
			}
			data.setCount(count);
			xnpaw.setTime(Calendar.getInstance().getTimeInMillis());
			return true;
		}
	}
	
	//设置活动计数(设置第二个的活动次数)
	public boolean setActiveNum2(int activeId, int count){
		if(readonly){
			RoleLivenessManager.logger.error("Readonly时，不能调用setActive");
			return false;
		}
		ActivityConfNew conf = RoleLivenessManager.getInstance().getActivityConfNew(activeId);
		if(null == conf) {
			RoleLivenessManager.logger.error("角色[" + roleId + "]没有找到配置id为:" + activeId + "的活动！");
			return false;
		}
		xbean.PlayActiveData data = xnpad.getActives().get(activeId);
		if(data != null){
			data.setCount2(count);
		}
		return true;
	}
	
	
	//设置指定活动的活跃度
	public boolean setActiveLiveness(int activeId, int count){
		if(readonly){
			RoleLivenessManager.logger.error("Readonly时，不能调用setActive");
			return false;
		}
		checkAndResetActiveData();
		ActivityConfNew conf = RoleLivenessManager.getInstance().getActivityConfNew(activeId);
		if(null == conf) {
			RoleLivenessManager.logger.error("角色[" + roleId + "]没有找到配置id为:" + activeId + "的活动！");
			return false;
		}
		if(conf.getResettype() == 1){
			xbean.PlayActiveData data = xnpad.getActives().get(activeId);
			if(data == null){
				data = xbean.Pod.newPlayActiveData();
				data.setId(activeId);
				data.setCount(0);
				data.setActiveness(0);
				xnpad.getActives().put(activeId, data);
			}
			data.setActiveness(count);
			xnpad.setTime(Calendar.getInstance().getTimeInMillis());
			return true;
		} else {
			return true;
		}
	}
	
	
	//设置指定宝箱状态
	public void setChestState(int id, int state){
		xnpad.getChests().put(new Integer(id), new Integer(state));
	}
}
