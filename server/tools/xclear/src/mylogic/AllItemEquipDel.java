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
		xtable.Bag.remove(roleId);//bag	背包
		xtable.Depot.remove(roleId);//depot	仓库
		xtable.Equip.remove(roleId);//equip	装备背包
		xtable.Temp.remove(roleId);//temp	临时背包
		xtable.Questbag.remove(roleId);//questbag	任务背包
	}
	
//	public static void delOther(xbean.Item info){
//		ItemBase bi = main.Module.getInstance().getItemManager().toItemBase(info);
//		if (bi instanceof EquipItem) {
//			Equips.remove(info.getExtid());//equips装备信息表
//		}else if(bi instanceof FoodItem){
//			Quality.remove(info.getExtid());//quality恢复物品属性
//		}else if(bi instanceof AttrUpItem){
//			xtable.Enhancement.remove(info.getExtid());//enhancement	附魔属性表
//		}else if(bi instanceof SuperTreasureMapItem||bi instanceof TreasuremapItem){
//			xtable.Archmaps.remove(info.getExtid());//archmaps	藏宝图表
//		}
//	}
	
	
	

}
