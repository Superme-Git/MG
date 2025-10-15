
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ActivityItemLimit extends mkdb.XBean implements xbean.ActivityItemLimit {
	private java.util.HashMap<Integer, xbean.LimitItemInfo> limititemmap; // key为itemid

	@Override
	public void _reset_unsafe_() {
		limititemmap.clear();
	}

	ActivityItemLimit(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		limititemmap = new java.util.HashMap<Integer, xbean.LimitItemInfo>();
	}

	public ActivityItemLimit() {
		this(0, null, null);
	}

	public ActivityItemLimit(ActivityItemLimit _o_) {
		this(_o_, null, null);
	}

	ActivityItemLimit(xbean.ActivityItemLimit _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ActivityItemLimit) assign((ActivityItemLimit)_o1_);
		else if (_o1_ instanceof ActivityItemLimit.Data) assign((ActivityItemLimit.Data)_o1_);
		else if (_o1_ instanceof ActivityItemLimit.Const) assign(((ActivityItemLimit.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ActivityItemLimit _o_) {
		_o_._xdb_verify_unsafe_();
		limititemmap = new java.util.HashMap<Integer, xbean.LimitItemInfo>();
		for (java.util.Map.Entry<Integer, xbean.LimitItemInfo> _e_ : _o_.limititemmap.entrySet())
			limititemmap.put(_e_.getKey(), new LimitItemInfo(_e_.getValue(), this, "limititemmap"));
	}

	private void assign(ActivityItemLimit.Data _o_) {
		limititemmap = new java.util.HashMap<Integer, xbean.LimitItemInfo>();
		for (java.util.Map.Entry<Integer, xbean.LimitItemInfo> _e_ : _o_.limititemmap.entrySet())
			limititemmap.put(_e_.getKey(), new LimitItemInfo(_e_.getValue(), this, "limititemmap"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(limititemmap.size());
		for (java.util.Map.Entry<Integer, xbean.LimitItemInfo> _e_ : limititemmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				limititemmap = new java.util.HashMap<Integer, xbean.LimitItemInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.LimitItemInfo _v_ = new LimitItemInfo(0, this, "limititemmap");
				_v_.unmarshal(_os_);
				limititemmap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.ActivityItemLimit copy() {
		_xdb_verify_unsafe_();
		return new ActivityItemLimit(this);
	}

	@Override
	public xbean.ActivityItemLimit toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ActivityItemLimit toBean() {
		_xdb_verify_unsafe_();
		return new ActivityItemLimit(this); // same as copy()
	}

	@Override
	public xbean.ActivityItemLimit toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ActivityItemLimit toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.LimitItemInfo> getLimititemmap() { // key为itemid
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "limititemmap"), limititemmap);
	}

	@Override
	public java.util.Map<Integer, xbean.LimitItemInfo> getLimititemmapAsData() { // key为itemid
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.LimitItemInfo> limititemmap;
		ActivityItemLimit _o_ = this;
		limititemmap = new java.util.HashMap<Integer, xbean.LimitItemInfo>();
		for (java.util.Map.Entry<Integer, xbean.LimitItemInfo> _e_ : _o_.limititemmap.entrySet())
			limititemmap.put(_e_.getKey(), new LimitItemInfo.Data(_e_.getValue()));
		return limititemmap;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ActivityItemLimit _o_ = null;
		if ( _o1_ instanceof ActivityItemLimit ) _o_ = (ActivityItemLimit)_o1_;
		else if ( _o1_ instanceof ActivityItemLimit.Const ) _o_ = ((ActivityItemLimit.Const)_o1_).nThis();
		else return false;
		if (!limititemmap.equals(_o_.limititemmap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += limititemmap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(limititemmap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("limititemmap"));
		return lb;
	}

	private class Const implements xbean.ActivityItemLimit {
		ActivityItemLimit nThis() {
			return ActivityItemLimit.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ActivityItemLimit copy() {
			return ActivityItemLimit.this.copy();
		}

		@Override
		public xbean.ActivityItemLimit toData() {
			return ActivityItemLimit.this.toData();
		}

		public xbean.ActivityItemLimit toBean() {
			return ActivityItemLimit.this.toBean();
		}

		@Override
		public xbean.ActivityItemLimit toDataIf() {
			return ActivityItemLimit.this.toDataIf();
		}

		public xbean.ActivityItemLimit toBeanIf() {
			return ActivityItemLimit.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.LimitItemInfo> getLimititemmap() { // key为itemid
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(limititemmap);
		}

		@Override
		public java.util.Map<Integer, xbean.LimitItemInfo> getLimititemmapAsData() { // key为itemid
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.LimitItemInfo> limititemmap;
			ActivityItemLimit _o_ = ActivityItemLimit.this;
			limititemmap = new java.util.HashMap<Integer, xbean.LimitItemInfo>();
			for (java.util.Map.Entry<Integer, xbean.LimitItemInfo> _e_ : _o_.limititemmap.entrySet())
				limititemmap.put(_e_.getKey(), new LimitItemInfo.Data(_e_.getValue()));
			return limititemmap;
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
			return ActivityItemLimit.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ActivityItemLimit.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ActivityItemLimit.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ActivityItemLimit.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ActivityItemLimit.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ActivityItemLimit.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ActivityItemLimit.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ActivityItemLimit.this.hashCode();
		}

		@Override
		public String toString() {
			return ActivityItemLimit.this.toString();
		}

	}

	public static final class Data implements xbean.ActivityItemLimit {
		private java.util.HashMap<Integer, xbean.LimitItemInfo> limititemmap; // key为itemid

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			limititemmap = new java.util.HashMap<Integer, xbean.LimitItemInfo>();
		}

		Data(xbean.ActivityItemLimit _o1_) {
			if (_o1_ instanceof ActivityItemLimit) assign((ActivityItemLimit)_o1_);
			else if (_o1_ instanceof ActivityItemLimit.Data) assign((ActivityItemLimit.Data)_o1_);
			else if (_o1_ instanceof ActivityItemLimit.Const) assign(((ActivityItemLimit.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ActivityItemLimit _o_) {
			limititemmap = new java.util.HashMap<Integer, xbean.LimitItemInfo>();
			for (java.util.Map.Entry<Integer, xbean.LimitItemInfo> _e_ : _o_.limititemmap.entrySet())
				limititemmap.put(_e_.getKey(), new LimitItemInfo.Data(_e_.getValue()));
		}

		private void assign(ActivityItemLimit.Data _o_) {
			limititemmap = new java.util.HashMap<Integer, xbean.LimitItemInfo>();
			for (java.util.Map.Entry<Integer, xbean.LimitItemInfo> _e_ : _o_.limititemmap.entrySet())
				limititemmap.put(_e_.getKey(), new LimitItemInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(limititemmap.size());
			for (java.util.Map.Entry<Integer, xbean.LimitItemInfo> _e_ : limititemmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					limititemmap = new java.util.HashMap<Integer, xbean.LimitItemInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.LimitItemInfo _v_ = xbean.Pod.newLimitItemInfoData();
					_v_.unmarshal(_os_);
					limititemmap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.ActivityItemLimit copy() {
			return new Data(this);
		}

		@Override
		public xbean.ActivityItemLimit toData() {
			return new Data(this);
		}

		public xbean.ActivityItemLimit toBean() {
			return new ActivityItemLimit(this, null, null);
		}

		@Override
		public xbean.ActivityItemLimit toDataIf() {
			return this;
		}

		public xbean.ActivityItemLimit toBeanIf() {
			return new ActivityItemLimit(this, null, null);
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
		public java.util.Map<Integer, xbean.LimitItemInfo> getLimititemmap() { // key为itemid
			return limititemmap;
		}

		@Override
		public java.util.Map<Integer, xbean.LimitItemInfo> getLimititemmapAsData() { // key为itemid
			return limititemmap;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ActivityItemLimit.Data)) return false;
			ActivityItemLimit.Data _o_ = (ActivityItemLimit.Data) _o1_;
			if (!limititemmap.equals(_o_.limititemmap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += limititemmap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(limititemmap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
