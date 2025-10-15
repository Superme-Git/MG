package fire.pb.activity.answerquestion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fire.log.YYLogger;
import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.award.RewardObjs;
import fire.pb.main.ConfigManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.npc.QuestionManager;
import fire.pb.npc.SAskQuestion;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.SExpMessageTips;
import fire.pb.timer.ActivityManager;
import fire.pb.timer.SScheculedActivity;
import fire.pb.timer.ScheculedActivity;
import fire.pb.util.DateUtil;
import mkdb.Procedure;

/***
 * 活动答题 by changhao
 *
 */
public class PAnswerActivityQuestion extends Procedure {

	private final long roleid;
	private final int xiangguanid;
	private final int questionid;
	private final int answerid;
		
	public PAnswerActivityQuestion(long roleId, int xiangguanid, int questionid, int answerid) {

		this.roleid = roleId;
		this.xiangguanid = xiangguanid;
		this.questionid = questionid;
		this.answerid = answerid;
	}

	@Override
	protected boolean process() throws Exception
	{
		final long now = Calendar.getInstance().getTimeInMillis();
		Date nowdate = new Date(now);
		
		SAskQuestion sAskQuestion = new SAskQuestion();
		sAskQuestion.questiontype = SAskQuestion.ACTIVITY_ANSWER;
		sAskQuestion.xiangguanid = 0;
		sAskQuestion.lastresult = -1;
		sAskQuestion.questionid = 0;
		sAskQuestion.npckey = 0;
		
		//检查是否在活动时间内 by changhao
		int dayofweek = DateUtil.getWeekDay();
		ScheculedActivity answeractivity = ActivityManager.getAnswerQuestionActivity().get(dayofweek);
		if (answeractivity != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
						
			String startTimeStr = answeractivity.startTime;
			Date startDate = sdf.parse(startTimeStr);
			String endTimeStr = answeractivity.endTime;
			Date endDate = sdf.parse(endTimeStr);
			
			int startminutes = startDate.getHours() * 60 + startDate.getMinutes();
			int endminutes = endDate.getHours() * 60 + endDate.getMinutes();
			int nowminutes = nowdate.getHours() * 60 + nowdate.getMinutes();
			
			boolean start = fire.pb.activity.answerquestion.ActivityQuestionManager.getInstance().IsStart();
			if (nowminutes < startminutes || nowminutes > endminutes || start == false)
			{
				psendWhileCommit(roleid, sAskQuestion);
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160406, null);
				return true;	
			}
		}

		xbean.ActivityQuestion activityquestion = xtable.Roleid2activityquestion.get(roleid);
		if (activityquestion == null)
		{
			return true;
		}
		
		if (activityquestion.getCurquestionindex() >= activityquestion.getQuestionids().size()) //如果整套答题结束 by changhao
		{
			sAskQuestion.totalexp = activityquestion.getTotalexp();
			sAskQuestion.totalmoney = activityquestion.getTotalmoney();
			sAskQuestion.num = activityquestion.getCurquestionindex();
			sAskQuestion.cur = activityquestion.getAnswerrighttimes();
			psendWhileCommit(roleid, sAskQuestion);
			return true;
		}
		
		int curquestionid = activityquestion.getQuestionids().get(activityquestion.getCurquestionindex()); //取得当前的题目 by changhao
		
		//xdb.Procedure.pexecuteWhileCommit(new PSendActivityAnswerQuestionHelp(roleid, curquestionid));		

		int curstep = activityquestion.getCurquestionstep();
			
		fire.pb.mission.SActivityQuestion curqeustion = ConfigManager.getInstance().getConf(fire.pb.mission.SActivityQuestion.class).get(curquestionid + curstep);
		
