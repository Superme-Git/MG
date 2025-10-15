package fire.script;

import javax.script.ScriptException;

public interface IJavaScriptEngine {
	public Object eval(String js) throws ScriptException;

	public Double evalToDouble(String js);

	public Object evalWithMath(String js) throws ScriptException;

	public Object get(int effectId, boolean bopt);

	public Object get(String arg);

	public Double getDouble(int effectId, boolean bopt);

	public Double getDouble(String arg);

	public javax.script.ScriptEngine getEngine();

	public void put(String key, Object value);
}
