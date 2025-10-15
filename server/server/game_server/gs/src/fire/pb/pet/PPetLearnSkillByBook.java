package fire.pb.pet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fire.log.beans.OpPetSkiBean;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffPetImpl;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemBase;
import fire.pb.item.Pack;
import fire.pb.item.PetItemShuXing;
import fire.pb.item.pet.PetSkillItem;
import fire.pb.main.ConfigManager;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.Result;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.SkillPet;
import fire.pb.skill.SubSkillConfig;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.talk.MessageMgr;
import fire.pb.util.Misc;
import mkdb.Procedure;

public class PPetLearnSkillByBook extends Procedure {

	private long roleId;
	private int petKey;
	private int bookKey;

	public PPetLearnSkillByBook(long roleId, int petKey, int bookKey) {
		this.roleId = roleId;
		this.petKey = petKey;
		this.bookKey = bookKey;
	}

	@Override
	public boolean process() {

		if (Helper.isPetInBattle(roleId, petKey))
			return false;

		PetColumn petCol = new fire.pb.pet.PetColumn(roleId, PetColumnTypes.PET, false);
		Pet pet = petCol.getPet(petKey);
		if (pet == null) {
			Module.logger.error("[PPetLearnSkillByBook] petKey=" + petKey + " non-existent.");
			return true;
		}

		xbean.PetInfo petInfo = pet.getPetInfo();
		if (pet.isLocked() != -1) {
			MessageMgr.psendMsgNotify(roleId, Pet.PET_LOCK_ERROR_MSG, null);
			return true;
		}

		Pack bag = (Pack) fire.pb.item.Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		ItemBase item = bag.getItem(bookKey);
		if (item == null)
			return false;

		if ((item instanceof PetSkillItem) == false) {
			Module.logger.error("[PPetLearnSkillByBook] use item type != PetSkillItem.");
			return true;
		}

		// 是否有同类技能
		PetSkillItem skillItem = (PetSkillItem) item;
		int skillId = ((PetItemShuXing) skillItem.getItemAttr()).getSkillid();

		if (PetManager.getInstance().getSkillUpGrade(skillId) == null) {
			return false;
		}

		if (PetManager.getInstance().isActiveSkill(skillId)) {
			int count = 0;
			for (int tempId : pet.getBattleskillIds()) {
				if (PetManager.getInstance().isActiveSkill(tempId)) {
					count++;
				}
			}
			if (count >= 6) {
				MessageMgr.psendMsgNotify(roleId, 145221, null); // 宠物最多只能携带6个主动技能。
				return false;
			}
		}

		// 是否有同类技能
		if (pet.hasAnySkill(skillId)) {
			MessageMgr.psendMsgNotify(roleId, 141700, null);
			return true;
		}

		int oldSkillId = -4;
		// 获取学习技能结果
		int learnResult = getLearnSkillResult(pet);
		if (learnResult == RESULT_NULL) {
			// 未学习到技能
			// 不作处理
			return true;
		} else if (learnResult == RESULT_ADD) {
			// 学习技能
			if (pet.addSkill(skillId, -1, (int) skillItem.getDataItem().getExtid(), 1) == false) {
				return false;
			}
		} else if (learnResult >= RESULT_REPLACE) {
			// 替换技能
			oldSkillId = pet.insertSkill(learnResult, skillId, -1, 1);
			if (oldSkillId < 0) {
				return false;
			}
		} else {
			// 无效
			return true;
		}

		if (pet.getBattleskillIds().size() > pet.petSkillsGrid()) {
			return false;
		}

		if (bag.removeItemWithKey(bookKey, 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_peiyang, skillItem.getItemId(), "PetLearnSkill") != 1) {
			return false;
		}

		BuffAgent buffAgent = new BuffPetImpl(roleId, petKey);

		if (oldSkillId > 0) {
			// 去除战斗外被动技能带来的效果
			FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(oldSkillId);
			if (sconf != null && !sconf.isActiveSkill() && sconf.getType() == SkillConstant.BATTLE_SKILL_TYPE_PASSIVE && sconf.getSubSkills()[0] != null
					&& sconf.getSubSkills()[0].getBuffUnits()[0] != null) {
				for (SubSkillConfig subSkill : sconf.getSubSkills()) {
					for (BuffUnit buffArg : subSkill.getBuffUnits()) {
						if (buffArg != null && buffArg.buffIndex > 0)
							buffAgent.removeCBuffWithSP(buffArg.buffIndex);
					}
				}
			}
		}

		SkillPet spet = new SkillPet(petInfo, roleId);
		Result result = spet.addSkillBuffWhileOnline(null);
		buffAgent.psendSBuffChangeResult(result);
		spet.updateSkillBuffWhileOut(null);

		// 刷客户端协议
		SRefreshPetSkill send = new SRefreshPetSkill();
		send.petkey = petKey;
		pet.fillSRefreshPetSkill(send);
		mkdb.Procedure.psendWhileCommit(roleId, send);
		pet.updatePetScoreWhileChange();
		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.PET_SCORE, pet.getPetInfo().getPetscore());

