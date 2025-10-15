
package xbean;

public interface MarshalReceFlowerRecord extends mkdb.Bean {
	public MarshalReceFlowerRecord copy(); // deep clone
	public MarshalReceFlowerRecord toData(); // a Data instance
	public MarshalReceFlowerRecord toBean(); // a Bean instance
	public MarshalReceFlowerRecord toDataIf(); // a Data instance If need. else return this
	public MarshalReceFlowerRecord toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // roleid by changhao
	public String getName(); // 玩家名字 by changhao
	public com.locojoy.base.Octets getNameOctets(); // 玩家名字 by changhao
	public int getSchool(); // 玩家职业 by changhao
	public long getNum(); // 收花 by changhao

	public void setRoleid(long _v_); // roleid by changhao
	public void setName(String _v_); // 玩家名字 by changhao
	public void setNameOctets(com.locojoy.base.Octets _v_); // 玩家名字 by changhao
	public void setSchool(int _v_); // 玩家职业 by changhao
	public void setNum(long _v_); // 收花 by changhao
}
