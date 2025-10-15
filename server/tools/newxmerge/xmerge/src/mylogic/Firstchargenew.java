package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Firstchargenew implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.FirstCharge srcFirstCharge = xbean.Pod.newFirstChargeData(); 
		xbean.FirstCharge dstFirstCharge = xbean.Pod.newFirstChargeData(); 
		
		srcFirstCharge.unmarshal(src_value);
		dstFirstCharge.unmarshal(dest_value);
		
		if(srcFirstCharge.getChargestatus()>dstFirstCharge.getChargestatus()){
			dstFirstCharge.setChargestatus(srcFirstCharge.getChargestatus());
		}
		
		dest_value.clear();
		dstFirstCharge.marshal(dest_value);
		
		
		
		return true;
	}

}
