package xtable;

// typed table access point
public class Marketfloatingtab {
	Marketfloatingtab() {
	}

	public static xbean.MarketFloatingGoods get(String key) {
		return _Tables_.getInstance().marketfloatingtab.get(key);
	}

	public static xbean.MarketFloatingGoods get(String key, xbean.MarketFloatingGoods value) {
		return _Tables_.getInstance().marketfloatingtab.get(key, value);
	}

	public static void insert(String key, xbean.MarketFloatingGoods value) {
		_Tables_.getInstance().marketfloatingtab.insert(key, value);
	}

	public static void delete(String key) {
		_Tables_.getInstance().marketfloatingtab.delete(key);
	}

	public static boolean add(String key, xbean.MarketFloatingGoods value) {
		return _Tables_.getInstance().marketfloatingtab.add(key, value);
	}

	public static boolean remove(String key) {
		return _Tables_.getInstance().marketfloatingtab.remove(key);
	}

	public static mkdb.TTableCache<String, xbean.MarketFloatingGoods> getCache() {
		return _Tables_.getInstance().marketfloatingtab.getCache();
	}

	public static mkdb.TTable<String, xbean.MarketFloatingGoods> getTable() {
		return _Tables_.getInstance().marketfloatingtab;
	}

	public static xbean.MarketFloatingGoods select(String key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketFloatingGoods, xbean.MarketFloatingGoods>() {
			public xbean.MarketFloatingGoods get(xbean.MarketFloatingGoods v) { return v.toData(); }
		});
	}

	public static Float selectFloatingmin(String key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketFloatingGoods, Float>() {
				public Float get(xbean.MarketFloatingGoods v) { return v.getFloatingmin(); }
			});
	}

	public static Float selectFloatingmax(String key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketFloatingGoods, Float>() {
				public Float get(xbean.MarketFloatingGoods v) { return v.getFloatingmax(); }
			});
	}

	public static Float selectFloatingprice(String key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketFloatingGoods, Float>() {
				public Float get(xbean.MarketFloatingGoods v) { return v.getFloatingprice(); }
			});
	}

	public static Integer selectBasehangordernum(String key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketFloatingGoods, Integer>() {
				public Integer get(xbean.MarketFloatingGoods v) { return v.getBasehangordernum(); }
			});
	}

	public static Integer selectBasesalenum(String key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketFloatingGoods, Integer>() {
				public Integer get(xbean.MarketFloatingGoods v) { return v.getBasesalenum(); }
			});
	}

	public static Integer selectHangordernum(String key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketFloatingGoods, Integer>() {
				public Integer get(xbean.MarketFloatingGoods v) { return v.getHangordernum(); }
			});
	}

	public static Integer selectSalenum(String key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketFloatingGoods, Integer>() {
				public Integer get(xbean.MarketFloatingGoods v) { return v.getSalenum(); }
			});
	}

	public static Integer selectPriorperiodprice(String key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketFloatingGoods, Integer>() {
				public Integer get(xbean.MarketFloatingGoods v) { return v.getPriorperiodprice(); }
			});
	}

	public static Integer selectPrice(String key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketFloatingGoods, Integer>() {
				public Integer get(xbean.MarketFloatingGoods v) { return v.getPrice(); }
			});
	}

	public static Integer selectTotalmoney(String key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketFloatingGoods, Integer>() {
				public Integer get(xbean.MarketFloatingGoods v) { return v.getTotalmoney(); }
			});
	}

}
