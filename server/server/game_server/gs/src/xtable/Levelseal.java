package xtable;

// typed table access point
public class Levelseal {
	Levelseal() {
	}

	public static xbean.LevelSeal get(Integer key) {
		return _Tables_.getInstance().levelseal.get(key);
	}

	public static xbean.LevelSeal get(Integer key, xbean.LevelSeal value) {
		return _Tables_.getInstance().levelseal.get(key, value);
	}

	public static void insert(Integer key, xbean.LevelSeal value) {
		_Tables_.getInstance().levelseal.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().levelseal.delete(key);
	}

	public static boolean add(Integer key, xbean.LevelSeal value) {
		return _Tables_.getInstance().levelseal.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().levelseal.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.LevelSeal> getCache() {
		return _Tables_.getInstance().levelseal.getCache();
	}

	public static mkdb.TTable<Integer, xbean.LevelSeal> getTable() {
		return _Tables_.getInstance().levelseal;
	}

	public static xbean.LevelSeal select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.LevelSeal, xbean.LevelSeal>() {
			public xbean.LevelSeal get(xbean.LevelSeal v) { return v.toData(); }
		});
	}

	public static Integer selectLevel(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.LevelSeal, Integer>() {
				public Integer get(xbean.LevelSeal v) { return v.getLevel(); }
			});
	}

	public static Integer selectRolenum(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.LevelSeal, Integer>() {
				public Integer get(xbean.LevelSeal v) { return v.getRolenum(); }
			});
	}

}
