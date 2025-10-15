package fire.pb.pet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import xbean.PetInfo;
import fire.pb.DataInit;
import fire.pb.Petskill;
import fire.pb.attr.AttrType;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.course.CourseType;
import fire.pb.exception.NoSuchUniquePetException;
import fire.pb.item.SPetSkillSuccessConfig;
import fire.pb.ranklist.proc.PRankInsertPet;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.ranklist.proc.RankListManager;
import fire.pb.skill.SPetSkillupgrade;
import fire.pb.skill.SkillPet;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.util.StringConstant;

/**
 * 宠物的一些逻辑操作
 */
public class Pet {

	public static int PET_LOCK_ERROR_MSG = 142665;

	public static Pet getPet(xbean.PetInfo petInfo) {
		if (petInfo == null)
			return null;
		return new Pet(-1, -1, petInfo, true);
	}

	public static String getColorString(int color) {
		// return "";
		return "fffcfbfb"; // 弃用颜色 [2/25/2016 XGM]
	}

	public static Pet getPet(long roleId, PetColumn petColumn, int petKey,
			boolean readOnly) {
		try {
			if (petColumn == null)
				return null;
			xbean.PetInfo petInfo = petColumn.getPetInfo(petKey);
			if (petInfo == null)
				return null;
			return new Pet(roleId, petColumn.getPetColumnType(), petInfo, readOnly);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Pet getPet(long roleId, int petColumnType, int petKey,
			boolean readOnly) {
		try {
			PetColumn petColumn = new PetColumn(roleId, petColumnType, readOnly);
			xbean.PetInfo petInfo = petColumn.getPetInfo(petKey);
			if (petInfo == null)
				return null;
			return new Pet(roleId, petColumnType, petInfo, readOnly);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从宠物栏,宠物仓库,宠物回收站获取PetInfo
	 */
	public static Pet getPet(long uniquePetId, boolean readOnly) {

		IUniquePetWrap uniquePetWrap = null;
		try {
			uniquePetWrap = new UniquePetImpl(uniquePetId, readOnly);
		} catch (NoSuchUniquePetException e) {
			try {
				uniquePetWrap = new DiscardPetImpl(uniquePetId, readOnly);
			} catch (NoSuchUniquePetException e1) {
				return null;
			}
		}
		return uniquePetWrap.getPetInfo();
	}

	private final long roleId;

	private final int columnType;

	private final xbean.PetInfo petInfo;

	private final PetAttr petAttr;

	public Pet(long roleId, int columnType, PetInfo petInfo, boolean readonly) {

		this.roleId = roleId;
		this.columnType = columnType;
		this.petInfo = petInfo;
		if (petInfo == null) {
			throw new IllegalArgumentException(
					"ERROR: petInfo == null roleId = " + roleId);
		}

		this.petAttr = fire.pb.pet.Module.getInstance().getPetManager().getAttr(petInfo.getId());
		if (petAttr == null) {
			throw new IllegalArgumentException(
					"ERROR: petAttr == null petId = " + petInfo.getId()
							+ " roleId = " + roleId);
		}
	}

	public long getRoleId() {
		return roleId;
	}

	public int getColumnType() {
		return columnType;
	}

	public int getPetkey() {
		return petInfo.getKey();
	}

	public xbean.PetInfo getPetInfo() {
		return petInfo;
	}

	public xbean.PetInfo copyPetInfoBean() {
		return petInfo.copy();
	}

	public PetAttr getPetAttr() {
		return petAttr;
	}

	public int petSkillsGrid() {
		return 13;
	}

	public int getSkillMaxNum() {
		return petSkillsGrid();
	}

	public static int getClour(int starId) {
		return PetColour.WHITE;
	}

	/**
	 * 获取是否是珍品,1是珍品;0非珍品
	 */
	public int getTreasure() {
		return petInfo.getTreasure();
	}

	public int getBaseId() {
		return petInfo.getId();
	}

	public String getName() {
		return petInfo.getName();
	}

	public void setName(String newname) {
		petInfo.setName(newname);
	}

	public short getLevel() {
		return (short) petInfo.getLevel();
	}

	public void setLevel(short level) {
		petInfo.setLevel(level);
	}

	public int getUseLevel() {
		return petAttr.getUselevel();
	}

	public int getTakeLevel() {
		return petAttr.getTakelevel();
	}

	public int getSize() {
		return 0;
	}

	public int getColor() {
		return getClour(PetColour.WHITE);
	}

	public String getColorString() {
		return getColorString(getColor());
	}

	public int getPoint() {
		return petInfo.getPoint();
	}

	public void setPoint(int point) {
		petInfo.setPoint(point);
	}

	/**
	 * 使用 effect.PetImpl 操作 HP/MP
	 */
	public int getHp() {
		return petInfo.getHp();
	}

	public void setHp(int hp) {
		petInfo.setHp(hp);
	}

	public int getMp() {
		return petInfo.getMp();
	}

	public void setMp(int mp) {
		petInfo.setMp(mp);
	}

	public int getGrowrate() {
		return petInfo.getGrowrate();
	}

	public int getMaxLife() {
		return DataInit.PET_MAX_LIFE;
	}

	public int getLife() {
		return petInfo.getLife();
	}

	public void setLife(int life) {
		petInfo.setLife(life);
	}

	public int addLife(int addlife) {
		if(fire.pb.pet.Module.getInstance().getPetManager().isPetLifeForever(getPetAttr().getId())){
			return getLife();
		}
		
		setLife(Math.max(0,Math.min(DataInit.PET_MAX_LIFE, getLife() + addlife)));
		return getLife();
	}

	public long getExp() {
		return petInfo.getExp();
	}

	public void setExp(long exp) {
		petInfo.setExp(exp);
	}

	public int getKind() {
		return petInfo.getKind();
	}

	/**
	 * 初始的基础属性
	 */
	public xbean.BasicFightProperties getInitBfp() {
		return petInfo.getInitbfp();
	}

	/**
	 * 基础属性
	 */
	public xbean.BasicFightProperties getBfp() {
		return petInfo.getBfp();
	}

	/**
	 * 属性的效果
	 */
	public Map<Integer, Float> getEffects() {
		return petInfo.getEffects();
	}

	/**
	 * 属性的最终值
	 */
	public Map<Integer, Float> getFinalAttrs() {
		return petInfo.getFinalattrs();
	}

	/**
	 * 是否绑定
	 */
	public boolean isBind() {
		return petInfo.getIsbinded();
	}

	/**
	 * 设置绑定
	 */
	public void setBind(boolean isbind) {
		petInfo.setIsbinded(isbind);
	}

	/**
	 * 宠物唯一ID
	 */
	public long getUniqueId() {
		return petInfo.getUniqid();
	}

	/**
	 * 检测是否被锁,同时清除过期时间锁
	 * @return -1:没有锁 0:普通锁 >0:时间锁到期的时间
	 */
	public long isLocked() {
		if (petInfo.getIslocked()) {
			if (petInfo.getTimeout() > 0) {
				if (petInfo.getTimeout() < System.currentTimeMillis()) {
					// 已经超时
					// 清除过期时间锁
					petInfo.setTimeout(0);
					return 0;// 时间锁过期变成普通锁
				} else {
					return petInfo.getTimeout();// 时间锁到期时间
				}
			} else {
				return 0;// 上了锁
			}
		} else {
			return -1;// 没有锁
		}
	}

	/**
	 * 是否处于摆摊冻结期
	 * @return
	 */
	public boolean isMarketFreeze() {
		long t = petInfo.getMarketfreezeexpire();
		if (t > 0 && t > System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	/**
	 * 设置摆摊冻结期
	 * @param expireTime
	 */
	public void setMarketFreezeExpire(long expireTime) {
		petInfo.setMarketfreezeexpire(expireTime);
	}

	/**
	 * DB技能列表
	 */
	public List<xbean.PetSkill> getBattleskills() {
		return petInfo.getSkills();
	}

	/**
	 * 技能ID列表
	 */
	public List<Integer> getBattleskillIds() {
		List<Integer> skillIds = new LinkedList<Integer>();
		for (xbean.PetSkill skill : getBattleskills())
			skillIds.add(skill.getSkillid());
		return skillIds;
	}

	/**
	 * 是否有指定技能
	 */
	public boolean hasSkill(int skillId) {
		List<xbean.PetSkill> skills = getBattleskills();
		for (xbean.PetSkill skill : skills) {
			if (skill.getSkillid() == skillId
					/*|| (skillId == skill.getSkillid() / 100)*/ // 现在没有这种逻辑 [3/8/2016 XGM]
					) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获得技能等级
	 */
	public int getSkillLevel(int skillId) {
		List<xbean.PetSkill> skills = getBattleskills();
		SPetSkillupgrade config = PetManager.getInstance().getSkillUpGrade(skillId);
		if (config == null) {
			return 0;
		}
		int flag = config.getSign();
		for (xbean.PetSkill skill : skills) {
			SPetSkillupgrade temp = PetManager.getInstance().getSkillUpGrade(skill.getSkillid());
			if (temp == null) {
				continue;
			}
			if (temp.getSign() == flag) {
				return temp.getSkilllevel();
			}
		}
		return 0;
	}

	/**
	 * 是否有指定技能
	 */
	public boolean hasAnySkill(int skillId) {
		List<xbean.PetSkill> skills = getBattleskills();
		SPetSkillupgrade config = PetManager.getInstance().getSkillUpGrade(skillId);
		if (config == null) {
			return false;
		}
		int flag = config.getSign();
		for (xbean.PetSkill skill : skills) {
			SPetSkillupgrade temp = PetManager.getInstance().getSkillUpGrade(skill.getSkillid());
			if (temp == null) {
				continue;
			}
			if (temp.getSign() == flag) {
				return true;
			}
		}
		return false;
	}

	/**
	 * auto skillID
	 */
	public int getAutoSkillId() {
		List<xbean.PetSkill> skills = getBattleskills();
		for (xbean.PetSkill skill : skills) {
			FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skill.getSkillid());
			if (sconf != null && sconf.getAddautoSkill() == 1) {
				return skill.getSkillid();
			}
		}
		return -1;
	}

	/**
	 * 添加技能
	 */
	public boolean addSkill(int skillId, long expireTime, int skillExp, int skillType) {
		int maxSkillNum = getSkillMaxNum();
		if (skillType != 2) { // 2=被认证出的技能(需要留一个技能位给被认证出的技能)
			maxSkillNum -= 1;
		}
		List<xbean.PetSkill> skills = getBattleskills();
		if (skills.size() >= maxSkillNum)
			return false;

		if (hasAnySkill(skillId)) {
			return false;
		}

		xbean.PetSkill newSkill = xbean.Pod.newPetSkill();
		newSkill.setSkillid(skillId);
		newSkill.setSkilltype(skillType);
		skills.add(newSkill);
		updatePetScoreWhileChange();
		updatePetSkillChange();
		// 更新宠物历程信息
		if (petInfo.getLevel() >= 60) {
			fire.pb.course.CourseManager.achieveCourse(roleId, CourseType.HAVE_PET_COURSE, skillId, 0);
		}
		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.PET_SCORE, getPetInfo().getPetscore());
		return true;
	}

	/**
	 * 插入/替换指定位置的技能
	 */
	public int insertSkill(int index, int skillId, long expiretime, int skillType) {
		int maxSkillNum = getSkillMaxNum();
		List<xbean.PetSkill> skills = getBattleskills();
		int size = skills.size();
		if (size > maxSkillNum)
			return -1;
		if (index + 1 > size)
			return -2;// 越界了
		for (xbean.PetSkill skill : skills) {
			if (skill.getSkillid() == skillId)
				return -3;
		}
		xbean.PetSkill newSkill = xbean.Pod.newPetSkill();
		newSkill.setSkillid(skillId);
		newSkill.setSkilltype(skillType);
		xbean.PetSkill oldSkill = skills.set(index, newSkill);
		updatePetScoreWhileChange();
		updatePetSkillChange();
		// 更新宠物历程信息
		if (petInfo.getLevel() >= 60) {
			fire.pb.course.CourseManager.achieveCourse(roleId, CourseType.HAVE_PET_COURSE, skillId, 0);
		}
		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.PET_SCORE, getPetInfo().getPetscore());
		return oldSkill.getSkillid();
	}

	/**
	 * 删除技能
	 */
	public boolean removeSkillById(int skillId) {
		List<xbean.PetSkill> skills = getBattleskills();
		for (Iterator<xbean.PetSkill> it = skills.iterator(); it.hasNext();) {
			xbean.PetSkill skill = it.next();
			if (skill.getSkillid() == skillId) {
				it.remove();
				updatePetScoreWhileChange();
				updatePetSkillChange();
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否战斗中
	 */
	public static boolean isInBattle(final long roleId, final int petKey) {
		final fire.pb.pet.PetColumn petCol = new fire.pb.pet.PetColumn(roleId, PetColumnTypes.PET, true);
		if (petCol.getPetInfo(petKey) == null)
			return false;
		final fire.pb.buff.BuffAgent buffAgent = new fire.pb.buff.BuffRoleImpl(roleId, true);
		if (buffAgent.existState(BuffConstant.StateType.STATE_BATTLE_FIGHTER)
				|| buffAgent.existState(BuffConstant.StateType.STATE_BATTLE_WATCHER)
				|| buffAgent.existState(BuffConstant.StateType.STATE_REPLAY))
			return false;
		return true;
	}

	public static int getPetUseLevelConfig(int baseId) {
		PetAttr petAttr = fire.pb.pet.Module.getInstance().getPetManager().getAttr(baseId);
		if (petAttr == null)
			return 0;
		return petAttr.getUselevel();
	}

	/**
	 * 升级时能否领悟技能
	 */
	public boolean canLearnNewSkillWhileUpLevel() {
		if (Math.random() < getLearnSkillProbWhileUpLevel())
			return true;
		else
			return false;
	}

	/**
	 * 升级时领悟技能几率
	 */
	public float getLearnSkillProbWhileUpLevel() {
		int skillNum = getBattleskills().size();
		if (skillNum >= getSkillMaxNum())
			return 0f;
		if (skillNum >= 3)
			return 0f;
		java.util.Map<Integer, SPetSkillSuccessConfig> configMap = fire.pb.main.ConfigManager.getInstance().getConf(SPetSkillSuccessConfig.class);
		if (null == configMap)
			return 0f;

		SPetSkillSuccessConfig conf = configMap.get(skillNum);
		if (conf == null) {
			return 0f;// 4技能或更多时，不能通过升级来领悟技能
		}
		float value = (float) ((double) conf.getOdds() / conf.getBasenumber());
		return value;
	}

	/**
	 * 攻击资质
	 */
	public int getAttackapt() {
		return petInfo.getBornattackapt();
	}

	/**
	 * 防御资质
	 */
	public int getDefendapt() {
		return petInfo.getBorndefendapt();
	}

	/**
	 * 体力资质
	 */
	public int getPhyforceapt() {
		return petInfo.getBornphyforceapt();
	}

	/**
	 * 法力资质
	 */
	public int getMagicapt() {
		return petInfo.getBornmagicapt();
	}

	/**
	 * 速度资质
	 */
	public int getSpeedapt() {
		return petInfo.getBornspeedapt();
	}

	/**
	 * 闪躲资质
	 */
	public int getDodgeapt() {
		return petInfo.getBorndodgeapt();
	}

	/**
	 * 资质文字
	 */
	public String getAptitudeStringByAttrType(int attrType, boolean isHaveSuffix) {
		String ret = "";
		switch (attrType) {
		case AttrType.PET_ATTACK_APT:
			ret = StringConstant.ID2String("攻击", StringConstant.ATTACK);
			break;
		case AttrType.PET_DEFEND_APT:
			ret = StringConstant.ID2String("防御", StringConstant.DEFEND);
			break;
		case AttrType.PET_MAGIC_APT:
			ret = StringConstant.ID2String("法术", StringConstant.MAGIC);
			break;
		case AttrType.PET_PHYFORCE_APT:
			ret = StringConstant.ID2String("体力", StringConstant.PHYFORCE);
			break;
		case AttrType.PET_SPEED_APT:
			ret = StringConstant.ID2String("速度", StringConstant.SPEED);
			break;
		default:
			break;
		}
		if (isHaveSuffix == true && ret.length() > 0) {
			ret += StringConstant.ID2String("资质", StringConstant.APTITUDE);
		}
		return ret;
	}

	/**
	 * 当前展示技能
	 */
	public xbean.PetSkill getShowSkill(List<xbean.PetSkill> allSkills) {
		for (xbean.PetSkill petSkill : allSkills) {
			if (petSkill != null) {
				return petSkill;
			}
		}
		return null;
	}

	ConstantlyBuff getShowSkillBuff(List<xbean.PetSkill> allSkills) {
		xbean.PetSkill showSkill = getShowSkill(allSkills);
		if (showSkill == null)
			return null;
		long expireTime = -1; // 弃用技能到期时间 [2/24/2016 XGM]
		if (expireTime <= 0)
			return null;
		long now = System.currentTimeMillis();
		if (expireTime < now)
			return null;
		ConstantlyBuff cBuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(500214);
		cBuff.setTime(expireTime - now);
		return cBuff;
	}

	public fire.pb.Pet getProtocolPet() {
		final fire.pb.Pet pet = new fire.pb.Pet();
		pet.id = getBaseId();
		pet.ownername = "";
		pet.key = petInfo.getKey();
		pet.name = getName();
		pet.level = getLevel();
		pet.uselevel = getPetUseLevelConfig(pet.id);
		pet.gengu = 0;
		pet.initbfp.agi = (short) getInitBfp().getAgi();
		pet.initbfp.cons = (short) getInitBfp().getCons();
		pet.initbfp.endu = (short) getInitBfp().getEndu();
		pet.initbfp.iq = (short) getInitBfp().getIq();
		pet.initbfp.str = (short) getInitBfp().getStr();
		pet.bfp.agi = (short) getBfp().getAgi();
		pet.bfp.cons = (short) getBfp().getCons();
		pet.bfp.endu = (short) getBfp().getEndu();
		pet.bfp.iq = (short) getBfp().getIq();
		pet.bfp.str = (short) getBfp().getStr();
		pet.point = (short) getPetInfo().getPoint();
		pet.autoaddcons = (byte) getPetInfo().getAutoaddcons();
		pet.autoaddiq = (byte) getPetInfo().getAutoaddiq();
		pet.autoaddstr = (byte) getPetInfo().getAutoaddstr();
		pet.autoaddendu = (byte) getPetInfo().getAutoaddendu();
		pet.autoaddagi = (byte) getPetInfo().getAutoaddagi();
		pet.pointresetcount = (short) getPetInfo().getPointresetcount();
		pet.exp = getExp();

		pet.attackapt = getAttackapt();
		pet.defendapt = getDefendapt();
		pet.phyforceapt = getPhyforceapt();
		pet.magicapt = getMagicapt();
		pet.speedapt = getSpeedapt();
		pet.dodgeapt = getDodgeapt();

		pet.growrate = (float) petInfo.getGrowrate();
		pet.life = getLife();
		pet.kind = getKind();
		pet.scale = (byte) getSize();

		fire.pb.effect.Role epet = new fire.pb.effect.PetImpl(getPetInfo());
		pet.maxhp = epet.getMaxHp();
		pet.maxmp = epet.getMaxMp();
		pet.hp = epet.getHp();
		pet.mp = epet.getMp();
		pet.attack = epet.getAttack();
		pet.defend = epet.getDefend();
		pet.speed = epet.getSpeed();
		pet.magicattack = epet.getMagicAttack(); // 灵力
		pet.magicdef = epet.getMagicDef();
		pet.nexp = epet.getNextExp();
		List<xbean.PetSkill> allSkills = getBattleskills();
		for (xbean.PetSkill skill : allSkills) {
			Petskill petSkill = new Petskill();
			petSkill.skillid = skill.getSkillid();
			petSkill.skillexp = 0;
			petSkill.skilltype = (byte) skill.getSkilltype();
			petSkill.certification = (byte) skill.getCertification();
			pet.skills.add(petSkill);
		}

		if (isLocked() != -1)
			pet.flag = (byte) (pet.flag | fire.pb.Pet.FLAG_LOCK);
		if (isBind())
			pet.flag = (byte) (pet.flag | fire.pb.Pet.FLAG_BIND);

		pet.timeout = petInfo.getTimeout();
		pet.ownerid = petInfo.getOwnerid();
		pet.ownername = xtable.Properties.selectRolename(petInfo.getOwnerid());
		pet.rank = getPetRankListByUid(petInfo.getUniqid());
		pet.starid = (short) PetColour.WHITE;
		pet.practisetimes = (short) 0;
		pet.changegengu = 0;
		pet.skill_grids = petSkillsGrid();
		pet.aptaddcount = (byte) petInfo.getAptaddcount();
		pet.growrateaddcount = (byte) petInfo.getGrowrateaddcount();
		pet.washcount = (short) petInfo.getWashcount();

		pet.petscore = petInfo.getPetscore();
		pet.petbasescore = petInfo.getPetbasescore();
		pet.petdye1 = petInfo.getPetdye1();
		pet.petdye2 = petInfo.getPetdye2();
		pet.shenshouinccount = petInfo.getShenshouinccount();
		pet.marketfreezeexpire = petInfo.getMarketfreezeexpire();
		return pet;
	}

	public static int getPetRankListByUid(long uId) {
		int rankId = RankListManager.getPetRankidByUniqueID(uId);
		return rankId < 0 ? 0 : rankId;
	}

	public fire.msp.showpetinfo getShowPetInfo() {
		int skillEffect = 0;
		xbean.PetSkill showSkill = getShowSkill(getBattleskills());
		if (showSkill != null)
			skillEffect = getShowSkillEffect(showSkill.getSkillid());
		return new fire.msp.showpetinfo(getBaseId(), getPetkey(), getName(), getColor(), getSize(), skillEffect);
	}

	public int getShowSkillEffect(int showskillid) {
		return 0;
	}

	/**
	 * 填充技能协议
	 */
	public void fillSRefreshPetSkill(SRefreshPetSkill send) {
		for (xbean.PetSkill skill : getBattleskills()) {
			Petskill petSkill = new Petskill();
			petSkill.skillexp = 0;
			petSkill.skillid = skill.getSkillid();
			petSkill.skilltype = (byte) skill.getSkilltype();
			petSkill.certification = (byte) skill.getCertification();
			send.skills.add(petSkill);
		}
	}

	/**
	 * 上线处理 
	 */
	public void online() {
		int petHp = petInfo.getHp();
		int petMp = petInfo.getMp();

		offline();// 先强制下线清理，再重新处理上线
		SkillPet skillPet = new SkillPet(petInfo, roleId);

		skillPet.addSkillBuffWhileOnline(null);
		if (petHp <= 0)
			petHp = 1;

		petInfo.setHp(petHp);
		petInfo.setMp(petMp);
	}

	/**
	 * 下线处理
	 */
	public void offline() {
		petInfo.getBuffpet().getBuffs().clear();
		petInfo.getEffects().clear();
		petInfo.getFinalattrs().clear();
	}

	/**
	 * 计算宠物的评分
	 */
	public int calculatePetScore() {
		return (int) CalcPetAttr.doCalculate(this);
	}

	/**
	 * 计算宠物的一级属性评分
	 */
	public int calculatePetBaseScore() {
		return (int) CalcPetAttr.doBaseScoreCalculate(this);
	}

	/**
	 * 更新宠物评分
	 */
	public void updatePetScoreWhileChange() {
		int currentScore = petInfo.getPetscore();
		int newScore = calculatePetScore();
		int baseScore = calculatePetBaseScore();
		if (currentScore == newScore)
			return;

		petInfo.setPetscore(newScore);
		
		petInfo.setPetbasescore(baseScore);
		if (((newScore - baseScore) >= petAttr.treasureScore)
				&& (petInfo.getSkills().size() >= petAttr.treasureskillnums)) {
			petInfo.setTreasure(1);
		} else {
			petInfo.setTreasure(0);
		}

		SRefreshPetScore send = new SRefreshPetScore();
		send.petkey = getPetkey();
		send.petscore = newScore;
		send.petbasescore = baseScore;
		mkdb.Procedure.psendWhileCommit(petInfo.getOwnerid(), send);

		mkdb.Procedure.pexecuteWhileCommit(new PRankInsertPet(petInfo.getUniqid(), true));

		mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(petInfo.getOwnerid()));
	}

	public void updatePetSkillChange() {
		// roleid == -1 || petkey == 0 则表示还没进宠物栏,是在创建中
		if (getRoleId() != -1 && getPetkey() > 0) {
			fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.PetSkillChangeEvent(getRoleId(), getPetkey()));
		}
	}
}
