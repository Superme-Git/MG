package fire.pb.pet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mkdb.Transaction;
import fire.pb.DataInit;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffPetImpl;
import fire.pb.course.CourseType;
import fire.pb.skill.Result;
import fire.pb.skill.SkillPet;
import fire.pb.talk.MessageMgr;

/**
 * 增加宠物经验
 * addexp >0 加经验
 * addexp <0 减经验
 */
public class PAddPetExpProc extends mkdb.Procedure {

	static public final int ADD_REASON_DEFAULT = 0; // 默认
	static public final int ADD_REASON_EXPCULTIVATE = 1; // 使用经验道具培养

	private final long roleId;
	private final int petKey;
	private final long addExp;
	private boolean showMsg = true;
	private int reason = ADD_REASON_DEFAULT;
	
	public PAddPetExpProc(final long roleId, final int petKey, final long addExp) {
		super();
		this.roleId = roleId;
		this.petKey = petKey;
		this.addExp = addExp;
	}

	public PAddPetExpProc(final long roleId, final int petKey,
			final long addExp, final boolean show) {
		super();
		this.roleId = roleId;
		this.petKey = petKey;
		this.addExp = addExp;
		this.showMsg = show;
	}

	public PAddPetExpProc(final long roleId, final int petKey,
			final long addExp, final boolean show, final int reason) {
		this(roleId, petKey, addExp, show);
		this.reason = reason;
	}

	public boolean process() {

		if (addExp == 0)
			return false;

		if (Helper.isPetInBattle(roleId, petKey))
			return false; // 战斗中的参战宠不能执行当前操作

		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		Pet pet = petCol.getPet(petKey);
		if (pet == null)
			return false;

		final xbean.PetInfo petInfo = pet.getPetInfo();

		int oldLevel = petInfo.getLevel();

		int levelSpace = DataInit.ROLE_PET_LEVEL_SPACE;
		if (reason == ADD_REASON_EXPCULTIVATE) {
			levelSpace = DataInit.ROLE_PET_LEVEL_SPACE_OFEXPITEM;
		}

		int roleLevel = xtable.Properties.selectLevel(roleId);
		if (petInfo.getLevel() >= roleLevel + levelSpace && addExp > 0) {
			if (reason == ADD_REASON_DEFAULT) {
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 141794, null);
			}
			return false;
		}

		final long curExp = petInfo.getExp();
		final fire.pb.effect.Role epet = new fire.pb.effect.PetImpl(roleId, petKey);
		final long nextExp = epet.getNextExp();
		if (curExp + addExp <= 0) {
			petInfo.setExp(0);
		} else if (curExp + addExp >= nextExp) {
			petInfo.setExp(curExp + addExp);
			Map<Integer, Float> changeAttrs = new HashMap<Integer, Float>();
			int i = 1;
			while (petInfo.getExp() >= epet.getNextExp()) {
				Map<Integer, Float> res = epet.levelUp();
				if (res != null) {
					changeAttrs.putAll(res);
					CalcPetAttr.graspPekSkillWhileUplevel(roleId, petKey); // 领悟技能
				} else {
					break;
				}

				i++;
				if (i > 155) {
					Module.logger.error("宠物一次升级次数过多 PetExp = "
							+ petInfo.getExp() + "NextExp = "
							+ epet.getNextExp());
					break;
				}
			}

			if (!changeAttrs.isEmpty()) {
				// 不管领悟不领悟新技能都要重新加载一次被动技能，因为数值会随宠物等级而变化
				SkillPet skillPet = new SkillPet(petInfo, roleId);
				Result result = skillPet.addSkillBuffWhileOnline(null);
				// 发送结果变化
				for (Map.Entry<Integer, Float> entry : changeAttrs.entrySet()) {
					if (!result.getChangedAttrs().containsKey(entry.getKey()))
						result.getChangedAttrs().put(entry.getKey(), entry.getValue());
				}
				BuffAgent buffAgent = new BuffPetImpl(roleId, petKey);
				buffAgent.psendSBuffChangeResult(result);
			}
		} else {
			petInfo.setExp(curExp + addExp);
		}

		if (addExp > 0) {
			if (showMsg) {
				java.util.ArrayList<String> params = new java.util.ArrayList<String>();
				params.add(petInfo.getName());
				PetAttr petAttr = fire.pb.pet.Module.getInstance().getPetManager().getAttr(petInfo.getId());
				if (petAttr != null) {
					params.add(petAttr.getColour());
				} else {
					params.add("ffff0000");
				}
				params.add(Long.toString(addExp));
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleId, 141157, params);
			}
		}

		if (petInfo.getLevel() > oldLevel) {
			// 发送消息提示升级
			List<String> param = new ArrayList<String>();
			param.add(pet.getName());
			param.add(fire.pb.pet.Module.getInstance().getPetColorRGB(pet.getColor()));
			if (Transaction.current() == null) {
				MessageMgr.sendMsgNotify(roleId, 141404, param);
			} else {
				MessageMgr.psendMsgNotifyWhileCommit(roleId, 141404, param);
			}
		}

		int newLevel = petInfo.getLevel();
		if (newLevel - oldLevel > 0) {

			pet.updatePetScoreWhileChange();
			fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.PET_SCORE, pet.getPetInfo().getPetscore());
			if (newLevel >= 60) {
				List<xbean.PetSkill> skills = pet.getBattleskills();
				for (xbean.PetSkill skill : skills) {
					fire.pb.course.CourseManager.achieveCourse(roleId, CourseType.HAVE_PET_COURSE, skill.getSkillid(), 0);
				}
			}
		}

		// 修改给客户端发经验的方式
		mkdb.Procedure.psendWhileCommit(roleId, new SRefreshPetExp(petKey, petInfo.getExp()));

		onLog(roleId, petInfo.getKey(), petInfo.getUniqid(), petInfo.getId(), curExp, addExp, oldLevel, newLevel, reason);
		return true;
	}

	private void onLog(long roleId, int petKey, long uniqId, int petId, long curExp, long addExp, int oldLevel, int newLevel, int reason) {
		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[PAddPetExpProc] roleId:" + roleId
					+ " petKey:" + petKey
					+ " uniqId:" + uniqId
					+ " petId:" + petId
					+ " curExp:" + curExp
					+ " addExp:" + addExp
					+ " oldLevel:" + oldLevel
					+ " newLevel:" + newLevel
					+ " reason:" + reason);
		}
	}
}
