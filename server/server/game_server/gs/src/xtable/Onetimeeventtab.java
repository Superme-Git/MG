package xtable;

// typed table access point
public class Onetimeeventtab {
	Onetimeeventtab() {
	}

	public static xbean.OnetimeEvent get(Long key) {
		return _Tables_.getInstance().onetimeeventtab.get(key);
	}

	public static xbean.OnetimeEvent get(Long key, xbean.OnetimeEvent value) {
		return _Tables_.getInstance().onetimeeventtab.get(key, value);
	}

	public static void insert(Long key, xbean.OnetimeEvent value) {
		_Tables_.getInstance().onetimeeventtab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().onetimeeventtab.delete(key);
	}

	public static boolean add(Long key, xbean.OnetimeEvent value) {
		return _Tables_.getInstance().onetimeeventtab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().onetimeeventtab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.OnetimeEvent> getCache() {
		return _Tables_.getInstance().onetimeeventtab.getCache();
	}

	public static mkdb.TTable<Long, xbean.OnetimeEvent> getTable() {
		return _Tables_.getInstance().onetimeeventtab;
	}

}
