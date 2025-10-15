package xtable;

// typed table access point
public class Friends {
	Friends() {
	}

	public static xbean.FriendGroups get(Long key) {
		return _Tables_.getInstance().friends.get(key);
	}

	public static xbean.FriendGroups get(Long key, xbean.FriendGroups value) {
		return _Tables_.getInstance().friends.get(key, value);
	}

	public static void insert(Long key, xbean.FriendGroups value) {
		_Tables_.getInstance().friends.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().friends.delete(key);
	}

	public static boolean add(Long key, xbean.FriendGroups value) {
		return _Tables_.getInstance().friends.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().friends.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.FriendGroups> getCache() {
		return _Tables_.getInstance().friends.getCache();
	}

	public static mkdb.TTable<Long, xbean.FriendGroups> getTable() {
		return _Tables_.getInstance().friends;
	}

	public static xbean.FriendGroups select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FriendGroups, xbean.FriendGroups>() {
			public xbean.FriendGroups get(xbean.FriendGroups v) { return v.toData(); }
		});
	}

	public static Integer selectRefusestrangermsg(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FriendGroups, Integer>() {
				public Integer get(xbean.FriendGroups v) { return v.getRefusestrangermsg(); }
			});
	}

	public static java.util.Map<Long, xbean.FriendBean> selectFriendmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FriendGroups, java.util.Map<Long, xbean.FriendBean>>() {
				public java.util.Map<Long, xbean.FriendBean> get(xbean.FriendGroups v) { return v.getFriendmapAsData(); }
			});
	}

	public static xbean.FriendlyDegreesLimit selectFriendlydegreeslimit(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FriendGroups, xbean.FriendlyDegreesLimit>() {
				public xbean.FriendlyDegreesLimit get(xbean.FriendGroups v) { return v.getFriendlydegreeslimit(); }
			});
	}

	public static String selectSign(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FriendGroups, String>() {
				public String get(xbean.FriendGroups v) { return v.getSign(); }
			});
	}

	public static java.util.List<xbean.OfflineMsg> selectOfflinemsglist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FriendGroups, java.util.List<xbean.OfflineMsg>>() {
				public java.util.List<xbean.OfflineMsg> get(xbean.FriendGroups v) { return v.getOfflinemsglistAsData(); }
			});
	}

}