		fire.pb.course.CourseManager.achieveUpdate(roleId, fire.pb.course.CourseType.LEARN_PET);
		// 运营日志
		onLog(roleId, pet, skillId);
		return true;
	}

	public static final int RESULT_INVALID = -3; // 无效
	public static final int RESULT_NULL = -2; // 未学习到技能
	public static final int RESULT_ADD = -1; // 添加技能
	public static final int RESULT_REPLACE = 0; // 替换技能(替换技能列表中的技能下标从0开始)

	public int getLearnSkillResult(Pet pet) {
		// 当宠物技能数不足2个时，学习技能时只会增加技能，不会顶替
		// 当宠物技能数达到2个时，学习技能后，较大几率顶替原有技能，较小几率额外增加技能
		// 当宠物技能达到4个后，学习技能只会顶替原有技能，随机顶替，所有技能被顶替几率一样。

		Map<Integer, PetLearnSkillRateConfig> confs = ConfigManager.getInstance().getConf(PetLearnSkillRateConfig.class);
		if (confs != null) {
			List<xbean.PetSkill> skills = pet.getBattleskills();
			int num = skills.size();

			// 是否替换现有技能
			boolean isReplace = true;
			PetLearnSkillRateConfig conf = confs.get(num);
			if (conf != null) {
				if (conf.getRate() > 0) {
					// 这里把随机值的范围定在[0-9999]
					// 0<=r<=9999
					int r = Misc.getRandomBetween(0, 9999);
					int v = (int) (conf.getRate() * 10000);
					if (v >= r) {
						isReplace = false;
					}
				}
			}

			// 直接添加
			if (num < 4) {
				if (isReplace == false) {
					return RESULT_ADD;
				}
			}

			// 取可替换的技能下标
			List<Integer> replaceIndexList = new ArrayList<Integer>();
			for (int i = 0; i < skills.size(); ++i) {
				xbean.PetSkill skill = skills.get(i);
				if (skill.getCertification() == 0 && isCanBeReplace(pet, i)) {
					replaceIndexList.add(i);
				}
			}

			// 随机一个可替换下标
			if (replaceIndexList.size() > 0) {
				int index = Misc.getRandomBetween(0, replaceIndexList.size() - 1);
				return replaceIndexList.get(index);
			}

			// 这里应该到不了
			return RESULT_NULL;
		}
		return RESULT_INVALID;
	}

	/**
	 * 神兽技能是否可被替换
	 * 
	 * @param index
	 * @return
	 */
	private boolean isCanBeReplace(Pet pet, int index) {
		if (PetTypeEnum.SACREDANIMAL != pet.getKind()) {
			return true;
		}

		boolean isCanBeReplace = true;
		switch (index) {
		case 0:
			if (pet.getPetAttr().getIsbindskill1() != 0)
				isCanBeReplace = false;
			break;
		case 1:
			if (pet.getPetAttr().getIsbindskill2() != 0)
				isCanBeReplace = false;
			break;
		case 2:
			if (pet.getPetAttr().getIsbindskill3() != 0)
				isCanBeReplace = false;
			break;
		case 3:
			if (pet.getPetAttr().getIsbindskill4() != 0)
				isCanBeReplace = false;
			break;
		case 4:
			if (pet.getPetAttr().getIsbindskill5() != 0)
				isCanBeReplace = false;
			break;
		default:
			break;
		}
		return isCanBeReplace;
	}

	public void onLog(long roleId, Pet pet, int SkiId) {
		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			List<Integer> skillIds = new ArrayList<Integer>();
			List<Integer> skillTypes = new ArrayList<Integer>();
			List<Integer> skillCertifys = new ArrayList<Integer>();
			for (xbean.PetSkill skill : pet.getPetInfo().getSkills()) {
				skillIds.add(skill.getSkillid());
				skillTypes.add(skill.getSkilltype());
				skillCertifys.add(skill.getCertification());
			}
			Module.logger.info("[PPetLearnSkillByBook] roleId:" + roleId
					+ " petKey:" + pet.getPetInfo().getKey()
					+ " uniqId:" + pet.getPetInfo().getUniqid()
					+ " petId:" + pet.getPetInfo().getId()
					+ " skillIds:" + Arrays.toString(skillIds.toArray())
					+ " skillTypes:" + Arrays.toString(skillTypes.toArray())
					+ " skillCertifys:" + Arrays.toString(skillCertifys.toArray()));
		}
		
		// 运营日志
		writeYYLogger(roleId, pet, SkiId);
	}

	/**
	 * 运营日志
	 * @param roleId
	 * @param pet
	 * @param SkiId
	 */
	private void writeYYLogger(long roleId, Pet pet, int SkiId) {
		if(pet == null) return;

		int isTrea = pet.getPetInfo().getPetscore() > pet.getPetAttr().getTreasureScore() ? 1 : 0;
		OpPetSkiBean opPetSkiBean = new OpPetSkiBean(pet.getPetInfo().getId(), 
				isTrea, pet.getPetInfo().getUniqid(),SkiId, OpPetSkiBean.Op_PetSkiBean_Operate_Add);
		
		fire.log.YYLogger.petSkiLog(roleId, opPetSkiBean);
	}
}
