package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class Limitfashion implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
//		xbean.LimitFashionInfo src_info = xbean.Pod.newLimitFashionInfoData();
//		xbean.LimitFashionInfo dest_info = xbean.Pod.newLimitFashionInfoData();
//		src_info.unmarshal(src_value);
//		dest_info.unmarshal(dest_value);
//		if (src_info.getLastupdate() - dest_info.getLastupdate() < 
//				7L*24L*3600L*1000L) {
//			dest_info.setLeftnum(src_info.getLeftnum() > dest_info.getLeftnum() ? 
//					src_info.getLeftnum() : dest_info.getLeftnum());
//		} else {
//			if (dest_info.getLastupdate() < src_info.getLastupdate()) {
//				dest_info.setLastupdate(src_info.getLastupdate());
//				dest_info.setLeftnum(src_info.getLeftnum());
//			}
//		}
//		
//		dest_value.clear();
//		dest_info.marshal(dest_value);
		return true;
	}

}
