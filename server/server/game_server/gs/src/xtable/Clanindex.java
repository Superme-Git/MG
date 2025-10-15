package xtable;

// typed table access point
public class Clanindex {
	Clanindex() {
	}

	public static xbean.ClanIndex get(Integer key) {
		return _Tables_.getInstance().clanindex.get(key);
	}

	public static xbean.ClanIndex get(Integer key, xbean.ClanIndex value) {
		return _Tables_.getInstance().clanindex.get(key, value);
	}

	public static void insert(Integer key, xbean.ClanIndex value) {
		_Tables_.getInstance().clanindex.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().clanindex.delete(key);
	}

	public static boolean add(Integer key, xbean.ClanIndex value) {
		return _Tables_.getInstance().clanindex.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().clanindex.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ClanIndex> getCache() {
		return _Tables_.getInstance().clanindex.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ClanIndex> getTable() {
		return _Tables_.getInstance().clanindex;
	}

	public static xbean.ClanIndex select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanIndex, xbean.ClanIndex>() {
			public xbean.ClanIndex get(xbean.ClanIndex v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Long> selectIndex(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanIndex, java.util.Map<Integer, Long>>() {
				public java.util.Map<Integer, Long> get(xbean.ClanIndex v) { return v.getIndexAsData(); }
			});
	}

}
