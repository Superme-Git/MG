package xtable;

// typed table access point
public class Ticketiteminfo {
	Ticketiteminfo() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().ticketiteminfo.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.TicketItemExInfo value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.TicketItemExInfo get(Long key) {
		return _Tables_.getInstance().ticketiteminfo.get(key);
	}

	public static xbean.TicketItemExInfo get(Long key, xbean.TicketItemExInfo value) {
		return _Tables_.getInstance().ticketiteminfo.get(key, value);
	}

	public static void insert(Long key, xbean.TicketItemExInfo value) {
		_Tables_.getInstance().ticketiteminfo.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().ticketiteminfo.delete(key);
	}

	public static boolean add(Long key, xbean.TicketItemExInfo value) {
		return _Tables_.getInstance().ticketiteminfo.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().ticketiteminfo.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.TicketItemExInfo> getCache() {
		return _Tables_.getInstance().ticketiteminfo.getCache();
	}

	public static mkdb.TTable<Long, xbean.TicketItemExInfo> getTable() {
		return _Tables_.getInstance().ticketiteminfo;
	}

	public static xbean.TicketItemExInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TicketItemExInfo, xbean.TicketItemExInfo>() {
			public xbean.TicketItemExInfo get(xbean.TicketItemExInfo v) { return v.toData(); }
		});
	}

	public static String selectTicketcode(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TicketItemExInfo, String>() {
				public String get(xbean.TicketItemExInfo v) { return v.getTicketcode(); }
			});
	}

}
