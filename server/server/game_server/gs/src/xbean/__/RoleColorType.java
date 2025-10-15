
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleColorType extends mkdb.XBean implements xbean.RoleColorType {
	private int colorpos1; // 染色部位1
	private int colorpos2; // 染色部位2

	@Override
	public void _reset_unsafe_() {
		colorpos1 = 0;
		colorpos2 = 0;
	}

	RoleColorType(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RoleColorType() {
		this(0, null, null);
	}

	public RoleColorType(RoleColorType _o_) {
		this(_o_, null, null);
	}

	RoleColorType(xbean.RoleColorType _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleColorType) assign((RoleColorType)_o1_);
		else if (_o1_ instanceof RoleColorType.Data) assign((RoleColorType.Data)_o1_);
		else if (_o1_ instanceof RoleColorType.Const) assign(((RoleColorType.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleColorType _o_) {
		_o_._xdb_verify_unsafe_();
		colorpos1 = _o_.colorpos1;
		colorpos2 = _o_.colorpos2;
	}

	private void assign(RoleColorType.Data _o_) {
		colorpos1 = _o_.colorpos1;
		colorpos2 = _o_.colorpos2;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(colorpos1);
		_os_.marshal(colorpos2);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		colorpos1 = _os_.unmarshal_int();
		colorpos2 = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RoleColorType copy() {
		_xdb_verify_unsafe_();
		return new RoleColorType(this);
	}

	@Override
	public xbean.RoleColorType toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleColorType toBean() {
		_xdb_verify_unsafe_();
		return new RoleColorType(this); // same as copy()
	}

	@Override
	public xbean.RoleColorType toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleColorType toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getColorpos1() { // 染色部位1
		_xdb_verify_unsafe_();
		return colorpos1;
	}

	@Override
	public int getColorpos2() { // 染色部位2
		_xdb_verify_unsafe_();
		return colorpos2;
	}

	@Override
	public void setColorpos1(int _v_) { // 染色部位1
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "colorpos1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, colorpos1) {
					public void rollback() { colorpos1 = _xdb_saved; }
				};}});
		colorpos1 = _v_;
	}

	@Override
	public void setColorpos2(int _v_) { // 染色部位2
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "colorpos2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, colorpos2) {
					public void rollback() { colorpos2 = _xdb_saved; }
				};}});
		colorpos2 = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleColorType _o_ = null;
		if ( _o1_ instanceof RoleColorType ) _o_ = (RoleColorType)_o1_;
		else if ( _o1_ instanceof RoleColorType.Const ) _o_ = ((RoleColorType.Const)_o1_).nThis();
		else return false;
		if (colorpos1 != _o_.colorpos1) return false;
		if (colorpos2 != _o_.colorpos2) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += colorpos1;
		_h_ += colorpos2;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(colorpos1);
		_sb_.append(",");
		_sb_.append(colorpos2);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("colorpos1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("colorpos2"));
		return lb;
	}

	private class Const implements xbean.RoleColorType {
		RoleColorType nThis() {
			return RoleColorType.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleColorType copy() {
			return RoleColorType.this.copy();
		}

		@Override
		public xbean.RoleColorType toData() {
			return RoleColorType.this.toData();
		}

		public xbean.RoleColorType toBean() {
			return RoleColorType.this.toBean();
		}

		@Override
		public xbean.RoleColorType toDataIf() {
			return RoleColorType.this.toDataIf();
		}

		public xbean.RoleColorType toBeanIf() {
			return RoleColorType.this.toBeanIf();
		}

		@Override
		public int getColorpos1() { // 染色部位1
			_xdb_verify_unsafe_();
			return colorpos1;
		}

		@Override
		public int getColorpos2() { // 染色部位2
			_xdb_verify_unsafe_();
			return colorpos2;
		}

		@Override
		public void setColorpos1(int _v_) { // 染色部位1
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setColorpos2(int _v_) { // 染色部位2
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
			return RoleColorType.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleColorType.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleColorType.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleColorType.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleColorType.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleColorType.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleColorType.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleColorType.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleColorType.this.toString();
		}

	}

	public static final class Data implements xbean.RoleColorType {
		private int colorpos1; // 染色部位1
		private int colorpos2; // 染色部位2

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RoleColorType _o1_) {
			if (_o1_ instanceof RoleColorType) assign((RoleColorType)_o1_);
			else if (_o1_ instanceof RoleColorType.Data) assign((RoleColorType.Data)_o1_);
			else if (_o1_ instanceof RoleColorType.Const) assign(((RoleColorType.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleColorType _o_) {
			colorpos1 = _o_.colorpos1;
			colorpos2 = _o_.colorpos2;
		}

		private void assign(RoleColorType.Data _o_) {
			colorpos1 = _o_.colorpos1;
			colorpos2 = _o_.colorpos2;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(colorpos1);
			_os_.marshal(colorpos2);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			colorpos1 = _os_.unmarshal_int();
			colorpos2 = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RoleColorType copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleColorType toData() {
			return new Data(this);
		}

		public xbean.RoleColorType toBean() {
			return new RoleColorType(this, null, null);
		}

		@Override
		public xbean.RoleColorType toDataIf() {
			return this;
		}

		public xbean.RoleColorType toBeanIf() {
			return new RoleColorType(this, null, null);
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
		public int getColorpos1() { // 染色部位1
			return colorpos1;
		}

		@Override
		public int getColorpos2() { // 染色部位2
			return colorpos2;
		}

		@Override
		public void setColorpos1(int _v_) { // 染色部位1
			colorpos1 = _v_;
		}

		@Override
		public void setColorpos2(int _v_) { // 染色部位2
			colorpos2 = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleColorType.Data)) return false;
			RoleColorType.Data _o_ = (RoleColorType.Data) _o1_;
			if (colorpos1 != _o_.colorpos1) return false;
			if (colorpos2 != _o_.colorpos2) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += colorpos1;
			_h_ += colorpos2;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(colorpos1);
			_sb_.append(",");
			_sb_.append(colorpos2);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
