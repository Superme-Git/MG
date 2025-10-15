package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


public class Activeroletable implements IMerge {
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

		long roleid = src_key.unmarshal_long();
		if (roleid!=100)
			throw new RuntimeException("only system role need to be merged!");
		return true;
	}

}

