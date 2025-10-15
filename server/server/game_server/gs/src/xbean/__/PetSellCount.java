
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PetSellCount extends mkdb.XBean implements xbean.PetSellCount {
	private int count; // 次数 *byte够了
	private long resettime; // 重置时间

	@Override
	public void _reset_unsafe_() {
		count = 0;
		resettime = 0L;
	}

	PetSellCount(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public PetSellCount() {
		this(0, null, null);
	}

	public PetSellCount(PetSellCount _o_) {
		this(_o_, null, null);
	}

	PetSellCount(xbean.PetSellCount _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PetSellCount) assign((PetSellCount)_o1_);
		else if (_o1_ instanceof PetSellCount.Data) assign((PetSellCount.Data)_o1_);
		else if (_o1_ instanceof PetSellCount.Const) assign(((PetSellCount.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PetSellCount _o_) {
		_o_._xdb_verify_unsafe_();
		count = _o_.count;
		resettime = _o_.resettime;
	}

	private void assign(PetSellCount.Data _o_) {
		count = _o_.count;
		resettime = _o_.resettime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(count);
		_os_.marshal(resettime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		count = _os_.unmarshal_int();
		resettime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.PetSellCount copy() {
		_xdb_verify_unsafe_();
		return new PetSellCount(this);
	}

	@Override
	public xbean.PetSellCount toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PetSellCount toBean() {
		_xdb_verify_unsafe_();
		return new PetSellCount(this); // same as copy()
	}

	@Override
	public xbean.PetSellCount toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PetSellCount toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getCount() { // 次数 *byte够了
		_xdb_verify_unsafe_();
		return count;
	}

	@Override
	public long getResettime() { // 重置时间
		_xdb_verify_unsafe_();
		return resettime;
	}

	@Override
	public void setCount(int _v_) { // 次数 *byte够了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "count") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, count) {
					public void rollback() { count = _xdb_saved; }
				};}});
		count = _v_;
	}

	@Override
	public void setResettime(long _v_) { // 重置时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "resettime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, resettime) {
					public void rollback() { resettime = _xdb_saved; }
				};}});
		resettime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PetSellCount _o_ = null;
		if ( _o1_ instanceof PetSellCount ) _o_ = (PetSellCount)_o1_;
		else if ( _o1_ instanceof PetSellCount.Const ) _o_ = ((PetSellCount.Const)_o1_).nThis();
		else return false;
		if (count != _o_.count) return false;
		if (resettime != _o_.resettime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += count;
		_h_ += resettime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(count);
		_sb_.append(",");
		_sb_.append(resettime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("count"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("resettime"));
		return lb;
	}

	private class Const implements xbean.PetSellCount {
		PetSellCount nThis() {
			return PetSellCount.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PetSellCount copy() {
			return PetSellCount.this.copy();
		}

		@Override
		public xbean.PetSellCount toData() {
			return PetSellCount.this.toData();
		}

		public xbean.PetSellCount toBean() {
			return PetSellCount.this.toBean();
		}

		@Override
		public xbean.PetSellCount toDataIf() {
			return PetSellCount.this.toDataIf();
		}

		public xbean.PetSellCount toBeanIf() {
			return PetSellCount.this.toBeanIf();
		}

		@Override
		public int getCount() { // 次数 *byte够了
			_xdb_verify_unsafe_();
			return count;
		}

		@Override
		public long getResettime() { // 重置时间
			_xdb_verify_unsafe_();
			return resettime;
		}

		@Override
		public void setCount(int _v_) { // 次数 *byte够了
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setResettime(long _v_) { // 重置时间
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
			return PetSellCount.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PetSellCount.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PetSellCount.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PetSellCount.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PetSellCount.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PetSellCount.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PetSellCount.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PetSellCount.this.hashCode();
		}

		@Override
		public String toString() {
			return PetSellCount.this.toString();
		}

	}

	public static final class Data implements xbean.PetSellCount {
		private int count; // 次数 *byte够了
		private long resettime; // 重置时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.PetSellCount _o1_) {
			if (_o1_ instanceof PetSellCount) assign((PetSellCount)_o1_);
			else if (_o1_ instanceof PetSellCount.Data) assign((PetSellCount.Data)_o1_);
			else if (_o1_ instanceof PetSellCount.Const) assign(((PetSellCount.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PetSellCount _o_) {
			count = _o_.count;
			resettime = _o_.resettime;
		}

		private void assign(PetSellCount.Data _o_) {
			count = _o_.count;
			resettime = _o_.resettime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(count);
			_os_.marshal(resettime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			count = _os_.unmarshal_int();
			resettime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.PetSellCount copy() {
			return new Data(this);
		}

		@Override
		public xbean.PetSellCount toData() {
			return new Data(this);
		}

		public xbean.PetSellCount toBean() {
			return new PetSellCount(this, null, null);
		}

		@Override
		public xbean.PetSellCount toDataIf() {
			return this;
		}

		public xbean.PetSellCount toBeanIf() {
			return new PetSellCount(this, null, null);
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
		public int getCount() { // 次数 *byte够了
			return count;
		}

		@Override
		public long getResettime() { // 重置时间
			return resettime;
		}

		@Override
		public void setCount(int _v_) { // 次数 *byte够了
			count = _v_;
		}

		@Override
		public void setResettime(long _v_) { // 重置时间
			resettime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PetSellCount.Data)) return false;
			PetSellCount.Data _o_ = (PetSellCount.Data) _o1_;
			if (count != _o_.count) return false;
			if (resettime != _o_.resettime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += count;
			_h_ += resettime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(count);
			_sb_.append(",");
			_sb_.append(resettime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
