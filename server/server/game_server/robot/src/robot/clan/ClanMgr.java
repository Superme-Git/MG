package robot.clan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import robot.ConfigMgr;
import robot.pos.LogicPos;
import fire.pb.clan.RuneRequestInfo;

/**
 * 公会管理器
 * 
 * @author yt
 */
public class ClanMgr {

	public long timeDelay = 12000;

	// public long resetTime = 0l;
	// 是否首次
	public boolean isFirst = false;

	// 是否有公会
	public volatile boolean isHaveClan = false;

	// // 是否已经进入下层
	// private boolean isNextsaveClan = false;
	//
	// // 是否清除副本
	// private boolean isClearClan = false;
	public Object locks = new Object();

	public Object nextSaveLocks = new Object();

	public Object clearClanLocks = new Object();

	public Object processLocks = new Object();

	public Object battleNpcLocks = new Object();

	java.util.Random random = new java.util.Random();

	int m = 0;

	public int currentClanNum = 0;

	public Object currentClanNumLocks = new Object();

	// public int nextSaveNum = 1;
	//
	// public long nextClearClanNum = 0;
	public static ArrayList<fire.pb.clan.RuneRequestInfo> runerequestinfolist = new java.util.ArrayList<fire.pb.clan.RuneRequestInfo>();

	public final static int LIVE_SKILL_WARRIOR_ID = 1117; // 战士 by changhao

	public final static int LIVE_SKILL_MAGIC_ID = 1517; // 法师 by changhao

	public final static int LIVE_SKILL_PRIEST_ID = 1617; // 牧师 by changhao

	public final static int LIVE_SKILL_DRUID_ID = 1417; // 德鲁伊 by changhao

	public final static int LIVE_SKILL_HUNTER_ID = 1317; // 猎人 by changhao

	public final static int LIVE_SKILL_PALADIN_ID = 1217; // 圣骑士 by changhao

	public final static int LIVE_SKILL_SAMAN_ID = 1717; // 萨满 by changhao

	public final static int LIVE_SKILL_ROUGE_ID = 1817; // 盗贼 by changhao

	public final static int LIVE_SKILL_WARLOCK_ID = 1917; // 术士 by changhao

	public String[] nameArr = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

	public static ClanMgr _instance = new ClanMgr();

	public static ClanMgr getInstance() {
		return _instance;
	}

	// 公会副本中npcid对应服务id
	public static Map<Integer, Integer> npcId2ServiceIdMap = new ConcurrentHashMap<Integer, Integer>();

	public static Map<Long, Long> battleNpcMap = new ConcurrentHashMap<Long, Long>();

	public static Map<Long, Integer> battleNpcNumMap = new ConcurrentHashMap<Long, Integer>();

	static public Vector<LogicPos> randomPos1616 = new Vector<LogicPos>();

	public static int listSize = 0;
	static {
		// 附魔卷轴
		runerequestinfolist.add(new RuneRequestInfo(331201));
		runerequestinfolist.add(new RuneRequestInfo(331203));
		runerequestinfolist.add(new RuneRequestInfo(331205));
		runerequestinfolist.add(new RuneRequestInfo(331207));
		runerequestinfolist.add(new RuneRequestInfo(331209));
		runerequestinfolist.add(new RuneRequestInfo(331202));
		runerequestinfolist.add(new RuneRequestInfo(331204));
		runerequestinfolist.add(new RuneRequestInfo(331206));
		runerequestinfolist.add(new RuneRequestInfo(331208));
		int m = 100355;
		for (int i = 180405; i <= 180433; i++) {
			for (int j = m; j <= 100383;) {
				npcId2ServiceIdMap.put(i, j);
				m++;
				break;
			}
		}
		randomPos1616.add(new LogicPos(21 * 16, 88 * 16));
		randomPos1616.add(new LogicPos(53 * 16, 63 * 16));
		randomPos1616.add(new LogicPos(83 * 16, 24 * 16));
		randomPos1616.add(new LogicPos(121 * 16, 34 * 16));
		randomPos1616.add(new LogicPos(127 * 16, 73 * 16));
		randomPos1616.add(new LogicPos(103 * 16, 101 * 16));
		randomPos1616.add(new LogicPos(75 * 16, 96 * 16));
		randomPos1616.add(new LogicPos(67 * 16, 58 * 16));
		randomPos1616.add(new LogicPos(23 * 16, 96 * 16));
		randomPos1616.add(new LogicPos(21 * 16, 42 * 16));
		listSize = randomPos1616.size();
	}

