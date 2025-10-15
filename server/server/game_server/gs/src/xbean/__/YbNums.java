
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class YbNums extends mkdb.XBean implements xbean.YbNums {
	private java.util.HashMap<Long, xbean.YbNum> roleyb; // 角色id -> 符石 by changhao

	@Override
	public void _reset_unsafe_() {
		roleyb.clear();
	}

	YbNums(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		roleyb = new java.util.HashMap<Long, xbean.YbNum>();
	}

	public YbNums() {
		this(0, null, null);
	}

	public YbNums(YbNums _o_) {
		this(_o_, null, null);
	}

	YbNums(xbean.YbNums _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof YbNums) assign((YbNums)_o1_);
		else if (_o1_ instanceof YbNums.Data) assign((YbNums.Data)_o1_);
		else if (_o1_ instanceof YbNums.Const) assign(((YbNums.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(YbNums _o_) {
		_o_._xdb_verify_unsafe_();
		roleyb = new java.util.HashMap<Long, xbean.YbNum>();
		for (java.util.Map.Entry<Long, xbean.YbNum> _e_ : _o_.roleyb.entrySet())
			roleyb.put(_e_.getKey(), new YbNum(_e_.getValue(), this, "roleyb"));
	}

	private void assign(YbNums.Data _o_) {
		roleyb = new java.util.HashMap<Long, xbean.YbNum>();
		for (java.util.Map.Entry<Long, xbean.YbNum> _e_ : _o_.roleyb.entrySet())
			roleyb.put(_e_.getKey(), new YbNum(_e_.getValue(), this, "roleyb"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(roleyb.size());
		for (java.util.Map.Entry<Long, xbean.YbNum> _e_ : roleyb.entrySet())
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
				roleyb = new java.util.HashMap<Long, xbean.YbNum>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.YbNum _v_ = new YbNum(0, this, "roleyb");
				_v_.unmarshal(_os_);
				roleyb.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.YbNums copy() {
		_xdb_verify_unsafe_();
		return new YbNums(this);
	}

	@Override
	public xbean.YbNums toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.YbNums toBean() {
		_xdb_verify_unsafe_();
		return new YbNums(this); // same as copy()
	}

	@Override
	public xbean.YbNums toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.YbNums toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, xbean.YbNum> getRoleyb() { // 角色id -> 符石 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "roleyb"), roleyb);
	}

	@Override
	public java.util.Map<Long, xbean.YbNum> getRoleybAsData() { // 角色id -> 符石 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.YbNum> roleyb;
		YbNums _o_ = this;
		roleyb = new java.util.HashMap<Long, xbean.YbNum>();
		for (java.util.Map.Entry<Long, xbean.YbNum> _e_ : _o_.roleyb.entrySet())
			roleyb.put(_e_.getKey(), new YbNum.Data(_e_.getValue()));
		return roleyb;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		YbNums _o_ = null;
		if ( _o1_ instanceof YbNums ) _o_ = (YbNums)_o1_;
		else if ( _o1_ instanceof YbNums.Const ) _o_ = ((YbNums.Const)_o1_).nThis();
		else return false;
		if (!roleyb.equals(_o_.roleyb)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleyb.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleyb);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("roleyb"));
		return lb;
	}

	private class Const implements xbean.YbNums {
		YbNums nThis() {
			return YbNums.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.YbNums copy() {
			return YbNums.this.copy();
		}

		@Override
		public xbean.YbNums toData() {
			return YbNums.this.toData();
		}

		public xbean.YbNums toBean() {
			return YbNums.this.toBean();
		}

		@Override
		public xbean.YbNums toDataIf() {
			return YbNums.this.toDataIf();
		}

		public xbean.YbNums toBeanIf() {
			return YbNums.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, xbean.YbNum> getRoleyb() { // 角色id -> 符石 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(roleyb);
		}

		@Override
		public java.util.Map<Long, xbean.YbNum> getRoleybAsData() { // 角色id -> 符石 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.YbNum> roleyb;
			YbNums _o_ = YbNums.this;
			roleyb = new java.util.HashMap<Long, xbean.YbNum>();
			for (java.util.Map.Entry<Long, xbean.YbNum> _e_ : _o_.roleyb.entrySet())
				roleyb.put(_e_.getKey(), new YbNum.Data(_e_.getValue()));
			return roleyb;
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
			return YbNums.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return YbNums.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return YbNums.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return YbNums.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return YbNums.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return YbNums.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return YbNums.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return YbNums.this.hashCode();
		}

		@Override
		public String toString() {
			return YbNums.this.toString();
		}

	}

	public static final class Data implements xbean.YbNums {
		private java.util.HashMap<Long, xbean.YbNum> roleyb; // 角色id -> 符石 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			roleyb = new java.util.HashMap<Long, xbean.YbNum>();
		}

		Data(xbean.YbNums _o1_) {
			if (_o1_ instanceof YbNums) assign((YbNums)_o1_);
			else if (_o1_ instanceof YbNums.Data) assign((YbNums.Data)_o1_);
			else if (_o1_ instanceof YbNums.Const) assign(((YbNums.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(YbNums _o_) {
			roleyb = new java.util.HashMap<Long, xbean.YbNum>();
			for (java.util.Map.Entry<Long, xbean.YbNum> _e_ : _o_.roleyb.entrySet())
				roleyb.put(_e_.getKey(), new YbNum.Data(_e_.getValue()));
		}

		private void assign(YbNums.Data _o_) {
			roleyb = new java.util.HashMap<Long, xbean.YbNum>();
			for (java.util.Map.Entry<Long, xbean.YbNum> _e_ : _o_.roleyb.entrySet())
				roleyb.put(_e_.getKey(), new YbNum.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(roleyb.size());
			for (java.util.Map.Entry<Long, xbean.YbNum> _e_ : roleyb.entrySet())
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
					roleyb = new java.util.HashMap<Long, xbean.YbNum>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.YbNum _v_ = xbean.Pod.newYbNumData();
					_v_.unmarshal(_os_);
					roleyb.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.YbNums copy() {
			return new Data(this);
		}

		@Override
		public xbean.YbNums toData() {
			return new Data(this);
		}

		public xbean.YbNums toBean() {
			return new YbNums(this, null, null);
		}

		@Override
		public xbean.YbNums toDataIf() {
			return this;
		}

		public xbean.YbNums toBeanIf() {
			return new YbNums(this, null, null);
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
		public java.util.Map<Long, xbean.YbNum> getRoleyb() { // 角色id -> 符石 by changhao
			return roleyb;
		}

		@Override
		public java.util.Map<Long, xbean.YbNum> getRoleybAsData() { // 角色id -> 符石 by changhao
			return roleyb;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof YbNums.Data)) return false;
			YbNums.Data _o_ = (YbNums.Data) _o1_;
			if (!roleyb.equals(_o_.roleyb)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleyb.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleyb);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
