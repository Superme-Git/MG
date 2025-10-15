package gs.counter.jmx;

public class LevelRoles implements LevelRolesMXBean {

	private java.util.HashMap<gs.counter.bean.LevelStatKey, gs.counter.bean.LevelRoleCount> map = new java.util.HashMap<gs.counter.bean.LevelStatKey, gs.counter.bean.LevelRoleCount>();

	private static class GetTask extends com.locojoy.jmonitor.counter.GetTask {
		private java.util.HashMap<gs.counter.bean.LevelStatKey, gs.counter.bean.LevelRoleCount> result = null;
		@Override
		public void doGetTask() {
			@SuppressWarnings("unchecked")
			final java.util.HashMap<gs.counter.bean.LevelStatKey, gs.counter.bean.LevelRoleCount> temp = (java.util.HashMap<gs.counter.bean.LevelStatKey, gs.counter.bean.LevelRoleCount>)LevelRoles.getInstance().map.clone();
			result = temp;
		}
		public java.util.HashMap<gs.counter.bean.LevelStatKey, gs.counter.bean.LevelRoleCount> getResult() {
			return result;
		}
	}

	@Override
	public java.util.Map<gs.counter.bean.LevelStatKey, gs.counter.bean.LevelRoleCount> getLevelRoles() {
		GetTask task = new GetTask();
		task.runTask();
		return task.getResult();
	}

	final private gs.counter.bean.LevelRoleCount get( gs.counter.bean.LevelStatKey key) {
		gs.counter.bean.LevelRoleCount result = map.get( key);
		if( null == result) {
			result = new gs.counter.bean.LevelRoleCount();
			map.put( key, result);
		}
		return result;
	}

	public void setVariable_count( long roleid, long _value_) {
		gs.counter.bean.LevelStatKey beankey = gs.counter.impl.RoleLoginLogout.getInstance().get( roleid);
		gs.counter.bean.LevelRoleCount beanvalue = get( beankey);
		beanvalue.count += _value_;
	}

	public void setRoleCountValue( gs.counter.bean.LevelStatKey beankey, long _value_) {
		gs.counter.bean.LevelRoleCount beanvalue = get( beankey);
		beanvalue.count += _value_;
	}

	private LevelRoles() {
	}
	static final private LevelRoles instance = new LevelRoles();
	static public LevelRoles getInstance() {
		return instance;
	}
}

