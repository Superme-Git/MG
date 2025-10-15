package xtable;

// typed table access point
public class Acceptedscenariomission {
	Acceptedscenariomission() {
	}

	public static xbean.AcceptedScenarioMission get(Long key) {
		return _Tables_.getInstance().acceptedscenariomission.get(key);
	}

	public static xbean.AcceptedScenarioMission get(Long key, xbean.AcceptedScenarioMission value) {
		return _Tables_.getInstance().acceptedscenariomission.get(key, value);
	}

	public static void insert(Long key, xbean.AcceptedScenarioMission value) {
		_Tables_.getInstance().acceptedscenariomission.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().acceptedscenariomission.delete(key);
	}

	public static boolean add(Long key, xbean.AcceptedScenarioMission value) {
		return _Tables_.getInstance().acceptedscenariomission.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().acceptedscenariomission.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.AcceptedScenarioMission> getCache() {
		return _Tables_.getInstance().acceptedscenariomission.getCache();
	}

	public static mkdb.TTable<Long, xbean.AcceptedScenarioMission> getTable() {
		return _Tables_.getInstance().acceptedscenariomission;
	}

	public static xbean.AcceptedScenarioMission select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AcceptedScenarioMission, xbean.AcceptedScenarioMission>() {
			public xbean.AcceptedScenarioMission get(xbean.AcceptedScenarioMission v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.Mission> selectAccepted(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AcceptedScenarioMission, java.util.Map<Integer, xbean.Mission>>() {
				public java.util.Map<Integer, xbean.Mission> get(xbean.AcceptedScenarioMission v) { return v.getAcceptedAsData(); }
			});
	}

}
