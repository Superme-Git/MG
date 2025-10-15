package fire.pb.mission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.PAddExpProc;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.event.AddGemToEquip;
import fire.pb.event.EquipItemEvent;
import fire.pb.event.ForgeDecorationEvent;
import fire.pb.event.RefineEquipEvent;
import fire.pb.event.SetProtectPasswordEvent;
import fire.pb.item.CombineItemEvent;
import fire.pb.item.EnterBingFengInstEvent;
import fire.pb.item.EnterJingYingInstEvent;
import fire.pb.item.Equip;
import fire.pb.item.EquipMakeEvent;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Pack;
import fire.pb.item.RoleAddPointEvent;
import fire.pb.map.SceneManager;
import fire.pb.master.MasterManager;
import fire.pb.mission.util.AnswerCommitParam;
import fire.pb.mission.util.EMissionCommit;
import fire.pb.mission.util.GMCommitParam;
import fire.pb.mission.util.IMissionCommitParam;
import fire.pb.mission.util.MoneyCommitParam;
import fire.pb.mission.util.NpcselectCommitParam;
import fire.pb.mission.util.PetCommitParam;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.pet.PetTypeEnum;
import fire.pb.talk.MessageMgr;
import fire.pb.util.MessageUtil;
import fire.pb.util.RolePropConf;

public abstract class RoleMission implements IRoleMission {

	private final String getStateStr( final int state ) {
		switch( state ) {
		case MissionStatus.ABANDON: return "已放弃";
		case MissionStatus.COMMITED: return "已提交";
		case MissionStatus.FAILED: return "已失败";
		case MissionStatus.FINISHED: return "已完成";
		case MissionStatus.PROCESSING: return "未完成";
		case MissionStatus.UNACCEPT: return "未接受";
		default:
			return "未知";
		}
	}
	
	@Override
	public String toString() {
		return "任务ID="+ getId() + ", 状态:" + getStateStr( getState() );
	}
	protected final long roleid;
	protected final boolean readonly;
	protected MissionConfig conf;
	protected xbean.Mission data;
	
	public long getRoleId() {
		return roleid;
	}

	public boolean isReadonly() {
		return readonly;
	}
	
	public RoleMission(final long roleid, final boolean readonly) {
		this.roleid = roleid;
		this.readonly = readonly;
	}
	
	public MissionConfig getConf() {
		return conf;
	}
	
	public SSpecialScenarioQuestConfig getSConf() {
		return conf.getSpecialScenarioQuestConfig();
	}
	
	public boolean isFairylandQuest() {
		if (conf == null)
			return false;
		return UtilHelper.isFairylandMap(conf.exeIndo.mapID);
	}
	
	boolean accept(int taskid,int round, Integer initValue) {
		data.setId(taskid);
		data.setStatus(MissionStatus.PROCESSING);
		data.setDeadline(0);
		data.setTaskvalue(0);
		data.setRound(round);
		conf = MissionManager.getInstance().getMissionConfig(getId());
		if (!_setTaskvalue(initValue == null ? initValue() : initValue)) {
			return false;
		}
		return true;
	}
	
	public static boolean isScenarioIgnoreMap(long roleid, int mapid) {
		MissionColumn tsc = new MissionColumn(roleid, true);
		for (RoleMission mission : tsc) {
			if (mission.getConf().exeIndo.ignoreMaps.contains(new Integer(mapid))) {
				SceneManager.logger.debug("主线任务:" + mission.getId() +",配置了不遇怪地图:" + mapid);
				return true;
			}
		}
		return false;
	}
	
	public static int getScenarioPatroling(long roleid) {
		MissionColumn tsc = new MissionColumn(roleid, true);
		int taskid = 0;
		for (RoleMission mission : tsc) {
			if (mission.getConf() != null &&
					UtilHelper.getMissionExeType(mission.getConf().exeIndo.missionType) == MissionExeTypes.PATROL) {
				taskid = mission.getId();
				break;
			}
		}
		return taskid;
	}
	
	static RoleMission getScenarioTaskInstance(final long roleid, final int lineid) {
		final int id = UtilHelper.getMissionId(lineid, UtilHelper.getFirstStep());
		if (UtilHelper.isBranchScenarioMission(id)) {
			xbean.AcceptedScenarioMission branchdata = xtable.Acceptedscenariomission.get(roleid);
			if (branchdata == null) {
				branchdata = xbean.Pod.newAcceptedScenarioMission();
				xtable.Acceptedscenariomission.insert(roleid, branchdata);
			} 
			xbean.Mission ret = branchdata.getAccepted().get(lineid);
			if (ret == null) {
				ret = xbean.Pod.newMission();
				ret.setId(UtilHelper.getMissionId(lineid, UtilHelper.getLastStep()));
				ret.setStatus(MissionStatus.UNACCEPT);
				branchdata.getAccepted().put(lineid, ret);
			}
			return new MissionBranchScenario(roleid, ret);
		}
		if (UtilHelper.isMajorScenarioMission(id))
			return new MissionMajorScenario(roleid, false);
		return null;
	}
	
	
	public static void handleGuangwu(long roleid){
		ArrayList<Integer> taskIds = new ArrayList<Integer>();
		taskIds.add(206406);
		for(int taskid : taskIds){
		//	final int lineid = Helper.getTaskLineid(taskid);
			final RoleMission mission = new MissionColumn(roleid, false).getMission(taskid);
			//if (mission == null)
				//return;
		//	final TaskScenario mission = TaskScenario.getScenarioTaskInstance(roleid, lineid);
			if(mission==null || mission.getId() < 1) continue;
			Map<Integer, SSpecialScenarioQuestConfig> cfgs = fire.pb.main.ConfigManager.getInstance().getConf(SSpecialScenarioQuestConfig.class);
			if(cfgs!=null){
				SSpecialScenarioQuestConfig cfg = cfgs.get(taskid);
				if(cfg!=null && cfg.params.size()>1){
					Equip equip = new Equip(roleid, true);
					//武器评分
					int score = fire.pb.item.Module.getInstance().getEquipScoreAll(roleid,equip.getItemByPos(cfg.params.get(0)));
					if(score >= cfg.params.get(1)){
						mission.setTaskvalue(1);
					}
				}
			}
		}
	}
	
	
	
