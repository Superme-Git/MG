package fire.pb.ranklist.getrankinfo;

import fire.pb.PropRole;
import fire.pb.ranklist.RankType;
import mkdb.Procedure;

/***
 * 公会等级排行
 * @author changhao
 *
 */
public class PFactionRankInfo extends Procedure{
	private int rank;
	private int rankType;
	private long roleId;
	private long id;
	
	public PFactionRankInfo(int rank, int rankType, long roleId, long id){
		this.rank = rank;
		this.rankType = rankType;
		this.roleId = roleId;
		this.id = id;
	}

	@Override
	protected boolean process() throws Exception
	{
		long rankRoleId = -1;
		long clankey = -1;
		if (rank == -1)
		{
			rankRoleId = roleId;
		}
		else
		{
			if (rankType == RankType.FACTION_ZONGHE)
			{
				xbean.ClanZongHeRankList list = xtable.Clanzonghelist.select(1);
				if (list == null)
				{
					return true;
				}
				
				int count = 0;
				for (xbean.ClanZongHeRankRecord e : list.getRecords())
				{
					if (e.getClankey() == id)
					{
						rank = count;
						break;
					}
					
					count ++;
				}
				
				if (rank >= list.getRecords().size())
				{
					return false;
				}
				
				xbean.ClanZongHeRankRecord record = list.getRecords().get(rank);
				if (record == null)
				{
					return true;
				}
			
				clankey = record.getClankey();
			}
			else if (rankType == RankType.FACTION_COPY)
			{
				xbean.ClanProgressRankList list = xtable.Clanprogressranklist.select(RankType.FACTION_COPY);
				if (list == null)
				{
					return true;
				}
				
				int count = 0;
				for (xbean.ClanProgressRankRecord e : list.getRecords())
				{
					if (e.getClankey() == id)
					{
						rank = count;
						break;
					}
					
					count ++;
				}
				
				if (rank >= list.getRecords().size())
				{
					return false;
				}
				
				xbean.ClanProgressRankRecord record = list.getRecords().get(rank);
				if (record == null)
				{
					return true;
				}
			
				clankey = record.getClankey();					
			}
			else if (rankType == RankType.FACTION_RANK_LEVEL)
			{
				xbean.ClanRankList list = xtable.Clanranklist.select(1);
				if (list == null)
				{
					return true;
				}
				
				int count = 0;
				for (xbean.ClanRankRecord e : list.getRecords())
				{
					if (e.getClankey() == id)
					{
						rank = count;
						break;
					}
					
					count ++;
				}
				
				if (rank >= list.getRecords().size())
				{
					return false;
				}
				
				xbean.ClanRankRecord record = list.getRecords().get(rank);
				if (record == null)
				{
					return true;
				}
			
				clankey = record.getClankey();				
			}
			else if (rankType == RankType.FACTION_MC)
			{
				xbean.ClanProgressRankList list = xtable.Clanprogressranklist.select(1);
				if (list == null)
				{
					return true;
				}
				
				int count = 0;
				for (xbean.ClanProgressRankRecord e : list.getRecords())
				{
					if (e.getClankey() == id)
					{
						rank = count;
						break;
					}
					
					count ++;
				}
				
				if (rank >= list.getRecords().size())
				{
					return false;
				}
				
				xbean.ClanProgressRankRecord record = list.getRecords().get(rank);
				if (record == null)
				{
					return true;
				}
			
				clankey = record.getClankey();				
			}
		}
		
		if (rank == -1)
		{
			PropRole prop = new PropRole(rankRoleId,true);
			clankey = prop.getProperties().getClankey();
		}
		
		SFactionRankInfo send = new SFactionRankInfo();
//		Long factionId = prop.getFactionKey();
		send.factionkey = clankey;
		send.title = "";
		send.lastname ="";
		send.factionmasterid = 0;
		if (clankey > 0)
		{
			xbean.ClanInfo info = xtable.Clans.select(clankey);
			if(info != null)
			{
				send.title = info.getClanaim();
				send.lastname = info.getClanname();
				send.factionmasterid =info.getClanmaster();
				
				if (info.getOldclanname() != null)
					send.lastname = info.getOldclanname();
			}
		}
		else
		{
			return true;
		}
		
		mkdb.Procedure.psendWhileCommit(roleId, send);
		
		return true;
	}
	
}
