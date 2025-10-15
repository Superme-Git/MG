
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ImpExamStateRecord extends mkdb.XBean implements xbean.ImpExamStateRecord {
	private java.util.ArrayList<Long> idslist; // 能参加智慧试炼state测试的id列表
	private java.util.ArrayList<Long> passidslist; // 通过的角色id列表
	private java.util.ArrayList<xbean.ImpExamStatePassRole> passrolelist; // 通过的角色列表

	@Override
	public void _reset_unsafe_() {
		idslist.clear();
		passidslist.clear();
		passrolelist.clear();
	}

	ImpExamStateRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		idslist = new java.util.ArrayList<Long>();
		passidslist = new java.util.ArrayList<Long>();
		passrolelist = new java.util.ArrayList<xbean.ImpExamStatePassRole>();
	}

	public ImpExamStateRecord() {
		this(0, null, null);
	}

	public ImpExamStateRecord(ImpExamStateRecord _o_) {
		this(_o_, null, null);
	}

	ImpExamStateRecord(xbean.ImpExamStateRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ImpExamStateRecord) assign((ImpExamStateRecord)_o1_);
		else if (_o1_ instanceof ImpExamStateRecord.Data) assign((ImpExamStateRecord.Data)_o1_);
		else if (_o1_ instanceof ImpExamStateRecord.Const) assign(((ImpExamStateRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ImpExamStateRecord _o_) {
		_o_._xdb_verify_unsafe_();
		idslist = new java.util.ArrayList<Long>();
		idslist.addAll(_o_.idslist);
		passidslist = new java.util.ArrayList<Long>();
		passidslist.addAll(_o_.passidslist);
		passrolelist = new java.util.ArrayList<xbean.ImpExamStatePassRole>();
		for (xbean.ImpExamStatePassRole _v_ : _o_.passrolelist)
			passrolelist.add(new ImpExamStatePassRole(_v_, this, "passrolelist"));
	}

	private void assign(ImpExamStateRecord.Data _o_) {
		idslist = new java.util.ArrayList<Long>();
		idslist.addAll(_o_.idslist);
		passidslist = new java.util.ArrayList<Long>();
		passidslist.addAll(_o_.passidslist);
		passrolelist = new java.util.ArrayList<xbean.ImpExamStatePassRole>();
		for (xbean.ImpExamStatePassRole _v_ : _o_.passrolelist)
			passrolelist.add(new ImpExamStatePassRole(_v_, this, "passrolelist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(idslist.size());
		for (Long _v_ : idslist) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(passidslist.size());
		for (Long _v_ : passidslist) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(passrolelist.size());
		for (xbean.ImpExamStatePassRole _v_ : passrolelist) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			idslist.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			passidslist.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.ImpExamStatePassRole _v_ = new ImpExamStatePassRole(0, this, "passrolelist");
			_v_.unmarshal(_os_);
			passrolelist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.ImpExamStateRecord copy() {
		_xdb_verify_unsafe_();
		return new ImpExamStateRecord(this);
	}

	@Override
	public xbean.ImpExamStateRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ImpExamStateRecord toBean() {
		_xdb_verify_unsafe_();
		return new ImpExamStateRecord(this); // same as copy()
	}

	@Override
	public xbean.ImpExamStateRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ImpExamStateRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Long> getIdslist() { // 能参加智慧试炼state测试的id列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "idslist"), idslist);
	}

	public java.util.List<Long> getIdslistAsData() { // 能参加智慧试炼state测试的id列表
		_xdb_verify_unsafe_();
		java.util.List<Long> idslist;
		ImpExamStateRecord _o_ = this;
		idslist = new java.util.ArrayList<Long>();
		idslist.addAll(_o_.idslist);
		return idslist;
	}

	@Override
	public java.util.List<Long> getPassidslist() { // 通过的角色id列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "passidslist"), passidslist);
	}

	public java.util.List<Long> getPassidslistAsData() { // 通过的角色id列表
		_xdb_verify_unsafe_();
		java.util.List<Long> passidslist;
		ImpExamStateRecord _o_ = this;
		passidslist = new java.util.ArrayList<Long>();
		passidslist.addAll(_o_.passidslist);
		return passidslist;
	}

	@Override
	public java.util.List<xbean.ImpExamStatePassRole> getPassrolelist() { // 通过的角色列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "passrolelist"), passrolelist);
	}

	public java.util.List<xbean.ImpExamStatePassRole> getPassrolelistAsData() { // 通过的角色列表
		_xdb_verify_unsafe_();
		java.util.List<xbean.ImpExamStatePassRole> passrolelist;
		ImpExamStateRecord _o_ = this;
		passrolelist = new java.util.ArrayList<xbean.ImpExamStatePassRole>();
		for (xbean.ImpExamStatePassRole _v_ : _o_.passrolelist)
			passrolelist.add(new ImpExamStatePassRole.Data(_v_));
		return passrolelist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ImpExamStateRecord _o_ = null;
		if ( _o1_ instanceof ImpExamStateRecord ) _o_ = (ImpExamStateRecord)_o1_;
		else if ( _o1_ instanceof ImpExamStateRecord.Const ) _o_ = ((ImpExamStateRecord.Const)_o1_).nThis();
		else return false;
		if (!idslist.equals(_o_.idslist)) return false;
		if (!passidslist.equals(_o_.passidslist)) return false;
		if (!passrolelist.equals(_o_.passrolelist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += idslist.hashCode();
		_h_ += passidslist.hashCode();
		_h_ += passrolelist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(idslist);
		_sb_.append(",");
		_sb_.append(passidslist);
		_sb_.append(",");
		_sb_.append(passrolelist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("idslist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("passidslist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("passrolelist"));
		return lb;
	}

	private class Const implements xbean.ImpExamStateRecord {
		ImpExamStateRecord nThis() {
			return ImpExamStateRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ImpExamStateRecord copy() {
			return ImpExamStateRecord.this.copy();
		}

		@Override
		public xbean.ImpExamStateRecord toData() {
			return ImpExamStateRecord.this.toData();
		}

		public xbean.ImpExamStateRecord toBean() {
			return ImpExamStateRecord.this.toBean();
		}

		@Override
		public xbean.ImpExamStateRecord toDataIf() {
			return ImpExamStateRecord.this.toDataIf();
		}

		public xbean.ImpExamStateRecord toBeanIf() {
			return ImpExamStateRecord.this.toBeanIf();
		}

		@Override
		public java.util.List<Long> getIdslist() { // 能参加智慧试炼state测试的id列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(idslist);
		}

		public java.util.List<Long> getIdslistAsData() { // 能参加智慧试炼state测试的id列表
			_xdb_verify_unsafe_();
			java.util.List<Long> idslist;
			ImpExamStateRecord _o_ = ImpExamStateRecord.this;
		idslist = new java.util.ArrayList<Long>();
		idslist.addAll(_o_.idslist);
			return idslist;
		}

		@Override
		public java.util.List<Long> getPassidslist() { // 通过的角色id列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(passidslist);
		}

		public java.util.List<Long> getPassidslistAsData() { // 通过的角色id列表
			_xdb_verify_unsafe_();
			java.util.List<Long> passidslist;
			ImpExamStateRecord _o_ = ImpExamStateRecord.this;
		passidslist = new java.util.ArrayList<Long>();
		passidslist.addAll(_o_.passidslist);
			return passidslist;
		}

		@Override
		public java.util.List<xbean.ImpExamStatePassRole> getPassrolelist() { // 通过的角色列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(passrolelist);
		}

		public java.util.List<xbean.ImpExamStatePassRole> getPassrolelistAsData() { // 通过的角色列表
			_xdb_verify_unsafe_();
			java.util.List<xbean.ImpExamStatePassRole> passrolelist;
			ImpExamStateRecord _o_ = ImpExamStateRecord.this;
		passrolelist = new java.util.ArrayList<xbean.ImpExamStatePassRole>();
		for (xbean.ImpExamStatePassRole _v_ : _o_.passrolelist)
			passrolelist.add(new ImpExamStatePassRole.Data(_v_));
			return passrolelist;
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
			return ImpExamStateRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ImpExamStateRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ImpExamStateRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ImpExamStateRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ImpExamStateRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ImpExamStateRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ImpExamStateRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ImpExamStateRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return ImpExamStateRecord.this.toString();
		}

	}

	public static final class Data implements xbean.ImpExamStateRecord {
		private java.util.ArrayList<Long> idslist; // 能参加智慧试炼state测试的id列表
		private java.util.ArrayList<Long> passidslist; // 通过的角色id列表
		private java.util.ArrayList<xbean.ImpExamStatePassRole> passrolelist; // 通过的角色列表

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			idslist = new java.util.ArrayList<Long>();
			passidslist = new java.util.ArrayList<Long>();
			passrolelist = new java.util.ArrayList<xbean.ImpExamStatePassRole>();
		}

		Data(xbean.ImpExamStateRecord _o1_) {
			if (_o1_ instanceof ImpExamStateRecord) assign((ImpExamStateRecord)_o1_);
			else if (_o1_ instanceof ImpExamStateRecord.Data) assign((ImpExamStateRecord.Data)_o1_);
			else if (_o1_ instanceof ImpExamStateRecord.Const) assign(((ImpExamStateRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ImpExamStateRecord _o_) {
			idslist = new java.util.ArrayList<Long>();
			idslist.addAll(_o_.idslist);
			passidslist = new java.util.ArrayList<Long>();
			passidslist.addAll(_o_.passidslist);
			passrolelist = new java.util.ArrayList<xbean.ImpExamStatePassRole>();
			for (xbean.ImpExamStatePassRole _v_ : _o_.passrolelist)
				passrolelist.add(new ImpExamStatePassRole.Data(_v_));
		}

		private void assign(ImpExamStateRecord.Data _o_) {
			idslist = new java.util.ArrayList<Long>();
			idslist.addAll(_o_.idslist);
			passidslist = new java.util.ArrayList<Long>();
			passidslist.addAll(_o_.passidslist);
			passrolelist = new java.util.ArrayList<xbean.ImpExamStatePassRole>();
			for (xbean.ImpExamStatePassRole _v_ : _o_.passrolelist)
				passrolelist.add(new ImpExamStatePassRole.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(idslist.size());
			for (Long _v_ : idslist) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(passidslist.size());
			for (Long _v_ : passidslist) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(passrolelist.size());
			for (xbean.ImpExamStatePassRole _v_ : passrolelist) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				idslist.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				passidslist.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.ImpExamStatePassRole _v_ = xbean.Pod.newImpExamStatePassRoleData();
				_v_.unmarshal(_os_);
				passrolelist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.ImpExamStateRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.ImpExamStateRecord toData() {
			return new Data(this);
		}

		public xbean.ImpExamStateRecord toBean() {
			return new ImpExamStateRecord(this, null, null);
		}

		@Override
		public xbean.ImpExamStateRecord toDataIf() {
			return this;
		}

		public xbean.ImpExamStateRecord toBeanIf() {
			return new ImpExamStateRecord(this, null, null);
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
		public java.util.List<Long> getIdslist() { // 能参加智慧试炼state测试的id列表
			return idslist;
		}

		@Override
		public java.util.List<Long> getIdslistAsData() { // 能参加智慧试炼state测试的id列表
			return idslist;
		}

		@Override
		public java.util.List<Long> getPassidslist() { // 通过的角色id列表
			return passidslist;
		}

		@Override
		public java.util.List<Long> getPassidslistAsData() { // 通过的角色id列表
			return passidslist;
		}

		@Override
		public java.util.List<xbean.ImpExamStatePassRole> getPassrolelist() { // 通过的角色列表
			return passrolelist;
		}

		@Override
		public java.util.List<xbean.ImpExamStatePassRole> getPassrolelistAsData() { // 通过的角色列表
			return passrolelist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ImpExamStateRecord.Data)) return false;
			ImpExamStateRecord.Data _o_ = (ImpExamStateRecord.Data) _o1_;
			if (!idslist.equals(_o_.idslist)) return false;
			if (!passidslist.equals(_o_.passidslist)) return false;
			if (!passrolelist.equals(_o_.passrolelist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += idslist.hashCode();
			_h_ += passidslist.hashCode();
			_h_ += passrolelist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(idslist);
			_sb_.append(",");
			_sb_.append(passidslist);
			_sb_.append(",");
			_sb_.append(passrolelist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
