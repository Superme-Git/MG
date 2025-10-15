package xtable;

// typed table access point
public class Rolenumfornewserver {
	Rolenumfornewserver() {
	}

	public static xbean.ServerRoles get(Integer key) {
		return _Tables_.getInstance().rolenumfornewserver.get(key);
	}

	public static xbean.ServerRoles get(Integer key, xbean.ServerRoles value) {
		return _Tables_.getInstance().rolenumfornewserver.get(key, value);
	}

	public static void insert(Integer key, xbean.ServerRoles value) {
		_Tables_.getInstance().rolenumfornewserver.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().rolenumfornewserver.delete(key);
	}

	public static boolean add(Integer key, xbean.ServerRoles value) {
		return _Tables_.getInstance().rolenumfornewserver.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().rolenumfornewserver.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ServerRoles> getCache() {
		return _Tables_.getInstance().rolenumfornewserver.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ServerRoles> getTable() {
		return _Tables_.getInstance().rolenumfornewserver;
	}

	public static xbean.ServerRoles select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ServerRoles, xbean.ServerRoles>() {
			public xbean.ServerRoles get(xbean.ServerRoles v) { return v.toData(); }
		});
	}

	public static Integer selectRolenum(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ServerRoles, Integer>() {
				public Integer get(xbean.ServerRoles v) { return v.getRolenum(); }
			});
	}

	public static Long selectCreatetime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ServerRoles, Long>() {
				public Long get(xbean.ServerRoles v) { return v.getCreatetime(); }
			});
	}

}
