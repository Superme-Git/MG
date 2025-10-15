package xtable;

// typed table access point
public class Funopenclosetab {
	Funopenclosetab() {
	}

	public static xbean.FunOpenClose get(Integer key) {
		return _Tables_.getInstance().funopenclosetab.get(key);
	}

	public static xbean.FunOpenClose get(Integer key, xbean.FunOpenClose value) {
		return _Tables_.getInstance().funopenclosetab.get(key, value);
	}

	public static void insert(Integer key, xbean.FunOpenClose value) {
		_Tables_.getInstance().funopenclosetab.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().funopenclosetab.delete(key);
	}

	public static boolean add(Integer key, xbean.FunOpenClose value) {
		return _Tables_.getInstance().funopenclosetab.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().funopenclosetab.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.FunOpenClose> getCache() {
		return _Tables_.getInstance().funopenclosetab.getCache();
	}

	public static mkdb.TTable<Integer, xbean.FunOpenClose> getTable() {
		return _Tables_.getInstance().funopenclosetab;
	}

	public static xbean.FunOpenClose select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.FunOpenClose, xbean.FunOpenClose>() {
			public xbean.FunOpenClose get(xbean.FunOpenClose v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Integer> selectFunmap(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.FunOpenClose, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.FunOpenClose v) { return v.getFunmapAsData(); }
			});
	}

}
