package xtable;

// typed table access point
public class Offlinemsg {
	Offlinemsg() {
	}

	public static xbean.OfflineMsgList get(Long key) {
		return _Tables_.getInstance().offlinemsg.get(key);
	}

	public static xbean.OfflineMsgList get(Long key, xbean.OfflineMsgList value) {
		return _Tables_.getInstance().offlinemsg.get(key, value);
	}

	public static void insert(Long key, xbean.OfflineMsgList value) {
		_Tables_.getInstance().offlinemsg.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().offlinemsg.delete(key);
	}

	public static boolean add(Long key, xbean.OfflineMsgList value) {
		return _Tables_.getInstance().offlinemsg.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().offlinemsg.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.OfflineMsgList> getCache() {
		return _Tables_.getInstance().offlinemsg.getCache();
	}

	public static mkdb.TTable<Long, xbean.OfflineMsgList> getTable() {
		return _Tables_.getInstance().offlinemsg;
	}

	public static xbean.OfflineMsgList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.OfflineMsgList, xbean.OfflineMsgList>() {
			public xbean.OfflineMsgList get(xbean.OfflineMsgList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.OfflineMsgProtocol> selectProtocollist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.OfflineMsgList, java.util.List<xbean.OfflineMsgProtocol>>() {
				public java.util.List<xbean.OfflineMsgProtocol> get(xbean.OfflineMsgList v) { return v.getProtocollistAsData(); }
			});
	}

}
