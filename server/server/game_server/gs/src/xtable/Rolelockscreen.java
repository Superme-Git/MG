package xtable;

// typed table access point
public class Rolelockscreen {
	Rolelockscreen() {
	}

	public static Boolean get(Long key) {
		return _Tables_.getInstance().rolelockscreen.get(key);
	}

	public static Boolean get(Long key, Boolean value) {
		return _Tables_.getInstance().rolelockscreen.get(key, value);
	}

	public static void insert(Long key, Boolean value) {
		_Tables_.getInstance().rolelockscreen.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolelockscreen.delete(key);
	}

	public static boolean add(Long key, Boolean value) {
		return _Tables_.getInstance().rolelockscreen.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolelockscreen.remove(key);
	}

	public static mkdb.TTableCache<Long, Boolean> getCache() {
		return _Tables_.getInstance().rolelockscreen.getCache();
	}

	public static mkdb.TTable<Long, Boolean> getTable() {
		return _Tables_.getInstance().rolelockscreen;
	}

	public static Boolean select(Long key) {
		return getTable().select(key, new mkdb.TField<Boolean, Boolean>() {
			public Boolean get(Boolean v) { return v; }
		});
	}

}
