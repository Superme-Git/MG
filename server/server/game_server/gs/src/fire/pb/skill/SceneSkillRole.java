package fire.pb.skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fire.pb.attr.AttrType;
import fire.pb.attr.EffectType;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.battle.DemoResult;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffManager;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.IBuff;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.effect.RoleImpl;
import fire.pb.item.EquipItem;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.skill.fight.FightSkillConfig;
import fire.script.JavaScript;
import fire.script.SceneJSEngine;
import mkdb.Transaction;

public class SceneSkillRole
{
	protected long roleId;
	protected boolean readonly;
	public SceneSkillRole(long roleId)
	{
		this(roleId,false);
	}
	
	
	public SceneSkillRole(long roleId, boolean readonly)
	{
		this.roleId = roleId;
		this.readonly = readonly;
	}

	public Map<Integer,Float> useItem2Pet(int petId, int itemId , List<fire.pb.item.Effect> effects)
	{
		PetColumn petColumn = new PetColumn(roleId, PetColumnTypes.PET, readonly);
		Pet pet = petColumn.getPet(petId);
		boolean canAddHp = true;
		//boolean poison = false;
		for (fire.pb.item.Effect effect : effects) {
			if (effect.effectid == EffectType.PET_ATTACK_APT_ABL && effect.value < 0.0 ) {
				//poison = true;
				break;
			}
		}
		Map<Integer,Float> changedAttrs = new HashMap<Integer,Float>();
		fire.pb.effect.PetImpl epet = new fire.pb.effect.PetImpl(roleId,petId);
		for(fire.pb.item.Effect effect : effects)
		{
			switch(effect.effectid)
			{
			case EffectType.HP_ABL:
				if(!canAddHp)
					break;
				epet.addHp((int)(effect.value));
				changedAttrs.put(AttrType.HP, (float)epet.getHp());
				break;
			case EffectType.HP_PCT:
				if(!canAddHp)
					break;
				epet.addHp((int)(epet.getMaxHp() * Float.valueOf(effect.value)));
				changedAttrs.put(AttrType.HP, (float)epet.getHp());
				break;				
			case EffectType.MP_ABL:
				epet.addMp((int)(effect.value));
				changedAttrs.put(AttrType.MP, (float)epet.getMp());
				break;
			case EffectType.MP_PCT:
				epet.addMp((int)(epet.getMaxMp() * Float.valueOf(effect.value)));
				changedAttrs.put(AttrType.MP, (float)epet.getMp());
				break;
			case EffectType.PET_LIFE_ABL:
				if(!fire.pb.pet.Module.getInstance().getPetManager().isPetLifeForever(pet.getPetAttr().getId())){
					int oldlife = pet.getLife();
					int changelife = pet.addLife((int)effect.value) - oldlife;
					changedAttrs.put(AttrType.PET_LIFE, (float)pet.getLife());
				}
				break;
			case EffectType.PET_ATTACK_APT_ABL:
				epet.addBornattackApt( (int)effect.value );
				changedAttrs.put(AttrType.PET_ATTACK_APT, (float)epet.getAttackApt() );
				break;
			case EffectType.PET_DEFEND_APT_ABL:
				epet.addBorndefendApt( (int)effect.value );
				changedAttrs.put(AttrType.PET_DEFEND_APT, (float)epet.getDefendApt() );
				break;
			case EffectType.PET_DODGE_APT_ABL:
				epet.addBorndodgeApt( (int)effect.value );
				changedAttrs.put(AttrType.PET_DODGE_APT, (float)epet.getDodgeApt() );
				break;
			case EffectType.PET_MAGIC_APT_ABL:
				epet.addBornmagicApt( (int)effect.value );
				changedAttrs.put(AttrType.PET_MAGIC_APT, (float)epet.getMagicApt() );
				break;
			case EffectType.PET_PHYFORCE_APT_ABL:
				epet.addBornphyforceApt( (int)effect.value );
				changedAttrs.put(AttrType.PET_PHYFORCE_APT, (float)epet.getPhyforceApt() );
				break;
			case EffectType.PET_SPEED_APT_ABL:
				epet.addBornspeedApt( (int)effect.value );
				changedAttrs.put(AttrType.PET_SPEED_APT, (float)epet.getSpeedApt() );
				break;
			}
		}
		changedAttrs.putAll(epet.updateAllFinalAttrs());
		return changedAttrs;		
	}

