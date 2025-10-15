package xtable;

// typed table access point
public class Itemrecover {
	Itemrecover() {
	}

	public static xbean.Itemrecoverlist get(Long key) {
		return _Tables_.getInstance().itemrecover.get(key);
	}

	public static xbean.Itemrecoverlist get(Long key, xbean.Itemrecoverlist value) {
		return _Tables_.getInstance().itemrecover.get(key, value);
	}

	public static void insert(Long key, xbean.Itemrecoverlist value) {
		_Tables_.getInstance().itemrecover.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().itemrecover.delete(key);
	}

	public static boolean add(Long key, xbean.Itemrecoverlist value) {
		return _Tables_.getInstance().itemrecover.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().itemrecover.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.Itemrecoverlist> getCache() {
		return _Tables_.getInstance().itemrecover.getCache();
	}

	public static mkdb.TTable<Long, xbean.Itemrecoverlist> getTable() {
		return _Tables_.getInstance().itemrecover;
	}

	public static xbean.Itemrecoverlist select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Itemrecoverlist, xbean.Itemrecoverlist>() {
			public xbean.Itemrecoverlist get(xbean.Itemrecoverlist v) { return v.toData(); }
		});
	}

	public static java.util.List<Long> selectUniqids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Itemrecoverlist, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.Itemrecoverlist v) { return v.getUniqidsAsData(); }
			});
	}

}
