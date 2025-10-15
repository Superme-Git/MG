package fire.pb.state;


import fire.pb.state.MulDayLogin;
import mkdb.Procedure;


public class POpenMulDayLoginGiftBag extends Procedure {

	private final long roleid;

	public POpenMulDayLoginGiftBag(long roleid) {

		super();
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		MulDayLogin.online(roleid);

		return true;
	}

}
