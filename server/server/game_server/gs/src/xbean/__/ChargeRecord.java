
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ChargeRecord extends mkdb.XBean implements xbean.ChargeRecord {
	private int chargecount; // 已经充值次数
	private long chargetime; // 上次的时间

	@Override
	public void _reset_unsafe_() {
		chargecount = 0;
		chargetime = 0L;
	}

	ChargeRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ChargeRecord() {
		this(0, null, null);
	}

	public ChargeRecord(ChargeRecord _o_) {
		this(_o_, null, null);
	}

	ChargeRecord(xbean.ChargeRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ChargeRecord) assign((ChargeRecord)_o1_);
		else if (_o1_ instanceof ChargeRecord.Data) assign((ChargeRecord.Data)_o1_);
		else if (_o1_ instanceof ChargeRecord.Const) assign(((ChargeRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ChargeRecord _o_) {
		_o_._xdb_verify_unsafe_();
		chargecount = _o_.chargecount;
		chargetime = _o_.chargetime;
	}

	private void assign(ChargeRecord.Data _o_) {
		chargecount = _o_.chargecount;
		chargetime = _o_.chargetime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(chargecount);
		_os_.marshal(chargetime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		chargecount = _os_.unmarshal_int();
		chargetime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ChargeRecord copy() {
		_xdb_verify_unsafe_();
		return new ChargeRecord(this);
	}

	@Override
	public xbean.ChargeRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChargeRecord toBean() {
		_xdb_verify_unsafe_();
		return new ChargeRecord(this); // same as copy()
	}

	@Override
	public xbean.ChargeRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChargeRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getChargecount() { // 已经充值次数
		_xdb_verify_unsafe_();
		return chargecount;
	}

	@Override
	public long getChargetime() { // 上次的时间
		_xdb_verify_unsafe_();
		return chargetime;
	}

	@Override
	public void setChargecount(int _v_) { // 已经充值次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "chargecount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, chargecount) {
					public void rollback() { chargecount = _xdb_saved; }
				};}});
		chargecount = _v_;
	}

	@Override
	public void setChargetime(long _v_) { // 上次的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "chargetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, chargetime) {
					public void rollback() { chargetime = _xdb_saved; }
				};}});
		chargetime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ChargeRecord _o_ = null;
		if ( _o1_ instanceof ChargeRecord ) _o_ = (ChargeRecord)_o1_;
		else if ( _o1_ instanceof ChargeRecord.Const ) _o_ = ((ChargeRecord.Const)_o1_).nThis();
		else return false;
		if (chargecount != _o_.chargecount) return false;
		if (chargetime != _o_.chargetime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += chargecount;
		_h_ += chargetime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(chargecount);
		_sb_.append(",");
		_sb_.append(chargetime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("chargecount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("chargetime"));
		return lb;
	}

	private class Const implements xbean.ChargeRecord {
		ChargeRecord nThis() {
			return ChargeRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ChargeRecord copy() {
			return ChargeRecord.this.copy();
		}

		@Override
		public xbean.ChargeRecord toData() {
			return ChargeRecord.this.toData();
		}

		public xbean.ChargeRecord toBean() {
			return ChargeRecord.this.toBean();
		}

		@Override
		public xbean.ChargeRecord toDataIf() {
			return ChargeRecord.this.toDataIf();
		}

		public xbean.ChargeRecord toBeanIf() {
			return ChargeRecord.this.toBeanIf();
		}

		@Override
		public int getChargecount() { // 已经充值次数
			_xdb_verify_unsafe_();
			return chargecount;
		}

		@Override
		public long getChargetime() { // 上次的时间
			_xdb_verify_unsafe_();
			return chargetime;
		}

		@Override
		public void setChargecount(int _v_) { // 已经充值次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChargetime(long _v_) { // 上次的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean toConst() {
			_xdb_verify_unsafe_();
			return this;
		}

		@Override
		public boolean isConst() {
			_xdb_verify_unsafe_();
			return true;
		}

		@Override
		public boolean isData() {
			return ChargeRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ChargeRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ChargeRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ChargeRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ChargeRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ChargeRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ChargeRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ChargeRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return ChargeRecord.this.toString();
		}

	}

	public static final class Data implements xbean.ChargeRecord {
		private int chargecount; // 已经充值次数
		private long chargetime; // 上次的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ChargeRecord _o1_) {
			if (_o1_ instanceof ChargeRecord) assign((ChargeRecord)_o1_);
			else if (_o1_ instanceof ChargeRecord.Data) assign((ChargeRecord.Data)_o1_);
			else if (_o1_ instanceof ChargeRecord.Const) assign(((ChargeRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ChargeRecord _o_) {
			chargecount = _o_.chargecount;
			chargetime = _o_.chargetime;
		}

		private void assign(ChargeRecord.Data _o_) {
			chargecount = _o_.chargecount;
			chargetime = _o_.chargetime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(chargecount);
			_os_.marshal(chargetime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			chargecount = _os_.unmarshal_int();
			chargetime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ChargeRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.ChargeRecord toData() {
			return new Data(this);
		}

		public xbean.ChargeRecord toBean() {
			return new ChargeRecord(this, null, null);
		}

		@Override
		public xbean.ChargeRecord toDataIf() {
			return this;
		}

		public xbean.ChargeRecord toBeanIf() {
			return new ChargeRecord(this, null, null);
		}

		// mkdb.Bean interface. Data Unsupported
		public boolean xdbManaged() { throw new UnsupportedOperationException(); }
		public mkdb.Bean xdbParent() { throw new UnsupportedOperationException(); }
		public String xdbVarname()  { throw new UnsupportedOperationException(); }
		public Long    xdbObjId()   { throw new UnsupportedOperationException(); }
		public mkdb.Bean toConst()   { throw new UnsupportedOperationException(); }
		public boolean isConst()    { return false; }
		public boolean isData()     { return true; }

		@Override
		public int getChargecount() { // 已经充值次数
			return chargecount;
		}

		@Override
		public long getChargetime() { // 上次的时间
			return chargetime;
		}

		@Override
		public void setChargecount(int _v_) { // 已经充值次数
			chargecount = _v_;
		}

		@Override
		public void setChargetime(long _v_) { // 上次的时间
			chargetime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ChargeRecord.Data)) return false;
			ChargeRecord.Data _o_ = (ChargeRecord.Data) _o1_;
			if (chargecount != _o_.chargecount) return false;
			if (chargetime != _o_.chargetime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += chargecount;
			_h_ += chargetime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(chargecount);
			_sb_.append(",");
			_sb_.append(chargetime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
