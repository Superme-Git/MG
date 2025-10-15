package fire.script;

import fire.pb.attr.FightAttrType;

public class SceneJSEngine extends AbstractJSEngine
{	
	public void setSkillLevel(int skilllv)
	{
		put("skilllevela", skilllv);
		put("skilllevel", skilllv);
		put("mineffect", 0);
	}
	
	public void setQuality(int quality)
	{
		put("quality", quality);
	}
	
	public void setSkillTargetNum(int num)
	{
		put("effectnuma", num);
		put("effectnum", num);
	}
	
	public void setOpERole(fire.pb.effect.Role erole)
	{
		put("gradea", erole.getLevel());
		put("hpa", erole.getHp());
		put("mpa", erole.getMp());
		put("stra", erole.getStr());
		put("consa", erole.getCons());
		put("iqa", erole.getIq());
		put("endua", erole.getEndu());
		put("agia", erole.getAgi());
		put("attacka", erole.getAttack());
		put("defa", erole.getDefend());
		put("hita", erole.getHit());
		put("speeda", erole.getSpeed());
		put("maxhpa", erole.getMaxHp());
		put("uplimithpa", erole.getUplimithp());
		put("maxmpa", erole.getMaxMp());
		put("magicattacka", erole.getMagicAttack());
		put("magicdefa", erole.getMagicDef());
		put("doda", erole.getDefend());
		put("unseala", erole.getAttrById(FightAttrType.UNSEAL));
	}
	public void setAimERole(fire.pb.effect.Role erole)
	{
		put("gradeb", erole.getLevel());
		put("hpb", erole.getHp());
		put("mpb", erole.getMp());
		put("strb", erole.getStr());
		put("consb", erole.getCons());
		put("iqb", erole.getIq());
		put("endub", erole.getEndu());
		put("agib", erole.getAgi());
		put("defb", erole.getDefend());
		put("hitb", erole.getHit());
		put("speedb", erole.getSpeed());
		put("maxhpb", erole.getMaxHp());
		put("uplimithpb", erole.getUplimithp());
		put("maxmpb", erole.getMaxMp());
		put("attackb", erole.getAttack());
		put("magicattackb", erole.getMagicAttack());
		put("magicdefb", erole.getMagicDef());
		put("dodb", erole.getDodge());
		put("unsealb", erole.getAttrById(FightAttrType.UNSEAL));

	}
	
}
