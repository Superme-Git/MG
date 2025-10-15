package xtable;

// typed table access point
public class Roleuseitemcount {
	Roleuseitemcount() {
	}

	public static xbean.ItemUse get(Long key) {
		return _Tables_.getInstance().roleuseitemcount.get(key);
	}

	public static xbean.ItemUse get(Long key, xbean.ItemUse value) {
		return _Tables_.getInstance().roleuseitemcount.get(key, value);
	}

	public static void insert(Long key, xbean.ItemUse value) {
		_Tables_.getInstance().roleuseitemcount.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleuseitemcount.delete(key);
	}

	public static boolean add(Long key, xbean.ItemUse value) {
		return _Tables_.getInstance().roleuseitemcount.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleuseitemcount.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ItemUse> getCache() {
		return _Tables_.getInstance().roleuseitemcount.getCache();
	}

	public static mkdb.TTable<Long, xbean.ItemUse> getTable() {
		return _Tables_.getInstance().roleuseitemcount;
	}

	public static xbean.ItemUse select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ItemUse, xbean.ItemUse>() {
			public xbean.ItemUse get(xbean.ItemUse v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.ItemUseCount> selectIteminfo(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ItemUse, java.util.Map<Integer, xbean.ItemUseCount>>() {
				public java.util.Map<Integer, xbean.ItemUseCount> get(xbean.ItemUse v) { return v.getIteminfoAsData(); }
			});
	}

}
