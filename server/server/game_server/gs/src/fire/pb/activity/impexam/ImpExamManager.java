package fire.pb.activity.impexam;

import java.util.List;
import java.util.Map;

import fire.log.Logger;
import fire.msp.IGetRolesCallBack;
import fire.pb.map.MapConfig;
import fire.pb.map.RoleManager;
import fire.pb.mission.SNotifyTuiSongList;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.mission.activelist.SActivityOpen;
import fire.pb.mission.notify.TuiSongNotifyManager;
import fire.pb.npc.ImpExamType;
import fire.pb.npc.SAttendImpExam;
import fire.pb.npc.SImpExamHelp;
import fire.pb.npc.SSendImpExamAssist;
import fire.pb.npc.SSendImpExamState;
import fire.pb.npc.SSendImpExamProv;
import fire.pb.npc.SSendImpExamStart;
import fire.pb.npc.SSendImpExamVill;
import fire.pb.util.DateValidate;

/**
 * 
 * @author cn
 */
public class ImpExamManager {
	
	static class SingletonHolder {
		static ImpExamManager singleton = new ImpExamManager();
	}

	public static ImpExamManager getInstance() {
		return SingletonHolder.singleton;
	}

	public static final Logger logger = Logger.getLogger("SYSTEM");
	
	//全村统考活动开始和结束时间
	private long villExamStartTime = 0;
	private long villExamEndTime = 0;
	
	//全省统考开始和结束时间
	private long provExamStartTime = 0;
	private long provExamEndTime = 0;
	
	//全国统考开始和结束时间
	private long stateExamStartTime = 0;
	private long stateExamEndTime = 0;
	
	public static long TOWNE_HOUR = DateValidate.hourMills*2;
	
	public static boolean test = false;
	
	private ImpExamManager(){
	}
	
	public void initVillExam(long begin, long end){
		provExamStartTime = 0;
		provExamEndTime = 0;
		stateExamStartTime = 0;
		stateExamEndTime = 0;
		villExamStartTime = begin;
		villExamEndTime = end;
		impExamPull(ImpExamType.IMPEXAM_VILL);
	}
	
	public void initVillExamResume(long begin, long end){
		provExamStartTime = 0;
		provExamEndTime = 0;
		stateExamStartTime = 0;
		stateExamEndTime = 0;
		villExamStartTime = begin;
		villExamEndTime = end;
	}
	
	public void initProvExam(long begin, long end){
		villExamStartTime = 0;
		villExamEndTime = 0;
		provExamStartTime = begin;
		provExamEndTime = end;
		new PClearImpExamProvProc().submit(); 
	}
	
	public void initProvExamResume(long begin, long end){
		villExamStartTime = 0;
		villExamEndTime = 0;
		provExamStartTime = begin;
		provExamEndTime = end;
	}
	
	public void initStateExam(long begin, long end){
		villExamStartTime = 0;
		villExamEndTime = 0;
		stateExamStartTime = begin;
		stateExamEndTime = end;
		new PClearImpExamStateProc().submit(); 
	}
	
	public void initStateExamResume(long begin, long end){
		villExamStartTime = 0;
		villExamEndTime = 0;
		stateExamStartTime = begin;
		stateExamEndTime = end;
	}
	
	public void endImpExam(){
		provExamStartTime = 0;
		provExamEndTime = 0;
		stateExamStartTime = 0;
		stateExamEndTime = 0;
		villExamStartTime = 0;
		villExamEndTime = 0;
	}
	
	public void endStateExam(){
		new PImpExamStateRankProc().submit();
	}
	
