package xtable;

// typed table access point
public class Teaminvite {
	Teaminvite() {
	}

	public static xbean.InviteInfo get(Long key) {
		return _Tables_.getInstance().teaminvite.get(key);
	}

	public static xbean.InviteInfo get(Long key, xbean.InviteInfo value) {
		return _Tables_.getInstance().teaminvite.get(key, value);
	}

	public static void insert(Long key, xbean.InviteInfo value) {
		_Tables_.getInstance().teaminvite.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().teaminvite.delete(key);
	}

	public static boolean add(Long key, xbean.InviteInfo value) {
		return _Tables_.getInstance().teaminvite.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().teaminvite.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.InviteInfo> getCache() {
		return _Tables_.getInstance().teaminvite.getCache();
	}

	public static mkdb.TTable<Long, xbean.InviteInfo> getTable() {
		return _Tables_.getInstance().teaminvite;
	}

	public static xbean.InviteInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InviteInfo, xbean.InviteInfo>() {
			public xbean.InviteInfo get(xbean.InviteInfo v) { return v.toData(); }
		});
	}

	public static Boolean selectBeinginvited(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InviteInfo, Boolean>() {
				public Boolean get(xbean.InviteInfo v) { return v.getBeinginvited(); }
			});
	}

	public static xbean.TeamInvite selectInviting(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InviteInfo, xbean.TeamInvite>() {
				public xbean.TeamInvite get(xbean.InviteInfo v) { return v.getInviting(); }
			});
	}

	public static java.util.List<xbean.TeamInvite> selectInvited(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.InviteInfo, java.util.List<xbean.TeamInvite>>() {
				public java.util.List<xbean.TeamInvite> get(xbean.InviteInfo v) { return v.getInvitedAsData(); }
			});
	}

}
