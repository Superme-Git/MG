package xtable;

// typed table access point
public class Opentraderwheeltable {
	Opentraderwheeltable() {
	}

	public static xbean.TraderWheel get(Long key) {
		return _Tables_.getInstance().opentraderwheeltable.get(key);
	}

	public static xbean.TraderWheel get(Long key, xbean.TraderWheel value) {
		return _Tables_.getInstance().opentraderwheeltable.get(key, value);
	}

	public static void insert(Long key, xbean.TraderWheel value) {
		_Tables_.getInstance().opentraderwheeltable.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().opentraderwheeltable.delete(key);
	}

	public static boolean add(Long key, xbean.TraderWheel value) {
		return _Tables_.getInstance().opentraderwheeltable.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().opentraderwheeltable.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.TraderWheel> getCache() {
		return _Tables_.getInstance().opentraderwheeltable.getCache();
	}

	public static mkdb.TTable<Long, xbean.TraderWheel> getTable() {
		return _Tables_.getInstance().opentraderwheeltable;
	}

	public static xbean.TraderWheel select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TraderWheel, xbean.TraderWheel>() {
			public xbean.TraderWheel get(xbean.TraderWheel v) { return v.toData(); }
		});
	}

	public static Integer selectBoxtype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TraderWheel, Integer>() {
				public Integer get(xbean.TraderWheel v) { return v.getBoxtype(); }
			});
	}

	public static Integer selectItemindex(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TraderWheel, Integer>() {
				public Integer get(xbean.TraderWheel v) { return v.getItemindex(); }
			});
	}

}
