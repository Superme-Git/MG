
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Decision extends mkdb.XBean implements xbean.Decision {
	private int operatetype; // 操作类型
	private int operator; // 操作方的战斗单位索引
	private int aim; // 目标的战斗单位索引
	private int operateid; // 操作数，如使用物品的物品ID
	private boolean isauto; // 操作是否是自动

	@Override
	public void _reset_unsafe_() {
		operatetype = 0;
		operator = 0;
		aim = 0;
		operateid = 0;
		isauto = false;
	}

	Decision(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		isauto = false;
	}

	public Decision() {
		this(0, null, null);
	}

	public Decision(Decision _o_) {
		this(_o_, null, null);
	}

	Decision(xbean.Decision _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Decision) assign((Decision)_o1_);
		else if (_o1_ instanceof Decision.Data) assign((Decision.Data)_o1_);
		else if (_o1_ instanceof Decision.Const) assign(((Decision.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Decision _o_) {
		_o_._xdb_verify_unsafe_();
		operatetype = _o_.operatetype;
		operator = _o_.operator;
		aim = _o_.aim;
		operateid = _o_.operateid;
		isauto = _o_.isauto;
	}

	private void assign(Decision.Data _o_) {
		operatetype = _o_.operatetype;
		operator = _o_.operator;
		aim = _o_.aim;
		operateid = _o_.operateid;
		isauto = _o_.isauto;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(operatetype);
		_os_.marshal(operator);
		_os_.marshal(aim);
		_os_.marshal(operateid);
		_os_.marshal(isauto);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		operatetype = _os_.unmarshal_int();
		operator = _os_.unmarshal_int();
		aim = _os_.unmarshal_int();
		operateid = _os_.unmarshal_int();
		isauto = _os_.unmarshal_boolean();
		return _os_;
	}

	@Override
	public xbean.Decision copy() {
		_xdb_verify_unsafe_();
		return new Decision(this);
	}

	@Override
	public xbean.Decision toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Decision toBean() {
		_xdb_verify_unsafe_();
		return new Decision(this); // same as copy()
	}

	@Override
	public xbean.Decision toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Decision toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getOperatetype() { // 操作类型
		_xdb_verify_unsafe_();
		return operatetype;
	}

	@Override
	public int getOperator() { // 操作方的战斗单位索引
		_xdb_verify_unsafe_();
		return operator;
	}

	@Override
	public int getAim() { // 目标的战斗单位索引
		_xdb_verify_unsafe_();
		return aim;
	}

	@Override
	public int getOperateid() { // 操作数，如使用物品的物品ID
		_xdb_verify_unsafe_();
		return operateid;
	}

	@Override
	public boolean getIsauto() { // 操作是否是自动
		_xdb_verify_unsafe_();
		return isauto;
	}

	@Override
	public void setOperatetype(int _v_) { // 操作类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "operatetype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, operatetype) {
					public void rollback() { operatetype = _xdb_saved; }
				};}});
		operatetype = _v_;
	}

	@Override
	public void setOperator(int _v_) { // 操作方的战斗单位索引
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "operator") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, operator) {
					public void rollback() { operator = _xdb_saved; }
				};}});
		operator = _v_;
	}

	@Override
	public void setAim(int _v_) { // 目标的战斗单位索引
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "aim") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, aim) {
					public void rollback() { aim = _xdb_saved; }
				};}});
		aim = _v_;
	}

	@Override
	public void setOperateid(int _v_) { // 操作数，如使用物品的物品ID
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "operateid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, operateid) {
					public void rollback() { operateid = _xdb_saved; }
				};}});
		operateid = _v_;
	}

	@Override
	public void setIsauto(boolean _v_) { // 操作是否是自动
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "isauto") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, isauto) {
					public void rollback() { isauto = _xdb_saved; }
				};}});
		isauto = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Decision _o_ = null;
		if ( _o1_ instanceof Decision ) _o_ = (Decision)_o1_;
		else if ( _o1_ instanceof Decision.Const ) _o_ = ((Decision.Const)_o1_).nThis();
		else return false;
		if (operatetype != _o_.operatetype) return false;
		if (operator != _o_.operator) return false;
		if (aim != _o_.aim) return false;
		if (operateid != _o_.operateid) return false;
		if (isauto != _o_.isauto) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += operatetype;
		_h_ += operator;
		_h_ += aim;
		_h_ += operateid;
		_h_ += isauto ? 1231 : 1237;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(operatetype);
		_sb_.append(",");
		_sb_.append(operator);
		_sb_.append(",");
		_sb_.append(aim);
		_sb_.append(",");
		_sb_.append(operateid);
		_sb_.append(",");
		_sb_.append(isauto);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("operatetype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("operator"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("aim"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("operateid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("isauto"));
		return lb;
	}

	private class Const implements xbean.Decision {
		Decision nThis() {
			return Decision.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Decision copy() {
			return Decision.this.copy();
		}

		@Override
		public xbean.Decision toData() {
			return Decision.this.toData();
		}

		public xbean.Decision toBean() {
			return Decision.this.toBean();
		}

		@Override
		public xbean.Decision toDataIf() {
			return Decision.this.toDataIf();
		}

		public xbean.Decision toBeanIf() {
			return Decision.this.toBeanIf();
		}

		@Override
		public int getOperatetype() { // 操作类型
			_xdb_verify_unsafe_();
			return operatetype;
		}

		@Override
		public int getOperator() { // 操作方的战斗单位索引
			_xdb_verify_unsafe_();
			return operator;
		}

		@Override
		public int getAim() { // 目标的战斗单位索引
			_xdb_verify_unsafe_();
			return aim;
		}

		@Override
		public int getOperateid() { // 操作数，如使用物品的物品ID
			_xdb_verify_unsafe_();
			return operateid;
		}

		@Override
		public boolean getIsauto() { // 操作是否是自动
			_xdb_verify_unsafe_();
			return isauto;
		}

		@Override
		public void setOperatetype(int _v_) { // 操作类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOperator(int _v_) { // 操作方的战斗单位索引
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAim(int _v_) { // 目标的战斗单位索引
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOperateid(int _v_) { // 操作数，如使用物品的物品ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIsauto(boolean _v_) { // 操作是否是自动
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
			return Decision.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Decision.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Decision.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Decision.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Decision.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Decision.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Decision.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Decision.this.hashCode();
		}

		@Override
		public String toString() {
			return Decision.this.toString();
		}

	}

	public static final class Data implements xbean.Decision {
		private int operatetype; // 操作类型
		private int operator; // 操作方的战斗单位索引
		private int aim; // 目标的战斗单位索引
		private int operateid; // 操作数，如使用物品的物品ID
		private boolean isauto; // 操作是否是自动

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			isauto = false;
		}

		Data(xbean.Decision _o1_) {
			if (_o1_ instanceof Decision) assign((Decision)_o1_);
			else if (_o1_ instanceof Decision.Data) assign((Decision.Data)_o1_);
			else if (_o1_ instanceof Decision.Const) assign(((Decision.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Decision _o_) {
			operatetype = _o_.operatetype;
			operator = _o_.operator;
			aim = _o_.aim;
			operateid = _o_.operateid;
			isauto = _o_.isauto;
		}

		private void assign(Decision.Data _o_) {
			operatetype = _o_.operatetype;
			operator = _o_.operator;
			aim = _o_.aim;
			operateid = _o_.operateid;
			isauto = _o_.isauto;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(operatetype);
			_os_.marshal(operator);
			_os_.marshal(aim);
			_os_.marshal(operateid);
			_os_.marshal(isauto);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			operatetype = _os_.unmarshal_int();
			operator = _os_.unmarshal_int();
			aim = _os_.unmarshal_int();
			operateid = _os_.unmarshal_int();
			isauto = _os_.unmarshal_boolean();
			return _os_;
		}

		@Override
		public xbean.Decision copy() {
			return new Data(this);
		}

		@Override
		public xbean.Decision toData() {
			return new Data(this);
		}

		public xbean.Decision toBean() {
			return new Decision(this, null, null);
		}

		@Override
		public xbean.Decision toDataIf() {
			return this;
		}

		public xbean.Decision toBeanIf() {
			return new Decision(this, null, null);
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
		public int getOperatetype() { // 操作类型
			return operatetype;
		}

		@Override
		public int getOperator() { // 操作方的战斗单位索引
			return operator;
		}

		@Override
		public int getAim() { // 目标的战斗单位索引
			return aim;
		}

		@Override
		public int getOperateid() { // 操作数，如使用物品的物品ID
			return operateid;
		}

		@Override
		public boolean getIsauto() { // 操作是否是自动
			return isauto;
		}

		@Override
		public void setOperatetype(int _v_) { // 操作类型
			operatetype = _v_;
		}

		@Override
		public void setOperator(int _v_) { // 操作方的战斗单位索引
			operator = _v_;
		}

		@Override
		public void setAim(int _v_) { // 目标的战斗单位索引
			aim = _v_;
		}

		@Override
		public void setOperateid(int _v_) { // 操作数，如使用物品的物品ID
			operateid = _v_;
		}

		@Override
		public void setIsauto(boolean _v_) { // 操作是否是自动
			isauto = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Decision.Data)) return false;
			Decision.Data _o_ = (Decision.Data) _o1_;
			if (operatetype != _o_.operatetype) return false;
			if (operator != _o_.operator) return false;
			if (aim != _o_.aim) return false;
			if (operateid != _o_.operateid) return false;
			if (isauto != _o_.isauto) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += operatetype;
			_h_ += operator;
			_h_ += aim;
			_h_ += operateid;
			_h_ += isauto ? 1231 : 1237;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(operatetype);
			_sb_.append(",");
			_sb_.append(operator);
			_sb_.append(",");
			_sb_.append(aim);
			_sb_.append(",");
			_sb_.append(operateid);
			_sb_.append(",");
			_sb_.append(isauto);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
