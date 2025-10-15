package fire.pb.item;

public class PUseItemToItem extends DPUseItem {
	private final int dstpackid;
	private final int dstkeyinpack;
	public PUseItemToItem(final long roleId, final int srckeyinpack, final int usednum, final int packid, final int keyinpack) {
		super(roleId, srckeyinpack, usednum);
		this.dstpackid = packid;
		this.dstkeyinpack = keyinpack;
	}
	@Override
	protected UseItemHandler getUseItemHandler() {
		UseItemHandler ret = super.getUseItemHandler();
		if (ret == null)
			ret	= new UseItemToItem(dstpackid, dstkeyinpack);
		return ret;
	}
}
