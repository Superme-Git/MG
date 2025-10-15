package fire.pb.master.proc;


import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.item.Module;
import fire.pb.map.SceneManager;
import fire.pb.master.MasterManager;
import fire.pb.master.SNotifyMaster;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.DateValidate;

/**
 * 师傅拉徒弟拜师
 * @author nobody
 */
public class PAcceptPrenticeProc extends mkdb.Procedure{
	
	private long prenticeRoleId;
	private int flag;
	public PAcceptPrenticeProc(long masterId, int flag){
		this.prenticeRoleId = masterId;
		this.flag = flag;
	}

	@Override
	protected boolean process() throws Exception {
		Team team = TeamManager.getTeamByRoleId(prenticeRoleId);
		if(team == null){
			fire.pb.talk.MessageMgr.sendMsgNotify(prenticeRoleId, 146389 , null);
			return false;
		}
		if(team.getAllMemberIds().size() >2 || team.getNormalMemberIds().size() !=2){
			fire.pb.talk.MessageMgr.sendMsgNotify(prenticeRoleId, 146389 , null);
			return false;
		}
		
		long masterId = team.getTeamLeaderId();
		if(masterId == prenticeRoleId){
			return false;
		}
		
		PropRole papprent = new PropRole(prenticeRoleId, true);
		if(flag != 1){
			List<String> parms = new ArrayList<String>();
			parms.add(papprent.getName());
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146486, parms);
			return false;
		}
		
