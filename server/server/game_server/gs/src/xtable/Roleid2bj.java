package xtable;

// typed table access point
public class Roleid2bj {
	Roleid2bj() {
	}

	public static Integer get(Long key) {
		return _Tables_.getInstance().roleid2bj.get(key);
	}

	public static Integer get(Long key, Integer value) {
		return _Tables_.getInstance().roleid2bj.get(key, value);
	}

	public static void insert(Long key, Integer value) {
		_Tables_.getInstance().roleid2bj.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleid2bj.delete(key);
	}

	public static boolean add(Long key, Integer value) {
		return _Tables_.getInstance().roleid2bj.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleid2bj.remove(key);
	}

	public static mkdb.TTableCache<Long, Integer> getCache() {
		return _Tables_.getInstance().roleid2bj.getCache();
	}

	public static mkdb.TTable<Long, Integer> getTable() {
		return _Tables_.getInstance().roleid2bj;
	}

	public static Integer select(Long key) {
		return getTable().select(key, new mkdb.TField<Integer, Integer>() {
			public Integer get(Integer v) { return v; }
		});
	}

}
