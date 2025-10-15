package xtable;

// typed table access point
public class Targetid2teammatch {
	Targetid2teammatch() {
	}

	public static xbean.ETeamMatch get(Integer key) {
		return _Tables_.getInstance().targetid2teammatch.get(key);
	}

	public static xbean.ETeamMatch get(Integer key, xbean.ETeamMatch value) {
		return _Tables_.getInstance().targetid2teammatch.get(key, value);
	}

	public static void insert(Integer key, xbean.ETeamMatch value) {
		_Tables_.getInstance().targetid2teammatch.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().targetid2teammatch.delete(key);
	}

	public static boolean add(Integer key, xbean.ETeamMatch value) {
		return _Tables_.getInstance().targetid2teammatch.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().targetid2teammatch.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ETeamMatch> getCache() {
		return _Tables_.getInstance().targetid2teammatch.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ETeamMatch> getTable() {
		return _Tables_.getInstance().targetid2teammatch;
	}

	public static xbean.ETeamMatch select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ETeamMatch, xbean.ETeamMatch>() {
			public xbean.ETeamMatch get(xbean.ETeamMatch v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, xbean.TeamMatch> selectRoleid2matchdata(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ETeamMatch, java.util.Map<Long, xbean.TeamMatch>>() {
				public java.util.Map<Long, xbean.TeamMatch> get(xbean.ETeamMatch v) { return v.getRoleid2matchdataAsData(); }
			});
	}

	public static java.util.Map<Long, xbean.TeamMatch> selectTeamid2matchdata(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ETeamMatch, java.util.Map<Long, xbean.TeamMatch>>() {
				public java.util.Map<Long, xbean.TeamMatch> get(xbean.ETeamMatch v) { return v.getTeamid2matchdataAsData(); }
			});
	}

	public static java.util.List<xbean.TeamMatch> selectTeammatchdatalist(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ETeamMatch, java.util.List<xbean.TeamMatch>>() {
				public java.util.List<xbean.TeamMatch> get(xbean.ETeamMatch v) { return v.getTeammatchdatalistAsData(); }
			});
	}

	public static java.util.List<xbean.TeamMatch> selectRolematchdatalist(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ETeamMatch, java.util.List<xbean.TeamMatch>>() {
				public java.util.List<xbean.TeamMatch> get(xbean.ETeamMatch v) { return v.getRolematchdatalistAsData(); }
			});
	}

}
