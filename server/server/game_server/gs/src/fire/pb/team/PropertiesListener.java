package fire.pb.team;

import com.locojoy.base.Runnable;

import mkdb.logs.Note;
/**
 * Properties表监听器，监听hp，mp的变化
 * 
 * @author nobody
 *
 */
public class PropertiesListener implements mkdb.logs.Listener {

	@Override
	public void onChanged(Object key) {
	
	}

	@Override
	public void onRemoved(Object key) {
		
	}

	@Override
	public void onChanged(Object key, String fullVarName, Note note) {
		
		long roleId = (Long)key;
		mkdb.Executor.getInstance().submit(new NotifyMemberHpMpChange(roleId));
	}
	
	public class NotifyMemberHpMpChange extends Runnable
	{

		private long roleId;
		
		NotifyMemberHpMpChange(long roleId)
		{
			this.roleId = roleId;
		}

		@Override
		public void run()
		{
			Long battleId = xtable.Roleid2battleid.select(roleId);
			if(battleId != null)
				return;//战斗中不广播给队友
			Team team = TeamManager.selectTeamByRoleId(roleId);
			if(team == null)
				return;//无队伍
			team.notifyHpMpChange(roleId);
			return;
		}
		
	}
}
