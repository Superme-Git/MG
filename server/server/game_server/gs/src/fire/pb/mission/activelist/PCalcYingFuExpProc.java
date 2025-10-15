package fire.pb.mission.activelist;

import mkdb.Procedure;

public class PCalcYingFuExpProc extends Procedure {
	private final long roleid;
	
	public PCalcYingFuExpProc(final long roleid) {
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
		if (actrole != null) {
			actrole.calcYingFuExp();
		}
		
		return true;
	}
	
	

}
