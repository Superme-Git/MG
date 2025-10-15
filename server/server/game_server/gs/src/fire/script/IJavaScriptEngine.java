package fire.script;

import javax.script.ScriptException;

public interface IJavaScriptEngine
{
	public Double getDouble(int effectId, boolean bopt);
	public Double getDouble(String arg);
	public Object get(int effectId, boolean bopt);
	public Object get(String arg);
	public Object eval(String js) throws ScriptException;
	public Double evalToDouble(String js);
	public void put(String key, Object value);
}
