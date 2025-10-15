package fire.pb.pet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.attr.AttrType;
import fire.pb.attr.ScoreType;
import fire.pb.effect.SPetAbilityScore;
import fire.pb.map.SceneManager;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.talk.MessageMgr;
import fire.pb.util.Misc;

public class CalcPetAttr {

	private final PetAttr petAttr;
	private final xbean.PetInfo petInfo;

	public CalcPetAttr(final xbean.PetInfo petInfo) {
		this.petInfo = petInfo;
		if (null == petInfo) {
			Module.logger.warn("[CalcPetAttr] 构造函数 petInfo == null");
			throw new IllegalArgumentException("参数错误,不能使用此类进行计算, petInfo == null");
		}
		petAttr = Module.getInstance().getPetManager().getAttr(petInfo.getId());
		if (null == petAttr) {
			Module.logger.warn("[CalcPetAttr] 构造函数  宠物数据表找不到宠物ID " + petInfo.getId());
			throw new IllegalArgumentException("参数错误,不该使用此类进行计算, petAttr == null");
		}
	}

	/**
	 * 获取宠物配置
	 */
	public PetAttr getPetAttr() {
		return petAttr;
	}

	public static int PET_NAME = 3;
	public static int PET_GROW_RATE_ID = 4;

	/**
	 * 设置宠物的成长,寿命,名字
	 */
	public void setPetAttrByInitAttrs(Map<Integer, Object> initAttrs) {

		// 设置成长
		initGrowRate();

		// 设置寿命
		petInfo.setLife(getLifeFromConfig());

		// 设置名字
		if (initAttrs != null && initAttrs.containsKey(PET_NAME))
			petInfo.setName((String) (initAttrs.get(PET_NAME)));
		else
			petInfo.setName(getPetNameFromConfig());
	}
	
	/**
	 * 获得配置表的宠物名称
	 */
	public String getPetNameFromConfig() {
		if (null == petAttr)
			return null;
		return petAttr.name;
	}
	
	/**
	 * 分配初始点数
	 */
	public void setInitAttrPoint() {

		if (null == petInfo || petAttr == null)
			return;

		// 分配初始点数
		final int[] point = new int[5];
		if (petAttr.getInitPointAssignType() == 1) { // == 1 (平均分配)
			int n = petAttr.getInitPoint() / 5;
			int s = petAttr.getInitPoint() - n * 5;
			for (int i = 0; i < point.length; i++) {
				point[i] = n;
			}
			// 未分配完的点数加到这里
			point[3] += s;

		} else { // == 2 (随机分配)
			// 随机分配,最少10点
			if (petAttr.getInitPoint() >= 50) {
				Misc.RandomDistribute(petAttr.getInitPoint() - 50, 5, point);
				for (int i = 0; i < point.length; i++) {
					point[i] += 10;
				}
			} else {
				for (int i = 0; i < point.length; i++) {
					point[i] = 0;
				}
				if (petAttr.getInitPoint() > 0) {
					Misc.RandomDistribute(petAttr.getInitPoint(), 5, point);
				}
			}
		}

		petInfo.getBfp().setCons(point[0]);
		petInfo.getBfp().setIq(point[1]);
		petInfo.getBfp().setStr(point[2]);
		petInfo.getBfp().setEndu(point[3]);
		petInfo.getBfp().setAgi(point[4]);

		petInfo.getInitbfp().setCons(petInfo.getBfp().getCons());
		petInfo.getInitbfp().setIq(petInfo.getBfp().getIq());
		petInfo.getInitbfp().setStr(petInfo.getBfp().getStr());
		petInfo.getInitbfp().setEndu(petInfo.getBfp().getEndu());
		petInfo.getInitbfp().setAgi(petInfo.getBfp().getAgi());
	}

	/**
	 * 初始化成长率
	 */
	public int initGrowRate() {

		if (null == petInfo || null == petAttr)
			return -1;

		final java.util.ArrayList<Integer> growRates = petAttr.getGrowrate();
		if (growRates.size() != 7)
			return -1;

		if (petAttr.getGrowrateconst() > 0) {
			petInfo.setGrowrate(petAttr.getGrowrateconst());
			return petAttr.getGrowrateconst();
		} else {
			final int r = mkdb.Mkdb.random().nextInt(growRates.size());
			if (r >= 0 && r < growRates.size()) {
				petInfo.setGrowrate(growRates.get(r));
				return growRates.get(r);
			} else {
				petInfo.setGrowrate(growRates.get(0));
				return growRates.get(0);
			}
		}
	}
	
	/**
	 * 生成技能
	 */
	public void genPetSkill(xbean.PetInfo petInfo) {
		Pet pet = Pet.getPet(petInfo);
		pet.getBattleskills().clear();
		for (SkillRate skillRate : petAttr.getSkills()) {
			if (fire.pb.util.Misc.checkRate(1000 - 1, skillRate.rate))
				pet.addSkill(skillRate.skillid, -1, 0, 0);
		}
	}
	
