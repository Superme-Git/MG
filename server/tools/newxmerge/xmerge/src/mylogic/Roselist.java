package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Roselist implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
//		xbean.RoseRankList src_list = xbean.Pod.newRoseRankListData();
//		xbean.RoseRankList des_list = xbean.Pod.newRoseRankListData();
//		
//		src_list.unmarshal(src_value);
//		des_list.unmarshal(dest_value);
//		
//		java.util.List<xbean.RoseListRecord> recordList = src_list.getRecords();
//		for(xbean.RoseListRecord current : recordList){
//			tryInsertToDes(current, des_list.getRecords());
//		}
//		
//		int size = des_list.getRecords().size();
//		if(size > 20){
//			for(int i=20; i<size; i++){
//				des_list.getRecords().remove(20);
//			}
//		}
//		
//		dest_value.clear();
//		dest_value.marshal(des_list);
		
		return true;
	}

//	private void tryInsertToDes(RoseListRecord current,	List<RoseListRecord> records) {
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

//	private boolean compareTo(RoseListRecord current, RoseListRecord roseListRecord) {
//		if(current.getMarshaldata().getRosenum() > roseListRecord.getMarshaldata().getRosenum()){
//			return true;
//		}else if(current.getMarshaldata().getRosenum() < roseListRecord.getMarshaldata().getRosenum()){
//			return false;
//		}else{
//			if(current.getTime() <= roseListRecord.getTime())
//				return true;
//		}
//		return false;
//	}

}
