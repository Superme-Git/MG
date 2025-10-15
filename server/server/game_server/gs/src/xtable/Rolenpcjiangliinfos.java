package xtable;

// typed table access point
public class Rolenpcjiangliinfos {
	Rolenpcjiangliinfos() {
	}

	public static xbean.RolenpcjiangliInfo get(Long key) {
		return _Tables_.getInstance().rolenpcjiangliinfos.get(key);
	}

	public static xbean.RolenpcjiangliInfo get(Long key, xbean.RolenpcjiangliInfo value) {
		return _Tables_.getInstance().rolenpcjiangliinfos.get(key, value);
	}

	public static void insert(Long key, xbean.RolenpcjiangliInfo value) {
		_Tables_.getInstance().rolenpcjiangliinfos.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolenpcjiangliinfos.delete(key);
	}

	public static boolean add(Long key, xbean.RolenpcjiangliInfo value) {
		return _Tables_.getInstance().rolenpcjiangliinfos.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolenpcjiangliinfos.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RolenpcjiangliInfo> getCache() {
		return _Tables_.getInstance().rolenpcjiangliinfos.getCache();
	}

	public static mkdb.TTable<Long, xbean.RolenpcjiangliInfo> getTable() {
		return _Tables_.getInstance().rolenpcjiangliinfos;
	}

	public static xbean.RolenpcjiangliInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolenpcjiangliInfo, xbean.RolenpcjiangliInfo>() {
			public xbean.RolenpcjiangliInfo get(xbean.RolenpcjiangliInfo v) { return v.toData(); }
		});
	}

	public static Integer selectAwardtimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolenpcjiangliInfo, Integer>() {
				public Integer get(xbean.RolenpcjiangliInfo v) { return v.getAwardtimes(); }
			});
	}

	public static Long selectLastawardtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RolenpcjiangliInfo, Long>() {
				public Long get(xbean.RolenpcjiangliInfo v) { return v.getLastawardtime(); }
			});
	}

}