	/**
	 * 获取资质
	 */
	public static int getApt(int max, int min) {
		if (max == min)
			return max;
		if (max < min)
			return -1;
		int apt = fire.pb.util.Misc.getRandomBetween(min, max);
		return apt;
	}

	/**
	 * 设置攻击资质
	 */
	public void setBornAttackApt(int attackapt){
		if (null == petAttr || null == petInfo){
			Module.logger.warn("[CalcPetAttr.setBornAttackApt] 参数错误");
			return;
		}
		if (attackapt < 0) {
			if (petAttr.getAttackaptconst() > 0) {
				attackapt = petAttr.getAttackaptconst();
			} else {
				attackapt = getApt(petAttr.getAttackaptmax(), petAttr.getAttackaptmin());
			}
		}
		if (attackapt >= 0) {
			petInfo.setBornattackapt(attackapt);
		}
	}

	/**
	 * 设置防御资质
	 */
	public void setBornDefendApt(int defendapt){
		if (null == petAttr || null == petInfo){
			Module.logger.warn("[CalcPetAttr.setBornDefendApt] 参数错误");
			return;
		}
		if (defendapt < 0) {
			if (petAttr.getDefendaptconst() > 0) {
				defendapt = petAttr.getDefendaptconst();
			} else {
				defendapt = getApt(petAttr.getDefendaptmax(), petAttr.getDefendaptmin());
			}
		}
		if (defendapt >= 0) {
			petInfo.setBorndefendapt(defendapt);
		}
	}
	
	/**
	 * 设置体力资质
	 */
	public void setBornPhyforceApt(int phyforceapt){
		if (null == petAttr || null ==petInfo){
			Module.logger.warn("[CalcPetAttr.setBornPhyforceApt] 参数错误");
			return;
		}
		if (phyforceapt < 0) {
			if (petAttr.getPhyforceaptconst() > 0) {
				phyforceapt = petAttr.getPhyforceaptconst();
			} else {
				phyforceapt = getApt(petAttr.getPhyforceaptmax(), petAttr.getPhyforceaptmin());
			}
		}
		if (phyforceapt >= 0) {
			petInfo.setBornphyforceapt(phyforceapt);
		}
	}
	
	/**
	 * 设置法力资质
	 */
	public void setBornMagicApt(int magicapt){
		if ( null == petAttr || null ==petInfo){
			Module.logger.warn("[CalcPetAttr.setBornMagicApt] 参数错误");
			return;
		}
		if (magicapt < 0) {
			if (petAttr.getMagicaptconst() > 0) {
				magicapt = petAttr.getMagicaptconst();
			} else {
				magicapt = getApt(petAttr.getMagicaptmax(), petAttr.getMagicaptmin());
			}
		}
		if (magicapt >= 0) {
			petInfo.setBornmagicapt(magicapt);
		}	
	}

	/**
	 * 设置速度资质
	 */
	public void setBornSpeedApt(int speedapt){
		if ( null == petAttr || null ==petInfo){
			Module.logger.warn("[CalcPetAttr.setBornSpeedApt] 参数错误");
			return;
		}
		if (speedapt < 0) {
			if (petAttr.getSpeedaptconst() > 0) {
				speedapt = petAttr.getSpeedaptconst();
			} else {
				speedapt = getApt(petAttr.getSpeedaptmax(), petAttr.getSpeedaptmin());
			}
		}
		if (speedapt >= 0) {
			petInfo.setBornspeedapt(speedapt);
		}
	}
	
	/**
	 * 设置躲闪资质
	 */
	public void setBornDodgeApt(int dodgeapt){
		if ( null == petAttr || null ==petInfo){
			Module.logger.warn("[CalcPetAttr.setBornDodgeApt] 参数错误 ");
			return;
		}
		if (dodgeapt < 0) {
			dodgeapt = getApt(0, 0);
		}
		if (dodgeapt >= 0) {
			petInfo.setBorndodgeapt(dodgeapt);
		}
	}

	/**
	 * 获得配置表的宠物初始寿命
	 */
	public int getLifeFromConfig() {
		if (null == petAttr) {
			return 0;
		}
		return petAttr.getLife();
	}

	/**
	 * 宠物的一级属性评分计算
	 */
	public static int doBaseScoreCalculate(Pet pet) {
		double score = 0.0;
		fire.pb.effect.Role epet = new fire.pb.effect.PetImpl(pet.getPetInfo());

		//一级属性评分
		int allPoint = epet.getAgi() + epet.getCons() + epet.getEndu() + epet.getIq() + epet.getStr();
		score += allPoint * fire.pb.scoremanager.Module.getInstance().getCoe(ScoreType.PET_LEVEL_SCORE);
		return (int)score;
	}

