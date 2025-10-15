package fire.pb.buff.single;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.buff.BuffConfig;

public class SingleBuffConfig extends BuffConfig
{
	private List<Integer> targetBuffs = new ArrayList<Integer>();
	private Map<Integer,Float> effects;
	
	public SingleBuffConfig(fire.pb.buff.SIBuffConfig conf) throws Exception
	{
		setBuffTypeId(conf.id);
		setClassName(conf.classname);
		setBuffFlagtype(conf.bufftype);
		if(conf.getTargetBuffs() != null&& !conf.getTargetBuffs().equals("null"))
		{
			String[] tarbuffstrs = conf.getTargetBuffs().split(";");
			for (String buffstr : tarbuffstrs)
			{
				targetBuffs.add(((Double)Double.parseDouble(buffstr.trim())).intValue());
			}
		}
		effects = fire.pb.effect.Module.parseEffects(conf.getEffects());
	}
	

	public List<Integer> getTargetBuffs()
	{
		return targetBuffs;
	}

	public Map<Integer, Float> getEffects()
	{
		return effects;
	}
	
	
}
