
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class WelfareBean extends mkdb.XBean implements xbean.WelfareBean {
	private int buytimes; // 购买次数
	private long lastbuytime; // 

	@Override
	public void _reset_unsafe_() {
		buytimes = 0;
		lastbuytime = 0L;
	}

	WelfareBean(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public WelfareBean() {
		this(0, null, null);
	}

	public WelfareBean(WelfareBean _o_) {
		this(_o_, null, null);
	}

	WelfareBean(xbean.WelfareBean _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof WelfareBean) assign((WelfareBean)_o1_);
		else if (_o1_ instanceof WelfareBean.Data) assign((WelfareBean.Data)_o1_);
		else if (_o1_ instanceof WelfareBean.Const) assign(((WelfareBean.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(WelfareBean _o_) {
		_o_._xdb_verify_unsafe_();
		buytimes = _o_.buytimes;
		lastbuytime = _o_.lastbuytime;
	}

	private void assign(WelfareBean.Data _o_) {
		buytimes = _o_.buytimes;
		lastbuytime = _o_.lastbuytime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(buytimes);
		_os_.marshal(lastbuytime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		buytimes = _os_.unmarshal_int();
		lastbuytime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.WelfareBean copy() {
		_xdb_verify_unsafe_();
		return new WelfareBean(this);
	}

	@Override
	public xbean.WelfareBean toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WelfareBean toBean() {
		_xdb_verify_unsafe_();
		return new WelfareBean(this); // same as copy()
	}

	@Override
	public xbean.WelfareBean toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WelfareBean toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getBuytimes() { // 购买次数
		_xdb_verify_unsafe_();
		return buytimes;
	}

	@Override
	public long getLastbuytime() { // 
		_xdb_verify_unsafe_();
		return lastbuytime;
	}

	@Override
	public void setBuytimes(int _v_) { // 购买次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "buytimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, buytimes) {
					public void rollback() { buytimes = _xdb_saved; }
				};}});
		buytimes = _v_;
	}

	@Override
	public void setLastbuytime(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastbuytime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastbuytime) {
					public void rollback() { lastbuytime = _xdb_saved; }
				};}});
		lastbuytime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		WelfareBean _o_ = null;
		if ( _o1_ instanceof WelfareBean ) _o_ = (WelfareBean)_o1_;
		else if ( _o1_ instanceof WelfareBean.Const ) _o_ = ((WelfareBean.Const)_o1_).nThis();
		else return false;
		if (buytimes != _o_.buytimes) return false;
		if (lastbuytime != _o_.lastbuytime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += buytimes;
		_h_ += lastbuytime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(buytimes);
		_sb_.append(",");
		_sb_.append(lastbuytime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("buytimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastbuytime"));
		return lb;
	}

	private class Const implements xbean.WelfareBean {
		WelfareBean nThis() {
			return WelfareBean.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.WelfareBean copy() {
			return WelfareBean.this.copy();
		}

		@Override
		public xbean.WelfareBean toData() {
			return WelfareBean.this.toData();
		}

		public xbean.WelfareBean toBean() {
			return WelfareBean.this.toBean();
		}

		@Override
		public xbean.WelfareBean toDataIf() {
			return WelfareBean.this.toDataIf();
		}

		public xbean.WelfareBean toBeanIf() {
			return WelfareBean.this.toBeanIf();
		}

		@Override
		public int getBuytimes() { // 购买次数
			_xdb_verify_unsafe_();
			return buytimes;
		}

		@Override
		public long getLastbuytime() { // 
			_xdb_verify_unsafe_();
			return lastbuytime;
		}

		@Override
		public void setBuytimes(int _v_) { // 购买次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastbuytime(long _v_) { // 
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
			return WelfareBean.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return WelfareBean.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return WelfareBean.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return WelfareBean.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return WelfareBean.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return WelfareBean.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return WelfareBean.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return WelfareBean.this.hashCode();
		}

		@Override
		public String toString() {
			return WelfareBean.this.toString();
		}

	}

	public static final class Data implements xbean.WelfareBean {
		private int buytimes; // 购买次数
		private long lastbuytime; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.WelfareBean _o1_) {
			if (_o1_ instanceof WelfareBean) assign((WelfareBean)_o1_);
			else if (_o1_ instanceof WelfareBean.Data) assign((WelfareBean.Data)_o1_);
			else if (_o1_ instanceof WelfareBean.Const) assign(((WelfareBean.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(WelfareBean _o_) {
			buytimes = _o_.buytimes;
			lastbuytime = _o_.lastbuytime;
		}

		private void assign(WelfareBean.Data _o_) {
			buytimes = _o_.buytimes;
			lastbuytime = _o_.lastbuytime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(buytimes);
			_os_.marshal(lastbuytime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			buytimes = _os_.unmarshal_int();
			lastbuytime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.WelfareBean copy() {
			return new Data(this);
		}

		@Override
		public xbean.WelfareBean toData() {
			return new Data(this);
		}

		public xbean.WelfareBean toBean() {
			return new WelfareBean(this, null, null);
		}

		@Override
		public xbean.WelfareBean toDataIf() {
			return this;
		}

		public xbean.WelfareBean toBeanIf() {
			return new WelfareBean(this, null, null);
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
		public int getBuytimes() { // 购买次数
			return buytimes;
		}

		@Override
		public long getLastbuytime() { // 
			return lastbuytime;
		}

		@Override
		public void setBuytimes(int _v_) { // 购买次数
			buytimes = _v_;
		}

		@Override
		public void setLastbuytime(long _v_) { // 
			lastbuytime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof WelfareBean.Data)) return false;
			WelfareBean.Data _o_ = (WelfareBean.Data) _o1_;
			if (buytimes != _o_.buytimes) return false;
			if (lastbuytime != _o_.lastbuytime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += buytimes;
			_h_ += lastbuytime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(buytimes);
			_sb_.append(",");
			_sb_.append(lastbuytime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
