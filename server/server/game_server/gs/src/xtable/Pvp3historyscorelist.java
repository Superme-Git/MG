package xtable;

// typed table access point
public class Pvp3historyscorelist {
	Pvp3historyscorelist() {
	}

	public static xbean.PvP3ScoreRankList get(Integer key) {
		return _Tables_.getInstance().pvp3historyscorelist.get(key);
	}

	public static xbean.PvP3ScoreRankList get(Integer key, xbean.PvP3ScoreRankList value) {
		return _Tables_.getInstance().pvp3historyscorelist.get(key, value);
	}

	public static void insert(Integer key, xbean.PvP3ScoreRankList value) {
		_Tables_.getInstance().pvp3historyscorelist.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().pvp3historyscorelist.delete(key);
	}

	public static boolean add(Integer key, xbean.PvP3ScoreRankList value) {
		return _Tables_.getInstance().pvp3historyscorelist.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().pvp3historyscorelist.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.PvP3ScoreRankList> getCache() {
		return _Tables_.getInstance().pvp3historyscorelist.getCache();
	}

	public static mkdb.TTable<Integer, xbean.PvP3ScoreRankList> getTable() {
		return _Tables_.getInstance().pvp3historyscorelist;
	}

	public static xbean.PvP3ScoreRankList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3ScoreRankList, xbean.PvP3ScoreRankList>() {
			public xbean.PvP3ScoreRankList get(xbean.PvP3ScoreRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.PvP3ScoreRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3ScoreRankList, java.util.List<xbean.PvP3ScoreRecord>>() {
				public java.util.List<xbean.PvP3ScoreRecord> get(xbean.PvP3ScoreRankList v) { return v.getRecordsAsData(); }
			});
	}

}
