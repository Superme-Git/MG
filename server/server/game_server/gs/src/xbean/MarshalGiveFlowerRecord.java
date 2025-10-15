
package xbean;

public interface MarshalGiveFlowerRecord extends mkdb.Bean {
	public MarshalGiveFlowerRecord copy(); // deep clone
	public MarshalGiveFlowerRecord toData(); // a Data instance
	public MarshalGiveFlowerRecord toBean(); // a Bean instance
	public MarshalGiveFlowerRecord toDataIf(); // a Data instance If need. else return this
	public MarshalGiveFlowerRecord toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // roleid by changhao
	public String getName(); // 玩家名字 by changhao
	public com.locojoy.base.Octets getNameOctets(); // 玩家名字 by changhao
	public int getSchool(); // 玩家职业 by changhao
	public long getNum(); // 送花 by changhao

	public void setRoleid(long _v_); // roleid by changhao
	public void setName(String _v_); // 玩家名字 by changhao
	public void setNameOctets(com.locojoy.base.Octets _v_); // 玩家名字 by changhao
	public void setSchool(int _v_); // 玩家职业 by changhao
	public void setNum(long _v_); // 送花 by changhao
}
