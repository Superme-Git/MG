package xtable;

// typed table access point
public class Teamfilters {
	Teamfilters() {
	}

	public static xbean.TeamFilter get(Long key) {
		return _Tables_.getInstance().teamfilters.get(key);
	}

	public static xbean.TeamFilter get(Long key, xbean.TeamFilter value) {
		return _Tables_.getInstance().teamfilters.get(key, value);
	}

	public static void insert(Long key, xbean.TeamFilter value) {
		_Tables_.getInstance().teamfilters.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().teamfilters.delete(key);
	}

	public static boolean add(Long key, xbean.TeamFilter value) {
		return _Tables_.getInstance().teamfilters.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().teamfilters.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.TeamFilter> getCache() {
		return _Tables_.getInstance().teamfilters.getCache();
	}

	public static mkdb.TTable<Long, xbean.TeamFilter> getTable() {
		return _Tables_.getInstance().teamfilters;
	}

}
