package xtable;

// typed table access point
public class Gacdpic {
	Gacdpic() {
	}

	public static xbean.GacdPicStatus get(Integer key) {
		return _Tables_.getInstance().gacdpic.get(key);
	}

	public static xbean.GacdPicStatus get(Integer key, xbean.GacdPicStatus value) {
		return _Tables_.getInstance().gacdpic.get(key, value);
	}

	public static void insert(Integer key, xbean.GacdPicStatus value) {
		_Tables_.getInstance().gacdpic.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().gacdpic.delete(key);
	}

	public static boolean add(Integer key, xbean.GacdPicStatus value) {
		return _Tables_.getInstance().gacdpic.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().gacdpic.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.GacdPicStatus> getCache() {
		return _Tables_.getInstance().gacdpic.getCache();
	}

	public static mkdb.TTable<Integer, xbean.GacdPicStatus> getTable() {
		return _Tables_.getInstance().gacdpic;
	}

	public static xbean.GacdPicStatus select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.GacdPicStatus, xbean.GacdPicStatus>() {
			public xbean.GacdPicStatus get(xbean.GacdPicStatus v) { return v.toData(); }
		});
	}

	public static Long selectPictime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.GacdPicStatus, Long>() {
				public Long get(xbean.GacdPicStatus v) { return v.getPictime(); }
			});
	}

	public static String selectAnswer(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.GacdPicStatus, String>() {
				public String get(xbean.GacdPicStatus v) { return v.getAnswer(); }
			});
	}

	public static Integer selectResult(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.GacdPicStatus, Integer>() {
				public Integer get(xbean.GacdPicStatus v) { return v.getResult(); }
			});
	}

	public static Long selectQuestiontime(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.GacdPicStatus, Long>() {
				public Long get(xbean.GacdPicStatus v) { return v.getQuestiontime(); }
			});
	}

}
