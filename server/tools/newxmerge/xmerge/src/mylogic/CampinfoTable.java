package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class CampinfoTable extends RankListSortProcess implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
		
//		xbean.CampInfo src_index = xbean.Pod.newCampInfoData();
//		xbean.CampInfo des_index = xbean.Pod.newCampInfoData();
//		
//		src_index.unmarshal(src_value);
//		des_index.unmarshal(dest_value);
//		
//		des_index.setLeaguenumber(des_index.getLeaguenumber() + src_index.getLeaguenumber());
//		des_index.setTribenumber(des_index.getTribenumber() + src_index.getTribenumber());
//		des_index.getBattleresult().clear();
//		
//		for(xbean.HonourRecord record : src_index.getLeaguerecords()){
//			xbean.HonourRecord temp = record.toBean();
//			des_index.getLeaguerecords().add(temp);
//		}
//		
//		for(xbean.HonourRecord record : src_index.getTriberecords()){
//			xbean.HonourRecord temp = record.toBean();
//			des_index.getTriberecords().add(temp);
//		}
//		
//		sortCurrentRankList(des_index.getLeaguerecords());
//		sortCurrentRankList(des_index.getTriberecords());
//		
//		int leagueSize = des_index.getLeaguerecords().size();
//		if(leagueSize > xmerge.Constant.CAMP_RANK_NUM){
//			for(int i=xmerge.Constant.CAMP_RANK_NUM; i<leagueSize; i++){
//				des_index.getLeaguerecords().remove(xmerge.Constant.CAMP_RANK_NUM);
//			}
//		}
//		
//		int triberSize = des_index.getTriberecords().size();
//		if(triberSize > xmerge.Constant.CAMP_RANK_NUM){
//			for(int i=xmerge.Constant.CAMP_RANK_NUM; i<triberSize; i++){
//				des_index.getTriberecords().remove(xmerge.Constant.CAMP_RANK_NUM);
//			}
//		}
//		
//		dest_value.clear();
//		des_index.marshal(dest_value);
		return true;
	}

	@Override
	public boolean compareTo(Object srcO, Object destO) {
//		xbean.HonourRecord src = (xbean.HonourRecord)srcO;
//		xbean.HonourRecord dest = (xbean.HonourRecord)destO;
//		if(src.getHonour() > dest.getHonour()){
			return true;
//		}else{
//			return false;
//		}
	}
}
