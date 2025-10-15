package xtable;

// typed table access point
public class Impexamstatetbl {
	Impexamstatetbl() {
	}

	public static xbean.ImpExamStateRecord get(Integer key) {
		return _Tables_.getInstance().impexamstatetbl.get(key);
	}

	public static xbean.ImpExamStateRecord get(Integer key, xbean.ImpExamStateRecord value) {
		return _Tables_.getInstance().impexamstatetbl.get(key, value);
	}

	public static void insert(Integer key, xbean.ImpExamStateRecord value) {
		_Tables_.getInstance().impexamstatetbl.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().impexamstatetbl.delete(key);
	}

	public static boolean add(Integer key, xbean.ImpExamStateRecord value) {
		return _Tables_.getInstance().impexamstatetbl.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().impexamstatetbl.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ImpExamStateRecord> getCache() {
		return _Tables_.getInstance().impexamstatetbl.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ImpExamStateRecord> getTable() {
		return _Tables_.getInstance().impexamstatetbl;
	}

	public static xbean.ImpExamStateRecord select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamStateRecord, xbean.ImpExamStateRecord>() {
			public xbean.ImpExamStateRecord get(xbean.ImpExamStateRecord v) { return v.toData(); }
		});
	}

	public static java.util.List<Long> selectIdslist(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamStateRecord, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.ImpExamStateRecord v) { return v.getIdslistAsData(); }
			});
	}

	public static java.util.List<Long> selectPassidslist(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamStateRecord, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.ImpExamStateRecord v) { return v.getPassidslistAsData(); }
			});
	}

	public static java.util.List<xbean.ImpExamStatePassRole> selectPassrolelist(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ImpExamStateRecord, java.util.List<xbean.ImpExamStatePassRole>>() {
				public java.util.List<xbean.ImpExamStatePassRole> get(xbean.ImpExamStateRecord v) { return v.getPassrolelistAsData(); }
			});
	}

}
