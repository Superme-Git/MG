package gs.counter.impl;

public class RoleLoginLogout extends gs.counter.RoleLoginLogout {

	final private com.locojoy.jmonitor.counter.RoleMap<gs.counter.bean.LevelStatKey> rolemap = new com.locojoy.jmonitor.counter.RoleMap<gs.counter.bean.LevelStatKey>( new gs.counter.bean.LevelStatKey());

	static private class RoleLoginTask extends com.locojoy.jmonitor.counter.SetTask {
		private long roleid;
		private gs.counter.bean.LevelStatKey data;

		public RoleLoginTask( long roleid, gs.counter.bean.LevelStatKey data) {
			this.roleid = roleid;
			this.data = data;
		}

		@Override
		public void doRunTask() {
			if( RoleLoginLogout.getInstance().rolemap.newRole( roleid, data))
				gs.counter.jmx.LevelRoles.getInstance().setRoleCountValue( data, 1);
		}
	};

	@Override
	protected void raw_roleLogin( long roleid, int level, int sex, int menpai) {
		new RoleLoginTask( roleid, new gs.counter.bean.LevelStatKey( level, sex, menpai)).runTask();
	}

	static private class RoleLogoutTask extends com.locojoy.jmonitor.counter.SetTask {
		private long roleid;

		public RoleLogoutTask( long roleid) {
			this.roleid = roleid;
		}

		@Override
		public void doRunTask() {
			gs.counter.bean.LevelStatKey data = RoleLoginLogout.getInstance().rolemap.removeRole( roleid);
			if( null != data)
				gs.counter.jmx.LevelRoles.getInstance().setRoleCountValue( data, -1);
		}
	};

	@Override
	protected void raw_roleLogout( long roleid) {
		new RoleLogoutTask( roleid).runTask();
	}

	static private class RoleLevelUpTask extends com.locojoy.jmonitor.counter.SetTask {
		private long roleid;
		private gs.counter.bean.LevelStatKey data;

		public RoleLevelUpTask( long roleid, gs.counter.bean.LevelStatKey data) {
			this.roleid = roleid;
			this.data = data;
		}

		@Override
		public void doRunTask() {
			new RoleLogoutTask( roleid).doRunTask();
			new RoleLoginTask( roleid, data).doRunTask();
		}
	};

	@Override
	protected void raw_roleLevelUp( long roleid, int level, int sex, int menpai) {
		new RoleLevelUpTask( roleid, new gs.counter.bean.LevelStatKey( level, sex, menpai)).runTask();
	}

	final static private RoleLoginLogout instance = new RoleLoginLogout();
	static public RoleLoginLogout getInstance() {
		return instance;
	}

	public gs.counter.bean.LevelStatKey get( long roleid) {
		return rolemap.getRole( roleid);
	}

}

