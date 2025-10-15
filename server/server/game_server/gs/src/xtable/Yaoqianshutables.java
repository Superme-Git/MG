package xtable;

// typed table access point
public class Yaoqianshutables {
	Yaoqianshutables() {
	}

	public static xbean.YaoQianShuInfo get(Long key) {
		return _Tables_.getInstance().yaoqianshutables.get(key);
	}

	public static xbean.YaoQianShuInfo get(Long key, xbean.YaoQianShuInfo value) {
		return _Tables_.getInstance().yaoqianshutables.get(key, value);
	}

	public static void insert(Long key, xbean.YaoQianShuInfo value) {
		_Tables_.getInstance().yaoqianshutables.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().yaoqianshutables.delete(key);
	}

	public static boolean add(Long key, xbean.YaoQianShuInfo value) {
		return _Tables_.getInstance().yaoqianshutables.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().yaoqianshutables.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.YaoQianShuInfo> getCache() {
		return _Tables_.getInstance().yaoqianshutables.getCache();
	}

	public static mkdb.TTable<Long, xbean.YaoQianShuInfo> getTable() {
		return _Tables_.getInstance().yaoqianshutables;
	}

	public static xbean.YaoQianShuInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YaoQianShuInfo, xbean.YaoQianShuInfo>() {
			public xbean.YaoQianShuInfo get(xbean.YaoQianShuInfo v) { return v.toData(); }
		});
	}

	public static Long selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YaoQianShuInfo, Long>() {
				public Long get(xbean.YaoQianShuInfo v) { return v.getRoleid(); }
			});
	}

	public static java.util.List<Long> selectLookroleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YaoQianShuInfo, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.YaoQianShuInfo v) { return v.getLookroleidsAsData(); }
			});
	}

	public static Integer selectMapid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YaoQianShuInfo, Integer>() {
				public Integer get(xbean.YaoQianShuInfo v) { return v.getMapid(); }
			});
	}

	public static Integer selectPosx(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YaoQianShuInfo, Integer>() {
				public Integer get(xbean.YaoQianShuInfo v) { return v.getPosx(); }
			});
	}

	public static Integer selectPosy(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YaoQianShuInfo, Integer>() {
				public Integer get(xbean.YaoQianShuInfo v) { return v.getPosy(); }
			});
	}

}
