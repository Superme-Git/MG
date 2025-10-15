package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

/**
 * 测试使用的stub
 * 接口待定
 * */
public class User implements xmerge.IMerge {
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.User srcUser = xbean.Pod.newUserData(); 
		xbean.User dstUser = xbean.Pod.newUserData(); 
		srcUser.unmarshal(src_value);
		dstUser.unmarshal(dest_value);
		dstUser.setCreatetime(Math.max(srcUser.getCreatetime(), dstUser.getCreatetime()));
		dstUser.getIdlist().addAll(srcUser.getIdlist());
		
		dest_value.clear();
		dstUser.marshal(dest_value);
		return true;
	}

}