	private Map<Long, Clan> clans = new HashMap<Long, Clan>();

	// 会长id对应公会id
	private Map<Long, Long> masterId2ClanIdMap = new ConcurrentHashMap<Long, Long>();

	// 会员id对应公会id
	private Map<Long, Long> memberId2ClanIdMap = new ConcurrentHashMap<Long, Long>();

	// 公会对应的所有成员
	public Map<Long, Map<Long, ClanMember>> clanId2ClanMembers = new ConcurrentHashMap<Long, Map<Long, ClanMember>>();

	// 没有公会的成员
	private Map<Long, Long> noClanMemberIdMap = new ConcurrentHashMap<Long, Long>();

	// 公会id对应的公会信息
	public Map<Long, Clan> clanId2ClanMap = new ConcurrentHashMap<Long, Clan>();

	// 公会id对应的请求符文信息
	public Map<Long, Map<Long, java.util.List<fire.pb.clan.RuneRequestInfo>>> clanId2RuneRequestInfoListMap = new ConcurrentHashMap<Long, Map<Long, java.util.List<fire.pb.clan.RuneRequestInfo>>>();

	// 战斗中的角色
	public Map<Long, Long> battleRoleMap = new ConcurrentHashMap<Long, Long>();

	// 会长id对应公会名字
	private Map<Long, String> clanId2NameMap = new ConcurrentHashMap<Long, String>();

	public Object object = new Object();

	public Map<Long, Clan> getClans() {
		return clans;
	}

	public Clan getClan(long roleId) {
		Long getClanId = getClanId(roleId);
		if (getClanId != null)
			return clanId2ClanMap.get(getClanId);
		return null;
	}

	public boolean setClanPro(long roleId, int nextSaveNum, long nextClearClanNum, long resetTime) {
		Long clanId = getClanId(roleId);
		if (clanId != null) {
			Clan clan = clanId2ClanMap.get(clanId);
			if (nextSaveNum != -1)
				clan.nextSaveNum = nextSaveNum;
			if (nextClearClanNum != -1)
				clan.nextClearClanNum = nextClearClanNum;
			if (resetTime != -1)
				clan.resetTime = resetTime;
			return true;
		}
		return false;
	}

	/**
	 * 创建公会成功
	 * 
	 * @param clanId
	 * @param roleId
	 */
	public void createClan(long clanId, long roleId, String name) {
		masterId2ClanIdMap.put(roleId, clanId);
		ClanMember cm = new ClanMember();
		cm.memberId = roleId;
		Map<Long, ClanMember> memid2ClanMember = new ConcurrentHashMap<Long, ClanMember>();
		memid2ClanMember.put(roleId, cm);
		clanId2ClanMembers.put(clanId, memid2ClanMember);
		Clan cl = new Clan();
		cl.masterid = roleId;
		clanId2ClanMap.put(clanId, cl);
		noClanMemberIdMap.remove(roleId);
		clanId2NameMap.put(clanId, name);
	}

	public String getClanName(long clanId) {
		return clanId2NameMap.get(clanId);
	}

	/**
	 * 添加成员
	 * 
	 * @param clanId
	 * @param roleId
	 */
	public void addMem(long clanId, long roleId) {
		if (memberId2ClanIdMap.containsKey(roleId))
			return;
		memberId2ClanIdMap.put(roleId, clanId);
		ClanMember cm = new ClanMember();
		cm.memberId = roleId;
		Map<Long, ClanMember> memId2ClanMemersMap = clanId2ClanMembers.get(clanId);
		if (memId2ClanMemersMap == null) {
			Map<Long, ClanMember> memid2ClanMember = new ConcurrentHashMap<Long, ClanMember>();
			memid2ClanMember.put(roleId, cm);
			clanId2ClanMembers.put(clanId, memid2ClanMember);
		} else {
			memId2ClanMemersMap.put(roleId, cm);
		}
		noClanMemberIdMap.remove(roleId);
	}

