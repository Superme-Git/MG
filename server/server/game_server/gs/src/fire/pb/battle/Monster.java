package fire.pb.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.pb.attr.AttrType;
import fire.pb.attr.EffectType;
import fire.pb.attr.FightAttrType;
import fire.pb.battle.ai.MonsterAIManager;
import fire.pb.battle.ai.MonsterBattleAI;
import fire.pb.effect.Module;
import fire.pb.effect.PetImpl;
import fire.pb.effect.RoleImpl;
import fire.pb.map.SMineArea;
import fire.pb.map.SShowAreainfo;
import fire.pb.npc.MonsterConfig;
import fire.pb.npc.MonsterExtraAttrConfig;
import fire.pb.npc.SMonsterConfig;
import fire.pb.npc.SMonsterSkill;
import fire.pb.npc.SWildMonsterConfig;
import fire.pb.pet.Pet;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.skill.SkillAgent;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.SkillMonster;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.util.MathUtil;
import fire.pb.util.Misc;
import fire.pb.util.Parser;
import fire.pb.util.StringConstant;


public class Monster
{
	private int index;
	private int monsterId;
	private int shape = 0;
	private long key = 0;
	private int level = -1;
	private String name="";
	private int fighterId = 0;//指定的fighterId
	private double bonus = 1;//属性及技能等级修正
	private xbean.Monster monsterbean = null;
	
	public static int MONSTER_WILD = 1;//普通野生怪
	public static int MONSTER_CHIEF = 2;//头领怪
	public static int MONSTER_BABY = 3;//宝宝怪
	public static int MONSTER_BOSS = 4;//boss怪
	public static int MONSTER_CLANBOSS = 5;//公会boss怪

	public final static int ADD_POINT_CONS = 0;
	public final static int ADD_POINT_IQ = 1;
	public final static int ADD_POINT_STR = 2;
	public final static int ADD_POINT_AGI = 3;
	public final static int ADD_POINT_ENDU = 4;
	
	public Monster(xbean.Monster monsterbean)
	{
		this.monsterbean = monsterbean;
		this.monsterId = monsterbean.getMonsterid();
	}
	
	public Monster(int monsterId)
	{
		this.monsterId = monsterId;
	}
	
	public Monster(int monsterId,int level)
	{
		this.monsterId = monsterId;
		this.level = level;
	}
	
	public Monster(int monsterId, int level, int shape)
	{
		this.monsterId = monsterId;
		this.shape = shape;
		this.level = level;
	}
	public Monster(int monsterId, int level, int shape,String name)
	{
		this.monsterId = monsterId;
		this.shape = shape;
		this.level = level;
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}


	public int getMonsterId()
	{
		return monsterId;
	}

	public void setMonsterId(int monsterId)
	{
		this.monsterId = monsterId;
	}

	public int getShape()
	{
		return shape;
	}

