package mylogic;

import java.util.ArrayList;
import java.util.List;

import xbean.ClanRankRecord;
import xbean.RoleRankRecord;
import xmerge.Constant;
import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.ranklist.RankType;
import fire.pb.ranklist.proc.RankListManager;

public class Rolerankdatalist implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.RoleRankList srcRoleRankList = xbean.Pod.newRoleRankListData(); 
		xbean.RoleRankList dstRoleRankList = xbean.Pod.newRoleRankListData(); 
		srcRoleRankList.unmarshal(src_value);
		dstRoleRankList.unmarshal(dest_value);
		
		java.util.List<xbean.RoleRankRecord> recordList = srcRoleRankList.getRecords();
		for(xbean.RoleRankRecord current : recordList){
			tryInsertToDes(current, dstRoleRankList.getRecords());
		}
		
		int size = dstRoleRankList.getRecords().size();
		if(size > Constant.LEVEL_RANK_LIST_NUM){
			for(int i=Constant.LEVEL_RANK_LIST_NUM; i<size; i++){
				dstRoleRankList.getRecords().remove(Constant.LEVEL_RANK_LIST_NUM);
			}
		}
		
		
		dest_value.clear();
		dstRoleRankList.marshal(dest_value);
		return true;
	}
	
	
	private void tryInsertToDes(RoleRankRecord current,	List<RoleRankRecord> records) {
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

	private boolean compareTo(RoleRankRecord currentRecord, RoleRankRecord otherRecord) {
		if (currentRecord.getScore() > otherRecord.getScore()) {
			return true;
		}
		if (currentRecord.getScore() == otherRecord.getScore()) {
			if (currentRecord.getLevel() < otherRecord.getLevel()) {
				return true;
			} else {
				if (currentRecord.getLevel() == otherRecord.getLevel()) {
					if (currentRecord.getTriggertime() < otherRecord
							.getTriggertime()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	
	
	
}