package xtable;

// typed table access point
public class User {
	User() {
	}

	public static xbean.User get(Integer key) {
		return _Tables_.getInstance().user.get(key);
	}

	public static xbean.User get(Integer key, xbean.User value) {
		return _Tables_.getInstance().user.get(key, value);
	}

	public static void insert(Integer key, xbean.User value) {
		_Tables_.getInstance().user.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().user.delete(key);
	}

	public static boolean add(Integer key, xbean.User value) {
		return _Tables_.getInstance().user.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().user.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.User> getCache() {
		return _Tables_.getInstance().user.getCache();
	}

	public static mkdb.TTable<Integer, xbean.User> getTable() {
		return _Tables_.getInstance().user;
	}

	public static xbean.User select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.User, xbean.User>() {
			public xbean.User get(xbean.User v) { return v.toData(); }
		});
	}

	public static Long selectPrevloginroleid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.User, Long>() {
				public Long get(xbean.User v) { return v.getPrevloginroleid(); }
			});
	}

	public static java.util.List<Long> selectIdlist(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.User, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.User v) { return v.getIdlistAsData(); }
			});
	}

	public static Long selectCreatetime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.User, Long>() {
				public Long get(xbean.User v) { return v.getCreatetime(); }
			});
	}

	public static Integer selectIsfirst(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.User, Integer>() {
				public Integer get(xbean.User v) { return v.getIsfirst(); }
			});
	}

}
