package xtable;

// typed table access point
public class Rolepingbirolesinfos {
	Rolepingbirolesinfos() {
	}

	public static xbean.RolePingBiRolesInfo get(Long key) {
		return _Tables_.getInstance().rolepingbirolesinfos.get(key);
	}

	public static xbean.RolePingBiRolesInfo get(Long key, xbean.RolePingBiRolesInfo value) {
		return _Tables_.getInstance().rolepingbirolesinfos.get(key, value);
	}

	public static void insert(Long key, xbean.RolePingBiRolesInfo value) {
		_Tables_.getInstance().rolepingbirolesinfos.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolepingbirolesinfos.delete(key);
	}

	public static boolean add(Long key, xbean.RolePingBiRolesInfo value) {
		return _Tables_.getInstance().rolepingbirolesinfos.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolepingbirolesinfos.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RolePingBiRolesInfo> getCache() {
		return _Tables_.getInstance().rolepingbirolesinfos.getCache();
	}

	public static mkdb.TTable<Long, xbean.RolePingBiRolesInfo> getTable() {
		return _Tables_.getInstance().rolepingbirolesinfos;
	}

	public static xbean.RolePingBiRolesInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolePingBiRolesInfo, xbean.RolePingBiRolesInfo>() {
			public xbean.RolePingBiRolesInfo get(xbean.RolePingBiRolesInfo v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, Long> selectPingbiroles(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolePingBiRolesInfo, java.util.Map<Long, Long>>() {
				public java.util.Map<Long, Long> get(xbean.RolePingBiRolesInfo v) { return v.getPingbirolesAsData(); }
			});
	}

}
