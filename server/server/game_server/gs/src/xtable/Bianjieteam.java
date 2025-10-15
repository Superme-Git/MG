package xtable;

// typed table access point
public class Bianjieteam {
	Bianjieteam() {
	}

	public static xbean.BJTeamInfo get(Integer key) {
		return _Tables_.getInstance().bianjieteam.get(key);
	}

	public static xbean.BJTeamInfo get(Integer key, xbean.BJTeamInfo value) {
		return _Tables_.getInstance().bianjieteam.get(key, value);
	}

	public static void insert(Integer key, xbean.BJTeamInfo value) {
		_Tables_.getInstance().bianjieteam.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().bianjieteam.delete(key);
	}

	public static boolean add(Integer key, xbean.BJTeamInfo value) {
		return _Tables_.getInstance().bianjieteam.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().bianjieteam.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.BJTeamInfo> getCache() {
		return _Tables_.getInstance().bianjieteam.getCache();
	}

	public static mkdb.TTable<Integer, xbean.BJTeamInfo> getTable() {
		return _Tables_.getInstance().bianjieteam;
	}

	public static xbean.BJTeamInfo select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.BJTeamInfo, xbean.BJTeamInfo>() {
			public xbean.BJTeamInfo get(xbean.BJTeamInfo v) { return v.toData(); }
		});
	}

	public static Integer selectServiceid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.BJTeamInfo, Integer>() {
				public Integer get(xbean.BJTeamInfo v) { return v.getServiceid(); }
			});
	}

	public static java.util.List<Long> selectBjdata(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.BJTeamInfo, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.BJTeamInfo v) { return v.getBjdataAsData(); }
			});
	}

}
