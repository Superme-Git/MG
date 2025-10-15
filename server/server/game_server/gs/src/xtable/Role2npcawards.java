package xtable;

// typed table access point
public class Role2npcawards {
	Role2npcawards() {
	}

	public static xbean.npcawardrecords get(Long key) {
		return _Tables_.getInstance().role2npcawards.get(key);
	}

	public static xbean.npcawardrecords get(Long key, xbean.npcawardrecords value) {
		return _Tables_.getInstance().role2npcawards.get(key, value);
	}

	public static void insert(Long key, xbean.npcawardrecords value) {
		_Tables_.getInstance().role2npcawards.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().role2npcawards.delete(key);
	}

	public static boolean add(Long key, xbean.npcawardrecords value) {
		return _Tables_.getInstance().role2npcawards.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().role2npcawards.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.npcawardrecords> getCache() {
		return _Tables_.getInstance().role2npcawards.getCache();
	}

	public static mkdb.TTable<Long, xbean.npcawardrecords> getTable() {
		return _Tables_.getInstance().role2npcawards;
	}

	public static xbean.npcawardrecords select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.npcawardrecords, xbean.npcawardrecords>() {
			public xbean.npcawardrecords get(xbean.npcawardrecords v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.npcaward> selectRecords(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.npcawardrecords, java.util.Map<Integer, xbean.npcaward>>() {
				public java.util.Map<Integer, xbean.npcaward> get(xbean.npcawardrecords v) { return v.getRecordsAsData(); }
			});
	}

}
