package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


public class Tablekeycounter implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
		Integer src_counter = new Integer(src_value.unmarshal_int());
		Integer dest_counter = new Integer(dest_value.unmarshal_int());
		
		dest_value.clear();
		dest_value.marshal(src_counter + dest_counter);
		
		return true;
	}

}

