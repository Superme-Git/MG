
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TaskEventInfo extends mkdb.XBean implements xbean.TaskEventInfo {
	private java.util.HashMap<Integer, Long> eventindexs; // key为taskid,value为eventidx

	@Override
	public void _reset_unsafe_() {
		eventindexs.clear();
	}

	TaskEventInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		eventindexs = new java.util.HashMap<Integer, Long>();
	}

	public TaskEventInfo() {
		this(0, null, null);
	}

	public TaskEventInfo(TaskEventInfo _o_) {
		this(_o_, null, null);
	}

	TaskEventInfo(xbean.TaskEventInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TaskEventInfo) assign((TaskEventInfo)_o1_);
		else if (_o1_ instanceof TaskEventInfo.Data) assign((TaskEventInfo.Data)_o1_);
		else if (_o1_ instanceof TaskEventInfo.Const) assign(((TaskEventInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TaskEventInfo _o_) {
		_o_._xdb_verify_unsafe_();
		eventindexs = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.eventindexs.entrySet())
			eventindexs.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(TaskEventInfo.Data _o_) {
		eventindexs = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.eventindexs.entrySet())
			eventindexs.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(eventindexs.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : eventindexs.entrySet())
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
				eventindexs = new java.util.HashMap<Integer, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				eventindexs.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.TaskEventInfo copy() {
		_xdb_verify_unsafe_();
		return new TaskEventInfo(this);
	}

	@Override
	public xbean.TaskEventInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TaskEventInfo toBean() {
		_xdb_verify_unsafe_();
		return new TaskEventInfo(this); // same as copy()
	}

	@Override
	public xbean.TaskEventInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TaskEventInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Long> getEventindexs() { // key为taskid,value为eventidx
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "eventindexs"), eventindexs);
	}

	@Override
	public java.util.Map<Integer, Long> getEventindexsAsData() { // key为taskid,value为eventidx
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Long> eventindexs;
		TaskEventInfo _o_ = this;
		eventindexs = new java.util.HashMap<Integer, Long>();
		for (java.util.Map.Entry<Integer, Long> _e_ : _o_.eventindexs.entrySet())
			eventindexs.put(_e_.getKey(), _e_.getValue());
		return eventindexs;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TaskEventInfo _o_ = null;
		if ( _o1_ instanceof TaskEventInfo ) _o_ = (TaskEventInfo)_o1_;
		else if ( _o1_ instanceof TaskEventInfo.Const ) _o_ = ((TaskEventInfo.Const)_o1_).nThis();
		else return false;
		if (!eventindexs.equals(_o_.eventindexs)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += eventindexs.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(eventindexs);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("eventindexs"));
		return lb;
	}

	private class Const implements xbean.TaskEventInfo {
		TaskEventInfo nThis() {
			return TaskEventInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TaskEventInfo copy() {
			return TaskEventInfo.this.copy();
		}

		@Override
		public xbean.TaskEventInfo toData() {
			return TaskEventInfo.this.toData();
		}

		public xbean.TaskEventInfo toBean() {
			return TaskEventInfo.this.toBean();
		}

		@Override
		public xbean.TaskEventInfo toDataIf() {
			return TaskEventInfo.this.toDataIf();
		}

		public xbean.TaskEventInfo toBeanIf() {
			return TaskEventInfo.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Long> getEventindexs() { // key为taskid,value为eventidx
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(eventindexs);
		}

		@Override
		public java.util.Map<Integer, Long> getEventindexsAsData() { // key为taskid,value为eventidx
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Long> eventindexs;
			TaskEventInfo _o_ = TaskEventInfo.this;
			eventindexs = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.eventindexs.entrySet())
				eventindexs.put(_e_.getKey(), _e_.getValue());
			return eventindexs;
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
			return TaskEventInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TaskEventInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TaskEventInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TaskEventInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TaskEventInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TaskEventInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TaskEventInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TaskEventInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return TaskEventInfo.this.toString();
		}

	}

	public static final class Data implements xbean.TaskEventInfo {
		private java.util.HashMap<Integer, Long> eventindexs; // key为taskid,value为eventidx

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			eventindexs = new java.util.HashMap<Integer, Long>();
		}

		Data(xbean.TaskEventInfo _o1_) {
			if (_o1_ instanceof TaskEventInfo) assign((TaskEventInfo)_o1_);
			else if (_o1_ instanceof TaskEventInfo.Data) assign((TaskEventInfo.Data)_o1_);
			else if (_o1_ instanceof TaskEventInfo.Const) assign(((TaskEventInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TaskEventInfo _o_) {
			eventindexs = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.eventindexs.entrySet())
				eventindexs.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(TaskEventInfo.Data _o_) {
			eventindexs = new java.util.HashMap<Integer, Long>();
			for (java.util.Map.Entry<Integer, Long> _e_ : _o_.eventindexs.entrySet())
				eventindexs.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(eventindexs.size());
			for (java.util.Map.Entry<Integer, Long> _e_ : eventindexs.entrySet())
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
					eventindexs = new java.util.HashMap<Integer, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					eventindexs.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.TaskEventInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.TaskEventInfo toData() {
			return new Data(this);
		}

		public xbean.TaskEventInfo toBean() {
			return new TaskEventInfo(this, null, null);
		}

		@Override
		public xbean.TaskEventInfo toDataIf() {
			return this;
		}

		public xbean.TaskEventInfo toBeanIf() {
			return new TaskEventInfo(this, null, null);
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
		public java.util.Map<Integer, Long> getEventindexs() { // key为taskid,value为eventidx
			return eventindexs;
		}

		@Override
		public java.util.Map<Integer, Long> getEventindexsAsData() { // key为taskid,value为eventidx
			return eventindexs;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TaskEventInfo.Data)) return false;
			TaskEventInfo.Data _o_ = (TaskEventInfo.Data) _o1_;
			if (!eventindexs.equals(_o_.eventindexs)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += eventindexs.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(eventindexs);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
