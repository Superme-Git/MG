package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

public class UserMergeImplementThrowException implements xmerge.IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		throw new Exception("merge Exception... ...");
	}

}