	private int initValue() {
		if (conf == null)
			throw new NullPointerException("任务配置为null");
		switch (UtilHelper.getMissionFinType(conf.exeIndo.missionType)) {
		case MissionFinTypes.NUL: // 无
			if (UtilHelper.getMissionExeType(conf.exeIndo.missionType) == MissionExeTypes.SKILLUP) {
				//获取人物等级 和技能等级
				//int rolelevel = xtable.Properties.selectLevel(roleid);
				fire.pb.skill.SkillRole sr = new fire.pb.skill.SkillRole(getRoleId(), true);
				if(false == sr.checkhaveskillcanupgrade()){
					return 1;
				} else {
					return 0;
				}
			}
			if (UtilHelper.getMissionExeType(conf.exeIndo.missionType) == MissionExeTypes.EQUIP) {
				return 0;
			}
			return 0;
		case MissionFinTypes.AREA:
			return 0;
		case MissionFinTypes.COUNT:
			return 0;
		case MissionFinTypes.ITEM:
			if (UtilHelper.getMissionExeType(conf.exeIndo.missionType ) != MissionExeTypes.USE ) {
				fire.pb.item.Pack bag = new fire.pb.item.Pack(getRoleId(), true);
				int itemid = conf.exeIndo.targetID;
				int num = bag.getItemNum(itemid, 0);
				return num;
			}
			return 0;
		case MissionFinTypes.ITEMCOUNT: //  复合计数,战斗机次数
			return 0;
		case MissionFinTypes.MONEY:
			final long money = fire.pb.item.Module.getInstance().getMoney(roleid);
			if (money > Integer.MAX_VALUE)
				return Integer.MAX_VALUE;
			return (int)money;
		case MissionFinTypes.PET:
			fire.pb.pet.PetColumn pc = 
				new fire.pb.pet.PetColumn( 
					roleid, fire.pb.pet.PetColumnTypes.PET, false );
			int petnum = 0;
			for (fire.pb.Pet pet : pc.getAllProtocolPets()) {
				if (pet.id == conf.exeIndo.giveBackPetID && pet.kind == PetTypeEnum.WILD) {
					petnum++;
				}
			}
			return petnum;
		case MissionFinTypes.LEVEL:
			return xtable.Properties.selectLevel(roleid);
		case MissionFinTypes.OTHER:
			return getOtherTaskInitValue();
		default:
			return 0;
		}
	}
	
	private int getOtherTaskInitValue() {
		SSpecialScenarioQuestConfig sconf = conf.getSpecialScenarioQuestConfig();
		if (sconf == null)
			return 0;
		int type = sconf.类型ID;
		switch (type) {
//		case Helper.EQUIP_QIANGHUA:
//		{
//			Equip equip = new Equip(roleid, true);
//			if (sconf.params == null || sconf.params.isEmpty()){
//				int level = 0;
//				for (ItemBase bi : equip) {
//					if (bi != null && (bi instanceof EquipItem)) {
//						if (bi.getItemAttr().等级 > level)
//							level = bi.getItemAttr().等级;
//					}
//				}
//				return level;
//			} else {
//				int pos = sconf.params.get(0);
//				EquipItem ei = (EquipItem)equip.getItemByPos(pos);
//				if (ei == null) {
//					return 0;
//				} else {
//					return ei.getItemAttr().等级;
//				}
//			}
//		}
		case UtilHelper.JOIN_CREATE_FACTION://加入或创建公会
			//检测是否有公会
			//xbean.ClanInfo claninfo = ClanUtils.getClanInfoById(roleid, false);//用这个会死锁
			xbean.Properties pro = xtable.Properties.select(roleid);
			if(pro.getClankey() > 0)
				return 1;
		}
		
		return 0;
	}

//	private void _setMissionDeadline() {
//		if (conf != null && conf.activeInfo.timelimit > 0) {
//			Calendar now = Calendar.getInstance();
//			now.add( Calendar.MINUTE, conf.activeInfo.timelimit );
//			long deadline = now.getTimeInMillis();
//			if ( deadline == 0 ) {
//				deadline++;
//			}
//			data.setDeadline( deadline );
//		}
//	}
	
	private boolean _setTaskvalue(final int value) {
		if (data.getStatus() == MissionStatus.PROCESSING || data.getStatus() == MissionStatus.FINISHED) {
			data.setTaskvalue(value);
			SRefreshMissionValue p = new SRefreshMissionValue();
			p.missionid = getId();
			p.missionidvalue = value;
			p.missionidround = data.getRound();
			mkdb.Procedure.psendWhileCommit(roleid, p);
			final boolean isFinish = isTaskFinished();
			if (data.getStatus() != MissionStatus.FINISHED && isFinish) {
				Module.logger.debug(getRoleId() + " taskid:"+getId() + "已经完成");
				data.setStatus(MissionStatus.FINISHED);
//				SRefreshScenarioQuestState send = new SRefreshScenarioQuestState();
//				send.questid = getID();
//				send.queststate = getState();
//				xdb.Procedure.psendWhileCommit(getRoleid(), send);
			} else if ( data.getStatus() == MissionStatus.FINISHED && !isFinish ) {
				Module.logger.debug(getRoleId() + " taskid:"+getId() + "没有完成");
				// 设置为初始状态,可能有的任务并没有这些参数
				data.setStatus( MissionStatus.PROCESSING );
//				SRefreshScenarioQuestState send = new SRefreshScenarioQuestState();
//				send.questid = getID();
//				send.queststate = getState();
//				xdb.Procedure.psendWhileCommit(getRoleid(), send);
			}
			return true;
		}
		return false;
	}
	
	public boolean setTaskvalue(final int value) {
		_setTaskvalue(value);
		if (getState() == MissionStatus.FINISHED) {
			if (isAutoCommit()) {
				if (!new PCommitMajorMission(roleid, getId(), true).call()) {
					return false;
				}
				return true;
			}
		}
		SRefreshMissionState send = new SRefreshMissionState();
		send.missionid = getId();
		send.missionstatus = getState();
		if (send.missionid< 1001 || send.missionid>  999999)
			Module.logger.error("Marshal Error.method:setTaskvalue.taskid:"+send.missionid);
		mkdb.Transaction.tsendWhileCommit(roleid, send);
		return true;
	}


	public MissionReward commit(IMissionCommitParam commitparam, boolean isCapital) {
		MissionReward curReward = new MissionReward();
		if (!(commitparam instanceof GMCommitParam)) {
			if (!struckOffCommit(commitparam, isCapital)) {
				return null;
			}
			if (!drawReward(commitparam, curReward, isCapital)) {
				return null;
			}
		}
		if (commitparam.getNpcKey() > 0) {
			final int npcid = fire.pb.map.SceneNpcManager.selectNpcByKey(commitparam.getNpcKey()).getNpcID();
			if (conf.exeIndo.npcID != npcid) {
				Module.logger.error( "剧情任务NpcID不符合" );
				return null;
			}
		}
		
		if(getId() == 102018){
			int curlevel = xtable.Properties.selectLevel(roleid);
			if(curlevel<35){
			}
		}
		data.setStatus(MissionStatus.COMMITED);
		SRefreshMissionState p = new SRefreshMissionState();
		p.missionid = getId();
		p.missionstatus = MissionStatus.COMMITED;
		if (p.missionid< 1001 || p.missionid>  999999)
			Module.logger.error("Marshal Error.method:commit.taskid:"+p.missionid);
		mkdb.Transaction.tsendWhileCommit(roleid, p);
		showMissionResult(isCapital);
		data.getPath().add(getId());
		return curReward;
	}

