package fire.pb.item.pet;

import fire.pb.item.Commontext;
import fire.pb.item.ItemMgr;
import fire.pb.item.PetItem;
import fire.pb.item.PetItemShuXing;
import fire.pb.item.Commontext.UseResult;

public class PetSkillItem extends PetItem {
	public PetSkillItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}

	public PetSkillItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}

	@Override
	public UseResult appendToPet(int petkey, int num) {
		if (num != 1)
			return Commontext.UseResult.FAIL;
		PetItemShuXing ia = (PetItemShuXing) itemAttr;
		if (useSkillBook(roleid, petkey, ia, (int) itemData.getExtid()))
			return Commontext.UseResult.SUCC;
		else
			return Commontext.UseResult.FAIL;
	}

	public boolean useSkillBook(long roleId, int petkey, PetItemShuXing attr,
			int skillexp) {
		// 宠物技能书的实例,不能直接使用 [2/25/2016 XGM]
		return false;
	}
}
