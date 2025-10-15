
package xbean;

public interface LDTeamRoleInfoDes extends mkdb.Bean {
	public LDTeamRoleInfoDes copy(); // deep clone
	public LDTeamRoleInfoDes toData(); // a Data instance
	public LDTeamRoleInfoDes toBean(); // a Bean instance
	public LDTeamRoleInfoDes toDataIf(); // a Data instance If need. else return this
	public LDTeamRoleInfoDes toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 角色Id
	public String getRolename(); // 角色名
	public com.locojoy.base.Octets getRolenameOctets(); // 角色名
	public int getShape(); // 角色造型
	public int getLevel(); // 角色等级
	public int getSchool(); // 职业

	public void setRoleid(long _v_); // 角色Id
	public void setRolename(String _v_); // 角色名
	public void setRolenameOctets(com.locojoy.base.Octets _v_); // 角色名
	public void setShape(int _v_); // 角色造型
	public void setLevel(int _v_); // 角色等级
	public void setSchool(int _v_); // 职业
}
