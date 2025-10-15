
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class YybFushiNums extends mkdb.XBean implements xbean.YybFushiNums {
	private java.util.HashMap<Long, xbean.YybFushiNum> rolefushi; // 

	@Override
	public void _reset_unsafe_() {
		rolefushi.clear();
	}

	YybFushiNums(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		rolefushi = new java.util.HashMap<Long, xbean.YybFushiNum>();
	}

	public YybFushiNums() {
		this(0, null, null);
	}

	public YybFushiNums(YybFushiNums _o_) {
		this(_o_, null, null);
	}

	YybFushiNums(xbean.YybFushiNums _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof YybFushiNums) assign((YybFushiNums)_o1_);
		else if (_o1_ instanceof YybFushiNums.Data) assign((YybFushiNums.Data)_o1_);
		else if (_o1_ instanceof YybFushiNums.Const) assign(((YybFushiNums.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(YybFushiNums _o_) {
		_o_._xdb_verify_unsafe_();
		rolefushi = new java.util.HashMap<Long, xbean.YybFushiNum>();
		for (java.util.Map.Entry<Long, xbean.YybFushiNum> _e_ : _o_.rolefushi.entrySet())
			rolefushi.put(_e_.getKey(), new YybFushiNum(_e_.getValue(), this, "rolefushi"));
	}

	private void assign(YybFushiNums.Data _o_) {
		rolefushi = new java.util.HashMap<Long, xbean.YybFushiNum>();
		for (java.util.Map.Entry<Long, xbean.YybFushiNum> _e_ : _o_.rolefushi.entrySet())
			rolefushi.put(_e_.getKey(), new YybFushiNum(_e_.getValue(), this, "rolefushi"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(rolefushi.size());
		for (java.util.Map.Entry<Long, xbean.YybFushiNum> _e_ : rolefushi.entrySet())
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
				rolefushi = new java.util.HashMap<Long, xbean.YybFushiNum>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.YybFushiNum _v_ = new YybFushiNum(0, this, "rolefushi");
				_v_.unmarshal(_os_);
				rolefushi.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.YybFushiNums copy() {
		_xdb_verify_unsafe_();
		return new YybFushiNums(this);
	}

	@Override
	public xbean.YybFushiNums toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.YybFushiNums toBean() {
		_xdb_verify_unsafe_();
		return new YybFushiNums(this); // same as copy()
	}

	@Override
	public xbean.YybFushiNums toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.YybFushiNums toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, xbean.YybFushiNum> getRolefushi() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "rolefushi"), rolefushi);
	}

	@Override
	public java.util.Map<Long, xbean.YybFushiNum> getRolefushiAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.YybFushiNum> rolefushi;
		YybFushiNums _o_ = this;
		rolefushi = new java.util.HashMap<Long, xbean.YybFushiNum>();
		for (java.util.Map.Entry<Long, xbean.YybFushiNum> _e_ : _o_.rolefushi.entrySet())
			rolefushi.put(_e_.getKey(), new YybFushiNum.Data(_e_.getValue()));
		return rolefushi;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		YybFushiNums _o_ = null;
		if ( _o1_ instanceof YybFushiNums ) _o_ = (YybFushiNums)_o1_;
		else if ( _o1_ instanceof YybFushiNums.Const ) _o_ = ((YybFushiNums.Const)_o1_).nThis();
		else return false;
		if (!rolefushi.equals(_o_.rolefushi)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += rolefushi.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolefushi);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("rolefushi"));
		return lb;
	}

	private class Const implements xbean.YybFushiNums {
		YybFushiNums nThis() {
			return YybFushiNums.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.YybFushiNums copy() {
			return YybFushiNums.this.copy();
		}

		@Override
		public xbean.YybFushiNums toData() {
			return YybFushiNums.this.toData();
		}

		public xbean.YybFushiNums toBean() {
			return YybFushiNums.this.toBean();
		}

		@Override
		public xbean.YybFushiNums toDataIf() {
			return YybFushiNums.this.toDataIf();
		}

		public xbean.YybFushiNums toBeanIf() {
			return YybFushiNums.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, xbean.YybFushiNum> getRolefushi() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(rolefushi);
		}

		@Override
		public java.util.Map<Long, xbean.YybFushiNum> getRolefushiAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.YybFushiNum> rolefushi;
			YybFushiNums _o_ = YybFushiNums.this;
			rolefushi = new java.util.HashMap<Long, xbean.YybFushiNum>();
			for (java.util.Map.Entry<Long, xbean.YybFushiNum> _e_ : _o_.rolefushi.entrySet())
				rolefushi.put(_e_.getKey(), new YybFushiNum.Data(_e_.getValue()));
			return rolefushi;
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
			return YybFushiNums.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return YybFushiNums.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return YybFushiNums.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return YybFushiNums.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return YybFushiNums.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return YybFushiNums.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return YybFushiNums.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return YybFushiNums.this.hashCode();
		}

		@Override
		public String toString() {
			return YybFushiNums.this.toString();
		}

	}

	public static final class Data implements xbean.YybFushiNums {
		private java.util.HashMap<Long, xbean.YybFushiNum> rolefushi; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			rolefushi = new java.util.HashMap<Long, xbean.YybFushiNum>();
		}

		Data(xbean.YybFushiNums _o1_) {
			if (_o1_ instanceof YybFushiNums) assign((YybFushiNums)_o1_);
			else if (_o1_ instanceof YybFushiNums.Data) assign((YybFushiNums.Data)_o1_);
			else if (_o1_ instanceof YybFushiNums.Const) assign(((YybFushiNums.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(YybFushiNums _o_) {
			rolefushi = new java.util.HashMap<Long, xbean.YybFushiNum>();
			for (java.util.Map.Entry<Long, xbean.YybFushiNum> _e_ : _o_.rolefushi.entrySet())
				rolefushi.put(_e_.getKey(), new YybFushiNum.Data(_e_.getValue()));
		}

		private void assign(YybFushiNums.Data _o_) {
			rolefushi = new java.util.HashMap<Long, xbean.YybFushiNum>();
			for (java.util.Map.Entry<Long, xbean.YybFushiNum> _e_ : _o_.rolefushi.entrySet())
				rolefushi.put(_e_.getKey(), new YybFushiNum.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(rolefushi.size());
			for (java.util.Map.Entry<Long, xbean.YybFushiNum> _e_ : rolefushi.entrySet())
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
					rolefushi = new java.util.HashMap<Long, xbean.YybFushiNum>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.YybFushiNum _v_ = xbean.Pod.newYybFushiNumData();
					_v_.unmarshal(_os_);
					rolefushi.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.YybFushiNums copy() {
			return new Data(this);
		}

		@Override
		public xbean.YybFushiNums toData() {
			return new Data(this);
		}

		public xbean.YybFushiNums toBean() {
			return new YybFushiNums(this, null, null);
		}

		@Override
		public xbean.YybFushiNums toDataIf() {
			return this;
		}

		public xbean.YybFushiNums toBeanIf() {
			return new YybFushiNums(this, null, null);
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
		public java.util.Map<Long, xbean.YybFushiNum> getRolefushi() { // 
			return rolefushi;
		}

		@Override
		public java.util.Map<Long, xbean.YybFushiNum> getRolefushiAsData() { // 
			return rolefushi;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof YybFushiNums.Data)) return false;
			YybFushiNums.Data _o_ = (YybFushiNums.Data) _o1_;
			if (!rolefushi.equals(_o_.rolefushi)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += rolefushi.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(rolefushi);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
