package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


public class Achievementlist extends RankListSortProcess implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
//		xbean.AchievementRankList src_list = xbean.Pod.newAchievementRankListData();
//		xbean.AchievementRankList dest_list = xbean.Pod.newAchievementRankListData();
//		
//		src_list.unmarshal(src_value);
//		dest_list.unmarshal(dest_value);
//		
//		dest_list.getRecords().addAll(src_list.getRecords());
//		sortCurrentRankList(dest_list.getRecords());
//		
//		int size = dest_list.getRecords().size();
//		if(dest_list.getRecords().size() > xmerge.Constant.CHENG_JIU_RANK_LIST_NUM){
//			for(int i=xmerge.Constant.CHENG_JIU_RANK_LIST_NUM; i<size; i++){
//				dest_list.getRecords().remove(xmerge.Constant.CHENG_JIU_RANK_LIST_NUM);
//			}
//		}
//		
//		dest_value.clear();
//		dest_value.marshal(dest_list);
		
		return true;
		
	}

	@Override
	public boolean compareTo(Object srcO, Object destO) {
//		xbean.AchievementListRecord src = (xbean.AchievementListRecord)srcO;
//		xbean.AchievementListRecord dest = (xbean.AchievementListRecord)destO;
//		
//		if(src.getMarshaldata().getTotalpoint() > dest.getMarshaldata().getTotalpoint()){
//			return true;
//		}else if(src.getMarshaldata().getTotalpoint() < dest.getMarshaldata().getTotalpoint()){
//			return false;
//		}else{
//			if(src.getMarshaldata().getFinishnum() > dest.getMarshaldata().getFinishnum()){
//				return true;
//			}else if(src.getMarshaldata().getFinishnum() < dest.getMarshaldata().getFinishnum()){
//				return false;
//			}else{
//				if(src.getTime() >= dest.getTime())
//					return true;
				
				return false;
//			}
//		}
		
	}

}

