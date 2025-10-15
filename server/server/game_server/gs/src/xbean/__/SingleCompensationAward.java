
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class SingleCompensationAward extends mkdb.XBean implements xbean.SingleCompensationAward {
	private int type; // 奖励类型
	private long id; // 奖励id,类型是物品时为物品id
	private long num; // 奖励数量
	private long flag; // 奖励标记,类型是物品时有绑定标记

	@Override
	public void _reset_unsafe_() {
		type = 0;
		id = 0L;
		num = 0L;
		flag = 0L;
	}

	SingleCompensationAward(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public SingleCompensationAward() {
		this(0, null, null);
	}

	public SingleCompensationAward(SingleCompensationAward _o_) {
		this(_o_, null, null);
	}

	SingleCompensationAward(xbean.SingleCompensationAward _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof SingleCompensationAward) assign((SingleCompensationAward)_o1_);
		else if (_o1_ instanceof SingleCompensationAward.Data) assign((SingleCompensationAward.Data)_o1_);
		else if (_o1_ instanceof SingleCompensationAward.Const) assign(((SingleCompensationAward.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(SingleCompensationAward _o_) {
		_o_._xdb_verify_unsafe_();
		type = _o_.type;
		id = _o_.id;
		num = _o_.num;
		flag = _o_.flag;
	}

	private void assign(SingleCompensationAward.Data _o_) {
		type = _o_.type;
		id = _o_.id;
		num = _o_.num;
		flag = _o_.flag;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(type);
		_os_.marshal(id);
		_os_.marshal(num);
		_os_.marshal(flag);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		type = _os_.unmarshal_int();
		id = _os_.unmarshal_long();
		num = _os_.unmarshal_long();
		flag = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.SingleCompensationAward copy() {
		_xdb_verify_unsafe_();
		return new SingleCompensationAward(this);
	}

	@Override
	public xbean.SingleCompensationAward toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SingleCompensationAward toBean() {
		_xdb_verify_unsafe_();
		return new SingleCompensationAward(this); // same as copy()
	}

	@Override
	public xbean.SingleCompensationAward toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.SingleCompensationAward toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getType() { // 奖励类型
		_xdb_verify_unsafe_();
		return type;
	}

	@Override
	public long getId() { // 奖励id,类型是物品时为物品id
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public long getNum() { // 奖励数量
		_xdb_verify_unsafe_();
		return num;
	}

	@Override
	public long getFlag() { // 奖励标记,类型是物品时有绑定标记
		_xdb_verify_unsafe_();
		return flag;
	}

	@Override
	public void setType(int _v_) { // 奖励类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "type") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, type) {
					public void rollback() { type = _xdb_saved; }
				};}});
		type = _v_;
	}

	@Override
	public void setId(long _v_) { // 奖励id,类型是物品时为物品id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setNum(long _v_) { // 奖励数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "num") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, num) {
					public void rollback() { num = _xdb_saved; }
				};}});
		num = _v_;
	}

	@Override
	public void setFlag(long _v_) { // 奖励标记,类型是物品时有绑定标记
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "flag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, flag) {
					public void rollback() { flag = _xdb_saved; }
				};}});
		flag = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		SingleCompensationAward _o_ = null;
		if ( _o1_ instanceof SingleCompensationAward ) _o_ = (SingleCompensationAward)_o1_;
		else if ( _o1_ instanceof SingleCompensationAward.Const ) _o_ = ((SingleCompensationAward.Const)_o1_).nThis();
		else return false;
		if (type != _o_.type) return false;
		if (id != _o_.id) return false;
		if (num != _o_.num) return false;
		if (flag != _o_.flag) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += type;
		_h_ += id;
		_h_ += num;
		_h_ += flag;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(type);
		_sb_.append(",");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(num);
		_sb_.append(",");
		_sb_.append(flag);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("type"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("num"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("flag"));
		return lb;
	}

	private class Const implements xbean.SingleCompensationAward {
		SingleCompensationAward nThis() {
			return SingleCompensationAward.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.SingleCompensationAward copy() {
			return SingleCompensationAward.this.copy();
		}

		@Override
		public xbean.SingleCompensationAward toData() {
			return SingleCompensationAward.this.toData();
		}

		public xbean.SingleCompensationAward toBean() {
			return SingleCompensationAward.this.toBean();
		}

		@Override
		public xbean.SingleCompensationAward toDataIf() {
			return SingleCompensationAward.this.toDataIf();
		}

		public xbean.SingleCompensationAward toBeanIf() {
			return SingleCompensationAward.this.toBeanIf();
		}

		@Override
		public int getType() { // 奖励类型
			_xdb_verify_unsafe_();
			return type;
		}

		@Override
		public long getId() { // 奖励id,类型是物品时为物品id
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public long getNum() { // 奖励数量
			_xdb_verify_unsafe_();
			return num;
		}

		@Override
		public long getFlag() { // 奖励标记,类型是物品时有绑定标记
			_xdb_verify_unsafe_();
			return flag;
		}

		@Override
		public void setType(int _v_) { // 奖励类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setId(long _v_) { // 奖励id,类型是物品时为物品id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNum(long _v_) { // 奖励数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFlag(long _v_) { // 奖励标记,类型是物品时有绑定标记
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
			return SingleCompensationAward.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return SingleCompensationAward.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return SingleCompensationAward.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return SingleCompensationAward.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return SingleCompensationAward.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return SingleCompensationAward.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return SingleCompensationAward.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return SingleCompensationAward.this.hashCode();
		}

		@Override
		public String toString() {
			return SingleCompensationAward.this.toString();
		}

	}

	public static final class Data implements xbean.SingleCompensationAward {
		private int type; // 奖励类型
		private long id; // 奖励id,类型是物品时为物品id
		private long num; // 奖励数量
		private long flag; // 奖励标记,类型是物品时有绑定标记

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.SingleCompensationAward _o1_) {
			if (_o1_ instanceof SingleCompensationAward) assign((SingleCompensationAward)_o1_);
			else if (_o1_ instanceof SingleCompensationAward.Data) assign((SingleCompensationAward.Data)_o1_);
			else if (_o1_ instanceof SingleCompensationAward.Const) assign(((SingleCompensationAward.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(SingleCompensationAward _o_) {
			type = _o_.type;
			id = _o_.id;
			num = _o_.num;
			flag = _o_.flag;
		}

		private void assign(SingleCompensationAward.Data _o_) {
			type = _o_.type;
			id = _o_.id;
			num = _o_.num;
			flag = _o_.flag;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(type);
			_os_.marshal(id);
			_os_.marshal(num);
			_os_.marshal(flag);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			type = _os_.unmarshal_int();
			id = _os_.unmarshal_long();
			num = _os_.unmarshal_long();
			flag = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.SingleCompensationAward copy() {
			return new Data(this);
		}

		@Override
		public xbean.SingleCompensationAward toData() {
			return new Data(this);
		}

		public xbean.SingleCompensationAward toBean() {
			return new SingleCompensationAward(this, null, null);
		}

		@Override
		public xbean.SingleCompensationAward toDataIf() {
			return this;
		}

		public xbean.SingleCompensationAward toBeanIf() {
			return new SingleCompensationAward(this, null, null);
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
		public int getType() { // 奖励类型
			return type;
		}

		@Override
		public long getId() { // 奖励id,类型是物品时为物品id
			return id;
		}

		@Override
		public long getNum() { // 奖励数量
			return num;
		}

		@Override
		public long getFlag() { // 奖励标记,类型是物品时有绑定标记
			return flag;
		}

		@Override
		public void setType(int _v_) { // 奖励类型
			type = _v_;
		}

		@Override
		public void setId(long _v_) { // 奖励id,类型是物品时为物品id
			id = _v_;
		}

		@Override
		public void setNum(long _v_) { // 奖励数量
			num = _v_;
		}

		@Override
		public void setFlag(long _v_) { // 奖励标记,类型是物品时有绑定标记
			flag = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof SingleCompensationAward.Data)) return false;
			SingleCompensationAward.Data _o_ = (SingleCompensationAward.Data) _o1_;
			if (type != _o_.type) return false;
			if (id != _o_.id) return false;
			if (num != _o_.num) return false;
			if (flag != _o_.flag) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += type;
			_h_ += id;
			_h_ += num;
			_h_ += flag;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(type);
			_sb_.append(",");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(num);
			_sb_.append(",");
			_sb_.append(flag);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
