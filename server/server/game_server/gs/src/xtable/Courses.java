package xtable;

// typed table access point
public class Courses {
	Courses() {
	}

	public static xbean.lichengxinxi get(Long key) {
		return _Tables_.getInstance().courses.get(key);
	}

	public static xbean.lichengxinxi get(Long key, xbean.lichengxinxi value) {
		return _Tables_.getInstance().courses.get(key, value);
	}

	public static void insert(Long key, xbean.lichengxinxi value) {
		_Tables_.getInstance().courses.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().courses.delete(key);
	}

	public static boolean add(Long key, xbean.lichengxinxi value) {
		return _Tables_.getInstance().courses.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().courses.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.lichengxinxi> getCache() {
		return _Tables_.getInstance().courses.getCache();
	}

	public static mkdb.TTable<Long, xbean.lichengxinxi> getTable() {
		return _Tables_.getInstance().courses;
	}

	public static xbean.lichengxinxi select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.lichengxinxi, xbean.lichengxinxi>() {
			public xbean.lichengxinxi get(xbean.lichengxinxi v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.Course> selectLicheng(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.lichengxinxi, java.util.Map<Integer, xbean.Course>>() {
				public java.util.Map<Integer, xbean.Course> get(xbean.lichengxinxi v) { return v.getLichengAsData(); }
			});
	}

}