	public Map<Integer,Float> useItem(List<fire.pb.item.Effect> effects)
	{
		fire.pb.effect.Role erole = new fire.pb.effect.RoleImpl(roleId);
		Map<Integer,Float> changedAttrs = new HashMap<Integer,Float>();
		float improve = 1f;
		SkillRole srole = new SkillRole(roleId);
		if(srole.hasSkill(SkillConstant.SPECIAL_SKILL_SHEN_NONG))
			improve = 1.1f;
		
		for(fire.pb.item.Effect effect : effects)
		{
			switch(effect.effectid)
			{
			case EffectType.HP_ABL:
				erole.addHp((int)(effect.value* improve));
				changedAttrs.put(AttrType.HP, (float)erole.getHp());
				break;
			case EffectType.HP_PCT:
				erole.addHp((int)(erole.getMaxHp() * Float.valueOf(effect.value)* improve));
				changedAttrs.put(AttrType.HP, (float)erole.getHp());
				break;				
			case EffectType.MP_ABL:
				erole.addMp((int)(effect.value* improve));
				changedAttrs.put(AttrType.MP, (float)erole.getMp());
				break;
			case EffectType.MP_PCT:
				erole.addMp((int)(erole.getMaxMp() * Float.valueOf(effect.value) * improve));
				changedAttrs.put(AttrType.MP, (float)erole.getMp());
				break;
			}
		}
		changedAttrs.putAll(erole.updateAllFinalAttrs());
		
		return changedAttrs;
	}
	
