package xtable;

// typed table access point
public class Broadcastmsgtab {
	Broadcastmsgtab() {
	}

	public static xbean.BroadcastMsgList get(Integer key) {
		return _Tables_.getInstance().broadcastmsgtab.get(key);
	}

	public static xbean.BroadcastMsgList get(Integer key, xbean.BroadcastMsgList value) {
		return _Tables_.getInstance().broadcastmsgtab.get(key, value);
	}

	public static void insert(Integer key, xbean.BroadcastMsgList value) {
		_Tables_.getInstance().broadcastmsgtab.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().broadcastmsgtab.delete(key);
	}

	public static boolean add(Integer key, xbean.BroadcastMsgList value) {
		return _Tables_.getInstance().broadcastmsgtab.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().broadcastmsgtab.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.BroadcastMsgList> getCache() {
		return _Tables_.getInstance().broadcastmsgtab.getCache();
	}

	public static mkdb.TTable<Integer, xbean.BroadcastMsgList> getTable() {
		return _Tables_.getInstance().broadcastmsgtab;
	}

	public static xbean.BroadcastMsgList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.BroadcastMsgList, xbean.BroadcastMsgList>() {
			public xbean.BroadcastMsgList get(xbean.BroadcastMsgList v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.BroadcastMsg> selectMsglist(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.BroadcastMsgList, java.util.List<xbean.BroadcastMsg>>() {
				public java.util.List<xbean.BroadcastMsg> get(xbean.BroadcastMsgList v) { return v.getMsglistAsData(); }
			});
	}

}
