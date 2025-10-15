
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceInfoCol extends mkdb.XBean implements xbean.InstanceInfoCol {
	private int lastinstanceid; // 最后进入的一个副本
	private int counts; // 今天做了多少次副本
	private long accepttime; // 最后接副本时间
	private java.util.HashMap<Integer, xbean.InstanceTaskInfo> instinfo; // key 副本id, value InstanceTaskInfo
	private java.util.HashMap<Integer, xbean.InstanceTimeAwardInfo> instcount; // 今天进入的副本id和次数
	private int fanpai; // 当前副本是否需要翻牌 0 不需要, 1 需要
	private int reset; // 当前副本是否重置过 0 没有重置过, 1 重置过

	@Override
	public void _reset_unsafe_() {
		lastinstanceid = 0;
		counts = 0;
		accepttime = 0L;
		instinfo.clear();
		instcount.clear();
		fanpai = 0;
		reset = 0;
	}

	InstanceInfoCol(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		instinfo = new java.util.HashMap<Integer, xbean.InstanceTaskInfo>();
		instcount = new java.util.HashMap<Integer, xbean.InstanceTimeAwardInfo>();
		fanpai = 0;
		reset = 0;
	}

	public InstanceInfoCol() {
		this(0, null, null);
	}

	public InstanceInfoCol(InstanceInfoCol _o_) {
		this(_o_, null, null);
	}

	InstanceInfoCol(xbean.InstanceInfoCol _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceInfoCol) assign((InstanceInfoCol)_o1_);
		else if (_o1_ instanceof InstanceInfoCol.Data) assign((InstanceInfoCol.Data)_o1_);
		else if (_o1_ instanceof InstanceInfoCol.Const) assign(((InstanceInfoCol.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceInfoCol _o_) {
		_o_._xdb_verify_unsafe_();
		lastinstanceid = _o_.lastinstanceid;
		counts = _o_.counts;
		accepttime = _o_.accepttime;
		instinfo = new java.util.HashMap<Integer, xbean.InstanceTaskInfo>();
		for (java.util.Map.Entry<Integer, xbean.InstanceTaskInfo> _e_ : _o_.instinfo.entrySet())
			instinfo.put(_e_.getKey(), new InstanceTaskInfo(_e_.getValue(), this, "instinfo"));
		instcount = new java.util.HashMap<Integer, xbean.InstanceTimeAwardInfo>();
		for (java.util.Map.Entry<Integer, xbean.InstanceTimeAwardInfo> _e_ : _o_.instcount.entrySet())
			instcount.put(_e_.getKey(), new InstanceTimeAwardInfo(_e_.getValue(), this, "instcount"));
		fanpai = _o_.fanpai;
		reset = _o_.reset;
	}

	private void assign(InstanceInfoCol.Data _o_) {
		lastinstanceid = _o_.lastinstanceid;
		counts = _o_.counts;
		accepttime = _o_.accepttime;
		instinfo = new java.util.HashMap<Integer, xbean.InstanceTaskInfo>();
		for (java.util.Map.Entry<Integer, xbean.InstanceTaskInfo> _e_ : _o_.instinfo.entrySet())
			instinfo.put(_e_.getKey(), new InstanceTaskInfo(_e_.getValue(), this, "instinfo"));
		instcount = new java.util.HashMap<Integer, xbean.InstanceTimeAwardInfo>();
		for (java.util.Map.Entry<Integer, xbean.InstanceTimeAwardInfo> _e_ : _o_.instcount.entrySet())
			instcount.put(_e_.getKey(), new InstanceTimeAwardInfo(_e_.getValue(), this, "instcount"));
		fanpai = _o_.fanpai;
		reset = _o_.reset;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(lastinstanceid);
		_os_.marshal(counts);
		_os_.marshal(accepttime);
		_os_.compact_uint32(instinfo.size());
		for (java.util.Map.Entry<Integer, xbean.InstanceTaskInfo> _e_ : instinfo.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(instcount.size());
		for (java.util.Map.Entry<Integer, xbean.InstanceTimeAwardInfo> _e_ : instcount.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(fanpai);
		_os_.marshal(reset);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		lastinstanceid = _os_.unmarshal_int();
		counts = _os_.unmarshal_int();
		accepttime = _os_.unmarshal_long();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				instinfo = new java.util.HashMap<Integer, xbean.InstanceTaskInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.InstanceTaskInfo _v_ = new InstanceTaskInfo(0, this, "instinfo");
				_v_.unmarshal(_os_);
				instinfo.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				instcount = new java.util.HashMap<Integer, xbean.InstanceTimeAwardInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.InstanceTimeAwardInfo _v_ = new InstanceTimeAwardInfo(0, this, "instcount");
				_v_.unmarshal(_os_);
				instcount.put(_k_, _v_);
			}
		}
		fanpai = _os_.unmarshal_int();
		reset = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.InstanceInfoCol copy() {
		_xdb_verify_unsafe_();
		return new InstanceInfoCol(this);
	}

	@Override
	public xbean.InstanceInfoCol toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceInfoCol toBean() {
		_xdb_verify_unsafe_();
		return new InstanceInfoCol(this); // same as copy()
	}

	@Override
	public xbean.InstanceInfoCol toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceInfoCol toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getLastinstanceid() { // 最后进入的一个副本
		_xdb_verify_unsafe_();
		return lastinstanceid;
	}

	@Override
	public int getCounts() { // 今天做了多少次副本
		_xdb_verify_unsafe_();
		return counts;
	}

	@Override
	public long getAccepttime() { // 最后接副本时间
		_xdb_verify_unsafe_();
		return accepttime;
	}

	@Override
	public java.util.Map<Integer, xbean.InstanceTaskInfo> getInstinfo() { // key 副本id, value InstanceTaskInfo
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "instinfo"), instinfo);
	}

	@Override
	public java.util.Map<Integer, xbean.InstanceTaskInfo> getInstinfoAsData() { // key 副本id, value InstanceTaskInfo
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.InstanceTaskInfo> instinfo;
		InstanceInfoCol _o_ = this;
		instinfo = new java.util.HashMap<Integer, xbean.InstanceTaskInfo>();
		for (java.util.Map.Entry<Integer, xbean.InstanceTaskInfo> _e_ : _o_.instinfo.entrySet())
			instinfo.put(_e_.getKey(), new InstanceTaskInfo.Data(_e_.getValue()));
		return instinfo;
	}

	@Override
	public java.util.Map<Integer, xbean.InstanceTimeAwardInfo> getInstcount() { // 今天进入的副本id和次数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "instcount"), instcount);
	}

	@Override
	public java.util.Map<Integer, xbean.InstanceTimeAwardInfo> getInstcountAsData() { // 今天进入的副本id和次数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.InstanceTimeAwardInfo> instcount;
		InstanceInfoCol _o_ = this;
		instcount = new java.util.HashMap<Integer, xbean.InstanceTimeAwardInfo>();
		for (java.util.Map.Entry<Integer, xbean.InstanceTimeAwardInfo> _e_ : _o_.instcount.entrySet())
			instcount.put(_e_.getKey(), new InstanceTimeAwardInfo.Data(_e_.getValue()));
		return instcount;
	}

	@Override
	public int getFanpai() { // 当前副本是否需要翻牌 0 不需要, 1 需要
		_xdb_verify_unsafe_();
		return fanpai;
	}

	@Override
	public int getReset() { // 当前副本是否重置过 0 没有重置过, 1 重置过
		_xdb_verify_unsafe_();
		return reset;
	}

	@Override
	public void setLastinstanceid(int _v_) { // 最后进入的一个副本
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastinstanceid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lastinstanceid) {
					public void rollback() { lastinstanceid = _xdb_saved; }
				};}});
		lastinstanceid = _v_;
	}

	@Override
	public void setCounts(int _v_) { // 今天做了多少次副本
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "counts") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, counts) {
					public void rollback() { counts = _xdb_saved; }
				};}});
		counts = _v_;
	}

	@Override
	public void setAccepttime(long _v_) { // 最后接副本时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "accepttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, accepttime) {
					public void rollback() { accepttime = _xdb_saved; }
				};}});
		accepttime = _v_;
	}

	@Override
	public void setFanpai(int _v_) { // 当前副本是否需要翻牌 0 不需要, 1 需要
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fanpai") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, fanpai) {
					public void rollback() { fanpai = _xdb_saved; }
				};}});
		fanpai = _v_;
	}

	@Override
	public void setReset(int _v_) { // 当前副本是否重置过 0 没有重置过, 1 重置过
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "reset") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, reset) {
					public void rollback() { reset = _xdb_saved; }
				};}});
		reset = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceInfoCol _o_ = null;
		if ( _o1_ instanceof InstanceInfoCol ) _o_ = (InstanceInfoCol)_o1_;
		else if ( _o1_ instanceof InstanceInfoCol.Const ) _o_ = ((InstanceInfoCol.Const)_o1_).nThis();
		else return false;
		if (lastinstanceid != _o_.lastinstanceid) return false;
		if (counts != _o_.counts) return false;
		if (accepttime != _o_.accepttime) return false;
		if (!instinfo.equals(_o_.instinfo)) return false;
		if (!instcount.equals(_o_.instcount)) return false;
		if (fanpai != _o_.fanpai) return false;
		if (reset != _o_.reset) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += lastinstanceid;
		_h_ += counts;
		_h_ += accepttime;
		_h_ += instinfo.hashCode();
		_h_ += instcount.hashCode();
		_h_ += fanpai;
		_h_ += reset;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(lastinstanceid);
		_sb_.append(",");
		_sb_.append(counts);
		_sb_.append(",");
		_sb_.append(accepttime);
		_sb_.append(",");
		_sb_.append(instinfo);
		_sb_.append(",");
		_sb_.append(instcount);
		_sb_.append(",");
		_sb_.append(fanpai);
		_sb_.append(",");
		_sb_.append(reset);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastinstanceid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("counts"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("accepttime"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("instinfo"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("instcount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fanpai"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("reset"));
		return lb;
	}

	private class Const implements xbean.InstanceInfoCol {
		InstanceInfoCol nThis() {
			return InstanceInfoCol.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceInfoCol copy() {
			return InstanceInfoCol.this.copy();
		}

		@Override
		public xbean.InstanceInfoCol toData() {
			return InstanceInfoCol.this.toData();
		}

		public xbean.InstanceInfoCol toBean() {
			return InstanceInfoCol.this.toBean();
		}

		@Override
		public xbean.InstanceInfoCol toDataIf() {
			return InstanceInfoCol.this.toDataIf();
		}

		public xbean.InstanceInfoCol toBeanIf() {
			return InstanceInfoCol.this.toBeanIf();
		}

		@Override
		public int getLastinstanceid() { // 最后进入的一个副本
			_xdb_verify_unsafe_();
			return lastinstanceid;
		}

		@Override
		public int getCounts() { // 今天做了多少次副本
			_xdb_verify_unsafe_();
			return counts;
		}

		@Override
		public long getAccepttime() { // 最后接副本时间
			_xdb_verify_unsafe_();
			return accepttime;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceTaskInfo> getInstinfo() { // key 副本id, value InstanceTaskInfo
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(instinfo);
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceTaskInfo> getInstinfoAsData() { // key 副本id, value InstanceTaskInfo
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.InstanceTaskInfo> instinfo;
			InstanceInfoCol _o_ = InstanceInfoCol.this;
			instinfo = new java.util.HashMap<Integer, xbean.InstanceTaskInfo>();
			for (java.util.Map.Entry<Integer, xbean.InstanceTaskInfo> _e_ : _o_.instinfo.entrySet())
				instinfo.put(_e_.getKey(), new InstanceTaskInfo.Data(_e_.getValue()));
			return instinfo;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceTimeAwardInfo> getInstcount() { // 今天进入的副本id和次数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(instcount);
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceTimeAwardInfo> getInstcountAsData() { // 今天进入的副本id和次数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.InstanceTimeAwardInfo> instcount;
			InstanceInfoCol _o_ = InstanceInfoCol.this;
			instcount = new java.util.HashMap<Integer, xbean.InstanceTimeAwardInfo>();
			for (java.util.Map.Entry<Integer, xbean.InstanceTimeAwardInfo> _e_ : _o_.instcount.entrySet())
				instcount.put(_e_.getKey(), new InstanceTimeAwardInfo.Data(_e_.getValue()));
			return instcount;
		}

		@Override
		public int getFanpai() { // 当前副本是否需要翻牌 0 不需要, 1 需要
			_xdb_verify_unsafe_();
			return fanpai;
		}

		@Override
		public int getReset() { // 当前副本是否重置过 0 没有重置过, 1 重置过
			_xdb_verify_unsafe_();
			return reset;
		}

		@Override
		public void setLastinstanceid(int _v_) { // 最后进入的一个副本
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCounts(int _v_) { // 今天做了多少次副本
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAccepttime(long _v_) { // 最后接副本时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFanpai(int _v_) { // 当前副本是否需要翻牌 0 不需要, 1 需要
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReset(int _v_) { // 当前副本是否重置过 0 没有重置过, 1 重置过
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
			return InstanceInfoCol.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceInfoCol.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceInfoCol.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceInfoCol.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceInfoCol.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceInfoCol.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceInfoCol.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceInfoCol.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceInfoCol.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceInfoCol {
		private int lastinstanceid; // 最后进入的一个副本
		private int counts; // 今天做了多少次副本
		private long accepttime; // 最后接副本时间
		private java.util.HashMap<Integer, xbean.InstanceTaskInfo> instinfo; // key 副本id, value InstanceTaskInfo
		private java.util.HashMap<Integer, xbean.InstanceTimeAwardInfo> instcount; // 今天进入的副本id和次数
		private int fanpai; // 当前副本是否需要翻牌 0 不需要, 1 需要
		private int reset; // 当前副本是否重置过 0 没有重置过, 1 重置过

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			instinfo = new java.util.HashMap<Integer, xbean.InstanceTaskInfo>();
			instcount = new java.util.HashMap<Integer, xbean.InstanceTimeAwardInfo>();
			fanpai = 0;
			reset = 0;
		}

		Data(xbean.InstanceInfoCol _o1_) {
			if (_o1_ instanceof InstanceInfoCol) assign((InstanceInfoCol)_o1_);
			else if (_o1_ instanceof InstanceInfoCol.Data) assign((InstanceInfoCol.Data)_o1_);
			else if (_o1_ instanceof InstanceInfoCol.Const) assign(((InstanceInfoCol.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceInfoCol _o_) {
			lastinstanceid = _o_.lastinstanceid;
			counts = _o_.counts;
			accepttime = _o_.accepttime;
			instinfo = new java.util.HashMap<Integer, xbean.InstanceTaskInfo>();
			for (java.util.Map.Entry<Integer, xbean.InstanceTaskInfo> _e_ : _o_.instinfo.entrySet())
				instinfo.put(_e_.getKey(), new InstanceTaskInfo.Data(_e_.getValue()));
			instcount = new java.util.HashMap<Integer, xbean.InstanceTimeAwardInfo>();
			for (java.util.Map.Entry<Integer, xbean.InstanceTimeAwardInfo> _e_ : _o_.instcount.entrySet())
				instcount.put(_e_.getKey(), new InstanceTimeAwardInfo.Data(_e_.getValue()));
			fanpai = _o_.fanpai;
			reset = _o_.reset;
		}

		private void assign(InstanceInfoCol.Data _o_) {
			lastinstanceid = _o_.lastinstanceid;
			counts = _o_.counts;
			accepttime = _o_.accepttime;
			instinfo = new java.util.HashMap<Integer, xbean.InstanceTaskInfo>();
			for (java.util.Map.Entry<Integer, xbean.InstanceTaskInfo> _e_ : _o_.instinfo.entrySet())
				instinfo.put(_e_.getKey(), new InstanceTaskInfo.Data(_e_.getValue()));
			instcount = new java.util.HashMap<Integer, xbean.InstanceTimeAwardInfo>();
			for (java.util.Map.Entry<Integer, xbean.InstanceTimeAwardInfo> _e_ : _o_.instcount.entrySet())
				instcount.put(_e_.getKey(), new InstanceTimeAwardInfo.Data(_e_.getValue()));
			fanpai = _o_.fanpai;
			reset = _o_.reset;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(lastinstanceid);
			_os_.marshal(counts);
			_os_.marshal(accepttime);
			_os_.compact_uint32(instinfo.size());
			for (java.util.Map.Entry<Integer, xbean.InstanceTaskInfo> _e_ : instinfo.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(instcount.size());
			for (java.util.Map.Entry<Integer, xbean.InstanceTimeAwardInfo> _e_ : instcount.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(fanpai);
			_os_.marshal(reset);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			lastinstanceid = _os_.unmarshal_int();
			counts = _os_.unmarshal_int();
			accepttime = _os_.unmarshal_long();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					instinfo = new java.util.HashMap<Integer, xbean.InstanceTaskInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.InstanceTaskInfo _v_ = xbean.Pod.newInstanceTaskInfoData();
					_v_.unmarshal(_os_);
					instinfo.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					instcount = new java.util.HashMap<Integer, xbean.InstanceTimeAwardInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.InstanceTimeAwardInfo _v_ = xbean.Pod.newInstanceTimeAwardInfoData();
					_v_.unmarshal(_os_);
					instcount.put(_k_, _v_);
				}
			}
			fanpai = _os_.unmarshal_int();
			reset = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.InstanceInfoCol copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceInfoCol toData() {
			return new Data(this);
		}

		public xbean.InstanceInfoCol toBean() {
			return new InstanceInfoCol(this, null, null);
		}

		@Override
		public xbean.InstanceInfoCol toDataIf() {
			return this;
		}

		public xbean.InstanceInfoCol toBeanIf() {
			return new InstanceInfoCol(this, null, null);
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
		public int getLastinstanceid() { // 最后进入的一个副本
			return lastinstanceid;
		}

		@Override
		public int getCounts() { // 今天做了多少次副本
			return counts;
		}

		@Override
		public long getAccepttime() { // 最后接副本时间
			return accepttime;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceTaskInfo> getInstinfo() { // key 副本id, value InstanceTaskInfo
			return instinfo;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceTaskInfo> getInstinfoAsData() { // key 副本id, value InstanceTaskInfo
			return instinfo;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceTimeAwardInfo> getInstcount() { // 今天进入的副本id和次数
			return instcount;
		}

		@Override
		public java.util.Map<Integer, xbean.InstanceTimeAwardInfo> getInstcountAsData() { // 今天进入的副本id和次数
			return instcount;
		}

		@Override
		public int getFanpai() { // 当前副本是否需要翻牌 0 不需要, 1 需要
			return fanpai;
		}

		@Override
		public int getReset() { // 当前副本是否重置过 0 没有重置过, 1 重置过
			return reset;
		}

		@Override
		public void setLastinstanceid(int _v_) { // 最后进入的一个副本
			lastinstanceid = _v_;
		}

		@Override
		public void setCounts(int _v_) { // 今天做了多少次副本
			counts = _v_;
		}

		@Override
		public void setAccepttime(long _v_) { // 最后接副本时间
			accepttime = _v_;
		}

		@Override
		public void setFanpai(int _v_) { // 当前副本是否需要翻牌 0 不需要, 1 需要
			fanpai = _v_;
		}

		@Override
		public void setReset(int _v_) { // 当前副本是否重置过 0 没有重置过, 1 重置过
			reset = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceInfoCol.Data)) return false;
			InstanceInfoCol.Data _o_ = (InstanceInfoCol.Data) _o1_;
			if (lastinstanceid != _o_.lastinstanceid) return false;
			if (counts != _o_.counts) return false;
			if (accepttime != _o_.accepttime) return false;
			if (!instinfo.equals(_o_.instinfo)) return false;
			if (!instcount.equals(_o_.instcount)) return false;
			if (fanpai != _o_.fanpai) return false;
			if (reset != _o_.reset) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += lastinstanceid;
			_h_ += counts;
			_h_ += accepttime;
			_h_ += instinfo.hashCode();
			_h_ += instcount.hashCode();
			_h_ += fanpai;
			_h_ += reset;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(lastinstanceid);
			_sb_.append(",");
			_sb_.append(counts);
			_sb_.append(",");
			_sb_.append(accepttime);
			_sb_.append(",");
			_sb_.append(instinfo);
			_sb_.append(",");
			_sb_.append(instcount);
			_sb_.append(",");
			_sb_.append(fanpai);
			_sb_.append(",");
			_sb_.append(reset);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
