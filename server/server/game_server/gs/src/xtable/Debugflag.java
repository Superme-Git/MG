package xtable;

// typed table access point
public class Debugflag {
	Debugflag() {
	}

	public static xbean.DebugFlag get(Long key) {
		return _Tables_.getInstance().debugflag.get(key);
	}

	public static xbean.DebugFlag get(Long key, xbean.DebugFlag value) {
		return _Tables_.getInstance().debugflag.get(key, value);
	}

	public static void insert(Long key, xbean.DebugFlag value) {
		_Tables_.getInstance().debugflag.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().debugflag.delete(key);
	}

	public static boolean add(Long key, xbean.DebugFlag value) {
		return _Tables_.getInstance().debugflag.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().debugflag.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.DebugFlag> getCache() {
		return _Tables_.getInstance().debugflag.getCache();
	}

	public static mkdb.TTable<Long, xbean.DebugFlag> getTable() {
		return _Tables_.getInstance().debugflag;
	}

	public static xbean.DebugFlag select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.DebugFlag, xbean.DebugFlag>() {
			public xbean.DebugFlag get(xbean.DebugFlag v) { return v.toData(); }
		});
	}

	public static java.util.List<Integer> selectDebugs(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.DebugFlag, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.DebugFlag v) { return v.getDebugsAsData(); }
			});
	}

}
