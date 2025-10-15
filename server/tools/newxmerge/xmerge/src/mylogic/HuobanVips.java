package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class HuobanVips implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.HuoBanVip srcHuoBanVip = xbean.Pod.newHuoBanVipData(); 
		xbean.HuoBanVip dstHuoBanVip = xbean.Pod.newHuoBanVipData(); 
		srcHuoBanVip.unmarshal(src_value);
		dstHuoBanVip.unmarshal(dest_value);
		
		dstHuoBanVip.getHuobans().clear();
		dstHuoBanVip.getHuobans().addAll(srcHuoBanVip.getHuobans());
		
		dest_value.clear();
		dstHuoBanVip.marshal(dest_value);
		return true;
	}
	
}