	/**
	 * 移除成员
	 * 
	 * @param clanId
	 * @param roleId
	 */
	public void removeMem(Long clanId, long roleId) {
		if (clanId == -1) {
			clanId = masterId2ClanIdMap.get(roleId);
			if (clanId == null)
				clanId = memberId2ClanIdMap.get(roleId);
		}
		if (clanId == null)
			return;
		if (masterId2ClanIdMap.containsKey(roleId))
			masterId2ClanIdMap.remove(roleId);
		memberId2ClanIdMap.remove(roleId);
		Map<Long, ClanMember> memId2ClanMemersMap = clanId2ClanMembers.get(clanId);
		if (memId2ClanMemersMap != null) {
			memId2ClanMemersMap.remove(roleId);
			// 如果这个公会没有人了，把这个公会的整个结构清空
			if (memId2ClanMemersMap.size() == 0) {
				clanId2ClanMembers.remove(clanId);
				clanId2ClanMap.remove(clanId);
			} else {
				Clan cl = clanId2ClanMap.get(clanId);
				// 会长转给副会长
				if ((roleId == cl.masterid) && (cl.vicemasterid != -1)) {
					cl.masterid = cl.vicemasterid;
					cl.vicemasterid = -1;
					masterId2ClanIdMap.put(cl.masterid, clanId);
					memberId2ClanIdMap.remove(cl.masterid);
				}
			}
			noClanMemberIdMap.put(roleId, roleId);
		}
	}

	/**
	 * 改变职务
	 * 
	 * @param clanId
	 * @param roleId
	 * @param position
	 */
	public void changePosition(Long clanId, long roleId, int position) {
		if (clanId == -1) {
			clanId = masterId2ClanIdMap.get(roleId);
			if (clanId == null)
				clanId = memberId2ClanIdMap.get(roleId);
		}
		if (clanId == null)
			return;
		Clan cl = clanId2ClanMap.get(clanId);
		// 转为会长
		if (position == 1) {
			long masterid = cl.masterid;
			if (masterid == roleId)
				return;
			cl.masterid = roleId;
			cl.vicemasterid = -1;
			memberId2ClanIdMap.remove(roleId);
			masterId2ClanIdMap.put(roleId, clanId);
			masterId2ClanIdMap.remove(masterid);
			memberId2ClanIdMap.put(masterid, clanId);
		}
		// 转为副会长
		if (position == 2) {
			cl.vicemasterid = roleId;
		}
	}

	/**
	 * 是否是会长
	 * 
	 * @param roleId
	 */
	public boolean isMaster(long roleId) {
		return masterId2ClanIdMap.containsKey(roleId);
	}

	/**
	 * 是否是会員
	 * 
	 * @param roleId
	 */
	public boolean isMember(long roleId) {
		return memberId2ClanIdMap.containsKey(roleId);
	}

	/**
	 * 是否是会員
	 * 
	 * @param roleId
	 */
	public Long getClanId(long roleId) {
		Long clanId = memberId2ClanIdMap.get(roleId);
		if (clanId == null)
			clanId = masterId2ClanIdMap.get(roleId);
		return clanId;
	}

	/**
	 * 得到随机的成员
	 * 
	 * @param roleId
	 */
	public Long getRandomMem(long roleId) {
		Long clanId = masterId2ClanIdMap.get(roleId);
		// mkdb.Trace.error("**********随机的成员" + roleId + "----" + clanId);
		if (clanId == null)
			return -1l;
		Map<Long, ClanMember> memberMap = clanId2ClanMembers.get(clanId);
		if (memberMap != null && memberMap.size() > 0) {
			Map<Long, ClanMember> memberCopyMap = new ConcurrentHashMap<Long, ClanMember>();
			memberCopyMap.putAll(memberMap);
			memberCopyMap.remove(roleId);
			// mkdb.Trace.error("**********随机的成员memberCopyMap" + roleId + "----"
			// + memberCopyMap);
			if (memberCopyMap.size() > 0)
				return (Long) memberCopyMap.keySet().toArray()[0];
		}
		return -1l;
	}

	/**
	 * 得到随机的公会
	 * 
	 * @param roleId
	 */
	public Long getRandomClan() {
		int size = clanId2ClanMap.size();
		if (size > 0) {
			int index = ClanMgr.getInstance().getRandom(size);
			return (Long) clanId2ClanMap.keySet().toArray()[index];
		}
		return -1l;
	}

