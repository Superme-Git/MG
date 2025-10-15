package fire.pb.clan.srv;

import fire.pb.ranklist.RankType;
import fire.pb.ranklist.proc.RankListManager;
import mkdb.Procedure;


public class ClanRank extends Procedure {

	private xbean.ClanInfo clanInfo;
	
	public ClanRank(xbean.ClanInfo clanInfo){
		this.clanInfo = clanInfo;
	}
	
	public boolean process(){
		if(this.clanInfo == null)
			return false;
		
		xbean.ClanInfo claninfo = xtable.Clans.select(clanInfo.getKey());
		if (claninfo == null)
		{
			return true;
		}		
		
		final long now = java.util.Calendar.getInstance().getTimeInMillis();
		
		xbean.ClanRankRecord record = xbean.Pod.newClanRankRecord();
		
		record.setClankey(clanInfo.getKey());
		record.setClanname(clanInfo.getClanname());
		record.setLevel(clanInfo.getClanlevel());
		record.setMembernum(clanInfo.getMembers().size());
		record.setHotel(clanInfo.getHouse().get(4));
		record.setTriggertime(now);
		
		xbean.ClanRankList list = xtable.Clanranklist.get(1);
		if(null == list){
			list = xbean.Pod.newClanRankList();
			xtable.Clanranklist.insert(1, list);
		}
		
		RankListManager.getInstance().tryInsertRecord(RankType.FACTION_RANK_LEVEL, list.getRecords(), record);
		
		return true;
	}

}
