package fire.pb.pet;

/**
 * 改名
 */
public class PModPetName extends mkdb.Procedure {

	private long roleId;
	private int petKey;
	private String name;

	public PModPetName(long roleId, int petKey, String name) {
		this.roleId = roleId;
		this.petKey = petKey;
		this.name = name;
	}

	public boolean process() {
		if (roleId < 0 || petKey < 1 || name == null)
			return false;
		final PetColumn petcol = new PetColumn(roleId, PetColumnTypes.PET, false);
		return petcol.modPetName(petKey, name);
	}

}
