
package fire.pb.circletask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;

import fire.pb.PropRole;
import fire.pb.activity.ActivityConfNew;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.circletask.ActiveQuestData;
import fire.pb.circletask.CircTask;
import fire.pb.circletask.CircTaskChallengeNpc;
import fire.pb.circletask.CircTaskClass;
import fire.pb.circletask.CircTaskConf;
import fire.pb.circletask.CircTaskItemCollect;
import fire.pb.circletask.CircTaskItemFind;
import fire.pb.circletask.CircTaskItemFindLegend;
import fire.pb.circletask.CircTaskItemUse;
import fire.pb.circletask.CircTaskKillMonster;
import fire.pb.circletask.CircTaskMailNpc;
import fire.pb.circletask.CircTaskPatrol;
import fire.pb.circletask.CircTaskPetCatch;
import fire.pb.circletask.CircleTaskAutoAccept;
import fire.pb.circletask.DemonstrateDefaultMonster;
import fire.pb.circletask.Random4AnYeMaXiTuan;
import fire.pb.circletask.RefreshDataType;
import fire.pb.circletask.RenXingCircTaskCost;
import fire.pb.circletask.SCircTaskMonsterConfig;
import fire.pb.circletask.SpecialQuestID;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.circletask.SpecialQuestType;
import fire.pb.circletask.anye.RoleAnYeTask;
import fire.pb.circletask.catchit.CatchItQuest;
import fire.pb.circletask.catchit.PGiveUpCatchItTask;
import fire.pb.event.ArriveTeamSpecialQuestEvent;
import fire.pb.event.BattleEndEvent;
import fire.pb.event.Event;
import fire.pb.event.EventHandler;
import fire.pb.event.LeaveTeamSpecialQuestEvent;
import fire.pb.game.MoneyType;
import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.EquipItem;
import fire.pb.item.FoodItem;
import fire.pb.item.Pack;
import fire.pb.item.SItemToItem;
import fire.pb.main.ConfigManager;
import fire.pb.map.GridPos;
import fire.pb.map.MapConfig;
import fire.pb.map.Npc;
import fire.pb.map.Position;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.SItemToPos;
import fire.pb.map.SWorldMapConfig;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.SReqMissionCanAccept;
import fire.pb.mission.UtilHelper;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.mission.activelist.RoleLivenessManager;
import fire.pb.mission.instance.LevelGroupAward;
import fire.pb.npc.NpcServices;
import fire.pb.npc.SchoolMaster;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.pb.util.BagUtil;
import fire.pb.util.GeneralFormat;
import fire.pb.util.Misc;

import org.apache.log4j.Logger;

import xbean.CircleTaskInfo;
public class CircleTaskManager implements EventHandler{
	//public static final java.util.Properties sqprop = fire.pb.main.ConfigManager.getInstance().getPropConf("specialquest");
	
	public static final Logger logger = Logger.getLogger("TASK");
	private static CircleTaskManager instance = new CircleTaskManager();
	public static final int BUFF_ID = 500206;
	
