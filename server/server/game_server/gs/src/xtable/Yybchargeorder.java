package xtable;

// typed table access point
public class Yybchargeorder {
	Yybchargeorder() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().yybchargeorder.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.YybOrder value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.YybOrder get(Long key) {
		return _Tables_.getInstance().yybchargeorder.get(key);
	}

	public static xbean.YybOrder get(Long key, xbean.YybOrder value) {
		return _Tables_.getInstance().yybchargeorder.get(key, value);
	}

	public static void insert(Long key, xbean.YybOrder value) {
		_Tables_.getInstance().yybchargeorder.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().yybchargeorder.delete(key);
	}

	public static boolean add(Long key, xbean.YybOrder value) {
		return _Tables_.getInstance().yybchargeorder.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().yybchargeorder.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.YybOrder> getCache() {
		return _Tables_.getInstance().yybchargeorder.getCache();
	}

	public static mkdb.TTable<Long, xbean.YybOrder> getTable() {
		return _Tables_.getInstance().yybchargeorder;
	}

	public static xbean.YybOrder select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YybOrder, xbean.YybOrder>() {
			public xbean.YybOrder get(xbean.YybOrder v) { return v.toData(); }
		});
	}

	public static Integer selectUserid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YybOrder, Integer>() {
				public Integer get(xbean.YybOrder v) { return v.getUserid(); }
			});
	}

	public static Long selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YybOrder, Long>() {
				public Long get(xbean.YybOrder v) { return v.getRoleid(); }
			});
	}

	public static Integer selectStatus(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YybOrder, Integer>() {
				public Integer get(xbean.YybOrder v) { return v.getStatus(); }
			});
	}

	public static String selectPlatname(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YybOrder, String>() {
				public String get(xbean.YybOrder v) { return v.getPlatname(); }
			});
	}

	public static Integer selectNum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YybOrder, Integer>() {
				public Integer get(xbean.YybOrder v) { return v.getNum(); }
			});
	}

	public static Integer selectLocalsaveamt(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YybOrder, Integer>() {
				public Integer get(xbean.YybOrder v) { return v.getLocalsaveamt(); }
			});
	}

	public static Integer selectLocalbalance(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YybOrder, Integer>() {
				public Integer get(xbean.YybOrder v) { return v.getLocalbalance(); }
			});
	}

	public static Integer selectLocalgenbalance(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YybOrder, Integer>() {
				public Integer get(xbean.YybOrder v) { return v.getLocalgenbalance(); }
			});
	}

	public static Long selectCreatetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YybOrder, Long>() {
				public Long get(xbean.YybOrder v) { return v.getCreatetime(); }
			});
	}

	public static Integer selectRetrytimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YybOrder, Integer>() {
				public Integer get(xbean.YybOrder v) { return v.getRetrytimes(); }
			});
	}

	public static Integer selectConsumetype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.YybOrder, Integer>() {
				public Integer get(xbean.YybOrder v) { return v.getConsumetype(); }
			});
	}

}
