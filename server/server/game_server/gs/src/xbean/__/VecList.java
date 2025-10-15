
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class VecList extends mkdb.XBean implements xbean.VecList {
	private java.util.ArrayList<xbean.MazeInfo> list; // 

	@Override
	public void _reset_unsafe_() {
		list.clear();
	}

	VecList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		list = new java.util.ArrayList<xbean.MazeInfo>();
	}

	public VecList() {
		this(0, null, null);
	}

	public VecList(VecList _o_) {
		this(_o_, null, null);
	}

	VecList(xbean.VecList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof VecList) assign((VecList)_o1_);
		else if (_o1_ instanceof VecList.Data) assign((VecList.Data)_o1_);
		else if (_o1_ instanceof VecList.Const) assign(((VecList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(VecList _o_) {
		_o_._xdb_verify_unsafe_();
		list = new java.util.ArrayList<xbean.MazeInfo>();
		for (xbean.MazeInfo _v_ : _o_.list)
			list.add(new MazeInfo(_v_, this, "list"));
	}

	private void assign(VecList.Data _o_) {
		list = new java.util.ArrayList<xbean.MazeInfo>();
		for (xbean.MazeInfo _v_ : _o_.list)
			list.add(new MazeInfo(_v_, this, "list"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(list.size());
		for (xbean.MazeInfo _v_ : list) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.MazeInfo _v_ = new MazeInfo(0, this, "list");
			_v_.unmarshal(_os_);
			list.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.VecList copy() {
		_xdb_verify_unsafe_();
		return new VecList(this);
	}

	@Override
	public xbean.VecList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.VecList toBean() {
		_xdb_verify_unsafe_();
		return new VecList(this); // same as copy()
	}

	@Override
	public xbean.VecList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.VecList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.MazeInfo> getList() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "list"), list);
	}

	public java.util.List<xbean.MazeInfo> getListAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.MazeInfo> list;
		VecList _o_ = this;
		list = new java.util.ArrayList<xbean.MazeInfo>();
		for (xbean.MazeInfo _v_ : _o_.list)
			list.add(new MazeInfo.Data(_v_));
		return list;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		VecList _o_ = null;
		if ( _o1_ instanceof VecList ) _o_ = (VecList)_o1_;
		else if ( _o1_ instanceof VecList.Const ) _o_ = ((VecList.Const)_o1_).nThis();
		else return false;
		if (!list.equals(_o_.list)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += list.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(list);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("list"));
		return lb;
	}

	private class Const implements xbean.VecList {
		VecList nThis() {
			return VecList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.VecList copy() {
			return VecList.this.copy();
		}

		@Override
		public xbean.VecList toData() {
			return VecList.this.toData();
		}

		public xbean.VecList toBean() {
			return VecList.this.toBean();
		}

		@Override
		public xbean.VecList toDataIf() {
			return VecList.this.toDataIf();
		}

		public xbean.VecList toBeanIf() {
			return VecList.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.MazeInfo> getList() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(list);
		}

		public java.util.List<xbean.MazeInfo> getListAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.MazeInfo> list;
			VecList _o_ = VecList.this;
		list = new java.util.ArrayList<xbean.MazeInfo>();
		for (xbean.MazeInfo _v_ : _o_.list)
			list.add(new MazeInfo.Data(_v_));
			return list;
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
			return VecList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return VecList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return VecList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return VecList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return VecList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return VecList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return VecList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return VecList.this.hashCode();
		}

		@Override
		public String toString() {
			return VecList.this.toString();
		}

	}

	public static final class Data implements xbean.VecList {
		private java.util.ArrayList<xbean.MazeInfo> list; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			list = new java.util.ArrayList<xbean.MazeInfo>();
		}

		Data(xbean.VecList _o1_) {
			if (_o1_ instanceof VecList) assign((VecList)_o1_);
			else if (_o1_ instanceof VecList.Data) assign((VecList.Data)_o1_);
			else if (_o1_ instanceof VecList.Const) assign(((VecList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(VecList _o_) {
			list = new java.util.ArrayList<xbean.MazeInfo>();
			for (xbean.MazeInfo _v_ : _o_.list)
				list.add(new MazeInfo.Data(_v_));
		}

		private void assign(VecList.Data _o_) {
			list = new java.util.ArrayList<xbean.MazeInfo>();
			for (xbean.MazeInfo _v_ : _o_.list)
				list.add(new MazeInfo.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(list.size());
			for (xbean.MazeInfo _v_ : list) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.MazeInfo _v_ = xbean.Pod.newMazeInfoData();
				_v_.unmarshal(_os_);
				list.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.VecList copy() {
			return new Data(this);
		}

		@Override
		public xbean.VecList toData() {
			return new Data(this);
		}

		public xbean.VecList toBean() {
			return new VecList(this, null, null);
		}

		@Override
		public xbean.VecList toDataIf() {
			return this;
		}

		public xbean.VecList toBeanIf() {
			return new VecList(this, null, null);
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
		public java.util.List<xbean.MazeInfo> getList() { // 
			return list;
		}

		@Override
		public java.util.List<xbean.MazeInfo> getListAsData() { // 
			return list;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof VecList.Data)) return false;
			VecList.Data _o_ = (VecList.Data) _o1_;
			if (!list.equals(_o_.list)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += list.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(list);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
