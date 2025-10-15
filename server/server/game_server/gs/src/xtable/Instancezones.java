package xtable;

// typed table access point
public class Instancezones {
	Instancezones() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().instancezones.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.InstanceZone value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.InstanceZone get(Long key) {
		return _Tables_.getInstance().instancezones.get(key);
	}

	public static xbean.InstanceZone get(Long key, xbean.InstanceZone value) {
		return _Tables_.getInstance().instancezones.get(key, value);
	}

	public static void insert(Long key, xbean.InstanceZone value) {
		_Tables_.getInstance().instancezones.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().instancezones.delete(key);
	}

	public static boolean add(Long key, xbean.InstanceZone value) {
		return _Tables_.getInstance().instancezones.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().instancezones.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.InstanceZone> getCache() {
		return _Tables_.getInstance().instancezones.getCache();
	}

	public static mkdb.TTable<Long, xbean.InstanceZone> getTable() {
		return _Tables_.getInstance().instancezones;
	}

	public static xbean.InstanceZone select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceZone, xbean.InstanceZone>() {
			public xbean.InstanceZone get(xbean.InstanceZone v) { return v.toData(); }
		});
	}

	public static Integer selectInstzoneid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceZone, Integer>() {
				public Integer get(xbean.InstanceZone v) { return v.getInstzoneid(); }
			});
	}

	public static Long selectOwnerid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceZone, Long>() {
				public Long get(xbean.InstanceZone v) { return v.getOwnerid(); }
			});
	}

	public static Integer selectZonestate(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceZone, Integer>() {
				public Integer get(xbean.InstanceZone v) { return v.getZonestate(); }
			});
	}

	public static Long selectExtid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceZone, Long>() {
				public Long get(xbean.InstanceZone v) { return v.getExtid(); }
			});
	}

	public static java.util.List<Long> selectRoleids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceZone, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.InstanceZone v) { return v.getRoleidsAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.InstanceSave> selectSaves(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceZone, java.util.Map<Integer, xbean.InstanceSave>>() {
				public java.util.Map<Integer, xbean.InstanceSave> get(xbean.InstanceZone v) { return v.getSavesAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.InstanceNpc> selectNpcs(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceZone, java.util.Map<Integer, xbean.InstanceNpc>>() {
				public java.util.Map<Integer, xbean.InstanceNpc> get(xbean.InstanceZone v) { return v.getNpcsAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.InstanceNpcServ> selectServices(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceZone, java.util.Map<Integer, xbean.InstanceNpcServ>>() {
				public java.util.Map<Integer, xbean.InstanceNpcServ> get(xbean.InstanceZone v) { return v.getServicesAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.InstanceTimer> selectTimers(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceZone, java.util.Map<Integer, xbean.InstanceTimer>>() {
				public java.util.Map<Integer, xbean.InstanceTimer> get(xbean.InstanceZone v) { return v.getTimersAsData(); }
			});
	}

	public static Long selectStarttime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceZone, Long>() {
				public Long get(xbean.InstanceZone v) { return v.getStarttime(); }
			});
	}

}
