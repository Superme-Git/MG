package xtable;

// typed table access point
public class Anyelegendtimer {
	Anyelegendtimer() {
	}

	public static xbean.AnYeLegendFuture get(Long key) {
		return _Tables_.getInstance().anyelegendtimer.get(key);
	}

	public static xbean.AnYeLegendFuture get(Long key, xbean.AnYeLegendFuture value) {
		return _Tables_.getInstance().anyelegendtimer.get(key, value);
	}

	public static void insert(Long key, xbean.AnYeLegendFuture value) {
		_Tables_.getInstance().anyelegendtimer.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().anyelegendtimer.delete(key);
	}

	public static boolean add(Long key, xbean.AnYeLegendFuture value) {
		return _Tables_.getInstance().anyelegendtimer.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().anyelegendtimer.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.AnYeLegendFuture> getCache() {
		return _Tables_.getInstance().anyelegendtimer.getCache();
	}

	public static mkdb.TTable<Long, xbean.AnYeLegendFuture> getTable() {
		return _Tables_.getInstance().anyelegendtimer;
	}

}
