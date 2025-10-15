package fire.pb.item;
import com.locojoy.base.Octets;

import mkdb.Procedure;


public class PEnhancementTimeout extends Procedure
{
	public long roleId;
	
	public PEnhancementTimeout( long roleId)
	{
		this.roleId = roleId;
	}
	
	@Override
	protected boolean process() throws Exception
	{	
		UpdateEnhancementTimeout(roleId);
		
		return true;
	}
	
	public static void UpdateEnhancementTimeout(long roleId)
	{
		Equip equip = new Equip(roleId, false);
		for (ItemBase item : equip)
		{
			if(item instanceof EquipItem)
			{
				EquipItem equipitem = (EquipItem)item;
				
				if (equipitem.updateEnhancementTimeOut() > 0) //附魔超时 by changhao
				{				
					fire.pb.item.PPutOnEquip.freshEquipBuff(roleId, equipitem);
					
					Octets tips = equipitem.getTips();
					if (tips != null)
					{
						SGetItemTips send = new SGetItemTips(BagTypes.EQUIP, item.getKey(), tips);
						Procedure.psendWhileCommit(roleId, send);
					}
				}
			}
		}		
	}
}
