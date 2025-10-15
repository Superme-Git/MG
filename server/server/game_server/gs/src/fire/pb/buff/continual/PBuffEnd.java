package fire.pb.buff.continual;

import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;

public class PBuffEnd extends mkdb.Procedure
{
	final protected long roleid;
	final protected int buffId;
	
	public PBuffEnd(long roleid, int buffId)
	{
		this.roleid = roleid;
		this.buffId = buffId;
	}
	
	@Override
	protected boolean process()
	{
		BuffAgent agent = new BuffRoleImpl(roleid);
		if (agent.existBuff(BuffConstant.StateType.STATE_BATTLE_FIGHTER))
		{

			agent.removeCBuffWhileBattleEnd(buffId);
			fire.pb.buff.BuffAgent.logger.info("Role:" + roleid+" BUFF: " + buffId + " will be removed while battle end.");
		} else
		{
			boolean succ = agent.removeTimeoutCBuffWithSP(buffId);
			if(!succ)
				fire.pb.buff.BuffAgent.logger.error("角色:" + roleid+" BUFF超时时: " + buffId + " 清除失败。");
			if(agent.existBuffBean(buffId))
				fire.pb.buff.BuffAgent.logger.error("角色:" + roleid+" BUFF超时时: " + buffId + " 还存在。");
			fire.pb.buff.BuffAgent.logger.info("Role:" + roleid+" BUFF: " + buffId + " has disappeared.(time out)");

		}
		return true;
	}
}
