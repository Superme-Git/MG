package fire.pb.compensation;

import fire.pb.talk.MessageMgr;

public class PGetRoleSingleCompensition extends mkdb.Procedure
{
	private final long roleId;
	
	public PGetRoleSingleCompensition(long roleId)
	{
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception
	{
		if(xtable.Compensationroles.select(roleId) == null)
		{
			MessageMgr.psendMsgNotify(roleId, 143054, null);
			return true;
		}
		
		CompensationRole role = CompensationRole.getSingleCompensation(roleId, false);
		boolean b = false;
		if (b) {
			return role.fetchCompensitions();
		}
		return false;
	}
}
