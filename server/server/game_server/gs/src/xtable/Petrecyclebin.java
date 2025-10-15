package xtable;

// typed table access point
public class Petrecyclebin {
	Petrecyclebin() {
	}

	public static xbean.DiscardPet get(Long key) {
		return _Tables_.getInstance().petrecyclebin.get(key);
	}

	public static xbean.DiscardPet get(Long key, xbean.DiscardPet value) {
		return _Tables_.getInstance().petrecyclebin.get(key, value);
	}

	public static void insert(Long key, xbean.DiscardPet value) {
		_Tables_.getInstance().petrecyclebin.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().petrecyclebin.delete(key);
	}

	public static boolean add(Long key, xbean.DiscardPet value) {
		return _Tables_.getInstance().petrecyclebin.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().petrecyclebin.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.DiscardPet> getCache() {
		return _Tables_.getInstance().petrecyclebin.getCache();
	}

	public static mkdb.TTable<Long, xbean.DiscardPet> getTable() {
		return _Tables_.getInstance().petrecyclebin;
	}

	public static xbean.DiscardPet select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.DiscardPet, xbean.DiscardPet>() {
			public xbean.DiscardPet get(xbean.DiscardPet v) { return v.toData(); }
		});
	}

	public static xbean.PetInfo selectPet(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.DiscardPet, xbean.PetInfo>() {
				public xbean.PetInfo get(xbean.DiscardPet v) { return v.getPet(); }
			});
	}

	public static Long selectRoleid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.DiscardPet, Long>() {
				public Long get(xbean.DiscardPet v) { return v.getRoleid(); }
			});
	}

	public static Long selectDeletedate(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.DiscardPet, Long>() {
				public Long get(xbean.DiscardPet v) { return v.getDeletedate(); }
			});
	}

	public static Integer selectReason(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.DiscardPet, Integer>() {
				public Integer get(xbean.DiscardPet v) { return v.getReason(); }
			});
	}

}
