package xtable;

// typed table access point
public class Fushirecordtable {
	Fushirecordtable() {
	}

	public static xbean.RoleFuShiRecord get(Long key) {
		return _Tables_.getInstance().fushirecordtable.get(key);
	}

	public static xbean.RoleFuShiRecord get(Long key, xbean.RoleFuShiRecord value) {
		return _Tables_.getInstance().fushirecordtable.get(key, value);
	}

	public static void insert(Long key, xbean.RoleFuShiRecord value) {
		_Tables_.getInstance().fushirecordtable.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().fushirecordtable.delete(key);
	}

	public static boolean add(Long key, xbean.RoleFuShiRecord value) {
		return _Tables_.getInstance().fushirecordtable.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().fushirecordtable.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleFuShiRecord> getCache() {
		return _Tables_.getInstance().fushirecordtable.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleFuShiRecord> getTable() {
		return _Tables_.getInstance().fushirecordtable;
	}

	public static xbean.RoleFuShiRecord select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleFuShiRecord, xbean.RoleFuShiRecord>() {
			public xbean.RoleFuShiRecord get(xbean.RoleFuShiRecord v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.FuShiRecord> selectRecords(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleFuShiRecord, java.util.List<xbean.FuShiRecord>>() {
				public java.util.List<xbean.FuShiRecord> get(xbean.RoleFuShiRecord v) { return v.getRecordsAsData(); }
			});
	}

}
