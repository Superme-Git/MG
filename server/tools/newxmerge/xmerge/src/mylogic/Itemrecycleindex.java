package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


public class Itemrecycleindex implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

		xbean.ItemRecycleIndex srcdi = xbean.Pod.newItemRecycleIndexData(); 
		srcdi.unmarshal(src_value);
		xbean.ItemRecycleIndex dstdi = xbean.Pod.newItemRecycleIndexData(); 
		dstdi.unmarshal(dest_value);
		dstdi.getDayrecycle().addAll(srcdi.getDayrecycle());
		
		dest_value.clear();
		dstdi.marshal(dest_value);
		
		return true;
	}

}

