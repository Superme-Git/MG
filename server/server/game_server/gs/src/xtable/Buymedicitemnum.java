package xtable;

// typed table access point
public class Buymedicitemnum {
	Buymedicitemnum() {
	}

	public static xbean.BuyMedicItemNum get(Long key) {
		return _Tables_.getInstance().buymedicitemnum.get(key);
	}

	public static xbean.BuyMedicItemNum get(Long key, xbean.BuyMedicItemNum value) {
		return _Tables_.getInstance().buymedicitemnum.get(key, value);
	}

	public static void insert(Long key, xbean.BuyMedicItemNum value) {
		_Tables_.getInstance().buymedicitemnum.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().buymedicitemnum.delete(key);
	}

	public static boolean add(Long key, xbean.BuyMedicItemNum value) {
		return _Tables_.getInstance().buymedicitemnum.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().buymedicitemnum.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.BuyMedicItemNum> getCache() {
		return _Tables_.getInstance().buymedicitemnum.getCache();
	}

	public static mkdb.TTable<Long, xbean.BuyMedicItemNum> getTable() {
		return _Tables_.getInstance().buymedicitemnum;
	}

	public static xbean.BuyMedicItemNum select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BuyMedicItemNum, xbean.BuyMedicItemNum>() {
			public xbean.BuyMedicItemNum get(xbean.BuyMedicItemNum v) { return v.toData(); }
		});
	}

	public static Integer selectBuynum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BuyMedicItemNum, Integer>() {
				public Integer get(xbean.BuyMedicItemNum v) { return v.getBuynum(); }
			});
	}

	public static Integer selectImpeachdaynum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BuyMedicItemNum, Integer>() {
				public Integer get(xbean.BuyMedicItemNum v) { return v.getImpeachdaynum(); }
			});
	}

}
