
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class OffLineHook extends mkdb.XBean implements xbean.OffLineHook {
	private long hooktime; // 开始挂机时间
	private long remainfivebeitime; // 点击使用5倍的时候封存5倍时间
	private long fivebeitimestart; // 开始使用五倍时间的时间
	private long fivebeitotalusetime; // 最后一次点击前总的使用时间
	private int flag; // 0=无挂机或者已经领取了挂机经验 1=单倍挂机 2=5倍挂机

	@Override
	public void _reset_unsafe_() {
		hooktime = 0L;
		remainfivebeitime = 0L;
		fivebeitimestart = 0L;
		fivebeitotalusetime = 0L;
		flag = 0;
	}

	OffLineHook(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public OffLineHook() {
		this(0, null, null);
	}

	public OffLineHook(OffLineHook _o_) {
		this(_o_, null, null);
	}

	OffLineHook(xbean.OffLineHook _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof OffLineHook) assign((OffLineHook)_o1_);
		else if (_o1_ instanceof OffLineHook.Data) assign((OffLineHook.Data)_o1_);
		else if (_o1_ instanceof OffLineHook.Const) assign(((OffLineHook.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(OffLineHook _o_) {
		_o_._xdb_verify_unsafe_();
		hooktime = _o_.hooktime;
		remainfivebeitime = _o_.remainfivebeitime;
		fivebeitimestart = _o_.fivebeitimestart;
		fivebeitotalusetime = _o_.fivebeitotalusetime;
		flag = _o_.flag;
	}

	private void assign(OffLineHook.Data _o_) {
		hooktime = _o_.hooktime;
		remainfivebeitime = _o_.remainfivebeitime;
		fivebeitimestart = _o_.fivebeitimestart;
		fivebeitotalusetime = _o_.fivebeitotalusetime;
		flag = _o_.flag;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(hooktime);
		_os_.marshal(remainfivebeitime);
		_os_.marshal(fivebeitimestart);
		_os_.marshal(fivebeitotalusetime);
		_os_.marshal(flag);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		hooktime = _os_.unmarshal_long();
		remainfivebeitime = _os_.unmarshal_long();
		fivebeitimestart = _os_.unmarshal_long();
		fivebeitotalusetime = _os_.unmarshal_long();
		flag = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.OffLineHook copy() {
		_xdb_verify_unsafe_();
		return new OffLineHook(this);
	}

	@Override
	public xbean.OffLineHook toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OffLineHook toBean() {
		_xdb_verify_unsafe_();
		return new OffLineHook(this); // same as copy()
	}

	@Override
	public xbean.OffLineHook toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.OffLineHook toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getHooktime() { // 开始挂机时间
		_xdb_verify_unsafe_();
		return hooktime;
	}

	@Override
	public long getRemainfivebeitime() { // 点击使用5倍的时候封存5倍时间
		_xdb_verify_unsafe_();
		return remainfivebeitime;
	}

	@Override
	public long getFivebeitimestart() { // 开始使用五倍时间的时间
		_xdb_verify_unsafe_();
		return fivebeitimestart;
	}

	@Override
	public long getFivebeitotalusetime() { // 最后一次点击前总的使用时间
		_xdb_verify_unsafe_();
		return fivebeitotalusetime;
	}

	@Override
	public int getFlag() { // 0=无挂机或者已经领取了挂机经验 1=单倍挂机 2=5倍挂机
		_xdb_verify_unsafe_();
		return flag;
	}

	@Override
	public void setHooktime(long _v_) { // 开始挂机时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "hooktime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, hooktime) {
					public void rollback() { hooktime = _xdb_saved; }
				};}});
		hooktime = _v_;
	}

	@Override
	public void setRemainfivebeitime(long _v_) { // 点击使用5倍的时候封存5倍时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "remainfivebeitime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, remainfivebeitime) {
					public void rollback() { remainfivebeitime = _xdb_saved; }
				};}});
		remainfivebeitime = _v_;
	}

	@Override
	public void setFivebeitimestart(long _v_) { // 开始使用五倍时间的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fivebeitimestart") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, fivebeitimestart) {
					public void rollback() { fivebeitimestart = _xdb_saved; }
				};}});
		fivebeitimestart = _v_;
	}

	@Override
	public void setFivebeitotalusetime(long _v_) { // 最后一次点击前总的使用时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fivebeitotalusetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, fivebeitotalusetime) {
					public void rollback() { fivebeitotalusetime = _xdb_saved; }
				};}});
		fivebeitotalusetime = _v_;
	}

	@Override
	public void setFlag(int _v_) { // 0=无挂机或者已经领取了挂机经验 1=单倍挂机 2=5倍挂机
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "flag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, flag) {
					public void rollback() { flag = _xdb_saved; }
				};}});
		flag = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		OffLineHook _o_ = null;
		if ( _o1_ instanceof OffLineHook ) _o_ = (OffLineHook)_o1_;
		else if ( _o1_ instanceof OffLineHook.Const ) _o_ = ((OffLineHook.Const)_o1_).nThis();
		else return false;
		if (hooktime != _o_.hooktime) return false;
		if (remainfivebeitime != _o_.remainfivebeitime) return false;
		if (fivebeitimestart != _o_.fivebeitimestart) return false;
		if (fivebeitotalusetime != _o_.fivebeitotalusetime) return false;
		if (flag != _o_.flag) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += hooktime;
		_h_ += remainfivebeitime;
		_h_ += fivebeitimestart;
		_h_ += fivebeitotalusetime;
		_h_ += flag;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(hooktime);
		_sb_.append(",");
		_sb_.append(remainfivebeitime);
		_sb_.append(",");
		_sb_.append(fivebeitimestart);
		_sb_.append(",");
		_sb_.append(fivebeitotalusetime);
		_sb_.append(",");
		_sb_.append(flag);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("hooktime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("remainfivebeitime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fivebeitimestart"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fivebeitotalusetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("flag"));
		return lb;
	}

	private class Const implements xbean.OffLineHook {
		OffLineHook nThis() {
			return OffLineHook.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.OffLineHook copy() {
			return OffLineHook.this.copy();
		}

		@Override
		public xbean.OffLineHook toData() {
			return OffLineHook.this.toData();
		}

		public xbean.OffLineHook toBean() {
			return OffLineHook.this.toBean();
		}

		@Override
		public xbean.OffLineHook toDataIf() {
			return OffLineHook.this.toDataIf();
		}

		public xbean.OffLineHook toBeanIf() {
			return OffLineHook.this.toBeanIf();
		}

		@Override
		public long getHooktime() { // 开始挂机时间
			_xdb_verify_unsafe_();
			return hooktime;
		}

		@Override
		public long getRemainfivebeitime() { // 点击使用5倍的时候封存5倍时间
			_xdb_verify_unsafe_();
			return remainfivebeitime;
		}

		@Override
		public long getFivebeitimestart() { // 开始使用五倍时间的时间
			_xdb_verify_unsafe_();
			return fivebeitimestart;
		}

		@Override
		public long getFivebeitotalusetime() { // 最后一次点击前总的使用时间
			_xdb_verify_unsafe_();
			return fivebeitotalusetime;
		}

		@Override
		public int getFlag() { // 0=无挂机或者已经领取了挂机经验 1=单倍挂机 2=5倍挂机
			_xdb_verify_unsafe_();
			return flag;
		}

		@Override
		public void setHooktime(long _v_) { // 开始挂机时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRemainfivebeitime(long _v_) { // 点击使用5倍的时候封存5倍时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFivebeitimestart(long _v_) { // 开始使用五倍时间的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFivebeitotalusetime(long _v_) { // 最后一次点击前总的使用时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFlag(int _v_) { // 0=无挂机或者已经领取了挂机经验 1=单倍挂机 2=5倍挂机
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
			return OffLineHook.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return OffLineHook.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return OffLineHook.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return OffLineHook.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return OffLineHook.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return OffLineHook.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return OffLineHook.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return OffLineHook.this.hashCode();
		}

		@Override
		public String toString() {
			return OffLineHook.this.toString();
		}

	}

	public static final class Data implements xbean.OffLineHook {
		private long hooktime; // 开始挂机时间
		private long remainfivebeitime; // 点击使用5倍的时候封存5倍时间
		private long fivebeitimestart; // 开始使用五倍时间的时间
		private long fivebeitotalusetime; // 最后一次点击前总的使用时间
		private int flag; // 0=无挂机或者已经领取了挂机经验 1=单倍挂机 2=5倍挂机

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.OffLineHook _o1_) {
			if (_o1_ instanceof OffLineHook) assign((OffLineHook)_o1_);
			else if (_o1_ instanceof OffLineHook.Data) assign((OffLineHook.Data)_o1_);
			else if (_o1_ instanceof OffLineHook.Const) assign(((OffLineHook.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(OffLineHook _o_) {
			hooktime = _o_.hooktime;
			remainfivebeitime = _o_.remainfivebeitime;
			fivebeitimestart = _o_.fivebeitimestart;
			fivebeitotalusetime = _o_.fivebeitotalusetime;
			flag = _o_.flag;
		}

		private void assign(OffLineHook.Data _o_) {
			hooktime = _o_.hooktime;
			remainfivebeitime = _o_.remainfivebeitime;
			fivebeitimestart = _o_.fivebeitimestart;
			fivebeitotalusetime = _o_.fivebeitotalusetime;
			flag = _o_.flag;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(hooktime);
			_os_.marshal(remainfivebeitime);
			_os_.marshal(fivebeitimestart);
			_os_.marshal(fivebeitotalusetime);
			_os_.marshal(flag);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			hooktime = _os_.unmarshal_long();
			remainfivebeitime = _os_.unmarshal_long();
			fivebeitimestart = _os_.unmarshal_long();
			fivebeitotalusetime = _os_.unmarshal_long();
			flag = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.OffLineHook copy() {
			return new Data(this);
		}

		@Override
		public xbean.OffLineHook toData() {
			return new Data(this);
		}

		public xbean.OffLineHook toBean() {
			return new OffLineHook(this, null, null);
		}

		@Override
		public xbean.OffLineHook toDataIf() {
			return this;
		}

		public xbean.OffLineHook toBeanIf() {
			return new OffLineHook(this, null, null);
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
		public long getHooktime() { // 开始挂机时间
			return hooktime;
		}

		@Override
		public long getRemainfivebeitime() { // 点击使用5倍的时候封存5倍时间
			return remainfivebeitime;
		}

		@Override
		public long getFivebeitimestart() { // 开始使用五倍时间的时间
			return fivebeitimestart;
		}

		@Override
		public long getFivebeitotalusetime() { // 最后一次点击前总的使用时间
			return fivebeitotalusetime;
		}

		@Override
		public int getFlag() { // 0=无挂机或者已经领取了挂机经验 1=单倍挂机 2=5倍挂机
			return flag;
		}

		@Override
		public void setHooktime(long _v_) { // 开始挂机时间
			hooktime = _v_;
		}

		@Override
		public void setRemainfivebeitime(long _v_) { // 点击使用5倍的时候封存5倍时间
			remainfivebeitime = _v_;
		}

		@Override
		public void setFivebeitimestart(long _v_) { // 开始使用五倍时间的时间
			fivebeitimestart = _v_;
		}

		@Override
		public void setFivebeitotalusetime(long _v_) { // 最后一次点击前总的使用时间
			fivebeitotalusetime = _v_;
		}

		@Override
		public void setFlag(int _v_) { // 0=无挂机或者已经领取了挂机经验 1=单倍挂机 2=5倍挂机
			flag = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof OffLineHook.Data)) return false;
			OffLineHook.Data _o_ = (OffLineHook.Data) _o1_;
			if (hooktime != _o_.hooktime) return false;
			if (remainfivebeitime != _o_.remainfivebeitime) return false;
			if (fivebeitimestart != _o_.fivebeitimestart) return false;
			if (fivebeitotalusetime != _o_.fivebeitotalusetime) return false;
			if (flag != _o_.flag) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += hooktime;
			_h_ += remainfivebeitime;
			_h_ += fivebeitimestart;
			_h_ += fivebeitotalusetime;
			_h_ += flag;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(hooktime);
			_sb_.append(",");
			_sb_.append(remainfivebeitime);
			_sb_.append(",");
			_sb_.append(fivebeitimestart);
			_sb_.append(",");
			_sb_.append(fivebeitotalusetime);
			_sb_.append(",");
			_sb_.append(flag);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
