package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


public class Rolemoneylist extends RankListSortProcess implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
//		xbean.RoleMoneyRankList src_list = xbean.Pod.newRoleMoneyRankListData();
//		xbean.RoleMoneyRankList dest_list = xbean.Pod.newRoleMoneyRankListData();
//		
//		src_list.unmarshal(src_value);
//		dest_list.unmarshal(dest_value);
//		
//		dest_list.getRecords().addAll(src_list.getRecords());
//		sortCurrentRankList(dest_list.getRecords());
//		
//		int size = dest_list.getRecords().size();
//		if(dest_list.getRecords().size() > xmerge.Constant.WEALTH_RANK_LIST_NUM){
//			for(int i=xmerge.Constant.WEALTH_RANK_LIST_NUM; i<size; i++){
//				dest_list.getRecords().remove(xmerge.Constant.WEALTH_RANK_LIST_NUM);
//			}
//		}
//		
//		dest_value.clear();
//		dest_value.marshal(dest_list);
		
		return true;
	}

	@Override
	public boolean compareTo(Object srcO, Object destO) {
//		xbean.RoleMoneyListRecord src = (xbean.RoleMoneyListRecord)srcO;
//		xbean.RoleMoneyListRecord dest = (xbean.RoleMoneyListRecord)destO;
//		
//		if(src.getMarshaldata().getWealth() >= dest.getMarshaldata().getWealth())
//			return true;
		
		return false;
	}

}