	public void impExamPull(final int impExamType){
		RoleManager.getRolesByConditions(-1, 30, -1, -1, -1, 
				new IGetRolesCallBack(){
			
			@Override
			public void process(List<Long> roleIds){
				
	 			SActivityOpen so = new SActivityOpen();
				
				//智力试练全村统考拉人
				SAttendImpExam send = new SAttendImpExam();
				send.impexamtype = impExamType;
				
				//全村统考拉人
				if(ImpExamType.IMPEXAM_VILL == impExamType){
					if(roleIds.size()>0){
						so.activityid = 214;
						mkdb.Procedure.psendWhileCommit(roleIds, send);
						mkdb.Procedure.psendWhileCommit(roleIds, so);
					}
				}
				
				//全省统考拉人
				if(ImpExamType.IMPEXAM_PROV == impExamType){
					so.activityid = 215;
					SNotifyTuiSongList notifyList = new SNotifyTuiSongList();
					notifyList.notifylist.add(TuiSongNotifyManager.IMPEXAM_PROV);

					if(roleIds.size() >0){
						mkdb.Procedure.psendWhileCommit(roleIds, send);
						mkdb.Procedure.psendWhileCommit(roleIds, notifyList);
						mkdb.Procedure.psendWhileCommit(roleIds, so);
					}
				}
			}
		});
	}
	
	public boolean isVillExamTime(){
		long cur = System.currentTimeMillis();
		if(villExamStartTime <cur && villExamEndTime>cur){
			return true;
		}
		return false;
	}
	
	public boolean isProvExamTime(){
		long cur = System.currentTimeMillis();
		if(provExamStartTime <cur && provExamEndTime>cur){
			return true;
		}
		return false;
	}
	
	public boolean isStateExamTime(){
		long cur = System.currentTimeMillis();
		if(stateExamStartTime <cur && stateExamEndTime>cur){
			return true;
		}
		return false;
	}
	
	public int isInImpExamTime(){
		
		if(isVillExamTime()){
			return ImpExamType.IMPEXAM_VILL;
		}
		
		if(isProvExamTime()){
			return ImpExamType.IMPEXAM_PROV;
		}
		
		if(isStateExamTime()){
			return ImpExamType.IMPEXAM_STATE;
		}
		return -1;
	}
	
	public boolean isImpExamTime(int ieType){
		
		if(ImpExamType.IMPEXAM_VILL == ieType){
			if(isVillExamTime()){
				return true;
			}
		}
		
		if(ImpExamType.IMPEXAM_PROV == ieType){
			if(isProvExamTime()){
				return true;
			}
		}
		
		if(ImpExamType.IMPEXAM_STATE == ieType){
			if(isStateExamTime()){
				return true;
			}
		}
		
		return false;
	}
	
