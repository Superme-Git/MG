
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class LDVideoHisList extends mkdb.XBean implements xbean.LDVideoHisList {
	private java.util.ArrayList<String> ldvidehisinfo; // 生死战历史排行记录

	@Override
	public void _reset_unsafe_() {
		ldvidehisinfo.clear();
	}

	LDVideoHisList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		ldvidehisinfo = new java.util.ArrayList<String>();
	}

	public LDVideoHisList() {
		this(0, null, null);
	}

	public LDVideoHisList(LDVideoHisList _o_) {
		this(_o_, null, null);
	}

	LDVideoHisList(xbean.LDVideoHisList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LDVideoHisList) assign((LDVideoHisList)_o1_);
		else if (_o1_ instanceof LDVideoHisList.Data) assign((LDVideoHisList.Data)_o1_);
		else if (_o1_ instanceof LDVideoHisList.Const) assign(((LDVideoHisList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LDVideoHisList _o_) {
		_o_._xdb_verify_unsafe_();
		ldvidehisinfo = new java.util.ArrayList<String>();
		ldvidehisinfo.addAll(_o_.ldvidehisinfo);
	}

	private void assign(LDVideoHisList.Data _o_) {
		ldvidehisinfo = new java.util.ArrayList<String>();
		ldvidehisinfo.addAll(_o_.ldvidehisinfo);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(ldvidehisinfo.size());
		for (String _v_ : ldvidehisinfo) {
			_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			String _v_ = "";
			_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			ldvidehisinfo.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.LDVideoHisList copy() {
		_xdb_verify_unsafe_();
		return new LDVideoHisList(this);
	}

	@Override
	public xbean.LDVideoHisList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LDVideoHisList toBean() {
		_xdb_verify_unsafe_();
		return new LDVideoHisList(this); // same as copy()
	}

	@Override
	public xbean.LDVideoHisList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LDVideoHisList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<String> getLdvidehisinfo() { // 生死战历史排行记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "ldvidehisinfo"), ldvidehisinfo);
	}

	public java.util.List<String> getLdvidehisinfoAsData() { // 生死战历史排行记录
		_xdb_verify_unsafe_();
		java.util.List<String> ldvidehisinfo;
		LDVideoHisList _o_ = this;
		ldvidehisinfo = new java.util.ArrayList<String>();
		ldvidehisinfo.addAll(_o_.ldvidehisinfo);
		return ldvidehisinfo;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LDVideoHisList _o_ = null;
		if ( _o1_ instanceof LDVideoHisList ) _o_ = (LDVideoHisList)_o1_;
		else if ( _o1_ instanceof LDVideoHisList.Const ) _o_ = ((LDVideoHisList.Const)_o1_).nThis();
		else return false;
		if (!ldvidehisinfo.equals(_o_.ldvidehisinfo)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += ldvidehisinfo.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ldvidehisinfo);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ldvidehisinfo"));
		return lb;
	}

	private class Const implements xbean.LDVideoHisList {
		LDVideoHisList nThis() {
			return LDVideoHisList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LDVideoHisList copy() {
			return LDVideoHisList.this.copy();
		}

		@Override
		public xbean.LDVideoHisList toData() {
			return LDVideoHisList.this.toData();
		}

		public xbean.LDVideoHisList toBean() {
			return LDVideoHisList.this.toBean();
		}

		@Override
		public xbean.LDVideoHisList toDataIf() {
			return LDVideoHisList.this.toDataIf();
		}

		public xbean.LDVideoHisList toBeanIf() {
			return LDVideoHisList.this.toBeanIf();
		}

		@Override
		public java.util.List<String> getLdvidehisinfo() { // 生死战历史排行记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(ldvidehisinfo);
		}

		public java.util.List<String> getLdvidehisinfoAsData() { // 生死战历史排行记录
			_xdb_verify_unsafe_();
			java.util.List<String> ldvidehisinfo;
			LDVideoHisList _o_ = LDVideoHisList.this;
		ldvidehisinfo = new java.util.ArrayList<String>();
		ldvidehisinfo.addAll(_o_.ldvidehisinfo);
			return ldvidehisinfo;
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
			return LDVideoHisList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LDVideoHisList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return LDVideoHisList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LDVideoHisList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LDVideoHisList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LDVideoHisList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LDVideoHisList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LDVideoHisList.this.hashCode();
		}

		@Override
		public String toString() {
			return LDVideoHisList.this.toString();
		}

	}

	public static final class Data implements xbean.LDVideoHisList {
		private java.util.ArrayList<String> ldvidehisinfo; // 生死战历史排行记录

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			ldvidehisinfo = new java.util.ArrayList<String>();
		}

		Data(xbean.LDVideoHisList _o1_) {
			if (_o1_ instanceof LDVideoHisList) assign((LDVideoHisList)_o1_);
			else if (_o1_ instanceof LDVideoHisList.Data) assign((LDVideoHisList.Data)_o1_);
			else if (_o1_ instanceof LDVideoHisList.Const) assign(((LDVideoHisList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LDVideoHisList _o_) {
			ldvidehisinfo = new java.util.ArrayList<String>();
			ldvidehisinfo.addAll(_o_.ldvidehisinfo);
		}

		private void assign(LDVideoHisList.Data _o_) {
			ldvidehisinfo = new java.util.ArrayList<String>();
			ldvidehisinfo.addAll(_o_.ldvidehisinfo);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(ldvidehisinfo.size());
			for (String _v_ : ldvidehisinfo) {
				_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				String _v_ = "";
				_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				ldvidehisinfo.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.LDVideoHisList copy() {
			return new Data(this);
		}

		@Override
		public xbean.LDVideoHisList toData() {
			return new Data(this);
		}

		public xbean.LDVideoHisList toBean() {
			return new LDVideoHisList(this, null, null);
		}

		@Override
		public xbean.LDVideoHisList toDataIf() {
			return this;
		}

		public xbean.LDVideoHisList toBeanIf() {
			return new LDVideoHisList(this, null, null);
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
		public java.util.List<String> getLdvidehisinfo() { // 生死战历史排行记录
			return ldvidehisinfo;
		}

		@Override
		public java.util.List<String> getLdvidehisinfoAsData() { // 生死战历史排行记录
			return ldvidehisinfo;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LDVideoHisList.Data)) return false;
			LDVideoHisList.Data _o_ = (LDVideoHisList.Data) _o1_;
			if (!ldvidehisinfo.equals(_o_.ldvidehisinfo)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += ldvidehisinfo.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(ldvidehisinfo);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
