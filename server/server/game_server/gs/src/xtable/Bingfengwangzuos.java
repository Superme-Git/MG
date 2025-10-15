package xtable;

// typed table access point
public class Bingfengwangzuos {
	Bingfengwangzuos() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().bingfengwangzuos.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.BingFengWangZuo value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.BingFengWangZuo get(Long key) {
		return _Tables_.getInstance().bingfengwangzuos.get(key);
	}

	public static xbean.BingFengWangZuo get(Long key, xbean.BingFengWangZuo value) {
		return _Tables_.getInstance().bingfengwangzuos.get(key, value);
	}

	public static void insert(Long key, xbean.BingFengWangZuo value) {
		_Tables_.getInstance().bingfengwangzuos.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().bingfengwangzuos.delete(key);
	}

	public static boolean add(Long key, xbean.BingFengWangZuo value) {
		return _Tables_.getInstance().bingfengwangzuos.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().bingfengwangzuos.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.BingFengWangZuo> getCache() {
		return _Tables_.getInstance().bingfengwangzuos.getCache();
	}

	public static mkdb.TTable<Long, xbean.BingFengWangZuo> getTable() {
		return _Tables_.getInstance().bingfengwangzuos;
	}

	public static xbean.BingFengWangZuo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengWangZuo, xbean.BingFengWangZuo>() {
			public xbean.BingFengWangZuo get(xbean.BingFengWangZuo v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, Integer> selectDeathtimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengWangZuo, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.BingFengWangZuo v) { return v.getDeathtimesAsData(); }
			});
	}

}
