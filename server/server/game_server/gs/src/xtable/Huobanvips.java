package xtable;

// typed table access point
public class Huobanvips {
	Huobanvips() {
	}

	public static xbean.HuoBanVip get(Integer key) {
		return _Tables_.getInstance().huobanvips.get(key);
	}

	public static xbean.HuoBanVip get(Integer key, xbean.HuoBanVip value) {
		return _Tables_.getInstance().huobanvips.get(key, value);
	}

	public static void insert(Integer key, xbean.HuoBanVip value) {
		_Tables_.getInstance().huobanvips.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().huobanvips.delete(key);
	}

	public static boolean add(Integer key, xbean.HuoBanVip value) {
		return _Tables_.getInstance().huobanvips.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().huobanvips.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.HuoBanVip> getCache() {
		return _Tables_.getInstance().huobanvips.getCache();
	}

	public static mkdb.TTable<Integer, xbean.HuoBanVip> getTable() {
		return _Tables_.getInstance().huobanvips;
	}

	public static xbean.HuoBanVip select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.HuoBanVip, xbean.HuoBanVip>() {
			public xbean.HuoBanVip get(xbean.HuoBanVip v) { return v.toData(); }
		});
	}

	public static java.util.List<Integer> selectHuobans(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.HuoBanVip, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.HuoBanVip v) { return v.getHuobansAsData(); }
			});
	}

}
