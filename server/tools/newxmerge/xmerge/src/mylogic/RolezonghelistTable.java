package mylogic;

import java.util.ArrayList;
import java.util.List;

import xbean.RoleRankRecord;
import xbean.RoleZongheRankRecord;
import xmerge.Constant;
import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.ranklist.RankType;
import fire.pb.ranklist.proc.RankListManager;

public class RolezonghelistTable implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.RoleZongheRankList srcRoleZongheRankList = xbean.Pod.newRoleZongheRankListData(); 
		xbean.RoleZongheRankList dstRoleZongheRankList = xbean.Pod.newRoleZongheRankListData(); 
		srcRoleZongheRankList.unmarshal(src_value);
		dstRoleZongheRankList.unmarshal(dest_value);
		
		java.util.List<xbean.RoleZongheRankRecord> recordList = srcRoleZongheRankList.getRecords();
		for(xbean.RoleZongheRankRecord current : recordList){
			tryInsertToDes(current, dstRoleZongheRankList.getRecords());
		}
		
		int size = dstRoleZongheRankList.getRecords().size();
		if(size > Constant.ROLE_ZONGHE_LIST_NUM){
			for(int i=Constant.ROLE_ZONGHE_LIST_NUM; i<size; i++){
				dstRoleZongheRankList.getRecords().remove(Constant.ROLE_ZONGHE_LIST_NUM);
			}
		}
		
		
		
		dest_value.clear();
		dstRoleZongheRankList.marshal(dest_value);
		return true;
	}
	
	private void tryInsertToDes(RoleZongheRankRecord current,	List<RoleZongheRankRecord> records) {
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

	private boolean compareTo(RoleZongheRankRecord currentRecord, RoleZongheRankRecord otherRecord) {
		if (currentRecord.getScore() > otherRecord.getScore()) {
			return true;
		}

		if (currentRecord.getScore() == otherRecord.getScore()) {
			return currentRecord.getTriggertime() < otherRecord
					.getTriggertime();
		}

		return false;
	
	}
	
	

}
