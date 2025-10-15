package fire.pb.item;

public class PUseItemToRole extends DPUseItem {
	private final long objroleId;
	public PUseItemToRole(long roleId, int keyinpack, int usenum) {
		this(roleId, keyinpack, usenum, roleId);
	}
	
	public PUseItemToRole(long roleId, int keyinpack, int usenum, boolean singleplayer) {
		this(roleId, keyinpack, usenum, roleId, singleplayer);
	}
	
	public PUseItemToRole(long roleId, int keyinpack, int usenum, long objroleId) {
		this(roleId, keyinpack, usenum, objroleId, true);
	}
	
	public PUseItemToRole(long roleId, int keyinpack, int usenum, long objroleId, boolean singleplayer) {
		super(roleId, keyinpack, usenum, singleplayer);
		this.objroleId = objroleId;
	}

	@Override
	protected UseItemHandler getUseItemHandler() {
		UseItemHandler handler;
		if ((handler = super.getUseItemHandler()) == null) {
			handler = new UseItemToRole(objroleId);
		}
		return handler;
	}
	
}