	public SAcceptMission toProtocol() {
		SAcceptMission send = new SAcceptMission();
		send.missioninfo.dstnpckey = 0;//conf.activeInfo.npcID;
		send.missioninfo.missionid = getId();
		send.missioninfo.missionvalue = data.getTaskvalue();
		send.missioninfo.missionstatus = getState();
		send.missioninfo.missionround = data.getRound();
		return send;
	}

	//检测特殊类型任务是否完成
	private boolean checkOtherSpecailQuest() {
		SSpecialScenarioQuestConfig sconf = conf.getSpecialScenarioQuestConfig();
		if ( sconf == null ) {
			throw new IllegalArgumentException( "任务id=" + getId() + "配置有误" );
	//		return false;
		}
		int type = sconf.类型ID;
		switch (type) {
		case UtilHelper.JOIN_CREATE_FACTION://加入或创建公会
			return getTaskvalue() >= 1;
		case UtilHelper.GEMCOMBINE://宝石合成
			if ( sconf.params == null || sconf.params.size() < 1 ) {
				return getTaskvalue() >= 1;
			}
			return getTaskvalue() >= sconf.params.get(0);
		case UtilHelper.COMPLETEDTASK: {
			if ( sconf.params == null || sconf.params.isEmpty() ) {
				return false;
			}
			return getTaskvalue() >= sconf.params.get(0);
		}
		case UtilHelper.XUEWEILEVEL: {
			if ( sconf.params == null || sconf.params.isEmpty() ) {
				return false;
			}
			return getTaskvalue() >= sconf.params.get(0);
		}
		case UtilHelper.COMPLETEDSCHOOLTASK: {
			if ( sconf.params == null || sconf.params.isEmpty() || sconf.params.size() < 2 ) {
				Module.logger.error("checkOtherSpecailQuest params error!taskid:"+this.getId());
				return false;
			}
			return getTaskvalue() >= sconf.params.get( 1 );
		}
		case UtilHelper.EQUIP_MAKE://装备打造
			if ( sconf.params == null || sconf.params.size() < 1 ) {
				return getTaskvalue() >= 1;
			}
			return getTaskvalue() >= sconf.params.get(0);
		case UtilHelper.GEM_ADD://宝石镶嵌
			if ( sconf.params == null || sconf.params.size() < 1 ) {
				return getTaskvalue() >= 1;
			}
			return getTaskvalue() >= sconf.params.get(0);
		case UtilHelper.ADD_ROLEPOINT:
			if ( sconf.params == null || sconf.params.size() < 1 ) {
				return getTaskvalue() >= 1;
			}
			return getTaskvalue() >= sconf.params.get(0);
		case UtilHelper.JINGYING_INST:
			if ( sconf.params == null || sconf.params.size() < 1 ) {
				return getTaskvalue() >= 1;
			}
			return getTaskvalue() >= sconf.params.get(0);
		case UtilHelper.BINGFENG_INST:
			if ( sconf.params == null || sconf.params.size() < 1 ) {
				return getTaskvalue() >= 1;
			}
			return getTaskvalue() >= sconf.params.get(0);
		default: 
			return false;
		}
	}
	

	public int getTaskvalue() {
		switch (UtilHelper.getMissionFinType(conf.exeIndo.missionType)) {
		case MissionFinTypes.MONEY:
			long curmoney =  new Pack(roleid, true).getMoney();
			return curmoney > Integer.MAX_VALUE?Integer.MAX_VALUE:(int)curmoney;
		case MissionFinTypes.LEVEL:
			Integer rolelevel = xtable.Properties.selectLevel(roleid);
			if (rolelevel == null)
				return 0;
			return rolelevel.intValue();
		case MissionFinTypes.OTHER:
			return getSpecailTaskvalue();
		default:
			return data.getTaskvalue();
		}
	}
	
	private int getSpecailTaskvalue() {
		SSpecialScenarioQuestConfig sconf = conf.getSpecialScenarioQuestConfig();
		if ( sconf == null ) {
			throw new IllegalArgumentException( "任务id=" + getId() + "配置有误" );
	//		return false;
		}
		return data.getTaskvalue();
	}
	
	boolean isTaskFinished() {
		switch ( UtilHelper.getMissionFinType( conf.exeIndo.missionType ) ) {
		case MissionFinTypes.NUL: // 无
			if ( UtilHelper.getMissionExeType( conf.exeIndo.missionType ) ==
				MissionExeTypes.AIBATTLE ) {
				return false;
			} else if ( UtilHelper.getMissionExeType( conf.exeIndo.missionType ) == MissionExeTypes.PATROL) {
				return false;
			} else if ( UtilHelper.getMissionExeType( conf.exeIndo.missionType ) == MissionExeTypes.BUY) {
				if(conf.exeIndo.targetID == 0)
					return true;
				else
					return getTaskvalue() == conf.exeIndo.targetID;
			} else if (UtilHelper.getMissionExeType(conf.exeIndo.missionType) == MissionExeTypes.SKILLUP) {
				return getTaskvalue() > 0;
			} else if (UtilHelper.getMissionExeType(conf.exeIndo.missionType) == MissionExeTypes.EQUIP) {
				return getTaskvalue() > 0;
			}
			return true;
		case MissionFinTypes.AREA:
			return true;
		case MissionFinTypes.COUNT:
			return getTaskvalue() >= conf.battleInfo.battleTimes;
		case MissionFinTypes.ITEM:
			if (UtilHelper.getMissionExeType(conf.exeIndo.missionType ) == MissionExeTypes.USE) {
				return true;
			}
			if ( UtilHelper.getMissionExeType( conf.exeIndo.missionType ) == MissionExeTypes.PRACTISEAREA )
				return conf.battleInfo.dropItemNum <= getTaskvalue();
			else
				return conf.exeIndo.targetNum <= getTaskvalue();
		case MissionFinTypes.ITEMCOUNT: //  复合计数,战斗机次数
			return conf.battleInfo.monsterNum <= getTaskvalue();
		case MissionFinTypes.MONEY:
			Module.logger.info("任务" +getId()+ "金钱为" + getTaskvalue());
			return conf.exeIndo.giveBackMoney <= getTaskvalue();
		case MissionFinTypes.PET:
			return getTaskvalue() > 0;
		case MissionFinTypes.QUESTION:
			return true;
		case MissionFinTypes.LEVEL:
			return getTaskvalue() >= conf.limitInfo.minLevel;
		case MissionFinTypes.OTHER:
			return checkOtherSpecailQuest();
		default:
			return false;
		}
	}

	@Override
	public int getState() {
		return data.getStatus();
	}
	
