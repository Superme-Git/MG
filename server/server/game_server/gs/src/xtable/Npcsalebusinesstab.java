package xtable;

// typed table access point
public class Npcsalebusinesstab {
	Npcsalebusinesstab() {
	}

	public static xbean.NpcSaleBusiness get(Long key) {
		return _Tables_.getInstance().npcsalebusinesstab.get(key);
	}

	public static xbean.NpcSaleBusiness get(Long key, xbean.NpcSaleBusiness value) {
		return _Tables_.getInstance().npcsalebusinesstab.get(key, value);
	}

	public static void insert(Long key, xbean.NpcSaleBusiness value) {
		_Tables_.getInstance().npcsalebusinesstab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().npcsalebusinesstab.delete(key);
	}

	public static boolean add(Long key, xbean.NpcSaleBusiness value) {
		return _Tables_.getInstance().npcsalebusinesstab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().npcsalebusinesstab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.NpcSaleBusiness> getCache() {
		return _Tables_.getInstance().npcsalebusinesstab.getCache();
	}

	public static mkdb.TTable<Long, xbean.NpcSaleBusiness> getTable() {
		return _Tables_.getInstance().npcsalebusinesstab;
	}

	public static xbean.NpcSaleBusiness select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NpcSaleBusiness, xbean.NpcSaleBusiness>() {
			public xbean.NpcSaleBusiness get(xbean.NpcSaleBusiness v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.GoodsBusiness> selectGoodsmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NpcSaleBusiness, java.util.Map<Integer, xbean.GoodsBusiness>>() {
				public java.util.Map<Integer, xbean.GoodsBusiness> get(xbean.NpcSaleBusiness v) { return v.getGoodsmapAsData(); }
			});
	}

	public static Short selectIsactive(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NpcSaleBusiness, Short>() {
				public Short get(xbean.NpcSaleBusiness v) { return v.getIsactive(); }
			});
	}

	public static Long selectTime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NpcSaleBusiness, Long>() {
				public Long get(xbean.NpcSaleBusiness v) { return v.getTime(); }
			});
	}

}
