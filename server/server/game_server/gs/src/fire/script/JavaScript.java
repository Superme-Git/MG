package fire.script;

import java.util.List;

import javax.script.ScriptException;

import fire.pb.battle.Fighter;
import fire.pb.util.Parser;

public class JavaScript
{
	
	private String js;

	private Double value = null;

	private int funID  = -1;
	
	public static  boolean isNumeric(String str)
	{
		for(int i = str.length();--i>=0;)
		{
			if(!Character.isDigit(str.charAt(i))&&str.charAt(i)!='.'&&str.charAt(i)!='-')
			{
				return false;				
			}
		}
		return true;
	}
	
	public boolean isFun()
	{
		return value == null;
	}
	
	public Double GetDoubleValue()
	{
		return value;
	}
	
	public int GetfunID()
	{
		return funID;
	}
	
	public void SetfunID(int funID)
	{
		this.funID = funID;
	}
	public JavaScript(String js)
	{
		if(isNumeric(js))
		{
			value = Double.valueOf(js);
		}
		else
		{
			this.js = "with(Math){ return " +js + ";}";
			funID = JsFunManager.GetFunID(this.js);
		}
	}

	public JavaScript(String js, boolean neednoreturn)
	{
		if(isNumeric(js))
		{
			value = Double.valueOf(js);
		}
		else
		{
			this.js = "with(Math){ " +js + ";}";
			funID = JsFunManager.GetFunID(this.js);
		}
	}	
	
	public Double eval(AbstractJSEngine engine, Fighter opf, Fighter aimf)
	{
		try
		{
			if(value != null)
				return value;
			Object o = null;
			if(funID > -1)
				o = JsFunManager.JsFunbyID(engine, opf, aimf, funID);
			else
				o = engine.eval(js);
			if(o == null) return null;
			if(o instanceof Float)
				return ((Float)o).doubleValue();
			if(o instanceof Double)
				return (Double)o;
			if(o instanceof Integer)
				return ((Integer)o).doubleValue();
			if(o instanceof Long)
				return ((Long)o).doubleValue();
			if(o instanceof Boolean)
				return ((Boolean)o).booleanValue()==true?1.0:0.0;
			else
			{
				AbstractJSEngine.logger.error("JS脚本"+ js+"有错：/n");
				return 0.0;
			}
		} 
		catch (Exception e)
		{
			AbstractJSEngine.logger.error("JS脚本"+ js+"有错：/n"+ Parser.convertStackTrace2String(e));
			return 0.0;
		}
	}
	
	public Boolean evalToBoolean(AbstractJSEngine engine, Fighter opf, Fighter aimf)
	{
		try
		{
			if(value != null)
				return value.intValue() != 0 ? true : false;
			Object o = null;
			if(funID > -1)
				o = JsFunManager.JsFunbyID(engine, opf, aimf, funID);				
			else
				o = engine.eval(js);
			if(o == null) return false;
			if(o instanceof Boolean)
				return (Boolean)o;
			else if(o instanceof Integer)
				return ((Integer)o) != 0 ? true : false;
			else if(o instanceof Long)
				return ((Long)o) != 0 ? true : false;
			else
			{
				AbstractJSEngine.logger.error("JS脚本"+ js+"有错：/n");
				return false;
			}
		}
		catch (Exception e)
		{
			AbstractJSEngine.logger.error("JS脚本"+ js+"有错：/n"+ Parser.convertStackTrace2String(e));
			return false;
		}
	}		
	
	public String getJavaScriptString()
	{
		return js;
	}
	
	@Override
	public String toString()
	{
		return js;
	}
	
	public String getOringinString()
	{
		return this.js.substring(11, this.js.length() -1 );
	}
	
	/**
	 * 在原有公式的基础上后面追加一些片段，例如原来是"i/2",追加 "+2",变为"i/2+2"
	 * @param episode
	 */
	public JavaScript append(String episode)
	{
		StringBuffer sb = new StringBuffer(js);
		sb.insert(sb.length() - 1, episode);
		js = sb.toString();
		return this;
	}

	public void  parseUsedAttrTypes(List<Integer> usedattrtypea, List<Integer> usedattrtypeb)
	{
		try
		{
			if(value == null)
			{				
				if(funID > -1)
					return;
				for (int i = 0; i < fire.pb.effect.Module.fightAttrTypeIds.length; i++)
				{
					String name = fire.pb.effect.Module.getInstance().getEffectNameById(fire.pb.effect.Module.fightAttrTypeIds[i]);
					if(js.contains(name+"a") && !usedattrtypea.contains(fire.pb.effect.Module.fightAttrTypeIds[i]))
						usedattrtypea.add(fire.pb.effect.Module.fightAttrTypeIds[i]);
					if(js.contains(name+"b") && !usedattrtypeb.contains(fire.pb.effect.Module.fightAttrTypeIds[i]))
						usedattrtypeb.add(fire.pb.effect.Module.fightAttrTypeIds[i]);
				}	
				for (int i = 0; i < fire.pb.effect.Module.extfightAttrTypeIds.length; i++)
				{
					String name = fire.pb.effect.Module.getInstance().getEffectNameById(fire.pb.effect.Module.extfightAttrTypeIds[i]);
					if(js.contains(name+"a") && !usedattrtypea.contains(fire.pb.effect.Module.extfightAttrTypeIds[i]))
						usedattrtypea.add(fire.pb.effect.Module.extfightAttrTypeIds[i]);
					if(js.contains(name+"b") && !usedattrtypeb.contains(fire.pb.effect.Module.extfightAttrTypeIds[i]))
						usedattrtypeb.add(fire.pb.effect.Module.extfightAttrTypeIds[i]);
				}	
				Module.registerJavaScriptExpr(js,true,null);
			}
		} 
		catch (Exception e)
		{
			//return;
			e.printStackTrace();
		}

	}	
}
