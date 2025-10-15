package xtable;

// typed table access point
public class Petscorelist {
	Petscorelist() {
	}

	public static xbean.PetScoreRankList get(Integer key) {
		return _Tables_.getInstance().petscorelist.get(key);
	}

	public static xbean.PetScoreRankList get(Integer key, xbean.PetScoreRankList value) {
		return _Tables_.getInstance().petscorelist.get(key, value);
	}

	public static void insert(Integer key, xbean.PetScoreRankList value) {
		_Tables_.getInstance().petscorelist.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().petscorelist.delete(key);
	}

	public static boolean add(Integer key, xbean.PetScoreRankList value) {
		return _Tables_.getInstance().petscorelist.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().petscorelist.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.PetScoreRankList> getCache() {
		return _Tables_.getInstance().petscorelist.getCache();
	}

	public static mkdb.TTable<Integer, xbean.PetScoreRankList> getTable() {
		return _Tables_.getInstance().petscorelist;
	}

	public static xbean.PetScoreRankList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PetScoreRankList, xbean.PetScoreRankList>() {
			public xbean.PetScoreRankList get(xbean.PetScoreRankList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.PetScoreListRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PetScoreRankList, java.util.List<xbean.PetScoreListRecord>>() {
				public java.util.List<xbean.PetScoreListRecord> get(xbean.PetScoreRankList v) { return v.getRecordsAsData(); }
			});
	}

}
