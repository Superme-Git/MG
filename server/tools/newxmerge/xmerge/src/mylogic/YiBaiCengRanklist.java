package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;



public class YiBaiCengRanklist extends RankListSortProcess implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
//		xbean.YiBaiCengRankList src_list = xbean.Pod.newYiBaiCengRankListData();
//		xbean.YiBaiCengRankList dest_list = xbean.Pod.newYiBaiCengRankListData();
//		
//		src_list.unmarshal(src_value);
//		dest_list.unmarshal(dest_value);
//		
//		dest_list.getRecords().addAll(src_list.getRecords());
//		sortCurrentRankList(dest_list.getRecords());
//		
//		int size = dest_list.getRecords().size();
//		if(dest_list.getRecords().size() > xmerge.Constant.YIBAICENG_RANK_LIST_NUM){
//			for(int i=xmerge.Constant.YIBAICENG_RANK_LIST_NUM; i<size; i++){
//				dest_list.getRecords().remove(xmerge.Constant.YIBAICENG_RANK_LIST_NUM);
//			}
//		}
//		
//		dest_value.clear();
//		dest_value.marshal(dest_list);
		
		return true;
	}

	@Override
	public boolean compareTo(Object srcO, Object destO) {
//		xbean.YiBaiCengRankRecord src = (xbean.YiBaiCengRankRecord)srcO;
//		xbean.YiBaiCengRankRecord dest = (xbean.YiBaiCengRankRecord)destO;
//		
//		if(src.getLayer() > dest.getLayer()){
//			return true;
//		}else{
			return false;
//		}
	}

}

