
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ChargeHistory extends mkdb.XBean implements xbean.ChargeHistory {
	private java.util.HashMap<Long, xbean.ChargeOrder> charges; // 

	@Override
	public void _reset_unsafe_() {
		charges.clear();
	}

	ChargeHistory(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		charges = new java.util.HashMap<Long, xbean.ChargeOrder>();
	}

	public ChargeHistory() {
		this(0, null, null);
	}

	public ChargeHistory(ChargeHistory _o_) {
		this(_o_, null, null);
	}

	ChargeHistory(xbean.ChargeHistory _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ChargeHistory) assign((ChargeHistory)_o1_);
		else if (_o1_ instanceof ChargeHistory.Data) assign((ChargeHistory.Data)_o1_);
		else if (_o1_ instanceof ChargeHistory.Const) assign(((ChargeHistory.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ChargeHistory _o_) {
		_o_._xdb_verify_unsafe_();
		charges = new java.util.HashMap<Long, xbean.ChargeOrder>();
		for (java.util.Map.Entry<Long, xbean.ChargeOrder> _e_ : _o_.charges.entrySet())
			charges.put(_e_.getKey(), new ChargeOrder(_e_.getValue(), this, "charges"));
	}

	private void assign(ChargeHistory.Data _o_) {
		charges = new java.util.HashMap<Long, xbean.ChargeOrder>();
		for (java.util.Map.Entry<Long, xbean.ChargeOrder> _e_ : _o_.charges.entrySet())
			charges.put(_e_.getKey(), new ChargeOrder(_e_.getValue(), this, "charges"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(charges.size());
		for (java.util.Map.Entry<Long, xbean.ChargeOrder> _e_ : charges.entrySet())
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
				charges = new java.util.HashMap<Long, xbean.ChargeOrder>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.ChargeOrder _v_ = new ChargeOrder(0, this, "charges");
				_v_.unmarshal(_os_);
				charges.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.ChargeHistory copy() {
		_xdb_verify_unsafe_();
		return new ChargeHistory(this);
	}

	@Override
	public xbean.ChargeHistory toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChargeHistory toBean() {
		_xdb_verify_unsafe_();
		return new ChargeHistory(this); // same as copy()
	}

	@Override
	public xbean.ChargeHistory toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChargeHistory toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, xbean.ChargeOrder> getCharges() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "charges"), charges);
	}

	@Override
	public java.util.Map<Long, xbean.ChargeOrder> getChargesAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.ChargeOrder> charges;
		ChargeHistory _o_ = this;
		charges = new java.util.HashMap<Long, xbean.ChargeOrder>();
		for (java.util.Map.Entry<Long, xbean.ChargeOrder> _e_ : _o_.charges.entrySet())
			charges.put(_e_.getKey(), new ChargeOrder.Data(_e_.getValue()));
		return charges;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ChargeHistory _o_ = null;
		if ( _o1_ instanceof ChargeHistory ) _o_ = (ChargeHistory)_o1_;
		else if ( _o1_ instanceof ChargeHistory.Const ) _o_ = ((ChargeHistory.Const)_o1_).nThis();
		else return false;
		if (!charges.equals(_o_.charges)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += charges.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(charges);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("charges"));
		return lb;
	}

	private class Const implements xbean.ChargeHistory {
		ChargeHistory nThis() {
			return ChargeHistory.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ChargeHistory copy() {
			return ChargeHistory.this.copy();
		}

		@Override
		public xbean.ChargeHistory toData() {
			return ChargeHistory.this.toData();
		}

		public xbean.ChargeHistory toBean() {
			return ChargeHistory.this.toBean();
		}

		@Override
		public xbean.ChargeHistory toDataIf() {
			return ChargeHistory.this.toDataIf();
		}

		public xbean.ChargeHistory toBeanIf() {
			return ChargeHistory.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, xbean.ChargeOrder> getCharges() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(charges);
		}

		@Override
		public java.util.Map<Long, xbean.ChargeOrder> getChargesAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.ChargeOrder> charges;
			ChargeHistory _o_ = ChargeHistory.this;
			charges = new java.util.HashMap<Long, xbean.ChargeOrder>();
			for (java.util.Map.Entry<Long, xbean.ChargeOrder> _e_ : _o_.charges.entrySet())
				charges.put(_e_.getKey(), new ChargeOrder.Data(_e_.getValue()));
			return charges;
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
			return ChargeHistory.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ChargeHistory.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ChargeHistory.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ChargeHistory.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ChargeHistory.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ChargeHistory.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ChargeHistory.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ChargeHistory.this.hashCode();
		}

		@Override
		public String toString() {
			return ChargeHistory.this.toString();
		}

	}

	public static final class Data implements xbean.ChargeHistory {
		private java.util.HashMap<Long, xbean.ChargeOrder> charges; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			charges = new java.util.HashMap<Long, xbean.ChargeOrder>();
		}

		Data(xbean.ChargeHistory _o1_) {
			if (_o1_ instanceof ChargeHistory) assign((ChargeHistory)_o1_);
			else if (_o1_ instanceof ChargeHistory.Data) assign((ChargeHistory.Data)_o1_);
			else if (_o1_ instanceof ChargeHistory.Const) assign(((ChargeHistory.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ChargeHistory _o_) {
			charges = new java.util.HashMap<Long, xbean.ChargeOrder>();
			for (java.util.Map.Entry<Long, xbean.ChargeOrder> _e_ : _o_.charges.entrySet())
				charges.put(_e_.getKey(), new ChargeOrder.Data(_e_.getValue()));
		}

		private void assign(ChargeHistory.Data _o_) {
			charges = new java.util.HashMap<Long, xbean.ChargeOrder>();
			for (java.util.Map.Entry<Long, xbean.ChargeOrder> _e_ : _o_.charges.entrySet())
				charges.put(_e_.getKey(), new ChargeOrder.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(charges.size());
			for (java.util.Map.Entry<Long, xbean.ChargeOrder> _e_ : charges.entrySet())
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
					charges = new java.util.HashMap<Long, xbean.ChargeOrder>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.ChargeOrder _v_ = xbean.Pod.newChargeOrderData();
					_v_.unmarshal(_os_);
					charges.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.ChargeHistory copy() {
			return new Data(this);
		}

		@Override
		public xbean.ChargeHistory toData() {
			return new Data(this);
		}

		public xbean.ChargeHistory toBean() {
			return new ChargeHistory(this, null, null);
		}

		@Override
		public xbean.ChargeHistory toDataIf() {
			return this;
		}

		public xbean.ChargeHistory toBeanIf() {
			return new ChargeHistory(this, null, null);
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
		public java.util.Map<Long, xbean.ChargeOrder> getCharges() { // 
			return charges;
		}

		@Override
		public java.util.Map<Long, xbean.ChargeOrder> getChargesAsData() { // 
			return charges;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ChargeHistory.Data)) return false;
			ChargeHistory.Data _o_ = (ChargeHistory.Data) _o1_;
			if (!charges.equals(_o_.charges)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += charges.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(charges);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
