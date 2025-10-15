package xtable;

// typed table access point
public class Roleschargelimit {
	Roleschargelimit() {
	}

	public static xbean.RoleChargeLimit get(Long key) {
		return _Tables_.getInstance().roleschargelimit.get(key);
	}

	public static xbean.RoleChargeLimit get(Long key, xbean.RoleChargeLimit value) {
		return _Tables_.getInstance().roleschargelimit.get(key, value);
	}

	public static void insert(Long key, xbean.RoleChargeLimit value) {
		_Tables_.getInstance().roleschargelimit.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleschargelimit.delete(key);
	}

	public static boolean add(Long key, xbean.RoleChargeLimit value) {
		return _Tables_.getInstance().roleschargelimit.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleschargelimit.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleChargeLimit> getCache() {
		return _Tables_.getInstance().roleschargelimit.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleChargeLimit> getTable() {
		return _Tables_.getInstance().roleschargelimit;
	}

	public static xbean.RoleChargeLimit select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleChargeLimit, xbean.RoleChargeLimit>() {
			public xbean.RoleChargeLimit get(xbean.RoleChargeLimit v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.ChargeRecord> selectChargeslimit(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleChargeLimit, java.util.Map<Integer, xbean.ChargeRecord>>() {
				public java.util.Map<Integer, xbean.ChargeRecord> get(xbean.RoleChargeLimit v) { return v.getChargeslimitAsData(); }
			});
	}

}
