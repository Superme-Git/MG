package fire.pb.activity.answerquestion;

import fire.log.LogManager;
import fire.pb.main.ConfigManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.mission.activelist.SActivityOpen;
import fire.pb.npc.QuestionManager;
import fire.pb.npc.SAskQuestion;
import fire.pb.util.Misc;

/**
 * 活动答题
 * @author changhao
 * @param <q>
 * @see
 */

public class ActivityQuestionManager<q> {

	private static ActivityQuestionManager instance = new ActivityQuestionManager();
	
	private boolean start;

	private ActivityQuestionManager()
	{
		start = false;
	}

	public static ActivityQuestionManager getInstance()
	{
		return instance;
	}

	public boolean init() throws Exception
	{
		start = true;
		
		AnswerQuestionPull();
		
		return true;
	}
	
	public void stop()
	{
		start = false;
	}
	
	public void start()
	{
		start = true;
	}
	
	public boolean IsStart()
	{
		return start;
	}
	
	public void roleLoginCheck(final long roleId, int level){
		new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception
			{
				fire.pb.mission.activelist.RoleLiveness actrole = fire.pb.mission.activelist.RoleLiveness.getRoleLiveness(roleId, true);
				if (actrole != null)
				{
					int count = actrole.getActiveNum(fire.pb.mission.activelist.RoleLiveness.ANSWER_QUESTION);
				
					fire.pb.activity.ActivityConfNew activity = RoleLiveness.getConfigActivity(fire.pb.mission.activelist.RoleLiveness.ANSWER_QUESTION);
					
					boolean start = fire.pb.activity.answerquestion.ActivityQuestionManager.getInstance().IsStart();
					if (count < activity.maxnum && start == true) //一天次数上限 by changhao
					{
						if (level < activity.level)
						{
							return false;
						}
						
			 			SActivityOpen so = new SActivityOpen();
		 				so.activityid = 213;
						
						mkdb.Procedure.psendWhileCommit(roleId, so);
						return true;						
					}
				}
				
				return false;
				
			}
		}.submit();
	}

	public void sendQuestion(long roleid, xbean.ActivityQuestion activityquestion, byte lastresult)
	{	
		// 让角色回答问题
		SAskQuestion sask = new SAskQuestion();		
		
		int questionid = 0;
		if (activityquestion.getCurquestionindex() < activityquestion.getQuestionids().size())
		{
			int curstep = activityquestion.getCurquestionstep();
			if (curstep != 0 && curstep < 10)
			{
				int cur = 0;
				while (cur < curstep)
				{
					int qq = activityquestion.getQuestionids().get(activityquestion.getCurquestionindex()) + cur;
					fire.pb.mission.SActivityQuestion q = ConfigManager.getInstance().getConf(fire.pb.mission.SActivityQuestion.class).get(qq);
					sask.rightanswer.add(q.rightanswer);

					cur ++;
				}
			}
			
			questionid = activityquestion.getQuestionids().get(activityquestion.getCurquestionindex()) + activityquestion.getCurquestionstep();
		}			

		sask.lastresult = lastresult;
		sask.questionid = questionid;
		sask.questiontype = SAskQuestion.ACTIVITY_ANSWER;
		sask.npckey = 0;
		sask.xiangguanid = activityquestion.getCurquestionstep();
		sask.lasttime = 0;
		sask.cur = activityquestion.getAnswerrighttimes();
		sask.num = activityquestion.getCurquestionindex();
		sask.totalexp = activityquestion.getTotalexp();
		sask.totalmoney = activityquestion.getTotalmoney();
		sask.grab = activityquestion.getGrabreward();
		sask.helptimes = activityquestion.getHelptimes();
		
		fire.pb.mission.SActivityQuestion q = ConfigManager.getInstance().getConf(fire.pb.mission.SActivityQuestion.class).get(questionid);
		if (q == null)
		{
			LogManager.logger.error("sendQuestion error quesetionid:["+ questionid +"]");
			return;
		}
	
		mkdb.Procedure.psendWhileCommit(roleid, sask);
	}
	
	/***
	 * 重置玩家的答题数据 by changhao
	 * 
	 */
	public void ResetActivityQuestionData(xbean.ActivityQuestion activityquestion, long now, boolean clearreward)
	{
		int[] tmp = new int[QuestionManager.getInstance().activityquestionids.length];
		System.arraycopy(QuestionManager.getInstance().activityquestionids, 0, tmp, 0, QuestionManager.getInstance().activityquestionids.length);
		
		int[] questionlists = Misc.getRandomArray(tmp, QuestionManager.ActivityAnswerQuestionNum); //随机取一些题目 by changhao
				
		activityquestion.getQuestionids().clear();
		for (int id : questionlists)
		{
			activityquestion.getQuestionids().add(id);				
		}
		
		//activityquestion.getQuestionids().addAll(questionlists);
		activityquestion.setActivityquestionstarttime(now);
		activityquestion.setCurquestionindex(0);
		activityquestion.setCurquestionstep(0);
		activityquestion.setTotalexp(0);
		activityquestion.setTotalmoney(0);
		activityquestion.setTotalanswernum(0);
		if (clearreward == true)
		{
			activityquestion.setGrabreward(3);			
		}

		activityquestion.setHelptimes(0);
		activityquestion.setAnswerrighttimes(0);
	}
	
	public void AnswerQuestionPull()
	{
		if (start == true)
		{
			SActivityOpen msg = new SActivityOpen();
			
			msg.activityid = 213;
			gnet.link.Onlines.getInstance().broadcast(msg, 997);			
		}
	}
}
