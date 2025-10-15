package xtable;

// typed table access point
public class Pvp1races {
	Pvp1races() {
	}

	public static xbean.PvP1Race get(Integer key) {
		return _Tables_.getInstance().pvp1races.get(key);
	}

	public static xbean.PvP1Race get(Integer key, xbean.PvP1Race value) {
		return _Tables_.getInstance().pvp1races.get(key, value);
	}

	public static void insert(Integer key, xbean.PvP1Race value) {
		_Tables_.getInstance().pvp1races.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().pvp1races.delete(key);
	}

	public static boolean add(Integer key, xbean.PvP1Race value) {
		return _Tables_.getInstance().pvp1races.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().pvp1races.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.PvP1Race> getCache() {
		return _Tables_.getInstance().pvp1races.getCache();
	}

	public static mkdb.TTable<Integer, xbean.PvP1Race> getTable() {
		return _Tables_.getInstance().pvp1races;
	}

	public static xbean.PvP1Race select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Race, xbean.PvP1Race>() {
			public xbean.PvP1Race get(xbean.PvP1Race v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.PvP1RaceRole> selectAllroles(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Race, java.util.List<xbean.PvP1RaceRole>>() {
				public java.util.List<xbean.PvP1RaceRole> get(xbean.PvP1Race v) { return v.getAllrolesAsData(); }
			});
	}

	public static java.util.List<Long> selectAllrolesid(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Race, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.PvP1Race v) { return v.getAllrolesidAsData(); }
			});
	}

	public static java.util.List<xbean.PvP1QueueRole> selectWaitingqueue(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP1Race, java.util.List<xbean.PvP1QueueRole>>() {
				public java.util.List<xbean.PvP1QueueRole> get(xbean.PvP1Race v) { return v.getWaitingqueueAsData(); }
			});
	}

}
