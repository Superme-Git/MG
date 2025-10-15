package xtable;

// typed table access point
public class Roleid2worldredpacksendtotal {
	Roleid2worldredpacksendtotal() {
	}

	public static xbean.RedPack get(Long key) {
		return _Tables_.getInstance().roleid2worldredpacksendtotal.get(key);
	}

	public static xbean.RedPack get(Long key, xbean.RedPack value) {
		return _Tables_.getInstance().roleid2worldredpacksendtotal.get(key, value);
	}

	public static void insert(Long key, xbean.RedPack value) {
		_Tables_.getInstance().roleid2worldredpacksendtotal.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleid2worldredpacksendtotal.delete(key);
	}

	public static boolean add(Long key, xbean.RedPack value) {
		return _Tables_.getInstance().roleid2worldredpacksendtotal.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleid2worldredpacksendtotal.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RedPack> getCache() {
		return _Tables_.getInstance().roleid2worldredpacksendtotal.getCache();
	}

	public static mkdb.TTable<Long, xbean.RedPack> getTable() {
		return _Tables_.getInstance().roleid2worldredpacksendtotal;
	}

	public static xbean.RedPack select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RedPack, xbean.RedPack>() {
			public xbean.RedPack get(xbean.RedPack v) { return v.toData(); }
		});
	}

	public static Long selectWorldredpack(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RedPack, Long>() {
				public Long get(xbean.RedPack v) { return v.getWorldredpack(); }
			});
	}

}
