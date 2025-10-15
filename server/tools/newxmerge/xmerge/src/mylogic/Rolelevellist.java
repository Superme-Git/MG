package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


public class Rolelevellist extends RankListSortProcess implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
		xbean.RoleLevelRankList src_list = xbean.Pod.newRoleLevelRankListData();
		xbean.RoleLevelRankList dest_list = xbean.Pod.newRoleLevelRankListData();
		
		src_list.unmarshal(src_value);
		dest_list.unmarshal(dest_value);
		
		dest_list.getRecords().addAll(src_list.getRecords());
		sortCurrentRankList(dest_list.getRecords());
		
		int size = dest_list.getRecords().size();
		if(dest_list.getRecords().size() > xmerge.Constant.LEVEL_RANK_LIST_NUM){
			for(int i=xmerge.Constant.LEVEL_RANK_LIST_NUM; i<size; i++){
				dest_list.getRecords().remove(xmerge.Constant.LEVEL_RANK_LIST_NUM);
			}
		}
		
		dest_value.clear();
		dest_value.marshal(dest_list);
		
		return true;
	}

	@Override
	public boolean compareTo(Object srcO, Object destO) {
		xbean.RoleLevelListRecord src = (xbean.RoleLevelListRecord)srcO;
		xbean.RoleLevelListRecord dest = (xbean.RoleLevelListRecord)destO;
		
		if(src.getMarshaldata().getLevel() > dest.getMarshaldata().getLevel()){
			return true;
		}else if(src.getMarshaldata().getLevel() < dest.getMarshaldata().getLevel()){
			return false;
		}else{
			if(src.getTime() >= dest.getTime()){
				return true;
			}
				return false;
			
		}
		
	}

}

