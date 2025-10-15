package fire.pb.ranklist.proc;

import fire.pb.pet.Module;
import fire.pb.pet.Pet;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColour;
import fire.pb.ranklist.RankType;
import mkdb.Procedure;

/***
 * 插入排行榜宠物名字变化
 * @author changhao
 *
 */
public class PRankInsertPetChangeName extends Procedure {
	
	long uniqid;
	
	private boolean isShowMsg;

	public PRankInsertPetChangeName(long uniqid, boolean isShowMsg) {
		this.uniqid = uniqid;
		this.isShowMsg = isShowMsg;
	}
	
	@Override
	public boolean process(){
		
		Pet pet = Pet.getPet(uniqid, true);
		if(null == pet)
			return true;
		
		xbean.PetInfo petInfo = pet.getPetInfo();

		xbean.PetScoreRankList list = xtable.Petscorelist.get(1);
		if(null == list)
		{
			return false;
		}
		
		for (xbean.PetScoreListRecord e : list.getRecords())
		{
			if (e.getMarshaldata().getUniquepetid() == uniqid)
			{
				e.getMarshaldata().setPetname(petInfo.getName());
				return true;
			}
		}
		
		return true;
	}

	public boolean isShowMsg() {
		return isShowMsg;
	}

	public void setShowMsg(boolean isShowMsg) {
		this.isShowMsg = isShowMsg;
	}
	
	

}
