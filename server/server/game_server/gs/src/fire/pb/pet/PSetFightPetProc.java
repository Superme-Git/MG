package fire.pb.pet;

import fire.pb.DataInit;
import fire.pb.skill.SkillPet;

/**
 * 参战/取消
 */
public class PSetFightPetProc extends mkdb.Procedure {

	private final long roleId;
	private final int petKey;
	private final boolean on;
	private boolean isInBattle = false;

	// 战斗外参战/休息,调用这个构造
	public PSetFightPetProc(final long roleId, final int petKey, boolean on) {
		this.roleId = roleId;
		this.petKey = petKey;
		this.on = on;
	}

	// 战斗中召唤/召回,调用这个构造
	public PSetFightPetProc(final long roleId, final int petKey, boolean on, boolean isInBattle) {
		this(roleId, petKey, on);
		this.isInBattle = isInBattle;
	}

	public boolean process() {

		final fire.pb.PropRole prop = new fire.pb.PropRole(roleId, false);
		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);

		if (on) {
			final xbean.PetInfo petInfo = petCol.getPetInfo(petKey);
			if (null == petInfo)
				return false;

			// 检测参战等级差
			if (petInfo.getLevel() > prop.getLevel() + DataInit.PET_USELEVEL_SPACE) {
				return false;
			}
			
			// 检测寿命 如果寿命是永久的 就不用检查了
			boolean isLifeForever = fire.pb.pet.Module.getInstance().getPetManager().isPetLifeForever(petInfo.getId());
			if (petInfo.getLife() < DataInit.PET_FIGHT_LIFE_LIMIT && !isLifeForever) {
				return false;
			}
			
			// 检测参战等级
			if (prop.getLevel() < Pet.getPetUseLevelConfig(petInfo.getId())) {
				return false;
			}

			prop.setFightpetkey(petKey);

			final SSetFightPet send = new SSetFightPet(petKey, (byte) (isInBattle ? 1 : 0));
			psendWhileCommit(roleId, send);

			SkillPet skillAgent = new SkillPet(petInfo, roleId); // 更新宠物被动技能 by changaho
			skillAgent.updateSkillBuffWhileOut(null);

			// 通知任务模块
			fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.SetFightPetEvent(
					roleId, petInfo.getKey(), petInfo.getId(), Pet.getClour(PetColour.WHITE)));
		} else {

			if (prop.getFightpetkey() <= 0)
				return false;

			prop.setFightpetkey(-1);

			final SSetFightPetRest send = new SSetFightPetRest((byte) (isInBattle ? 1 : 0));
			psendWhileCommit(roleId, send);

			fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.SetFightPetEvent(
					roleId, -1, -1, -1));
		}

		// 记录日志
		if (Module.logger.isDebugEnabled()) {
			Module.logger.debug("[PSetFightPetProc] roleId:" + roleId
					+ " petKey:" + petKey
					+ " on:" + on
					+ " isInBattle:" + isInBattle);
		}
		return true;
	}
}
