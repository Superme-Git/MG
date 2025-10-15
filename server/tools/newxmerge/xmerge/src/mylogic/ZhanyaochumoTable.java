package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class ZhanyaochumoTable implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		mkdb.Trace.info("ZhanyaochumoTable="+src_key.unmarshal_long());
		return true;
	}
	
}