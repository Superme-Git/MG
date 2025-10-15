
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ModifyNameRole extends mkdb.XBean implements xbean.ModifyNameRole {
	private long lastbuytime; // 上次购买的时间
	private long lastmodtime; // 上次改名的时间
	private int buycount; // 记录已经购买的次数
	private int modcount; // 记录已经修改名字的次数

	@Override
	public void _reset_unsafe_() {
		lastbuytime = 0L;
		lastmodtime = 0L;
		buycount = 0;
		modcount = 0;
	}

	ModifyNameRole(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		buycount = 0;
		modcount = 0;
	}

	public ModifyNameRole() {
		this(0, null, null);
	}

	public ModifyNameRole(ModifyNameRole _o_) {
		this(_o_, null, null);
	}

	ModifyNameRole(xbean.ModifyNameRole _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ModifyNameRole) assign((ModifyNameRole)_o1_);
		else if (_o1_ instanceof ModifyNameRole.Data) assign((ModifyNameRole.Data)_o1_);
		else if (_o1_ instanceof ModifyNameRole.Const) assign(((ModifyNameRole.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ModifyNameRole _o_) {
		_o_._xdb_verify_unsafe_();
		lastbuytime = _o_.lastbuytime;
		lastmodtime = _o_.lastmodtime;
		buycount = _o_.buycount;
		modcount = _o_.modcount;
	}

	private void assign(ModifyNameRole.Data _o_) {
		lastbuytime = _o_.lastbuytime;
		lastmodtime = _o_.lastmodtime;
		buycount = _o_.buycount;
		modcount = _o_.modcount;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(lastbuytime);
		_os_.marshal(lastmodtime);
		_os_.marshal(buycount);
		_os_.marshal(modcount);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		lastbuytime = _os_.unmarshal_long();
		lastmodtime = _os_.unmarshal_long();
		buycount = _os_.unmarshal_int();
		modcount = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ModifyNameRole copy() {
		_xdb_verify_unsafe_();
		return new ModifyNameRole(this);
	}

	@Override
	public xbean.ModifyNameRole toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ModifyNameRole toBean() {
		_xdb_verify_unsafe_();
		return new ModifyNameRole(this); // same as copy()
	}

	@Override
	public xbean.ModifyNameRole toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ModifyNameRole toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getLastbuytime() { // 上次购买的时间
		_xdb_verify_unsafe_();
		return lastbuytime;
	}

	@Override
	public long getLastmodtime() { // 上次改名的时间
		_xdb_verify_unsafe_();
		return lastmodtime;
	}

	@Override
	public int getBuycount() { // 记录已经购买的次数
		_xdb_verify_unsafe_();
		return buycount;
	}

	@Override
	public int getModcount() { // 记录已经修改名字的次数
		_xdb_verify_unsafe_();
		return modcount;
	}

	@Override
	public void setLastbuytime(long _v_) { // 上次购买的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastbuytime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastbuytime) {
					public void rollback() { lastbuytime = _xdb_saved; }
				};}});
		lastbuytime = _v_;
	}

	@Override
	public void setLastmodtime(long _v_) { // 上次改名的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastmodtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastmodtime) {
					public void rollback() { lastmodtime = _xdb_saved; }
				};}});
		lastmodtime = _v_;
	}

	@Override
	public void setBuycount(int _v_) { // 记录已经购买的次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "buycount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, buycount) {
					public void rollback() { buycount = _xdb_saved; }
				};}});
		buycount = _v_;
	}

	@Override
	public void setModcount(int _v_) { // 记录已经修改名字的次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "modcount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, modcount) {
					public void rollback() { modcount = _xdb_saved; }
				};}});
		modcount = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ModifyNameRole _o_ = null;
		if ( _o1_ instanceof ModifyNameRole ) _o_ = (ModifyNameRole)_o1_;
		else if ( _o1_ instanceof ModifyNameRole.Const ) _o_ = ((ModifyNameRole.Const)_o1_).nThis();
		else return false;
		if (lastbuytime != _o_.lastbuytime) return false;
		if (lastmodtime != _o_.lastmodtime) return false;
		if (buycount != _o_.buycount) return false;
		if (modcount != _o_.modcount) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += lastbuytime;
		_h_ += lastmodtime;
		_h_ += buycount;
		_h_ += modcount;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(lastbuytime);
		_sb_.append(",");
		_sb_.append(lastmodtime);
		_sb_.append(",");
		_sb_.append(buycount);
		_sb_.append(",");
		_sb_.append(modcount);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastbuytime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastmodtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("buycount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("modcount"));
		return lb;
	}

	private class Const implements xbean.ModifyNameRole {
		ModifyNameRole nThis() {
			return ModifyNameRole.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ModifyNameRole copy() {
			return ModifyNameRole.this.copy();
		}

		@Override
		public xbean.ModifyNameRole toData() {
			return ModifyNameRole.this.toData();
		}

		public xbean.ModifyNameRole toBean() {
			return ModifyNameRole.this.toBean();
		}

		@Override
		public xbean.ModifyNameRole toDataIf() {
			return ModifyNameRole.this.toDataIf();
		}

		public xbean.ModifyNameRole toBeanIf() {
			return ModifyNameRole.this.toBeanIf();
		}

		@Override
		public long getLastbuytime() { // 上次购买的时间
			_xdb_verify_unsafe_();
			return lastbuytime;
		}

		@Override
		public long getLastmodtime() { // 上次改名的时间
			_xdb_verify_unsafe_();
			return lastmodtime;
		}

		@Override
		public int getBuycount() { // 记录已经购买的次数
			_xdb_verify_unsafe_();
			return buycount;
		}

		@Override
		public int getModcount() { // 记录已经修改名字的次数
			_xdb_verify_unsafe_();
			return modcount;
		}

		@Override
		public void setLastbuytime(long _v_) { // 上次购买的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastmodtime(long _v_) { // 上次改名的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBuycount(int _v_) { // 记录已经购买的次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setModcount(int _v_) { // 记录已经修改名字的次数
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
			return ModifyNameRole.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ModifyNameRole.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ModifyNameRole.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ModifyNameRole.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ModifyNameRole.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ModifyNameRole.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ModifyNameRole.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ModifyNameRole.this.hashCode();
		}

		@Override
		public String toString() {
			return ModifyNameRole.this.toString();
		}

	}

	public static final class Data implements xbean.ModifyNameRole {
		private long lastbuytime; // 上次购买的时间
		private long lastmodtime; // 上次改名的时间
		private int buycount; // 记录已经购买的次数
		private int modcount; // 记录已经修改名字的次数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			buycount = 0;
			modcount = 0;
		}

		Data(xbean.ModifyNameRole _o1_) {
			if (_o1_ instanceof ModifyNameRole) assign((ModifyNameRole)_o1_);
			else if (_o1_ instanceof ModifyNameRole.Data) assign((ModifyNameRole.Data)_o1_);
			else if (_o1_ instanceof ModifyNameRole.Const) assign(((ModifyNameRole.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ModifyNameRole _o_) {
			lastbuytime = _o_.lastbuytime;
			lastmodtime = _o_.lastmodtime;
			buycount = _o_.buycount;
			modcount = _o_.modcount;
		}

		private void assign(ModifyNameRole.Data _o_) {
			lastbuytime = _o_.lastbuytime;
			lastmodtime = _o_.lastmodtime;
			buycount = _o_.buycount;
			modcount = _o_.modcount;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(lastbuytime);
			_os_.marshal(lastmodtime);
			_os_.marshal(buycount);
			_os_.marshal(modcount);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			lastbuytime = _os_.unmarshal_long();
			lastmodtime = _os_.unmarshal_long();
			buycount = _os_.unmarshal_int();
			modcount = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ModifyNameRole copy() {
			return new Data(this);
		}

		@Override
		public xbean.ModifyNameRole toData() {
			return new Data(this);
		}

		public xbean.ModifyNameRole toBean() {
			return new ModifyNameRole(this, null, null);
		}

		@Override
		public xbean.ModifyNameRole toDataIf() {
			return this;
		}

		public xbean.ModifyNameRole toBeanIf() {
			return new ModifyNameRole(this, null, null);
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
		public long getLastbuytime() { // 上次购买的时间
			return lastbuytime;
		}

		@Override
		public long getLastmodtime() { // 上次改名的时间
			return lastmodtime;
		}

		@Override
		public int getBuycount() { // 记录已经购买的次数
			return buycount;
		}

		@Override
		public int getModcount() { // 记录已经修改名字的次数
			return modcount;
		}

		@Override
		public void setLastbuytime(long _v_) { // 上次购买的时间
			lastbuytime = _v_;
		}

		@Override
		public void setLastmodtime(long _v_) { // 上次改名的时间
			lastmodtime = _v_;
		}

		@Override
		public void setBuycount(int _v_) { // 记录已经购买的次数
			buycount = _v_;
		}

		@Override
		public void setModcount(int _v_) { // 记录已经修改名字的次数
			modcount = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ModifyNameRole.Data)) return false;
			ModifyNameRole.Data _o_ = (ModifyNameRole.Data) _o1_;
			if (lastbuytime != _o_.lastbuytime) return false;
			if (lastmodtime != _o_.lastmodtime) return false;
			if (buycount != _o_.buycount) return false;
			if (modcount != _o_.modcount) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += lastbuytime;
			_h_ += lastmodtime;
			_h_ += buycount;
			_h_ += modcount;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(lastbuytime);
			_sb_.append(",");
			_sb_.append(lastmodtime);
			_sb_.append(",");
			_sb_.append(buycount);
			_sb_.append(",");
			_sb_.append(modcount);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
