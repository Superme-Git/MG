package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class Fixeddatetimeoutitem implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
//		xbean.FixedDateTimeoutItemInfo src_list = xbean.Pod.newFixedDateTimeoutItemInfoData();
//		xbean.FixedDateTimeoutItemInfo dest_list = xbean.Pod.newFixedDateTimeoutItemInfoData();
//		
//		src_list.unmarshal(src_value);
//		dest_list.unmarshal(dest_value);
//		
//		dest_list.getItemuid().addAll(src_list.getItemuidAsData());
//		
//		dest_value.clear();
//		dest_list.marshal(dest_value);
		return true;
	}

}
