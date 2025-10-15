package fire.pb.item;

import mkdb.Procedure;

public class PRefreshNaiJiu extends Procedure {
	private int bagId;
	private long roleId;
	
	public PRefreshNaiJiu(int packid, long roleId) {
		this.bagId = packid;
		this.roleId = roleId;
	}
	
	public boolean process() {
		ItemMaps ic = Module.getInstance().getItemMaps(roleId, bagId, true);
		if (ic != null) {
			SRefreshNaiJiu endure = ic.getBagEndureInfo();
			mkdb.Procedure.psendWhileCommit(roleId, endure);
			
			//发送刷新界面消息
			mkdb.Procedure.psendWhileCommit(roleId, new SFreshRepairData());
		}
		return true;
	}

	
}
