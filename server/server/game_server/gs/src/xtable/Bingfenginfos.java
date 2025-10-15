package xtable;

// typed table access point
public class Bingfenginfos {
	Bingfenginfos() {
	}

	public static xbean.BingFengInfo get(Integer key) {
		return _Tables_.getInstance().bingfenginfos.get(key);
	}

	public static xbean.BingFengInfo get(Integer key, xbean.BingFengInfo value) {
		return _Tables_.getInstance().bingfenginfos.get(key, value);
	}

	public static void insert(Integer key, xbean.BingFengInfo value) {
		_Tables_.getInstance().bingfenginfos.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().bingfenginfos.delete(key);
	}

	public static boolean add(Integer key, xbean.BingFengInfo value) {
		return _Tables_.getInstance().bingfenginfos.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().bingfenginfos.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.BingFengInfo> getCache() {
		return _Tables_.getInstance().bingfenginfos.getCache();
	}

	public static mkdb.TTable<Integer, xbean.BingFengInfo> getTable() {
		return _Tables_.getInstance().bingfenginfos;
	}

	public static xbean.BingFengInfo select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengInfo, xbean.BingFengInfo>() {
			public xbean.BingFengInfo get(xbean.BingFengInfo v) { return v.toData(); }
		});
	}

	public static Integer selectInstzoneid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengInfo, Integer>() {
				public Integer get(xbean.BingFengInfo v) { return v.getInstzoneid(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectStagetime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengInfo, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.BingFengInfo v) { return v.getStagetimeAsData(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectStageround(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengInfo, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.BingFengInfo v) { return v.getStageroundAsData(); }
			});
	}

	public static java.util.Map<Integer, Long> selectStagebest(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengInfo, java.util.Map<Integer, Long>>() {
				public java.util.Map<Integer, Long> get(xbean.BingFengInfo v) { return v.getStagebestAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.SchoolStageTime> selectSchoolstage(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.BingFengInfo, java.util.Map<Integer, xbean.SchoolStageTime>>() {
				public java.util.Map<Integer, xbean.SchoolStageTime> get(xbean.BingFengInfo v) { return v.getSchoolstageAsData(); }
			});
	}

}
