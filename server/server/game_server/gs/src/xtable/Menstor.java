package xtable;

// typed table access point
public class Menstor {
	Menstor() {
	}

	public static xbean.MenstorAndApprent get(Long key) {
		return _Tables_.getInstance().menstor.get(key);
	}

	public static xbean.MenstorAndApprent get(Long key, xbean.MenstorAndApprent value) {
		return _Tables_.getInstance().menstor.get(key, value);
	}

	public static void insert(Long key, xbean.MenstorAndApprent value) {
		_Tables_.getInstance().menstor.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().menstor.delete(key);
	}

	public static boolean add(Long key, xbean.MenstorAndApprent value) {
		return _Tables_.getInstance().menstor.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().menstor.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.MenstorAndApprent> getCache() {
		return _Tables_.getInstance().menstor.getCache();
	}

	public static mkdb.TTable<Long, xbean.MenstorAndApprent> getTable() {
		return _Tables_.getInstance().menstor;
	}

	public static xbean.MenstorAndApprent select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, xbean.MenstorAndApprent>() {
			public xbean.MenstorAndApprent get(xbean.MenstorAndApprent v) { return v.toData(); }
		});
	}

	public static Integer selectFlag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, Integer>() {
				public Integer get(xbean.MenstorAndApprent v) { return v.getFlag(); }
			});
	}

	public static Integer selectMenstortitleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, Integer>() {
				public Integer get(xbean.MenstorAndApprent v) { return v.getMenstortitleid(); }
			});
	}

	public static Long selectFirstbaishitime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, Long>() {
				public Long get(xbean.MenstorAndApprent v) { return v.getFirstbaishitime(); }
			});
	}

	public static Long selectFirstapprentid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, Long>() {
				public Long get(xbean.MenstorAndApprent v) { return v.getFirstapprentid(); }
			});
	}

	public static Long selectShidezhi(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, Long>() {
				public Long get(xbean.MenstorAndApprent v) { return v.getShidezhi(); }
			});
	}

	public static Long selectMenstorid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, Long>() {
				public Long get(xbean.MenstorAndApprent v) { return v.getMenstorid(); }
			});
	}

	public static Long selectBashitime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, Long>() {
				public Long get(xbean.MenstorAndApprent v) { return v.getBashitime(); }
			});
	}

	public static Integer selectBashiflag(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, Integer>() {
				public Integer get(xbean.MenstorAndApprent v) { return v.getBashiflag(); }
			});
	}

	public static Integer selectEqipmentscore(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, Integer>() {
				public Integer get(xbean.MenstorAndApprent v) { return v.getEqipmentscore(); }
			});
	}

	public static java.util.Map<Integer, Integer> selectAchievement(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, java.util.Map<Integer, Integer>>() {
				public java.util.Map<Integer, Integer> get(xbean.MenstorAndApprent v) { return v.getAchievementAsData(); }
			});
	}

	public static Long selectBaishitime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, Long>() {
				public Long get(xbean.MenstorAndApprent v) { return v.getBaishitime(); }
			});
	}

	public static Long selectLastgivefushitime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, Long>() {
				public Long get(xbean.MenstorAndApprent v) { return v.getLastgivefushitime(); }
			});
	}

	public static Long selectFushi(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.MenstorAndApprent, Long>() {
				public Long get(xbean.MenstorAndApprent v) { return v.getFushi(); }
			});
	}

}
