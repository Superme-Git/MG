package fire.pb;

import mkdb.Procedure;

public class PUpdateFinalAttrs extends Procedure 
{
	long roleId;
	public PUpdateFinalAttrs(long roleId)
	{
		this.roleId  = roleId;
	}
	public boolean process(){
		fire.pb.effect.Role erole = new fire.pb.effect.RoleImpl(roleId);
		erole.updateAllFinalAttrs();
		return true;
	}
}
