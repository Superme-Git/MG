package xtable;

// typed table access point
public class Role2impexam {
	Role2impexam() {
	}

	public static xbean.ImpExamRecord get(Long key) {
		return _Tables_.getInstance().role2impexam.get(key);
	}

	public static xbean.ImpExamRecord get(Long key, xbean.ImpExamRecord value) {
		return _Tables_.getInstance().role2impexam.get(key, value);
	}

	public static void insert(Long key, xbean.ImpExamRecord value) {
		_Tables_.getInstance().role2impexam.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().role2impexam.delete(key);
	}

	public static boolean add(Long key, xbean.ImpExamRecord value) {
		return _Tables_.getInstance().role2impexam.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().role2impexam.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ImpExamRecord> getCache() {
		return _Tables_.getInstance().role2impexam.getCache();
	}

	public static mkdb.TTable<Long, xbean.ImpExamRecord> getTable() {
		return _Tables_.getInstance().role2impexam;
	}

	public static xbean.ImpExamRecord select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, xbean.ImpExamRecord>() {
			public xbean.ImpExamRecord get(xbean.ImpExamRecord v) { return v.toData(); }
		});
	}

	public static Integer selectMaxvillexamright(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getMaxvillexamright(); }
			});
	}

	public static Integer selectCurrexamaccexp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getCurrexamaccexp(); }
			});
	}

	public static Integer selectCurrexamaccmoney(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getCurrexamaccmoney(); }
			});
	}

	public static Integer selectAccrightnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getAccrightnum(); }
			});
	}

	public static Integer selectCurrexamaccright(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getCurrexamaccright(); }
			});
	}

	public static Integer selectDelwrongval(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getDelwrongval(); }
			});
	}

	public static Integer selectChorightval(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getChorightval(); }
			});
	}

	public static Integer selectMaxcontinueright(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getMaxcontinueright(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectRightquesmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.ImpExamRecord v) { return v.getRightquesmapAsData(); }
			});
	}

	public static Long selectExamsendquestime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Long>() {
				public Long get(xbean.ImpExamRecord v) { return v.getExamsendquestime(); }
			});
	}

	public static Long selectExamconsumetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Long>() {
				public Long get(xbean.ImpExamRecord v) { return v.getExamconsumetime(); }
			});
	}

	public static Long selectMinstateexamtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Long>() {
				public Long get(xbean.ImpExamRecord v) { return v.getMinstateexamtime(); }
			});
	}

	public static Integer selectMaxstateexamright(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getMaxstateexamright(); }
			});
	}

	public static Long selectStateexamtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Long>() {
				public Long get(xbean.ImpExamRecord v) { return v.getStateexamtime(); }
			});
	}

	public static Long selectLastanswertime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Long>() {
				public Long get(xbean.ImpExamRecord v) { return v.getLastanswertime(); }
			});
	}

	public static Integer selectHelpcnt(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getHelpcnt(); }
			});
	}

	public static Integer selectStep(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getStep(); }
			});
	}

	public static Integer selectFlag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getFlag(); }
			});
	}

	public static java.util.List<Integer> selectQuesidlist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.ImpExamRecord v) { return v.getQuesidlistAsData(); }
			});
	}

	public static Integer selectLastquesid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getLastquesid(); }
			});
	}

	public static Integer selectLastanswer(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getLastanswer(); }
			});
	}

	public static Integer selectLastisright(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Integer>() {
				public Integer get(xbean.ImpExamRecord v) { return v.getLastisright(); }
			});
	}

	public static java.util.List<Integer> selectWorngqueslist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.ImpExamRecord v) { return v.getWorngqueslistAsData(); }
			});
	}

	public static Long selectBegintime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamRecord, Long>() {
				public Long get(xbean.ImpExamRecord v) { return v.getBegintime(); }
			});
	}

}