	public void roleLoginCheck(final long roleId){
		new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				//long cur = System.currentTimeMillis();
				xbean.ImpExamRecord record = xtable.Role2impexam.get(roleId);
				if(record == null){
					record =  xbean.Pod.newImpExamRecord();
					xtable.Role2impexam.insert(roleId, record);
				}
				int impExamType = isInImpExamTime();
				if(impExamType == -1){
					return false;
				}

	 			SActivityOpen so = new SActivityOpen();
	 			
	 			if(impExamType == ImpExamType.IMPEXAM_VILL){
	 				so.activityid = 214;
	 				fire.pb.activity.ActivityConfNew activity = 
	 						RoleLiveness.getConfigActivity(fire.pb.mission.activelist.RoleLiveness.IMPEXAMVILL);
	 				int roleLv = xtable.Properties.selectLevel(roleId);
	 				if(roleLv < activity.level){
	 					return false;
	 				}
	 			}else if(impExamType == ImpExamType.IMPEXAM_PROV){
	 				fire.pb.activity.ActivityConfNew activity = 
	 						RoleLiveness.getConfigActivity(fire.pb.mission.activelist.RoleLiveness.IMPEXAMPROV);
	 				int roleLv = xtable.Properties.selectLevel(roleId);
	 				if(roleLv < activity.level){
	 					return false;
	 				}
	 				so.activityid = 215;
	 			}else if(impExamType == ImpExamType.IMPEXAM_STATE){
	 				so.activityid = 216;
	 			}
	 			
	 			int ieStep = record.getStep();
	 			int ieFlag = record.getFlag();
	 			if((ieStep == ImpExamType.IMPEXAM_STATE) 
	 					|| (ieStep == ImpExamType.IMPEXAM_STATE && ieFlag > 1)){
	 				return false;
	 			}
				
	 			SAttendImpExam send = new SAttendImpExam();
				send.impexamtype = impExamType;
				/*if((cur - record.getBegintime())<TOWNE_HOUR){
					int flag = record.getFlag();
					if(flag > 1){
						return false;
					}
				}*/
				if(impExamType == ImpExamType.IMPEXAM_VILL){
					if(isVillExamTime()){
						int flag = record.getFlag();
						if(flag > 1){
							return false;
						}
					}
				}else if(impExamType == ImpExamType.IMPEXAM_PROV){
					if(isProvExamTime()){
						int flag = record.getFlag();
						if(flag > 1){
							return false;
						}
					}
				}else if(impExamType == ImpExamType.IMPEXAM_STATE){
					if(isStateExamTime()){
						int flag = record.getFlag();
						if(flag > 1){
							return false;
						}
					}
				}else{
					return false;
				}
				mkdb.Procedure.psendWhileCommit(roleId, send);
				mkdb.Procedure.psendWhileCommit(roleId, so);
				return true;
			}
		}.submit();
	}
	
	public boolean answerCheck(long roleId){
		
		fire.pb.map.Role mapRole = fire.pb.map.RoleManager.getInstance().getRoleByID(roleId);
		if (mapRole == null){
			return true;
		}
		int mapId = mapRole.getMapId();	
		MapConfig conf =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.MapConfig.class).get(mapId);
		if(conf == null){
			return true;
		}
		/*if(conf.dynamic ==1){
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,140436, null);
			return false;
		}*/
		return true;
	}
	
	/** 
	 * 发送全村统考题目   存储过程中调用
	 * @param record
	 */
	public void sendVillExamQuestion(long roleId, xbean.ImpExamRecord record, int over){
		long cur = System.currentTimeMillis();
		SSendImpExamVill send = new SSendImpExamVill();
		
		long remianTime =  villExamEndTime-cur;
		int index = record.getLastquesid();
		int qid = -1;
		if(record.getQuesidlist().size() >=index+1){
			qid = record.getQuesidlist().get(index);
		}
		send.isover = (byte)over;
		if(qid == -1){
			send.isover = 1;
		}
		send.impexamdata.questionnum = index+1;
		send.impexamdata.questionid = qid;
		send.impexamdata.righttimes = record.getAccrightnum();
		send.impexamdata.remaintime = 0;
		if(remianTime >0){
			send.impexamdata.remaintime = remianTime;
		}
		send.impexamdata.lastright = (byte)record.getLastisright();
		send.impexamdata.accuexp = record.getCurrexamaccexp();
		send.impexamdata.accumoney = record.getCurrexamaccmoney();
		send.impexamdata.delwrongval = record.getDelwrongval();
		send.impexamdata.chorightval = record.getChorightval();
		send.impexamdata.helpcnt = record.getHelpcnt();
		send.historyright = record.getMaxvillexamright();
		mkdb.Procedure.psendWhileCommit(roleId, send);
	}
	
	/**
	 * 发全省统考题目
	 * @param roleId
	 * @param record
	 * @param lost
	 */
	public void sendProvExamQuestion(long roleId, xbean.ImpExamRecord record, int lost){
		SSendImpExamProv send = new SSendImpExamProv();
		long cur = System.currentTimeMillis();
		int index = record.getLastquesid();
		long remainTime = provExamEndTime-cur;
		int qid = -1;
		if(record.getQuesidlist().size() > index){
			qid = record.getQuesidlist().get(index);
		}
		send.lost = (byte)lost;
		if(qid == -1){
			send.lost = 1;
		}
		
		send.impexamdata.questionnum = index+1;
		send.impexamdata.questionid = qid;
		send.impexamdata.righttimes = record.getAccrightnum();
		send.impexamdata.remaintime = 0;
		if(remainTime >0){
			send.impexamdata.remaintime = remainTime;
		}
		send.impexamdata.lastright = (byte)record.getLastisright();
		send.impexamdata.accuexp = record.getCurrexamaccexp();
		send.impexamdata.accumoney = record.getCurrexamaccmoney();
		send.impexamdata.delwrongval = record.getDelwrongval();
		send.impexamdata.chorightval = record.getChorightval();
		
		send.titlename = getTitleName(record.getRightquesmap());
		send.rightmap.putAll(record.getRightquesmap());
		for(int i=1; i<=4; i++){
			if(send.rightmap.get(i) == null){
				send.rightmap.put(i, 0);
			}
		}
		mkdb.Procedure.psendWhileCommit(roleId, send);
		
	}
	
	/**
	 * 向客户端发送全国统考的题目
	 * @param roleId
	 * @param record
	 */
	public void sendStateExamQuestion(long roleId, xbean.ImpExamRecord record, int lost){
		SSendImpExamState send = new SSendImpExamState();
		long cur = System.currentTimeMillis();
		int index = record.getLastquesid();
		int qid = -1;
		long remianTime = stateExamEndTime-cur;
		if(record.getQuesidlist().size() > index){
			qid = record.getQuesidlist().get(index);
		}
		send.lost = (byte)lost;
		if(qid == -1){
			send.lost = 1;
		}
		
		send.impexamdata.questionnum = index+1;
		send.impexamdata.questionid = qid;
		send.impexamdata.righttimes = record.getAccrightnum();
		send.impexamdata.remaintime = 0;
		if(remianTime >0){
			send.impexamdata.remaintime = remianTime;
		}
		send.impexamdata.lastright = (byte)record.getLastisright();
		send.impexamdata.accuexp = record.getCurrexamaccexp();
		send.impexamdata.accumoney = record.getCurrexamaccmoney();
		send.historymintime = record.getMinstateexamtime();
		send.historymaxright = record.getMaxstateexamright();
		send.impexamusetime = record.getStateexamtime();
		mkdb.Procedure.psendWhileCommit(roleId, send);
		
	}
	
	/**
	 * 打开智力试练开始界面  全省统考或者    全国统考开始
	 * @param roleId
	 * @param record
	 * @param ImpExamtype
	 */
	public void sendImpExamStart(long roleId, xbean.ImpExamRecord record ,int impExamType){
		long cur = System.currentTimeMillis();
		SSendImpExamStart send = new SSendImpExamStart();
		send.impexamtype = (byte)impExamType;
		send.remaintime = stateExamEndTime - cur;
		send.historymaxright = record.getMaxstateexamright();
		send.historymintime = record.getMinstateexamtime();
		mkdb.Procedure.psendWhileCommit(roleId, send);
	}
	
	/**
	 * 这个后来修改
	 * @param map
	 * @return
	 */
	private String getTitleName(Map<Integer, Integer> map){
		return "";
	}
	
	/**
	 * 删除一个错误答案
	 */
	public void sendImpExamAssist(long roleId, int impExamType , int assistType , int errAnswerId){
		SSendImpExamAssist msg = new SSendImpExamAssist();
		msg.impexamtype = (byte)impExamType;
		msg.assisttype = (byte)assistType;
		msg.answerid = errAnswerId;
		mkdb.Procedure.psendWhileCommit(roleId, msg);
	}
	
	public void sendImpExamHelp(long roleId, int helpCnt){
		SImpExamHelp msg = new SImpExamHelp();
		msg.helpcnt = (byte)helpCnt;
		mkdb.Procedure.psendWhileCommit(roleId, msg);
	}
}
