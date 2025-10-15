package xtable;

// typed table access point
public class Roleid2weekgiverecegift {
	Roleid2weekgiverecegift() {
	}

	public static xbean.WeekGiveReceGift get(Long key) {
		return _Tables_.getInstance().roleid2weekgiverecegift.get(key);
	}

	public static xbean.WeekGiveReceGift get(Long key, xbean.WeekGiveReceGift value) {
		return _Tables_.getInstance().roleid2weekgiverecegift.get(key, value);
	}

	public static void insert(Long key, xbean.WeekGiveReceGift value) {
		_Tables_.getInstance().roleid2weekgiverecegift.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleid2weekgiverecegift.delete(key);
	}

	public static boolean add(Long key, xbean.WeekGiveReceGift value) {
		return _Tables_.getInstance().roleid2weekgiverecegift.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleid2weekgiverecegift.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.WeekGiveReceGift> getCache() {
		return _Tables_.getInstance().roleid2weekgiverecegift.getCache();
	}

	public static mkdb.TTable<Long, xbean.WeekGiveReceGift> getTable() {
		return _Tables_.getInstance().roleid2weekgiverecegift;
	}

	public static xbean.WeekGiveReceGift select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.WeekGiveReceGift, xbean.WeekGiveReceGift>() {
			public xbean.WeekGiveReceGift get(xbean.WeekGiveReceGift v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, xbean.GiveReceGift> selectWeekdata(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.WeekGiveReceGift, java.util.Map<Long, xbean.GiveReceGift>>() {
				public java.util.Map<Long, xbean.GiveReceGift> get(xbean.WeekGiveReceGift v) { return v.getWeekdataAsData(); }
			});
	}

}
