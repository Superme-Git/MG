package fire.pb.effect;

import fire.pb.attr.AttrType;
import fire.pb.attr.SRefreshPetData;
import fire.pb.pet.PetColumnTypes;


public class PRefreshPetAttr extends mkdb.Procedure
{
	private final long roleId;
	private final int petkey;
	public PRefreshPetAttr(long roleId, int petkey)
	{
		this.roleId = roleId;
		this.petkey = petkey;
	}
	
	@Override
	protected boolean process()
	{
		PetImpl erole = new PetImpl(roleId,petkey,true);
		
		SRefreshPetData snd = new SRefreshPetData();
		snd.columnid = PetColumnTypes.PET;
		snd.petkey = petkey;
		snd.datas.put(AttrType.CONS, (float)erole.getCons());
		snd.datas.put(AttrType.IQ, (float)erole.getIq());
		snd.datas.put(AttrType.STR, (float)erole.getStr());
		snd.datas.put(AttrType.AGI, (float)erole.getAgi());
		snd.datas.put(AttrType.ENDU, (float)erole.getEndu());
		snd.datas.put(AttrType.MAX_HP, (float)erole.getMaxHp());
		snd.datas.put(AttrType.HP, (float)erole.getHp());
		snd.datas.put(AttrType.MAX_MP, (float)erole.getMaxMp());
		snd.datas.put(AttrType.MP, (float)erole.getMp());
		snd.datas.put(AttrType.ATTACK, (float)erole.getAttack());
		snd.datas.put(AttrType.DEFEND, (float)erole.getDefend());
		snd.datas.put(AttrType.HIT_RATE, (float)erole.getHit());
		snd.datas.put(AttrType.DODGE_RATE, (float)erole.getDodge());
		snd.datas.put(AttrType.MAGIC_ATTACK, (float)erole.getMagicAttack());
		snd.datas.put(AttrType.MAGIC_DEF, (float)erole.getMagicDef());
		
		psend(roleId, snd);
		
		return true;
	}
}
