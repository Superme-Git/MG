package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class RoleDayPayTable implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.EDayPay srcEDayPay = xbean.Pod.newEDayPayData(); 
		xbean.EDayPay dstEDayPay = xbean.Pod.newEDayPayData(); 
		srcEDayPay.unmarshal(src_value);
		dstEDayPay.unmarshal(dest_value);
		
		dstEDayPay.getRoleid2daypay().putAll(srcEDayPay.getRoleid2daypay());
		
		dest_value.clear();
		dstEDayPay.marshal(dest_value);
		
		return true;
	}
	
}