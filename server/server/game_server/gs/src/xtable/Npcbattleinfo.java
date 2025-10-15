package xtable;

// typed table access point
public class Npcbattleinfo {
	Npcbattleinfo() {
	}

	public static xbean.npcBattleInfoCol get(Long key) {
		return _Tables_.getInstance().npcbattleinfo.get(key);
	}

	public static xbean.npcBattleInfoCol get(Long key, xbean.npcBattleInfoCol value) {
		return _Tables_.getInstance().npcbattleinfo.get(key, value);
	}

	public static void insert(Long key, xbean.npcBattleInfoCol value) {
		_Tables_.getInstance().npcbattleinfo.insert(key, value);
	}

	public static void delete(Long key) {
		_Tables_.getInstance().npcbattleinfo.delete(key);
	}

	public static boolean add(Long key, xbean.npcBattleInfoCol value) {
		return _Tables_.getInstance().npcbattleinfo.add(key, value);
	}

	public static boolean remove(Long key) {
		return _Tables_.getInstance().npcbattleinfo.remove(key);
	}

	public static mkdb.TTableCache<Long, xbean.npcBattleInfoCol> getCache() {
		return _Tables_.getInstance().npcbattleinfo.getCache();
	}

	public static mkdb.TTable<Long, xbean.npcBattleInfoCol> getTable() {
		return _Tables_.getInstance().npcbattleinfo;
	}

	public static xbean.npcBattleInfoCol select(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.npcBattleInfoCol, xbean.npcBattleInfoCol>() {
			public xbean.npcBattleInfoCol get(xbean.npcBattleInfoCol v) { return v.toData(); }
		});
	}

	public static java.util.Map<Long, Integer> selectBattleroles(Long key) {
		return getTable().select(key, new mkdb.TField<xbean.npcBattleInfoCol, java.util.Map<Long, Integer>>() {
				public java.util.Map<Long, Integer> get(xbean.npcBattleInfoCol v) { return v.getBattlerolesAsData(); }
			});
	}

}
