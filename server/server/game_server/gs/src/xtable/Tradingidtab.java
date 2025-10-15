package xtable;

// typed table access point
public class Tradingidtab {
	Tradingidtab() {
	}

	public static xbean.TradingMap get(Integer key) {
		return _Tables_.getInstance().tradingidtab.get(key);
	}

	public static xbean.TradingMap get(Integer key, xbean.TradingMap value) {
		return _Tables_.getInstance().tradingidtab.get(key, value);
	}

	public static void insert(Integer key, xbean.TradingMap value) {
		_Tables_.getInstance().tradingidtab.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().tradingidtab.delete(key);
	}

	public static boolean add(Integer key, xbean.TradingMap value) {
		return _Tables_.getInstance().tradingidtab.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().tradingidtab.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.TradingMap> getCache() {
		return _Tables_.getInstance().tradingidtab.getCache();
	}

	public static mkdb.TTable<Integer, xbean.TradingMap> getTable() {
		return _Tables_.getInstance().tradingidtab;
	}

	public static xbean.TradingMap select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.TradingMap, xbean.TradingMap>() {
			public xbean.TradingMap get(xbean.TradingMap v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.TradingPrice> selectSelllist(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.TradingMap, java.util.List<xbean.TradingPrice>>() {
				public java.util.List<xbean.TradingPrice> get(xbean.TradingMap v) { return v.getSelllistAsData(); }
			});
	}

	public static java.util.List<xbean.TradingPrice> selectBuylist(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.TradingMap, java.util.List<xbean.TradingPrice>>() {
				public java.util.List<xbean.TradingPrice> get(xbean.TradingMap v) { return v.getBuylistAsData(); }
			});
	}

}
