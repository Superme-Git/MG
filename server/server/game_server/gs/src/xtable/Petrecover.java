package xtable;

// typed table access point
public class Petrecover {
	Petrecover() {
	}

	public static xbean.Petrecoverlist get(Long key) {
		return _Tables_.getInstance().petrecover.get(key);
	}

	public static xbean.Petrecoverlist get(Long key, xbean.Petrecoverlist value) {
		return _Tables_.getInstance().petrecover.get(key, value);
	}

	public static void insert(Long key, xbean.Petrecoverlist value) {
		_Tables_.getInstance().petrecover.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().petrecover.delete(key);
	}

	public static boolean add(Long key, xbean.Petrecoverlist value) {
		return _Tables_.getInstance().petrecover.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().petrecover.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.Petrecoverlist> getCache() {
		return _Tables_.getInstance().petrecover.getCache();
	}

	public static mkdb.TTable<Long, xbean.Petrecoverlist> getTable() {
		return _Tables_.getInstance().petrecover;
	}

	public static xbean.Petrecoverlist select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Petrecoverlist, xbean.Petrecoverlist>() {
			public xbean.Petrecoverlist get(xbean.Petrecoverlist v) { return v.toData(); }
		});
	}

	public static java.util.List<Long> selectUniqids(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Petrecoverlist, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.Petrecoverlist v) { return v.getUniqidsAsData(); }
			});
	}

}