	public void setShape(int shape)
	{
		this.shape = shape;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	public int getFighterId()
	{
		return fighterId;
	}

	public void setFighterId(int fighterId)
	{
		this.fighterId = fighterId;
	}
	
	public void setBouns(double bonus)
	{
		this.bonus = bonus;
	}
	
	public double getBonus()
	{
		return this.bonus;
	}

	/**返回monsterbean*/
	public xbean.Monster getMonsterbean(final xbean.BattleInfo battle)
	{
		if(monsterbean == null)
			monsterbean = createMonster(this,battle);
		return monsterbean;
	}

	/**使用Monster构造xbean.Monster*/
	public static  xbean.Monster createMonster(Monster monster,final xbean.BattleInfo battle)
	{
		xbean.Monster monsterbean = createMonster(monster.getMonsterId(),monster.getLevel(),battle,monster.getBonus());
		if(monster.getShape() > 0)
			monsterbean.setShape(monster.getShape());
		return monsterbean;
	}
	
	/**使用表内配的级别和造型构造xbean.Monster*/
	public static xbean.Monster createMonster(int monsterId,final xbean.BattleInfo battle)
	{
		return createMonster(monsterId,-1,battle,1);
	}
	
	public static xbean.Monster createMonster(int monsterId,int level,final xbean.BattleInfo battle, double bonus)
	{
		return createMonster(monsterId, level, battle, bonus, new HashMap<Integer, Float>(), 0);
	}
	/**使用表内配的造型构造xbean.Monster*/
	public static xbean.Monster createMonster(int monsterId,int level,final xbean.BattleInfo battle, double bonus, Map<Integer,Float> fixedL2attrs, int color)
	{
		int monstertype = monsterId / 1000;
		switch (monstertype)
		{
		case 20:
		case 21:
		case 22:
		case 23:
			return createWildMonster(monsterId,level,battle,fixedL2attrs);
		case 24:
		case 25:
		case 26:
		case 27:
		case 28:
		case 29:
		case 30:
		case 31:
		case 32:
		case 33:
		case 34:
		case 35:
		case 36:
		case 37:
		case 38:
		case 39:
		case 40:
			return createNpcMonster(monsterId, level, battle, bonus, fixedL2attrs, null, color);
		default:
			return null;
		}
	}
	/**构造xbean.Monster*/
	public static xbean.Monster createMonster(int monsterId,int level ,int shape,final xbean.BattleInfo battle)
	{
		xbean.Monster monsterbean = createMonster(monsterId,level,battle,1); 
		if(shape > 0)
			monsterbean.setShape(shape);
		return monsterbean;
	}
	
	/**创建NPC类型的怪物*/
	static xbean.Monster createNpcMonster(int monsterId,int level,final xbean.BattleInfo battle, double bonus, Map<Integer,Float> fixedL2attrs, xbean.HuoBanshuxing huobanInfo/*XiaKeInfo xiaKeInfo*/, int color)
	{
		if(fixedL2attrs == null)
			fixedL2attrs = new HashMap<Integer, Float>();
		SMonsterConfig monsterConfig =  getSMonsterConfig(monsterId);
		xbean.Monster monster = xbean.Pod.newMonster();
		monster.setMonsterid(monsterId);
		
		//高位存颜色  地位存type
		int type = (color<<16) + monsterConfig.getFightnpctype();
		monster.setFightnpctype(type);
		monster.setMonstertype(monsterConfig.getMonstertype()); //设置这个怪物的类型 by changhao
		monster.setSpecialtype(monsterConfig.getSpecialtype()); //设置这个怪物的特别类型 by liuxinhua
		monster.setSchool(monsterConfig.getSchool()); //设置这个怪物的职业 by liuxinhua
		//对于怪物的造型，优先级顺序是: 外部传入 > 关联宠物造型 > 造型随机 > 造型ID
		monster.setShape(0);
		if (monsterConfig.getPet() != 0)
		{
			PetAttr petAttr =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.pet.PetAttr.class)
					.get(monsterConfig.getPet());
			if (petAttr == null)
				return null;
			monster.setShape(petAttr.getShape());
		}
		//造型随机
		else if(monsterConfig.getRandomShapes()!= null)
		{
			try
			{
				String[] strs = monsterConfig.getRandomShapes().split(";");
				if(strs.length > 0)
				{
					Integer[] randomShapes = new Integer[strs.length];
					for (int i = 0; i < strs.length; i++)
						randomShapes[i] = Float.valueOf(strs[i]).intValue();
					int random = Misc.getRandomBetween(0, randomShapes.length - 1);
					monster.setShape(randomShapes[random]);
				}
			}
			catch(Exception e)
			{
				BattleField.logger.error("怪物随机造型出错", e);
			}
		}
		else if(monsterConfig.getShape() != 0)
		{
			monster.setShape(monsterConfig.getShape());
		}
		
		if(level <= 0)// 设置等级
		{
			if(battle != null)//如果battle为null，则必定为固定等级
			{
				int leveltype = monsterConfig.getLevelType();
				switch (leveltype)
				{
				case 1:
					monster.setLevel(BattleField.getLeaderLevel(battle, true));
					break;
				case 2:
					monster.setLevel(BattleField.getTeamLevelWithWeight(battle, true));
					break;
				case 3:
					monster.setLevel(BattleField.getTeamMaxeLevel(battle, true));
					break;
				case 4:
					monster.setLevel(monsterConfig.getLevel());
					break;
				case 5:
					int arealevel = 1;
					if (battle.getAreaconf() != 0)
					{
						if (battle.getAreatype() == xbean.BattleInfo.AREA_BATTLEHIDE)
						{
							SMineArea areaInfo = fire.pb.main.ConfigManager.getInstance().getConf(
									fire.pb.map.SMineArea.class).get(battle.getAreaconf());
							if (areaInfo != null)
								arealevel = areaInfo.getLevel();
						} else if (battle.getAreatype() == xbean.BattleInfo.AREA_BATTLESHOW)
						{
							SShowAreainfo areaInfo = fire.pb.main.ConfigManager.getInstance().getConf(
									fire.pb.map.SShowAreainfo.class).get(battle.getAreaconf());
							if (areaInfo != null)
								arealevel = areaInfo.getTypelevel();
						}
					}
					monster.setLevel(arealevel);
					break;
				default:
					monster.setLevel(1);
					break;
				}

				if (leveltype != 4) {
					monster.setLevel(MathUtil.fixValueRange(monster.getLevel(), monsterConfig.getMinlevellimit(), monsterConfig.getMaxlevellimit()));
				}
			}
			else
			{
				monster.setLevel(monsterConfig.getLevel());
			}
		}
		else
		{
			monster.setLevel(level);
		}

		if(monster.getLevel() < 0)
			monster.setLevel(1);
		
		monster.setBodysize((float)monsterConfig.bodytype);
		
		//生成技能
		genNpcMonsterSkills(monsterConfig.skills, monster, bonus, huobanInfo);
		
		SkillAgent sagent = new SkillMonster(monster, 0);
		sagent.addSkillBuffWhileOnline(battle);
		
		// 构建Monster的ERole
		fire.pb.effect.Role emonster = new fire.pb.effect.MonsterImpl(monster);
		
		//先计算一遍属性，因为这种方法生成的怪属性，其一级属性和资质都为0，会生成出2级属性为负的怪，需要补齐
		emonster.updateAllFinalAttrs();// 设置最终属性
		
		int maxattack = BattleField.getHostRoleMaxAttack(battle);
		// 添加效果
		Float value = fixedL2attrs.get(AttrType.MAX_HP);
		if(value == null){
			emonster.attachEffect(EffectType.MAX_HP_ABL, (float)((monsterConfig.getHpMaxFactor() * monster.getLevel() + monsterConfig.getHpMaxAttackFactor() * maxattack + monsterConfig.getHpMaxConstant())*bonus /*- emonster.getMaxHp()*/));
		}else{
			emonster.attachEffect(EffectType.MAX_HP_ABL, (float)(value - emonster.getMaxHp()));
		}
		
		value = fixedL2attrs.get(AttrType.MAX_MP);
		if(value == null){
			emonster.attachEffect(EffectType.MAX_MP_ABL, (float)((monsterConfig.getMpMaxFactor() * monster.getLevel() + monsterConfig.getMpMaxConstant())*bonus /*- emonster.getMaxMp()*/));
		}else{
			emonster.attachEffect(EffectType.MAX_MP_ABL, (float)(value - emonster.getMaxMp()));
		}
		
		value = fixedL2attrs.get(AttrType.MAX_SP);
		if(value == null){
			emonster.attachEffect(EffectType.MAX_SP_ABL, monsterConfig.getMaxSp());
		}else{
			emonster.attachEffect(EffectType.MAX_SP_ABL, (float)(value - emonster.getMaxSp()));
		}
		
		value = fixedL2attrs.get(AttrType.DEFEND);
		if(value == null){
			emonster.attachEffect(EffectType.DEFEND_ABL, (float)((monsterConfig.getDefFactor() * monster.getLevel() + monsterConfig.getDefConstant())*bonus /*- emonster.getDefend()*/));
		}else{
			emonster.attachEffect(EffectType.DEFEND_ABL,(float)(value - emonster.getDefend()));
		}
		
		value = fixedL2attrs.get(AttrType.MAGIC_ATTACK);
		if(value == null){
			emonster.attachEffect(EffectType.MAGIC_ATTACK_ABL, (float)((monsterConfig.getMagicattFactor() * monster.getLevel() + monsterConfig.getMagicattConstant())*bonus /*- emonster.getMagicAttack()*/));
		}else{
			emonster.attachEffect(EffectType.MAGIC_ATTACK_ABL,(float)(value - emonster.getMagicAttack()));
		}
		
		value = fixedL2attrs.get(AttrType.MAGIC_DEF);
		if(value == null){
			emonster.attachEffect(EffectType.MAGIC_DEF_ABL, (float)((monsterConfig.getMagicDefFactor() * monster.getLevel() + monsterConfig.getMagicDefConstant())*bonus /*- emonster.getMagicDef()*/));
		}else{
			emonster.attachEffect(EffectType.MAGIC_DEF_ABL,(float)(value - emonster.getMagicDef()));
		}
		
		value = fixedL2attrs.get(AttrType.HIT_RATE);
		if(value == null){
			emonster.attachEffect(EffectType.HIT_RATE_ABL, (float)((monsterConfig.getAttallFactor() * monster.getLevel() + monsterConfig.getAttallConstant())*bonus /*- emonster.getHit()*/));
		}else{
			emonster.attachEffect(EffectType.HIT_RATE_ABL,(float)(value - emonster.getHit()));
		}
		
		value = fixedL2attrs.get(AttrType.DODGE_RATE);
		if(value == null){
			emonster.attachEffect(EffectType.DODGE_RATE_ABL, (float)((0 * monster.getLevel() + 0)*bonus /*- emonster.getDodge()*/));
		}else{
			emonster.attachEffect(EffectType.DODGE_RATE_ABL,(float)(value - emonster.getDodge()));
		}
		
		value = fixedL2attrs.get(AttrType.SPEED);
		if(value == null){
			emonster.attachEffect(EffectType.SPEED_ABL, (float)((monsterConfig.getSpeedFactor() * monster.getLevel() + monsterConfig.getSpeedConstant())*bonus /*- emonster.getSpeed()*/));
		}else{
			emonster.attachEffect(EffectType.SPEED_ABL,(float)(value - emonster.getSpeed()));
		}

		value = fixedL2attrs.get(AttrType.MEDICAL);
		if(value == null){
			emonster.attachEffect(EffectType.MEDICAL_ABL, (float)((monsterConfig.getMedicalFactor() * monster.getLevel() + monsterConfig.getMedicalConstant())*bonus /*- emonster.getAttrById(AttrType.MEDICAL)*/));
		}else{
			emonster.attachEffect(EffectType.MEDICAL_ABL,(float)(value - emonster.getAttrById(AttrType.MEDICAL)));
		}

		value = fixedL2attrs.get(AttrType.SEAL);
		if(value == null){
			emonster.attachEffect(EffectType.SEAL_ABL, (float)((monsterConfig.getSealhitFactor() * monster.getLevel() + monsterConfig.getSealhitConstant())*bonus /*- emonster.getAttrById(AttrType.SEAL)*/));
		}else{
			emonster.attachEffect(EffectType.SEAL_ABL,(float)(value - emonster.getAttrById(AttrType.SEAL)));
		}

		value = fixedL2attrs.get(AttrType.UNSEAL);
		if(value == null){
			emonster.attachEffect(EffectType.UNSEAL_ABL, (float)((monsterConfig.getUnsealFactor() * monster.getLevel() + monsterConfig.getUnsealConstant())*bonus /*- emonster.getAttrById(AttrType.UNSEAL)*/));
		}else{
			emonster.attachEffect(EffectType.UNSEAL_ABL,(float)(value - emonster.getAttrById(AttrType.UNSEAL)));
		}

		//再计算一遍属性，生成外功伤害
		emonster.updateAllFinalAttrs();// 设置最终属性

		value = fixedL2attrs.get(AttrType.ATTACK);
		if(value == null){
			emonster.attachEffect(EffectType.DAMAGE_ABL, (float)((monsterConfig.getAttackFactor() * monster.getLevel() + monsterConfig.getAttackConstant())*bonus /*- emonster.getAttack()*/));
		}else{
			emonster.attachEffect(EffectType.DAMAGE_ABL,(float)(value - emonster.getAttack()));
		}
		
		fillExtraAttrs(monsterConfig,fixedL2attrs, emonster, bonus);
		//免疫buff
		try{
			if(monsterConfig.getImmunebuffid() != null && !monsterConfig.getImmunebuffid().equals(""))
			{
				String[] strs = monsterConfig.getImmunebuffid().split(";");
				for(String str :strs)
					monster.getImmunebuffs().add(Float.valueOf(str).intValue());
			}
		}catch(Exception e){
			BattleField.logger.error(e);
		}
		
		emonster.updateAllFinalAttrs();// 设置最终属性
		emonster.fullHp();
		emonster.fullMp();// 补满MP
		emonster.setSp(monsterConfig.getDefaultBattleSp());
		//初始化怪物AI
		initMonsterAI(monster, huobanInfo, battle);
		// 设置怪物基本prop完毕，剩余buffAgent没有设置
		
		// 打印属性
		printFinalAttrs(monster, monsterConfig.getName());
		return monster;
	}
	
