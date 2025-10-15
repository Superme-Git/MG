package fire.pb.gm;

import fire.pb.pet.Module;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.pet.shenshou.PShenShouChongZhi;;

public class GM_shenshoureset extends GMCommand {
	@Override
	boolean exec(String[] args) {
		long roleId = getGmroleid();
		PetAttr pa = Module.getInstance().getPetManager().randGetOneShenShou();
		PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, true);
		fire.pb.pet.Pet pet = petCol.getPets().get(0);
		new PShenShouChongZhi(getGmroleid(), pet.getPetkey(), pa.getId()).submit();
		sendToGM("重置神兽成功");
		return true;
	}

	@Override
	String usage() {
		return "addpet petid level";
	}

}
