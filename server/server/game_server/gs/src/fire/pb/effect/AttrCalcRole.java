package fire.pb.effect;

import java.util.HashMap;
import java.util.Map;

import fire.pb.attr.AttrType;
import fire.pb.attr.EffectType;
import fire.pb.attr.ScoreType;

public abstract class AttrCalcRole extends AttrRole{
	public float getAttrByName(String name)
	{
		int attrId = Module.getInstance().getAttrIdByName(name);
		if(attrId <= 0)
			return 0f;
		
		return getAttrById(attrId);
	}
	
	protected float calcEffectBonus(int attrType, float attrValue)
	{
		Float abl = getEffects().get(attrType + 1);
		if (abl == null)
			abl = 0f;
		
		Float pct = getEffects().get(attrType + 2);
		if (pct == null)
			pct = 0f;
		float value = (float) ((attrValue + abl) * (1 +  pct));
		return value;
	}
	
	@Override
	public void attachEffect(int effectType, float value)
	{
		if(!validEffectId(effectType))
			return;
		Float oldValue = getEffects().get(effectType);
		if (oldValue == null)
		{
			oldValue = 0f;
//			getEffects().put(effectType, value);
		} 
//		else
		{
			float newValue = oldValue;
			if(effectType %10 == 1)//值的效果以累加的结果保存
				newValue = oldValue + value;
				//newValue = oldValue + (int)value;  FIXME 记住，这里不再取整，因为有的效果就是小于1的，例如伤害修正类
			else if(effectType %10 == 2)//百分比的效果以累乘的结果保存
				newValue = oldValue + value;//(1+oldValue)*(1+value) -1;
			if(newValue != 0)
				getEffects().put(effectType, newValue);
			else
				getEffects().remove(effectType);
		}
	}
	
	public Float GetEffect(int effectType)
	{
		if(!validEffectId(effectType))
			return null;
		return getEffects().get(effectType);
	}

	@Override
	public void detachEffect(int effectType, float value)
	{
		if(!validEffectId(effectType))
			return;
		Float oldValue = getEffects().get(effectType);
		if (oldValue == null)
		{
			oldValue = 0f;
//			getEffects().put(effectType, value);
		} 
//		else
		{
			float newValue = oldValue;
			if(effectType %10 == 1)//值的效果以累加的结果保存
				newValue = oldValue - value;
				//newValue = oldValue - (int)value; FIXME 记住，这里不再取整，因为有的效果就是小于1的，例如伤害修正类
			else if(effectType %10 == 2)//百分比的效果以累乘的结果保存
				newValue = oldValue-value;/*(1+oldValue)/(1+value) -1*/
			if(newValue != 0)
				getEffects().put(effectType, newValue);
			else
				getEffects().remove(effectType);
		}
	}
	
	@Override
	public void attachEffects(Map<Integer, Float> effects)
	{
		if(effects == null)
			return;
		for (Integer effectType : effects.keySet())
		{
			attachEffect(effectType, effects.get(effectType));
		}
	}

	@Override
	public void detachEffects(Map<Integer, Float> effects)
	{
		if(effects == null)
			return;
		for (Integer effectType : effects.keySet())
		{
			detachEffect(effectType, effects.get(effectType));
		}
	}
	
