package xtable;

// typed table access point
public class Roleinstancetask {
	Roleinstancetask() {
	}

	public static xbean.InstanceInfoCol get(Long key) {
		return _Tables_.getInstance().roleinstancetask.get(key);
	}

	public static xbean.InstanceInfoCol get(Long key, xbean.InstanceInfoCol value) {
		return _Tables_.getInstance().roleinstancetask.get(key, value);
	}

	public static void insert(Long key, xbean.InstanceInfoCol value) {
		_Tables_.getInstance().roleinstancetask.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleinstancetask.delete(key);
	}

	public static boolean add(Long key, xbean.InstanceInfoCol value) {
		return _Tables_.getInstance().roleinstancetask.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleinstancetask.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.InstanceInfoCol> getCache() {
		return _Tables_.getInstance().roleinstancetask.getCache();
	}

	public static mkdb.TTable<Long, xbean.InstanceInfoCol> getTable() {
		return _Tables_.getInstance().roleinstancetask;
	}

	public static xbean.InstanceInfoCol select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfoCol, xbean.InstanceInfoCol>() {
			public xbean.InstanceInfoCol get(xbean.InstanceInfoCol v) { return v.toData(); }
		});
	}

	public static Integer selectLastinstanceid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfoCol, Integer>() {
				public Integer get(xbean.InstanceInfoCol v) { return v.getLastinstanceid(); }
			});
	}

	public static Integer selectCounts(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfoCol, Integer>() {
				public Integer get(xbean.InstanceInfoCol v) { return v.getCounts(); }
			});
	}

	public static Long selectAccepttime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfoCol, Long>() {
				public Long get(xbean.InstanceInfoCol v) { return v.getAccepttime(); }
			});
	}

	public static java.util.Map<Integer, xbean.InstanceTaskInfo> selectInstinfo(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfoCol, java.util.Map<Integer, xbean.InstanceTaskInfo>>() {
				public java.util.Map<Integer, xbean.InstanceTaskInfo> get(xbean.InstanceInfoCol v) { return v.getInstinfoAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.InstanceTimeAwardInfo> selectInstcount(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfoCol, java.util.Map<Integer, xbean.InstanceTimeAwardInfo>>() {
				public java.util.Map<Integer, xbean.InstanceTimeAwardInfo> get(xbean.InstanceInfoCol v) { return v.getInstcountAsData(); }
			});
	}

	public static Integer selectFanpai(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfoCol, Integer>() {
				public Integer get(xbean.InstanceInfoCol v) { return v.getFanpai(); }
			});
	}

	public static Integer selectReset(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfoCol, Integer>() {
				public Integer get(xbean.InstanceInfoCol v) { return v.getReset(); }
			});
	}

}
