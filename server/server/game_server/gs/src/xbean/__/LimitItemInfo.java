
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class LimitItemInfo extends mkdb.XBean implements xbean.LimitItemInfo {
	private int maxnum; // 
	private int num; // 

	@Override
	public void _reset_unsafe_() {
		maxnum = 0;
		num = 0;
	}

	LimitItemInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public LimitItemInfo() {
		this(0, null, null);
	}

	public LimitItemInfo(LimitItemInfo _o_) {
		this(_o_, null, null);
	}

	LimitItemInfo(xbean.LimitItemInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LimitItemInfo) assign((LimitItemInfo)_o1_);
		else if (_o1_ instanceof LimitItemInfo.Data) assign((LimitItemInfo.Data)_o1_);
		else if (_o1_ instanceof LimitItemInfo.Const) assign(((LimitItemInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LimitItemInfo _o_) {
		_o_._xdb_verify_unsafe_();
		maxnum = _o_.maxnum;
		num = _o_.num;
	}

	private void assign(LimitItemInfo.Data _o_) {
		maxnum = _o_.maxnum;
		num = _o_.num;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(maxnum);
		_os_.marshal(num);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		maxnum = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.LimitItemInfo copy() {
		_xdb_verify_unsafe_();
		return new LimitItemInfo(this);
	}

	@Override
	public xbean.LimitItemInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LimitItemInfo toBean() {
		_xdb_verify_unsafe_();
		return new LimitItemInfo(this); // same as copy()
	}

	@Override
	public xbean.LimitItemInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LimitItemInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getMaxnum() { // 
		_xdb_verify_unsafe_();
		return maxnum;
	}

	@Override
	public int getNum() { // 
		_xdb_verify_unsafe_();
		return num;
	}

	@Override
	public void setMaxnum(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "maxnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, maxnum) {
					public void rollback() { maxnum = _xdb_saved; }
				};}});
		maxnum = _v_;
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
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LimitItemInfo _o_ = null;
		if ( _o1_ instanceof LimitItemInfo ) _o_ = (LimitItemInfo)_o1_;
		else if ( _o1_ instanceof LimitItemInfo.Const ) _o_ = ((LimitItemInfo.Const)_o1_).nThis();
		else return false;
		if (maxnum != _o_.maxnum) return false;
		if (num != _o_.num) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += maxnum;
		_h_ += num;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(maxnum);
		_sb_.append(",");
		_sb_.append(num);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("maxnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("num"));
		return lb;
	}

	private class Const implements xbean.LimitItemInfo {
		LimitItemInfo nThis() {
			return LimitItemInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LimitItemInfo copy() {
			return LimitItemInfo.this.copy();
		}

		@Override
		public xbean.LimitItemInfo toData() {
			return LimitItemInfo.this.toData();
		}

		public xbean.LimitItemInfo toBean() {
			return LimitItemInfo.this.toBean();
		}

		@Override
		public xbean.LimitItemInfo toDataIf() {
			return LimitItemInfo.this.toDataIf();
		}

		public xbean.LimitItemInfo toBeanIf() {
			return LimitItemInfo.this.toBeanIf();
		}

		@Override
		public int getMaxnum() { // 
			_xdb_verify_unsafe_();
			return maxnum;
		}

		@Override
		public int getNum() { // 
			_xdb_verify_unsafe_();
			return num;
		}

		@Override
		public void setMaxnum(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNum(int _v_) { // 
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
			return LimitItemInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LimitItemInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return LimitItemInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LimitItemInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LimitItemInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LimitItemInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LimitItemInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LimitItemInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return LimitItemInfo.this.toString();
		}

	}

	public static final class Data implements xbean.LimitItemInfo {
		private int maxnum; // 
		private int num; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.LimitItemInfo _o1_) {
			if (_o1_ instanceof LimitItemInfo) assign((LimitItemInfo)_o1_);
			else if (_o1_ instanceof LimitItemInfo.Data) assign((LimitItemInfo.Data)_o1_);
			else if (_o1_ instanceof LimitItemInfo.Const) assign(((LimitItemInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LimitItemInfo _o_) {
			maxnum = _o_.maxnum;
			num = _o_.num;
		}

		private void assign(LimitItemInfo.Data _o_) {
			maxnum = _o_.maxnum;
			num = _o_.num;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(maxnum);
			_os_.marshal(num);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			maxnum = _os_.unmarshal_int();
			num = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.LimitItemInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.LimitItemInfo toData() {
			return new Data(this);
		}

		public xbean.LimitItemInfo toBean() {
			return new LimitItemInfo(this, null, null);
		}

		@Override
		public xbean.LimitItemInfo toDataIf() {
			return this;
		}

		public xbean.LimitItemInfo toBeanIf() {
			return new LimitItemInfo(this, null, null);
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
		public int getMaxnum() { // 
			return maxnum;
		}

		@Override
		public int getNum() { // 
			return num;
		}

		@Override
		public void setMaxnum(int _v_) { // 
			maxnum = _v_;
		}

		@Override
		public void setNum(int _v_) { // 
			num = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LimitItemInfo.Data)) return false;
			LimitItemInfo.Data _o_ = (LimitItemInfo.Data) _o1_;
			if (maxnum != _o_.maxnum) return false;
			if (num != _o_.num) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += maxnum;
			_h_ += num;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(maxnum);
			_sb_.append(",");
			_sb_.append(num);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
