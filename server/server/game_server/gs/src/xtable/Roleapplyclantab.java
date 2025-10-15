package xtable;

// typed table access point
public class Roleapplyclantab {
	Roleapplyclantab() {
	}

	public static xbean.RoleApplyClanList get(Long key) {
		return _Tables_.getInstance().roleapplyclantab.get(key);
	}

	public static xbean.RoleApplyClanList get(Long key, xbean.RoleApplyClanList value) {
		return _Tables_.getInstance().roleapplyclantab.get(key, value);
	}

	public static void insert(Long key, xbean.RoleApplyClanList value) {
		_Tables_.getInstance().roleapplyclantab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleapplyclantab.delete(key);
	}

	public static boolean add(Long key, xbean.RoleApplyClanList value) {
		return _Tables_.getInstance().roleapplyclantab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleapplyclantab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleApplyClanList> getCache() {
		return _Tables_.getInstance().roleapplyclantab.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleApplyClanList> getTable() {
		return _Tables_.getInstance().roleapplyclantab;
	}

	public static xbean.RoleApplyClanList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleApplyClanList, xbean.RoleApplyClanList>() {
			public xbean.RoleApplyClanList get(xbean.RoleApplyClanList v) { return v.toData(); }
		});
	}

	public static Long selectOnekeylasttime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleApplyClanList, Long>() {
				public Long get(xbean.RoleApplyClanList v) { return v.getOnekeylasttime(); }
			});
	}

	public static java.util.Map<Long, xbean.RoleApplyClan> selectOnekeyapplymap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleApplyClanList, java.util.Map<Long, xbean.RoleApplyClan>>() {
				public java.util.Map<Long, xbean.RoleApplyClan> get(xbean.RoleApplyClanList v) { return v.getOnekeyapplymapAsData(); }
			});
	}

	public static java.util.Map<Long, xbean.RoleApplyClan> selectApplymap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleApplyClanList, java.util.Map<Long, xbean.RoleApplyClan>>() {
				public java.util.Map<Long, xbean.RoleApplyClan> get(xbean.RoleApplyClanList v) { return v.getApplymapAsData(); }
			});
	}

}
