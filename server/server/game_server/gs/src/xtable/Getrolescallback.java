package xtable;

// typed table access point
public class Getrolescallback {
	Getrolescallback() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().getrolescallback.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.GetRolesCallBackInst value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.GetRolesCallBackInst get(Long key) {
		return _Tables_.getInstance().getrolescallback.get(key);
	}

	public static xbean.GetRolesCallBackInst get(Long key, xbean.GetRolesCallBackInst value) {
		return _Tables_.getInstance().getrolescallback.get(key, value);
	}

	public static void insert(Long key, xbean.GetRolesCallBackInst value) {
		_Tables_.getInstance().getrolescallback.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().getrolescallback.delete(key);
	}

	public static boolean add(Long key, xbean.GetRolesCallBackInst value) {
		return _Tables_.getInstance().getrolescallback.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().getrolescallback.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.GetRolesCallBackInst> getCache() {
		return _Tables_.getInstance().getrolescallback.getCache();
	}

	public static mkdb.TTable<Long, xbean.GetRolesCallBackInst> getTable() {
		return _Tables_.getInstance().getrolescallback;
	}

}
