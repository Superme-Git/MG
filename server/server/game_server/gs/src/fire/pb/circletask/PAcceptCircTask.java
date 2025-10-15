
package fire.pb.circletask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fire.log.LogManager;
import fire.log.YYLogger;
import fire.msp.circletask.GCreatePosForCTGoto;
import fire.pb.CalcRoleAttr;
import fire.pb.GsClient;
import fire.pb.PropRole;
import fire.pb.circletask.CircTask;
import fire.pb.circletask.CircTaskClass;
import fire.pb.circletask.CircTaskConf;
import fire.pb.circletask.CircTaskGotoType;
import fire.pb.circletask.CircTaskItemUse;
import fire.pb.circletask.SpecialQuestID;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.game.MoneyType;
import fire.pb.main.ConfigManager;
import fire.pb.npc.NpcManager;
import fire.pb.npc.SNpcShare;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.scene.MapUtil;
import fire.pb.scene.sPos.GridPos;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.BagUtil;
import fire.pb.util.GeneralFormat;
import xbean.CircleTaskInfo;

public class PAcceptCircTask extends mkdb.Procedure {

	private final long roleid;
	private final long npckey; //不用了
	private final int npcid;
	private final int circletype;
	private final int ctquestid;
	private final boolean isnpcgive; //是否npc给予
	private fire.pb.circletask.CircTask circleconf;
	private final boolean isforce; //强制给予
	
	public PAcceptCircTask(long roleid, long npckey, int npcid, int circletype, boolean isnpcgive) {
		this.roleid = roleid;
		this.npckey = npckey;
		this.npcid = npcid;
		this.circletype = circletype;
		this.ctquestid = 0;
		this.isnpcgive = isnpcgive;
		this.isforce = false;
	}
	
	public PAcceptCircTask(long roleid, long npckey, int npcid, int circletype, int ctquestid) {
		this.roleid = roleid;
		this.npckey = 0;
		this.npcid = npcid;
		this.circletype = circletype;
		this.ctquestid = ctquestid;
		this.isnpcgive = false;
		this.isforce = false;
	}
	
	public PAcceptCircTask(long roleid, long npckey, int npcid, int circletype, int ctquestid, boolean isforce) {
		this.roleid = roleid;
		this.npckey = 0;
		this.npcid = npcid;
		this.circletype = circletype;
		this.ctquestid = ctquestid;
		this.isnpcgive = false;
		this.isforce = isforce;
	}

