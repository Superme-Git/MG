
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class CircleTaskMap extends mkdb.XBean implements xbean.CircleTaskMap {
	private java.util.HashMap<Integer, xbean.CircleTaskInfo> taskmap; // 

	@Override
	public void _reset_unsafe_() {
		taskmap.clear();
	}

	CircleTaskMap(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		taskmap = new java.util.HashMap<Integer, xbean.CircleTaskInfo>();
	}

	public CircleTaskMap() {
		this(0, null, null);
	}

	public CircleTaskMap(CircleTaskMap _o_) {
		this(_o_, null, null);
	}

	CircleTaskMap(xbean.CircleTaskMap _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof CircleTaskMap) assign((CircleTaskMap)_o1_);
		else if (_o1_ instanceof CircleTaskMap.Data) assign((CircleTaskMap.Data)_o1_);
		else if (_o1_ instanceof CircleTaskMap.Const) assign(((CircleTaskMap.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(CircleTaskMap _o_) {
		_o_._xdb_verify_unsafe_();
		taskmap = new java.util.HashMap<Integer, xbean.CircleTaskInfo>();
		for (java.util.Map.Entry<Integer, xbean.CircleTaskInfo> _e_ : _o_.taskmap.entrySet())
			taskmap.put(_e_.getKey(), new CircleTaskInfo(_e_.getValue(), this, "taskmap"));
	}

	private void assign(CircleTaskMap.Data _o_) {
		taskmap = new java.util.HashMap<Integer, xbean.CircleTaskInfo>();
		for (java.util.Map.Entry<Integer, xbean.CircleTaskInfo> _e_ : _o_.taskmap.entrySet())
			taskmap.put(_e_.getKey(), new CircleTaskInfo(_e_.getValue(), this, "taskmap"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(taskmap.size());
		for (java.util.Map.Entry<Integer, xbean.CircleTaskInfo> _e_ : taskmap.entrySet())
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
				taskmap = new java.util.HashMap<Integer, xbean.CircleTaskInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.CircleTaskInfo _v_ = new CircleTaskInfo(0, this, "taskmap");
				_v_.unmarshal(_os_);
				taskmap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.CircleTaskMap copy() {
		_xdb_verify_unsafe_();
		return new CircleTaskMap(this);
	}

	@Override
	public xbean.CircleTaskMap toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CircleTaskMap toBean() {
		_xdb_verify_unsafe_();
		return new CircleTaskMap(this); // same as copy()
	}

	@Override
	public xbean.CircleTaskMap toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CircleTaskMap toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.CircleTaskInfo> getTaskmap() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "taskmap"), taskmap);
	}

	@Override
	public java.util.Map<Integer, xbean.CircleTaskInfo> getTaskmapAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.CircleTaskInfo> taskmap;
		CircleTaskMap _o_ = this;
		taskmap = new java.util.HashMap<Integer, xbean.CircleTaskInfo>();
		for (java.util.Map.Entry<Integer, xbean.CircleTaskInfo> _e_ : _o_.taskmap.entrySet())
			taskmap.put(_e_.getKey(), new CircleTaskInfo.Data(_e_.getValue()));
		return taskmap;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		CircleTaskMap _o_ = null;
		if ( _o1_ instanceof CircleTaskMap ) _o_ = (CircleTaskMap)_o1_;
		else if ( _o1_ instanceof CircleTaskMap.Const ) _o_ = ((CircleTaskMap.Const)_o1_).nThis();
		else return false;
		if (!taskmap.equals(_o_.taskmap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += taskmap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(taskmap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("taskmap"));
		return lb;
	}

	private class Const implements xbean.CircleTaskMap {
		CircleTaskMap nThis() {
			return CircleTaskMap.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.CircleTaskMap copy() {
			return CircleTaskMap.this.copy();
		}

		@Override
		public xbean.CircleTaskMap toData() {
			return CircleTaskMap.this.toData();
		}

		public xbean.CircleTaskMap toBean() {
			return CircleTaskMap.this.toBean();
		}

		@Override
		public xbean.CircleTaskMap toDataIf() {
			return CircleTaskMap.this.toDataIf();
		}

		public xbean.CircleTaskMap toBeanIf() {
			return CircleTaskMap.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.CircleTaskInfo> getTaskmap() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(taskmap);
		}

		@Override
		public java.util.Map<Integer, xbean.CircleTaskInfo> getTaskmapAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.CircleTaskInfo> taskmap;
			CircleTaskMap _o_ = CircleTaskMap.this;
			taskmap = new java.util.HashMap<Integer, xbean.CircleTaskInfo>();
			for (java.util.Map.Entry<Integer, xbean.CircleTaskInfo> _e_ : _o_.taskmap.entrySet())
				taskmap.put(_e_.getKey(), new CircleTaskInfo.Data(_e_.getValue()));
			return taskmap;
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
			return CircleTaskMap.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return CircleTaskMap.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return CircleTaskMap.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return CircleTaskMap.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return CircleTaskMap.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return CircleTaskMap.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return CircleTaskMap.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return CircleTaskMap.this.hashCode();
		}

		@Override
		public String toString() {
			return CircleTaskMap.this.toString();
		}

	}

	public static final class Data implements xbean.CircleTaskMap {
		private java.util.HashMap<Integer, xbean.CircleTaskInfo> taskmap; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			taskmap = new java.util.HashMap<Integer, xbean.CircleTaskInfo>();
		}

		Data(xbean.CircleTaskMap _o1_) {
			if (_o1_ instanceof CircleTaskMap) assign((CircleTaskMap)_o1_);
			else if (_o1_ instanceof CircleTaskMap.Data) assign((CircleTaskMap.Data)_o1_);
			else if (_o1_ instanceof CircleTaskMap.Const) assign(((CircleTaskMap.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(CircleTaskMap _o_) {
			taskmap = new java.util.HashMap<Integer, xbean.CircleTaskInfo>();
			for (java.util.Map.Entry<Integer, xbean.CircleTaskInfo> _e_ : _o_.taskmap.entrySet())
				taskmap.put(_e_.getKey(), new CircleTaskInfo.Data(_e_.getValue()));
		}

		private void assign(CircleTaskMap.Data _o_) {
			taskmap = new java.util.HashMap<Integer, xbean.CircleTaskInfo>();
			for (java.util.Map.Entry<Integer, xbean.CircleTaskInfo> _e_ : _o_.taskmap.entrySet())
				taskmap.put(_e_.getKey(), new CircleTaskInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(taskmap.size());
			for (java.util.Map.Entry<Integer, xbean.CircleTaskInfo> _e_ : taskmap.entrySet())
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
					taskmap = new java.util.HashMap<Integer, xbean.CircleTaskInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.CircleTaskInfo _v_ = xbean.Pod.newCircleTaskInfoData();
					_v_.unmarshal(_os_);
					taskmap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.CircleTaskMap copy() {
			return new Data(this);
		}

		@Override
		public xbean.CircleTaskMap toData() {
			return new Data(this);
		}

		public xbean.CircleTaskMap toBean() {
			return new CircleTaskMap(this, null, null);
		}

		@Override
		public xbean.CircleTaskMap toDataIf() {
			return this;
		}

		public xbean.CircleTaskMap toBeanIf() {
			return new CircleTaskMap(this, null, null);
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
		public java.util.Map<Integer, xbean.CircleTaskInfo> getTaskmap() { // 
			return taskmap;
		}

		@Override
		public java.util.Map<Integer, xbean.CircleTaskInfo> getTaskmapAsData() { // 
			return taskmap;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof CircleTaskMap.Data)) return false;
			CircleTaskMap.Data _o_ = (CircleTaskMap.Data) _o1_;
			if (!taskmap.equals(_o_.taskmap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += taskmap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(taskmap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
