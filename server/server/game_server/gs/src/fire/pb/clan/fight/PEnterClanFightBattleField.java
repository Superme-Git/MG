package fire.pb.clan.fight;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import sun.util.calendar.BaseCalendar.Date;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamMemberState;

/**
 * 进入公会战场 by changhao
 */
public class PEnterClanFightBattleField extends mkdb.Procedure
{
	final long roleid;
	
	public PEnterClanFightBattleField(long roleid)
	{
		this.roleid = roleid;
	}

	@Override
	/***
	 * 锁定 ->team-> clanfightid -> roleid by changhao
	 */
	protected boolean process() throws Exception
	{		
		final long cur = System.currentTimeMillis();
		
		Long teamId = xtable.Roleid2teamid.select(roleid);
		
		Long cid = xtable.Roleidclan.select(roleid);
		if (cid == null) //没有公会 by changhao
		{
			MessageMgr.sendMsgNotify(roleid, 410049, null);					
			return false;
		}
		
		xbean.ClanInfo c = xtable.Clans.get(cid); //先锁公会 by changhao
		if (c != null)
		{				
			xbean.TeamInfo teaminfo = null;
			if (teamId != null)
			{
				teaminfo = xtable.Team.get(teamId); //锁定队伍 by changhao
			}
				
			long clankey = c.getKey();
			xbean.EClanfightids ids = xtable.Clanid2clanfightids.select(clankey);
			if (ids != null)
			{
				lock(mkdb.Lockeys.get(xtable.Locks.CLANFIGHTIDLOCK, ids.getIds())); //锁定公会战id by changhao
				
				java.util.Set<Long> roleids = new java.util.HashSet<Long>();
				long leaderid = roleid;
				if (teaminfo != null)
				{	
					boolean haveabsorleave = false;
					for (xbean.TeamMember member : teaminfo.getMembers())
					{
						if (member.getState() != TeamMemberState.eTeamNormal)
						{
							haveabsorleave = true;
						}
						
						roleids.add(member.getRoleid());
					}
					
					roleids.add(teaminfo.getTeamleaderid());
			
					leaderid = teaminfo.getTeamleaderid();
										
					if (haveabsorleave == true) //队伍中有暂离的队员 by changhao
					{
						MessageMgr.sendMsgNotify(roleid, 410018, null);	
						return false;
					}					
				}
				else
				{
					roleids.add(roleid);
				}
				
				lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids)); //锁定角色 by changhao
				
				for (Long r : roleids)
				{
					xbean.ClanMemberInfo mi = c.getMembers().get(r);
					if (mi == null) //有不在同一公会 by changhao
					{
						MessageMgr.sendMsgNotify(leaderid, 410012, null);
						return false;
					}
					
					long day1 = 24 * 60 * 60 * 1000;
					if (cur < mi.getJointime() + day1 * fire.pb.activity.clanfight.ActivityClanFightManager.CLAN_FIGHT_JOIN_CLAN_TIME) //有人加入公会时间不足2天 by changhao
					{
						if (roleids.size() > 1)
						{
							MessageMgr.sendMsgNotify(leaderid, 410013, null);
						}
						else
						{
							MessageMgr.sendMsgNotify(leaderid, 410014, null);							
						}
						
						return false;
					}
					
					xbean.Properties prop = xtable.Properties.get(r);
					if (prop == null)
					{
						return false;
					}
					
					if (prop.getLevel() < fire.pb.activity.clanfight.ActivityClanFightManager.CLAN_FIGHT_ROLE_LEVEL) //等级不够 by changhao
					{
						if (roleids.size() > 1)
						{
							MessageMgr.sendMsgNotify(leaderid, 410047, null);								
						}
						else
						{
							MessageMgr.sendMsgNotify(leaderid, 410046, null);								
						}
						
						return false;
					}
				}
				
				int ret = 100;
				
				for (Long id : ids.getIds())
				{
					if (id != null)
					{
						fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(id, false);
						if (bf != null)
						{							
							if (cur > bf.getClanfightBean().getActivitynoentertime() && cur < bf.getClanfightBean().getActivityendtime())
							{
								MessageMgr.sendMsgNotify(leaderid, 410011, null);	
								return false;
							}

							java.util.Date d = new java.util.Date();
							d.setTime(bf.getClanfightBean().getActivitystarttime());
							String s = d.toLocaleString();
							if (cur < bf.getClanfightBean().getActivitystarttime() || cur > bf.getClanfightBean().getActivitynoentertime())
							{
								continue;
							}
							
							int which = bf.getSideByClanid(clankey);
							
							ret = bf.enterBattleField(roleid, which);
							if (ret == 0)
							{								
								break;
							}
							
							if (ret == 2)
							{
								return false;
							}
						}
					}
				}	
				
				if (ids.getIds().size() == 0)
				{
					MessageMgr.sendMsgNotify(leaderid, 410045, null);					
				}
				else if (ret != 0 && ret != 2 && ret != 4)
				{				
					MessageMgr.sendMsgNotify(leaderid, 410044, null);						
				}
			}
			else
			{
				MessageMgr.sendMsgNotify(roleid, 410045, null);					
			}
		}
		else
		{
			MessageMgr.sendMsgNotify(roleid, 410049, null);
		}
		
		return true;
	}
}
