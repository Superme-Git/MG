package xtable;

// typed table access point
public class Roleyaoqianshutables {
	Roleyaoqianshutables() {
	}

	public static xbean.RoleYaoQianShuInfoMaps get(Long key) {
		return _Tables_.getInstance().roleyaoqianshutables.get(key);
	}

	public static xbean.RoleYaoQianShuInfoMaps get(Long key, xbean.RoleYaoQianShuInfoMaps value) {
		return _Tables_.getInstance().roleyaoqianshutables.get(key, value);
	}

	public static void insert(Long key, xbean.RoleYaoQianShuInfoMaps value) {
		_Tables_.getInstance().roleyaoqianshutables.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleyaoqianshutables.delete(key);
	}

	public static boolean add(Long key, xbean.RoleYaoQianShuInfoMaps value) {
		return _Tables_.getInstance().roleyaoqianshutables.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleyaoqianshutables.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleYaoQianShuInfoMaps> getCache() {
		return _Tables_.getInstance().roleyaoqianshutables.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleYaoQianShuInfoMaps> getTable() {
		return _Tables_.getInstance().roleyaoqianshutables;
	}

	public static xbean.RoleYaoQianShuInfoMaps select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleYaoQianShuInfoMaps, xbean.RoleYaoQianShuInfoMaps>() {
			public xbean.RoleYaoQianShuInfoMaps get(xbean.RoleYaoQianShuInfoMaps v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, xbean.RoleYaoQianShuInfo> selectYaoqianshumaps(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleYaoQianShuInfoMaps, java.util.Map<Long, xbean.RoleYaoQianShuInfo>>() {
				public java.util.Map<Long, xbean.RoleYaoQianShuInfo> get(xbean.RoleYaoQianShuInfoMaps v) { return v.getYaoqianshumapsAsData(); }
			});
	}

}
