package xtable;

// typed table access point
public class Activeusertable {
	Activeusertable() {
	}

	public static xbean.ActiveUserInfo get(Integer key) {
		return _Tables_.getInstance().activeusertable.get(key);
	}

	public static xbean.ActiveUserInfo get(Integer key, xbean.ActiveUserInfo value) {
		return _Tables_.getInstance().activeusertable.get(key, value);
	}

	public static void insert(Integer key, xbean.ActiveUserInfo value) {
		_Tables_.getInstance().activeusertable.insert(key, value);
	}

	public static void delete(Integer key) {
		_Tables_.getInstance().activeusertable.delete(key);
	}

	public static boolean add(Integer key, xbean.ActiveUserInfo value) {
		return _Tables_.getInstance().activeusertable.add(key, value);
	}

	public static boolean remove(Integer key) {
		return _Tables_.getInstance().activeusertable.remove(key);
	}

	public static mkdb.TTableCache<Integer, xbean.ActiveUserInfo> getCache() {
		return _Tables_.getInstance().activeusertable.getCache();
	}

	public static mkdb.TTable<Integer, xbean.ActiveUserInfo> getTable() {
		return _Tables_.getInstance().activeusertable;
	}

	public static xbean.ActiveUserInfo select(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, xbean.ActiveUserInfo>() {
			public xbean.ActiveUserInfo get(xbean.ActiveUserInfo v) { return v.toData(); }
		});
	}

	public static Integer selectFirsttimeenter(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getFirsttimeenter(); }
			});
	}

	public static Integer selectLasttimeenter(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimeenter(); }
			});
	}

	public static Integer selectLasttimeactiveday1(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimeactiveday1(); }
			});
	}

	public static Integer selectLasttimeactiveday2(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimeactiveday2(); }
			});
	}

	public static Integer selectLasttimeactiveweek1(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimeactiveweek1(); }
			});
	}

	public static Integer selectLasttimeactiveweek2(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimeactiveweek2(); }
			});
	}

	public static Integer selectFirsttimeactiveday(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getFirsttimeactiveday(); }
			});
	}

	public static Integer selectFirsttimeactiveweek(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getFirsttimeactiveweek(); }
			});
	}

	public static Integer selectLasttimechargeday1(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimechargeday1(); }
			});
	}

	public static Integer selectLasttimechargeday2(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimechargeday2(); }
			});
	}

	public static Integer selectLasttimechargeweek1(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimechargeweek1(); }
			});
	}

	public static Integer selectLasttimechargeweek2(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimechargeweek2(); }
			});
	}

	public static Integer selectFirsttimechargeday(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getFirsttimechargeday(); }
			});
	}

	public static Integer selectFirsttimechargeweek(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getFirsttimechargeweek(); }
			});
	}

	public static Integer selectLasttimechargeday(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimechargeday(); }
			});
	}

	public static Integer selectLasttimechargedaytotalamount(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimechargedaytotalamount(); }
			});
	}

	public static Integer selectLasttimechargeweek(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimechargeweek(); }
			});
	}

	public static Integer selectLasttimechargeweektotalamount(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimechargeweektotalamount(); }
			});
	}

	public static Integer selectChargetotalamount(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getChargetotalamount(); }
			});
	}

	public static Integer selectChargetotalrmb(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getChargetotalrmb(); }
			});
	}

	public static Long selectChargetotalamountlong(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Long>() {
				public Long get(xbean.ActiveUserInfo v) { return v.getChargetotalamountlong(); }
			});
	}

	public static Long selectChargetotalrmblong(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Long>() {
				public Long get(xbean.ActiveUserInfo v) { return v.getChargetotalrmblong(); }
			});
	}

	public static Integer selectChargetotalrmbuntillastday(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getChargetotalrmbuntillastday(); }
			});
	}

	public static Long selectChargetotalrmbuntillastdaylong(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Long>() {
				public Long get(xbean.ActiveUserInfo v) { return v.getChargetotalrmbuntillastdaylong(); }
			});
	}

	public static Integer selectConvertchargetotalamouttolong(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getConvertchargetotalamouttolong(); }
			});
	}

	public static Long selectFushiconsumetotalamount(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Long>() {
				public Long get(xbean.ActiveUserInfo v) { return v.getFushiconsumetotalamount(); }
			});
	}

	public static Long selectCashfushiconsumetotalamount(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Long>() {
				public Long get(xbean.ActiveUserInfo v) { return v.getCashfushiconsumetotalamount(); }
			});
	}

	public static Integer selectLasttimefushiconsumeday(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimefushiconsumeday(); }
			});
	}

	public static Integer selectLasttimefushiconsumedaytotalamount(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimefushiconsumedaytotalamount(); }
			});
	}

	public static Integer selectLasttimefushiconsumeweek(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimefushiconsumeweek(); }
			});
	}

	public static Integer selectLasttimefushiconsumeweektotalamount(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getLasttimefushiconsumeweektotalamount(); }
			});
	}

	public static Long selectFushibuytotalamount(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Long>() {
				public Long get(xbean.ActiveUserInfo v) { return v.getFushibuytotalamount(); }
			});
	}

	public static Long selectFushiselltotalamount(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Long>() {
				public Long get(xbean.ActiveUserInfo v) { return v.getFushiselltotalamount(); }
			});
	}

	public static Integer selectCashfushi(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getCashfushi(); }
			});
	}

	public static Integer selectBindfushi(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getBindfushi(); }
			});
	}

	public static Integer selectSysfushi(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getSysfushi(); }
			});
	}

	public static Integer selectFushiinplatform(Integer key) {
		return getTable().select(key, new mkdb.TField<xbean.ActiveUserInfo, Integer>() {
				public Integer get(xbean.ActiveUserInfo v) { return v.getFushiinplatform(); }
			});
	}

}
