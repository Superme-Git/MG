
package fire.pb.role;

import mkdb.Procedure;



public class PAddRoleNum extends Procedure {

	@Override
	protected boolean process() throws Exception {

		xbean.ServerRoles serverRoles = xtable.Rolenumfornewserver.get(1);
		if (serverRoles==null){
			serverRoles = xbean.Pod.newServerRoles();
			xtable.Rolenumfornewserver.insert(1, serverRoles);
		}else {
			serverRoles.setRolenum(serverRoles.getRolenum()+1);
			if (serverRoles.getRolenum() <= 1000) 
			    serverRoles.setCreatetime(System.currentTimeMillis());
		}
		return true;
	}

	
}

