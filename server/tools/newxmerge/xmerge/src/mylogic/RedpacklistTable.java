package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;


public class RedpacklistTable extends RankListSortProcess implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
		xbean.RedPackRankList src_list = xbean.Pod.newRedPackRankListData();
		xbean.RedPackRankList dest_list = xbean.Pod.newRedPackRankListData();
		
		src_list.unmarshal(src_value);
		dest_list.unmarshal(dest_value);
		
		dest_list.getRecords().addAll(src_list.getRecords());
		sortCurrentRankList(dest_list.getRecords());
		
		int size = dest_list.getRecords().size();
		if(dest_list.getRecords().size() > xmerge.Constant.REDPACK_LIST_NUM){
			for(int i=xmerge.Constant.REDPACK_LIST_NUM; i<size; i++){
				dest_list.getRecords().remove(xmerge.Constant.REDPACK_LIST_NUM);
			}
		}
		
		dest_value.clear();
		dest_value.marshal(dest_list);
		
		return true;
	}

	@Override
	public boolean compareTo(Object srcO, Object destO) {
		xbean.RedPackRecord src = (xbean.RedPackRecord)srcO;
		xbean.RedPackRecord dest = (xbean.RedPackRecord)destO;
		
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

