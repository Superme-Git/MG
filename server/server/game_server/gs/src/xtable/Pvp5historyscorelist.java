package xtable;

// typed table access point
public class Pvp5historyscorelist {
	Pvp5historyscorelist() {
	}

	public static xbean.PvP5ScoreRankList get(Integer key) {
		return _Tables_.getInstance().pvp5historyscorelist.get(key);
	}

	public static xbean.PvP5ScoreRankList get(Integer key, xbean.PvP5ScoreRankList value) {
		return _Tables_.getInstance().pvp5historyscorelist.get(key, value);
	}

	public static void insert(Integer key, xbean.PvP5ScoreRankList value) {
		_Tables_.getInstance().pvp5historyscorelist.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().pvp5historyscorelist.delete(key);
	}

	public static boolean add(Integer key, xbean.PvP5ScoreRankList value) {
		return _Tables_.getInstance().pvp5historyscorelist.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().pvp5historyscorelist.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.PvP5ScoreRankList> getCache() {
		return _Tables_.getInstance().pvp5historyscorelist.getCache();
	}

	public static mkdb.TTable<Integer, xbean.PvP5ScoreRankList> getTable() {
		return _Tables_.getInstance().pvp5historyscorelist;
	}

	public static xbean.PvP5ScoreRankList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5ScoreRankList, xbean.PvP5ScoreRankList>() {
			public xbean.PvP5ScoreRankList get(xbean.PvP5ScoreRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.PvP5ScoreRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5ScoreRankList, java.util.List<xbean.PvP5ScoreRecord>>() {
				public java.util.List<xbean.PvP5ScoreRecord> get(xbean.PvP5ScoreRankList v) { return v.getRecordsAsData(); }
			});
	}

}