	public static CircTaskConf getCircTaskConf(int ctconfid) {
		NavigableMap<Integer, CircTaskConf> circtaskconfig = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskConf.class);
		if (circtaskconfig.containsKey(ctconfid)) {
			return circtaskconfig.get(ctconfid);
		}
		return null;
	}
	
    public static CircTaskItemFind getCircTaskItemFindConf(int index) {
		NavigableMap<Integer, CircTaskItemFind> ctitemfind = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskItemFind.class);
		if (ctitemfind.containsKey(index)) {
			return ctitemfind.get(index);
		}
		return null;
	}
    
    public static int getCTRandomMailDest(long roleid, int ctgroup, CircleTaskInfo sqinfo) {	
		final fire.pb.PropRole role = new fire.pb.PropRole(roleid, true);
		int roleSchool = role.getSchool();
		java.util.List<Integer> maildestnpc = new ArrayList<Integer>();
		NavigableMap<Integer, CircTaskMailNpc> ctmailnpc = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskMailNpc.class);
		for (CircTaskMailNpc ctmn : ctmailnpc.values()) {
			if (ctgroup == ctmn.ctgroup && (roleSchool == ctmn.school || 0 == ctmn.school)) {
				String[] tmps = ctmn.npc.split(";");
				for (String str : tmps) {
					maildestnpc.add(Integer.parseInt(str));
				}
				sqinfo.setDoneparam(ctmn.getServiceid());//!修改后务必需要服务id
			}
		}
		
		if(maildestnpc.size() <= 0){
			CircleTaskManager.logger.error("角色[" + roleid + "]在" + ctgroup + "随机目标npc失败！:" +  maildestnpc.size());
			return 0;
		}
		
		int index =mkdb.Mkdb.random().nextInt(maildestnpc.size());
		return maildestnpc.get(index);
	}

	public static CircleTaskManager getInstance() {
		return instance;
	}

	public static boolean getSameItems(int itemid, java.util.List<Integer> items) {
		SItemToItem item2item = ConfigManager.getInstance().getConf(SItemToItem.class).get(itemid);
		if(item2item != null) {
			items.addAll(item2item.getItemsid());
			return true;
		}
		return false;
	}

	//物品、宠物的映射表关系查找
	public static boolean isItem2Item(int itemid, int targetItemId) {
		SItemToItem item2item = ConfigManager.getInstance().getConf(SItemToItem.class).get(itemid);
		if(item2item != null) {
			if(item2item.getItemsid().contains(new Integer(targetItemId)))
				return true;
			else
				return false;
		}
		return false;
	}
	
	public static void sendAllSpecialQuest(long roleid) {
		xbean.CircleTaskMap quest_map = xtable.Rolecircletask.select(roleid);
		if (null == quest_map)
			return;
		for (xbean.CircleTaskInfo sq : quest_map.getTaskmap().values()) {
			if (sq.getQuestclass() == CircTaskClass.CircTask_CatchIt)
				continue;
					
			CircleTaskManager.getInstance().refresh2roleOnEnterWorld(sq, roleid);
		}
	}

	// 任务回收的道具
	private java.util.Map<Integer, java.util.List<GeneralFormat> > items
		= new java.util.HashMap<Integer, java.util.List<GeneralFormat> >();
	
	private java.util.Set<Integer> circTaskTypes = new java.util.HashSet<Integer>();
	
	private java.util.Set<Integer> canRenxingCT = new java.util.HashSet<Integer>(); //可以任性一下的循环任务类型
	
	private Map<Integer, SWorldMapConfig> legendmaps = new java.util.HashMap<Integer, SWorldMapConfig>();
	
	// 师门任务配置数据 -----begin
	private java.util.ArrayList<Rate> sqrateconfig = new java.util.ArrayList<Rate>();
	
	
	private java.util.ArrayList<CircleTaskRate> sqctrateconfig = new java.util.ArrayList<CircleTaskRate>();
	
	//循环任务组配置表, key 对应x循环任务总表里的循环任务类型, value SQCTGroup
	private java.util.Map<Integer, CircleTaskClass> sqctClassCfg = new java.util.HashMap<Integer, CircleTaskClass>();
		
	private Map<Integer, CircleTaskTeamLeaderAward> sqteamLeaderCfg = new HashMap<Integer, CircleTaskTeamLeaderAward>();
	
	private Map<Integer, CircleTaskEventGroup> sqctEventGroup = new HashMap<Integer, CircleTaskEventGroup>();
	
	private java.util.Set<Integer> sendMailServicesId = new java.util.HashSet<Integer>();
	
	private NavigableMap<Integer, DemonstrateDefaultMonster> demonsMonster = null; 
	
	private java.util.Map<Integer, SchoolMaster> masterMap = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.npc.SchoolMaster.class);

	private int IFlegendtotalrate = 0;
	
	private Map<Integer, Long> schoolMasterKeyMap = new HashMap<Integer, Long>();
	
	private Map<Integer, CircTaskSchoolItemCollectConf> ctschoolItemCollConfMap = new HashMap<Integer, CircTaskSchoolItemCollectConf>();
	
	private Map<Integer, CircTaskSchoolPatrolConf> ctschoolPatrolConfMap = new HashMap<Integer, CircTaskSchoolPatrolConf>();
	
	private Map<Integer, CircMonster> circMonsterConfMap = new HashMap<Integer, CircMonster>();
	
	private Map<Integer, ItemRandomToMapPos> itemToPosConfMap = new HashMap<Integer, ItemRandomToMapPos>();

	private Map<Integer, BattleNpcInfo> battelNpcInfo = new HashMap<Integer, BattleNpcInfo>();
	
	public Map<Integer, BattleNpcInfo> getBattleNpcInfoMap(){
		return battelNpcInfo;
	}

	private Map<Integer, List<LevelGroupAward>> circTaskZong = new HashMap<Integer, List<LevelGroupAward>>();
	
	private Map<Integer, List<LevelGroupAward>> circTaskShareZong = new HashMap<Integer, List<LevelGroupAward>>();
	
	private Map<Integer, List<LevelGroupAward>> circTaskDoubleZong = new HashMap<Integer, List<LevelGroupAward>>();

	private Map<Integer, List<LevelGroupAward>> circTaskPeizi = new HashMap<Integer, List<LevelGroupAward>>();

	CircleTaskManager() {
		initItems();
		initCircTaskConfig();
		initCircTaskRateConfig();
		initCTSchoolItemCollConfig();
		initCTSchoolPatrolConfig();
		initCircMonsterConfig();
		
		NavigableMap<Integer, CircTask> circtask = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTask.class);
		for (CircTask curCT : circtask.values()) {
			circTaskTypes.add(new Integer(curCT.getType()));
			
			if (curCT.getAwardids() != null) {
				List<LevelGroupAward> levelGroup = new ArrayList<LevelGroupAward>();
				String [] awardLst = curCT.getAwardids().split(";");
				for (String awardStr : awardLst) {
					String [] awardSub = awardStr.split(",");
					LevelGroupAward group = new LevelGroupAward(curCT.getId(), Integer.parseInt(awardSub[0]), 
							Integer.parseInt(awardSub[1]), Integer.parseInt(awardSub[2]));
					
					levelGroup.add(group);
				}
				
				circTaskZong.put(curCT.getId(), levelGroup);
			}
			
			if (curCT.getShareawardids() != null) {
				List<LevelGroupAward> levelGroup = new ArrayList<LevelGroupAward>();
				String [] awardLst = curCT.getShareawardids().split(";");
				for (String awardStr : awardLst) {
					String [] awardSub = awardStr.split(",");
					LevelGroupAward group = new LevelGroupAward(curCT.getId(), Integer.parseInt(awardSub[0]), 
							Integer.parseInt(awardSub[1]), Integer.parseInt(awardSub[2]));
					
					levelGroup.add(group);
				}
				
				circTaskShareZong.put(curCT.getId(), levelGroup);
			}
			
			if (curCT.getDbappendawardids() != null) {
				List<LevelGroupAward> levelGroup = new ArrayList<LevelGroupAward>();
				String [] awardLst = curCT.getDbappendawardids().split(";");
				for (String awardStr : awardLst) {
					String [] awardSub = awardStr.split(",");
					LevelGroupAward group = new LevelGroupAward(curCT.getId(), Integer.parseInt(awardSub[0]), 
							Integer.parseInt(awardSub[1]), Integer.parseInt(awardSub[2]));
					
					levelGroup.add(group);
				}
				
				circTaskDoubleZong.put(curCT.getId(), levelGroup);
			}
		}
		
		NavigableMap<Integer, CircTaskConf> circtaskconf = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskConf.class);
		for (CircTaskConf curctconf : circtaskconf.values()) {
			if (curctconf.getIsrenxing() == 1) {
				canRenxingCT.add(new Integer(curctconf.getId()));
			}
			
			if (curctconf.getAwardids() == null)
				continue;
			
			List<LevelGroupAward> levelGroup = new ArrayList<LevelGroupAward>();
			String [] awardLst = curctconf.getAwardids().split(";");
			for (String awardStr : awardLst) {
				String [] awardSub = awardStr.split(",");
				LevelGroupAward group = new LevelGroupAward(curctconf.getId(), Integer.parseInt(awardSub[0]), 
						Integer.parseInt(awardSub[1]), Integer.parseInt(awardSub[2]));
				
				levelGroup.add(group);
			}
			
			circTaskPeizi.put(curctconf.getId(), levelGroup);
		}
		
		NavigableMap<Integer, CircTaskItemFindLegend> ctifls = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskItemFindLegend.class);
		IFlegendtotalrate = 0;
		for (CircTaskItemFindLegend ctifl : ctifls.values()) {
			IFlegendtotalrate += ctifl.getWeight();
		}
		
		NavigableMap<Integer, SWorldMapConfig> cwms = ConfigManager.getInstance().getConf(SWorldMapConfig.class);
		for (SWorldMapConfig cwm : cwms.values()) {
			if (cwm.getMaptype() == 3) //挂机地宫类型
				legendmaps.put(cwm.getId(), cwm);
		}
		
		NavigableMap<Integer, CircTaskMailNpc> ctmns = ConfigManager.getInstance().getConf(CircTaskMailNpc.class);
		for (CircTaskMailNpc ctmn : ctmns.values()) {
			if (ctmn.getServiceid() > 0)//所有送信类循环任务的服务id
				sendMailServicesId.add(ctmn.getServiceid());
		}

		demonsMonster = ConfigManager.getInstance().getConf(
				fire.pb.circletask.DemonstrateDefaultMonster.class);
		
		NavigableMap<Integer, CircTaskChallengeNpc> ctchallengenpc = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskChallengeNpc.class);
		for (CircTaskChallengeNpc ctcn : ctchallengenpc.values()) {
			BattleNpcInfo bInfo = new BattleNpcInfo(ctcn.getNpcid());
			battelNpcInfo.put(ctcn.getId(), bInfo);
		}
		
		fire.pb.event.Poster.getPoster().listenEvent(this, fire.pb.event.BattleEndEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, fire.pb.event.LeaveTeamSpecialQuestEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, fire.pb.event.ArriveTeamSpecialQuestEvent.class);
		
		int maxrecommend = 0;
		NavigableMap<Integer, ActivityConfNew> activityconf = RoleLiveness.getConfigActivity();
		for (ActivityConfNew conf : activityconf.values()) {
			if(conf.getRecommend() > maxrecommend)
				maxrecommend = conf.getRecommend();
		}
		RoleLivenessManager.MAXRECOMMEND = maxrecommend;
	}

	
	public boolean canRenXing(final int questtype) {
		if (this.canRenxingCT.contains(new Integer(questtype)))
			return true;
		return false;
	}
	
	public boolean genCTItemFindParam(long roleid, CircleTaskInfo sqinfo, int ctgroup, int submitnpc) {
		final PropRole role = new PropRole(roleid, true);
		int roleSchool = role.getSchool();
		int roleLevel = role.getLevel();
		
		NavigableMap<Integer, CircTaskItemFind> ctitemfind = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskItemFind.class);
		for (CircTaskItemFind ctif : ctitemfind.values()) {
			if (ctgroup == ctif.ctgroup && (roleSchool == ctif.school || 0 == ctif.school) 
					&& roleLevel >= ctif.levelmin && roleLevel <= ctif.levelmax) {
				String items = ctif.getRecycleitem();
				java.util.List<GeneralFormat> results = fire.pb.util.ParseString.parseString2GeneralFormat(items);
				if (results == null)
					return false;
				int totalrate = 0;
				for (int i=0; i< results.size(); i++)
					totalrate += results.get(i).getRate();
				int randrate = mkdb.Mkdb.random().nextInt(totalrate);
				int pilerate = 0;
				for (int i=0; i< results.size(); i++){
					pilerate += results.get(i).getRate();
					if (randrate < pilerate) {
						sqinfo.setDstitemid(results.get(i).getId());
						sqinfo.setDstitemnum(ctif.itemnum);
						final Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.BAG, true);
						if (bag.getItemNum(sqinfo.getDstitemid(), 0) >= sqinfo.getDstitemnum()) {
							CircleTaskManager.logger.debug("角色[" + roleid + "]背包里已经有物品id:" + sqinfo.getDstitemid());
							//设置npcid
							//int masterid = fire.pb.specialquest.SpecialQuestManager.getInstance().getMasterMap().get(schoolid).masterid;
							sqinfo.setDstnpcid(submitnpc);
							sqinfo.setDstnpckey(0);
							sqinfo.setDstmapid(0);
							sqinfo.setDstx(0);
							sqinfo.setDsty(0);
							sqinfo.setQueststate(SpecialQuestState.DONE);
						} else {
							CircleTaskManager.logger.debug("角色[" + roleid + "]背包里没有物品id:" + sqinfo.getDstitemid());
							java.util.List<Integer> sameitems = new ArrayList<Integer>();
							if(CircleTaskManager.getSameItems(sqinfo.getDstitemid(), sameitems) == true && sameitems.size() > 0){
								boolean idfind = false;
								for(Integer curitemid : sameitems) {
									if (bag.getItemNum(curitemid, 0) >= sqinfo.getDstitemnum()) {
										sqinfo.setDstnpcid(submitnpc);
										sqinfo.setDstnpckey(0);
										sqinfo.setDstmapid(0);
										sqinfo.setDstx(0);
										sqinfo.setDsty(0);
										sqinfo.setQueststate(SpecialQuestState.DONE);
										idfind = true;
										break;
									}
								}
								CircleTaskManager.logger.debug("角色[" + roleid + "]查同类物品表来找是否有物品.结果:" + idfind);
								if(idfind == false) {
									//设置npcid
									sqinfo.setDstnpcid(ctif.getShopnpc());
									sqinfo.setDstnpckey(0);
									sqinfo.setDstmapid(0);
									sqinfo.setDstx(0);
									sqinfo.setDsty(0);
									sqinfo.setQueststate(SpecialQuestState.UNDONE);
								}
							} else {
								//设置npcid
								sqinfo.setDstnpcid(ctif.getShopnpc());
								sqinfo.setDstnpckey(0);
								sqinfo.setDstmapid(0);
								sqinfo.setDstx(0);
								sqinfo.setDsty(0);
								sqinfo.setQueststate(SpecialQuestState.UNDONE);
							}
						}
						
						sqinfo.setGivetasknpcid(ctif.id);//这里存索引id供以后使用
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public int getCTCurLevelMap(long roleid) {
		Integer level = xtable.Properties.selectLevel(roleid);
		Map<Integer, CircTaskKillMonster> confData = ConfigManager.getInstance().getConf(CircTaskKillMonster.class);
		for (CircTaskKillMonster cd : confData.values()) {
			if (level >= cd.getLevelmin() && level <= cd.getLevelmax()) {
				return cd.mapid;
			}
		}
		return 0;
	}
	
	public boolean genCTKillMonsterParam(long roleid, CircleTaskInfo sqinfo, int ctgroup) {
		Integer level = xtable.Properties.selectLevel(roleid);
		Integer school = xtable.Properties.selectSchool(roleid);
		Map<Integer, CircTaskKillMonster> confData = ConfigManager.getInstance().getConf(CircTaskKillMonster.class);
		for (CircTaskKillMonster cd : confData.values()) {
			if (ctgroup == cd.getCtgroup() && (school == cd.getSchool() || 0 == cd.getSchool()) 
					&& (level >= cd.getLevelmin() && level <= cd.getLevelmax())) {
				int confmap = cd.getMapid();
				String confmonsters = cd.getMonsterids();
				int confnum = cd.getMonsternum();
				int confmonster = 0; //默认0,所有怪物都计数
				//随机出一个目标怪物
				java.util.List<GeneralFormat> results = fire.pb.util.ParseString.parseString2GeneralFormatWithoutException(confmonsters);
				if (results != null) {
					int totalrate = 0;
					for (int i=0; i< results.size(); i++)
						totalrate += results.get(i).getRate();
					int randrate = mkdb.Mkdb.random().nextInt(totalrate);
					int pilerate = 0;
					for (int i=0; i< results.size(); i++){
						pilerate += results.get(i).getRate();
						if (randrate < pilerate) {
							confmonster = results.get(i).getId();
						}
					}
				}
				sqinfo.setDstitemid(confmonster);
				sqinfo.setDstitemnum(confnum);
				sqinfo.setDstnpcid(0);
				sqinfo.setDstmapid(confmap);
				return true;
			}
		}
		return false;
	}

	
	public boolean genCTSchoolItemCollectParam(long roleid, CircleTaskInfo sqinfo,int ctgroup){
		Integer level = xtable.Properties.selectLevel(roleid);
		Integer school = xtable.Properties.selectSchool(roleid);
		CircTaskSchoolItemCollectConf ctschoolItemCollConf = getCTSchoolItemCollConf(ctgroup,school,level);
		if (ctschoolItemCollConf == null) {
			logger.debug("师门任务收集道具配置没找到group:" + ctgroup + " school:" + school + " level:" + level + "!");
			return false;
		}
		int index = Misc.getProbability(ctschoolItemCollConf.getMapProbs());
		sqinfo.setDstmapid(ctschoolItemCollConf.getMapids().get(index));
		sqinfo.setDstitemid(ctschoolItemCollConf.getItemid());
		sqinfo.setDstitemnum(0);
		sqinfo.setBattlenum(ctschoolItemCollConf.getItemnum());
		sqinfo.setDsty(0);	
		logger.debug("师门任务收集道具，门派：" + school + "等级:" + level + " 场景:" + sqinfo.getDstmapid());
		return true;
	}
	
	public boolean genCTSchoolPatrolParam(long roleid,CircleTaskInfo sqinfo,int ctgroup){
		Integer level = xtable.Properties.selectLevel(roleid);
		Integer school = xtable.Properties.selectSchool(roleid);
		CircTaskSchoolPatrolConf ctschoolPatrolConf = getCTSchoolPatrolConf(ctgroup,school,level);
		if (ctschoolPatrolConf == null)
			return false;
		int index = Misc.getProbability(ctschoolPatrolConf.getMapProbs());
		sqinfo.setDstmapid(ctschoolPatrolConf.getMapids().get(index));
		int battleCfgId = getCircTaskSchoolPatrolMonster(ctgroup, level, school);
		sqinfo.setDstitemid(battleCfgId);
		return true;
	}
	
	public int getCatchItBattleNpc(int groupid) {
		CircMonster littleMonser = circMonsterConfMap.get(groupid);
		int index = fire.pb.util.Misc.getProbability(littleMonser.battleProb);
		if (index != -1) {
			return littleMonser.battleId.get(index);
		}
		return 0;
	}
	

	
	public int getCatchItDayMax(long roleid, int questid) {

		final fire.pb.PropRole role = new fire.pb.PropRole(roleid, true);

		int rolelevel = role.getLevel();
		NavigableMap<Integer, CircTask> CTMap = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTask.class);
		for (CircTask ct : CTMap.values()) {
			if (rolelevel >= ct.levelmin
					&& rolelevel <= ct.levelmax
					&& ct.type == questid) {
				return ct.awardtime;
			}
		}
		
		return 0;
	}

	
	public int getCatchItMap(int groupid) {

		CircMonster littleMonser = circMonsterConfMap.get(groupid);
		int index = fire.pb.util.Misc.getProbability(littleMonser.mapProb);
		if (index != -1)
			return littleMonser.mapId.get(index);
		
		return 0;
	}

	
	public java.util.List<Integer> getCircleTypeByLevel(final int level) {
		java.util.List<Integer> ret = new ArrayList<Integer>();
		NavigableMap<Integer, CircleTaskAutoAccept> CTAutoAccept = ConfigManager.getInstance().getConf(fire.pb.circletask.CircleTaskAutoAccept.class);
		for(CircleTaskAutoAccept ct : CTAutoAccept.values()) {
			if (ct.id <= level) {
				ret.add(new Integer(ct.circletype));
			}
		}
		return ret;
	}
	
	
	
	public CircTask getCircTask(final int id) {
		NavigableMap<Integer, CircTask> CTMap = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTask.class);
		if (CTMap.containsKey(id)) {
			CircTask ct = CTMap.get(id);
			return ct;
		}
		return null;
	}
	
	public CircTask getCircTask(final int level, final int ctcate) {
		if (sqctClassCfg.isEmpty()) {
			CircleTaskManager.logger.debug("SQCTClass is null!");
			return null;
		}
		
		CircleTaskClass ctClass = sqctClassCfg.get(ctcate);
		
		//从x循环任务总表里随机出一行数据
		int id = -1;
		for (CircleTaskGroup ctGroup : ctClass.sqctGroup.values()) {
			if (level >= ctGroup.levelmin && level <= ctGroup.levelmax) {
				for (CircleTaskRate ctgg: ctGroup.listSQCT) {
					id = ctgg.id;
					break;
				}
				if (id == -1) {
					return null;
				}
				CircTask ctResult = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTask.class).get(id);
				return ctResult; //找到该等级段任务
			}
		}
		
//		CircleTaskGroup ctGroup = ctClass.sqctGroup.get(1);
		for (CircleTaskGroup ctgroupOther : ctClass.sqctGroup.values()) {
			int idother = -1;
			for (CircleTaskRate ctgg: ctgroupOther.listSQCT) {
				idother = ctgg.id;
				break;
			}
			CircTask ctResult = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTask.class).get(idother);
			return ctResult;
		}
		
		return null;
	}
	public CircTask getCircTask(final long roleid, final int ctcate) {

		final fire.pb.PropRole role = new fire.pb.PropRole(roleid, true);
		int rolelevel = role.getLevel();
		NavigableMap<Integer, CircTask> CTMap = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTask.class);
		for (CircTask ct : CTMap.values()) {
			if (rolelevel >= ct.levelmin
					&& rolelevel <= ct.levelmax
					&& ct.type == ctcate) {
				return ct;
			}
		}
		return null;
	}

	
	
	public CircTask getCircTaskByType(final int type) {
		int id = -1;
		if (sqctClassCfg.containsKey(type)) {
			CircleTaskClass sqctClass = sqctClassCfg.get(type);
			for (CircleTaskGroup ctgroup : sqctClass.sqctGroup.values()) {
				for (CircleTaskRate ctgg: ctgroup.listSQCT) {
					id = ctgg.id;
					break;
				}
			}
			
			if (id == -1) {
				return null;
			}
		}
		
		NavigableMap<Integer, CircTask> CTMap = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTask.class);
		if (CTMap.containsKey(id)) {
			CircTask ct = CTMap.get(id);
			return ct;
		}
		return null;
	}
	public int getCircTaskDoubleZong(final int taskid, final int rolelevel) {
		if (!circTaskDoubleZong.containsKey(taskid))
			return -1;
		
		List<LevelGroupAward> levelGroup = circTaskDoubleZong.get(taskid);
		for (LevelGroupAward award : levelGroup) {
			if (rolelevel >= award.levelmin && rolelevel <= award.levelmax) {
				return award.awardid;
			}
		}
		
		return -1;
	}
	public int getCircTaskPeizi(final int taskid, final int rolelevel) {
		if (!circTaskPeizi.containsKey(taskid))
			return -1;
		
		List<LevelGroupAward> levelGroup = circTaskPeizi.get(taskid);
		for (LevelGroupAward award : levelGroup) {
			if (rolelevel >= award.levelmin && rolelevel <= award.levelmax) {
				return award.awardid;
			}
		}
		
		return -1;
	}
	
	public int getCircTaskSchoolPatrolMonster(int ctgroup, int rolelevel, int schoolid) {

		CircTaskSchoolPatrolConf ctschoolPatrolConf = getCTSchoolPatrolConf(ctgroup,schoolid,rolelevel);
		if (ctschoolPatrolConf == null)
			return 0;
		
		int index = 0;

		index = Misc.getProbability(ctschoolPatrolConf.getBattleCFGProbs());
		
		int battleCFGId = ctschoolPatrolConf.getBattleCFGs().get(index);

		logger.debug("师门任务巡逻，没有获得门派：" + schoolid + "等级:" + rolelevel + " 战斗信息配置" + battleCFGId);

		return battleCFGId;
	}
	
	public int getCircTaskShareZong(final int taskid, final int rolelevel) {
		if (!circTaskShareZong.containsKey(taskid))
			return -1;
		
		List<LevelGroupAward> levelGroup = circTaskShareZong.get(taskid);
		for (LevelGroupAward award : levelGroup) {
			if (rolelevel >= award.levelmin && rolelevel <= award.levelmax) {
				return award.awardid;
			}
		}
		
		return -1;
	}

	
	public GeneralFormat getCircTaskTypeInfo(int level, int round, int type) {
		if (sqctrateconfig.isEmpty()) {
			CircleTaskManager.logger.debug("SQCTRate is null!");
			return null;
		}
		List<GeneralFormat> result = null;
		for (CircleTaskRate nsqr : sqctrateconfig){
			if (level >= nsqr.levelmin && level <= nsqr.levelmax && type == nsqr.type){
				result = nsqr.rounds.get(round % 10);
				break;
			}
		}
		if (result == null) {
			CircleTaskManager.logger.debug("SQCTRate find fail!");
			return null;
		}
		int totalrate = 0;
		for (int i=0; i< result.size(); i++)
			totalrate += result.get(i).getRate();
		int randrate = mkdb.Mkdb.random().nextInt(totalrate);
		int pilerate = 0;
		for (int i=0; i< result.size(); i++){
			pilerate += result.get(i).getRate();
			if (randrate < pilerate)
				return result.get(i);
		}
		CircleTaskManager.logger.debug("SQCTRate rate fail!");
		return null;
	}
	
	
	
	public int getCircTaskTypeInfoById(final int id, final int level, final int round, final int type, GeneralFormat format) {
		if (sqctClassCfg.isEmpty()) {
			CircleTaskManager.logger.debug("SQCTClass is null!");
			return -1;
		}
		
		List<GeneralFormat> result = null;
		int nRetId = id;
		CircleTaskClass ctClass = sqctClassCfg.get(type);
		
		if (id != -1) {
			//根据id获取 上一环 是x循环任务总表里的哪一行数据
			for (CircleTaskGroup ctGroup : ctClass.sqctGroup.values()) {
				for (CircleTaskRate ctRate : ctGroup.listSQCT) {
					if (ctRate.id == id) {
						int tmprnd = round % 10;
						if (ctRate.rounds.size() - 1 < tmprnd) {
							return -1;
						}
						result = ctRate.rounds.get(round % 10);
						break;
					}
				}
			}
		}
		else {
			//从x循环任务总表里随机出一行数据
			for (CircleTaskGroup ctGroup : ctClass.sqctGroup.values()) {
				if (level >= ctGroup.levelmin && level <= ctGroup.levelmax) {
					if (ctGroup.levelRate.size() > 0) {
						int nIndex = Misc.getProbability(ctGroup.levelRate);
						CircleTaskRate ctRate = ctGroup.listSQCT.get(nIndex);
						int tmprnd = round % 10;
						if (ctRate.rounds.size() - 1 < tmprnd) {
							return -1;
						}
						result = ctRate.rounds.get(round % 10);
						nRetId = ctRate.id;
					}
					else {
						//等级组中只有一个
						for (CircleTaskRate ctRate : ctGroup.listSQCT) {
							int tmprnd = round % 10;
							if (ctRate.rounds.size() - 1 < tmprnd) {
								return -1;
							}
							result = ctRate.rounds.get(round % 10);
							nRetId = ctRate.id;
							break;
						}
					}
					
					break;
				}
			}
		}
		
		//获取环数
		if (result == null) {
			CircleTaskManager.logger.debug("SQCTClass find fail!");
			return nRetId;
		}
		
		GeneralFormat formatTmp = null;
		
		int totalrate = 0;
		for (int i=0; i< result.size(); i++)
			totalrate += result.get(i).getRate();
		int randrate = mkdb.Mkdb.random().nextInt(totalrate);
		int pilerate = 0;
		for (int i=0; i< result.size(); i++){
			pilerate += result.get(i).getRate();
			if (randrate < pilerate) {
				formatTmp = result.get(i);
				break;
			}
		}
		
		if (formatTmp != null) {
			format.setId(formatTmp.getId());
			format.setRate(formatTmp.getRate());
			format.setSubid(formatTmp.getSubid());
		}
		
		CircleTaskManager.logger.debug("SQCTClass rate OK!");
		return nRetId;
	}
	
