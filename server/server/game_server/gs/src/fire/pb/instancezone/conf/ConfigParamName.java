package fire.pb.instancezone.conf;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class ConfigParamName {
	public final static String MSG_ID = "msgid";
	public final static String QUESTION_ID = "questionid";
	public final static String QUESTIONS_ID = "questionsid";
	public final static String TASK_ID = "taskid";
	public final static String BATTLE_ID = "battleid";
	public final static String SUBMIT_ITEM_IDS = "submititems";
	public final static String NPC_ID = "npcid";
	public final static String NPC_KEY = "npckey";
	public final static String NPC_SERVICE_ID = "serviceid";
	public final static String ANSWER_ID = "answerid";
	public final static String ANSWER_RIGHT = "answerright";
	public final static String BATTLE_RESULT = "battleresult";
	public final static String SAVE_ID = "saveid";
	public final static String SUB_SAVE_ID = "subsaveid";
	public final static String SUB_SAVE_COUNT = "subsavecount";
	public final static String MAP_ID = "mapid";
	public final static String POSX = "posx";
	public final static String POSY = "posy";
	public final static String ROLE_ID = "roleid";
	public final static String ROLE_IDS = "roleids";
	public final static String ACT_ID = "actid";
	public final static String COUNT = "count";

	public static Map<String, Object> parseConfigParams(String str) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (str == null || str.equals(""))
			return params;
		String[] strs = str.split(";");
		for (String paramstr : strs) {
			String[] paramstrs = paramstr.split("=");
			params.put(paramstrs[0],
					parseConfigValue(paramstrs[0], paramstrs[1]));

		}
		return params;
	}

	public static Object parseConfigValue(String name, String value) {
		if (name.equals(SUBMIT_ITEM_IDS)) {
			List<Integer> items = new LinkedList<Integer>();
			String[] strs = value.split(",");
			for (String str : strs)
				items.add(Integer.valueOf(str));
			return items;
		} else if (name.equals(ROLE_IDS)) {
			List<Long> longs = new LinkedList<Long>();
			String[] strs = value.split(",");
			for (String str : strs)
				longs.add(Long.valueOf(str));
			return longs;
		} else if (name.equals(TASK_ID)) {
			List<Integer> ints = new LinkedList<Integer>();
			String[] strs = value.split(",");
			for (String str : strs)
				ints.add(Integer.valueOf(str));
			return ints;
		} else if (name.equals(NPC_KEY) || name.equals(ROLE_ID))
			return Long.valueOf(value);
		else
			return Integer.valueOf(value);
	}

}