	boolean isAutoCommit() {
		final int exeType = UtilHelper.getMissionExeType(conf.exeIndo.missionType);
		final int finishType = UtilHelper.getMissionFinType(conf.exeIndo.missionType);
		if(exeType == MissionExeTypes.SKILLUP && finishType == MissionFinTypes.NUL) {
			return true;
		}
		if(exeType == MissionExeTypes.EQUIP && finishType == MissionFinTypes.NUL) {
			return true;
		}
		if (exeType == MissionExeTypes.NUL && finishType == MissionFinTypes.NUL) {
			if (this.data.getId() == 180107 || 
					this.data.getId() == 180207 ||
					this.data.getId() == 180307 ||
					this.data.getId() == 180407 ||
					this.data.getId() == 180507 ||
					this.data.getId() == 180607)//这些是升级技能任务，只用一次，写死不自动完成.
				return false;
			return true;
		}
		if (exeType == MissionExeTypes.PRACTISEAREA) {
			if (finishType == MissionFinTypes.ITEMCOUNT || finishType == MissionFinTypes.COUNT ||finishType == MissionFinTypes.ITEM)
				return true;
			return false;
		}
		if (exeType == MissionExeTypes.BUY){
			return true;
		}
		if (finishType == MissionFinTypes.LEVEL)
			return true;
		if (finishType == MissionFinTypes.OTHER) {
			return true;
		}
		return false;
	}
	
//	private boolean checkZhenfaElement(ItemCommitParam param) {
//		int zhenfaid = getTaskvalue();
//		Map<Integer, SFormationConfig> configs = fire.pb.main.ConfigManager.getInstance().getConf(SFormationConfig.class);
//		if (configs == null)
//			return false;
//		SFormationConfig config = configs.get(zhenfaid);
//		Set<Integer> elements = new HashSet<Integer>();
//		elements.addAll(config.elements);
//		if (elements.size() != param.getItems().size()) {
//			return false;
//		}
//		Bag bag = new Bag(roleid, false);
//		for (Map.Entry<Integer, Integer> commitItemElement : param.getItems().entrySet()) {
//			BasicItem bi = bag.getItem(commitItemElement.getKey());
//			if (!elements.contains(bi.getItemid()))
//				return false;
//			elements.remove(bi.getItemid());
//		}
//		if (!elements.isEmpty())
//			return false;
//		for (int itemkey : param.getItems().keySet()) {
//			if (bag.removeItemByKey(itemkey, 1, fire.log.YYLogger.COUNTER_TYPE, 7,"任务提交扣除") != 1)
//				return false;
//		}
//		return true;
//	}
	
//	private boolean checkXinItem(ItemCommitParam param) {
//		Bag bag = new Bag(roleid, false);
//		Set<Integer> needXins = new HashSet<Integer>();
//		needXins.add(50261);
//		needXins.add(50263);
//		needXins.add(50262);
//		for (Map.Entry<Integer, Integer> commitItemElement : param.getItems().entrySet()) {
//			BasicItem bi = bag.getItem(commitItemElement.getKey());
//			if (!needXins.contains(bi.getItemid()))
//				return false;
//			needXins.remove(bi.getItemid());
//		}
//		if (!needXins.isEmpty())
//			return false;
//		for (int itemkey : param.getItems().keySet()) {
//			if (bag.removeItemByKey(itemkey, 1, fire.log.YYLogger.COUNTER_TYPE, 7,"任务提交扣除") != 1)
//				return false;
//		}
//		return true;
//	}
	
