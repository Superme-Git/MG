
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ChargeReturnProfit extends mkdb.XBean implements xbean.ChargeReturnProfit {
	private int id; // 
	private int value; // 
	private int maxvalue; // 
	private int status; // 0是领取了1是未领取2是未到达 by changhao

	@Override
	public void _reset_unsafe_() {
		id = 0;
		value = 0;
		maxvalue = 0;
		status = 0;
	}

	ChargeReturnProfit(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ChargeReturnProfit() {
		this(0, null, null);
	}

	public ChargeReturnProfit(ChargeReturnProfit _o_) {
		this(_o_, null, null);
	}

	ChargeReturnProfit(xbean.ChargeReturnProfit _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ChargeReturnProfit) assign((ChargeReturnProfit)_o1_);
		else if (_o1_ instanceof ChargeReturnProfit.Data) assign((ChargeReturnProfit.Data)_o1_);
		else if (_o1_ instanceof ChargeReturnProfit.Const) assign(((ChargeReturnProfit.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ChargeReturnProfit _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		value = _o_.value;
		maxvalue = _o_.maxvalue;
		status = _o_.status;
	}

	private void assign(ChargeReturnProfit.Data _o_) {
		id = _o_.id;
		value = _o_.value;
		maxvalue = _o_.maxvalue;
		status = _o_.status;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(value);
		_os_.marshal(maxvalue);
		_os_.marshal(status);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		value = _os_.unmarshal_int();
		maxvalue = _os_.unmarshal_int();
		status = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ChargeReturnProfit copy() {
		_xdb_verify_unsafe_();
		return new ChargeReturnProfit(this);
	}

	@Override
	public xbean.ChargeReturnProfit toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChargeReturnProfit toBean() {
		_xdb_verify_unsafe_();
		return new ChargeReturnProfit(this); // same as copy()
	}

	@Override
	public xbean.ChargeReturnProfit toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChargeReturnProfit toBeanIf() {
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
	public int getValue() { // 
		_xdb_verify_unsafe_();
		return value;
	}

	@Override
	public int getMaxvalue() { // 
		_xdb_verify_unsafe_();
		return maxvalue;
	}

	@Override
	public int getStatus() { // 0是领取了1是未领取2是未到达 by changhao
		_xdb_verify_unsafe_();
		return status;
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
	public void setValue(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "value") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, value) {
					public void rollback() { value = _xdb_saved; }
				};}});
		value = _v_;
	}

	@Override
	public void setMaxvalue(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "maxvalue") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, maxvalue) {
					public void rollback() { maxvalue = _xdb_saved; }
				};}});
		maxvalue = _v_;
	}

	@Override
	public void setStatus(int _v_) { // 0是领取了1是未领取2是未到达 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "status") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, status) {
					public void rollback() { status = _xdb_saved; }
				};}});
		status = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ChargeReturnProfit _o_ = null;
		if ( _o1_ instanceof ChargeReturnProfit ) _o_ = (ChargeReturnProfit)_o1_;
		else if ( _o1_ instanceof ChargeReturnProfit.Const ) _o_ = ((ChargeReturnProfit.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (value != _o_.value) return false;
		if (maxvalue != _o_.maxvalue) return false;
		if (status != _o_.status) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += value;
		_h_ += maxvalue;
		_h_ += status;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(value);
		_sb_.append(",");
		_sb_.append(maxvalue);
		_sb_.append(",");
		_sb_.append(status);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("value"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("maxvalue"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("status"));
		return lb;
	}

	private class Const implements xbean.ChargeReturnProfit {
		ChargeReturnProfit nThis() {
			return ChargeReturnProfit.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ChargeReturnProfit copy() {
			return ChargeReturnProfit.this.copy();
		}

		@Override
		public xbean.ChargeReturnProfit toData() {
			return ChargeReturnProfit.this.toData();
		}

		public xbean.ChargeReturnProfit toBean() {
			return ChargeReturnProfit.this.toBean();
		}

		@Override
		public xbean.ChargeReturnProfit toDataIf() {
			return ChargeReturnProfit.this.toDataIf();
		}

		public xbean.ChargeReturnProfit toBeanIf() {
			return ChargeReturnProfit.this.toBeanIf();
		}

		@Override
		public int getId() { // 
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getValue() { // 
			_xdb_verify_unsafe_();
			return value;
		}

		@Override
		public int getMaxvalue() { // 
			_xdb_verify_unsafe_();
			return maxvalue;
		}

		@Override
		public int getStatus() { // 0是领取了1是未领取2是未到达 by changhao
			_xdb_verify_unsafe_();
			return status;
		}

		@Override
		public void setId(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setValue(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMaxvalue(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStatus(int _v_) { // 0是领取了1是未领取2是未到达 by changhao
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
			return ChargeReturnProfit.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ChargeReturnProfit.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ChargeReturnProfit.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ChargeReturnProfit.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ChargeReturnProfit.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ChargeReturnProfit.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ChargeReturnProfit.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ChargeReturnProfit.this.hashCode();
		}

		@Override
		public String toString() {
			return ChargeReturnProfit.this.toString();
		}

	}

	public static final class Data implements xbean.ChargeReturnProfit {
		private int id; // 
		private int value; // 
		private int maxvalue; // 
		private int status; // 0是领取了1是未领取2是未到达 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ChargeReturnProfit _o1_) {
			if (_o1_ instanceof ChargeReturnProfit) assign((ChargeReturnProfit)_o1_);
			else if (_o1_ instanceof ChargeReturnProfit.Data) assign((ChargeReturnProfit.Data)_o1_);
			else if (_o1_ instanceof ChargeReturnProfit.Const) assign(((ChargeReturnProfit.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ChargeReturnProfit _o_) {
			id = _o_.id;
			value = _o_.value;
			maxvalue = _o_.maxvalue;
			status = _o_.status;
		}

		private void assign(ChargeReturnProfit.Data _o_) {
			id = _o_.id;
			value = _o_.value;
			maxvalue = _o_.maxvalue;
			status = _o_.status;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(value);
			_os_.marshal(maxvalue);
			_os_.marshal(status);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			value = _os_.unmarshal_int();
			maxvalue = _os_.unmarshal_int();
			status = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ChargeReturnProfit copy() {
			return new Data(this);
		}

		@Override
		public xbean.ChargeReturnProfit toData() {
			return new Data(this);
		}

		public xbean.ChargeReturnProfit toBean() {
			return new ChargeReturnProfit(this, null, null);
		}

		@Override
		public xbean.ChargeReturnProfit toDataIf() {
			return this;
		}

		public xbean.ChargeReturnProfit toBeanIf() {
			return new ChargeReturnProfit(this, null, null);
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
		public int getValue() { // 
			return value;
		}

		@Override
		public int getMaxvalue() { // 
			return maxvalue;
		}

		@Override
		public int getStatus() { // 0是领取了1是未领取2是未到达 by changhao
			return status;
		}

		@Override
		public void setId(int _v_) { // 
			id = _v_;
		}

		@Override
		public void setValue(int _v_) { // 
			value = _v_;
		}

		@Override
		public void setMaxvalue(int _v_) { // 
			maxvalue = _v_;
		}

		@Override
		public void setStatus(int _v_) { // 0是领取了1是未领取2是未到达 by changhao
			status = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ChargeReturnProfit.Data)) return false;
			ChargeReturnProfit.Data _o_ = (ChargeReturnProfit.Data) _o1_;
			if (id != _o_.id) return false;
			if (value != _o_.value) return false;
			if (maxvalue != _o_.maxvalue) return false;
			if (status != _o_.status) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += value;
			_h_ += maxvalue;
			_h_ += status;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(value);
			_sb_.append(",");
			_sb_.append(maxvalue);
			_sb_.append(",");
			_sb_.append(status);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
