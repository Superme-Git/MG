package xtable;

// typed table access point
public class Compensationroles {
	Compensationroles() {
	}

	public static xbean.CompensationRole get(Long key) {
		return _Tables_.getInstance().compensationroles.get(key);
	}

	public static xbean.CompensationRole get(Long key, xbean.CompensationRole value) {
		return _Tables_.getInstance().compensationroles.get(key, value);
	}

	public static void insert(Long key, xbean.CompensationRole value) {
		_Tables_.getInstance().compensationroles.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().compensationroles.delete(key);
	}

	public static boolean add(Long key, xbean.CompensationRole value) {
		return _Tables_.getInstance().compensationroles.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().compensationroles.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.CompensationRole> getCache() {
		return _Tables_.getInstance().compensationroles.getCache();
	}

	public static mkdb.TTable<Long, xbean.CompensationRole> getTable() {
		return _Tables_.getInstance().compensationroles;
	}

	public static xbean.CompensationRole select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CompensationRole, xbean.CompensationRole>() {
			public xbean.CompensationRole get(xbean.CompensationRole v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, Long> selectSinglecompensations(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.CompensationRole, java.util.Map<Long, Long>>() {
				public java.util.Map<Long, Long> get(xbean.CompensationRole v) { return v.getSinglecompensationsAsData(); }
			});
	}

}
