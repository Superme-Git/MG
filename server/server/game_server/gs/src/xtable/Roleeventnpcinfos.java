package xtable;

// typed table access point
public class Roleeventnpcinfos {
	Roleeventnpcinfos() {
	}

	public static xbean.RoleEventNpcInfoColumn get(Long key) {
		return _Tables_.getInstance().roleeventnpcinfos.get(key);
	}

	public static xbean.RoleEventNpcInfoColumn get(Long key, xbean.RoleEventNpcInfoColumn value) {
		return _Tables_.getInstance().roleeventnpcinfos.get(key, value);
	}

	public static void insert(Long key, xbean.RoleEventNpcInfoColumn value) {
		_Tables_.getInstance().roleeventnpcinfos.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleeventnpcinfos.delete(key);
	}

	public static boolean add(Long key, xbean.RoleEventNpcInfoColumn value) {
		return _Tables_.getInstance().roleeventnpcinfos.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleeventnpcinfos.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleEventNpcInfoColumn> getCache() {
		return _Tables_.getInstance().roleeventnpcinfos.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleEventNpcInfoColumn> getTable() {
		return _Tables_.getInstance().roleeventnpcinfos;
	}

	public static xbean.RoleEventNpcInfoColumn select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleEventNpcInfoColumn, xbean.RoleEventNpcInfoColumn>() {
			public xbean.RoleEventNpcInfoColumn get(xbean.RoleEventNpcInfoColumn v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.RoleEventNpcInfo> selectEventinfo(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleEventNpcInfoColumn, java.util.Map<Integer, xbean.RoleEventNpcInfo>>() {
				public java.util.Map<Integer, xbean.RoleEventNpcInfo> get(xbean.RoleEventNpcInfoColumn v) { return v.getEventinfoAsData(); }
			});
	}

}
