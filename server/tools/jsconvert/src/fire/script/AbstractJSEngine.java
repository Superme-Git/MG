package fire.script;

import javax.script.ScriptException;

public class AbstractJSEngine implements IJavaScriptEngine {
	protected final javax.script.ScriptEngine engine;

	public AbstractJSEngine() {
		engine = ScriptEngineBindThreadMgr.getScriptEngine();
	}

	@Override
	public Object eval(String js) throws ScriptException {
		if (Module.getInstance().isRegisterJavaScriptExpr(js)) {
			return Module.getInstance().invokeFunction(this, js);
		}
		return engine.eval(js);
	}

	@Override
	public Double evalToDouble(String js) {
		try {
			Object o = eval(js);
			if (o == null)
				return null;
			if (o instanceof Double)
				return (Double) o;
			if (o instanceof Integer)
				return ((Integer) o).doubleValue();
			else
				throw new ScriptException("Unsupported Type: "
						+ o.getClass().toString());
		} catch (ScriptException e) {
			System.out.println("JS脚本" + js + "有错：/n" + e.toString());
			return 0.0;
		}
	}

	@Override
	public Object evalWithMath(String js) throws ScriptException {
		return engine.eval("with(Math){" + js + "}");
	}

	@Override
	public Object get(int effectId, boolean bopt) {
		String name = fire.pb.effect.Module.getInstance().getEffectNameById(
				effectId);
		if (bopt == true)
			name += "a";
		else
			name += "b";
		return get(name);
	}

	@Override
	public Object get(String arg) {
		return engine.get(arg);
	}

	@Override
	public Double getDouble(int effectId, boolean bopt) {
		String name = fire.pb.effect.Module.getInstance().getEffectNameById(
				effectId);
		if (bopt == true)
			name += "a";
		else
			name += "b";
		return getDouble(name);
	}

	@Override
	public Double getDouble(String arg) {
		Object o = engine.get(arg);
		if (o == null)
			return null;
		if (o instanceof Double)
			return (Double) o;
		if (o instanceof Float)
			return ((Float) o).doubleValue();
		if (o instanceof Integer)
			return ((Integer) o).doubleValue();
		return null;
	}

	@Override
	public javax.script.ScriptEngine getEngine() {
		return engine;
	}

	@Override
	public void put(String key, Object value) {
		engine.put(key, value);
	}
}
