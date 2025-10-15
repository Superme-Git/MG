package fire.pb.pet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.log.beans.OpPetSkiBean;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffPetImpl;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.Result;
import fire.pb.skill.SPetSkillupgrade;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.SkillPet;
import fire.pb.skill.SubSkillConfig;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.talk.MessageMgr;
import fire.pb.util.Misc;

/**
 * 客户端请求宠物技能认证
 * @author XGM
 */
public class PPetSkillCertification extends mkdb.Procedure {

	private final long roleId;
	private final int petKey; // 宠物key
	private final int skillId; // 技能ID
	private final int isConfirm; // 1:确定/0:取消

	public PPetSkillCertification(final long roleId, final int petKey,
			final int skillId, final int isConfirm) {
		this.roleId = roleId;
		this.petKey = petKey;
		this.skillId = skillId;
		this.isConfirm = isConfirm; 
	}

	@Override
	public boolean process(){

		if (Helper.isPetInBattle(roleId, petKey))
			return false;

		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		final Pet pet = petCol.getPet(petKey);
		if (null == pet)
			return false;

		if(pet.isLocked() != -1) {
			MessageMgr.psendMsgNotify(roleId, Pet.PET_LOCK_ERROR_MSG, null);
			return true;
		}

		// 神兽不能认证
		if (pet.getKind() == PetTypeEnum.SACREDANIMAL)
			return false;

		if (isConfirm == 1) {
			if (confirm(pet, skillId) == false) {
				return false;
			}			
		} else {
			if (cancel(pet, skillId) == false) {
				return false;
			}
		}

		BuffAgent buffAgent = new BuffPetImpl(roleId, petKey);
		if (isConfirm == 0) {
			// 去除战斗外被动技能带来的效果
			FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillId);
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
		SkillPet spet = new SkillPet(pet.getPetInfo(), roleId);
		Result result = spet.addSkillBuffWhileOnline(null);
		buffAgent.psendSBuffChangeResult(result);
		spet.updateSkillBuffWhileOut(null);

		// 刷新宠物技能
		SRefreshPetSkill send = new SRefreshPetSkill();
		send.petkey = petKey;
		pet.fillSRefreshPetSkill(send);
		mkdb.Procedure.psendWhileCommit(roleId, send);
		// 发送认证结果
		mkdb.Procedure.psendWhileCommit(roleId, new SPetSkillCertification(petKey, skillId, isConfirm));

		pet.updatePetScoreWhileChange();

		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.PET_SCORE, pet.getPetInfo().getPetscore());
		fire.pb.course.CourseManager.achieveUpdate(roleId, fire.pb.course.CourseType.REN_ZHENG_PET);
		
		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[PPetSkillCertification] roleId:" + roleId
					+ " skillId:" + skillId
					+ " isConfirm:" + isConfirm
					+ " petInfo:" + Helper.toString(pet.getPetInfo()));
		}
		return true;
	}

	/**
	 * 确定认证
	 * @param pet 宠物
	 * @param skillId 需要被认证的技能ID
	 * @return 成功/失败
	 */
	public boolean confirm(Pet pet, int skillId) {

		List<xbean.PetSkill> skills = pet.getBattleskills();
		xbean.PetSkill skill = null;
		for (xbean.PetSkill s : skills) {
			if (s.getCertification() == 1) {
				return false; // 已经有技能被认证了
			}
			if (s.getSkillid() == skillId) {
				skill = s;
			}
		}
		if (skill == null) {
			return false; // 没找到需要认证的技能
		}

		// 是否可被认证
		SPetSkillupgrade skillUpConfig = PetManager.getInstance().getSkillUpGrade(skillId);
		if (skillUpConfig == null) {
			return false;
		}
		if (skillUpConfig.getIscancertification() == 0) {
			return false;
		}

		// 消耗钱
		int money = pet.getPetAttr().getCertificationcost();
		if (money > 0) {
			Pack bag = new Pack(roleId, false);
			if (bag.subMoney(-money, "Pet skill certification", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_peiyang, 0) != -money)
				return false;
		}

		// 认证
		skill.setCertification(1);

		// 附加技能
		int newSkillId = getAppendSkillId(pet);
		if (newSkillId > 0) {
			pet.addSkill(newSkillId, -1, 0, 2);
		}
		
		//运营日志
		writeLogger(pet, skillId, OpPetSkiBean.Op_PetSkiBean_Operate_CerMagi);
		return true;
	}

	/**
	 * 取消认证
	 * @param pet 宠物
	 * @param skillId 取消认证时指定被删除的技能ID
	 * @return 成功/失败
	 */
	public boolean cancel(Pet pet, int skillId) {

		boolean isHaveDelSkill = false;
		List<xbean.PetSkill> skills = pet.getBattleskills();
		xbean.PetSkill skill = null;
		for (xbean.PetSkill s : skills) {
			if (s.getCertification() == 1) {
				skill = s;
			}
			if (s.getSkillid() == skillId) {
				isHaveDelSkill = true;
			}
		}
		if (skill == null) {
			return false; // 没有被认证的技能
		}
		if (isHaveDelSkill == false) {
			return false; // 没找到需要被删除的技能
		}

		// 消耗钱
		int money = pet.getPetAttr().getCancelcertificationcost();
		if (money > 0) {
			Pack bag = new Pack(roleId, false);
			if (bag.subMoney(-money, "Pet skill cancel certification", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_peiyang, 0) != -money)
				return false;
		}

		// 删除指定技能
		if (pet.removeSkillById(skillId) == true) {
			// 取消认证
			skill.setCertification(0);
		}
		
		// 运营日志
		writeLogger(pet, skillId, OpPetSkiBean.Op_PetSkiBean_Operate_CancelCerMagi);
				
		return true;
	}

	/**
	 * 获取可附加技能列表
	 * @param pet
	 * @param type
	 * @return 可附加技能列表
	 */
	private List<Integer> getAppendSkillIdList(Pet pet, int type) {

		List<Integer> skillIds = new ArrayList<Integer>();

		Map<Integer, SPetSkillupgrade> mapConfig = ConfigManager.getInstance().getConf(SPetSkillupgrade.class);
		if (mapConfig != null) {

			for (SPetSkillupgrade conf : mapConfig.values()) {

				// 可被附加的技能
				if (conf.getIscertificationappend() == type) {

					// 如果自己没有这个技能则添加到列表
					int skillId = conf.getId();
					if (pet.hasAnySkill(skillId) == false) {
						skillIds.add(conf.getId());
					}
				}
			}
		}
		return skillIds;
	}

	/**
	 * 获取附加技能ID
	 * @param pet 宠物
	 * @return 附加技能ID
	 */
	private int getAppendSkillId(Pet pet) {

		// 获取可附加技能列表
		List<Integer> appendList = getAppendSkillIdList(pet, 1);
		if (appendList.size() == 0) {
			appendList = getAppendSkillIdList(pet, 2);
		}

		// 随机一个技能ID
		if (appendList.size() > 0) {
			int r = Misc.getRandomBetween(0, appendList.size() - 1);
			return appendList.get(r);
		}
		return 0;
	}

	/**
	 * 运营日志
	 * @param pet
	 * @param skillId
	 */
	private void writeLogger(Pet pet, int skillId, int opId) {
		if(pet == null) return;
		
		int isTrea = pet.getPetInfo().getPetscore() > pet.getPetAttr().getTreasureScore() ? 1 : 0;
		OpPetSkiBean opPetSkiBean = new OpPetSkiBean(pet.getPetInfo().getId(), isTrea, pet.getUniqueId(), skillId, opId);
		fire.log.YYLogger.petSkiLog(roleId, opPetSkiBean);
	}
}