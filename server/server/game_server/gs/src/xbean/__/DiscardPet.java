
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class DiscardPet extends mkdb.XBean implements xbean.DiscardPet {
	private xbean.PetInfo pet; // 宠物属性
	private long roleid; // 宠物删除时的主人
	private long deletedate; // 删除日期
	private int reason; // 删除原因

	@Override
	public void _reset_unsafe_() {
		pet._reset_unsafe_();
		roleid = 0L;
		deletedate = 0L;
		reason = 0;
	}

	DiscardPet(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		pet = new PetInfo(0, this, "pet");
	}

	public DiscardPet() {
		this(0, null, null);
	}

	public DiscardPet(DiscardPet _o_) {
		this(_o_, null, null);
	}

	DiscardPet(xbean.DiscardPet _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof DiscardPet) assign((DiscardPet)_o1_);
		else if (_o1_ instanceof DiscardPet.Data) assign((DiscardPet.Data)_o1_);
		else if (_o1_ instanceof DiscardPet.Const) assign(((DiscardPet.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(DiscardPet _o_) {
		_o_._xdb_verify_unsafe_();
		pet = new PetInfo(_o_.pet, this, "pet");
		roleid = _o_.roleid;
		deletedate = _o_.deletedate;
		reason = _o_.reason;
	}

	private void assign(DiscardPet.Data _o_) {
		pet = new PetInfo(_o_.pet, this, "pet");
		roleid = _o_.roleid;
		deletedate = _o_.deletedate;
		reason = _o_.reason;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		pet.marshal(_os_);
		_os_.marshal(roleid);
		_os_.marshal(deletedate);
		_os_.marshal(reason);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		pet.unmarshal(_os_);
		roleid = _os_.unmarshal_long();
		deletedate = _os_.unmarshal_long();
		reason = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.DiscardPet copy() {
		_xdb_verify_unsafe_();
		return new DiscardPet(this);
	}

	@Override
	public xbean.DiscardPet toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.DiscardPet toBean() {
		_xdb_verify_unsafe_();
		return new DiscardPet(this); // same as copy()
	}

	@Override
	public xbean.DiscardPet toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.DiscardPet toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public xbean.PetInfo getPet() { // 宠物属性
		_xdb_verify_unsafe_();
		return pet;
	}

	@Override
	public long getRoleid() { // 宠物删除时的主人
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public long getDeletedate() { // 删除日期
		_xdb_verify_unsafe_();
		return deletedate;
	}

	@Override
	public int getReason() { // 删除原因
		_xdb_verify_unsafe_();
		return reason;
	}

	@Override
	public void setRoleid(long _v_) { // 宠物删除时的主人
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setDeletedate(long _v_) { // 删除日期
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "deletedate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, deletedate) {
					public void rollback() { deletedate = _xdb_saved; }
				};}});
		deletedate = _v_;
	}

	@Override
	public void setReason(int _v_) { // 删除原因
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "reason") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, reason) {
					public void rollback() { reason = _xdb_saved; }
				};}});
		reason = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		DiscardPet _o_ = null;
		if ( _o1_ instanceof DiscardPet ) _o_ = (DiscardPet)_o1_;
		else if ( _o1_ instanceof DiscardPet.Const ) _o_ = ((DiscardPet.Const)_o1_).nThis();
		else return false;
		if (!pet.equals(_o_.pet)) return false;
		if (roleid != _o_.roleid) return false;
		if (deletedate != _o_.deletedate) return false;
		if (reason != _o_.reason) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += pet.hashCode();
		_h_ += roleid;
		_h_ += deletedate;
		_h_ += reason;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pet);
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(deletedate);
		_sb_.append(",");
		_sb_.append(reason);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("pet"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("deletedate"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("reason"));
		return lb;
	}

	private class Const implements xbean.DiscardPet {
		DiscardPet nThis() {
			return DiscardPet.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.DiscardPet copy() {
			return DiscardPet.this.copy();
		}

		@Override
		public xbean.DiscardPet toData() {
			return DiscardPet.this.toData();
		}

		public xbean.DiscardPet toBean() {
			return DiscardPet.this.toBean();
		}

		@Override
		public xbean.DiscardPet toDataIf() {
			return DiscardPet.this.toDataIf();
		}

		public xbean.DiscardPet toBeanIf() {
			return DiscardPet.this.toBeanIf();
		}

		@Override
		public xbean.PetInfo getPet() { // 宠物属性
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(pet);
		}

		@Override
		public long getRoleid() { // 宠物删除时的主人
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public long getDeletedate() { // 删除日期
			_xdb_verify_unsafe_();
			return deletedate;
		}

		@Override
		public int getReason() { // 删除原因
			_xdb_verify_unsafe_();
			return reason;
		}

		@Override
		public void setRoleid(long _v_) { // 宠物删除时的主人
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDeletedate(long _v_) { // 删除日期
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReason(int _v_) { // 删除原因
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
			return DiscardPet.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return DiscardPet.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return DiscardPet.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return DiscardPet.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return DiscardPet.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return DiscardPet.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return DiscardPet.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return DiscardPet.this.hashCode();
		}

		@Override
		public String toString() {
			return DiscardPet.this.toString();
		}

	}

	public static final class Data implements xbean.DiscardPet {
		private xbean.PetInfo pet; // 宠物属性
		private long roleid; // 宠物删除时的主人
		private long deletedate; // 删除日期
		private int reason; // 删除原因

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			pet = new PetInfo.Data();
		}

		Data(xbean.DiscardPet _o1_) {
			if (_o1_ instanceof DiscardPet) assign((DiscardPet)_o1_);
			else if (_o1_ instanceof DiscardPet.Data) assign((DiscardPet.Data)_o1_);
			else if (_o1_ instanceof DiscardPet.Const) assign(((DiscardPet.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(DiscardPet _o_) {
			pet = new PetInfo.Data(_o_.pet);
			roleid = _o_.roleid;
			deletedate = _o_.deletedate;
			reason = _o_.reason;
		}

		private void assign(DiscardPet.Data _o_) {
			pet = new PetInfo.Data(_o_.pet);
			roleid = _o_.roleid;
			deletedate = _o_.deletedate;
			reason = _o_.reason;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			pet.marshal(_os_);
			_os_.marshal(roleid);
			_os_.marshal(deletedate);
			_os_.marshal(reason);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			pet.unmarshal(_os_);
			roleid = _os_.unmarshal_long();
			deletedate = _os_.unmarshal_long();
			reason = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.DiscardPet copy() {
			return new Data(this);
		}

		@Override
		public xbean.DiscardPet toData() {
			return new Data(this);
		}

		public xbean.DiscardPet toBean() {
			return new DiscardPet(this, null, null);
		}

		@Override
		public xbean.DiscardPet toDataIf() {
			return this;
		}

		public xbean.DiscardPet toBeanIf() {
			return new DiscardPet(this, null, null);
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
		public xbean.PetInfo getPet() { // 宠物属性
			return pet;
		}

		@Override
		public long getRoleid() { // 宠物删除时的主人
			return roleid;
		}

		@Override
		public long getDeletedate() { // 删除日期
			return deletedate;
		}

		@Override
		public int getReason() { // 删除原因
			return reason;
		}

		@Override
		public void setRoleid(long _v_) { // 宠物删除时的主人
			roleid = _v_;
		}

		@Override
		public void setDeletedate(long _v_) { // 删除日期
			deletedate = _v_;
		}

		@Override
		public void setReason(int _v_) { // 删除原因
			reason = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof DiscardPet.Data)) return false;
			DiscardPet.Data _o_ = (DiscardPet.Data) _o1_;
			if (!pet.equals(_o_.pet)) return false;
			if (roleid != _o_.roleid) return false;
			if (deletedate != _o_.deletedate) return false;
			if (reason != _o_.reason) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += pet.hashCode();
			_h_ += roleid;
			_h_ += deletedate;
			_h_ += reason;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(pet);
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(deletedate);
			_sb_.append(",");
			_sb_.append(reason);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
