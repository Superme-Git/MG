
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ShowingMission extends mkdb.XBean implements xbean.ShowingMission {
	private int missionid; // 
	private boolean isleader; // 

	@Override
	public void _reset_unsafe_() {
		missionid = 0;
		isleader = false;
	}

	ShowingMission(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ShowingMission() {
		this(0, null, null);
	}

	public ShowingMission(ShowingMission _o_) {
		this(_o_, null, null);
	}

	ShowingMission(xbean.ShowingMission _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ShowingMission) assign((ShowingMission)_o1_);
		else if (_o1_ instanceof ShowingMission.Data) assign((ShowingMission.Data)_o1_);
		else if (_o1_ instanceof ShowingMission.Const) assign(((ShowingMission.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ShowingMission _o_) {
		_o_._xdb_verify_unsafe_();
		missionid = _o_.missionid;
		isleader = _o_.isleader;
	}

	private void assign(ShowingMission.Data _o_) {
		missionid = _o_.missionid;
		isleader = _o_.isleader;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(missionid);
		_os_.marshal(isleader);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		missionid = _os_.unmarshal_int();
		isleader = _os_.unmarshal_boolean();
		return _os_;
	}

	@Override
	public xbean.ShowingMission copy() {
		_xdb_verify_unsafe_();
		return new ShowingMission(this);
	}

	@Override
	public xbean.ShowingMission toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ShowingMission toBean() {
		_xdb_verify_unsafe_();
		return new ShowingMission(this); // same as copy()
	}

	@Override
	public xbean.ShowingMission toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ShowingMission toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getMissionid() { // 
		_xdb_verify_unsafe_();
		return missionid;
	}

	@Override
	public boolean getIsleader() { // 
		_xdb_verify_unsafe_();
		return isleader;
	}

	@Override
	public void setMissionid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "missionid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, missionid) {
					public void rollback() { missionid = _xdb_saved; }
				};}});
		missionid = _v_;
	}

	@Override
	public void setIsleader(boolean _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "isleader") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, isleader) {
					public void rollback() { isleader = _xdb_saved; }
				};}});
		isleader = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ShowingMission _o_ = null;
		if ( _o1_ instanceof ShowingMission ) _o_ = (ShowingMission)_o1_;
		else if ( _o1_ instanceof ShowingMission.Const ) _o_ = ((ShowingMission.Const)_o1_).nThis();
		else return false;
		if (missionid != _o_.missionid) return false;
		if (isleader != _o_.isleader) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += missionid;
		_h_ += isleader ? 1231 : 1237;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(missionid);
		_sb_.append(",");
		_sb_.append(isleader);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("missionid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("isleader"));
		return lb;
	}

	private class Const implements xbean.ShowingMission {
		ShowingMission nThis() {
			return ShowingMission.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ShowingMission copy() {
			return ShowingMission.this.copy();
		}

		@Override
		public xbean.ShowingMission toData() {
			return ShowingMission.this.toData();
		}

		public xbean.ShowingMission toBean() {
			return ShowingMission.this.toBean();
		}

		@Override
		public xbean.ShowingMission toDataIf() {
			return ShowingMission.this.toDataIf();
		}

		public xbean.ShowingMission toBeanIf() {
			return ShowingMission.this.toBeanIf();
		}

		@Override
		public int getMissionid() { // 
			_xdb_verify_unsafe_();
			return missionid;
		}

		@Override
		public boolean getIsleader() { // 
			_xdb_verify_unsafe_();
			return isleader;
		}

		@Override
		public void setMissionid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIsleader(boolean _v_) { // 
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
			return ShowingMission.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ShowingMission.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ShowingMission.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ShowingMission.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ShowingMission.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ShowingMission.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ShowingMission.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ShowingMission.this.hashCode();
		}

		@Override
		public String toString() {
			return ShowingMission.this.toString();
		}

	}

	public static final class Data implements xbean.ShowingMission {
		private int missionid; // 
		private boolean isleader; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ShowingMission _o1_) {
			if (_o1_ instanceof ShowingMission) assign((ShowingMission)_o1_);
			else if (_o1_ instanceof ShowingMission.Data) assign((ShowingMission.Data)_o1_);
			else if (_o1_ instanceof ShowingMission.Const) assign(((ShowingMission.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ShowingMission _o_) {
			missionid = _o_.missionid;
			isleader = _o_.isleader;
		}

		private void assign(ShowingMission.Data _o_) {
			missionid = _o_.missionid;
			isleader = _o_.isleader;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(missionid);
			_os_.marshal(isleader);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			missionid = _os_.unmarshal_int();
			isleader = _os_.unmarshal_boolean();
			return _os_;
		}

		@Override
		public xbean.ShowingMission copy() {
			return new Data(this);
		}

		@Override
		public xbean.ShowingMission toData() {
			return new Data(this);
		}

		public xbean.ShowingMission toBean() {
			return new ShowingMission(this, null, null);
		}

		@Override
		public xbean.ShowingMission toDataIf() {
			return this;
		}

		public xbean.ShowingMission toBeanIf() {
			return new ShowingMission(this, null, null);
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
		public int getMissionid() { // 
			return missionid;
		}

		@Override
		public boolean getIsleader() { // 
			return isleader;
		}

		@Override
		public void setMissionid(int _v_) { // 
			missionid = _v_;
		}

		@Override
		public void setIsleader(boolean _v_) { // 
			isleader = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ShowingMission.Data)) return false;
			ShowingMission.Data _o_ = (ShowingMission.Data) _o1_;
			if (missionid != _o_.missionid) return false;
			if (isleader != _o_.isleader) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += missionid;
			_h_ += isleader ? 1231 : 1237;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(missionid);
			_sb_.append(",");
			_sb_.append(isleader);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
