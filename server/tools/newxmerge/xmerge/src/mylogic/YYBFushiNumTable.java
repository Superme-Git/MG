package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class YYBFushiNumTable implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.YybFushiNums srcYybFushiNums = xbean.Pod.newYybFushiNumsData(); 
		xbean.YybFushiNums dstYybFushiNums = xbean.Pod.newYybFushiNumsData(); 
		
		srcYybFushiNums.unmarshal(src_value);
		dstYybFushiNums.unmarshal(dest_value);
		
		dstYybFushiNums.getRolefushi().putAll(srcYybFushiNums.getRolefushi());
		
		dest_value.clear();
		dstYybFushiNums.marshal(dest_value);
		
		return true;
	}
	
}