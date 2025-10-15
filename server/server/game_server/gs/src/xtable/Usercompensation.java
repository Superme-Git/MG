package xtable;

// typed table access point
public class Usercompensation {
	Usercompensation() {
	}

	public static xbean.Compensation get(String key) {
		return _Tables_.getInstance().usercompensation.get(key);
	}

	public static xbean.Compensation get(String key, xbean.Compensation value) {
		return _Tables_.getInstance().usercompensation.get(key, value);
	}

	public static void insert(String key, xbean.Compensation value) {
		_Tables_.getInstance().usercompensation.insert(key, value);
	}

	public static void delete(String key) {
		_Tables_.getInstance().usercompensation.delete(key);
	}

	public static boolean add(String key, xbean.Compensation value) {
		return _Tables_.getInstance().usercompensation.add(key, value);
	}

	public static boolean remove(String key) {
		return _Tables_.getInstance().usercompensation.remove(key);
	}

	public static mkdb.TTableCache<String, xbean.Compensation> getCache() {
		return _Tables_.getInstance().usercompensation.getCache();
	}

	public static mkdb.TTable<String, xbean.Compensation> getTable() {
		return _Tables_.getInstance().usercompensation;
	}

	public static xbean.Compensation select(String key) {
		return getTable().select(key, new mkdb.TField<xbean.Compensation, xbean.Compensation>() {
			public xbean.Compensation get(xbean.Compensation v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.CompensationInfo> selectCompensationmap(String key) {
		return getTable().select(key, new mkdb.TField<xbean.Compensation, java.util.Map<Integer, xbean.CompensationInfo>>() {
				public java.util.Map<Integer, xbean.CompensationInfo> get(xbean.Compensation v) { return v.getCompensationmapAsData(); }
			});
	}

}
