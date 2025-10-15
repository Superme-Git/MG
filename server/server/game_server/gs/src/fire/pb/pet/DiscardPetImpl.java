package fire.pb.pet;

import fire.pb.exception.NoSuchUniquePetException;

public class DiscardPetImpl implements IUniquePetWrap {

	boolean readOnly = false;

	xbean.DiscardPet discardPet = null;

	public DiscardPetImpl(long uniqueId, boolean readOnly)
			throws NoSuchUniquePetException {
		if (readOnly) {
			discardPet = xtable.Petrecyclebin.select(uniqueId);
		} else {
			discardPet = xtable.Petrecyclebin.get(uniqueId);
		}

		if (null == discardPet)
			throw new NoSuchUniquePetException("uniqueId:" + uniqueId
					+ " Recycle bin not found!");

		this.readOnly = readOnly;
	}

	@Override
	public Pet getPetInfo() {
		xbean.PetInfo petInfo = discardPet.getPet();
		return Pet.getPet(petInfo);
	}

}
