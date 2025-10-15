package fire.pb.activity.impexam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xbean.Pod;
import fire.log.Logger;
import fire.log.YYLogger;
import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.award.RewardObjs;
import fire.pb.game.ImperialExamProvinceRepo;
import fire.pb.game.ImperialExamStateRepo;
import fire.pb.game.ImperialExamVillageRepo;
import fire.pb.game.SImperialExamStateRepo;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.npc.ImpExamAssistType;
import fire.pb.npc.ImpExamType;
import fire.pb.npc.NpcManager;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.SExpMessageTips;
import fire.pb.util.Misc;

/**
 * 
 * @author cn
 */
public class PSendImpExamAnswer extends mkdb.Procedure {

	private final long roleid;
	private final int answerid;
	private final int impExamType;
	private final int impExamAssistType;
	private static Logger logger = Logger.getLogger("SYSTEM");

	public PSendImpExamAnswer(long roleid, int answerid, int type, int assistType) {
		this.roleid = roleid;
		this.answerid = answerid;
		this.impExamType = type;
		this.impExamAssistType = assistType;
	}

	@Override
	protected boolean process() throws Exception {
		//检测一下当前是否在活动范围内
		int ieType = ImpExamManager.getInstance().isInImpExamTime();
		
		xbean.ImpExamRecord record = xtable.Role2impexam.get(roleid);
		xbean.ImpExamStateRecord dsRecord = null;
		
		if(record == null || !ImpExamManager.getInstance().answerCheck(roleid)){
			return false;
		}
		
		//两道题的答题时间小于2小时  认为是一次智力试练活动以内的
		if(System.currentTimeMillis() - record.getBegintime() < ImpExamManager.TOWNE_HOUR){
			//不是答题中
			if(record.getFlag() != 1){
				return false;
			}
		}
		
//		//不在同一天 
//		if(!DateValidate.inTheSameDay(System.currentTimeMillis(), record.getBegintime())){
//			//不是答题中
//			if(record.getFlag() != 1){
//				return false;
//			}
//		}
		
		//考试时间结束    
		if(ieType == -1){
			record.setFlag(3);
		}
		
		//或者客户端发送的进程与服务器不一致   客户端结束
		int step = record.getStep();
		if(ieType == -1 || step != impExamType){
			sendImpExamOver(record);
			return true;
		}
		
		//全省统考答最后一道题目   或者全国统考累积答对4道题目才能锁   防止不必要的浪费
		if((step == ImpExamType.IMPEXAM_PROV && record.getLastquesid() == 19) ||
		   (step == ImpExamType.IMPEXAM_STATE && record.getAccrightnum() == 4 )){
			dsRecord = xtable.Impexamstatetbl.get(1);
			if(dsRecord == null){
				dsRecord = Pod.newImpExamStateRecord();
				xtable.Impexamstatetbl.insert(1, dsRecord);
			}
		}
		
		fire.pb.PropRole prole = new fire.pb.PropRole(roleid, true);
		if(step == ImpExamType.IMPEXAM_PROV 
				&& impExamAssistType == ImpExamAssistType.DELWRONG){
			if(record.getDelwrongval() > 0){
				if(record.getWorngqueslist().size() <= 0){
					return false;
				}
				//处理删除错误答案
				delProvExamErrorAnswer(record, prole);
				return true;
			}else{
				return false;
			}
		}
		
		if(step == ImpExamType.IMPEXAM_VILL){
			return processVillExamAnswer(record, prole);
		}else if(step == ImpExamType.IMPEXAM_PROV){
			return processProvExamAnswer(record, prole, dsRecord, impExamAssistType);
		}else{
			return processStateExamAnswer(record, prole, dsRecord);
		}
	}
	
