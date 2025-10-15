package xtable;

// typed table access point
public class Rolebestow {
	Rolebestow() {
	}

	public static xbean.RoleBestowInfo get(Long key) {
		return _Tables_.getInstance().rolebestow.get(key);
	}

	public static xbean.RoleBestowInfo get(Long key, xbean.RoleBestowInfo value) {
		return _Tables_.getInstance().rolebestow.get(key, value);
	}

	public static void insert(Long key, xbean.RoleBestowInfo value) {
		_Tables_.getInstance().rolebestow.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolebestow.delete(key);
	}

	public static boolean add(Long key, xbean.RoleBestowInfo value) {
		return _Tables_.getInstance().rolebestow.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolebestow.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleBestowInfo> getCache() {
		return _Tables_.getInstance().rolebestow.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleBestowInfo> getTable() {
		return _Tables_.getInstance().rolebestow;
	}

	public static xbean.RoleBestowInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleBestowInfo, xbean.RoleBestowInfo>() {
			public xbean.RoleBestowInfo get(xbean.RoleBestowInfo v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, xbean.RoleBestowCount> selectRolebestowinfo(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleBestowInfo, java.util.Map<Long, xbean.RoleBestowCount>>() {
				public java.util.Map<Long, xbean.RoleBestowCount> get(xbean.RoleBestowInfo v) { return v.getRolebestowinfoAsData(); }
			});
	}

}
