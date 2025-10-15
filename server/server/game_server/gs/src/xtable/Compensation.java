package xtable;

// typed table access point
public class Compensation {
	Compensation() {
	}

	public static xbean.Compensation get(Long key) {
		return _Tables_.getInstance().compensation.get(key);
	}

	public static xbean.Compensation get(Long key, xbean.Compensation value) {
		return _Tables_.getInstance().compensation.get(key, value);
	}

	public static void insert(Long key, xbean.Compensation value) {
		_Tables_.getInstance().compensation.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().compensation.delete(key);
	}

	public static boolean add(Long key, xbean.Compensation value) {
		return _Tables_.getInstance().compensation.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().compensation.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.Compensation> getCache() {
		return _Tables_.getInstance().compensation.getCache();
	}

	public static mkdb.TTable<Long, xbean.Compensation> getTable() {
		return _Tables_.getInstance().compensation;
	}

	public static xbean.Compensation select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Compensation, xbean.Compensation>() {
			public xbean.Compensation get(xbean.Compensation v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.CompensationInfo> selectCompensationmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Compensation, java.util.Map<Integer, xbean.CompensationInfo>>() {
				public java.util.Map<Integer, xbean.CompensationInfo> get(xbean.Compensation v) { return v.getCompensationmapAsData(); }
			});
	}

}
