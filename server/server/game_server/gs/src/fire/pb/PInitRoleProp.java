package fire.pb;

import mkdb.Procedure;

public class PInitRoleProp extends Procedure {
	
	public PInitRoleProp(long roleid) {
		super();
		this.roleid = roleid;
	}

	private final long roleid;

	@Override
	protected boolean process() throws Exception {
		final fire.pb.effect.Role cattr = new fire.pb.effect.RoleImpl(roleid);
		cattr.updateAllFinalAttrs();
		cattr.fullHp();
		cattr.fullMp();
		cattr.fullPhyforce();
		cattr.fullEnergy();
		return true;
	}
	
}
