
package xbean;

public interface PlatformInfo extends mkdb.Bean {
	public PlatformInfo copy(); // deep clone
	public PlatformInfo toData(); // a Data instance
	public PlatformInfo toBean(); // a Bean instance
	public PlatformInfo toDataIf(); // a Data instance If need. else return this
	public PlatformInfo toBeanIf(); // a Bean instance If need. else return this

	public int getId(); // 商家id
	public String getName(); // 商家名，UTF-16LE编码
	public com.locojoy.base.Octets getNameOctets(); // 商家名，UTF-16LE编码
	public int getDiscount(); // 折扣介于1-100之间

	public void setId(int _v_); // 商家id
	public void setName(String _v_); // 商家名，UTF-16LE编码
	public void setNameOctets(com.locojoy.base.Octets _v_); // 商家名，UTF-16LE编码
	public void setDiscount(int _v_); // 折扣介于1-100之间
}
