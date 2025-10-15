package fire.pb.team;

import fire.pb.StateCommon;
import fire.pb.battle.PlayPKManage;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

/***
 * 创建队伍
 * @author changhao
 *
 */
public class PCreateTeam extends Procedure
{
	
	private final long leaderRoleId;
	private long teamId = -1;//成功执行后，这里为teamid
	
	
	public PCreateTeam(long leaderRoleId)
	{
		this.leaderRoleId = leaderRoleId;
	}


	@Override
	protected boolean process() throws Exception
	{
		//只锁rolelock
		BuffAgent buffagent = new BuffRoleImpl(leaderRoleId,false);
		if (buffagent.existBuff(PlayPKManage.BuffDuelID))
		{
			MessageMgr.sendMsgNotify(leaderRoleId, 141133,  null);
			return false;
		}
		if(!buffagent.canAddBuff(BuffConstant.StateType.STATE_TEAM))
		{
			MessageMgr.sendMsgNotify(leaderRoleId, TeamManager.ERROR_MSG_SELF_CANT_IN_TEAM, null);
			TeamManager.logger.info("PCreateTeam玩家(roleId=" + leaderRoleId+")处于不能组队的状态");
			return false;
		}
		
		//检查该玩家是否在副本中
		if (fire.pb.buff.Module.existState(leaderRoleId,
				BuffConstant.StateType.STATE_INSTANCE_ZONE)) {
			MessageMgr.sendMsgNotify(leaderRoleId, 160202, null);
			TeamManager.logger.info("PCreateTeam玩家(roleId=" + leaderRoleId+")处于副本中,不能组队");
			return false;
		}

		//检查该玩家是否在线
		if (StateCommon.isOnline(leaderRoleId))
		{			
			// 检查该玩家是否在队伍中
			if (xtable.Roleid2teamid.get(leaderRoleId) == null)
			{
				//创建队伍，发协议
				Team team = TeamManager.getInstance().createNewTeam(leaderRoleId);
				if(team == null)
				{	
					TeamManager.logger.error("FAIL:PCreateTeam创建队伍失败（可能由于状态冲突）。");
					return false;
				}
				
				
				teamId = team.teamId;
				TeamManager.logger.info("SUCC:PCreateTeam创建队伍, leaderRoleId: " + team.teamId);
				return true;
				
			} else
			{
				//返回消息说玩家已经在队伍中(illegal)
				TeamManager.logger.error("FAIL:PCreateTeam玩家已经在队伍中, leaderRoleId: " + leaderRoleId);
			}
		}
		else
		{
			//玩家已经不在线，不建立队伍(illegal)
			TeamManager.logger.error("FAIL:PCreateTeam玩家已经不在线, leaderRoleId: " + leaderRoleId);
		}
		return false;
	}


	public long getTeamId()
	{
		return teamId;
	}
	
	
}
