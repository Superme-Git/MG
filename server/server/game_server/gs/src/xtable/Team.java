package xtable;

// typed table access point
public class Team {
	Team() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().team.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.TeamInfo value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.TeamInfo get(Long key) {
		return _Tables_.getInstance().team.get(key);
	}

	public static xbean.TeamInfo get(Long key, xbean.TeamInfo value) {
		return _Tables_.getInstance().team.get(key, value);
	}

	public static void insert(Long key, xbean.TeamInfo value) {
		_Tables_.getInstance().team.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().team.delete(key);
	}

	public static boolean add(Long key, xbean.TeamInfo value) {
		return _Tables_.getInstance().team.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().team.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.TeamInfo> getCache() {
		return _Tables_.getInstance().team.getCache();
	}

	public static mkdb.TTable<Long, xbean.TeamInfo> getTable() {
		return _Tables_.getInstance().team;
	}

	public static xbean.TeamInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, xbean.TeamInfo>() {
			public xbean.TeamInfo get(xbean.TeamInfo v) { return v.toData(); }
		});
	}

	public static Long selectTeamleaderid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Long>() {
				public Long get(xbean.TeamInfo v) { return v.getTeamleaderid(); }
			});
	}

	public static Long selectCommanderroleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Long>() {
				public Long get(xbean.TeamInfo v) { return v.getCommanderroleid(); }
			});
	}

	public static Integer selectMinlevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Integer>() {
				public Integer get(xbean.TeamInfo v) { return v.getMinlevel(); }
			});
	}

	public static Integer selectMaxlevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Integer>() {
				public Integer get(xbean.TeamInfo v) { return v.getMaxlevel(); }
			});
	}

	public static Integer selectFormation(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Integer>() {
				public Integer get(xbean.TeamInfo v) { return v.getFormation(); }
			});
	}

	public static Integer selectFormationlevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Integer>() {
				public Integer get(xbean.TeamInfo v) { return v.getFormationlevel(); }
			});
	}

	public static Long selectSwitchleaderid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Long>() {
				public Long get(xbean.TeamInfo v) { return v.getSwitchleaderid(); }
			});
	}

	public static Long selectSwitchleadertime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Long>() {
				public Long get(xbean.TeamInfo v) { return v.getSwitchleadertime(); }
			});
	}

	public static Long selectSuccessswitchtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Long>() {
				public Long get(xbean.TeamInfo v) { return v.getSuccessswitchtime(); }
			});
	}

	public static java.util.List<xbean.TeamMember> selectMembers(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, java.util.List<xbean.TeamMember>>() {
				public java.util.List<xbean.TeamMember> get(xbean.TeamInfo v) { return v.getMembersAsData(); }
			});
	}

	public static java.util.Map<Long, Long> selectApplierids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, java.util.Map<Long, Long>>() {
				public java.util.Map<Long, Long> get(xbean.TeamInfo v) { return v.getApplieridsAsData(); }
			});
	}

	public static java.util.Map<Long, Long> selectInvitingids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, java.util.Map<Long, Long>>() {
				public java.util.Map<Long, Long> get(xbean.TeamInfo v) { return v.getInvitingidsAsData(); }
			});
	}

	public static Integer selectState(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Integer>() {
				public Integer get(xbean.TeamInfo v) { return v.getState(); }
			});
	}

	public static Integer selectSmapid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Integer>() {
				public Integer get(xbean.TeamInfo v) { return v.getSmapid(); }
			});
	}

	public static java.util.Map<Long, Long> selectHugs(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, java.util.Map<Long, Long>>() {
				public java.util.Map<Long, Long> get(xbean.TeamInfo v) { return v.getHugsAsData(); }
			});
	}

	public static Integer selectTargetid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Integer>() {
				public Integer get(xbean.TeamInfo v) { return v.getTargetid(); }
			});
	}

	public static Long selectOnekeytimestamp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Long>() {
				public Long get(xbean.TeamInfo v) { return v.getOnekeytimestamp(); }
			});
	}

	public static Long selectCreatetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TeamInfo, Long>() {
				public Long get(xbean.TeamInfo v) { return v.getCreatetime(); }
			});
	}

}
