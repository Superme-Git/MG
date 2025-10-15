
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanFights extends mkdb.XBean implements xbean.ClanFights {
	private long createtime; // 创建时间 by changhao
	private java.util.HashMap<Long, xbean.ClanFight> clan1vschan2; // 配对的公会战 by changhao

	@Override
	public void _reset_unsafe_() {
		createtime = 0L;
		clan1vschan2.clear();
	}

	ClanFights(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		clan1vschan2 = new java.util.HashMap<Long, xbean.ClanFight>();
	}

	public ClanFights() {
		this(0, null, null);
	}

	public ClanFights(ClanFights _o_) {
		this(_o_, null, null);
	}

	ClanFights(xbean.ClanFights _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanFights) assign((ClanFights)_o1_);
		else if (_o1_ instanceof ClanFights.Data) assign((ClanFights.Data)_o1_);
		else if (_o1_ instanceof ClanFights.Const) assign(((ClanFights.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanFights _o_) {
		_o_._xdb_verify_unsafe_();
		createtime = _o_.createtime;
		clan1vschan2 = new java.util.HashMap<Long, xbean.ClanFight>();
		for (java.util.Map.Entry<Long, xbean.ClanFight> _e_ : _o_.clan1vschan2.entrySet())
			clan1vschan2.put(_e_.getKey(), new ClanFight(_e_.getValue(), this, "clan1vschan2"));
	}

	private void assign(ClanFights.Data _o_) {
		createtime = _o_.createtime;
		clan1vschan2 = new java.util.HashMap<Long, xbean.ClanFight>();
		for (java.util.Map.Entry<Long, xbean.ClanFight> _e_ : _o_.clan1vschan2.entrySet())
			clan1vschan2.put(_e_.getKey(), new ClanFight(_e_.getValue(), this, "clan1vschan2"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(createtime);
		_os_.compact_uint32(clan1vschan2.size());
		for (java.util.Map.Entry<Long, xbean.ClanFight> _e_ : clan1vschan2.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		createtime = _os_.unmarshal_long();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				clan1vschan2 = new java.util.HashMap<Long, xbean.ClanFight>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.ClanFight _v_ = new ClanFight(0, this, "clan1vschan2");
				_v_.unmarshal(_os_);
				clan1vschan2.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.ClanFights copy() {
		_xdb_verify_unsafe_();
		return new ClanFights(this);
	}

	@Override
	public xbean.ClanFights toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanFights toBean() {
		_xdb_verify_unsafe_();
		return new ClanFights(this); // same as copy()
	}

	@Override
	public xbean.ClanFights toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanFights toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getCreatetime() { // 创建时间 by changhao
		_xdb_verify_unsafe_();
		return createtime;
	}

	@Override
	public java.util.Map<Long, xbean.ClanFight> getClan1vschan2() { // 配对的公会战 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "clan1vschan2"), clan1vschan2);
	}

	@Override
	public java.util.Map<Long, xbean.ClanFight> getClan1vschan2AsData() { // 配对的公会战 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.ClanFight> clan1vschan2;
		ClanFights _o_ = this;
		clan1vschan2 = new java.util.HashMap<Long, xbean.ClanFight>();
		for (java.util.Map.Entry<Long, xbean.ClanFight> _e_ : _o_.clan1vschan2.entrySet())
			clan1vschan2.put(_e_.getKey(), new ClanFight.Data(_e_.getValue()));
		return clan1vschan2;
	}

	@Override
	public void setCreatetime(long _v_) { // 创建时间 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "createtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, createtime) {
					public void rollback() { createtime = _xdb_saved; }
				};}});
		createtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanFights _o_ = null;
		if ( _o1_ instanceof ClanFights ) _o_ = (ClanFights)_o1_;
		else if ( _o1_ instanceof ClanFights.Const ) _o_ = ((ClanFights.Const)_o1_).nThis();
		else return false;
		if (createtime != _o_.createtime) return false;
		if (!clan1vschan2.equals(_o_.clan1vschan2)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += createtime;
		_h_ += clan1vschan2.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(createtime);
		_sb_.append(",");
		_sb_.append(clan1vschan2);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("createtime"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("clan1vschan2"));
		return lb;
	}

	private class Const implements xbean.ClanFights {
		ClanFights nThis() {
			return ClanFights.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanFights copy() {
			return ClanFights.this.copy();
		}

		@Override
		public xbean.ClanFights toData() {
			return ClanFights.this.toData();
		}

		public xbean.ClanFights toBean() {
			return ClanFights.this.toBean();
		}

		@Override
		public xbean.ClanFights toDataIf() {
			return ClanFights.this.toDataIf();
		}

		public xbean.ClanFights toBeanIf() {
			return ClanFights.this.toBeanIf();
		}

		@Override
		public long getCreatetime() { // 创建时间 by changhao
			_xdb_verify_unsafe_();
			return createtime;
		}

		@Override
		public java.util.Map<Long, xbean.ClanFight> getClan1vschan2() { // 配对的公会战 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(clan1vschan2);
		}

		@Override
		public java.util.Map<Long, xbean.ClanFight> getClan1vschan2AsData() { // 配对的公会战 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.ClanFight> clan1vschan2;
			ClanFights _o_ = ClanFights.this;
			clan1vschan2 = new java.util.HashMap<Long, xbean.ClanFight>();
			for (java.util.Map.Entry<Long, xbean.ClanFight> _e_ : _o_.clan1vschan2.entrySet())
				clan1vschan2.put(_e_.getKey(), new ClanFight.Data(_e_.getValue()));
			return clan1vschan2;
		}

		@Override
		public void setCreatetime(long _v_) { // 创建时间 by changhao
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
			return ClanFights.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanFights.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanFights.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanFights.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanFights.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanFights.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanFights.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanFights.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanFights.this.toString();
		}

	}

	public static final class Data implements xbean.ClanFights {
		private long createtime; // 创建时间 by changhao
		private java.util.HashMap<Long, xbean.ClanFight> clan1vschan2; // 配对的公会战 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			clan1vschan2 = new java.util.HashMap<Long, xbean.ClanFight>();
		}

		Data(xbean.ClanFights _o1_) {
			if (_o1_ instanceof ClanFights) assign((ClanFights)_o1_);
			else if (_o1_ instanceof ClanFights.Data) assign((ClanFights.Data)_o1_);
			else if (_o1_ instanceof ClanFights.Const) assign(((ClanFights.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanFights _o_) {
			createtime = _o_.createtime;
			clan1vschan2 = new java.util.HashMap<Long, xbean.ClanFight>();
			for (java.util.Map.Entry<Long, xbean.ClanFight> _e_ : _o_.clan1vschan2.entrySet())
				clan1vschan2.put(_e_.getKey(), new ClanFight.Data(_e_.getValue()));
		}

		private void assign(ClanFights.Data _o_) {
			createtime = _o_.createtime;
			clan1vschan2 = new java.util.HashMap<Long, xbean.ClanFight>();
			for (java.util.Map.Entry<Long, xbean.ClanFight> _e_ : _o_.clan1vschan2.entrySet())
				clan1vschan2.put(_e_.getKey(), new ClanFight.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(createtime);
			_os_.compact_uint32(clan1vschan2.size());
			for (java.util.Map.Entry<Long, xbean.ClanFight> _e_ : clan1vschan2.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			createtime = _os_.unmarshal_long();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					clan1vschan2 = new java.util.HashMap<Long, xbean.ClanFight>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.ClanFight _v_ = xbean.Pod.newClanFightData();
					_v_.unmarshal(_os_);
					clan1vschan2.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.ClanFights copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanFights toData() {
			return new Data(this);
		}

		public xbean.ClanFights toBean() {
			return new ClanFights(this, null, null);
		}

		@Override
		public xbean.ClanFights toDataIf() {
			return this;
		}

		public xbean.ClanFights toBeanIf() {
			return new ClanFights(this, null, null);
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
		public long getCreatetime() { // 创建时间 by changhao
			return createtime;
		}

		@Override
		public java.util.Map<Long, xbean.ClanFight> getClan1vschan2() { // 配对的公会战 by changhao
			return clan1vschan2;
		}

		@Override
		public java.util.Map<Long, xbean.ClanFight> getClan1vschan2AsData() { // 配对的公会战 by changhao
			return clan1vschan2;
		}

		@Override
		public void setCreatetime(long _v_) { // 创建时间 by changhao
			createtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanFights.Data)) return false;
			ClanFights.Data _o_ = (ClanFights.Data) _o1_;
			if (createtime != _o_.createtime) return false;
			if (!clan1vschan2.equals(_o_.clan1vschan2)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += createtime;
			_h_ += clan1vschan2.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(createtime);
			_sb_.append(",");
			_sb_.append(clan1vschan2);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
