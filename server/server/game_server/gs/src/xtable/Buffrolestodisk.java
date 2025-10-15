package xtable;

// typed table access point
public class Buffrolestodisk {
	Buffrolestodisk() {
	}

	public static xbean.StoredBuffRole get(Long key) {
		return _Tables_.getInstance().buffrolestodisk.get(key);
	}

	public static xbean.StoredBuffRole get(Long key, xbean.StoredBuffRole value) {
		return _Tables_.getInstance().buffrolestodisk.get(key, value);
	}

	public static void insert(Long key, xbean.StoredBuffRole value) {
		_Tables_.getInstance().buffrolestodisk.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().buffrolestodisk.delete(key);
	}

	public static boolean add(Long key, xbean.StoredBuffRole value) {
		return _Tables_.getInstance().buffrolestodisk.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().buffrolestodisk.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.StoredBuffRole> getCache() {
		return _Tables_.getInstance().buffrolestodisk.getCache();
	}

	public static mkdb.TTable<Long, xbean.StoredBuffRole> getTable() {
		return _Tables_.getInstance().buffrolestodisk;
	}

	public static xbean.StoredBuffRole select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.StoredBuffRole, xbean.StoredBuffRole>() {
			public xbean.StoredBuffRole get(xbean.StoredBuffRole v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.Buff> selectBuffs(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.StoredBuffRole, java.util.Map<Integer, xbean.Buff>>() {
				public java.util.Map<Integer, xbean.Buff> get(xbean.StoredBuffRole v) { return v.getBuffsAsData(); }
			});
	}

}