	private boolean struckOffCommit(IMissionCommitParam commitparam, boolean isCapital) {
		final int tasktype = UtilHelper.getMissionFinType(conf.exeIndo.missionType);
		switch (tasktype) {
		case MissionFinTypes.ITEM:
		{
			final int exetype = UtilHelper.getMissionExeType(conf.exeIndo.missionType);
			if (exetype == MissionExeTypes.USE) { 
				if (!isCapital && conf.aiInfo.aIID == 0) {
					fire.pb.item.ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(
							conf.exeIndo.useItemID);
					if (attr == null)
						return false;
					if (attr.useup == 1) {
						fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
						if (bag.removeItemById(conf.exeIndo.useItemID, 
								1, 
								fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 7,"任务提交扣除") != 1) {
							List<String> parameters = new ArrayList<String>();
							parameters.add(attr.name);
							fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 143911, parameters);
							return false;
						}
						return true;
					}
				}
				break;
			}
			if (exetype == MissionExeTypes.PRACTISEAREA) {
				break;
			}
			
			if (UtilHelper.getMissionExeType(conf.exeIndo.missionType) == MissionExeTypes.NPC) {
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
				if (commitparam.getCommitType() == EMissionCommit.SIMPLEITEM) {
					if (bag.removeItemById(conf.exeIndo.targetID, conf.exeIndo.targetNum, 
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, getId(),"任务提交扣除") != conf.exeIndo.targetNum) {
						fire.pb.item.ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(
								conf.exeIndo.targetID);
						if (attr != null) {
							List<String> parameters = new ArrayList<String>();
							parameters.add(attr.name);
							fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 143911, parameters);
						}
						return false;
					}
					ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(conf.exeIndo.targetID);
					if (attr != null) {
						List<String> parameters = new ArrayList<String>();
						parameters.add(attr.name);
						fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 142991, parameters);
					}
				} else {
					if (bag.removeItemById(conf.exeIndo.targetID, 
							conf.exeIndo.targetNum, 
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 
							7,"任务提交扣除") != conf.exeIndo.targetNum) {
						return false;
					}
				}
			}
			break;
		}
		case MissionFinTypes.MONEY:
		{
			if (commitparam instanceof MoneyCommitParam) {
				final int money = (int)((MoneyCommitParam)commitparam).getMoney();
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
				if (conf.exeIndo.giveBackMoney > money) {
					Module.logger.error( "金钱不足" );
					fire.pb.map.Npc npc = fire.pb.map.SceneNpcManager.selectNpcByKey(commitparam.getNpcKey());
					if (npc != null)
						fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 143254, npc.getNpcID(), null);
					return false;
				}
//				if (conf.activeInfo.giveBackMoney != money) {
//					Module.logger.debug( "提交金钱数量不正确" );
//					return false;
//				}
				if (bag.subMoney(-conf.exeIndo.giveBackMoney, "任务提交扣除",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 
						getId()) == -conf.exeIndo.giveBackMoney) {
					return true;
				}
			}
			return false;
		}
		case MissionFinTypes.PET:
		{
			if (commitparam.getCommitType() != EMissionCommit.PET) {
				Module.logger.error("任务id=" + getId() + "是提交宠物任务,但是并没有提交宠物");
				return false;
			}
			PetCommitParam param = (PetCommitParam)commitparam;
			fire.pb.pet.PetColumn pc = new fire.pb.pet.PetColumn(
					roleid, fire.pb.pet.PetColumnTypes.PET, false );
			final int petkey = param.getPetkey();
			xbean.PetInfo pi = pc.getPetInfo(petkey);
			if (pi == null)
				return false;
			if  (conf.exeIndo.giveBackPetID == pi.getId()) {
				return pc.removePet(petkey, PetColumn.REMOVE_REASON_TASK,getId()) == 0;
			} else {
				fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 144418, null);
			}
			return false;
		}
		case MissionFinTypes.OTHER:
		{
//			if (Helper.getOtherQuestParamType(conf.getSpecialScenarioQuestConfig().类型ID) == 
//					Helper.OtherQuestParamType.MASS_ITEM) {
//				if (getID() == Helper.tuibian100submitxin) {
//					if (!(commitparam instanceof ItemCommitParam))return false;
//					ItemCommitParam param = (ItemCommitParam)commitparam;
//					if (!checkXinItem(param))
//						return false;
//				}
//			}
		}
		}
		
	//	}
		return true;
	}
	
	int getNext(final IMissionCommitParam param) {
		if (param.getCommitType() != EMissionCommit.GM) {
			int next = -1;
			if ( UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.QUESTION ) {
				if ( param.getCommitType() != EMissionCommit.ANSWER ) {
					Module.logger.error( "答题不对"+getId() );
					throw new IllegalArgumentException( "答题不对"+getId() );
				}
				if ( ((AnswerCommitParam)param).getAnswerid() == conf.answerInfo.correctAnswerID ) {
					next = 0;
				} else {
					next = 1;
				}
			} else if ( UtilHelper.getMissionExeType( conf.exeIndo.missionType ) == MissionExeTypes.NPC &&
				UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.NUL ) {
				if ( param.getCommitType() != EMissionCommit.NPCSELECT ) {
					next = -1;
				} else {
					next = ((NpcselectCommitParam)param).getOptionid();
				}
			}
			return next;
		} else {
			return -1;
		}
	}
	
	protected final boolean onMissionLineCompleted() {
		final long roleid = getRoleId();
		final int taskline = UtilHelper.getMissionLineid(getId());
		MissionColumn tsc = new MissionColumn(roleid, false);
		if (UtilHelper.isBranchScenarioMission(getId())) {
			tsc.removeMissionline(taskline);
		}
		xbean.CommitedScenarioMission cst = xtable.Commitedscenariomission.get(roleid);
		if (cst == null) {
			cst = xbean.Pod.newCommitedScenarioMission();
			cst.getCommitted().add(taskline);
			xtable.Commitedscenariomission.insert(roleid, cst);
		} else {
			if (!cst.getCommitted().contains(taskline))
				cst.getCommitted().add(taskline);
		}
		if(this.getId() /  10000 == 51) {
			fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.LevelContinueEvent(roleid, this.getId()));
		}
		
		return tsc.refreshPostMissionLine(taskline);
	}
	
	
	private boolean drawReward(IMissionCommitParam commitparam, MissionReward curReward, boolean isCapital) {
		if (conf.rewardInfo.expReward > 0) {
			if (!new fire.pb.PAddExpProc(roleid, conf.rewardInfo.expReward, false, PAddExpProc.SCENARIO_QUEST, "剧情任务").call()) {
				Module.logger.error( "奖励人物经验出错" );
			}
			curReward.addRewardExp(conf.rewardInfo.expReward);
		}
		if (conf.rewardInfo.moneyReward > 0) {
			Pack bag = new Pack(roleid, false);
			long addmoney = bag.addSysMoney(conf.rewardInfo.moneyReward, "任务奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_ScenarioTask, getId() );
			ArrayList<String> params = new ArrayList<String>();
			params.add(String.valueOf(addmoney));
			//fire.pb.talk.Message.psendMsgNotifyWhileCommit(roleid, 140983, params);
			curReward.addRewardMoney(conf.rewardInfo.moneyReward);
		}
		if(curReward.getRewardMoney() > 0 && curReward.getRewardExp() > 0){
			ArrayList<String> params = new ArrayList<String>();
			params.add(String.valueOf(curReward.getRewardMoney()));
			params.add(String.valueOf(curReward.getRewardExp()));
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 162055, params);
		}
		
		if (conf.rewardInfo.petExpReward > 0) {
			xbean.Properties prop = xtable.Properties.select(roleid);
			if ( prop.getFightpetkey() != -1 ) {
				mkdb.Procedure proc = new fire.pb.pet.PAddPetExpProc(roleid, prop.getFightpetkey(), conf.rewardInfo.petExpReward);
				mkdb.Procedure.pexecuteWhileCommit(proc);
			}
		}
		if ( conf.rewardInfo.rewardItemList != null && !conf.rewardInfo.rewardItemList.isEmpty() ) {	
			if (conf.rewardInfo.rewardType == 1) {
				fire.pb.item.Pack bag = new fire.pb.item.Pack( roleid, false );
				if (conf.rewardInfo.rewardOption == 1) {
					//奖励道具，全给
					for (MissionConfig.ScenarioMissionRewardItem rewardItem : conf.rewardInfo.rewardItemList) {
						if (rewardItem.needShape != 0) {
							final int roleShape = RolePropConf.getShapeidByXshapeid(xtable.Properties.selectShape(roleid));
							if (roleShape != rewardItem.needShape) {
								continue;
							}	
						}
//						final Integer cardid = 
//								fire.pb.item.Module.getInstance().getItemManager().getCardByItem(rewardItem.itemid);
//						if ( cardid != null ) {
//							fire.pb.item.Module.getInstance().addCard( roleid, cardid, fire.log.YYLogger.COUNTER_TYPE, getID() );
//							continue;
//						}
//						BasicItem item = fire.pb.item.Module.getInstance().getItemManager().genBasicItem(rewardItem.itemid, rewardItem.itemnum);
//						
//						if (rewardItem.isBind) {
//							item.setFlag(fire.pb.Item.BIND);
//						}
						int added=bag.doAddItem(rewardItem.itemid, rewardItem.itemnum, 0, rewardItem.isBind?fire.pb.Item.BIND:0,
								"任务奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_Task, getId());
						if (added != rewardItem.itemnum) {
							fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 144875, null);
							return false;
//							ItemAttr itemattr = fire.pb.item.Module.getInstance().getItemManager().getAttr(rewardItem.itemid);
//							if (itemattr instanceof TaskItemAttr) {
//								TaskItemAttr attr = (TaskItemAttr)itemattr;
//								if (attr.是否放入任务道具页 == 1) {
//									fire.pb.talk.Message.psendMsgNotifyWhileRollback(roleid, 144875, null);
//									return false;
//								}
//							}
//							fire.pb.item.Temp temp = (fire.pb.item.Temp)
//									fire.pb.item.Module.getInstance().getItemColumn( 
//									roleid, fire.pb.item.BagTypes.TEMP, false );
//							temp.addItem(rewardItem.itemid, rewardItem.itemnum-added, 0, 
//									rewardItem.isBind?fire.pb.Item.BIND:0,
//									"任务奖励", fire.log.YYLogger.COUNTER_TYPE, getID());
							
						}
//						fire.pb.item.Helper.sendAddItemMsg(roleid, rewardItem.itemid, rewardItem.itemnum);
						MessageUtil.psendAddItemWhileCommit(roleid, rewardItem.itemid, rewardItem.itemnum);
						curReward.addRewardItem(rewardItem.itemid, rewardItem.itemnum);
					}
				} else if (conf.rewardInfo.rewardOption == 2) {
					//奖励道具，选择一个
					if (commitparam != null && commitparam instanceof NpcselectCommitParam) {
						NpcselectCommitParam nParam = (NpcselectCommitParam)commitparam;
						int selectOption = nParam.getOptionid();
						for (MissionConfig.ScenarioMissionRewardItem rewardItem : conf.rewardInfo.rewardItemList) {
							if (rewardItem.needShape != 0) {
								final int roleShape = RolePropConf.getShapeidByXshapeid(xtable.Properties.selectShape(roleid));
								if (roleShape != rewardItem.needShape) {
									continue;
								}
								if (selectOption == rewardItem.itemid) {
									int added=bag.doAddItem(rewardItem.itemid, rewardItem.itemnum, 0, rewardItem.isBind?fire.pb.Item.BIND:0,
											"任务奖励", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_Task, getId());
									if (added != rewardItem.itemnum) {
										fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 144875, null);
										return false;	
									}
									//fire.pb.item.Helper.sendAddItemMsg(roleid, rewardItem.itemid, rewardItem.itemnum);
									MessageUtil.psendAddItemWhileCommit(roleid, rewardItem.itemid, rewardItem.itemnum);
									curReward.addRewardItem(rewardItem.itemid, rewardItem.itemnum);
									break;
								}
							}
						}
					}
				}
			} else if (conf.rewardInfo.rewardType == 2) {
				PetColumn col = new PetColumn(roleid, PetColumnTypes.PET, false);
				if(col.size() >= col.getCapacity())
				{
					MessageMgr.sendMsgNotify(roleid, 142377, null);
					Module.logger.error( "宠物栏已满" );
					return false;//宠物栏已满
				}
				if (conf.rewardInfo.rewardOption == 1) {
					//奖励宠物，全给
					for (MissionConfig.ScenarioMissionRewardItem rewardPet : conf.rewardInfo.rewardItemList) {
						int RewardPetid = rewardPet.itemid;
						PetAttr pattr = fire.pb.pet.Module.getInstance().getPetManager().getAttr(RewardPetid);
						if(pattr == null) {
							Module.logger.error( "PetAttr null!" );
							return false;
						}						
						int takelevel = pattr.getTakelevel();
						fire.pb.PropRole prole = new fire.pb.PropRole(roleid, false);
						if (prole.getLevel()< takelevel)
						{
							MessageMgr.sendMsgNotify(roleid, 141610, null);
							Module.logger.error( "宠物的携带等级过高!" );
							return false;//宠物的携带等级过高
						}
						int ret = col.addPetByID(RewardPetid, rewardPet.isBind?true:false, PetColumn.ADD_REASON_TASK);
						Module.logger.info("角色[" + this.roleid + "]addPetByID,bind:" + rewardPet.isBind + ",pet:" + RewardPetid );
						if(ret > 0) {
							MessageMgr.psendMsgNotifyWhileCommit(roleid, 142378, MessageMgr.getStringList(pattr.getName()));
						} else {
							Module.logger.error("角色[" + this.roleid + "]addpet fail!" );
							return false;
						}
					}			
				} else if (conf.rewardInfo.rewardOption == 2) {
					//奖励宠物，选择一个
					if (commitparam != null && commitparam instanceof NpcselectCommitParam) {
						NpcselectCommitParam nParam = (NpcselectCommitParam)commitparam;
						int selectOption = nParam.getOptionid();
						for (MissionConfig.ScenarioMissionRewardItem rewardPet : conf.rewardInfo.rewardItemList) {
							int RewardPetid = rewardPet.itemid;
							if (selectOption == RewardPetid) {
								PetAttr pattr = fire.pb.pet.Module.getInstance().getPetManager().getAttr(RewardPetid);
								if(pattr == null) {
									Module.logger.error( "PetAttr null!" );
									return false;
								}							
								int takelevel = pattr.getTakelevel();
								fire.pb.PropRole prole = new fire.pb.PropRole(roleid, false);
								if (prole.getLevel()< takelevel)
								{
									MessageMgr.sendMsgNotify(roleid, 141610, null);
									Module.logger.error( "宠物的携带等级过高!" );
									return false;//宠物的携带等级过高
								}
								int ret = col.addPetByID(RewardPetid, rewardPet.isBind?true:false, PetColumn.ADD_REASON_TASK);
								Module.logger.info("角色[" + this.roleid + "]addPetByID,bind:" + rewardPet.isBind + ",pet:" + RewardPetid );
								if(ret > 0) {
									MessageMgr.psendMsgNotifyWhileCommit(roleid, 142378, MessageMgr.getStringList(pattr.getName()));
								} else {
									Module.logger.error("角色[" + this.roleid + "]addpet fail!" );
									return false;
								}
								break;
							}
						}	
					}
				}
			}
		}
		if (conf.rewardInfo.sMoney > 0) {
			ArrayList<String> params = new ArrayList<String>();
			params.add(String.valueOf(conf.rewardInfo.sMoney));
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit( roleid, 140984, params );
			curReward.addRewardSMoney(conf.rewardInfo.sMoney);
		}
		int masterKey = MasterManager.getTaskKeyBuyTaskId(getId());
		if(masterKey > 0){
			MasterManager.getInstance().updateStlx(roleid, masterKey, 1);
		}
		return true;
	}
	
	
	int getNextTask(final int next) {
		if (conf.rewardInfo.postMissionList.size() == 1) {
			// 一个后置任务则自动接收
			return conf.rewardInfo.postMissionList.get(0);
		//	Module.logger.debug( "自动接受任务成功" + roletasks );
		} else {	
			final int roleschool = xtable.Properties.selectSchool(roleid);
			int nmbyschool = getNextTaskByShape(roleschool); //先根据角色门派判断后续任务，如果没有再根据角色"形状"判断
			if (nmbyschool > 0)
				return nmbyschool;
			final int roleshape = RolePropConf.getShapeidByXshapeid(xtable.Properties.selectShape(roleid));
			int nm = getNextTaskByShape(roleshape);
			if (nm != 0)
				return nm;
			if (next == -1)
				return conf.rewardInfo.postMissionList.get(0);
			//			throw new RuntimeException( "分支任务配置错误" );
				
			//	throw new IllegalArgumentException();
		//	}
			final Integer nextMission = conf.rewardInfo.postMissionList.get(next);
			if (nextMission == null) {
				throw new RuntimeException( "分支任务配置错误" );
			}
			return nextMission;
		}
	}
	
	protected int getNextTaskByShape( final int shape ) {
		for (int nextMission : conf.rewardInfo.postMissionList) {
			MissionConfig nconf = MissionManager.getInstance().getMissionConfig( nextMission );
			if ( nconf != null && 
					nconf.limitInfo.requireRoleIDList.contains( (long)shape ) )
				return nextMission;
		}
		return 0;
	}
	
	private void showMissionResult(boolean isCapital) {
//		if ( conf.scenarioInfo.animationID != 0 && conf.scenarioInfo.animationID != -1 ) {
//			Helper.ANIMATIONTYPE type = Helper.getAnimationType(conf.scenarioInfo.animationID);
//			if ( type == Helper.ANIMATIONTYPE.CARD ) {
//				fire.pb.item.Module.getInstance().addCard( roleid, conf.scenarioInfo.animationID, fire.log.YYLogger.COUNTER_TYPE, getID() );		
//			}
//		}
		//////
		final boolean hasNote = conf.hasNote();
		if (!hasNote) {
			conf.afterNote(roleid, isCapital);
		} else {
			conf.addShowing(roleid, isCapital);
		}
	}

	@Override
	public int getId() {
		return data.getId();
	}

	void onEvent( final fire.pb.event.Event e) {
		final MissionConfig conf = getConf();
		if (conf == null)
			return;
		SSpecialScenarioQuestConfig sconf = conf.getSpecialScenarioQuestConfig();
		if (e instanceof fire.pb.event.EquipItemEvent) {
			if (getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.OTHER ) {
			} else if(getState() == MissionStatus.PROCESSING && 
					(((EquipItemEvent)e).getItemid() == conf.exeIndo.useItemID
					|| conf.exeIndo.useItemID <= 0
					|| CircleTaskManager.isItem2Item(conf.exeIndo.useItemID, ((EquipItemEvent)e).getItemid() )) &&
					UtilHelper.getMissionExeType(conf.exeIndo.missionType) == MissionExeTypes.EQUIP &&
					UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.NUL){
				//穿装备任务,完成
				setTaskvalue(1);
				return;
			}
			
		} else if (e instanceof SetProtectPasswordEvent) {
			if (getState() == MissionStatus.PROCESSING && 
					UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.OTHER) {
			}
		} else if (e instanceof RefineEquipEvent) {
			if (getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.OTHER ) {
				
			}
		} else if (e instanceof CombineItemEvent) {
			if (getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.OTHER ) {
				if (sconf.类型ID == UtilHelper.GEMCOMBINE) {
					setTaskvalue(getTaskvalue() + 1);
				}
			}
		} else if (e instanceof EquipMakeEvent) {
			if (getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.OTHER ) {
				if (sconf.类型ID == UtilHelper.EQUIP_MAKE) {
					setTaskvalue(getTaskvalue() + 1);
				}
			}
		} else if (e instanceof RoleAddPointEvent) {
			if (getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.OTHER ) {
				if (sconf.类型ID == UtilHelper.ADD_ROLEPOINT) {
					setTaskvalue(getTaskvalue() + 1);
				}
			}
		} else if (e instanceof EnterJingYingInstEvent) {
			if (getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.OTHER ) {
				if (sconf.类型ID == UtilHelper.JINGYING_INST) {
					setTaskvalue(getTaskvalue() + 1);
				}
			}
		} else if (e instanceof EnterBingFengInstEvent) {
			if (getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.OTHER ) {
				if (sconf.类型ID == UtilHelper.BINGFENG_INST) {
					setTaskvalue(getTaskvalue() + 1);
				}
			}
		} else if (e instanceof AddGemToEquip) {
			if (getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.OTHER ) {
				if (sconf.类型ID == UtilHelper.GEM_ADD) {
//					if (sconf.params != null && !sconf.params.isEmpty() 
//							&& sconf.params.get(0) == ((AddGemToEquip)e).getItemid()) 
						this.setTaskvalue(getTaskvalue() + 1);
				}
			}
		}else if (e instanceof fire.pb.event.MoneyChangeEvent) {
			if (getState() == MissionStatus.PROCESSING && MissionFinTypes.MONEY == UtilHelper.getMissionFinType(conf.exeIndo.missionType)) {
				long value = new Pack(roleid, true).getMoney();
				if (value > Integer.MAX_VALUE) {
					value = Integer.MAX_VALUE;
				}
				Module.logger.info("设置任务" +getId()+ "金钱为" + value);
				this.setTaskvalue((int)value);
			}
		}else if (e instanceof fire.pb.event.WinUndeadChallangeEvent) {
			if ( getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.OTHER ) {
			}
		}else if ( e instanceof fire.pb.event.ReleaseInfoEvent ) {
			if ( getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.OTHER ) {
				
			}
		} else if (e instanceof fire.pb.event.ReleaseJianghuzhaoji) {
			if ( getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.OTHER ) {
				
			}
		} else if (e instanceof fire.pb.event.PetStarEvent) {
			if ( getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.OTHER ) {
				
			}
			
		}else if(e instanceof fire.pb.event.JoinCampEvent){
			if ( getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.OTHER ) {
				
			}
		}
		else if(e instanceof fire.pb.event.FactionCreateOrJoinEvent){
			if ( getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.OTHER ) {
				if (sconf.类型ID == UtilHelper.JOIN_CREATE_FACTION) {
					setTaskvalue(1);
				}
			}
		}else if(e instanceof fire.pb.event.EnterPetLuckEvent){
			if ( getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.OTHER ) {
				
			}
		}else if (e instanceof fire.pb.event.VisitSite) {
			if (getState() == MissionStatus.PROCESSING && 
					UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.OTHER) {
				
			}
		} else if ( e instanceof fire.pb.event.SetFightPetEvent ) {
			
		} else if (e instanceof fire.pb.event.CircleTaskCompleteEvent){
			if (getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.OTHER) {
				if (sconf.类型ID == UtilHelper.COMPLETEDSCHOOLTASK) {
					if (sconf.params == null || sconf.params.isEmpty()) {
						return;
					}
					fire.pb.event.CircleTaskCompleteEvent event = (fire.pb.event.CircleTaskCompleteEvent)e;
					if(CircleTaskManager.getInstance().getCircTaskTypes().contains(event.getMissionID())){
						if(sconf.params.get(0) == event.getMissionID())
							setTaskvalue(getTaskvalue() + 1);
					}
				}
			}
		} else if ( e instanceof fire.pb.event.MissionCompleteEvent ) {
			if ( getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.OTHER ) {
				if (sconf.类型ID == UtilHelper.COMPLETEDTASK) {
					if (sconf.params == null || sconf.params.size() < 2 ) {
						return;
					}
					fire.pb.event.MissionCompleteEvent event = (fire.pb.event.MissionCompleteEvent)e;
					if ( sconf.params.contains( event.getMissionID() ) ) {
						setTaskvalue(getTaskvalue() + 1);
					}
				}
			}
		} else if (e instanceof fire.pb.event.GetMasterEvent ||
				e instanceof fire.pb.event.ReleaseApprenticeInfoEvent) {
			if ( getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.OTHER) {
				
			}
		} 
		else if (e instanceof fire.pb.event.UpdateInbornLevel) {
			if ( getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.OTHER ) {
				
			} else {
				if(UtilHelper.getMissionExeType(conf.exeIndo.missionType) == MissionExeTypes.SKILLUP &&
						UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.NUL){
					//职业技能升级任务,完成
					setTaskvalue(1);
					//new PCommitMajorMission(roleid, getID(), true).call();
					return;
				}
				int taskid = getId();//淘汰的逻辑,只为代码过渡期兼容老版配置
				if(taskid == 180107 || taskid == 180207 || taskid == 180307 || taskid == 180407 || taskid == 180507 || taskid == 180607){
					//升级一次职业技能任务,直接完成
					new PCommitMajorMission(roleid, getId(), true).call();
					return;
				}
			}
		} else if (e instanceof fire.pb.event.BagItemChange) {
			fire.pb.event.BagItemChange event = (fire.pb.event.BagItemChange)e;
			final int missionstatus = getState();
			if ( missionstatus != MissionStatus.PROCESSING && missionstatus != MissionStatus.FINISHED ) {
				return;
			}
			if (UtilHelper.getMissionFinType(conf.exeIndo.missionType) == MissionFinTypes.ITEM) {
				final int countitemid;
				if (UtilHelper.getMissionExeType(conf.exeIndo.missionType)
						== MissionExeTypes.PRACTISEAREA) {
					// 练功区掉落物品战斗
					countitemid = conf.battleInfo.dropItemID;
				} else {
					countitemid = conf.exeIndo.targetID;
				}
				if (event.getItemid() == countitemid) {
					final int hasNum = new fire.pb.item.Pack( getRoleId(), true ).getItemNum(countitemid, 0);
					setTaskvalue(hasNum);
				}
				return;
				
			}
			if ( UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.OTHER ) {
				
			}
			return;
		} else if ( e instanceof fire.pb.event.LevelupEvent ) {
			if ( getState() == MissionStatus.PROCESSING && UtilHelper.getMissionFinType(conf.exeIndo.missionType ) == MissionFinTypes.LEVEL ) {
				setTaskvalue( ((fire.pb.event.LevelupEvent)e).getLevel());
			}
		} else if ( e instanceof fire.pb.event.BuyItemEvent ) {
			//购买物品任务
			if (getState() == MissionStatus.PROCESSING && UtilHelper.getMissionExeType(conf.exeIndo.missionType) == MissionExeTypes.BUY){
				fire.pb.event.BuyItemEvent event = (fire.pb.event.BuyItemEvent)e;
				int relateid = event.getRelatedid();
				if(relateid == conf.exeIndo.targetID){
					this.setTaskvalue(relateid);
				}
			}
		} else if ( e instanceof fire.pb.event.PetColumnChange ) {
			if ( ( getState() == MissionStatus.PROCESSING || getState() == MissionStatus.FINISHED	) ) {
				if ( UtilHelper.getMissionFinType( conf.exeIndo.missionType ) == MissionFinTypes.PET ) {
					fire.pb.pet.PetColumn pc = new fire.pb.pet.PetColumn( getRoleId(), fire.pb.pet.PetColumnTypes.PET, true );
					int petnum = 0;
					for ( xbean.PetInfo pi : pc.getPetsMap().values() ) {
						if (pi.getKind() != PetTypeEnum.WILD) 
							continue;
						if ( pi.getId() == conf.exeIndo.giveBackPetID ) {
							petnum++;
						}
					}
					setTaskvalue(petnum);
					return;
				}
			}
		} else if ( e instanceof fire.pb.event.BattleEndEvent ) {
			fire.pb.event.BattleEndEvent event = ( fire.pb.event.BattleEndEvent)e;
			if ( !UtilHelper.isWinBattle(event) ) {
				return;
			}
			if ( ( getState() == MissionStatus.PROCESSING || getState() == MissionStatus.FINISHED	) ) {
				if ( UtilHelper.getMissionExeType( conf.exeIndo.missionType ) == MissionExeTypes.PRACTISEAREA ) {
					final int finishType = UtilHelper.getMissionFinType( conf.exeIndo.missionType );
					MissionConfig.BattleInfo bi = conf.battleInfo;
					
					if ( finishType == MissionFinTypes.ITEM ) {
						// 掉落物品
						if ( bi.battleZoneID <= 0 || event.getBattleResult().getAreaconf() == bi.battleZoneID ){	
							final int monsternum = 
									(bi.monsterList == null || bi.monsterList.isEmpty())
									? 1 : countMonsterNum(event);
							fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
							final int hasNum = bag.getItemNum(bi.dropItemID, 0);
							int addnum = 0;
							for (int i = 0; i < monsternum; i++) {
								if (addnum >= bi.dropItemNum - hasNum) {
									break;
								}
								if (fire.pb.util.Misc.getRandomBetween(1, 100) <= bi.drop) {
									addnum+=1;
								}
							}
							if (addnum != 0) {
								final int added = bag.doAddItem(bi.dropItemID, addnum, 
										"任务掉落", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_Task, getId());
								//fire.pb.item.Helper.sendAddItemMsg(getRoleid(), bi.dropItemID, added);
								if (mkdb.Transaction.current() != null)
									MessageUtil.psendAddItemWhileCommit(getRoleId(), bi.dropItemID, added);
							//	setTaskvalue(hasNum + added);
							}
						}
					}
					if (finishType == MissionFinTypes.COUNT) {
						if ( event.getBattleResult().getAreaconf() == bi.battleZoneID) {
							setTaskvalue(getTaskvalue() + 1);
						}
					}
					if (finishType == MissionFinTypes.ITEMCOUNT) {
						List<xbean.FailedMonster> monsters = event.getBattleResult().getFailedmonsters();
						if (monsters != null) {
							int num = countMonsterNum(event);
							if (num != 0)
								setTaskvalue(getTaskvalue() + num);
						}
					}
				} else if (UtilHelper.getMissionExeType(conf.exeIndo.missionType) == MissionExeTypes.AIBATTLE ) {
					final int finishType = UtilHelper.getMissionFinType( conf.exeIndo.missionType );
					//MissionConfig.BattleInfo bi = conf.battleInfo;
					if (finishType == MissionFinTypes.NUL) {
						if (!conf.rewardInfo.hasNote 
								&& conf.rewardInfo.jtype != MissionConfig.RewardInfo.JUMPTYPE.NULL) {
							
						}
					}
				}
			}
		} else if(e instanceof ForgeDecorationEvent){
			if(this.getId()== 207603 && getState()==MissionStatus.PROCESSING){
				setTaskvalue(1);
			}
		}
		return;
	}
	
	
	private int countMonsterNum(final fire.pb.event.BattleEndEvent event) {
		if ( !conf.battleInfo.monsterList.isEmpty() ) {
			ArrayList<Integer> needMonsters = new ArrayList<Integer>();
			for ( int needMonster : conf.battleInfo.monsterList ) {
				if ( UtilHelper.isMonsterType( needMonster ) ) {
					continue;
				}
				needMonsters.add( needMonster );
			}
			List<xbean.FailedMonster> monsters = event.getBattleResult().getFailedmonsters();
			if ( monsters != null ) {
				int num = 0;
				for ( xbean.FailedMonster fm : monsters ) {
					if ( needMonsters.contains( fm.getMonsterid() ) )
						num++;
				}
				return num;
			}
		}
		return 0;
	}
	
	public boolean abandon() {
		return false;
	}
}
