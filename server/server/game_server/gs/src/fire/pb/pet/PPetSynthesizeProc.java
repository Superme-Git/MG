package fire.pb.pet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.log.YYLogger;
import fire.pb.attr.AttrType;
import fire.pb.common.SCommon;
import fire.pb.main.ConfigManager;
import fire.pb.tel.utils.GoodsSafeLocksUtils;
import fire.pb.util.Misc;

/**
 * 宠物合成
 * @author XGM
 */
public class PPetSynthesizeProc extends mkdb.Procedure {

	private final long roleId;
	private final int petKey1;
	private final int petKey2;

	public PPetSynthesizeProc(final long roleId, final int petKey1, final int petKey2) {
		this.roleId = roleId;
		this.petKey1 = petKey1;
		this.petKey2 = petKey2;
	}

	@Override
	public boolean process() {

		Integer curLevel = xtable.Properties.selectLevel(roleId);
		if(curLevel == null)
			return false;

		// 人物达到55级才能合宠
		if (curLevel < 55)
			return false;

		// 参战的宠物不能合成
		Integer fightPetKey = xtable.Properties.selectFightpetkey(roleId);
		if (fightPetKey == petKey1 || fightPetKey == petKey2)
			return false;

		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		final Pet pet1 = petCol.getPet(petKey1);
		final Pet pet2 = petCol.getPet(petKey2);
		if (null == pet1 || null == pet2)
			return false;
		//道具锁判断
		if (GoodsSafeLocksUtils.checkLockStatus(roleId, pet1.getPetInfo())) {
			return false;
		}
		//道具锁判断
		if (GoodsSafeLocksUtils.checkLockStatus(roleId, pet2.getPetInfo())) {
			return false;
		}
		if (pet1.isBind() || pet2.isBind())
			return true;

		if (pet1.isLocked() != -1 || pet2.isLocked() != -1)
			return true;

		// 神兽，野生不能合成
		if (pet1.getKind() == PetTypeEnum.WILD || pet2.getKind() == PetTypeEnum.WILD
			|| pet1.getKind() == PetTypeEnum.SACREDANIMAL || pet2.getKind() == PetTypeEnum.SACREDANIMAL)
			return false;

		// 有技能认证的宠物不能合成
		if (isHaveSkillCertification(pet1) == true || isHaveSkillCertification(pet2) == true) {
			return false;
		}
		
		xbean.PetInfo newPetInfo = newPetInfoBySynthesize(pet1, pet2);
		if (newPetInfo != null) {
			if (petCol.removePet(pet1.getPetkey(), PetColumn.REMOVE_REASON_SYNTHESIZE) != 0) {
				return false;
			}
			if (petCol.removePet(pet2.getPetkey(), PetColumn.REMOVE_REASON_SYNTHESIZE) != 0) {
				return false;
			}
			if (petCol.add(newPetInfo, PetColumn.ADD_REASON_SYNTHESIZE) <= 0) {
				return false;
			}
			mkdb.Procedure.psendWhileCommit(roleId, new SPetSynthesize(newPetInfo.getKey()));

			Pet newpet = Pet.getPet(newPetInfo);
			newpet.updatePetScoreWhileChange();

			//历程合宠
			fire.pb.course.CourseManager.achieveUpdate(roleId, fire.pb.course.CourseType.HE_PET);
			fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.HE_PET_JI_NENG, newpet.getBattleskillIds().size());
			fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.PET_SCORE, newpet.getPetInfo().getPetscore());
			// 记录日志
			if (Module.logger.isInfoEnabled()) {
				Module.logger.info("[PPetSynthesizeProc] roleId:" + roleId
						+ " petInfo1:" + Helper.toString(pet1.getPetInfo())
						+ " petInfo2:" + Helper.toString(pet2.getPetInfo())
						+ " newPetInfo:" + Helper.toString(newPetInfo));
			}
			
