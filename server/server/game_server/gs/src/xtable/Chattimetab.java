package xtable;

// typed table access point
public class Chattimetab {
	Chattimetab() {
	}

	public static xbean.ChatTime get(Long key) {
		return _Tables_.getInstance().chattimetab.get(key);
	}

	public static xbean.ChatTime get(Long key, xbean.ChatTime value) {
		return _Tables_.getInstance().chattimetab.get(key, value);
	}

	public static void insert(Long key, xbean.ChatTime value) {
		_Tables_.getInstance().chattimetab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().chattimetab.delete(key);
	}

	public static boolean add(Long key, xbean.ChatTime value) {
		return _Tables_.getInstance().chattimetab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().chattimetab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ChatTime> getCache() {
		return _Tables_.getInstance().chattimetab.getCache();
	}

	public static mkdb.TTable<Long, xbean.ChatTime> getTable() {
		return _Tables_.getInstance().chattimetab;
	}

	public static xbean.ChatTime select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChatTime, xbean.ChatTime>() {
			public xbean.ChatTime get(xbean.ChatTime v) { return v.toData(); }
		});
	}

	public static Long selectLastworldchattime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChatTime, Long>() {
				public Long get(xbean.ChatTime v) { return v.getLastworldchattime(); }
			});
	}

	public static Long selectLastcurrentchattime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChatTime, Long>() {
				public Long get(xbean.ChatTime v) { return v.getLastcurrentchattime(); }
			});
	}

	public static Long selectLastprofessionchattime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChatTime, Long>() {
				public Long get(xbean.ChatTime v) { return v.getLastprofessionchattime(); }
			});
	}

	public static Long selectLastclanchattime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChatTime, Long>() {
				public Long get(xbean.ChatTime v) { return v.getLastclanchattime(); }
			});
	}

	public static Long selectLastteamapplychattime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChatTime, Long>() {
				public Long get(xbean.ChatTime v) { return v.getLastteamapplychattime(); }
			});
	}

}
