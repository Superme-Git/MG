package fire.pb.state;

import fire.pb.state.MulDayLogin;
import mkdb.Procedure;

public class PMulDayLoginDayChange extends Procedure {

	private final long roleid;

	public PMulDayLoginDayChange(long roleid) {

		super();
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		MulDayLogin.dayCross(roleid);

		return true;
	}

}
