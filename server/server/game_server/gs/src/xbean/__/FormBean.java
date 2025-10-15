
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class FormBean extends mkdb.XBean implements xbean.FormBean {
	private int activetimes; // 
	private int level; // 等级
	private int exp; // 经验 by changhao

	@Override
	public void _reset_unsafe_() {
		activetimes = 0;
		level = 0;
		exp = 0;
	}

	FormBean(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public FormBean() {
		this(0, null, null);
	}

	public FormBean(FormBean _o_) {
		this(_o_, null, null);
	}

	FormBean(xbean.FormBean _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof FormBean) assign((FormBean)_o1_);
		else if (_o1_ instanceof FormBean.Data) assign((FormBean.Data)_o1_);
		else if (_o1_ instanceof FormBean.Const) assign(((FormBean.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(FormBean _o_) {
		_o_._xdb_verify_unsafe_();
		activetimes = _o_.activetimes;
		level = _o_.level;
		exp = _o_.exp;
	}

	private void assign(FormBean.Data _o_) {
		activetimes = _o_.activetimes;
		level = _o_.level;
		exp = _o_.exp;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(activetimes);
		_os_.marshal(level);
		_os_.marshal(exp);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		activetimes = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		exp = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.FormBean copy() {
		_xdb_verify_unsafe_();
		return new FormBean(this);
	}

	@Override
	public xbean.FormBean toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FormBean toBean() {
		_xdb_verify_unsafe_();
		return new FormBean(this); // same as copy()
	}

	@Override
	public xbean.FormBean toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FormBean toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getActivetimes() { // 
		_xdb_verify_unsafe_();
		return activetimes;
	}

	@Override
	public int getLevel() { // 等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getExp() { // 经验 by changhao
		_xdb_verify_unsafe_();
		return exp;
	}

	@Override
	public void setActivetimes(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "activetimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, activetimes) {
					public void rollback() { activetimes = _xdb_saved; }
				};}});
		activetimes = _v_;
	}

	@Override
	public void setLevel(int _v_) { // 等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setExp(int _v_) { // 经验 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "exp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, exp) {
					public void rollback() { exp = _xdb_saved; }
				};}});
		exp = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		FormBean _o_ = null;
		if ( _o1_ instanceof FormBean ) _o_ = (FormBean)_o1_;
		else if ( _o1_ instanceof FormBean.Const ) _o_ = ((FormBean.Const)_o1_).nThis();
		else return false;
		if (activetimes != _o_.activetimes) return false;
		if (level != _o_.level) return false;
		if (exp != _o_.exp) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += activetimes;
		_h_ += level;
		_h_ += exp;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(activetimes);
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(exp);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("activetimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("exp"));
		return lb;
	}

	private class Const implements xbean.FormBean {
		FormBean nThis() {
			return FormBean.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.FormBean copy() {
			return FormBean.this.copy();
		}

		@Override
		public xbean.FormBean toData() {
			return FormBean.this.toData();
		}

		public xbean.FormBean toBean() {
			return FormBean.this.toBean();
		}

		@Override
		public xbean.FormBean toDataIf() {
			return FormBean.this.toDataIf();
		}

		public xbean.FormBean toBeanIf() {
			return FormBean.this.toBeanIf();
		}

		@Override
		public int getActivetimes() { // 
			_xdb_verify_unsafe_();
			return activetimes;
		}

		@Override
		public int getLevel() { // 等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getExp() { // 经验 by changhao
			_xdb_verify_unsafe_();
			return exp;
		}

		@Override
		public void setActivetimes(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExp(int _v_) { // 经验 by changhao
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
			return FormBean.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return FormBean.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return FormBean.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return FormBean.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return FormBean.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return FormBean.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return FormBean.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return FormBean.this.hashCode();
		}

		@Override
		public String toString() {
			return FormBean.this.toString();
		}

	}

	public static final class Data implements xbean.FormBean {
		private int activetimes; // 
		private int level; // 等级
		private int exp; // 经验 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.FormBean _o1_) {
			if (_o1_ instanceof FormBean) assign((FormBean)_o1_);
			else if (_o1_ instanceof FormBean.Data) assign((FormBean.Data)_o1_);
			else if (_o1_ instanceof FormBean.Const) assign(((FormBean.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(FormBean _o_) {
			activetimes = _o_.activetimes;
			level = _o_.level;
			exp = _o_.exp;
		}

		private void assign(FormBean.Data _o_) {
			activetimes = _o_.activetimes;
			level = _o_.level;
			exp = _o_.exp;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(activetimes);
			_os_.marshal(level);
			_os_.marshal(exp);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			activetimes = _os_.unmarshal_int();
			level = _os_.unmarshal_int();
			exp = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.FormBean copy() {
			return new Data(this);
		}

		@Override
		public xbean.FormBean toData() {
			return new Data(this);
		}

		public xbean.FormBean toBean() {
			return new FormBean(this, null, null);
		}

		@Override
		public xbean.FormBean toDataIf() {
			return this;
		}

		public xbean.FormBean toBeanIf() {
			return new FormBean(this, null, null);
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
		public int getActivetimes() { // 
			return activetimes;
		}

		@Override
		public int getLevel() { // 等级
			return level;
		}

		@Override
		public int getExp() { // 经验 by changhao
			return exp;
		}

		@Override
		public void setActivetimes(int _v_) { // 
			activetimes = _v_;
		}

		@Override
		public void setLevel(int _v_) { // 等级
			level = _v_;
		}

		@Override
		public void setExp(int _v_) { // 经验 by changhao
			exp = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof FormBean.Data)) return false;
			FormBean.Data _o_ = (FormBean.Data) _o1_;
			if (activetimes != _o_.activetimes) return false;
			if (level != _o_.level) return false;
			if (exp != _o_.exp) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += activetimes;
			_h_ += level;
			_h_ += exp;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(activetimes);
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(exp);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
