package xtable;

// typed table access point
public class Notify {
	Notify() {
	}

	public static xbean.RolesNotifyMap get(Integer key) {
		return _Tables_.getInstance().notify.get(key);
	}

	public static xbean.RolesNotifyMap get(Integer key, xbean.RolesNotifyMap value) {
		return _Tables_.getInstance().notify.get(key, value);
	}

	public static void insert(Integer key, xbean.RolesNotifyMap value) {
		_Tables_.getInstance().notify.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().notify.delete(key);
	}

	public static boolean add(Integer key, xbean.RolesNotifyMap value) {
		return _Tables_.getInstance().notify.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().notify.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.RolesNotifyMap> getCache() {
		return _Tables_.getInstance().notify.getCache();
	}

	public static mkdb.TTable<Integer, xbean.RolesNotifyMap> getTable() {
		return _Tables_.getInstance().notify;
	}

}
