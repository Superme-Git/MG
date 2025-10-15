
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class FriendGiveNum extends mkdb.XBean implements xbean.FriendGiveNum {
	private java.util.HashMap<Long, Integer> givenummap; // key为角色id value为数量 *byte够了
	private long resettime; // 重置时间

	@Override
	public void _reset_unsafe_() {
		givenummap.clear();
		resettime = 0L;
	}

	FriendGiveNum(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		givenummap = new java.util.HashMap<Long, Integer>();
	}

	public FriendGiveNum() {
		this(0, null, null);
	}

	public FriendGiveNum(FriendGiveNum _o_) {
		this(_o_, null, null);
	}

	FriendGiveNum(xbean.FriendGiveNum _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof FriendGiveNum) assign((FriendGiveNum)_o1_);
		else if (_o1_ instanceof FriendGiveNum.Data) assign((FriendGiveNum.Data)_o1_);
		else if (_o1_ instanceof FriendGiveNum.Const) assign(((FriendGiveNum.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(FriendGiveNum _o_) {
		_o_._xdb_verify_unsafe_();
		givenummap = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.givenummap.entrySet())
			givenummap.put(_e_.getKey(), _e_.getValue());
		resettime = _o_.resettime;
	}

	private void assign(FriendGiveNum.Data _o_) {
		givenummap = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.givenummap.entrySet())
			givenummap.put(_e_.getKey(), _e_.getValue());
		resettime = _o_.resettime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(givenummap.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : givenummap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(resettime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				givenummap = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				givenummap.put(_k_, _v_);
			}
		}
		resettime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.FriendGiveNum copy() {
		_xdb_verify_unsafe_();
		return new FriendGiveNum(this);
	}

	@Override
	public xbean.FriendGiveNum toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FriendGiveNum toBean() {
		_xdb_verify_unsafe_();
		return new FriendGiveNum(this); // same as copy()
	}

	@Override
	public xbean.FriendGiveNum toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FriendGiveNum toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, Integer> getGivenummap() { // key为角色id value为数量 *byte够了
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "givenummap"), givenummap);
	}

	@Override
	public java.util.Map<Long, Integer> getGivenummapAsData() { // key为角色id value为数量 *byte够了
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> givenummap;
		FriendGiveNum _o_ = this;
		givenummap = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.givenummap.entrySet())
			givenummap.put(_e_.getKey(), _e_.getValue());
		return givenummap;
	}

	@Override
	public long getResettime() { // 重置时间
		_xdb_verify_unsafe_();
		return resettime;
	}

	@Override
	public void setResettime(long _v_) { // 重置时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "resettime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, resettime) {
					public void rollback() { resettime = _xdb_saved; }
				};}});
		resettime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		FriendGiveNum _o_ = null;
		if ( _o1_ instanceof FriendGiveNum ) _o_ = (FriendGiveNum)_o1_;
		else if ( _o1_ instanceof FriendGiveNum.Const ) _o_ = ((FriendGiveNum.Const)_o1_).nThis();
		else return false;
		if (!givenummap.equals(_o_.givenummap)) return false;
		if (resettime != _o_.resettime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += givenummap.hashCode();
		_h_ += resettime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(givenummap);
		_sb_.append(",");
		_sb_.append(resettime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("givenummap"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("resettime"));
		return lb;
	}

	private class Const implements xbean.FriendGiveNum {
		FriendGiveNum nThis() {
			return FriendGiveNum.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.FriendGiveNum copy() {
			return FriendGiveNum.this.copy();
		}

		@Override
		public xbean.FriendGiveNum toData() {
			return FriendGiveNum.this.toData();
		}

		public xbean.FriendGiveNum toBean() {
			return FriendGiveNum.this.toBean();
		}

		@Override
		public xbean.FriendGiveNum toDataIf() {
			return FriendGiveNum.this.toDataIf();
		}

		public xbean.FriendGiveNum toBeanIf() {
			return FriendGiveNum.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, Integer> getGivenummap() { // key为角色id value为数量 *byte够了
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(givenummap);
		}

		@Override
		public java.util.Map<Long, Integer> getGivenummapAsData() { // key为角色id value为数量 *byte够了
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> givenummap;
			FriendGiveNum _o_ = FriendGiveNum.this;
			givenummap = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.givenummap.entrySet())
				givenummap.put(_e_.getKey(), _e_.getValue());
			return givenummap;
		}

		@Override
		public long getResettime() { // 重置时间
			_xdb_verify_unsafe_();
			return resettime;
		}

		@Override
		public void setResettime(long _v_) { // 重置时间
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
			return FriendGiveNum.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return FriendGiveNum.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return FriendGiveNum.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return FriendGiveNum.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return FriendGiveNum.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return FriendGiveNum.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return FriendGiveNum.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return FriendGiveNum.this.hashCode();
		}

		@Override
		public String toString() {
			return FriendGiveNum.this.toString();
		}

	}

	public static final class Data implements xbean.FriendGiveNum {
		private java.util.HashMap<Long, Integer> givenummap; // key为角色id value为数量 *byte够了
		private long resettime; // 重置时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			givenummap = new java.util.HashMap<Long, Integer>();
		}

		Data(xbean.FriendGiveNum _o1_) {
			if (_o1_ instanceof FriendGiveNum) assign((FriendGiveNum)_o1_);
			else if (_o1_ instanceof FriendGiveNum.Data) assign((FriendGiveNum.Data)_o1_);
			else if (_o1_ instanceof FriendGiveNum.Const) assign(((FriendGiveNum.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(FriendGiveNum _o_) {
			givenummap = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.givenummap.entrySet())
				givenummap.put(_e_.getKey(), _e_.getValue());
			resettime = _o_.resettime;
		}

		private void assign(FriendGiveNum.Data _o_) {
			givenummap = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.givenummap.entrySet())
				givenummap.put(_e_.getKey(), _e_.getValue());
			resettime = _o_.resettime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(givenummap.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : givenummap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(resettime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					givenummap = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					givenummap.put(_k_, _v_);
				}
			}
			resettime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.FriendGiveNum copy() {
			return new Data(this);
		}

		@Override
		public xbean.FriendGiveNum toData() {
			return new Data(this);
		}

		public xbean.FriendGiveNum toBean() {
			return new FriendGiveNum(this, null, null);
		}

		@Override
		public xbean.FriendGiveNum toDataIf() {
			return this;
		}

		public xbean.FriendGiveNum toBeanIf() {
			return new FriendGiveNum(this, null, null);
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
		public java.util.Map<Long, Integer> getGivenummap() { // key为角色id value为数量 *byte够了
			return givenummap;
		}

		@Override
		public java.util.Map<Long, Integer> getGivenummapAsData() { // key为角色id value为数量 *byte够了
			return givenummap;
		}

		@Override
		public long getResettime() { // 重置时间
			return resettime;
		}

		@Override
		public void setResettime(long _v_) { // 重置时间
			resettime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof FriendGiveNum.Data)) return false;
			FriendGiveNum.Data _o_ = (FriendGiveNum.Data) _o1_;
			if (!givenummap.equals(_o_.givenummap)) return false;
			if (resettime != _o_.resettime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += givenummap.hashCode();
			_h_ += resettime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(givenummap);
			_sb_.append(",");
			_sb_.append(resettime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