	//全村统考答题
	private boolean processVillExamAnswer(xbean.ImpExamRecord record, fire.pb.PropRole prole){
		
		int answer = record.getLastanswer();
		int right = record.getMaxcontinueright();
		int acright = record.getAccrightnum();
		int acexp = record.getCurrexamaccexp();
		int acmoney = record.getCurrexamaccmoney();
		int historyRight = record.getMaxvillexamright();
		int index = record.getLastquesid();
		int curright = record.getCurrexamaccright();
		int delwrongval = record.getDelwrongval();
		int chorightval = record.getChorightval();
		List<Integer> idList = record.getQuesidlist();
		int rewardId = 0;
		long curExamExp = 0;
		long curExamMoney = 0;
		
		
		java.util.NavigableMap<Integer, ImperialExamVillageRepo> keju1RepositoryMap = NpcManager.getInstance().getKeju1RepositoryMap();
		ImperialExamVillageRepo curRep = keju1RepositoryMap.get(idList.get(index));
		
		if(answer == answerid){
			right++;
			record.setAccrightnum(acright + 1);
			record.setLastisright(1);
			
			record.setCurrexamaccright(curright + 1);
			if((curright + 1) == 10){
				//获得宝珠
				record.setDelwrongval(delwrongval + 1);
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 190009);
			}else if((curright + 1) == 20){
				//获得棱镜
				record.setChorightval(chorightval + 1);
				
				fire.pb.course.CourseManager.achieveUpdate(roleid, fire.pb.course.CourseType.ZHI_HUI_SI_LIAN_CHU);
				
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid,190010);
			}
			
			if(historyRight<acright + 1){
				record.setMaxvillexamright(acright + 1);
			}
			rewardId = curRep.getRightrewardid();
		}else{
			right =0;
			record.setLastisright(0);
			rewardId = curRep.getErrorrewardid();
		}
		
		
		/*int times = 0;
		if(right == 0){
			times = -6;
		}else{
			times = right >=6 ? 6: right;
		}*/
		
		record.setMaxcontinueright(right);
		record.setLastanswertime(System.currentTimeMillis());
		
		//计算奖励开始
		Map<String, Object> paras = new HashMap<String, Object>(10);
		paras.put(RewardMgr.ANSWER_RIGHT_TIMES, record.getCurrexamaccright());
		Map<Integer, RewardObjs> result = RewardMgr.getInstance().distributeAllAward(roleid, rewardId, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_zhihuishilian, 0,
				PAddExpProc.OTHER_QUEST, "智慧试练", false);
		
		//运营日志
		YYLogger.completeTaskLog(roleid, RoleLiveness.IMPEXAMVILL, YYLogger.SUCC_FLAG, "");

		RewardObjs moneyaward = result.get(RewardMgr.MONEY_AWARD);
		if (moneyaward != null)
		{
			curExamMoney = moneyaward.getValue();
		}
		
		RewardObjs expaward = result.get(RewardMgr.EXP_AWARD);
		if (expaward != null)
		{
			curExamExp = expaward.getValue();
		}
		
		record.setCurrexamaccmoney(acmoney + (int)curExamMoney);
		record.setCurrexamaccexp(acexp + (int)curExamExp);
		
		RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
		if (actrole != null){
			actrole.handleActiveLivenessData(RoleLiveness.IMPEXAMVILL);
		}
		
		//计算奖励结束
		
		if(index +1>=idList.size()){
			record.setFlag(4);
			ImpExamManager.getInstance().sendVillExamQuestion(roleid, record, 1);
			
			return true;
		}
		
		//得到下一道题
		ImperialExamVillageRepo rep = keju1RepositoryMap.get(idList.get(index + 1));
		int nextAnswerId = 1;
		if(rep != null){
			nextAnswerId = rep.getAnswer();
		}
		record.setLastanswer(nextAnswerId);
		record.setLastquesid(index + 1);
		ImpExamManager.getInstance().sendVillExamQuestion(roleid, record, 0);
		
