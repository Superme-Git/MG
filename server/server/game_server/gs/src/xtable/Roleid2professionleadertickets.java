package xtable;

// typed table access point
public class Roleid2professionleadertickets {
	Roleid2professionleadertickets() {
	}

	public static xbean.ProfessionLeaderTicketInfo get(Long key) {
		return _Tables_.getInstance().roleid2professionleadertickets.get(key);
	}

	public static xbean.ProfessionLeaderTicketInfo get(Long key, xbean.ProfessionLeaderTicketInfo value) {
		return _Tables_.getInstance().roleid2professionleadertickets.get(key, value);
	}

	public static void insert(Long key, xbean.ProfessionLeaderTicketInfo value) {
		_Tables_.getInstance().roleid2professionleadertickets.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleid2professionleadertickets.delete(key);
	}

	public static boolean add(Long key, xbean.ProfessionLeaderTicketInfo value) {
		return _Tables_.getInstance().roleid2professionleadertickets.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleid2professionleadertickets.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ProfessionLeaderTicketInfo> getCache() {
		return _Tables_.getInstance().roleid2professionleadertickets.getCache();
	}

	public static mkdb.TTable<Long, xbean.ProfessionLeaderTicketInfo> getTable() {
		return _Tables_.getInstance().roleid2professionleadertickets;
	}

	public static xbean.ProfessionLeaderTicketInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderTicketInfo, xbean.ProfessionLeaderTicketInfo>() {
			public xbean.ProfessionLeaderTicketInfo get(xbean.ProfessionLeaderTicketInfo v) { return v.toData(); }
		});
	}

	public static Long selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderTicketInfo, Long>() {
				public Long get(xbean.ProfessionLeaderTicketInfo v) { return v.getRoleid(); }
			});
	}

	public static Integer selectTickets(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderTicketInfo, Integer>() {
				public Integer get(xbean.ProfessionLeaderTicketInfo v) { return v.getTickets(); }
			});
	}

	public static String selectWords(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderTicketInfo, String>() {
				public String get(xbean.ProfessionLeaderTicketInfo v) { return v.getWords(); }
			});
	}

}
