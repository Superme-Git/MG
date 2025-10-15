
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Compensation extends mkdb.XBean implements xbean.Compensation {
	private java.util.HashMap<Integer, xbean.CompensationInfo> compensationmap; // 领取补偿的id

	@Override
	public void _reset_unsafe_() {
		compensationmap.clear();
	}

	Compensation(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		compensationmap = new java.util.HashMap<Integer, xbean.CompensationInfo>();
	}

	public Compensation() {
		this(0, null, null);
	}

	public Compensation(Compensation _o_) {
		this(_o_, null, null);
	}

	Compensation(xbean.Compensation _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Compensation) assign((Compensation)_o1_);
		else if (_o1_ instanceof Compensation.Data) assign((Compensation.Data)_o1_);
		else if (_o1_ instanceof Compensation.Const) assign(((Compensation.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Compensation _o_) {
		_o_._xdb_verify_unsafe_();
		compensationmap = new java.util.HashMap<Integer, xbean.CompensationInfo>();
		for (java.util.Map.Entry<Integer, xbean.CompensationInfo> _e_ : _o_.compensationmap.entrySet())
			compensationmap.put(_e_.getKey(), new CompensationInfo(_e_.getValue(), this, "compensationmap"));
	}

	private void assign(Compensation.Data _o_) {
		compensationmap = new java.util.HashMap<Integer, xbean.CompensationInfo>();
		for (java.util.Map.Entry<Integer, xbean.CompensationInfo> _e_ : _o_.compensationmap.entrySet())
			compensationmap.put(_e_.getKey(), new CompensationInfo(_e_.getValue(), this, "compensationmap"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(compensationmap.size());
		for (java.util.Map.Entry<Integer, xbean.CompensationInfo> _e_ : compensationmap.entrySet())
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
				compensationmap = new java.util.HashMap<Integer, xbean.CompensationInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.CompensationInfo _v_ = new CompensationInfo(0, this, "compensationmap");
				_v_.unmarshal(_os_);
				compensationmap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.Compensation copy() {
		_xdb_verify_unsafe_();
		return new Compensation(this);
	}

	@Override
	public xbean.Compensation toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Compensation toBean() {
		_xdb_verify_unsafe_();
		return new Compensation(this); // same as copy()
	}

	@Override
	public xbean.Compensation toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Compensation toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.CompensationInfo> getCompensationmap() { // 领取补偿的id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "compensationmap"), compensationmap);
	}

	@Override
	public java.util.Map<Integer, xbean.CompensationInfo> getCompensationmapAsData() { // 领取补偿的id
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.CompensationInfo> compensationmap;
		Compensation _o_ = this;
		compensationmap = new java.util.HashMap<Integer, xbean.CompensationInfo>();
		for (java.util.Map.Entry<Integer, xbean.CompensationInfo> _e_ : _o_.compensationmap.entrySet())
			compensationmap.put(_e_.getKey(), new CompensationInfo.Data(_e_.getValue()));
		return compensationmap;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Compensation _o_ = null;
		if ( _o1_ instanceof Compensation ) _o_ = (Compensation)_o1_;
		else if ( _o1_ instanceof Compensation.Const ) _o_ = ((Compensation.Const)_o1_).nThis();
		else return false;
		if (!compensationmap.equals(_o_.compensationmap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += compensationmap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(compensationmap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("compensationmap"));
		return lb;
	}

	private class Const implements xbean.Compensation {
		Compensation nThis() {
			return Compensation.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Compensation copy() {
			return Compensation.this.copy();
		}

		@Override
		public xbean.Compensation toData() {
			return Compensation.this.toData();
		}

		public xbean.Compensation toBean() {
			return Compensation.this.toBean();
		}

		@Override
		public xbean.Compensation toDataIf() {
			return Compensation.this.toDataIf();
		}

		public xbean.Compensation toBeanIf() {
			return Compensation.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.CompensationInfo> getCompensationmap() { // 领取补偿的id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(compensationmap);
		}

		@Override
		public java.util.Map<Integer, xbean.CompensationInfo> getCompensationmapAsData() { // 领取补偿的id
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.CompensationInfo> compensationmap;
			Compensation _o_ = Compensation.this;
			compensationmap = new java.util.HashMap<Integer, xbean.CompensationInfo>();
			for (java.util.Map.Entry<Integer, xbean.CompensationInfo> _e_ : _o_.compensationmap.entrySet())
				compensationmap.put(_e_.getKey(), new CompensationInfo.Data(_e_.getValue()));
			return compensationmap;
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
			return Compensation.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Compensation.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Compensation.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Compensation.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Compensation.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Compensation.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Compensation.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Compensation.this.hashCode();
		}

		@Override
		public String toString() {
			return Compensation.this.toString();
		}

	}

	public static final class Data implements xbean.Compensation {
		private java.util.HashMap<Integer, xbean.CompensationInfo> compensationmap; // 领取补偿的id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			compensationmap = new java.util.HashMap<Integer, xbean.CompensationInfo>();
		}

		Data(xbean.Compensation _o1_) {
			if (_o1_ instanceof Compensation) assign((Compensation)_o1_);
			else if (_o1_ instanceof Compensation.Data) assign((Compensation.Data)_o1_);
			else if (_o1_ instanceof Compensation.Const) assign(((Compensation.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Compensation _o_) {
			compensationmap = new java.util.HashMap<Integer, xbean.CompensationInfo>();
			for (java.util.Map.Entry<Integer, xbean.CompensationInfo> _e_ : _o_.compensationmap.entrySet())
				compensationmap.put(_e_.getKey(), new CompensationInfo.Data(_e_.getValue()));
		}

		private void assign(Compensation.Data _o_) {
			compensationmap = new java.util.HashMap<Integer, xbean.CompensationInfo>();
			for (java.util.Map.Entry<Integer, xbean.CompensationInfo> _e_ : _o_.compensationmap.entrySet())
				compensationmap.put(_e_.getKey(), new CompensationInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(compensationmap.size());
			for (java.util.Map.Entry<Integer, xbean.CompensationInfo> _e_ : compensationmap.entrySet())
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
					compensationmap = new java.util.HashMap<Integer, xbean.CompensationInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.CompensationInfo _v_ = xbean.Pod.newCompensationInfoData();
					_v_.unmarshal(_os_);
					compensationmap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.Compensation copy() {
			return new Data(this);
		}

		@Override
		public xbean.Compensation toData() {
			return new Data(this);
		}

		public xbean.Compensation toBean() {
			return new Compensation(this, null, null);
		}

		@Override
		public xbean.Compensation toDataIf() {
			return this;
		}

		public xbean.Compensation toBeanIf() {
			return new Compensation(this, null, null);
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
		public java.util.Map<Integer, xbean.CompensationInfo> getCompensationmap() { // 领取补偿的id
			return compensationmap;
		}

		@Override
		public java.util.Map<Integer, xbean.CompensationInfo> getCompensationmapAsData() { // 领取补偿的id
			return compensationmap;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Compensation.Data)) return false;
			Compensation.Data _o_ = (Compensation.Data) _o1_;
			if (!compensationmap.equals(_o_.compensationmap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += compensationmap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(compensationmap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
