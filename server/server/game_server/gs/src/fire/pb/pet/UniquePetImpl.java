package fire.pb.pet;

import java.util.Collection;

import fire.pb.exception.NoSuchUniquePetException;

public class UniquePetImpl implements IUniquePetWrap {

	xbean.UniquePet uniquePet = null;

	boolean readOnly = false;

	long uniquePetId;

	public UniquePetImpl(long uniquePetId, boolean readOnly)
			throws NoSuchUniquePetException {

		if (readOnly) {
			uniquePet = xtable.Uniquepets.select(uniquePetId);
		} else {
			uniquePet = xtable.Uniquepets.get(uniquePetId);
		}

		if (null == uniquePet)
			throw new NoSuchUniquePetException("uniquePetId:" + uniquePetId
					+ " Uniquepets not found!");

		this.readOnly = readOnly;
		this.uniquePetId = uniquePetId;
	}

	@Override
	public Pet getPetInfo() {
		long roleId = uniquePet.getRoleid();
		Pet pet = getPetFromColumnAndDepot(roleId);
		return pet;
	}

	// 从摆摊背包获取宠物 by changhao
	public Pet getPetFromMarket(long roleId) {
		PetColumn column = null;
		try {
			column = new PetColumn(roleId, PetColumnTypes.MARKETPET, readOnly);
		} catch (Exception e) {
			e.printStackTrace();
			Module.logger.error("Error new PetColumn e:", e);
		}

		if (null != column) {
			Collection<xbean.PetInfo> petList = column.getPetsMap().values();
			for (xbean.PetInfo current : petList) {
				if (uniquePetId == current.getUniqid()) {
					return column.getPet(current.getKey());
				}
			}
		}
		return null;
	}

	private Pet getPetFromColumnAndDepot(long roleId) {
		PetColumn petColumn = null;
		try {
			petColumn = new PetColumn(roleId, PetColumnTypes.PET, readOnly);
		} catch (Exception e) {
			e.printStackTrace();
			Module.logger.error("Error new PetColumn e:", e);
		}

		// 找宠物栏
		if (null != petColumn) {
			Collection<xbean.PetInfo> petList = petColumn.getPetsMap().values();
			for (xbean.PetInfo current : petList) {
				if (uniquePetId == current.getUniqid()) {
					return petColumn.getPet(current.getKey());
				}
			}
		}

		// 找仓库
		PetColumn depot = null;
		try {
			depot = new PetColumn(roleId, PetColumnTypes.DEPOT, readOnly);
		} catch (Exception e) {
			e.printStackTrace();
			Module.logger.error("Error new PetColumn e:", e);
		}

		if (null != depot) {
			Collection<xbean.PetInfo> petList = depot.getPetsMap().values();
			for (xbean.PetInfo current : petList) {
				if (uniquePetId == current.getUniqid()) {
					return depot.getPet(current.getKey());
				}
			}
		}

		return null;
	}

}
