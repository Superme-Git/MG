package fire.pb.instancezone.conf;

public class TriggerCondition
{
	public int saveId;	
	public int type; 	
	public String conditionjs;	
	public TriggerCondition(String str)
	{
		String[] strs = str.split("#");
		saveId = Integer.valueOf(strs[0]);
		type = Integer.valueOf(strs[1]);
		if(type == SaveCondition.CHANGE_TYPE_SUBSAVES)
			conditionjs = strs[2].substring(1, strs[2].length()-1);
	}
	
}
