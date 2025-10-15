package fire.pb.shop.srv;

import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;

/**
 * 
 * 添加到宠物栏
 * 
 * @author liangyanpeng
 * 
 */
public class StoreShenShouContainer implements Container {
	protected BuyShopBase shop;
	public StoreShenShouContainer(BuyShopBase shop) {
		super();
		this.shop = shop;
	}

	@Override
	public boolean add(boolean isBind) {
		long roleId = shop.role.getRoleId();
		int petTypeId = shop.goods.getItemId();
		
		fire.pb.pet.PetManager petManager = fire.pb.pet.Module.getInstance().getPetManager();
		if (null == petManager) {
			Shop.LOG.error("角色id " + roleId + "兑换神兽错误");
			return false;
		}
		
		PetAttr petAttr = petManager.getAttr(petTypeId);
		if (null == petAttr) {
			Shop.LOG.error("角色id " + roleId + "兑换神兽" + "\t宠物属性错误\t宠物ID:" + petTypeId);
			return false;
		}
		
		PetColumn petcol = new PetColumn(shop.role.getRoleId(), PetColumnTypes.PET, false);
		if (petcol.size() >= petcol.getCapacity()) {
			// 宠物栏已满
			fire.pb.talk.MessageMgr.sendMsgNotify(shop.role.getRoleId(), 160198, null);
			Shop.LOG.error("神兽兑换,宠物栏已满");
			return false;
		}
		
		if (petcol.addpet(petAttr.getId(), petAttr.getInitlevel(), petAttr.getKind(), null,
				PetColumn.ADD_REASON_SHENSHOUDUIHUAN, -1, isBind, null) < 0) {
			Shop.LOG.error("角色id " + roleId + "兑换神兽" + "\t加神兽错误");
			return false;
		}
		
		Shop.LOG.error("角色id " + roleId + "兑换神兽ID:" + petTypeId + "成功");
		return true;
	}

	@Override
	public boolean remove(int petkey) {
		return false;
	}

}
