package xtable;

// typed table access point
public class Huobanproperty {
	Huobanproperty() {
	}

	public static xbean.HuoBanShuxingLevel get(Integer key) {
		return _Tables_.getInstance().huobanproperty.get(key);
	}

	public static xbean.HuoBanShuxingLevel get(Integer key, xbean.HuoBanShuxingLevel value) {
		return _Tables_.getInstance().huobanproperty.get(key, value);
	}

	public static void insert(Integer key, xbean.HuoBanShuxingLevel value) {
		_Tables_.getInstance().huobanproperty.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().huobanproperty.delete(key);
	}

	public static boolean add(Integer key, xbean.HuoBanShuxingLevel value) {
		return _Tables_.getInstance().huobanproperty.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().huobanproperty.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.HuoBanShuxingLevel> getCache() {
		return _Tables_.getInstance().huobanproperty.getCache();
	}

	public static mkdb.TTable<Integer, xbean.HuoBanShuxingLevel> getTable() {
		return _Tables_.getInstance().huobanproperty;
	}

	public static xbean.HuoBanShuxingLevel select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.HuoBanShuxingLevel, xbean.HuoBanShuxingLevel>() {
			public xbean.HuoBanShuxingLevel get(xbean.HuoBanShuxingLevel v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.HuoBanshuxing> selectHuobans(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.HuoBanShuxingLevel, java.util.Map<Integer, xbean.HuoBanshuxing>>() {
				public java.util.Map<Integer, xbean.HuoBanshuxing> get(xbean.HuoBanShuxingLevel v) { return v.getHuobansAsData(); }
			});
	}

}
