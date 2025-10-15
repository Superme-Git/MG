package xtable;

// typed table access point
public class Pettab {
	Pettab() {
	}

	public static mkdb.util.AutoKey<Long> getAutoKey() {
		return _Tables_.getInstance().pettab.getAutoKey();
	}

	public static Long nextKey() {
		return getAutoKey().next();
	}

	public static Long insert(xbean.PetItem value) {
		Long next = nextKey();
		insert(next, value);
		return next;
	}

	public static xbean.PetItem get(Long key) {
		return _Tables_.getInstance().pettab.get(key);
	}

	public static xbean.PetItem get(Long key, xbean.PetItem value) {
		return _Tables_.getInstance().pettab.get(key, value);
	}

	public static void insert(Long key, xbean.PetItem value) {
		_Tables_.getInstance().pettab.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().pettab.delete(key);
	}

	public static boolean add(Long key, xbean.PetItem value) {
		return _Tables_.getInstance().pettab.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().pettab.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.PetItem> getCache() {
		return _Tables_.getInstance().pettab.getCache();
	}

	public static mkdb.TTable<Long, xbean.PetItem> getTable() {
		return _Tables_.getInstance().pettab;
	}

	public static xbean.PetItem select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, xbean.PetItem>() {
			public xbean.PetItem get(xbean.PetItem v) { return v.toData(); }
		});
	}

	public static Long selectId(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Long>() {
				public Long get(xbean.PetItem v) { return v.getId(); }
			});
	}

	public static Integer selectFirstno(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getFirstno(); }
			});
	}

	public static Integer selectTwono(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getTwono(); }
			});
	}

	public static Integer selectThreeno(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getThreeno(); }
			});
	}

	public static Long selectUniquid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Long>() {
				public Long get(xbean.PetItem v) { return v.getUniquid(); }
			});
	}

	public static Integer selectKey(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getKey(); }
			});
	}

	public static Integer selectItemid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getItemid(); }
			});
	}

	public static Long selectExtid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Long>() {
				public Long get(xbean.PetItem v) { return v.getExtid(); }
			});
	}

	public static String selectName(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, String>() {
				public String get(xbean.PetItem v) { return v.getName(); }
			});
	}

	public static Long selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Long>() {
				public Long get(xbean.PetItem v) { return v.getRoleid(); }
			});
	}

	public static Integer selectLevel(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getLevel(); }
			});
	}

	public static Integer selectNumber(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getNumber(); }
			});
	}

	public static Integer selectPrice(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getPrice(); }
			});
	}

	public static Integer selectAttentionnumber(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getAttentionnumber(); }
			});
	}

	public static Long selectShowtime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Long>() {
				public Long get(xbean.PetItem v) { return v.getShowtime(); }
			});
	}

	public static Long selectExpiretime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Long>() {
				public Long get(xbean.PetItem v) { return v.getExpiretime(); }
			});
	}

	public static Integer selectAttack(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getAttack(); }
			});
	}

	public static Integer selectDefend(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getDefend(); }
			});
	}

	public static Integer selectSpeed(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getSpeed(); }
			});
	}

	public static Integer selectMagicattack(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getMagicattack(); }
			});
	}

	public static Integer selectMagicdef(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getMagicdef(); }
			});
	}

	public static Integer selectMaxhp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getMaxhp(); }
			});
	}

	public static Integer selectAttackapt(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getAttackapt(); }
			});
	}

	public static Integer selectDefendapt(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getDefendapt(); }
			});
	}

	public static Integer selectPhyforceapt(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getPhyforceapt(); }
			});
	}

	public static Integer selectMagicapt(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getMagicapt(); }
			});
	}

	public static Integer selectSpeedapt(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getSpeedapt(); }
			});
	}

	public static Integer selectDodgeapt(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getDodgeapt(); }
			});
	}

	public static Float selectGrowrate(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Float>() {
				public Float get(xbean.PetItem v) { return v.getGrowrate(); }
			});
	}

	public static java.util.List<Integer> selectSkills(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, java.util.List<Integer>>() {
				public java.util.List<Integer> get(xbean.PetItem v) { return v.getSkillsAsData(); }
			});
	}

	public static Integer selectSkillnumber(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getSkillnumber(); }
			});
	}

	public static Integer selectPetscore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.PetItem, Integer>() {
				public Integer get(xbean.PetItem v) { return v.getPetscore(); }
			});
	}

}
