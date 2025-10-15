package xtable;

// typed table access point
public class Rolehook {
	Rolehook() {
	}

	public static xbean.HookData get(Long key) {
		return _Tables_.getInstance().rolehook.get(key);
	}

	public static xbean.HookData get(Long key, xbean.HookData value) {
		return _Tables_.getInstance().rolehook.get(key, value);
	}

	public static void insert(Long key, xbean.HookData value) {
		_Tables_.getInstance().rolehook.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().rolehook.delete(key);
	}

	public static boolean add(Long key, xbean.HookData value) {
		return _Tables_.getInstance().rolehook.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().rolehook.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.HookData> getCache() {
		return _Tables_.getInstance().rolehook.getCache();
	}

	public static mkdb.TTable<Long, xbean.HookData> getTable() {
		return _Tables_.getInstance().rolehook;
	}

	public static xbean.HookData select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HookData, xbean.HookData>() {
			public xbean.HookData get(xbean.HookData v) { return v.toData(); }
		});
	}

	public static Short selectCangetdpoint(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HookData, Short>() {
				public Short get(xbean.HookData v) { return v.getCangetdpoint(); }
			});
	}

	public static Short selectGetdpoint(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HookData, Short>() {
				public Short get(xbean.HookData v) { return v.getGetdpoint(); }
			});
	}

	public static Boolean selectIsautobattle(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HookData, Boolean>() {
				public Boolean get(xbean.HookData v) { return v.getIsautobattle(); }
			});
	}

	public static Short selectCharoptype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HookData, Short>() {
				public Short get(xbean.HookData v) { return v.getCharoptype(); }
			});
	}

	public static Integer selectCharopid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HookData, Integer>() {
				public Integer get(xbean.HookData v) { return v.getCharopid(); }
			});
	}

	public static Short selectPetoptype(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HookData, Short>() {
				public Short get(xbean.HookData v) { return v.getPetoptype(); }
			});
	}

	public static Integer selectPetopid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HookData, Integer>() {
				public Integer get(xbean.HookData v) { return v.getPetopid(); }
			});
	}

	public static Long selectOfflineexp(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HookData, Long>() {
				public Long get(xbean.HookData v) { return v.getOfflineexp(); }
			});
	}

	public static Long selectLastgettime(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.HookData, Long>() {
				public Long get(xbean.HookData v) { return v.getLastgettime(); }
			});
	}

}
