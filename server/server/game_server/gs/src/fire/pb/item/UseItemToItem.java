package fire.pb.item;

import fire.pb.item.Commontext.UseResult;

public class UseItemToItem implements UseItemHandler {
	private final int packid;
	private final int keyinpack;
	public UseItemToItem(int packid, int keyinpack) {
		this.packid = packid;
		this.keyinpack = keyinpack;
	}
	@Override
	public UseResult onUse(long roleId, ItemBase bi, int usednum) {
		if (usednum != 1)
			return UseResult.FAIL;
		return bi.appendToItem(packid, keyinpack, usednum);
	}
}