		xbean.MenstorInfo menstor = xtable.Apprents.get(masterId);
		if(menstor == null){
			menstor = xbean.Pod.newMenstorInfo();
			xtable.Apprents.insert(masterId, menstor);
		}

		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, team.getNormalMemberIds()));

		//1.先检查师傅是否满足条件
		PropRole prole = new PropRole(masterId, true);
		if(prole.getLevel() <MasterManager.MASTER_MIN_LEVEL){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146388, null);
			return false;
		}
		xbean.MenstorAndApprent master = xtable.Menstor.get(masterId);
		if(master == null){
			master = xbean.Pod.newMenstorAndApprent();
			xtable.Menstor.insert(masterId, master);
		}
		if(master.getFlag() == 1){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146390, null);
			return false;
		}else if(master.getFlag() == 0){
			master.setFlag(2);
			mkdb.Procedure.psendWhileCommit(masterId, new SNotifyMaster(2));
		}
		int number = MasterManager.getMaxApprentice(prole.getLevel());
		if(menstor.getCurrapprentices().size() >= number){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146391, null);
			return false;
		}

		//2. 检查徒弟是否满足条件
		long prenticeRoleId = team.getNormalMemberIds().get(1);
		
		if(!MasterManager.getInstance().isFriendEatch(masterId, prenticeRoleId)){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146419 , null);
			fire.pb.talk.MessageMgr.sendMsgNotify(prenticeRoleId, 146419 , null);
			return false;
		}

		
		if(papprent.getLevel() >= MasterManager.APPRENT_MAX_LEVEL || papprent.getLevel()< MasterManager.APPRENT_MIN_LEVEL){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146402, null);
			return false;
		}

		xbean.MenstorAndApprent apprent = xtable.Menstor.get(prenticeRoleId);
		if(apprent == null){
			apprent = xbean.Pod.newMenstorAndApprent();
			xtable.Menstor.insert(prenticeRoleId, apprent);
		}
		//有师徒关系的人 不能为徒弟
		if(apprent.getFlag() != 0){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146392, null);
			return false;
		}

		//强行罢师时间超过24小时才能拜师
		long curr = System.currentTimeMillis();
		if(apprent.getBashiflag() == 2 && curr - apprent.getBashitime() < DateValidate.dayMills){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146393, null);
			return false;
		}

		//3.查看徒弟背包是否有结拜酒 QuestItemColumn
		fire.pb.item.Pack bag = new fire.pb.item.Pack(prenticeRoleId, false);
		if(bag.removeItemById(MasterManager.JIE_BAI_JIU_ITEEM_ID, 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_jiebai, MasterManager.JIE_BAI_JIU_ITEEM_ID, "结拜扣除任务道具") != 1){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146394, null);
			fire.pb.talk.MessageMgr.sendMsgNotify(prenticeRoleId, 146394, null);
			return false;
		}

		//开始拜师
		xbean.CurrApprent newApprent = xbean.Pod.newCurrApprent();
		newApprent.setLevel(papprent.getLevel());
		newApprent.getApprentinfo().setLevel(papprent.getLevel());
		newApprent.getApprentinfo().setName(papprent.getName());
		newApprent.getApprentinfo().setRoleid(prenticeRoleId);
		newApprent.getApprentinfo().setSchool(papprent.getSchool());
		newApprent.getApprentinfo().setShap(papprent.getShape());
		newApprent.setBaishitime(System.currentTimeMillis());
		
		int totalScore = Module.getInstance().getEquipTotalScore(prenticeRoleId);
		for(int key=MasterManager.CONTINUE_LOGIN_KEY; key<=MasterManager.VIP_KEY; key++){
			xbean.ApprentceChieve achieve = xbean.Pod.newApprentceChieve();
			Integer value = MasterManager.getInstance().lianxinMap.get(key);
			if(value != null){
				achieve.setTotal(value);
			}
			newApprent.getAchievement().put(key, achieve);
		}
		menstor.getCurrapprentices().add(newApprent);

		//先注释废弃称谓，过后删除
		//第一次收徒成功 给师傅一个称谓
		/*if(menstor.getApprentices().size() == 0 && 
				menstor.getCurrapprentices().size() == 1 &&
				!Title.hasTitle(masterId, MasterManager.MASTER_TITLE_LEVEL_0)){

			Title title = new Title(masterId, false);
			title.addTitle(MasterManager.MASTER_TITLE_LEVEL_0, "", -1);
			title.onTitle(MasterManager.MASTER_TITLE_LEVEL_0);
			master.setMenstortitleid(MasterManager.MASTER_TITLE_LEVEL_0);
		}

		int appTitleId = MasterManager.getAppentceTitleIdByIndex(menstor.getCurrapprentices().size() -1);
		if(appTitleId <0){
			return false;
		}
		Title title = new Title(prenticeRoleId, false);
		if(title.roleHasTitle(appTitleId)){
			title.removeTitle(appTitleId);
		}

		STitleConfig titleConfig = TitleManager.getTitleConfigById(appTitleId);
		if(titleConfig == null){
			return false;
		}
		
		String titleName = MasterManager.getInstance().getTitleName(appTitleId);
		if(titleName == null){
			return false;
		}
		
		title.addTitle(appTitleId, prole.getName()+titleName, -1);
		title.onTitle(appTitleId);*/

		apprent.setFlag(1);
		apprent.setMenstorid(masterId);
		apprent.getAchievement().clear();
		apprent.setEqipmentscore(totalScore);
		apprent.setBashiflag(0);
		apprent.setBaishitime(curr);
		
		//先注释废弃称谓，过后删除
		//newApprent.setTitleid(appTitleId);

		List<String> paras = fire.pb.util.MessageUtil.getMsgParaList(papprent.getName(), prole.getName());
		SceneManager.sendAll(MessageMgr.getMsgNotify(146357, 0, paras));
		
		mkdb.Procedure.psendWhileCommit(prenticeRoleId, new SNotifyMaster(1));
		
		List<String> parms1 = new ArrayList<String>();
		parms1.add(prole.getName());
		MessageMgr.psendSystemMessageToRole(prenticeRoleId, 146498, parms1);
		
		List<String> parms2 = new ArrayList<String>();
		parms2.add(papprent.getName());
		MessageMgr.psendSystemMessageToRole(masterId, 146499, parms2);
		
		return true;
	}
}
