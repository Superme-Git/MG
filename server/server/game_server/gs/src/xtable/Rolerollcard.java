package xtable;

// typed table access point
public class Rolerollcard {
	Rolerollcard() {
	}

	public static xbean.RollCardInfo get(Long key) {
		return _Tables_.getInstance().rolerollcard.get(key);
	}

	public static xbean.RollCardInfo get(Long key, xbean.RollCardInfo value) {
		return _Tables_.getInstance().rolerollcard.get(key, value);
	}

	public static void insert(Long key, xbean.RollCardInfo value) {
		_Tables_.getInstance().rolerollcard.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolerollcard.delete(key);
	}

	public static boolean add(Long key, xbean.RollCardInfo value) {
		return _Tables_.getInstance().rolerollcard.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolerollcard.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RollCardInfo> getCache() {
		return _Tables_.getInstance().rolerollcard.getCache();
	}

	public static mkdb.TTable<Long, xbean.RollCardInfo> getTable() {
		return _Tables_.getInstance().rolerollcard;
	}

	public static xbean.RollCardInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RollCardInfo, xbean.RollCardInfo>() {
			public xbean.RollCardInfo get(xbean.RollCardInfo v) { return v.toData(); }
		});
	}

	public static Integer selectServiceid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RollCardInfo, Integer>() {
				public Integer get(xbean.RollCardInfo v) { return v.getServiceid(); }
			});
	}

	public static Integer selectTakeflag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RollCardInfo, Integer>() {
				public Integer get(xbean.RollCardInfo v) { return v.getTakeflag(); }
			});
	}

	public static Integer selectBasemoney(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RollCardInfo, Integer>() {
				public Integer get(xbean.RollCardInfo v) { return v.getBasemoney(); }
			});
	}

	public static Integer selectBasesmoney(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RollCardInfo, Integer>() {
				public Integer get(xbean.RollCardInfo v) { return v.getBasesmoney(); }
			});
	}

	public static Integer selectBaseexp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RollCardInfo, Integer>() {
				public Integer get(xbean.RollCardInfo v) { return v.getBaseexp(); }
			});
	}

	public static Integer selectIndex(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RollCardInfo, Integer>() {
				public Integer get(xbean.RollCardInfo v) { return v.getIndex(); }
			});
	}

	public static java.util.List<xbean.WheelItem> selectWheelitems(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RollCardInfo, java.util.List<xbean.WheelItem>>() {
				public java.util.List<xbean.WheelItem> get(xbean.RollCardInfo v) { return v.getWheelitemsAsData(); }
			});
	}

}
