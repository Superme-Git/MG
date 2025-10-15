package fire.pb.item;

import fire.pb.item.Commontext.UseResult;

public class UseItemToRole implements UseItemHandler {
	private final long objroleId;
	public UseItemToRole(final long objroleId) {
		this.objroleId = objroleId;
	}
	@Override
	public UseResult onUse(long roleId, ItemBase bi, int usednum) {
		return bi.appendToRole(objroleId, usednum);
	}

}
