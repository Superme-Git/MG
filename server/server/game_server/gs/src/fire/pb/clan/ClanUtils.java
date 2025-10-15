package fire.pb.clan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.log.Logger;
import fire.pb.KeyCounterIndex;
import fire.pb.PropRole;
import fire.pb.clan.ClanMember;
import fire.pb.clan.ClanPositionType;
import fire.pb.clan.ClanSummaryInfo;
import fire.pb.clan.RoleBaseInfo;
import fire.pb.clan.SClanHotel;
import fire.pb.clan.SClanPosition;
import fire.pb.clan.srv.ClanManage;
import fire.pb.common.SCommon;
import fire.pb.instance.SInstaceConfig;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.faction.FactionInstZone;
import fire.pb.main.ConfigManager;
import fire.pb.map.DuplicateHelper;
import fire.pb.map.MapConfig;
import fire.pb.map.Transfer;
import fire.pb.mission.MissionColumn;
import fire.pb.mission.RoleMission;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;
import fire.pb.shop.srv.market.jdbc.Page;
import fire.pb.talk.MessageMgr;
import fire.pb.team.PAbsentReturnTeam;
import fire.pb.team.Team;
import fire.pb.title.STitleConfig;
import fire.pb.title.Title;
import fire.pb.util.StringConstant;
import mkdb.Procedure;
import mkdb.Transaction;

public class ClanUtils {
	public static final Logger logger = Logger.getLogger("CLAN");
	
	
	public static int MAPID = 1613;//公会地图
	public static final int[] GOHOME_MAP={1615,100,90};
	
	public static Map<Integer, SCommon> commonMap = ConfigManager.getInstance().getConf(SCommon.class);


	public static final int CLANINDEX_KEY = 1;
	
	public static final long CLAN_INST_NPC = 161303;
	public static final int CLAN_INST_SERVICE_1 = 900015;
	public static final int CLAN_INST_SERVICE_2 = 900016;
	
	public static final int PAGE_SIZE = 10;// 页面数量

	// 帮派职务名称
	public static Map<Integer, SClanPosition> FACTION_POSITION_NAME = ConfigManager.getInstance().getConf(SClanPosition.class);

	public static boolean addClanIndex(xbean.ClanInfo info) {
		if (info == null)
			return false;
		Integer counter = xtable.Tablekeynum.get(KeyCounterIndex.FACTION_KEY);
		if (null == counter) {
			xtable.Tablekeynum.insert(KeyCounterIndex.FACTION_KEY, new Integer(1));
			counter = xtable.Tablekeynum.get(KeyCounterIndex.FACTION_KEY);
		}
		info.setIndex(counter);
		xbean.ClanIndex clanIndex = xtable.Clanindex.get(CLANINDEX_KEY);
		if (clanIndex == null) {
			clanIndex = xbean.Pod.newClanIndex();
			xtable.Clanindex.insert(CLANINDEX_KEY, clanIndex);
		}
		clanIndex.getIndex().put(counter, info.getKey());

		xtable.Tablekeynum.remove(KeyCounterIndex.FACTION_KEY);
		xtable.Tablekeynum.insert(KeyCounterIndex.FACTION_KEY, counter + 1);

		return true;
	}

	/**
	 * 根据职位添加称号
	 * @param roleId
	 * @param newPosition
	 */
	public static void addClanTitleByPosition(long roleId, String clanname,int newPosition) {
		Map<Integer, STitleConfig> titiledatamap = ConfigManager.getInstance().getConf(fire.pb.title.STitleConfig.class);
//		xbean.ClanInfo clanInfo = getClanInfoById(roleId, true);
//		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleId)){
//			return;
//		}
		if (validatePosition(newPosition)) {
			Title title = new Title(roleId, false);
			String titleName = "";
			int new_titleid = getTitleIdByPosition(newPosition);
			STitleConfig titledata = titiledatamap.get(new_titleid);
			if (titledata != null) {
				titleName = clanname+StringConstant.ID2String("的", StringConstant.的)+titledata.titlename;
				if (!title.roleHaveTitle(new_titleid)) {
					title.addTitle(new_titleid, titleName, titledata.availtime);
				}
				if (title.roleHaveTitle(new_titleid)) {
					xbean.Properties prop = xtable.Properties.get(roleId);
					xbean.TitleInfo value = prop.getTitles().get(new_titleid);
					if (!titleName.equals(value.getTitlename())) {
						title.removeTitle(new_titleid);
						title.addTitle(new_titleid, titleName, titledata.availtime);
					}
				}
			}
		}
	}
	
