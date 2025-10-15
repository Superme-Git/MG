package fire.pb.state;


public class PRoleOnline extends mkdb.Procedure {
	final private long roleId;
	final private int userId;

	public long getRoleId() {
		return roleId;
	}

	public int getUserId() {
		return userId;
	}

	public PRoleOnline(int userId, long roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}

	@Override
	protected boolean process() {
		StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 角色开始登录：帐号userId = ").append(userId)
				.append(this.getClass()));
		Long battleId = xtable.Roleid2battleid.select(roleId);
		if (battleId != null) {
			Object[] battleids = new Object[] { battleId };
			lock(mkdb.Lockeys.get(xtable.Locks.BATTLE, battleids));// 如果战斗内上线，先锁battlelock
		}
		Object[] userids = new Object[] { userId };
		lock(mkdb.Lockeys.get(xtable.Locks.USERLOCK, userids));// 锁userlock

		final xbean.User u = xtable.User.get(userId);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, u.getIdlist()));// 锁全部的rolelock
		// xtable.Roleminigame.remove( roleId );
//		for (long rid : u.getIdlist()) {
//			if (rid != roleId) {
//				int stateid = StateManager.getStateIdByRoleId(rid);
//				if (stateid != State.UNENTRY_STATE && stateid != State.OFFLINE_PROTECT_STATE) {
//					StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 角色登录时，该账号的另一个角色id = ")
//							.append(rid).append("还处在状态").append(stateid));
//					return false;
//				}
//			}
//		}

		IState state = StateManager.getStateByRoleId(roleId);
		if (state.getState() == State.UNENTRY_STATE) {
			xtable.Roleid2battlemelonid.remove(roleId);
		}

		if (state.trigger(State.TRIGGER_ONLINE)) {
			// 上线成功
			StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 帐号userId = ").append(userId)
					.append("上线成功。"));
			// 记录远程日志
			// doRoleLoginLog(roleId,userId);
			
			// 运营日志
			fire.log.YYLogger.OpLog(userId, roleId, "login", u.getIsfirst(), 1);
			
			// 运营日志
			fire.log.YYLogger.rolNameLog(userId, roleId, u.getIsfirst());
			
			// 登陆日志
			if (u.getIsfirst() == 0) {
				u.setIsfirst(1);
			}

			return true;
		} else
			return false;// 上线失败的处理在DRoleOnlineFail中处理
	}

}
