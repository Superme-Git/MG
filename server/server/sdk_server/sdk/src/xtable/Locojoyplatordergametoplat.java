package xtable;

// typed table access point
public class Locojoyplatordergametoplat {
	Locojoyplatordergametoplat() {
	}

	public static String get(String key) {
		return _Tables_.getInstance().locojoyplatordergametoplat.get(key);
	}

	public static String get(String key, String value) {
		return _Tables_.getInstance().locojoyplatordergametoplat.get(key, value);
	}

	public static void insert(String key, String value) {
		_Tables_.getInstance().locojoyplatordergametoplat.insert(key, value);
	}

	public static void delete(String key) {
		_Tables_.getInstance().locojoyplatordergametoplat.delete(key);
	}

	public static boolean add(String key, String value) {
		return _Tables_.getInstance().locojoyplatordergametoplat.add(key, value);
	}

	public static boolean remove(String key) {
		return _Tables_.getInstance().locojoyplatordergametoplat.remove(key);
	}

	public static mkdb.TTableCache<String, String> getCache() {
		return _Tables_.getInstance().locojoyplatordergametoplat.getCache();
	}

	public static mkdb.TTable<String, String> getTable() {
		return _Tables_.getInstance().locojoyplatordergametoplat;
	}

	public static String select(String key) {
		return getTable().select(key, new mkdb.TField<String, String>() {
			public String get(String v) { return v; }
		});
	}

}
