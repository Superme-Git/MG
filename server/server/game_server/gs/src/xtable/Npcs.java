package xtable;

// typed table access point
public class Npcs {
	Npcs() {
	}

	public static xbean.NpcInfo get(Long key) {
		return _Tables_.getInstance().npcs.get(key);
	}

	public static xbean.NpcInfo get(Long key, xbean.NpcInfo value) {
		return _Tables_.getInstance().npcs.get(key, value);
	}

	public static void insert(Long key, xbean.NpcInfo value) {
		_Tables_.getInstance().npcs.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().npcs.delete(key);
	}

	public static boolean add(Long key, xbean.NpcInfo value) {
		return _Tables_.getInstance().npcs.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().npcs.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.NpcInfo> getCache() {
		return _Tables_.getInstance().npcs.getCache();
	}

	public static mkdb.TTable<Long, xbean.NpcInfo> getTable() {
		return _Tables_.getInstance().npcs;
	}

	public static xbean.NpcInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NpcInfo, xbean.NpcInfo>() {
			public xbean.NpcInfo get(xbean.NpcInfo v) { return v.toData(); }
		});
	}

	public static Integer selectIsinbattle(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NpcInfo, Integer>() {
				public Integer get(xbean.NpcInfo v) { return v.getIsinbattle(); }
			});
	}

}
