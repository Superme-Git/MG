package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

/**
 * ����ʹ�õ�stub��ֱ�ӷ���true
 * �ӿڴ���
 * */
public class UserMergeImplementReturnTrue implements xmerge.IMerge {
	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		src_value = dest_value;
		return true;
	}

}
