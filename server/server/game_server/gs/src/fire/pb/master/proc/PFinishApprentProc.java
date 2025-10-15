package fire.pb.master.proc;

import fire.pb.PropRole;
import fire.pb.master.MasterManager;
import fire.pb.master.SEvaluate;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

/**
 * 拜师出徒
 * @author nobody
 */
public class PFinishApprentProc extends mkdb.Procedure {
	
	private final long masterId;
	
	public PFinishApprentProc(long rid){
	    this.masterId = rid;
	}

	@Override
	protected boolean process() throws Exception {

		Team team = TeamManager.getTeamByRoleId(masterId);
		if(team == null){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146431 , null);
			return false;
		}
		if(team.getAllMemberIds().size() >2 || team.getNormalMemberIds().size() !=2){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146398 , null);
			return false;
		}
		
		xbean.MenstorInfo menstor = xtable.Apprents.get(masterId);
		if(menstor == null){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146431 , null);
			return false;
		}
		
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, team.getNormalMemberIds()));
		
		//师傅检查
		xbean.MenstorAndApprent master = xtable.Menstor.get(masterId);
		if(master == null || master.getFlag() != 2){
			return false;
		}
		
		//徒弟检查
		long prenticeRoleId = team.getNormalMemberIds().get(1);
		PropRole prole = new PropRole(prenticeRoleId, true);
		
		if(!isFriendEatch(prenticeRoleId)){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146419 , null);
			fire.pb.talk.MessageMgr.sendMsgNotify(prenticeRoleId, 146419 , null);
			return false;
		}
		
		//不具备出师等级
		if(prole.getLevel() <MasterManager.APPRENT_MAX_LEVEL){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146399 , null);
			return false;
		}
		//当前不是徒弟
		xbean.MenstorAndApprent apprent = xtable.Menstor.get(prenticeRoleId);
		if(apprent == null || apprent.getFlag() != 1 || apprent.getMenstorid() != masterId){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146403, null);
			return false;
		}
		
		//对师傅的评价
		SEvaluate send1 = new SEvaluate();
		send1.flag = 1;
		send1.roleid = masterId;
		mkdb.Procedure.psendWhileCommit(prenticeRoleId, send1);
		
		
		//对徒弟的评价
		SEvaluate send = new SEvaluate();
		send.flag = 2;
		send.roleid = prenticeRoleId;
		mkdb.Procedure.psendWhileCommit(masterId, send);
		return true;
	}
	
	
	private boolean isFriendEatch(long prenticeRoleId){
		
		xbean.FriendGroups groups = xtable.Friends.select(masterId);		
		xbean.FriendGroups groups1 = xtable.Friends.select(prenticeRoleId);		
		if(groups == null || groups1 == null){
			return false;
		}
		if(groups.getFriendmap().get(prenticeRoleId) == null || 
				groups1.getFriendmap().get(masterId) == null 
				){
			return false;
		}
		
		return true;
	}
	
}
