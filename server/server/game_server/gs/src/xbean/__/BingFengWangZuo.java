
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BingFengWangZuo extends mkdb.XBean implements xbean.BingFengWangZuo {
	private java.util.HashMap<Long, Integer> deathtimes; // 死亡次数 key为角色id

	@Override
	public void _reset_unsafe_() {
		deathtimes.clear();
	}

	BingFengWangZuo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		deathtimes = new java.util.HashMap<Long, Integer>();
	}

	public BingFengWangZuo() {
		this(0, null, null);
	}

	public BingFengWangZuo(BingFengWangZuo _o_) {
		this(_o_, null, null);
	}

	BingFengWangZuo(xbean.BingFengWangZuo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BingFengWangZuo) assign((BingFengWangZuo)_o1_);
		else if (_o1_ instanceof BingFengWangZuo.Data) assign((BingFengWangZuo.Data)_o1_);
		else if (_o1_ instanceof BingFengWangZuo.Const) assign(((BingFengWangZuo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BingFengWangZuo _o_) {
		_o_._xdb_verify_unsafe_();
		deathtimes = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.deathtimes.entrySet())
			deathtimes.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(BingFengWangZuo.Data _o_) {
		deathtimes = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.deathtimes.entrySet())
			deathtimes.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(deathtimes.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : deathtimes.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				deathtimes = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				deathtimes.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.BingFengWangZuo copy() {
		_xdb_verify_unsafe_();
		return new BingFengWangZuo(this);
	}

	@Override
	public xbean.BingFengWangZuo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BingFengWangZuo toBean() {
		_xdb_verify_unsafe_();
		return new BingFengWangZuo(this); // same as copy()
	}

	@Override
	public xbean.BingFengWangZuo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BingFengWangZuo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, Integer> getDeathtimes() { // 死亡次数 key为角色id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "deathtimes"), deathtimes);
	}

	@Override
	public java.util.Map<Long, Integer> getDeathtimesAsData() { // 死亡次数 key为角色id
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> deathtimes;
		BingFengWangZuo _o_ = this;
		deathtimes = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.deathtimes.entrySet())
			deathtimes.put(_e_.getKey(), _e_.getValue());
		return deathtimes;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BingFengWangZuo _o_ = null;
		if ( _o1_ instanceof BingFengWangZuo ) _o_ = (BingFengWangZuo)_o1_;
		else if ( _o1_ instanceof BingFengWangZuo.Const ) _o_ = ((BingFengWangZuo.Const)_o1_).nThis();
		else return false;
		if (!deathtimes.equals(_o_.deathtimes)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += deathtimes.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(deathtimes);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("deathtimes"));
		return lb;
	}

	private class Const implements xbean.BingFengWangZuo {
		BingFengWangZuo nThis() {
			return BingFengWangZuo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BingFengWangZuo copy() {
			return BingFengWangZuo.this.copy();
		}

		@Override
		public xbean.BingFengWangZuo toData() {
			return BingFengWangZuo.this.toData();
		}

		public xbean.BingFengWangZuo toBean() {
			return BingFengWangZuo.this.toBean();
		}

		@Override
		public xbean.BingFengWangZuo toDataIf() {
			return BingFengWangZuo.this.toDataIf();
		}

		public xbean.BingFengWangZuo toBeanIf() {
			return BingFengWangZuo.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, Integer> getDeathtimes() { // 死亡次数 key为角色id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(deathtimes);
		}

		@Override
		public java.util.Map<Long, Integer> getDeathtimesAsData() { // 死亡次数 key为角色id
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> deathtimes;
			BingFengWangZuo _o_ = BingFengWangZuo.this;
			deathtimes = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.deathtimes.entrySet())
				deathtimes.put(_e_.getKey(), _e_.getValue());
			return deathtimes;
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
			return BingFengWangZuo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BingFengWangZuo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BingFengWangZuo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BingFengWangZuo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BingFengWangZuo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BingFengWangZuo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BingFengWangZuo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BingFengWangZuo.this.hashCode();
		}

		@Override
		public String toString() {
			return BingFengWangZuo.this.toString();
		}

	}

	public static final class Data implements xbean.BingFengWangZuo {
		private java.util.HashMap<Long, Integer> deathtimes; // 死亡次数 key为角色id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			deathtimes = new java.util.HashMap<Long, Integer>();
		}

		Data(xbean.BingFengWangZuo _o1_) {
			if (_o1_ instanceof BingFengWangZuo) assign((BingFengWangZuo)_o1_);
			else if (_o1_ instanceof BingFengWangZuo.Data) assign((BingFengWangZuo.Data)_o1_);
			else if (_o1_ instanceof BingFengWangZuo.Const) assign(((BingFengWangZuo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BingFengWangZuo _o_) {
			deathtimes = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.deathtimes.entrySet())
				deathtimes.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(BingFengWangZuo.Data _o_) {
			deathtimes = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.deathtimes.entrySet())
				deathtimes.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(deathtimes.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : deathtimes.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					deathtimes = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					deathtimes.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.BingFengWangZuo copy() {
			return new Data(this);
		}

		@Override
		public xbean.BingFengWangZuo toData() {
			return new Data(this);
		}

		public xbean.BingFengWangZuo toBean() {
			return new BingFengWangZuo(this, null, null);
		}

		@Override
		public xbean.BingFengWangZuo toDataIf() {
			return this;
		}

		public xbean.BingFengWangZuo toBeanIf() {
			return new BingFengWangZuo(this, null, null);
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
		public java.util.Map<Long, Integer> getDeathtimes() { // 死亡次数 key为角色id
			return deathtimes;
		}

		@Override
		public java.util.Map<Long, Integer> getDeathtimesAsData() { // 死亡次数 key为角色id
			return deathtimes;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BingFengWangZuo.Data)) return false;
			BingFengWangZuo.Data _o_ = (BingFengWangZuo.Data) _o1_;
			if (!deathtimes.equals(_o_.deathtimes)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += deathtimes.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(deathtimes);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
