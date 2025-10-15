
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RegRec extends mkdb.XBean implements xbean.RegRec {
	private java.util.HashMap<Integer, xbean.RegMonth> monthmap; // 月Map
	private long lastregtime; // 上次签到时间

	@Override
	public void _reset_unsafe_() {
		monthmap.clear();
		lastregtime = 0L;
	}

	RegRec(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		monthmap = new java.util.HashMap<Integer, xbean.RegMonth>();
	}

	public RegRec() {
		this(0, null, null);
	}

	public RegRec(RegRec _o_) {
		this(_o_, null, null);
	}

	RegRec(xbean.RegRec _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RegRec) assign((RegRec)_o1_);
		else if (_o1_ instanceof RegRec.Data) assign((RegRec.Data)_o1_);
		else if (_o1_ instanceof RegRec.Const) assign(((RegRec.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RegRec _o_) {
		_o_._xdb_verify_unsafe_();
		monthmap = new java.util.HashMap<Integer, xbean.RegMonth>();
		for (java.util.Map.Entry<Integer, xbean.RegMonth> _e_ : _o_.monthmap.entrySet())
			monthmap.put(_e_.getKey(), new RegMonth(_e_.getValue(), this, "monthmap"));
		lastregtime = _o_.lastregtime;
	}

	private void assign(RegRec.Data _o_) {
		monthmap = new java.util.HashMap<Integer, xbean.RegMonth>();
		for (java.util.Map.Entry<Integer, xbean.RegMonth> _e_ : _o_.monthmap.entrySet())
			monthmap.put(_e_.getKey(), new RegMonth(_e_.getValue(), this, "monthmap"));
		lastregtime = _o_.lastregtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(monthmap.size());
		for (java.util.Map.Entry<Integer, xbean.RegMonth> _e_ : monthmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(lastregtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				monthmap = new java.util.HashMap<Integer, xbean.RegMonth>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.RegMonth _v_ = new RegMonth(0, this, "monthmap");
				_v_.unmarshal(_os_);
				monthmap.put(_k_, _v_);
			}
		}
		lastregtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RegRec copy() {
		_xdb_verify_unsafe_();
		return new RegRec(this);
	}

	@Override
	public xbean.RegRec toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RegRec toBean() {
		_xdb_verify_unsafe_();
		return new RegRec(this); // same as copy()
	}

	@Override
	public xbean.RegRec toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RegRec toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.RegMonth> getMonthmap() { // 月Map
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "monthmap"), monthmap);
	}

	@Override
	public java.util.Map<Integer, xbean.RegMonth> getMonthmapAsData() { // 月Map
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.RegMonth> monthmap;
		RegRec _o_ = this;
		monthmap = new java.util.HashMap<Integer, xbean.RegMonth>();
		for (java.util.Map.Entry<Integer, xbean.RegMonth> _e_ : _o_.monthmap.entrySet())
			monthmap.put(_e_.getKey(), new RegMonth.Data(_e_.getValue()));
		return monthmap;
	}

	@Override
	public long getLastregtime() { // 上次签到时间
		_xdb_verify_unsafe_();
		return lastregtime;
	}

	@Override
	public void setLastregtime(long _v_) { // 上次签到时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastregtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastregtime) {
					public void rollback() { lastregtime = _xdb_saved; }
				};}});
		lastregtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RegRec _o_ = null;
		if ( _o1_ instanceof RegRec ) _o_ = (RegRec)_o1_;
		else if ( _o1_ instanceof RegRec.Const ) _o_ = ((RegRec.Const)_o1_).nThis();
		else return false;
		if (!monthmap.equals(_o_.monthmap)) return false;
		if (lastregtime != _o_.lastregtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += monthmap.hashCode();
		_h_ += lastregtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(monthmap);
		_sb_.append(",");
		_sb_.append(lastregtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("monthmap"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastregtime"));
		return lb;
	}

	private class Const implements xbean.RegRec {
		RegRec nThis() {
			return RegRec.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RegRec copy() {
			return RegRec.this.copy();
		}

		@Override
		public xbean.RegRec toData() {
			return RegRec.this.toData();
		}

		public xbean.RegRec toBean() {
			return RegRec.this.toBean();
		}

		@Override
		public xbean.RegRec toDataIf() {
			return RegRec.this.toDataIf();
		}

		public xbean.RegRec toBeanIf() {
			return RegRec.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.RegMonth> getMonthmap() { // 月Map
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(monthmap);
		}

		@Override
		public java.util.Map<Integer, xbean.RegMonth> getMonthmapAsData() { // 月Map
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.RegMonth> monthmap;
			RegRec _o_ = RegRec.this;
			monthmap = new java.util.HashMap<Integer, xbean.RegMonth>();
			for (java.util.Map.Entry<Integer, xbean.RegMonth> _e_ : _o_.monthmap.entrySet())
				monthmap.put(_e_.getKey(), new RegMonth.Data(_e_.getValue()));
			return monthmap;
		}

		@Override
		public long getLastregtime() { // 上次签到时间
			_xdb_verify_unsafe_();
			return lastregtime;
		}

		@Override
		public void setLastregtime(long _v_) { // 上次签到时间
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
			return RegRec.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RegRec.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RegRec.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RegRec.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RegRec.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RegRec.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RegRec.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RegRec.this.hashCode();
		}

		@Override
		public String toString() {
			return RegRec.this.toString();
		}

	}

	public static final class Data implements xbean.RegRec {
		private java.util.HashMap<Integer, xbean.RegMonth> monthmap; // 月Map
		private long lastregtime; // 上次签到时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			monthmap = new java.util.HashMap<Integer, xbean.RegMonth>();
		}

		Data(xbean.RegRec _o1_) {
			if (_o1_ instanceof RegRec) assign((RegRec)_o1_);
			else if (_o1_ instanceof RegRec.Data) assign((RegRec.Data)_o1_);
			else if (_o1_ instanceof RegRec.Const) assign(((RegRec.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RegRec _o_) {
			monthmap = new java.util.HashMap<Integer, xbean.RegMonth>();
			for (java.util.Map.Entry<Integer, xbean.RegMonth> _e_ : _o_.monthmap.entrySet())
				monthmap.put(_e_.getKey(), new RegMonth.Data(_e_.getValue()));
			lastregtime = _o_.lastregtime;
		}

		private void assign(RegRec.Data _o_) {
			monthmap = new java.util.HashMap<Integer, xbean.RegMonth>();
			for (java.util.Map.Entry<Integer, xbean.RegMonth> _e_ : _o_.monthmap.entrySet())
				monthmap.put(_e_.getKey(), new RegMonth.Data(_e_.getValue()));
			lastregtime = _o_.lastregtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(monthmap.size());
			for (java.util.Map.Entry<Integer, xbean.RegMonth> _e_ : monthmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(lastregtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					monthmap = new java.util.HashMap<Integer, xbean.RegMonth>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.RegMonth _v_ = xbean.Pod.newRegMonthData();
					_v_.unmarshal(_os_);
					monthmap.put(_k_, _v_);
				}
			}
			lastregtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RegRec copy() {
			return new Data(this);
		}

		@Override
		public xbean.RegRec toData() {
			return new Data(this);
		}

		public xbean.RegRec toBean() {
			return new RegRec(this, null, null);
		}

		@Override
		public xbean.RegRec toDataIf() {
			return this;
		}

		public xbean.RegRec toBeanIf() {
			return new RegRec(this, null, null);
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
		public java.util.Map<Integer, xbean.RegMonth> getMonthmap() { // 月Map
			return monthmap;
		}

		@Override
		public java.util.Map<Integer, xbean.RegMonth> getMonthmapAsData() { // 月Map
			return monthmap;
		}

		@Override
		public long getLastregtime() { // 上次签到时间
			return lastregtime;
		}

		@Override
		public void setLastregtime(long _v_) { // 上次签到时间
			lastregtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RegRec.Data)) return false;
			RegRec.Data _o_ = (RegRec.Data) _o1_;
			if (!monthmap.equals(_o_.monthmap)) return false;
			if (lastregtime != _o_.lastregtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += monthmap.hashCode();
			_h_ += lastregtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(monthmap);
			_sb_.append(",");
			_sb_.append(lastregtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
