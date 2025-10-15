package xtable;

// typed table access point
public class Serviceinfos {
	Serviceinfos() {
	}

	public static xbean.ServiceInfo get(Integer key) {
		return _Tables_.getInstance().serviceinfos.get(key);
	}

	public static xbean.ServiceInfo get(Integer key, xbean.ServiceInfo value) {
		return _Tables_.getInstance().serviceinfos.get(key, value);
	}

	public static void insert(Integer key, xbean.ServiceInfo value) {
		_Tables_.getInstance().serviceinfos.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().serviceinfos.delete(key);
	}

	public static boolean add(Integer key, xbean.ServiceInfo value) {
		return _Tables_.getInstance().serviceinfos.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().serviceinfos.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ServiceInfo> getCache() {
		return _Tables_.getInstance().serviceinfos.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ServiceInfo> getTable() {
		return _Tables_.getInstance().serviceinfos;
	}

	public static xbean.ServiceInfo select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ServiceInfo, xbean.ServiceInfo>() {
			public xbean.ServiceInfo get(xbean.ServiceInfo v) { return v.toData(); }
		});
	}

	public static Integer selectYear(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ServiceInfo, Integer>() {
				public Integer get(xbean.ServiceInfo v) { return v.getYear(); }
			});
	}

	public static Integer selectWeeks(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ServiceInfo, Integer>() {
				public Integer get(xbean.ServiceInfo v) { return v.getWeeks(); }
			});
	}

	public static Integer selectActiverecommend(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ServiceInfo, Integer>() {
				public Integer get(xbean.ServiceInfo v) { return v.getActiverecommend(); }
			});
	}

	public static Long selectUpdatetime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ServiceInfo, Long>() {
				public Long get(xbean.ServiceInfo v) { return v.getUpdatetime(); }
			});
	}

	public static Integer selectDays(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ServiceInfo, Integer>() {
				public Integer get(xbean.ServiceInfo v) { return v.getDays(); }
			});
	}

}
