package xtable;

// typed table access point
public class Rolesafelock {
	Rolesafelock() {
	}

	public static xbean.RoleLockInfo get(Long key) {
		return _Tables_.getInstance().rolesafelock.get(key);
	}

	public static xbean.RoleLockInfo get(Long key, xbean.RoleLockInfo value) {
		return _Tables_.getInstance().rolesafelock.get(key, value);
	}

	public static void insert(Long key, xbean.RoleLockInfo value) {
		_Tables_.getInstance().rolesafelock.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolesafelock.delete(key);
	}

	public static boolean add(Long key, xbean.RoleLockInfo value) {
		return _Tables_.getInstance().rolesafelock.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolesafelock.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleLockInfo> getCache() {
		return _Tables_.getInstance().rolesafelock.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleLockInfo> getTable() {
		return _Tables_.getInstance().rolesafelock;
	}

	public static xbean.RoleLockInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleLockInfo, xbean.RoleLockInfo>() {
			public xbean.RoleLockInfo get(xbean.RoleLockInfo v) { return v.toData(); }
		});
	}

	public static String selectPassword(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleLockInfo, String>() {
				public String get(xbean.RoleLockInfo v) { return v.getPassword(); }
			});
	}

	public static Long selectUnlocktime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleLockInfo, Long>() {
				public Long get(xbean.RoleLockInfo v) { return v.getUnlocktime(); }
			});
	}

	public static Long selectForceunlocktime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleLockInfo, Long>() {
				public Long get(xbean.RoleLockInfo v) { return v.getForceunlocktime(); }
			});
	}

	public static Integer selectErrortimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleLockInfo, Integer>() {
				public Integer get(xbean.RoleLockInfo v) { return v.getErrortimes(); }
			});
	}

	public static Long selectFullerrortime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleLockInfo, Long>() {
				public Long get(xbean.RoleLockInfo v) { return v.getFullerrortime(); }
			});
	}

}
