package xtable;

// typed table access point
public class Roleid2kickinfo {
	Roleid2kickinfo() {
	}

	public static xbean.KickInfo get(Long key) {
		return _Tables_.getInstance().roleid2kickinfo.get(key);
	}

	public static xbean.KickInfo get(Long key, xbean.KickInfo value) {
		return _Tables_.getInstance().roleid2kickinfo.get(key, value);
	}

	public static void insert(Long key, xbean.KickInfo value) {
		_Tables_.getInstance().roleid2kickinfo.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleid2kickinfo.delete(key);
	}

	public static boolean add(Long key, xbean.KickInfo value) {
		return _Tables_.getInstance().roleid2kickinfo.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleid2kickinfo.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.KickInfo> getCache() {
		return _Tables_.getInstance().roleid2kickinfo.getCache();
	}

	public static mkdb.TTable<Long, xbean.KickInfo> getTable() {
		return _Tables_.getInstance().roleid2kickinfo;
	}

	public static xbean.KickInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.KickInfo, xbean.KickInfo>() {
			public xbean.KickInfo get(xbean.KickInfo v) { return v.toData(); }
		});
	}

	public static java.util.List<Long> selectKicktime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.KickInfo, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.KickInfo v) { return v.getKicktimeAsData(); }
			});
	}

	public static Long selectExpire(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.KickInfo, Long>() {
				public Long get(xbean.KickInfo v) { return v.getExpire(); }
			});
	}

}
