package xtable;

// typed table access point
public class Rolebuygoodslimits {
	Rolebuygoodslimits() {
	}

	public static xbean.RoleBuyGoodsLimit get(Long key) {
		return _Tables_.getInstance().rolebuygoodslimits.get(key);
	}

	public static xbean.RoleBuyGoodsLimit get(Long key, xbean.RoleBuyGoodsLimit value) {
		return _Tables_.getInstance().rolebuygoodslimits.get(key, value);
	}

	public static void insert(Long key, xbean.RoleBuyGoodsLimit value) {
		_Tables_.getInstance().rolebuygoodslimits.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolebuygoodslimits.delete(key);
	}

	public static boolean add(Long key, xbean.RoleBuyGoodsLimit value) {
		return _Tables_.getInstance().rolebuygoodslimits.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolebuygoodslimits.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RoleBuyGoodsLimit> getCache() {
		return _Tables_.getInstance().rolebuygoodslimits.getCache();
	}

	public static mkdb.TTable<Long, xbean.RoleBuyGoodsLimit> getTable() {
		return _Tables_.getInstance().rolebuygoodslimits;
	}

	public static xbean.RoleBuyGoodsLimit select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleBuyGoodsLimit, xbean.RoleBuyGoodsLimit>() {
			public xbean.RoleBuyGoodsLimit get(xbean.RoleBuyGoodsLimit v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.NumberAndTime> selectDaylimit(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleBuyGoodsLimit, java.util.Map<Integer, xbean.NumberAndTime>>() {
				public java.util.Map<Integer, xbean.NumberAndTime> get(xbean.RoleBuyGoodsLimit v) { return v.getDaylimitAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.NumberAndTime> selectWeeklimit(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleBuyGoodsLimit, java.util.Map<Integer, xbean.NumberAndTime>>() {
				public java.util.Map<Integer, xbean.NumberAndTime> get(xbean.RoleBuyGoodsLimit v) { return v.getWeeklimitAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.NumberAndTime> selectMonthlimit(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RoleBuyGoodsLimit, java.util.Map<Integer, xbean.NumberAndTime>>() {
				public java.util.Map<Integer, xbean.NumberAndTime> get(xbean.RoleBuyGoodsLimit v) { return v.getMonthlimitAsData(); }
			});
	}

}
