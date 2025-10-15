package fire.pb.buff.continual;

import fire.pb.buff.BaseBuff;
import fire.pb.buff.BuffRoleImpl;

public class NullBuff extends BaseBuff
{
	public NullBuff(){}

	@Override
	public int detach(BuffRoleImpl buffRole){return 0;}

}
