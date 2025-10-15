package xtable;

// typed table access point
public class Instancetask {
	Instancetask() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().instancetask.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.InstanceInfo value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.InstanceInfo get(Long key) {
		return _Tables_.getInstance().instancetask.get(key);
	}

	public static xbean.InstanceInfo get(Long key, xbean.InstanceInfo value) {
		return _Tables_.getInstance().instancetask.get(key, value);
	}

	public static void insert(Long key, xbean.InstanceInfo value) {
		_Tables_.getInstance().instancetask.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().instancetask.delete(key);
	}

	public static boolean add(Long key, xbean.InstanceInfo value) {
		return _Tables_.getInstance().instancetask.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().instancetask.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.InstanceInfo> getCache() {
		return _Tables_.getInstance().instancetask.getCache();
	}

	public static mkdb.TTable<Long, xbean.InstanceInfo> getTable() {
		return _Tables_.getInstance().instancetask;
	}

	public static xbean.InstanceInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, xbean.InstanceInfo>() {
			public xbean.InstanceInfo get(xbean.InstanceInfo v) { return v.toData(); }
		});
	}

	public static Long selectOwnerid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, Long>() {
				public Long get(xbean.InstanceInfo v) { return v.getOwnerid(); }
			});
	}

	public static java.util.Set<Long> selectRoleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, java.util.Set<Long>>() {
				public java.util.Set<Long> get(xbean.InstanceInfo v) { return v.getRoleidsAsData(); }
			});
	}

	public static java.util.List<xbean.InstanceNpcInfo> selectNpcs(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, java.util.List<xbean.InstanceNpcInfo>>() {
				public java.util.List<xbean.InstanceNpcInfo> get(xbean.InstanceInfo v) { return v.getNpcsAsData(); }
			});
	}

	public static java.util.List<xbean.InstanceNpcInfo> selectAsistnpcs(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, java.util.List<xbean.InstanceNpcInfo>>() {
				public java.util.List<xbean.InstanceNpcInfo> get(xbean.InstanceInfo v) { return v.getAsistnpcsAsData(); }
			});
	}

	public static Integer selectCopyflag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, Integer>() {
				public Integer get(xbean.InstanceInfo v) { return v.getCopyflag(); }
			});
	}

	public static Integer selectInstanceid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, Integer>() {
				public Integer get(xbean.InstanceInfo v) { return v.getInstanceid(); }
			});
	}

	public static Integer selectCurrentstep(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, Integer>() {
				public Integer get(xbean.InstanceInfo v) { return v.getCurrentstep(); }
			});
	}

	public static Integer selectCurrentstate(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, Integer>() {
				public Integer get(xbean.InstanceInfo v) { return v.getCurrentstate(); }
			});
	}

	public static Integer selectCurrenttaskid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, Integer>() {
				public Integer get(xbean.InstanceInfo v) { return v.getCurrenttaskid(); }
			});
	}

	public static Integer selectServiceid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, Integer>() {
				public Integer get(xbean.InstanceInfo v) { return v.getServiceid(); }
			});
	}

	public static Integer selectCurrentmapid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, Integer>() {
				public Integer get(xbean.InstanceInfo v) { return v.getCurrentmapid(); }
			});
	}

	public static Long selectAccepttime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, Long>() {
				public Long get(xbean.InstanceInfo v) { return v.getAccepttime(); }
			});
	}

	public static Integer selectFinalcounts(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, Integer>() {
				public Integer get(xbean.InstanceInfo v) { return v.getFinalcounts(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectSubsaves(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceInfo, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.InstanceInfo v) { return v.getSubsavesAsData(); }
			});
	}

}