	/**
	 * 得到随机的公会
	 * 
	 * @param roleId
	 */
	public Long getRandomClanByCopy() {
		int size = clanId2ClanMap.size();
		if (size > 0) {
			int index = ClanMgr.getInstance().getRandom(size);
			return (Long) clanId2ClanMap.keySet().toArray()[index];
		}
		return -1l;
	}

	/**
	 * 得到随机的没有公会的成员
	 * 
	 * @param roleId
	 */
	public Long getRandomOnlineNotClan() {
		int size = noClanMemberIdMap.size();
		if (size > 0) {
			int index = ClanMgr.getInstance().getRandom(size);
			return (Long) noClanMemberIdMap.keySet().toArray()[index];
		}
		return -1l;
	}

	/**
	 * 得到公会的数量
	 * 
	 * @return
	 */
	public int getClanNum() {
		return clanId2ClanMap.size();
	}

	/**
	 * 是否有公会
	 * 
	 * @return
	 */
	public boolean isHaveClanByRoleId(long roleId) {
		if (masterId2ClanIdMap.containsKey(roleId)) {
			return true;
		}
		if (memberId2ClanIdMap.containsKey(roleId)) {
			return true;
		}
		return false;
	}

	/**
	 * 重新设置
	 * 
	 * @return
	 */
	public void reset(Long roleId) {
		Clan clan = ClanMgr._instance.getClan(roleId);
		clan.nextSaveNum = 1;
		clan.nextClearClanNum++;
	}

	/**
	 * 得到随机值
	 * 
	 * @return
	 */
	public double getRandom() {
		double random = 1.0;
		// 第一个玩家上来后把几率调小点，目的是创建公会
		synchronized (ClanMgr.getInstance().locks) {
			if (!ClanMgr.getInstance().isFirst) {
				ClanMgr.getInstance().isFirst = true;
				random = 0.1;
			}
		}
		return random;
	}

	/**
	 * 得到随机值
	 * 
	 * @return
	 */
	public int getRandom(int arg) {
		return random.nextInt(arg);
	}

	/***
	 * 通过职业ID得到对应的附魔道具ID
	 * 
	 * @param school
	 * @return
	 */
	public int GetEnhanceItemIdBySchool(int school) {
		int liveskillid = GetEnhanceLiveSkillBySchool(school);
		int itemid = ConfigMgr.getInstance().getConfig(fire.pb.skill.SLifeSkill.class).get(liveskillid).enhanceitemid;
		return itemid;
	}

	/***
	 * 通过职业找到对应的生活技能
	 * 
	 * @param school
	 * @return
	 */
	public int GetEnhanceLiveSkillBySchool(int school) {
		int id = 0;
		if (school == 11) {
			id = LIVE_SKILL_WARRIOR_ID;
		} else if (school == 12) {
			id = LIVE_SKILL_PALADIN_ID;
		} else if (school == 13) {
			id = LIVE_SKILL_HUNTER_ID;
		} else if (school == 14) {
			id = LIVE_SKILL_DRUID_ID;
		} else if (school == 15) {
			id = LIVE_SKILL_MAGIC_ID;
		} else if (school == 16) {
			id = LIVE_SKILL_PRIEST_ID;
		} else if (school == 17) {
			id = LIVE_SKILL_SAMAN_ID;
		} else if (school == 18) {
			id = LIVE_SKILL_ROUGE_ID;
		} else if (school == 19) {
			id = LIVE_SKILL_WARLOCK_ID;
		}
		fire.pb.skill.SInbornInfo info = ConfigMgr.getInstance().getConfig(fire.pb.skill.SInbornInfo.class).get(id);
		int liveskillid = info.pointToSkillList.get(1);
		return liveskillid;
	}

	/**
	 * 缓存公会对应的请求符文
	 * 
	 * @param clanId
	 * @param runerequestinfolist
	 */
	public void cacheClanId2runerequestinfolist(Long clanId, long roleId, List<fire.pb.clan.RuneRequestInfo> runerequestinfolist) {
		if (clanId == null)
			clanId = getClanId(roleId);
		if (clanId == null)
			return;
		Map<Long, List<fire.pb.clan.RuneRequestInfo>> oldRunerequestinfoMap = clanId2RuneRequestInfoListMap.get(clanId);
		if (oldRunerequestinfoMap == null) {
			Map<Long, List<fire.pb.clan.RuneRequestInfo>> newRunerequestinfoMap = new ConcurrentHashMap<>();
			newRunerequestinfoMap.put(roleId, runerequestinfolist);
			clanId2RuneRequestInfoListMap.put(clanId, newRunerequestinfoMap);
		} else {
			List<fire.pb.clan.RuneRequestInfo> oldRunerequestinfolist = oldRunerequestinfoMap.get(roleId);
			if (oldRunerequestinfolist == null) {
				oldRunerequestinfoMap.put(roleId, runerequestinfolist);
			} else {
				oldRunerequestinfolist.addAll(runerequestinfolist);
			}
		}
	}

