package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Professionleader implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.ProfessionLeaderInfo srcProfessionLeaderInfo = xbean.Pod.newProfessionLeaderInfoData();
		xbean.ProfessionLeaderInfo dstProfessionLeaderInfo = xbean.Pod.newProfessionLeaderInfoData();
		srcProfessionLeaderInfo.unmarshal(src_value);
		dstProfessionLeaderInfo.unmarshal(dest_value);
		
		dest_value.clear();
		dstProfessionLeaderInfo.marshal(dest_value);
		return true;
	}
	
}