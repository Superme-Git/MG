package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class RoleRenXingdataTable implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		return true;
	}
	
}