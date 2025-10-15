package xtable;

// typed table access point
public class Professionleaderfightdata {
	Professionleaderfightdata() {
	}

	public static xbean.Monster get(Long key) {
		return _Tables_.getInstance().professionleaderfightdata.get(key);
	}

	public static xbean.Monster get(Long key, xbean.Monster value) {
		return _Tables_.getInstance().professionleaderfightdata.get(key, value);
	}

	public static void insert(Long key, xbean.Monster value) {
		_Tables_.getInstance().professionleaderfightdata.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().professionleaderfightdata.delete(key);
	}

	public static boolean add(Long key, xbean.Monster value) {
		return _Tables_.getInstance().professionleaderfightdata.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().professionleaderfightdata.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.Monster> getCache() {
		return _Tables_.getInstance().professionleaderfightdata.getCache();
	}

	public static mkdb.TTable<Long, xbean.Monster> getTable() {
		return _Tables_.getInstance().professionleaderfightdata;
	}

	public static xbean.Monster select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, xbean.Monster>() {
			public xbean.Monster get(xbean.Monster v) { return v.toData(); }
		});
	}

	public static Integer selectMonsterid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, Integer>() {
				public Integer get(xbean.Monster v) { return v.getMonsterid(); }
			});
	}

	public static Integer selectShape(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, Integer>() {
				public Integer get(xbean.Monster v) { return v.getShape(); }
			});
	}

	public static Integer selectSchool(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, Integer>() {
				public Integer get(xbean.Monster v) { return v.getSchool(); }
			});
	}

	public static Integer selectLevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, Integer>() {
				public Integer get(xbean.Monster v) { return v.getLevel(); }
			});
	}

	public static Float selectBodysize(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, Float>() {
				public Float get(xbean.Monster v) { return v.getBodysize(); }
			});
	}

	public static Integer selectHp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, Integer>() {
				public Integer get(xbean.Monster v) { return v.getHp(); }
			});
	}

	public static Integer selectUplimithp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, Integer>() {
				public Integer get(xbean.Monster v) { return v.getUplimithp(); }
			});
	}

	public static Integer selectMp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, Integer>() {
				public Integer get(xbean.Monster v) { return v.getMp(); }
			});
	}

	public static Integer selectSp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, Integer>() {
				public Integer get(xbean.Monster v) { return v.getSp(); }
			});
	}

	public static xbean.BasicFightProperties selectBfp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, xbean.BasicFightProperties>() {
				public xbean.BasicFightProperties get(xbean.Monster v) { return v.getBfp(); }
			});
	}

	public static java.util.Map<Integer, Float> selectEffects(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, java.util.Map<Integer, Float>>() {
				public java.util.Map<Integer, Float> get(xbean.Monster v) { return v.getEffectsAsData(); }
			});
	}

	public static java.util.Map<Integer, Float> selectFinalattrs(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, java.util.Map<Integer, Float>>() {
				public java.util.Map<Integer, Float> get(xbean.Monster v) { return v.getFinalattrsAsData(); }
			});
	}

	public static xbean.BuffAgent selectBuffagent(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, xbean.BuffAgent>() {
				public xbean.BuffAgent get(xbean.Monster v) { return v.getBuffagent(); }
			});
	}

	public static java.util.List<xbean.BattleAI> selectAis(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, java.util.List<xbean.BattleAI>>() {
				public java.util.List<xbean.BattleAI> get(xbean.Monster v) { return v.getAisAsData(); }
			});
	}

	public static Integer selectFightnpctype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, Integer>() {
				public Integer get(xbean.Monster v) { return v.getFightnpctype(); }
			});
	}

	public static Integer selectMonstertype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, Integer>() {
				public Integer get(xbean.Monster v) { return v.getMonstertype(); }
			});
	}

	public static Integer selectSpecialtype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, Integer>() {
				public Integer get(xbean.Monster v) { return v.getSpecialtype(); }
			});
	}

	public static java.util.List<xbean.MonsterSkill> selectSkills(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, java.util.List<xbean.MonsterSkill>>() {
				public java.util.List<xbean.MonsterSkill> get(xbean.Monster v) { return v.getSkillsAsData(); }
			});
	}

	public static java.util.List<Integer> selectImmunebuffs(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.Monster v) { return v.getImmunebuffsAsData(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectExtradata(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Monster, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.Monster v) { return v.getExtradataAsData(); }
			});
	}

}
