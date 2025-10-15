package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Livedieweeknumtab implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.LiveDieWeekNum dest_LiveDieWeekNum=xbean.Pod.newLiveDieWeekNumData();
		dest_LiveDieWeekNum.unmarshal(dest_value);
		
		dest_value.clear();
		dest_LiveDieWeekNum.marshal(dest_value);
		
		return true;
	}
	
}