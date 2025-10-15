package fire.pb.mission.instance;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class ConfigParamName
{
	//参数ID添加由策划定义
	public final static String MSG_ID = "msgid";//消息ID
	public final static String QUESTION_ID = "questionid";//题ID
	public final static String QUESTIONS_ID = "questionsid";//题库ID
	public final static String TASK_ID = "taskid";// 任务ID,list
	public final static String BATTLE_ID = "battleid";// 战斗ID
	public final static String SUBMIT_ITEM_IDS = "submititems";// 需要提交的物品ID
	public final static String NPC_ID = "npcid";//NPC ID
	public final static String NPC_KEY = "npckey";//NPC KEY
	public final static String NPC_SERVICE_ID = "serviceid";//服务ID
	public final static String ANSWER_ID = "answerid";//答案
	public final static String ANSWER_RIGHT = "answerright";//答正确与否，0不正确，1正确
	public final static String BATTLE_RESULT = "battleresult";//战斗结果
	public final static String SAVE_ID = "saveid";//进度id
	public final static String SUB_SAVE_ID = "subsaveid";//子进度id
	public final static String SUB_SAVE_COUNT = "subsavecount";//完成子进度个数
	public final static String MAP_ID = "mapid";
	public final static String POSX = "posx";
	public final static String POSY = "posy";
	public final static String ROLE_ID = "roleid";
	public final static String ROLE_IDS = "roleids";//list
	public final static String ACT_ID = "actid";//动作id
	public final static String COUNT = "count";//个数
	// XXX !!注意：如果在此定义新的配置名称，一定要在parseConfigValue方法里写对其value的解析方法
	// 如果不写，默认做Integer类型处理
	
	public static Map<String,Object> parseConfigParams(String str)
	{
		Map<String,Object> params = new HashMap<String, Object>();
		if(str == null || str.equals(""))
			return params;
		String[] strs = str.split(";");
		for(String paramstr : strs)
		{
			String[] paramstrs = paramstr.split("=");
			params.put(paramstrs[0], parseConfigValue(paramstrs[0], paramstrs[1]));
			
		}
		return params;
	}
	
	public static Object parseConfigValue(String name, String value)
	{
		if(name.equals(SUBMIT_ITEM_IDS))
		{
			List<Integer> items = new LinkedList<Integer>();
			String[] strs = value.split(",");
			for(String str : strs)
				items.add(Integer.valueOf(str));
			return items;
		}
		else if(name.equals(ROLE_IDS))
		{
			List<Long> longs = new LinkedList<Long>();
			String[] strs = value.split(",");
			for(String str : strs)
				longs.add(Long.valueOf(str));
			return longs;
		}
		else if(name.equals(TASK_ID))
		{
			List<Integer> ints = new LinkedList<Integer>();
			String[] strs = value.split(",");
			for(String str : strs)
				ints.add(Integer.valueOf(str));
			return ints;
		}
		else if(name.equals(NPC_KEY) || name.equals(ROLE_ID))
			return Long.valueOf(value);
		else
			return Integer.valueOf(value);//默认int型
	}

}
