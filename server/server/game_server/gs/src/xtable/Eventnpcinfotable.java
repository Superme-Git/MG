package xtable;

// typed table access point
public class Eventnpcinfotable {
	Eventnpcinfotable() {
	}

	public static xbean.eventNpcInfoCol get(Integer key) {
		return _Tables_.getInstance().eventnpcinfotable.get(key);
	}

	public static xbean.eventNpcInfoCol get(Integer key, xbean.eventNpcInfoCol value) {
		return _Tables_.getInstance().eventnpcinfotable.get(key, value);
	}

	public static void insert(Integer key, xbean.eventNpcInfoCol value) {
		_Tables_.getInstance().eventnpcinfotable.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().eventnpcinfotable.delete(key);
	}

	public static boolean add(Integer key, xbean.eventNpcInfoCol value) {
		return _Tables_.getInstance().eventnpcinfotable.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().eventnpcinfotable.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.eventNpcInfoCol> getCache() {
		return _Tables_.getInstance().eventnpcinfotable.getCache();
	}

	public static mkdb.TTable<Integer, xbean.eventNpcInfoCol> getTable() {
		return _Tables_.getInstance().eventnpcinfotable;
	}

	public static xbean.eventNpcInfoCol select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.eventNpcInfoCol, xbean.eventNpcInfoCol>() {
			public xbean.eventNpcInfoCol get(xbean.eventNpcInfoCol v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, xbean.eventNpcInfo> selectNpcinfo(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.eventNpcInfoCol, java.util.Map<Long, xbean.eventNpcInfo>>() {
				public java.util.Map<Long, xbean.eventNpcInfo> get(xbean.eventNpcInfoCol v) { return v.getNpcinfoAsData(); }
			});
	}

}
