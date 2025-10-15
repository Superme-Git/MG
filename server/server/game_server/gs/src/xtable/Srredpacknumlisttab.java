package xtable;

// typed table access point
public class Srredpacknumlisttab {
	Srredpacknumlisttab() {
	}

	public static xbean.SRRedPackNumList get(Long key) {
		return _Tables_.getInstance().srredpacknumlisttab.get(key);
	}

	public static xbean.SRRedPackNumList get(Long key, xbean.SRRedPackNumList value) {
		return _Tables_.getInstance().srredpacknumlisttab.get(key, value);
	}

	public static void insert(Long key, xbean.SRRedPackNumList value) {
		_Tables_.getInstance().srredpacknumlisttab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().srredpacknumlisttab.delete(key);
	}

	public static boolean add(Long key, xbean.SRRedPackNumList value) {
		return _Tables_.getInstance().srredpacknumlisttab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().srredpacknumlisttab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.SRRedPackNumList> getCache() {
		return _Tables_.getInstance().srredpacknumlisttab.getCache();
	}

	public static mkdb.TTable<Long, xbean.SRRedPackNumList> getTable() {
		return _Tables_.getInstance().srredpacknumlisttab;
	}

	public static xbean.SRRedPackNumList select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SRRedPackNumList, xbean.SRRedPackNumList>() {
			public xbean.SRRedPackNumList get(xbean.SRRedPackNumList v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.SRRedPackNum> selectSrredpacknumlist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.SRRedPackNumList, java.util.Map<Integer, xbean.SRRedPackNum>>() {
				public java.util.Map<Integer, xbean.SRRedPackNum> get(xbean.SRRedPackNumList v) { return v.getSrredpacknumlistAsData(); }
			});
	}

}
