package xtable;

// typed table access point
public class Auuserinfo {
	Auuserinfo() {
	}

	public static xbean.AUUserInfo get(Integer key) {
		return _Tables_.getInstance().auuserinfo.get(key);
	}

	public static xbean.AUUserInfo get(Integer key, xbean.AUUserInfo value) {
		return _Tables_.getInstance().auuserinfo.get(key, value);
	}

	public static void insert(Integer key, xbean.AUUserInfo value) {
		_Tables_.getInstance().auuserinfo.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().auuserinfo.delete(key);
	}

	public static boolean add(Integer key, xbean.AUUserInfo value) {
		return _Tables_.getInstance().auuserinfo.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().auuserinfo.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.AUUserInfo> getCache() {
		return _Tables_.getInstance().auuserinfo.getCache();
	}

	public static mkdb.TTable<Integer, xbean.AUUserInfo> getTable() {
		return _Tables_.getInstance().auuserinfo;
	}

	public static xbean.AUUserInfo select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, xbean.AUUserInfo>() {
			public xbean.AUUserInfo get(xbean.AUUserInfo v) { return v.toData(); }
		});
	}

	public static Integer selectRetcode(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, Integer>() {
				public Integer get(xbean.AUUserInfo v) { return v.getRetcode(); }
			});
	}

	public static Integer selectFunc(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, Integer>() {
				public Integer get(xbean.AUUserInfo v) { return v.getFunc(); }
			});
	}

	public static Integer selectFuncparm(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, Integer>() {
				public Integer get(xbean.AUUserInfo v) { return v.getFuncparm(); }
			});
	}

	public static Integer selectLoginip(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, Integer>() {
				public Integer get(xbean.AUUserInfo v) { return v.getLoginip(); }
			});
	}

	public static Integer selectBlisgm(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, Integer>() {
				public Integer get(xbean.AUUserInfo v) { return v.getBlisgm(); }
			});
	}

	public static java.util.List<Integer> selectAuth(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.AUUserInfo v) { return v.getAuthAsData(); }
			});
	}

	public static Integer selectAlgorithm(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, Integer>() {
				public Integer get(xbean.AUUserInfo v) { return v.getAlgorithm(); }
			});
	}

	public static String selectNickname(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, String>() {
				public String get(xbean.AUUserInfo v) { return v.getNickname(); }
			});
	}

	public static String selectUsername(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, String>() {
				public String get(xbean.AUUserInfo v) { return v.getUsername(); }
			});
	}

	public static String selectMac(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, String>() {
				public String get(xbean.AUUserInfo v) { return v.getMac(); }
			});
	}

	public static String selectOs(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, String>() {
				public String get(xbean.AUUserInfo v) { return v.getOs(); }
			});
	}

	public static String selectPlatform(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, String>() {
				public String get(xbean.AUUserInfo v) { return v.getPlatform(); }
			});
	}

	public static String selectPlatname(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, String>() {
				public String get(xbean.AUUserInfo v) { return v.getPlatname(); }
			});
	}

	public static String selectClientsource(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, String>() {
				public String get(xbean.AUUserInfo v) { return v.getClientsource(); }
			});
	}

	public static String selectIdfa(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.AUUserInfo, String>() {
				public String get(xbean.AUUserInfo v) { return v.getIdfa(); }
			});
	}

}
