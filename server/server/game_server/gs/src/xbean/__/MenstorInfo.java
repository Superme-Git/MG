
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MenstorInfo extends mkdb.XBean implements xbean.MenstorInfo {
	private java.util.LinkedList<xbean.CurrApprent> currapprentices; // 当前的所有的徒弟注意要有顺序性
	private java.util.LinkedList<xbean.HasApprent> apprentices; // 当前已经出徒的徒弟
	private long updatetime; // 每日上线的时候 只更新一次

	@Override
	public void _reset_unsafe_() {
		currapprentices.clear();
		apprentices.clear();
		updatetime = 0L;
	}

	MenstorInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		currapprentices = new java.util.LinkedList<xbean.CurrApprent>();
		apprentices = new java.util.LinkedList<xbean.HasApprent>();
	}

	public MenstorInfo() {
		this(0, null, null);
	}

	public MenstorInfo(MenstorInfo _o_) {
		this(_o_, null, null);
	}

	MenstorInfo(xbean.MenstorInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MenstorInfo) assign((MenstorInfo)_o1_);
		else if (_o1_ instanceof MenstorInfo.Data) assign((MenstorInfo.Data)_o1_);
		else if (_o1_ instanceof MenstorInfo.Const) assign(((MenstorInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MenstorInfo _o_) {
		_o_._xdb_verify_unsafe_();
		currapprentices = new java.util.LinkedList<xbean.CurrApprent>();
		for (xbean.CurrApprent _v_ : _o_.currapprentices)
			currapprentices.add(new CurrApprent(_v_, this, "currapprentices"));
		apprentices = new java.util.LinkedList<xbean.HasApprent>();
		for (xbean.HasApprent _v_ : _o_.apprentices)
			apprentices.add(new HasApprent(_v_, this, "apprentices"));
		updatetime = _o_.updatetime;
	}

	private void assign(MenstorInfo.Data _o_) {
		currapprentices = new java.util.LinkedList<xbean.CurrApprent>();
		for (xbean.CurrApprent _v_ : _o_.currapprentices)
			currapprentices.add(new CurrApprent(_v_, this, "currapprentices"));
		apprentices = new java.util.LinkedList<xbean.HasApprent>();
		for (xbean.HasApprent _v_ : _o_.apprentices)
			apprentices.add(new HasApprent(_v_, this, "apprentices"));
		updatetime = _o_.updatetime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(currapprentices.size());
		for (xbean.CurrApprent _v_ : currapprentices) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(apprentices.size());
		for (xbean.HasApprent _v_ : apprentices) {
			_v_.marshal(_os_);
		}
		_os_.marshal(updatetime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.CurrApprent _v_ = new CurrApprent(0, this, "currapprentices");
			_v_.unmarshal(_os_);
			currapprentices.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.HasApprent _v_ = new HasApprent(0, this, "apprentices");
			_v_.unmarshal(_os_);
			apprentices.add(_v_);
		}
		updatetime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.MenstorInfo copy() {
		_xdb_verify_unsafe_();
		return new MenstorInfo(this);
	}

	@Override
	public xbean.MenstorInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MenstorInfo toBean() {
		_xdb_verify_unsafe_();
		return new MenstorInfo(this); // same as copy()
	}

	@Override
	public xbean.MenstorInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MenstorInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.CurrApprent> getCurrapprentices() { // 当前的所有的徒弟注意要有顺序性
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "currapprentices"), currapprentices);
	}

	public java.util.List<xbean.CurrApprent> getCurrapprenticesAsData() { // 当前的所有的徒弟注意要有顺序性
		_xdb_verify_unsafe_();
		java.util.List<xbean.CurrApprent> currapprentices;
		MenstorInfo _o_ = this;
		currapprentices = new java.util.LinkedList<xbean.CurrApprent>();
		for (xbean.CurrApprent _v_ : _o_.currapprentices)
			currapprentices.add(new CurrApprent.Data(_v_));
		return currapprentices;
	}

	@Override
	public java.util.List<xbean.HasApprent> getApprentices() { // 当前已经出徒的徒弟
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "apprentices"), apprentices);
	}

	public java.util.List<xbean.HasApprent> getApprenticesAsData() { // 当前已经出徒的徒弟
		_xdb_verify_unsafe_();
		java.util.List<xbean.HasApprent> apprentices;
		MenstorInfo _o_ = this;
		apprentices = new java.util.LinkedList<xbean.HasApprent>();
		for (xbean.HasApprent _v_ : _o_.apprentices)
			apprentices.add(new HasApprent.Data(_v_));
		return apprentices;
	}

	@Override
	public long getUpdatetime() { // 每日上线的时候 只更新一次
		_xdb_verify_unsafe_();
		return updatetime;
	}

	@Override
	public void setUpdatetime(long _v_) { // 每日上线的时候 只更新一次
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "updatetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, updatetime) {
					public void rollback() { updatetime = _xdb_saved; }
				};}});
		updatetime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MenstorInfo _o_ = null;
		if ( _o1_ instanceof MenstorInfo ) _o_ = (MenstorInfo)_o1_;
		else if ( _o1_ instanceof MenstorInfo.Const ) _o_ = ((MenstorInfo.Const)_o1_).nThis();
		else return false;
		if (!currapprentices.equals(_o_.currapprentices)) return false;
		if (!apprentices.equals(_o_.apprentices)) return false;
		if (updatetime != _o_.updatetime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += currapprentices.hashCode();
		_h_ += apprentices.hashCode();
		_h_ += updatetime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(currapprentices);
		_sb_.append(",");
		_sb_.append(apprentices);
		_sb_.append(",");
		_sb_.append(updatetime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("currapprentices"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("apprentices"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("updatetime"));
		return lb;
	}

	private class Const implements xbean.MenstorInfo {
		MenstorInfo nThis() {
			return MenstorInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MenstorInfo copy() {
			return MenstorInfo.this.copy();
		}

		@Override
		public xbean.MenstorInfo toData() {
			return MenstorInfo.this.toData();
		}

		public xbean.MenstorInfo toBean() {
			return MenstorInfo.this.toBean();
		}

		@Override
		public xbean.MenstorInfo toDataIf() {
			return MenstorInfo.this.toDataIf();
		}

		public xbean.MenstorInfo toBeanIf() {
			return MenstorInfo.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.CurrApprent> getCurrapprentices() { // 当前的所有的徒弟注意要有顺序性
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(currapprentices);
		}

		public java.util.List<xbean.CurrApprent> getCurrapprenticesAsData() { // 当前的所有的徒弟注意要有顺序性
			_xdb_verify_unsafe_();
			java.util.List<xbean.CurrApprent> currapprentices;
			MenstorInfo _o_ = MenstorInfo.this;
		currapprentices = new java.util.LinkedList<xbean.CurrApprent>();
		for (xbean.CurrApprent _v_ : _o_.currapprentices)
			currapprentices.add(new CurrApprent.Data(_v_));
			return currapprentices;
		}

		@Override
		public java.util.List<xbean.HasApprent> getApprentices() { // 当前已经出徒的徒弟
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(apprentices);
		}

		public java.util.List<xbean.HasApprent> getApprenticesAsData() { // 当前已经出徒的徒弟
			_xdb_verify_unsafe_();
			java.util.List<xbean.HasApprent> apprentices;
			MenstorInfo _o_ = MenstorInfo.this;
		apprentices = new java.util.LinkedList<xbean.HasApprent>();
		for (xbean.HasApprent _v_ : _o_.apprentices)
			apprentices.add(new HasApprent.Data(_v_));
			return apprentices;
		}

		@Override
		public long getUpdatetime() { // 每日上线的时候 只更新一次
			_xdb_verify_unsafe_();
			return updatetime;
		}

		@Override
		public void setUpdatetime(long _v_) { // 每日上线的时候 只更新一次
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
			return MenstorInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MenstorInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MenstorInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MenstorInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MenstorInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MenstorInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MenstorInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MenstorInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return MenstorInfo.this.toString();
		}

	}

	public static final class Data implements xbean.MenstorInfo {
		private java.util.LinkedList<xbean.CurrApprent> currapprentices; // 当前的所有的徒弟注意要有顺序性
		private java.util.LinkedList<xbean.HasApprent> apprentices; // 当前已经出徒的徒弟
		private long updatetime; // 每日上线的时候 只更新一次

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			currapprentices = new java.util.LinkedList<xbean.CurrApprent>();
			apprentices = new java.util.LinkedList<xbean.HasApprent>();
		}

		Data(xbean.MenstorInfo _o1_) {
			if (_o1_ instanceof MenstorInfo) assign((MenstorInfo)_o1_);
			else if (_o1_ instanceof MenstorInfo.Data) assign((MenstorInfo.Data)_o1_);
			else if (_o1_ instanceof MenstorInfo.Const) assign(((MenstorInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MenstorInfo _o_) {
			currapprentices = new java.util.LinkedList<xbean.CurrApprent>();
			for (xbean.CurrApprent _v_ : _o_.currapprentices)
				currapprentices.add(new CurrApprent.Data(_v_));
			apprentices = new java.util.LinkedList<xbean.HasApprent>();
			for (xbean.HasApprent _v_ : _o_.apprentices)
				apprentices.add(new HasApprent.Data(_v_));
			updatetime = _o_.updatetime;
		}

		private void assign(MenstorInfo.Data _o_) {
			currapprentices = new java.util.LinkedList<xbean.CurrApprent>();
			for (xbean.CurrApprent _v_ : _o_.currapprentices)
				currapprentices.add(new CurrApprent.Data(_v_));
			apprentices = new java.util.LinkedList<xbean.HasApprent>();
			for (xbean.HasApprent _v_ : _o_.apprentices)
				apprentices.add(new HasApprent.Data(_v_));
			updatetime = _o_.updatetime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(currapprentices.size());
			for (xbean.CurrApprent _v_ : currapprentices) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(apprentices.size());
			for (xbean.HasApprent _v_ : apprentices) {
				_v_.marshal(_os_);
			}
			_os_.marshal(updatetime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.CurrApprent _v_ = xbean.Pod.newCurrApprentData();
				_v_.unmarshal(_os_);
				currapprentices.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.HasApprent _v_ = xbean.Pod.newHasApprentData();
				_v_.unmarshal(_os_);
				apprentices.add(_v_);
			}
			updatetime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.MenstorInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.MenstorInfo toData() {
			return new Data(this);
		}

		public xbean.MenstorInfo toBean() {
			return new MenstorInfo(this, null, null);
		}

		@Override
		public xbean.MenstorInfo toDataIf() {
			return this;
		}

		public xbean.MenstorInfo toBeanIf() {
			return new MenstorInfo(this, null, null);
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
		public java.util.List<xbean.CurrApprent> getCurrapprentices() { // 当前的所有的徒弟注意要有顺序性
			return currapprentices;
		}

		@Override
		public java.util.List<xbean.CurrApprent> getCurrapprenticesAsData() { // 当前的所有的徒弟注意要有顺序性
			return currapprentices;
		}

		@Override
		public java.util.List<xbean.HasApprent> getApprentices() { // 当前已经出徒的徒弟
			return apprentices;
		}

		@Override
		public java.util.List<xbean.HasApprent> getApprenticesAsData() { // 当前已经出徒的徒弟
			return apprentices;
		}

		@Override
		public long getUpdatetime() { // 每日上线的时候 只更新一次
			return updatetime;
		}

		@Override
		public void setUpdatetime(long _v_) { // 每日上线的时候 只更新一次
			updatetime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MenstorInfo.Data)) return false;
			MenstorInfo.Data _o_ = (MenstorInfo.Data) _o1_;
			if (!currapprentices.equals(_o_.currapprentices)) return false;
			if (!apprentices.equals(_o_.apprentices)) return false;
			if (updatetime != _o_.updatetime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += currapprentices.hashCode();
			_h_ += apprentices.hashCode();
			_h_ += updatetime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(currapprentices);
			_sb_.append(",");
			_sb_.append(apprentices);
			_sb_.append(",");
			_sb_.append(updatetime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
