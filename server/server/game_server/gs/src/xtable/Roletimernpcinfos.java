package xtable;

// typed table access point
public class Roletimernpcinfos {
	Roletimernpcinfos() {
	}

	public static xbean.RoleActiveTimerNpcInfo get(Long key) {
		return _Tables_.getInstance().roletimernpcinfos.get(key);
	}

	public static xbean.RoleActiveTimerNpcInfo get(Long key, xbean.RoleActiveTimerNpcInfo value) {
		return _Tables_.getInstance().roletimernpcinfos.get(key, value);
	}

	public static void insert(Long key, xbean.RoleActiveTimerNpcInfo value) {
		_Tables_.getInstance().roletimernpcinfos.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roletimernpcinfos.delete(key);
	}

	public static boolean add(Long key, xbean.RoleActiveTimerNpcInfo value) {
		return _Tables_.getInstance().roletimernpcinfos.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roletimernpcinfos.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleActiveTimerNpcInfo> getCache() {
		return _Tables_.getInstance().roletimernpcinfos.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleActiveTimerNpcInfo> getTable() {
		return _Tables_.getInstance().roletimernpcinfos;
	}

	public static xbean.RoleActiveTimerNpcInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleActiveTimerNpcInfo, xbean.RoleActiveTimerNpcInfo>() {
			public xbean.RoleActiveTimerNpcInfo get(xbean.RoleActiveTimerNpcInfo v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.RoleTimerNpcInfo> selectActinfo(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleActiveTimerNpcInfo, java.util.Map<Integer, xbean.RoleTimerNpcInfo>>() {
				public java.util.Map<Integer, xbean.RoleTimerNpcInfo> get(xbean.RoleActiveTimerNpcInfo v) { return v.getActinfoAsData(); }
			});
	}

}
