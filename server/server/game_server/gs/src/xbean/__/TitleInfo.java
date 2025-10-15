
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TitleInfo extends mkdb.XBean implements xbean.TitleInfo {
	private int titleid; // 称谓id
	private String titlename; // 称谓名
	private long availtime; // 剩余有效时间

	@Override
	public void _reset_unsafe_() {
		titleid = 0;
		titlename = "";
		availtime = 0L;
	}

	TitleInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		titlename = "";
	}

	public TitleInfo() {
		this(0, null, null);
	}

	public TitleInfo(TitleInfo _o_) {
		this(_o_, null, null);
	}

	TitleInfo(xbean.TitleInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TitleInfo) assign((TitleInfo)_o1_);
		else if (_o1_ instanceof TitleInfo.Data) assign((TitleInfo.Data)_o1_);
		else if (_o1_ instanceof TitleInfo.Const) assign(((TitleInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TitleInfo _o_) {
		_o_._xdb_verify_unsafe_();
		titleid = _o_.titleid;
		titlename = _o_.titlename;
		availtime = _o_.availtime;
	}

	private void assign(TitleInfo.Data _o_) {
		titleid = _o_.titleid;
		titlename = _o_.titlename;
		availtime = _o_.availtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(titleid);
		_os_.marshal(titlename, mkdb.Const.IO_CHARSET);
		_os_.marshal(availtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		titleid = _os_.unmarshal_int();
		titlename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		availtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.TitleInfo copy() {
		_xdb_verify_unsafe_();
		return new TitleInfo(this);
	}

	@Override
	public xbean.TitleInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TitleInfo toBean() {
		_xdb_verify_unsafe_();
		return new TitleInfo(this); // same as copy()
	}

	@Override
	public xbean.TitleInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TitleInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getTitleid() { // 称谓id
		_xdb_verify_unsafe_();
		return titleid;
	}

	@Override
	public String getTitlename() { // 称谓名
		_xdb_verify_unsafe_();
		return titlename;
	}

	@Override
	public com.locojoy.base.Octets getTitlenameOctets() { // 称谓名
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getTitlename(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getAvailtime() { // 剩余有效时间
		_xdb_verify_unsafe_();
		return availtime;
	}

	@Override
	public void setTitleid(int _v_) { // 称谓id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "titleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, titleid) {
					public void rollback() { titleid = _xdb_saved; }
				};}});
		titleid = _v_;
	}

	@Override
	public void setTitlename(String _v_) { // 称谓名
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "titlename") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, titlename) {
					public void rollback() { titlename = _xdb_saved; }
				};}});
		titlename = _v_;
	}

	@Override
	public void setTitlenameOctets(com.locojoy.base.Octets _v_) { // 称谓名
		_xdb_verify_unsafe_();
		this.setTitlename(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setAvailtime(long _v_) { // 剩余有效时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "availtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, availtime) {
					public void rollback() { availtime = _xdb_saved; }
				};}});
		availtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TitleInfo _o_ = null;
		if ( _o1_ instanceof TitleInfo ) _o_ = (TitleInfo)_o1_;
		else if ( _o1_ instanceof TitleInfo.Const ) _o_ = ((TitleInfo.Const)_o1_).nThis();
		else return false;
		if (titleid != _o_.titleid) return false;
		if (!titlename.equals(_o_.titlename)) return false;
		if (availtime != _o_.availtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += titleid;
		_h_ += titlename.hashCode();
		_h_ += availtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(titleid);
		_sb_.append(",");
		_sb_.append("'").append(titlename).append("'");
		_sb_.append(",");
		_sb_.append(availtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("titleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("titlename"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("availtime"));
		return lb;
	}

	private class Const implements xbean.TitleInfo {
		TitleInfo nThis() {
			return TitleInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TitleInfo copy() {
			return TitleInfo.this.copy();
		}

		@Override
		public xbean.TitleInfo toData() {
			return TitleInfo.this.toData();
		}

		public xbean.TitleInfo toBean() {
			return TitleInfo.this.toBean();
		}

		@Override
		public xbean.TitleInfo toDataIf() {
			return TitleInfo.this.toDataIf();
		}

		public xbean.TitleInfo toBeanIf() {
			return TitleInfo.this.toBeanIf();
		}

		@Override
		public int getTitleid() { // 称谓id
			_xdb_verify_unsafe_();
			return titleid;
		}

		@Override
		public String getTitlename() { // 称谓名
			_xdb_verify_unsafe_();
			return titlename;
		}

		@Override
		public com.locojoy.base.Octets getTitlenameOctets() { // 称谓名
			_xdb_verify_unsafe_();
			return TitleInfo.this.getTitlenameOctets();
		}

		@Override
		public long getAvailtime() { // 剩余有效时间
			_xdb_verify_unsafe_();
			return availtime;
		}

		@Override
		public void setTitleid(int _v_) { // 称谓id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTitlename(String _v_) { // 称谓名
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTitlenameOctets(com.locojoy.base.Octets _v_) { // 称谓名
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAvailtime(long _v_) { // 剩余有效时间
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
			return TitleInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TitleInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TitleInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TitleInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TitleInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TitleInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TitleInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TitleInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return TitleInfo.this.toString();
		}

	}

	public static final class Data implements xbean.TitleInfo {
		private int titleid; // 称谓id
		private String titlename; // 称谓名
		private long availtime; // 剩余有效时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			titlename = "";
		}

		Data(xbean.TitleInfo _o1_) {
			if (_o1_ instanceof TitleInfo) assign((TitleInfo)_o1_);
			else if (_o1_ instanceof TitleInfo.Data) assign((TitleInfo.Data)_o1_);
			else if (_o1_ instanceof TitleInfo.Const) assign(((TitleInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TitleInfo _o_) {
			titleid = _o_.titleid;
			titlename = _o_.titlename;
			availtime = _o_.availtime;
		}

		private void assign(TitleInfo.Data _o_) {
			titleid = _o_.titleid;
			titlename = _o_.titlename;
			availtime = _o_.availtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(titleid);
			_os_.marshal(titlename, mkdb.Const.IO_CHARSET);
			_os_.marshal(availtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			titleid = _os_.unmarshal_int();
			titlename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			availtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.TitleInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.TitleInfo toData() {
			return new Data(this);
		}

		public xbean.TitleInfo toBean() {
			return new TitleInfo(this, null, null);
		}

		@Override
		public xbean.TitleInfo toDataIf() {
			return this;
		}

		public xbean.TitleInfo toBeanIf() {
			return new TitleInfo(this, null, null);
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
		public int getTitleid() { // 称谓id
			return titleid;
		}

		@Override
		public String getTitlename() { // 称谓名
			return titlename;
		}

		@Override
		public com.locojoy.base.Octets getTitlenameOctets() { // 称谓名
			return com.locojoy.base.Octets.wrap(getTitlename(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getAvailtime() { // 剩余有效时间
			return availtime;
		}

		@Override
		public void setTitleid(int _v_) { // 称谓id
			titleid = _v_;
		}

		@Override
		public void setTitlename(String _v_) { // 称谓名
			if (null == _v_)
				throw new NullPointerException();
			titlename = _v_;
		}

		@Override
		public void setTitlenameOctets(com.locojoy.base.Octets _v_) { // 称谓名
			this.setTitlename(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setAvailtime(long _v_) { // 剩余有效时间
			availtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TitleInfo.Data)) return false;
			TitleInfo.Data _o_ = (TitleInfo.Data) _o1_;
			if (titleid != _o_.titleid) return false;
			if (!titlename.equals(_o_.titlename)) return false;
			if (availtime != _o_.availtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += titleid;
			_h_ += titlename.hashCode();
			_h_ += availtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(titleid);
			_sb_.append(",");
			_sb_.append("'").append(titlename).append("'");
			_sb_.append(",");
			_sb_.append(availtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
