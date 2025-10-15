package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


public class GiveflowerlistTable extends RankListSortProcess implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
		xbean.GiveFlowerRankList src_list = xbean.Pod.newGiveFlowerRankListData();
		xbean.GiveFlowerRankList dest_list = xbean.Pod.newGiveFlowerRankListData();
		
		src_list.unmarshal(src_value);
		dest_list.unmarshal(dest_value);
		
		dest_list.getRecords().addAll(src_list.getRecords());
		sortCurrentRankList(dest_list.getRecords());
		
		int size = dest_list.getRecords().size();
		if(dest_list.getRecords().size() > xmerge.Constant.GIVEROSE_LIST_NUM){
			for(int i=xmerge.Constant.GIVEROSE_LIST_NUM; i<size; i++){
				dest_list.getRecords().remove(xmerge.Constant.GIVEROSE_LIST_NUM);
			}
		}
		
		dest_value.clear();
		dest_value.marshal(dest_list);
		
		return true;
	}

	@Override
	public boolean compareTo(Object srcO, Object destO) {
		xbean.GiveFlowerRecord src = (xbean.GiveFlowerRecord)srcO;
		xbean.GiveFlowerRecord dest = (xbean.GiveFlowerRecord)destO;
		
		if(src.getMarshaldata().getNum() > dest.getMarshaldata().getNum()){
			return true;
		}else if(src.getMarshaldata().getNum() < dest.getMarshaldata().getNum()){
			return false;
		}else{
			if(src.getTriggertime() >= dest.getTriggertime()){
				return true;
			}
				return false;
		}
	}

}

