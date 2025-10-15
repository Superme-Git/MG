
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class LiveDieRoleInfo extends mkdb.XBean implements xbean.LiveDieRoleInfo {
	private long objectid; // 下战书目标玩家id
	private long invitationtime; // 下战书时间
	private int selecttype; // 战书模式0单人   1组队
	private int acceptflag; // 目标是否接受挑战 0初始状态   1接受战书

	@Override
	public void _reset_unsafe_() {
		objectid = 0L;
		invitationtime = 0L;
		selecttype = 0;
		acceptflag = 0;
	}

	LiveDieRoleInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public LiveDieRoleInfo() {
		this(0, null, null);
	}

	public LiveDieRoleInfo(LiveDieRoleInfo _o_) {
		this(_o_, null, null);
	}

	LiveDieRoleInfo(xbean.LiveDieRoleInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LiveDieRoleInfo) assign((LiveDieRoleInfo)_o1_);
		else if (_o1_ instanceof LiveDieRoleInfo.Data) assign((LiveDieRoleInfo.Data)_o1_);
		else if (_o1_ instanceof LiveDieRoleInfo.Const) assign(((LiveDieRoleInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LiveDieRoleInfo _o_) {
		_o_._xdb_verify_unsafe_();
		objectid = _o_.objectid;
		invitationtime = _o_.invitationtime;
		selecttype = _o_.selecttype;
		acceptflag = _o_.acceptflag;
	}

	private void assign(LiveDieRoleInfo.Data _o_) {
		objectid = _o_.objectid;
		invitationtime = _o_.invitationtime;
		selecttype = _o_.selecttype;
		acceptflag = _o_.acceptflag;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(objectid);
		_os_.marshal(invitationtime);
		_os_.marshal(selecttype);
		_os_.marshal(acceptflag);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		objectid = _os_.unmarshal_long();
		invitationtime = _os_.unmarshal_long();
		selecttype = _os_.unmarshal_int();
		acceptflag = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.LiveDieRoleInfo copy() {
		_xdb_verify_unsafe_();
		return new LiveDieRoleInfo(this);
	}

	@Override
	public xbean.LiveDieRoleInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LiveDieRoleInfo toBean() {
		_xdb_verify_unsafe_();
		return new LiveDieRoleInfo(this); // same as copy()
	}

	@Override
	public xbean.LiveDieRoleInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LiveDieRoleInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getObjectid() { // 下战书目标玩家id
		_xdb_verify_unsafe_();
		return objectid;
	}

	@Override
	public long getInvitationtime() { // 下战书时间
		_xdb_verify_unsafe_();
		return invitationtime;
	}

	@Override
	public int getSelecttype() { // 战书模式0单人   1组队
		_xdb_verify_unsafe_();
		return selecttype;
	}

	@Override
	public int getAcceptflag() { // 目标是否接受挑战 0初始状态   1接受战书
		_xdb_verify_unsafe_();
		return acceptflag;
	}

	@Override
	public void setObjectid(long _v_) { // 下战书目标玩家id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "objectid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, objectid) {
					public void rollback() { objectid = _xdb_saved; }
				};}});
		objectid = _v_;
	}

	@Override
	public void setInvitationtime(long _v_) { // 下战书时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "invitationtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, invitationtime) {
					public void rollback() { invitationtime = _xdb_saved; }
				};}});
		invitationtime = _v_;
	}

	@Override
	public void setSelecttype(int _v_) { // 战书模式0单人   1组队
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "selecttype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, selecttype) {
					public void rollback() { selecttype = _xdb_saved; }
				};}});
		selecttype = _v_;
	}

	@Override
	public void setAcceptflag(int _v_) { // 目标是否接受挑战 0初始状态   1接受战书
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "acceptflag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, acceptflag) {
					public void rollback() { acceptflag = _xdb_saved; }
				};}});
		acceptflag = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LiveDieRoleInfo _o_ = null;
		if ( _o1_ instanceof LiveDieRoleInfo ) _o_ = (LiveDieRoleInfo)_o1_;
		else if ( _o1_ instanceof LiveDieRoleInfo.Const ) _o_ = ((LiveDieRoleInfo.Const)_o1_).nThis();
		else return false;
		if (objectid != _o_.objectid) return false;
		if (invitationtime != _o_.invitationtime) return false;
		if (selecttype != _o_.selecttype) return false;
		if (acceptflag != _o_.acceptflag) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += objectid;
		_h_ += invitationtime;
		_h_ += selecttype;
		_h_ += acceptflag;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(objectid);
		_sb_.append(",");
		_sb_.append(invitationtime);
		_sb_.append(",");
		_sb_.append(selecttype);
		_sb_.append(",");
		_sb_.append(acceptflag);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("objectid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("invitationtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("selecttype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("acceptflag"));
		return lb;
	}

	private class Const implements xbean.LiveDieRoleInfo {
		LiveDieRoleInfo nThis() {
			return LiveDieRoleInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LiveDieRoleInfo copy() {
			return LiveDieRoleInfo.this.copy();
		}

		@Override
		public xbean.LiveDieRoleInfo toData() {
			return LiveDieRoleInfo.this.toData();
		}

		public xbean.LiveDieRoleInfo toBean() {
			return LiveDieRoleInfo.this.toBean();
		}

		@Override
		public xbean.LiveDieRoleInfo toDataIf() {
			return LiveDieRoleInfo.this.toDataIf();
		}

		public xbean.LiveDieRoleInfo toBeanIf() {
			return LiveDieRoleInfo.this.toBeanIf();
		}

		@Override
		public long getObjectid() { // 下战书目标玩家id
			_xdb_verify_unsafe_();
			return objectid;
		}

		@Override
		public long getInvitationtime() { // 下战书时间
			_xdb_verify_unsafe_();
			return invitationtime;
		}

		@Override
		public int getSelecttype() { // 战书模式0单人   1组队
			_xdb_verify_unsafe_();
			return selecttype;
		}

		@Override
		public int getAcceptflag() { // 目标是否接受挑战 0初始状态   1接受战书
			_xdb_verify_unsafe_();
			return acceptflag;
		}

		@Override
		public void setObjectid(long _v_) { // 下战书目标玩家id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInvitationtime(long _v_) { // 下战书时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSelecttype(int _v_) { // 战书模式0单人   1组队
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAcceptflag(int _v_) { // 目标是否接受挑战 0初始状态   1接受战书
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
			return LiveDieRoleInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LiveDieRoleInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return LiveDieRoleInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LiveDieRoleInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LiveDieRoleInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LiveDieRoleInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LiveDieRoleInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LiveDieRoleInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return LiveDieRoleInfo.this.toString();
		}

	}

	public static final class Data implements xbean.LiveDieRoleInfo {
		private long objectid; // 下战书目标玩家id
		private long invitationtime; // 下战书时间
		private int selecttype; // 战书模式0单人   1组队
		private int acceptflag; // 目标是否接受挑战 0初始状态   1接受战书

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.LiveDieRoleInfo _o1_) {
			if (_o1_ instanceof LiveDieRoleInfo) assign((LiveDieRoleInfo)_o1_);
			else if (_o1_ instanceof LiveDieRoleInfo.Data) assign((LiveDieRoleInfo.Data)_o1_);
			else if (_o1_ instanceof LiveDieRoleInfo.Const) assign(((LiveDieRoleInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LiveDieRoleInfo _o_) {
			objectid = _o_.objectid;
			invitationtime = _o_.invitationtime;
			selecttype = _o_.selecttype;
			acceptflag = _o_.acceptflag;
		}

		private void assign(LiveDieRoleInfo.Data _o_) {
			objectid = _o_.objectid;
			invitationtime = _o_.invitationtime;
			selecttype = _o_.selecttype;
			acceptflag = _o_.acceptflag;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(objectid);
			_os_.marshal(invitationtime);
			_os_.marshal(selecttype);
			_os_.marshal(acceptflag);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			objectid = _os_.unmarshal_long();
			invitationtime = _os_.unmarshal_long();
			selecttype = _os_.unmarshal_int();
			acceptflag = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.LiveDieRoleInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.LiveDieRoleInfo toData() {
			return new Data(this);
		}

		public xbean.LiveDieRoleInfo toBean() {
			return new LiveDieRoleInfo(this, null, null);
		}

		@Override
		public xbean.LiveDieRoleInfo toDataIf() {
			return this;
		}

		public xbean.LiveDieRoleInfo toBeanIf() {
			return new LiveDieRoleInfo(this, null, null);
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
		public long getObjectid() { // 下战书目标玩家id
			return objectid;
		}

		@Override
		public long getInvitationtime() { // 下战书时间
			return invitationtime;
		}

		@Override
		public int getSelecttype() { // 战书模式0单人   1组队
			return selecttype;
		}

		@Override
		public int getAcceptflag() { // 目标是否接受挑战 0初始状态   1接受战书
			return acceptflag;
		}

		@Override
		public void setObjectid(long _v_) { // 下战书目标玩家id
			objectid = _v_;
		}

		@Override
		public void setInvitationtime(long _v_) { // 下战书时间
			invitationtime = _v_;
		}

		@Override
		public void setSelecttype(int _v_) { // 战书模式0单人   1组队
			selecttype = _v_;
		}

		@Override
		public void setAcceptflag(int _v_) { // 目标是否接受挑战 0初始状态   1接受战书
			acceptflag = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LiveDieRoleInfo.Data)) return false;
			LiveDieRoleInfo.Data _o_ = (LiveDieRoleInfo.Data) _o1_;
			if (objectid != _o_.objectid) return false;
			if (invitationtime != _o_.invitationtime) return false;
			if (selecttype != _o_.selecttype) return false;
			if (acceptflag != _o_.acceptflag) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += objectid;
			_h_ += invitationtime;
			_h_ += selecttype;
			_h_ += acceptflag;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(objectid);
			_sb_.append(",");
			_sb_.append(invitationtime);
			_sb_.append(",");
			_sb_.append(selecttype);
			_sb_.append(",");
			_sb_.append(acceptflag);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
