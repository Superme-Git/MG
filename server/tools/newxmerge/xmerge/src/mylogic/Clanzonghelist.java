package mylogic;

import java.util.List;

import xbean.ClanZongHeRankRecord;
import xmerge.Constant;
import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class Clanzonghelist implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.ClanZongHeRankList srcClanZongHeRankList = xbean.Pod.newClanZongHeRankListData(); 
		xbean.ClanZongHeRankList dstClanZongHeRankList = xbean.Pod.newClanZongHeRankListData(); 
		srcClanZongHeRankList.unmarshal(src_value);
		dstClanZongHeRankList.unmarshal(dest_value);
		
		java.util.List<xbean.ClanZongHeRankRecord> recordList = srcClanZongHeRankList.getRecords();
		for(xbean.ClanZongHeRankRecord current : recordList){
			tryInsertToDes(current, dstClanZongHeRankList.getRecords());
		}
		
		int size = dstClanZongHeRankList.getRecords().size();
		if(size > Constant.CLAN_ZHONGHE_LIST_NUM){
			for(int i=Constant.CLAN_ZHONGHE_LIST_NUM; i<size; i++){
				dstClanZongHeRankList.getRecords().remove(Constant.CLAN_ZHONGHE_LIST_NUM);
			}
		}
		
		dest_value.clear();
		dstClanZongHeRankList.marshal(dest_value);
		
		
		
		
		
		return true;
	}
	
	
	private void tryInsertToDes(ClanZongHeRankRecord current,	List<ClanZongHeRankRecord> records) {
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

	private boolean compareTo(ClanZongHeRankRecord currentRecord, ClanZongHeRankRecord otherRecord) {
		if (currentRecord.getZonghe() > otherRecord.getZonghe()) {
			return true;
		}

		if (currentRecord.getZonghe() == otherRecord.getZonghe()) {
			return currentRecord.getTriggertime() < otherRecord
					.getTriggertime();
		}

		return false;
	}

}
