package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class RolescoreranklistTable extends RankListSortProcess implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		
		
//		xbean.RoleScoreList src_index = xbean.Pod.newRoleScoreListData();
//		xbean.RoleScoreList des_index = xbean.Pod.newRoleScoreListData();
//		
//		src_index.unmarshal(src_value);
//		des_index.unmarshal(dest_value);
//		
//		for(xbean.RoleScoreBean scoreBean : src_index.getRecords()){
//			xbean.RoleScoreBean  temp = scoreBean.toBean();
//			des_index.getRecords().add(temp);
//		}
//		sortCurrentRankList(des_index.getRecords());
//		int size = des_index.getRecords().size();
//		if(size > xmerge.Constant.PVP_SCORE_RANK_NUM){
//			for(int i=xmerge.Constant.PVP_SCORE_RANK_NUM; i<size; i++){
//				des_index.getRecords().remove(xmerge.Constant.PVP_SCORE_RANK_NUM);
//			}
//		}
//		
//		dest_value.clear();
//		des_index.marshal(dest_value);
		
		return true;
	}

	@Override
	public boolean compareTo(Object srcO, Object destO) {
//		xbean.RoleScoreBean src = (xbean.RoleScoreBean)srcO;
//		xbean.RoleScoreBean dest = (xbean.RoleScoreBean)destO;
//		if(src.getHistoryscore() > dest.getHistoryscore()){
//			return true;
//		}else{
			return false;
//		}
	}
}
