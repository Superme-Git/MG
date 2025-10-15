package xtable;

// typed table access point
public class Roleid2activityquestion {
	Roleid2activityquestion() {
	}

	public static xbean.ActivityQuestion get(Long key) {
		return _Tables_.getInstance().roleid2activityquestion.get(key);
	}

	public static xbean.ActivityQuestion get(Long key, xbean.ActivityQuestion value) {
		return _Tables_.getInstance().roleid2activityquestion.get(key, value);
	}

	public static void insert(Long key, xbean.ActivityQuestion value) {
		_Tables_.getInstance().roleid2activityquestion.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleid2activityquestion.delete(key);
	}

	public static boolean add(Long key, xbean.ActivityQuestion value) {
		return _Tables_.getInstance().roleid2activityquestion.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleid2activityquestion.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ActivityQuestion> getCache() {
		return _Tables_.getInstance().roleid2activityquestion.getCache();
	}

	public static mkdb.TTable<Long, xbean.ActivityQuestion> getTable() {
		return _Tables_.getInstance().roleid2activityquestion;
	}

	public static xbean.ActivityQuestion select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityQuestion, xbean.ActivityQuestion>() {
			public xbean.ActivityQuestion get(xbean.ActivityQuestion v) { return v.toData(); }
		});
	}

	public static Long selectActivityquestionstarttime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityQuestion, Long>() {
				public Long get(xbean.ActivityQuestion v) { return v.getActivityquestionstarttime(); }
			});
	}

	public static java.util.List<Integer> selectQuestionids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityQuestion, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.ActivityQuestion v) { return v.getQuestionidsAsData(); }
			});
	}

	public static Integer selectCurquestionindex(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityQuestion, Integer>() {
				public Integer get(xbean.ActivityQuestion v) { return v.getCurquestionindex(); }
			});
	}

	public static Integer selectCurquestionstep(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityQuestion, Integer>() {
				public Integer get(xbean.ActivityQuestion v) { return v.getCurquestionstep(); }
			});
	}

	public static Integer selectAnswerrighttimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityQuestion, Integer>() {
				public Integer get(xbean.ActivityQuestion v) { return v.getAnswerrighttimes(); }
			});
	}

	public static Integer selectTotalanswernum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityQuestion, Integer>() {
				public Integer get(xbean.ActivityQuestion v) { return v.getTotalanswernum(); }
			});
	}

	public static Integer selectGrabreward(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityQuestion, Integer>() {
				public Integer get(xbean.ActivityQuestion v) { return v.getGrabreward(); }
			});
	}

	public static Integer selectTotalexp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityQuestion, Integer>() {
				public Integer get(xbean.ActivityQuestion v) { return v.getTotalexp(); }
			});
	}

	public static Integer selectTotalmoney(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityQuestion, Integer>() {
				public Integer get(xbean.ActivityQuestion v) { return v.getTotalmoney(); }
			});
	}

	public static Integer selectHelptimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ActivityQuestion, Integer>() {
				public Integer get(xbean.ActivityQuestion v) { return v.getHelptimes(); }
			});
	}

}
