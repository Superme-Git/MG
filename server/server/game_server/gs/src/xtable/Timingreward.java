package xtable;

// typed table access point
public class Timingreward {
	Timingreward() {
	}

	public static xbean.RewardData get(Long key) {
		return _Tables_.getInstance().timingreward.get(key);
	}

	public static xbean.RewardData get(Long key, xbean.RewardData value) {
		return _Tables_.getInstance().timingreward.get(key, value);
	}

	public static void insert(Long key, xbean.RewardData value) {
		_Tables_.getInstance().timingreward.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().timingreward.delete(key);
	}

	public static boolean add(Long key, xbean.RewardData value) {
		return _Tables_.getInstance().timingreward.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().timingreward.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RewardData> getCache() {
		return _Tables_.getInstance().timingreward.getCache();
	}

	public static mkdb.TTable<Long, xbean.RewardData> getTable() {
		return _Tables_.getInstance().timingreward;
	}

	public static xbean.RewardData select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RewardData, xbean.RewardData>() {
			public xbean.RewardData get(xbean.RewardData v) { return v.toData(); }
		});
	}

	public static Integer selectRewardid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RewardData, Integer>() {
				public Integer get(xbean.RewardData v) { return v.getRewardid(); }
			});
	}

	public static Long selectLastrewardtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RewardData, Long>() {
				public Long get(xbean.RewardData v) { return v.getLastrewardtime(); }
			});
	}

	public static Long selectTimewait(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RewardData, Long>() {
				public Long get(xbean.RewardData v) { return v.getTimewait(); }
			});
	}

}
