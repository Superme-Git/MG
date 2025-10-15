package fire.pb.master.proc;


import java.util.List;

import fire.pb.PropRole;
import fire.pb.master.MasterManager;
import fire.pb.master.SNotifyMaster;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

/**
 * 师徒相互评价
 * @author nobody
 */
public class PEvaluateProc extends mkdb.Procedure{
	
	private long roleId;//发起评价的人
	private int flag;//1=对师傅评价 2=对徒弟的评价
	private long evaluateId;//被评价的人
	
	public PEvaluateProc(long roleId, long roleId1, int flag){
		this.roleId = roleId;
		this.evaluateId = roleId1;
		this.flag = flag;
	}

	@Override
	protected boolean process() throws Exception {
		
		boolean currIsMaster = true;
		long masterId = roleId;
		long prenticeId = evaluateId;
		if(flag == 1){
			prenticeId = roleId;
			masterId = evaluateId;
			currIsMaster = false;
		}
		
		Team team = TeamManager.getTeamByRoleId(masterId);
		if(team == null){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 146398 , null);
			return false;
		}
		
		//师徒关系表
		xbean.MenstorInfo menstor = xtable.Apprents.get(masterId);
		if(menstor == null){
			return false;
		}
		
		
		List<Long> members = team.getNormalMemberIds();
		if(team.getAllMemberIds().size() >2 || members.size() !=2 || !members.contains(prenticeId)){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 146398 , null);
			return false;
		}
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, members));
		
		//徒弟检查
		PropRole prole = new PropRole(prenticeId, true);
		if(prole.getLevel() <MasterManager.APPRENT_MAX_LEVEL){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId , 146399 , null);
			return false;
		}
		//当前不是徒弟
		xbean.MenstorAndApprent apprent = xtable.Menstor.get(prenticeId);
		if(apprent == null || apprent.getFlag() != 1 || apprent.getMenstorid() != masterId){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 146400, null);
			return false;
		}
		
		xbean.MenstorAndApprent master = xtable.Menstor.get(masterId);
		if(master == null || master.getFlag() != 2){
			return false;
		}
		
		xbean.CurrApprent currApp = null;
		int appIndex = -1;
		for(int i=0; i<menstor.getCurrapprentices().size(); i++){
			xbean.CurrApprent temp = menstor.getCurrapprentices().get(i);
			if(temp.getApprentinfo().getRoleid() == prenticeId){
				currApp = temp;
				appIndex = i;
				break;
			}
		}
		
		if(currApp == null || appIndex == -1){
			return false;
		}
		
		if(currIsMaster){
			if(currApp.getMastercomment() == 1){
				return false;
			}
			currApp.setMastercomment(1);
		}else{
			if(currApp.getApprentcomment() == 1){
				return false;
			}
			currApp.setApprentcomment(1);
		}
		
		//师徒互相评价完成  给奖励   徒弟出徒
		if(currApp.getApprentcomment() >0 && currApp.getMastercomment() > 0){

			apprent.setFlag(0);//出师
			
			//2.把徒弟转成出师弟子
			xbean.HasApprent hasApp = xbean.Pod.newHasApprent();
			hasApp.setName(prole.getName());
			hasApp.setLevel(prole.getLevel());
			hasApp.setSchool(prole.getSchool());
			hasApp.setRoleid(prole.getRoleId());
			hasApp.setShap(prole.getShape());
			menstor.getApprentices().add(0, hasApp);
			if(menstor.getApprentices().size() >40){
				menstor.getApprentices().remove(40);
			}
			
			
			if(master.getFirstapprentid() == 0){
				master.setFirstapprentid(prole.getRoleId());
				master.setFirstbaishitime(currApp.getBaishitime());
			}
			menstor.getCurrapprentices().remove(appIndex);
			
			mkdb.Procedure.psend(prenticeId, new SNotifyMaster());
		}
		return true;
	}
}
