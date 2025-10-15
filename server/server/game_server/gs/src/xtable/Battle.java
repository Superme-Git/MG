package xtable;

// typed table access point
public class Battle {
	Battle() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().battle.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.BattleInfo value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.BattleInfo get(Long key) {
		return _Tables_.getInstance().battle.get(key);
	}

	public static xbean.BattleInfo get(Long key, xbean.BattleInfo value) {
		return _Tables_.getInstance().battle.get(key, value);
	}

	public static void insert(Long key, xbean.BattleInfo value) {
		_Tables_.getInstance().battle.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().battle.delete(key);
	}

	public static boolean add(Long key, xbean.BattleInfo value) {
		return _Tables_.getInstance().battle.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().battle.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.BattleInfo> getCache() {
		return _Tables_.getInstance().battle.getCache();
	}

	public static mkdb.TTable<Long, xbean.BattleInfo> getTable() {
		return _Tables_.getInstance().battle;
	}

	public static Long selectBattleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Long>() {
				public Long get(xbean.BattleInfo v) { return v.getBattleid(); }
			});
	}

	public static Integer selectConfigid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getConfigid(); }
			});
	}

	public static Integer selectBackground(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getBackground(); }
			});
	}

	public static Integer selectBgm(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getBgm(); }
			});
	}

	public static Integer selectEnvironment(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getEnvironment(); }
			});
	}

	public static Integer selectGmactiontimetimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getGmactiontimetimes(); }
			});
	}

	public static Integer selectEnvchangefrom(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getEnvchangefrom(); }
			});
	}

	public static Integer selectPhase(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getPhase(); }
			});
	}

	public static Integer selectRound(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getRound(); }
			});
	}

	public static Boolean selectCanrunaway(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Boolean>() {
				public Boolean get(xbean.BattleInfo v) { return v.getCanrunaway(); }
			});
	}

	public static Integer selectAutokey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getAutokey(); }
			});
	}

	public static Integer selectBattletype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getBattletype(); }
			});
	}

	public static Integer selectAreatype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getAreatype(); }
			});
	}

	public static Integer selectAreaconf(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getAreaconf(); }
			});
	}

	public static Integer selectBabyflag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getBabyflag(); }
			});
	}

	public static Integer selectBabybagfull(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getBabybagfull(); }
			});
	}

	public static Long selectHostroleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Long>() {
				public Long get(xbean.BattleInfo v) { return v.getHostroleid(); }
			});
	}

	public static Long selectMonsterid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Long>() {
				public Long get(xbean.BattleInfo v) { return v.getMonsterid(); }
			});
	}

	public static Integer selectMonstertype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getMonstertype(); }
			});
	}

	public static Integer selectHostform(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getHostform(); }
			});
	}

	public static Integer selectGuestform(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getGuestform(); }
			});
	}

	public static Integer selectHostformlevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getHostformlevel(); }
			});
	}

	public static Integer selectGuestformlevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getGuestformlevel(); }
			});
	}

	public static Float selectHostformeffcet(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Float>() {
				public Float get(xbean.BattleInfo v) { return v.getHostformeffcet(); }
			});
	}

	public static java.util.Map<Long, Integer> selectRoleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.BattleInfo v) { return v.getRoleidsAsData(); }
			});
	}

	public static java.util.NavigableMap<Integer, xbean.Fighter> selectFighters(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.NavigableMap<Integer, xbean.Fighter>>() {
				public java.util.NavigableMap<Integer, xbean.Fighter> get(xbean.BattleInfo v) { return v.getFightersAsData(); }
			});
	}

	public static java.util.Set<Integer> selectSkipeddeadfighters(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.Set<Integer>>() {
				public java.util.Set<Integer> get(xbean.BattleInfo v) { return v.getSkipeddeadfightersAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.Monster> selectMonsters(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.Map<Integer, xbean.Monster>>() {
				public java.util.Map<Integer, xbean.Monster> get(xbean.BattleInfo v) { return v.getMonstersAsData(); }
			});
	}

	public static java.util.List<xbean.FailedMonster> selectFailedmonsters(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.List<xbean.FailedMonster>>() {
				public java.util.List<xbean.FailedMonster> get(xbean.BattleInfo v) { return v.getFailedmonstersAsData(); }
			});
	}

	public static java.util.NavigableMap<Integer, xbean.Fighter> selectInifighters(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.NavigableMap<Integer, xbean.Fighter>>() {
				public java.util.NavigableMap<Integer, xbean.Fighter> get(xbean.BattleInfo v) { return v.getInifightersAsData(); }
			});
	}

	public static java.util.Map<Long, Integer> selectAudience(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.BattleInfo v) { return v.getAudienceAsData(); }
			});
	}

	public static java.util.NavigableMap<Integer, xbean.Fighter> selectWatchers(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.NavigableMap<Integer, xbean.Fighter>>() {
				public java.util.NavigableMap<Integer, xbean.Fighter> get(xbean.BattleInfo v) { return v.getWatchersAsData(); }
			});
	}

	public static java.util.List<Long> selectOfflineroleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.BattleInfo v) { return v.getOfflineroleidsAsData(); }
			});
	}

	public static Long selectBattlebegintime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Long>() {
				public Long get(xbean.BattleInfo v) { return v.getBattlebegintime(); }
			});
	}

	public static Long selectRoundtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Long>() {
				public Long get(xbean.BattleInfo v) { return v.getRoundtime(); }
			});
	}

	public static Long selectScriptplaytime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Long>() {
				public Long get(xbean.BattleInfo v) { return v.getScriptplaytime(); }
			});
	}

	public static java.util.Map<Integer, xbean.Decision> selectDecisions(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.Map<Integer, xbean.Decision>>() {
				public java.util.Map<Integer, xbean.Decision> get(xbean.BattleInfo v) { return v.getDecisionsAsData(); }
			});
	}

	public static java.util.Set<Long> selectFeedbacks(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.Set<Long>>() {
				public java.util.Set<Long> get(xbean.BattleInfo v) { return v.getFeedbacksAsData(); }
			});
	}

	public static Integer selectBattleresult(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getBattleresult(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectRoundresult(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.BattleInfo v) { return v.getRoundresultAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.Protectors> selectProtectormap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.Map<Integer, xbean.Protectors>>() {
				public java.util.Map<Integer, xbean.Protectors> get(xbean.BattleInfo v) { return v.getProtectormapAsData(); }
			});
	}

	public static java.util.List<xbean.BattleAI> selectBattleais(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.List<xbean.BattleAI>>() {
				public java.util.List<xbean.BattleAI> get(xbean.BattleInfo v) { return v.getBattleaisAsData(); }
			});
	}

	public static java.util.Map<Integer, Float> selectBattledatas(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.Map<Integer, Float>>() {
				public java.util.Map<Integer, Float> get(xbean.BattleInfo v) { return v.getBattledatasAsData(); }
			});
	}

	public static java.util.List<Integer> selectItemtimelist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.BattleInfo v) { return v.getItemtimelistAsData(); }
			});
	}

	public static java.util.Map<Long, Integer> selectBattledeads(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.BattleInfo v) { return v.getBattledeadsAsData(); }
			});
	}

	public static Integer selectBattlemaxround(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getBattlemaxround(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectParams(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.BattleInfo v) { return v.getParamsAsData(); }
			});
	}

	public static java.util.List<Long> selectQuitteamroleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.BattleInfo v) { return v.getQuitteamroleidsAsData(); }
			});
	}

	public static java.util.List<Long> selectAbsentteamroleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.BattleInfo v) { return v.getAbsentteamroleidsAsData(); }
			});
	}

	public static java.util.List<Long> selectTeamidlist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.BattleInfo v) { return v.getTeamidlistAsData(); }
			});
	}

	public static Integer selectLivedieteam(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BattleInfo, Integer>() {
				public Integer get(xbean.BattleInfo v) { return v.getLivedieteam(); }
			});
	}

}
