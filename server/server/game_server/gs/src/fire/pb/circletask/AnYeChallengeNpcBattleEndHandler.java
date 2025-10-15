package fire.pb.circletask;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.HelpCountManage;
import fire.pb.PAddClanPointProc;
import fire.pb.PAddExpProc;
import fire.pb.battle.BasicEndHandler;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.circletask.anye.RoleAnYeTask;
import fire.pb.main.ConfigManager;

public class AnYeChallengeNpcBattleEndHandler extends BasicEndHandler{
	private int circletype;
	private final long capitalid;
	private final long npckey;
	private final int taskpos;
	private final long enterTime;
	
	public AnYeChallengeNpcBattleEndHandler(int circletype, final long capitalid, final long npckey, final int taskpos){
		super();
		this.circletype = circletype;
		this.capitalid = capitalid;
		this.npckey = npckey;
		this.taskpos = taskpos;
		this.enterTime = System.currentTimeMillis();
	}
	
	@Override
	public boolean handleAward(){
		if(circletype != 1080000)
			return false;
		
		RoleAnYeTask rayt = new RoleAnYeTask(capitalid);
		xbean.AnYeTask ayt = rayt.getTasks().get(taskpos);
		if (ayt == null)
			return false;
		if (ayt.getState() == SpecialQuestState.SUCCESS)
			return false;
		
		long now = System.currentTimeMillis();
		if (!fire.pb.util.DateValidate.inTheSameWeek(enterTime, now)) {
			//FIXME:战斗中跨周了，不再完成这个任务！
			return false;
		}
		
		//xbean.Fighter fightLeader = battle.getFighters().get(1);
		java.util.List<xbean.Fighter> roleids = super.getWinRoles();
		CircleTaskManager.logger.debug("[" + capitalid + "]" + "WinRoles:" + roleids.size());
		for (xbean.Fighter xfighter : roleids) {
			if(xfighter.getUniqueid() == this.capitalid)
			{
				rayt.handleTaskOver(taskpos);
				break;
			}
		}
		
		//拿到完成任务 人的公会信息
		Long taskRoleFaction = xtable.Properties.selectClankey(this.capitalid);//任务所有人即队长的公会id
		//援助奖励，帮队友
		StringBuilder sb = new StringBuilder();
		for (xbean.Fighter xfighter : roleids) {
			long memberid = xfighter.getUniqueid();
			if(memberid == capitalid)
				continue;
			String rolename = xtable.Properties.selectRolename(memberid);
			sb.append(rolename).append(",");
			Integer level = xtable.Properties.selectLevel(memberid);
			if(level == null || level.intValue() < 50)
				continue;
			
			Long curMemFactionkey = xtable.Properties.selectClankey(memberid);
			if(taskRoleFaction!=null&&curMemFactionkey != null && curMemFactionkey > 0 && taskRoleFaction > 0L) {
				if(curMemFactionkey != taskRoleFaction.longValue()){
					//世界援助
					//final fire.pb.PropRole prole = new fire.pb.PropRole(memberid, false);
					long addexp = HelpCountManage.getAddExpValueByHelpFight(memberid);
					long resultexp=HelpCountManage.getInstance().addExpValue(memberid, 1, "马戏团世界援助奖励");
					if(resultexp>0){
						new fire.pb.PAddExpProc(memberid, addexp, PAddExpProc.OTHER,"马戏团世界援助").call();
					}
					//添加声望奖励
					fire.pb.HelpCountManage.getInstance().addShengWangToRole(memberid,"马戏团援助添加",YYLoggerTuJingEnum.tujing_Value_huodongjiangli, 2);
				} else {
					//公会援助
					//final fire.pb.PropRole prole = new fire.pb.PropRole(memberid, false);
					long addexp = HelpCountManage.getAddExpValueByHelpFight(memberid);
					long resultexp=HelpCountManage.getInstance().addExpValue(memberid, 1, "马戏团公会援助奖励");
					if(resultexp>0){
						new fire.pb.PAddExpProc(memberid, addexp, PAddExpProc.OTHER,"马戏团公会援助").call();
					}
					//添加声望奖励
					fire.pb.HelpCountManage.getInstance().addShengWangToRole(memberid,"马戏团援助添加",YYLoggerTuJingEnum.tujing_Value_huodongjiangli, 2);
					
					int addcount = 5;
					int resultvalue=HelpCountManage.getInstance().addFactionValue(memberid, addcount, "马戏团公会援助奖励");
					if(resultvalue>0){
						new PAddClanPointProc(memberid, resultvalue, YYLoggerTuJingEnum.tujing_Value_huodongjiangli,false, "马戏团公会援助").call();
					}
					HelpCountManage.getInstance().addFactionHelpNum(memberid, "马戏团公会援助");
				}
			} else {
				//世界援助
				//final fire.pb.PropRole prole = new fire.pb.PropRole(memberid, false);
				long addexp = HelpCountManage.getAddExpValueByHelpFight(memberid);
				long resultexp=HelpCountManage.getInstance().addExpValue(memberid, 1, "马戏团公会援助奖励");
				if(resultexp>0){
					new fire.pb.PAddExpProc(memberid, addexp, PAddExpProc.OTHER,"马戏团公会援助").call();
				}
				//添加声望奖励
				fire.pb.HelpCountManage.getInstance().addShengWangToRole(memberid,"马戏团援助添加",YYLoggerTuJingEnum.tujing_Value_huodongjiangli, 2);
			}
		}
		
		//替换求助信息
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.lastIndexOf(","));
			
			AnYeMaXiTuanConf conf = ConfigManager.getInstance().getConf(AnYeMaXiTuanConf.class).get(ayt.getId());
			if (conf != null) {
				fire.pb.talk.MessageMgr.sendHelpMsg(capitalid, conf.getId(), conf.getTaskname(), sb.toString());
			}
		}
		
		return true;
	}
	
	@Override
	public boolean handleDeath() {
		super.handleDeath();
		CircleTaskManager.logger.debug("[" + capitalid + "]" + "AnYeChallengeNpcBattleEndHandler!");
		return true;
	}
}