package xtable;

// typed table access point
public class Invitepeopleinfo {
	Invitepeopleinfo() {
	}

	public static xbean.InvitePeopleInfo get(Long key) {
		return _Tables_.getInstance().invitepeopleinfo.get(key);
	}

	public static xbean.InvitePeopleInfo get(Long key, xbean.InvitePeopleInfo value) {
		return _Tables_.getInstance().invitepeopleinfo.get(key, value);
	}

	public static void insert(Long key, xbean.InvitePeopleInfo value) {
		_Tables_.getInstance().invitepeopleinfo.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().invitepeopleinfo.delete(key);
	}

	public static boolean add(Long key, xbean.InvitePeopleInfo value) {
		return _Tables_.getInstance().invitepeopleinfo.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().invitepeopleinfo.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.InvitePeopleInfo> getCache() {
		return _Tables_.getInstance().invitepeopleinfo.getCache();
	}

	public static mkdb.TTable<Long, xbean.InvitePeopleInfo> getTable() {
		return _Tables_.getInstance().invitepeopleinfo;
	}

	public static xbean.InvitePeopleInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InvitePeopleInfo, xbean.InvitePeopleInfo>() {
			public xbean.InvitePeopleInfo get(xbean.InvitePeopleInfo v) { return v.toData(); }
		});
	}

	public static Long selectInviteme(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InvitePeopleInfo, Long>() {
				public Long get(xbean.InvitePeopleInfo v) { return v.getInviteme(); }
			});
	}

	public static java.util.List<Long> selectAminvites(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InvitePeopleInfo, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.InvitePeopleInfo v) { return v.getAminvitesAsData(); }
			});
	}

	public static java.util.List<Integer> selectAwardhistory(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InvitePeopleInfo, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.InvitePeopleInfo v) { return v.getAwardhistoryAsData(); }
			});
	}

}
