package fire.pb.compensation;

import fire.pb.talk.MessageMgr;
import mkdb.Lockeys;

public class PCancelSingleCompensition extends mkdb.Procedure
{
	private final long compenskey;
	
	public PCancelSingleCompensition(long compenskey)
	{
		this.compenskey = compenskey;
	}

	@Override
	protected boolean process() throws Exception
	{
		
		xbean.SingleCompensation xcompens = xtable.Singlecompensations.get(compenskey);
		
		if(xcompens == null)
		{
			return true;
		}
		
		xtable.Singlecompensations.remove(compenskey);
		
		lock(Lockeys.get(xtable.Locks.COMPENSATIONROLES, xcompens.getRoleids()));
		
		for(long roleId : xcompens.getRoleids())
		{
			xbean.CompensationRole xrole =  xtable.Compensationroles.get(roleId);
			if(xrole == null)
				continue;
			Long gettime = xrole.getSinglecompensations().remove(compenskey);
			if(gettime == null || gettime != 0)
				continue;
			MessageMgr.psendSystemMessageToRole(roleId, 143058, null);
		}
		return true;
	}
}
