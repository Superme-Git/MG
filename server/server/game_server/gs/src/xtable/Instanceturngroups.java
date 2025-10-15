package xtable;

// typed table access point
public class Instanceturngroups {
	Instanceturngroups() {
	}

	public static xbean.InstanceTurnGroup get(Integer key) {
		return _Tables_.getInstance().instanceturngroups.get(key);
	}

	public static xbean.InstanceTurnGroup get(Integer key, xbean.InstanceTurnGroup value) {
		return _Tables_.getInstance().instanceturngroups.get(key, value);
	}

	public static void insert(Integer key, xbean.InstanceTurnGroup value) {
		_Tables_.getInstance().instanceturngroups.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().instanceturngroups.delete(key);
	}

	public static boolean add(Integer key, xbean.InstanceTurnGroup value) {
		return _Tables_.getInstance().instanceturngroups.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().instanceturngroups.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.InstanceTurnGroup> getCache() {
		return _Tables_.getInstance().instanceturngroups.getCache();
	}

	public static mkdb.TTable<Integer, xbean.InstanceTurnGroup> getTable() {
		return _Tables_.getInstance().instanceturngroups;
	}

	public static xbean.InstanceTurnGroup select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceTurnGroup, xbean.InstanceTurnGroup>() {
			public xbean.InstanceTurnGroup get(xbean.InstanceTurnGroup v) { return v.toData(); }
		});
	}

	public static Integer selectTurngroup(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceTurnGroup, Integer>() {
				public Integer get(xbean.InstanceTurnGroup v) { return v.getTurngroup(); }
			});
	}

	public static Integer selectTurnindex(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceTurnGroup, Integer>() {
				public Integer get(xbean.InstanceTurnGroup v) { return v.getTurnindex(); }
			});
	}

	public static Integer selectTurntype(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.InstanceTurnGroup, Integer>() {
				public Integer get(xbean.InstanceTurnGroup v) { return v.getTurntype(); }
			});
	}

}
