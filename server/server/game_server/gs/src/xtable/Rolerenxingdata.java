package xtable;

// typed table access point
public class Rolerenxingdata {
	Rolerenxingdata() {
	}

	public static xbean.RenXingData get(Long key) {
		return _Tables_.getInstance().rolerenxingdata.get(key);
	}

	public static xbean.RenXingData get(Long key, xbean.RenXingData value) {
		return _Tables_.getInstance().rolerenxingdata.get(key, value);
	}

	public static void insert(Long key, xbean.RenXingData value) {
		_Tables_.getInstance().rolerenxingdata.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolerenxingdata.delete(key);
	}

	public static boolean add(Long key, xbean.RenXingData value) {
		return _Tables_.getInstance().rolerenxingdata.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolerenxingdata.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.RenXingData> getCache() {
		return _Tables_.getInstance().rolerenxingdata.getCache();
	}

	public static mkdb.TTable<Long, xbean.RenXingData> getTable() {
		return _Tables_.getInstance().rolerenxingdata;
	}

	public static xbean.RenXingData select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RenXingData, xbean.RenXingData>() {
			public xbean.RenXingData get(xbean.RenXingData v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, Integer> selectRenxinmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.RenXingData, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.RenXingData v) { return v.getRenxinmapAsData(); }
			});
	}

}
