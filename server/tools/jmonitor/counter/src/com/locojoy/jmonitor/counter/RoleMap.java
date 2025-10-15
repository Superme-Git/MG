package com.locojoy.jmonitor.counter;

import java.util.HashMap;
import java.util.Map;

public class RoleMap<RoleBean> {

	private RoleBean defaultrolebean = null;
	private final Map<Long,RoleBean>	map = new HashMap<Long,RoleBean>();

	public RoleMap( RoleBean defaultbean) {
		this.defaultrolebean = defaultbean;
	}

	public boolean newRole( long rid, RoleBean rb) {
		return null == map.put( rid, rb);
	}

	public RoleBean removeRole( long rid) {
		return map.remove( rid);
	}
	
	public RoleBean getRole( long rid) {
		RoleBean rb = map.get( rid);
		if( null == rb)
			return defaultrolebean;
		else
			return rb;
	}
}
