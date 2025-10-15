
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class LianyunAwardInfo extends mkdb.XBean implements xbean.LianyunAwardInfo {
	private java.util.HashMap<Integer, Long> awards; // 

	@Override
	public void _reset_unsafe_() {
		awards.clear();
	}

	LianyunAwardInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		awards = new java.util.HashMap<Integer, Long>();
	}

	public LianyunAwardInfo() {
		this(0, null, null);
	}

	public LianyunAwardInfo(LianyunAwardInfo _o_) {
		this(_o_, null, null);
	}

	LianyunAwardInfo(xbean.LianyunAwardInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LianyunAwardInfo) assign((LianyunAwardInfo)_o1_);
		else if (_o1_ instanceof LianyunAwardInfo.Data) assign((LianyunAwardInfo.Data)_o1_);
		else if (_o1_ instanceof LianyunAwardInfo.Const) assign(((LianyunAwardInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LianyunAwardInfo _o_) {
		_o_._xdb_verify_unsafe_();
		awards = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.awards.entrySet())
			awards.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(LianyunAwardInfo.Data _o_) {
		awards = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.awards.entrySet())
			awards.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(awards.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : awards.entrySet())
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
				awards = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				awards.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.LianyunAwardInfo copy() {
		_xdb_verify_unsafe_();
		return new LianyunAwardInfo(this);
	}

	@Override
	public xbean.LianyunAwardInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LianyunAwardInfo toBean() {
		_xdb_verify_unsafe_();
		return new LianyunAwardInfo(this); // same as copy()
	}

	@Override
	public xbean.LianyunAwardInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LianyunAwardInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Long> getAwards() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "awards"), awards);
	}

	@Override
	public java.util.Map<Integer, Long> getAwardsAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> awards;
		LianyunAwardInfo _o_ = this;
		awards = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.awards.entrySet())
			awards.put(_e_.getKey(), _e_.getValue());
		return awards;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LianyunAwardInfo _o_ = null;
		if ( _o1_ instanceof LianyunAwardInfo ) _o_ = (LianyunAwardInfo)_o1_;
		else if ( _o1_ instanceof LianyunAwardInfo.Const ) _o_ = ((LianyunAwardInfo.Const)_o1_).nThis();
		else return false;
		if (!awards.equals(_o_.awards)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += awards.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(awards);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("awards"));
		return lb;
	}

	private class Const implements xbean.LianyunAwardInfo {
		LianyunAwardInfo nThis() {
			return LianyunAwardInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LianyunAwardInfo copy() {
			return LianyunAwardInfo.this.copy();
		}

		@Override
		public xbean.LianyunAwardInfo toData() {
			return LianyunAwardInfo.this.toData();
		}

		public xbean.LianyunAwardInfo toBean() {
			return LianyunAwardInfo.this.toBean();
		}

		@Override
		public xbean.LianyunAwardInfo toDataIf() {
			return LianyunAwardInfo.this.toDataIf();
		}

		public xbean.LianyunAwardInfo toBeanIf() {
			return LianyunAwardInfo.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Long> getAwards() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(awards);
		}

		@Override
		public java.util.Map<Integer, Long> getAwardsAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> awards;
			LianyunAwardInfo _o_ = LianyunAwardInfo.this;
			awards = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.awards.entrySet())
				awards.put(_e_.getKey(), _e_.getValue());
			return awards;
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
			return LianyunAwardInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LianyunAwardInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return LianyunAwardInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LianyunAwardInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LianyunAwardInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LianyunAwardInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LianyunAwardInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LianyunAwardInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return LianyunAwardInfo.this.toString();
		}

	}

	public static final class Data implements xbean.LianyunAwardInfo {
		private java.util.HashMap<Integer, Long> awards; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			awards = new java.util.HashMap<Integer, Long>();
		}

		Data(xbean.LianyunAwardInfo _o1_) {
			if (_o1_ instanceof LianyunAwardInfo) assign((LianyunAwardInfo)_o1_);
			else if (_o1_ instanceof LianyunAwardInfo.Data) assign((LianyunAwardInfo.Data)_o1_);
			else if (_o1_ instanceof LianyunAwardInfo.Const) assign(((LianyunAwardInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LianyunAwardInfo _o_) {
			awards = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.awards.entrySet())
				awards.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(LianyunAwardInfo.Data _o_) {
			awards = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.awards.entrySet())
				awards.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(awards.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : awards.entrySet())
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
					awards = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					awards.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.LianyunAwardInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.LianyunAwardInfo toData() {
			return new Data(this);
		}

		public xbean.LianyunAwardInfo toBean() {
			return new LianyunAwardInfo(this, null, null);
		}

		@Override
		public xbean.LianyunAwardInfo toDataIf() {
			return this;
		}

		public xbean.LianyunAwardInfo toBeanIf() {
			return new LianyunAwardInfo(this, null, null);
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
		public java.util.Map<Integer, Long> getAwards() { // 
			return awards;
		}

		@Override
		public java.util.Map<Integer, Long> getAwardsAsData() { // 
			return awards;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LianyunAwardInfo.Data)) return false;
			LianyunAwardInfo.Data _o_ = (LianyunAwardInfo.Data) _o1_;
			if (!awards.equals(_o_.awards)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += awards.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(awards);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
