package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


public class Levelseal implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
		xbean.LevelSeal srcdata = xbean.Pod.newLevelSealData();
		srcdata.unmarshal(src_value);
		xbean.LevelSeal destdata = xbean.Pod.newLevelSealData();
		destdata.unmarshal(dest_value);
		destdata.setRolenum(srcdata.getRolenum() + destdata.getRolenum());
		dest_value.replace(destdata.marshal(new OctetsStream()));
		return true;
	}

}

