
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Effect extends mkdb.XBean implements xbean.Effect {
	private int effecttypeid; // 目标加成属性类型Id
	private float value; // 加成的值
	private boolean enable; // 是否生效

	@Override
	public void _reset_unsafe_() {
		effecttypeid = 0;
		value = 0.0f;
		enable = false;
	}

	Effect(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public Effect() {
		this(0, null, null);
	}

	public Effect(Effect _o_) {
		this(_o_, null, null);
	}

	Effect(xbean.Effect _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Effect) assign((Effect)_o1_);
		else if (_o1_ instanceof Effect.Data) assign((Effect.Data)_o1_);
		else if (_o1_ instanceof Effect.Const) assign(((Effect.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Effect _o_) {
		_o_._xdb_verify_unsafe_();
		effecttypeid = _o_.effecttypeid;
		value = _o_.value;
		enable = _o_.enable;
	}

	private void assign(Effect.Data _o_) {
		effecttypeid = _o_.effecttypeid;
		value = _o_.value;
		enable = _o_.enable;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(effecttypeid);
		_os_.marshal(value);
		_os_.marshal(enable);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		effecttypeid = _os_.unmarshal_int();
		value = _os_.unmarshal_float();
		enable = _os_.unmarshal_boolean();
		return _os_;
	}

	@Override
	public xbean.Effect copy() {
		_xdb_verify_unsafe_();
		return new Effect(this);
	}

	@Override
	public xbean.Effect toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Effect toBean() {
		_xdb_verify_unsafe_();
		return new Effect(this); // same as copy()
	}

	@Override
	public xbean.Effect toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Effect toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getEffecttypeid() { // 目标加成属性类型Id
		_xdb_verify_unsafe_();
		return effecttypeid;
	}

	@Override
	public float getValue() { // 加成的值
		_xdb_verify_unsafe_();
		return value;
	}

	@Override
	public boolean getEnable() { // 是否生效
		_xdb_verify_unsafe_();
		return enable;
	}

	@Override
	public void setEffecttypeid(int _v_) { // 目标加成属性类型Id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "effecttypeid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, effecttypeid) {
					public void rollback() { effecttypeid = _xdb_saved; }
				};}});
		effecttypeid = _v_;
	}

	@Override
	public void setValue(float _v_) { // 加成的值
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "value") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogFloat(this, value) {
					public void rollback() { value = _xdb_saved; }
				};}});
		value = _v_;
	}

	@Override
	public void setEnable(boolean _v_) { // 是否生效
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "enable") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, enable) {
					public void rollback() { enable = _xdb_saved; }
				};}});
		enable = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Effect _o_ = null;
		if ( _o1_ instanceof Effect ) _o_ = (Effect)_o1_;
		else if ( _o1_ instanceof Effect.Const ) _o_ = ((Effect.Const)_o1_).nThis();
		else return false;
		if (effecttypeid != _o_.effecttypeid) return false;
		if (value != _o_.value) return false;
		if (enable != _o_.enable) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += effecttypeid;
		_h_ += value;
		_h_ += enable ? 1231 : 1237;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(effecttypeid);
		_sb_.append(",");
		_sb_.append(value);
		_sb_.append(",");
		_sb_.append(enable);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("effecttypeid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("value"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("enable"));
		return lb;
	}

	private class Const implements xbean.Effect {
		Effect nThis() {
			return Effect.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Effect copy() {
			return Effect.this.copy();
		}

		@Override
		public xbean.Effect toData() {
			return Effect.this.toData();
		}

		public xbean.Effect toBean() {
			return Effect.this.toBean();
		}

		@Override
		public xbean.Effect toDataIf() {
			return Effect.this.toDataIf();
		}

		public xbean.Effect toBeanIf() {
			return Effect.this.toBeanIf();
		}

		@Override
		public int getEffecttypeid() { // 目标加成属性类型Id
			_xdb_verify_unsafe_();
			return effecttypeid;
		}

		@Override
		public float getValue() { // 加成的值
			_xdb_verify_unsafe_();
			return value;
		}

		@Override
		public boolean getEnable() { // 是否生效
			_xdb_verify_unsafe_();
			return enable;
		}

		@Override
		public void setEffecttypeid(int _v_) { // 目标加成属性类型Id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setValue(float _v_) { // 加成的值
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEnable(boolean _v_) { // 是否生效
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
			return Effect.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Effect.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Effect.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Effect.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Effect.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Effect.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Effect.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Effect.this.hashCode();
		}

		@Override
		public String toString() {
			return Effect.this.toString();
		}

	}

	public static final class Data implements xbean.Effect {
		private int effecttypeid; // 目标加成属性类型Id
		private float value; // 加成的值
		private boolean enable; // 是否生效

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.Effect _o1_) {
			if (_o1_ instanceof Effect) assign((Effect)_o1_);
			else if (_o1_ instanceof Effect.Data) assign((Effect.Data)_o1_);
			else if (_o1_ instanceof Effect.Const) assign(((Effect.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Effect _o_) {
			effecttypeid = _o_.effecttypeid;
			value = _o_.value;
			enable = _o_.enable;
		}

		private void assign(Effect.Data _o_) {
			effecttypeid = _o_.effecttypeid;
			value = _o_.value;
			enable = _o_.enable;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(effecttypeid);
			_os_.marshal(value);
			_os_.marshal(enable);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			effecttypeid = _os_.unmarshal_int();
			value = _os_.unmarshal_float();
			enable = _os_.unmarshal_boolean();
			return _os_;
		}

		@Override
		public xbean.Effect copy() {
			return new Data(this);
		}

		@Override
		public xbean.Effect toData() {
			return new Data(this);
		}

		public xbean.Effect toBean() {
			return new Effect(this, null, null);
		}

		@Override
		public xbean.Effect toDataIf() {
			return this;
		}

		public xbean.Effect toBeanIf() {
			return new Effect(this, null, null);
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
		public int getEffecttypeid() { // 目标加成属性类型Id
			return effecttypeid;
		}

		@Override
		public float getValue() { // 加成的值
			return value;
		}

		@Override
		public boolean getEnable() { // 是否生效
			return enable;
		}

		@Override
		public void setEffecttypeid(int _v_) { // 目标加成属性类型Id
			effecttypeid = _v_;
		}

		@Override
		public void setValue(float _v_) { // 加成的值
			value = _v_;
		}

		@Override
		public void setEnable(boolean _v_) { // 是否生效
			enable = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Effect.Data)) return false;
			Effect.Data _o_ = (Effect.Data) _o1_;
			if (effecttypeid != _o_.effecttypeid) return false;
			if (value != _o_.value) return false;
			if (enable != _o_.enable) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += effecttypeid;
			_h_ += value;
			_h_ += enable ? 1231 : 1237;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(effecttypeid);
			_sb_.append(",");
			_sb_.append(value);
			_sb_.append(",");
			_sb_.append(enable);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