	/**
	 * 在存储过程中执行
	 * @param roleId
	 * @param clanname
	 * @param newPosition
	 * @return
	 */
	public static mkdb.Procedure addClanTitleByPositionProc(long roleId,String clanname, int newPosition){
		return new mkdb.Procedure(){
			@Override
			protected boolean process() {
				addClanTitleByPosition(roleId,clanname,newPosition);
				return true;
			}
		};
	}
	
	
	
	

	/**
	 * 将公会添加到缓存
	 */
	public static void addClanToCache(xbean.ClanInfo v) {
		Module.clanListMap.put(v.getIndex(), v);
		//如果这个公会有人弹劾了
		xbean.ImpeachMent impeachMent = v.getImpeachment();
		if(impeachMent!=null){
			if(impeachMent.getImpeachroleid()>0){
				PRequestImpeachMent.impeachmentclans.put(v.getKey(), impeachMent.getImpeachtime());
			}
		}
	}

	public static void broadcastClanMsg(Collection<Long> roleids, int msgid, List<String> paras) {
		MessageMgr.psendMsgNotifyWhileCommit(roleids, msgid, 0, paras);
	}

	public static void changeClanTitleByPosition(long roleId, int oldPosition, int newPosition) {
		xbean.ClanInfo clanInfo = getClanInfoById(roleId, true);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleId))
			return;
		removeClanTitleByPosition(roleId, oldPosition);
		addClanTitleByPosition(roleId,clanInfo.getClanname(), newPosition);
	}

	/**
	 * 进入公会地图
	 * @param roleid
	 * @return
	 */
	public static boolean enterClanMap(final long roleid) {
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (clanInfo == null) {
			MessageMgr.sendMsgNotify(roleid, 145077, null);
			return false;
		}
		
		final Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
		boolean allow = true;
		if (team != null) {
			if (team.isTeamLeader(roleid)) {
				for (long id : team.getAllMemberIds()) {
					xbean.ClanInfo memClan = ClanUtils.getClanInfoById(id, true);
					if (memClan == null || clanInfo.getKey() != memClan.getKey()) {
						if (!team.isNormalMember(id))
							continue;
						allow = false;
						MessageMgr.sendMsgNotify(roleid, 140657, null);
						break;
					}
				}
			}
			else {
				if (team.isNormalMember(roleid) || team.isReturnMember(roleid)) {
					// 组队状态下队员正常状态无法进行传送操作
					MessageMgr.sendMsgNotify(roleid, 160180, null);
					return false;
				}
			}
		}
		
		if (!allow) {
			return false;
		}

		if (!clanInfo.getMembers().containsKey(roleid))
			return false;
		
		final fire.pb.map.Role maprole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleid);
		if (null == maprole)
			return false;
		if (MAPID == (int) maprole.getScene()) {// 已经在副本里
			return false;
		} else {
			// 进入副本地图
			MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(MAPID);
			int x = 22;
			int y = 30;
			if (cfg != null) {
				x = cfg.xjPos;
				y = cfg.yjPos;
			}
			DuplicateHelper.enterDynamicSceneWhileCommit(roleid, MAPID, x, y, clanInfo.getKey(),
					/*factionInfo.getFactionname()*/"", true, DynamicSceneType.NORMAL_SCENE,
					SRoleEnterScene.ENTER_COMMON_SCENCE);
		}
		return true;
	}

	/**
	 * 进入公会地图
	 * @param roleid
	 * @param xpos x坐标
	 * @param ypos y坐标
	 * @return
	 */
	public static boolean enterClanMap(final long roleid, final int xpos, final int ypos) {
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (clanInfo == null) {
			MessageMgr.sendMsgNotify(roleid, 145077, null);
			return false;
		}
		
		final Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
		boolean allow = true;
		if (team != null) {
			if (team.isTeamLeader(roleid)) {
				for (long id : team.getAllMemberIds()) {
					xbean.ClanInfo memClan = ClanUtils.getClanInfoById(id, true);
					if (memClan == null || clanInfo.getKey() != memClan.getKey()) {
						if (!team.isNormalMember(id))
							continue;
						allow = false;
						MessageMgr.sendMsgNotify(roleid, 140657, null);
						break;
					}
				}
			}
			else {
				if (team.isNormalMember(roleid) || team.isReturnMember(roleid)) {
					// 组队状态下队员正常状态无法进行传送操作
					MessageMgr.sendMsgNotify(roleid, 160180, null);
					return false;
				}
			}
		}
		
		if (!allow) {
			return false;
		}

		if (!clanInfo.getMembers().containsKey(roleid))
			return false;
		
		final fire.pb.map.Role maprole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleid);
		if (null == maprole)
			return false;
		// 进入副本地图
		DuplicateHelper.enterDynamicSceneWhileCommit(roleid, MAPID, xpos, ypos, clanInfo.getKey(),
				"", true, DynamicSceneType.NORMAL_SCENE,
				SRoleEnterScene.ENTER_COMMON_SCENCE);
		return true;
	}

	/**
	 * 获得建筑升级需要的最大的保底资金
	 * @param clanInfo
	 * @return
	 */
	public static java.util.Map<Integer, Integer> getBuildLevelupMax(xbean.ClanInfo clanInfo){
		//升级费用+[(当前建筑等级+1)的维护费用+当前等级其他建筑维护费用]*3
		HashMap<Integer, Integer> map =new HashMap<Integer, Integer>();
		int level_lobby=clanInfo.getClanlevel();
		int level_bank=clanInfo.getHouse().get(ClanManage.GoldBank);
		int level_hotel=clanInfo.getHouse().get(ClanManage.Hotel);
		int level_drugstone=clanInfo.getHouse().get(ClanManage.DrugStone);
		fire.pb.clan.SClanLobby lobby =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanLobby.class).get(level_lobby);//大厅
		fire.pb.clan.SClanGoldBank bank =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanGoldBank.class).get(level_bank);//金库
		fire.pb.clan.SClanHotel hotel =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanHotel.class).get(level_hotel);//旅馆
		fire.pb.clan.SClanDrugstore drugstone =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanDrugstore.class).get(level_drugstone);//药房
		if(lobby==null||bank==null||hotel==null||drugstone==null){
			map.put(ClanManage.Lobby, 0);
			map.put(ClanManage.GoldBank, 0);
			map.put(ClanManage.Hotel, 0);
			map.put(ClanManage.DrugStone, 0);
		}else{
			fire.pb.clan.SClanLobby lobby2 =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanLobby.class).get(level_lobby+1);//大厅
			fire.pb.clan.SClanGoldBank bank2 =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanGoldBank.class).get(level_bank+1);//金库
			fire.pb.clan.SClanHotel hotel2 =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanHotel.class).get(level_hotel+1);//旅馆
			fire.pb.clan.SClanDrugstore drugstone2 =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanDrugstore.class).get(level_drugstone+1);//药房
			int next_cost_lobby=lobby.getCosteveryday();;
			int next_cost_bank=bank.getCosteveryday();
			int next_cost_hotel=hotel.getCosteveryday();
			int next_cost_drugstone=drugstone.getCosteveryday();
			if(lobby2!=null){
				next_cost_lobby=lobby2.getCosteveryday();
			}
			if(bank2!=null){
				next_cost_bank=bank2.getCosteveryday();
			}
			if(hotel2!=null){
				next_cost_hotel=hotel2.getCosteveryday();
			}
			if(drugstone2!=null){
				next_cost_drugstone=drugstone2.getCosteveryday();
			}
			
			int value_lobby=lobby.getLevelupcost()+(next_cost_lobby+bank.getCosteveryday()+hotel.getCosteveryday()+drugstone.getCosteveryday())*3;//大厅
			int value_bank=bank.getLevelupcost()+(next_cost_bank+lobby.getCosteveryday()+hotel.getCosteveryday()+drugstone.getCosteveryday())*3;//金库
			int value_hotel=hotel.getLevelupcost()+(next_cost_hotel+bank.getCosteveryday()+lobby.getCosteveryday()+drugstone.getCosteveryday())*3;//旅馆
			int value_drugstone=drugstone.getLevelupcost()+(next_cost_drugstone+bank.getCosteveryday()+hotel.getCosteveryday()+lobby.getCosteveryday())*3;//药房
			map.put(ClanManage.Lobby, value_lobby);
			map.put(ClanManage.GoldBank, value_bank);
			map.put(ClanManage.Hotel, value_hotel);
			map.put(ClanManage.DrugStone, value_drugstone);
		}
		return map;
	}

	/**
	 * 通过id获得公会
	 * @param roleId
	 * @return
	 */
	public static long getClanIdByRoleId(long roleId) {
		Long clanId = xtable.Roleidclan.select(roleId);
		if (clanId != null && clanId > 0) {
			return clanId.longValue();
		} else
			return 0L;
	}

	public static xbean.ClanInfo getClanInfoById(long roleId, boolean readOnly) {
		Long clanId = xtable.Roleidclan.select(roleId);
		xbean.ClanInfo clanInfo = null;
		if (clanId != null && clanId > 0) {
			if (readOnly) {
				clanInfo = xtable.Clans.select(clanId);
			} else {
				clanInfo = xtable.Clans.get(clanId);
			}
		}
		if (clanInfo != null && clanInfo.getMembers().containsKey(roleId)) {
			return clanInfo;
		}
		return null;
	}

	
	public static xbean.ClanInfo getClanInfoByIndex(Integer clanindex) {
		xbean.ClanIndex indexAndKey = xtable.Clanindex.select(CLANINDEX_KEY);
		if (indexAndKey == null || indexAndKey.getIndex().isEmpty()) {
			return null;
		}
		Map<Integer, Long> indexMap = indexAndKey.getIndex();
		Long clanKey = indexMap.get(clanindex);
		if (clanKey == null) 
			return null;
		return xtable.Clans.select(clanKey);
	}

	public static long getClanInstById(final long factionId, final int instid) {
		xbean.ClanInfo clanInfo = null;
		if (factionId > 0) {
			clanInfo = xtable.Clans.select(factionId);
		}
		if (clanInfo != null) {
			xbean.ClanInstances factionInst = xtable.Claninstances.select(clanInfo.getKey());
			if (factionInst != null) {
				if (factionInst.getInstkey().containsKey(instid))
					return factionInst.getInstkey().get(instid);
				else
					return 0L;
			}
		}
		return 0L;
	}
	
	public static HashMap<Integer, Integer> getClanInstOpen(xbean.ClanInfo clanInfo) {
		HashMap<Integer, Integer> claninst = new HashMap<Integer, Integer>();
		
		Integer claninstservice = clanInfo.getClaninstservice();
		if (claninstservice == 0) {
			claninst.put(CLAN_INST_SERVICE_1, 1);
		} else {
			claninst.put(claninstservice, 1);
		}
		
		Map<Integer, SInstaceConfig> sInstaceConfig = ConfigManager.getInstance().getConf(SInstaceConfig.class);
		xbean.ClanInstances clanInst = xtable.Claninstances.select(clanInfo.getKey());
		if (clanInst != null) {
			for (SInstaceConfig configOne : sInstaceConfig.values()) {
				int instid = configOne.id;
				int serviceid = configOne.getServiceid();
				if (serviceid == 0 || claninst.containsKey(serviceid)) {
					continue;
				}
				
				//如果该副本没有打过或者没有打通关
				if (clanInst.getInststate().get(instid) == null || clanInst.getInststate().get(instid) != 1) {
					//获取该副本的前置副本id
					int beforeInstid = configOne.getAfterid();
					if (beforeInstid == 0) {
						//如果没有前置副本,直接返回该服务
						claninst.put(serviceid, 0);
						continue;
					}
					
					//如果前置副本没有打过,继续判断下一个副本
					if (clanInst.getInststate().get(beforeInstid) == null)
						continue;
					//如果前置副本没有通关,继续判断下一个副本
					if (clanInst.getInststate().get(beforeInstid) != 1)
						continue;
				}
				//把该副本的服务返回给客户端
				claninst.put(serviceid, 0);
			}
		}
		return claninst;
	}
	
	/*
	 * 公会副本npc的动态服务
	 */

	public static void addDynamicServices(long roleid, long npckey, int npcid,  ArrayList<Integer> result) {
		if (npcid != CLAN_INST_NPC) {
			return;
		}
		
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (clanInfo == null)
			return;
		
		Integer claninstservice = clanInfo.getClaninstservice();
		if (claninstservice == 0)
			result.add(CLAN_INST_SERVICE_1);
		else 
			result.add(claninstservice);
		
	}

	/**
	 * @return 获取公会地图id,获取失败返回-1
	 */
	public static int getClanMapId() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(181);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		logger.error("获取公会地图id常量错误");
		return -1;
	}

	/**
	 * 获得当前公会对应职位人数
	 * @param claninfo
	 * @param position
	 * @return
	 */
	public static int getClanPosition(xbean.ClanInfo claninfo,int position){
		int value=0;
		java.util.Map<Long, xbean.ClanMemberInfo> membermap=claninfo.getMembersAsData();
		if(membermap!=null){
			for(xbean.ClanMemberInfo member:membermap.values()){
				if(member!=null&&member.getClanposition()==position){
					value++;
				}
			}
		}
		
		
		return value;
	}

	/**
	 * 获得大厅维护需要的资金值是四个等级建筑加起来的值
	 * @param clanInfo
	 * @return
	 */
	public static int getCostClanDTMoney(xbean.ClanInfo clanInfo){
		//这里需要处理如果其他建筑等级比大厅高，需要用大厅等级去处理
		int level_lobby=clanInfo.getClanlevel();
		int level_bank=clanInfo.getHouse().get(ClanManage.GoldBank);
		if(level_bank>level_lobby){
			level_bank=level_lobby;
		}
		int level_hotel=clanInfo.getHouse().get(ClanManage.Hotel);
		if(level_hotel>level_lobby){
			level_hotel=level_lobby;
		}
		int level_drugstone=clanInfo.getHouse().get(ClanManage.DrugStone);
		if(level_drugstone>level_lobby){
			level_drugstone=level_lobby;
		}
		fire.pb.clan.SClanLobby lobby =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanLobby.class).get(level_lobby);//大厅
		fire.pb.clan.SClanGoldBank bank =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanGoldBank.class).get(level_bank);//金库
		fire.pb.clan.SClanHotel hotel =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanHotel.class).get(level_hotel);//旅馆
		fire.pb.clan.SClanDrugstore drugstone =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.clan.SClanDrugstore.class).get(level_drugstone);//药房
		if(lobby==null||bank==null||hotel==null||drugstone==null){
			return 0;
		}
		return (lobby.getCosteveryday()+bank.getCosteveryday()+hotel.getCosteveryday()+drugstone.getCosteveryday());
	}
	
	public static int getCurrentClanPoint(long roleid) {
		if (roleid < 0)
			return 0;
		xbean.RoleClanPoint rc = xtable.Roleclanpoint.select(roleid);
		return rc == null ? 0 : rc.getCurrentclanpoint();
	}
	
	public static int getHistoryContribution(long roleid) {
		if (roleid < 0)
			return 0;
		xbean.RoleClanPoint rc = xtable.Roleclanpoint.select(roleid);
		return rc == null ? 0 : rc.getHistoryclanpoint();
	}
	
	/**
	 * 获得公会列表 翻页方式返回
	 */
	public static List<ClanSummaryInfo> getPageFactionList(int currPage) {
		List<ClanSummaryInfo> list = new ArrayList<>();
		List<Integer> dellist=new ArrayList<Integer>();
		
		List<xbean.ClanInfo> ClanInfolist=new ArrayList<xbean.ClanInfo>();
		ClanInfolist.addAll(Module.clanListMap.values());
		
		for (xbean.ClanInfo n : ClanInfolist) {
			xbean.ClanInfo v = xtable.Clans.select(n.getKey());
			if (v == null) {
				dellist.add(n.getIndex());
				continue;
			}
			list.add(summaryClanInfo(v));
		}
		
		//删除没有的
		for(int delindex:dellist){
			Module.clanListMap.remove(delindex);
		}
		
		int totalRow = list.size();
		if (totalRow == 0) {
			return null;
		}
		Page page = new Page(totalRow, currPage, PAGE_SIZE);
		if (currPage > page.getTotalPage()) {
			return null;
		}
		int fromIndex = page.getBeginIndex(), toIndex = page.getEndIndex();
		return list.subList(fromIndex, toIndex);
	}
	
	
	public static xbean.RoleClanPoint getRoleClanPoint(long roleid, boolean readOnly) {
		xbean.RoleClanPoint roleCon = null;
		if (readOnly) {
			roleCon = xtable.Roleclanpoint.select(roleid);
		} else {
			roleCon = xtable.Roleclanpoint.get(roleid);
		}
		if (roleCon == null) {
			newRoleClanPoint(roleid);
			return getRoleClanPoint(roleid, readOnly);
		}
		return roleCon;
	}
	
	public static int getTitleIdByPosition(int position) {
		if (!validatePosition(position)) {
			return -1;
		}
		if (position == ClanPositionType.ClanViceMaster) {
			return Constant.FACTION_VICE_MASTER_TITLE;
		}
		if (position == ClanPositionType.ClanMaster) {
			return Constant.FACTION_MASTER_TITLE;
		}
		if (position == ClanPositionType.ClanArmyGroup1) {
			return Constant.FACTION_ONE_ARMY_GROUP_MASTER_TITLE;
		}
		if (position == ClanPositionType.ClanArmyGroup2) {
			return Constant.FACTION_TWO_ARMY_GROUP_MASTER_TITLE;
		}
		if (position == ClanPositionType.ClanArmyGroup3) {
			return Constant.FACTION_THREE_ARMY_GROUP_MASTER_TITLE;
		}
		if (position == ClanPositionType.ClanArmyGroup4) {
			return Constant.FACTION_FOUR_ARMY_GROUP_MASTER_TITLE;
		}
		if (position == ClanPositionType.ClanArmyGroupElite1) {
			return Constant.FACTION_ONE_ARMY_GROUP_ELITE_TITLE;
		}
		if (position == ClanPositionType.ClanArmyGroupElite2) {
			return Constant.FACTION_TWO_ARMY_GROUP_ELITE_TITLE;
		}
		if (position == ClanPositionType.ClanArmyGroupElite3) {
			return Constant.FACTION_THREE_ARMY_GROUP_ELITE_TITLE;
		}
		if (position == ClanPositionType.ClanArmyGroupElite4) {
			return Constant.FACTION_FOUR_ARMY_GROUP_ELITE_TITLE;
		}
		if (position == ClanPositionType.ClanMember) {
			return Constant.FACTION_MEMBER_TITLE;
		}
		return -1;
	}

	/**
	 * 根据玩家等级相关信息，判断玩家加入公会的时候，对应职位是否满
	 * @param level
	 * @param claninfo
	 * @param flag 如果是true 表示加入后的职位为成员
	 * @return
	 */
	public static boolean isAddClanPositionFull(xbean.ClanInfo claninfo){
		int position=ClanPositionType.ClanMember;
		if(isClanPositionFull(claninfo,position)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 判断公会成员是否满
	 * @param claninfo
	 * @return
	 */
	public static boolean isClanMemberFull(xbean.ClanInfo claninfo) {
		if (claninfo == null){
			return true;
		}
		int level = claninfo.getHouse().get(ClanManage.Hotel);
		Map<Integer, SClanHotel> hotel =  fire.pb.main.ConfigManager.getInstance().getConf(SClanHotel.class);//g公会旅馆数据表.xlsx
		if(hotel==null){
			return true;
		}
		SClanHotel sFactionHotel=hotel.get(level);//根据等级获得公会旅馆数据表
		if(sFactionHotel==null){
			return true;
		}
		if (claninfo.getMembers().size() >= sFactionHotel.getPeoplemax()){
			return true;
		}
		return false;
	}

	/**
	 * 判断公会某个职务是否满
	 * @param claninfo
	 * @param position
	 * @return
	 */
	public static boolean isClanPositionFull(xbean.ClanInfo claninfo,int position){
		if (claninfo == null){
			return true;
		}
		if (!ClanUtils.validatePosition(position)) {
			logger.error("目标职位无效。");
			return true;
		}
		int level = claninfo.getHouse().get(ClanManage.Hotel);
		Map<Integer, SClanHotel> hotel =  fire.pb.main.ConfigManager.getInstance().getConf(SClanHotel.class);//g公会旅馆数据表.xlsx
		if(hotel==null){
			return true;
		}
		int curnum=getClanPosition(claninfo,position);//当前职位人数
		//成员人数不限制
		int maxnum=0;
		if(position==ClanPositionType.ClanMember){
			maxnum=10000000;
		}else{
			maxnum=FACTION_POSITION_NAME.get(position).getPosmaxnum();
		}
		
		if(curnum>=maxnum){
			return true;
		}
		
		return false;
	}

	/**
	 * 判断玩家是否在公会副本中
	 * @param roleId
	 * @return
	 */
	public static boolean isInClanDungeon(long roleId){
		// 是否在公会副本中
		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(roleId, true);
		if (null != instzone && instzone instanceof FactionInstZone) {
			return true;
		}
		return false;
	}

	/**
	 * 判断玩家是否在公会地图
	 * @param roleId
	 * @return
	 */
	public static boolean isInClanMap(long roleId){
		final fire.pb.map.Role maprole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
		if (null != maprole&&MAPID == (int) maprole.getScene()){
			return true;
		}
		return false;
	}

	
	/**
	 * 跳出所有副本相关地图（需要判断队伍，如果在队伍中，就暂离队伍）
	 * @param roleId
	 */
	public static void jumoOutClanAllMap(long roleId){
		// 是否在公会副本中
		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(roleId, true);
		if (null != instzone && instzone instanceof FactionInstZone) {
			ClanUtils.jumpOutClanInstanceZone(roleId);
			return ;
		}
		//跳出公会地图至临安
		ClanUtils.jumpOutClanMap(roleId);
	}

	/**
	 * 跳出公会副本地图
	 * @param roleid
	 */
	private static void jumpOutClanInstanceZone(long roleid) {
		final fire.pb.map.Role maprole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleid);
		if (null == maprole)
			return;
		//如果有队伍就暂离队伍
		new PAbsentReturnTeam(roleid, 1).call();
		Transfer.justGotoWhileCommit(roleid, GOHOME_MAP[0], GOHOME_MAP[1], GOHOME_MAP[2], SRoleEnterScene.FORCE_GOTO);
	}

	/**
	 * 跳出公会地图
	 * @param roleid
	 */
	private static void jumpOutClanMap(long roleid) {
		final fire.pb.map.Role maprole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleid);
		if (null == maprole)
			return;
		if (MAPID != maprole.getMapId()) {// 不在帮派地图里
			return;
		}
		//如果有队伍就暂离队伍
		new PAbsentReturnTeam(roleid, 1).call();
		Transfer.justGotoWhileCommit(roleid, GOHOME_MAP[0], GOHOME_MAP[1], GOHOME_MAP[2], SRoleEnterScene.FORCE_GOTO);
	}

	public static void newRoleClanPoint(long roleid) {
		xbean.RoleClanPoint clanpoint = xtable.Roleclanpoint.select(roleid);
		if (clanpoint == null) {
			clanpoint = xbean.Pod.newRoleClanPoint();
			xtable.Roleclanpoint.insert(roleid, clanpoint);
		}
	}
	
	public static void removeClanTitleByPosition(long roleId, int oldPosition) {
		xbean.ClanInfo clanInfo = getClanInfoById(roleId, true);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleId))
			return;
		if (validatePosition(oldPosition)) {
			Title title = new Title(roleId, false);
			title.removeTitle(getTitleIdByPosition(oldPosition));
		}
	}

	/**
	 * 将公会从缓存中移出
	 */
	public static void removeFactionFromCache(int index) {
		Module.clanListMap.remove(index);
	}

	

	/**
	 * @param position  职位
	 * @return 根据职位返回职务名称
	 */
	public static String retNameByPosition(int position) {
		return FACTION_POSITION_NAME.get(position).getPosname();
	}
	
	public static boolean setClanInstById(final long factionId, final long instkey, final int instid) {
		xbean.ClanInfo clanInfo = null;
		if (factionId > 0) {
			clanInfo = xtable.Clans.select(factionId);
		}
		
		if (clanInfo != null) {
			xbean.ClanInstances clanInst = xtable.Claninstances.get(clanInfo.getKey());
			if (clanInst != null) {
				clanInst.getInstkey().put(instid, instkey);
				return true;
			}
			return true;
		}
		
		return false;
	}
	
	

	public static boolean setClanInstByRoleId(final long roleId, final long instkey, final int instid) {
		Long factionId = xtable.Roleidclan.select(roleId);
		xbean.ClanInfo clanInfo = null;
		if (factionId != null && factionId > 0) {
			clanInfo = xtable.Clans.get(factionId);
		}
		if (clanInfo != null && clanInfo.getMembers().containsKey(roleId)) {
			xbean.ClanInstances clanInst = xtable.Claninstances.get(clanInfo.getKey());
			if (clanInst != null) {
				clanInst.getInstkey().put(instid, instkey);
				return true;
			}
		}
		return false;
	}
	
	public static void sortApplicants(List<RoleBaseInfo> applicants, Comparator<RoleBaseInfo> c) {
		Collections.sort(applicants, c);
	}
	
	public static void sortFactions(List<ClanSummaryInfo> factionList, Comparator<ClanSummaryInfo> c) {
		Collections.sort(factionList, c);
	}

	public static void sortMembers(List<ClanMember> members, Comparator<ClanMember> c) {
		Collections.sort(members, c);
	}
	
	public static ClanSummaryInfo summaryClanInfo(xbean.ClanInfo v) {
		PropRole propRole = new PropRole(v.getClanmaster(), true);
		ClanSummaryInfo summary = new ClanSummaryInfo();
		summary.clanid = v.getKey();
		summary.index = v.getIndex();
		summary.clanname = v.getClanname();
		summary.clanlevel = v.getClanlevel();
		summary.clanmasterid = v.getClanmaster();
		summary.clanmastername = propRole.getName();
		summary.membernum = v.getMembers() == null ? 0 : v.getMembers().size();
		summary.hotellevel=v.getHouse().get(ClanManage.Hotel);
		summary.oldclanname = v.getOldclanname();
		return summary;
	}
	
	/**
	 * 检测职位是否合法
	 * @param position
	 * @return
	 */
	public static boolean validatePosition(int position) {
		if(position >= ClanPositionType.ClanMaster&&position <= ClanPositionType.ClanMember){
			return true;
		}
		return false;
	}

}
