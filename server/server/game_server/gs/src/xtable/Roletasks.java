package xtable;

// typed table access point
public class Roletasks {
	Roletasks() {
	}

	public static xbean.TaskInfos get(Long key) {
		return _Tables_.getInstance().roletasks.get(key);
	}

	public static xbean.TaskInfos get(Long key, xbean.TaskInfos value) {
		return _Tables_.getInstance().roletasks.get(key, value);
	}

	public static void insert(Long key, xbean.TaskInfos value) {
		_Tables_.getInstance().roletasks.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roletasks.delete(key);
	}

	public static boolean add(Long key, xbean.TaskInfos value) {
		return _Tables_.getInstance().roletasks.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roletasks.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.TaskInfos> getCache() {
		return _Tables_.getInstance().roletasks.getCache();
	}

	public static mkdb.TTable<Long, xbean.TaskInfos> getTable() {
		return _Tables_.getInstance().roletasks;
	}

	public static xbean.TaskInfos select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TaskInfos, xbean.TaskInfos>() {
			public xbean.TaskInfos get(xbean.TaskInfos v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.TaskDlgInfo> selectTasksmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TaskInfos, java.util.Map<Integer, xbean.TaskDlgInfo>>() {
				public java.util.Map<Integer, xbean.TaskDlgInfo> get(xbean.TaskInfos v) { return v.getTasksmapAsData(); }
			});
	}

}
