package xtable;

// typed table access point
public class Onlinerefresh {
	Onlinerefresh() {
	}

	public static xbean.OnlinerefreshFuture get(Long key) {
		return _Tables_.getInstance().onlinerefresh.get(key);
	}

	public static xbean.OnlinerefreshFuture get(Long key, xbean.OnlinerefreshFuture value) {
		return _Tables_.getInstance().onlinerefresh.get(key, value);
	}

	public static void insert(Long key, xbean.OnlinerefreshFuture value) {
		_Tables_.getInstance().onlinerefresh.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().onlinerefresh.delete(key);
	}

	public static boolean add(Long key, xbean.OnlinerefreshFuture value) {
		return _Tables_.getInstance().onlinerefresh.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().onlinerefresh.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.OnlinerefreshFuture> getCache() {
		return _Tables_.getInstance().onlinerefresh.getCache();
	}

	public static mkdb.TTable<Long, xbean.OnlinerefreshFuture> getTable() {
		return _Tables_.getInstance().onlinerefresh;
	}

}
