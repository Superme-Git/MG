
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PlatformInfo extends mkdb.XBean implements xbean.PlatformInfo {
	private int id; // 商家id
	private String name; // 商家名，UTF-16LE编码
	private int discount; // 折扣介于1-100之间

	@Override
	public void _reset_unsafe_() {
		id = 0;
		name = "";
		discount = 0;
	}

	PlatformInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		name = "";
	}

	public PlatformInfo() {
		this(0, null, null);
	}

	public PlatformInfo(PlatformInfo _o_) {
		this(_o_, null, null);
	}

	PlatformInfo(xbean.PlatformInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PlatformInfo) assign((PlatformInfo)_o1_);
		else if (_o1_ instanceof PlatformInfo.Data) assign((PlatformInfo.Data)_o1_);
		else if (_o1_ instanceof PlatformInfo.Const) assign(((PlatformInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PlatformInfo _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		name = _o_.name;
		discount = _o_.discount;
	}

	private void assign(PlatformInfo.Data _o_) {
		id = _o_.id;
		name = _o_.name;
		discount = _o_.discount;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(name, mkdb.Const.IO_CHARSET);
		_os_.marshal(discount);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		discount = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.PlatformInfo copy() {
		_xdb_verify_unsafe_();
		return new PlatformInfo(this);
	}

	@Override
	public xbean.PlatformInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PlatformInfo toBean() {
		_xdb_verify_unsafe_();
		return new PlatformInfo(this); // same as copy()
	}

	@Override
	public xbean.PlatformInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PlatformInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getId() { // 商家id
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public String getName() { // 商家名，UTF-16LE编码
		_xdb_verify_unsafe_();
		return name;
	}

	@Override
	public com.locojoy.base.Octets getNameOctets() { // 商家名，UTF-16LE编码
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getDiscount() { // 折扣介于1-100之间
		_xdb_verify_unsafe_();
		return discount;
	}

	@Override
	public void setId(int _v_) { // 商家id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setName(String _v_) { // 商家名，UTF-16LE编码
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "name") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, name) {
					public void rollback() { name = _xdb_saved; }
				};}});
		name = _v_;
	}

	@Override
	public void setNameOctets(com.locojoy.base.Octets _v_) { // 商家名，UTF-16LE编码
		_xdb_verify_unsafe_();
		this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setDiscount(int _v_) { // 折扣介于1-100之间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "discount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, discount) {
					public void rollback() { discount = _xdb_saved; }
				};}});
		discount = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PlatformInfo _o_ = null;
		if ( _o1_ instanceof PlatformInfo ) _o_ = (PlatformInfo)_o1_;
		else if ( _o1_ instanceof PlatformInfo.Const ) _o_ = ((PlatformInfo.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (!name.equals(_o_.name)) return false;
		if (discount != _o_.discount) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += name.hashCode();
		_h_ += discount;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append("'").append(name).append("'");
		_sb_.append(",");
		_sb_.append(discount);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("name"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("discount"));
		return lb;
	}

	private class Const implements xbean.PlatformInfo {
		PlatformInfo nThis() {
			return PlatformInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PlatformInfo copy() {
			return PlatformInfo.this.copy();
		}

		@Override
		public xbean.PlatformInfo toData() {
			return PlatformInfo.this.toData();
		}

		public xbean.PlatformInfo toBean() {
			return PlatformInfo.this.toBean();
		}

		@Override
		public xbean.PlatformInfo toDataIf() {
			return PlatformInfo.this.toDataIf();
		}

		public xbean.PlatformInfo toBeanIf() {
			return PlatformInfo.this.toBeanIf();
		}

		@Override
		public int getId() { // 商家id
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public String getName() { // 商家名，UTF-16LE编码
			_xdb_verify_unsafe_();
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 商家名，UTF-16LE编码
			_xdb_verify_unsafe_();
			return PlatformInfo.this.getNameOctets();
		}

		@Override
		public int getDiscount() { // 折扣介于1-100之间
			_xdb_verify_unsafe_();
			return discount;
		}

		@Override
		public void setId(int _v_) { // 商家id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setName(String _v_) { // 商家名，UTF-16LE编码
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 商家名，UTF-16LE编码
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDiscount(int _v_) { // 折扣介于1-100之间
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
			return PlatformInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PlatformInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PlatformInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PlatformInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PlatformInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PlatformInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PlatformInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PlatformInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return PlatformInfo.this.toString();
		}

	}

	public static final class Data implements xbean.PlatformInfo {
		private int id; // 商家id
		private String name; // 商家名，UTF-16LE编码
		private int discount; // 折扣介于1-100之间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			name = "";
		}

		Data(xbean.PlatformInfo _o1_) {
			if (_o1_ instanceof PlatformInfo) assign((PlatformInfo)_o1_);
			else if (_o1_ instanceof PlatformInfo.Data) assign((PlatformInfo.Data)_o1_);
			else if (_o1_ instanceof PlatformInfo.Const) assign(((PlatformInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PlatformInfo _o_) {
			id = _o_.id;
			name = _o_.name;
			discount = _o_.discount;
		}

		private void assign(PlatformInfo.Data _o_) {
			id = _o_.id;
			name = _o_.name;
			discount = _o_.discount;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(name, mkdb.Const.IO_CHARSET);
			_os_.marshal(discount);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			discount = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.PlatformInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.PlatformInfo toData() {
			return new Data(this);
		}

		public xbean.PlatformInfo toBean() {
			return new PlatformInfo(this, null, null);
		}

		@Override
		public xbean.PlatformInfo toDataIf() {
			return this;
		}

		public xbean.PlatformInfo toBeanIf() {
			return new PlatformInfo(this, null, null);
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
		public int getId() { // 商家id
			return id;
		}

		@Override
		public String getName() { // 商家名，UTF-16LE编码
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 商家名，UTF-16LE编码
			return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getDiscount() { // 折扣介于1-100之间
			return discount;
		}

		@Override
		public void setId(int _v_) { // 商家id
			id = _v_;
		}

		@Override
		public void setName(String _v_) { // 商家名，UTF-16LE编码
			if (null == _v_)
				throw new NullPointerException();
			name = _v_;
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 商家名，UTF-16LE编码
			this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setDiscount(int _v_) { // 折扣介于1-100之间
			discount = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PlatformInfo.Data)) return false;
			PlatformInfo.Data _o_ = (PlatformInfo.Data) _o1_;
			if (id != _o_.id) return false;
			if (!name.equals(_o_.name)) return false;
			if (discount != _o_.discount) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += name.hashCode();
			_h_ += discount;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append("'").append(name).append("'");
			_sb_.append(",");
			_sb_.append(discount);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
