package fire.pb.pet;

public class PPetAddPointProc extends mkdb.Procedure {

	private final long roleId;
	private final int petKey;
	private int cons; // 体质加点数
	private int iq; // 智力加点数
	private int str; // 力量加点数
	private int endu; // 耐力加点数
	private int agi; // 敏捷加点数

	public PPetAddPointProc(final long roleId, final int petKey,
			final int cons, final int iq, final int str, final int endu,
			final int agi) {
		this.roleId = roleId;
		this.petKey = petKey;
		this.cons = cons;
		this.iq = iq;
		this.str = str;
		this.endu = endu;
		this.agi = agi;
	}

	public boolean process() {

		if (Integer.MAX_VALUE - cons - iq - str - endu - agi <= 0)
			return false;

		if (cons < 0 || iq < 0 || str < 0 || endu < 0 || agi < 0)
			return false;

		if (Helper.isPetInBattle(roleId, petKey))
			return false;

		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		final xbean.PetInfo petInfo = petCol.getPetInfo(petKey);
		if (null == petInfo)
			return false;

		final int curPoint = petInfo.getPoint();
		final long addPoint = str + iq + cons + endu + agi;

		if (addPoint > curPoint)
			return false;

		final fire.pb.effect.Role epet = new fire.pb.effect.PetImpl(roleId, petKey);
		java.util.Map<Integer, Float> res = epet.addPoints(cons, str, agi, endu, iq);
		if (res != null) {
			final fire.pb.attr.SRefreshPetData refresh = new fire.pb.attr.SRefreshPetData();
			refresh.columnid = PetColumnTypes.PET;
			refresh.petkey = petKey;
			refresh.datas.putAll(res);
			psendWhileCommit(roleId, refresh);

			Pet pet = Pet.getPet(petInfo);
			pet.updatePetScoreWhileChange();
			
			fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.PET_SCORE, pet.getPetInfo().getPetscore());

			// 记录日志
			if (Module.logger.isInfoEnabled()) {
				Module.logger.info("[PPetAddPointProc] roleId:" + roleId
						+ " petKey:" + petKey
						+ " uniqId:" + petInfo.getUniqid()
						+ " petId:" + petInfo.getId()
						+ " addStr:" + str // 力量
						+ " addIq:" + iq // 智力
						+ " addCons:" + cons // 体质
						+ " addEndu:" + endu // 耐力
						+ " addAgi:" + agi // 敏捷
						+ " newStr:" + pet.getBfp().getStr() // 力量
						+ " newIq:" + pet.getBfp().getIq() // 智力
						+ " newCons:" + pet.getBfp().getCons() // 体质
						+ " newEndu:" + pet.getBfp().getEndu() // 耐力
						+ " newAgi:" + pet.getBfp().getAgi() // 敏捷
						+ " addPoint:" + addPoint // 增加点数
						+ " oldPoint:" + curPoint // 加点前的剩余点数
						+ " newPoint:" + petInfo.getPoint() // 加点后的剩余点数
						);
			}
		}
		return true;
	}
}
