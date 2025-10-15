package xtable;

// typed table access point
public class Gmcreatenpcuniversal {
	Gmcreatenpcuniversal() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().gmcreatenpcuniversal.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.CreateNpcExecType value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.CreateNpcExecType get(Long key) {
		return _Tables_.getInstance().gmcreatenpcuniversal.get(key);
	}

	public static xbean.CreateNpcExecType get(Long key, xbean.CreateNpcExecType value) {
		return _Tables_.getInstance().gmcreatenpcuniversal.get(key, value);
	}

	public static void insert(Long key, xbean.CreateNpcExecType value) {
		_Tables_.getInstance().gmcreatenpcuniversal.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().gmcreatenpcuniversal.delete(key);
	}

	public static boolean add(Long key, xbean.CreateNpcExecType value) {
		return _Tables_.getInstance().gmcreatenpcuniversal.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().gmcreatenpcuniversal.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.CreateNpcExecType> getCache() {
		return _Tables_.getInstance().gmcreatenpcuniversal.getCache();
	}

	public static mkdb.TTable<Long, xbean.CreateNpcExecType> getTable() {
		return _Tables_.getInstance().gmcreatenpcuniversal;
	}

}
