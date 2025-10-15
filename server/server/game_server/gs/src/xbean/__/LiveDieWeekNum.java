
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class LiveDieWeekNum extends mkdb.XBean implements xbean.LiveDieWeekNum {
	private long weeknum; // 生死战清除录像记录

	@Override
	public void _reset_unsafe_() {
		weeknum = -1;
	}

	LiveDieWeekNum(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		weeknum = -1;
	}

	public LiveDieWeekNum() {
		this(0, null, null);
	}

	public LiveDieWeekNum(LiveDieWeekNum _o_) {
		this(_o_, null, null);
	}

	LiveDieWeekNum(xbean.LiveDieWeekNum _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LiveDieWeekNum) assign((LiveDieWeekNum)_o1_);
		else if (_o1_ instanceof LiveDieWeekNum.Data) assign((LiveDieWeekNum.Data)_o1_);
		else if (_o1_ instanceof LiveDieWeekNum.Const) assign(((LiveDieWeekNum.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LiveDieWeekNum _o_) {
		_o_._xdb_verify_unsafe_();
		weeknum = _o_.weeknum;
	}

	private void assign(LiveDieWeekNum.Data _o_) {
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
	public xbean.LiveDieWeekNum copy() {
		_xdb_verify_unsafe_();
		return new LiveDieWeekNum(this);
	}

	@Override
	public xbean.LiveDieWeekNum toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LiveDieWeekNum toBean() {
		_xdb_verify_unsafe_();
		return new LiveDieWeekNum(this); // same as copy()
	}

	@Override
	public xbean.LiveDieWeekNum toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LiveDieWeekNum toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getWeeknum() { // 生死战清除录像记录
		_xdb_verify_unsafe_();
		return weeknum;
	}

	@Override
	public void setWeeknum(long _v_) { // 生死战清除录像记录
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
		LiveDieWeekNum _o_ = null;
		if ( _o1_ instanceof LiveDieWeekNum ) _o_ = (LiveDieWeekNum)_o1_;
		else if ( _o1_ instanceof LiveDieWeekNum.Const ) _o_ = ((LiveDieWeekNum.Const)_o1_).nThis();
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

	private class Const implements xbean.LiveDieWeekNum {
		LiveDieWeekNum nThis() {
			return LiveDieWeekNum.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LiveDieWeekNum copy() {
			return LiveDieWeekNum.this.copy();
		}

		@Override
		public xbean.LiveDieWeekNum toData() {
			return LiveDieWeekNum.this.toData();
		}

		public xbean.LiveDieWeekNum toBean() {
			return LiveDieWeekNum.this.toBean();
		}

		@Override
		public xbean.LiveDieWeekNum toDataIf() {
			return LiveDieWeekNum.this.toDataIf();
		}

		public xbean.LiveDieWeekNum toBeanIf() {
			return LiveDieWeekNum.this.toBeanIf();
		}

		@Override
		public long getWeeknum() { // 生死战清除录像记录
			_xdb_verify_unsafe_();
			return weeknum;
		}

		@Override
		public void setWeeknum(long _v_) { // 生死战清除录像记录
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
			return LiveDieWeekNum.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LiveDieWeekNum.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return LiveDieWeekNum.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LiveDieWeekNum.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LiveDieWeekNum.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LiveDieWeekNum.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LiveDieWeekNum.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LiveDieWeekNum.this.hashCode();
		}

		@Override
		public String toString() {
			return LiveDieWeekNum.this.toString();
		}

	}

	public static final class Data implements xbean.LiveDieWeekNum {
		private long weeknum; // 生死战清除录像记录

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			weeknum = -1;
		}

		Data(xbean.LiveDieWeekNum _o1_) {
			if (_o1_ instanceof LiveDieWeekNum) assign((LiveDieWeekNum)_o1_);
			else if (_o1_ instanceof LiveDieWeekNum.Data) assign((LiveDieWeekNum.Data)_o1_);
			else if (_o1_ instanceof LiveDieWeekNum.Const) assign(((LiveDieWeekNum.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LiveDieWeekNum _o_) {
			weeknum = _o_.weeknum;
		}

		private void assign(LiveDieWeekNum.Data _o_) {
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
		public xbean.LiveDieWeekNum copy() {
			return new Data(this);
		}

		@Override
		public xbean.LiveDieWeekNum toData() {
			return new Data(this);
		}

		public xbean.LiveDieWeekNum toBean() {
			return new LiveDieWeekNum(this, null, null);
		}

		@Override
		public xbean.LiveDieWeekNum toDataIf() {
			return this;
		}

		public xbean.LiveDieWeekNum toBeanIf() {
			return new LiveDieWeekNum(this, null, null);
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
		public long getWeeknum() { // 生死战清除录像记录
			return weeknum;
		}

		@Override
		public void setWeeknum(long _v_) { // 生死战清除录像记录
			weeknum = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LiveDieWeekNum.Data)) return false;
			LiveDieWeekNum.Data _o_ = (LiveDieWeekNum.Data) _o1_;
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
