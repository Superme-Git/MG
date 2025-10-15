
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class HuoBanZhenrongInfo extends mkdb.XBean implements xbean.HuoBanZhenrongInfo {
	private int zhenfa; // 阵容光环编号
	private java.util.ArrayList<Integer> huoban; // value-伙伴id

	@Override
	public void _reset_unsafe_() {
		zhenfa = 0;
		huoban.clear();
	}

	HuoBanZhenrongInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		huoban = new java.util.ArrayList<Integer>();
	}

	public HuoBanZhenrongInfo() {
		this(0, null, null);
	}

	public HuoBanZhenrongInfo(HuoBanZhenrongInfo _o_) {
		this(_o_, null, null);
	}

	HuoBanZhenrongInfo(xbean.HuoBanZhenrongInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof HuoBanZhenrongInfo) assign((HuoBanZhenrongInfo)_o1_);
		else if (_o1_ instanceof HuoBanZhenrongInfo.Data) assign((HuoBanZhenrongInfo.Data)_o1_);
		else if (_o1_ instanceof HuoBanZhenrongInfo.Const) assign(((HuoBanZhenrongInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(HuoBanZhenrongInfo _o_) {
		_o_._xdb_verify_unsafe_();
		zhenfa = _o_.zhenfa;
		huoban = new java.util.ArrayList<Integer>();
		huoban.addAll(_o_.huoban);
	}

	private void assign(HuoBanZhenrongInfo.Data _o_) {
		zhenfa = _o_.zhenfa;
		huoban = new java.util.ArrayList<Integer>();
		huoban.addAll(_o_.huoban);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(zhenfa);
		_os_.compact_uint32(huoban.size());
		for (Integer _v_ : huoban) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		zhenfa = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			huoban.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.HuoBanZhenrongInfo copy() {
		_xdb_verify_unsafe_();
		return new HuoBanZhenrongInfo(this);
	}

	@Override
	public xbean.HuoBanZhenrongInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanZhenrongInfo toBean() {
		_xdb_verify_unsafe_();
		return new HuoBanZhenrongInfo(this); // same as copy()
	}

	@Override
	public xbean.HuoBanZhenrongInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanZhenrongInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getZhenfa() { // 阵容光环编号
		_xdb_verify_unsafe_();
		return zhenfa;
	}

	@Override
	public java.util.List<Integer> getHuoban() { // value-伙伴id
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "huoban"), huoban);
	}

	public java.util.List<Integer> getHuobanAsData() { // value-伙伴id
		_xdb_verify_unsafe_();
		java.util.List<Integer> huoban;
		HuoBanZhenrongInfo _o_ = this;
		huoban = new java.util.ArrayList<Integer>();
		huoban.addAll(_o_.huoban);
		return huoban;
	}

	@Override
	public void setZhenfa(int _v_) { // 阵容光环编号
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "zhenfa") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, zhenfa) {
					public void rollback() { zhenfa = _xdb_saved; }
				};}});
		zhenfa = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		HuoBanZhenrongInfo _o_ = null;
		if ( _o1_ instanceof HuoBanZhenrongInfo ) _o_ = (HuoBanZhenrongInfo)_o1_;
		else if ( _o1_ instanceof HuoBanZhenrongInfo.Const ) _o_ = ((HuoBanZhenrongInfo.Const)_o1_).nThis();
		else return false;
		if (zhenfa != _o_.zhenfa) return false;
		if (!huoban.equals(_o_.huoban)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += zhenfa;
		_h_ += huoban.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(zhenfa);
		_sb_.append(",");
		_sb_.append(huoban);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("zhenfa"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("huoban"));
		return lb;
	}

	private class Const implements xbean.HuoBanZhenrongInfo {
		HuoBanZhenrongInfo nThis() {
			return HuoBanZhenrongInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.HuoBanZhenrongInfo copy() {
			return HuoBanZhenrongInfo.this.copy();
		}

		@Override
		public xbean.HuoBanZhenrongInfo toData() {
			return HuoBanZhenrongInfo.this.toData();
		}

		public xbean.HuoBanZhenrongInfo toBean() {
			return HuoBanZhenrongInfo.this.toBean();
		}

		@Override
		public xbean.HuoBanZhenrongInfo toDataIf() {
			return HuoBanZhenrongInfo.this.toDataIf();
		}

		public xbean.HuoBanZhenrongInfo toBeanIf() {
			return HuoBanZhenrongInfo.this.toBeanIf();
		}

		@Override
		public int getZhenfa() { // 阵容光环编号
			_xdb_verify_unsafe_();
			return zhenfa;
		}

		@Override
		public java.util.List<Integer> getHuoban() { // value-伙伴id
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(huoban);
		}

		public java.util.List<Integer> getHuobanAsData() { // value-伙伴id
			_xdb_verify_unsafe_();
			java.util.List<Integer> huoban;
			HuoBanZhenrongInfo _o_ = HuoBanZhenrongInfo.this;
		huoban = new java.util.ArrayList<Integer>();
		huoban.addAll(_o_.huoban);
			return huoban;
		}

		@Override
		public void setZhenfa(int _v_) { // 阵容光环编号
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
			return HuoBanZhenrongInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return HuoBanZhenrongInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return HuoBanZhenrongInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return HuoBanZhenrongInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return HuoBanZhenrongInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return HuoBanZhenrongInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return HuoBanZhenrongInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return HuoBanZhenrongInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return HuoBanZhenrongInfo.this.toString();
		}

	}

	public static final class Data implements xbean.HuoBanZhenrongInfo {
		private int zhenfa; // 阵容光环编号
		private java.util.ArrayList<Integer> huoban; // value-伙伴id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			huoban = new java.util.ArrayList<Integer>();
		}

		Data(xbean.HuoBanZhenrongInfo _o1_) {
			if (_o1_ instanceof HuoBanZhenrongInfo) assign((HuoBanZhenrongInfo)_o1_);
			else if (_o1_ instanceof HuoBanZhenrongInfo.Data) assign((HuoBanZhenrongInfo.Data)_o1_);
			else if (_o1_ instanceof HuoBanZhenrongInfo.Const) assign(((HuoBanZhenrongInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(HuoBanZhenrongInfo _o_) {
			zhenfa = _o_.zhenfa;
			huoban = new java.util.ArrayList<Integer>();
			huoban.addAll(_o_.huoban);
		}

		private void assign(HuoBanZhenrongInfo.Data _o_) {
			zhenfa = _o_.zhenfa;
			huoban = new java.util.ArrayList<Integer>();
			huoban.addAll(_o_.huoban);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(zhenfa);
			_os_.compact_uint32(huoban.size());
			for (Integer _v_ : huoban) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			zhenfa = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				huoban.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.HuoBanZhenrongInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.HuoBanZhenrongInfo toData() {
			return new Data(this);
		}

		public xbean.HuoBanZhenrongInfo toBean() {
			return new HuoBanZhenrongInfo(this, null, null);
		}

		@Override
		public xbean.HuoBanZhenrongInfo toDataIf() {
			return this;
		}

		public xbean.HuoBanZhenrongInfo toBeanIf() {
			return new HuoBanZhenrongInfo(this, null, null);
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
		public int getZhenfa() { // 阵容光环编号
			return zhenfa;
		}

		@Override
		public java.util.List<Integer> getHuoban() { // value-伙伴id
			return huoban;
		}

		@Override
		public java.util.List<Integer> getHuobanAsData() { // value-伙伴id
			return huoban;
		}

		@Override
		public void setZhenfa(int _v_) { // 阵容光环编号
			zhenfa = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof HuoBanZhenrongInfo.Data)) return false;
			HuoBanZhenrongInfo.Data _o_ = (HuoBanZhenrongInfo.Data) _o1_;
			if (zhenfa != _o_.zhenfa) return false;
			if (!huoban.equals(_o_.huoban)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += zhenfa;
			_h_ += huoban.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(zhenfa);
			_sb_.append(",");
			_sb_.append(huoban);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
