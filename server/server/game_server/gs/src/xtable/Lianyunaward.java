package xtable;

// typed table access point
public class Lianyunaward {
	Lianyunaward() {
	}

	public static xbean.LianyunAwardInfo get(Integer key) {
		return _Tables_.getInstance().lianyunaward.get(key);
	}

	public static xbean.LianyunAwardInfo get(Integer key, xbean.LianyunAwardInfo value) {
		return _Tables_.getInstance().lianyunaward.get(key, value);
	}

	public static void insert(Integer key, xbean.LianyunAwardInfo value) {
		_Tables_.getInstance().lianyunaward.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().lianyunaward.delete(key);
	}

	public static boolean add(Integer key, xbean.LianyunAwardInfo value) {
		return _Tables_.getInstance().lianyunaward.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().lianyunaward.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.LianyunAwardInfo> getCache() {
		return _Tables_.getInstance().lianyunaward.getCache();
	}

	public static mkdb.TTable<Integer, xbean.LianyunAwardInfo> getTable() {
		return _Tables_.getInstance().lianyunaward;
	}

	public static xbean.LianyunAwardInfo select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.LianyunAwardInfo, xbean.LianyunAwardInfo>() {
			public xbean.LianyunAwardInfo get(xbean.LianyunAwardInfo v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Long> selectAwards(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.LianyunAwardInfo, java.util.Map<Integer, Long>>() {
				public java.util.Map<Integer, Long> get(xbean.LianyunAwardInfo v) { return v.getAwardsAsData(); }
			});
	}

}
