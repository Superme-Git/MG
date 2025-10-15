
package xbean;

public interface LDRoleInfoDes extends mkdb.Bean {
	public LDRoleInfoDes copy(); // deep clone
	public LDRoleInfoDes toData(); // a Data instance
	public LDRoleInfoDes toBean(); // a Bean instance
	public LDRoleInfoDes toDataIf(); // a Data instance If need. else return this
	public LDRoleInfoDes toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 角色Id
	public String getRolename(); // 角色名
	public com.locojoy.base.Octets getRolenameOctets(); // 角色名
	public int getShape(); // 角色造型
	public int getLevel(); // 角色等级
	public int getSchool(); // 职业
	public int getTeamnum(); // 队伍当前人数
	public int getTeamnummax(); // 队伍最大人数

	public void setRoleid(long _v_); // 角色Id
	public void setRolename(String _v_); // 角色名
	public void setRolenameOctets(com.locojoy.base.Octets _v_); // 角色名
	public void setShape(int _v_); // 角色造型
	public void setLevel(int _v_); // 角色等级
	public void setSchool(int _v_); // 职业
	public void setTeamnum(int _v_); // 队伍当前人数
	public void setTeamnummax(int _v_); // 队伍最大人数
}
