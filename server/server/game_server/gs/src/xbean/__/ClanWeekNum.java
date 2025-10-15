
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanWeekNum extends mkdb.XBean implements xbean.ClanWeekNum {
	private long weeknum; // 公会周更新记录周数

	@Override
	public void _reset_unsafe_() {
		weeknum = -1;
	}

	ClanWeekNum(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		weeknum = -1;
	}

	public ClanWeekNum() {
		this(0, null, null);
	}

	public ClanWeekNum(ClanWeekNum _o_) {
		this(_o_, null, null);
	}

	ClanWeekNum(xbean.ClanWeekNum _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanWeekNum) assign((ClanWeekNum)_o1_);
		else if (_o1_ instanceof ClanWeekNum.Data) assign((ClanWeekNum.Data)_o1_);
		else if (_o1_ instanceof ClanWeekNum.Const) assign(((ClanWeekNum.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanWeekNum _o_) {
		_o_._xdb_verify_unsafe_();
		weeknum = _o_.weeknum;
	}

	private void assign(ClanWeekNum.Data _o_) {
		weeknum = _o_.weeknum;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(weeknum);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		weeknum = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ClanWeekNum copy() {
		_xdb_verify_unsafe_();
		return new ClanWeekNum(this);
	}

	@Override
	public xbean.ClanWeekNum toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanWeekNum toBean() {
		_xdb_verify_unsafe_();
		return new ClanWeekNum(this); // same as copy()
	}

	@Override
	public xbean.ClanWeekNum toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanWeekNum toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getWeeknum() { // 公会周更新记录周数
		_xdb_verify_unsafe_();
		return weeknum;
	}

	@Override
	public void setWeeknum(long _v_) { // 公会周更新记录周数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "weeknum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, weeknum) {
					public void rollback() { weeknum = _xdb_saved; }
				};}});
		weeknum = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanWeekNum _o_ = null;
		if ( _o1_ instanceof ClanWeekNum ) _o_ = (ClanWeekNum)_o1_;
		else if ( _o1_ instanceof ClanWeekNum.Const ) _o_ = ((ClanWeekNum.Const)_o1_).nThis();
		else return false;
		if (weeknum != _o_.weeknum) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += weeknum;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(weeknum);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("weeknum"));
		return lb;
	}

	private class Const implements xbean.ClanWeekNum {
		ClanWeekNum nThis() {
			return ClanWeekNum.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanWeekNum copy() {
			return ClanWeekNum.this.copy();
		}

		@Override
		public xbean.ClanWeekNum toData() {
			return ClanWeekNum.this.toData();
		}

		public xbean.ClanWeekNum toBean() {
			return ClanWeekNum.this.toBean();
		}

		@Override
		public xbean.ClanWeekNum toDataIf() {
			return ClanWeekNum.this.toDataIf();
		}

		public xbean.ClanWeekNum toBeanIf() {
			return ClanWeekNum.this.toBeanIf();
		}

		@Override
		public long getWeeknum() { // 公会周更新记录周数
			_xdb_verify_unsafe_();
			return weeknum;
		}

		@Override
		public void setWeeknum(long _v_) { // 公会周更新记录周数
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
			return ClanWeekNum.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanWeekNum.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanWeekNum.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanWeekNum.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanWeekNum.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanWeekNum.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanWeekNum.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanWeekNum.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanWeekNum.this.toString();
		}

	}

	public static final class Data implements xbean.ClanWeekNum {
		private long weeknum; // 公会周更新记录周数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			weeknum = -1;
		}

		Data(xbean.ClanWeekNum _o1_) {
			if (_o1_ instanceof ClanWeekNum) assign((ClanWeekNum)_o1_);
			else if (_o1_ instanceof ClanWeekNum.Data) assign((ClanWeekNum.Data)_o1_);
			else if (_o1_ instanceof ClanWeekNum.Const) assign(((ClanWeekNum.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanWeekNum _o_) {
			weeknum = _o_.weeknum;
		}

		private void assign(ClanWeekNum.Data _o_) {
			weeknum = _o_.weeknum;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(weeknum);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			weeknum = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ClanWeekNum copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanWeekNum toData() {
			return new Data(this);
		}

		public xbean.ClanWeekNum toBean() {
			return new ClanWeekNum(this, null, null);
		}

		@Override
		public xbean.ClanWeekNum toDataIf() {
			return this;
		}

		public xbean.ClanWeekNum toBeanIf() {
			return new ClanWeekNum(this, null, null);
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
		public long getWeeknum() { // 公会周更新记录周数
			return weeknum;
		}

		@Override
		public void setWeeknum(long _v_) { // 公会周更新记录周数
			weeknum = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanWeekNum.Data)) return false;
			ClanWeekNum.Data _o_ = (ClanWeekNum.Data) _o1_;
			if (weeknum != _o_.weeknum) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += weeknum;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(weeknum);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
