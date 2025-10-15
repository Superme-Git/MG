package xtable;

// typed table access point
public class Clanfights {
	Clanfights() {
	}

	public static xbean.ClanFights get(Long key) {
		return _Tables_.getInstance().clanfights.get(key);
	}

	public static xbean.ClanFights get(Long key, xbean.ClanFights value) {
		return _Tables_.getInstance().clanfights.get(key, value);
	}

	public static void insert(Long key, xbean.ClanFights value) {
		_Tables_.getInstance().clanfights.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().clanfights.delete(key);
	}

	public static boolean add(Long key, xbean.ClanFights value) {
		return _Tables_.getInstance().clanfights.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().clanfights.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ClanFights> getCache() {
		return _Tables_.getInstance().clanfights.getCache();
	}

	public static mkdb.TTable<Long, xbean.ClanFights> getTable() {
		return _Tables_.getInstance().clanfights;
	}

	public static xbean.ClanFights select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFights, xbean.ClanFights>() {
			public xbean.ClanFights get(xbean.ClanFights v) { return v.toData(); }
		});
	}

	public static Long selectCreatetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFights, Long>() {
				public Long get(xbean.ClanFights v) { return v.getCreatetime(); }
			});
	}

	public static java.util.Map<Long, xbean.ClanFight> selectClan1vschan2(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanFights, java.util.Map<Long, xbean.ClanFight>>() {
				public java.util.Map<Long, xbean.ClanFight> get(xbean.ClanFights v) { return v.getClan1vschan2AsData(); }
			});
	}

}
