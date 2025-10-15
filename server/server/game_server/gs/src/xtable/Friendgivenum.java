package xtable;

// typed table access point
public class Friendgivenum {
	Friendgivenum() {
	}

	public static xbean.FriendGiveNum get(Long key) {
		return _Tables_.getInstance().friendgivenum.get(key);
	}

	public static xbean.FriendGiveNum get(Long key, xbean.FriendGiveNum value) {
		return _Tables_.getInstance().friendgivenum.get(key, value);
	}

	public static void insert(Long key, xbean.FriendGiveNum value) {
		_Tables_.getInstance().friendgivenum.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().friendgivenum.delete(key);
	}

	public static boolean add(Long key, xbean.FriendGiveNum value) {
		return _Tables_.getInstance().friendgivenum.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().friendgivenum.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.FriendGiveNum> getCache() {
		return _Tables_.getInstance().friendgivenum.getCache();
	}

	public static mkdb.TTable<Long, xbean.FriendGiveNum> getTable() {
		return _Tables_.getInstance().friendgivenum;
	}

	public static xbean.FriendGiveNum select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FriendGiveNum, xbean.FriendGiveNum>() {
			public xbean.FriendGiveNum get(xbean.FriendGiveNum v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, Integer> selectGivenummap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FriendGiveNum, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.FriendGiveNum v) { return v.getGivenummapAsData(); }
			});
	}

	public static Long selectResettime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.FriendGiveNum, Long>() {
				public Long get(xbean.FriendGiveNum v) { return v.getResettime(); }
			});
	}

}
