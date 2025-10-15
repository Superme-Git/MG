
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class GongHuiBaoXiangInfo extends mkdb.XBean implements xbean.GongHuiBaoXiangInfo {
	private int opentimes; // 拾取次数
	private long lastopentime; // 上次拾取时间

	@Override
	public void _reset_unsafe_() {
		opentimes = 0;
		lastopentime = 0L;
	}

	GongHuiBaoXiangInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public GongHuiBaoXiangInfo() {
		this(0, null, null);
	}

	public GongHuiBaoXiangInfo(GongHuiBaoXiangInfo _o_) {
		this(_o_, null, null);
	}

	GongHuiBaoXiangInfo(xbean.GongHuiBaoXiangInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof GongHuiBaoXiangInfo) assign((GongHuiBaoXiangInfo)_o1_);
		else if (_o1_ instanceof GongHuiBaoXiangInfo.Data) assign((GongHuiBaoXiangInfo.Data)_o1_);
		else if (_o1_ instanceof GongHuiBaoXiangInfo.Const) assign(((GongHuiBaoXiangInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(GongHuiBaoXiangInfo _o_) {
		_o_._xdb_verify_unsafe_();
		opentimes = _o_.opentimes;
		lastopentime = _o_.lastopentime;
	}

	private void assign(GongHuiBaoXiangInfo.Data _o_) {
		opentimes = _o_.opentimes;
		lastopentime = _o_.lastopentime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(opentimes);
		_os_.marshal(lastopentime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		opentimes = _os_.unmarshal_int();
		lastopentime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.GongHuiBaoXiangInfo copy() {
		_xdb_verify_unsafe_();
		return new GongHuiBaoXiangInfo(this);
	}

	@Override
	public xbean.GongHuiBaoXiangInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GongHuiBaoXiangInfo toBean() {
		_xdb_verify_unsafe_();
		return new GongHuiBaoXiangInfo(this); // same as copy()
	}

	@Override
	public xbean.GongHuiBaoXiangInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GongHuiBaoXiangInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getOpentimes() { // 拾取次数
		_xdb_verify_unsafe_();
		return opentimes;
	}

	@Override
	public long getLastopentime() { // 上次拾取时间
		_xdb_verify_unsafe_();
		return lastopentime;
	}

	@Override
	public void setOpentimes(int _v_) { // 拾取次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "opentimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, opentimes) {
					public void rollback() { opentimes = _xdb_saved; }
				};}});
		opentimes = _v_;
	}

	@Override
	public void setLastopentime(long _v_) { // 上次拾取时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastopentime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastopentime) {
					public void rollback() { lastopentime = _xdb_saved; }
				};}});
		lastopentime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		GongHuiBaoXiangInfo _o_ = null;
		if ( _o1_ instanceof GongHuiBaoXiangInfo ) _o_ = (GongHuiBaoXiangInfo)_o1_;
		else if ( _o1_ instanceof GongHuiBaoXiangInfo.Const ) _o_ = ((GongHuiBaoXiangInfo.Const)_o1_).nThis();
		else return false;
		if (opentimes != _o_.opentimes) return false;
		if (lastopentime != _o_.lastopentime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += opentimes;
		_h_ += lastopentime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(opentimes);
		_sb_.append(",");
		_sb_.append(lastopentime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("opentimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastopentime"));
		return lb;
	}

	private class Const implements xbean.GongHuiBaoXiangInfo {
		GongHuiBaoXiangInfo nThis() {
			return GongHuiBaoXiangInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.GongHuiBaoXiangInfo copy() {
			return GongHuiBaoXiangInfo.this.copy();
		}

		@Override
		public xbean.GongHuiBaoXiangInfo toData() {
			return GongHuiBaoXiangInfo.this.toData();
		}

		public xbean.GongHuiBaoXiangInfo toBean() {
			return GongHuiBaoXiangInfo.this.toBean();
		}

		@Override
		public xbean.GongHuiBaoXiangInfo toDataIf() {
			return GongHuiBaoXiangInfo.this.toDataIf();
		}

		public xbean.GongHuiBaoXiangInfo toBeanIf() {
			return GongHuiBaoXiangInfo.this.toBeanIf();
		}

		@Override
		public int getOpentimes() { // 拾取次数
			_xdb_verify_unsafe_();
			return opentimes;
		}

		@Override
		public long getLastopentime() { // 上次拾取时间
			_xdb_verify_unsafe_();
			return lastopentime;
		}

		@Override
		public void setOpentimes(int _v_) { // 拾取次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastopentime(long _v_) { // 上次拾取时间
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
			return GongHuiBaoXiangInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return GongHuiBaoXiangInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return GongHuiBaoXiangInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return GongHuiBaoXiangInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return GongHuiBaoXiangInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return GongHuiBaoXiangInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return GongHuiBaoXiangInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return GongHuiBaoXiangInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return GongHuiBaoXiangInfo.this.toString();
		}

	}

	public static final class Data implements xbean.GongHuiBaoXiangInfo {
		private int opentimes; // 拾取次数
		private long lastopentime; // 上次拾取时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.GongHuiBaoXiangInfo _o1_) {
			if (_o1_ instanceof GongHuiBaoXiangInfo) assign((GongHuiBaoXiangInfo)_o1_);
			else if (_o1_ instanceof GongHuiBaoXiangInfo.Data) assign((GongHuiBaoXiangInfo.Data)_o1_);
			else if (_o1_ instanceof GongHuiBaoXiangInfo.Const) assign(((GongHuiBaoXiangInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(GongHuiBaoXiangInfo _o_) {
			opentimes = _o_.opentimes;
			lastopentime = _o_.lastopentime;
		}

		private void assign(GongHuiBaoXiangInfo.Data _o_) {
			opentimes = _o_.opentimes;
			lastopentime = _o_.lastopentime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(opentimes);
			_os_.marshal(lastopentime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			opentimes = _os_.unmarshal_int();
			lastopentime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.GongHuiBaoXiangInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.GongHuiBaoXiangInfo toData() {
			return new Data(this);
		}

		public xbean.GongHuiBaoXiangInfo toBean() {
			return new GongHuiBaoXiangInfo(this, null, null);
		}

		@Override
		public xbean.GongHuiBaoXiangInfo toDataIf() {
			return this;
		}

		public xbean.GongHuiBaoXiangInfo toBeanIf() {
			return new GongHuiBaoXiangInfo(this, null, null);
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
		public int getOpentimes() { // 拾取次数
			return opentimes;
		}

		@Override
		public long getLastopentime() { // 上次拾取时间
			return lastopentime;
		}

		@Override
		public void setOpentimes(int _v_) { // 拾取次数
			opentimes = _v_;
		}

		@Override
		public void setLastopentime(long _v_) { // 上次拾取时间
			lastopentime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof GongHuiBaoXiangInfo.Data)) return false;
			GongHuiBaoXiangInfo.Data _o_ = (GongHuiBaoXiangInfo.Data) _o1_;
			if (opentimes != _o_.opentimes) return false;
			if (lastopentime != _o_.lastopentime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += opentimes;
			_h_ += lastopentime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(opentimes);
			_sb_.append(",");
			_sb_.append(lastopentime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
