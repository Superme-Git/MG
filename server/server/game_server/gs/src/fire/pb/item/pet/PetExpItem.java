package fire.pb.item.pet;

import fire.pb.item.Commontext;
import fire.pb.item.ItemMgr;
import fire.pb.item.PetItem;
import fire.pb.item.Commontext.UseResult;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;


public class PetExpItem extends PetItem {
	public PetExpItem( ItemMgr im, int itemid ) {
		super( im, itemid );
	}

	public PetExpItem( ItemMgr im, xbean.Item item ) {
		super( im, item );
	}

	@Override
	public UseResult appendToPet(int petkey, int num) {

		PetColumn petcol = new PetColumn(getOwnerid(), PetColumnTypes.PET, false);
		Pet pet = petcol.getPet(petkey);
		if(pet == null)
			return Commontext.UseResult.FAIL;

//		PetItemAttr petItemAttrConf = (PetItemAttr)attr;
//		new PAddPetExpProc(roleId, petkey, petItemAttrConf.getAddExp(), false).call();

		return Commontext.UseResult.SUCC;
	}
}