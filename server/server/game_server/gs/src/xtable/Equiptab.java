package xtable;

// typed table access point
public class Equiptab {
	Equiptab() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().equiptab.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.EquipItem value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.EquipItem get(Long key) {
		return _Tables_.getInstance().equiptab.get(key);
	}

	public static xbean.EquipItem get(Long key, xbean.EquipItem value) {
		return _Tables_.getInstance().equiptab.get(key, value);
	}

	public static void insert(Long key, xbean.EquipItem value) {
		_Tables_.getInstance().equiptab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().equiptab.delete(key);
	}

	public static boolean add(Long key, xbean.EquipItem value) {
		return _Tables_.getInstance().equiptab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().equiptab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.EquipItem> getCache() {
		return _Tables_.getInstance().equiptab.getCache();
	}

	public static mkdb.TTable<Long, xbean.EquipItem> getTable() {
		return _Tables_.getInstance().equiptab;
	}

	public static xbean.EquipItem select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, xbean.EquipItem>() {
			public xbean.EquipItem get(xbean.EquipItem v) { return v.toData(); }
		});
	}

	public static Long selectId(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Long>() {
				public Long get(xbean.EquipItem v) { return v.getId(); }
			});
	}

	public static Integer selectFirstno(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getFirstno(); }
			});
	}

	public static Integer selectTwono(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getTwono(); }
			});
	}

	public static Integer selectThreeno(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getThreeno(); }
			});
	}

	public static Long selectUniquid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Long>() {
				public Long get(xbean.EquipItem v) { return v.getUniquid(); }
			});
	}

	public static Integer selectKey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getKey(); }
			});
	}

	public static Integer selectItemid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getItemid(); }
			});
	}

	public static Long selectExtid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Long>() {
				public Long get(xbean.EquipItem v) { return v.getExtid(); }
			});
	}

	public static String selectName(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, String>() {
				public String get(xbean.EquipItem v) { return v.getName(); }
			});
	}

	public static Long selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Long>() {
				public Long get(xbean.EquipItem v) { return v.getRoleid(); }
			});
	}

	public static Integer selectLevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getLevel(); }
			});
	}

	public static Integer selectNumber(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getNumber(); }
			});
	}

	public static Integer selectPrice(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getPrice(); }
			});
	}

	public static Integer selectAttentionnumber(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getAttentionnumber(); }
			});
	}

	public static Long selectShowtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Long>() {
				public Long get(xbean.EquipItem v) { return v.getShowtime(); }
			});
	}

	public static Long selectExpiretime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Long>() {
				public Long get(xbean.EquipItem v) { return v.getExpiretime(); }
			});
	}

	public static Integer selectEffect(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getEffect(); }
			});
	}

	public static Integer selectSkill(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getSkill(); }
			});
	}

	public static Integer selectColor(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getColor(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectAttr(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.EquipItem v) { return v.getAttrAsData(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectAddattr(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.EquipItem v) { return v.getAddattrAsData(); }
			});
	}

	public static Integer selectTotalattr(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getTotalattr(); }
			});
	}

	public static Integer selectEquipscore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.EquipItem, Integer>() {
				public Integer get(xbean.EquipItem v) { return v.getEquipscore(); }
			});
	}

}
