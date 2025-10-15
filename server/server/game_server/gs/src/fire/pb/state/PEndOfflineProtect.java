package fire.pb.state;

import java.util.ArrayList;
import java.util.List;

import fire.msp.role.GRemoveRoleFromScene;
import fire.pb.GsClient;
import fire.pb.circletask.catchit.CatchItQuest;


public class PEndOfflineProtect extends mkdb.Procedure
{

	final private long roleId;
	
	public PEndOfflineProtect(long roleId)
	{
		this.roleId = roleId;
	}
	
	@Override
	protected boolean process()
	{
		List<Long> ids = new ArrayList<Long>();
		ids.add(roleId);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, ids));//先锁rolelock
		// 清除锁屏状态		
		if(xtable.Rolelockscreen.select(roleId) != null)
			xtable.Rolelockscreen.remove(roleId);
		IState state = StateManager.getStateByRoleId(roleId);
		state.trigger(State.TRIGGER_TIME_OUT);
		GsClient.pSendWhileCommit(new GRemoveRoleFromScene(roleId));
		
		//清除角色刷出的任务怪
		CatchItQuest.clearCathcItQuestNpc(roleId);
		return true;
	}
}
