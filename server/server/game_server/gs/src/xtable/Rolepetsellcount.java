package xtable;

// typed table access point
public class Rolepetsellcount {
	Rolepetsellcount() {
	}

	public static xbean.PetSellCount get(Long key) {
		return _Tables_.getInstance().rolepetsellcount.get(key);
	}

	public static xbean.PetSellCount get(Long key, xbean.PetSellCount value) {
		return _Tables_.getInstance().rolepetsellcount.get(key, value);
	}

	public static void insert(Long key, xbean.PetSellCount value) {
		_Tables_.getInstance().rolepetsellcount.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolepetsellcount.delete(key);
	}

	public static boolean add(Long key, xbean.PetSellCount value) {
		return _Tables_.getInstance().rolepetsellcount.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolepetsellcount.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.PetSellCount> getCache() {
		return _Tables_.getInstance().rolepetsellcount.getCache();
	}

	public static mkdb.TTable<Long, xbean.PetSellCount> getTable() {
		return _Tables_.getInstance().rolepetsellcount;
	}

	public static xbean.PetSellCount select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetSellCount, xbean.PetSellCount>() {
			public xbean.PetSellCount get(xbean.PetSellCount v) { return v.toData(); }
		});
	}

	public static Integer selectCount(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetSellCount, Integer>() {
				public Integer get(xbean.PetSellCount v) { return v.getCount(); }
			});
	}

	public static Long selectResettime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetSellCount, Long>() {
				public Long get(xbean.PetSellCount v) { return v.getResettime(); }
			});
	}

}