	/**创建Huoban类型的怪物*/
	public static xbean.Monster createHuobanMonster(int monsterId, int level,
			final xbean.BattleInfo battle, double bonus,
			Map<Integer, Float> fixedL2attrs, xbean.HuoBanshuxing huobanInfo, int color) {
		if (fixedL2attrs == null)
			fixedL2attrs = new HashMap<Integer, Float>();
		
		if (huobanInfo == null)
			return null;
		
		xbean.Monster monster = xbean.Pod.newMonster();
		monster.setMonsterid(monsterId);

		// 高位存颜色 地位存type
		int type = (color << 16) + 3; // 助战
		monster.setFightnpctype(type);
		monster.setMonstertype(0); 
		monster.setSpecialtype(0);
		monster.setSchool(huobanInfo.getSchool()); // 设置这个怪物的职业 by liuxinhua
		// 对于怪物的造型，优先级顺序是: 外部传入 > 关联宠物造型 > 造型随机 > 造型ID
		monster.setShape(0);
		if (huobanInfo.getShape() != 0) {
			monster.setShape(huobanInfo.getShape());
		}

		if (level <= 0)// 设置等级
		{
			if (battle != null)// 如果battle为null，则必定为固定等级
			{
				monster.setLevel(BattleField.getLeaderLevel(battle, true));
			} else {
				monster.setLevel(huobanInfo.getLevel());
			}
		} else {
			monster.setLevel(level);
		}

		if (monster.getLevel() < 0)
			monster.setLevel(1);

		monster.setBodysize((float) huobanInfo.getBodytype());

		// 生成技能
		List<SMonsterSkill> mSkill = new ArrayList<SMonsterSkill>();
		genNpcMonsterSkills(mSkill, monster, bonus, huobanInfo);

		SkillAgent sagent = new SkillMonster(monster, 0);
		sagent.addSkillBuffWhileOnline(battle);

		// 构建Monster的ERole
		fire.pb.effect.Role emonster = new fire.pb.effect.MonsterImpl(monster);

		// 先计算一遍属性，因为这种方法生成的怪属性，其一级属性和资质都为0，会生成出2级属性为负的怪，需要补齐
		emonster.updateAllFinalAttrs();// 设置最终属性

		int maxattack = BattleField.getHostRoleMaxAttack(battle);
		// 添加效果
		Float value = fixedL2attrs.get(AttrType.MAX_HP);
		if (value == null) {
//			emonster.attachEffect(
//					EffectType.MAX_HP_ABL, (float) ((monsterConfig.getHpMaxFactor()	* monster.getLevel()
//							+ monsterConfig.getHpMaxAttackFactor() * maxattack + monsterConfig
//							.getHpMaxConstant()) * bonus /*- emonster.getMaxHp()*/));
		} else {
			emonster.attachEffect(EffectType.MAX_HP_ABL,
					(float) (value - emonster.getMaxHp()));
		}

		value = fixedL2attrs.get(AttrType.MAX_MP);
		if (value == null) {
//			emonster.attachEffect(
//					EffectType.MAX_MP_ABL,
//					(float) ((monsterConfig.getMpMaxFactor()
//							* monster.getLevel() + monsterConfig
//							.getMpMaxConstant()) * bonus /*- emonster.getMaxMp()*/));
		} else {
			emonster.attachEffect(EffectType.MAX_MP_ABL,
					(float) (value - emonster.getMaxMp()));
		}

		value = fixedL2attrs.get(AttrType.MAX_SP);
		if (value == null) {
//			emonster.attachEffect(EffectType.MAX_SP_ABL,
//					monsterConfig.getMaxSp());
		} else {
			emonster.attachEffect(EffectType.MAX_SP_ABL,
					(float) (value - emonster.getMaxSp()));
		}

		value = fixedL2attrs.get(AttrType.DEFEND);
		if (value == null) {
//			emonster.attachEffect(
//					EffectType.DEFEND_ABL,
//					(float) ((monsterConfig.getDefFactor() * monster.getLevel() + monsterConfig
//							.getDefConstant()) * bonus /*- emonster.getDefend()*/));
		} else {
			emonster.attachEffect(EffectType.DEFEND_ABL,
					(float) (value - emonster.getDefend()));
		}

		value = fixedL2attrs.get(AttrType.MAGIC_ATTACK);
		if (value == null) {
//			emonster.attachEffect(
//					EffectType.MAGIC_ATTACK_ABL,
//					(float) ((monsterConfig.getMagicattFactor()
//							* monster.getLevel() + monsterConfig
//							.getMagicattConstant()) * bonus /*- emonster.getMagicAttack()*/));
		} else {
			emonster.attachEffect(EffectType.MAGIC_ATTACK_ABL, (float) (value - emonster.getMagicAttack()));
		}

		value = fixedL2attrs.get(AttrType.MAGIC_DEF);
		if (value == null) {
//			emonster.attachEffect(
//					EffectType.MAGIC_DEF_ABL,
//					(float) ((monsterConfig.getMagicDefFactor()
//							* monster.getLevel() + monsterConfig
//							.getMagicDefConstant()) * bonus /*- emonster.getMagicDef()*/));
		} else {
			emonster.attachEffect(EffectType.MAGIC_DEF_ABL,
					(float) (value - emonster.getMagicDef()));
		}

		value = fixedL2attrs.get(AttrType.HIT_RATE);
		if (value == null) {
//			emonster.attachEffect(
//					EffectType.HIT_RATE_ABL,
//					(float) ((monsterConfig.getAttallFactor()
//							* monster.getLevel() + monsterConfig
//							.getAttallConstant()) * bonus /*- emonster.getHit()*/));
		} else {
			emonster.attachEffect(EffectType.HIT_RATE_ABL,
					(float) (value - emonster.getHit()));
		}

		value = fixedL2attrs.get(AttrType.DODGE_RATE);
		if (value == null) {
			emonster.attachEffect(
					EffectType.DODGE_RATE_ABL,
					(float) ((0 * monster.getLevel() + 0) * bonus /*- emonster.getDodge()*/));
		} else {
			emonster.attachEffect(EffectType.DODGE_RATE_ABL,
					(float) (value - emonster.getDodge()));
		}

		value = fixedL2attrs.get(AttrType.SPEED);
		if (value == null) {
//			emonster.attachEffect(
//					EffectType.SPEED_ABL,
//					(float) ((monsterConfig.getSpeedFactor()
//							* monster.getLevel() + monsterConfig
//							.getSpeedConstant()) * bonus /*- emonster.getSpeed()*/));
		} else {
			emonster.attachEffect(EffectType.SPEED_ABL,
					(float) (value - emonster.getSpeed()));
		}

		value = fixedL2attrs.get(AttrType.MEDICAL);
		if (value == null) {
//			emonster.attachEffect(
//					EffectType.MEDICAL_ABL,
//					(float) ((monsterConfig.getMedicalFactor()
//							* monster.getLevel() + monsterConfig
//							.getMedicalConstant()) * bonus /*- emonster.getAttrById(AttrType.MEDICAL)*/));
		} else {
			emonster.attachEffect(EffectType.MEDICAL_ABL,
					(float) (value - emonster.getAttrById(AttrType.MEDICAL)));
		}

		value = fixedL2attrs.get(AttrType.SEAL);
		if (value == null) {
//			emonster.attachEffect(
//					EffectType.SEAL_ABL,
//					(float) ((monsterConfig.getSealhitFactor()
//							* monster.getLevel() + monsterConfig
//							.getSealhitConstant()) * bonus /*- emonster.getAttrById(AttrType.SEAL)*/));
		} else {
			emonster.attachEffect(EffectType.SEAL_ABL,
					(float) (value - emonster.getAttrById(AttrType.SEAL)));
		}

		value = fixedL2attrs.get(AttrType.UNSEAL);
		if (value == null) {
//			emonster.attachEffect(
//					EffectType.UNSEAL_ABL,
//					(float) ((monsterConfig.getUnsealFactor()
//							* monster.getLevel() + monsterConfig
//							.getUnsealConstant()) * bonus /*- emonster.getAttrById(AttrType.UNSEAL)*/));
		} else {
			emonster.attachEffect(EffectType.UNSEAL_ABL,
					(float) (value - emonster.getAttrById(AttrType.UNSEAL)));
		}

		// 再计算一遍属性，生成外功伤害
		emonster.updateAllFinalAttrs();// 设置最终属性

		value = fixedL2attrs.get(AttrType.ATTACK);
		if (value == null) {
//			emonster.attachEffect(
//					EffectType.DAMAGE_ABL,
//					(float) ((monsterConfig.getAttackFactor()
//							* monster.getLevel() + monsterConfig
//							.getAttackConstant()) * bonus /*- emonster.getAttack()*/));
		} else {
			emonster.attachEffect(EffectType.DAMAGE_ABL,
					(float) (value - emonster.getAttack()));
		}

//		fillExtraAttrs(monsterConfig, fixedL2attrs, emonster, bonus);
		// 免疫buff
//		try {
//			if (monsterConfig.getImmunebuffid() != null
//					&& !monsterConfig.getImmunebuffid().equals("")) {
//				String[] strs = monsterConfig.getImmunebuffid().split(";");
//				for (String str : strs)
//					monster.getImmunebuffs().add(Float.valueOf(str).intValue());
//			}
//		} catch (Exception e) {
//			BattleField.logger.error(e);
//		}

		emonster.updateAllFinalAttrs();// 设置最终属性
		emonster.fullHp();
		emonster.fullMp();// 补满MP
		// 初始化怪物AI
		initMonsterAI(monster, huobanInfo, battle);
		// 设置怪物基本prop完毕，剩余buffAgent没有设置

		// 打印属性
		printFinalAttrs(monster, huobanInfo.getHuobanname());
		return monster;
	}
	
