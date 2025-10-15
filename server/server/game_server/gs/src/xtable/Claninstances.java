package xtable;

// typed table access point
public class Claninstances {
	Claninstances() {
	}

	public static xbean.ClanInstances get(Long key) {
		return _Tables_.getInstance().claninstances.get(key);
	}

	public static xbean.ClanInstances get(Long key, xbean.ClanInstances value) {
		return _Tables_.getInstance().claninstances.get(key, value);
	}

	public static void insert(Long key, xbean.ClanInstances value) {
		_Tables_.getInstance().claninstances.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().claninstances.delete(key);
	}

	public static boolean add(Long key, xbean.ClanInstances value) {
		return _Tables_.getInstance().claninstances.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().claninstances.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ClanInstances> getCache() {
		return _Tables_.getInstance().claninstances.getCache();
	}

	public static mkdb.TTable<Long, xbean.ClanInstances> getTable() {
		return _Tables_.getInstance().claninstances;
	}

	public static xbean.ClanInstances select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInstances, xbean.ClanInstances>() {
			public xbean.ClanInstances get(xbean.ClanInstances v) { return v.toData(); }
		});
	}

	public static Long selectClanid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInstances, Long>() {
				public Long get(xbean.ClanInstances v) { return v.getClanid(); }
			});
	}

	public static Long selectCurrentzonekey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInstances, Long>() {
				public Long get(xbean.ClanInstances v) { return v.getCurrentzonekey(); }
			});
	}

	public static java.util.Map<Integer, Long> selectInstkey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInstances, java.util.Map<Integer, Long>>() {
				public java.util.Map<Integer, Long> get(xbean.ClanInstances v) { return v.getInstkeyAsData(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectInststate(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInstances, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.ClanInstances v) { return v.getInststateAsData(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectInststep(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInstances, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.ClanInstances v) { return v.getInststepAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.ClanSaveInfo> selectSaveinfo(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInstances, java.util.Map<Integer, xbean.ClanSaveInfo>>() {
				public java.util.Map<Integer, xbean.ClanSaveInfo> get(xbean.ClanInstances v) { return v.getSaveinfoAsData(); }
			});
	}

	public static java.util.Map<Integer, Long> selectInstreset(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInstances, java.util.Map<Integer, Long>>() {
				public java.util.Map<Integer, Long> get(xbean.ClanInstances v) { return v.getInstresetAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.ClanBossInfo> selectBossinfo(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInstances, java.util.Map<Integer, xbean.ClanBossInfo>>() {
				public java.util.Map<Integer, xbean.ClanBossInfo> get(xbean.ClanInstances v) { return v.getBossinfoAsData(); }
			});
	}

}
