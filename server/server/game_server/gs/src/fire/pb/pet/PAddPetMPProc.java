package fire.pb.pet;

import java.util.HashMap;

import fire.pb.attr.AttrType;
import fire.pb.effect.PetImpl;
import fire.pb.effect.Role;
import mkdb.Procedure;

public class PAddPetMPProc extends Procedure {

	private final long roleId;
	private final int petKey;
	private final long addMp;

	public PAddPetMPProc(long roleId, int petKey, long addMp) {
		super();
		this.roleId = roleId;
		this.petKey = petKey;
		this.addMp = addMp;
	}

	public boolean process() {
		if (addMp == 0)
			return false;

		final xbean.Properties prop = xtable.Properties.get(roleId);
		if (null == prop)
			return false;

		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		final xbean.PetInfo petInfo = petCol.getPetInfo(petKey);
		if (null == petInfo)
			return false;

		final java.util.HashMap<Integer, Float> petAttr = new HashMap<Integer, Float>();
		Role petImpl = new PetImpl(roleId, petKey);
		int tmp = petImpl.addMp((int) addMp);
		if (tmp != 0) {
			petAttr.put(AttrType.MP, (float) petImpl.getMp());
			fire.pb.attr.SRefreshPetData refrenshData = new fire.pb.attr.SRefreshPetData(PetColumnTypes.PET, petKey, petAttr);
			psendWhileCommit(roleId, refrenshData);
			return true;
		}
		return false;
	}
}
