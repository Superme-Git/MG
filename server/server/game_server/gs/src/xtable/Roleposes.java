package xtable;

// typed table access point
public class Roleposes {
	Roleposes() {
	}

	public static xbean.RolePos get(Long key) {
		return _Tables_.getInstance().roleposes.get(key);
	}

	public static xbean.RolePos get(Long key, xbean.RolePos value) {
		return _Tables_.getInstance().roleposes.get(key, value);
	}

	public static void insert(Long key, xbean.RolePos value) {
		_Tables_.getInstance().roleposes.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleposes.delete(key);
	}

	public static boolean add(Long key, xbean.RolePos value) {
		return _Tables_.getInstance().roleposes.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleposes.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RolePos> getCache() {
		return _Tables_.getInstance().roleposes.getCache();
	}

	public static mkdb.TTable<Long, xbean.RolePos> getTable() {
		return _Tables_.getInstance().roleposes;
	}

	public static xbean.RolePos select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolePos, xbean.RolePos>() {
			public xbean.RolePos get(xbean.RolePos v) { return v.toData(); }
		});
	}

	public static Integer selectMapid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolePos, Integer>() {
				public Integer get(xbean.RolePos v) { return v.getMapid(); }
			});
	}

	public static Integer selectPosx(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolePos, Integer>() {
				public Integer get(xbean.RolePos v) { return v.getPosx(); }
			});
	}

	public static Integer selectPosy(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolePos, Integer>() {
				public Integer get(xbean.RolePos v) { return v.getPosy(); }
			});
	}

	public static Long selectOwnerid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolePos, Long>() {
				public Long get(xbean.RolePos v) { return v.getOwnerid(); }
			});
	}

	public static Integer selectDynamicmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolePos, Integer>() {
				public Integer get(xbean.RolePos v) { return v.getDynamicmap(); }
			});
	}

	public static Integer selectDynamicposx(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolePos, Integer>() {
				public Integer get(xbean.RolePos v) { return v.getDynamicposx(); }
			});
	}

	public static Integer selectDynamicposy(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolePos, Integer>() {
				public Integer get(xbean.RolePos v) { return v.getDynamicposy(); }
			});
	}

	public static Integer selectStatus(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolePos, Integer>() {
				public Integer get(xbean.RolePos v) { return v.getStatus(); }
			});
	}

	public static Integer selectHastask(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolePos, Integer>() {
				public Integer get(xbean.RolePos v) { return v.getHastask(); }
			});
	}

}