	private boolean validEffectId(int effectId) 
	{
		switch(effectId)
		{
		case EffectType.HP_ABL:
		case EffectType.HP_PCT:
		case EffectType.MP_ABL:
		case EffectType.MP_PCT:
		case EffectType.UP_LIMITED_HP_ABL:
		case EffectType.UP_LIMITED_HP_PCT:
			return false;
		}
		return true;
	}
	@Override
	public Map<Integer, Float> updateAllFinalAttrs()
	{
		int uplimithp = this.getUplimithp();
		int maxmp = this.getMaxMp();
		Map<Integer, Float> finalAttrs = getFinalAttrs();
		Map<Integer, Float> changedFinalAttrs = new HashMap<Integer, Float>();
		for (int i = 0; i < Module.fightAttrTypeIds.length; i++)
		{
			Float newValue = 0f;
			switch (Module.fightAttrTypeIds[i])
			{
			case 0:
				break;
			case AttrType.CONS:
				newValue = calcCons();
				break;
			case AttrType.IQ:
				newValue = calcIq();
				break;
			case AttrType.STR:
				newValue = calcStr();
				break;
			case AttrType.ENDU:
				newValue = calcEndu();
				break;
			case AttrType.AGI:
				newValue = calcAgi();
				break;
			case AttrType.ATTACK:
				newValue = calcAttack();
				break;
			case AttrType.MAGIC_ATTACK:
				newValue = calcMagicAttack();
				break;
			case AttrType.MAGIC_DEF:
				newValue = calcMagicDef();
				break;
			case AttrType.DEFEND:
				newValue = calcDefend();
				break;
			case AttrType.HIT_RATE:
				newValue = calcHit();
				break;
			case AttrType.DODGE_RATE:
				newValue = calcDodge();
				break;
			case AttrType.SPEED:
				newValue = calcSpeed();
				break;
			case AttrType.MAX_HP:
				newValue = calcMaxHp();
				break;
			case AttrType.MAX_MP:
				newValue = calcMaxMp();
				break;
			case AttrType.ENLIMIT:
				newValue = calcEnlimit();
				break;
			case AttrType.PFLIMIT:
				newValue = calcPflimit();
				break;
			case AttrType.MEDICAL:
				newValue = calcMedical();
				break;
			case AttrType.SEAL:
				newValue = calcSeal();
				break;
			case AttrType.UNSEAL:
				newValue = calcUnseal();
				break;
			default:
				newValue= calcDefaultAttr(Module.fightAttrTypeIds[i]);
			}
			
			Float oldvalue = finalAttrs.get(Module.fightAttrTypeIds[i]);
			Float initvalue = Module.getInstance().getInitValueByAttrId(Module.fightAttrTypeIds[i]);
			//两种情况说明属性有变化：1.没有旧值 && 新值 != 初始值，2.有旧值 && 旧值 != 新值
			if(oldvalue == null && !newValue.equals(initvalue))
			{
				finalAttrs.put(Module.fightAttrTypeIds[i], newValue);
				changedFinalAttrs.put(Module.fightAttrTypeIds[i], newValue);
			}
			else if(oldvalue != null && !oldvalue.equals(newValue))
			{
				if(newValue.equals(initvalue))
					finalAttrs.remove(Module.fightAttrTypeIds[i]);
				else
					finalAttrs.put(Module.fightAttrTypeIds[i], newValue);
				changedFinalAttrs.put(Module.fightAttrTypeIds[i], newValue);
			}
		}
		if(changedFinalAttrs.containsKey(AttrType.MAX_HP) )
		{
			int newuplimithp = this.getUplimithp();
			changedFinalAttrs.put(AttrType.UP_LIMITED_HP,(float)newuplimithp);
			int newhp = Math.max(1 , this.getHp()+newuplimithp - uplimithp);
			this.setHp(newhp);
			changedFinalAttrs.put(AttrType.HP, (float)newhp);
		}
		if(changedFinalAttrs.containsKey(AttrType.MAX_MP))
		{
			int newmaxmp = this.getMaxMp();
			int newmp =  Math.max(1 , this.getMp()+newmaxmp - maxmp);
			this.setMp(newmp);
			changedFinalAttrs.put(AttrType.MP, (float)newmp);
		}
		return changedFinalAttrs;
	}
	
	
	
	@Override
	public int updateScore() {
		Double nScore = 0.0;
		double xisu = fire.pb.scoremanager.Module.getInstance().getCoe(ScoreType.ROLE_LEVEL_SCORE);
		int allbft = getBFP().getAgi() + getBFP().getStr() + getBFP().getCons() + getBFP().getEndu() + getBFP().getIq();
		nScore = allbft * xisu;
		
		return nScore.intValue();
	}
	@Override
	protected Map<Integer, Float> getFinalAttrs() {
		// TODO Auto-generated method stub
		return null;
	}
	protected abstract Map<Integer,Float> getEffects();
	protected abstract xbean.BasicFightProperties getBFP();	
	
	protected abstract float calcPflimit();
	protected abstract float calcEnlimit();
	protected abstract float calcMaxMp();
	protected abstract float calcMaxHp();
	protected abstract float calcSpeed();
	protected abstract float calcDodge();
	protected abstract float calcHit();
	protected abstract float calcDefend();
	protected abstract float calcMagicDef();
	protected abstract float calcMagicAttack();
	protected abstract float calcAttack();
	protected abstract float calcMedical();
	protected abstract float calcSeal();
	protected abstract float calcUnseal();
	
	protected float calcCons()
	{
		return calcEffectBonus(AttrType.CONS, getBFP().getCons());
	}

	protected float calcIq()
	{
		return calcEffectBonus(AttrType.IQ, getBFP().getIq());
	}

	protected float calcStr()
	{
		return calcEffectBonus(AttrType.STR, getBFP().getStr());
	}

	protected float calcEndu()
	{
		return calcEffectBonus(AttrType.ENDU, getBFP().getEndu());
	}

	protected float calcAgi()
	{
		return calcEffectBonus(AttrType.AGI, getBFP().getAgi());
	}
	
	protected float calcDefaultAttr(int attrType)
	{
		return calcEffectBonus(attrType, Module.getInstance().getInitValueByAttrId(attrType));//放入初值
	}
}
