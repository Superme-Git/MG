package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.battle.pvp5.PvP5RankVisitor;

public class Pvp5scorelist implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		
		xbean.PvP5ScoreRankList srcPvP5ScoreRankList = xbean.Pod.newPvP5ScoreRankListData(); 
		xbean.PvP5ScoreRankList dstPvP5ScoreRankList = xbean.Pod.newPvP5ScoreRankListData(); 
		srcPvP5ScoreRankList.unmarshal(src_value);
		dstPvP5ScoreRankList.unmarshal(dest_value);
		
		dstPvP5ScoreRankList.getRecords().addAll(srcPvP5ScoreRankList.getRecords());
		
		int recordMax = xmerge.Constant.PVP5_HistoryRankRecordMax;
		PvP5RankVisitor.sort(dstPvP5ScoreRankList.getRecords(),null);
		PvP5RankVisitor.resize(dstPvP5ScoreRankList.getRecords(), recordMax);
		
		dest_value.clear();
		dstPvP5ScoreRankList.marshal(dest_value);
		
		return true;
	}

}
