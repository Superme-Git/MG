package fire.pb.instancezone.bingfeng;
import mkdb.Procedure;

public class PRemoveRankList extends Procedure {
	private final int rankid;
	private final long roleid;
	private final int schoolid;

	public PRemoveRankList(int rankid, long roleid, int schoolid) {
		this.rankid = rankid;
		this.roleid = roleid;
		this.schoolid = schoolid;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.BingFengRank xrank = xtable.Bingfenglists.get(rankid);
		if (xrank == null)
			return false;
		
		xbean.BingFengRankList xrankList = xrank.getRanks().get(schoolid);
		if (xrankList == null)
			return false;
		
		xbean.BingFengListRecord removeRecord = null;
		for (xbean.BingFengListRecord record : xrankList.getRecords()) {
			if (record.getMarshaldata().getRoleid() == roleid) {
				removeRecord = record;
				break;
			}
		}
		
		if (removeRecord != null)
			xrankList.getRecords().remove(removeRecord);
		return true;
	}
	
	
}
