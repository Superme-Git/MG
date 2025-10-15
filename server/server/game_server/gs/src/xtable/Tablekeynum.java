package xtable;

// typed table access point
public class Tablekeynum {
	Tablekeynum() {
	}

	public static Integer get(Integer key) {
		return _Tables_.getInstance().tablekeynum.get(key);
	}

	public static Integer get(Integer key, Integer value) {
		return _Tables_.getInstance().tablekeynum.get(key, value);
	}

	public static void insert(Integer key, Integer value) {
		_Tables_.getInstance().tablekeynum.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().tablekeynum.delete(key);
	}

	public static boolean add(Integer key, Integer value) {
		return _Tables_.getInstance().tablekeynum.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().tablekeynum.remove(key);
	}

	public static mkdb.TTableCache<Integer, Integer> getCache() {
		return _Tables_.getInstance().tablekeynum.getCache();
	}

	public static mkdb.TTable<Integer, Integer> getTable() {
		return _Tables_.getInstance().tablekeynum;
	}

	public static Integer select(Integer key) {
		return getTable().select(key, new mkdb.TField<Integer, Integer>() {
			public Integer get(Integer v) { return v; }
		});
	}

}
