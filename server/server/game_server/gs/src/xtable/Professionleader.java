package xtable;

// typed table access point
public class Professionleader {
	Professionleader() {
	}

	public static xbean.ProfessionLeaderInfo get(Integer key) {
		return _Tables_.getInstance().professionleader.get(key);
	}

	public static xbean.ProfessionLeaderInfo get(Integer key, xbean.ProfessionLeaderInfo value) {
		return _Tables_.getInstance().professionleader.get(key, value);
	}

	public static void insert(Integer key, xbean.ProfessionLeaderInfo value) {
		_Tables_.getInstance().professionleader.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().professionleader.delete(key);
	}

	public static boolean add(Integer key, xbean.ProfessionLeaderInfo value) {
		return _Tables_.getInstance().professionleader.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().professionleader.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ProfessionLeaderInfo> getCache() {
		return _Tables_.getInstance().professionleader.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ProfessionLeaderInfo> getTable() {
		return _Tables_.getInstance().professionleader;
	}

	public static xbean.ProfessionLeaderInfo select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderInfo, xbean.ProfessionLeaderInfo>() {
			public xbean.ProfessionLeaderInfo get(xbean.ProfessionLeaderInfo v) { return v.toData(); }
		});
	}

	public static Long selectRoleid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderInfo, Long>() {
				public Long get(xbean.ProfessionLeaderInfo v) { return v.getRoleid(); }
			});
	}

	public static Integer selectRefreshtimes(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderInfo, Integer>() {
				public Integer get(xbean.ProfessionLeaderInfo v) { return v.getRefreshtimes(); }
			});
	}

	public static Long selectLastrefreshtime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderInfo, Long>() {
				public Long get(xbean.ProfessionLeaderInfo v) { return v.getLastrefreshtime(); }
			});
	}

	public static java.util.List<xbean.Monster> selectMonsterbean(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderInfo, java.util.List<xbean.Monster>>() {
				public java.util.List<xbean.Monster> get(xbean.ProfessionLeaderInfo v) { return v.getMonsterbeanAsData(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectShapecomponent(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ProfessionLeaderInfo, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.ProfessionLeaderInfo v) { return v.getShapecomponentAsData(); }
			});
	}

}
