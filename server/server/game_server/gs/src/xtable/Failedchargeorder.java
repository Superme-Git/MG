package xtable;

// typed table access point
public class Failedchargeorder {
	Failedchargeorder() {
	}

	public static xbean.ChargeOrder get(Long key) {
		return _Tables_.getInstance().failedchargeorder.get(key);
	}

	public static xbean.ChargeOrder get(Long key, xbean.ChargeOrder value) {
		return _Tables_.getInstance().failedchargeorder.get(key, value);
	}

	public static void insert(Long key, xbean.ChargeOrder value) {
		_Tables_.getInstance().failedchargeorder.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().failedchargeorder.delete(key);
	}

	public static boolean add(Long key, xbean.ChargeOrder value) {
		return _Tables_.getInstance().failedchargeorder.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().failedchargeorder.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ChargeOrder> getCache() {
		return _Tables_.getInstance().failedchargeorder.getCache();
	}

	public static mkdb.TTable<Long, xbean.ChargeOrder> getTable() {
		return _Tables_.getInstance().failedchargeorder;
	}

	public static xbean.ChargeOrder select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, xbean.ChargeOrder>() {
			public xbean.ChargeOrder get(xbean.ChargeOrder v) { return v.toData(); }
		});
	}

	public static Integer selectStatus(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, Integer>() {
				public Integer get(xbean.ChargeOrder v) { return v.getStatus(); }
			});
	}

	public static String selectPlatformsn(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, String>() {
				public String get(xbean.ChargeOrder v) { return v.getPlatformsn(); }
			});
	}

	public static String selectPlattype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, String>() {
				public String get(xbean.ChargeOrder v) { return v.getPlattype(); }
			});
	}

	public static String selectPlatformuid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, String>() {
				public String get(xbean.ChargeOrder v) { return v.getPlatformuid(); }
			});
	}

	public static String selectReceipt(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, String>() {
				public String get(xbean.ChargeOrder v) { return v.getReceipt(); }
			});
	}

	public static String selectTranscationid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, String>() {
				public String get(xbean.ChargeOrder v) { return v.getTranscationid(); }
			});
	}

	public static Long selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, Long>() {
				public Long get(xbean.ChargeOrder v) { return v.getRoleid(); }
			});
	}

	public static Integer selectNum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, Integer>() {
				public Integer get(xbean.ChargeOrder v) { return v.getNum(); }
			});
	}

	public static Integer selectGoodid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, Integer>() {
				public Integer get(xbean.ChargeOrder v) { return v.getGoodid(); }
			});
	}

	public static Integer selectGoodnum(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, Integer>() {
				public Integer get(xbean.ChargeOrder v) { return v.getGoodnum(); }
			});
	}

	public static Long selectCreatetime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, Long>() {
				public Long get(xbean.ChargeOrder v) { return v.getCreatetime(); }
			});
	}

	public static Integer selectUserid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, Integer>() {
				public Integer get(xbean.ChargeOrder v) { return v.getUserid(); }
			});
	}

	public static Integer selectRetrytimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeOrder, Integer>() {
				public Integer get(xbean.ChargeOrder v) { return v.getRetrytimes(); }
			});
	}

}
