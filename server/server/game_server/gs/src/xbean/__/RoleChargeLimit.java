
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleChargeLimit extends mkdb.XBean implements xbean.RoleChargeLimit {
	private java.util.HashMap<Integer, xbean.ChargeRecord> chargeslimit; // key为商品id,value为充值次数记录

	@Override
	public void _reset_unsafe_() {
		chargeslimit.clear();
	}

	RoleChargeLimit(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		chargeslimit = new java.util.HashMap<Integer, xbean.ChargeRecord>();
	}

	public RoleChargeLimit() {
		this(0, null, null);
	}

	public RoleChargeLimit(RoleChargeLimit _o_) {
		this(_o_, null, null);
	}

	RoleChargeLimit(xbean.RoleChargeLimit _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleChargeLimit) assign((RoleChargeLimit)_o1_);
		else if (_o1_ instanceof RoleChargeLimit.Data) assign((RoleChargeLimit.Data)_o1_);
		else if (_o1_ instanceof RoleChargeLimit.Const) assign(((RoleChargeLimit.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleChargeLimit _o_) {
		_o_._xdb_verify_unsafe_();
		chargeslimit = new java.util.HashMap<Integer, xbean.ChargeRecord>();
		for (java.util.Map.Entry<Integer, xbean.ChargeRecord> _e_ : _o_.chargeslimit.entrySet())
			chargeslimit.put(_e_.getKey(), new ChargeRecord(_e_.getValue(), this, "chargeslimit"));
	}

	private void assign(RoleChargeLimit.Data _o_) {
		chargeslimit = new java.util.HashMap<Integer, xbean.ChargeRecord>();
		for (java.util.Map.Entry<Integer, xbean.ChargeRecord> _e_ : _o_.chargeslimit.entrySet())
			chargeslimit.put(_e_.getKey(), new ChargeRecord(_e_.getValue(), this, "chargeslimit"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(chargeslimit.size());
		for (java.util.Map.Entry<Integer, xbean.ChargeRecord> _e_ : chargeslimit.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				chargeslimit = new java.util.HashMap<Integer, xbean.ChargeRecord>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.ChargeRecord _v_ = new ChargeRecord(0, this, "chargeslimit");
				_v_.unmarshal(_os_);
				chargeslimit.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RoleChargeLimit copy() {
		_xdb_verify_unsafe_();
		return new RoleChargeLimit(this);
	}

	@Override
	public xbean.RoleChargeLimit toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleChargeLimit toBean() {
		_xdb_verify_unsafe_();
		return new RoleChargeLimit(this); // same as copy()
	}

	@Override
	public xbean.RoleChargeLimit toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleChargeLimit toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.ChargeRecord> getChargeslimit() { // key为商品id,value为充值次数记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "chargeslimit"), chargeslimit);
	}

	@Override
	public java.util.Map<Integer, xbean.ChargeRecord> getChargeslimitAsData() { // key为商品id,value为充值次数记录
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.ChargeRecord> chargeslimit;
		RoleChargeLimit _o_ = this;
		chargeslimit = new java.util.HashMap<Integer, xbean.ChargeRecord>();
		for (java.util.Map.Entry<Integer, xbean.ChargeRecord> _e_ : _o_.chargeslimit.entrySet())
			chargeslimit.put(_e_.getKey(), new ChargeRecord.Data(_e_.getValue()));
		return chargeslimit;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleChargeLimit _o_ = null;
		if ( _o1_ instanceof RoleChargeLimit ) _o_ = (RoleChargeLimit)_o1_;
		else if ( _o1_ instanceof RoleChargeLimit.Const ) _o_ = ((RoleChargeLimit.Const)_o1_).nThis();
		else return false;
		if (!chargeslimit.equals(_o_.chargeslimit)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += chargeslimit.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(chargeslimit);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("chargeslimit"));
		return lb;
	}

	private class Const implements xbean.RoleChargeLimit {
		RoleChargeLimit nThis() {
			return RoleChargeLimit.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleChargeLimit copy() {
			return RoleChargeLimit.this.copy();
		}

		@Override
		public xbean.RoleChargeLimit toData() {
			return RoleChargeLimit.this.toData();
		}

		public xbean.RoleChargeLimit toBean() {
			return RoleChargeLimit.this.toBean();
		}

		@Override
		public xbean.RoleChargeLimit toDataIf() {
			return RoleChargeLimit.this.toDataIf();
		}

		public xbean.RoleChargeLimit toBeanIf() {
			return RoleChargeLimit.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.ChargeRecord> getChargeslimit() { // key为商品id,value为充值次数记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(chargeslimit);
		}

		@Override
		public java.util.Map<Integer, xbean.ChargeRecord> getChargeslimitAsData() { // key为商品id,value为充值次数记录
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.ChargeRecord> chargeslimit;
			RoleChargeLimit _o_ = RoleChargeLimit.this;
			chargeslimit = new java.util.HashMap<Integer, xbean.ChargeRecord>();
			for (java.util.Map.Entry<Integer, xbean.ChargeRecord> _e_ : _o_.chargeslimit.entrySet())
				chargeslimit.put(_e_.getKey(), new ChargeRecord.Data(_e_.getValue()));
			return chargeslimit;
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
			return RoleChargeLimit.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleChargeLimit.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleChargeLimit.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleChargeLimit.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleChargeLimit.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleChargeLimit.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleChargeLimit.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleChargeLimit.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleChargeLimit.this.toString();
		}

	}

	public static final class Data implements xbean.RoleChargeLimit {
		private java.util.HashMap<Integer, xbean.ChargeRecord> chargeslimit; // key为商品id,value为充值次数记录

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			chargeslimit = new java.util.HashMap<Integer, xbean.ChargeRecord>();
		}

		Data(xbean.RoleChargeLimit _o1_) {
			if (_o1_ instanceof RoleChargeLimit) assign((RoleChargeLimit)_o1_);
			else if (_o1_ instanceof RoleChargeLimit.Data) assign((RoleChargeLimit.Data)_o1_);
			else if (_o1_ instanceof RoleChargeLimit.Const) assign(((RoleChargeLimit.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleChargeLimit _o_) {
			chargeslimit = new java.util.HashMap<Integer, xbean.ChargeRecord>();
			for (java.util.Map.Entry<Integer, xbean.ChargeRecord> _e_ : _o_.chargeslimit.entrySet())
				chargeslimit.put(_e_.getKey(), new ChargeRecord.Data(_e_.getValue()));
		}

		private void assign(RoleChargeLimit.Data _o_) {
			chargeslimit = new java.util.HashMap<Integer, xbean.ChargeRecord>();
			for (java.util.Map.Entry<Integer, xbean.ChargeRecord> _e_ : _o_.chargeslimit.entrySet())
				chargeslimit.put(_e_.getKey(), new ChargeRecord.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(chargeslimit.size());
			for (java.util.Map.Entry<Integer, xbean.ChargeRecord> _e_ : chargeslimit.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					chargeslimit = new java.util.HashMap<Integer, xbean.ChargeRecord>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.ChargeRecord _v_ = xbean.Pod.newChargeRecordData();
					_v_.unmarshal(_os_);
					chargeslimit.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RoleChargeLimit copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleChargeLimit toData() {
			return new Data(this);
		}

		public xbean.RoleChargeLimit toBean() {
			return new RoleChargeLimit(this, null, null);
		}

		@Override
		public xbean.RoleChargeLimit toDataIf() {
			return this;
		}

		public xbean.RoleChargeLimit toBeanIf() {
			return new RoleChargeLimit(this, null, null);
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
		public java.util.Map<Integer, xbean.ChargeRecord> getChargeslimit() { // key为商品id,value为充值次数记录
			return chargeslimit;
		}

		@Override
		public java.util.Map<Integer, xbean.ChargeRecord> getChargeslimitAsData() { // key为商品id,value为充值次数记录
			return chargeslimit;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleChargeLimit.Data)) return false;
			RoleChargeLimit.Data _o_ = (RoleChargeLimit.Data) _o1_;
			if (!chargeslimit.equals(_o_.chargeslimit)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += chargeslimit.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(chargeslimit);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
