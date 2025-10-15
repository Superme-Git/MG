
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class CircleTaskCompleteTimes extends mkdb.XBean implements xbean.CircleTaskCompleteTimes {
	private java.util.HashMap<Integer, Integer> circletaskcounts; // key 为循环类型
	private long logtime; // 

	@Override
	public void _reset_unsafe_() {
		circletaskcounts.clear();
		logtime = 0L;
	}

	CircleTaskCompleteTimes(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		circletaskcounts = new java.util.HashMap<Integer, Integer>();
	}

	public CircleTaskCompleteTimes() {
		this(0, null, null);
	}

	public CircleTaskCompleteTimes(CircleTaskCompleteTimes _o_) {
		this(_o_, null, null);
	}

	CircleTaskCompleteTimes(xbean.CircleTaskCompleteTimes _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof CircleTaskCompleteTimes) assign((CircleTaskCompleteTimes)_o1_);
		else if (_o1_ instanceof CircleTaskCompleteTimes.Data) assign((CircleTaskCompleteTimes.Data)_o1_);
		else if (_o1_ instanceof CircleTaskCompleteTimes.Const) assign(((CircleTaskCompleteTimes.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(CircleTaskCompleteTimes _o_) {
		_o_._xdb_verify_unsafe_();
		circletaskcounts = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.circletaskcounts.entrySet())
			circletaskcounts.put(_e_.getKey(), _e_.getValue());
		logtime = _o_.logtime;
	}

	private void assign(CircleTaskCompleteTimes.Data _o_) {
		circletaskcounts = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.circletaskcounts.entrySet())
			circletaskcounts.put(_e_.getKey(), _e_.getValue());
		logtime = _o_.logtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(circletaskcounts.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : circletaskcounts.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(logtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				circletaskcounts = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				circletaskcounts.put(_k_, _v_);
			}
		}
		logtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.CircleTaskCompleteTimes copy() {
		_xdb_verify_unsafe_();
		return new CircleTaskCompleteTimes(this);
	}

	@Override
	public xbean.CircleTaskCompleteTimes toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CircleTaskCompleteTimes toBean() {
		_xdb_verify_unsafe_();
		return new CircleTaskCompleteTimes(this); // same as copy()
	}

	@Override
	public xbean.CircleTaskCompleteTimes toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CircleTaskCompleteTimes toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Integer> getCircletaskcounts() { // key 为循环类型
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "circletaskcounts"), circletaskcounts);
	}

	@Override
	public java.util.Map<Integer, Integer> getCircletaskcountsAsData() { // key 为循环类型
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> circletaskcounts;
		CircleTaskCompleteTimes _o_ = this;
		circletaskcounts = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.circletaskcounts.entrySet())
			circletaskcounts.put(_e_.getKey(), _e_.getValue());
		return circletaskcounts;
	}

	@Override
	public long getLogtime() { // 
		_xdb_verify_unsafe_();
		return logtime;
	}

	@Override
	public void setLogtime(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "logtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, logtime) {
					public void rollback() { logtime = _xdb_saved; }
				};}});
		logtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		CircleTaskCompleteTimes _o_ = null;
		if ( _o1_ instanceof CircleTaskCompleteTimes ) _o_ = (CircleTaskCompleteTimes)_o1_;
		else if ( _o1_ instanceof CircleTaskCompleteTimes.Const ) _o_ = ((CircleTaskCompleteTimes.Const)_o1_).nThis();
		else return false;
		if (!circletaskcounts.equals(_o_.circletaskcounts)) return false;
		if (logtime != _o_.logtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += circletaskcounts.hashCode();
		_h_ += logtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(circletaskcounts);
		_sb_.append(",");
		_sb_.append(logtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("circletaskcounts"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("logtime"));
		return lb;
	}

	private class Const implements xbean.CircleTaskCompleteTimes {
		CircleTaskCompleteTimes nThis() {
			return CircleTaskCompleteTimes.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.CircleTaskCompleteTimes copy() {
			return CircleTaskCompleteTimes.this.copy();
		}

		@Override
		public xbean.CircleTaskCompleteTimes toData() {
			return CircleTaskCompleteTimes.this.toData();
		}

		public xbean.CircleTaskCompleteTimes toBean() {
			return CircleTaskCompleteTimes.this.toBean();
		}

		@Override
		public xbean.CircleTaskCompleteTimes toDataIf() {
			return CircleTaskCompleteTimes.this.toDataIf();
		}

		public xbean.CircleTaskCompleteTimes toBeanIf() {
			return CircleTaskCompleteTimes.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Integer> getCircletaskcounts() { // key 为循环类型
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(circletaskcounts);
		}

		@Override
		public java.util.Map<Integer, Integer> getCircletaskcountsAsData() { // key 为循环类型
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> circletaskcounts;
			CircleTaskCompleteTimes _o_ = CircleTaskCompleteTimes.this;
			circletaskcounts = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.circletaskcounts.entrySet())
				circletaskcounts.put(_e_.getKey(), _e_.getValue());
			return circletaskcounts;
		}

		@Override
		public long getLogtime() { // 
			_xdb_verify_unsafe_();
			return logtime;
		}

		@Override
		public void setLogtime(long _v_) { // 
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
			return CircleTaskCompleteTimes.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return CircleTaskCompleteTimes.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return CircleTaskCompleteTimes.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return CircleTaskCompleteTimes.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return CircleTaskCompleteTimes.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return CircleTaskCompleteTimes.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return CircleTaskCompleteTimes.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return CircleTaskCompleteTimes.this.hashCode();
		}

		@Override
		public String toString() {
			return CircleTaskCompleteTimes.this.toString();
		}

	}

	public static final class Data implements xbean.CircleTaskCompleteTimes {
		private java.util.HashMap<Integer, Integer> circletaskcounts; // key 为循环类型
		private long logtime; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			circletaskcounts = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.CircleTaskCompleteTimes _o1_) {
			if (_o1_ instanceof CircleTaskCompleteTimes) assign((CircleTaskCompleteTimes)_o1_);
			else if (_o1_ instanceof CircleTaskCompleteTimes.Data) assign((CircleTaskCompleteTimes.Data)_o1_);
			else if (_o1_ instanceof CircleTaskCompleteTimes.Const) assign(((CircleTaskCompleteTimes.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(CircleTaskCompleteTimes _o_) {
			circletaskcounts = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.circletaskcounts.entrySet())
				circletaskcounts.put(_e_.getKey(), _e_.getValue());
			logtime = _o_.logtime;
		}

		private void assign(CircleTaskCompleteTimes.Data _o_) {
			circletaskcounts = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.circletaskcounts.entrySet())
				circletaskcounts.put(_e_.getKey(), _e_.getValue());
			logtime = _o_.logtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(circletaskcounts.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : circletaskcounts.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(logtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					circletaskcounts = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					circletaskcounts.put(_k_, _v_);
				}
			}
			logtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.CircleTaskCompleteTimes copy() {
			return new Data(this);
		}

		@Override
		public xbean.CircleTaskCompleteTimes toData() {
			return new Data(this);
		}

		public xbean.CircleTaskCompleteTimes toBean() {
			return new CircleTaskCompleteTimes(this, null, null);
		}

		@Override
		public xbean.CircleTaskCompleteTimes toDataIf() {
			return this;
		}

		public xbean.CircleTaskCompleteTimes toBeanIf() {
			return new CircleTaskCompleteTimes(this, null, null);
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
		public java.util.Map<Integer, Integer> getCircletaskcounts() { // key 为循环类型
			return circletaskcounts;
		}

		@Override
		public java.util.Map<Integer, Integer> getCircletaskcountsAsData() { // key 为循环类型
			return circletaskcounts;
		}

		@Override
		public long getLogtime() { // 
			return logtime;
		}

		@Override
		public void setLogtime(long _v_) { // 
			logtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof CircleTaskCompleteTimes.Data)) return false;
			CircleTaskCompleteTimes.Data _o_ = (CircleTaskCompleteTimes.Data) _o1_;
			if (!circletaskcounts.equals(_o_.circletaskcounts)) return false;
			if (logtime != _o_.logtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += circletaskcounts.hashCode();
			_h_ += logtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(circletaskcounts);
			_sb_.append(",");
			_sb_.append(logtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
