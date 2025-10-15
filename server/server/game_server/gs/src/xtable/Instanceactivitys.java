package xtable;

// typed table access point
public class Instanceactivitys {
	Instanceactivitys() {
	}

	public static xbean.InstanceActivity get(Integer key) {
		return _Tables_.getInstance().instanceactivitys.get(key);
	}

	public static xbean.InstanceActivity get(Integer key, xbean.InstanceActivity value) {
		return _Tables_.getInstance().instanceactivitys.get(key, value);
	}

	public static void insert(Integer key, xbean.InstanceActivity value) {
		_Tables_.getInstance().instanceactivitys.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().instanceactivitys.delete(key);
	}

	public static boolean add(Integer key, xbean.InstanceActivity value) {
		return _Tables_.getInstance().instanceactivitys.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().instanceactivitys.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.InstanceActivity> getCache() {
		return _Tables_.getInstance().instanceactivitys.getCache();
	}

	public static mkdb.TTable<Integer, xbean.InstanceActivity> getTable() {
		return _Tables_.getInstance().instanceactivitys;
	}

	public static xbean.InstanceActivity select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceActivity, xbean.InstanceActivity>() {
			public xbean.InstanceActivity get(xbean.InstanceActivity v) { return v.toData(); }
		});
	}

	public static Integer selectInstanceid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceActivity, Integer>() {
				public Integer get(xbean.InstanceActivity v) { return v.getInstanceid(); }
			});
	}

	public static Integer selectState(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceActivity, Integer>() {
				public Integer get(xbean.InstanceActivity v) { return v.getState(); }
			});
	}

	public static Integer selectBaseid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceActivity, Integer>() {
				public Integer get(xbean.InstanceActivity v) { return v.getBaseid(); }
			});
	}

	public static Long selectStarttime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceActivity, Long>() {
				public Long get(xbean.InstanceActivity v) { return v.getStarttime(); }
			});
	}

	public static Long selectEndtime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceActivity, Long>() {
				public Long get(xbean.InstanceActivity v) { return v.getEndtime(); }
			});
	}

}
