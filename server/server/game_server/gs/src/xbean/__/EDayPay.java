
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class EDayPay extends mkdb.XBean implements xbean.EDayPay {
	private java.util.HashMap<Long, xbean.DayPay> roleid2daypay; // 队伍分赃链表 by changhao

	@Override
	public void _reset_unsafe_() {
		roleid2daypay.clear();
	}

	EDayPay(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		roleid2daypay = new java.util.HashMap<Long, xbean.DayPay>();
	}

	public EDayPay() {
		this(0, null, null);
	}

	public EDayPay(EDayPay _o_) {
		this(_o_, null, null);
	}

	EDayPay(xbean.EDayPay _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof EDayPay) assign((EDayPay)_o1_);
		else if (_o1_ instanceof EDayPay.Data) assign((EDayPay.Data)_o1_);
		else if (_o1_ instanceof EDayPay.Const) assign(((EDayPay.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(EDayPay _o_) {
		_o_._xdb_verify_unsafe_();
		roleid2daypay = new java.util.HashMap<Long, xbean.DayPay>();
		for (java.util.Map.Entry<Long, xbean.DayPay> _e_ : _o_.roleid2daypay.entrySet())
			roleid2daypay.put(_e_.getKey(), new DayPay(_e_.getValue(), this, "roleid2daypay"));
	}

	private void assign(EDayPay.Data _o_) {
		roleid2daypay = new java.util.HashMap<Long, xbean.DayPay>();
		for (java.util.Map.Entry<Long, xbean.DayPay> _e_ : _o_.roleid2daypay.entrySet())
			roleid2daypay.put(_e_.getKey(), new DayPay(_e_.getValue(), this, "roleid2daypay"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(roleid2daypay.size());
		for (java.util.Map.Entry<Long, xbean.DayPay> _e_ : roleid2daypay.entrySet())
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
				roleid2daypay = new java.util.HashMap<Long, xbean.DayPay>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.DayPay _v_ = new DayPay(0, this, "roleid2daypay");
				_v_.unmarshal(_os_);
				roleid2daypay.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.EDayPay copy() {
		_xdb_verify_unsafe_();
		return new EDayPay(this);
	}

	@Override
	public xbean.EDayPay toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EDayPay toBean() {
		_xdb_verify_unsafe_();
		return new EDayPay(this); // same as copy()
	}

	@Override
	public xbean.EDayPay toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EDayPay toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, xbean.DayPay> getRoleid2daypay() { // 队伍分赃链表 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "roleid2daypay"), roleid2daypay);
	}

	@Override
	public java.util.Map<Long, xbean.DayPay> getRoleid2daypayAsData() { // 队伍分赃链表 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.DayPay> roleid2daypay;
		EDayPay _o_ = this;
		roleid2daypay = new java.util.HashMap<Long, xbean.DayPay>();
		for (java.util.Map.Entry<Long, xbean.DayPay> _e_ : _o_.roleid2daypay.entrySet())
			roleid2daypay.put(_e_.getKey(), new DayPay.Data(_e_.getValue()));
		return roleid2daypay;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		EDayPay _o_ = null;
		if ( _o1_ instanceof EDayPay ) _o_ = (EDayPay)_o1_;
		else if ( _o1_ instanceof EDayPay.Const ) _o_ = ((EDayPay.Const)_o1_).nThis();
		else return false;
		if (!roleid2daypay.equals(_o_.roleid2daypay)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid2daypay.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid2daypay);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("roleid2daypay"));
		return lb;
	}

	private class Const implements xbean.EDayPay {
		EDayPay nThis() {
			return EDayPay.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.EDayPay copy() {
			return EDayPay.this.copy();
		}

		@Override
		public xbean.EDayPay toData() {
			return EDayPay.this.toData();
		}

		public xbean.EDayPay toBean() {
			return EDayPay.this.toBean();
		}

		@Override
		public xbean.EDayPay toDataIf() {
			return EDayPay.this.toDataIf();
		}

		public xbean.EDayPay toBeanIf() {
			return EDayPay.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, xbean.DayPay> getRoleid2daypay() { // 队伍分赃链表 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(roleid2daypay);
		}

		@Override
		public java.util.Map<Long, xbean.DayPay> getRoleid2daypayAsData() { // 队伍分赃链表 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.DayPay> roleid2daypay;
			EDayPay _o_ = EDayPay.this;
			roleid2daypay = new java.util.HashMap<Long, xbean.DayPay>();
			for (java.util.Map.Entry<Long, xbean.DayPay> _e_ : _o_.roleid2daypay.entrySet())
				roleid2daypay.put(_e_.getKey(), new DayPay.Data(_e_.getValue()));
			return roleid2daypay;
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
			return EDayPay.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return EDayPay.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return EDayPay.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return EDayPay.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return EDayPay.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return EDayPay.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return EDayPay.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return EDayPay.this.hashCode();
		}

		@Override
		public String toString() {
			return EDayPay.this.toString();
		}

	}

	public static final class Data implements xbean.EDayPay {
		private java.util.HashMap<Long, xbean.DayPay> roleid2daypay; // 队伍分赃链表 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			roleid2daypay = new java.util.HashMap<Long, xbean.DayPay>();
		}

		Data(xbean.EDayPay _o1_) {
			if (_o1_ instanceof EDayPay) assign((EDayPay)_o1_);
			else if (_o1_ instanceof EDayPay.Data) assign((EDayPay.Data)_o1_);
			else if (_o1_ instanceof EDayPay.Const) assign(((EDayPay.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(EDayPay _o_) {
			roleid2daypay = new java.util.HashMap<Long, xbean.DayPay>();
			for (java.util.Map.Entry<Long, xbean.DayPay> _e_ : _o_.roleid2daypay.entrySet())
				roleid2daypay.put(_e_.getKey(), new DayPay.Data(_e_.getValue()));
		}

		private void assign(EDayPay.Data _o_) {
			roleid2daypay = new java.util.HashMap<Long, xbean.DayPay>();
			for (java.util.Map.Entry<Long, xbean.DayPay> _e_ : _o_.roleid2daypay.entrySet())
				roleid2daypay.put(_e_.getKey(), new DayPay.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(roleid2daypay.size());
			for (java.util.Map.Entry<Long, xbean.DayPay> _e_ : roleid2daypay.entrySet())
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
					roleid2daypay = new java.util.HashMap<Long, xbean.DayPay>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.DayPay _v_ = xbean.Pod.newDayPayData();
					_v_.unmarshal(_os_);
					roleid2daypay.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.EDayPay copy() {
			return new Data(this);
		}

		@Override
		public xbean.EDayPay toData() {
			return new Data(this);
		}

		public xbean.EDayPay toBean() {
			return new EDayPay(this, null, null);
		}

		@Override
		public xbean.EDayPay toDataIf() {
			return this;
		}

		public xbean.EDayPay toBeanIf() {
			return new EDayPay(this, null, null);
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
		public java.util.Map<Long, xbean.DayPay> getRoleid2daypay() { // 队伍分赃链表 by changhao
			return roleid2daypay;
		}

		@Override
		public java.util.Map<Long, xbean.DayPay> getRoleid2daypayAsData() { // 队伍分赃链表 by changhao
			return roleid2daypay;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof EDayPay.Data)) return false;
			EDayPay.Data _o_ = (EDayPay.Data) _o1_;
			if (!roleid2daypay.equals(_o_.roleid2daypay)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid2daypay.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid2daypay);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
