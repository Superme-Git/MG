
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleEventNpcInfo extends mkdb.XBean implements xbean.RoleEventNpcInfo {
	private int awardtimes; // //领取奖励次数
	private long lastawardtime; // //上次领取奖励时间

	@Override
	public void _reset_unsafe_() {
		awardtimes = 0;
		lastawardtime = 0L;
	}

	RoleEventNpcInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RoleEventNpcInfo() {
		this(0, null, null);
	}

	public RoleEventNpcInfo(RoleEventNpcInfo _o_) {
		this(_o_, null, null);
	}

	RoleEventNpcInfo(xbean.RoleEventNpcInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleEventNpcInfo) assign((RoleEventNpcInfo)_o1_);
		else if (_o1_ instanceof RoleEventNpcInfo.Data) assign((RoleEventNpcInfo.Data)_o1_);
		else if (_o1_ instanceof RoleEventNpcInfo.Const) assign(((RoleEventNpcInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleEventNpcInfo _o_) {
		_o_._xdb_verify_unsafe_();
		awardtimes = _o_.awardtimes;
		lastawardtime = _o_.lastawardtime;
	}

	private void assign(RoleEventNpcInfo.Data _o_) {
		awardtimes = _o_.awardtimes;
		lastawardtime = _o_.lastawardtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(awardtimes);
		_os_.marshal(lastawardtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		awardtimes = _os_.unmarshal_int();
		lastawardtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RoleEventNpcInfo copy() {
		_xdb_verify_unsafe_();
		return new RoleEventNpcInfo(this);
	}

	@Override
	public xbean.RoleEventNpcInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleEventNpcInfo toBean() {
		_xdb_verify_unsafe_();
		return new RoleEventNpcInfo(this); // same as copy()
	}

	@Override
	public xbean.RoleEventNpcInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleEventNpcInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getAwardtimes() { // //领取奖励次数
		_xdb_verify_unsafe_();
		return awardtimes;
	}

	@Override
	public long getLastawardtime() { // //上次领取奖励时间
		_xdb_verify_unsafe_();
		return lastawardtime;
	}

	@Override
	public void setAwardtimes(int _v_) { // //领取奖励次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "awardtimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, awardtimes) {
					public void rollback() { awardtimes = _xdb_saved; }
				};}});
		awardtimes = _v_;
	}

	@Override
	public void setLastawardtime(long _v_) { // //上次领取奖励时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastawardtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastawardtime) {
					public void rollback() { lastawardtime = _xdb_saved; }
				};}});
		lastawardtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleEventNpcInfo _o_ = null;
		if ( _o1_ instanceof RoleEventNpcInfo ) _o_ = (RoleEventNpcInfo)_o1_;
		else if ( _o1_ instanceof RoleEventNpcInfo.Const ) _o_ = ((RoleEventNpcInfo.Const)_o1_).nThis();
		else return false;
		if (awardtimes != _o_.awardtimes) return false;
		if (lastawardtime != _o_.lastawardtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += awardtimes;
		_h_ += lastawardtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(awardtimes);
		_sb_.append(",");
		_sb_.append(lastawardtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("awardtimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastawardtime"));
		return lb;
	}

	private class Const implements xbean.RoleEventNpcInfo {
		RoleEventNpcInfo nThis() {
			return RoleEventNpcInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleEventNpcInfo copy() {
			return RoleEventNpcInfo.this.copy();
		}

		@Override
		public xbean.RoleEventNpcInfo toData() {
			return RoleEventNpcInfo.this.toData();
		}

		public xbean.RoleEventNpcInfo toBean() {
			return RoleEventNpcInfo.this.toBean();
		}

		@Override
		public xbean.RoleEventNpcInfo toDataIf() {
			return RoleEventNpcInfo.this.toDataIf();
		}

		public xbean.RoleEventNpcInfo toBeanIf() {
			return RoleEventNpcInfo.this.toBeanIf();
		}

		@Override
		public int getAwardtimes() { // //领取奖励次数
			_xdb_verify_unsafe_();
			return awardtimes;
		}

		@Override
		public long getLastawardtime() { // //上次领取奖励时间
			_xdb_verify_unsafe_();
			return lastawardtime;
		}

		@Override
		public void setAwardtimes(int _v_) { // //领取奖励次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastawardtime(long _v_) { // //上次领取奖励时间
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
			return RoleEventNpcInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleEventNpcInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleEventNpcInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleEventNpcInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleEventNpcInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleEventNpcInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleEventNpcInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleEventNpcInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleEventNpcInfo.this.toString();
		}

	}

	public static final class Data implements xbean.RoleEventNpcInfo {
		private int awardtimes; // //领取奖励次数
		private long lastawardtime; // //上次领取奖励时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RoleEventNpcInfo _o1_) {
			if (_o1_ instanceof RoleEventNpcInfo) assign((RoleEventNpcInfo)_o1_);
			else if (_o1_ instanceof RoleEventNpcInfo.Data) assign((RoleEventNpcInfo.Data)_o1_);
			else if (_o1_ instanceof RoleEventNpcInfo.Const) assign(((RoleEventNpcInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleEventNpcInfo _o_) {
			awardtimes = _o_.awardtimes;
			lastawardtime = _o_.lastawardtime;
		}

		private void assign(RoleEventNpcInfo.Data _o_) {
			awardtimes = _o_.awardtimes;
			lastawardtime = _o_.lastawardtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(awardtimes);
			_os_.marshal(lastawardtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			awardtimes = _os_.unmarshal_int();
			lastawardtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RoleEventNpcInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleEventNpcInfo toData() {
			return new Data(this);
		}

		public xbean.RoleEventNpcInfo toBean() {
			return new RoleEventNpcInfo(this, null, null);
		}

		@Override
		public xbean.RoleEventNpcInfo toDataIf() {
			return this;
		}

		public xbean.RoleEventNpcInfo toBeanIf() {
			return new RoleEventNpcInfo(this, null, null);
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
		public int getAwardtimes() { // //领取奖励次数
			return awardtimes;
		}

		@Override
		public long getLastawardtime() { // //上次领取奖励时间
			return lastawardtime;
		}

		@Override
		public void setAwardtimes(int _v_) { // //领取奖励次数
			awardtimes = _v_;
		}

		@Override
		public void setLastawardtime(long _v_) { // //上次领取奖励时间
			lastawardtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleEventNpcInfo.Data)) return false;
			RoleEventNpcInfo.Data _o_ = (RoleEventNpcInfo.Data) _o1_;
			if (awardtimes != _o_.awardtimes) return false;
			if (lastawardtime != _o_.lastawardtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += awardtimes;
			_h_ += lastawardtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(awardtimes);
			_sb_.append(",");
			_sb_.append(lastawardtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