	private static xbean.Monster createWildMonster(int monsterId,int level,final xbean.BattleInfo battle, Map<Integer,Float> fixedL2attrs)
	{
		if(fixedL2attrs == null)
			fixedL2attrs = new HashMap<Integer, Float>();
		SWildMonsterConfig monsterConfig = getSWildMonsterConfig(monsterId);
		if (monsterConfig == null){
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("can't find monsterconfig:"+monsterId);}
			return null;
		}
		xbean.Monster monster = xbean.Pod.newMonster();
		monster.setMonsterid(monsterId);
		monster.setFightnpctype(monsterConfig.getFightnpctype());
		monster.setMonstertype(monsterConfig.getMonstertype());
		monster.setSpecialtype(monsterConfig.getSpecialtype()); 
		monster.setSchool(monsterConfig.getSchool()); 

		monster.setBodysize((float)monsterConfig.bodytype);
		//对于怪物的造型，优先级顺序是: 外部传入 > 关联宠物造型 > 造型随机 > 造型ID
		monster.setShape(0);
		if (monsterConfig.getPet() != 0)
		{
			PetAttr petAttr =  fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.pet.PetAttr.class)
					.get(monsterConfig.getPet());
			if (petAttr == null)
				return null;
			monster.setShape(petAttr.getShape());
		}
		//造型随机
		else if(monsterConfig.getRandomShapes()!= null)
		{
			try
			{
				String[] strs = monsterConfig.getRandomShapes().split(";");
				if(strs.length > 0)
				{
					Integer[] randomShapes = new Integer[strs.length];
					for (int i = 0; i < strs.length; i++)
						randomShapes[i] = Float.valueOf(strs[i]).intValue();
					int random = Misc.getRandomBetween(0, randomShapes.length - 1);
					monster.setShape(randomShapes[random]);
				}
			}
			catch(Exception e)
			{
				BattleField.logger.error("怪物随机造型出错", e);
			}
		}
		else if(monsterConfig.getShape() != 0)
		{
			monster.setShape(monsterConfig.getShape());
		}
		if(level <= 0)// 设置等级
		{
			if(battle != null)//如果battle为null，则必定为固定等级
			{
				int leveltype = monsterConfig.getLevelType();
				switch (leveltype)
				{
				case 1:
					monster.setLevel(BattleField.getLeaderLevel(battle, true));
					break;
				case 2:
					monster.setLevel(BattleField.getTeamLevelWithWeight(battle, true));
					break;
				case 3:
					monster.setLevel(BattleField.getTeamMaxeLevel(battle, true));
					break;
				case 4:
					monster.setLevel(monsterConfig.getLevel());
					break;
				case 5:
					int arealevel = 1;
					if (battle.getAreaconf() != 0)
					{
						if (battle.getAreatype() == xbean.BattleInfo.AREA_BATTLEHIDE)
						{
							SMineArea areaInfo =  fire.pb.main.ConfigManager.getInstance().getConf(
									fire.pb.map.SMineArea.class).get(battle.getAreaconf());
							if (areaInfo != null)
								arealevel = areaInfo.getLevel();
						} else if (battle.getAreatype() == xbean.BattleInfo.AREA_BATTLESHOW)
						{
							SShowAreainfo areaInfo =  fire.pb.main.ConfigManager.getInstance().getConf(
									fire.pb.map.SShowAreainfo.class).get(battle.getAreaconf());
							if (areaInfo != null)
								arealevel = areaInfo.getTypelevel();
						}
					}
					monster.setLevel(arealevel);
					break;
				default:
					monster.setLevel(monsterConfig.getLevel());
					break;
				}

				if (leveltype != 4) {
					monster.setLevel(MathUtil.fixValueRange(monster.getLevel(), monsterConfig.getMinlevellimit(), monsterConfig.getMaxlevellimit()));
				}
			}
			else
			{
				monster.setLevel(monsterConfig.getLevel());
			}
		}
		else
		{
			monster.setLevel(level);
		}

		if(monster.getLevel() < 0)
			monster.setLevel(1);

		setBFP(monster, monsterConfig, monster.getLevel());// 设置基本5项属性(随机一种加点方式)
		genNpcMonsterSkills(monsterConfig.skills, monster, 1, null);
		//生成技能
		SkillAgent skillmonster = new SkillMonster(monster, 0);
		skillmonster.addSkillBuffWhileOnline(battle);
		
		// 构建Monster的ERole
		fire.pb.effect.Role emonster = new fire.pb.effect.MonsterImpl(monster);
		
		emonster.updateAllFinalAttrs();// 设置最终属性
		emonster.fullHp();// 补满HP
		emonster.fullMp();// 补满MP
		//初始化怪物AI
		initMonsterAI(monster,null,battle);
		// 设置怪物基本prop完毕，剩余effects和buffAgent没有设置
		
		// 打印属性
		printFinalAttrs(monster, monsterConfig.getName());
		return monster;
	}

	/**打印最终属性*/
	public static void printFinalAttrs(xbean.Monster monster, String name)
	{
		if (BattleField.logger.isDebugEnabled()) {
			fire.pb.effect.Role emonster = new fire.pb.effect.MonsterImpl(monster);
		
				BattleField.logger.debug("=====printFinalAttrs begin=====");
				BattleField.logger.debug("monster id:" + monster.getMonsterid()
						+ "\t" + "name:" + name
						+ "\t" + "lv:" + monster.getLevel());
				for (int i = 0; i < Module.fightAttrTypeIds.length; i++)
				{
					int attrId = Module.fightAttrTypeIds[i];
					float value = emonster.getAttrById(attrId);
					if (value > 0)
						BattleField.logger.debug("attr id:" + attrId + "\t" + "value:" + value);
				}
				BattleField.logger.debug("=====printFinalAttrs end=====");
		}
	}
	
	public static Set<Integer> fixedExtraAttrs = new HashSet<Integer>();
	static
	{
		fixedExtraAttrs.add(FightAttrType.SEAL_LEVEL);
		fixedExtraAttrs.add(FightAttrType.ANTI_SEAL_LEVEL);
		fixedExtraAttrs.add(FightAttrType.PHY_CRITC_LEVEL);
		fixedExtraAttrs.add(FightAttrType.PHYSIC_CRIT_PCT);
		fixedExtraAttrs.add(FightAttrType.ANTI_PHY_CRITC_LEVEL);
		fixedExtraAttrs.add(FightAttrType.MAGIC_CRITC_LEVEL);
		fixedExtraAttrs.add(FightAttrType.MAGIC_CRIT_PCT);
		fixedExtraAttrs.add(FightAttrType.ANTI_MAGIC_CRITC_LEVEL);
		fixedExtraAttrs.add(FightAttrType.HEAL_RATE);
		fixedExtraAttrs.add(FightAttrType.HEAL_DEGREE);
		fixedExtraAttrs.add(FightAttrType.PHYSIC_DAMGE_PIERCE_RATE);
		fixedExtraAttrs.add(FightAttrType.ANTI_PHYSIC_DAMGE_PIERCE_RATE);
		fixedExtraAttrs.add(FightAttrType.MAGIC_DAMGE_PIERCE_RATE);
		fixedExtraAttrs.add(FightAttrType.ANTI_MAGIC_DAMGE_PIERCE_RATE);
		fixedExtraAttrs.add(FightAttrType.HEAL_DEEP_RATE);
		fixedExtraAttrs.add(FightAttrType.ANTI_HEAL_DEEP_RATE);
		fixedExtraAttrs.add(FightAttrType.KONGZHI_JIACHENG);
		fixedExtraAttrs.add(FightAttrType.KONGZHI_MIANYI);
		fixedExtraAttrs.add(FightAttrType.ATTACK_BACK_LEVEL);
		fixedExtraAttrs.add(FightAttrType.ANTI_ATTACK_BACK_RATE);
		fixedExtraAttrs.add(FightAttrType.ATTACK_BACK_RATE);
		fixedExtraAttrs.add(FightAttrType.ATTACK_BACK_PCT);
		fixedExtraAttrs.add(FightAttrType.ANTI_ATTACK_BACK_LEVEL);
		fixedExtraAttrs.add(FightAttrType.RETURN_HURT_LEVEL);
		fixedExtraAttrs.add(FightAttrType.RETURN_HURT_PCT);
		fixedExtraAttrs.add(FightAttrType.ANTI_RETURN_HURT_LEVEL);
		fixedExtraAttrs.add(FightAttrType.PHYSIC_COMBO_ATTACK_RATE);
		fixedExtraAttrs.add(FightAttrType.PHYSIC_COMBO_ATTACK_COUNT);
		fixedExtraAttrs.add(FightAttrType.EXTRA_ATTACK_RATE);
		fixedExtraAttrs.add(FightAttrType.EXTRA_ATTACK_COUNT);
		fixedExtraAttrs.add(FightAttrType.ABSORB_FIRE_ODDS);
		fixedExtraAttrs.add(FightAttrType.ABSORB_WATER_ODDS);
		fixedExtraAttrs.add(FightAttrType.ABSORB_EARTH_ODDS);
		fixedExtraAttrs.add(FightAttrType.ABSORB_THUNDER_ODDS);
		fixedExtraAttrs.add(FightAttrType.ABSORB_OTHER_ODDS);
	}
	
	private static void fillFixedExtraAttrs(Map<Integer,Float> fixedvalues, fire.pb.effect.Role emonster, double bonus)
	{
		for(int attrId : fixedExtraAttrs){
			Float floatvalue = fixedvalues.get(attrId);
			if(floatvalue != null)
				emonster.attachEffect(attrId + 1, (float)(floatvalue*bonus /*- emonster.getAttrById(attrId)*/));
		}
	}
	
	private static void fillExtraAttrs(MonsterExtraAttrConfig monsterConfig,Map<Integer,Float> fixedattrs, fire.pb.effect.Role emonster, double bonus)
	{
		Map<Integer,Float> cfgattrs = getMonsterConfigAttrs(monsterConfig, emonster.getLevel());
		cfgattrs.putAll(fixedattrs);
		fillFixedExtraAttrs(cfgattrs, emonster, bonus);
		//fillMonsterXiuLian(monsterConfig, emonster, bonus);	
	}
	
	private static Map<Integer,Float> getMonsterConfigAttrs(MonsterExtraAttrConfig monsterConfig, int monsterLevel)
	{
		Map<Integer,Float> cfgattrs = new HashMap<Integer, Float>();
		//封印等级
		cfgattrs.put(FightAttrType.SEAL_LEVEL, (float)monsterConfig.getAddseal());
		//抗封印等级
		cfgattrs.put(FightAttrType.ANTI_SEAL_LEVEL, (float)monsterConfig.getDefseal());
		
		//物理暴击等级
		cfgattrs.put(FightAttrType.PHY_CRITC_LEVEL, (float)(monsterConfig.getWulicritical() + monsterConfig.getWulicriticalFactor() * monsterLevel));
		//物理暴击程度
		cfgattrs.put(FightAttrType.PHYSIC_CRIT_PCT, (float)(monsterConfig.getWulicriticaldegree() + monsterConfig.getWulicriticaldegreeFactor() * monsterLevel));
		//物理抗暴等级
		cfgattrs.put(FightAttrType.ANTI_PHY_CRITC_LEVEL, (float)(monsterConfig.getWulicriticaldef() + monsterConfig.getWulicriticaldefFactor() * monsterLevel));
		//法术暴击等级
		cfgattrs.put(FightAttrType.MAGIC_CRITC_LEVEL, (float)(monsterConfig.getFashucritical() + monsterConfig.getFashucriticalFactor() * monsterLevel));
		//法术暴击程度
		cfgattrs.put(FightAttrType.MAGIC_CRIT_PCT, (float)(monsterConfig.getFashucriticaldegree() + monsterConfig.getFashucriticaldegreeFactor() * monsterLevel));
		//法术抗暴等级
		cfgattrs.put(FightAttrType.ANTI_MAGIC_CRITC_LEVEL, (float)(monsterConfig.getFashucriticaldef() + monsterConfig.getFashucriticaldefFactor() * monsterLevel));

		//治疗暴击率
		cfgattrs.put(FightAttrType.HEAL_RATE, (float)(monsterConfig.getHeallv() + monsterConfig.getHeallvFactor() * monsterLevel));
		//治疗暴击程度
		cfgattrs.put(FightAttrType.HEAL_DEGREE, (float)(monsterConfig.getHealdegree() + monsterConfig.getHealdegreeFactor() * monsterLevel));
		//物理穿透
		cfgattrs.put(FightAttrType.PHYSIC_DAMGE_PIERCE_RATE, (float)(monsterConfig.getPhysicchuantou() + monsterConfig.getPhysicchuantouFactor() * monsterLevel));
		//物理抵抗
		cfgattrs.put(FightAttrType.ANTI_PHYSIC_DAMGE_PIERCE_RATE, (float)(monsterConfig.getPhysicdikang() + monsterConfig.getPhysicdikangFactor() * monsterLevel));
		//法术穿透
		cfgattrs.put(FightAttrType.MAGIC_DAMGE_PIERCE_RATE, (float)(monsterConfig.getMagicchuantou() + monsterConfig.getMagicchuantouFactor() * monsterLevel));
		//法术抵抗
		cfgattrs.put(FightAttrType.ANTI_MAGIC_DAMGE_PIERCE_RATE, (float)(monsterConfig.getMagicdikang() + monsterConfig.getMagicdikangFactor() * monsterLevel));
		//治疗加深
		cfgattrs.put(FightAttrType.HEAL_DEEP_RATE, (float)(monsterConfig.getMedicaljiashen() + monsterConfig.getMedicaljiashenFactor() * monsterLevel));
		//被治疗加深
		cfgattrs.put(FightAttrType.ANTI_HEAL_DEEP_RATE, (float)(monsterConfig.getOnmedicaljiashen() + monsterConfig.getOnmedicaljiashenFactor() * monsterLevel));
		//控制加成
		cfgattrs.put(FightAttrType.KONGZHI_JIACHENG, (float)(monsterConfig.getKongzhijiacheng() + monsterConfig.getKongzhijiachengFactor() * monsterLevel));
		//控制免疫
		cfgattrs.put(FightAttrType.KONGZHI_MIANYI, (float)(monsterConfig.getKongzhimianyi() + monsterConfig.getKongzhimianyiFactor() * monsterLevel));

		//反击几率
		cfgattrs.put(FightAttrType.ATTACK_BACK_RATE, (float)monsterConfig.getFightbackprob());
		//反击伤害系数
		cfgattrs.put(FightAttrType.ATTACK_BACK_PCT, (float)monsterConfig.getFightbackhurtratio());
			
		//反击等级
		cfgattrs.put(FightAttrType.ATTACK_BACK_LEVEL, (float)0);
		//反击次数
		cfgattrs.put(FightAttrType.ANTI_ATTACK_BACK_RATE, (float)0);

		//抗反击等级
		cfgattrs.put(FightAttrType.ANTI_ATTACK_BACK_LEVEL, (float)0);
		
		//反震等级
		cfgattrs.put(FightAttrType.RETURN_HURT_LEVEL, (float)0);
		//反震程度
		cfgattrs.put(FightAttrType.RETURN_HURT_PCT, (float)0);
		//抗反震等级
		cfgattrs.put(FightAttrType.ANTI_RETURN_HURT_LEVEL, (float)0);
		
		//连击率
		cfgattrs.put(FightAttrType.PHYSIC_COMBO_ATTACK_RATE, (float)0);
		//连击次数
		cfgattrs.put(FightAttrType.PHYSIC_COMBO_ATTACK_COUNT, (float)0);
		
		//追击率
		cfgattrs.put(FightAttrType.EXTRA_ATTACK_RATE, (float)0);
		//追击次数
		cfgattrs.put(FightAttrType.EXTRA_ATTACK_COUNT, (float)0);
		
		//火属性吸收
		cfgattrs.put(FightAttrType.ABSORB_FIRE_ODDS, (float)0);
		//水属性吸收
		cfgattrs.put(FightAttrType.ABSORB_WATER_ODDS, (float)0);
		//土属性吸收
		cfgattrs.put(FightAttrType.ABSORB_EARTH_ODDS, (float)0);
		//雷属性吸收
		cfgattrs.put(FightAttrType.ABSORB_THUNDER_ODDS, (float)0);
		//保留属性吸收
		cfgattrs.put(FightAttrType.ABSORB_OTHER_ODDS, (float)0);

		//额外属性1
		cfgattrs.put(monsterConfig.getExtraattrid1(), (float)monsterConfig.getExtraattrvalue1());
		//额外属性2
		cfgattrs.put(monsterConfig.getExtraattrid2(), (float)monsterConfig.getExtraattrvalue2());
		//额外属性3
		cfgattrs.put(monsterConfig.getExtraattrid3(), (float)monsterConfig.getExtraattrvalue3());
		//额外属性4
		cfgattrs.put(monsterConfig.getExtraattrid4(), (float)monsterConfig.getExtraattrvalue4());
		//额外属性5
		cfgattrs.put(monsterConfig.getExtraattrid5(), (float)monsterConfig.getExtraattrvalue5());
		//额外属性6
		cfgattrs.put(monsterConfig.getExtraattrid6(), (float)monsterConfig.getExtraattrvalue6());
		//额外属性7
		cfgattrs.put(monsterConfig.getExtraattrid7(), (float)monsterConfig.getExtraattrvalue7());
		//额外属性8
		cfgattrs.put(monsterConfig.getExtraattrid8(), (float)monsterConfig.getExtraattrvalue8());
		//额外属性9
		cfgattrs.put(monsterConfig.getExtraattrid9(), (float)monsterConfig.getExtraattrvalue9());
		//额外属性10
		cfgattrs.put(monsterConfig.getExtraattrid10(), (float)monsterConfig.getExtraattrvalue10());

		return cfgattrs;
	}
	
	/**
	 * 初始化怪物AI
	 * @param huobanInfo 
	 */
	private static void initMonsterAI(xbean.Monster monster, xbean.HuoBanshuxing huobanInfo/*XiaKeInfo xiaKeInfo*/,xbean.BattleInfo battle)
	{
		MonsterBattleAI monsterAI = null;
		if(battle != null && huobanInfo == null){
				monsterAI = MonsterAIManager.getInstance().getMonsterAIs().get(monster.getMonsterid());
		}
		else
		{
			monsterAI = MonsterAIManager.getInstance().getMonsterAIs().get(monster.getMonsterid());
		}
		
		if (huobanInfo != null && battle != null)
		{
			int fightAItype = 0;
			BattleConfig battleConfig = fire.pb.battle.Module.getInstance().getBattleConfigs().get(battle.getConfigid());
			if(battleConfig!= null && 0 < battleConfig.fightAItype && battleConfig.fightAItype < 5 )
				fightAItype = battleConfig.fightAItype - 1;
			
			if (battle.getBattletype() == BattleType.BATTLE_PVP)
				fightAItype = 3;
			
			Integer aiid = huobanInfo.getBattleai().get(fightAItype);
			if (aiid == null) {
				aiid = huobanInfo.getBattleai().get(0);
			}
			
			if (aiid > 0) {
				xbean.BattleAI ai = xbean.Pod.newBattleAI();
				ai.setId(aiid);
				monster.getAis().add(ai);
			}
		}
		else
		{
			if(monsterAI == null)
				return;
			for(int aiId : monsterAI.getBattleAIs())
			{
				xbean.BattleAI ai = xbean.Pod.newBattleAI();
				ai.setId(aiId);
				ai.setEnableround(1);
				monster.getAis().add(ai);
			}
			for(Parser.ID2Odds id2odds : monsterAI.getBattleAIOdds())
			{
				int aiId = id2odds.getRandomId();
				if(aiId > 0)
				{
					xbean.BattleAI ai = xbean.Pod.newBattleAI();
					ai.setId(aiId);
					monster.getAis().add(ai);
				}
			}
		}
	}
