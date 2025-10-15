package fire.pb.buff.instant;

import java.util.Map;

import fire.pb.buff.AbstractBuff;
import fire.script.JavaScript;

public class InstantBuff extends AbstractBuff
{
	protected InstantBuffCfg buffConfig;
	protected String[] values;
	protected int buffId;
	protected String sortArg;
	
	protected Map<Integer, JavaScript> effects;
	
	public InstantBuff(InstantBuffCfg buffConfig)
	{
		this.buffConfig = buffConfig;
		this.buffId = buffConfig.getBuffTypeId();
	}

	@Override
	public int getId()
	{
		return buffId;
	}
	
	
	public double randomValueInScale(double value, float min, float max)
	{
		double finalfloat = min + Math.random() * (max - min);
		return value * finalfloat;
	}
}
