package xtable;

// typed table access point
public class Timerfutures {
	Timerfutures() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().timerfutures.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.TimerFuture value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.TimerFuture get(Long key) {
		return _Tables_.getInstance().timerfutures.get(key);
	}

	public static xbean.TimerFuture get(Long key, xbean.TimerFuture value) {
		return _Tables_.getInstance().timerfutures.get(key, value);
	}

	public static void insert(Long key, xbean.TimerFuture value) {
		_Tables_.getInstance().timerfutures.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().timerfutures.delete(key);
	}

	public static boolean add(Long key, xbean.TimerFuture value) {
		return _Tables_.getInstance().timerfutures.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().timerfutures.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.TimerFuture> getCache() {
		return _Tables_.getInstance().timerfutures.getCache();
	}

	public static mkdb.TTable<Long, xbean.TimerFuture> getTable() {
		return _Tables_.getInstance().timerfutures;
	}

}
