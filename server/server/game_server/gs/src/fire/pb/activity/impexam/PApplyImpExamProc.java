package fire.pb.activity.impexam;

import java.util.List;

import fire.log.Logger;
import fire.pb.game.ImperialExamProvinceRepo;
import fire.pb.game.ImperialExamStateRepo;
import fire.pb.game.ImperialExamVillageRepo;
import fire.pb.game.SImperialExamVillageRepo;
import fire.pb.mission.SRemoveTuiSong;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.npc.ImpExamType;
import fire.pb.npc.NpcManager;
import fire.pb.util.DateValidate;
import fire.pb.util.Misc;

/**
 * 申请智力试练 开始    智力试练
 * @author cn
 */
public class PApplyImpExamProc extends mkdb.Procedure{
	
	private final long roleId;
	private final int type; // 1:全村统考 2：全省统考 3:全国统考
	private final int operate; // 0=申请  1=开始
	private static Logger logger = Logger.getLogger("SYSTEM");
	
	public PApplyImpExamProc(long roleId, int type, int operate){
		this.roleId = roleId;
		this.type = type;
		this.operate = operate;
	}

	@Override
	protected boolean process() throws Exception {
		
		//检测一下当前是否在活动范围内
		boolean isIe = ImpExamManager.getInstance().isImpExamTime(type);
		if(!isIe){
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,140434, null);
			return false;
		}
		
		if(!ImpExamManager.getInstance().answerCheck(roleId)){
			return false;
		}
		
		xbean.ImpExamRecord record = xtable.Role2impexam.get(roleId);
		if(record == null){
			record =  xbean.Pod.newImpExamRecord();
			xtable.Role2impexam.insert(roleId, record);
		}
		
