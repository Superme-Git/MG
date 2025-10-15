package fire.pb.effect;

import java.util.HashMap;
import java.util.Map;

import fire.pb.attr.AttrType;

public abstract class AttrRole implements Role
{

	@Override
	public int addHp(int v)
	{
		if(v == 0)
			return 0;
		int oldvalue = this.getHp();
		this.setHp(Math.max(0, (int) Math.min((long) this.getUplimithp(), oldvalue + (long) v)));
		return this.getHp() - oldvalue;
	}
	@Override
	public int addMp(int v)
	{
		if(v == 0)
			return 0;
		int oldvalue = this.getMp();
		this.setMp(Math.max(0, (int) Math.min((long) this.getMaxMp(), oldvalue + (long) v)));
		return this.getMp() - oldvalue;
	}
	
	@Override
	public Map<Integer,Float> fullHp()
	{
		Map<Integer,Float> changedAttrs = new HashMap<Integer, Float>();
		int ulhp = this.getUplimithp();
		this.setHp(ulhp);
		changedAttrs.put(AttrType.HP, (float)ulhp);
		return changedAttrs;
	}
	
	@Override
	public Map<Integer,Float> fullMp()
	{
		Map<Integer,Float> changedAttrs = new HashMap<Integer, Float>();
		int maxmp = this.getMaxMp();
		this.setMp(maxmp);
		changedAttrs.put(AttrType.MP, (float)maxmp);
		return changedAttrs;
	}
	
	//获取当前hp占maxhp的百分比
	@Override
	public float getHpPercent()
	{
		int hp = this.getHp();
		float maxhp = getMaxHp();
		return hp / maxhp;
	}

	// 获取当前mp占maxmp的百分比
	@Override
	public float getMpPercent()
	{
		int mp = this.getMp();
		float maxmp = getMaxMp();
		return mp / maxmp;
	}
	public int getEnlimit()
	{
		return (int)getAttrById(AttrType.ENLIMIT);
	}

	public int getPflimit()
	{
		return (int)getAttrById(AttrType.PFLIMIT);
	}
	@Override
	public int getMaxHp()
	{
		return (int)getAttrById(AttrType.MAX_HP);
	}
	@Override
	public int getMaxSp()
	{
		return (int)getAttrById(AttrType.MAX_SP);
	}
	@Override
	public int getUplimithp()
	{
		int ulhp =  (getMaxHp()- getWound());
		if(ulhp <= 0)
			return 1;//最小为1
		return ulhp;
	}
	@Override
	public int getMaxMp()
	{
		return (int)getAttrById(AttrType.MAX_MP);
	}

	@Override
	public int getCons()
	{
		return (int)getAttrById(AttrType.CONS);
	}

	@Override
	public int getIq()
	{
		return (int)getAttrById(AttrType.IQ);
	}

	@Override
	public int getStr()
	{
		return (int)getAttrById(AttrType.STR);
	}

	@Override
	public int getEndu()
	{
		return (int)getAttrById(AttrType.ENDU);
	}

	@Override
	public int getAgi()
	{
		return (int)getAttrById(AttrType.AGI);
	}
	
	@Override
	public int getHit()
	{
		return (int)getAttrById(AttrType.HIT_RATE);
	}

	@Override
	public int getAttack()
	{
		return (int)getAttrById(AttrType.ATTACK);
	}

	@Override
	public int getDefend()
	{
		return (int)getAttrById(AttrType.DEFEND);
	}

	@Override
	public int getSpeed()
	{
		return (int)getAttrById(AttrType.SPEED);
	}

	@Override
	public int getDodge()
	{
		return (int)getAttrById(AttrType.DODGE_RATE);
	}

	@Override
	public int getMagicAttack()
	{
		return (int)getAttrById(AttrType.MAGIC_ATTACK);
	}
	
	@Override
	public int getMagicDef()
	{
		return (int)getAttrById(AttrType.MAGIC_DEF);
	}
	
	@Override
	public float getAttrById(int attrId)
	{
		if(attrId == AttrType.HP)
			return getHp();
		else if(attrId == AttrType.MP)
			return getMp();
		else if(attrId == AttrType.SP)
			return getSp();
		Float value = getFinalAttrs().get(attrId);
		if(value == null)
		{
			value = Module.getInstance().getInitValueByAttrId(attrId);
			if(value == null)
				return 0f;
			else
				return value;
		}
		else
			return value;
	}
	protected abstract Map<Integer,Float> getFinalAttrs();
}
