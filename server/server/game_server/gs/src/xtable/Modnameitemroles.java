package xtable;

// typed table access point
public class Modnameitemroles {
	Modnameitemroles() {
	}

	public static xbean.ModifyNameRole get(Long key) {
		return _Tables_.getInstance().modnameitemroles.get(key);
	}

	public static xbean.ModifyNameRole get(Long key, xbean.ModifyNameRole value) {
		return _Tables_.getInstance().modnameitemroles.get(key, value);
	}

	public static void insert(Long key, xbean.ModifyNameRole value) {
		_Tables_.getInstance().modnameitemroles.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().modnameitemroles.delete(key);
	}

	public static boolean add(Long key, xbean.ModifyNameRole value) {
		return _Tables_.getInstance().modnameitemroles.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().modnameitemroles.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ModifyNameRole> getCache() {
		return _Tables_.getInstance().modnameitemroles.getCache();
	}

	public static mkdb.TTable<Long, xbean.ModifyNameRole> getTable() {
		return _Tables_.getInstance().modnameitemroles;
	}

	public static xbean.ModifyNameRole select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ModifyNameRole, xbean.ModifyNameRole>() {
			public xbean.ModifyNameRole get(xbean.ModifyNameRole v) { return v.toData(); }
		});
	}

	public static Long selectLastbuytime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ModifyNameRole, Long>() {
				public Long get(xbean.ModifyNameRole v) { return v.getLastbuytime(); }
			});
	}

	public static Long selectLastmodtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ModifyNameRole, Long>() {
				public Long get(xbean.ModifyNameRole v) { return v.getLastmodtime(); }
			});
	}

	public static Integer selectBuycount(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ModifyNameRole, Integer>() {
				public Integer get(xbean.ModifyNameRole v) { return v.getBuycount(); }
			});
	}

	public static Integer selectModcount(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ModifyNameRole, Integer>() {
				public Integer get(xbean.ModifyNameRole v) { return v.getModcount(); }
			});
	}

}
