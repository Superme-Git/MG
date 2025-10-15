package xtable;

// typed table access point
public class Awarditemlimit {
	Awarditemlimit() {
	}

	public static xbean.ActivityItemLimit get(Integer key) {
		return _Tables_.getInstance().awarditemlimit.get(key);
	}

	public static xbean.ActivityItemLimit get(Integer key, xbean.ActivityItemLimit value) {
		return _Tables_.getInstance().awarditemlimit.get(key, value);
	}

	public static void insert(Integer key, xbean.ActivityItemLimit value) {
		_Tables_.getInstance().awarditemlimit.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().awarditemlimit.delete(key);
	}

	public static boolean add(Integer key, xbean.ActivityItemLimit value) {
		return _Tables_.getInstance().awarditemlimit.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().awarditemlimit.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ActivityItemLimit> getCache() {
		return _Tables_.getInstance().awarditemlimit.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ActivityItemLimit> getTable() {
		return _Tables_.getInstance().awarditemlimit;
	}

	public static xbean.ActivityItemLimit select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityItemLimit, xbean.ActivityItemLimit>() {
			public xbean.ActivityItemLimit get(xbean.ActivityItemLimit v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.LimitItemInfo> selectLimititemmap(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityItemLimit, java.util.Map<Integer, xbean.LimitItemInfo>>() {
				public java.util.Map<Integer, xbean.LimitItemInfo> get(xbean.ActivityItemLimit v) { return v.getLimititemmapAsData(); }
			});
	}

}
