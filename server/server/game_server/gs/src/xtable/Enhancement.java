package xtable;

// typed table access point
public class Enhancement {
	Enhancement() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().enhancement.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.EnhancementAttr value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.EnhancementAttr get(Long key) {
		return _Tables_.getInstance().enhancement.get(key);
	}

	public static xbean.EnhancementAttr get(Long key, xbean.EnhancementAttr value) {
		return _Tables_.getInstance().enhancement.get(key, value);
	}

	public static void insert(Long key, xbean.EnhancementAttr value) {
		_Tables_.getInstance().enhancement.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().enhancement.delete(key);
	}

	public static boolean add(Long key, xbean.EnhancementAttr value) {
		return _Tables_.getInstance().enhancement.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().enhancement.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.EnhancementAttr> getCache() {
		return _Tables_.getInstance().enhancement.getCache();
	}

	public static mkdb.TTable<Long, xbean.EnhancementAttr> getTable() {
		return _Tables_.getInstance().enhancement;
	}

	public static xbean.EnhancementAttr select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EnhancementAttr, xbean.EnhancementAttr>() {
			public xbean.EnhancementAttr get(xbean.EnhancementAttr v) { return v.toData(); }
		});
	}

	public static Integer selectLevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EnhancementAttr, Integer>() {
				public Integer get(xbean.EnhancementAttr v) { return v.getLevel(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectAttrmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EnhancementAttr, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.EnhancementAttr v) { return v.getAttrmapAsData(); }
			});
	}

}
