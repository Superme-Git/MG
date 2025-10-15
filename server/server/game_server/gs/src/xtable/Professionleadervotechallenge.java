package xtable;

// typed table access point
public class Professionleadervotechallenge {
	Professionleadervotechallenge() {
	}

	public static xbean.ProfessionLeaderVoteInfo get(Long key) {
		return _Tables_.getInstance().professionleadervotechallenge.get(key);
	}

	public static xbean.ProfessionLeaderVoteInfo get(Long key, xbean.ProfessionLeaderVoteInfo value) {
		return _Tables_.getInstance().professionleadervotechallenge.get(key, value);
	}

	public static void insert(Long key, xbean.ProfessionLeaderVoteInfo value) {
		_Tables_.getInstance().professionleadervotechallenge.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().professionleadervotechallenge.delete(key);
	}

	public static boolean add(Long key, xbean.ProfessionLeaderVoteInfo value) {
		return _Tables_.getInstance().professionleadervotechallenge.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().professionleadervotechallenge.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ProfessionLeaderVoteInfo> getCache() {
		return _Tables_.getInstance().professionleadervotechallenge.getCache();
	}

	public static mkdb.TTable<Long, xbean.ProfessionLeaderVoteInfo> getTable() {
		return _Tables_.getInstance().professionleadervotechallenge;
	}

	public static xbean.ProfessionLeaderVoteInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderVoteInfo, xbean.ProfessionLeaderVoteInfo>() {
			public xbean.ProfessionLeaderVoteInfo get(xbean.ProfessionLeaderVoteInfo v) { return v.toData(); }
		});
	}

	public static Long selectVotetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderVoteInfo, Long>() {
				public Long get(xbean.ProfessionLeaderVoteInfo v) { return v.getVotetime(); }
			});
	}

	public static Long selectChallengetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderVoteInfo, Long>() {
				public Long get(xbean.ProfessionLeaderVoteInfo v) { return v.getChallengetime(); }
			});
	}

}
