package xtable;

// typed table access point
public class Timernpcinfotable {
	Timernpcinfotable() {
	}

	public static xbean.timerNpcInfoCol get(Integer key) {
		return _Tables_.getInstance().timernpcinfotable.get(key);
	}

	public static xbean.timerNpcInfoCol get(Integer key, xbean.timerNpcInfoCol value) {
		return _Tables_.getInstance().timernpcinfotable.get(key, value);
	}

	public static void insert(Integer key, xbean.timerNpcInfoCol value) {
		_Tables_.getInstance().timernpcinfotable.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().timernpcinfotable.delete(key);
	}

	public static boolean add(Integer key, xbean.timerNpcInfoCol value) {
		return _Tables_.getInstance().timernpcinfotable.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().timernpcinfotable.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.timerNpcInfoCol> getCache() {
		return _Tables_.getInstance().timernpcinfotable.getCache();
	}

	public static mkdb.TTable<Integer, xbean.timerNpcInfoCol> getTable() {
		return _Tables_.getInstance().timernpcinfotable;
	}

	public static xbean.timerNpcInfoCol select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.timerNpcInfoCol, xbean.timerNpcInfoCol>() {
			public xbean.timerNpcInfoCol get(xbean.timerNpcInfoCol v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, xbean.timerNpcInfo> selectNpcinfo(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.timerNpcInfoCol, java.util.Map<Long, xbean.timerNpcInfo>>() {
				public java.util.Map<Long, xbean.timerNpcInfo> get(xbean.timerNpcInfoCol v) { return v.getNpcinfoAsData(); }
			});
	}

}
