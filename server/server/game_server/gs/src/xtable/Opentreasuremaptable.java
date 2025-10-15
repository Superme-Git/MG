package xtable;

// typed table access point
public class Opentreasuremaptable {
	Opentreasuremaptable() {
	}

	public static xbean.TreasureMapInfo get(Long key) {
		return _Tables_.getInstance().opentreasuremaptable.get(key);
	}

	public static xbean.TreasureMapInfo get(Long key, xbean.TreasureMapInfo value) {
		return _Tables_.getInstance().opentreasuremaptable.get(key, value);
	}

	public static void insert(Long key, xbean.TreasureMapInfo value) {
		_Tables_.getInstance().opentreasuremaptable.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().opentreasuremaptable.delete(key);
	}

	public static boolean add(Long key, xbean.TreasureMapInfo value) {
		return _Tables_.getInstance().opentreasuremaptable.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().opentreasuremaptable.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.TreasureMapInfo> getCache() {
		return _Tables_.getInstance().opentreasuremaptable.getCache();
	}

	public static mkdb.TTable<Long, xbean.TreasureMapInfo> getTable() {
		return _Tables_.getInstance().opentreasuremaptable;
	}

	public static xbean.TreasureMapInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TreasureMapInfo, xbean.TreasureMapInfo>() {
			public xbean.TreasureMapInfo get(xbean.TreasureMapInfo v) { return v.toData(); }
		});
	}

	public static Integer selectAwardid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TreasureMapInfo, Integer>() {
				public Integer get(xbean.TreasureMapInfo v) { return v.getAwardid(); }
			});
	}

	public static Integer selectTableindex(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TreasureMapInfo, Integer>() {
				public Integer get(xbean.TreasureMapInfo v) { return v.getTableindex(); }
			});
	}

	public static Integer selectItemid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TreasureMapInfo, Integer>() {
				public Integer get(xbean.TreasureMapInfo v) { return v.getItemid(); }
			});
	}

	public static Integer selectItemkey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.TreasureMapInfo, Integer>() {
				public Integer get(xbean.TreasureMapInfo v) { return v.getItemkey(); }
			});
	}

}
