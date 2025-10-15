
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TransfromInfo extends mkdb.XBean implements xbean.TransfromInfo {
	private int transformid; // 人物变身时的shape
	private int questid; // 
	private long validdate; // 人物变身时的时限
	private int ride; // 人物变身时的坐骑

	@Override
	public void _reset_unsafe_() {
		transformid = 0;
		questid = 0;
		validdate = 0;
		ride = 0;
	}

	TransfromInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		transformid = 0;
		questid = 0;
		validdate = 0;
		ride = 0;
	}

	public TransfromInfo() {
		this(0, null, null);
	}

	public TransfromInfo(TransfromInfo _o_) {
		this(_o_, null, null);
	}

	TransfromInfo(xbean.TransfromInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TransfromInfo) assign((TransfromInfo)_o1_);
		else if (_o1_ instanceof TransfromInfo.Data) assign((TransfromInfo.Data)_o1_);
		else if (_o1_ instanceof TransfromInfo.Const) assign(((TransfromInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TransfromInfo _o_) {
		_o_._xdb_verify_unsafe_();
		transformid = _o_.transformid;
		questid = _o_.questid;
		validdate = _o_.validdate;
		ride = _o_.ride;
	}

	private void assign(TransfromInfo.Data _o_) {
		transformid = _o_.transformid;
		questid = _o_.questid;
		validdate = _o_.validdate;
		ride = _o_.ride;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(transformid);
		_os_.marshal(questid);
		_os_.marshal(validdate);
		_os_.marshal(ride);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		transformid = _os_.unmarshal_int();
		questid = _os_.unmarshal_int();
		validdate = _os_.unmarshal_long();
		ride = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.TransfromInfo copy() {
		_xdb_verify_unsafe_();
		return new TransfromInfo(this);
	}

	@Override
	public xbean.TransfromInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TransfromInfo toBean() {
		_xdb_verify_unsafe_();
		return new TransfromInfo(this); // same as copy()
	}

	@Override
	public xbean.TransfromInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TransfromInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getTransformid() { // 人物变身时的shape
		_xdb_verify_unsafe_();
		return transformid;
	}

	@Override
	public int getQuestid() { // 
		_xdb_verify_unsafe_();
		return questid;
	}

	@Override
	public long getValiddate() { // 人物变身时的时限
		_xdb_verify_unsafe_();
		return validdate;
	}

	@Override
	public int getRide() { // 人物变身时的坐骑
		_xdb_verify_unsafe_();
		return ride;
	}

	@Override
	public void setTransformid(int _v_) { // 人物变身时的shape
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "transformid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, transformid) {
					public void rollback() { transformid = _xdb_saved; }
				};}});
		transformid = _v_;
	}

	@Override
	public void setQuestid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "questid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, questid) {
					public void rollback() { questid = _xdb_saved; }
				};}});
		questid = _v_;
	}

	@Override
	public void setValiddate(long _v_) { // 人物变身时的时限
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "validdate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, validdate) {
					public void rollback() { validdate = _xdb_saved; }
				};}});
		validdate = _v_;
	}

	@Override
	public void setRide(int _v_) { // 人物变身时的坐骑
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "ride") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, ride) {
					public void rollback() { ride = _xdb_saved; }
				};}});
		ride = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TransfromInfo _o_ = null;
		if ( _o1_ instanceof TransfromInfo ) _o_ = (TransfromInfo)_o1_;
		else if ( _o1_ instanceof TransfromInfo.Const ) _o_ = ((TransfromInfo.Const)_o1_).nThis();
		else return false;
		if (transformid != _o_.transformid) return false;
		if (questid != _o_.questid) return false;
		if (validdate != _o_.validdate) return false;
		if (ride != _o_.ride) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += transformid;
		_h_ += questid;
		_h_ += validdate;
		_h_ += ride;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(transformid);
		_sb_.append(",");
		_sb_.append(questid);
		_sb_.append(",");
		_sb_.append(validdate);
		_sb_.append(",");
		_sb_.append(ride);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("transformid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("questid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("validdate"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ride"));
		return lb;
	}

	private class Const implements xbean.TransfromInfo {
		TransfromInfo nThis() {
			return TransfromInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TransfromInfo copy() {
			return TransfromInfo.this.copy();
		}

		@Override
		public xbean.TransfromInfo toData() {
			return TransfromInfo.this.toData();
		}

		public xbean.TransfromInfo toBean() {
			return TransfromInfo.this.toBean();
		}

		@Override
		public xbean.TransfromInfo toDataIf() {
			return TransfromInfo.this.toDataIf();
		}

		public xbean.TransfromInfo toBeanIf() {
			return TransfromInfo.this.toBeanIf();
		}

		@Override
		public int getTransformid() { // 人物变身时的shape
			_xdb_verify_unsafe_();
			return transformid;
		}

		@Override
		public int getQuestid() { // 
			_xdb_verify_unsafe_();
			return questid;
		}

		@Override
		public long getValiddate() { // 人物变身时的时限
			_xdb_verify_unsafe_();
			return validdate;
		}

		@Override
		public int getRide() { // 人物变身时的坐骑
			_xdb_verify_unsafe_();
			return ride;
		}

		@Override
		public void setTransformid(int _v_) { // 人物变身时的shape
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setQuestid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setValiddate(long _v_) { // 人物变身时的时限
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRide(int _v_) { // 人物变身时的坐骑
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
			return TransfromInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TransfromInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TransfromInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TransfromInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TransfromInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TransfromInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TransfromInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TransfromInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return TransfromInfo.this.toString();
		}

	}

	public static final class Data implements xbean.TransfromInfo {
		private int transformid; // 人物变身时的shape
		private int questid; // 
		private long validdate; // 人物变身时的时限
		private int ride; // 人物变身时的坐骑

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			transformid = 0;
			questid = 0;
			validdate = 0;
			ride = 0;
		}

		Data(xbean.TransfromInfo _o1_) {
			if (_o1_ instanceof TransfromInfo) assign((TransfromInfo)_o1_);
			else if (_o1_ instanceof TransfromInfo.Data) assign((TransfromInfo.Data)_o1_);
			else if (_o1_ instanceof TransfromInfo.Const) assign(((TransfromInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TransfromInfo _o_) {
			transformid = _o_.transformid;
			questid = _o_.questid;
			validdate = _o_.validdate;
			ride = _o_.ride;
		}

		private void assign(TransfromInfo.Data _o_) {
			transformid = _o_.transformid;
			questid = _o_.questid;
			validdate = _o_.validdate;
			ride = _o_.ride;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(transformid);
			_os_.marshal(questid);
			_os_.marshal(validdate);
			_os_.marshal(ride);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			transformid = _os_.unmarshal_int();
			questid = _os_.unmarshal_int();
			validdate = _os_.unmarshal_long();
			ride = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.TransfromInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.TransfromInfo toData() {
			return new Data(this);
		}

		public xbean.TransfromInfo toBean() {
			return new TransfromInfo(this, null, null);
		}

		@Override
		public xbean.TransfromInfo toDataIf() {
			return this;
		}

		public xbean.TransfromInfo toBeanIf() {
			return new TransfromInfo(this, null, null);
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
		public int getTransformid() { // 人物变身时的shape
			return transformid;
		}

		@Override
		public int getQuestid() { // 
			return questid;
		}

		@Override
		public long getValiddate() { // 人物变身时的时限
			return validdate;
		}

		@Override
		public int getRide() { // 人物变身时的坐骑
			return ride;
		}

		@Override
		public void setTransformid(int _v_) { // 人物变身时的shape
			transformid = _v_;
		}

		@Override
		public void setQuestid(int _v_) { // 
			questid = _v_;
		}

		@Override
		public void setValiddate(long _v_) { // 人物变身时的时限
			validdate = _v_;
		}

		@Override
		public void setRide(int _v_) { // 人物变身时的坐骑
			ride = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TransfromInfo.Data)) return false;
			TransfromInfo.Data _o_ = (TransfromInfo.Data) _o1_;
			if (transformid != _o_.transformid) return false;
			if (questid != _o_.questid) return false;
			if (validdate != _o_.validdate) return false;
			if (ride != _o_.ride) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += transformid;
			_h_ += questid;
			_h_ += validdate;
			_h_ += ride;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(transformid);
			_sb_.append(",");
			_sb_.append(questid);
			_sb_.append(",");
			_sb_.append(validdate);
			_sb_.append(",");
			_sb_.append(ride);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
