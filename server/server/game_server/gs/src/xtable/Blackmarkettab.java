package xtable;

// typed table access point
public class Blackmarkettab {
	Blackmarkettab() {
	}

	public static xbean.RoleBlackMarket get(Long key) {
		return _Tables_.getInstance().blackmarkettab.get(key);
	}

	public static xbean.RoleBlackMarket get(Long key, xbean.RoleBlackMarket value) {
		return _Tables_.getInstance().blackmarkettab.get(key, value);
	}

	public static void insert(Long key, xbean.RoleBlackMarket value) {
		_Tables_.getInstance().blackmarkettab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().blackmarkettab.delete(key);
	}

	public static boolean add(Long key, xbean.RoleBlackMarket value) {
		return _Tables_.getInstance().blackmarkettab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().blackmarkettab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleBlackMarket> getCache() {
		return _Tables_.getInstance().blackmarkettab.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleBlackMarket> getTable() {
		return _Tables_.getInstance().blackmarkettab;
	}

	public static xbean.RoleBlackMarket select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleBlackMarket, xbean.RoleBlackMarket>() {
			public xbean.RoleBlackMarket get(xbean.RoleBlackMarket v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, xbean.GoldOrder> selectGoldordersale(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleBlackMarket, java.util.Map<Long, xbean.GoldOrder>>() {
				public java.util.Map<Long, xbean.GoldOrder> get(xbean.RoleBlackMarket v) { return v.getGoldordersaleAsData(); }
			});
	}

	public static java.util.Map<Long, xbean.GoldOrder> selectGoldorderbuy(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleBlackMarket, java.util.Map<Long, xbean.GoldOrder>>() {
				public java.util.Map<Long, xbean.GoldOrder> get(xbean.RoleBlackMarket v) { return v.getGoldorderbuyAsData(); }
			});
	}

}
