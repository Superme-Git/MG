package xtable;

// typed table access point
public class Npcfollowroles {
	Npcfollowroles() {
	}

	public static xbean.NpcFollowInfo get(Long key) {
		return _Tables_.getInstance().npcfollowroles.get(key);
	}

	public static xbean.NpcFollowInfo get(Long key, xbean.NpcFollowInfo value) {
		return _Tables_.getInstance().npcfollowroles.get(key, value);
	}

	public static void insert(Long key, xbean.NpcFollowInfo value) {
		_Tables_.getInstance().npcfollowroles.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().npcfollowroles.delete(key);
	}

	public static boolean add(Long key, xbean.NpcFollowInfo value) {
		return _Tables_.getInstance().npcfollowroles.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().npcfollowroles.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.NpcFollowInfo> getCache() {
		return _Tables_.getInstance().npcfollowroles.getCache();
	}

	public static mkdb.TTable<Long, xbean.NpcFollowInfo> getTable() {
		return _Tables_.getInstance().npcfollowroles;
	}

	public static xbean.NpcFollowInfo select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NpcFollowInfo, xbean.NpcFollowInfo>() {
			public xbean.NpcFollowInfo get(xbean.NpcFollowInfo v) { return v.toData(); }
		});
	}

	public static Integer selectNpcid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NpcFollowInfo, Integer>() {
				public Integer get(xbean.NpcFollowInfo v) { return v.getNpcid(); }
			});
	}

	public static Integer selectQuestid(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NpcFollowInfo, Integer>() {
				public Integer get(xbean.NpcFollowInfo v) { return v.getQuestid(); }
			});
	}

	public static Long selectValiddate(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.NpcFollowInfo, Long>() {
				public Long get(xbean.NpcFollowInfo v) { return v.getValiddate(); }
			});
	}

}
