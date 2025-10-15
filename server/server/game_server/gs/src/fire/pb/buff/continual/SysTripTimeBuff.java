package fire.pb.buff.continual;
import fire.pb.buff.BuffAgent;
import fire.pb.skill.Result;
import xbean.Buff;

/**
 * 系统三倍时间结束的时候，判断是否要加成就，如果需要加成就，则加成就。
 *
 */
public class SysTripTimeBuff extends ConstantlyBuff {

	public SysTripTimeBuff(Buff buffBean) {
		super(buffBean);
	}
	
	public SysTripTimeBuff(ConstantlyBuffConfig buffConfig) {
		super(buffConfig);
	}

	@Override
	public Result closure(BuffAgent buffAgent, boolean timeout) {

		return super.closure(buffAgent, timeout);
	}
}
