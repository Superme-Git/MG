package xtable;

// typed table access point
public class Worldeventtab {
	Worldeventtab() {
	}

	public static xbean.EventInfo get(Long key) {
		return _Tables_.getInstance().worldeventtab.get(key);
	}

	public static xbean.EventInfo get(Long key, xbean.EventInfo value) {
		return _Tables_.getInstance().worldeventtab.get(key, value);
	}

	public static void insert(Long key, xbean.EventInfo value) {
		_Tables_.getInstance().worldeventtab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().worldeventtab.delete(key);
	}

	public static boolean add(Long key, xbean.EventInfo value) {
		return _Tables_.getInstance().worldeventtab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().worldeventtab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.EventInfo> getCache() {
		return _Tables_.getInstance().worldeventtab.getCache();
	}

	public static mkdb.TTable<Long, xbean.EventInfo> getTable() {
		return _Tables_.getInstance().worldeventtab;
	}

}
