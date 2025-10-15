
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ImpeachMent extends mkdb.XBean implements xbean.ImpeachMent {
	private long impeachroleid; // 发起弹劾的人
	private long impeachtime; // 发起弹劾的时间
	private short impeachallnum; // 需要相应弹劾的总人数
	private java.util.LinkedList<Long> acceptimpeachroleids; // 相应弹劾的角色ID

	@Override
	public void _reset_unsafe_() {
		impeachroleid = 0;
		impeachtime = 0L;
		impeachallnum = 0;
		acceptimpeachroleids.clear();
	}

	ImpeachMent(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		impeachroleid = 0;
		acceptimpeachroleids = new java.util.LinkedList<Long>();
	}

	public ImpeachMent() {
		this(0, null, null);
	}

	public ImpeachMent(ImpeachMent _o_) {
		this(_o_, null, null);
	}

	ImpeachMent(xbean.ImpeachMent _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ImpeachMent) assign((ImpeachMent)_o1_);
		else if (_o1_ instanceof ImpeachMent.Data) assign((ImpeachMent.Data)_o1_);
		else if (_o1_ instanceof ImpeachMent.Const) assign(((ImpeachMent.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ImpeachMent _o_) {
		_o_._xdb_verify_unsafe_();
		impeachroleid = _o_.impeachroleid;
		impeachtime = _o_.impeachtime;
		impeachallnum = _o_.impeachallnum;
		acceptimpeachroleids = new java.util.LinkedList<Long>();
		acceptimpeachroleids.addAll(_o_.acceptimpeachroleids);
	}

	private void assign(ImpeachMent.Data _o_) {
		impeachroleid = _o_.impeachroleid;
		impeachtime = _o_.impeachtime;
		impeachallnum = _o_.impeachallnum;
		acceptimpeachroleids = new java.util.LinkedList<Long>();
		acceptimpeachroleids.addAll(_o_.acceptimpeachroleids);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(impeachroleid);
		_os_.marshal(impeachtime);
		_os_.marshal(impeachallnum);
		_os_.compact_uint32(acceptimpeachroleids.size());
		for (Long _v_ : acceptimpeachroleids) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		impeachroleid = _os_.unmarshal_long();
		impeachtime = _os_.unmarshal_long();
		impeachallnum = _os_.unmarshal_short();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			acceptimpeachroleids.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.ImpeachMent copy() {
		_xdb_verify_unsafe_();
		return new ImpeachMent(this);
	}

	@Override
	public xbean.ImpeachMent toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ImpeachMent toBean() {
		_xdb_verify_unsafe_();
		return new ImpeachMent(this); // same as copy()
	}

	@Override
	public xbean.ImpeachMent toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ImpeachMent toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getImpeachroleid() { // 发起弹劾的人
		_xdb_verify_unsafe_();
		return impeachroleid;
	}

	@Override
	public long getImpeachtime() { // 发起弹劾的时间
		_xdb_verify_unsafe_();
		return impeachtime;
	}

	@Override
	public short getImpeachallnum() { // 需要相应弹劾的总人数
		_xdb_verify_unsafe_();
		return impeachallnum;
	}

	@Override
	public java.util.List<Long> getAcceptimpeachroleids() { // 相应弹劾的角色ID
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "acceptimpeachroleids"), acceptimpeachroleids);
	}

	public java.util.List<Long> getAcceptimpeachroleidsAsData() { // 相应弹劾的角色ID
		_xdb_verify_unsafe_();
		java.util.List<Long> acceptimpeachroleids;
		ImpeachMent _o_ = this;
		acceptimpeachroleids = new java.util.LinkedList<Long>();
		acceptimpeachroleids.addAll(_o_.acceptimpeachroleids);
		return acceptimpeachroleids;
	}

	@Override
	public void setImpeachroleid(long _v_) { // 发起弹劾的人
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "impeachroleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, impeachroleid) {
					public void rollback() { impeachroleid = _xdb_saved; }
				};}});
		impeachroleid = _v_;
	}

	@Override
	public void setImpeachtime(long _v_) { // 发起弹劾的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "impeachtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, impeachtime) {
					public void rollback() { impeachtime = _xdb_saved; }
				};}});
		impeachtime = _v_;
	}

	@Override
	public void setImpeachallnum(short _v_) { // 需要相应弹劾的总人数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "impeachallnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogShort(this, impeachallnum) {
					public void rollback() { impeachallnum = _xdb_saved; }
				};}});
		impeachallnum = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ImpeachMent _o_ = null;
		if ( _o1_ instanceof ImpeachMent ) _o_ = (ImpeachMent)_o1_;
		else if ( _o1_ instanceof ImpeachMent.Const ) _o_ = ((ImpeachMent.Const)_o1_).nThis();
		else return false;
		if (impeachroleid != _o_.impeachroleid) return false;
		if (impeachtime != _o_.impeachtime) return false;
		if (impeachallnum != _o_.impeachallnum) return false;
		if (!acceptimpeachroleids.equals(_o_.acceptimpeachroleids)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += impeachroleid;
		_h_ += impeachtime;
		_h_ += impeachallnum;
		_h_ += acceptimpeachroleids.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(impeachroleid);
		_sb_.append(",");
		_sb_.append(impeachtime);
		_sb_.append(",");
		_sb_.append(impeachallnum);
		_sb_.append(",");
		_sb_.append(acceptimpeachroleids);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("impeachroleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("impeachtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("impeachallnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("acceptimpeachroleids"));
		return lb;
	}

	private class Const implements xbean.ImpeachMent {
		ImpeachMent nThis() {
			return ImpeachMent.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ImpeachMent copy() {
			return ImpeachMent.this.copy();
		}

		@Override
		public xbean.ImpeachMent toData() {
			return ImpeachMent.this.toData();
		}

		public xbean.ImpeachMent toBean() {
			return ImpeachMent.this.toBean();
		}

		@Override
		public xbean.ImpeachMent toDataIf() {
			return ImpeachMent.this.toDataIf();
		}

		public xbean.ImpeachMent toBeanIf() {
			return ImpeachMent.this.toBeanIf();
		}

		@Override
		public long getImpeachroleid() { // 发起弹劾的人
			_xdb_verify_unsafe_();
			return impeachroleid;
		}

		@Override
		public long getImpeachtime() { // 发起弹劾的时间
			_xdb_verify_unsafe_();
			return impeachtime;
		}

		@Override
		public short getImpeachallnum() { // 需要相应弹劾的总人数
			_xdb_verify_unsafe_();
			return impeachallnum;
		}

		@Override
		public java.util.List<Long> getAcceptimpeachroleids() { // 相应弹劾的角色ID
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(acceptimpeachroleids);
		}

		public java.util.List<Long> getAcceptimpeachroleidsAsData() { // 相应弹劾的角色ID
			_xdb_verify_unsafe_();
			java.util.List<Long> acceptimpeachroleids;
			ImpeachMent _o_ = ImpeachMent.this;
		acceptimpeachroleids = new java.util.LinkedList<Long>();
		acceptimpeachroleids.addAll(_o_.acceptimpeachroleids);
			return acceptimpeachroleids;
		}

		@Override
		public void setImpeachroleid(long _v_) { // 发起弹劾的人
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setImpeachtime(long _v_) { // 发起弹劾的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setImpeachallnum(short _v_) { // 需要相应弹劾的总人数
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
			return ImpeachMent.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ImpeachMent.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ImpeachMent.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ImpeachMent.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ImpeachMent.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ImpeachMent.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ImpeachMent.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ImpeachMent.this.hashCode();
		}

		@Override
		public String toString() {
			return ImpeachMent.this.toString();
		}

	}

	public static final class Data implements xbean.ImpeachMent {
		private long impeachroleid; // 发起弹劾的人
		private long impeachtime; // 发起弹劾的时间
		private short impeachallnum; // 需要相应弹劾的总人数
		private java.util.LinkedList<Long> acceptimpeachroleids; // 相应弹劾的角色ID

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			impeachroleid = 0;
			acceptimpeachroleids = new java.util.LinkedList<Long>();
		}

		Data(xbean.ImpeachMent _o1_) {
			if (_o1_ instanceof ImpeachMent) assign((ImpeachMent)_o1_);
			else if (_o1_ instanceof ImpeachMent.Data) assign((ImpeachMent.Data)_o1_);
			else if (_o1_ instanceof ImpeachMent.Const) assign(((ImpeachMent.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ImpeachMent _o_) {
			impeachroleid = _o_.impeachroleid;
			impeachtime = _o_.impeachtime;
			impeachallnum = _o_.impeachallnum;
			acceptimpeachroleids = new java.util.LinkedList<Long>();
			acceptimpeachroleids.addAll(_o_.acceptimpeachroleids);
		}

		private void assign(ImpeachMent.Data _o_) {
			impeachroleid = _o_.impeachroleid;
			impeachtime = _o_.impeachtime;
			impeachallnum = _o_.impeachallnum;
			acceptimpeachroleids = new java.util.LinkedList<Long>();
			acceptimpeachroleids.addAll(_o_.acceptimpeachroleids);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(impeachroleid);
			_os_.marshal(impeachtime);
			_os_.marshal(impeachallnum);
			_os_.compact_uint32(acceptimpeachroleids.size());
			for (Long _v_ : acceptimpeachroleids) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			impeachroleid = _os_.unmarshal_long();
			impeachtime = _os_.unmarshal_long();
			impeachallnum = _os_.unmarshal_short();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				acceptimpeachroleids.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.ImpeachMent copy() {
			return new Data(this);
		}

		@Override
		public xbean.ImpeachMent toData() {
			return new Data(this);
		}

		public xbean.ImpeachMent toBean() {
			return new ImpeachMent(this, null, null);
		}

		@Override
		public xbean.ImpeachMent toDataIf() {
			return this;
		}

		public xbean.ImpeachMent toBeanIf() {
			return new ImpeachMent(this, null, null);
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
		public long getImpeachroleid() { // 发起弹劾的人
			return impeachroleid;
		}

		@Override
		public long getImpeachtime() { // 发起弹劾的时间
			return impeachtime;
		}

		@Override
		public short getImpeachallnum() { // 需要相应弹劾的总人数
			return impeachallnum;
		}

		@Override
		public java.util.List<Long> getAcceptimpeachroleids() { // 相应弹劾的角色ID
			return acceptimpeachroleids;
		}

		@Override
		public java.util.List<Long> getAcceptimpeachroleidsAsData() { // 相应弹劾的角色ID
			return acceptimpeachroleids;
		}

		@Override
		public void setImpeachroleid(long _v_) { // 发起弹劾的人
			impeachroleid = _v_;
		}

		@Override
		public void setImpeachtime(long _v_) { // 发起弹劾的时间
			impeachtime = _v_;
		}

		@Override
		public void setImpeachallnum(short _v_) { // 需要相应弹劾的总人数
			impeachallnum = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ImpeachMent.Data)) return false;
			ImpeachMent.Data _o_ = (ImpeachMent.Data) _o1_;
			if (impeachroleid != _o_.impeachroleid) return false;
			if (impeachtime != _o_.impeachtime) return false;
			if (impeachallnum != _o_.impeachallnum) return false;
			if (!acceptimpeachroleids.equals(_o_.acceptimpeachroleids)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += impeachroleid;
			_h_ += impeachtime;
			_h_ += impeachallnum;
			_h_ += acceptimpeachroleids.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(impeachroleid);
			_sb_.append(",");
			_sb_.append(impeachtime);
			_sb_.append(",");
			_sb_.append(impeachallnum);
			_sb_.append(",");
			_sb_.append(acceptimpeachroleids);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
