package xtable;

// typed table access point
public class Huobancolumns {
	Huobancolumns() {
	}

	public static xbean.HuoBanColumn get(Long key) {
		return _Tables_.getInstance().huobancolumns.get(key);
	}

	public static xbean.HuoBanColumn get(Long key, xbean.HuoBanColumn value) {
		return _Tables_.getInstance().huobancolumns.get(key, value);
	}

	public static void insert(Long key, xbean.HuoBanColumn value) {
		_Tables_.getInstance().huobancolumns.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().huobancolumns.delete(key);
	}

	public static boolean add(Long key, xbean.HuoBanColumn value) {
		return _Tables_.getInstance().huobancolumns.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().huobancolumns.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.HuoBanColumn> getCache() {
		return _Tables_.getInstance().huobancolumns.getCache();
	}

	public static mkdb.TTable<Long, xbean.HuoBanColumn> getTable() {
		return _Tables_.getInstance().huobancolumns;
	}

	public static xbean.HuoBanColumn select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HuoBanColumn, xbean.HuoBanColumn>() {
			public xbean.HuoBanColumn get(xbean.HuoBanColumn v) { return v.toData(); }
		});
	}

	public static java.util.List<Integer> selectFighthuobans(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HuoBanColumn, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.HuoBanColumn v) { return v.getFighthuobansAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.HuoBanInfo> selectHuobans(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HuoBanColumn, java.util.Map<Integer, xbean.HuoBanInfo>>() {
				public java.util.Map<Integer, xbean.HuoBanInfo> get(xbean.HuoBanColumn v) { return v.getHuobansAsData(); }
			});
	}

	public static Integer selectViphuoban(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HuoBanColumn, Integer>() {
				public Integer get(xbean.HuoBanColumn v) { return v.getViphuoban(); }
			});
	}

	public static Integer selectWeek(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HuoBanColumn, Integer>() {
				public Integer get(xbean.HuoBanColumn v) { return v.getWeek(); }
			});
	}

}