	public boolean giveRoleSendMail(long roleid, int schoolid, CircleTaskInfo sqinfo, CircTaskConf ctconf, int submitnpc) {
		if (sqinfo == null)
			return false;
		int dst = CircleTaskManager.getCTRandomMailDest(roleid, ctconf.ctgroup, sqinfo);
		if (dst == 0) {
			CircleTaskManager.logger.error("角色[" + roleid + "]随机目标npc:" + dst + ",失败！");
			return false;
		}
		CircleTaskManager.logger.debug("角色[" + roleid + "]送信类循环任务随机目标npc:" + dst + ",服务id:" + sqinfo.getDoneparam());
		sqinfo.setQuestid(circletype);
		sqinfo.setQuesttype(ctconf.id);
		sqinfo.setSmallaward(sqinfo.getSmallaward() + 1);
		sqinfo.setBigaward(sqinfo.getBigaward() + 1);
		sqinfo.setDstitemid(0);
		sqinfo.setDstnpcid(dst);
		sqinfo.setDstnpckey(0);
		sqinfo.setLastgiveuptime(0);
		sqinfo.setQueststate(SpecialQuestState.UNDONE);
		
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleid, circletype);
		if (ct == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]循环类型:" + ctconf.id + "配置文件错误！");
			return false;
		}
		sqinfo.setRound(sqinfo.getRound() % ct.ring + 1);
		if (ct.getTaskcnt() == 1) {
			sqinfo.setSumnum(sqinfo.getSumnum() + 1);
		}
		sqinfo.setTakequesttime(Calendar.getInstance().getTimeInMillis());
		sqinfo.setAcceptquesttime(Calendar.getInstance().getTimeInMillis());
		sqinfo.setQuestclass(ctconf.cttype);
		sqinfo.setQuestgroup(ctconf.ctgroup);
		sqinfo.setAutocomp(ctconf.autocomp);
		sqinfo.setSubmitnpc((ctconf.autocomp == 0 ) ? submitnpc : 0);
		java.util.ArrayList<String> args = new java.util.ArrayList<String>();
		args.add(NpcManager.getInstance().getNpcNameByID(dst));

		if(ctconf.gototype == CircTaskGotoType.CircTask_GotoInit){
			CircleTaskManager.getInstance().giveRoleCTMailGoto(roleid, sqinfo, 0,0, isnpcgive);
		}else if(ctconf.gototype == CircTaskGotoType.CircTask_GotoRand){
			SNpcShare npcShare = NpcManager.getInstance().getNpcShareByID(dst);
			GCreatePosForCTGoto gcp = new GCreatePosForCTGoto();
			gcp.mapid = npcShare.getMapid();
			gcp.roleid = roleid;
			gcp.npckey = npckey;
			gcp.ctconfid = ctconf.id;
			gcp.gototype = ctconf.gototype;
			gcp.cttype = circletype;
			gcp.isnpc = isnpcgive == true ? 1 : 0;
			GsClient.pSendWhileCommit(gcp);
		}
		CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务:送信SendMail配置完毕.");
		return true;
	}

	public boolean giveRoleItemUse(long roleid, int schoolid, CircleTaskInfo sqinfo, CircTaskConf ctconf, int submitnpc) {
		if (sqinfo == null)
			return false;
		CircTaskItemUse ctiu = CircleTaskManager.getInstance().getCTItemUseData(roleid, ctconf.ctgroup);
		if (ctiu == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]获取使用道具数据失败！");
			return false;
		}
		sqinfo.setQuestid(circletype);
		sqinfo.setQuesttype(ctconf.id);	
		sqinfo.setSmallaward(sqinfo.getSmallaward() + 1);
		sqinfo.setBigaward(sqinfo.getBigaward() + 1);
		sqinfo.setDstitemid(ctiu.getItemid());
		sqinfo.setDstmapid(ctiu.getMapid());
		sqinfo.setSubitemtableid(ctiu.getId());
		if(1 == ctiu.israndompos) {
			GridPos gpos = MapUtil.getRandomArrivablePos(ctiu.getMapid());
			//ctiu.xpos = gpos.toPosition().getX();
			//ctiu.ypos = gpos.toPosition().getY();
			ctiu.xpos = gpos.getX();
			ctiu.ypos = gpos.getY();
		} else {
			//sqinfo.setDstx(ctiu.getXpos());
			//sqinfo.setDsty(ctiu.getYpos());
		}
		sqinfo.setLastgiveuptime(0);
		sqinfo.setQueststate(SpecialQuestState.UNDONE);
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleid, circletype);
		if (ct == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]循环类型:" + ctconf.id + "配置文件错误！");
			return false;
		}
		sqinfo.setRound(sqinfo.getRound() % ct.ring + 1);
		if (ct.getTaskcnt() == 1) {
			sqinfo.setSumnum(sqinfo.getSumnum() + 1);
		}
		sqinfo.setTakequesttime(System.currentTimeMillis());
		sqinfo.setAcceptquesttime(Calendar.getInstance().getTimeInMillis());
		sqinfo.setQuestclass(ctconf.cttype);
		sqinfo.setQuestgroup(ctconf.ctgroup);
		sqinfo.setAutocomp(ctconf.autocomp);
		sqinfo.setSubmitnpc((ctconf.autocomp == 0 ) ? submitnpc : 0);
		//给予道具
		BagUtil.addItem(roleid, ctiu.getItemid(), 1, "circtask school quest itemuse", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_Task, circletype);
		
		if(ctconf.gototype == CircTaskGotoType.CircTask_GotoInit){
			CircleTaskManager.getInstance().giveRoleCTItemUseGoto(roleid, sqinfo, ctiu.getXpos(),ctiu.getYpos(), isnpcgive);
		}else if(ctconf.gototype == CircTaskGotoType.CircTask_GotoRand){
			GCreatePosForCTGoto gcp = new GCreatePosForCTGoto();
			gcp.mapid = ctiu.getMapid();
			gcp.roleid = roleid;
			gcp.npckey = npckey;
			gcp.ctconfid = ctconf.id;
			gcp.gototype = ctconf.gototype;
			gcp.cttype = circletype;
			gcp.isnpc = isnpcgive == true ? 1 : 0;
			GsClient.pSendWhileCommit(gcp);
		}
		CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务:使用道具ItemUse配置完毕.");
		CircleTask.loggerCTSchoolQuestOnTake(roleid, sqinfo);
		return true;
	}
	
	public boolean giveRoleItemCollect(long roleid, int schoolid, CircleTaskInfo sqinfo, CircTaskConf ctconf, int submitnpc) {
		if (sqinfo == null)
			return false;
		sqinfo.setQuestid(circletype);
		sqinfo.setQuesttype(ctconf.id);
		sqinfo.setSmallaward(sqinfo.getSmallaward() + 1);
		sqinfo.setBigaward(sqinfo.getBigaward() + 1);
		if (false == CircleTaskManager.getInstance().genCTSchoolItemCollectParam(roleid, sqinfo, ctconf.ctgroup))
		{
			CircleTaskManager.logger.error("角色[" + roleid + "]生成道具收集数据失败！");
			return false;
		}
		sqinfo.setDstnpckey(0);
		sqinfo.setLastgiveuptime(0);
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleid, circletype);
		if (ct == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]循环类型:" + ctconf.id + "配置文件错误！");
			return false;
		}
		sqinfo.setRound(sqinfo.getRound() % ct.ring + 1);
		if (ct.getTaskcnt() == 1) {
			sqinfo.setSumnum(sqinfo.getSumnum() + 1);
		}
		sqinfo.setQueststate(SpecialQuestState.UNDONE);
		sqinfo.setTakequesttime(Calendar.getInstance().getTimeInMillis());
		sqinfo.setAcceptquesttime(Calendar.getInstance().getTimeInMillis());
		sqinfo.setQuestclass(ctconf.cttype);
		sqinfo.setQuestgroup(ctconf.ctgroup);
		sqinfo.setAutocomp(ctconf.autocomp);
		sqinfo.setSubmitnpc((ctconf.autocomp == 0 ) ? submitnpc : 0);
		if(ctconf.gototype == CircTaskGotoType.CircTask_GotoInit){
			CircleTaskManager.getInstance().giveRoleCTItemCollectGoto(roleid, sqinfo, 0,0, isnpcgive);
		}else if(ctconf.gototype == CircTaskGotoType.CircTask_GotoRand){
			GCreatePosForCTGoto gcp = new GCreatePosForCTGoto();
			gcp.mapid = sqinfo.getDstmapid();
			gcp.roleid = roleid;
			gcp.npckey = npckey;
			gcp.ctconfid = ctconf.id;
			gcp.gototype = ctconf.gototype;
			gcp.cttype = circletype;
			gcp.isnpc = isnpcgive == true ? 1 : 0;
			GsClient.pSendWhileCommit(gcp);
		}
		CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务:收集道具ItemCollect配置完毕.");
		CircleTask.loggerCTSchoolQuestOnTake(roleid, sqinfo);
		return true;
	}

	public boolean giveRolePetCatch(long roleid, int schoolid, CircleTaskInfo sqinfo, CircTaskConf ctconf, int submitnpc) {
		if (sqinfo == null)
			return false;
		//这里偷懒用的之前用的itemfind结构，因为数据结构一样！
		ItemFindInfo ifi = CircleTaskManager.getInstance().getCTPetFindInfo(roleid, ctconf.ctgroup);
		if (ifi == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]抓宠物配置文件数据错误！");
			return false;
		}
		//获取配置的目标物品id
		int dstpetid = ifi.getId();
		if (dstpetid == 0) {
			CircleTaskManager.logger.error("角色[" + roleid + "]抓宠物配置文件数据错误！");
			return false;
		}
		CircleTaskManager.logger.debug("循环任务抓宠物,id:" + dstpetid + " num:" + ifi.getNum() + " shopnpc:" + ifi.getShopnpc());
		//组建sqinfo
		sqinfo.setQuestid(circletype);
		sqinfo.setQuesttype(ctconf.id);
		sqinfo.setSmallaward(sqinfo.getSmallaward() + 1);
		sqinfo.setBigaward(sqinfo.getBigaward() + 1);
		//设置寻找的物品信息
		sqinfo.setDstitemid(dstpetid);
		sqinfo.setDstitemnum(ifi.getNum());	
		sqinfo.setLastgiveuptime(0);
		sqinfo.setTakequesttime(Calendar.getInstance().getTimeInMillis());
		sqinfo.setAcceptquesttime(Calendar.getInstance().getTimeInMillis());
		//查找有无宠物
		final PetColumn pc = new PetColumn(roleid, PetColumnTypes.PET, true);	
		int targetnum = pc.getPetNumByMapping(sqinfo.getDstitemid()); //获取宠物背包里非战斗的目标宠物数量，包含映射表
		if(targetnum >= sqinfo.getDstitemnum()) {
			sqinfo.setDstnpcid(submitnpc);
			sqinfo.setDstnpckey(0);
			sqinfo.setDstmapid(0);
			sqinfo.setDstx(0);
			sqinfo.setDsty(0);	
			sqinfo.setQueststate(SpecialQuestState.DONE);
		} else {
			sqinfo.setDstnpcid(ifi.getShopnpc());
			sqinfo.setDstnpckey(0);
			sqinfo.setDstmapid(0);
			sqinfo.setDstx(0);
			sqinfo.setDsty(0);	
			sqinfo.setQueststate(SpecialQuestState.UNDONE);
		}
		
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleid, circletype);
		if (ct == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]循环类型:" + ctconf.id + "配置文件错误！");
			return false;
		}
		sqinfo.setRound(sqinfo.getRound() % ct.ring + 1);
		if (ct.getTaskcnt() == 1) {
			sqinfo.setSumnum(sqinfo.getSumnum() + 1);
		}
		sqinfo.setQuestclass(ctconf.cttype);
		sqinfo.setQuestgroup(ctconf.ctgroup);
		sqinfo.setAutocomp(ctconf.autocomp);
		sqinfo.setSubmitnpc((ctconf.autocomp == 0 ) ? submitnpc : 0);
		if(ctconf.gototype == CircTaskGotoType.CircTask_GotoInit){
			CircleTaskManager.getInstance().giveRoleCTPetCatchGoto(roleid, sqinfo, 0,0, isnpcgive);
		}else if(ctconf.gototype == CircTaskGotoType.CircTask_GotoRand){
			SNpcShare npcShare = NpcManager.getInstance().getNpcShareByID(sqinfo.getDstnpcid());
			GCreatePosForCTGoto gcp = new GCreatePosForCTGoto();
			gcp.mapid = npcShare.getMapid();
			gcp.roleid = roleid;
			gcp.npckey = npckey;
			gcp.ctconfid = ctconf.id;
			gcp.gototype = ctconf.gototype;
			gcp.cttype = circletype;
			gcp.isnpc = isnpcgive == true ? 1 : 0;
			GsClient.pSendWhileCommit(gcp);
		}
		CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务:抓宠物PetCatch配置完毕.");
		CircleTask.loggerCTSchoolQuestOnTake(roleid, sqinfo);
		return true;
	}

	public boolean giveRoleItemFind(long roleid, int schoolid, CircleTaskInfo sqinfo, CircTaskConf ctconf, int submitnpc) {
		if (sqinfo == null)
			return false;
		
		if (false == CircleTaskManager.getInstance().genCTItemFindParam(roleid, sqinfo, ctconf.ctgroup, submitnpc))
		{
			CircleTaskManager.logger.error("角色[" + roleid + "]生成道具寻找数据失败！");
			return false;
		}
		
		CircleTaskManager.logger.debug("循环任务寻物,id:" + sqinfo.getDstitemid() + " num:" + sqinfo.getDstitemnum() + " npc:" + sqinfo.getDstnpcid());
		//组建sqinfo
		sqinfo.setQuestid(circletype);
		sqinfo.setQuesttype(ctconf.id);
		sqinfo.setSmallaward(sqinfo.getSmallaward() + 1);
		sqinfo.setBigaward(sqinfo.getBigaward() + 1);
		//设置寻找的物品信息
			
		sqinfo.setLastgiveuptime(0);
		sqinfo.setTakequesttime(Calendar.getInstance().getTimeInMillis());
		sqinfo.setAcceptquesttime(Calendar.getInstance().getTimeInMillis());
		//查找背包物品
		
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleid, circletype);
		if (ct == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]循环类型:" + ctconf.id + "配置文件错误！");
			return false;
		}
		sqinfo.setRound(sqinfo.getRound() % ct.ring + 1);
		if (ct.getTaskcnt() == 1) {
			sqinfo.setSumnum(sqinfo.getSumnum() + 1);
		}
		sqinfo.setQuestclass(ctconf.cttype);
		sqinfo.setQuestgroup(ctconf.ctgroup);
		sqinfo.setAutocomp(ctconf.autocomp);
		sqinfo.setSubmitnpc((ctconf.autocomp == 0 ) ? submitnpc : 0);
		if(ctconf.gototype == CircTaskGotoType.CircTask_GotoInit){
			CircleTaskManager.getInstance().giveRoleCTItemFindGoto(roleid, sqinfo, 0,0, isnpcgive);
		}else if(ctconf.gototype == CircTaskGotoType.CircTask_GotoRand){
			SNpcShare npcShare = NpcManager.getInstance().getNpcShareByID(sqinfo.getDstnpcid());
			GCreatePosForCTGoto gcp = new GCreatePosForCTGoto();
			gcp.mapid = npcShare.getMapid();
			gcp.roleid = roleid;
			gcp.npckey = npckey;
			gcp.ctconfid = ctconf.id;
			gcp.gototype = ctconf.gototype;
			gcp.cttype = circletype;
			gcp.isnpc = isnpcgive == true ? 1 : 0;
			GsClient.pSendWhileCommit(gcp);
		}
		CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务:找物品ItemFind配置完毕.");
		CircleTask.loggerCTSchoolQuestOnTake(roleid, sqinfo);
		return true;
	}
	
	public boolean giveRolePatrol(long roleid, int schoolid, CircleTaskInfo sqinfo, CircTaskConf ctconf, int submitnpc) {
		if (null == sqinfo)
			return false;
		sqinfo.setQuestid(circletype);
		sqinfo.setQuesttype(ctconf.id);
		sqinfo.setSmallaward(sqinfo.getSmallaward() + 1);
		sqinfo.setBigaward(sqinfo.getBigaward() + 1);	
		sqinfo.setDstnpckey(0);
		if (false == CircleTaskManager.getInstance().genCTSchoolPatrolParam(roleid, sqinfo, ctconf.ctgroup)) {
			CircleTaskManager.logger.error("角色[" + roleid + "]生成巡逻数据错误！");
			return false;
		}
		if(sqinfo.getDstitemid() <= 0) {
			CircleTaskManager.logger.error("角色[" + roleid + "]生成巡逻数据错误！dstitemid:" + sqinfo.getDstitemid() + ",战斗配置错误！");
			return false;
		}
		sqinfo.setDstnpcid(0);
		//sqinfo.setDstx(0);
		//sqinfo.setDsty(0);
		sqinfo.setLastgiveuptime(0);
		
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleid, circletype);
		if (ct == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]循环类型:" + ctconf.id + "配置文件错误！");
			return false;
		}
		sqinfo.setRound(sqinfo.getRound() % ct.ring + 1);
		if (ct.getTaskcnt() == 1) {
			sqinfo.setSumnum(sqinfo.getSumnum() + 1);
		}
		sqinfo.setQuestclass(ctconf.cttype);
		sqinfo.setQuestgroup(ctconf.ctgroup);
		sqinfo.setQueststate(SpecialQuestState.UNDONE);
		sqinfo.setTakequesttime(Calendar.getInstance().getTimeInMillis());
		sqinfo.setAcceptquesttime(Calendar.getInstance().getTimeInMillis());
		sqinfo.setAutocomp(ctconf.autocomp);
		sqinfo.setSubmitnpc((ctconf.autocomp == 0 ) ? submitnpc : 0);
		if (ctconf.gototype == CircTaskGotoType.CircTask_GotoInit) {
			CircleTaskManager.getInstance().giveRoleCTPatrolGoto(roleid,
					sqinfo, 0, 0, isnpcgive);
		} 
		else if (ctconf.gototype == CircTaskGotoType.CircTask_GotoRand || ctconf.gototype == CircTaskGotoType.CircTask_GotoGuild) {
			GCreatePosForCTGoto gcp = new GCreatePosForCTGoto();
			gcp.mapid = sqinfo.getDstmapid();
			gcp.roleid = roleid;
			gcp.npckey = npckey;
			gcp.ctconfid = ctconf.id;
			gcp.gototype = ctconf.gototype;
			gcp.cttype = circletype;
			gcp.isnpc = isnpcgive == true ? 1 : 0;
			GsClient.pSendWhileCommit(gcp);
		}
		CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务:巡逻Patrol配置完毕.");
		return true;
	}
	
	public boolean giveRoleKillMonster(long roleid, int schoolid, CircleTaskInfo sqinfo, CircTaskConf ctconf, int submitnpc) {
		if (null == sqinfo)
			return false;
		//生成设置要求的杀怪信息
		if (false == CircleTaskManager.getInstance().genCTKillMonsterParam(roleid, sqinfo, ctconf.ctgroup)) {
			CircleTaskManager.logger.error("角色[" + roleid + "]生成杀怪数据错误！");
			return false;
		}
		CircleTaskManager.logger.debug("循环任务杀怪,杀怪目标怪物id:" + sqinfo.getDstitemid() + " 要求杀怪数量num:" + sqinfo.getDstitemnum());
		//组建sqinfo
		sqinfo.setQuestid(circletype);
		sqinfo.setQuesttype(ctconf.id);
		sqinfo.setSmallaward(sqinfo.getSmallaward() + 1);
		sqinfo.setBigaward(sqinfo.getBigaward() + 1);
		sqinfo.setLastgiveuptime(0);
		sqinfo.setTakequesttime(Calendar.getInstance().getTimeInMillis());
		sqinfo.setAcceptquesttime(Calendar.getInstance().getTimeInMillis());
		//设置npcid(本类型无用,destnpcid存杀怪计数)
		sqinfo.setDstnpcid(0); 
		sqinfo.setDstnpckey(0);
		sqinfo.setDstx(0);//杀怪计数!!!
		sqinfo.setDsty(0);
		sqinfo.setQueststate(SpecialQuestState.UNDONE);

		final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleid, circletype);
		if (ct == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]循环类型:" + ctconf.id + "配置文件错误！");
			return false;
		}
		sqinfo.setRound(sqinfo.getRound() % ct.ring + 1);
		if (ct.getTaskcnt() == 1) {
			sqinfo.setSumnum(sqinfo.getSumnum() + 1);
		}
		sqinfo.setQuestclass(ctconf.cttype);
		sqinfo.setQuestgroup(ctconf.ctgroup);
		sqinfo.setAutocomp(ctconf.autocomp);
		sqinfo.setSubmitnpc((ctconf.autocomp == 0 ) ? submitnpc : 0);
		if(ctconf.gototype == CircTaskGotoType.CircTask_GotoInit){
			CircleTaskManager.getInstance().giveRoleCTKillMonsterGoto(roleid, sqinfo, 0,0, isnpcgive);
		}else if(ctconf.gototype == CircTaskGotoType.CircTask_GotoRand){
			SNpcShare npcShare = NpcManager.getInstance().getNpcShareByID(sqinfo.getDstnpcid());
			GCreatePosForCTGoto gcp = new GCreatePosForCTGoto();
			gcp.mapid = npcShare.getMapid();
			gcp.roleid = roleid;
			gcp.npckey = npckey;
			gcp.ctconfid = ctconf.id;
			gcp.gototype = ctconf.gototype;
			gcp.cttype = circletype;
			gcp.isnpc = isnpcgive == true ? 1 : 0;
			GsClient.pSendWhileCommit(gcp);
		}
		CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务:杀怪计数KillMonster配置完毕.");
		CircleTask.loggerCTSchoolQuestOnTake(roleid, sqinfo);
		return true;
	}
	
	public boolean giveRoleChallengeNpc(long roleid, int schoolid, CircleTaskInfo sqinfo, final int circTaskId, CircTaskConf ctconf, int submitnpc,  final boolean firstTime) {
		if (null == sqinfo)
			return false;
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(sqinfo.getId());
		if (ct == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]循环类型:" + ctconf.id + "配置文件错误！");
			return false;
		}
		int battleAI = CircleTaskManager.getInstance().getCTChallengeNpcBattleAI(roleid, ctconf.ctgroup, sqinfo);
		if (0 == battleAI){
			CircleTaskManager.logger.error("角色[" + roleid + "]挑战NPC配置文件数据错误！");
			return false;
		}
		CircleTaskManager.logger.debug("循环任务挑战NPC,npcid:" + sqinfo.getDstnpcid() + " 战斗AI:" + sqinfo.getDoneparam());
		
		final long now = Calendar.getInstance().getTimeInMillis();
		
		
		int circle = ct.getCycle();
		if (ct.teamnum > 0) {
			CircleTaskManager.logger.debug("循环任务挑战NPC,配置组队:" + ct.teamnum);
			fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
			if (null == team || !team.isTeamLeader(roleid)){
				List<String> paras = new ArrayList<String>();
				paras.add(String.valueOf(ct.teamnum));
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150522, npcid, paras);
				return false;
			}
			
			// 全队正常状态成员 加锁
			List<Long> members = new ArrayList<Long>();
			members.addAll(team.getNormalMemberIds());
			
			//判断有无等级不足的队员
			List<String> levelApplyNames = new ArrayList<String>();
			for (Long mem : team.getNormalMemberIds()) {
				PropRole memberRole = new PropRole(mem, true);
				if(memberRole.getLevel() < ct.levelmin) {
					levelApplyNames.add(memberRole.getName());
				}
			}
			
			//有人等级不足
			if (levelApplyNames.size() > 0) {
				StringBuffer strParam = new StringBuffer();
				int nIndex = 0;
				for (String name : levelApplyNames) {
					if (nIndex != (levelApplyNames.size() - 1)) {
						strParam.append(name).append(",");
					}
					else {
						strParam.append(name);
					}
				}
				
				List<String> paras = new ArrayList<String>();
				paras.add(strParam.toString());
				paras.add(String.valueOf(ct.levelmin));
				for (Long mem : team.getNormalMemberIds()) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(mem, 150523, npcid, paras);
				}
				
				return false;
			}
			
			if (firstTime) {
				if (members.size() < ct.teamnum) {
					List<String> paras = new ArrayList<String>();
					paras.add(String.valueOf(ct.teamnum));
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150522, npcid, paras);	
					return false;
				}
			}
			
			int teamLeaderRound = sqinfo.getRound();
			
			for (Long rid: members) {
				xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(rid);
				if (null == quest_map) {
					quest_map = xbean.Pod.newCircleTaskMap();
					xtable.Rolecircletask.insert(rid, quest_map);
				}
				xbean.CircleTaskInfo questinfo = quest_map.getTaskmap().get(circletype);
				if (null == questinfo) {
					questinfo = xbean.Pod.newCircleTaskInfo();
					quest_map.getTaskmap().put(circletype, questinfo);
				} 
				questinfo.setId(circTaskId);
				//questinfo.setGivetasknpcid(npcid);
				
				//组建sqinfo
				questinfo.setQuestid(circletype);
				questinfo.setQuesttype(ctconf.id);
				questinfo.setSmallaward(questinfo.getSmallaward() + 1);
				questinfo.setBigaward(questinfo.getBigaward() + 1);
				questinfo.setDstitemid(0);
				questinfo.setDstitemnum(0);	
				questinfo.setLastgiveuptime(0);
				
				//判断是否清0的代码放到这里
				switch(circle) {
				case 1: {
					if (!fire.pb.util.DateValidate.inTheSameDay(questinfo.getTakequesttime(), now)) {
						questinfo.setSumnum(0);
						questinfo.setCatchitsingletime(0);
						questinfo.setCatchitdoubletime(0);
					}
					break;
				}
				case 2: {
					if (!fire.pb.util.DateValidate.inTheSameWeek(questinfo.getTakequesttime(), now)) {
						questinfo.setSumnum(0);
						questinfo.setCatchitsingletime(0);
						questinfo.setCatchitdoubletime(0);
					}
					break;
				}
				case 3: {
					if (!fire.pb.util.DateValidate.inTheSameMonth(questinfo.getTakequesttime(), now)) {
						questinfo.setSumnum(0);
						questinfo.setCatchitsingletime(0);
						questinfo.setCatchitdoubletime(0);
					}
					break;
				}
				default:
					break;
				}
				questinfo.setTakequesttime(now);
				questinfo.setAcceptquesttime(now);
				
				questinfo.setDstnpcid(sqinfo.getDstnpcid());
				questinfo.setDstnpckey(sqinfo.getDstnpckey());
				questinfo.setDstmapid(sqinfo.getDstmapid());
				questinfo.setDoneparam(sqinfo.getDoneparam());
				questinfo.setDstx(0);
				questinfo.setDsty(0);
				questinfo.setQueststate(SpecialQuestState.UNDONE);
				if (ctconf.id == ct.specialtask) {
					questinfo.setRound(teamLeaderRound + 1);
				}
				else {
					questinfo.setRound(teamLeaderRound % ct.ring + 1);
				}
				
				if (ct.getTaskcnt() == 1) {
					questinfo.setSumnum(questinfo.getSumnum() + 1);
				}
				questinfo.setQuestclass(ctconf.cttype);
				questinfo.setQuestgroup(ctconf.ctgroup);
				questinfo.setAutocomp(ctconf.autocomp);
				questinfo.setSubmitnpc((ctconf.autocomp == 0 ) ? submitnpc : 0);
				if(ctconf.gototype == CircTaskGotoType.CircTask_GotoInit){
					CircleTaskManager.getInstance().giveRoleCTKillMonsterGoto(rid, questinfo, 0,0, isnpcgive);
				}else if(ctconf.gototype == CircTaskGotoType.CircTask_GotoRand){
					SNpcShare npcShare = NpcManager.getInstance().getNpcShareByID(questinfo.getDstnpcid());
					GCreatePosForCTGoto gcp = new GCreatePosForCTGoto();
					gcp.mapid = npcShare.getMapid();
					gcp.roleid = rid;
					gcp.npckey = npckey;
					gcp.ctconfid = ctconf.id;
					gcp.gototype = ctconf.gototype;
					gcp.cttype = circletype;
					gcp.isnpc = isnpcgive == true ? 1 : 0;
					GsClient.pSendWhileCommit(gcp);
				}
				CircleTaskManager.logger.debug("角色[" + rid + "]循环任务:NPC挑战ChallengeNpc配置完毕.");
				CircleTask.loggerCTSchoolQuestOnTake(rid, sqinfo);
			}
		} else {
			CircleTaskManager.logger.debug("循环任务挑战NPC,配置单人:" + ct.teamnum);
			//组建sqinfo
			sqinfo.setQuestid(circletype);
			sqinfo.setQuesttype(ctconf.id);
			sqinfo.setSmallaward(sqinfo.getSmallaward() + 1);
			sqinfo.setBigaward(sqinfo.getBigaward() + 1);
			sqinfo.setDstitemid(0);
			sqinfo.setDstitemnum(0);	
			sqinfo.setLastgiveuptime(0);
			sqinfo.setTakequesttime(Calendar.getInstance().getTimeInMillis());
			sqinfo.setAcceptquesttime(Calendar.getInstance().getTimeInMillis());
			sqinfo.setDstnpckey(0);
			sqinfo.setDstmapid(0);
			sqinfo.setDstx(0);
			sqinfo.setDsty(0);
			sqinfo.setQueststate(SpecialQuestState.UNDONE);
			
			sqinfo.setRound(sqinfo.getRound() % ct.ring + 1);
			if (ct.getTaskcnt() == 1) {
				sqinfo.setSumnum(sqinfo.getSumnum() + 1);
			}
			sqinfo.setQuestclass(ctconf.cttype);
			sqinfo.setQuestgroup(ctconf.ctgroup);
			sqinfo.setAutocomp(ctconf.autocomp);
			sqinfo.setSubmitnpc((ctconf.autocomp == 0 ) ? submitnpc : 0);
			if(ctconf.gototype == CircTaskGotoType.CircTask_GotoInit){
				CircleTaskManager.getInstance().giveRoleCTKillMonsterGoto(roleid, sqinfo, 0,0, isnpcgive);
			}else if(ctconf.gototype == CircTaskGotoType.CircTask_GotoRand){
				SNpcShare npcShare = NpcManager.getInstance().getNpcShareByID(sqinfo.getDstnpcid());
				GCreatePosForCTGoto gcp = new GCreatePosForCTGoto();
				gcp.mapid = npcShare.getMapid();
				gcp.roleid = roleid;
				gcp.npckey = npckey;
				gcp.ctconfid = ctconf.id;
				gcp.gototype = ctconf.gototype;
				gcp.cttype = circletype;
				gcp.isnpc = isnpcgive == true ? 1 : 0;
				GsClient.pSendWhileCommit(gcp);
			}
			CircleTaskManager.logger.debug("角色[" + roleid + "]循环任务:NPC挑战ChallengeNpc配置完毕.");
			CircleTask.loggerCTSchoolQuestOnTake(roleid, sqinfo);
		}
		return true;
	}
	
	private int parseNpcIdFromConfString(PropRole prole, String npcstr) {
		if (prole == null || npcstr == null)
			return 0;
		int schoolid = prole.getSchool();
		String[] schoolnpcs = npcstr.split(",");
		for (String schoolnpc : schoolnpcs) {
			String[] tempstr = schoolnpc.split("@");
			if (tempstr.length != 2) {
				continue;
			}
			if (Integer.parseInt(tempstr[0]) == schoolid)
				return Integer.parseInt(tempstr[1]);
		}
		return 0;
	}

	public boolean process() {
		
		/*
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
		{
			boolean ok = fire.pb.fushi.DayPayManager.getInstance().CheckDayPayWithLevel(roleid);
			if (ok == false)
			{
				Module.logger.info("PAcceptCircTask:角色[" + roleid + "]任务接取失败-nodaypay:" + circletype);
				return false;
			}
		}
		*/
		//先锁一下队伍
		Team team = TeamManager.getTeamByRoleId(roleid);
		if (team != null) {
			List<Long> members = new ArrayList<Long>();
			members.addAll(team.getNormalMemberIds());
			
			this.lock(xtable.Locks.ROLELOCK, members);
		}
		
		//公会任务
		if (circletype == 1060000) {
			//判断玩家是否在公会里
			Long clankey=xtable.Properties.selectClankey(roleid);
			if (clankey == null||clankey<=0) {
				CircleTaskManager.logger.error("角色[" + roleid + "]公会:" + circletype);
				return false;
			}
		}
				
		final fire.pb.PropRole prole = new fire.pb.PropRole(roleid, false);
		circleconf = CircleTaskManager.getInstance().getCircTask(prole.getLevel(), circletype);
		if (circleconf == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]没有找到配置循环类型:" + circletype);
			return false;
		}
		else if (prole.getLevel() < circleconf.levelmin) {
			if (circletype == SpecialQuestID.catchIt) {
				List<String> paras = new ArrayList<String>();
				paras.add(String.valueOf(circleconf.levelmin));
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150542, npcid, paras);
			}
			return false;
		}
		
		int tctid = circleconf.getId();
		
		CircleTaskManager.logger.info("角色[" + roleid + "]开始接受循环任务:" + circletype + ",预定任务id:" + ctquestid + ".强制:" + isforce);
		if (prole.getCurExp() >= CalcRoleAttr.ROLE_MAX_CUREXP && !isforce) {
			CircleTaskManager.logger.error("角色[" + roleid + "]经验:" + prole.getCurExp() + ",大于当前上限值!" + circletype + "失败!");
			if(npcid > 0)
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 100091, npcid, null);
			return true;
		}
			
		xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(roleid);
		if (null == quest_map) {
			quest_map = xbean.Pod.newCircleTaskMap();
			xtable.Rolecircletask.insert(roleid, quest_map);
		}
		xbean.CircleTaskInfo questinfo = quest_map.getTaskmap().get(circletype);
		if (null == questinfo) {
			questinfo = xbean.Pod.newCircleTaskInfo();
			quest_map.getTaskmap().put(circletype, questinfo);
		} else {
			if (!isforce && circletype != 1040000) {
				if (questinfo.getQueststate() == SpecialQuestState.DONE || questinfo.getQueststate() == SpecialQuestState.UNDONE) {
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, this.circleconf.getAlreadytips(), null);
					CircleTaskManager.logger.error("角色[" + roleid + "]当前循环任务" + circletype + "状态:" + questinfo.getQueststate() + ",尚未成功提交,无法接受新任务!");
					return false;
				}
			}else{
				questinfo.setQueststate(0);
			}
		}
		CircleTaskManager.logger.info(
				"角色[" + roleid + "]循环任务[" + questinfo.getQuestid() + "]初始信息=" +
				"Id:" + questinfo.getId() + "," +
				"Questid:" + questinfo.getQuestid() + "," +
				"Queststate:" + questinfo.getQueststate() + "," +
				"Round:" + questinfo.getRound() + "," +
				"Sumnum:" + questinfo.getSumnum() + "," +
				"Questtype:" + questinfo.getQuesttype() + "," +
				"Lastgiveuptime:" + questinfo.getLastgiveuptime() + "," +
				"Takequesttime:" + questinfo.getTakequesttime() + "," +
				"Sametimes:" + questinfo.getSametimes() + "," +
				"Lasttype:" + questinfo.getLasttype() + "," +
				"Questclass:" + questinfo.getQuestclass() + "," +
				"Questgroup:" + questinfo.getQuestgroup() + "," +
				"Autocomp:" + questinfo.getAutocomp() + "," +
				"Submitnpc:" + questinfo.getSubmitnpc() + "," +
				"danbei:" + questinfo.getCatchitsingletime() + "," +
				"suangbei:" + questinfo.getCatchitdoubletime() + "," +
				"Openawardnum:" + questinfo.getOpenawardnum() + "."
				);
		
		int circleMax = CircleTaskManager.getInstance().getCTCircleMax(circleconf);
		final long now = Calendar.getInstance().getTimeInMillis();
		
		//英雄试练特殊处理
		if (circletype == 1050000) {
			if (!fire.pb.util.DateValidate.inTheSameDay(questinfo.getAcceptquesttime(), now))
				questinfo.setSumnum(0);
		}
		else if (circletype == 1120000)
		{
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
			long ret = bag.subCurrency(-60, MoneyType.MoneyType_ProfContribute, "titletask", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_professionleader_titletask, 0);
			if (ret != -60)
			{
				return false;
			}
		}
		else if (circletype == 1130000)
		{
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
			long ret = bag.subCurrency(-120, MoneyType.MoneyType_ProfContribute, "titletask", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_professionleader_titletask, 0);
			if (ret != -120)
			{
				return false;
			}
		}
		else if (circletype == 1140000)
		{
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
			long ret = bag.subCurrency(-180, MoneyType.MoneyType_ProfContribute, "titletask", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_professionleader_titletask, 0);
			if (ret != -180)
			{
				return false;
			}
		}
		
		int circle = circleconf.getCycle();
		//判断是否清0的代码放到这里
		switch(circle) {
		case 1: {
			if (!fire.pb.util.DateValidate.inTheSameDay(questinfo.getTakequesttime(), now)) {
				questinfo.setSumnum(0);
				questinfo.setCatchitsingletime(0);
				questinfo.setCatchitdoubletime(0);
				if (circleconf.getClearround() == 1) {
					questinfo.setRound(0);
				}
			}
			break;
		}
		case 2: {
			if (!fire.pb.util.DateValidate.inTheSameWeek(questinfo.getTakequesttime(), now)) {
				questinfo.setSumnum(0);
				questinfo.setCatchitsingletime(0);
				questinfo.setCatchitdoubletime(0);
				if (circleconf.getClearround() == 1) {
					questinfo.setRound(0);
				}
			}
			break;
		}
		case 3: {
			if (!fire.pb.util.DateValidate.inTheSameMonth(questinfo.getTakequesttime(), now)) {
				questinfo.setSumnum(0);
				questinfo.setCatchitsingletime(0);
				questinfo.setCatchitdoubletime(0);
				if (circleconf.getClearround() == 1) {
					questinfo.setRound(0);
				}
			}
			break;
		}
		default:
			break;
		}
		
		if (circleMax > 0) {
			if(!isforce){
				if (questinfo.getSumnum() >= circleMax) {
					if (circletype == 1060000) {
						fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150551, null);
					}
					else {
						fire.pb.talk.MessageMgr.sendMsgNotify(roleid, this.circleconf.getMaxnumtips(), null);
					}
					
					CircleTaskManager.logger.error("角色[" + roleid + "]当前任务" + circletype + "次数:" + questinfo.getSumnum() + ",超过最大次数!");
					return false;
				}
			} else {
				questinfo.setSumnum(0);
				questinfo.setRound(0);
				CircleTaskManager.logger.info("角色[" + roleid + "]当前任务" + circletype + ",次数:" + questinfo.getSumnum() + "已经置〇!");
			}
		}
		
		
		
		GeneralFormat format = new GeneralFormat();
		int circTaskId = -1;
		boolean firstTime = false;
		if (questinfo.getId() > 0 && questinfo.getRound() != circleconf.ring && questinfo.getRound() != 0) {
			circTaskId = CircleTaskManager.getInstance().getCircTaskTypeInfoById(tctid, prole.getLevel(), questinfo.getRound(), circletype, format);
		}
		else {
			circTaskId = CircleTaskManager.getInstance().getCircTaskTypeInfoById(-1, prole.getLevel(), questinfo.getRound(), circletype, format);
			if (isnpcgive)
				firstTime = true;
		}
		
		//这里拿配置，如果需要，走外部随机规则逻辑随机下一环任务
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(circTaskId);
		if (ct != null) {
			if(ct.isoutrandom == 1){
				CircleTaskManager.getInstance().getNextRandomTask(roleid, questinfo.getRound(), format);
				CircleTaskManager.logger.info("角色[" + roleid + "]任务" + circletype + "调用外部随机逻辑,基于环数:" + questinfo.getRound() + ","
						+ "随机到任务:" + format.getId() + ".");
			}
		}
		
