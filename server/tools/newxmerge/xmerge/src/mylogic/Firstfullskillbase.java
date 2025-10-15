package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


public class Firstfullskillbase implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
		//只留一个服务器的数据即可
		return true;
	}

}

