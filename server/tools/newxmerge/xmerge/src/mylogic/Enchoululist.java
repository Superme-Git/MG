package mylogic;

import java.util.List;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Enchoululist implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		
//		xbean.EnchouluRankList srcRankList = xbean.Pod.newEnchouluRankListData();
//		xbean.EnchouluRankList dstRankList = xbean.Pod.newEnchouluRankListData();
//		src_value.unmarshal(srcRankList);
//		dest_value.unmarshal(dstRankList);
//		
//		for(xbean.EnchouluListRecord record : srcRankList.getRecords())
//			tryInsertToDes(record, dstRankList.getRecords());
//		
//		int size = dstRankList.getRecords().size();
//		if(size > 30){
//			for(int i=30; i<size; i++){
//				dstRankList.getRecords().remove(30);
//			}
//		}
//		
//		dest_value.clear();
//		dest_value.marshal(dstRankList);
		return true;
	}


//	private void tryInsertToDes(xbean.EnchouluListRecord current,	List<xbean.EnchouluListRecord> records) {
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
	
//	private static boolean compareTo(xbean.EnchouluListRecord current, xbean.EnchouluListRecord other) {
//		if(current.getMarshaldata().getStage() > other.getMarshaldata().getStage())
//			return true;
//		
//		if(current.getMarshaldata().getStage() == other.getMarshaldata().getStage() && current.getTime()< other.getTime())
//			return true;
//		
////		if(current.getMarshaldata().getSaveid() == other.getMarshaldata().getSaveid()){
////			if(current.getMarshaldata().getFailednum() < other.getMarshaldata().getFailednum()){
////				return true;
////			}
////		}
//		return false;
//	}
 


}
