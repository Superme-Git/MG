package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class Clanindex implements IMerge {
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
		xbean.ClanIndex src_index = xbean.Pod.newClanIndexData();
		xbean.ClanIndex dest_index = xbean.Pod.newClanIndexData();
		
		src_index.unmarshal(src_value);
		dest_index.unmarshal(dest_value);
		
		dest_index.getIndex().putAll(src_index.getIndex());
		
		dest_value.clear();
		dest_index.marshal(dest_value);
		
		return true;
	}

}

