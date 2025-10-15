package xtable;

// typed table access point
public class Changeschool {
	Changeschool() {
	}

	public static xbean.ChangeSchoolInfo get(Long key) {
		return _Tables_.getInstance().changeschool.get(key);
	}

	public static xbean.ChangeSchoolInfo get(Long key, xbean.ChangeSchoolInfo value) {
		return _Tables_.getInstance().changeschool.get(key, value);
	}

	public static void insert(Long key, xbean.ChangeSchoolInfo value) {
		_Tables_.getInstance().changeschool.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().changeschool.delete(key);
	}

	public static boolean add(Long key, xbean.ChangeSchoolInfo value) {
		return _Tables_.getInstance().changeschool.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().changeschool.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ChangeSchoolInfo> getCache() {
		return _Tables_.getInstance().changeschool.getCache();
	}

	public static mkdb.TTable<Long, xbean.ChangeSchoolInfo> getTable() {
		return _Tables_.getInstance().changeschool;
	}

	public static xbean.ChangeSchoolInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChangeSchoolInfo, xbean.ChangeSchoolInfo>() {
			public xbean.ChangeSchoolInfo get(xbean.ChangeSchoolInfo v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.ChangeSchoolRecord> selectRecords(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChangeSchoolInfo, java.util.List<xbean.ChangeSchoolRecord>>() {
				public java.util.List<xbean.ChangeSchoolRecord> get(xbean.ChangeSchoolInfo v) { return v.getRecordsAsData(); }
			});
	}

	public static Integer selectChangeweaponcount(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChangeSchoolInfo, Integer>() {
				public Integer get(xbean.ChangeSchoolInfo v) { return v.getChangeweaponcount(); }
			});
	}

	public static Integer selectChangegemcount(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChangeSchoolInfo, Integer>() {
				public Integer get(xbean.ChangeSchoolInfo v) { return v.getChangegemcount(); }
			});
	}

}
