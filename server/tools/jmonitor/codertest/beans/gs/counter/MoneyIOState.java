package gs.counter;

public abstract class MoneyIOState {

	protected abstract void raw_setincountValue( long roleid, int mtype, long asid, long _value_);
	protected abstract void raw_setoutcountValue( long roleid, int mtype, long asid, long _value_);

	private class SetincountValueTask extends xdb.Procedure.Task { 

		private long roleid;
		private int mtype;
		private long asid;
		private long _value_;

		public SetincountValueTask( long roleid, int mtype, long asid, long _value_) {
			this.roleid = roleid;
			this.mtype = mtype;
			this.asid = asid;
			this._value_ = _value_;
		}

		@Override
		public void run() {
			raw_setincountValue( roleid, mtype, asid, _value_);
		}

	}

	public void setincountValue( long roleid, int mtype, long asid, long _value_) {
		if( null != xdb.Transaction.current())
			xdb.Procedure.ppostWhileCommit( new SetincountValueTask( roleid, mtype, asid, _value_));
		else
			raw_setincountValue( roleid, mtype, asid, _value_);
	}

	private class SetoutcountValueTask extends xdb.Procedure.Task { 

		private long roleid;
		private int mtype;
		private long asid;
		private long _value_;

		public SetoutcountValueTask( long roleid, int mtype, long asid, long _value_) {
			this.roleid = roleid;
			this.mtype = mtype;
			this.asid = asid;
			this._value_ = _value_;
		}

		@Override
		public void run() {
			raw_setoutcountValue( roleid, mtype, asid, _value_);
		}

	}

	public void setoutcountValue( long roleid, int mtype, long asid, long _value_) {
		if( null != xdb.Transaction.current())
			xdb.Procedure.ppostWhileCommit( new SetoutcountValueTask( roleid, mtype, asid, _value_));
		else
			raw_setoutcountValue( roleid, mtype, asid, _value_);
	}

	static public final int mtype_Value_Quest = 0;
	static public final int mtype_Value_ItemSellBuy = 1;

	public void setincountQuestValue( long roleid, long asid, long _value_) {
		setincountValue( roleid, mtype_Value_Quest, asid,  _value_);
	}

	public void setincountItemSellBuyValue( long roleid, long asid, long _value_) {
		setincountValue( roleid, mtype_Value_ItemSellBuy, asid,  _value_);
	}

	public void setoutcountQuestValue( long roleid, long asid, long _value_) {
		setoutcountValue( roleid, mtype_Value_Quest, asid,  _value_);
	}

	public void setoutcountItemSellBuyValue( long roleid, long asid, long _value_) {
		setoutcountValue( roleid, mtype_Value_ItemSellBuy, asid,  _value_);
	}

}

