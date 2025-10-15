package xtable;

// typed table access point
public class Subscriptions {
	Subscriptions() {
	}

	public static xbean.subscription get(Long key) {
		return _Tables_.getInstance().subscriptions.get(key);
	}

	public static xbean.subscription get(Long key, xbean.subscription value) {
		return _Tables_.getInstance().subscriptions.get(key, value);
	}

	public static void insert(Long key, xbean.subscription value) {
		_Tables_.getInstance().subscriptions.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().subscriptions.delete(key);
	}

	public static boolean add(Long key, xbean.subscription value) {
		return _Tables_.getInstance().subscriptions.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().subscriptions.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.subscription> getCache() {
		return _Tables_.getInstance().subscriptions.getCache();
	}

	public static mkdb.TTable<Long, xbean.subscription> getTable() {
		return _Tables_.getInstance().subscriptions;
	}

	public static xbean.subscription select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.subscription, xbean.subscription>() {
			public xbean.subscription get(xbean.subscription v) { return v.toData(); }
		});
	}

	public static Long selectExpiretime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.subscription, Long>() {
				public Long get(xbean.subscription v) { return v.getExpiretime(); }
			});
	}

	public static Long selectSubscribetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.subscription, Long>() {
				public Long get(xbean.subscription v) { return v.getSubscribetime(); }
			});
	}

}
