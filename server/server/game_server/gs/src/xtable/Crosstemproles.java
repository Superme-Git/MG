package xtable;

// typed table access point
public class Crosstemproles {
	Crosstemproles() {
	}

	public static xbean.CrossTempRole get(Long key) {
		return _Tables_.getInstance().crosstemproles.get(key);
	}

	public static xbean.CrossTempRole get(Long key, xbean.CrossTempRole value) {
		return _Tables_.getInstance().crosstemproles.get(key, value);
	}

	public static void insert(Long key, xbean.CrossTempRole value) {
		_Tables_.getInstance().crosstemproles.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().crosstemproles.delete(key);
	}

	public static boolean add(Long key, xbean.CrossTempRole value) {
		return _Tables_.getInstance().crosstemproles.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().crosstemproles.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.CrossTempRole> getCache() {
		return _Tables_.getInstance().crosstemproles.getCache();
	}

	public static mkdb.TTable<Long, xbean.CrossTempRole> getTable() {
		return _Tables_.getInstance().crosstemproles;
	}

	public static xbean.CrossTempRole select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CrossTempRole, xbean.CrossTempRole>() {
			public xbean.CrossTempRole get(xbean.CrossTempRole v) { return v.toData(); }
		});
	}

	public static Integer selectZoneid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CrossTempRole, Integer>() {
				public Integer get(xbean.CrossTempRole v) { return v.getZoneid(); }
			});
	}

	public static Long selectLastcopytime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CrossTempRole, Long>() {
				public Long get(xbean.CrossTempRole v) { return v.getLastcopytime(); }
			});
	}

	public static Short selectCopyresult(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CrossTempRole, Short>() {
				public Short get(xbean.CrossTempRole v) { return v.getCopyresult(); }
			});
	}

	public static java.util.Map<String, Boolean> selectFinishedtable(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CrossTempRole, java.util.Map<String, Boolean>>() {
				public java.util.Map<String, Boolean> get(xbean.CrossTempRole v) { return v.getFinishedtableAsData(); }
			});
	}

	public static Boolean selectCrosstatus(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CrossTempRole, Boolean>() {
				public Boolean get(xbean.CrossTempRole v) { return v.getCrosstatus(); }
			});
	}

	public static Integer selectFlag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CrossTempRole, Integer>() {
				public Integer get(xbean.CrossTempRole v) { return v.getFlag(); }
			});
	}

}
