package xtable;

// typed table access point
public class Beginnertip {
	Beginnertip() {
	}

	public static xbean.BeginnerTip get(Long key) {
		return _Tables_.getInstance().beginnertip.get(key);
	}

	public static xbean.BeginnerTip get(Long key, xbean.BeginnerTip value) {
		return _Tables_.getInstance().beginnertip.get(key, value);
	}

	public static void insert(Long key, xbean.BeginnerTip value) {
		_Tables_.getInstance().beginnertip.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().beginnertip.delete(key);
	}

	public static boolean add(Long key, xbean.BeginnerTip value) {
		return _Tables_.getInstance().beginnertip.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().beginnertip.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.BeginnerTip> getCache() {
		return _Tables_.getInstance().beginnertip.getCache();
	}

	public static mkdb.TTable<Long, xbean.BeginnerTip> getTable() {
		return _Tables_.getInstance().beginnertip;
	}

	public static xbean.BeginnerTip select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BeginnerTip, xbean.BeginnerTip>() {
			public xbean.BeginnerTip get(xbean.BeginnerTip v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Integer> selectTips(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.BeginnerTip, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.BeginnerTip v) { return v.getTipsAsData(); }
			});
	}

}
