package xtable;

// typed table access point
public class Instancefuture {
	Instancefuture() {
	}

	public static xbean.InstanceFutureInfo get(Long key) {
		return _Tables_.getInstance().instancefuture.get(key);
	}

	public static xbean.InstanceFutureInfo get(Long key, xbean.InstanceFutureInfo value) {
		return _Tables_.getInstance().instancefuture.get(key, value);
	}

	public static void insert(Long key, xbean.InstanceFutureInfo value) {
		_Tables_.getInstance().instancefuture.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().instancefuture.delete(key);
	}

	public static boolean add(Long key, xbean.InstanceFutureInfo value) {
		return _Tables_.getInstance().instancefuture.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().instancefuture.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.InstanceFutureInfo> getCache() {
		return _Tables_.getInstance().instancefuture.getCache();
	}

	public static mkdb.TTable<Long, xbean.InstanceFutureInfo> getTable() {
		return _Tables_.getInstance().instancefuture;
	}

}
