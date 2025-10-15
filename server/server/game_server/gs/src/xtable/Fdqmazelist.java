package xtable;

// typed table access point
public class Fdqmazelist {
	Fdqmazelist() {
	}

	public static xbean.VecList get(Long key) {
		return _Tables_.getInstance().fdqmazelist.get(key);
	}

	public static xbean.VecList get(Long key, xbean.VecList value) {
		return _Tables_.getInstance().fdqmazelist.get(key, value);
	}

	public static void insert(Long key, xbean.VecList value) {
		_Tables_.getInstance().fdqmazelist.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().fdqmazelist.delete(key);
	}

	public static boolean add(Long key, xbean.VecList value) {
		return _Tables_.getInstance().fdqmazelist.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().fdqmazelist.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.VecList> getCache() {
		return _Tables_.getInstance().fdqmazelist.getCache();
	}

	public static mkdb.TTable<Long, xbean.VecList> getTable() {
		return _Tables_.getInstance().fdqmazelist;
	}

	public static xbean.VecList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.VecList, xbean.VecList>() {
			public xbean.VecList get(xbean.VecList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.MazeInfo> selectList(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.VecList, java.util.List<xbean.MazeInfo>>() {
				public java.util.List<xbean.MazeInfo> get(xbean.VecList v) { return v.getListAsData(); }
			});
	}

}
