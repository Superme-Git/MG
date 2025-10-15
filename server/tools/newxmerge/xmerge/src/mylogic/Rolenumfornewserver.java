package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class Rolenumfornewserver implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

		xbean.ServerRoles srcxbean = xbean.Pod.newServerRolesData();
		xbean.ServerRoles dstxbean = xbean.Pod.newServerRolesData();
		srcxbean.unmarshal(src_value);
		dstxbean.unmarshal(dest_value);
		
		dstxbean.setRolenum(dstxbean.getRolenum()+srcxbean.getRolenum());
		dstxbean.setCreatetime(Math.max(dstxbean.getCreatetime(), srcxbean.getCreatetime()));
		
		dest_value.clear();
		dstxbean.marshal(dest_value);
		return true;
	}

}

