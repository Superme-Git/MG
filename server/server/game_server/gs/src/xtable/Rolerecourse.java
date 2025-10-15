package xtable;

// typed table access point
public class Rolerecourse {
	Rolerecourse() {
	}

	public static xbean.RoleRecourse get(Long key) {
		return _Tables_.getInstance().rolerecourse.get(key);
	}

	public static xbean.RoleRecourse get(Long key, xbean.RoleRecourse value) {
		return _Tables_.getInstance().rolerecourse.get(key, value);
	}

	public static void insert(Long key, xbean.RoleRecourse value) {
		_Tables_.getInstance().rolerecourse.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolerecourse.delete(key);
	}

	public static boolean add(Long key, xbean.RoleRecourse value) {
		return _Tables_.getInstance().rolerecourse.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolerecourse.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleRecourse> getCache() {
		return _Tables_.getInstance().rolerecourse.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleRecourse> getTable() {
		return _Tables_.getInstance().rolerecourse;
	}

	public static xbean.RoleRecourse select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleRecourse, xbean.RoleRecourse>() {
			public xbean.RoleRecourse get(xbean.RoleRecourse v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.TaskRecourse> selectRecoursetask(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleRecourse, java.util.Map<Integer, xbean.TaskRecourse>>() {
				public java.util.Map<Integer, xbean.TaskRecourse> get(xbean.RoleRecourse v) { return v.getRecoursetaskAsData(); }
			});
	}

}