		if (curqeustion != null) //当前需要回答的问题 by changhao
		{					
			sAskQuestion.lastresult = -1;
			sAskQuestion.cur = 0;
			sAskQuestion.num = 0;
			sAskQuestion.totalexp = activityquestion.getTotalexp();
			sAskQuestion.totalmoney = activityquestion.getTotalmoney();
	
			sAskQuestion.lastresult = 1;
			
			int reward = 0;
			
			boolean havenextstep = false; //是否有下一步 by changhao
			if (curqeustion.rightanswer == answerid) //回答正确 by changhao
			{
				sAskQuestion.lastresult = 1;				
				//判断是否有下一步题 by changhao
				fire.pb.mission.SActivityQuestion nextquestion = ConfigManager.getInstance().getConf(fire.pb.mission.SActivityQuestion.class).get(curquestionid + curstep + 1);
				if (nextquestion != null && nextquestion.questionid == curqeustion.questionid)
				{
					if (nextquestion.step == curstep + 1) //这个题还有后续步骤 by changhao
					{
						sAskQuestion.questionid = nextquestion.id;
						activityquestion.setCurquestionstep(nextquestion.step);
						havenextstep = true;
					}
				}
				else
				{
					sAskQuestion.lastresult = 1;
					reward = curqeustion.rightrewardid;
					activityquestion.setAnswerrighttimes(activityquestion.getAnswerrighttimes() + 1);
					
					//可以领取奖励了 by changhao
					if (activityquestion.getAnswerrighttimes() >= QuestionManager.ActivityQuestionRewardNum)
					{
						if (activityquestion.getGrabreward() == 3)
						{
							activityquestion.setGrabreward(1);
						}
					}
				}					
			}				
			else
			{
				sAskQuestion.lastresult = -1;
				reward = curqeustion.errorrewardid;					
			}
			
			long money = 0;
			long exp = 0;
			long reviseExp = 0;
			long yingfuExpVal = 0;	
			if (havenextstep == false) //如果没有下一步 by changhao
			{				
				Map<String, Object> paras = new HashMap<String, Object>(10);
				paras.put(RewardMgr.ANSWER_RIGHT_TIMES, activityquestion.getAnswerrighttimes());
				Map<Integer, RewardObjs> result = RewardMgr.getInstance().distributeAllAward(roleid, reward, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_dati, 0,
						PAddExpProc.OTHER_QUEST, "答题", false);
				
				//运营日志 答题
				YYLogger.completeActivityLog(roleid, RoleLiveness.ANSWER_QUESTION, YYLogger.SUCC_FLAG, "");
		
				RewardObjs moneyaward = result.get(RewardMgr.MONEY_AWARD);
				if (moneyaward != null)
				{
					money = moneyaward.getValue();
				}
				
				RewardObjs expaward = result.get(RewardMgr.EXP_AWARD);
				if (expaward != null)
				{
					exp = expaward.getValue();
				}
				RewardObjs reviseExpAward = result.get(RewardMgr.EXP_ADD_AWARD);
				if (reviseExpAward != null)
				{
					reviseExp = reviseExpAward.getValue();
				}
				RewardObjs yingfuExpValAward = result.get(RewardMgr.EXP_YF_AWARD);
				if (yingfuExpValAward != null)
				{
					yingfuExpVal = yingfuExpValAward.getValue();
				}
				
				
				activityquestion.setTotalmoney(activityquestion.getTotalmoney() + (int)money);
				activityquestion.setTotalexp(activityquestion.getTotalexp() + (int)exp);				
				
				activityquestion.setCurquestionstep(0);
				int nextquestionindex = activityquestion.getCurquestionindex();
				nextquestionindex ++; //下一道 by changhao
				activityquestion.setCurquestionindex(nextquestionindex);
				
				if (nextquestionindex >= activityquestion.getQuestionids().size()) //全部答完 by changhao
				{
					sAskQuestion.questionid = 0;
					//PlayActiveRole actrole = PlayActiveRole.getPlayActiveRole(roleid, false);
				    //actrole.handleActiveLivenessData(PlayActiveRole.ANSWER_QUESTION);
				    
					//fire.pb.activity.answerquestion.ActivityQuestionManager.getInstance().ResetActivityQuestionData(activityquestion, now, false);
				}
				else
				{
					sAskQuestion.questionid = activityquestion.getQuestionids().get(activityquestion.getCurquestionindex()); //取得下次需要回答的题目 by changhao
				}
				
				RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid, false);
			    actrole.handleActiveLivenessData(RoleLiveness.ANSWER_QUESTION);
			}
			
			sAskQuestion.totalexp = activityquestion.getTotalexp();
			sAskQuestion.totalmoney = activityquestion.getTotalmoney();
			sAskQuestion.grab = activityquestion.getGrabreward();
			sAskQuestion.helptimes = activityquestion.getHelptimes();
			sAskQuestion.num = activityquestion.getCurquestionindex();
			sAskQuestion.cur = activityquestion.getAnswerrighttimes();
			
			psendWhileCommit(roleid, sAskQuestion);
			//发送经验 和金钱
			if (havenextstep == false)
			{
				if(yingfuExpVal>0||reviseExp!=0){
					//发送经验
					HashMap<Integer, Long> msgtips = new HashMap<Integer, Long>();
					if (yingfuExpVal > 0) msgtips.put(180003, yingfuExpVal);
					if (reviseExp > 0) msgtips.put(180004, reviseExp);
					if (reviseExp < 0) msgtips.put(180005, -reviseExp);
					SExpMessageTips expMsg = new SExpMessageTips(180001, exp, msgtips);
					mkdb.Procedure.psendWhileCommit(roleid, expMsg);
					//发送金钱
					java.util.List<String> strings = new java.util.ArrayList<String>();
					strings.add(String.valueOf(money));
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, MessageMgr.ADD_MONEY, strings);
				}else{
					java.util.List<String> strings = new java.util.ArrayList<String>();
					strings.add(String.valueOf(exp));
					strings.add(String.valueOf(money));
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 160481, strings);
						
				}	
			}
			
			//checkEnhancementTimeoutEquip
		}
		
		return true;
	}

	public long getRoleid() {
		return roleid;
	}

	public int getXiangguanid() {
		return xiangguanid;
	}

	public int getQuestionid() {
		return questionid;
	}

	public int getAnswerid() {
		return answerid;
	}	
	
	
}

