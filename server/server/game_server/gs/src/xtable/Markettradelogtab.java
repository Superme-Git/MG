package xtable;

// typed table access point
public class Markettradelogtab {
	Markettradelogtab() {
	}

	public static xbean.MarketTradeLog get(Long key) {
		return _Tables_.getInstance().markettradelogtab.get(key);
	}

	public static xbean.MarketTradeLog get(Long key, xbean.MarketTradeLog value) {
		return _Tables_.getInstance().markettradelogtab.get(key, value);
	}

	public static void insert(Long key, xbean.MarketTradeLog value) {
		_Tables_.getInstance().markettradelogtab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().markettradelogtab.delete(key);
	}

	public static boolean add(Long key, xbean.MarketTradeLog value) {
		return _Tables_.getInstance().markettradelogtab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().markettradelogtab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.MarketTradeLog> getCache() {
		return _Tables_.getInstance().markettradelogtab.getCache();
	}

	public static mkdb.TTable<Long, xbean.MarketTradeLog> getTable() {
		return _Tables_.getInstance().markettradelogtab;
	}

	public static xbean.MarketTradeLog select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketTradeLog, xbean.MarketTradeLog>() {
			public xbean.MarketTradeLog get(xbean.MarketTradeLog v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.LogBean> selectBuylog(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketTradeLog, java.util.List<xbean.LogBean>>() {
				public java.util.List<xbean.LogBean> get(xbean.MarketTradeLog v) { return v.getBuylogAsData(); }
			});
	}

	public static java.util.List<xbean.LogBean> selectSalelog(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MarketTradeLog, java.util.List<xbean.LogBean>>() {
				public java.util.List<xbean.LogBean> get(xbean.MarketTradeLog v) { return v.getSalelogAsData(); }
			});
	}

}
