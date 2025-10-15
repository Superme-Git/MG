
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BattleAI extends mkdb.XBean implements xbean.BattleAI {
	private int id; // 
	private int count; // 成功执行的次数
	private int enableround; // 启用时的回合数

	@Override
	public void _reset_unsafe_() {
		id = 0;
		count = 0;
		enableround = 0;
	}

	BattleAI(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		count = 0;
		enableround = 0;
	}

	public BattleAI() {
		this(0, null, null);
	}

	public BattleAI(BattleAI _o_) {
		this(_o_, null, null);
	}

	BattleAI(xbean.BattleAI _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BattleAI) assign((BattleAI)_o1_);
		else if (_o1_ instanceof BattleAI.Data) assign((BattleAI.Data)_o1_);
		else if (_o1_ instanceof BattleAI.Const) assign(((BattleAI.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BattleAI _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		count = _o_.count;
		enableround = _o_.enableround;
	}

	private void assign(BattleAI.Data _o_) {
		id = _o_.id;
		count = _o_.count;
		enableround = _o_.enableround;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(count);
		_os_.marshal(enableround);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		count = _os_.unmarshal_int();
		enableround = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.BattleAI copy() {
		_xdb_verify_unsafe_();
		return new BattleAI(this);
	}

	@Override
	public xbean.BattleAI toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BattleAI toBean() {
		_xdb_verify_unsafe_();
		return new BattleAI(this); // same as copy()
	}

	@Override
	public xbean.BattleAI toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BattleAI toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getId() { // 
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public int getCount() { // 成功执行的次数
		_xdb_verify_unsafe_();
		return count;
	}

	@Override
	public int getEnableround() { // 启用时的回合数
		_xdb_verify_unsafe_();
		return enableround;
	}

	@Override
	public void setId(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setCount(int _v_) { // 成功执行的次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "count") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, count) {
					public void rollback() { count = _xdb_saved; }
				};}});
		count = _v_;
	}

	@Override
	public void setEnableround(int _v_) { // 启用时的回合数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "enableround") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, enableround) {
					public void rollback() { enableround = _xdb_saved; }
				};}});
		enableround = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BattleAI _o_ = null;
		if ( _o1_ instanceof BattleAI ) _o_ = (BattleAI)_o1_;
		else if ( _o1_ instanceof BattleAI.Const ) _o_ = ((BattleAI.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (count != _o_.count) return false;
		if (enableround != _o_.enableround) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += count;
		_h_ += enableround;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(count);
		_sb_.append(",");
		_sb_.append(enableround);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("count"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("enableround"));
		return lb;
	}

	private class Const implements xbean.BattleAI {
		BattleAI nThis() {
			return BattleAI.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BattleAI copy() {
			return BattleAI.this.copy();
		}

		@Override
		public xbean.BattleAI toData() {
			return BattleAI.this.toData();
		}

		public xbean.BattleAI toBean() {
			return BattleAI.this.toBean();
		}

		@Override
		public xbean.BattleAI toDataIf() {
			return BattleAI.this.toDataIf();
		}

		public xbean.BattleAI toBeanIf() {
			return BattleAI.this.toBeanIf();
		}

		@Override
		public int getId() { // 
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getCount() { // 成功执行的次数
			_xdb_verify_unsafe_();
			return count;
		}

		@Override
		public int getEnableround() { // 启用时的回合数
			_xdb_verify_unsafe_();
			return enableround;
		}

		@Override
		public void setId(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCount(int _v_) { // 成功执行的次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEnableround(int _v_) { // 启用时的回合数
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
			return BattleAI.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BattleAI.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BattleAI.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BattleAI.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BattleAI.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BattleAI.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BattleAI.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BattleAI.this.hashCode();
		}

		@Override
		public String toString() {
			return BattleAI.this.toString();
		}

	}

	public static final class Data implements xbean.BattleAI {
		private int id; // 
		private int count; // 成功执行的次数
		private int enableround; // 启用时的回合数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			count = 0;
			enableround = 0;
		}

		Data(xbean.BattleAI _o1_) {
			if (_o1_ instanceof BattleAI) assign((BattleAI)_o1_);
			else if (_o1_ instanceof BattleAI.Data) assign((BattleAI.Data)_o1_);
			else if (_o1_ instanceof BattleAI.Const) assign(((BattleAI.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BattleAI _o_) {
			id = _o_.id;
			count = _o_.count;
			enableround = _o_.enableround;
		}

		private void assign(BattleAI.Data _o_) {
			id = _o_.id;
			count = _o_.count;
			enableround = _o_.enableround;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(count);
			_os_.marshal(enableround);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			count = _os_.unmarshal_int();
			enableround = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.BattleAI copy() {
			return new Data(this);
		}

		@Override
		public xbean.BattleAI toData() {
			return new Data(this);
		}

		public xbean.BattleAI toBean() {
			return new BattleAI(this, null, null);
		}

		@Override
		public xbean.BattleAI toDataIf() {
			return this;
		}

		public xbean.BattleAI toBeanIf() {
			return new BattleAI(this, null, null);
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
		public int getId() { // 
			return id;
		}

		@Override
		public int getCount() { // 成功执行的次数
			return count;
		}

		@Override
		public int getEnableround() { // 启用时的回合数
			return enableround;
		}

		@Override
		public void setId(int _v_) { // 
			id = _v_;
		}

		@Override
		public void setCount(int _v_) { // 成功执行的次数
			count = _v_;
		}

		@Override
		public void setEnableround(int _v_) { // 启用时的回合数
			enableround = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BattleAI.Data)) return false;
			BattleAI.Data _o_ = (BattleAI.Data) _o1_;
			if (id != _o_.id) return false;
			if (count != _o_.count) return false;
			if (enableround != _o_.enableround) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += count;
			_h_ += enableround;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(count);
			_sb_.append(",");
			_sb_.append(enableround);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
