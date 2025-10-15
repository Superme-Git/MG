package fire.pb.shop.srv;

import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;

/**
 * 
 * 添加到宠物栏
 * 
 * @author liangyanpeng
 * 
 */
public class StorePetContainer implements Container {


	protected BuyShopBase shop;

	public StorePetContainer(BuyShopBase shop) {
		super();
		this.shop = shop;
	}

	@Override
	public boolean add(boolean isBind) {
		PetColumn petcol = new PetColumn(shop.role.getRoleId(), PetColumnTypes.PET, false);
		if (petcol.size() >= petcol.getCapacity()) {
			// 宠物栏已满
			fire.pb.talk.MessageMgr.sendMsgNotify(shop.role.getRoleId(), 160198, null);
			return false;
		}
		// 返回-1是添加宠物失败，这里宠物接口没有提供丰富的状态来表明什么原因的失败
		return petcol.addPetByID(shop.goods.getItemId(), isBind, PetColumn.ADD_REASON_SHOP_BUY) != -1 ? true : false;
	}

	@Override
	public boolean remove(int petkey) {
		return false;
	}

}
