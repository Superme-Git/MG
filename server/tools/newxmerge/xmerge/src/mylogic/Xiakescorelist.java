package mylogic;

import java.util.List;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Xiakescorelist implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
//		xbean.xiakeScoreRankList src_list = xbean.Pod.newxiakeScoreRankListData();
//		xbean.xiakeScoreRankList des_list = xbean.Pod.newxiakeScoreRankListData();
//		
//		src_list.unmarshal(src_value);
//		des_list.unmarshal(dest_value);
//		
//		java.util.List<xbean.MarshalXiakeScoreRecord> recordList = src_list.getRecords();
//		for(xbean.MarshalXiakeScoreRecord current : recordList){
//			tryInsertToDes(current, des_list.getRecords());
//		}
//		
//		int size = des_list.getRecords().size();
//		if(size > Constant.XIAKE_LIST_NUM){
//			for(int i=Constant.XIAKE_LIST_NUM; i<size; i++){
//				des_list.getRecords().remove(Constant.XIAKE_LIST_NUM);
//			}
//		}
//		
//		dest_value.clear();
//		dest_value.marshal(des_list);
		
		return true;
	}

//	private void tryInsertToDes(MarshalXiakeScoreRecord current,	List<MarshalXiakeScoreRecord> records) {
//		for(int i=records.size()-1; i>=0; i--){
//			if(compareTo(current, records.get(i))){
//				if(i==0){
//					records.add(0, current);
//				}
//				continue;
//			}else{
//				records.add(i+1, current);
//				break;
//			}
//		}
//		
//	}
//
//	private boolean compareTo(MarshalXiakeScoreRecord current, MarshalXiakeScoreRecord xiakeRecord) {
//		if(current.getXiakescore() >= xiakeRecord.getXiakescore())
//			return true;
//		return false;
//	}

}
