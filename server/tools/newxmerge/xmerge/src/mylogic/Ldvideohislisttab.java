package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Ldvideohislisttab implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		
		xbean.LDVideoHisList srcLDVideoHisList = xbean.Pod.newLDVideoHisListData(); 
		xbean.LDVideoHisList dstLDVideoHisList = xbean.Pod.newLDVideoHisListData(); 
		srcLDVideoHisList.unmarshal(src_value);
		dstLDVideoHisList.unmarshal(dest_value);
		
		dstLDVideoHisList.getLdvidehisinfo().addAll(srcLDVideoHisList.getLdvidehisinfo());
		
		dest_value.clear();
		dstLDVideoHisList.marshal(dest_value);
		
		return true;
	}
	
}