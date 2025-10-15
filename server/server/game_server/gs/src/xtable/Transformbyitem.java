package xtable;

// typed table access point
public class Transformbyitem {
	Transformbyitem() {
	}

	public static xbean.TransfromByItemData get(Long key) {
		return _Tables_.getInstance().transformbyitem.get(key);
	}

	public static xbean.TransfromByItemData get(Long key, xbean.TransfromByItemData value) {
		return _Tables_.getInstance().transformbyitem.get(key, value);
	}

	public static void insert(Long key, xbean.TransfromByItemData value) {
		_Tables_.getInstance().transformbyitem.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().transformbyitem.delete(key);
	}

	public static boolean add(Long key, xbean.TransfromByItemData value) {
		return _Tables_.getInstance().transformbyitem.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().transformbyitem.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.TransfromByItemData> getCache() {
		return _Tables_.getInstance().transformbyitem.getCache();
	}

	public static mkdb.TTable<Long, xbean.TransfromByItemData> getTable() {
		return _Tables_.getInstance().transformbyitem;
	}

	public static xbean.TransfromByItemData select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TransfromByItemData, xbean.TransfromByItemData>() {
			public xbean.TransfromByItemData get(xbean.TransfromByItemData v) { return v.toData(); }
		});
	}

	public static Integer selectUseitemid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TransfromByItemData, Integer>() {
				public Integer get(xbean.TransfromByItemData v) { return v.getUseitemid(); }
			});
	}

	public static Integer selectTransformid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TransfromByItemData, Integer>() {
				public Integer get(xbean.TransfromByItemData v) { return v.getTransformid(); }
			});
	}

	public static Long selectValiddate(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TransfromByItemData, Long>() {
				public Long get(xbean.TransfromByItemData v) { return v.getValiddate(); }
			});
	}

}
