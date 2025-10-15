package fire.pb.pet;

import java.util.List;

public class PAddPetByPetID extends mkdb.Procedure {

	private final long roleId;
	private final int petColumnId;
	private final int petId;
	private final int level;
	private int type;
	private int reason;
	private List<Integer> skills = null;
	private int starId = 1;
	private final boolean isBind;

	public PAddPetByPetID(long roleId, int petId, int level, int petColumnId,
			int type, List<Integer> skills, int reason, int starId,
			boolean isBind) {
		this.roleId = roleId;
		this.petColumnId = petColumnId;
		this.petId = petId;
		this.level = level;
		this.type = type;
		this.reason = reason;
		this.starId = starId;
		this.skills = skills;
		this.isBind = isBind;
	}

	public PAddPetByPetID(long roleId, int petId, int level, int petColumnId,
			int type, int reason, int starId, boolean isBind) {
		this.roleId = roleId;
		this.petColumnId = petColumnId;
		this.petId = petId;
		this.level = level;
		this.type = type;
		this.reason = reason;
		this.starId = starId;
		this.isBind = isBind;
	}

	@Override
	public boolean process() {
		final PetColumn petCol = new PetColumn(roleId, petColumnId, false);
		int petkey = petCol.addpet(petId, level, type, skills, reason, starId, isBind, null);
		if (petkey < 0)
			return false;
		return true;
	}
}
