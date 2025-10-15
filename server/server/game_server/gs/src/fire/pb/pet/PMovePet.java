package fire.pb.pet;

public class PMovePet extends mkdb.Procedure {

	private final int srcCol;
	private final int srcKey;
	private final int dstCol;
//	private final long npcKey;
	private final long roleId;

	PMovePet(int srcCol, int srcKey, int dstCol, long npcKey, long roleId) {
		super();
		this.srcCol = srcCol;
		this.srcKey = srcKey;
		this.dstCol = dstCol;
//		this.npcKey = npcKey;
		this.roleId = roleId;
	}

	public boolean process() {

		final PetColumn srcPetCol = new PetColumn(roleId, srcCol, false);
		if (srcCol == dstCol)
			return false;
		final PetColumn dstPetCol = new PetColumn(roleId, dstCol, false);

		final int ret = PetColumn.doMovePet(srcPetCol, srcKey, dstPetCol);
		if (ret < 0) {
			final SPetError errosend = new SPetError(ret);
			psendWhileRollback(roleId, errosend);
		}
		return ret >= 0;
	}
}