//	public void initCtItems(){
//		NavigableMap<Integer, CircTaskItemFind> ctitemfind = ConfigManager.getInstance().getConf(fire.pb.specialquest.CircTaskItemFind.class);
//		for (CircTaskItemFind ctif : ctitemfind.values()){
//			//items.put(itemstr.id, fire.pb.util.ParseString.parseString2GeneralFormat(itemstr.itemstr));
//		}
//	}
	
	public java.util.Set<Integer> getCircTaskTypes() {
		return this.circTaskTypes;
	}
	
	public int getCircTaskZong(final int taskid, final int rolelevel) {
		if (!circTaskZong.containsKey(taskid))
			return -1;
		
		List<LevelGroupAward> levelGroup = circTaskZong.get(taskid);
		for (LevelGroupAward award : levelGroup) {
			if (rolelevel >= award.levelmin && rolelevel <= award.levelmax) {
				return award.awardid;
			}
		}
		
		return -1;
	}
	
	public int getCTChallengeNpcBattleAI(long roleid, int ctgroup, CircleTaskInfo sqinfo) {
		final fire.pb.PropRole role = new fire.pb.PropRole(roleid, true);
		int roleSchool = role.getSchool();
		int roleLevel = role.getLevel();
		
		NavigableMap<Integer, CircTaskChallengeNpc> ctchallengenpc = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskChallengeNpc.class);
		List<Integer> tempL = new LinkedList<Integer>();
		for (Entry<Integer, CircTaskChallengeNpc> e : ctchallengenpc.entrySet()) {
			if(ctgroup == e.getValue().ctgroup) {
				tempL.add(e.getValue().id);
			}
		}
		int randv = 0;
		if(tempL.size() > 0) {
			Collections.shuffle(tempL);
			randv = tempL.get(0).intValue();
		}
		for (CircTaskChallengeNpc ctcn : ctchallengenpc.values()) {
			if (ctgroup == ctcn.ctgroup && (roleSchool == ctcn.school || 0 == ctcn.school) 
					&& roleLevel >= ctcn.levelmin && roleLevel <= ctcn.levelmax && 
					(randv == 0 ? true : ctcn.id == randv)) {
				String battleinfo = ctcn.getBattleinfo();
				java.util.List<GeneralFormat> results = fire.pb.util.ParseString.parseString2GeneralFormat(battleinfo);
				if (results == null)
					break;
				
				int totalrate = 0;
				for (int i=0; i< results.size(); i++)
					totalrate += results.get(i).getRate();
				int randrate = mkdb.Mkdb.random().nextInt(totalrate);
				int pilerate = 0;
				for (int i=0; i< results.size(); i++){
					pilerate += results.get(i).getRate();
					if (randrate < pilerate) {
						int battleai = results.get(i).getId();
						BattleNpcInfo bInfo = battelNpcInfo.get(ctcn.getId());
						int npcId = bInfo.getNpcId();
						if (npcId != -1) 
							sqinfo.setDstnpcid(npcId);
						
						sqinfo.setDoneparam(battleai);
						return battleai;
					}
				}
			}
		}
		return 0;
	}
	
	public int getCTCircleMax(CircTask circleconf) {
		if (circleconf == null)
			return 0;
		
		return circleconf.round * circleconf.ring;
	}
	
	public int getCTCircleMax(long roleid, int cttype) {
		final fire.pb.PropRole role = new fire.pb.PropRole(roleid, true);
		int roleLevel = role.getLevel();
		NavigableMap<Integer, CircTask> circtaskrate = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTask.class);
		for (CircTask ct : circtaskrate.values()) {
			if (roleLevel >= ct.levelmin
					&& roleLevel <= ct.levelmax
					&& ct.type == cttype) {
				return ct.round * ct.ring;
			}
		}
		return 0;
	}

	public CircTaskItemFind getCTItemFindData(long roleid, int ctgroup) {
		final fire.pb.PropRole role = new fire.pb.PropRole(roleid, true);
		int roleSchool = role.getSchool();
		
		NavigableMap<Integer, CircTaskItemFind> ctitemfind = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskItemFind.class);
		for (CircTaskItemFind ctiu : ctitemfind.values()) {
			if (ctgroup == ctiu.ctgroup && roleSchool == ctiu.school) {
				return ctiu;
			}
		}
		return null;
	}

	
	public ItemFindInfo getCTItemFindInfo(long roleid, int ctgroup) {
		final fire.pb.PropRole role = new fire.pb.PropRole(roleid, true);
		int roleSchool = role.getSchool();
		int roleLevel = role.getLevel();
		
		NavigableMap<Integer, CircTaskItemFind> ctitemfind = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskItemFind.class);
		for (CircTaskItemFind ctif : ctitemfind.values()) {
			if (ctgroup == ctif.ctgroup && (roleSchool == ctif.school || 0 == ctif.school) 
					&& roleLevel >= ctif.levelmin && roleLevel <= ctif.levelmax) {
				String items = ctif.getRecycleitem();
				java.util.List<GeneralFormat> results = fire.pb.util.ParseString.parseString2GeneralFormat(items);
				if (results == null)
					return null;
				
				int totalrate = 0;
				for (int i=0; i< results.size(); i++)
					totalrate += results.get(i).getRate();
				int randrate = mkdb.Mkdb.random().nextInt(totalrate);
				int pilerate = 0;
				for (int i=0; i< results.size(); i++){
					pilerate += results.get(i).getRate();
					if (randrate < pilerate) {
						ItemFindInfo ifi = new ItemFindInfo(results.get(i).getId(), results.get(i).getRate(), ctif.itemnum, ctif.levelmin, ctif.levelmax, ctif.shopnpc);
						return ifi;
					}
				}
			}
		}
		return null;
	}
	
	
	public int getCTItemFindLegend() {
		NavigableMap<Integer, CircTaskItemFindLegend> ctifls = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskItemFindLegend.class);
		int randrate = mkdb.Mkdb.random().nextInt(IFlegendtotalrate);
		int pilerate = 0;
		for (CircTaskItemFindLegend ctifl : ctifls.values()) {
			pilerate += ctifl.getWeight();
			if (randrate < pilerate) {
				return ctifl.getId();
			}
		}
		return 0;
	}
	
	public CircTaskItemUse getCTItemUseData(int id) {
		NavigableMap<Integer, CircTaskItemUse> ctitemuse = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskItemUse.class);
		if (ctitemuse.containsKey(id))
			return ctitemuse.get(id);

		return null;
	}
	
	public CircTaskItemUse getCTItemUseData(long roleid, int ctgroup) {
		
		final fire.pb.PropRole role = new fire.pb.PropRole(roleid, true);
		int roleSchool = role.getSchool();
		int roleLevel = role.getLevel();

		NavigableMap<Integer, CircTaskItemUse> ctitemuse = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskItemUse.class);
		for (CircTaskItemUse ctiu : ctitemuse.values()) {
			if (ctgroup == ctiu.ctgroup && (roleSchool == ctiu.school || 0 == ctiu.school)
					&& (roleLevel >= ctiu.getLevelmin() && roleLevel <= ctiu.getLevelmax()) ) {
				return ctiu;
			}
		}

		return null;
	}
	
	
	public ItemFindInfo getCTPetFindInfo(long roleid, int ctgroup) {
		final fire.pb.PropRole role = new fire.pb.PropRole(roleid, true);
		int roleSchool = role.getSchool();
		int roleLevel = role.getLevel();
		
		NavigableMap<Integer, CircTaskPetCatch> ctpetcatch = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskPetCatch.class);
		for (CircTaskPetCatch ctif : ctpetcatch.values()) {
			if (ctgroup == ctif.ctgroup && (roleSchool == ctif.school || 0 == ctif.school) 
					&& roleLevel >= ctif.levelmin && roleLevel <= ctif.levelmax) {
				String pets = ctif.getRecycleitem();
				java.util.List<GeneralFormat> results = fire.pb.util.ParseString.parseString2GeneralFormat(pets);
				if (results == null)
					return null;
				
				int totalrate = 0;
				for (int i=0; i< results.size(); i++)
					totalrate += results.get(i).getRate();
				int randrate = mkdb.Mkdb.random().nextInt(totalrate);
				int pilerate = 0;
				for (int i=0; i< results.size(); i++){
					pilerate += results.get(i).getRate();
					if (randrate < pilerate) {
						ItemFindInfo ifi = new ItemFindInfo(results.get(i).getId(), results.get(i).getRate(), ctif.itemnum, ctif.levelmin, ctif.levelmax, ctif.shopnpc);
						return ifi;
					}
				}
			}
		}
		return null;
	}
	
	public CircTaskSchoolItemCollectConf getCTSchoolItemCollConf(int ctgroup,int school,int level) {
		for (CircTaskSchoolItemCollectConf ctic : ctschoolItemCollConfMap.values()) {
			if (ctgroup == ctic.getGroup() && (school == ctic.getSchool() || 0 == ctic.getSchool()) 
					&& (level >= ctic.getLevelMin() && level <= ctic.getLevelMax())) {
				return ctic;
			}
		}
		
		return null;
	}
	
	
	public CircTaskSchoolPatrolConf getCTSchoolPatrolConf(int ctgroup,int school,int level) {
		for (CircTaskSchoolPatrolConf ctp : ctschoolPatrolConfMap.values()) {
			if (ctgroup == ctp.getGroup() && (school == ctp.getSchool() || 0 == ctp.getSchool()) 
					&& (level >= ctp.getLevelMin() && level <= ctp.getLevelMax())) {
				return ctp;
			}
		}
		
		return null;
	}
	
	
	public int getDestItemid(int reclaimid){
		java.util.List<GeneralFormat> formats = items.get(reclaimid);
		if (formats == null || formats.isEmpty())
			return 0;
		int totalrate = 0;
		for (GeneralFormat f : formats){
			totalrate += f.getRate();
		}
		int randrate = mkdb.Mkdb.random().nextInt(totalrate);
		int pilerate = 0;
		for (GeneralFormat f : formats){
			pilerate += f.getRate();
			if (randrate < pilerate)
				return f.getId();
		}
		return 0;
	}
	
	
	public CircleTaskEventGroup getEventGroup(int biaoid) {
		if (sqctEventGroup.containsKey(biaoid)) {
			return sqctEventGroup.get(biaoid);
		}
		
		return null;
	}
	
	
	public int getItemToMap(final int itemid) {
		if (itemToPosConfMap.containsKey(itemid)) {
			ItemRandomToMapPos itemPos = itemToPosConfMap.get(itemid);
			int nIndex = Misc.getProbability(itemPos.mapRateList);
			if (nIndex < itemPos.mapList.size()) {
				return itemPos.mapList.get(nIndex);
			}
		}
		
		return 0;
	}
	
	
	public java.util.Map<Integer, SchoolMaster> getMasterMap() {

		return masterMap;
	}
	
	public void getNextRandomTask(long roleid, int round, GeneralFormat format) {
		final fire.pb.PropRole role = new fire.pb.PropRole(roleid, true);
		int roleLevel = role.getLevel();
		NavigableMap<Integer, Random4AnYeMaXiTuan> confs = ConfigManager.getInstance().getConf(fire.pb.circletask.Random4AnYeMaXiTuan.class);
		for (Random4AnYeMaXiTuan conf : confs.values()) {
			if(conf.getLevelmin() <= roleLevel && conf.getLevelmax() >= roleLevel && conf.getRoundmin() < round && conf.getRoundmax() > round) {
				String taskrates = conf.getQuestrate();
				java.util.List<GeneralFormat> results = fire.pb.util.ParseString.parseString2GeneralFormat(taskrates);
				if (results == null)
					return;
				
				int totalrate = 0;
				for (int i=0; i< results.size(); i++)
					totalrate += results.get(i).getRate();
				int randrate = mkdb.Mkdb.random().nextInt(totalrate);
				int pilerate = 0;
				for (int i=0; i< results.size(); i++){
					pilerate += results.get(i).getRate();
					if (randrate < pilerate) {
						format.setId(results.get(i).getId());
						format.setRate(results.get(i).getRate());
						format.setSubid(results.get(i).getSubid());
						return;
					}
				}
			}
		}
	}
	
	public int getRenXingCost(int times, int moneytype) {
		RenXingCircTaskCost dest = null;
		NavigableMap<Integer, RenXingCircTaskCost> costinfo = ConfigManager.getInstance().getConf(fire.pb.circletask.RenXingCircTaskCost.class);
		dest = costinfo.firstEntry().getValue();
		for (RenXingCircTaskCost cur : costinfo.values()){
			if (times >= cur.getId())
				dest = cur;
			if (times < cur.getId())
				break;
		}
		if (moneytype == MoneyType.MoneyType_HearthStone) {
			return dest.getStonecost();
		} else if (moneytype == MoneyType.MoneyType_ShengWang) {
			return dest.getXiayicost();
		} else if (moneytype == MoneyType.MoneyType_GoldCoin) {
			//这里要怪策划，我被迫符石当金币6_6
			return dest.getStonecost();
		} else {
			return -1;
		}
	}
	
	public Long getRoleMastaerKey(long roleid,int school) {
		SchoolMaster sm = getMasterMap().get( school );
		Long npckey = schoolMasterKeyMap.get(school);
		if (npckey!=null)
			return npckey;
		if (sm == null)
			return null;
		final int npcid = sm.getMasterid();
		
		for(fire.pb.map.Npc npc : SceneNpcManager.getInstance().getNpcs().values())
		{
			if(npc.getNpcID() == npcid){
				npckey = npc.getNpcKey();
				schoolMasterKeyMap.put(school, npckey);
				break;
			}
		}
		return npckey;
	}
	
	public String getSchoolName(int schoolid) {
		final DemonstrateDefaultMonster monsters = demonsMonster.get(schoolid);
		if (null == monsters)
			return "";
		else
			return monsters.schoolname;
	}
		
	public GeneralFormat getSchoolQuestType(int level, int round){
		if (sqrateconfig.isEmpty())
			return null;
		List<GeneralFormat> result = null;
		for (Rate nsqr : sqrateconfig){
			if (level >= nsqr.levelmin && level <= nsqr.levelmax){
				result = nsqr.rounds.get(round % 10);
				break;
			}
		}
		if (result == null)
			return null;
		int totalrate = 0;
		for (int i=0; i< result.size(); i++)
			totalrate += result.get(i).getRate();
		int randrate = mkdb.Mkdb.random().nextInt(totalrate);
		int pilerate = 0;
		for (int i=0; i< result.size(); i++){
			pilerate += result.get(i).getRate();
			if (randrate < pilerate)
				return result.get(i);
		}
		return null;
	}
	
	public GeneralFormat getSchoolQuestTypeExcept(int level, int round, int questtype){
		if (sqrateconfig.isEmpty())
			return null;
		List<GeneralFormat> result = null;
		for (Rate nsqr : sqrateconfig){
			if (level >= nsqr.levelmin && level <= nsqr.levelmax){
				result = nsqr.rounds.get(round % 10);
				break;
			}
		}
		if (result == null)
			return null;
		int totalrate = 0;
		for (int i=0; i< result.size(); i++){
			if (result.get(i).getId() != questtype)
				totalrate += result.get(i).getRate();
		}
		
		if(totalrate == 0){
			logger.error("师门任务配置有误！ level=" + level +",round=" + round + ",questtype=" + questtype);
		}
		int randrate = mkdb.Mkdb.random().nextInt(totalrate);
		int pilerate = 0;
		for (int i=0; i< result.size(); i++){
			if (result.get(i).getId() != questtype){
				pilerate += result.get(i).getRate();
				if (randrate < pilerate)
					return result.get(i);
			}
		}
		return null;
	}
	
	public java.util.Set<Integer> getSendMailServicesId() {
		return this.sendMailServicesId;
	}
	
	public CircleTaskTeamLeaderAward getTeamLeaderAward(int questId) {
		if (sqteamLeaderCfg != null) {
			return sqteamLeaderCfg.get(questId);
		}
		
		return null;
	}
	
	public boolean giveRoleCTItemCollectGoto(long roleid, xbean.CircleTaskInfo sqinfo, int posx, int posy, boolean isnpc) {
		
		if (sqinfo == null)
			return false;
		
		sqinfo.setDstx(posx);
		sqinfo.setDsty(posy);

		CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, isnpc);

		return true;
	}
	
	public boolean giveRoleCTItemFindGoto(long roleid, xbean.CircleTaskInfo sqinfo, int posx, int posy, boolean isnpc) {
		
		if (sqinfo == null)
			return false;
		
		sqinfo.setDstx(posx);
		sqinfo.setDsty(posy);

		CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, isnpc);

		return true;
	}
	
	
	
	
	public boolean giveRoleCTItemUseGoto(long roleid, xbean.CircleTaskInfo sqinfo, int posx, int posy, boolean isnpc) {
		
		if (sqinfo == null)
			return false;
		
		sqinfo.setDstx(posx);
		sqinfo.setDsty(posy);

		CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, isnpc);

		return true;
	}
	
	public boolean giveRoleCTKillMonsterGoto(long roleid, xbean.CircleTaskInfo sqinfo, int posx, int posy, boolean isnpc) {
		
		if (sqinfo == null)
			return false;
		
		sqinfo.setDstx(posx);
		sqinfo.setDsty(posy);

		CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, isnpc);

		return true;
	}
	
	
	public boolean giveRoleCTMailGoto(long roleid, xbean.CircleTaskInfo sqinfo, int posx, int posy, boolean isnpc) {

		if (sqinfo == null)
			return false;
		
		sqinfo.setDstx(posx);
		sqinfo.setDsty(posy);

		CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, isnpc);
		
		return true;
	}
	
	public boolean giveRoleCTPatrolGoto(long roleid, xbean.CircleTaskInfo sqinfo, int posx, int posy, boolean isnpc) {
		
		if (sqinfo == null)
			return false;
		
		sqinfo.setDstx(posx);
		sqinfo.setDsty(posy);

		CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, isnpc);

		return true;
	}
	
	
	public boolean giveRoleCTPetCatchGoto(long roleid, xbean.CircleTaskInfo sqinfo, int posx, int posy, boolean isnpc) {
		
		if (sqinfo == null)
			return false;
		
		sqinfo.setDstx(posx);
		sqinfo.setDsty(posy);

		CircleTaskManager.getInstance().refresh2role(sqinfo, roleid, isnpc);

		return true;
	}
	
	private void initCircMonsterConfig() {
		Map<Integer, SCircTaskMonsterConfig> monsterData = ConfigManager.getInstance().getConf(SCircTaskMonsterConfig.class);
		//private Map<Integer, CircMonster> circMonsterConfMap = new HashMap<Integer, CircMonster>();
		for(SCircTaskMonsterConfig conf : monsterData.values()) {
			CircMonster littleMonser = new CircMonster();
			
			String[] strs = conf.mapids.split(";");
			for (String mapidProb : strs) {
				String[] tmps = mapidProb.split("@");
				littleMonser.mapId.add(Integer.parseInt(tmps[0]));
				littleMonser.mapProb.add(Integer.parseInt(tmps[1]));
			}
			
			strs = conf.battleinfo.split(";");
			for (String battleProb : strs) {
				String[] tmps = battleProb.split("@");
				littleMonser.battleId.add(Integer.parseInt(tmps[0]));
				littleMonser.battleProb.add(Integer.parseInt(tmps[1]));
			}
			
			circMonsterConfMap.put(conf.group, littleMonser);
		}
	}
	
	private void initCircTaskConfig() {
		
	}
	
	
	public void initCircTaskRateConfig(){
		
		Map<Integer, CircTask> circtask = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTask.class);
		for (CircTask nsqctr : circtask.values()) {
			
			CircleTaskRate sqct = new CircleTaskRate();
			sqct.id = nsqctr.id;
			sqct.groupid = nsqctr.levelgroup;
			sqct.levelmin = nsqctr.levelmin;
			sqct.levelmax = nsqctr.levelmax;
			sqct.type = nsqctr.type;
			sqct.maxnum = nsqctr.round * nsqctr.ring;
			for (String str : nsqctr.getQuestrate()){
				sqct.rounds.add(fire.pb.util.ParseString.parseString2GeneralFormat(str));
			}
			sqctrateconfig.add(sqct);
			
			CircleTaskClass ctClass = sqctClassCfg.get(nsqctr.type);
			if (ctClass == null) {
				ctClass = new CircleTaskClass();
				ctClass.circletype = nsqctr.type;
				sqctClassCfg.put(nsqctr.type, ctClass);
			}
			
			CircleTaskGroup ctGroup = ctClass.sqctGroup.get(nsqctr.levelgroup);
			if (ctGroup == null) {
				ctGroup = new CircleTaskGroup();
				ctGroup.circletype = nsqctr.type;
				ctGroup.groupid = nsqctr.levelgroup;
				ctGroup.levelmax = nsqctr.levelmax;
				ctGroup.levelmin = nsqctr.levelmin;
				
				ctClass.sqctGroup.put(nsqctr.levelgroup, ctGroup);
			}
			
			if (nsqctr.levelrate != 0) {
				ctGroup.levelRate.add(nsqctr.levelrate);
			}
			ctGroup.listSQCT.add(sqct);
			
			if (nsqctr.getTeamleaderaward() != null) {
				CircleTaskTeamLeaderAward teamLeaderAward = new CircleTaskTeamLeaderAward();
				for (String str : nsqctr.getTeamleaderaward()) {
					String award[] = str.split("@");
					teamLeaderAward.itemIds.add(Integer.parseInt(award[0]));
					teamLeaderAward.awardRate.add(Integer.parseInt(award[1]));
				}
				
				sqteamLeaderCfg.put(nsqctr.type, teamLeaderAward);
			}
			
			if (nsqctr.getSpecialevent() != null) {
				CircleTaskEventGroup sqctEvent = new CircleTaskEventGroup(nsqctr.getSpecialevent());
				sqctEventGroup.put(nsqctr.id, sqctEvent);
			}
		}
	}
	
	

	private void initCTSchoolItemCollConfig() {
		Map<Integer, CircTaskItemCollect> rawData = ConfigManager.getInstance().getConf(CircTaskItemCollect.class);
		for (CircTaskItemCollect sCTItemCollect : rawData.values()) {
			
			CircTaskSchoolItemCollectConf ctschoolItemCollConf = 
					new CircTaskSchoolItemCollectConf(sCTItemCollect.id,sCTItemCollect.ctgroup,sCTItemCollect.school,
							sCTItemCollect.levelmin,sCTItemCollect.levelmax,sCTItemCollect.itemid, sCTItemCollect.itemnum);
			
			String[] strs = sCTItemCollect.getMapid().split(";");
			for (String mapidProb : strs) {
				String[] tmps = mapidProb.split("@");
				ctschoolItemCollConf.getMapids().add(Integer.parseInt(tmps[0]));
				ctschoolItemCollConf.getMapProbs().add(Integer.parseInt(tmps[1]));
			}
			strs = sCTItemCollect.monsterid.split(";");
			for (int i=0;i<strs.length;i++) {
				List<Integer> mapMonsters = ctschoolItemCollConf.getMonsters().get(i);
				if (mapMonsters == null){
					mapMonsters = new ArrayList<Integer>();
					ctschoolItemCollConf.getMonsters().put(i, mapMonsters);
				}
				
				String[] tmps = strs[i].split(",");
				for (String string : tmps) {
					String[] tmps2 = string.split("@");
					mapMonsters.add(Integer.parseInt(tmps2[0]));
				}
			}
			ctschoolItemCollConfMap.put(sCTItemCollect.id, ctschoolItemCollConf);
		}
	}
	
	

	private void initCTSchoolPatrolConfig() {
		Map<Integer, CircTaskPatrol> rawData = ConfigManager.getInstance().getConf(CircTaskPatrol.class);
		for (CircTaskPatrol sCircTaskPatrol : rawData.values()) {
			CircTaskSchoolPatrolConf ctSchoolPatrolConf = new CircTaskSchoolPatrolConf(sCircTaskPatrol.id, 
																sCircTaskPatrol.ctgroup,
																sCircTaskPatrol.schoolid,
																sCircTaskPatrol.levelmin,
																sCircTaskPatrol.levelmax);
			String[] strs = sCircTaskPatrol.mapid.split(";");
			for (String mapidProb : strs) {
				String[] tmps = mapidProb.split("@");
				ctSchoolPatrolConf.getMapids().add(Integer.parseInt(tmps[0]));
				ctSchoolPatrolConf.getMapProbs().add(Integer.parseInt(tmps[1]));
			}
			
			strs = sCircTaskPatrol.battlecfgid.split(";");
			for (String battlecfgidProb : strs) {
				String[] tmps = battlecfgidProb.split("@");
				ctSchoolPatrolConf.getBattleCFGs().add(Integer.parseInt(tmps[0]));
				ctSchoolPatrolConf.getBattleCFGProbs().add(Integer.parseInt(tmps[1]));
			}
			
			ctschoolPatrolConfMap.put(sCircTaskPatrol.id, ctSchoolPatrolConf);
		}
	}
	
	public void initItems(){
		
		Map<Integer, SItemToPos> itemToPos = ConfigManager.getInstance().getConf(fire.pb.map.SItemToPos.class);
		
		for(SItemToPos itempos : itemToPos.values()) {
			ItemRandomToMapPos randomPos = new ItemRandomToMapPos();
			if (itempos.getMapList() != null) {
				String [] str = itempos.getMapList().split(";");
				for (String mapinfo : str) {
					String[] tmp = mapinfo.split("@");
					randomPos.mapList.add(Integer.parseInt(tmp[0]));
					randomPos.mapRateList.add(Integer.parseInt(tmp[1]));
				}
				
				randomPos.remotePos = itempos.getMapRemotePos();
			}
			
			itemToPosConfMap.put(itempos.getId(), randomPos);
		}
	}
	
	

	public boolean isCTItemUse(long roleid, int itemid) {
		
		final fire.pb.PropRole role = new fire.pb.PropRole(roleid, true);
		int roleSchool = role.getSchool();

		NavigableMap<Integer, CircTaskItemUse> ctitemuse = ConfigManager.getInstance().getConf(fire.pb.circletask.CircTaskItemUse.class);
		for (CircTaskItemUse ctiu : ctitemuse.values()) {
			if ((roleSchool == ctiu.school || ctiu.school == 0) && itemid == ctiu.itemid) {
				return true;
			}
		}

		return false;
	}
	
	

	//是否是传说要求的地图
	public boolean isLegendMap(int level, int mapid) {
		for (SWorldMapConfig cwm : legendmaps.values()) {
			if(mapid == cwm.id && level <= cwm.LevelLimitMax && level >= cwm.LevelLimitMin )
				return true;
		}
		return false;
	}
	
	

	public boolean isSendMailService(int serviceid) {
		return this.sendMailServicesId.contains(new Integer(serviceid));
	}
	
	@Override
	public void onEvent(Event e) {
		long rid = e.getRoleid();
		
		if (e instanceof BattleEndEvent) {
			BattleEndEvent battleEndEvent = (BattleEndEvent) e;
			
			if(UtilHelper.isWinBattle(battleEndEvent)) {
				//如果赢了才判断马戏团传说
				RoleAnYeTask rayt = new RoleAnYeTask(rid);
				for(Map.Entry<Integer, xbean.AnYeTask> ent : rayt.getTasks().entrySet()){
					xbean.AnYeTask ayt = ent.getValue();
					if (ayt.getState() == SpecialQuestState.SUCCESS)
						continue;//已完成的马戏团任务不管了
					if (ent.getKey() != rayt.getAnYeData().getLegendtask())
						continue;//只判断当前传说的那个一个任务！其他都不管了
					long now = Calendar.getInstance().getTimeInMillis();
					if(ayt.getKind() == CircTaskClass.CircTask_ItemFind && ayt.getLegend() == 2 && now < ayt.getLegendend()) {
						//判断等级地图对不对
						Role role = RoleManager.getInstance().getRoleByID(rid);
						if (role == null)
							continue;
						int curLevelMapid = CircleTaskManager.getInstance().getCTCurLevelMap(rid);
						if (role.getMapId() != curLevelMapid)
							continue;
						//这种情况下加次数，次数到了，给物品
						ayt.setParam3(ayt.getParam3() + 1L);
						RoleAnYeTask.logger.info("角色[" + rid + "]传说马戏团任务" + ent.getKey() + ",id[" + ayt.getId() + "],当前次数:" + ayt.getParam3() + ",需求次数:" + ayt.getParam2());
						if(ayt.getParam3() >= ayt.getParam2()) {
							int targetitem = ayt.getLegenditem() > 0 ? ayt.getLegenditem() : ayt.getDstitemid();
							//尼玛要处理品质,只能自己瞎逼搞
							Pack bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(rid, fire.pb.item.BagTypes.BAG, false);	
							fire.pb.item.ItemBase titem = fire.pb.item.Module.getInstance().getItemManager().genItemBase(targetitem, 1);
							if (titem instanceof FoodItem) {
								((FoodItem) titem).setQuality(1);
							}			
							if (bag.isFull())
							{
								fire.pb.item.ItemMaps tempBag = fire.pb.item.Module.getInstance().getItemMaps(rid, BagTypes.TEMP, false);
								if (tempBag.doAddItem(titem, -1, "马戏团传说", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_guaji, targetitem) != AddItemResult.SUCC)
								{
									continue;
								}
							}
							else
							{
								if(bag.doAddItem(titem, -1, "马戏团传说", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_guaji, targetitem) != AddItemResult.SUCC)
								{
									continue;
								}
							}	
							//把这个传说去掉,还要取消掉定时器
							//rayt.getAnYeData().setLegendtask(999);
							ayt.setLegend(3);
							fire.pb.talk.MessageMgr.sendMsgNotify(rid, 166119, null);
							xbean.AnYeLegendFuture efur = xtable.Anyelegendtimer.get(rid);
							if (efur != null && efur.getLegendfuture() != null){
								efur.getLegendfuture().cancel(true);
							}
							xtable.Anyelegendtimer.remove(rid);
						}
					}
				}
			}
			
			if (CircleTask.isHaveItemCollect(e.getRoleid()) > 0) {
				List<xbean.FailedMonster> monsters = battleEndEvent.getBattleResult().getFailedmonsters();
				CircleTask sq = new CircleTask(rid, false);
				for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
					xbean.CircleTaskInfo sqinfo = sq.getSpecialQuestInfo(curType);
					if (sqinfo == null)
						continue;
					if (sqinfo.getQuestclass() != CircTaskClass.CircTask_ItemCollect) 
						continue;
					Role role = RoleManager.getInstance().getRoleByID(rid);
					if (role == null)
						continue;
					if (role.getMapId() != sqinfo.getDstmapid()) 
						continue;
					
					int newDstitemnum = sqinfo.getDstitemnum() + monsters.size();
					if (newDstitemnum >= sqinfo.getBattlenum()) 
						newDstitemnum = sqinfo.getBattlenum();
					BagUtil.addItem(role.getRoleID(), sqinfo.getDstitemid(), 
							newDstitemnum - sqinfo.getDstitemnum(), 
							"circtask quest item collect", 
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_Task, 
							curType);
					sqinfo.setDstitemnum(newDstitemnum);
					
					if (sqinfo.getDstitemnum() >= sqinfo.getBattlenum()){
						sq.stepState(curType, SpecialQuestState.DONE);
					} else {
						final SRefreshQuestData data = new SRefreshQuestData();
						data.questid = curType;
						data.datas.put(RefreshDataType.DEST_ITEM1_NUM, (long)sqinfo.getDstitemnum());
						mkdb.Procedure.psendWhileCommit(rid, data);
					}
				}
			}
			if (CircleTask.isHaveKillMonster(e.getRoleid()) > 0) {
				List<xbean.FailedMonster> monsters = battleEndEvent.getBattleResult().getFailedmonsters();
				//java.util.Map<Integer, xbean.Monster> mm2 = battleEndEvent.getBattleResult().getMonsters();
				CircleTask sq = new CircleTask(rid, false);
				for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
					xbean.CircleTaskInfo sqinfo = sq.getSpecialQuestInfo(curType);
					if (sqinfo == null)
						continue;
					if (sqinfo.getQuestid() == 1050000) {
						if (sqinfo.getQuestclass() != CircTaskClass.CircTask_KillMonster)
							continue;
						Role role = RoleManager.getInstance().getRoleByID(rid);
						if (role == null)
							continue;
						//if (role.getMapId() != sqinfo.getDstmapid()) 
							//continue;		
						
						//判断战斗中是否有好友
						xbean.FriendGroups groups = xtable.Friends.get(rid);
						if (groups == null) {
							continue;
						}
						boolean canDo = false;
						java.util.Map<Long, Integer> battleroles = battleEndEvent.getBattleResult().getRoleids();
						for(java.util.Map.Entry<Long, Integer> ent : battleroles.entrySet()) {
							if(groups.getFriendmap().get(ent.getKey()) != null) {
								canDo = true;
								break;
							}
						}
						
						if (canDo) {
							int num = 0;
							int monsterid = sqinfo.getDstitemid();
							for (xbean.FailedMonster monster : monsters) {
								if (monsterid == 0 || monster.getMonsterid() == monsterid)
									num++;
							}
							//sqinfo.setDstnpcid(sqinfo.getDstnpcid() + num); //设置杀怪计数
							sqinfo.setDstx(sqinfo.getDstx() + num); //设置杀怪计数
							if (sqinfo.getDstx() >= sqinfo.getDstitemnum()){
								//sq.stepState(curType, SpecialQuestState.DONE);
								new UpdateCircleTaskState(rid, curType, SpecialQuestState.DONE).call();
							} else {
								final SRefreshQuestData data = new SRefreshQuestData();
								data.questid = curType;
								data.datas.put(RefreshDataType.DEST_XPOS, (long)sqinfo.getDstx());
								mkdb.Procedure.psendWhileCommit(rid, data);
							}
						}
					} else {
						if (sqinfo.getQuestclass() != CircTaskClass.CircTask_KillMonster)
							continue;
						Role role = RoleManager.getInstance().getRoleByID(rid);
						if (role == null)
							continue;
						//if (role.getMapId() != sqinfo.getDstmapid()) 
							//continue;		
						int num = 0;
						int monsterid = sqinfo.getDstitemid();
						for (xbean.FailedMonster monster : monsters) {
							if (monsterid == 0 || monster.getMonsterid() == monsterid)
								num++;
						}
						//sqinfo.setDstnpcid(sqinfo.getDstnpcid() + num); //设置杀怪计数
						sqinfo.setDstx(sqinfo.getDstx() + num); //设置杀怪计数
						if (sqinfo.getDstx() >= sqinfo.getDstitemnum()){
							//sq.stepState(curType, SpecialQuestState.DONE);
							new UpdateCircleTaskState(rid, curType, SpecialQuestState.DONE).call();
						} else {
							final SRefreshQuestData data = new SRefreshQuestData();
							data.questid = curType;
							data.datas.put(RefreshDataType.DEST_XPOS, (long)sqinfo.getDstx());
							mkdb.Procedure.psendWhileCommit(rid, data);
						}
					}
				}
			}
		}
		else if (e instanceof LeaveTeamSpecialQuestEvent) {
//			LeaveTeamSpecialQuestEvent leaveTeamEvent = (LeaveTeamSpecialQuestEvent)e;
			Role memberRole = RoleManager.getInstance().getRoleByID(rid);
			if (memberRole == null)
				return;
			MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(memberRole.getMapId());
			if (cfg == null) {
				return;
			}
			
			if (cfg.dynamic != 0)
				return;

			PGiveUpCatchItTask giveup = new PGiveUpCatchItTask(rid, SpecialQuestID.catchIt);
			giveup.call();
			for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
				final CircTask ct = CircleTaskManager.getInstance().getCircTask(rid, curType);
				if (ct == null) {
					continue;
				}
				if (ct.teamnum > 0 && ct.teamshare == 1)
					new PGiveUpCircleTask(rid, curType).call();
			}
		}
		else if (e instanceof ArriveTeamSpecialQuestEvent) {
			Role memberRole = RoleManager.getInstance().getRoleByID(rid);
			if (memberRole == null)
				return;
			MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(memberRole.getMapId());
			if (cfg == null) {
				return;
			}
			
			if (cfg.dynamic != 0)
				return;
			
			ArriveTeamSpecialQuestEvent teamEvent = (ArriveTeamSpecialQuestEvent)e;
			final long leadreId = teamEvent.getRoleid();
			final long applierId = teamEvent.getApplierRoleId();
			
			xbean.CircleTaskMap questMapLeadre = xtable.Rolecircletask.get(leadreId);
			if (null == questMapLeadre) {
				return;
			}
			
			{
				xbean.CircleTaskInfo questInfoLeadre = questMapLeadre.getTaskmap().get(SpecialQuestID.catchIt);
				if (null == questInfoLeadre) {
					return;
				}
				
				if (questInfoLeadre.getQueststate() != SpecialQuestState.UNDONE)
					return;
				
				//判断入队玩家等级是否够
				final CircTask ct = CircleTaskManager.getInstance().getCircTask(questInfoLeadre.getId());
				PropRole pRole = new PropRole(applierId, true);
				
				if (pRole.getLevel() < ct.getLevelmin())
					return;
				
				xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(applierId);
				if (null == quest_map) {
					quest_map = xbean.Pod.newCircleTaskMap();
					xtable.Rolecircletask.insert(applierId, quest_map);
				}
				xbean.CircleTaskInfo questinfo = quest_map.getTaskmap().get(SpecialQuestID.catchIt);
				if (null == questinfo) {
					questinfo = xbean.Pod.newCircleTaskInfo();
					quest_map.getTaskmap().put(SpecialQuestID.catchIt, questinfo);
				}
				
				final long now = Calendar.getInstance().getTimeInMillis();
				
				questinfo.setId(questInfoLeadre.getId());
				questinfo.setDstitemid(questInfoLeadre.getDstitemid());
				questinfo.setDstmapid(questInfoLeadre.getDstmapid());
				questinfo.setDstnpcid(questInfoLeadre.getDstnpcid());
				questinfo.setDstnpckey(questInfoLeadre.getDstnpckey());
				questinfo.setDstx(questInfoLeadre.getDstx());
				questinfo.setDsty(questInfoLeadre.getDsty());
				questinfo.setLastgiveuptime(0);
				questinfo.setQuestid(questInfoLeadre.getQuestid());
				questinfo.setNpcname(questInfoLeadre.getNpcname());
				questinfo.setQueststate(SpecialQuestState.UNDONE);
				questinfo.setQuesttype(questInfoLeadre.getQuesttype());
				questinfo.setQuestclass(questInfoLeadre.getQuestclass());
				questinfo.setRound(questInfoLeadre.getRound());
				questinfo.setFirsttasktime(now);
				//判断是否清0的代码放到这里
				if (!fire.pb.util.DateValidate.inTheSameDay(questinfo.getTakequesttime(), now)) {
					questinfo.setSumnum(0);
					questinfo.setCatchitsingletime(0);
					questinfo.setCatchitdoubletime(0);
				}
				questinfo.setTakequesttime(now);
				questinfo.setDoneparam(questInfoLeadre.getDoneparam());
				CircleTaskManager.getInstance().refresh2role(questinfo, applierId, false);
			}
			
			for(Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
				if(SpecialQuestID.catchIt == curType)
					continue;
				
				xbean.CircleTaskInfo questInfoLeadre = questMapLeadre.getTaskmap().get(curType);
				if (null == questInfoLeadre) {
					continue;
				}		
				if (questInfoLeadre.getQueststate() != SpecialQuestState.UNDONE)
					continue;
				
				final CircTask ct = CircleTaskManager.getInstance().getCircTask(rid, curType);
				if (ct == null) {
					continue;
				}
				if (ct.teamnum > 0 && ct.teamshare == 1) {
					xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(applierId);
					if (null == quest_map) {
						quest_map = xbean.Pod.newCircleTaskMap();
						xtable.Rolecircletask.insert(applierId, quest_map);
					}
					xbean.CircleTaskInfo questinfo = quest_map.getTaskmap().get(curType);
					if (null == questinfo) {
						questinfo = xbean.Pod.newCircleTaskInfo();
						quest_map.getTaskmap().put(curType, questinfo);
					}
					
					final long now = Calendar.getInstance().getTimeInMillis();
					
					questinfo.setId(questInfoLeadre.getId());
					questinfo.setDstitemid(questInfoLeadre.getDstitemid());
					questinfo.setDstmapid(questInfoLeadre.getDstmapid());
					questinfo.setDstnpcid(questInfoLeadre.getDstnpcid());
					questinfo.setDstnpckey(questInfoLeadre.getDstnpckey());
					questinfo.setDstx(questInfoLeadre.getDstx());
					questinfo.setDsty(questInfoLeadre.getDsty());
					questinfo.setLastgiveuptime(0);
					questinfo.setQuestid(questInfoLeadre.getQuestid());
					questinfo.setNpcname(questInfoLeadre.getNpcname());
					questinfo.setQueststate(SpecialQuestState.UNDONE);
					questinfo.setQuesttype(questInfoLeadre.getQuesttype());
					questinfo.setQuestclass(questInfoLeadre.getQuestclass());
					questinfo.setRound(questInfoLeadre.getRound());
					questinfo.setFirsttasktime(now);
					questinfo.setTakequesttime(now);
					questinfo.setDoneparam(questInfoLeadre.getDoneparam());
					CircleTaskManager.getInstance().refresh2role(questinfo, applierId, false);
				}
			}
		}
	}
	
	public boolean process(long roleid, long npckey, int serviceid) {

		fire.pb.map.Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		if (npc == null)
			return false;
		switch (serviceid) {
		case NpcServices.CIRCTASK_SCHOOL1:
		case NpcServices.CIRCTASK_SCHOOL2:
		case NpcServices.CIRCTASK_SCHOOL3:
		case NpcServices.CIRCTASK_SCHOOL4:
		case NpcServices.CIRCTASK_SCHOOL5:
		case NpcServices.CIRCTASK_SCHOOL6: {
			//new PGiveRoleCircTask(roleid, npckey, npc.getNpcID()).submit();
		}
			break;
		case NpcServices.CIRCTASK_SCHOOL_SUBMIT1:
		case NpcServices.CIRCTASK_SCHOOL_SUBMIT2:
		case NpcServices.CIRCTASK_SCHOOL_SUBMIT3:
		case NpcServices.CIRCTASK_SCHOOL_SUBMIT4:
		case NpcServices.CIRCTASK_SCHOOL_SUBMIT5:
		case NpcServices.CIRCTASK_SCHOOL_SUBMIT6:{
			CircleTask sq = new CircleTask(roleid, true);
			int curState = sq.getSpecialQuestState(1010000);
			if (curState != SpecialQuestState.DONE) {
				return false;
			}
			new PSubmitCircleTask(1010000, roleid, npckey, new java.util.ArrayList<fire.pb.npc.SubmitUnit>()).submit();
			return true;
		}
		case NpcServices.SEND_MAIL: {
			new PSendMail2Dst(roleid, npc.getNpcID()).submit();
			return true;
		}
		case NpcServices.CATCH_IT: {
			return true;
		}
		}
		return true;
	}
	
	public void refresh2role(xbean.CircleTaskInfo sqinfo, long roleid, boolean isnpcgive) {
		CircleTaskManager.logger.info("refresh2role:" + roleid + ",questid:" + sqinfo.getQuestid());
		
		if (sqinfo.getQueststate() == fire.pb.circletask.SpecialQuestState.RECYCLE)
			return;
		
		if (sqinfo.getQueststate() <= 0)
			return;
		
		final CircTask ctask = CircleTaskManager.getInstance().getCircTask(sqinfo.getId());
		if (ctask == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]x循环任务配置表:" + sqinfo.getId() + ",未找到数据!");
			return;
		}
		
		final long now = Calendar.getInstance().getTimeInMillis();
		
		int circle = ctask.getCycle();
		//判断是否清0的代码放到这里
		switch(circle) {
		case 1: {
			if (!fire.pb.util.DateValidate.inTheSameDay(sqinfo.getTakequesttime(), now)) {
				if (ctask.getClearround() == 1) {
					sqinfo.setRound(1);
					sqinfo.setSumnum(0);
				}
			}
			break;
		}
		case 2: {
			if (!fire.pb.util.DateValidate.inTheSameWeek(sqinfo.getTakequesttime(), now)) {
				if (ctask.getClearround() == 1) {
					sqinfo.setRound(1);
					sqinfo.setSumnum(0);
				}
			}
			break;
		}
		case 3: {
			if (!fire.pb.util.DateValidate.inTheSameMonth(sqinfo.getTakequesttime(), now)) {
				if (ctask.getClearround() == 1) {
					sqinfo.setRound(1);
					sqinfo.setSumnum(0);
				}
			}
			break;
		}
		default:
			break;
		}
		
		final SRefreshSpecialQuest send = new SRefreshSpecialQuest();
		send.dstitemid = sqinfo.getDstitemid();
		send.dstitemnum = sqinfo.getDstitemnum();
		send.dstmapid = sqinfo.getDstmapid();
		send.dstnpcid = sqinfo.getDstnpcid();
		send.dstnpckey = sqinfo.getDstnpckey();
		Npc npc = SceneNpcManager.selectNpcByKey(send.dstnpckey);
		if (npc != null)
			send.dstnpcname = npc.getName();
		send.questid = sqinfo.getQuestid();
		send.queststate = sqinfo.getQueststate();
		send.questtype = sqinfo.getQuesttype();
		send.dstx = sqinfo.getDstx();
		send.dsty = sqinfo.getDsty();
		send.round = sqinfo.getRound();
		send.sumnum = sqinfo.getSumnum();
		send.dstitemid2 = sqinfo.getBattlenum();
		if (isnpcgive) {
			// 这里表示是从npc处接的任务
			send.islogin = 2;
		} else {
			send.islogin = 0;
		}
		int questtype = sqinfo.getQuesttype();
		if (questtype == SpecialQuestType.Demonstrate
				|| questtype == SpecialQuestType.Rescue
				|| questtype == SpecialQuestType.Tame) {
			BuffAgent agent = new BuffRoleImpl(roleid, true);
			ConstantlyBuff buff = agent.getBuff(BUFF_ID);
			if (buff != null)
				send.validtime = System.currentTimeMillis()
						+ buff.getRemianingTime(agent);
		}
		if (sqinfo.getQuesttype() == SpecialQuestType.CaiJi) {
			send.dstitemidnum2 = sqinfo.getSametimes();
		}

		if (sqinfo.getQuestclass() == CircTaskClass.CircTask_ItemFind
				&& sqinfo.getDoneparam() > 0) {
			send.dstitemidnum2 = sqinfo.getDoneparam();
		}

		CircleTaskManager.logger.info(new StringBuilder().append("角色[")
				.append(roleid).append("]循环任务[").append(sqinfo.getQuestid()).append("]发送信息=").append
				(",npcid:").append(sqinfo.getDstnpcid()).append
				(",npckey:").append(sqinfo.getDstnpckey()).append(",send!"));
		if (mkdb.Transaction.current() != null)
			mkdb.Procedure.psendWhileCommit(roleid, send);
		else
			gnet.link.Onlines.getInstance().send(roleid, send);
	}
	
	
	// 跟上一个方法区别是发送的消息是ActiveQuestData
	public void refresh2roleActiveQuestData( long roleid) {
		xbean.TaskInfos infos = xtable.Roletasks.select(roleid);
		if (infos==null)
			return;
		for (xbean.TaskDlgInfo sqinfo : infos.getTasksmap().values()) {
			
			if (sqinfo.getTaskstate()==SpecialQuestState.ABANDONED||sqinfo.getTaskstate()==SpecialQuestState.SUCCESS) {
				continue;
			}
			int taskid = sqinfo.getTaskid();
//			if (taskid==MissionConst.BOSI_TASKID||taskid==PSchoolAnswerClear.SCHOOL_ANSWER_ID){
//				if (!DateValidate.inTheSameDay(sqinfo.getEndtime(),System.currentTimeMillis())){
//					fire.pb.mission.Module.getInstance().untrackQuest(roleid, taskid);
//					continue;
//				}
//			}
			ActiveQuestData aqd = new ActiveQuestData();
			aqd.questid = taskid;
			aqd.dstmapid = sqinfo.getDstmapid();
			aqd.dstnpcid = sqinfo.getDstnpcid();
			aqd.dstnpckey = sqinfo.getDstnpckey();
			aqd.dstx = sqinfo.getDstx();
			aqd.dsty = sqinfo.getDsty();
			aqd.queststate = sqinfo.getTaskstate();
			aqd.dstitemid = sqinfo.getDstitemid();
			aqd.sumnum = sqinfo.getSumnum();
			aqd.rewardsmoney = sqinfo.getRound();
			if (sqinfo.getEndtime()>0)
				aqd.rewardmoney = sqinfo.getEndtime();
			SRefreshActiveQuest sraq = new SRefreshActiveQuest();
			sraq.questdata = aqd;
			mkdb.Procedure.psendWhileCommit(roleid, sraq);
		}
	}
	
	
	public void refresh2roleOnEnterWorld(xbean.CircleTaskInfo sqinfo, long roleid) {
		CircleTaskManager.logger.info("refresh2roleOnEnterWorld:" + roleid + ",questid:" + sqinfo.getQuestid());
		if (sqinfo.getQueststate() == fire.pb.circletask.SpecialQuestState.RECYCLE) 
			return;
		if (sqinfo.getQueststate()<=0) 
			return;
		
		final CircTask ctask = CircleTaskManager.getInstance().getCircTask(sqinfo.getId());
		if (ctask == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]x循环任务配置表:" + sqinfo.getId() + ",未找到数据!");
			return;
		}
		
		if (ctask.teamnum > 0) {
			//判断还有无队伍buff
			if (!fire.pb.buff.Module.existState(roleid, fire.pb.buff.BuffConstant.StateType.STATE_TEAM)) {
				sqinfo.setDstitemnum(0);
				sqinfo.setDstmapid(0);
				sqinfo.setDstnpcid(0);
				sqinfo.setDstnpckey(0);
				sqinfo.setDstx(0);
				sqinfo.setDsty(0);
				sqinfo.setQueststate(SpecialQuestState.ABANDONED);
				sqinfo.setQuesttype(0);
				sqinfo.setBattlenum(0);
				sqinfo.setNpcname("");
				sqinfo.setBigaward(0);
				sqinfo.setRound(0);
				
				final SRefreshSpecialQuestState sendstate = new SRefreshSpecialQuestState();
				sendstate.questid = sqinfo.getQuestid();
				sendstate.state = SpecialQuestState.ABANDONED;
				
				if (mkdb.Transaction.current() != null)
					mkdb.Procedure.psendWhileCommit(roleid, sendstate);
				else
					gnet.link.Onlines.getInstance().send(roleid, sendstate);
				
				new mkdb.Procedure() {
					@Override
					protected boolean process() {
						SReqMissionCanAccept send = new SReqMissionCanAccept();
						fire.pb.mission.Module.getInstance().getCanAcceptMission(roleid, send.missions);
						mkdb.Procedure.psend(roleid, send);
						return true;
					}
				}.call();
				return;
			}
		}
		else {
			final long now = Calendar.getInstance().getTimeInMillis();
			
			int circle = ctask.getCycle();
			//判断是否清0的代码放到这里
			switch(circle) {
			case 1: {
				if (!fire.pb.util.DateValidate.inTheSameDay(sqinfo.getTakequesttime(), now)) {
					if (ctask.getClearround() == 1) {
						sqinfo.setRound(1);
						sqinfo.setSumnum(0);
					}
				}
				break;
			}
			case 2: {
				if (!fire.pb.util.DateValidate.inTheSameWeek(sqinfo.getTakequesttime(), now)) {
					if (ctask.getClearround() == 1) {
						sqinfo.setRound(1);
						sqinfo.setSumnum(0);
					}
				}
				break;
			}
			case 3: {
				if (!fire.pb.util.DateValidate.inTheSameMonth(sqinfo.getTakequesttime(), now)) {
					if (ctask.getClearround() == 1) {
						sqinfo.setRound(1);
						sqinfo.setSumnum(0);
					}
				}
				break;
			}
			default:
				break;
			}
		}

		final SRefreshSpecialQuest send = new SRefreshSpecialQuest();
		send.dstitemid = sqinfo.getDstitemid();
		send.dstitemnum = sqinfo.getDstitemnum();
		send.dstmapid = sqinfo.getDstmapid();
		send.dstnpcid = sqinfo.getDstnpcid();
		send.dstnpckey = sqinfo.getDstnpckey();
		Npc npc = SceneNpcManager.selectNpcByKey(send.dstnpckey);
		if (npc != null && npc.getNpcID() == sqinfo.getDstnpcid())
			send.dstnpcname = npc.getName();
		else {
			//在原有位置上新建一个npc
			if (sqinfo.getQuestclass() == CircTaskClass.CircTask_CatchIt && ctask.teamnum == 0) {
				if (sqinfo.getDstnpcid() > 0) {
					long npckey = SceneNpcManager.getInstance().getNextId();
					Position pos = new GridPos(sqinfo.getDstx(), sqinfo.getDsty()).toPosition();
					SceneNpcManager.createNpcByPos(npckey, sqinfo.getDstnpcid(), sqinfo.getNpcname(), sqinfo.getDstmapid(), 0, pos.getX(), pos.getY(), 0, 0);

					send.dstnpckey = npckey;
					sqinfo.setDstnpckey(npckey);
					send.dstnpcname = sqinfo.getNpcname();
					
					CatchItQuest.addCatchItQuestTimer(roleid, sqinfo.getQuestid(), sqinfo.getId());
				}
			}
		}
		send.questid = sqinfo.getQuestid();
		send.queststate = sqinfo.getQueststate();
		send.questtype = sqinfo.getQuesttype();
		send.dstx = sqinfo.getDstx();
		send.dsty = sqinfo.getDsty();
		send.round = sqinfo.getRound();
		send.sumnum = sqinfo.getSumnum();
		send.dstitemid2 = sqinfo.getBattlenum();
		send.islogin = 1;
		int questtype = sqinfo.getQuesttype();
		if (questtype==SpecialQuestType.Demonstrate||questtype==SpecialQuestType.Rescue||questtype==SpecialQuestType.Tame) {
			BuffAgent agent = new BuffRoleImpl(roleid,true);
			ConstantlyBuff buff =agent.getBuff(BUFF_ID);
			if(buff!=null)
			  send.validtime = System.currentTimeMillis()+buff.getRemianingTime(agent);
		}
		if (sqinfo.getQuesttype() == SpecialQuestType.CaiJi){
			send.dstitemidnum2 = sqinfo.getSametimes();
		}
		
		if (sqinfo.getQuestclass() == CircTaskClass.CircTask_ItemFind && sqinfo.getDoneparam() > 0) {
			send.dstitemidnum2 = sqinfo.getDoneparam();
		}
	
		CircleTaskManager.logger.info("refresh2roleOnEnterWorld:" + roleid + ",questid:" + sqinfo.getQuestid() + ",send!");
		if (mkdb.Transaction.current() != null)
			mkdb.Procedure.psendWhileCommit(roleid, send);
		else
			gnet.link.Onlines.getInstance().send(roleid, send);
	}
	
	
	public void refreshQuestState(long roleid, int questid, int state) {

		final SRefreshSpecialQuestState sendstate = new SRefreshSpecialQuestState();
		sendstate.questid = questid;
		sendstate.state = state;
		mkdb.Procedure.psendWhileCommit(roleid, sendstate);
	}
	
	
	public void sendSpecialQuestWhileEnterWord(long roleid) {

		SSendActiveQuestList questList = new SSendActiveQuestList();
		//ActiveQuestData data = null;

		// 发送家族特产任务
//		xbean.SpecialProductTask task = xtable.Familyspecialproduct.select(roleid);
//		if (null != task) {
//			if (task.getTaskstep() == SpecialQuestState.DONE) {
//				data = FamilySpecialProductExecutor.getExecutor().createSpecialProductTaskData(task.getTaskcout(), task.getCurrenttasknpcinfo());
//				questList.memberlist.add(data);
//			}else if(task.getTaskstep() == SpecialQuestState.FAIL){
//				data = FamilySpecialProductExecutor.getExecutor().createSpecialProductTaskData(task.getTaskcout(), task.getCurrenttasknpcinfo());
//				data.queststate = SpecialQuestState.FAIL;
//				questList.memberlist.add(data);
//			}
//		}
		
//		//发送帮派四方巡视任务
//		xbean.PatrolTaskInfo ptInfo = xtable.Factionpatroltask.select(roleid);
//		if(null != ptInfo && ptInfo.getStatus() != SpecialQuestState.ABANDONED && ptInfo.getStatus() != SpecialQuestState.SUCCESS){
//			data = FactionPatrol.createFactionPatrolTaskData(ptInfo);  //用于进入游戏时刷新任务栏帮派四方巡视显示
//			data.queststate = ptInfo.getStatus();
//			questList.memberlist.add(data);
//		}
		
		// 如果还有结拜任务
//		xbean.SwornMemberInfo info = SwearRelationManager.getInstance().getRoleSwearTaskStep(roleid);
//		if(null != info){
//			ActiveQuestData quest = SwearRelationManager.getInstance().getRefreshTaskBySwearStep(info);
//			if(null != quest){
//				questList.memberlist.add(quest);
//			}
//		}
		

		// 留给其他特殊任务的接口，在此处添加任务
		CircleTaskManager.getInstance().refresh2roleActiveQuestData( roleid);
		
		// data = new ActiveQuestData();
		// questList.memberlist.add(data);

		// ---------end---------


		// 师徒
		

		// end
		if (questList.memberlist.size() == 0)
			return;

		gnet.link.Onlines.getInstance().send(roleid, questList);

	}
	
	/**
	 * 获取角色的公会任务次数
	 * @param roleid
	 * @return
	 */
	public int getGongHuiRenWuTimes(final long roleid) {
		xbean.CircleTaskMap quest_map = xtable.Rolecircletask.select(roleid);
		if (null == quest_map)
			return 0;
		xbean.CircleTaskInfo sq = quest_map.getTaskmap().get(1060000);
		if (sq == null)
			return 0;
		
		return sq.getSumnum();
	}
}
