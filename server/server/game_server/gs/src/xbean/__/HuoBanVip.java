
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class HuoBanVip extends mkdb.XBean implements xbean.HuoBanVip {
	private java.util.ArrayList<Integer> huobans; // vip免费的数量

	@Override
	public void _reset_unsafe_() {
		huobans.clear();
	}

	HuoBanVip(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		huobans = new java.util.ArrayList<Integer>();
	}

	public HuoBanVip() {
		this(0, null, null);
	}

	public HuoBanVip(HuoBanVip _o_) {
		this(_o_, null, null);
	}

	HuoBanVip(xbean.HuoBanVip _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof HuoBanVip) assign((HuoBanVip)_o1_);
		else if (_o1_ instanceof HuoBanVip.Data) assign((HuoBanVip.Data)_o1_);
		else if (_o1_ instanceof HuoBanVip.Const) assign(((HuoBanVip.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(HuoBanVip _o_) {
		_o_._xdb_verify_unsafe_();
		huobans = new java.util.ArrayList<Integer>();
		huobans.addAll(_o_.huobans);
	}

	private void assign(HuoBanVip.Data _o_) {
		huobans = new java.util.ArrayList<Integer>();
		huobans.addAll(_o_.huobans);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(huobans.size());
		for (Integer _v_ : huobans) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			huobans.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.HuoBanVip copy() {
		_xdb_verify_unsafe_();
		return new HuoBanVip(this);
	}

	@Override
	public xbean.HuoBanVip toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanVip toBean() {
		_xdb_verify_unsafe_();
		return new HuoBanVip(this); // same as copy()
	}

	@Override
	public xbean.HuoBanVip toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanVip toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<Integer> getHuobans() { // vip免费的数量
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "huobans"), huobans);
	}

	public java.util.List<Integer> getHuobansAsData() { // vip免费的数量
		_xdb_verify_unsafe_();
		java.util.List<Integer> huobans;
		HuoBanVip _o_ = this;
		huobans = new java.util.ArrayList<Integer>();
		huobans.addAll(_o_.huobans);
		return huobans;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		HuoBanVip _o_ = null;
		if ( _o1_ instanceof HuoBanVip ) _o_ = (HuoBanVip)_o1_;
		else if ( _o1_ instanceof HuoBanVip.Const ) _o_ = ((HuoBanVip.Const)_o1_).nThis();
		else return false;
		if (!huobans.equals(_o_.huobans)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += huobans.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(huobans);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("huobans"));
		return lb;
	}

	private class Const implements xbean.HuoBanVip {
		HuoBanVip nThis() {
			return HuoBanVip.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.HuoBanVip copy() {
			return HuoBanVip.this.copy();
		}

		@Override
		public xbean.HuoBanVip toData() {
			return HuoBanVip.this.toData();
		}

		public xbean.HuoBanVip toBean() {
			return HuoBanVip.this.toBean();
		}

		@Override
		public xbean.HuoBanVip toDataIf() {
			return HuoBanVip.this.toDataIf();
		}

		public xbean.HuoBanVip toBeanIf() {
			return HuoBanVip.this.toBeanIf();
		}

		@Override
		public java.util.List<Integer> getHuobans() { // vip免费的数量
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(huobans);
		}

		public java.util.List<Integer> getHuobansAsData() { // vip免费的数量
			_xdb_verify_unsafe_();
			java.util.List<Integer> huobans;
			HuoBanVip _o_ = HuoBanVip.this;
		huobans = new java.util.ArrayList<Integer>();
		huobans.addAll(_o_.huobans);
			return huobans;
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
			return HuoBanVip.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return HuoBanVip.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return HuoBanVip.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return HuoBanVip.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return HuoBanVip.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return HuoBanVip.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return HuoBanVip.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return HuoBanVip.this.hashCode();
		}

		@Override
		public String toString() {
			return HuoBanVip.this.toString();
		}

	}

	public static final class Data implements xbean.HuoBanVip {
		private java.util.ArrayList<Integer> huobans; // vip免费的数量

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			huobans = new java.util.ArrayList<Integer>();
		}

		Data(xbean.HuoBanVip _o1_) {
			if (_o1_ instanceof HuoBanVip) assign((HuoBanVip)_o1_);
			else if (_o1_ instanceof HuoBanVip.Data) assign((HuoBanVip.Data)_o1_);
			else if (_o1_ instanceof HuoBanVip.Const) assign(((HuoBanVip.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(HuoBanVip _o_) {
			huobans = new java.util.ArrayList<Integer>();
			huobans.addAll(_o_.huobans);
		}

		private void assign(HuoBanVip.Data _o_) {
			huobans = new java.util.ArrayList<Integer>();
			huobans.addAll(_o_.huobans);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(huobans.size());
			for (Integer _v_ : huobans) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				huobans.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.HuoBanVip copy() {
			return new Data(this);
		}

		@Override
		public xbean.HuoBanVip toData() {
			return new Data(this);
		}

		public xbean.HuoBanVip toBean() {
			return new HuoBanVip(this, null, null);
		}

		@Override
		public xbean.HuoBanVip toDataIf() {
			return this;
		}

		public xbean.HuoBanVip toBeanIf() {
			return new HuoBanVip(this, null, null);
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
		public java.util.List<Integer> getHuobans() { // vip免费的数量
			return huobans;
		}

		@Override
		public java.util.List<Integer> getHuobansAsData() { // vip免费的数量
			return huobans;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof HuoBanVip.Data)) return false;
			HuoBanVip.Data _o_ = (HuoBanVip.Data) _o1_;
			if (!huobans.equals(_o_.huobans)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += huobans.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(huobans);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
