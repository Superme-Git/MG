package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

/**
 * 测试使用的stub，直接返回true
 * 接口待定
 * */
public class UserMergeImplementReturnTrue implements xmerge.IMerge {
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		src_value = dest_value;
		return true;
	}

}
