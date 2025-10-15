
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Gather extends mkdb.XBean implements xbean.Gather {
	private long gatherkey; // 采集物key
	private int result; // 采集的结果,战斗或者获得物品
	private long endgathertime; // 开始采集的时间

	@Override
	public void _reset_unsafe_() {
		gatherkey = 0L;
		result = 0;
		endgathertime = 0L;
	}

	Gather(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public Gather() {
		this(0, null, null);
	}

	public Gather(Gather _o_) {
		this(_o_, null, null);
	}

	Gather(xbean.Gather _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Gather) assign((Gather)_o1_);
		else if (_o1_ instanceof Gather.Data) assign((Gather.Data)_o1_);
		else if (_o1_ instanceof Gather.Const) assign(((Gather.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Gather _o_) {
		_o_._xdb_verify_unsafe_();
		gatherkey = _o_.gatherkey;
		result = _o_.result;
		endgathertime = _o_.endgathertime;
	}

	private void assign(Gather.Data _o_) {
		gatherkey = _o_.gatherkey;
		result = _o_.result;
		endgathertime = _o_.endgathertime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(gatherkey);
		_os_.marshal(result);
		_os_.marshal(endgathertime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		gatherkey = _os_.unmarshal_long();
		result = _os_.unmarshal_int();
		endgathertime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.Gather copy() {
		_xdb_verify_unsafe_();
		return new Gather(this);
	}

	@Override
	public xbean.Gather toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Gather toBean() {
		_xdb_verify_unsafe_();
		return new Gather(this); // same as copy()
	}

	@Override
	public xbean.Gather toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Gather toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getGatherkey() { // 采集物key
		_xdb_verify_unsafe_();
		return gatherkey;
	}

	@Override
	public int getResult() { // 采集的结果,战斗或者获得物品
		_xdb_verify_unsafe_();
		return result;
	}

	@Override
	public long getEndgathertime() { // 开始采集的时间
		_xdb_verify_unsafe_();
		return endgathertime;
	}

	@Override
	public void setGatherkey(long _v_) { // 采集物key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "gatherkey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, gatherkey) {
					public void rollback() { gatherkey = _xdb_saved; }
				};}});
		gatherkey = _v_;
	}

	@Override
	public void setResult(int _v_) { // 采集的结果,战斗或者获得物品
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "result") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, result) {
					public void rollback() { result = _xdb_saved; }
				};}});
		result = _v_;
	}

	@Override
	public void setEndgathertime(long _v_) { // 开始采集的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "endgathertime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, endgathertime) {
					public void rollback() { endgathertime = _xdb_saved; }
				};}});
		endgathertime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Gather _o_ = null;
		if ( _o1_ instanceof Gather ) _o_ = (Gather)_o1_;
		else if ( _o1_ instanceof Gather.Const ) _o_ = ((Gather.Const)_o1_).nThis();
		else return false;
		if (gatherkey != _o_.gatherkey) return false;
		if (result != _o_.result) return false;
		if (endgathertime != _o_.endgathertime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += gatherkey;
		_h_ += result;
		_h_ += endgathertime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(gatherkey);
		_sb_.append(",");
		_sb_.append(result);
		_sb_.append(",");
		_sb_.append(endgathertime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("gatherkey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("result"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("endgathertime"));
		return lb;
	}

	private class Const implements xbean.Gather {
		Gather nThis() {
			return Gather.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Gather copy() {
			return Gather.this.copy();
		}

		@Override
		public xbean.Gather toData() {
			return Gather.this.toData();
		}

		public xbean.Gather toBean() {
			return Gather.this.toBean();
		}

		@Override
		public xbean.Gather toDataIf() {
			return Gather.this.toDataIf();
		}

		public xbean.Gather toBeanIf() {
			return Gather.this.toBeanIf();
		}

		@Override
		public long getGatherkey() { // 采集物key
			_xdb_verify_unsafe_();
			return gatherkey;
		}

		@Override
		public int getResult() { // 采集的结果,战斗或者获得物品
			_xdb_verify_unsafe_();
			return result;
		}

		@Override
		public long getEndgathertime() { // 开始采集的时间
			_xdb_verify_unsafe_();
			return endgathertime;
		}

		@Override
		public void setGatherkey(long _v_) { // 采集物key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setResult(int _v_) { // 采集的结果,战斗或者获得物品
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEndgathertime(long _v_) { // 开始采集的时间
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
			return Gather.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Gather.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Gather.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Gather.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Gather.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Gather.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Gather.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Gather.this.hashCode();
		}

		@Override
		public String toString() {
			return Gather.this.toString();
		}

	}

	public static final class Data implements xbean.Gather {
		private long gatherkey; // 采集物key
		private int result; // 采集的结果,战斗或者获得物品
		private long endgathertime; // 开始采集的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.Gather _o1_) {
			if (_o1_ instanceof Gather) assign((Gather)_o1_);
			else if (_o1_ instanceof Gather.Data) assign((Gather.Data)_o1_);
			else if (_o1_ instanceof Gather.Const) assign(((Gather.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Gather _o_) {
			gatherkey = _o_.gatherkey;
			result = _o_.result;
			endgathertime = _o_.endgathertime;
		}

		private void assign(Gather.Data _o_) {
			gatherkey = _o_.gatherkey;
			result = _o_.result;
			endgathertime = _o_.endgathertime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(gatherkey);
			_os_.marshal(result);
			_os_.marshal(endgathertime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			gatherkey = _os_.unmarshal_long();
			result = _os_.unmarshal_int();
			endgathertime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.Gather copy() {
			return new Data(this);
		}

		@Override
		public xbean.Gather toData() {
			return new Data(this);
		}

		public xbean.Gather toBean() {
			return new Gather(this, null, null);
		}

		@Override
		public xbean.Gather toDataIf() {
			return this;
		}

		public xbean.Gather toBeanIf() {
			return new Gather(this, null, null);
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
		public long getGatherkey() { // 采集物key
			return gatherkey;
		}

		@Override
		public int getResult() { // 采集的结果,战斗或者获得物品
			return result;
		}

		@Override
		public long getEndgathertime() { // 开始采集的时间
			return endgathertime;
		}

		@Override
		public void setGatherkey(long _v_) { // 采集物key
			gatherkey = _v_;
		}

		@Override
		public void setResult(int _v_) { // 采集的结果,战斗或者获得物品
			result = _v_;
		}

		@Override
		public void setEndgathertime(long _v_) { // 开始采集的时间
			endgathertime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Gather.Data)) return false;
			Gather.Data _o_ = (Gather.Data) _o1_;
			if (gatherkey != _o_.gatherkey) return false;
			if (result != _o_.result) return false;
			if (endgathertime != _o_.endgathertime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += gatherkey;
			_h_ += result;
			_h_ += endgathertime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(gatherkey);
			_sb_.append(",");
			_sb_.append(result);
			_sb_.append(",");
			_sb_.append(endgathertime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
