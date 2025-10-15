package fire.pb.instancezone.conf;

import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;

import fire.pb.instancezone.Module;


public class SaveCondition
{
	public int saveId;	
	public static int CHANGE_TYPE_ACTIVE = 1;	
	public static int CHANGE_TYPE_COMPLETE = 2;	
	public static int CHANGE_TYPE_SUBSAVES = 3;		
	public int type; 
	
	private javax.script.ScriptEngine jsengine;	
	public SaveCondition(int saveId, int type)
	{
		this.saveId = saveId;
		this.type = type;
	}
	
	public void setSubSaves(Map<Integer,Boolean> subsaves)
	{
		javax.script.ScriptEngineManager em = new javax.script.ScriptEngineManager();
		jsengine = em.getEngineByName("JavaScript");
		Bindings bindings = jsengine.createBindings();
		for(Map.Entry<Integer,Boolean> entry : subsaves.entrySet())
			bindings.put("s"+entry.getKey(), entry.getValue());
		jsengine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
	}
	
	public boolean trigger(TriggerCondition triggercondition)
	{
		if(triggercondition.saveId != this.saveId)
			return false;
		if(triggercondition.type != this.type)
			return false;
		if(triggercondition.type == CHANGE_TYPE_SUBSAVES)
		{
			try
			{
				Object o = jsengine.eval("with(Math){" +triggercondition.conditionjs +"}");
				if(o instanceof Boolean)
					return (Boolean)o;
				else
					return false;
			} catch (Exception e)
			{
				Module.logger.error(e);
				throw new IllegalArgumentException("错误的触发脚本：" + triggercondition.conditionjs, e);
			}
		}
		return true;
	}
	

	
}
