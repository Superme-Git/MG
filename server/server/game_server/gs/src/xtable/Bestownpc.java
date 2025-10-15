package xtable;

// typed table access point
public class Bestownpc {
	Bestownpc() {
	}

	public static xbean.BestowNpcInfo get(Long key) {
		return _Tables_.getInstance().bestownpc.get(key);
	}

	public static xbean.BestowNpcInfo get(Long key, xbean.BestowNpcInfo value) {
		return _Tables_.getInstance().bestownpc.get(key, value);
	}

	public static void insert(Long key, xbean.BestowNpcInfo value) {
		_Tables_.getInstance().bestownpc.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().bestownpc.delete(key);
	}

	public static boolean add(Long key, xbean.BestowNpcInfo value) {
		return _Tables_.getInstance().bestownpc.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().bestownpc.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.BestowNpcInfo> getCache() {
		return _Tables_.getInstance().bestownpc.getCache();
	}

	public static mkdb.TTable<Long, xbean.BestowNpcInfo> getTable() {
		return _Tables_.getInstance().bestownpc;
	}

	public static xbean.BestowNpcInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BestowNpcInfo, xbean.BestowNpcInfo>() {
			public xbean.BestowNpcInfo get(xbean.BestowNpcInfo v) { return v.toData(); }
		});
	}

	public static Long selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BestowNpcInfo, Long>() {
				public Long get(xbean.BestowNpcInfo v) { return v.getRoleid(); }
			});
	}

}
