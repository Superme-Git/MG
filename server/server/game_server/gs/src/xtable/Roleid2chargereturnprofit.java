package xtable;

// typed table access point
public class Roleid2chargereturnprofit {
	Roleid2chargereturnprofit() {
	}

	public static xbean.EChargeReturnProfit get(Long key) {
		return _Tables_.getInstance().roleid2chargereturnprofit.get(key);
	}

	public static xbean.EChargeReturnProfit get(Long key, xbean.EChargeReturnProfit value) {
		return _Tables_.getInstance().roleid2chargereturnprofit.get(key, value);
	}

	public static void insert(Long key, xbean.EChargeReturnProfit value) {
		_Tables_.getInstance().roleid2chargereturnprofit.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleid2chargereturnprofit.delete(key);
	}

	public static boolean add(Long key, xbean.EChargeReturnProfit value) {
		return _Tables_.getInstance().roleid2chargereturnprofit.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleid2chargereturnprofit.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.EChargeReturnProfit> getCache() {
		return _Tables_.getInstance().roleid2chargereturnprofit.getCache();
	}

	public static mkdb.TTable<Long, xbean.EChargeReturnProfit> getTable() {
		return _Tables_.getInstance().roleid2chargereturnprofit;
	}

	public static xbean.EChargeReturnProfit select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EChargeReturnProfit, xbean.EChargeReturnProfit>() {
			public xbean.EChargeReturnProfit get(xbean.EChargeReturnProfit v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.ChargeReturnProfit> selectReturnprofitmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EChargeReturnProfit, java.util.Map<Integer, xbean.ChargeReturnProfit>>() {
				public java.util.Map<Integer, xbean.ChargeReturnProfit> get(xbean.EChargeReturnProfit v) { return v.getReturnprofitmapAsData(); }
			});
	}

}
