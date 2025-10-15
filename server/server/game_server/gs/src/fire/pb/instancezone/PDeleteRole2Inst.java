package fire.pb.instancezone;

import java.util.List;

import mkdb.Lockeys;
import mkdb.Procedure;

public class PDeleteRole2Inst extends Procedure {

	private final List<Long> roles;
	public PDeleteRole2Inst(final List<Long> roles) {
		this.roles = roles;
	}
	@Override
	protected boolean process() throws Exception {
		Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roles));
		for (Long roleid : roles) {
			xtable.Role2instancezone.remove(roleid);
		}
		
		return true;
	}
	
	


}
