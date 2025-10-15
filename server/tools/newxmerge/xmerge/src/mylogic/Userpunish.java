package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class Userpunish implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {

		xbean.UserPunish xbeanSrc = xbean.Pod.newUserPunishData();
		xbean.UserPunish xbeanDest = xbean.Pod.newUserPunishData();
		xbeanSrc.unmarshal(src_value);
		xbeanDest.unmarshal(dest_value);

		xbeanDest.getRecords().addAll(xbeanSrc.getRecords());
		if (xbeanSrc.getReleasetime() > xbeanDest.getReleasetime())
			xbeanDest.setReleasetime(xbeanSrc.getReleasetime());
		
		dest_value.clear();
		xbeanDest.marshal(dest_value);
		return true;
	}

}
