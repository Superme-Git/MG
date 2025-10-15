
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InstanceNpcSave extends mkdb.XBean implements xbean.InstanceNpcSave {
	private long npckey; // npckey
	private long roleid; // 有值表示被该角色占用,没有值表示空闲
	private int finishcount; // 已经完成的数量
	private int count; // 需要完成的数量

	@Override
	public void _reset_unsafe_() {
		npckey = 0L;
		roleid = 0L;
		finishcount = 0;
		count = 0;
	}

	InstanceNpcSave(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public InstanceNpcSave() {
		this(0, null, null);
	}

	public InstanceNpcSave(InstanceNpcSave _o_) {
		this(_o_, null, null);
	}

	InstanceNpcSave(xbean.InstanceNpcSave _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InstanceNpcSave) assign((InstanceNpcSave)_o1_);
		else if (_o1_ instanceof InstanceNpcSave.Data) assign((InstanceNpcSave.Data)_o1_);
		else if (_o1_ instanceof InstanceNpcSave.Const) assign(((InstanceNpcSave.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InstanceNpcSave _o_) {
		_o_._xdb_verify_unsafe_();
		npckey = _o_.npckey;
		roleid = _o_.roleid;
		finishcount = _o_.finishcount;
		count = _o_.count;
	}

	private void assign(InstanceNpcSave.Data _o_) {
		npckey = _o_.npckey;
		roleid = _o_.roleid;
		finishcount = _o_.finishcount;
		count = _o_.count;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(npckey);
		_os_.marshal(roleid);
		_os_.marshal(finishcount);
		_os_.marshal(count);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		npckey = _os_.unmarshal_long();
		roleid = _os_.unmarshal_long();
		finishcount = _os_.unmarshal_int();
		count = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.InstanceNpcSave copy() {
		_xdb_verify_unsafe_();
		return new InstanceNpcSave(this);
	}

	@Override
	public xbean.InstanceNpcSave toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceNpcSave toBean() {
		_xdb_verify_unsafe_();
		return new InstanceNpcSave(this); // same as copy()
	}

	@Override
	public xbean.InstanceNpcSave toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InstanceNpcSave toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getNpckey() { // npckey
		_xdb_verify_unsafe_();
		return npckey;
	}

	@Override
	public long getRoleid() { // 有值表示被该角色占用,没有值表示空闲
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public int getFinishcount() { // 已经完成的数量
		_xdb_verify_unsafe_();
		return finishcount;
	}

	@Override
	public int getCount() { // 需要完成的数量
		_xdb_verify_unsafe_();
		return count;
	}

	@Override
	public void setNpckey(long _v_) { // npckey
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "npckey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, npckey) {
					public void rollback() { npckey = _xdb_saved; }
				};}});
		npckey = _v_;
	}

	@Override
	public void setRoleid(long _v_) { // 有值表示被该角色占用,没有值表示空闲
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setFinishcount(int _v_) { // 已经完成的数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "finishcount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, finishcount) {
					public void rollback() { finishcount = _xdb_saved; }
				};}});
		finishcount = _v_;
	}

	@Override
	public void setCount(int _v_) { // 需要完成的数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "count") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, count) {
					public void rollback() { count = _xdb_saved; }
				};}});
		count = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InstanceNpcSave _o_ = null;
		if ( _o1_ instanceof InstanceNpcSave ) _o_ = (InstanceNpcSave)_o1_;
		else if ( _o1_ instanceof InstanceNpcSave.Const ) _o_ = ((InstanceNpcSave.Const)_o1_).nThis();
		else return false;
		if (npckey != _o_.npckey) return false;
		if (roleid != _o_.roleid) return false;
		if (finishcount != _o_.finishcount) return false;
		if (count != _o_.count) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += npckey;
		_h_ += roleid;
		_h_ += finishcount;
		_h_ += count;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey);
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(finishcount);
		_sb_.append(",");
		_sb_.append(count);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("npckey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("finishcount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("count"));
		return lb;
	}

	private class Const implements xbean.InstanceNpcSave {
		InstanceNpcSave nThis() {
			return InstanceNpcSave.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InstanceNpcSave copy() {
			return InstanceNpcSave.this.copy();
		}

		@Override
		public xbean.InstanceNpcSave toData() {
			return InstanceNpcSave.this.toData();
		}

		public xbean.InstanceNpcSave toBean() {
			return InstanceNpcSave.this.toBean();
		}

		@Override
		public xbean.InstanceNpcSave toDataIf() {
			return InstanceNpcSave.this.toDataIf();
		}

		public xbean.InstanceNpcSave toBeanIf() {
			return InstanceNpcSave.this.toBeanIf();
		}

		@Override
		public long getNpckey() { // npckey
			_xdb_verify_unsafe_();
			return npckey;
		}

		@Override
		public long getRoleid() { // 有值表示被该角色占用,没有值表示空闲
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getFinishcount() { // 已经完成的数量
			_xdb_verify_unsafe_();
			return finishcount;
		}

		@Override
		public int getCount() { // 需要完成的数量
			_xdb_verify_unsafe_();
			return count;
		}

		@Override
		public void setNpckey(long _v_) { // npckey
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // 有值表示被该角色占用,没有值表示空闲
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFinishcount(int _v_) { // 已经完成的数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCount(int _v_) { // 需要完成的数量
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
			return InstanceNpcSave.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InstanceNpcSave.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InstanceNpcSave.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InstanceNpcSave.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InstanceNpcSave.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InstanceNpcSave.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InstanceNpcSave.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InstanceNpcSave.this.hashCode();
		}

		@Override
		public String toString() {
			return InstanceNpcSave.this.toString();
		}

	}

	public static final class Data implements xbean.InstanceNpcSave {
		private long npckey; // npckey
		private long roleid; // 有值表示被该角色占用,没有值表示空闲
		private int finishcount; // 已经完成的数量
		private int count; // 需要完成的数量

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.InstanceNpcSave _o1_) {
			if (_o1_ instanceof InstanceNpcSave) assign((InstanceNpcSave)_o1_);
			else if (_o1_ instanceof InstanceNpcSave.Data) assign((InstanceNpcSave.Data)_o1_);
			else if (_o1_ instanceof InstanceNpcSave.Const) assign(((InstanceNpcSave.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InstanceNpcSave _o_) {
			npckey = _o_.npckey;
			roleid = _o_.roleid;
			finishcount = _o_.finishcount;
			count = _o_.count;
		}

		private void assign(InstanceNpcSave.Data _o_) {
			npckey = _o_.npckey;
			roleid = _o_.roleid;
			finishcount = _o_.finishcount;
			count = _o_.count;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(npckey);
			_os_.marshal(roleid);
			_os_.marshal(finishcount);
			_os_.marshal(count);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			npckey = _os_.unmarshal_long();
			roleid = _os_.unmarshal_long();
			finishcount = _os_.unmarshal_int();
			count = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.InstanceNpcSave copy() {
			return new Data(this);
		}

		@Override
		public xbean.InstanceNpcSave toData() {
			return new Data(this);
		}

		public xbean.InstanceNpcSave toBean() {
			return new InstanceNpcSave(this, null, null);
		}

		@Override
		public xbean.InstanceNpcSave toDataIf() {
			return this;
		}

		public xbean.InstanceNpcSave toBeanIf() {
			return new InstanceNpcSave(this, null, null);
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
		public long getNpckey() { // npckey
			return npckey;
		}

		@Override
		public long getRoleid() { // 有值表示被该角色占用,没有值表示空闲
			return roleid;
		}

		@Override
		public int getFinishcount() { // 已经完成的数量
			return finishcount;
		}

		@Override
		public int getCount() { // 需要完成的数量
			return count;
		}

		@Override
		public void setNpckey(long _v_) { // npckey
			npckey = _v_;
		}

		@Override
		public void setRoleid(long _v_) { // 有值表示被该角色占用,没有值表示空闲
			roleid = _v_;
		}

		@Override
		public void setFinishcount(int _v_) { // 已经完成的数量
			finishcount = _v_;
		}

		@Override
		public void setCount(int _v_) { // 需要完成的数量
			count = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InstanceNpcSave.Data)) return false;
			InstanceNpcSave.Data _o_ = (InstanceNpcSave.Data) _o1_;
			if (npckey != _o_.npckey) return false;
			if (roleid != _o_.roleid) return false;
			if (finishcount != _o_.finishcount) return false;
			if (count != _o_.count) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += npckey;
			_h_ += roleid;
			_h_ += finishcount;
			_h_ += count;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(npckey);
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(finishcount);
			_sb_.append(",");
			_sb_.append(count);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
