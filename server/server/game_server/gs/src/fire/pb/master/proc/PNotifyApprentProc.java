package fire.pb.master.proc;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.master.MasterManager;
import fire.pb.master.SNotifyAppMaster;
import fire.pb.master.SNotifyMaster;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.DateValidate;

/**
 * 通知徒弟拜师
 * @author nobody
 */
public class PNotifyApprentProc extends mkdb.Procedure{
	
	private long masterId;
	
	
	public PNotifyApprentProc(long masterId){
		this.masterId = masterId;
	}

	@Override
	protected boolean process() throws Exception {

		PropRole prole = new PropRole(masterId, true);
		if(prole.getLevel() <MasterManager.MASTER_MIN_LEVEL){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146388, null);
			return false;
		}
		Team team = TeamManager.selectTeamByRoleId(masterId);
		if(team == null){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146389 , null);
			return false;
		}
		if(team.getAllMemberIds().size() >2 || team.getNormalMemberIds().size() !=2){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146389 , null);
			return false;
		}
		//2. 检查徒弟是否满足条件
		long prenticeRoleId = team.getNormalMemberIds().get(1);
		xbean.MenstorAndApprent master = xtable.Menstor.select(masterId);
		if(master != null){
			if(master.getFlag() == 1){
				fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146390, null);
				return false;
			}else if(master.getFlag() == 0){
				master.setFlag(2);
				mkdb.Procedure.psendWhileCommit(masterId, new SNotifyMaster(2));
			}
		}
		
		int number = MasterManager.getMaxApprentice(prole.getLevel());
		xbean.MenstorInfo menstor = xtable.Apprents.select(masterId);
		if(menstor != null && menstor.getCurrapprentices().size() >= number){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146391, null);
			return false;
		}
		
		if(!MasterManager.getInstance().isFriendEatch(masterId, prenticeRoleId)){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146419 , null);
			fire.pb.talk.MessageMgr.sendMsgNotify(prenticeRoleId, 146419 , null);
			return false;
		}

		PropRole papprent = new PropRole(prenticeRoleId, true);
		if(papprent.getLevel() >= MasterManager.APPRENT_MAX_LEVEL || papprent.getLevel()< MasterManager.APPRENT_MIN_LEVEL){
			fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146402, null);
			return false;
		}

		xbean.MenstorAndApprent apprent = xtable.Menstor.select(prenticeRoleId);
		//强行罢师时间超过24小时才能拜师
		long curr = System.currentTimeMillis();
		if(apprent != null){
			//有师徒关系的人 不能为徒弟
			if(apprent.getFlag() != 0){
				fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146392, null);
				return false;
			}
			
			if(apprent.getBashiflag() == 2 && curr - apprent.getBashitime() < DateValidate.dayMills){
				fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146393, null);
				return false;
			}
		}
		
		SNotifyAppMaster send = new SNotifyAppMaster();
		send.mastername = prole.getName();
		mkdb.Procedure.psendWhileCommit(prenticeRoleId, send);
		
		List<String> parms = new ArrayList<String>();
		parms.add(papprent.getName());
		fire.pb.talk.MessageMgr.sendMsgNotify(masterId, 146500, parms);
		
		return true;
	}
}
