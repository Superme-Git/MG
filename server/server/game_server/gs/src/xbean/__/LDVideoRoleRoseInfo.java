
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class LDVideoRoleRoseInfo extends mkdb.XBean implements xbean.LDVideoRoleRoseInfo {
	private String videoid; // 录像id
	private int rosenum; // 次数

	@Override
	public void _reset_unsafe_() {
		videoid = "";
		rosenum = 0;
	}

	LDVideoRoleRoseInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		videoid = "";
	}

	public LDVideoRoleRoseInfo() {
		this(0, null, null);
	}

	public LDVideoRoleRoseInfo(LDVideoRoleRoseInfo _o_) {
		this(_o_, null, null);
	}

	LDVideoRoleRoseInfo(xbean.LDVideoRoleRoseInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LDVideoRoleRoseInfo) assign((LDVideoRoleRoseInfo)_o1_);
		else if (_o1_ instanceof LDVideoRoleRoseInfo.Data) assign((LDVideoRoleRoseInfo.Data)_o1_);
		else if (_o1_ instanceof LDVideoRoleRoseInfo.Const) assign(((LDVideoRoleRoseInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LDVideoRoleRoseInfo _o_) {
		_o_._xdb_verify_unsafe_();
		videoid = _o_.videoid;
		rosenum = _o_.rosenum;
	}

	private void assign(LDVideoRoleRoseInfo.Data _o_) {
		videoid = _o_.videoid;
		rosenum = _o_.rosenum;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(videoid, mkdb.Const.IO_CHARSET);
		_os_.marshal(rosenum);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		videoid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		rosenum = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.LDVideoRoleRoseInfo copy() {
		_xdb_verify_unsafe_();
		return new LDVideoRoleRoseInfo(this);
	}

	@Override
	public xbean.LDVideoRoleRoseInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LDVideoRoleRoseInfo toBean() {
		_xdb_verify_unsafe_();
		return new LDVideoRoleRoseInfo(this); // same as copy()
	}

	@Override
	public xbean.LDVideoRoleRoseInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LDVideoRoleRoseInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public String getVideoid() { // 录像id
		_xdb_verify_unsafe_();
		return videoid;
	}

	@Override
	public com.locojoy.base.Octets getVideoidOctets() { // 录像id
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getVideoid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getRosenum() { // 次数
		_xdb_verify_unsafe_();
		return rosenum;
	}

	@Override
	public void setVideoid(String _v_) { // 录像id
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "videoid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, videoid) {
					public void rollback() { videoid = _xdb_saved; }
				};}});
		videoid = _v_;
	}

	@Override
	public void setVideoidOctets(com.locojoy.base.Octets _v_) { // 录像id
		_xdb_verify_unsafe_();
		this.setVideoid(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setRosenum(int _v_) { // 次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rosenum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rosenum) {
					public void rollback() { rosenum = _xdb_saved; }
				};}});
		rosenum = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LDVideoRoleRoseInfo _o_ = null;
		if ( _o1_ instanceof LDVideoRoleRoseInfo ) _o_ = (LDVideoRoleRoseInfo)_o1_;
		else if ( _o1_ instanceof LDVideoRoleRoseInfo.Const ) _o_ = ((LDVideoRoleRoseInfo.Const)_o1_).nThis();
		else return false;
		if (!videoid.equals(_o_.videoid)) return false;
		if (rosenum != _o_.rosenum) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += videoid.hashCode();
		_h_ += rosenum;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("'").append(videoid).append("'");
		_sb_.append(",");
		_sb_.append(rosenum);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("videoid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rosenum"));
		return lb;
	}

	private class Const implements xbean.LDVideoRoleRoseInfo {
		LDVideoRoleRoseInfo nThis() {
			return LDVideoRoleRoseInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LDVideoRoleRoseInfo copy() {
			return LDVideoRoleRoseInfo.this.copy();
		}

		@Override
		public xbean.LDVideoRoleRoseInfo toData() {
			return LDVideoRoleRoseInfo.this.toData();
		}

		public xbean.LDVideoRoleRoseInfo toBean() {
			return LDVideoRoleRoseInfo.this.toBean();
		}

		@Override
		public xbean.LDVideoRoleRoseInfo toDataIf() {
			return LDVideoRoleRoseInfo.this.toDataIf();
		}

		public xbean.LDVideoRoleRoseInfo toBeanIf() {
			return LDVideoRoleRoseInfo.this.toBeanIf();
		}

		@Override
		public String getVideoid() { // 录像id
			_xdb_verify_unsafe_();
			return videoid;
		}

		@Override
		public com.locojoy.base.Octets getVideoidOctets() { // 录像id
			_xdb_verify_unsafe_();
			return LDVideoRoleRoseInfo.this.getVideoidOctets();
		}

		@Override
		public int getRosenum() { // 次数
			_xdb_verify_unsafe_();
			return rosenum;
		}

		@Override
		public void setVideoid(String _v_) { // 录像id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setVideoidOctets(com.locojoy.base.Octets _v_) { // 录像id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRosenum(int _v_) { // 次数
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
			return LDVideoRoleRoseInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LDVideoRoleRoseInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return LDVideoRoleRoseInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LDVideoRoleRoseInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LDVideoRoleRoseInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LDVideoRoleRoseInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LDVideoRoleRoseInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LDVideoRoleRoseInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return LDVideoRoleRoseInfo.this.toString();
		}

	}

	public static final class Data implements xbean.LDVideoRoleRoseInfo {
		private String videoid; // 录像id
		private int rosenum; // 次数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			videoid = "";
		}

		Data(xbean.LDVideoRoleRoseInfo _o1_) {
			if (_o1_ instanceof LDVideoRoleRoseInfo) assign((LDVideoRoleRoseInfo)_o1_);
			else if (_o1_ instanceof LDVideoRoleRoseInfo.Data) assign((LDVideoRoleRoseInfo.Data)_o1_);
			else if (_o1_ instanceof LDVideoRoleRoseInfo.Const) assign(((LDVideoRoleRoseInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LDVideoRoleRoseInfo _o_) {
			videoid = _o_.videoid;
			rosenum = _o_.rosenum;
		}

		private void assign(LDVideoRoleRoseInfo.Data _o_) {
			videoid = _o_.videoid;
			rosenum = _o_.rosenum;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(videoid, mkdb.Const.IO_CHARSET);
			_os_.marshal(rosenum);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			videoid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			rosenum = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.LDVideoRoleRoseInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.LDVideoRoleRoseInfo toData() {
			return new Data(this);
		}

		public xbean.LDVideoRoleRoseInfo toBean() {
			return new LDVideoRoleRoseInfo(this, null, null);
		}

		@Override
		public xbean.LDVideoRoleRoseInfo toDataIf() {
			return this;
		}

		public xbean.LDVideoRoleRoseInfo toBeanIf() {
			return new LDVideoRoleRoseInfo(this, null, null);
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
		public String getVideoid() { // 录像id
			return videoid;
		}

		@Override
		public com.locojoy.base.Octets getVideoidOctets() { // 录像id
			return com.locojoy.base.Octets.wrap(getVideoid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getRosenum() { // 次数
			return rosenum;
		}

		@Override
		public void setVideoid(String _v_) { // 录像id
			if (null == _v_)
				throw new NullPointerException();
			videoid = _v_;
		}

		@Override
		public void setVideoidOctets(com.locojoy.base.Octets _v_) { // 录像id
			this.setVideoid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setRosenum(int _v_) { // 次数
			rosenum = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LDVideoRoleRoseInfo.Data)) return false;
			LDVideoRoleRoseInfo.Data _o_ = (LDVideoRoleRoseInfo.Data) _o1_;
			if (!videoid.equals(_o_.videoid)) return false;
			if (rosenum != _o_.rosenum) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += videoid.hashCode();
			_h_ += rosenum;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append("'").append(videoid).append("'");
			_sb_.append(",");
			_sb_.append(rosenum);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
