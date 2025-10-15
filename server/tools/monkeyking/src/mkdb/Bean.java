package mkdb;

import com.locojoy.base.Marshal.Marshal;

public interface Bean extends Marshal {
	// �ڲ��������ԣ��������������ڸ��ٵ��ԡ�
	public boolean xdbManaged();
	public Bean    xdbParent();
	public String  xdbVarname();
	public Long    xdbObjId();
	/**
	 * this method is out-of transaction. DO NOT use in transaction.
	 */
	public void _reset_unsafe_();

	// Const ת���ӿڡ�
	public Bean toConst();
	public boolean isConst(); // for debug

	// Data
	public boolean isData(); // for debug
}
