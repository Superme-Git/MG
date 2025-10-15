
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RewardData extends mkdb.XBean implements xbean.RewardData {
	private int rewardid; // 领了多少个奖励,缺省是0
	private long lastrewardtime; // 上次领取奖励的时间
	private long timewait; // 距离下次奖励领取等待时间

	@Override
	public void _reset_unsafe_() {
		rewardid = 0;
		lastrewardtime = 0L;
		timewait = 0L;
	}

	RewardData(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RewardData() {
		this(0, null, null);
	}

	public RewardData(RewardData _o_) {
		this(_o_, null, null);
	}

	RewardData(xbean.RewardData _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RewardData) assign((RewardData)_o1_);
		else if (_o1_ instanceof RewardData.Data) assign((RewardData.Data)_o1_);
		else if (_o1_ instanceof RewardData.Const) assign(((RewardData.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RewardData _o_) {
		_o_._xdb_verify_unsafe_();
		rewardid = _o_.rewardid;
		lastrewardtime = _o_.lastrewardtime;
		timewait = _o_.timewait;
	}

	private void assign(RewardData.Data _o_) {
		rewardid = _o_.rewardid;
		lastrewardtime = _o_.lastrewardtime;
		timewait = _o_.timewait;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(rewardid);
		_os_.marshal(lastrewardtime);
		_os_.marshal(timewait);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		rewardid = _os_.unmarshal_int();
		lastrewardtime = _os_.unmarshal_long();
		timewait = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RewardData copy() {
		_xdb_verify_unsafe_();
		return new RewardData(this);
	}

	@Override
	public xbean.RewardData toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RewardData toBean() {
		_xdb_verify_unsafe_();
		return new RewardData(this); // same as copy()
	}

	@Override
	public xbean.RewardData toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RewardData toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getRewardid() { // 领了多少个奖励,缺省是0
		_xdb_verify_unsafe_();
		return rewardid;
	}

	@Override
	public long getLastrewardtime() { // 上次领取奖励的时间
		_xdb_verify_unsafe_();
		return lastrewardtime;
	}

	@Override
	public long getTimewait() { // 距离下次奖励领取等待时间
		_xdb_verify_unsafe_();
		return timewait;
	}

	@Override
	public void setRewardid(int _v_) { // 领了多少个奖励,缺省是0
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rewardid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rewardid) {
					public void rollback() { rewardid = _xdb_saved; }
				};}});
		rewardid = _v_;
	}

	@Override
	public void setLastrewardtime(long _v_) { // 上次领取奖励的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastrewardtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastrewardtime) {
					public void rollback() { lastrewardtime = _xdb_saved; }
				};}});
		lastrewardtime = _v_;
	}

	@Override
	public void setTimewait(long _v_) { // 距离下次奖励领取等待时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "timewait") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, timewait) {
					public void rollback() { timewait = _xdb_saved; }
				};}});
		timewait = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RewardData _o_ = null;
		if ( _o1_ instanceof RewardData ) _o_ = (RewardData)_o1_;
		else if ( _o1_ instanceof RewardData.Const ) _o_ = ((RewardData.Const)_o1_).nThis();
		else return false;
		if (rewardid != _o_.rewardid) return false;
		if (lastrewardtime != _o_.lastrewardtime) return false;
		if (timewait != _o_.timewait) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += rewardid;
		_h_ += lastrewardtime;
		_h_ += timewait;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rewardid);
		_sb_.append(",");
		_sb_.append(lastrewardtime);
		_sb_.append(",");
		_sb_.append(timewait);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rewardid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastrewardtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("timewait"));
		return lb;
	}

	private class Const implements xbean.RewardData {
		RewardData nThis() {
			return RewardData.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RewardData copy() {
			return RewardData.this.copy();
		}

		@Override
		public xbean.RewardData toData() {
			return RewardData.this.toData();
		}

		public xbean.RewardData toBean() {
			return RewardData.this.toBean();
		}

		@Override
		public xbean.RewardData toDataIf() {
			return RewardData.this.toDataIf();
		}

		public xbean.RewardData toBeanIf() {
			return RewardData.this.toBeanIf();
		}

		@Override
		public int getRewardid() { // 领了多少个奖励,缺省是0
			_xdb_verify_unsafe_();
			return rewardid;
		}

		@Override
		public long getLastrewardtime() { // 上次领取奖励的时间
			_xdb_verify_unsafe_();
			return lastrewardtime;
		}

		@Override
		public long getTimewait() { // 距离下次奖励领取等待时间
			_xdb_verify_unsafe_();
			return timewait;
		}

		@Override
		public void setRewardid(int _v_) { // 领了多少个奖励,缺省是0
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastrewardtime(long _v_) { // 上次领取奖励的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTimewait(long _v_) { // 距离下次奖励领取等待时间
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
			return RewardData.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RewardData.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RewardData.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RewardData.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RewardData.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RewardData.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RewardData.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RewardData.this.hashCode();
		}

		@Override
		public String toString() {
			return RewardData.this.toString();
		}

	}

	public static final class Data implements xbean.RewardData {
		private int rewardid; // 领了多少个奖励,缺省是0
		private long lastrewardtime; // 上次领取奖励的时间
		private long timewait; // 距离下次奖励领取等待时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RewardData _o1_) {
			if (_o1_ instanceof RewardData) assign((RewardData)_o1_);
			else if (_o1_ instanceof RewardData.Data) assign((RewardData.Data)_o1_);
			else if (_o1_ instanceof RewardData.Const) assign(((RewardData.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RewardData _o_) {
			rewardid = _o_.rewardid;
			lastrewardtime = _o_.lastrewardtime;
			timewait = _o_.timewait;
		}

		private void assign(RewardData.Data _o_) {
			rewardid = _o_.rewardid;
			lastrewardtime = _o_.lastrewardtime;
			timewait = _o_.timewait;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(rewardid);
			_os_.marshal(lastrewardtime);
			_os_.marshal(timewait);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			rewardid = _os_.unmarshal_int();
			lastrewardtime = _os_.unmarshal_long();
			timewait = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RewardData copy() {
			return new Data(this);
		}

		@Override
		public xbean.RewardData toData() {
			return new Data(this);
		}

		public xbean.RewardData toBean() {
			return new RewardData(this, null, null);
		}

		@Override
		public xbean.RewardData toDataIf() {
			return this;
		}

		public xbean.RewardData toBeanIf() {
			return new RewardData(this, null, null);
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
		public int getRewardid() { // 领了多少个奖励,缺省是0
			return rewardid;
		}

		@Override
		public long getLastrewardtime() { // 上次领取奖励的时间
			return lastrewardtime;
		}

		@Override
		public long getTimewait() { // 距离下次奖励领取等待时间
			return timewait;
		}

		@Override
		public void setRewardid(int _v_) { // 领了多少个奖励,缺省是0
			rewardid = _v_;
		}

		@Override
		public void setLastrewardtime(long _v_) { // 上次领取奖励的时间
			lastrewardtime = _v_;
		}

		@Override
		public void setTimewait(long _v_) { // 距离下次奖励领取等待时间
			timewait = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RewardData.Data)) return false;
			RewardData.Data _o_ = (RewardData.Data) _o1_;
			if (rewardid != _o_.rewardid) return false;
			if (lastrewardtime != _o_.lastrewardtime) return false;
			if (timewait != _o_.timewait) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += rewardid;
			_h_ += lastrewardtime;
			_h_ += timewait;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(rewardid);
			_sb_.append(",");
			_sb_.append(lastrewardtime);
			_sb_.append(",");
			_sb_.append(timewait);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
