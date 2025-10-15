package fire.pb.buff.continual;

import java.util.HashMap;
import java.util.Map;

import fire.pb.buff.BuffConfig;
import fire.script.JavaScript;

/**默认的持续性buff表配置*/
public class ConstantlyBuffConfig extends BuffConfig
{
	protected Map<Integer, JavaScript> effectJavaScript = new HashMap<Integer, JavaScript>();;
	private Map<Integer,Float> effects = new HashMap<Integer, Float>();


	/**不要使用，初始化默认配置时使用*/
	public ConstantlyBuffConfig(fire.pb.buff.SCBuffConfig scbuffconfig) throws Exception
	{
		buffTypeId = scbuffconfig.id;
		if (scbuffconfig.instantbuffeffect != null && !scbuffconfig.instantbuffeffect.equals(""))
		{
			String[] effectStr = scbuffconfig.instantbuffeffect.split(";");
			for (int i = 0; i < effectStr.length; i++)
			{
				String[] tmpstrs = effectStr[i].split("=");
				if (tmpstrs.length >= 2)
				{
					int effectId = fire.pb.effect.Module.getInstance().getIdByName(tmpstrs[0].trim());// 第一个“=”之前的字符串是Effectname
					effectJavaScript.put(effectId, (new JavaScript(effectStr[i].substring(tmpstrs[0].length() + 1))));
//					throw new Exception("Wrong effects str.");
				}
			}
		}
		effects.putAll(fire.pb.effect.Module.parseEffects(scbuffconfig.getEffects()));		
	}
	
	public Map<Integer,JavaScript> effectJavaScripts()
	{
		return effectJavaScript;
	}
	
	public Map<Integer,Float> getEffects()
	{
		return effects;
	}

}
