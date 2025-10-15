
package xbean;

public interface IdState extends mkdb.Bean {
	public IdState copy(); // deep clone
	public IdState toData(); // a Data instance
	public IdState toBean(); // a Bean instance
	public IdState toDataIf(); // a Data instance If need. else return this
	public IdState toBeanIf(); // a Bean instance If need. else return this

	public long getNextid(); // ID������0���ѷ��䣻����0����������ָ����һ������id��
	public int getLocalid(); // ID�����Զ�˷�����
	public String getPeerip(); // ID�����Զ�˷�������ַ
	public com.locojoy.base.Octets getPeeripOctets(); // ID�����Զ�˷�������ַ
	public long getTime(); // ID�����ʱ��

	public void setNextid(long _v_); // ID������0���ѷ��䣻����0����������ָ����һ������id��
	public void setLocalid(int _v_); // ID�����Զ�˷�����
	public void setPeerip(String _v_); // ID�����Զ�˷�������ַ
	public void setPeeripOctets(com.locojoy.base.Octets _v_); // ID�����Զ�˷�������ַ
	public void setTime(long _v_); // ID�����ʱ��
}
