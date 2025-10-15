package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class CampscoreranklistTable extends RankListSortProcess implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		
//		xbean.CampBattleScoreRankList src_index =  xbean.Pod.newCampBattleScoreRankListData();
//		xbean.CampBattleScoreRankList des_index =  xbean.Pod.newCampBattleScoreRankListData();
//		src_index.unmarshal(src_value);
//		des_index.unmarshal(dest_value);
//		
//		des_index.getLeaguescoresortlist().addAll(src_index.getLeaguescoresortlist());
//		des_index.getTribescoresortlist().addAll(src_index.getTribescoresortlist());
//		
//		sortCurrentRankList(des_index.getLeaguescoresortlist());
//		sortCurrentRankList(des_index.getTribescoresortlist());
//		
//		int leagueSize = des_index.getLeaguescoresortlist().size();
//		int trimbleSize = des_index.getTribescoresortlist().size();
//		
//		if(leagueSize > xmerge.Constant.CAMP_SCORE_RANK_NUM){
//			for(int i=xmerge.Constant.CAMP_SCORE_RANK_NUM; i<leagueSize; i++){
//				des_index.getLeaguescoresortlist().remove(xmerge.Constant.CAMP_SCORE_RANK_NUM);
//			}
//		}
//		
//		if(trimbleSize > xmerge.Constant.CAMP_SCORE_RANK_NUM){
//			for(int i=xmerge.Constant.CAMP_SCORE_RANK_NUM; i<trimbleSize; i++){
//				des_index.getTribescoresortlist().remove(xmerge.Constant.CAMP_SCORE_RANK_NUM);
//			}
//		}
//		
//		dest_value.clear();
//		des_index.marshal(dest_value);
		
		return true;
	}

	@Override
	public boolean compareTo(Object srcO, Object destO) {
//		xbean.ScoreRole src = (xbean.ScoreRole)srcO;
//		xbean.ScoreRole dest = (xbean.ScoreRole)destO;
//		if(src.getScore() > dest.getScore()){
//			return true;
//		}else{
			return false;
//		}
	}

}
