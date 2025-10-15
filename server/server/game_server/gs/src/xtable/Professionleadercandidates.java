package xtable;

// typed table access point
public class Professionleadercandidates {
	Professionleadercandidates() {
	}

	public static xbean.ProfessionLeaderCand get(Integer key) {
		return _Tables_.getInstance().professionleadercandidates.get(key);
	}

	public static xbean.ProfessionLeaderCand get(Integer key, xbean.ProfessionLeaderCand value) {
		return _Tables_.getInstance().professionleadercandidates.get(key, value);
	}

	public static void insert(Integer key, xbean.ProfessionLeaderCand value) {
		_Tables_.getInstance().professionleadercandidates.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().professionleadercandidates.delete(key);
	}

	public static boolean add(Integer key, xbean.ProfessionLeaderCand value) {
		return _Tables_.getInstance().professionleadercandidates.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().professionleadercandidates.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ProfessionLeaderCand> getCache() {
		return _Tables_.getInstance().professionleadercandidates.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ProfessionLeaderCand> getTable() {
		return _Tables_.getInstance().professionleadercandidates;
	}

	public static xbean.ProfessionLeaderCand select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderCand, xbean.ProfessionLeaderCand>() {
			public xbean.ProfessionLeaderCand get(xbean.ProfessionLeaderCand v) { return v.toData(); }
		});
	}

	public static java.util.List<Long> selectCandidatelist(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderCand, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.ProfessionLeaderCand v) { return v.getCandidatelistAsData(); }
			});
	}

}
