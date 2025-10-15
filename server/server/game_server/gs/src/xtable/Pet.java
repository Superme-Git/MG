package xtable;

// typed table access point
public class Pet {
	Pet() {
	}

	public static xbean.Pets get(Long key) {
		return _Tables_.getInstance().pet.get(key);
	}

	public static xbean.Pets get(Long key, xbean.Pets value) {
		return _Tables_.getInstance().pet.get(key, value);
	}

	public static void insert(Long key, xbean.Pets value) {
		_Tables_.getInstance().pet.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().pet.delete(key);
	}

	public static boolean add(Long key, xbean.Pets value) {
		return _Tables_.getInstance().pet.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().pet.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.Pets> getCache() {
		return _Tables_.getInstance().pet.getCache();
	}

	public static mkdb.TTable<Long, xbean.Pets> getTable() {
		return _Tables_.getInstance().pet;
	}

	public static xbean.Pets select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Pets, xbean.Pets>() {
			public xbean.Pets get(xbean.Pets v) { return v.toData(); }
		});
	}

	public static Integer selectCapacity(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Pets, Integer>() {
				public Integer get(xbean.Pets v) { return v.getCapacity(); }
			});
	}

	public static Integer selectNextid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Pets, Integer>() {
				public Integer get(xbean.Pets v) { return v.getNextid(); }
			});
	}

	public static java.util.Map<Integer, xbean.PetInfo> selectPetmap(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.Pets, java.util.Map<Integer, xbean.PetInfo>>() {
				public java.util.Map<Integer, xbean.PetInfo> get(xbean.Pets v) { return v.getPetmapAsData(); }
			});
	}

}
