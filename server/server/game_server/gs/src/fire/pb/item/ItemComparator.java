package fire.pb.item;

import fire.pb.item.groceries.SuperTreasureMapItem;
import fire.pb.item.groceries.TreasuremapItem;

public class ItemComparator implements java.util.Comparator<ItemBase> {
@Override
public int compare( ItemBase o1, ItemBase o2) {
	
	final int sid1 =Module.getInstance().getItemManager().getAttr( o1.getDataItem().getId() ).sortid;
	final int sid2 =Module.getInstance().getItemManager().getAttr( o2.getDataItem().getId() ).sortid;;
	if (sid1 < sid2)
		return -1;
	else if (sid1 > sid2)
		return 1;
	if (o1.getItemId() < o2.getItemId())
		return -1;
	else if (o1.getItemId() > o2.getItemId())
		return 1;
	if (o1 instanceof TreasuremapItem ) {
		TreasuremapItem ti1 = (TreasuremapItem)o1;
		TreasuremapItem ti2 = (TreasuremapItem)o2;
		xbean.ArchMap prop1 = ti1.getExtInfo();
		xbean.ArchMap prop2 = ti2.getExtInfo();
		if (prop1 != null && prop2 != null) {
			if (prop1.getMapid() > prop2.getMapid()) {
				return 1;
			} else if (prop1.getMapid() < prop2.getMapid()) {
				return -1;
			}
		}
	} else if (o1 instanceof SuperTreasureMapItem) {
		SuperTreasureMapItem ti1 = (SuperTreasureMapItem)o1;
		SuperTreasureMapItem ti2 = (SuperTreasureMapItem)o2;
		xbean.ArchMap prop1 = ti1.getExtInfo();
		xbean.ArchMap prop2 = ti2.getExtInfo();
		if (prop1 != null && prop2 != null) {
			if (prop1.getMapid() > prop2.getMapid()) {
				return 1;
			} else if (prop1.getMapid() < prop2.getMapid()) {
				return -1;
			}
		}
	} else if (o1 instanceof FoodItem) {
		FoodItem item1 = (FoodItem)o1;
		FoodItem item2 = (FoodItem)o2;
		
		xbean.RecoveryAttr quality1 = item1.getExtInfo();
		xbean.RecoveryAttr quality2 = item2.getExtInfo();
		if (quality1 != null && quality2 != null) {
			if (quality1.getQuality() > quality2.getQuality()) {
				return 1;
			}
			else {
				return -1;
			}
		}
	}
	
	if (o1.getNumber() > o2.getNumber())
		return -1;
	else if (o1.getNumber() < o2.getNumber())
		return 1;
	if (o1.getFlags() < o2.getFlags())
		return -1;
	else if (o1.getFlags() > o2.getFlags())
		return 1;
	if (o1.getTimeout() < o2.getTimeout())
		return -1;
	else if (o1.getTimeout() > o2.getTimeout())
		return 1;
	
	return o1.getKey() - o2.getKey();
}
}
