package xtable;

// typed table access point
public class Clanid2clanfightids {
	Clanid2clanfightids() {
	}

	public static xbean.EClanfightids get(Long key) {
		return _Tables_.getInstance().clanid2clanfightids.get(key);
	}

	public static xbean.EClanfightids get(Long key, xbean.EClanfightids value) {
		return _Tables_.getInstance().clanid2clanfightids.get(key, value);
	}

	public static void insert(Long key, xbean.EClanfightids value) {
		_Tables_.getInstance().clanid2clanfightids.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().clanid2clanfightids.delete(key);
	}

	public static boolean add(Long key, xbean.EClanfightids value) {
		return _Tables_.getInstance().clanid2clanfightids.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().clanid2clanfightids.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.EClanfightids> getCache() {
		return _Tables_.getInstance().clanid2clanfightids.getCache();
	}

	public static mkdb.TTable<Long, xbean.EClanfightids> getTable() {
		return _Tables_.getInstance().clanid2clanfightids;
	}

	public static xbean.EClanfightids select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EClanfightids, xbean.EClanfightids>() {
			public xbean.EClanfightids get(xbean.EClanfightids v) { return v.toData(); }
		});
	}

	public static java.util.List<Long> selectIds(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EClanfightids, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.EClanfightids v) { return v.getIdsAsData(); }
			});
	}

}
