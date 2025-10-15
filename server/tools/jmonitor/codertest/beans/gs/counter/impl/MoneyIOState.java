package gs.counter.impl;

public class MoneyIOState extends gs.counter.MoneyIOState {

	static private class SetIncountTask extends com.locojoy.jmonitor.counter.SetTask {

		long _value_;
		long roleid;
		int mtype;
		long asid;

		public SetIncountTask( long roleid, int mtype, long asid, long _value_) {
			this._value_ = _value_;
			this.roleid = roleid;
			this.mtype = mtype;
			this.asid = asid;
		}

		@Override
		public void doRunTask() {
			gs.counter.jmx.MoneyIOState.getInstance().setVariable_incount( roleid, mtype, asid, _value_);
		}
	};

	@Override
	protected void raw_setincountValue( long roleid, int mtype, long asid, long _value_) {
		new SetIncountTask( roleid, mtype, asid, _value_).runTask();
	}

	static private class SetOutcountTask extends com.locojoy.jmonitor.counter.SetTask {

		long _value_;
		long roleid;
		int mtype;
		long asid;

		public SetOutcountTask( long roleid, int mtype, long asid, long _value_) {
			this._value_ = _value_;
			this.roleid = roleid;
			this.mtype = mtype;
			this.asid = asid;
		}

		@Override
		public void doRunTask() {
			gs.counter.jmx.MoneyIOState.getInstance().setVariable_outcount( roleid, mtype, asid, _value_);
		}
	};

	@Override
	protected void raw_setoutcountValue( long roleid, int mtype, long asid, long _value_) {
		new SetOutcountTask( roleid, mtype, asid, _value_).runTask();
	}


	static private final MoneyIOState instance = new MoneyIOState();
	static public MoneyIOState getInstance() {
		return instance;
	}

}

