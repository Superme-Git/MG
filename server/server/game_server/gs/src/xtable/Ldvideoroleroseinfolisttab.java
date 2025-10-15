package xtable;

// typed table access point
public class Ldvideoroleroseinfolisttab {
	Ldvideoroleroseinfolisttab() {
	}

	public static xbean.LDVideoRoleRoseInfoList get(Long key) {
		return _Tables_.getInstance().ldvideoroleroseinfolisttab.get(key);
	}

	public static xbean.LDVideoRoleRoseInfoList get(Long key, xbean.LDVideoRoleRoseInfoList value) {
		return _Tables_.getInstance().ldvideoroleroseinfolisttab.get(key, value);
	}

	public static void insert(Long key, xbean.LDVideoRoleRoseInfoList value) {
		_Tables_.getInstance().ldvideoroleroseinfolisttab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().ldvideoroleroseinfolisttab.delete(key);
	}

	public static boolean add(Long key, xbean.LDVideoRoleRoseInfoList value) {
		return _Tables_.getInstance().ldvideoroleroseinfolisttab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().ldvideoroleroseinfolisttab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.LDVideoRoleRoseInfoList> getCache() {
		return _Tables_.getInstance().ldvideoroleroseinfolisttab.getCache();
	}

	public static mkdb.TTable<Long, xbean.LDVideoRoleRoseInfoList> getTable() {
		return _Tables_.getInstance().ldvideoroleroseinfolisttab;
	}

	public static xbean.LDVideoRoleRoseInfoList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.LDVideoRoleRoseInfoList, xbean.LDVideoRoleRoseInfoList>() {
			public xbean.LDVideoRoleRoseInfoList get(xbean.LDVideoRoleRoseInfoList v) { return v.toData(); }
		});
	}

	public static java.util.Map<String, xbean.LDVideoRoleRoseInfo> selectLdvideoroleroseinfolist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.LDVideoRoleRoseInfoList, java.util.Map<String, xbean.LDVideoRoleRoseInfo>>() {
				public java.util.Map<String, xbean.LDVideoRoleRoseInfo> get(xbean.LDVideoRoleRoseInfoList v) { return v.getLdvideoroleroseinfolistAsData(); }
			});
	}

}
