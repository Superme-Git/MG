package xtable;

// typed table access point
public class Roletaskeventtab {
	Roletaskeventtab() {
	}

	public static xbean.TaskEventInfo get(Long key) {
		return _Tables_.getInstance().roletaskeventtab.get(key);
	}

	public static xbean.TaskEventInfo get(Long key, xbean.TaskEventInfo value) {
		return _Tables_.getInstance().roletaskeventtab.get(key, value);
	}

	public static void insert(Long key, xbean.TaskEventInfo value) {
		_Tables_.getInstance().roletaskeventtab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roletaskeventtab.delete(key);
	}

	public static boolean add(Long key, xbean.TaskEventInfo value) {
		return _Tables_.getInstance().roletaskeventtab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roletaskeventtab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.TaskEventInfo> getCache() {
		return _Tables_.getInstance().roletaskeventtab.getCache();
	}

	public static mkdb.TTable<Long, xbean.TaskEventInfo> getTable() {
		return _Tables_.getInstance().roletaskeventtab;
	}

	public static xbean.TaskEventInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TaskEventInfo, xbean.TaskEventInfo>() {
			public xbean.TaskEventInfo get(xbean.TaskEventInfo v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Long> selectEventindexs(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TaskEventInfo, java.util.Map<Integer, Long>>() {
				public java.util.Map<Integer, Long> get(xbean.TaskEventInfo v) { return v.getEventindexsAsData(); }
			});
	}

}