		if(type == ImpExamType.IMPEXAM_VILL){
			return processVillExam(record);
		}else
		if(type == ImpExamType.IMPEXAM_PROV){
			return processProvExam(record);
		}else
		if(type == ImpExamType.IMPEXAM_STATE){
			return processStateExam(record);
		}else{
			return false;
		}
	}
	
	//全村统考处理
	private boolean processVillExam(xbean.ImpExamRecord record){
		//上次答题时间
		int flag = record.getFlag();
		long cur = System.currentTimeMillis();
		
		fire.pb.activity.ActivityConfNew activity = 
				RoleLiveness.getConfigActivity(fire.pb.mission.activelist.RoleLiveness.IMPEXAMVILL);
		int roleLv = xtable.Properties.selectLevel(roleId);
		if(roleLv < activity.level){
			return false;
		}
		
		//不是一周的  清零
		if(!DateValidate.inTheSameWeek(cur, record.getBegintime())){
			record.setCurrexamaccright(0);
			record.setDelwrongval(0);
			record.setChorightval(0);
			record.setHelpcnt(0);
		}
		if(!DateValidate.inTheSameDay(cur, record.getBegintime()) || ImpExamManager.test){
			record.getQuesidlist().clear();
			java.util.NavigableMap<Integer, ImperialExamVillageRepo> keju1RepositoryMap = NpcManager.getInstance().getKeju1RepositoryMap();
			Integer dest[]=Misc.getRandomValues(1, keju1RepositoryMap.size(), ImpExamConst.IMPEXAM_VILL_QUESIZE).toArray(new Integer[ImpExamConst.IMPEXAM_VILL_QUESIZE]);
			List<Integer> qIDList = record.getQuesidlist();
			for (int i = 0; i < dest.length; i++) {
				qIDList.add(dest[i]);
			}
			ImperialExamVillageRepo rep = keju1RepositoryMap.get(qIDList.get(0));
			record.setLastanswer(rep.getAnswer());
			record.setLastquesid(0);
			record.setBegintime(cur);
			record.setFlag(1);
			record.setStep(ImpExamType.IMPEXAM_VILL);
			record.setLastisright(-1);
			record.setMaxcontinueright(0);
			record.setCurrexamaccexp(0);
			record.setCurrexamaccmoney(0);
			record.setAccrightnum(0);
			record.setCurrexamaccright(0);
			record.setHelpcnt(0);
			flag = record.getFlag();
		}else{
			//答题失败或者已经放弃过答题
			if(flag !=1){
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,143812, null);
				return false;
			}
		}
		
		ImpExamManager.getInstance().sendVillExamQuestion(roleId, record, 0);		
		return true;
	}
	
	//全省统考全国统考处理
	private boolean processProvExam(xbean.ImpExamRecord record){
		
		//上次答题时间
		int flag = record.getFlag();
		long cur = System.currentTimeMillis();
		int step = record.getStep();
		
		fire.pb.activity.ActivityConfNew activity = 
				RoleLiveness.getConfigActivity(fire.pb.mission.activelist.RoleLiveness.IMPEXAMPROV);
		int roleLv = xtable.Properties.selectLevel(roleId);
		if(roleLv < activity.level){
			return false;
		}
		
		//不在同一天的 直接生成题目
		if(!DateValidate.inTheSameDay(cur, record.getBegintime()) || ImpExamManager.test){
			
			//申请开始答题
			/*if(operate == 0 && type== ImpExamType.IMPEXAM_PROV){
				ImpExamManager.getInstance().sendImpExamStart(roleId, record ,ImpExamType.IMPEXAM_PROV);
				return true;
			}*/
			//正式开始答题 
			record.getQuesidlist().clear();
			java.util.NavigableMap<Integer, ImperialExamProvinceRepo> keju2RepositoryMap = NpcManager.getInstance().getKeju2RepositoryMap();
			Integer dest[]=Misc.getRandomValues(1, keju2RepositoryMap.size(), ImpExamConst.IMPEXAM_PROV_QUESIZE).toArray(new Integer[ImpExamConst.IMPEXAM_PROV_QUESIZE]);
			List<Integer> qIDList = record.getQuesidlist();
			for (int i = 0; i < dest.length; i++) {
				qIDList.add(dest[i]);
			}
			ImperialExamProvinceRepo rep = keju2RepositoryMap.get(qIDList.get(0));
			record.setLastanswer(rep.getAnswer());
			record.setLastquesid(0);
			record.setBegintime(cur);
			record.setFlag(1);
			record.setStep(ImpExamType.IMPEXAM_PROV);
			record.setLastisright(-1);
			record.getRightquesmap().put(1, 0);
			record.getRightquesmap().put(2, 0);
			record.getRightquesmap().put(3, 0);
			record.getRightquesmap().put(4, 0);
			record.setMaxcontinueright(0);
			record.setCurrexamaccexp(0);
			record.setCurrexamaccmoney(0);
			record.setAccrightnum(0);
			record.setCurrexamaccright(0);
			//测试加
			//record.setDelwrongval(10);
			//record.setChorightval(10);
			flag = record.getFlag();
			//错误答案
			logger.debug("ProvExam roleid:" + roleId + " quesid:" + rep.id + " answer:" + rep.answer);
			List<Integer> wrongQuesList = record.getWorngqueslist();
			for(int i=1;i<=4;i++){
				if(rep.getAnswer() != i){
					wrongQuesList.add(i);
					logger.debug("ProvExam roleid:" + roleId + " quesid:" + rep.id + " wronganswer:" + i);
				}
			}
		}else{
			//答题失败或者已经放弃过答题
			if(flag != 1){
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,143812, null);
				return false;
			}
		}
		
		step = record.getStep();
		if(step != ImpExamType.IMPEXAM_PROV){
			return false;
		}
		
		record.setExamsendquestime(cur);
		//发送全省统考题目
		ImpExamManager.getInstance().sendProvExamQuestion(roleId, record, 0);
	
		return true;
	}
	
	//全国统考处理
	private boolean processStateExam(xbean.ImpExamRecord record){
		
		//上次答题时间
		int flag = record.getFlag();
		long cur = System.currentTimeMillis();
		int step = record.getStep();
		xbean.ImpExamStateRecord iesRecord = null;
		
		iesRecord = xtable.Impexamstatetbl.get(1);
		if(iesRecord == null){
			return false;
		}
		
		if(!iesRecord.getIdslist().contains(roleId)){
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,190013, null);
		}
		
		//不在同一天的 直接生成题目
		if(!DateValidate.inTheSameDay(cur, record.getBegintime()) || ImpExamManager.test){
			
			//申请开始答题
			if(operate == 0 && type== ImpExamType.IMPEXAM_STATE){
				ImpExamManager.getInstance().sendImpExamStart(roleId, record ,ImpExamType.IMPEXAM_STATE);
				return true;
			}
			//正式开始答题 
			record.getQuesidlist().clear();
			java.util.NavigableMap<Integer, ImperialExamStateRepo> keju3RepositoryMap = NpcManager.getInstance().getKeju3RepositoryMap();
			Integer dest[]=Misc.getRandomValues(1, keju3RepositoryMap.size(), ImpExamConst.IMPEXAM_STATE_QUESIZE).toArray(new Integer[ImpExamConst.IMPEXAM_STATE_QUESIZE]);
			List<Integer> qIDList = record.getQuesidlist();
			for (int i = 0; i < dest.length; i++) {
				qIDList.add(dest[i]);
			}
			ImperialExamStateRepo rep = keju3RepositoryMap.get(qIDList.get(0));
			record.setLastanswer(rep.getAnswer());
			record.setLastquesid(0);
			record.setBegintime(cur);
			record.setFlag(1);
			record.setStep(ImpExamType.IMPEXAM_PROV);
			record.setLastisright(-1);
			record.getRightquesmap().put(1, 0);
			record.getRightquesmap().put(2, 0);
			record.getRightquesmap().put(3, 0);
			record.getRightquesmap().put(4, 0);
			record.setMaxcontinueright(0);
			record.setCurrexamaccexp(0);
			record.setCurrexamaccmoney(0);
			record.setAccrightnum(0);
			record.setCurrexamaccright(0);
			flag = record.getFlag();
		}else{
			//答题失败或者已经放弃过答题
			if(flag != 1){
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,143812, null);
				return false;
			}
		}
		
		step = record.getStep();
		if(step != ImpExamType.IMPEXAM_STATE){
			return false;
		}			
		//发送全国统考题目
		ImpExamManager.getInstance().sendStateExamQuestion(roleId, record, 0);
		
		SRemoveTuiSong send = new SRemoveTuiSong();
		send.removeid = 16;
		mkdb.Procedure.psendWhileCommit(roleId, send);

		return true;
	}
}
