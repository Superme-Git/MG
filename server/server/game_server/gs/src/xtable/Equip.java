package xtable;

// typed table access point
public class Equip {
	Equip() {
	}

	public static xbean.Bag get(Long key) {
		return _Tables_.getInstance().equip.get(key);
	}

	public static xbean.Bag get(Long key, xbean.Bag value) {
		return _Tables_.getInstance().equip.get(key, value);
	}

	public static void insert(Long key, xbean.Bag value) {
		_Tables_.getInstance().equip.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().equip.delete(key);
	}

	public static boolean add(Long key, xbean.Bag value) {
		return _Tables_.getInstance().equip.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().equip.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.Bag> getCache() {
		return _Tables_.getInstance().equip.getCache();
	}

	public static mkdb.TTable<Long, xbean.Bag> getTable() {
		return _Tables_.getInstance().equip;
	}

	public static xbean.Bag select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Bag, xbean.Bag>() {
			public xbean.Bag get(xbean.Bag v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Long> selectCurrency(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Bag, java.util.Map<Integer, Long>>() {
				public java.util.Map<Integer, Long> get(xbean.Bag v) { return v.getCurrencyAsData(); }
			});
	}

	public static Integer selectCapacity(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Bag, Integer>() {
				public Integer get(xbean.Bag v) { return v.getCapacity(); }
			});
	}

	public static Integer selectNextid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Bag, Integer>() {
				public Integer get(xbean.Bag v) { return v.getNextid(); }
			});
	}

	public static java.util.Map<Integer, xbean.Item> selectItems(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Bag, java.util.Map<Integer, xbean.Item>>() {
				public java.util.Map<Integer, xbean.Item> get(xbean.Bag v) { return v.getItemsAsData(); }
			});
	}

	public static Integer selectLocked(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Bag, Integer>() {
				public Integer get(xbean.Bag v) { return v.getLocked(); }
			});
	}

}