	public static double doCalculate(Pet pet) {

		double score = 0;

		fire.pb.effect.Role epet = new fire.pb.effect.PetImpl(pet.getPetInfo());

		Map<Integer, SPetAbilityScore> petScoreCnf = fire.pb.scoremanager.Module.getInstance().getPetAbility();
		for (SPetAbilityScore petScore : petScoreCnf.values()) {
			double attrValue = getCurrentAttrValue(pet, petScore.id, epet);
			score += attrValue * petScore.score;
		}

		// 一级属性评分
		int allPoint = epet.getAgi() + epet.getCons() + epet.getEndu() + epet.getIq() + epet.getStr();
		score += allPoint * fire.pb.scoremanager.Module.getInstance().getCoe(ScoreType.PET_LEVEL_SCORE);

		// 技能评分
		int skillScore = 0;
		for (int skillId : pet.getBattleskillIds()) {
			skillScore = skillScore + PetManager.getInstance().getSkillScore(skillId);
		}
		score = score + skillScore;

		// 成长评分
		int growRate = pet.getPetInfo().getGrowrate() - 952;
		if (growRate < 0)
			growRate = 0;
		score += growRate * fire.pb.scoremanager.Module.getInstance().getCoe(ScoreType.PET_GROWING) / 1000;

		return score;
	}

	private static double getCurrentAttrValue(Pet pet, int attributeId, fire.pb.effect.Role epet) {
		switch (attributeId) {
		case AttrType.PET_ATTACK_APT:
			return pet.getAttackapt();

		case AttrType.PET_DEFEND_APT:
			return pet.getDefendapt();

		case AttrType.PET_PHYFORCE_APT:
			return pet.getPhyforceapt();

		case AttrType.PET_SPEED_APT:
			return pet.getSpeedapt();

		case AttrType.PET_MAGIC_APT:
			return pet.getMagicapt();

		case AttrType.PET_DODGE_APT:
			return pet.getDodgeapt();

		case AttrType.ATTACK:
			return epet.getAttack();

		case AttrType.DEFEND:
			return epet.getDefend();

		case AttrType.MAX_HP:
			return epet.getMaxHp();

		case AttrType.MAGIC_ATTACK:
			return epet.getMagicAttack();

		case AttrType.SPEED:
			return epet.getSpeed();

		case AttrType.DODGE_RATE:
			return epet.getDodge();

		case AttrType.MAGIC_DEF:
			return epet.getMagicDef();

		case AttrType.PET_GROW_RATE:
			return ((double) pet.getGrowrate()) / 1000;

		case AttrType.PET_FIGHT_LEVEL:
			return pet.getUseLevel();

		default:
			break;
		}
		return 0;
	}

	/**
	 * 宠物升级时领悟技能
	 */
	public static void graspPekSkillWhileUplevel(final long roleId, final int petKey) {

		final fire.pb.pet.PetColumn petCol = new fire.pb.pet.PetColumn(roleId, PetColumnTypes.PET, false);
		xbean.PetInfo petInfo = petCol.getPetInfo(petKey);
		if (petInfo == null)
			return;

		Pet pet = Pet.getPet(petInfo);
		if (!pet.canLearnNewSkillWhileUpLevel())
			return;

		int graspSkillId = getGraspSkillID(pet.getBattleskillIds(), pet.getPetAttr());
		if (-1 == graspSkillId)
			return;
		boolean succ = petCol.addSkill(petKey, graspSkillId, 0, 0);
		if (!succ)
			return;

		fire.pb.pet.SRefreshPetSkill send = new fire.pb.pet.SRefreshPetSkill();
		send.petkey = petKey;
		pet.fillSRefreshPetSkill(send);
		gnet.link.Onlines.getInstance().send(roleId, send);

		List<String> param = new ArrayList<String>();
		param.add(petInfo.getName());
		FightSkillConfig skillConfig = fire.pb.skill.Module.getInstance().getFightSkillConfig(graspSkillId);
		if (null == skillConfig)
			return;
		String colorRgb = fire.pb.pet.Module.getInstance().getPetColorRGB(Pet.getClour(PetColour.WHITE));
		param.add(colorRgb);
		param.add(skillConfig.getSkillName());
		MessageMgr.psendMsgNotifyWhileCommit(roleId, 141705, param);

		// 领悟技能需要广播
		String rolename = xtable.Properties.selectRolename(roleId);
		List<String> params = MessageMgr.getStringList(rolename, pet.getPetAttr().getName(), colorRgb, skillConfig.getSkillName());
		SceneManager.psendAroundWhileCommit(roleId, MessageMgr.getMsgNotify(142722, 0, params));// 9屏
	}

	private static int getGraspSkillID(List<Integer> skills, PetAttr petAttr) {

		List<SkillRate> bornSkills = petAttr.getSkills();
		List<Integer> graspSkills = new ArrayList<Integer>();

		for (SkillRate rate : bornSkills) {
			if (skills.contains(rate.getSkillid()))
				continue;
			graspSkills.add(rate.getSkillid());
		}

		if (graspSkills.size() == 0)
			return -1;

		return graspSkills.get(Misc.getRandomBetween(0, graspSkills.size() - 1));
	}
}
