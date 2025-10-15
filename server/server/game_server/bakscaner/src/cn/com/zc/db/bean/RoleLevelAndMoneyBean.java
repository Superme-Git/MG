package cn.com.zc.db.bean;

import java.util.List;


public class RoleLevelAndMoneyBean {
	
	private String tick;

	private List<RoleMoneyBean> roleMoneyList;
	
	private List<RoleLevelBean> roleLevelList;
	
	public List<RoleMoneyBean> getRoleMoneyList() {
		return roleMoneyList;
	}

	public void setRoleMoneyList(List<RoleMoneyBean> roleMoneyList) {
		this.roleMoneyList = roleMoneyList;
	}

	public List<RoleLevelBean> getRoleLevelList() {
		return roleLevelList;
	}

	public void setRoleLevelList(List<RoleLevelBean> roleLevelList) {
		this.roleLevelList = roleLevelList;
	}

	
	public String getTick() {
		return tick;
	}

	public void setTick(String tick) {
		this.tick = tick;
	}


}
