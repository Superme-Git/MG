package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class QuickPay implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
//		xbean.QuickPayInfo srcQuickPayInfo = xbean.Pod.newQuickPayInfoData(); 
//		xbean.QuickPayInfo dstQuickPayInfo = xbean.Pod.newQuickPayInfoData(); 
//		srcQuickPayInfo.unmarshal(src_value);
//		dstQuickPayInfo.unmarshal(dest_value);
//		dstQuickPayInfo.getMintvector().addAll(srcQuickPayInfo.getMintvector());
//		
//		dest_value.clear();
//		dstQuickPayInfo.marshal(dest_value);
		return true;
	}

}
