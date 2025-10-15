package fire.pb.buff;

import java.util.Map;

import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.item.EquipItemType;

public class BuffManager
{

	public static ConstantlyBuff createEquipmentBuff(int equipType,Map<Integer,Float> effects)
	{
		Integer buffId = getBuffIdByEquipType(equipType);
		if(buffId!=null)
		{
			ConstantlyBuff cbuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(buffId);
			if(cbuff!=null )
			{
				cbuff.getEffects().putAll(effects);
				return cbuff;
			}
		}
		return null;
	}
	
	public static Integer getBuffIdByEquipType(int equipType)
	{
		switch(equipType)
		{
		case EquipItemType.ARMS:
			return 500000;
		case EquipItemType.ADORN:
			return 500002;
		case EquipItemType.LORICAE:
			return 500003;
		case EquipItemType.WAISTBAND:
			return 500004;
		case EquipItemType.BOOT:
			return 500005;
		case EquipItemType.TIRE:
			return 500006;
		default:
			return null;
		}
	}
	
}
