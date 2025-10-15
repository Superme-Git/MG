package xtable;

// typed table access point
public class Commitedmission {
	Commitedmission() {
	}

	public static xbean.CommitedMissions get(Long key) {
		return _Tables_.getInstance().commitedmission.get(key);
	}

	public static xbean.CommitedMissions get(Long key, xbean.CommitedMissions value) {
		return _Tables_.getInstance().commitedmission.get(key, value);
	}

	public static void insert(Long key, xbean.CommitedMissions value) {
		_Tables_.getInstance().commitedmission.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().commitedmission.delete(key);
	}

	public static boolean add(Long key, xbean.CommitedMissions value) {
		return _Tables_.getInstance().commitedmission.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().commitedmission.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.CommitedMissions> getCache() {
		return _Tables_.getInstance().commitedmission.getCache();
	}

	public static mkdb.TTable<Long, xbean.CommitedMissions> getTable() {
		return _Tables_.getInstance().commitedmission;
	}

	public static xbean.CommitedMissions select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CommitedMissions, xbean.CommitedMissions>() {
			public xbean.CommitedMissions get(xbean.CommitedMissions v) { return v.toData(); }
		});
	}

	public static java.util.List<Integer> selectCommitted(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CommitedMissions, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.CommitedMissions v) { return v.getCommittedAsData(); }
			});
	}

}
