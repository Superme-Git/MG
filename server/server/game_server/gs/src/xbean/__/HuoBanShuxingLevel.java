
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class HuoBanShuxingLevel extends mkdb.XBean implements xbean.HuoBanShuxingLevel {
	private java.util.HashMap<Integer, xbean.HuoBanshuxing> huobans; // key 为等级id,value为等级对应的伙伴信息

	@Override
	public void _reset_unsafe_() {
		huobans.clear();
	}

	HuoBanShuxingLevel(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		huobans = new java.util.HashMap<Integer, xbean.HuoBanshuxing>();
	}

	public HuoBanShuxingLevel() {
		this(0, null, null);
	}

	public HuoBanShuxingLevel(HuoBanShuxingLevel _o_) {
		this(_o_, null, null);
	}

	HuoBanShuxingLevel(xbean.HuoBanShuxingLevel _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof HuoBanShuxingLevel) assign((HuoBanShuxingLevel)_o1_);
		else if (_o1_ instanceof HuoBanShuxingLevel.Data) assign((HuoBanShuxingLevel.Data)_o1_);
		else if (_o1_ instanceof HuoBanShuxingLevel.Const) assign(((HuoBanShuxingLevel.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(HuoBanShuxingLevel _o_) {
		_o_._xdb_verify_unsafe_();
		huobans = new java.util.HashMap<Integer, xbean.HuoBanshuxing>();
		for (java.util.Map.Entry<Integer, xbean.HuoBanshuxing> _e_ : _o_.huobans.entrySet())
			huobans.put(_e_.getKey(), new HuoBanshuxing(_e_.getValue(), this, "huobans"));
	}

	private void assign(HuoBanShuxingLevel.Data _o_) {
		huobans = new java.util.HashMap<Integer, xbean.HuoBanshuxing>();
		for (java.util.Map.Entry<Integer, xbean.HuoBanshuxing> _e_ : _o_.huobans.entrySet())
			huobans.put(_e_.getKey(), new HuoBanshuxing(_e_.getValue(), this, "huobans"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(huobans.size());
		for (java.util.Map.Entry<Integer, xbean.HuoBanshuxing> _e_ : huobans.entrySet())
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
				huobans = new java.util.HashMap<Integer, xbean.HuoBanshuxing>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.HuoBanshuxing _v_ = new HuoBanshuxing(0, this, "huobans");
				_v_.unmarshal(_os_);
				huobans.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.HuoBanShuxingLevel copy() {
		_xdb_verify_unsafe_();
		return new HuoBanShuxingLevel(this);
	}

	@Override
	public xbean.HuoBanShuxingLevel toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanShuxingLevel toBean() {
		_xdb_verify_unsafe_();
		return new HuoBanShuxingLevel(this); // same as copy()
	}

	@Override
	public xbean.HuoBanShuxingLevel toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanShuxingLevel toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.HuoBanshuxing> getHuobans() { // key 为等级id,value为等级对应的伙伴信息
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "huobans"), huobans);
	}

	@Override
	public java.util.Map<Integer, xbean.HuoBanshuxing> getHuobansAsData() { // key 为等级id,value为等级对应的伙伴信息
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.HuoBanshuxing> huobans;
		HuoBanShuxingLevel _o_ = this;
		huobans = new java.util.HashMap<Integer, xbean.HuoBanshuxing>();
		for (java.util.Map.Entry<Integer, xbean.HuoBanshuxing> _e_ : _o_.huobans.entrySet())
			huobans.put(_e_.getKey(), new HuoBanshuxing.Data(_e_.getValue()));
		return huobans;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		HuoBanShuxingLevel _o_ = null;
		if ( _o1_ instanceof HuoBanShuxingLevel ) _o_ = (HuoBanShuxingLevel)_o1_;
		else if ( _o1_ instanceof HuoBanShuxingLevel.Const ) _o_ = ((HuoBanShuxingLevel.Const)_o1_).nThis();
		else return false;
		if (!huobans.equals(_o_.huobans)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += huobans.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(huobans);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("huobans"));
		return lb;
	}

	private class Const implements xbean.HuoBanShuxingLevel {
		HuoBanShuxingLevel nThis() {
			return HuoBanShuxingLevel.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.HuoBanShuxingLevel copy() {
			return HuoBanShuxingLevel.this.copy();
		}

		@Override
		public xbean.HuoBanShuxingLevel toData() {
			return HuoBanShuxingLevel.this.toData();
		}

		public xbean.HuoBanShuxingLevel toBean() {
			return HuoBanShuxingLevel.this.toBean();
		}

		@Override
		public xbean.HuoBanShuxingLevel toDataIf() {
			return HuoBanShuxingLevel.this.toDataIf();
		}

		public xbean.HuoBanShuxingLevel toBeanIf() {
			return HuoBanShuxingLevel.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.HuoBanshuxing> getHuobans() { // key 为等级id,value为等级对应的伙伴信息
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(huobans);
		}

		@Override
		public java.util.Map<Integer, xbean.HuoBanshuxing> getHuobansAsData() { // key 为等级id,value为等级对应的伙伴信息
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.HuoBanshuxing> huobans;
			HuoBanShuxingLevel _o_ = HuoBanShuxingLevel.this;
			huobans = new java.util.HashMap<Integer, xbean.HuoBanshuxing>();
			for (java.util.Map.Entry<Integer, xbean.HuoBanshuxing> _e_ : _o_.huobans.entrySet())
				huobans.put(_e_.getKey(), new HuoBanshuxing.Data(_e_.getValue()));
			return huobans;
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
			return HuoBanShuxingLevel.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return HuoBanShuxingLevel.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return HuoBanShuxingLevel.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return HuoBanShuxingLevel.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return HuoBanShuxingLevel.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return HuoBanShuxingLevel.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return HuoBanShuxingLevel.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return HuoBanShuxingLevel.this.hashCode();
		}

		@Override
		public String toString() {
			return HuoBanShuxingLevel.this.toString();
		}

	}

	public static final class Data implements xbean.HuoBanShuxingLevel {
		private java.util.HashMap<Integer, xbean.HuoBanshuxing> huobans; // key 为等级id,value为等级对应的伙伴信息

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			huobans = new java.util.HashMap<Integer, xbean.HuoBanshuxing>();
		}

		Data(xbean.HuoBanShuxingLevel _o1_) {
			if (_o1_ instanceof HuoBanShuxingLevel) assign((HuoBanShuxingLevel)_o1_);
			else if (_o1_ instanceof HuoBanShuxingLevel.Data) assign((HuoBanShuxingLevel.Data)_o1_);
			else if (_o1_ instanceof HuoBanShuxingLevel.Const) assign(((HuoBanShuxingLevel.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(HuoBanShuxingLevel _o_) {
			huobans = new java.util.HashMap<Integer, xbean.HuoBanshuxing>();
			for (java.util.Map.Entry<Integer, xbean.HuoBanshuxing> _e_ : _o_.huobans.entrySet())
				huobans.put(_e_.getKey(), new HuoBanshuxing.Data(_e_.getValue()));
		}

		private void assign(HuoBanShuxingLevel.Data _o_) {
			huobans = new java.util.HashMap<Integer, xbean.HuoBanshuxing>();
			for (java.util.Map.Entry<Integer, xbean.HuoBanshuxing> _e_ : _o_.huobans.entrySet())
				huobans.put(_e_.getKey(), new HuoBanshuxing.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(huobans.size());
			for (java.util.Map.Entry<Integer, xbean.HuoBanshuxing> _e_ : huobans.entrySet())
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
					huobans = new java.util.HashMap<Integer, xbean.HuoBanshuxing>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.HuoBanshuxing _v_ = xbean.Pod.newHuoBanshuxingData();
					_v_.unmarshal(_os_);
					huobans.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.HuoBanShuxingLevel copy() {
			return new Data(this);
		}

		@Override
		public xbean.HuoBanShuxingLevel toData() {
			return new Data(this);
		}

		public xbean.HuoBanShuxingLevel toBean() {
			return new HuoBanShuxingLevel(this, null, null);
		}

		@Override
		public xbean.HuoBanShuxingLevel toDataIf() {
			return this;
		}

		public xbean.HuoBanShuxingLevel toBeanIf() {
			return new HuoBanShuxingLevel(this, null, null);
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
		public java.util.Map<Integer, xbean.HuoBanshuxing> getHuobans() { // key 为等级id,value为等级对应的伙伴信息
			return huobans;
		}

		@Override
		public java.util.Map<Integer, xbean.HuoBanshuxing> getHuobansAsData() { // key 为等级id,value为等级对应的伙伴信息
			return huobans;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof HuoBanShuxingLevel.Data)) return false;
			HuoBanShuxingLevel.Data _o_ = (HuoBanShuxingLevel.Data) _o1_;
			if (!huobans.equals(_o_.huobans)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += huobans.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(huobans);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
