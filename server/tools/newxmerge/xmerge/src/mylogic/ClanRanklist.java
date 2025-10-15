package mylogic;

import java.util.List;

import xbean.ClanRankRecord;
import xmerge.Constant;
import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;


public class ClanRanklist implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.ClanRankList src_list = xbean.Pod.newClanRankListData();
		xbean.ClanRankList des_list = xbean.Pod.newClanRankListData();
		
		src_list.unmarshal(src_value);
		des_list.unmarshal(dest_value);
		
		java.util.List<xbean.ClanRankRecord> recordList = src_list.getRecords();
		for(xbean.ClanRankRecord current : recordList){
			tryInsertToDes(current, des_list.getRecords());
		}
		
		int size = des_list.getRecords().size();
		if(size > Constant.FACTION_LIST_NUM){
			for(int i=Constant.FACTION_LIST_NUM; i<size; i++){
				des_list.getRecords().remove(Constant.FACTION_LIST_NUM);
			}
		}
		
		dest_value.clear();
		dest_value.marshal(des_list);
		
		return true;
	}

	private void tryInsertToDes(ClanRankRecord current,	List<ClanRankRecord> records) {
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

	private boolean compareTo(ClanRankRecord current, ClanRankRecord factionRankRecord) {
		if(current.getLevel() > factionRankRecord.getLevel())
			return true;
		if(current.getLevel() == factionRankRecord.getLevel() && current.getTriggertime()< factionRankRecord.getTriggertime())
			return true;
		return false;
	}

}
