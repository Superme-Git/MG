package xtable;

// typed table access point
public class Battleflag {
	Battleflag() {
	}

	public static xbean.BattleFlags get(Long key) {
		return _Tables_.getInstance().battleflag.get(key);
	}

	public static xbean.BattleFlags get(Long key, xbean.BattleFlags value) {
		return _Tables_.getInstance().battleflag.get(key, value);
	}

	public static void insert(Long key, xbean.BattleFlags value) {
		_Tables_.getInstance().battleflag.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().battleflag.delete(key);
	}

	public static boolean add(Long key, xbean.BattleFlags value) {
		return _Tables_.getInstance().battleflag.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().battleflag.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.BattleFlags> getCache() {
		return _Tables_.getInstance().battleflag.getCache();
	}

	public static mkdb.TTable<Long, xbean.BattleFlags> getTable() {
		return _Tables_.getInstance().battleflag;
	}

	public static xbean.BattleFlags select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleFlags, xbean.BattleFlags>() {
			public xbean.BattleFlags get(xbean.BattleFlags v) { return v.toData(); }
		});
	}

	public static java.util.List<String> selectFriendflag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleFlags, java.util.List<String>>() {
				public java.util.List<String> get(xbean.BattleFlags v) { return v.getFriendflagAsData(); }
			});
	}

	public static java.util.List<String> selectEnemyflag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleFlags, java.util.List<String>>() {
				public java.util.List<String> get(xbean.BattleFlags v) { return v.getEnemyflagAsData(); }
			});
	}

}
