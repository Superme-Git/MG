
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceAskInfo extends mkdb.XBean implements xbean.InstanceAskInfo {
	private java.util.HashMap<Long, Integer> answerinfo; // key为角色id,value为回答状态,0为不同意,1为同意
	private int instid; // 副本ID
	private long asktime; // 询问的时间

	@Override
	public void _reset_unsafe_() {
		answerinfo.clear();
		instid = 0;
		asktime = 0L;
	}

	InstanceAskInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		answerinfo = new java.util.HashMap<Long, Integer>();
	}

	public InstanceAskInfo() {
		this(0, null, null);
	}

	public InstanceAskInfo(InstanceAskInfo _o_) {
		this(_o_, null, null);
	}

	InstanceAskInfo(xbean.InstanceAskInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceAskInfo) assign((InstanceAskInfo)_o1_);
		else if (_o1_ instanceof InstanceAskInfo.Data) assign((InstanceAskInfo.Data)_o1_);
		else if (_o1_ instanceof InstanceAskInfo.Const) assign(((InstanceAskInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceAskInfo _o_) {
		_o_._xdb_verify_unsafe_();
		answerinfo = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.answerinfo.entrySet())
			answerinfo.put(_e_.getKey(), _e_.getValue());
		instid = _o_.instid;
		asktime = _o_.asktime;
	}

	private void assign(InstanceAskInfo.Data _o_) {
		answerinfo = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.answerinfo.entrySet())
			answerinfo.put(_e_.getKey(), _e_.getValue());
		instid = _o_.instid;
		asktime = _o_.asktime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(answerinfo.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : answerinfo.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(instid);
		_os_.marshal(asktime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				answerinfo = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				answerinfo.put(_k_, _v_);
			}
		}
		instid = _os_.unmarshal_int();
		asktime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.InstanceAskInfo copy() {
		_xdb_verify_unsafe_();
		return new InstanceAskInfo(this);
	}

	@Override
	public xbean.InstanceAskInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceAskInfo toBean() {
		_xdb_verify_unsafe_();
		return new InstanceAskInfo(this); // same as copy()
	}

	@Override
	public xbean.InstanceAskInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceAskInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, Integer> getAnswerinfo() { // key为角色id,value为回答状态,0为不同意,1为同意
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "answerinfo"), answerinfo);
	}

	@Override
	public java.util.Map<Long, Integer> getAnswerinfoAsData() { // key为角色id,value为回答状态,0为不同意,1为同意
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> answerinfo;
		InstanceAskInfo _o_ = this;
		answerinfo = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.answerinfo.entrySet())
			answerinfo.put(_e_.getKey(), _e_.getValue());
		return answerinfo;
	}

	@Override
	public int getInstid() { // 副本ID
		_xdb_verify_unsafe_();
		return instid;
	}

	@Override
	public long getAsktime() { // 询问的时间
		_xdb_verify_unsafe_();
		return asktime;
	}

	@Override
	public void setInstid(int _v_) { // 副本ID
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "instid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, instid) {
					public void rollback() { instid = _xdb_saved; }
				};}});
		instid = _v_;
	}

	@Override
	public void setAsktime(long _v_) { // 询问的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "asktime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, asktime) {
					public void rollback() { asktime = _xdb_saved; }
				};}});
		asktime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceAskInfo _o_ = null;
		if ( _o1_ instanceof InstanceAskInfo ) _o_ = (InstanceAskInfo)_o1_;
		else if ( _o1_ instanceof InstanceAskInfo.Const ) _o_ = ((InstanceAskInfo.Const)_o1_).nThis();
		else return false;
		if (!answerinfo.equals(_o_.answerinfo)) return false;
		if (instid != _o_.instid) return false;
		if (asktime != _o_.asktime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += answerinfo.hashCode();
		_h_ += instid;
		_h_ += asktime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(answerinfo);
		_sb_.append(",");
		_sb_.append(instid);
		_sb_.append(",");
		_sb_.append(asktime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("answerinfo"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("instid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("asktime"));
		return lb;
	}

	private class Const implements xbean.InstanceAskInfo {
		InstanceAskInfo nThis() {
			return InstanceAskInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceAskInfo copy() {
			return InstanceAskInfo.this.copy();
		}

		@Override
		public xbean.InstanceAskInfo toData() {
			return InstanceAskInfo.this.toData();
		}

		public xbean.InstanceAskInfo toBean() {
			return InstanceAskInfo.this.toBean();
		}

		@Override
		public xbean.InstanceAskInfo toDataIf() {
			return InstanceAskInfo.this.toDataIf();
		}

		public xbean.InstanceAskInfo toBeanIf() {
			return InstanceAskInfo.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, Integer> getAnswerinfo() { // key为角色id,value为回答状态,0为不同意,1为同意
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(answerinfo);
		}

		@Override
		public java.util.Map<Long, Integer> getAnswerinfoAsData() { // key为角色id,value为回答状态,0为不同意,1为同意
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> answerinfo;
			InstanceAskInfo _o_ = InstanceAskInfo.this;
			answerinfo = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.answerinfo.entrySet())
				answerinfo.put(_e_.getKey(), _e_.getValue());
			return answerinfo;
		}

		@Override
		public int getInstid() { // 副本ID
			_xdb_verify_unsafe_();
			return instid;
		}

		@Override
		public long getAsktime() { // 询问的时间
			_xdb_verify_unsafe_();
			return asktime;
		}

		@Override
		public void setInstid(int _v_) { // 副本ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAsktime(long _v_) { // 询问的时间
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
			return InstanceAskInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceAskInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceAskInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceAskInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceAskInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceAskInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceAskInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceAskInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceAskInfo.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceAskInfo {
		private java.util.HashMap<Long, Integer> answerinfo; // key为角色id,value为回答状态,0为不同意,1为同意
		private int instid; // 副本ID
		private long asktime; // 询问的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			answerinfo = new java.util.HashMap<Long, Integer>();
		}

		Data(xbean.InstanceAskInfo _o1_) {
			if (_o1_ instanceof InstanceAskInfo) assign((InstanceAskInfo)_o1_);
			else if (_o1_ instanceof InstanceAskInfo.Data) assign((InstanceAskInfo.Data)_o1_);
			else if (_o1_ instanceof InstanceAskInfo.Const) assign(((InstanceAskInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceAskInfo _o_) {
			answerinfo = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.answerinfo.entrySet())
				answerinfo.put(_e_.getKey(), _e_.getValue());
			instid = _o_.instid;
			asktime = _o_.asktime;
		}

		private void assign(InstanceAskInfo.Data _o_) {
			answerinfo = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.answerinfo.entrySet())
				answerinfo.put(_e_.getKey(), _e_.getValue());
			instid = _o_.instid;
			asktime = _o_.asktime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(answerinfo.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : answerinfo.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(instid);
			_os_.marshal(asktime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					answerinfo = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					answerinfo.put(_k_, _v_);
				}
			}
			instid = _os_.unmarshal_int();
			asktime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.InstanceAskInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceAskInfo toData() {
			return new Data(this);
		}

		public xbean.InstanceAskInfo toBean() {
			return new InstanceAskInfo(this, null, null);
		}

		@Override
		public xbean.InstanceAskInfo toDataIf() {
			return this;
		}

		public xbean.InstanceAskInfo toBeanIf() {
			return new InstanceAskInfo(this, null, null);
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
		public java.util.Map<Long, Integer> getAnswerinfo() { // key为角色id,value为回答状态,0为不同意,1为同意
			return answerinfo;
		}

		@Override
		public java.util.Map<Long, Integer> getAnswerinfoAsData() { // key为角色id,value为回答状态,0为不同意,1为同意
			return answerinfo;
		}

		@Override
		public int getInstid() { // 副本ID
			return instid;
		}

		@Override
		public long getAsktime() { // 询问的时间
			return asktime;
		}

		@Override
		public void setInstid(int _v_) { // 副本ID
			instid = _v_;
		}

		@Override
		public void setAsktime(long _v_) { // 询问的时间
			asktime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceAskInfo.Data)) return false;
			InstanceAskInfo.Data _o_ = (InstanceAskInfo.Data) _o1_;
			if (!answerinfo.equals(_o_.answerinfo)) return false;
			if (instid != _o_.instid) return false;
			if (asktime != _o_.asktime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += answerinfo.hashCode();
			_h_ += instid;
			_h_ += asktime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(answerinfo);
			_sb_.append(",");
			_sb_.append(instid);
			_sb_.append(",");
			_sb_.append(asktime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
