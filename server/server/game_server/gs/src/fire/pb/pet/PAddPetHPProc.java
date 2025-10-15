package fire.pb.pet;

import java.util.HashMap;

import fire.pb.attr.AttrType;
import fire.pb.effect.PetImpl;
import fire.pb.effect.Role;
import mkdb.Procedure;

public class PAddPetHPProc extends Procedure {

	private final long roleId;
	private final int petKey;
	private final long addHp;

	public PAddPetHPProc(final long roleId, final int petKey, final long addHp) {
		super();
		this.roleId = roleId;
		this.petKey = petKey;
		this.addHp = addHp;
	}

	public boolean process() {
		if (addHp == 0)
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
		int tmp = petImpl.addHp((int) addHp);
		if (tmp != 0) {
			petAttr.put(AttrType.HP, (float) petImpl.getHp());
			fire.pb.attr.SRefreshPetData refrenshData = new fire.pb.attr.SRefreshPetData(PetColumnTypes.PET, petKey, petAttr);
			psendWhileCommit(roleId, refrenshData);
			return true;
		}
		return false;
	}
}
