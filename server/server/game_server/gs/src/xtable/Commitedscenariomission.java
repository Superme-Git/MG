package xtable;

// typed table access point
public class Commitedscenariomission {
	Commitedscenariomission() {
	}

	public static xbean.CommitedScenarioMission get(Long key) {
		return _Tables_.getInstance().commitedscenariomission.get(key);
	}

	public static xbean.CommitedScenarioMission get(Long key, xbean.CommitedScenarioMission value) {
		return _Tables_.getInstance().commitedscenariomission.get(key, value);
	}

	public static void insert(Long key, xbean.CommitedScenarioMission value) {
		_Tables_.getInstance().commitedscenariomission.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().commitedscenariomission.delete(key);
	}

	public static boolean add(Long key, xbean.CommitedScenarioMission value) {
		return _Tables_.getInstance().commitedscenariomission.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().commitedscenariomission.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.CommitedScenarioMission> getCache() {
		return _Tables_.getInstance().commitedscenariomission.getCache();
	}

	public static mkdb.TTable<Long, xbean.CommitedScenarioMission> getTable() {
		return _Tables_.getInstance().commitedscenariomission;
	}

	public static xbean.CommitedScenarioMission select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CommitedScenarioMission, xbean.CommitedScenarioMission>() {
			public xbean.CommitedScenarioMission get(xbean.CommitedScenarioMission v) { return v.toData(); }
		});
	}

	public static java.util.List<Integer> selectCommitted(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CommitedScenarioMission, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.CommitedScenarioMission v) { return v.getCommittedAsData(); }
			});
	}

}
