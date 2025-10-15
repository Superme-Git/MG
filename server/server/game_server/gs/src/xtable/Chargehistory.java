package xtable;

// typed table access point
public class Chargehistory {
	Chargehistory() {
	}

	public static xbean.ChargeHistory get(Integer key) {
		return _Tables_.getInstance().chargehistory.get(key);
	}

	public static xbean.ChargeHistory get(Integer key, xbean.ChargeHistory value) {
		return _Tables_.getInstance().chargehistory.get(key, value);
	}

	public static void insert(Integer key, xbean.ChargeHistory value) {
		_Tables_.getInstance().chargehistory.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().chargehistory.delete(key);
	}

	public static boolean add(Integer key, xbean.ChargeHistory value) {
		return _Tables_.getInstance().chargehistory.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().chargehistory.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ChargeHistory> getCache() {
		return _Tables_.getInstance().chargehistory.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ChargeHistory> getTable() {
		return _Tables_.getInstance().chargehistory;
	}

	public static xbean.ChargeHistory select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeHistory, xbean.ChargeHistory>() {
			public xbean.ChargeHistory get(xbean.ChargeHistory v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, xbean.ChargeOrder> selectCharges(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ChargeHistory, java.util.Map<Long, xbean.ChargeOrder>>() {
				public java.util.Map<Long, xbean.ChargeOrder> get(xbean.ChargeHistory v) { return v.getChargesAsData(); }
			});
	}

}
