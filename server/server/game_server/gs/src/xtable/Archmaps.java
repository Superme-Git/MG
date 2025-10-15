package xtable;

// typed table access point
public class Archmaps {
	Archmaps() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().archmaps.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.ArchMap value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.ArchMap get(Long key) {
		return _Tables_.getInstance().archmaps.get(key);
	}

	public static xbean.ArchMap get(Long key, xbean.ArchMap value) {
		return _Tables_.getInstance().archmaps.get(key, value);
	}

	public static void insert(Long key, xbean.ArchMap value) {
		_Tables_.getInstance().archmaps.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().archmaps.delete(key);
	}

	public static boolean add(Long key, xbean.ArchMap value) {
		return _Tables_.getInstance().archmaps.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().archmaps.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ArchMap> getCache() {
		return _Tables_.getInstance().archmaps.getCache();
	}

	public static mkdb.TTable<Long, xbean.ArchMap> getTable() {
		return _Tables_.getInstance().archmaps;
	}

	public static xbean.ArchMap select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ArchMap, xbean.ArchMap>() {
			public xbean.ArchMap get(xbean.ArchMap v) { return v.toData(); }
		});
	}

	public static Integer selectMapid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ArchMap, Integer>() {
				public Integer get(xbean.ArchMap v) { return v.getMapid(); }
			});
	}

	public static Integer selectPosx(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ArchMap, Integer>() {
				public Integer get(xbean.ArchMap v) { return v.getPosx(); }
			});
	}

	public static Integer selectPosy(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ArchMap, Integer>() {
				public Integer get(xbean.ArchMap v) { return v.getPosy(); }
			});
	}

}
