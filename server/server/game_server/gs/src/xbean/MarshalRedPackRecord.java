
package xbean;

public interface MarshalRedPackRecord extends mkdb.Bean {
	public MarshalRedPackRecord copy(); // deep clone
	public MarshalRedPackRecord toData(); // a Data instance
	public MarshalRedPackRecord toBean(); // a Bean instance
	public MarshalRedPackRecord toDataIf(); // a Data instance If need. else return this
	public MarshalRedPackRecord toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // roleid by changhao
	public String getName(); // 玩家名字 by changhao
	public com.locojoy.base.Octets getNameOctets(); // 玩家名字 by changhao
	public int getSchool(); // 玩家职业 by changhao
	public long getNum(); // 符石数量(普通服)金币数量(点卡服) by changhao

	public void setRoleid(long _v_); // roleid by changhao
	public void setName(String _v_); // 玩家名字 by changhao
	public void setNameOctets(com.locojoy.base.Octets _v_); // 玩家名字 by changhao
	public void setSchool(int _v_); // 玩家职业 by changhao
	public void setNum(long _v_); // 符石数量(普通服)金币数量(点卡服) by changhao
}
