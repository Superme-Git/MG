package xtable;

// typed table access point
public class Frole {
	Frole() {
	}

	public static xbean.ERole get(Long key) {
		return _Tables_.getInstance().frole.get(key);
	}

	public static xbean.ERole get(Long key, xbean.ERole value) {
		return _Tables_.getInstance().frole.get(key, value);
	}

	public static void insert(Long key, xbean.ERole value) {
		_Tables_.getInstance().frole.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().frole.delete(key);
	}

	public static boolean add(Long key, xbean.ERole value) {
		return _Tables_.getInstance().frole.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().frole.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ERole> getCache() {
		return _Tables_.getInstance().frole.getCache();
	}

	public static mkdb.TTable<Long, xbean.ERole> getTable() {
		return _Tables_.getInstance().frole;
	}

	public static xbean.ERole select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ERole, xbean.ERole>() {
			public xbean.ERole get(xbean.ERole v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Float> selectFinalattrs(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ERole, java.util.Map<Integer, Float>>() {
				public java.util.Map<Integer, Float> get(xbean.ERole v) { return v.getFinalattrsAsData(); }
			});
	}

	public static java.util.Map<Integer, Float> selectEffects(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ERole, java.util.Map<Integer, Float>>() {
				public java.util.Map<Integer, Float> get(xbean.ERole v) { return v.getEffectsAsData(); }
			});
	}

}
