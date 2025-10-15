package xtable;

// typed table access point
public class Redpackinfoviewlisttab {
	Redpackinfoviewlisttab() {
	}

	public static xbean.RedPackInfoViewList get(Integer key) {
		return _Tables_.getInstance().redpackinfoviewlisttab.get(key);
	}

	public static xbean.RedPackInfoViewList get(Integer key, xbean.RedPackInfoViewList value) {
		return _Tables_.getInstance().redpackinfoviewlisttab.get(key, value);
	}

	public static void insert(Integer key, xbean.RedPackInfoViewList value) {
		_Tables_.getInstance().redpackinfoviewlisttab.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().redpackinfoviewlisttab.delete(key);
	}

	public static boolean add(Integer key, xbean.RedPackInfoViewList value) {
		return _Tables_.getInstance().redpackinfoviewlisttab.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().redpackinfoviewlisttab.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.RedPackInfoViewList> getCache() {
		return _Tables_.getInstance().redpackinfoviewlisttab.getCache();
	}

	public static mkdb.TTable<Integer, xbean.RedPackInfoViewList> getTable() {
		return _Tables_.getInstance().redpackinfoviewlisttab;
	}

	public static xbean.RedPackInfoViewList select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RedPackInfoViewList, xbean.RedPackInfoViewList>() {
			public xbean.RedPackInfoViewList get(xbean.RedPackInfoViewList v) { return v.toData(); }
		});
	}

	public static java.util.Map<String, xbean.RedPackBaseInfo> selectRedpackinfoviewlist(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.RedPackInfoViewList, java.util.Map<String, xbean.RedPackBaseInfo>>() {
				public java.util.Map<String, xbean.RedPackBaseInfo> get(xbean.RedPackInfoViewList v) { return v.getRedpackinfoviewlistAsData(); }
			});
	}

}
