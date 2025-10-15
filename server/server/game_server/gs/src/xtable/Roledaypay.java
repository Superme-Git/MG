package xtable;

// typed table access point
public class Roledaypay {
	Roledaypay() {
	}

	public static xbean.EDayPay get(Integer key) {
		return _Tables_.getInstance().roledaypay.get(key);
	}

	public static xbean.EDayPay get(Integer key, xbean.EDayPay value) {
		return _Tables_.getInstance().roledaypay.get(key, value);
	}

	public static void insert(Integer key, xbean.EDayPay value) {
		_Tables_.getInstance().roledaypay.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().roledaypay.delete(key);
	}

	public static boolean add(Integer key, xbean.EDayPay value) {
		return _Tables_.getInstance().roledaypay.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().roledaypay.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.EDayPay> getCache() {
		return _Tables_.getInstance().roledaypay.getCache();
	}

	public static mkdb.TTable<Integer, xbean.EDayPay> getTable() {
		return _Tables_.getInstance().roledaypay;
	}

	public static xbean.EDayPay select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.EDayPay, xbean.EDayPay>() {
			public xbean.EDayPay get(xbean.EDayPay v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, xbean.DayPay> selectRoleid2daypay(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.EDayPay, java.util.Map<Long, xbean.DayPay>>() {
				public java.util.Map<Long, xbean.DayPay> get(xbean.EDayPay v) { return v.getRoleid2daypayAsData(); }
			});
	}

}
