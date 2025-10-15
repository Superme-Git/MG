package gs.counter;

public abstract class RoleLoginLogout {
	protected abstract void raw_roleLogin( long roleid, int level, int sex, int menpai);
	protected abstract void raw_roleLogout( long roleid);
	protected abstract void raw_roleLevelUp( long roleid, int level, int sex, int menpai);

	private class RoleLoginTask extends xdb.Procedure.Task { 

		private long roleid;
		private int level;
		private int sex;
		private int menpai;

		public RoleLoginTask( long roleid, int level, int sex, int menpai) {
			this.roleid = roleid;
			this.level = level;
			this.sex = sex;
			this.menpai = menpai;
		}

		@Override
		public void run() {
			raw_roleLogin( roleid, level, sex, menpai);
		}
	}

	public void roleLogin( long roleid, int level, int sex, int menpai) {
		if( null != xdb.Transaction.current())
			xdb.Procedure.ppostWhileCommit( new RoleLoginTask( roleid, level, sex, menpai));
		else
			raw_roleLogin( roleid, level, sex, menpai);
	}

	private class RoleLogoutTask extends xdb.Procedure.Task { 

		private long roleid;

		public RoleLogoutTask( long roleid) {
			this.roleid = roleid;
		}

		@Override
		public void run() {
			raw_roleLogout( roleid);
		}
	}

	public void roleLogout( long roleid) {
		if( null != xdb.Transaction.current())
			xdb.Procedure.ppostWhileCommit( new RoleLogoutTask( roleid));
		else
			raw_roleLogout( roleid);
	}

	private class RoleLevelUpTask extends xdb.Procedure.Task { 

		private long roleid;
		private int level;
		private int sex;
		private int menpai;

		public RoleLevelUpTask( long roleid, int level, int sex, int menpai) {
			this.roleid = roleid;
			this.level = level;
			this.sex = sex;
			this.menpai = menpai;
		}

		@Override
		public void run() {
			raw_roleLevelUp( roleid, level, sex, menpai);
		}
	}

	public void roleLevelUp( long roleid, int level, int sex, int menpai) {
		if( null != xdb.Transaction.current())
			xdb.Procedure.ppostWhileCommit( new RoleLevelUpTask( roleid, level, sex, menpai));
		else
			raw_roleLevelUp( roleid, level, sex, menpai);
	}

}

