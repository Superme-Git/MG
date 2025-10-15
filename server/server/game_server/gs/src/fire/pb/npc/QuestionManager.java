package fire.pb.npc;

import java.util.HashMap;
import java.util.Map;

import fire.pb.main.ConfigManager;
import fire.pb.mission.Squestions;

public class QuestionManager
{
	public static int ActivityAnswerQuestionNum = 10; //每次活动答题的数量 by changhao
	
	public static int ActivityQuestionRewardNum = 5; //回答对几道题给奖励  by changhao
	
	private final static QuestionManager _instance = new QuestionManager();
	
	private Map<Integer,QuestionLib> libs = new HashMap<Integer, QuestionLib>();
	
	public Map<Integer, fire.pb.mission.SActivityQuestion> activityquestions = new HashMap<Integer, fire.pb.mission.SActivityQuestion>();
	
	public int[] activityquestionids;
	
	private Map<Integer,Squestions> allquestion ;
	
	private int ActivityQuestionNum; //活动答题的数量 by changhao
	
	public static QuestionManager getInstance()
	{
		return _instance;
	}
	
	private QuestionManager(){}
	
	public void init()
	{
		allquestion = ConfigManager.getInstance().getConf(Squestions.class);
		for(Squestions squestion : allquestion.values())
		{
			QuestionLib lib = libs.get(squestion.questionsid);
			if(lib == null)
			{
				lib = new QuestionLib(squestion.questionsid);
				libs.put(lib.libId, lib);
			}
			lib.questions.put(squestion.id, squestion);
		}
		
		Map<Integer, fire.pb.mission.SActivityQuestion> questions = ConfigManager.getInstance().getConf(fire.pb.mission.SActivityQuestion.class);
		
		ActivityQuestionNum = 0;
		for (fire.pb.mission.SActivityQuestion question : questions.values())
		{
			if (question.step == 0)
			{
				ActivityQuestionNum ++;
				activityquestions.put(question.id, question);
			}
		}
		
		activityquestionids = new int[ActivityQuestionNum];
		
		int count = 0;
		for (Integer id : activityquestions.keySet())
		{
			activityquestionids[count] = id;
			count ++;	
		}
		
		fire.pb.common.SCommon c1 = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(224);
		if (c1 != null)
		{
			ActivityAnswerQuestionNum = Integer.parseInt(c1.getValue());
		}
		
		fire.pb.common.SCommon c2 = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(235);
		if (c2 != null)
		{
			ActivityQuestionRewardNum = Integer.parseInt(c2.getValue());
		}
	}
	
	
	public int GetActivityQuestionNum()
	{
		return ActivityQuestionNum;
	}

	public Map<Integer, QuestionLib> getQuestionLibs()
	{
		return libs;
	}


	public Map<Integer, Squestions> getAllQuestions()
	{
		return allquestion;
	}
	
	
}