			// 运营日志
			writeYYLogger(pet1, pet2, newPetInfo, newpet);
			//清除数据
        	GoodsSafeLocksUtils.doClearDataWhileCommit(roleId);
		}
		return true;
	}

	/**
	 * 运营日志
	 * @param pet1
	 * @param pet2
	 * @param newPetInfo
	 * @param newpet
	 */
	private void writeYYLogger(final Pet pet1, final Pet pet2, xbean.PetInfo newPetInfo, Pet newpet) {
		if(pet1 == null || pet2 == null || newPetInfo == null || newpet == null) return;
		fire.log.beans.OpPetMixBean opPetMixBean = new fire.log.beans.OpPetMixBean(
				pet1.getPetAttr().getId(), pet1.getPetAttr().getTreasureScore() > pet1.getPetInfo().getPetscore() ? 0 : 1,
				pet2.getPetAttr().getId(), pet2.getPetAttr().getTreasureScore() > pet2.getPetInfo().getPetscore() ? 0 : 1,
				newPetInfo.getId(), newpet.getPetAttr().getTreasureScore() > newPetInfo.getPetscore() ? 0 : 1);
		YYLogger.petMixLog(roleId, opPetMixBean);
	}

	/**
	 * 是否有技能被认证
	 * @param pet 需要验证的宠物
	 * @return true:被认证 false:没被认证
	 */
	public boolean isHaveSkillCertification(Pet pet)
	{
		List<xbean.PetSkill> skills = pet.getBattleskills();
		for (xbean.PetSkill s : skills) {
			if (s.getCertification() == 1) {
				return true;
			}
		}
		return false;
	}
	
	public List<Integer> getSkillList(int newID, Pet pet1, Pet pet2)
	{
		List<Integer> skillIds = new ArrayList<Integer>();

		// 添加必得技能(sr.rate==1001为必得技能,且只在宠物合成有效)
		if (newID == pet1.getBaseId()) {
			PetAttr attr = Module.getInstance().getPetManager().getAttr(pet1.getBaseId());
			for (SkillRate sr : attr.getSkills()) {
				if (sr.rate >= 1001)
					skillIds.add(sr.getSkillid());
			}
		} else {
			PetAttr attr = Module.getInstance().getPetManager().getAttr(pet2.getBaseId());
			for (SkillRate sr : attr.getSkills()) {
				if (sr.rate >= 1001)
					skillIds.add(sr.getSkillid());
			}
		}

		// 添加其他技能
		List<Integer> randomSkillIds = pet1.getBattleskillIds();
		for (Integer id : pet2.getBattleskillIds()) {
			if (randomSkillIds.contains(id) == false) {
				randomSkillIds.add(id);
			}
		}
		for (Integer id : skillIds) {
			while (randomSkillIds.contains((Object)id) == true) {
				randomSkillIds.remove((Object)id);
			}
		}

		// 额外继承x个技能几率 /（额外可继承0个技能几率+┅┅+额外可继承最大个技能几率）
		Map<Integer, PetSynthesizeSkillRateConfig> confs = fire.pb.main.ConfigManager.getInstance().getConf(
				PetSynthesizeSkillRateConfig.class);
		if (confs != null) {
			// 总几率
			int sumValue = 0;
			for (int i = 0; i <= randomSkillIds.size(); i++) {
				PetSynthesizeSkillRateConfig conf = confs.get(i);
				if (conf != null) {
					sumValue += conf.getValue();
				}
			}

			// 获取技能
			int r = Misc.getRandomBetween(1, sumValue);
			int curValue = 0;
			for (int num = 0; num <= randomSkillIds.size(); num++) {
				PetSynthesizeSkillRateConfig conf = confs.get(num);
				if (conf != null) {
					curValue += conf.getValue();
					if (curValue >= r) {
						// 添加可得数量的技能(随机添加技能)
						for (int n = 0; n < num; n++) {
							int index = Misc.getRandomBetween(0, randomSkillIds.size() - 1);
							int id = randomSkillIds.get(index);
							skillIds.add(id);
							randomSkillIds.remove(index);
						}
						break;
					}
				}
			}
		}
		return skillIds;
	}

	static Double getCommonConf(int id)
	{
		java.util.Map<Integer, SCommon> confs = ConfigManager.getInstance().getConf(SCommon.class);
		if (confs != null) {
			SCommon conf = confs.get(id);
			if (conf != null) {
				return Double.parseDouble(conf.getValue());
			}
		}
		return null;
	}

	/**
	 * 获得资质参数最小值的配置
	 * @return
	 */
	public double getAptFactorMinConf()
	{
		Double p = getCommonConf(102);
		if (p == null)
			p = 0.9;
		return p;
	}

	/**
	 * 获得资质参数最大值的配置
	 * @return
	 */
	public double getAptFactorMaxConf()
	{
		Double p = getCommonConf(103);
		if (p == null)
			p = 1.08;
		return p;
	}

	/**
	 * 随机一个资质
	 * @param v1
	 * @param v2
	 * @return
	 */
	public int randomAptitude(int v1, int v2)
	{
		double p1 = getAptFactorMinConf();
		double p2 = getAptFactorMaxConf();

		int min = (int)((v1 + v2) / 2.0 * p1);
		int max = (int)((v1 + v2) / 2.0 * p2);
		return Misc.getRandomBetween(min, max);
	}

	/**
	 * 获得资质上限
	 * @param pet1
	 * @param pet2
	 * @param maxValue1
	 * @param maxValue2
	 * @return
	 */
	public int getAptitudeMax(Pet pet1, Pet pet2, int maxValue1, int maxValue2)
	{
		if (pet1.getKind() == PetTypeEnum.VARIATION || pet2.getKind() == PetTypeEnum.VARIATION) {
			// 一只变异和一只普通,则普通的上限乘一个系数
			double aptFactorMax = getAptFactorMaxConf();
			if (pet1.getKind() == PetTypeEnum.BABY) {
				maxValue1 = (int) (maxValue1 * aptFactorMax);
			} else if (pet2.getKind() == PetTypeEnum.BABY) {
				maxValue2 = (int) (maxValue2 * aptFactorMax);
			}
		}
		return Math.max(maxValue1, maxValue2);
	}

	/**
	 * 修正资质值
	 * 如果合成中有变异的宠,则限制资质上限
	 * @param pet1
	 * @param pet2
	 * @param value
	 * @param aptId
	 * @return
	 */
	public int fixAptitudeValue(Pet pet1, Pet pet2, int value, int aptId)
	{
		if (pet1.getKind() == PetTypeEnum.VARIATION || pet2.getKind() == PetTypeEnum.VARIATION) {
			int maxValue = -1;
			switch (aptId) {
			case AttrType.PET_ATTACK_APT:
				maxValue = getAptitudeMax(pet1, pet2, pet1.getPetAttr().getAttackaptmax(), pet2.getPetAttr().getAttackaptmax());
				break;
			case AttrType.PET_DEFEND_APT:
				maxValue = getAptitudeMax(pet1, pet2, pet1.getPetAttr().getDefendaptmax(), pet2.getPetAttr().getDefendaptmax());
				break;
			case AttrType.PET_MAGIC_APT:
				maxValue = getAptitudeMax(pet1, pet2, pet1.getPetAttr().getMagicaptmax(), pet2.getPetAttr().getMagicaptmax());
				break;
			case AttrType.PET_PHYFORCE_APT:
				maxValue = getAptitudeMax(pet1, pet2, pet1.getPetAttr().getPhyforceaptmax(), pet2.getPetAttr().getPhyforceaptmax());
				break;
			case AttrType.PET_DODGE_APT:
				maxValue = getAptitudeMax(pet1, pet2, 0, 0);
				break;
			case AttrType.PET_SPEED_APT:
				maxValue = getAptitudeMax(pet1, pet2, pet1.getPetAttr().getSpeedaptmax(), pet2.getPetAttr().getSpeedaptmax());
				break;
			default:
				Module.logger.error("[PPetSynthesizeProc.fixAptitudeValue] roleId:" + roleId
						+ " apt:" + aptId);
				return 0;
			}
			// 限制不能超出限制
			value = Math.min(value, maxValue);
		}
		return value;
	}

	public int randomGrowrate(int v1, int v2)
	{
		Double p1 = getCommonConf(104);
		Double p2 = getCommonConf(105);
		if (p1 == null)
			p1 = 48.0;
		if (p2 == null)
			p2 = 24.0;

		int min = (int)((v1 + v2) / 2.0 - p1);
		int max = (int)((v1 + v2) / 2.0 + p2);
		return Misc.getRandomBetween(min, max);
	}

	public xbean.PetInfo newPetInfoBySynthesize(Pet pet1, Pet pet2)
	{
		// ID规则:两者取其一
		int newID = Misc.getRatePercent() <= 50 ? pet1.getBaseId() : pet2.getBaseId();
		// Kind规则:
		int kind = (newID == pet1.getBaseId() ? pet1.getKind() : pet2.getKind());
		// 等级规则:两者之间取
		int newLevel = Misc.getRandomBetween(pet1.getLevel(), pet2.getLevel());
		// 技能规则:
		List<Integer> skillIds = getSkillList(newID, pet1, pet2);

		// 创建宠物
		xbean.PetInfo newPetInfo = PetColumn.createPet(roleId, newID, newLevel, skillIds, kind, PetColour.WHITE, false);

		// 资质规则:
		int attackApt = randomAptitude(pet1.getAttackapt(), pet2.getAttackapt());
		attackApt = fixAptitudeValue(pet1, pet2, attackApt, AttrType.PET_ATTACK_APT);
		int defendApt = randomAptitude(pet1.getDefendapt(), pet2.getDefendapt());
		defendApt = fixAptitudeValue(pet1, pet2, defendApt, AttrType.PET_DEFEND_APT);
		int magicApt = randomAptitude(pet1.getMagicapt(), pet2.getMagicapt());
		magicApt = fixAptitudeValue(pet1, pet2, magicApt, AttrType.PET_MAGIC_APT);
		int phyforceApt = randomAptitude(pet1.getPhyforceapt(), pet2.getPhyforceapt());
		phyforceApt = fixAptitudeValue(pet1, pet2, phyforceApt, AttrType.PET_PHYFORCE_APT);
		int dodgeApt = randomAptitude(pet1.getDodgeapt(), pet2.getDodgeapt());
		dodgeApt = fixAptitudeValue(pet1, pet2, dodgeApt, AttrType.PET_DODGE_APT);
		int speedApt = randomAptitude(pet1.getSpeedapt(), pet2.getSpeedapt());
		speedApt = fixAptitudeValue(pet1, pet2, speedApt, AttrType.PET_SPEED_APT);

		final CalcPetAttr cattr = new CalcPetAttr(newPetInfo);
		cattr.setBornAttackApt(attackApt);
		cattr.setBornDefendApt(defendApt);
		cattr.setBornMagicApt(magicApt);
		cattr.setBornPhyforceApt(phyforceApt);
		cattr.setBornDodgeApt(dodgeApt);
		cattr.setBornSpeedApt(speedApt);

		// 成长规则:
		int growrate = randomGrowrate(pet1.getGrowrate(), pet2.getGrowrate());
		newPetInfo.setGrowrate(growrate);

		// 清空加点方案
		newPetInfo.setAutoaddcons(0);
		newPetInfo.setAutoaddiq(0);
		newPetInfo.setAutoaddstr(0);
		newPetInfo.setAutoaddendu(0);
		newPetInfo.setAutoaddagi(0);

		return newPetInfo;
	}
}