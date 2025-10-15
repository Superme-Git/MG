package fire.pb.buff.continual;

/**装备属性buff*/
public class EquipmentBuff extends ConstantlyBuff
{

	public EquipmentBuff(ConstantlyBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	public EquipmentBuff(xbean.Buff buffBean)	
	{
		super(buffBean);
	}


}
