package robot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import mkio.ILoginIns;

public class LoginRoleMgr {
	private static LoginRoleMgr instance = new LoginRoleMgr();

	private LoginRoleMgr() {
	}

	public static LoginRoleMgr getInstance() {
		return instance;
	}

	// key为roleid
	private Map<Long, LoginRole> roles = new ConcurrentHashMap<Long, LoginRole>();

	public LoginRole getLoginRole(long roleId) {
		return roles.get(roleId);
	}

	public void removeLoginRole(long roleId) {
		roles.remove(roleId);
	}

	public Map<Long, LoginRole> getRoles() {
		return roles;
	}

	public void addLoginRole(LoginRole role) {
		if (role.roleId <= 0)
			return;
		roles.put(role.roleId, role);
	}

	public static LoginRole findLoginRole(mkio.Protocol p) {
		ILoginIns ins = (ILoginIns) p.getContext();
		LoginUI ui = (LoginUI) ins.getLoginUI();
		return ui.getLoginRole();
	}

}
