package fire.pb.item.pet;

import fire.pb.item.Commontext;
import fire.pb.item.ItemMgr;
import fire.pb.item.PetItem;
import fire.pb.item.Commontext.UseResult;


public class PetAptItem extends PetItem {
	public PetAptItem( ItemMgr im, int itemid ) {
		super( im, itemid );
	}

	public PetAptItem( ItemMgr im, xbean.Item item ) {
		super( im, item );
	}

	@Override
	public UseResult appendToPet(int petkey, int num) {
		return Commontext.UseResult.SUCC;
	}
}