	/**
	 * 根据玩家和道具id移除捐赠的请求
	 * 
	 * @param roleId
	 * @param clanId
	 * @param iteamId
	 */
	public void removeRunerRoleIDAndIteamID(long roleId, long clanId, int iteamId) {
		Map<Long, List<fire.pb.clan.RuneRequestInfo>> oldRunerequestinfoMap = clanId2RuneRequestInfoListMap.get(clanId);
		if (oldRunerequestinfoMap == null) {
			return;
		}
		List<fire.pb.clan.RuneRequestInfo> oldRunerequestinfolist = oldRunerequestinfoMap.get(roleId);
		int index = 0;
		int i = 0;
		for (RuneRequestInfo runeRequestInfo : oldRunerequestinfolist) {
			if (runeRequestInfo.itemid == iteamId) {
				index = i;
			}
			i++;
		}
		oldRunerequestinfolist.remove(index);
	}

	/**
	 * 得到受捐赠的玩家和道具id
	 * 
	 * @param school
	 * @param clanId
	 * @return
	 */
	public List<?> getRunerRoleIDAndIteamID(int school, Long clanId, long roleId) {
		if (clanId == null)
			clanId = getClanId(roleId);
		if (clanId == null)
			return null;
		List<Object> dataList = new ArrayList<>();
		Map<Long, List<fire.pb.clan.RuneRequestInfo>> oldRunerequestinfoMap = clanId2RuneRequestInfoListMap.get(clanId);
		if (oldRunerequestinfoMap == null) {
			return null;
		}
		if (oldRunerequestinfoMap.size() > 0) {
			int iteamId = GetEnhanceItemIdBySchool(school);
			for (Long otherId : oldRunerequestinfoMap.keySet()) {
				List<fire.pb.clan.RuneRequestInfo> oldRunerequestinfolist = oldRunerequestinfoMap.get(otherId);
				for (RuneRequestInfo runeRequestInfo : oldRunerequestinfolist) {
					if (runeRequestInfo.itemid == iteamId) {
						dataList.add(otherId);
						dataList.add(iteamId);
						return dataList;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 得到公会的随机名字
	 * 
	 * @param num
	 * @return
	 */
	public String getRandomName(int num) {
		StringBuffer names = new StringBuffer();
		int size = nameArr.length;
		for (int i = 0; i < num; i++) {
			int index = random.nextInt(size);
			names.append(nameArr[index]);
		}
		return names.toString();
	}

	/**
	 * 添加数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月10日 下午3:11:29
	 * @version 1.0
	 * @param targetId
	 * @return
	 */
	public synchronized boolean addBattleNPC(long targetId, long roleId) {
		if (battleNpcMap.put(targetId, roleId) == null) {
			return true;
		}
		return false;
	}

	/**
	 * 添加数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月10日 下午3:11:29
	 * @version 1.0
	 * @param roleId
	 * @return
	 */
	public synchronized boolean addBattleRole(long targetId, long roleId) {
		if (battleRoleMap.put(targetId, roleId) == null) {
			battleRoleMap.put(roleId, targetId);
			return true;
		}
		return false;
	}

	/**
	 * 移除数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月10日 下午3:11:29
	 * @version 1.0
	 * @param roleId
	 * @return
	 */
	public synchronized void removeBattleRole(long roleId, long targetId) {
		battleRoleMap.remove(roleId);
		battleRoleMap.remove(targetId);
	}

	/**
	 * 得到物理地图id
	 * 
	 * @author yangtao
	 * @dateTime 2016年12月17日 下午1:20:10
	 * @version 1.0
	 * @param sceneId
	 * @return
	 */
	public static int getBaseMapIdBySceneId(long sceneId) {
		int param = (int) (Math.pow(2, 32) - 1);
		Long mapId = sceneId & param;
		return mapId.intValue();
	}
}
