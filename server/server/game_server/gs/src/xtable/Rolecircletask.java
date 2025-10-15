package xtable;

// typed table access point
public class Rolecircletask {
	Rolecircletask() {
	}

	public static xbean.CircleTaskMap get(Long key) {
		return _Tables_.getInstance().rolecircletask.get(key);
	}

	public static xbean.CircleTaskMap get(Long key, xbean.CircleTaskMap value) {
		return _Tables_.getInstance().rolecircletask.get(key, value);
	}

	public static void insert(Long key, xbean.CircleTaskMap value) {
		_Tables_.getInstance().rolecircletask.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolecircletask.delete(key);
	}

	public static boolean add(Long key, xbean.CircleTaskMap value) {
		return _Tables_.getInstance().rolecircletask.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolecircletask.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.CircleTaskMap> getCache() {
		return _Tables_.getInstance().rolecircletask.getCache();
	}

	public static mkdb.TTable<Long, xbean.CircleTaskMap> getTable() {
		return _Tables_.getInstance().rolecircletask;
	}

	public static xbean.CircleTaskMap select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CircleTaskMap, xbean.CircleTaskMap>() {
			public xbean.CircleTaskMap get(xbean.CircleTaskMap v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.CircleTaskInfo> selectTaskmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CircleTaskMap, java.util.Map<Integer, xbean.CircleTaskInfo>>() {
				public java.util.Map<Integer, xbean.CircleTaskInfo> get(xbean.CircleTaskMap v) { return v.getTaskmapAsData(); }
			});
	}

}