//	 /**
//	  * 生成技能
//	  * @param monsterConfig
//	  * @param monster
//	 * @param xiaKeInfo 
//	  */
//	private static void genNpcMonsterSkills(List<SMonsterSkill> skills,xbean.Monster monster, double bonus, XiaKeInfo xiaKeInfo){
//		for(SMonsterSkill sskill : skills){
//			if(sskill.skillid != 0 && Misc.checkRate(1000, Integer.valueOf(sskill.showrate)))
//			{
//				xbean.MonsterSkill xskill = xbean.Pod.newMonsterSkill();
//				xskill.setId(sskill.skillid);
//				xskill.setSkilllevel((int)((sskill.levelfactor * monster.getLevel() + sskill.levelconstant) * bonus));
//				if(isHireNpcMonster(monster.getMonsterid()))
//				{//协战npc的技能等级不能超过等级+10
//					xskill.setSkilllevel(Math.min( monster.getLevel() + 10, xskill.getSkilllevel() ));
//				}
//				xskill.setCastrate(sskill.castrate);
//				monster.getSkills().add(xskill);
//			}
//		}
//		if (xiaKeInfo != null) 
//			for (Integer skillid : xiaKeInfo.getSkills().keySet()) {
//				xbean.MonsterSkill xskill = xbean.Pod.newMonsterSkill();
//				xskill.setId(skillid);
//				xskill.setSkilllevel(1);
//				xskill.setCastrate(0);
//				monster.getSkills().add(xskill);
//			}
//	}
	
	/**
	  * 生成技能
	  * @param monsterConfig
	  * @param monster
	 * @param huobanInfo 
	  */
	private static void genNpcMonsterSkills(List<SMonsterSkill> skills,xbean.Monster monster, double bonus, xbean.HuoBanshuxing huobanInfo){
		for (SMonsterSkill sskill : skills) {
			if (sskill.skillid == 0)
				continue;
			
			FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(sskill.getSkillid());
			if (sconf == null)
				continue;
			
			if (sconf.isActiveSkill()
					&& sconf.getType() != SkillConstant.BATTLE_SKILL_TYPE_PASSIVE 
					&& sconf.getType() != SkillConstant.BATTLE_SKILL_TYPE_PASSIVE_TRIGGER) {
				if (Misc.checkRate(1000 - 1, Integer.valueOf(sskill.showrate))) {
					xbean.MonsterSkill xskill = xbean.Pod.newMonsterSkill();
					xskill.setId(sskill.skillid);
					xskill.setSkilllevel((int) ((sskill.levelfactor	* monster.getLevel() + sskill.levelconstant) * bonus));
					if (isHireNpcMonster(monster.getMonsterid())) {// 协战npc的技能等级不能超过等级+10
						xskill.setSkilllevel(Math.min(monster.getLevel() + 10, xskill.getSkilllevel()));
					}
					xskill.setCastrate(sskill.castrate);
					monster.getSkills().add(xskill);
				}
			}
			else {
				if (Misc.checkRate(1000 - 1, Integer.valueOf(sskill.showrate))) {
					xbean.MonsterSkill xskill = xbean.Pod.newMonsterSkill();
					xskill.setId(sskill.skillid);
					xskill.setSkilllevel((int) ((sskill.levelfactor	* monster.getLevel() + sskill.levelconstant) * bonus));
					if (isHireNpcMonster(monster.getMonsterid())) {// 协战npc的技能等级不能超过等级+10
						xskill.setSkilllevel(Math.min(monster.getLevel() + 10, xskill.getSkilllevel()));
					}
					xskill.setCastrate(0);
					monster.getSkills().add(xskill);
				}
			}
		}
		
		List<Integer> skillLst = new ArrayList<Integer>();
		if (huobanInfo != null && huobanInfo.getSkills() != null && huobanInfo.getSkills().size() > 0) {
			for (Integer skillid : huobanInfo.getSkills()) {
				FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillid);
				if (sconf != null
						&& sconf.isActiveSkill()
						&& sconf.getType() != SkillConstant.BATTLE_SKILL_TYPE_PASSIVE 
						&& sconf.getType() != SkillConstant.BATTLE_SKILL_TYPE_PASSIVE_TRIGGER) {
					skillLst.add(skillid);
				}
			}
		}
		
		int nskillsize = skillLst.size();
		if(skillLst.contains(SkillConstant.BATTLE_ROLE_BEHUNTER))
			nskillsize--;
		if(skillLst.contains(SkillConstant.BATTLE_HUOBAN_BEHUNTER_1))
			nskillsize--;
		if(skillLst.contains(SkillConstant.BATTLE_HUOBAN_BEHUNTER_2))
			nskillsize--;
		if(skillLst.contains(SkillConstant.BATTLE_HUOBAN_BEHUNTER_3))
			nskillsize--;
		if(skillLst.contains(SkillConstant.BATTLE_HUOBAN_BEHUNTER_4))
			nskillsize--;
		if (huobanInfo != null && huobanInfo.getSkills() != null && huobanInfo.getSkills().size() > 0) {
			for (Integer skillid : huobanInfo.getSkills()) {
				
				if (skillLst.contains(skillid)) {
					//主动技能
					xbean.MonsterSkill xskill = xbean.Pod.newMonsterSkill();
					xskill.setId(skillid);
					xskill.setSkilllevel((int) (huobanInfo.getLevelfactor()	* huobanInfo.getLevel() + huobanInfo.getLevelconstant()));
					if(nskillsize==0||skillid == SkillConstant.BATTLE_ROLE_BEHUNTER
							||skillid == SkillConstant.BATTLE_HUOBAN_BEHUNTER_1
							||skillid == SkillConstant.BATTLE_HUOBAN_BEHUNTER_2
							||skillid == SkillConstant.BATTLE_HUOBAN_BEHUNTER_3
							||skillid == SkillConstant.BATTLE_HUOBAN_BEHUNTER_4)
						xskill.setCastrate(0);
					else
						xskill.setCastrate(1000 / nskillsize);
					monster.getSkills().add(xskill);
				}
				else {
					//被动技能
					xbean.MonsterSkill xskill = xbean.Pod.newMonsterSkill();
					xskill.setId(skillid);
					xskill.setSkilllevel((int) (huobanInfo.getLevelfactor()	* huobanInfo.getLevel() + huobanInfo.getLevelconstant()));
					xskill.setCastrate(0);
					monster.getSkills().add(xskill);
				}
			}
		}
	}

	/**
	 * 根据配置设置属性点
	 * @param monster
	 * @param monsterConfig
	 * @param level
	 */
	private static void setBFP(xbean.Monster monster,SWildMonsterConfig monsterConfig,int level)
	{
		// 分配初始点数
		final int [] point = new int[5];
		if (monsterConfig.getInitPointAssignType() == 1) { // == 1 (平均分配)
			int n = monsterConfig.getInitPoint() / 5;
			int s = monsterConfig.getInitPoint() - n * 5;
			for (int i = 0; i < point.length; i++) {
				point[i] = n;
			}
			// 未分配完的点数加到这里
			point[ADD_POINT_ENDU] += s;

		} else { // == 2 (随机分配)
			// 随机分配,最少10点
			if (monsterConfig.getInitPoint() >= 50) {
				Misc.RandomDistribute(monsterConfig.getInitPoint() - 50, 5, point);
				for (int i = 0; i < point.length; i++) {
					point[i] += 10;
				}
			} else {
				for (int i = 0; i < point.length; i++) {
					point[i] = 0;
				}
				if (monsterConfig.getInitPoint() > 0) {
					Misc.RandomDistribute(monsterConfig.getInitPoint(), 5, point);
				}
			}
		}

		// 加点
		int cons = 1;
		int iq = 1;
		int str = 1;
		int endu = 1;
		int agi = 1;
		if (monsterConfig.addpoint.size() == 5) {
			cons = monsterConfig.addpoint.get(0); // 体质
			iq = monsterConfig.addpoint.get(1);   // 智力
			str = monsterConfig.addpoint.get(2);  // 力量
			endu = monsterConfig.addpoint.get(3); // 耐力
			agi = monsterConfig.addpoint.get(4);  // 敏捷
		}
		monster.getBfp().setCons(point[ADD_POINT_CONS] + cons * level);
		monster.getBfp().setIq(point[ADD_POINT_IQ]     + iq   * level);
		monster.getBfp().setStr(point[ADD_POINT_STR]   + str  * level);
		monster.getBfp().setEndu(point[ADD_POINT_ENDU] + endu * level);
		monster.getBfp().setAgi(point[ADD_POINT_AGI]   + agi  * level);
	}
	
	/**
	 * 根据角色当前2级属性生成怪物，只能保证2级属性一致，1级属性全为0
	 * 存储过程中调用
	 * @param roleId
	 * @return xbean.Monster
	 */
	public static xbean.Monster copyMonsterFromRoleOrPet(long roleId,int monsterid,boolean fromRole,int petkey)
	{
		xbean.Monster monster = xbean.Pod.newMonster();
		monster.setMonsterid(monsterid);
		fire.pb.effect.Role erole = null;
		if (fromRole) 
		{
		    erole = new RoleImpl(roleId,true);
		}
		else
		{
			erole = new PetImpl(roleId, petkey, true);
		}
		// 构建Monster的ERole
		fire.pb.effect.Role emonster = new fire.pb.effect.MonsterImpl(monster);
		
		SMonsterConfig monsterConfig =  getSMonsterConfig(monsterid);
		
		if (fromRole) {
			xbean.Properties prole = xtable.Properties.select(roleId);
			monster.setShape(prole.getShape());
			monster.setBodysize((float)monsterConfig.bodytype);
			monster.setLevel(prole.getLevel());
		}else {
			PetColumn petColumn = new PetColumn(roleId, PetColumnTypes.PET, true);
			Pet pet = petColumn.getPet(petkey);
			if (pet == null)
				throw new IllegalArgumentException("错误的petkey：" + petkey);
			monster.setShape(pet.getPetAttr().getShape());
			monster.setBodysize(pet.getSize());
			monster.setLevel(pet.getLevel());
		}
		
		//生成技能
		genNpcMonsterSkills(monsterConfig.getSkills(), monster, 1.0, null);
		//怪不能赋予与人相同的一级属性，因为一级属性计算二级属性时，怪与人的公式是不一样的，只能给怪的一级属性全赋0
		//第一次计算最终属性，因为1级属性即使为0，2级属性也不是0
		emonster.updateAllFinalAttrs();// 先计算一遍2级属性

		// 添加二级属性效果
		emonster.attachEffect(EffectType.MAX_HP_ABL, erole.getAttrById(FightAttrType.MAX_HP) - emonster.getAttrById(FightAttrType.MAX_HP));
		emonster.attachEffect(EffectType.MAX_MP_ABL, erole.getAttrById(FightAttrType.MAX_MP) - emonster.getAttrById(FightAttrType.MAX_MP));
		emonster.attachEffect(EffectType.DEFEND_ABL, erole.getAttrById(FightAttrType.DEFEND) - emonster.getAttrById(FightAttrType.DEFEND));
		emonster.attachEffect(EffectType.MAGIC_ATTACK_ABL, erole.getAttrById(FightAttrType.MAGIC_ATTACK)  - emonster.getAttrById(FightAttrType.MAGIC_ATTACK));
		emonster.attachEffect(EffectType.MAGIC_DEF_ABL, erole.getAttrById(FightAttrType.MAGIC_DEF) - emonster.getAttrById(FightAttrType.MAGIC_DEF));
		emonster.attachEffect(EffectType.HIT_RATE_ABL, erole.getAttrById(FightAttrType.HIT_RATE) - emonster.getAttrById(FightAttrType.HIT_RATE));
		emonster.attachEffect(EffectType.DODGE_RATE_ABL, erole.getAttrById(FightAttrType.DODGE_RATE) - emonster.getAttrById(FightAttrType.DODGE_RATE));
		emonster.attachEffect(EffectType.SPEED_ABL, erole.getAttrById(FightAttrType.SPEED) - emonster.getAttrById(FightAttrType.SPEED));

		//第二次计算最终属性，得出与人物相等的2级属性
		emonster.updateAllFinalAttrs();// 计算最终属性
		
		//添加伤害
		emonster.attachEffect(EffectType.DAMAGE_ABL, erole.getAttrById(FightAttrType.ATTACK) - emonster.getAttrById(FightAttrType.ATTACK));
		
		//封印等级
		emonster.attachEffect(EffectType.SEAL_LEVEL_ABL, erole.getAttrById(FightAttrType.SEAL_LEVEL) - emonster.getAttrById(FightAttrType.SEAL_LEVEL));
		//抗封印等级
		emonster.attachEffect(EffectType.ANTI_SEAL_LEVEL_ABL, erole.getAttrById(FightAttrType.ANTI_SEAL_LEVEL) - emonster.getAttrById(FightAttrType.ANTI_SEAL_LEVEL));
		
		//物理暴击等级
		emonster.attachEffect(EffectType.PHY_CRITC_LEVEL_ABL, erole.getAttrById(FightAttrType.PHY_CRITC_LEVEL) - emonster.getAttrById(FightAttrType.PHY_CRITC_LEVEL));
		//物理暴击程度
		emonster.attachEffect(EffectType.PHYSIC_CRIT_PCT_ABL , erole.getAttrById(FightAttrType.PHYSIC_CRIT_PCT) - emonster.getAttrById(FightAttrType.PHYSIC_CRIT_PCT));
		//物理抗暴等级
		emonster.attachEffect(EffectType.ANTI_PHY_CRITC_LEVEL_ABL, erole.getAttrById(FightAttrType.ANTI_PHY_CRITC_LEVEL) - emonster.getAttrById(FightAttrType.ANTI_PHY_CRITC_LEVEL));
		
		//法术暴击等级
		emonster.attachEffect(EffectType.MAGIC_CRITC_LEVEL_ABL, erole.getAttrById(AttrType.MAGIC_CRITC_LEVEL) - emonster.getAttrById(FightAttrType.MAGIC_CRITC_LEVEL));
		//法术暴击程度
		emonster.attachEffect(EffectType.MAGIC_CRIT_PCT_ABL, erole.getAttrById(FightAttrType.MAGIC_CRIT_PCT) - emonster.getAttrById(FightAttrType.MAGIC_CRIT_PCT));
		//法术抗暴等级
		emonster.attachEffect(EffectType.ANTI_MAGIC_CRITC_LEVEL_ABL, erole.getAttrById(AttrType.ANTI_MAGIC_CRITC_LEVEL) - emonster.getAttrById(FightAttrType.ANTI_MAGIC_CRITC_LEVEL));
		
		//反击等级
		emonster.attachEffect(EffectType.ATTACK_BACK_LEVEL_ABL, erole.getAttrById(FightAttrType.ATTACK_BACK_LEVEL) - emonster.getAttrById(FightAttrType.ATTACK_BACK_LEVEL));
/*		//反击次数
		emonster.attachEffect(EffectType.ATTACK_BACK_COUNT_ABL, erole.getAttrById(FightAttrType.ANTI_ATTACK_BACK_RATE) - emonster.getAttrById(FightAttrType.ANTI_ATTACK_BACK_RATE));
		//反击程度
*/		emonster.attachEffect(EffectType.RETURN_ATTACK_PCT_ABL, emonster.getAttrById(FightAttrType.ATTACK_BACK_PCT) /*- emonster.getAttrById(FightAttrType.ANTI_ATTACK_BACK_RATE)*/);
		//抗反击等级
		emonster.attachEffect(EffectType.ANTI_ATTACK_BACK_LEVEL_ABL, erole.getAttrById(FightAttrType.ANTI_ATTACK_BACK_LEVEL) - emonster.getAttrById(FightAttrType.ANTI_ATTACK_BACK_LEVEL));
		
		
		//反震等级
		emonster.attachEffect(EffectType.RETURN_HURT_LEVEL_ABL, erole.getAttrById(FightAttrType.RETURN_HURT_LEVEL) - emonster.getAttrById(FightAttrType.RETURN_HURT_LEVEL));
		//反震程度
		emonster.attachEffect(EffectType.RETURN_HURT_PCT_ABL, erole.getAttrById(FightAttrType.RETURN_HURT_PCT) - emonster.getAttrById(FightAttrType.RETURN_HURT_PCT));
		//抗反震等级
		emonster.attachEffect(EffectType.ANTI_RETURN_HURT_LEVEL_ABL, erole.getAttrById(FightAttrType.ANTI_RETURN_HURT_LEVEL) - emonster.getAttrById(FightAttrType.ANTI_RETURN_HURT_LEVEL));
		
		//连击率
		emonster.attachEffect(EffectType.COMBO_ATTACK_RATE_ABL, erole.getAttrById(FightAttrType.PHYSIC_COMBO_ATTACK_RATE) - emonster.getAttrById(FightAttrType.PHYSIC_COMBO_ATTACK_RATE));
		//连击伤害系数
		emonster.attachEffect(EffectType.COMBO_ATTACK_PCT_ABL, erole.getAttrById(FightAttrType.PHYSIC_COMBO_ATTACK_PCT) - emonster.getAttrById(FightAttrType.PHYSIC_COMBO_ATTACK_PCT));
		//连击次数
		emonster.attachEffect(EffectType.COMBO_ATTACK_COUNT_ABL, erole.getAttrById(FightAttrType.PHYSIC_COMBO_ATTACK_COUNT) - emonster.getAttrById(FightAttrType.PHYSIC_COMBO_ATTACK_COUNT));
		
		//法术连击率
		emonster.attachEffect(EffectType.MAGIC_COMBO_ATTACK_RATE_ABL, erole.getAttrById(FightAttrType.MAGIC_COMBO_ATTACK_RATE) - emonster.getAttrById(FightAttrType.MAGIC_COMBO_ATTACK_RATE));
		//连击伤害系数
		emonster.attachEffect(EffectType.MAGIC_COMBO_ATTACK_PCT_ABL, erole.getAttrById(FightAttrType.MAGIC_COMBO_ATTACK_PCT) - emonster.getAttrById(FightAttrType.MAGIC_COMBO_ATTACK_PCT));
		//法术连击次数
		emonster.attachEffect(EffectType.MAGIC_COMBO_ATTACK_COUNT_ABL, erole.getAttrById(FightAttrType.MAGIC_COMBO_ATTACK_COUNT) - emonster.getAttrById(FightAttrType.MAGIC_COMBO_ATTACK_COUNT));

		//追击率
		emonster.attachEffect(EffectType.EXTRA_ATTACK_RATE_ABL, erole.getAttrById(FightAttrType.EXTRA_ATTACK_RATE) - emonster.getAttrById(FightAttrType.EXTRA_ATTACK_RATE));
		//追击次数
		emonster.attachEffect(EffectType.EXTRA_ATTACK_COUNT_ABL, erole.getAttrById(FightAttrType.EXTRA_ATTACK_COUNT) - emonster.getAttrById(FightAttrType.EXTRA_ATTACK_COUNT));
		
		//火属性吸收
		emonster.attachEffect(EffectType.ABSORB_FIRE_ODDS_ABL, erole.getAttrById(FightAttrType.ABSORB_FIRE_ODDS) - emonster.getAttrById(FightAttrType.ABSORB_FIRE_ODDS));
		//水属性吸收
		emonster.attachEffect(EffectType.ABSORB_WATER_ODDS_ABL, erole.getAttrById(FightAttrType.ABSORB_WATER_ODDS) - emonster.getAttrById(FightAttrType.ABSORB_WATER_ODDS));
		//土属性吸收
		emonster.attachEffect(EffectType.ABSORB_EARTH_ODDS_ABL, erole.getAttrById(FightAttrType.ABSORB_EARTH_ODDS) - emonster.getAttrById(FightAttrType.ABSORB_EARTH_ODDS));
		//雷属性吸收
		emonster.attachEffect(EffectType.ABSORB_THUNDER_ODDS_ABL, erole.getAttrById(FightAttrType.ABSORB_THUNDER_ODDS) - emonster.getAttrById(FightAttrType.ABSORB_THUNDER_ODDS));
		//保留属性吸收
		emonster.attachEffect(EffectType.ABSORB_OTHER_ODDS_ABL, erole.getAttrById(FightAttrType.ABSORB_OTHER_ODDS) - emonster.getAttrById(FightAttrType.ABSORB_OTHER_ODDS));
/*		//修炼属性，因为怪物没有修炼技能，将修炼转化为属性
		emonster.attachEffect(EffectType.ATTACK_BOUNS_ABL, skillagent.getLevel(SkillConstant.ASSIST_SKILL_ATTACK_BONUS) - emonster.getAttrById(FightAttrType.ATTACK_BOUNS));
		emonster.attachEffect(EffectType.MAGIC_ATTACK_BOUNS_ABL, skillagent.getLevel(SkillConstant.ASSIST_SKILL_MAGIC_ATTACK_BONUS) - emonster.getAttrById(FightAttrType.MAGIC_ATTACK_BOUNS));
		emonster.attachEffect(EffectType.SEAL_BOUNS_ABL, skillagent.getLevel(SkillConstant.ASSIST_SKILL_SEAL_BONUS) - emonster.getAttrById(FightAttrType.SEAL_BOUNS));
		emonster.attachEffect(EffectType.DEF_BOUNS_ABL, skillagent.getLevel(SkillConstant.ASSIST_SKILL_DEF_BONUS) - emonster.getAttrById(FightAttrType.DEF_BOUNS));
		emonster.attachEffect(EffectType.MAGIC_DEF_BOUNS_ABL, skillagent.getLevel(SkillConstant.ASSIST_SKILL_MAGIC_DEF_BONUS) - emonster.getAttrById(FightAttrType.MAGIC_DEF_BOUNS));
		emonster.attachEffect(EffectType.UNSEAL_BOUNS_ABL, skillagent.getLevel(SkillConstant.ASSIST_SKILL_UNSEAL_BONUS) - emonster.getAttrById(FightAttrType.UNSEAL_BOUNS));
*/		
		//第三次计算最终属性，得出3级属性——物理伤害！
		emonster.updateAllFinalAttrs();// 计算最终属性
		
		
		emonster.fullHp();// 补满HP
		emonster.fullMp();// 补满MP
		// 设置怪物基本prop完毕，剩余buffAgent没有设置
		return monster;
	}
	
	/**
	 * 获取怪物在表内配的id
	 * @param monsterId 默认返回“怪物”
	 */
	public static String getMonsterName(final int monsterId)
	{
		MonsterConfig cfg = getMonsterConfig(monsterId);
		if(cfg != null)
			return cfg.getName();
		else
			return StringConstant.ID2String("怪物", StringConstant.怪物);
	}
	
	public static fire.pb.npc.MonsterConfig getMonsterConfig(final int monsterId)
	{
		if(isNpcMonster(monsterId))
		{
			fire.pb.npc.SMonsterConfig cfg = getSMonsterConfig(monsterId);
			if(cfg != null)
				return cfg; 
		}
		else
		{
			fire.pb.npc.SWildMonsterConfig cfg = getSWildMonsterConfig(monsterId);
			if(cfg != null)
				return cfg; 
		}
		return null; 
	}
	
	public fire.pb.npc.SMonsterConfig getSMonsterConfig()
	{
		if(!isNpcMonster())
			return null;
		return getSMonsterConfig(this.monsterId);
	}
	
	public fire.pb.npc.SWildMonsterConfig getSWildMonsterConfig()
	{
		if(isNpcMonster())
			return null;
		return getSWildMonsterConfig(this.monsterId);
	}
	
	public static fire.pb.npc.SMonsterConfig getSMonsterConfig(final int id) {
		return fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.npc.SMonsterConfig.class)
				.get(id);
	}

	public static fire.pb.npc.SWildMonsterConfig getSWildMonsterConfig(final int id) {
		return fire.pb.main.ConfigManager.getInstance().getConf(
				fire.pb.npc.SWildMonsterConfig.class).get(id);
	}

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}
	
	public boolean isNpcMonster()
	{
		return isNpcMonster(this.monsterId);
	}
	//是否是雇佣npc怪
	public static boolean isHireNpcMonster(int monsterId)
	{
		int monstertype = monsterId / 1000;
		return monstertype == 28;
	}
	
	public static boolean isNpcMonster(int monsterId)
	{
		int monstertype = monsterId / 1000;
		switch (monstertype)
		{
		case 20:
		case 21:
		case 22:
		case 23:
			return false;
		case 24:
		case 25:
		case 26:
		case 27:
		case 28:
		case 29:
		case 30:
		case 31:
		case 32:
		case 33:
		case 34:
		case 35:
		case 36:
		case 37:
		case 38:
		case 39:
		case 40:
			return true;
		}
		return false;
	}
	
	/**
	 * 此方法验证某怪物是否可以倒地死亡
	 * 对于一般的怪物来说，死亡时即会被击飞，但是有些怪物可能会到底死亡
	 * @param monsterId
	 * @return
	 */
	public static boolean canDeath(int monsterId)
	{
		if(!isNpcMonster(monsterId))
			return false;
		SMonsterConfig monsterconfig = getSMonsterConfig(monsterId);
		if(monsterconfig == null)
			return false;
		return monsterconfig.daodi == 1;
	}
	
	
	
	public static void main(String[] args)
	{
		double d = 3;
		System.out.println(d);
		String s = String.valueOf(d);
		System.out.println(s);
	}
}