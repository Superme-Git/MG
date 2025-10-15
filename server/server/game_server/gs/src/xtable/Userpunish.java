package xtable;

// typed table access point
public class Userpunish {
	Userpunish() {
	}

	public static xbean.UserPunish get(Integer key) {
		return _Tables_.getInstance().userpunish.get(key);
	}

	public static xbean.UserPunish get(Integer key, xbean.UserPunish value) {
		return _Tables_.getInstance().userpunish.get(key, value);
	}

	public static void insert(Integer key, xbean.UserPunish value) {
		_Tables_.getInstance().userpunish.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().userpunish.delete(key);
	}

	public static boolean add(Integer key, xbean.UserPunish value) {
		return _Tables_.getInstance().userpunish.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().userpunish.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.UserPunish> getCache() {
		return _Tables_.getInstance().userpunish.getCache();
	}

	public static mkdb.TTable<Integer, xbean.UserPunish> getTable() {
		return _Tables_.getInstance().userpunish;
	}

	public static xbean.UserPunish select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserPunish, xbean.UserPunish>() {
			public xbean.UserPunish get(xbean.UserPunish v) { return v.toData(); }
		});
	}

	public static Integer selectUserid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserPunish, Integer>() {
				public Integer get(xbean.UserPunish v) { return v.getUserid(); }
			});
	}

	public static java.util.List<xbean.PunishRecord> selectRecords(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserPunish, java.util.List<xbean.PunishRecord>>() {
				public java.util.List<xbean.PunishRecord> get(xbean.UserPunish v) { return v.getRecordsAsData(); }
			});
	}

	public static Long selectReleasetime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserPunish, Long>() {
				public Long get(xbean.UserPunish v) { return v.getReleasetime(); }
			});
	}

	public static Integer selectWaiguatimes(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserPunish, Integer>() {
				public Integer get(xbean.UserPunish v) { return v.getWaiguatimes(); }
			});
	}

	public static Long selectSendmsgtime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.UserPunish, Long>() {
				public Long get(xbean.UserPunish v) { return v.getSendmsgtime(); }
			});
	}

}
