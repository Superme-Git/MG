package xtable;

// typed table access point
public class Claninstbestlevel {
	Claninstbestlevel() {
	}

	public static xbean.ClanInstBestLevel get(Integer key) {
		return _Tables_.getInstance().claninstbestlevel.get(key);
	}

	public static xbean.ClanInstBestLevel get(Integer key, xbean.ClanInstBestLevel value) {
		return _Tables_.getInstance().claninstbestlevel.get(key, value);
	}

	public static void insert(Integer key, xbean.ClanInstBestLevel value) {
		_Tables_.getInstance().claninstbestlevel.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().claninstbestlevel.delete(key);
	}

	public static boolean add(Integer key, xbean.ClanInstBestLevel value) {
		return _Tables_.getInstance().claninstbestlevel.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().claninstbestlevel.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ClanInstBestLevel> getCache() {
		return _Tables_.getInstance().claninstbestlevel.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ClanInstBestLevel> getTable() {
		return _Tables_.getInstance().claninstbestlevel;
	}

	public static xbean.ClanInstBestLevel select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInstBestLevel, xbean.ClanInstBestLevel>() {
			public xbean.ClanInstBestLevel get(xbean.ClanInstBestLevel v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Long> selectInstsave(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInstBestLevel, java.util.Map<Integer, Long>>() {
				public java.util.Map<Integer, Long> get(xbean.ClanInstBestLevel v) { return v.getInstsaveAsData(); }
			});
	}

}
