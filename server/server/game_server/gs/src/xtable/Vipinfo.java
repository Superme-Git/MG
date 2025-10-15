package xtable;

// typed table access point
public class Vipinfo {
	Vipinfo() {
	}

	public static xbean.Vipinfo get(Long key) {
		return _Tables_.getInstance().vipinfo.get(key);
	}

	public static xbean.Vipinfo get(Long key, xbean.Vipinfo value) {
		return _Tables_.getInstance().vipinfo.get(key, value);
	}

	public static void insert(Long key, xbean.Vipinfo value) {
		_Tables_.getInstance().vipinfo.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().vipinfo.delete(key);
	}

	public static boolean add(Long key, xbean.Vipinfo value) {
		return _Tables_.getInstance().vipinfo.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().vipinfo.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.Vipinfo> getCache() {
		return _Tables_.getInstance().vipinfo.getCache();
	}

	public static mkdb.TTable<Long, xbean.Vipinfo> getTable() {
		return _Tables_.getInstance().vipinfo;
	}

	public static xbean.Vipinfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Vipinfo, xbean.Vipinfo>() {
			public xbean.Vipinfo get(xbean.Vipinfo v) { return v.toData(); }
		});
	}

	public static Integer selectVipexp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Vipinfo, Integer>() {
				public Integer get(xbean.Vipinfo v) { return v.getVipexp(); }
			});
	}

	public static Integer selectViplevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Vipinfo, Integer>() {
				public Integer get(xbean.Vipinfo v) { return v.getViplevel(); }
			});
	}

	public static Integer selectBonus(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Vipinfo, Integer>() {
				public Integer get(xbean.Vipinfo v) { return v.getBonus(); }
			});
	}

	public static Integer selectGotbonus(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Vipinfo, Integer>() {
				public Integer get(xbean.Vipinfo v) { return v.getGotbonus(); }
			});
	}

}
