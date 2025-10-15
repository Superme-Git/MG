package xtable;

// typed table access point
public class Runeinfotab {
	Runeinfotab() {
	}

	public static xbean.RuneInfo get(Long key) {
		return _Tables_.getInstance().runeinfotab.get(key);
	}

	public static xbean.RuneInfo get(Long key, xbean.RuneInfo value) {
		return _Tables_.getInstance().runeinfotab.get(key, value);
	}

	public static void insert(Long key, xbean.RuneInfo value) {
		_Tables_.getInstance().runeinfotab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().runeinfotab.delete(key);
	}

	public static boolean add(Long key, xbean.RuneInfo value) {
		return _Tables_.getInstance().runeinfotab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().runeinfotab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RuneInfo> getCache() {
		return _Tables_.getInstance().runeinfotab.getCache();
	}

	public static mkdb.TTable<Long, xbean.RuneInfo> getTable() {
		return _Tables_.getInstance().runeinfotab;
	}

	public static xbean.RuneInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RuneInfo, xbean.RuneInfo>() {
			public xbean.RuneInfo get(xbean.RuneInfo v) { return v.toData(); }
		});
	}

	public static Integer selectDayrequestnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RuneInfo, Integer>() {
				public Integer get(xbean.RuneInfo v) { return v.getDayrequestnum(); }
			});
	}

	public static Integer selectAllgivenum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RuneInfo, Integer>() {
				public Integer get(xbean.RuneInfo v) { return v.getAllgivenum(); }
			});
	}

	public static Integer selectAllacceptnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RuneInfo, Integer>() {
				public Integer get(xbean.RuneInfo v) { return v.getAllacceptnum(); }
			});
	}

	public static Integer selectItemlevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RuneInfo, Integer>() {
				public Integer get(xbean.RuneInfo v) { return v.getItemlevel(); }
			});
	}

}
