package xtable;

// typed table access point
public class Skillroles {
	Skillroles() {
	}

	public static xbean.SkillRole get(Long key) {
		return _Tables_.getInstance().skillroles.get(key);
	}

	public static xbean.SkillRole get(Long key, xbean.SkillRole value) {
		return _Tables_.getInstance().skillroles.get(key, value);
	}

	public static void insert(Long key, xbean.SkillRole value) {
		_Tables_.getInstance().skillroles.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().skillroles.delete(key);
	}

	public static boolean add(Long key, xbean.SkillRole value) {
		return _Tables_.getInstance().skillroles.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().skillroles.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.SkillRole> getCache() {
		return _Tables_.getInstance().skillroles.getCache();
	}

	public static mkdb.TTable<Long, xbean.SkillRole> getTable() {
		return _Tables_.getInstance().skillroles;
	}

	public static xbean.SkillRole select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SkillRole, xbean.SkillRole>() {
			public xbean.SkillRole get(xbean.SkillRole v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Integer> selectSkillbases(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SkillRole, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.SkillRole v) { return v.getSkillbasesAsData(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectInborns(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SkillRole, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.SkillRole v) { return v.getInbornsAsData(); }
			});
	}

	public static Long selectLastdegradetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SkillRole, Long>() {
				public Long get(xbean.SkillRole v) { return v.getLastdegradetime(); }
			});
	}

	public static java.util.Map<Integer, xbean.AssistSkill> selectAssistskills(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SkillRole, java.util.Map<Integer, xbean.AssistSkill>>() {
				public java.util.Map<Integer, xbean.AssistSkill> get(xbean.SkillRole v) { return v.getAssistskillsAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.PracticeSkill> selectPracticeskill(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SkillRole, java.util.Map<Integer, xbean.PracticeSkill>>() {
				public java.util.Map<Integer, xbean.PracticeSkill> get(xbean.SkillRole v) { return v.getPracticeskillAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.LiveSkill> selectLiveskill(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SkillRole, java.util.Map<Integer, xbean.LiveSkill>>() {
				public java.util.Map<Integer, xbean.LiveSkill> get(xbean.SkillRole v) { return v.getLiveskillAsData(); }
			});
	}

	public static java.util.List<xbean.SpecialSkill> selectSpecialskills(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SkillRole, java.util.List<xbean.SpecialSkill>>() {
				public java.util.List<xbean.SpecialSkill> get(xbean.SkillRole v) { return v.getSpecialskillsAsData(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectExtskills(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SkillRole, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.SkillRole v) { return v.getExtskillsAsData(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectSkillsequence(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SkillRole, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.SkillRole v) { return v.getSkillsequenceAsData(); }
			});
	}

	public static java.util.Map<Integer, Long> selectLastusedtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SkillRole, java.util.Map<Integer, Long>>() {
				public java.util.Map<Integer, Long> get(xbean.SkillRole v) { return v.getLastusedtimeAsData(); }
			});
	}

}
