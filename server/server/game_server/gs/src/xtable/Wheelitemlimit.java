package xtable;

// typed table access point
public class Wheelitemlimit {
	Wheelitemlimit() {
	}

	public static xbean.WheelItemLimit get(Integer key) {
		return _Tables_.getInstance().wheelitemlimit.get(key);
	}

	public static xbean.WheelItemLimit get(Integer key, xbean.WheelItemLimit value) {
		return _Tables_.getInstance().wheelitemlimit.get(key, value);
	}

	public static void insert(Integer key, xbean.WheelItemLimit value) {
		_Tables_.getInstance().wheelitemlimit.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().wheelitemlimit.delete(key);
	}

	public static boolean add(Integer key, xbean.WheelItemLimit value) {
		return _Tables_.getInstance().wheelitemlimit.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().wheelitemlimit.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.WheelItemLimit> getCache() {
		return _Tables_.getInstance().wheelitemlimit.getCache();
	}

	public static mkdb.TTable<Integer, xbean.WheelItemLimit> getTable() {
		return _Tables_.getInstance().wheelitemlimit;
	}

	public static xbean.WheelItemLimit select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.WheelItemLimit, xbean.WheelItemLimit>() {
			public xbean.WheelItemLimit get(xbean.WheelItemLimit v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Integer> selectLimitmap(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.WheelItemLimit, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.WheelItemLimit v) { return v.getLimitmapAsData(); }
			});
	}

}
