package xtable;

// typed table access point
public class Refreshmonsterinfo {
	Refreshmonsterinfo() {
	}

	public static xbean.RefreshMonsterNpcInfo get(Integer key) {
		return _Tables_.getInstance().refreshmonsterinfo.get(key);
	}

	public static xbean.RefreshMonsterNpcInfo get(Integer key, xbean.RefreshMonsterNpcInfo value) {
		return _Tables_.getInstance().refreshmonsterinfo.get(key, value);
	}

	public static void insert(Integer key, xbean.RefreshMonsterNpcInfo value) {
		_Tables_.getInstance().refreshmonsterinfo.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().refreshmonsterinfo.delete(key);
	}

	public static boolean add(Integer key, xbean.RefreshMonsterNpcInfo value) {
		return _Tables_.getInstance().refreshmonsterinfo.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().refreshmonsterinfo.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.RefreshMonsterNpcInfo> getCache() {
		return _Tables_.getInstance().refreshmonsterinfo.getCache();
	}

	public static mkdb.TTable<Integer, xbean.RefreshMonsterNpcInfo> getTable() {
		return _Tables_.getInstance().refreshmonsterinfo;
	}

	public static xbean.RefreshMonsterNpcInfo select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RefreshMonsterNpcInfo, xbean.RefreshMonsterNpcInfo>() {
			public xbean.RefreshMonsterNpcInfo get(xbean.RefreshMonsterNpcInfo v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, Integer> selectNpcinfos(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RefreshMonsterNpcInfo, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.RefreshMonsterNpcInfo v) { return v.getNpcinfosAsData(); }
			});
	}

}
