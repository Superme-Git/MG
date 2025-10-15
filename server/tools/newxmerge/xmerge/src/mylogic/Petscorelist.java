package mylogic;

import java.util.List;


import com.locojoy.base.Marshal.OctetsStream;

import xbean.PetScoreListRecord;
import xmerge.Constant;
import xmerge.IMerge;

public class Petscorelist implements IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		xbean.PetScoreRankList src_list = xbean.Pod.newPetScoreRankListData();
		xbean.PetScoreRankList des_list = xbean.Pod.newPetScoreRankListData();
		
		src_list.unmarshal(src_value);
		des_list.unmarshal(dest_value);
		
		java.util.List<xbean.PetScoreListRecord> recordList = src_list.getRecords();
		for(xbean.PetScoreListRecord current : recordList){
			tryInsertToDes(current, des_list.getRecords());
		}
		
		int size = des_list.getRecords().size();
		if(size > Constant.PET_SCORE_RANK_LIST_NUM){
			for(int i=Constant.PET_SCORE_RANK_LIST_NUM; i<size; i++){
				des_list.getRecords().remove(Constant.PET_SCORE_RANK_LIST_NUM);
			}
		}
		
		dest_value.clear();
		dest_value.marshal(des_list);
		
		return true;
	}

	private void tryInsertToDes(PetScoreListRecord current,	List<PetScoreListRecord> records) {
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

	private boolean compareTo(PetScoreListRecord current, PetScoreListRecord petScoreListRecord) {
		if(current.getMarshaldata().getPetgrade() >= petScoreListRecord.getMarshaldata().getPetgrade())
			return true;
		return false;
	}

}
