package xtable;

// typed table access point
public class Takebackmarketcontainertab {
	Takebackmarketcontainertab() {
	}

	public static Integer get(Long key) {
		return _Tables_.getInstance().takebackmarketcontainertab.get(key);
	}

	public static Integer get(Long key, Integer value) {
		return _Tables_.getInstance().takebackmarketcontainertab.get(key, value);
	}

	public static void insert(Long key, Integer value) {
		_Tables_.getInstance().takebackmarketcontainertab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().takebackmarketcontainertab.delete(key);
	}

	public static boolean add(Long key, Integer value) {
		return _Tables_.getInstance().takebackmarketcontainertab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().takebackmarketcontainertab.remove(key);
	}

	public static mkdb.TTableCache<Long, Integer> getCache() {
		return _Tables_.getInstance().takebackmarketcontainertab.getCache();
	}

	public static mkdb.TTable<Long, Integer> getTable() {
		return _Tables_.getInstance().takebackmarketcontainertab;
	}

	public static Integer select(Long key) {
		return getTable().select(key, new mkdb.TField<Integer, Integer>() {
			public Integer get(Integer v) { return v; }
		});
	}

}
