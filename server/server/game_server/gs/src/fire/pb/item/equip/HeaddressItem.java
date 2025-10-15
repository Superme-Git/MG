package fire.pb.item.equip;

import fire.pb.item.EquipItem;
import fire.pb.item.EquipItemType;
import fire.pb.item.ItemMgr;

import com.locojoy.base.Octets;

public class HeaddressItem extends EquipItem {
	public HeaddressItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}
	
	@Override
	public Octets getTips() {
		return super.getTipsAgain();
	}
	
	public HeaddressItem(ItemMgr im, xbean.Item item ) {
		super(im, item );
		// TODO Auto-generated constructor stub
	}
	
	//只能在存储过程中调用
	public void setUnfixedAttribute(int genWay){
	}
	
	@Override
	public int getEquipType() {
		return EquipItemType.TIRE;
	}
	

}
