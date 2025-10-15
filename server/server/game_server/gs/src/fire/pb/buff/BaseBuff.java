package fire.pb.buff;
public abstract class BaseBuff
{
	protected BuffRoleImpl buffRole;	
	public BaseBuff(){}	
	public int attach(BuffRoleImpl buffRole){return 0;}	
	public int detach(BuffRoleImpl buffRole){return 0;}
}
