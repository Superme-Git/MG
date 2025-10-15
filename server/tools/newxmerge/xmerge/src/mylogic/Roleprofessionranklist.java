package mylogic;

import java.util.ArrayList;
import java.util.List;

import xbean.RoleProfessionRankList;
import xbean.RoleProfessionRankRecord;
import xbean.RoleRankRecord;
import xmerge.Constant;
import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.ranklist.RankType;
import fire.pb.ranklist.proc.RankListManager;

public class Roleprofessionranklist implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		int  ranktype = src_key.unmarshal_int();
		xbean.RoleProfessionRankList srcRoleProfessionRankList = xbean.Pod.newRoleProfessionRankListData(); 
		xbean.RoleProfessionRankList dstRoleProfessionRankList = xbean.Pod.newRoleProfessionRankListData(); 
		srcRoleProfessionRankList.unmarshal(src_value);
		dstRoleProfessionRankList.unmarshal(dest_value);
		
		java.util.List<xbean.RoleProfessionRankRecord> recordList = srcRoleProfessionRankList.getRecords();
		for(xbean.RoleProfessionRankRecord current : recordList){
			tryInsertToDes(current, dstRoleProfessionRankList.getRecords());
		}
		
		int size = dstRoleProfessionRankList.getRecords().size();
		if(size > Constant.LEVEL_PROFESSION_LIST_NUM){
			for(int i=Constant.LEVEL_PROFESSION_LIST_NUM; i<size; i++){
				dstRoleProfessionRankList.getRecords().remove(Constant.LEVEL_PROFESSION_LIST_NUM);
			}
		}
		
		dest_value.clear();
		dstRoleProfessionRankList.marshal(dest_value);
		return true;
	}
	
	private void tryInsertToDes(RoleProfessionRankRecord current,	List<RoleProfessionRankRecord> records) {
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

	private boolean compareTo(RoleProfessionRankRecord currentRecord, RoleProfessionRankRecord otherRecord) {
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