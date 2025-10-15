package fire.pb.item;

import mkdb.Procedure;

public class PAddVipPack extends Procedure {

	private final long roleid;
	private final int packid;
	private final int extsize;
	
	public PAddVipPack(final long roleid, final int packid, final int extsize) {
		this.roleid = roleid;
		this.packid = packid;
		this.extsize = extsize;
	}
	
	@Override
	protected boolean process() throws Exception {
		if (packid != BagTypes.BAG && packid != BagTypes.DEPOT)
			return false;
		
		final ItemMaps bag = Module.getInstance().getItemMaps(roleid, packid, false);
		int capacity = bag.getCapacity();
		
		int maxCapacity = Module.getInstance().getMaxBagCapacity(roleid, packid);
		if (capacity >= maxCapacity) {
			return false;
		}
		
		if ((capacity + extsize) > maxCapacity)
			return false;
		
		bag.addCapacity(extsize); // 增加格子
		
		SRefreshPackSize send = new SRefreshPackSize();
		send.packid = packid;
		send.cap = bag.getCapacity();
		mkdb.Procedure.psendWhileCommit(roleid, send);
		return true;
	}
}
