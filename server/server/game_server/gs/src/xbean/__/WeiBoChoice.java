
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class WeiBoChoice extends mkdb.XBean implements xbean.WeiBoChoice {
	private int times; // 次数
	private int setting; // 设定

	@Override
	public void _reset_unsafe_() {
		times = 0;
		setting = 0;
	}

	WeiBoChoice(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public WeiBoChoice() {
		this(0, null, null);
	}

	public WeiBoChoice(WeiBoChoice _o_) {
		this(_o_, null, null);
	}

	WeiBoChoice(xbean.WeiBoChoice _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof WeiBoChoice) assign((WeiBoChoice)_o1_);
		else if (_o1_ instanceof WeiBoChoice.Data) assign((WeiBoChoice.Data)_o1_);
		else if (_o1_ instanceof WeiBoChoice.Const) assign(((WeiBoChoice.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(WeiBoChoice _o_) {
		_o_._xdb_verify_unsafe_();
		times = _o_.times;
		setting = _o_.setting;
	}

	private void assign(WeiBoChoice.Data _o_) {
		times = _o_.times;
		setting = _o_.setting;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(times);
		_os_.marshal(setting);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		times = _os_.unmarshal_int();
		setting = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.WeiBoChoice copy() {
		_xdb_verify_unsafe_();
		return new WeiBoChoice(this);
	}

	@Override
	public xbean.WeiBoChoice toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WeiBoChoice toBean() {
		_xdb_verify_unsafe_();
		return new WeiBoChoice(this); // same as copy()
	}

	@Override
	public xbean.WeiBoChoice toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WeiBoChoice toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getTimes() { // 次数
		_xdb_verify_unsafe_();
		return times;
	}

	@Override
	public int getSetting() { // 设定
		_xdb_verify_unsafe_();
		return setting;
	}

	@Override
	public void setTimes(int _v_) { // 次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "times") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, times) {
					public void rollback() { times = _xdb_saved; }
				};}});
		times = _v_;
	}

	@Override
	public void setSetting(int _v_) { // 设定
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "setting") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, setting) {
					public void rollback() { setting = _xdb_saved; }
				};}});
		setting = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		WeiBoChoice _o_ = null;
		if ( _o1_ instanceof WeiBoChoice ) _o_ = (WeiBoChoice)_o1_;
		else if ( _o1_ instanceof WeiBoChoice.Const ) _o_ = ((WeiBoChoice.Const)_o1_).nThis();
		else return false;
		if (times != _o_.times) return false;
		if (setting != _o_.setting) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += times;
		_h_ += setting;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(times);
		_sb_.append(",");
		_sb_.append(setting);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("times"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("setting"));
		return lb;
	}

	private class Const implements xbean.WeiBoChoice {
		WeiBoChoice nThis() {
			return WeiBoChoice.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.WeiBoChoice copy() {
			return WeiBoChoice.this.copy();
		}

		@Override
		public xbean.WeiBoChoice toData() {
			return WeiBoChoice.this.toData();
		}

		public xbean.WeiBoChoice toBean() {
			return WeiBoChoice.this.toBean();
		}

		@Override
		public xbean.WeiBoChoice toDataIf() {
			return WeiBoChoice.this.toDataIf();
		}

		public xbean.WeiBoChoice toBeanIf() {
			return WeiBoChoice.this.toBeanIf();
		}

		@Override
		public int getTimes() { // 次数
			_xdb_verify_unsafe_();
			return times;
		}

		@Override
		public int getSetting() { // 设定
			_xdb_verify_unsafe_();
			return setting;
		}

		@Override
		public void setTimes(int _v_) { // 次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSetting(int _v_) { // 设定
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
			return WeiBoChoice.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return WeiBoChoice.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return WeiBoChoice.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return WeiBoChoice.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return WeiBoChoice.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return WeiBoChoice.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return WeiBoChoice.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return WeiBoChoice.this.hashCode();
		}

		@Override
		public String toString() {
			return WeiBoChoice.this.toString();
		}

	}

	public static final class Data implements xbean.WeiBoChoice {
		private int times; // 次数
		private int setting; // 设定

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.WeiBoChoice _o1_) {
			if (_o1_ instanceof WeiBoChoice) assign((WeiBoChoice)_o1_);
			else if (_o1_ instanceof WeiBoChoice.Data) assign((WeiBoChoice.Data)_o1_);
			else if (_o1_ instanceof WeiBoChoice.Const) assign(((WeiBoChoice.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(WeiBoChoice _o_) {
			times = _o_.times;
			setting = _o_.setting;
		}

		private void assign(WeiBoChoice.Data _o_) {
			times = _o_.times;
			setting = _o_.setting;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(times);
			_os_.marshal(setting);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			times = _os_.unmarshal_int();
			setting = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.WeiBoChoice copy() {
			return new Data(this);
		}

		@Override
		public xbean.WeiBoChoice toData() {
			return new Data(this);
		}

		public xbean.WeiBoChoice toBean() {
			return new WeiBoChoice(this, null, null);
		}

		@Override
		public xbean.WeiBoChoice toDataIf() {
			return this;
		}

		public xbean.WeiBoChoice toBeanIf() {
			return new WeiBoChoice(this, null, null);
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
		public int getTimes() { // 次数
			return times;
		}

		@Override
		public int getSetting() { // 设定
			return setting;
		}

		@Override
		public void setTimes(int _v_) { // 次数
			times = _v_;
		}

		@Override
		public void setSetting(int _v_) { // 设定
			setting = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof WeiBoChoice.Data)) return false;
			WeiBoChoice.Data _o_ = (WeiBoChoice.Data) _o1_;
			if (times != _o_.times) return false;
			if (setting != _o_.setting) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += times;
			_h_ += setting;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(times);
			_sb_.append(",");
			_sb_.append(setting);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
