
package xbean;

public interface IdState extends xdb.Bean {
	public IdState copy(); // deep clone
	public IdState toData(); // a Data instance
	public IdState toBean(); // a Bean instance
	public IdState toDataIf(); // a Data instance If need. else return this
	public IdState toBeanIf(); // a Bean instance If need. else return this

	public long getNextid(); // ID。等于0，已分配；大于0，空闲链表，指向下一个空闲id；
	public int getLocalid(); // ID分配的远端分组编号
	public String getPeerip(); // ID分配的远端服务器地址
	public com.goldhuman.Common.Octets getPeeripOctets(); // ID分配的远端服务器地址
	public long getTime(); // ID分配的时间

	public void setNextid(long _v_); // ID。等于0，已分配；大于0，空闲链表，指向下一个空闲id；
	public void setLocalid(int _v_); // ID分配的远端分组编号
	public void setPeerip(String _v_); // ID分配的远端服务器地址
	public void setPeeripOctets(com.goldhuman.Common.Octets _v_); // ID分配的远端服务器地址
	public void setTime(long _v_); // ID分配的时间
}
