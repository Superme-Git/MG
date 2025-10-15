package xtable;

// typed table access point
public class Openschoolwheeltable {
	Openschoolwheeltable() {
	}

	public static xbean.SchoolWheel get(Long key) {
		return _Tables_.getInstance().openschoolwheeltable.get(key);
	}

	public static xbean.SchoolWheel get(Long key, xbean.SchoolWheel value) {
		return _Tables_.getInstance().openschoolwheeltable.get(key, value);
	}

	public static void insert(Long key, xbean.SchoolWheel value) {
		_Tables_.getInstance().openschoolwheeltable.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().openschoolwheeltable.delete(key);
	}

	public static boolean add(Long key, xbean.SchoolWheel value) {
		return _Tables_.getInstance().openschoolwheeltable.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().openschoolwheeltable.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.SchoolWheel> getCache() {
		return _Tables_.getInstance().openschoolwheeltable.getCache();
	}

	public static mkdb.TTable<Long, xbean.SchoolWheel> getTable() {
		return _Tables_.getInstance().openschoolwheeltable;
	}

	public static xbean.SchoolWheel select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SchoolWheel, xbean.SchoolWheel>() {
			public xbean.SchoolWheel get(xbean.SchoolWheel v) { return v.toData(); }
		});
	}

	public static Integer selectAwardid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SchoolWheel, Integer>() {
				public Integer get(xbean.SchoolWheel v) { return v.getAwardid(); }
			});
	}

	public static Integer selectItemindex(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SchoolWheel, Integer>() {
				public Integer get(xbean.SchoolWheel v) { return v.getItemindex(); }
			});
	}

}
