package xtable;

// typed table access point
public class Pvp5races {
	Pvp5races() {
	}

	public static xbean.PvP5Race get(Integer key) {
		return _Tables_.getInstance().pvp5races.get(key);
	}

	public static xbean.PvP5Race get(Integer key, xbean.PvP5Race value) {
		return _Tables_.getInstance().pvp5races.get(key, value);
	}

	public static void insert(Integer key, xbean.PvP5Race value) {
		_Tables_.getInstance().pvp5races.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().pvp5races.delete(key);
	}

	public static boolean add(Integer key, xbean.PvP5Race value) {
		return _Tables_.getInstance().pvp5races.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().pvp5races.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.PvP5Race> getCache() {
		return _Tables_.getInstance().pvp5races.getCache();
	}

	public static mkdb.TTable<Integer, xbean.PvP5Race> getTable() {
		return _Tables_.getInstance().pvp5races;
	}

	public static xbean.PvP5Race select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Race, xbean.PvP5Race>() {
			public xbean.PvP5Race get(xbean.PvP5Race v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.PvP5RaceRole> selectAllrolescampa(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Race, java.util.List<xbean.PvP5RaceRole>>() {
				public java.util.List<xbean.PvP5RaceRole> get(xbean.PvP5Race v) { return v.getAllrolescampaAsData(); }
			});
	}

	public static java.util.List<xbean.PvP5RaceRole> selectAllrolescampb(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Race, java.util.List<xbean.PvP5RaceRole>>() {
				public java.util.List<xbean.PvP5RaceRole> get(xbean.PvP5Race v) { return v.getAllrolescampbAsData(); }
			});
	}

	public static java.util.List<Long> selectAllrolesidcampa(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Race, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.PvP5Race v) { return v.getAllrolesidcampaAsData(); }
			});
	}

	public static java.util.List<Long> selectAllrolesidcampb(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Race, java.util.List<Long>>() {
				public java.util.List<Long> get(xbean.PvP5Race v) { return v.getAllrolesidcampbAsData(); }
			});
	}

	public static java.util.List<xbean.PvP5QueueRole> selectWaitingqueue(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Race, java.util.List<xbean.PvP5QueueRole>>() {
				public java.util.List<xbean.PvP5QueueRole> get(xbean.PvP5Race v) { return v.getWaitingqueueAsData(); }
			});
	}

	public static Integer selectIsend(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.PvP5Race, Integer>() {
				public Integer get(xbean.PvP5Race v) { return v.getIsend(); }
			});
	}

}
