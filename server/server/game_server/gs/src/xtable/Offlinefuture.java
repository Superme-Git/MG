package xtable;

// typed table access point
public class Offlinefuture {
	Offlinefuture() {
	}

	public static xbean.OfflineFuture get(Long key) {
		return _Tables_.getInstance().offlinefuture.get(key);
	}

	public static xbean.OfflineFuture get(Long key, xbean.OfflineFuture value) {
		return _Tables_.getInstance().offlinefuture.get(key, value);
	}

	public static void insert(Long key, xbean.OfflineFuture value) {
		_Tables_.getInstance().offlinefuture.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().offlinefuture.delete(key);
	}

	public static boolean add(Long key, xbean.OfflineFuture value) {
		return _Tables_.getInstance().offlinefuture.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().offlinefuture.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.OfflineFuture> getCache() {
		return _Tables_.getInstance().offlinefuture.getCache();
	}

	public static mkdb.TTable<Long, xbean.OfflineFuture> getTable() {
		return _Tables_.getInstance().offlinefuture;
	}

}
