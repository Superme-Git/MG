package xtable;

// typed table access point
public class Huobanzhenrongs {
	Huobanzhenrongs() {
	}

	public static xbean.HuoBanZhenrong get(Long key) {
		return _Tables_.getInstance().huobanzhenrongs.get(key);
	}

	public static xbean.HuoBanZhenrong get(Long key, xbean.HuoBanZhenrong value) {
		return _Tables_.getInstance().huobanzhenrongs.get(key, value);
	}

	public static void insert(Long key, xbean.HuoBanZhenrong value) {
		_Tables_.getInstance().huobanzhenrongs.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().huobanzhenrongs.delete(key);
	}

	public static boolean add(Long key, xbean.HuoBanZhenrong value) {
		return _Tables_.getInstance().huobanzhenrongs.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().huobanzhenrongs.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.HuoBanZhenrong> getCache() {
		return _Tables_.getInstance().huobanzhenrongs.getCache();
	}

	public static mkdb.TTable<Long, xbean.HuoBanZhenrong> getTable() {
		return _Tables_.getInstance().huobanzhenrongs;
	}

	public static xbean.HuoBanZhenrong select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HuoBanZhenrong, xbean.HuoBanZhenrong>() {
			public xbean.HuoBanZhenrong get(xbean.HuoBanZhenrong v) { return v.toData(); }
		});
	}

	public static Integer selectCurrent(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HuoBanZhenrong, Integer>() {
				public Integer get(xbean.HuoBanZhenrong v) { return v.getCurrent(); }
			});
	}

	public static java.util.Map<Integer, xbean.HuoBanZhenrongInfo> selectZhenrong(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HuoBanZhenrong, java.util.Map<Integer, xbean.HuoBanZhenrongInfo>>() {
				public java.util.Map<Integer, xbean.HuoBanZhenrongInfo> get(xbean.HuoBanZhenrong v) { return v.getZhenrongAsData(); }
			});
	}

}
