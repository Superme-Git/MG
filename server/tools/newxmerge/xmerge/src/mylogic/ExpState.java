package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class ExpState implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		Long desValue = dest_value.unmarshal_long();
		Long srcValue = src_value.unmarshal_long();
		
		desValue = desValue + srcValue;
		
		dest_value.clear();
		dest_value.marshal(desValue);
		
		return true;
	}

}
