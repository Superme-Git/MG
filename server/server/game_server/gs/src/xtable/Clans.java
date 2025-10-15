package xtable;

// typed table access point
public class Clans {
	Clans() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().clans.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.ClanInfo value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.ClanInfo get(Long key) {
		return _Tables_.getInstance().clans.get(key);
	}

	public static xbean.ClanInfo get(Long key, xbean.ClanInfo value) {
		return _Tables_.getInstance().clans.get(key, value);
	}

	public static void insert(Long key, xbean.ClanInfo value) {
		_Tables_.getInstance().clans.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().clans.delete(key);
	}

	public static boolean add(Long key, xbean.ClanInfo value) {
		return _Tables_.getInstance().clans.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().clans.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ClanInfo> getCache() {
		return _Tables_.getInstance().clans.getCache();
	}

	public static mkdb.TTable<Long, xbean.ClanInfo> getTable() {
		return _Tables_.getInstance().clans;
	}

	public static xbean.ClanInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, xbean.ClanInfo>() {
			public xbean.ClanInfo get(xbean.ClanInfo v) { return v.toData(); }
		});
	}

	public static Integer selectIndex(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Integer>() {
				public Integer get(xbean.ClanInfo v) { return v.getIndex(); }
			});
	}

	public static Long selectKey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Long>() {
				public Long get(xbean.ClanInfo v) { return v.getKey(); }
			});
	}

	public static String selectClanname(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, String>() {
				public String get(xbean.ClanInfo v) { return v.getClanname(); }
			});
	}

	public static String selectOldclanname(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, String>() {
				public String get(xbean.ClanInfo v) { return v.getOldclanname(); }
			});
	}

	public static Long selectCreator(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Long>() {
				public Long get(xbean.ClanInfo v) { return v.getCreator(); }
			});
	}

	public static Integer selectPopularity(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Integer>() {
				public Integer get(xbean.ClanInfo v) { return v.getPopularity(); }
			});
	}

	public static Integer selectClanlevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Integer>() {
				public Integer get(xbean.ClanInfo v) { return v.getClanlevel(); }
			});
	}

	public static Integer selectClanlevelmax(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Integer>() {
				public Integer get(xbean.ClanInfo v) { return v.getClanlevelmax(); }
			});
	}

	public static String selectClanaim(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, String>() {
				public String get(xbean.ClanInfo v) { return v.getClanaim(); }
			});
	}

	public static Long selectClanmaster(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Long>() {
				public Long get(xbean.ClanInfo v) { return v.getClanmaster(); }
			});
	}

	public static Long selectClanvicemaster(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Long>() {
				public Long get(xbean.ClanInfo v) { return v.getClanvicemaster(); }
			});
	}

	public static Integer selectClanrank(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Integer>() {
				public Integer get(xbean.ClanInfo v) { return v.getClanrank(); }
			});
	}

	public static java.util.Map<Integer, xbean.ClanSkill> selectClanskillmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, java.util.Map<Integer, xbean.ClanSkill>>() {
				public java.util.Map<Integer, xbean.ClanSkill> get(xbean.ClanInfo v) { return v.getClanskillmapAsData(); }
			});
	}

	public static java.util.Map<Long, xbean.ClanMemberInfo> selectMembers(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, java.util.Map<Long, xbean.ClanMemberInfo>>() {
				public java.util.Map<Long, xbean.ClanMemberInfo> get(xbean.ClanInfo v) { return v.getMembersAsData(); }
			});
	}

	public static java.util.Map<Long, Long> selectApplicants(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, java.util.Map<Long, Long>>() {
				public java.util.Map<Long, Long> get(xbean.ClanInfo v) { return v.getApplicantsAsData(); }
			});
	}

	public static Long selectCreatetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Long>() {
				public Long get(xbean.ClanInfo v) { return v.getCreatetime(); }
			});
	}

	public static Integer selectMoney(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Integer>() {
				public Integer get(xbean.ClanInfo v) { return v.getMoney(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectHouse(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.ClanInfo v) { return v.getHouseAsData(); }
			});
	}

	public static Integer selectBonus(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Integer>() {
				public Integer get(xbean.ClanInfo v) { return v.getBonus(); }
			});
	}

	public static Integer selectAutostate(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Integer>() {
				public Integer get(xbean.ClanInfo v) { return v.getAutostate(); }
			});
	}

	public static Integer selectTotalscore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Integer>() {
				public Integer get(xbean.ClanInfo v) { return v.getTotalscore(); }
			});
	}

	public static Integer selectFiremembernum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Integer>() {
				public Integer get(xbean.ClanInfo v) { return v.getFiremembernum(); }
			});
	}

	public static Integer selectClaninstservice(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Integer>() {
				public Integer get(xbean.ClanInfo v) { return v.getClaninstservice(); }
			});
	}

	public static java.util.List<xbean.ClanEventRecord> selectClaneventrecordlist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, java.util.List<xbean.ClanEventRecord>>() {
				public java.util.List<xbean.ClanEventRecord> get(xbean.ClanInfo v) { return v.getClaneventrecordlistAsData(); }
			});
	}

	public static Short selectRequestlevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Short>() {
				public Short get(xbean.ClanInfo v) { return v.getRequestlevel(); }
			});
	}

	public static xbean.ImpeachMent selectImpeachment(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, xbean.ImpeachMent>() {
				public xbean.ImpeachMent get(xbean.ClanInfo v) { return v.getImpeachment(); }
			});
	}

	public static Integer selectClandkp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Integer>() {
				public Integer get(xbean.ClanInfo v) { return v.getClandkp(); }
			});
	}

	public static Integer selectLastclandkp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ClanInfo, Integer>() {
				public Integer get(xbean.ClanInfo v) { return v.getLastclandkp(); }
			});
	}

}
