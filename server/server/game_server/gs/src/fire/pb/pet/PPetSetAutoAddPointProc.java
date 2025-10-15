package fire.pb.pet;

/**
 * 客户端请求设置宠物加点方案
 * @author XGM
 */
public class PPetSetAutoAddPointProc extends mkdb.Procedure {

	private final long roleId;
	private final int petKey;
	private int cons; 		//体质加点数
	private int iq; 		//智力加点数
	private int str; 		//力量加点数
	private int endu; 		//耐力加点数
	private int agi; 		//敏捷加点数

	public PPetSetAutoAddPointProc(final long roleId, final int petKey,
			final int str, final int iq, final int cons,
			final int endu, final int agi) {
		this.roleId = roleId;
		this.petKey = petKey;
		this.cons = cons;
		this.iq = iq; 
		this.str = str;
		this.endu = endu;
		this.agi = agi;
	}

	@Override
	public boolean process(){
		int sum = cons + iq + str + endu + agi;
		if (sum < 0 || sum > 5)
			return false;

		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		final xbean.PetInfo petInfo = petCol.getPetInfo(petKey);
		if (null == petInfo)
			return false;

		// 设置方案
		petInfo.setAutoaddcons(cons);
		petInfo.setAutoaddiq(iq);
		petInfo.setAutoaddstr(str);
		petInfo.setAutoaddendu(endu);
		petInfo.setAutoaddagi(agi);

		// 通知客户端
		final SPetSetAutoAddPoint send = new SPetSetAutoAddPoint(petKey, str, iq, cons, endu, agi);
		psendWhileCommit(roleId, send);

		// 记录日志
		if (Module.logger.isDebugEnabled()) {
			Module.logger.debug("[PPetSetAutoAddPointProc] roleId:" + roleId
					+ " petKey:" + petKey
					+ " uniqId:" + petInfo.getUniqid()
					+ " petId:" + petInfo.getId()
					+ " cons:" + cons	//体质
					+ " iq:" + iq		//智力
					+ " str:" + str		//力量
					+ " endu:" + endu	//耐力
					+ " agi:" + agi		//敏捷
					+ " sum:" + sum);
		}
		return true;
	}
}
