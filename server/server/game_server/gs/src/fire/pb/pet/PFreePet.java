package fire.pb.pet;

import java.util.List;

/**
 * 放生
 */
public class PFreePet extends mkdb.Procedure {

	private long roleId;
	private List<Integer> petKeys;

	public PFreePet(long roleId, List<Integer> petKeys) {
		this.roleId = roleId;
		this.petKeys = petKeys;
	}

	public boolean process() {

		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);

		boolean allFree = true;
		for (int petKey : petKeys) {
			if (!fire.pb.pet.Pet.isInBattle(roleId, petKey))
				return false;
			if (!petCol.freePet(petKey)) {
				allFree = false;
				break;
			}
		}
		return allFree;
	}
}
