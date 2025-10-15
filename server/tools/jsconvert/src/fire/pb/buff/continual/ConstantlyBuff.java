package fire.pb.buff.continual;

import java.util.Map;
import fire.pb.buff.AbstractBuff;
import fire.script.JavaScript;
/***/
public class ConstantlyBuff extends AbstractBuff
{
	final protected ConstantlyBuffConfig buffConfig;
	protected int buffId;
	Map<Integer, JavaScript> effects ;
	
	/**新建一个Buff*/
	public ConstantlyBuff(ConstantlyBuffConfig buffConfig)
	{
		this.buffConfig = buffConfig;
		this.buffId = buffConfig.getBuffTypeId();
	}	
		
	public ConstantlyBuffConfig getBuffConfig()
	{
		return buffConfig;
	}
	
	@Override
	public int getId(){return buffId;}

}
