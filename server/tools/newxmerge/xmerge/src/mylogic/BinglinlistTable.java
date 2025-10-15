package mylogic;

import com.locojoy.base.Marshal.OctetsStream;

import xmerge.IMerge;

public class BinglinlistTable extends RankListSortProcess implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value, OctetsStream dest_value) throws Exception {
		
//		xbean.BingLinRecords src_index =  xbean.Pod.newBingLinRecordsData();
//		xbean.BingLinRecords des_index =  xbean.Pod.newBingLinRecordsData();
//		src_index.unmarshal(src_value);
//		des_index.unmarshal(dest_value);
//		
//		des_index.getRecords().addAll(src_index.getRecords());
//		sortCurrentRankList(des_index.getRecords());
//		
//		int size = des_index.getRecords().size();
//		if(size > xmerge.Constant.BINGLINCHENGXIA_RANK_NUM){
//			for(int i=xmerge.Constant.BINGLINCHENGXIA_RANK_NUM; i<size; i++){
//				des_index.getRecords().remove(xmerge.Constant.BINGLINCHENGXIA_RANK_NUM);
//			}
//		}
//		
//		dest_value.clear();
//		des_index.marshal(dest_value);
		
		return true;
	}

	@Override
	public boolean compareTo(Object srcO, Object destO) {
//		xbean.BingLinRecord src = (xbean.BingLinRecord)srcO;
//		xbean.BingLinRecord dest = (xbean.BingLinRecord)destO;
//		if(src.getDamage() > dest.getDamage()){
//			return true;
//		}else{
			return false;
//		}
	}
}
