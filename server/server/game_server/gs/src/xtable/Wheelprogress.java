package xtable;

// typed table access point
public class Wheelprogress {
	Wheelprogress() {
	}

	public static xbean.WheelInfos get(Long key) {
		return _Tables_.getInstance().wheelprogress.get(key);
	}

	public static xbean.WheelInfos get(Long key, xbean.WheelInfos value) {
		return _Tables_.getInstance().wheelprogress.get(key, value);
	}

	public static void insert(Long key, xbean.WheelInfos value) {
		_Tables_.getInstance().wheelprogress.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().wheelprogress.delete(key);
	}

	public static boolean add(Long key, xbean.WheelInfos value) {
		return _Tables_.getInstance().wheelprogress.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().wheelprogress.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.WheelInfos> getCache() {
		return _Tables_.getInstance().wheelprogress.getCache();
	}

	public static mkdb.TTable<Long, xbean.WheelInfos> getTable() {
		return _Tables_.getInstance().wheelprogress;
	}

	public static xbean.WheelInfos select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.WheelInfos, xbean.WheelInfos>() {
			public xbean.WheelInfos get(xbean.WheelInfos v) { return v.toData(); }
		});
	}

	public static java.util.List<xbean.WheelInfo> selectWheellist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.WheelInfos, java.util.List<xbean.WheelInfo>>() {
				public java.util.List<xbean.WheelInfo> get(xbean.WheelInfos v) { return v.getWheellistAsData(); }
			});
	}

}
