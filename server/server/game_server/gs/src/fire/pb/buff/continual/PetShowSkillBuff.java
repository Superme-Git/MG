package fire.pb.buff.continual;

import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.skill.Result;

public class PetShowSkillBuff extends ConstantlyBuff
{
	public PetShowSkillBuff(xbean.Buff buffBean)
	{
		super(buffBean);
	}
	public PetShowSkillBuff(ConstantlyBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	@Override
	public Result closure(BuffAgent buffAgent, boolean timeout)
	{
		Result result = super.closure(buffAgent, timeout);
		if(result == null || !result.isSuccess())
			return result;
		if(timeout)
		{//时间到了，换另一个buff
			if(buffAgent instanceof BuffRoleImpl)
			{
				long roleId = ((BuffRoleImpl)buffAgent).getRoleId();
				int showpetkey = xtable.Properties.selectShowpetkey(roleId);
				if(showpetkey < 0)
					return result;
				PetColumn petcol = new PetColumn(roleId, PetColumnTypes.PET, false);
				petcol.addShowSkillBuff();
				//通知场景
				Pet pet = petcol.getPet(showpetkey);
				fire.msp.move.GNotifyMapPetInfo send = new fire.msp.move.GNotifyMapPetInfo(roleId, pet.getShowPetInfo());
				fire.pb.GsClient.pSendWhileCommit(send);	
					
			}
		}
		return result;
	}
}
