package fire.pb.item.equip;

import fire.pb.item.EquipItem;
import fire.pb.item.EquipItemType;
import fire.pb.item.ItemMgr;

public class NecklaceItem extends EquipItem implements IEquipType {
	public NecklaceItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}
	
	public NecklaceItem(ItemMgr im, xbean.Item item ) {
		super(im, item );
	}
	
	//只能在存储过程中调用
	public void setUnfixedAttribute(int genWay){
	}

	@Override
	public int getEquipType() {
		return EquipItemType.ADORN;
	}
}
