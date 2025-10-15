package mylogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import xbean.BingFengListRecord;
import xbean.RoleZongheRankRecord;
import xmerge.Constant;
import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.ranklist.RankType;
import fire.pb.ranklist.proc.RankListManager;

public class Bingfenglists implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		int type=src_key.unmarshal_int();
		xbean.BingFengRank srcBingFengRank = xbean.Pod.newBingFengRankData(); 
		xbean.BingFengRank dstBingFengRank = xbean.Pod.newBingFengRankData(); 
		srcBingFengRank.unmarshal(src_value);
		dstBingFengRank.unmarshal(dest_value);
		
		Map<Integer, xbean.BingFengRankList> srcrankschool=srcBingFengRank.getRanks();
		Map<Integer, xbean.BingFengRankList> dstrankschool=dstBingFengRank.getRanks();
		for(Entry<Integer, xbean.BingFengRankList> entry:srcrankschool.entrySet()){
			int key=entry.getKey();
			xbean.BingFengRankList src=entry.getValue();
			xbean.BingFengRankList dst=dstrankschool.get(key);
			if(dst==null){
				dstrankschool.put(key, src);
			}else{
				java.util.List<xbean.BingFengListRecord> recordList = src.getRecords();
				for(xbean.BingFengListRecord current : recordList){
					tryInsertToDes(current, dst.getRecords());
				}
				
				int size = dst.getRecords().size();
				if(size > Constant.BINGFENG_LIST_NUM){
					for(int i=Constant.BINGFENG_LIST_NUM; i<size; i++){
						dst.getRecords().remove(Constant.BINGFENG_LIST_NUM);
					}
				}
				
			}
		}
		
		dest_value.clear();
		dstBingFengRank.marshal(dest_value);
		
		
		
		
		
		
		return true;
	}
	
	
	private void tryInsertToDes(BingFengListRecord current,	List<BingFengListRecord> records) {
		for(int i=records.size()-1; i>=0; i--){
			if(compareTo(current, records.get(i))){
				if(i==0){
					records.add(0, current);
				}
				continue;
			}else{
				records.add(i+1, current);
				break;
			}
		}
		
	}

	private boolean compareTo(BingFengListRecord currentRecord, BingFengListRecord otherRecord) {
		if (currentRecord.getMarshaldata().getStage() > otherRecord.getMarshaldata().getStage())
			return true;
		
		if (currentRecord.getMarshaldata().getStage() < otherRecord.getMarshaldata().getStage())
			return false;
		
		if (currentRecord.getMarshaldata().getRounds() < otherRecord.getMarshaldata().getRounds())
			return true;
		
		if (currentRecord.getMarshaldata().getRounds() > otherRecord.getMarshaldata().getRounds())
			return false;
		
		if (currentRecord.getMarshaldata().getTimes() < otherRecord.getMarshaldata().getTimes())
			return true;

		return false;
	}

}
