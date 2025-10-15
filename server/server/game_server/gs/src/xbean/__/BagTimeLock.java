
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BagTimeLock extends mkdb.XBean implements xbean.BagTimeLock {
	private int itemkey; // 
	private int num; // 
	private long timeout; // 超时时间

	@Override
	public void _reset_unsafe_() {
		itemkey = 0;
		num = 0;
		timeout = 0L;
	}

	BagTimeLock(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public BagTimeLock() {
		this(0, null, null);
	}

	public BagTimeLock(BagTimeLock _o_) {
		this(_o_, null, null);
	}

	BagTimeLock(xbean.BagTimeLock _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BagTimeLock) assign((BagTimeLock)_o1_);
		else if (_o1_ instanceof BagTimeLock.Data) assign((BagTimeLock.Data)_o1_);
		else if (_o1_ instanceof BagTimeLock.Const) assign(((BagTimeLock.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BagTimeLock _o_) {
		_o_._xdb_verify_unsafe_();
		itemkey = _o_.itemkey;
		num = _o_.num;
		timeout = _o_.timeout;
	}

	private void assign(BagTimeLock.Data _o_) {
		itemkey = _o_.itemkey;
		num = _o_.num;
		timeout = _o_.timeout;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(itemkey);
		_os_.marshal(num);
		_os_.marshal(timeout);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		itemkey = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		timeout = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.BagTimeLock copy() {
		_xdb_verify_unsafe_();
		return new BagTimeLock(this);
	}

	@Override
	public xbean.BagTimeLock toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BagTimeLock toBean() {
		_xdb_verify_unsafe_();
		return new BagTimeLock(this); // same as copy()
	}

	@Override
	public xbean.BagTimeLock toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BagTimeLock toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getItemkey() { // 
		_xdb_verify_unsafe_();
		return itemkey;
	}

	@Override
	public int getNum() { // 
		_xdb_verify_unsafe_();
		return num;
	}

	@Override
	public long getTimeout() { // 超时时间
		_xdb_verify_unsafe_();
		return timeout;
	}

	@Override
	public void setItemkey(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemkey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemkey) {
					public void rollback() { itemkey = _xdb_saved; }
				};}});
		itemkey = _v_;
	}

	@Override
	public void setNum(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "num") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, num) {
					public void rollback() { num = _xdb_saved; }
				};}});
		num = _v_;
	}

	@Override
	public void setTimeout(long _v_) { // 超时时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "timeout") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, timeout) {
					public void rollback() { timeout = _xdb_saved; }
				};}});
		timeout = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BagTimeLock _o_ = null;
		if ( _o1_ instanceof BagTimeLock ) _o_ = (BagTimeLock)_o1_;
		else if ( _o1_ instanceof BagTimeLock.Const ) _o_ = ((BagTimeLock.Const)_o1_).nThis();
		else return false;
		if (itemkey != _o_.itemkey) return false;
		if (num != _o_.num) return false;
		if (timeout != _o_.timeout) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += itemkey;
		_h_ += num;
		_h_ += timeout;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemkey);
		_sb_.append(",");
		_sb_.append(num);
		_sb_.append(",");
		_sb_.append(timeout);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemkey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("num"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("timeout"));
		return lb;
	}

	private class Const implements xbean.BagTimeLock {
		BagTimeLock nThis() {
			return BagTimeLock.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BagTimeLock copy() {
			return BagTimeLock.this.copy();
		}

		@Override
		public xbean.BagTimeLock toData() {
			return BagTimeLock.this.toData();
		}

		public xbean.BagTimeLock toBean() {
			return BagTimeLock.this.toBean();
		}

		@Override
		public xbean.BagTimeLock toDataIf() {
			return BagTimeLock.this.toDataIf();
		}

		public xbean.BagTimeLock toBeanIf() {
			return BagTimeLock.this.toBeanIf();
		}

		@Override
		public int getItemkey() { // 
			_xdb_verify_unsafe_();
			return itemkey;
		}

		@Override
		public int getNum() { // 
			_xdb_verify_unsafe_();
			return num;
		}

		@Override
		public long getTimeout() { // 超时时间
			_xdb_verify_unsafe_();
			return timeout;
		}

		@Override
		public void setItemkey(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNum(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTimeout(long _v_) { // 超时时间
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
			return BagTimeLock.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BagTimeLock.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BagTimeLock.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BagTimeLock.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BagTimeLock.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BagTimeLock.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BagTimeLock.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BagTimeLock.this.hashCode();
		}

		@Override
		public String toString() {
			return BagTimeLock.this.toString();
		}

	}

	public static final class Data implements xbean.BagTimeLock {
		private int itemkey; // 
		private int num; // 
		private long timeout; // 超时时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.BagTimeLock _o1_) {
			if (_o1_ instanceof BagTimeLock) assign((BagTimeLock)_o1_);
			else if (_o1_ instanceof BagTimeLock.Data) assign((BagTimeLock.Data)_o1_);
			else if (_o1_ instanceof BagTimeLock.Const) assign(((BagTimeLock.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BagTimeLock _o_) {
			itemkey = _o_.itemkey;
			num = _o_.num;
			timeout = _o_.timeout;
		}

		private void assign(BagTimeLock.Data _o_) {
			itemkey = _o_.itemkey;
			num = _o_.num;
			timeout = _o_.timeout;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(itemkey);
			_os_.marshal(num);
			_os_.marshal(timeout);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			itemkey = _os_.unmarshal_int();
			num = _os_.unmarshal_int();
			timeout = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.BagTimeLock copy() {
			return new Data(this);
		}

		@Override
		public xbean.BagTimeLock toData() {
			return new Data(this);
		}

		public xbean.BagTimeLock toBean() {
			return new BagTimeLock(this, null, null);
		}

		@Override
		public xbean.BagTimeLock toDataIf() {
			return this;
		}

		public xbean.BagTimeLock toBeanIf() {
			return new BagTimeLock(this, null, null);
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
		public int getItemkey() { // 
			return itemkey;
		}

		@Override
		public int getNum() { // 
			return num;
		}

		@Override
		public long getTimeout() { // 超时时间
			return timeout;
		}

		@Override
		public void setItemkey(int _v_) { // 
			itemkey = _v_;
		}

		@Override
		public void setNum(int _v_) { // 
			num = _v_;
		}

		@Override
		public void setTimeout(long _v_) { // 超时时间
			timeout = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BagTimeLock.Data)) return false;
			BagTimeLock.Data _o_ = (BagTimeLock.Data) _o1_;
			if (itemkey != _o_.itemkey) return false;
			if (num != _o_.num) return false;
			if (timeout != _o_.timeout) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += itemkey;
			_h_ += num;
			_h_ += timeout;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(itemkey);
			_sb_.append(",");
			_sb_.append(num);
			_sb_.append(",");
			_sb_.append(timeout);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
