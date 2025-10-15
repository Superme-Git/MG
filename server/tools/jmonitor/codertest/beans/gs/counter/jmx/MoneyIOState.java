package gs.counter.jmx;

public class MoneyIOState implements MoneyIOStateMXBean {

	private java.util.HashMap<gs.counter.bean.MoneyStatKey, gs.counter.bean.MoneyIOCount> map = new java.util.HashMap<gs.counter.bean.MoneyStatKey, gs.counter.bean.MoneyIOCount>();

	private static class GetTask extends com.locojoy.jmonitor.counter.GetTask {
		private java.util.HashMap<gs.counter.bean.MoneyStatKey, gs.counter.bean.MoneyIOCount> result = null;
		@Override
		public void doGetTask() {
			result = MoneyIOState.getInstance().map;
			MoneyIOState.getInstance().map = new java.util.HashMap<gs.counter.bean.MoneyStatKey, gs.counter.bean.MoneyIOCount>();
		}
		public java.util.HashMap<gs.counter.bean.MoneyStatKey, gs.counter.bean.MoneyIOCount> getResult() {
			return result;
		}
	}

	@Override
	public java.util.Map<gs.counter.bean.MoneyStatKey, gs.counter.bean.MoneyIOCount> getMoneyIOState() {
		GetTask task = new GetTask();
		task.runTask();
		return task.getResult();
	}

	final private gs.counter.bean.MoneyIOCount get( gs.counter.bean.MoneyStatKey key) {
		gs.counter.bean.MoneyIOCount result = map.get( key);
		if( null == result) {
			result = new gs.counter.bean.MoneyIOCount();
			map.put( key, result);
		}
		return result;
	}

	public void setVariable_incount( long roleid, int mtype, long asid, long _value_) {
		gs.counter.bean.MoneyStatKey beankey = new gs.counter.bean.MoneyStatKey( gs.counter.impl.RoleLoginLogout.getInstance().get( roleid), mtype, asid);
		gs.counter.bean.MoneyIOCount beanvalue = get( beankey);
		beanvalue.incount += _value_;
	}

	public void setVariable_outcount( long roleid, int mtype, long asid, long _value_) {
		gs.counter.bean.MoneyStatKey beankey = new gs.counter.bean.MoneyStatKey( gs.counter.impl.RoleLoginLogout.getInstance().get( roleid), mtype, asid);
		gs.counter.bean.MoneyIOCount beanvalue = get( beankey);
		beanvalue.outcount += _value_;
	}

	private MoneyIOState() {
	}
	static final private MoneyIOState instance = new MoneyIOState();
	static public MoneyIOState getInstance() {
		return instance;
	}
}

