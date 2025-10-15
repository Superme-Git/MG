package fire.pb.buff;

import java.util.Properties;

import fire.pb.main.ConfigMgr;
import fire.pb.util.FireProp;

public class AbstractBuff implements IBuff
{

	@Override
	public int getId()
	{
		return -1;
	}

	static Properties prop = ConfigMgr.getInstance().getPropConf("battle");
	protected static final float RAND_SPEED_VALUE = FireProp.getFloatValue(prop, "mtgserv.rand_speed_value"); //乱速浮动值	0.05
	protected static final float DAMAGE_DEFEND_PARAM = FireProp.getFloatValue(prop, "mtgserv.damage_defend_param"); //防御减伤系数	0.5
	protected static final float MIX_PHYSIC_DAMAGE_VALUE = FireProp.getFloatValue(prop, "mtgserv.mix_physic_damage_value"); //最低物理伤害系数	0.1
	protected static final float DAMAGE_PROTECT_RATE = FireProp.getFloatValue(prop, "mtgserv.damage_protect_rate"); //保护分担伤害系数	0.5
	protected static final float MIX_PHYSIC_CRIT_RATE = FireProp.getFloatValue(prop, "mtgserv.mix_physic_crit_rate"); //最低物理暴击率	0.05
	protected static final float MIX_MAGIC_CRIT_RATE = FireProp.getFloatValue(prop, "mtgserv.mix_magic_crit_rate"); //最低法术暴击率	0.05
	protected static final float MIX_HEAL_CRIT_RATE = FireProp.getFloatValue(prop, "mtgserv.mix_heal_crit_rate"); //最低治疗暴击率	0.05
	protected static final float DAMAGE_FLOAT_VALUE = 0.0f;//FireProp.getFloatValue(prop, "mtgserv.damage_float_value"); //伤害浮动值	0.05
	protected static final float POISON_INFECT_RATE = FireProp.getFloatValue(prop, "mtgserv.poison_infect_rate"); //毒传染率	0.15
	public static final float MYSTERY_FORBID_RATE = FireProp.getFloatValue(prop, "mtgserv.mystery_forbid_rate"); //奥术禁锢率	0.15
	protected static final float PANDORA_BOX_RATE = FireProp.getFloatValue(prop, "mtgserv.pandora_box_rate"); //超度邪恶率	0.5
	
}
