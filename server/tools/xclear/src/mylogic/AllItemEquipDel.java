package mylogic;

import fire.pb.item.EquipItem;
import fire.pb.item.FoodItem;
import fire.pb.item.ItemBase;
import fire.pb.item.groceries.AttrUpItem;
import fire.pb.item.groceries.SuperTreasureMapItem;
import fire.pb.item.groceries.TreasuremapItem;
import xtable.Equips;
import xtable.Quality;


public class AllItemEquipDel {
	
	public static void remove(long roleId){
//		xbean.Bag bag=xtable.Bag.get(roleId);
//		if(bag!=null){
//			for(xbean.Item info:bag.getItems().values()){
//				if(info==null){
//					continue;
//				}
//				delOther(info);
//			}
//		}
//		xbean.Bag depot=xtable.Depot.get(roleId);
//		if(depot!=null){
//			for(xbean.Item info:depot.getItems().values()){
//				if(info==null){
//					continue;
//				}
//				delOther(info);
//			}
//		}
//		xbean.Bag equip=xtable.Equip.get(roleId);
//		if(equip!=null){
//			for(xbean.Item info:equip.getItems().values()){
//				if(info==null){
//					continue;
//				}
//				delOther(info);
//			}
//		}
//		xbean.Bag temp=xtable.Temp.get(roleId);
//		if(temp!=null){
//			for(xbean.Item info:temp.getItems().values()){
//				if(info==null){
//					continue;
//				}
//				delOther(info);
//			}
//		}
//		xbean.Bag questbag=xtable.Questbag.get(roleId);
//		if(questbag!=null){
//			for(xbean.Item info:questbag.getItems().values()){
//				if(info==null){
//					continue;
//				}
//				delOther(info);
//			}
//		}
		xtable.Bag.remove(roleId);//bag	����
		xtable.Depot.remove(roleId);//depot	�ֿ�
		xtable.Equip.remove(roleId);//equip	װ������
		xtable.Temp.remove(roleId);//temp	��ʱ����
		xtable.Questbag.remove(roleId);//questbag	���񱳰�
	}
	
//	public static void delOther(xbean.Item info){
//		ItemBase bi = main.Module.getInstance().getItemManager().toItemBase(info);
//		if (bi instanceof EquipItem) {
//			Equips.remove(info.getExtid());//equipsװ����Ϣ��
//		}else if(bi instanceof FoodItem){
//			Quality.remove(info.getExtid());//quality�ָ���Ʒ����
//		}else if(bi instanceof AttrUpItem){
//			xtable.Enhancement.remove(info.getExtid());//enhancement	��ħ���Ա�
//		}else if(bi instanceof SuperTreasureMapItem||bi instanceof TreasuremapItem){
//			xtable.Archmaps.remove(info.getExtid());//archmaps	�ر�ͼ��
//		}
//	}
	
	
	

}
