package fire.pb.item.pet;

import fire.pb.attr.AttrType;
import fire.pb.item.Commontext;
import fire.pb.item.ItemMgr;
import fire.pb.item.PetItem;
import fire.pb.item.PetItemShuXing;
import fire.pb.item.Commontext.UseResult;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;


public class PetLifeItem extends PetItem {
	public PetLifeItem( ItemMgr im, int itemid ) {
		super( im, itemid );
	}

	public PetLifeItem( ItemMgr im, xbean.Item item ) {
		super( im, item );
	}

	@Override
	public UseResult appendToPet(int petkey, int num) {

		PetColumn petcol = new PetColumn(getOwnerid(), PetColumnTypes.PET, false);
		Pet pet = petcol.getPet(petkey);
		if(pet == null)
			return Commontext.UseResult.FAIL;

		PetItemShuXing petItemAttrConf = (PetItemShuXing)itemAttr;

		int curValue = pet.getLife();
		int maxValue = pet.getMaxLife();
		int addValue = petItemAttrConf.getAddLife();
		int finalValue = 0;

		boolean isForever = fire.pb.pet.Module.getInstance().getPetManager().isPetLifeForever(pet.getPetAttr().getId());
		if (curValue >= maxValue || isForever) {
			MessageMgr.sendMsgNotify(roleid, 142929, null);
			return Commontext.UseResult.FAIL; // 达到最大值
		}

		finalValue = pet.addLife(addValue);

		fire.pb.attr.SRefreshPetData send = new fire.pb.attr.SRefreshPetData();
		send.columnid = PetColumnTypes.PET;
		send.petkey = pet.getPetkey();
		send.datas.put(AttrType.PET_LIFE, (float)pet.getLife());
		mkdb.Procedure.psendWhileCommit(roleid, send);

		// 记录日志
		if (fire.pb.pet.Module.logger.isInfoEnabled()) {
			fire.pb.pet.Module.logger.info("[PetLifeItem] roleId:" + roleid
					+ " petKey:" + petkey
					+ " uniqId:" + pet.getUniqueId()
					+ " petId:" + pet.getBaseId()
					+ " itemId:" + getItemId()
					+ " ItemName:" + getName()
					+ " itemNum:" + num
					+ " curValue:" + curValue
					+ " maxValue:" + maxValue
					+ " addValue:" + addValue
					+ " finalValue:" + finalValue);
		}
		return Commontext.UseResult.SUCC;
	}
}