package fire.pb.pet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 辅助
 * @author XGM
 */
public class Helper {

	/**
	 * 宠物属性转换成字符串
	 */
	public static String toString(xbean.PetInfo petInfo) {

		List<Integer> skillIds = new ArrayList<Integer>();
		List<Integer> skillTypes = new ArrayList<Integer>();
		List<Integer> skillCertifys = new ArrayList<Integer>();
		for (xbean.PetSkill skill : petInfo.getSkills()) {
			skillIds.add(skill.getSkillid());
			skillTypes.add(skill.getSkilltype());
			skillCertifys.add(skill.getCertification());
		}

		return "{ key:" + petInfo.getKey()
				+ " uniqId:" + petInfo.getUniqid()
				+ " ownerId:" + petInfo.getOwnerid()
				+ " id:" + petInfo.getId()		// 配置表id
				+ " name:" + petInfo.getName()
				+ " kind:" + petInfo.getKind()
				+ " isBind:" + petInfo.getIsbinded()
				+ " lv:" + petInfo.getLevel()
				+ " exp:" + petInfo.getExp()
				+ " point:" + petInfo.getPoint()			// 潜能点
				+ " cons:" + petInfo.getBfp().getCons()		// 体质 
				+ " iq:" + petInfo.getBfp().getIq()			// 智力
				+ " str:" + petInfo.getBfp().getStr()		// 力量
				+ " endu:" + petInfo.getBfp().getEndu()		// 耐力
				+ " agi:" + petInfo.getBfp().getAgi()		// 敏捷
				+ " attackApt:" + petInfo.getBornattackapt()		// 攻击资质
				+ " defendApt:" + petInfo.getBorndefendapt()		// 防御资质
				+ " magicApt:" + petInfo.getBornmagicapt()			// 法力资质
				+ " phyforceApt:" + petInfo.getBornphyforceapt()	// 体力资质
				+ " speedApt:" + petInfo.getBornspeedapt()			// 速度资质
				+ " aptAddCount:" + petInfo.getAptaddcount()		// 资质培养次数
				+ " growRate:" + petInfo.getGrowrate()				// 成长率
				+ " growRateAddCount:" + petInfo.getGrowrateaddcount() // 成长率培养次数
				+ " washCount:" + petInfo.getWashcount()	// 洗练次数
				+ " life:" + petInfo.getLife()				// 寿命
				+ " skillIds:" + Arrays.toString(skillIds.toArray())
				+ " skillTypes:" + Arrays.toString(skillTypes.toArray())
				+ " skillCertifys:" + Arrays.toString(skillCertifys.toArray())
				+ " }";
	}

	/**
	 * 宠物是否在战斗中参战
	 */
	public static boolean isPetInBattle(long roleId, int petKey) {
		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			Integer key = xtable.Properties.selectFightpetkey(roleId);
			if (key != null && key == petKey) {
				return true;
			}
		}
		return false;
	}
}
