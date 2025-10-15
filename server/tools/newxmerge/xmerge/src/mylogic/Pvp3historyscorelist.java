package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.battle.pvp3.PvP3RankVisitor;

public class Pvp3historyscorelist implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.PvP3ScoreRankList srcPvP3ScoreRankList = xbean.Pod.newPvP3ScoreRankListData(); 
		xbean.PvP3ScoreRankList dstPvP3ScoreRankList = xbean.Pod.newPvP3ScoreRankListData(); 
		srcPvP3ScoreRankList.unmarshal(src_value);
		dstPvP3ScoreRankList.unmarshal(dest_value);
		
		dstPvP3ScoreRankList.getRecords().addAll(srcPvP3ScoreRankList.getRecords());
		
		int recordMax = PvP3RankVisitor.getRecordMax();
		PvP3RankVisitor.sort(dstPvP3ScoreRankList.getRecords(),null);
		PvP3RankVisitor.resize(dstPvP3ScoreRankList.getRecords(), recordMax);
		
		dest_value.clear();
		dstPvP3ScoreRankList.marshal(dest_value);
		return true;
	}
	

}
