package xtable;

// typed table access point
public class Equips {
	Equips() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().equips.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.Equip value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.Equip get(Long key) {
		return _Tables_.getInstance().equips.get(key);
	}

	public static xbean.Equip get(Long key, xbean.Equip value) {
		return _Tables_.getInstance().equips.get(key, value);
	}

	public static void insert(Long key, xbean.Equip value) {
		_Tables_.getInstance().equips.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().equips.delete(key);
	}

	public static boolean add(Long key, xbean.Equip value) {
		return _Tables_.getInstance().equips.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().equips.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.Equip> getCache() {
		return _Tables_.getInstance().equips.getCache();
	}

	public static mkdb.TTable<Long, xbean.Equip> getTable() {
		return _Tables_.getInstance().equips;
	}

	public static xbean.Equip select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, xbean.Equip>() {
			public xbean.Equip get(xbean.Equip v) { return v.toData(); }
		});
	}

	public static Integer selectEquiplevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, Integer>() {
				public Integer get(xbean.Equip v) { return v.getEquiplevel(); }
			});
	}

	public static Integer selectRepairtimes(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, Integer>() {
				public Integer get(xbean.Equip v) { return v.getRepairtimes(); }
			});
	}

	public static Integer selectEndure(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, Integer>() {
				public Integer get(xbean.Equip v) { return v.getEndure(); }
			});
	}

	public static Integer selectCurmaxendure(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, Integer>() {
				public Integer get(xbean.Equip v) { return v.getCurmaxendure(); }
			});
	}

	public static String selectProducer(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, String>() {
				public String get(xbean.Equip v) { return v.getProducer(); }
			});
	}

	public static Integer selectExtendure(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, Integer>() {
				public Integer get(xbean.Equip v) { return v.getExtendure(); }
			});
	}

	public static Integer selectMaxendure(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, Integer>() {
				public Integer get(xbean.Equip v) { return v.getMaxendure(); }
			});
	}

	public static Integer selectSkill(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, Integer>() {
				public Integer get(xbean.Equip v) { return v.getSkill(); }
			});
	}

	public static Integer selectEffect(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, Integer>() {
				public Integer get(xbean.Equip v) { return v.getEffect(); }
			});
	}

	public static Integer selectTreasure(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, Integer>() {
				public Integer get(xbean.Equip v) { return v.getTreasure(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectAttr(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.Equip v) { return v.getAttrAsData(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectAddattr(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.Equip v) { return v.getAddattrAsData(); }
			});
	}

	public static java.util.Map<Integer, xbean.EnhancementData> selectEnhancement(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, java.util.Map<Integer, xbean.EnhancementData>>() {
				public java.util.Map<Integer, xbean.EnhancementData> get(xbean.Equip v) { return v.getEnhancementAsData(); }
			});
	}

	public static Integer selectEquipscore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, Integer>() {
				public Integer get(xbean.Equip v) { return v.getEquipscore(); }
			});
	}

	public static java.util.List<Integer> selectDiamonds(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Equip, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.Equip v) { return v.getDiamondsAsData(); }
			});
	}

}
