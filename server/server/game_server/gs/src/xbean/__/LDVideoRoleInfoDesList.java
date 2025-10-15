
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class LDVideoRoleInfoDesList extends mkdb.XBean implements xbean.LDVideoRoleInfoDesList {
	private java.util.ArrayList<xbean.LDVideoRoleInfoDes> ldvideoroleinfodeslistall; // 生死战录像信息

	@Override
	public void _reset_unsafe_() {
		ldvideoroleinfodeslistall.clear();
	}

	LDVideoRoleInfoDesList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		ldvideoroleinfodeslistall = new java.util.ArrayList<xbean.LDVideoRoleInfoDes>();
	}

	public LDVideoRoleInfoDesList() {
		this(0, null, null);
	}

	public LDVideoRoleInfoDesList(LDVideoRoleInfoDesList _o_) {
		this(_o_, null, null);
	}

	LDVideoRoleInfoDesList(xbean.LDVideoRoleInfoDesList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LDVideoRoleInfoDesList) assign((LDVideoRoleInfoDesList)_o1_);
		else if (_o1_ instanceof LDVideoRoleInfoDesList.Data) assign((LDVideoRoleInfoDesList.Data)_o1_);
		else if (_o1_ instanceof LDVideoRoleInfoDesList.Const) assign(((LDVideoRoleInfoDesList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LDVideoRoleInfoDesList _o_) {
		_o_._xdb_verify_unsafe_();
		ldvideoroleinfodeslistall = new java.util.ArrayList<xbean.LDVideoRoleInfoDes>();
		for (xbean.LDVideoRoleInfoDes _v_ : _o_.ldvideoroleinfodeslistall)
			ldvideoroleinfodeslistall.add(new LDVideoRoleInfoDes(_v_, this, "ldvideoroleinfodeslistall"));
	}

	private void assign(LDVideoRoleInfoDesList.Data _o_) {
		ldvideoroleinfodeslistall = new java.util.ArrayList<xbean.LDVideoRoleInfoDes>();
		for (xbean.LDVideoRoleInfoDes _v_ : _o_.ldvideoroleinfodeslistall)
			ldvideoroleinfodeslistall.add(new LDVideoRoleInfoDes(_v_, this, "ldvideoroleinfodeslistall"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(ldvideoroleinfodeslistall.size());
		for (xbean.LDVideoRoleInfoDes _v_ : ldvideoroleinfodeslistall) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.LDVideoRoleInfoDes _v_ = new LDVideoRoleInfoDes(0, this, "ldvideoroleinfodeslistall");
			_v_.unmarshal(_os_);
			ldvideoroleinfodeslistall.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.LDVideoRoleInfoDesList copy() {
		_xdb_verify_unsafe_();
		return new LDVideoRoleInfoDesList(this);
	}

	@Override
	public xbean.LDVideoRoleInfoDesList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LDVideoRoleInfoDesList toBean() {
		_xdb_verify_unsafe_();
		return new LDVideoRoleInfoDesList(this); // same as copy()
	}

	@Override
	public xbean.LDVideoRoleInfoDesList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LDVideoRoleInfoDesList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.LDVideoRoleInfoDes> getLdvideoroleinfodeslistall() { // 生死战录像信息
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "ldvideoroleinfodeslistall"), ldvideoroleinfodeslistall);
	}

	public java.util.List<xbean.LDVideoRoleInfoDes> getLdvideoroleinfodeslistallAsData() { // 生死战录像信息
		_xdb_verify_unsafe_();
		java.util.List<xbean.LDVideoRoleInfoDes> ldvideoroleinfodeslistall;
		LDVideoRoleInfoDesList _o_ = this;
		ldvideoroleinfodeslistall = new java.util.ArrayList<xbean.LDVideoRoleInfoDes>();
		for (xbean.LDVideoRoleInfoDes _v_ : _o_.ldvideoroleinfodeslistall)
			ldvideoroleinfodeslistall.add(new LDVideoRoleInfoDes.Data(_v_));
		return ldvideoroleinfodeslistall;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LDVideoRoleInfoDesList _o_ = null;
		if ( _o1_ instanceof LDVideoRoleInfoDesList ) _o_ = (LDVideoRoleInfoDesList)_o1_;
		else if ( _o1_ instanceof LDVideoRoleInfoDesList.Const ) _o_ = ((LDVideoRoleInfoDesList.Const)_o1_).nThis();
		else return false;
		if (!ldvideoroleinfodeslistall.equals(_o_.ldvideoroleinfodeslistall)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += ldvideoroleinfodeslistall.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ldvideoroleinfodeslistall);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ldvideoroleinfodeslistall"));
		return lb;
	}

	private class Const implements xbean.LDVideoRoleInfoDesList {
		LDVideoRoleInfoDesList nThis() {
			return LDVideoRoleInfoDesList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LDVideoRoleInfoDesList copy() {
			return LDVideoRoleInfoDesList.this.copy();
		}

		@Override
		public xbean.LDVideoRoleInfoDesList toData() {
			return LDVideoRoleInfoDesList.this.toData();
		}

		public xbean.LDVideoRoleInfoDesList toBean() {
			return LDVideoRoleInfoDesList.this.toBean();
		}

		@Override
		public xbean.LDVideoRoleInfoDesList toDataIf() {
			return LDVideoRoleInfoDesList.this.toDataIf();
		}

		public xbean.LDVideoRoleInfoDesList toBeanIf() {
			return LDVideoRoleInfoDesList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.LDVideoRoleInfoDes> getLdvideoroleinfodeslistall() { // 生死战录像信息
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(ldvideoroleinfodeslistall);
		}

		public java.util.List<xbean.LDVideoRoleInfoDes> getLdvideoroleinfodeslistallAsData() { // 生死战录像信息
			_xdb_verify_unsafe_();
			java.util.List<xbean.LDVideoRoleInfoDes> ldvideoroleinfodeslistall;
			LDVideoRoleInfoDesList _o_ = LDVideoRoleInfoDesList.this;
		ldvideoroleinfodeslistall = new java.util.ArrayList<xbean.LDVideoRoleInfoDes>();
		for (xbean.LDVideoRoleInfoDes _v_ : _o_.ldvideoroleinfodeslistall)
			ldvideoroleinfodeslistall.add(new LDVideoRoleInfoDes.Data(_v_));
			return ldvideoroleinfodeslistall;
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
			return LDVideoRoleInfoDesList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LDVideoRoleInfoDesList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return LDVideoRoleInfoDesList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LDVideoRoleInfoDesList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LDVideoRoleInfoDesList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LDVideoRoleInfoDesList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LDVideoRoleInfoDesList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LDVideoRoleInfoDesList.this.hashCode();
		}

		@Override
		public String toString() {
			return LDVideoRoleInfoDesList.this.toString();
		}

	}

	public static final class Data implements xbean.LDVideoRoleInfoDesList {
		private java.util.ArrayList<xbean.LDVideoRoleInfoDes> ldvideoroleinfodeslistall; // 生死战录像信息

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			ldvideoroleinfodeslistall = new java.util.ArrayList<xbean.LDVideoRoleInfoDes>();
		}

		Data(xbean.LDVideoRoleInfoDesList _o1_) {
			if (_o1_ instanceof LDVideoRoleInfoDesList) assign((LDVideoRoleInfoDesList)_o1_);
			else if (_o1_ instanceof LDVideoRoleInfoDesList.Data) assign((LDVideoRoleInfoDesList.Data)_o1_);
			else if (_o1_ instanceof LDVideoRoleInfoDesList.Const) assign(((LDVideoRoleInfoDesList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LDVideoRoleInfoDesList _o_) {
			ldvideoroleinfodeslistall = new java.util.ArrayList<xbean.LDVideoRoleInfoDes>();
			for (xbean.LDVideoRoleInfoDes _v_ : _o_.ldvideoroleinfodeslistall)
				ldvideoroleinfodeslistall.add(new LDVideoRoleInfoDes.Data(_v_));
		}

		private void assign(LDVideoRoleInfoDesList.Data _o_) {
			ldvideoroleinfodeslistall = new java.util.ArrayList<xbean.LDVideoRoleInfoDes>();
			for (xbean.LDVideoRoleInfoDes _v_ : _o_.ldvideoroleinfodeslistall)
				ldvideoroleinfodeslistall.add(new LDVideoRoleInfoDes.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(ldvideoroleinfodeslistall.size());
			for (xbean.LDVideoRoleInfoDes _v_ : ldvideoroleinfodeslistall) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.LDVideoRoleInfoDes _v_ = xbean.Pod.newLDVideoRoleInfoDesData();
				_v_.unmarshal(_os_);
				ldvideoroleinfodeslistall.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.LDVideoRoleInfoDesList copy() {
			return new Data(this);
		}

		@Override
		public xbean.LDVideoRoleInfoDesList toData() {
			return new Data(this);
		}

		public xbean.LDVideoRoleInfoDesList toBean() {
			return new LDVideoRoleInfoDesList(this, null, null);
		}

		@Override
		public xbean.LDVideoRoleInfoDesList toDataIf() {
			return this;
		}

		public xbean.LDVideoRoleInfoDesList toBeanIf() {
			return new LDVideoRoleInfoDesList(this, null, null);
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
		public java.util.List<xbean.LDVideoRoleInfoDes> getLdvideoroleinfodeslistall() { // 生死战录像信息
			return ldvideoroleinfodeslistall;
		}

		@Override
		public java.util.List<xbean.LDVideoRoleInfoDes> getLdvideoroleinfodeslistallAsData() { // 生死战录像信息
			return ldvideoroleinfodeslistall;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LDVideoRoleInfoDesList.Data)) return false;
			LDVideoRoleInfoDesList.Data _o_ = (LDVideoRoleInfoDesList.Data) _o1_;
			if (!ldvideoroleinfodeslistall.equals(_o_.ldvideoroleinfodeslistall)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += ldvideoroleinfodeslistall.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(ldvideoroleinfodeslistall);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
