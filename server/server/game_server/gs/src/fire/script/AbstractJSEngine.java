package fire.script;

import java.util.HashMap;

import javax.script.ScriptException;

import fire.log.Logger;
import fire.pb.PropConf;
import fire.pb.util.Parser;

public class AbstractJSEngine implements IJavaScriptEngine
{

	static Logger logger = Logger.getLogger("BATTLE");
	
	public AbstractJSEngine()
	{
	}
	
	@Override
	public Double getDouble(String arg)
	{
		Object o = get(arg);
		if(o == null) return null;
		if(o instanceof Double)
			return (Double)o;
		if(o instanceof Float)
			return ((Float)o).doubleValue();
		if(o instanceof Integer)
			return ((Integer)o).doubleValue();
		return null;
	}
	@Override
	public Object get(String arg)
	{
		if(PropConf.Battle.DEBUG_FORMULA_MODULE == 1)
		{
			return getEngine().get(arg);
		}
		else
			return getMap().get(arg);
	}
	
	@Override
	public Double getDouble(int effectId, boolean bopt)
	{
		String name = fire.pb.effect.Module.getInstance().getEffectNameById(effectId);
		if (bopt == true)
			name += "a";
		else
			name += "b";
		return getDouble(name);
	}
	
	@Override
	public Object get(int effectId, boolean bopt)
	{
		String name = fire.pb.effect.Module.getInstance().getEffectNameById(effectId);
		if (bopt == true)
			name += "a";
		else
			name += "b";
		return get(name);
	}	
	
	@Override
	public Object eval(String js) throws ScriptException
	{
		try
		{
			int funID = JsFunManager.GetFunID(js);
			if(funID > -1)
				return JsFunManager.JsFunbyID(this, null, null, funID);
	
			if(PropConf.Battle.DEBUG_FORMULA_MODULE == 1)
			{
				if (Module.getInstance().isRegisterJavaScriptExpr(js)) {
					return Module.getInstance().invokeFunction(this, js);
				}				
				logger.error("1:JS脚本需要使用js引擎编译，脚本:"+ js);
				return ScriptEngineBindThreadManager.getScriptEngine().eval(js);
			}
			logger.error("1:JS脚本调用未转换成公式的脚本:"+ js);
			return 0.0;
		}
		catch (Exception e)
		{
			AbstractJSEngine.logger.error("JS脚本"+ js+"有错：/n"+ Parser.convertStackTrace2String(e));
			return 0.0;
		}
	}

	@Override
	public Double evalToDouble(String js)
	{
		try
		{
			Object o = eval(js);
			if(o == null) return null;
			if(o instanceof Double)
				return (Double)o;
			if(o instanceof Integer)
				return ((Integer)o).doubleValue();
			else
			{
				logger.error("JS脚本"+ js+"有错：/n");
				return 0.0;
			}
		} catch (ScriptException e)
		{
			logger.error("JS脚本"+ js+"有错：/n"+ e.toString());
			return 0.0;
		}
	}

	public javax.script.ScriptEngine getEngine()
	{
		return ScriptEngineBindThreadManager.getScriptEngine();
	}
	public HashMap<String, Object> getMap()
	{
		return ParamMapBindThreadManager.getMap();
	}	
	@Override
	public void put(String key, Object value)
	{
		if(PropConf.Battle.DEBUG_FORMULA_MODULE == 1)
		{
			getEngine().put(key, value);
		}
		else
			getMap().put(key, value);
	}

}