	public Result equip(int equiptype, Map<Integer, Float> effects, final List<Integer> skills) {
		
		Result result = new Result(true);
		BuffAgent agent = new BuffRoleImpl(roleId);
		
		List<BuffUnit> buffArgs = null;
		SkillRole srole = new SkillRole(roleId);
		srole.removeSpecialSkillWithSP(equiptype);
		SceneJSEngine engine = new SceneJSEngine();
		RoleImpl erole = new RoleImpl(roleId,false);
		engine.setOpERole(erole);
		
		for (Integer effectid : skills) {
			try {
				FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(effectid);
				if (sconf != null) {
					if (sconf.isUseInBattle()&& sconf.getSubSkills()[0] != null&& sconf.getSubSkills()[0].getBuffUnits()[0] != null) {
						continue;
					}
				}
				
				buffArgs = fire.pb.item.Module.getItemBuffs(effectid);
				if (buffArgs == null || buffArgs.size() < 1)
					continue;
				
				for (BuffUnit buffarg : buffArgs) {
					if (buffarg == null)
						continue;
					ConstantlyBuffConfig buffconf = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(buffarg.buffIndex);
					if (buffconf == null)
						continue;
					if(SkillConstant.BATTLE_EQUIP_LESS_SP_SKILL <= effectid && effectid <= SkillConstant.SPECIAL_SKILL_GOD_EYE)
					{
						ConstantlyBuff buff =fire.pb.buff.Module.getInstance().createConstantlyBuff(buffarg.buffIndex);
						ConstantlyBuff cbuff = (ConstantlyBuff)buff.copy();
						if(buffarg.effectJavascriptMap!=null&&buffarg.effectJavascriptMap.size() > 0)
						{
							Map<Integer,Float> buffeffects = calcEffects(engine, buffarg.effectJavascriptMap);
							cbuff.setEffects(buffeffects);
						}
						result.updateResult(agent.addCBuff(cbuff));
					}
					else
					{
						for (Entry<Integer,Float> effectattr : buffconf.getEffects().entrySet()) {
							int key = effectattr.getKey();
							float value = effectattr.getValue();
							if(effects.containsKey(key))
								effects.put(key, effects.get(key)+value);
							else
								effects.put(key, value);
						}
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (effects != null) {
			ConstantlyBuff buff = BuffManager.createEquipmentBuff(equiptype,
					effects);
			if (buff != null) {
				result.updateResult(agent.addCBuff(buff));
			}
		}
		
		srole.addSpecialSkillWithSP(equiptype, skills);
		
		return result;
	}
	
	private Map<Integer,Float> calcEffects(SceneJSEngine engine,Map<Integer,JavaScript> scripts)
	{
		Map<Integer,Float> effects = new HashMap<Integer, Float>();
		
		for(Map.Entry<Integer, JavaScript> script : scripts.entrySet())
		{
			effects.put(script.getKey(),script.getValue().eval(engine,null,null).floatValue());
		}
		
		return effects;
	}
	
	public DemoResult useItemInBattle(xbean.BattleInfo battle, final int operater, final int aim, List<BuffUnit> buffArgs)
	{
		
		DemoResult result = new DemoResult();

		for(BuffUnit buffarg : buffArgs)
		{
			int buffType = buffarg.buffIndex/10000;
			IBuff buff = null;
			switch (buffType)
			{
			case 50:
				buff = fire.pb.buff.Module.getInstance().createConstantlyBuff(buffarg.buffIndex);
				break;
			case 51:
				buff = fire.pb.buff.Module.getInstance().createInstantBuff(buffarg.buffIndex);
				break;
			default:
			}
			if(buff == null)
				continue;
			
		}
		
		
		return result;

	}
	
	public Result unequip(int equipType,List<ConstantlyBuffConfig> cbuffs)
	{
		Result result = new Result(true);
		BuffAgent agent = new BuffRoleImpl(roleId);
		Integer buffid = fire.pb.buff.BuffManager.getBuffIdByEquipType((equipType));
		if (buffid != null)
			result.updateResult(agent.removeCBuff(buffid));
		SkillRole srole = new SkillRole(roleId);
		srole.removeSpecialSkillWithSP(equipType);
		return result;
	}
	
	public boolean addEquipEffectAndSkillWithSP(EquipItem equipItem)
	{
		if (equipItem.getExtInfo().getEndure() <= 0)
			return false;

		Result r = addEquipEffectAndSkill(equipItem);
		if (r == null)
			return false;

		SRefreshRoleData snd = new SRefreshRoleData();
		snd.datas.putAll(fire.pb.effect.Module.getClientAttrs(r
				.getChangedAttrs()));
		if (Transaction.current() != null)
			mkdb.Procedure.psendWhileCommit(roleId, snd);
		else
			gnet.link.Onlines.getInstance().send(roleId, snd);
		return true;
	}
	
	public Result addEquipEffectAndSkill(EquipItem equipItem)
	{
		Map<Integer, Float> effects = new HashMap<Integer, Float>();
		List<ConstantlyBuffConfig> buffs = new ArrayList<ConstantlyBuffConfig>();
		equipItem.getEffectsAndBuffs(effects, buffs);

		xbean.Equip equipAttr = equipItem.getExtInfo();
		List<Integer> skills = new ArrayList<Integer>();
		if (equipAttr.getSkill() > 0) {
			skills.add(equipAttr.getSkill());
		}
		if (equipAttr.getEffect() > 0) {
			skills.add(equipAttr.getEffect());
		}

		Result result = new Result(true);
		result.updateResult(equip(equipItem.getEquipType(), effects, skills));
		return result;
	}
	
	public boolean removeEquipEffectAndSkillWithSP(EquipItem equipItem)
	{
		HashMap<Integer,Float> effects = new java.util.HashMap<Integer,Float>();
		ArrayList<ConstantlyBuffConfig> cbuffs = 
			new java.util.ArrayList<ConstantlyBuffConfig>();
		equipItem.getEffectsAndBuffs(effects, cbuffs);
		Result r = unequip(equipItem.getEquipType(), cbuffs);
		SRefreshRoleData snd = new SRefreshRoleData();
		snd.datas.putAll(fire.pb.effect.Module.getClientAttrs(r.getChangedAttrs()));
		if(Transaction.current() != null)
			mkdb.Procedure.psendWhileCommit(roleId, snd);
		else
			gnet.link.Onlines.getInstance().send( roleId, snd );
		return true;
	}
	
}
