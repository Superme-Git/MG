/**
 * 
 */
package fire.pb.team;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
/**
 * 每多少秒做一次队伍匹配 by changhao 
 *
 */
public class PTeamMatchTask extends TimerTask {
	
	
	@Override
	public void run() {
		
		mkdb.Procedure teammatchtask = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				xbean.ETeamMatch temp = xtable.Targetid2teammatch.select(0);
				if (temp == null)
				{
					return true;
				}
				
				java.util.HashSet<Long> teamids = new java.util.HashSet<Long>();
				java.util.HashSet<Long> roleids = new java.util.HashSet<Long>();
				
				for (Long teamid : temp.getTeamid2matchdata().keySet())
				{
					if (teamid != null)
					{
						xbean.TeamInfo teamInfo = xtable.Team.select(teamid);
						if (teamInfo != null)
						{
							List<Long> ids = new ArrayList<Long>();
							ids.add(teamInfo.getTeamleaderid());
							for (xbean.TeamMember member : teamInfo.getMembers())
							{
								ids.add(member.getRoleid());
							}
							
							roleids.addAll(ids);
							teamids.add(teamid);
						}
					}	
				}
				
				roleids.addAll(temp.getRoleid2matchdata().keySet());
				
				lock(mkdb.Lockeys.get(xtable.Locks.TEAMLOCK, teamids));
				lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids));
				
				
				xbean.ETeamMatch ematch = xtable.Targetid2teammatch.get(0);
				if (ematch == null)
				{
					return true;
				}
				
				long cur = System.currentTimeMillis();
												
				for (xbean.TeamMatch e : ematch.getTeammatchdatalist())
				{
					if (cur < e.getTimestamp())
					{
						continue;
					}
					
					for (xbean.TeamMatch e1 : ematch.getRolematchdatalist())
					{
						if (cur < e1.getTimestamp())
						{
							continue;
						}
						
						Long teamid = xtable.Roleid2teamid.select(e.getRoleid());
						if (teamid != null)
						{
							xbean.TeamInfo teaminfo = xtable.Team.select(teamid);
							if (teaminfo != null)
							{
								if (e.getTargetid() != e1.getTargetid())
								{
									continue;
								}
								
								if (!checkLevel(e1.getRoleid(), e))
								{
									continue;
								}
								
								xbean.InviteInfo invitedInfo = xtable.Teaminvite.select(e1.getRoleid());
								if (invitedInfo != null)
								{
									if (invitedInfo.getBeinginvited() == true)
									{
										if (cur - invitedInfo.getInviting().getInvitetime() <= TeamManager.MAX_INVITE_TIMEOUT)
										{
											continue;
										}
									}
									else
									{
										boolean find = false;
										for (xbean.TeamInvite invited : invitedInfo.getInvited())
										{
											if ((cur - invited.getInvitetime()) <= TeamManager.MAX_INVITE_TIMEOUT)
											{
												if (invited.getRoleid() == e.getRoleid())
												{
													find = true;
													break;
												}
											}
										}
										
										if (find == true)
										{
											continue;
										}
									}
								}
								
								long time = cur + TeamManager.MAX_MATCH_CLIENT_TIME_OUT;
								long teamtime = cur + TeamManager.MAX_MATCH_CLIENT_TEAM_TIME_OUT;
								
								e.setTimestamp(teamtime);
								e1.setTimestamp(time);	
								xbean.TeamMatch teammatch = ematch.getTeamid2matchdata().get(teamid);
								teammatch.setTimestamp(teamtime);
								xbean.TeamMatch teammatch1 = ematch.getRoleid2matchdata().get(e1.getRoleid());
								teammatch1.setTimestamp(time);								
								
								SForceInviteJointTeam teammsg = new SForceInviteJointTeam();
								teammsg.roleid = e1.getRoleid();
								
								mkdb.Procedure.psendWhileCommit(e.getRoleid(), teammsg);
							}
						}
					}
				}				
				
				return true;
			}
			
		};
		teammatchtask.submit();			
	}
	
	public boolean checkTeam(xbean.TeamInfo teaminfo)
	{
		
		
		return true;
	}
	
	public boolean checkLevel(long roleid, xbean.TeamMatch teammatch)
	{
		xbean.Properties prop = xtable.Properties.select(roleid);
		int level = prop.getLevel();
		
		if (level >= teammatch.getLevelmin() && level <= teammatch.getLevelmax())
		{
			return true;
		}
		
		return false;
	}

}
