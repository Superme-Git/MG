package xtable;

// typed table access point
public class Role2weibonotify {
	Role2weibonotify() {
	}

	public static xbean.WeiBoNotify get(Long key) {
		return _Tables_.getInstance().role2weibonotify.get(key);
	}

	public static xbean.WeiBoNotify get(Long key, xbean.WeiBoNotify value) {
		return _Tables_.getInstance().role2weibonotify.get(key, value);
	}

	public static void insert(Long key, xbean.WeiBoNotify value) {
		_Tables_.getInstance().role2weibonotify.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().role2weibonotify.delete(key);
	}

	public static boolean add(Long key, xbean.WeiBoNotify value) {
		return _Tables_.getInstance().role2weibonotify.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().role2weibonotify.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.WeiBoNotify> getCache() {
		return _Tables_.getInstance().role2weibonotify.getCache();
	}

	public static mkdb.TTable<Long, xbean.WeiBoNotify> getTable() {
		return _Tables_.getInstance().role2weibonotify;
	}

	public static xbean.WeiBoNotify select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.WeiBoNotify, xbean.WeiBoNotify>() {
			public xbean.WeiBoNotify get(xbean.WeiBoNotify v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.WeiBoChoice> selectWeibo(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.WeiBoNotify, java.util.Map<Integer, xbean.WeiBoChoice>>() {
				public java.util.Map<Integer, xbean.WeiBoChoice> get(xbean.WeiBoNotify v) { return v.getWeiboAsData(); }
			});
	}

	public static Integer selectTakeawardflag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.WeiBoNotify, Integer>() {
				public Integer get(xbean.WeiBoNotify v) { return v.getTakeawardflag(); }
			});
	}

}