//		java.util.List<String> strings = new java.util.ArrayList<String>();
//		strings.add(String.valueOf(curExamExp));
//		strings.add(String.valueOf(curExamMoney));
//		fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 160481, strings);
		//发送经验和金钱提示
		sendExpAndMoneyTips(curExamExp,curExamMoney,result);
		
		return true;
	}
	
	//全省统考答题
	private boolean processProvExamAnswer(xbean.ImpExamRecord record, fire.pb.PropRole prole, xbean.ImpExamStateRecord ieStateRecord, int assistType){

		int answer = record.getLastanswer();
		int right = record.getMaxcontinueright();
		int acright = record.getAccrightnum();
		int acexp = record.getCurrexamaccexp();
		int acmoney = record.getCurrexamaccmoney();
		int index = record.getLastquesid();
		List<Integer> idList = record.getQuesidlist();
		int rewardId = 0;
		long curExamExp = 0;
		long curExamMoney = 0;
		long cur = System.currentTimeMillis();
		long diffTime = (cur - record.getExamsendquestime()) / 1000;
		int curright = record.getCurrexamaccright();
		
		if(ImpExamAssistType.CHORIGHT == assistType){
			if (record.getChorightval() <= 0){
				return false;
			}
		}
		
		Map<Integer, Integer> map = record.getRightquesmap();
		int page = index/5+1;
		if(map.get(page) == null){
			map.put(page, 0);
		}
		
		java.util.NavigableMap<Integer, ImperialExamProvinceRepo> keju2RepositoryMap = NpcManager.getInstance().getKeju2RepositoryMap();
		ImperialExamProvinceRepo curRep = keju2RepositoryMap.get(idList.get(index));
		
		if(answer == answerid 
				|| (ImpExamAssistType.CHORIGHT == assistType && record.getChorightval() > 0)){
			right++;
			acright++;
			record.setAccrightnum(acright);
			record.setLastisright(1);
			int rightTimes = map.get(page);
			map.put(page, rightTimes +1);

			record.setExamconsumetime(diffTime);
			record.setCurrexamaccright(curright + 1);
			rewardId = curRep.getRightrewardid();
			if(ImpExamAssistType.CHORIGHT == assistType){
				int curChoRightVal = record.getChorightval();
				curChoRightVal -= 1;
				record.setChorightval(curChoRightVal);
			}
		}else{
			right =0;
			record.setLastisright(0);
			record.setExamconsumetime(diffTime + 20);
			rewardId = curRep.getErrorrewardid();
		}
		
		record.setMaxcontinueright(right);
		record.setLastanswertime(cur);
		
		//计算奖励开始
		Map<String, Object> paras = new HashMap<String, Object>(10);
		paras.put(RewardMgr.ANSWER_RIGHT_TIMES, record.getCurrexamaccright());
		Map<Integer, RewardObjs> result = RewardMgr.getInstance().distributeAllAward(roleid, rewardId, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_zhihuishilian, 0,
				PAddExpProc.OTHER_QUEST, "智慧试练", false);
		
		//运营日志
		YYLogger.completeTaskLog(roleid, RoleLiveness.IMPEXAMPROV, YYLogger.SUCC_FLAG, "");

		RewardObjs moneyaward = result.get(RewardMgr.MONEY_AWARD);
		if (moneyaward != null){
			curExamMoney = moneyaward.getValue();
		}
		
		RewardObjs expaward = result.get(RewardMgr.EXP_AWARD);
		if (expaward != null){
			curExamExp = expaward.getValue();
		}
		
		record.setCurrexamaccmoney(acmoney + (int)curExamMoney);
		record.setCurrexamaccexp(acexp + (int)curExamExp);
		
		RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
		if (actrole != null){
			actrole.handleActiveLivenessData(RoleLiveness.IMPEXAMPROV);
		}
		//计算奖励结束
		
		if(index +1>=idList.size()){//通关  开启全国统考
			if(checkPassProvExam(record)){
				if(ieStateRecord == null){
					ieStateRecord = Pod.newImpExamStateRecord();
					xtable.Impexamstatetbl.insert(1, ieStateRecord);
				}
				if(!ieStateRecord.getIdslist().contains(roleid)){
					ieStateRecord.getIdslist().add(roleid);
				}
				record.setFlag(3);
				ImpExamManager.getInstance().sendProvExamQuestion(roleid, record, 1);
				
//				java.util.List<String> strings = new java.util.ArrayList<String>();
//				strings.add(String.valueOf(curExamExp));
//				strings.add(String.valueOf(curExamMoney));
//				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 160481, strings);
				//发送经验和金钱提示
				sendExpAndMoneyTips(curExamExp,curExamMoney,result);
				
				startStateExam(record);
				ImpExamManager.getInstance().sendImpExamStart(roleid, record, ImpExamType.IMPEXAM_STATE);
				
				//完成参加智力试练全国统考历程
				fire.pb.course.CourseManager.achieveCourse(roleid, fire.pb.course.CourseType.ACTIVE_COURSE, RoleLiveness.IMPEXAMSTATE, 0);
			}else{
				record.setFlag(3);
				ImpExamManager.getInstance().sendProvExamQuestion(roleid, record, 1);
				
//				java.util.List<String> strings = new java.util.ArrayList<String>();
//				strings.add(String.valueOf(curExamExp));
//				strings.add(String.valueOf(curExamMoney));
//				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 160481, strings);
				//发送经验和金钱提示
				sendExpAndMoneyTips(curExamExp,curExamMoney,result);
			}
			
		}else{
			ImperialExamProvinceRepo rep = keju2RepositoryMap.get(idList.get(index +1));
			int nextAnswerId = 1;
			if(rep != null){
				nextAnswerId = rep.getAnswer();
			}
			record.setLastanswer(nextAnswerId);
			record.setLastquesid(index +1);
			record.setExamsendquestime(cur);
			//错误答案
			//logger.debug("ProvExam roleid:" + roleid + " quesid:" + rep.id + " answer:" + rep.answer);
			List<Integer> wrongQuesList = record.getWorngqueslist();
			wrongQuesList.clear();
			for(int i=1;i<=4;i++){
				if(rep.getAnswer() != i){
					wrongQuesList.add(i);
					logger.debug("ProvExam roleid:" + roleid + " quesid:" + rep.id + " wronganswer:" + i);
				}
			}
			ImpExamManager.getInstance().sendProvExamQuestion(roleid, record, 0);
			
//			java.util.List<String> strings = new java.util.ArrayList<String>();
//			strings.add(String.valueOf(curExamExp));
//			strings.add(String.valueOf(curExamMoney));
//			fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 160481, strings);
			//发送经验和金钱提示
			sendExpAndMoneyTips(curExamExp,curExamMoney,result);
		}
		
		return true;
	}
	
	
	/**
	 * 检查全省统考是否通过
	 * @param map
	 * @return
	 */
	private boolean checkPassProvExam(xbean.ImpExamRecord record){
		int curRight = record.getCurrexamaccright();
		long curConsumeTime = record.getExamconsumetime();
		if(curRight < 15 || curConsumeTime > 300){
			return false;
		}
		return true;
	}
	
	//全省统考答题
	private void delProvExamErrorAnswer(xbean.ImpExamRecord record, fire.pb.PropRole prole){

		List<Integer> errIdList = record.getWorngqueslist();
		
		int delIdx = Misc.getRandomBetween(0, errIdList.size()-1);
		int delQueId = errIdList.get(delIdx);
		errIdList.remove(delIdx);
		int curDelWrongVal = record.getDelwrongval();
		curDelWrongVal -= 1;
		if(curDelWrongVal < 0){
			curDelWrongVal = 0;
		}
		logger.debug("delProvExamErrorAnswer roleid:" + roleid + " delIdx:" + delIdx + " delQueId:" + delQueId);
		record.setDelwrongval(curDelWrongVal);
		
		ImpExamManager.getInstance().sendImpExamAssist(roleid, ImpExamType.IMPEXAM_PROV, ImpExamAssistType.DELWRONG, delQueId);

	}
	
	//通知客户端打开全国统考开始窗口
	private void startStateExam(xbean.ImpExamRecord record){
		
		record.setStep(ImpExamType.IMPEXAM_STATE);
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
		record.setBegintime(System.currentTimeMillis());
		record.setFlag(1);
		record.setStep(ImpExamType.IMPEXAM_STATE);
		record.setLastisright(-1);
		record.setMaxcontinueright(0);
		record.setCurrexamaccexp(0);
		record.setCurrexamaccmoney(0);
		record.setAccrightnum(0);
		record.setStateexamtime(0);
	}
	
	private boolean processStateExamAnswer(xbean.ImpExamRecord record, fire.pb.PropRole prole, xbean.ImpExamStateRecord dsRecord){
		
		int answer = record.getLastanswer();
		int right = record.getMaxcontinueright();
		int acright = record.getAccrightnum();
		int acexp = record.getCurrexamaccexp();
		int acmoney = record.getCurrexamaccmoney();
		int historyRight = record.getMaxstateexamright();
		int index = record.getLastquesid();
		int curright = record.getCurrexamaccright();
		List<Integer> idList = record.getQuesidlist();
		long cur = System.currentTimeMillis();
		int rewardId = 0;
		long curExamExp = 0;
		long curExamMoney = 0;
		
		java.util.NavigableMap<Integer, ImperialExamStateRepo> keju3RepositoryMap = NpcManager.getInstance().getKeju3RepositoryMap();
		ImperialExamStateRepo curRep = keju3RepositoryMap.get(idList.get(index));
		if(answer == answerid){
			right++;
			acright++;
			record.setAccrightnum(acright);
			record.setLastisright(1);
			record.setCurrexamaccright(curright + 1);
			
			if(historyRight<acright+1){
				record.setMaxstateexamright(acright);
			}
			rewardId = curRep.getRightrewardid();
		}else{
			right =0;
			record.setLastisright(0);
			rewardId = curRep.getErrorrewardid();
		}
		
		record.setMaxcontinueright(right);
		record.setLastanswertime(cur);
		
		//计算奖励开始
		Map<String, Object> paras = new HashMap<String, Object>(10);
		paras.put(RewardMgr.ANSWER_RIGHT_TIMES, record.getCurrexamaccright());
		Map<Integer, RewardObjs> result = RewardMgr.getInstance().distributeAllAward(roleid, rewardId, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_zhihuishilian, 0,
				PAddExpProc.OTHER_QUEST, "智慧试练", false);
		
		//运营日志
		YYLogger.completeTaskLog(roleid, RoleLiveness.IMPEXAMSTATE, YYLogger.SUCC_FLAG, "");

		RewardObjs moneyaward = result.get(RewardMgr.MONEY_AWARD);
		if (moneyaward != null)
		{
			curExamMoney = moneyaward.getValue();
		}
		
		RewardObjs expaward = result.get(RewardMgr.EXP_AWARD);
		if (expaward != null)
		{
			curExamExp = expaward.getValue();
		}
		
		record.setCurrexamaccmoney(acmoney + (int)curExamMoney);
		record.setCurrexamaccexp(acexp + (int)curExamExp);
		
		RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
		if (actrole != null){
			actrole.handleActiveLivenessData(RoleLiveness.IMPEXAMSTATE);
		}
		
		//计算奖励结束
		
		int time  = (index + 1 - acright) * 30 * 1000;
		long passDSTime = System.currentTimeMillis() - record.getBegintime();
		if(time>0){
			passDSTime = passDSTime+time;
		}
		record.setStateexamtime(passDSTime);
		
		//答到最后一题了
		if(index +1>=idList.size()){
			if(record.getMinstateexamtime() == 0){
				record.setMinstateexamtime(passDSTime);
			}else if(passDSTime<record.getMinstateexamtime()){
				record.setMinstateexamtime(passDSTime);
			}
			record.setFlag(4);
			
			//答对3道题目才算过关
			if(acright >= 3){
				if(dsRecord == null){
					dsRecord = xtable.Impexamstatetbl.get(1);
					if(dsRecord == null){
						dsRecord = Pod.newImpExamStateRecord();
						xtable.Impexamstatetbl.insert(1, dsRecord);
					}
				}
				if(!dsRecord.getPassidslist().contains(roleid)){
					dsRecord.getPassidslist().add(roleid);
					int idsIndex = dsRecord.getPassidslist().indexOf(roleid);
					xbean.ImpExamStatePassRole iesPassRole = Pod.newImpExamStatePassRole();
					iesPassRole.setRoleid(roleid);
					iesPassRole.setAccrightnum(acright);
					iesPassRole.setStateexamtime(passDSTime);
					dsRecord.getPassrolelist().add(idsIndex,iesPassRole);
				}else{
					int idsIndex = dsRecord.getPassidslist().indexOf(roleid);
					xbean.ImpExamStatePassRole iesPassRole = Pod.newImpExamStatePassRole();
					iesPassRole.setRoleid(roleid);
					iesPassRole.setAccrightnum(acright);
					iesPassRole.setStateexamtime(passDSTime);
					dsRecord.getPassrolelist().add(idsIndex, iesPassRole);
				}
			}
			
			ImpExamManager.getInstance().sendStateExamQuestion(roleid, record, 1);
			
//			java.util.List<String> strings = new java.util.ArrayList<String>();
//			strings.add(String.valueOf(curExamExp));
//			strings.add(String.valueOf(curExamMoney));
//			fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 160481, strings);
			//发送经验和金钱提示
			sendExpAndMoneyTips(curExamExp,curExamMoney,result);
			
			return true;
		}
		
		ImperialExamStateRepo rep = keju3RepositoryMap.get(idList.get(index +1));
		int nextAnswerId = 1;
		if(rep != null){
			nextAnswerId = rep.getAnswer();
		}
		record.setLastanswer(nextAnswerId);
		record.setLastquesid(index +1);
		record.setBegintime(cur);
		ImpExamManager.getInstance().sendStateExamQuestion(roleid, record, 0);
		
//		java.util.List<String> strings = new java.util.ArrayList<String>();
//		strings.add(String.valueOf(curExamExp));
//		strings.add(String.valueOf(curExamMoney));
//		fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 160481, strings);
		//发送经验和金钱提示
		sendExpAndMoneyTips(curExamExp,curExamMoney,result);
		
		return true;
	}
	
	/**
	 * 检验答题是否通过
	 * @param map
	 * @param index
	 * @return
	 */
	public boolean checkPassProvExam(Map<Integer, Integer> map, int index, int page){
		int times = map.get(page);
		if(index == 4 && times<3){
			return false;
		}else if ((index ==9 ||index == 14) && times<4){
			return false;
		}else if(index == 19 && times<5){
			return false;
		}
		return true;
	}
	
	
	private void sendImpExamOver(xbean.ImpExamRecord record){
		if(impExamType == ImpExamType.IMPEXAM_VILL){
			ImpExamManager.getInstance().sendVillExamQuestion(roleid, record, 1);
		}else if(impExamType == ImpExamType.IMPEXAM_PROV){
			ImpExamManager.getInstance().sendProvExamQuestion(roleid, record, 1);
		}else{
			ImpExamManager.getInstance().sendStateExamQuestion(roleid, record, 1);
		}
	}
	
	/**
	 * 发送经验和金钱提示
	 * 
	 * @author yangtao
	 * @dateTime 2016年7月8日 上午10:32:58
	 * @version 1.0
	 * @param curExamExp
	 * @param curExamMoney
	 */
	private void sendExpAndMoneyTips(long curExamExp, long curExamMoney) {
		java.util.List<String> strings = new java.util.ArrayList<String>();
		strings.add(String.valueOf(curExamExp));
		strings.add(String.valueOf(curExamMoney));
		fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 160481, strings);
	}

	/**
	 * 发送经验和金钱提示
	 * @author yangtao
	 * @dateTime 2016年7月8日 上午10:38:17
	 * @version 1.0
	 * @param curExamExp
	 * @param curExamMoney
	 * @param result
	 */
	private void sendExpAndMoneyTips(long curExamExp, long curExamMoney, Map<Integer, RewardObjs> result) {
		long reviseExp = 0;
		long yingfuExpVal = 0;
		RewardObjs reviseExpAward = result.get(RewardMgr.EXP_ADD_AWARD);
		if (reviseExpAward != null) {
			reviseExp = reviseExpAward.getValue();
		}
		RewardObjs yingfuExpValAward = result.get(RewardMgr.EXP_YF_AWARD);
		if (yingfuExpValAward != null) {
			yingfuExpVal = yingfuExpValAward.getValue();
		}
		if (yingfuExpVal > 0 || reviseExp != 0) {
			// 发送经验
			HashMap<Integer, Long> msgtips = new HashMap<Integer, Long>();
			if (yingfuExpVal > 0)
				msgtips.put(180003, yingfuExpVal);
			if (reviseExp > 0)
				msgtips.put(180004, reviseExp);
			if (reviseExp < 0)
				msgtips.put(180005, -reviseExp);
			SExpMessageTips expMsg = new SExpMessageTips(180001, curExamExp, msgtips);
			mkdb.Procedure.psendWhileCommit(roleid, expMsg);
			// 发送金钱
			java.util.List<String> strings = new java.util.ArrayList<String>();
			strings.add(String.valueOf(curExamMoney));
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, MessageMgr.ADD_MONEY, strings);
		} else {
			sendExpAndMoneyTips(curExamExp, curExamMoney);
		}
	}
	
}
