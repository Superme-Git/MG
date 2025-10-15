
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceTimeAwardInfo extends mkdb.XBean implements xbean.InstanceTimeAwardInfo {
	private java.util.HashMap<Integer, Integer> stepawardtimes; // key stepId, value 该step给的奖励次数

	@Override
	public void _reset_unsafe_() {
		stepawardtimes.clear();
	}

	InstanceTimeAwardInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		stepawardtimes = new java.util.HashMap<Integer, Integer>();
	}

	public InstanceTimeAwardInfo() {
		this(0, null, null);
	}

	public InstanceTimeAwardInfo(InstanceTimeAwardInfo _o_) {
		this(_o_, null, null);
	}

	InstanceTimeAwardInfo(xbean.InstanceTimeAwardInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceTimeAwardInfo) assign((InstanceTimeAwardInfo)_o1_);
		else if (_o1_ instanceof InstanceTimeAwardInfo.Data) assign((InstanceTimeAwardInfo.Data)_o1_);
		else if (_o1_ instanceof InstanceTimeAwardInfo.Const) assign(((InstanceTimeAwardInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceTimeAwardInfo _o_) {
		_o_._xdb_verify_unsafe_();
		stepawardtimes = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stepawardtimes.entrySet())
			stepawardtimes.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(InstanceTimeAwardInfo.Data _o_) {
		stepawardtimes = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stepawardtimes.entrySet())
			stepawardtimes.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(stepawardtimes.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : stepawardtimes.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				stepawardtimes = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				stepawardtimes.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.InstanceTimeAwardInfo copy() {
		_xdb_verify_unsafe_();
		return new InstanceTimeAwardInfo(this);
	}

	@Override
	public xbean.InstanceTimeAwardInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceTimeAwardInfo toBean() {
		_xdb_verify_unsafe_();
		return new InstanceTimeAwardInfo(this); // same as copy()
	}

	@Override
	public xbean.InstanceTimeAwardInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceTimeAwardInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Integer> getStepawardtimes() { // key stepId, value 该step给的奖励次数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "stepawardtimes"), stepawardtimes);
	}

	@Override
	public java.util.Map<Integer, Integer> getStepawardtimesAsData() { // key stepId, value 该step给的奖励次数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> stepawardtimes;
		InstanceTimeAwardInfo _o_ = this;
		stepawardtimes = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stepawardtimes.entrySet())
			stepawardtimes.put(_e_.getKey(), _e_.getValue());
		return stepawardtimes;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceTimeAwardInfo _o_ = null;
		if ( _o1_ instanceof InstanceTimeAwardInfo ) _o_ = (InstanceTimeAwardInfo)_o1_;
		else if ( _o1_ instanceof InstanceTimeAwardInfo.Const ) _o_ = ((InstanceTimeAwardInfo.Const)_o1_).nThis();
		else return false;
		if (!stepawardtimes.equals(_o_.stepawardtimes)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += stepawardtimes.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(stepawardtimes);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("stepawardtimes"));
		return lb;
	}

	private class Const implements xbean.InstanceTimeAwardInfo {
		InstanceTimeAwardInfo nThis() {
			return InstanceTimeAwardInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceTimeAwardInfo copy() {
			return InstanceTimeAwardInfo.this.copy();
		}

		@Override
		public xbean.InstanceTimeAwardInfo toData() {
			return InstanceTimeAwardInfo.this.toData();
		}

		public xbean.InstanceTimeAwardInfo toBean() {
			return InstanceTimeAwardInfo.this.toBean();
		}

		@Override
		public xbean.InstanceTimeAwardInfo toDataIf() {
			return InstanceTimeAwardInfo.this.toDataIf();
		}

		public xbean.InstanceTimeAwardInfo toBeanIf() {
			return InstanceTimeAwardInfo.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Integer> getStepawardtimes() { // key stepId, value 该step给的奖励次数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(stepawardtimes);
		}

		@Override
		public java.util.Map<Integer, Integer> getStepawardtimesAsData() { // key stepId, value 该step给的奖励次数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> stepawardtimes;
			InstanceTimeAwardInfo _o_ = InstanceTimeAwardInfo.this;
			stepawardtimes = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stepawardtimes.entrySet())
				stepawardtimes.put(_e_.getKey(), _e_.getValue());
			return stepawardtimes;
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
			return InstanceTimeAwardInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceTimeAwardInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceTimeAwardInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceTimeAwardInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceTimeAwardInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceTimeAwardInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceTimeAwardInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceTimeAwardInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceTimeAwardInfo.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceTimeAwardInfo {
		private java.util.HashMap<Integer, Integer> stepawardtimes; // key stepId, value 该step给的奖励次数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			stepawardtimes = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.InstanceTimeAwardInfo _o1_) {
			if (_o1_ instanceof InstanceTimeAwardInfo) assign((InstanceTimeAwardInfo)_o1_);
			else if (_o1_ instanceof InstanceTimeAwardInfo.Data) assign((InstanceTimeAwardInfo.Data)_o1_);
			else if (_o1_ instanceof InstanceTimeAwardInfo.Const) assign(((InstanceTimeAwardInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceTimeAwardInfo _o_) {
			stepawardtimes = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stepawardtimes.entrySet())
				stepawardtimes.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(InstanceTimeAwardInfo.Data _o_) {
			stepawardtimes = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.stepawardtimes.entrySet())
				stepawardtimes.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(stepawardtimes.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : stepawardtimes.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					stepawardtimes = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					stepawardtimes.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.InstanceTimeAwardInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceTimeAwardInfo toData() {
			return new Data(this);
		}

		public xbean.InstanceTimeAwardInfo toBean() {
			return new InstanceTimeAwardInfo(this, null, null);
		}

		@Override
		public xbean.InstanceTimeAwardInfo toDataIf() {
			return this;
		}

		public xbean.InstanceTimeAwardInfo toBeanIf() {
			return new InstanceTimeAwardInfo(this, null, null);
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
		public java.util.Map<Integer, Integer> getStepawardtimes() { // key stepId, value 该step给的奖励次数
			return stepawardtimes;
		}

		@Override
		public java.util.Map<Integer, Integer> getStepawardtimesAsData() { // key stepId, value 该step给的奖励次数
			return stepawardtimes;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceTimeAwardInfo.Data)) return false;
			InstanceTimeAwardInfo.Data _o_ = (InstanceTimeAwardInfo.Data) _o1_;
			if (!stepawardtimes.equals(_o_.stepawardtimes)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += stepawardtimes.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(stepawardtimes);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
