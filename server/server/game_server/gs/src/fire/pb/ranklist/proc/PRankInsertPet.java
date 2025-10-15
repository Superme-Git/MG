package fire.pb.ranklist.proc;

import fire.pb.pet.Module;
import fire.pb.pet.Pet;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColour;
import fire.pb.ranklist.RankType;
import mkdb.Procedure;

/***
 * 插入排行榜
 * @author changhao
 *
 */
public class PRankInsertPet extends Procedure {
	
	long uniqid;
	
	private boolean isShowMsg;

	public PRankInsertPet(long uniqid, boolean isShowMsg) {
		this.uniqid = uniqid;
		this.isShowMsg = isShowMsg;
	}
	
	@Override
	public boolean process(){
		
		Pet pet = Pet.getPet(uniqid, true);
		if(null == pet)
			return true;
		
		xbean.PetInfo petInfo = pet.getPetInfo();
		if(petInfo.getPetscore() < 2000 - 600)
			return true;
		
		xbean.PetScoreListRecord recored = xbean.Pod.newPetScoreListRecord();
		recored.setTime(System.currentTimeMillis());
		
		xbean.MarshalPetScoreRecord marshRecord = recored.getMarshaldata();
		marshRecord.setColour(Pet.getClour(PetColour.WHITE));
		String nickName = xtable.Properties.selectRolename(petInfo.getOwnerid());
		if(null == nickName){
			nickName = "";
		}
		marshRecord.setNickname(nickName);
		marshRecord.setPetgrade(petInfo.getPetscore());
		PetAttr attr = Module.getInstance().getPetManager().getAttr(petInfo.getId());
		String petName = attr == null ? "" : petInfo.getName();
		marshRecord.setPetname(petName);
		marshRecord.setRoleid(petInfo.getOwnerid());
		marshRecord.setUniquepetid(petInfo.getUniqid());
		
		
		xbean.PetScoreRankList list = xtable.Petscorelist.get(1);
		if(null == list){
			list = xbean.Pod.newPetScoreRankList();
			xtable.Petscorelist.insert(1, list);
		}
		
		RankListManager.getInstance().tryInsertRecord(RankType.PET_GRADE_RANK, list.getRecords(), recored);
		
		return true;
	}

	public boolean isShowMsg() {
		return isShowMsg;
	}

	public void setShowMsg(boolean isShowMsg) {
		this.isShowMsg = isShowMsg;
	}
	
	

}
