package xtable;

// typed table access point
public class Roleofflinehook {
	Roleofflinehook() {
	}

	public static xbean.OffLineHook get(Long key) {
		return _Tables_.getInstance().roleofflinehook.get(key);
	}

	public static xbean.OffLineHook get(Long key, xbean.OffLineHook value) {
		return _Tables_.getInstance().roleofflinehook.get(key, value);
	}

	public static void insert(Long key, xbean.OffLineHook value) {
		_Tables_.getInstance().roleofflinehook.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleofflinehook.delete(key);
	}

	public static boolean add(Long key, xbean.OffLineHook value) {
		return _Tables_.getInstance().roleofflinehook.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleofflinehook.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.OffLineHook> getCache() {
		return _Tables_.getInstance().roleofflinehook.getCache();
	}

	public static mkdb.TTable<Long, xbean.OffLineHook> getTable() {
		return _Tables_.getInstance().roleofflinehook;
	}

	public static xbean.OffLineHook select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.OffLineHook, xbean.OffLineHook>() {
			public xbean.OffLineHook get(xbean.OffLineHook v) { return v.toData(); }
		});
	}

	public static Long selectHooktime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.OffLineHook, Long>() {
				public Long get(xbean.OffLineHook v) { return v.getHooktime(); }
			});
	}

	public static Long selectRemainfivebeitime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.OffLineHook, Long>() {
				public Long get(xbean.OffLineHook v) { return v.getRemainfivebeitime(); }
			});
	}

	public static Long selectFivebeitimestart(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.OffLineHook, Long>() {
				public Long get(xbean.OffLineHook v) { return v.getFivebeitimestart(); }
			});
	}

	public static Long selectFivebeitotalusetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.OffLineHook, Long>() {
				public Long get(xbean.OffLineHook v) { return v.getFivebeitotalusetime(); }
			});
	}

	public static Integer selectFlag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.OffLineHook, Integer>() {
				public Integer get(xbean.OffLineHook v) { return v.getFlag(); }
			});
	}

}
