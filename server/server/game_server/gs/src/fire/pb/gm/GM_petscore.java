package fire.pb.gm;

import fire.pb.pet.Pet;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;

public class GM_petscore extends GMCommand {

	@Override
	boolean exec(String[] args) {
		long roleid = getGmroleid();
		final fire.pb.pet.PetColumn petcol = new fire.pb.pet.PetColumn(
				roleid, PetColumnTypes.PET, true);

		for (Pet pet : petcol.getPets()) {
			int score = pet.calculatePetScore();
			if (score != 0) {
				String petName = pet.getName();
				MessageMgr.sendMsgToPop(roleid, "宠物： " + petName + " 的评分为： "
						+ score);
			}
		}

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
