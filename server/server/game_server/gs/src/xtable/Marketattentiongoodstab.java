package xtable;

// typed table access point
public class Marketattentiongoodstab {
	Marketattentiongoodstab() {
	}

	public static xbean.AttentionGoods get(Long key) {
		return _Tables_.getInstance().marketattentiongoodstab.get(key);
	}

	public static xbean.AttentionGoods get(Long key, xbean.AttentionGoods value) {
		return _Tables_.getInstance().marketattentiongoodstab.get(key, value);
	}

	public static void insert(Long key, xbean.AttentionGoods value) {
		_Tables_.getInstance().marketattentiongoodstab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().marketattentiongoodstab.delete(key);
	}

	public static boolean add(Long key, xbean.AttentionGoods value) {
		return _Tables_.getInstance().marketattentiongoodstab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().marketattentiongoodstab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.AttentionGoods> getCache() {
		return _Tables_.getInstance().marketattentiongoodstab.getCache();
	}

	public static mkdb.TTable<Long, xbean.AttentionGoods> getTable() {
		return _Tables_.getInstance().marketattentiongoodstab;
	}

	public static xbean.AttentionGoods select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AttentionGoods, xbean.AttentionGoods>() {
			public xbean.AttentionGoods get(xbean.AttentionGoods v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.AttentionGoodsBean> selectBuyattentions(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AttentionGoods, java.util.List<xbean.AttentionGoodsBean>>() {
				public java.util.List<xbean.AttentionGoodsBean> get(xbean.AttentionGoods v) { return v.getBuyattentionsAsData(); }
			});
	}

	public static java.util.List<xbean.AttentionGoodsBean> selectPublicityattentions(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.AttentionGoods, java.util.List<xbean.AttentionGoodsBean>>() {
				public java.util.List<xbean.AttentionGoodsBean> get(xbean.AttentionGoods v) { return v.getPublicityattentionsAsData(); }
			});
	}

}
