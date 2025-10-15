package xtable;

// typed table access point
public class Redpackroleinfolisttab {
	Redpackroleinfolisttab() {
	}

	public static xbean.RedPackRoleInfoList get(Long key) {
		return _Tables_.getInstance().redpackroleinfolisttab.get(key);
	}

	public static xbean.RedPackRoleInfoList get(Long key, xbean.RedPackRoleInfoList value) {
		return _Tables_.getInstance().redpackroleinfolisttab.get(key, value);
	}

	public static void insert(Long key, xbean.RedPackRoleInfoList value) {
		_Tables_.getInstance().redpackroleinfolisttab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().redpackroleinfolisttab.delete(key);
	}

	public static boolean add(Long key, xbean.RedPackRoleInfoList value) {
		return _Tables_.getInstance().redpackroleinfolisttab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().redpackroleinfolisttab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RedPackRoleInfoList> getCache() {
		return _Tables_.getInstance().redpackroleinfolisttab.getCache();
	}

	public static mkdb.TTable<Long, xbean.RedPackRoleInfoList> getTable() {
		return _Tables_.getInstance().redpackroleinfolisttab;
	}

	public static xbean.RedPackRoleInfoList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RedPackRoleInfoList, xbean.RedPackRoleInfoList>() {
			public xbean.RedPackRoleInfoList get(xbean.RedPackRoleInfoList v) { return v.toData(); }
		});
	}

	public static java.util.Map<String, xbean.RedPackInfo> selectRedpackinfolist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RedPackRoleInfoList, java.util.Map<String, xbean.RedPackInfo>>() {
				public java.util.Map<String, xbean.RedPackInfo> get(xbean.RedPackRoleInfoList v) { return v.getRedpackinfolistAsData(); }
			});
	}

}
