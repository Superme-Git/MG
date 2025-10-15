package xtable;

// typed table access point
public class Properties {
	Properties() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().properties.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.Properties value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.Properties get(Long key) {
		return _Tables_.getInstance().properties.get(key);
	}

	public static xbean.Properties get(Long key, xbean.Properties value) {
		return _Tables_.getInstance().properties.get(key, value);
	}

	public static void insert(Long key, xbean.Properties value) {
		_Tables_.getInstance().properties.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().properties.delete(key);
	}

	public static boolean add(Long key, xbean.Properties value) {
		return _Tables_.getInstance().properties.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().properties.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.Properties> getCache() {
		return _Tables_.getInstance().properties.getCache();
	}

	public static mkdb.TTable<Long, xbean.Properties> getTable() {
		return _Tables_.getInstance().properties;
	}

	public static xbean.Properties select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, xbean.Properties>() {
			public xbean.Properties get(xbean.Properties v) { return v.toData(); }
		});
	}

	public static String selectRolename(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, String>() {
				public String get(xbean.Properties v) { return v.getRolename(); }
			});
	}

	public static java.util.List<String> selectUsedname(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, java.util.List<String>>() {
				public java.util.List<String> get(xbean.Properties v) { return v.getUsednameAsData(); }
			});
	}

	public static Integer selectLevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getLevel(); }
			});
	}

	public static Integer selectShape(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getShape(); }
			});
	}

	public static Integer selectSex(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getSex(); }
			});
	}

	public static Long selectExp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getExp(); }
			});
	}

	public static Integer selectRace(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getRace(); }
			});
	}

	public static Integer selectSchool(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getSchool(); }
			});
	}

	public static Integer selectColor(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getColor(); }
			});
	}

	public static Integer selectTitle(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getTitle(); }
			});
	}

	public static Integer selectHp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getHp(); }
			});
	}

	public static Integer selectUplimithp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getUplimithp(); }
			});
	}

	public static Integer selectWound(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getWound(); }
			});
	}

	public static Integer selectMp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getMp(); }
			});
	}

	public static Integer selectSp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getSp(); }
			});
	}

	public static xbean.BasicFightProperties selectBfp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, xbean.BasicFightProperties>() {
				public xbean.BasicFightProperties get(xbean.Properties v) { return v.getBfp(); }
			});
	}

	public static xbean.RoleAddPointProperties selectAddpointfp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, xbean.RoleAddPointProperties>() {
				public xbean.RoleAddPointProperties get(xbean.Properties v) { return v.getAddpointfp(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectPoint(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.Properties v) { return v.getPointAsData(); }
			});
	}

	public static Integer selectScheme(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getScheme(); }
			});
	}

	public static Integer selectSchemechanges(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getSchemechanges(); }
			});
	}

	public static Long selectSchemechangetimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getSchemechangetimes(); }
			});
	}

	public static Long selectAllexp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getAllexp(); }
			});
	}

	public static Long selectSceneid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getSceneid(); }
			});
	}

	public static Integer selectPosx(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getPosx(); }
			});
	}

	public static Integer selectPosy(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getPosy(); }
			});
	}

	public static Integer selectPosz(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getPosz(); }
			});
	}

	public static Long selectLaststaticsceneid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getLaststaticsceneid(); }
			});
	}

	public static Integer selectLaststaticposx(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getLaststaticposx(); }
			});
	}

	public static Integer selectLaststaticposy(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getLaststaticposy(); }
			});
	}

	public static Integer selectLaststaticposz(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getLaststaticposz(); }
			});
	}

	public static Integer selectFightpetkey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getFightpetkey(); }
			});
	}

	public static Integer selectShowpetkey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getShowpetkey(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectSysconfigmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.Properties v) { return v.getSysconfigmapAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.TitleInfo> selectTitles(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, java.util.Map<Integer, xbean.TitleInfo>>() {
				public java.util.Map<Integer, xbean.TitleInfo> get(xbean.Properties v) { return v.getTitlesAsData(); }
			});
	}

	public static Integer selectDefultform(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getDefultform(); }
			});
	}

	public static java.util.Map<Integer, xbean.FormBean> selectFormationsmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, java.util.Map<Integer, xbean.FormBean>>() {
				public java.util.Map<Integer, xbean.FormBean> get(xbean.Properties v) { return v.getFormationsmapAsData(); }
			});
	}

	public static Long selectClankey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getClankey(); }
			});
	}

	public static Long selectOldclankey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getOldclankey(); }
			});
	}

	public static Integer selectExitstate(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getExitstate(); }
			});
	}

	public static Long selectOnlinetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getOnlinetime(); }
			});
	}

	public static Long selectOfflinetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getOfflinetime(); }
			});
	}

	public static Long selectCreatetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getCreatetime(); }
			});
	}

	public static Long selectBindtel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getBindtel(); }
			});
	}

	public static Integer selectLogintimesofday(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getLogintimesofday(); }
			});
	}

	public static Integer selectIsgetbindtelaward(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getIsgetbindtelaward(); }
			});
	}

	public static Long selectBindteltime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getBindteltime(); }
			});
	}

	public static Integer selectIsbindtelagain(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getIsbindtelagain(); }
			});
	}

	public static Integer selectBindtelagainnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getBindtelagainnum(); }
			});
	}

	public static java.util.List<Long> selectUnbindteltime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.Properties v) { return v.getUnbindteltimeAsData(); }
			});
	}

	public static String selectPassword(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, String>() {
				public String get(xbean.Properties v) { return v.getPassword(); }
			});
	}

	public static Long selectForcedelpdtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getForcedelpdtime(); }
			});
	}

	public static Long selectForcedelendtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getForcedelendtime(); }
			});
	}

	public static Integer selectErrortimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getErrortimes(); }
			});
	}

	public static Long selectLockendtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getLockendtime(); }
			});
	}

	public static Integer selectIsopensafelock(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getIsopensafelock(); }
			});
	}

	public static Long selectSumonlinetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getSumonlinetime(); }
			});
	}

	public static Integer selectUserid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getUserid(); }
			});
	}

	public static Boolean selectAddranklist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Boolean>() {
				public Boolean get(xbean.Properties v) { return v.getAddranklist(); }
			});
	}

	public static Long selectDeletetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getDeletetime(); }
			});
	}

	public static Integer selectLastloginip(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getLastloginip(); }
			});
	}

	public static String selectPlatformuid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, String>() {
				public String get(xbean.Properties v) { return v.getPlatformuid(); }
			});
	}

	public static Integer selectViplevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getViplevel(); }
			});
	}

	public static Integer selectEnergy(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getEnergy(); }
			});
	}

	public static Integer selectPlaytimetimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getPlaytimetimes(); }
			});
	}

	public static Integer selectRolezonghemaxscore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getRolezonghemaxscore(); }
			});
	}

	public static Integer selectSkillscore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getSkillscore(); }
			});
	}

	public static Integer selectPetscore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getPetscore(); }
			});
	}

	public static Integer selectManypetscore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getManypetscore(); }
			});
	}

	public static Integer selectLevelscore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getLevelscore(); }
			});
	}

	public static Integer selectXiulianscroe(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getXiulianscroe(); }
			});
	}

	public static Integer selectRolescore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getRolescore(); }
			});
	}

	public static Integer selectEquipscore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getEquipscore(); }
			});
	}

	public static Long selectHistorymaxprofessioncontribute(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getHistorymaxprofessioncontribute(); }
			});
	}

	public static Integer selectRide(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getRide(); }
			});
	}

	public static Long selectBestpetid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getBestpetid(); }
			});
	}

	public static Integer selectRolecolor1(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getRolecolor1(); }
			});
	}

	public static Integer selectRolecolor2(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getRolecolor2(); }
			});
	}

	public static java.util.List<xbean.RoleColorType> selectColorroomlist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, java.util.List<xbean.RoleColorType>>() {
				public java.util.List<xbean.RoleColorType> get(xbean.Properties v) { return v.getColorroomlistAsData(); }
			});
	}

	public static Integer selectCruise(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getCruise(); }
			});
	}

	public static Integer selectCruise2(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getCruise2(); }
			});
	}

	public static Integer selectCruise3(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getCruise3(); }
			});
	}

	public static java.util.List<xbean.BattleAI> selectFighteai(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, java.util.List<xbean.BattleAI>>() {
				public java.util.List<xbean.BattleAI> get(xbean.Properties v) { return v.getFighteaiAsData(); }
			});
	}

	public static Integer selectEquipeffect(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getEquipeffect(); }
			});
	}

	public static Integer selectPilot(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getPilot(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectTupotips(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.Properties v) { return v.getTupotipsAsData(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectTupofinish(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.Properties v) { return v.getTupofinishAsData(); }
			});
	}

	public static Integer selectYingfuxishu(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getYingfuxishu(); }
			});
	}

	public static java.util.Map<Integer, String> selectDepotnameinfo(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, java.util.Map<Integer, String>>() {
				public java.util.Map<Integer, String> get(xbean.Properties v) { return v.getDepotnameinfoAsData(); }
			});
	}

	public static Long selectExpiretime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getExpiretime(); }
			});
	}

	public static Integer selectFirstprompt(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Integer>() {
				public Integer get(xbean.Properties v) { return v.getFirstprompt(); }
			});
	}

	public static Long selectLastchargetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, Long>() {
				public Long get(xbean.Properties v) { return v.getLastchargetime(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectLineconfigmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Properties, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.Properties v) { return v.getLineconfigmapAsData(); }
			});
	}

}
