package fire.pb.item;

import fire.pb.item.Commontext.UseResult;

public class UseItemToPet implements UseItemHandler {
	private final int petkey;
	public UseItemToPet(final int petkey) {
		this.petkey = petkey;
	}

	@Override
	public UseResult onUse(long roleId, ItemBase bi, int usednum) {
		return bi.appendToPet(petkey, usednum);
	}
}
