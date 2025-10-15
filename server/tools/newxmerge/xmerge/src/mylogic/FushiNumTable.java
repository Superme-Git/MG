package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class FushiNumTable implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.YbNums srcYbNums = xbean.Pod.newYbNumsData(); 
		xbean.YbNums dstYbNums = xbean.Pod.newYbNumsData(); 
		
		srcYbNums.unmarshal(src_value);
		dstYbNums.unmarshal(dest_value);
		
		dstYbNums.getRoleyb().putAll(srcYbNums.getRoleyb());
		
		dest_value.clear();
		dstYbNums.marshal(dest_value);
		
		return true;
	}
	
}