package xtable;

// typed table access point
public class Livedieroleinfotab {
	Livedieroleinfotab() {
	}

	public static xbean.LiveDieRoleInfo get(Long key) {
		return _Tables_.getInstance().livedieroleinfotab.get(key);
	}

	public static xbean.LiveDieRoleInfo get(Long key, xbean.LiveDieRoleInfo value) {
		return _Tables_.getInstance().livedieroleinfotab.get(key, value);
	}

	public static void insert(Long key, xbean.LiveDieRoleInfo value) {
		_Tables_.getInstance().livedieroleinfotab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().livedieroleinfotab.delete(key);
	}

	public static boolean add(Long key, xbean.LiveDieRoleInfo value) {
		return _Tables_.getInstance().livedieroleinfotab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().livedieroleinfotab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.LiveDieRoleInfo> getCache() {
		return _Tables_.getInstance().livedieroleinfotab.getCache();
	}

	public static mkdb.TTable<Long, xbean.LiveDieRoleInfo> getTable() {
		return _Tables_.getInstance().livedieroleinfotab;
	}

	public static xbean.LiveDieRoleInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.LiveDieRoleInfo, xbean.LiveDieRoleInfo>() {
			public xbean.LiveDieRoleInfo get(xbean.LiveDieRoleInfo v) { return v.toData(); }
		});
	}

	public static Long selectObjectid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.LiveDieRoleInfo, Long>() {
				public Long get(xbean.LiveDieRoleInfo v) { return v.getObjectid(); }
			});
	}

	public static Long selectInvitationtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.LiveDieRoleInfo, Long>() {
				public Long get(xbean.LiveDieRoleInfo v) { return v.getInvitationtime(); }
			});
	}

	public static Integer selectSelecttype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.LiveDieRoleInfo, Integer>() {
				public Integer get(xbean.LiveDieRoleInfo v) { return v.getSelecttype(); }
			});
	}

	public static Integer selectAcceptflag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.LiveDieRoleInfo, Integer>() {
				public Integer get(xbean.LiveDieRoleInfo v) { return v.getAcceptflag(); }
			});
	}

}
