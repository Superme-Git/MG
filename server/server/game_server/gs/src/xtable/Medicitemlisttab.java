package xtable;

// typed table access point
public class Medicitemlisttab {
	Medicitemlisttab() {
	}

	public static xbean.MedicItemList get(Long key) {
		return _Tables_.getInstance().medicitemlisttab.get(key);
	}

	public static xbean.MedicItemList get(Long key, xbean.MedicItemList value) {
		return _Tables_.getInstance().medicitemlisttab.get(key, value);
	}

	public static void insert(Long key, xbean.MedicItemList value) {
		_Tables_.getInstance().medicitemlisttab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().medicitemlisttab.delete(key);
	}

	public static boolean add(Long key, xbean.MedicItemList value) {
		return _Tables_.getInstance().medicitemlisttab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().medicitemlisttab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.MedicItemList> getCache() {
		return _Tables_.getInstance().medicitemlisttab.getCache();
	}

	public static mkdb.TTable<Long, xbean.MedicItemList> getTable() {
		return _Tables_.getInstance().medicitemlisttab;
	}

	public static xbean.MedicItemList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MedicItemList, xbean.MedicItemList>() {
			public xbean.MedicItemList get(xbean.MedicItemList v) { return v.toData(); }
		});
	}

	public static Long selectMedicitemrefreshtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MedicItemList, Long>() {
				public Long get(xbean.MedicItemList v) { return v.getMedicitemrefreshtime(); }
			});
	}

	public static Integer selectSelecttype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MedicItemList, Integer>() {
				public Integer get(xbean.MedicItemList v) { return v.getSelecttype(); }
			});
	}

	public static java.util.Map<Integer, xbean.MedicItem> selectMedicitems(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MedicItemList, java.util.Map<Integer, xbean.MedicItem>>() {
				public java.util.Map<Integer, xbean.MedicItem> get(xbean.MedicItemList v) { return v.getMedicitemsAsData(); }
			});
	}

}
