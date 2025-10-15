package fire.pb.triggers;

public class PBattleAiTrigger extends mkdb.Procedure
{
	private long roleId;
	private int triggerId;
	public PBattleAiTrigger(long roleId, int triggerId)
	{
		this.roleId = roleId;
		this.triggerId = triggerId;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		return new TriggerRole(roleId, false).trigger(triggerId);
	}
	
}
