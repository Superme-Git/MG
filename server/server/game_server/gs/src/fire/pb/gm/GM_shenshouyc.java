package fire.pb.gm;

import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.pet.shenshou.PShenShouYangCheng;

public class GM_shenshouyc extends GMCommand {
	@Override
	boolean exec(String[] args) {
		long roleId = getGmroleid();
		PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, true);
		fire.pb.pet.Pet pet = petCol.getPets().get(0);
		new PShenShouYangCheng(getGmroleid(), pet.getPetkey()).submit();
		sendToGM("神兽养成成功");
		return true;
	}

	@Override
	String usage() {
		return "//shenshouyc 神兽养成";
	}
}
