package xtable;

// typed table access point
public class Rolesalegoodslimits {
	Rolesalegoodslimits() {
	}

	public static xbean.RoleSaleGoodsLimit get(Long key) {
		return _Tables_.getInstance().rolesalegoodslimits.get(key);
	}

	public static xbean.RoleSaleGoodsLimit get(Long key, xbean.RoleSaleGoodsLimit value) {
		return _Tables_.getInstance().rolesalegoodslimits.get(key, value);
	}

	public static void insert(Long key, xbean.RoleSaleGoodsLimit value) {
		_Tables_.getInstance().rolesalegoodslimits.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolesalegoodslimits.delete(key);
	}

	public static boolean add(Long key, xbean.RoleSaleGoodsLimit value) {
		return _Tables_.getInstance().rolesalegoodslimits.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolesalegoodslimits.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleSaleGoodsLimit> getCache() {
		return _Tables_.getInstance().rolesalegoodslimits.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleSaleGoodsLimit> getTable() {
		return _Tables_.getInstance().rolesalegoodslimits;
	}

	public static xbean.RoleSaleGoodsLimit select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleSaleGoodsLimit, xbean.RoleSaleGoodsLimit>() {
			public xbean.RoleSaleGoodsLimit get(xbean.RoleSaleGoodsLimit v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.NumberAndTime> selectDaylimit(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleSaleGoodsLimit, java.util.Map<Integer, xbean.NumberAndTime>>() {
				public java.util.Map<Integer, xbean.NumberAndTime> get(xbean.RoleSaleGoodsLimit v) { return v.getDaylimitAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.NumberAndTime> selectWeeklimit(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleSaleGoodsLimit, java.util.Map<Integer, xbean.NumberAndTime>>() {
				public java.util.Map<Integer, xbean.NumberAndTime> get(xbean.RoleSaleGoodsLimit v) { return v.getWeeklimitAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.NumberAndTime> selectMonthlimit(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleSaleGoodsLimit, java.util.Map<Integer, xbean.NumberAndTime>>() {
				public java.util.Map<Integer, xbean.NumberAndTime> get(xbean.RoleSaleGoodsLimit v) { return v.getMonthlimitAsData(); }
			});
	}

}
