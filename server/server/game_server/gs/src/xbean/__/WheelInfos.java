
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class WheelInfos extends mkdb.XBean implements xbean.WheelInfos {
	private java.util.ArrayList<xbean.WheelInfo> wheellist; // 

	@Override
	public void _reset_unsafe_() {
		wheellist.clear();
	}

	WheelInfos(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		wheellist = new java.util.ArrayList<xbean.WheelInfo>();
	}

	public WheelInfos() {
		this(0, null, null);
	}

	public WheelInfos(WheelInfos _o_) {
		this(_o_, null, null);
	}

	WheelInfos(xbean.WheelInfos _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof WheelInfos) assign((WheelInfos)_o1_);
		else if (_o1_ instanceof WheelInfos.Data) assign((WheelInfos.Data)_o1_);
		else if (_o1_ instanceof WheelInfos.Const) assign(((WheelInfos.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(WheelInfos _o_) {
		_o_._xdb_verify_unsafe_();
		wheellist = new java.util.ArrayList<xbean.WheelInfo>();
		for (xbean.WheelInfo _v_ : _o_.wheellist)
			wheellist.add(new WheelInfo(_v_, this, "wheellist"));
	}

	private void assign(WheelInfos.Data _o_) {
		wheellist = new java.util.ArrayList<xbean.WheelInfo>();
		for (xbean.WheelInfo _v_ : _o_.wheellist)
			wheellist.add(new WheelInfo(_v_, this, "wheellist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(wheellist.size());
		for (xbean.WheelInfo _v_ : wheellist) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.WheelInfo _v_ = new WheelInfo(0, this, "wheellist");
			_v_.unmarshal(_os_);
			wheellist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.WheelInfos copy() {
		_xdb_verify_unsafe_();
		return new WheelInfos(this);
	}

	@Override
	public xbean.WheelInfos toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WheelInfos toBean() {
		_xdb_verify_unsafe_();
		return new WheelInfos(this); // same as copy()
	}

	@Override
	public xbean.WheelInfos toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WheelInfos toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.WheelInfo> getWheellist() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "wheellist"), wheellist);
	}

	public java.util.List<xbean.WheelInfo> getWheellistAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.WheelInfo> wheellist;
		WheelInfos _o_ = this;
		wheellist = new java.util.ArrayList<xbean.WheelInfo>();
		for (xbean.WheelInfo _v_ : _o_.wheellist)
			wheellist.add(new WheelInfo.Data(_v_));
		return wheellist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		WheelInfos _o_ = null;
		if ( _o1_ instanceof WheelInfos ) _o_ = (WheelInfos)_o1_;
		else if ( _o1_ instanceof WheelInfos.Const ) _o_ = ((WheelInfos.Const)_o1_).nThis();
		else return false;
		if (!wheellist.equals(_o_.wheellist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += wheellist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(wheellist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("wheellist"));
		return lb;
	}

	private class Const implements xbean.WheelInfos {
		WheelInfos nThis() {
			return WheelInfos.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.WheelInfos copy() {
			return WheelInfos.this.copy();
		}

		@Override
		public xbean.WheelInfos toData() {
			return WheelInfos.this.toData();
		}

		public xbean.WheelInfos toBean() {
			return WheelInfos.this.toBean();
		}

		@Override
		public xbean.WheelInfos toDataIf() {
			return WheelInfos.this.toDataIf();
		}

		public xbean.WheelInfos toBeanIf() {
			return WheelInfos.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.WheelInfo> getWheellist() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(wheellist);
		}

		public java.util.List<xbean.WheelInfo> getWheellistAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.WheelInfo> wheellist;
			WheelInfos _o_ = WheelInfos.this;
		wheellist = new java.util.ArrayList<xbean.WheelInfo>();
		for (xbean.WheelInfo _v_ : _o_.wheellist)
			wheellist.add(new WheelInfo.Data(_v_));
			return wheellist;
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
			return WheelInfos.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return WheelInfos.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return WheelInfos.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return WheelInfos.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return WheelInfos.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return WheelInfos.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return WheelInfos.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return WheelInfos.this.hashCode();
		}

		@Override
		public String toString() {
			return WheelInfos.this.toString();
		}

	}

	public static final class Data implements xbean.WheelInfos {
		private java.util.ArrayList<xbean.WheelInfo> wheellist; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			wheellist = new java.util.ArrayList<xbean.WheelInfo>();
		}

		Data(xbean.WheelInfos _o1_) {
			if (_o1_ instanceof WheelInfos) assign((WheelInfos)_o1_);
			else if (_o1_ instanceof WheelInfos.Data) assign((WheelInfos.Data)_o1_);
			else if (_o1_ instanceof WheelInfos.Const) assign(((WheelInfos.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(WheelInfos _o_) {
			wheellist = new java.util.ArrayList<xbean.WheelInfo>();
			for (xbean.WheelInfo _v_ : _o_.wheellist)
				wheellist.add(new WheelInfo.Data(_v_));
		}

		private void assign(WheelInfos.Data _o_) {
			wheellist = new java.util.ArrayList<xbean.WheelInfo>();
			for (xbean.WheelInfo _v_ : _o_.wheellist)
				wheellist.add(new WheelInfo.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(wheellist.size());
			for (xbean.WheelInfo _v_ : wheellist) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.WheelInfo _v_ = xbean.Pod.newWheelInfoData();
				_v_.unmarshal(_os_);
				wheellist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.WheelInfos copy() {
			return new Data(this);
		}

		@Override
		public xbean.WheelInfos toData() {
			return new Data(this);
		}

		public xbean.WheelInfos toBean() {
			return new WheelInfos(this, null, null);
		}

		@Override
		public xbean.WheelInfos toDataIf() {
			return this;
		}

		public xbean.WheelInfos toBeanIf() {
			return new WheelInfos(this, null, null);
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
		public java.util.List<xbean.WheelInfo> getWheellist() { // 
			return wheellist;
		}

		@Override
		public java.util.List<xbean.WheelInfo> getWheellistAsData() { // 
			return wheellist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof WheelInfos.Data)) return false;
			WheelInfos.Data _o_ = (WheelInfos.Data) _o1_;
			if (!wheellist.equals(_o_.wheellist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += wheellist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(wheellist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
