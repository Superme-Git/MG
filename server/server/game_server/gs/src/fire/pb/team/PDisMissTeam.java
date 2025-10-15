package fire.pb.team;

import java.util.Set;

import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.OperateType;
import fire.pb.talk.MessageMgr;

/***
 * 解散队伍
 * @author changhao
 *
 */
public class PDisMissTeam extends mkdb.Procedure
{
	public static int REASON_SYSTEM = 1;//系统解散
	
	public static int REASON_LEADER = 2;//队长主动解散
	
	public static int REASON_LEADER_QUIT = 3;//队长退出解散
	
	private final long teamId;
	private final int reason;

	public PDisMissTeam(long teamId, int reason)
	{
		this.teamId = teamId;
		this.reason = reason;
	}
	
	@Override
	protected boolean process()
	{
		Team team = TeamManager.getTeamByTeamID(teamId);
		if(team == null)
			return false;
		this.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, team.getAllMemberIds()));
		long leaderId = team.getTeamLeaderId();
		if(reason == REASON_LEADER)
		{//解散队伍的buff冲突
			BuffAgent brole = new BuffRoleImpl(leaderId, false);
			if(!brole.canAddBuff(OperateType.TEAM_DISMISS))
				return false; 
		}
		
		Set<Long> roleIds = team.getAllMemberIdSet();
		team.dismissTeam();
		if(reason == REASON_LEADER)
		{
			String name = xtable.Properties.selectRolename(leaderId);
			for(long id : roleIds)
				if(id != leaderId)
					MessageMgr.psendMsgNotifyWhileCommit(id, 142585, MessageMgr.getStringList(name));
				else
					MessageMgr.psendMsgNotifyWhileCommit(id, 141049, MessageMgr.getStringList(name));
					
		}
		else if(reason == REASON_SYSTEM)
		{
			//TODO 提示
		}
		else if(reason == REASON_LEADER_QUIT)
		{
			//不用提示
		}
		
		return true;
	}
}
