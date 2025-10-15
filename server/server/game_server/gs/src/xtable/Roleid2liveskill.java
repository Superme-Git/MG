package xtable;

// typed table access point
public class Roleid2liveskill {
	Roleid2liveskill() {
	}

	public static xbean.ELiveSkill get(Long key) {
		return _Tables_.getInstance().roleid2liveskill.get(key);
	}

	public static xbean.ELiveSkill get(Long key, xbean.ELiveSkill value) {
		return _Tables_.getInstance().roleid2liveskill.get(key, value);
	}

	public static void insert(Long key, xbean.ELiveSkill value) {
		_Tables_.getInstance().roleid2liveskill.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().roleid2liveskill.delete(key);
	}

	public static boolean add(Long key, xbean.ELiveSkill value) {
		return _Tables_.getInstance().roleid2liveskill.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().roleid2liveskill.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.ELiveSkill> getCache() {
		return _Tables_.getInstance().roleid2liveskill.getCache();
	}

	public static mkdb.TTable<Long, xbean.ELiveSkill> getTable() {
		return _Tables_.getInstance().roleid2liveskill;
	}

	public static xbean.ELiveSkill select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ELiveSkill, xbean.ELiveSkill>() {
			public xbean.ELiveSkill get(xbean.ELiveSkill v) { return v.toData(); }
		});
	}

	public static java.util.Map<Integer, xbean.LiveSkill> selectLiveskilllist(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.ELiveSkill, java.util.Map<Integer, xbean.LiveSkill>>() {
				public java.util.Map<Integer, xbean.LiveSkill> get(xbean.ELiveSkill v) { return v.getLiveskilllistAsData(); }
			});
	}

}