//		format = SpecialQuestManager.getInstance().getCircTaskTypeInfo(prole.getLevel(), questinfo.getRound(), circletype);
		if (circTaskId == -1) {
			CircleTaskManager.logger.error("角色[" + roleid + "]任务" + circletype + ",当前等级:" + prole.getLevel() + ",环数:" + questinfo.getRound() + ",未找到对应随机配置！");
			//throw new RuntimeException("根据玩家当前等级和当前环数，没有找到配置的任务类型，level=" + prole.getLevel() + "  round=" + questinfo.getRound());
			return false;
		}
		
		questinfo.setId(circTaskId);
		
		int questconfid = format.getId();	
		if(this.ctquestid > 0){
			questconfid = this.ctquestid;
		}
		CircleTaskManager.logger.debug("角色[" + roleid + "]任务" + circletype + "随机到下一循环任务id:" + questconfid + ".");
		CircTaskConf ctc = CircleTaskManager.getCircTaskConf(questconfid);
		if(ctc == null){
			CircleTaskManager.logger.error("角色[" + roleid + "]x循环任务配置表:" + questconfid + ",未找到数据!");
			return false;
		}
		
		if (questinfo.getLasttype() == format.getId()) {
			questinfo.setSametimes(questinfo.getSametimes() + 1);
		} else {
			questinfo.setSametimes(1);
			questinfo.setLasttype(format.getId());
		}
		
		int submitnpc = (ctc.getAutocomp() == 0) ? parseNpcIdFromConfString(prole, ctc.getSubmitnpc()) : 0;
		boolean giveSchoolquestResult = false;
		switch (ctc.cttype) {
			case CircTaskClass.CircTask_Mail: {
				giveSchoolquestResult = giveRoleSendMail(roleid, prole.getSchool(), questinfo, ctc, submitnpc);
				break;
			}
			case CircTaskClass.CircTask_ItemUse: {
				giveSchoolquestResult = giveRoleItemUse(roleid, prole.getSchool(), questinfo, ctc, submitnpc);
				break;
			}
			case CircTaskClass.CircTask_ItemCollect: {
				giveSchoolquestResult = giveRoleItemCollect(roleid, prole.getSchool(), questinfo, ctc, submitnpc);
				break;
			}
			case CircTaskClass.CircTask_ItemFind: {
				giveSchoolquestResult = giveRoleItemFind(roleid, prole.getSchool(), questinfo, ctc, submitnpc);
				break;
			}
			case CircTaskClass.CircTask_PetCatch: {
				giveSchoolquestResult = giveRolePetCatch(roleid, prole.getSchool(), questinfo, ctc, submitnpc);
				break;
			}
			case CircTaskClass.CircTask_Patrol: {
				giveSchoolquestResult = giveRolePatrol(roleid, prole.getSchool(), questinfo, ctc, submitnpc);
				break;
			}
			case CircTaskClass.CircTask_CatchIt: {//给捉鬼任务
				giveSchoolquestResult = giveRoleCatchIt(ctc, circTaskId, firstTime);
				break;
			}
			case CircTaskClass.CircTask_KillMonster: {
				giveSchoolquestResult = giveRoleKillMonster(roleid, prole.getSchool(), questinfo, ctc, submitnpc);
				break;
			}
			case CircTaskClass.CircTask_ChallengeNpc: {
				giveSchoolquestResult = giveRoleChallengeNpc(roleid, prole.getSchool(), questinfo, circTaskId, ctc, submitnpc, firstTime);
				break;
			}
		}
		CircleTaskManager.logger.debug("角色[" + roleid + "]接受循环任务" + circletype + "完毕,结果:" + giveSchoolquestResult + ".");
		if(giveSchoolquestResult) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, this.circleconf.getSuccesstips(), null);
		}
		CircleTaskManager.logger.info(
				"角色[" + roleid + "]循环任务[" + questinfo.getQuestid() + "]结果信息=" +
				"Id:" + questinfo.getId() + "," +
				"Questid:" + questinfo.getQuestid() + "," +
				"Queststate:" + questinfo.getQueststate() + "," +
				"Round:" + questinfo.getRound() + "," +
				"Sumnum:" + questinfo.getSumnum() + "," +
				"Questtype:" + questinfo.getQuesttype() + "," +
				"Lastgiveuptime:" + questinfo.getLastgiveuptime() + "," +
				"Takequesttime:" + questinfo.getTakequesttime() + "," +
				"Sametimes:" + questinfo.getSametimes() + "," +
				"Lasttype:" + questinfo.getLasttype() + "," +
				"Questclass:" + questinfo.getQuestclass() + "," +
				"Questgroup:" + questinfo.getQuestgroup() + "," +
				"Autocomp:" + questinfo.getAutocomp() + "," +
				"Submitnpc:" + questinfo.getSubmitnpc() + "," +
				"Openawardnum:" + questinfo.getOpenawardnum() + "."
				);
		//运营日志，接取任务
		YYLogger.acceptTaskLog(roleid, questinfo.getQuestid());
		return giveSchoolquestResult;
	}
	
	public boolean giveRoleCatchIt(final CircTaskConf ctconf, final int circTaskId, final boolean firstTime) {
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(circTaskId);
		if(ct == null) {
			CircleTaskManager.logger.error("角色[" + roleid + "]x循环任务配置表:" + circletype + ",未找到数据!");
			return false;
		}
		
		PropRole pRole = new PropRole(roleid, true);
		if (pRole.getLevel() < ct.levelmin) {
			List<String> paras = new ArrayList<String>();
			paras.add(String.valueOf(ct.levelmin));
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150542, npcid, paras);
			return false;
		}
		
		if (ct.teamnum > 0) {
			fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
			if (null == team || !team.isTeamLeader(roleid)){
				List<String> paras = new ArrayList<String>();
				paras.add(String.valueOf(ct.teamnum));
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150522, npcid, paras);
				return false;
			}

			//判断有无等级不足的队员
			List<String> levelApplyNames = new ArrayList<String>();
			for (Long mem : team.getNormalMemberIds()) {
				PropRole memberRole = new PropRole(mem, true);
				if(memberRole.getLevel() < ct.levelmin) {
					levelApplyNames.add(memberRole.getName());
				}
			}
			
			//有人等级不足
			if (levelApplyNames.size() > 0) {
				StringBuffer strParam = new StringBuffer();
				int nIndex = 0;
				for (String name : levelApplyNames) {
					if (nIndex != (levelApplyNames.size() - 1)) {
						strParam.append(name).append(",");
					}
					else {
						strParam.append(name);
					}
				}
				
				List<String> paras = new ArrayList<String>();
				paras.add(strParam.toString());
				paras.add(String.valueOf(ct.levelmin));
				for (Long mem : team.getNormalMemberIds()) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(mem, 150523, npcid, paras);
				}
				
				return false;
			}
			
			List<Long> members = new ArrayList<Long>();
			members.addAll(team.getNormalMemberIds());
			if (firstTime) { //首次接任务需要判断队伍人数
				if (members.size() < ct.teamnum) {
					List<String> paras = new ArrayList<String>();
					paras.add(String.valueOf(ct.teamnum));
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 150522, npcid, paras);	
					return false;
				}
			}
			
			int circle = ct.getCycle();
			final long now = Calendar.getInstance().getTimeInMillis();
			// 全队正常状态成员 加锁
			this.lock(xtable.Locks.ROLELOCK, members);
			for (Long rid: members) {
				xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(rid);
				if (null == quest_map) {
					quest_map = xbean.Pod.newCircleTaskMap();
					xtable.Rolecircletask.insert(rid, quest_map);
				}
				xbean.CircleTaskInfo questinfo = quest_map.getTaskmap().get(circletype);
				if (null == questinfo) {
					questinfo = xbean.Pod.newCircleTaskInfo();
					quest_map.getTaskmap().put(circletype, questinfo);
				}
				
				//判断是否清0的代码放到这里
				switch(circle) {
				case 1: {
					if (!fire.pb.util.DateValidate.inTheSameDay(questinfo.getTakequesttime(), now)) {
						questinfo.setSumnum(0);
						questinfo.setCatchitsingletime(0);
						questinfo.setCatchitdoubletime(0);
					}
					break;
				}
				case 2: {
					if (!fire.pb.util.DateValidate.inTheSameWeek(questinfo.getTakequesttime(), now)) {
						questinfo.setSumnum(0);
						questinfo.setCatchitsingletime(0);
						questinfo.setCatchitdoubletime(0);
					}
					break;
				}
				case 3: {
					if (!fire.pb.util.DateValidate.inTheSameMonth(questinfo.getTakequesttime(), now)) {
						questinfo.setSumnum(0);
						questinfo.setCatchitsingletime(0);
						questinfo.setCatchitdoubletime(0);
					}
					break;
				}
				default:
					break;
				}
				
				questinfo.setId(circTaskId);
				questinfo.setGivetasknpcid(npcid);
			}
		}
		else { //不需要组队
			xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(roleid);
			if (null == quest_map) {
				quest_map = xbean.Pod.newCircleTaskMap();
				xtable.Rolecircletask.insert(roleid, quest_map);
			}
			xbean.CircleTaskInfo questinfo = quest_map.getTaskmap().get(circletype);
			if (null == questinfo) {
				questinfo = xbean.Pod.newCircleTaskInfo();
				quest_map.getTaskmap().put(circletype, questinfo);
			} 
			else {
				if (questinfo.getQueststate() != SpecialQuestState.SUCCESS && questinfo.getQueststate() != 0) {
					if (questinfo.getQueststate() == SpecialQuestState.FAIL) {
						return false;
					} else if (questinfo.getQueststate() == SpecialQuestState.DONE || questinfo.getQueststate() == SpecialQuestState.UNDONE) {
						return false;
					}
				}
			}
			questinfo.setId(circTaskId);
			questinfo.setGivetasknpcid(npcid);
		}
		
		//判断是否随机到鬼王任务
		int questID = 0;
		if (ctquestid != 0) {
			questID = ctquestid;
		}
		else {
			questID = ctconf.getId();
		}

		//地图id
		int dstmapid = CircleTaskManager.getInstance().getCatchItMap(ctconf.ctgroup);
		//根据战场信息获取主npc id，然后通知客户端显示
		int battleShowNpc = CircleTaskManager.getInstance().getCatchItBattleNpc(ctconf.ctgroup); //捉鬼
		
		SNpcShare npc = ConfigManager.getInstance().getConf(fire.pb.npc.SNpcShare.class).get(battleShowNpc);
		String npcname = NpcManager.getInstance().getNpcName(npc);
		if (npcname.length() == 0) {
			LogManager.logger.error("大萌萌没有名字");
		}
		
		fire.msp.circletask.GCreateNpcForCatchIt req = new fire.msp.circletask.GCreateNpcForCatchIt();
		req.mapid = dstmapid;
		req.name = npcname;
		req.battleid = npc.battleinfo;
		req.npcid = battleShowNpc;
		req.roleid = roleid;
		req.givetasknpcid = npcid;
		req.width = 100;
		req.height = 100;
		req.questtype = questID;
		req.questid = circletype;
		req.teamnum = ct.teamnum;
		if (firstTime) {
			req.firsttime = 1;
		}
		else
			req.firsttime = 0;
		GsClient.pSendWhileCommit(req);
		return true;
	}


}
