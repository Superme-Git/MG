package fire.pb.ranklist.proc;

import fire.pb.ranklist.RankType;
import mkdb.Procedure;

/**
 * 
 * 工会进度排行 by changhao
 *
 */
public class PFactionProgressUpdateProc extends Procedure {
	private long factionkey;
	private int progress;
	private int zoneid;
	private long deltatime;
	private float bosshp;
	private String copyname;
	public PFactionProgressUpdateProc(long roleId, int progress, int zoneid, long deltatime, float bosshp, String copyname){
		this.factionkey = roleId;
		this.progress = progress;
		this.zoneid = zoneid;
		this.deltatime = deltatime;
		this.bosshp = bosshp;
		this.copyname = copyname;
	}
	
	public boolean process(){
		
		int ranktype = 0;
		if (zoneid == 110)
		{
			ranktype = RankType.FACTION_COPY;
		}
		else if (zoneid == 111)
		{
			ranktype = RankType.FACTION_COPY;			
		}
		else if (zoneid == 112)
		{
			ranktype = RankType.FACTION_COPY;			
		}
		
		if (ranktype == 0)
		{
			return true;
		}		
		
		xbean.ClanInfo clanInfo = xtable.Clans.select(factionkey);
		
		final long now = java.util.Calendar.getInstance().getTimeInMillis();
		long delta = now - deltatime;
		if (delta <= 0)
		{
			delta = 9999999;
		}

		if (bosshp < 0f)
			bosshp = 0f;
		
		xbean.ClanProgressRankRecord record = xbean.Pod.newClanProgressRankRecord();
		record.setClankey(factionkey);
		record.setClanname(clanInfo.getClanname());
		record.setProgress(progress);
		record.setTime(delta);
		record.setTriggertime(now);
		record.setCopyid(zoneid);
		record.setBosshp(bosshp);
		record.setCopyname(copyname);
		
		xbean.ClanProgressRankList list = xtable.Clanprogressranklist.get(ranktype);
		if (null == list) {
			list = xbean.Pod.newClanProgressRankList();
			xtable.Clanprogressranklist.insert(ranktype, list);
		}
		
		RankListManager.getInstance().tryInsertRecord(
				ranktype, list.getRecords(), record);
		
		return true;
	}

}
