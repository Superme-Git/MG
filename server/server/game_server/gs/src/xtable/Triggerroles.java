package xtable;

// typed table access point
public class Triggerroles {
	Triggerroles() {
	}

	public static xbean.TriggerRole get(Long key) {
		return _Tables_.getInstance().triggerroles.get(key);
	}

	public static xbean.TriggerRole get(Long key, xbean.TriggerRole value) {
		return _Tables_.getInstance().triggerroles.get(key, value);
	}

	public static void insert(Long key, xbean.TriggerRole value) {
		_Tables_.getInstance().triggerroles.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().triggerroles.delete(key);
	}

	public static boolean add(Long key, xbean.TriggerRole value) {
		return _Tables_.getInstance().triggerroles.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().triggerroles.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.TriggerRole> getCache() {
		return _Tables_.getInstance().triggerroles.getCache();
	}

	public static mkdb.TTable<Long, xbean.TriggerRole> getTable() {
		return _Tables_.getInstance().triggerroles;
	}

	public static xbean.TriggerRole select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TriggerRole, xbean.TriggerRole>() {
			public xbean.TriggerRole get(xbean.TriggerRole v) { return v.toData(); }
		});
	}

	public static java.util.List<Integer> selectTriggeredids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TriggerRole, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.TriggerRole v) { return v.getTriggeredidsAsData(); }
			});
	}

}
