package xtable;

// typed table access point
public class Pvp3races {
	Pvp3races() {
	}

	public static xbean.PvP3Race get(Integer key) {
		return _Tables_.getInstance().pvp3races.get(key);
	}

	public static xbean.PvP3Race get(Integer key, xbean.PvP3Race value) {
		return _Tables_.getInstance().pvp3races.get(key, value);
	}

	public static void insert(Integer key, xbean.PvP3Race value) {
		_Tables_.getInstance().pvp3races.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().pvp3races.delete(key);
	}

	public static boolean add(Integer key, xbean.PvP3Race value) {
		return _Tables_.getInstance().pvp3races.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().pvp3races.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.PvP3Race> getCache() {
		return _Tables_.getInstance().pvp3races.getCache();
	}

	public static mkdb.TTable<Integer, xbean.PvP3Race> getTable() {
		return _Tables_.getInstance().pvp3races;
	}

	public static xbean.PvP3Race select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Race, xbean.PvP3Race>() {
			public xbean.PvP3Race get(xbean.PvP3Race v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.PvP3RaceRole> selectAllroles(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Race, java.util.List<xbean.PvP3RaceRole>>() {
				public java.util.List<xbean.PvP3RaceRole> get(xbean.PvP3Race v) { return v.getAllrolesAsData(); }
			});
	}

	public static java.util.List<Long> selectAllrolesid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Race, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.PvP3Race v) { return v.getAllrolesidAsData(); }
			});
	}

	public static java.util.List<xbean.PvP3QueueRole> selectWaitingqueue(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP3Race, java.util.List<xbean.PvP3QueueRole>>() {
				public java.util.List<xbean.PvP3QueueRole> get(xbean.PvP3Race v) { return v.getWaitingqueueAsData(); }
			});
	}

}
