
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class HuoBanZhenrong extends mkdb.XBean implements xbean.HuoBanZhenrong {
	private int current; // 当前阵容编号
	private java.util.HashMap<Integer, xbean.HuoBanZhenrongInfo> zhenrong; // key-阵容编号(1,2,3)

	@Override
	public void _reset_unsafe_() {
		current = 0;
		zhenrong.clear();
	}

	HuoBanZhenrong(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		zhenrong = new java.util.HashMap<Integer, xbean.HuoBanZhenrongInfo>();
	}

	public HuoBanZhenrong() {
		this(0, null, null);
	}

	public HuoBanZhenrong(HuoBanZhenrong _o_) {
		this(_o_, null, null);
	}

	HuoBanZhenrong(xbean.HuoBanZhenrong _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof HuoBanZhenrong) assign((HuoBanZhenrong)_o1_);
		else if (_o1_ instanceof HuoBanZhenrong.Data) assign((HuoBanZhenrong.Data)_o1_);
		else if (_o1_ instanceof HuoBanZhenrong.Const) assign(((HuoBanZhenrong.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(HuoBanZhenrong _o_) {
		_o_._xdb_verify_unsafe_();
		current = _o_.current;
		zhenrong = new java.util.HashMap<Integer, xbean.HuoBanZhenrongInfo>();
		for (java.util.Map.Entry<Integer, xbean.HuoBanZhenrongInfo> _e_ : _o_.zhenrong.entrySet())
			zhenrong.put(_e_.getKey(), new HuoBanZhenrongInfo(_e_.getValue(), this, "zhenrong"));
	}

	private void assign(HuoBanZhenrong.Data _o_) {
		current = _o_.current;
		zhenrong = new java.util.HashMap<Integer, xbean.HuoBanZhenrongInfo>();
		for (java.util.Map.Entry<Integer, xbean.HuoBanZhenrongInfo> _e_ : _o_.zhenrong.entrySet())
			zhenrong.put(_e_.getKey(), new HuoBanZhenrongInfo(_e_.getValue(), this, "zhenrong"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(current);
		_os_.compact_uint32(zhenrong.size());
		for (java.util.Map.Entry<Integer, xbean.HuoBanZhenrongInfo> _e_ : zhenrong.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		current = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				zhenrong = new java.util.HashMap<Integer, xbean.HuoBanZhenrongInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.HuoBanZhenrongInfo _v_ = new HuoBanZhenrongInfo(0, this, "zhenrong");
				_v_.unmarshal(_os_);
				zhenrong.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.HuoBanZhenrong copy() {
		_xdb_verify_unsafe_();
		return new HuoBanZhenrong(this);
	}

	@Override
	public xbean.HuoBanZhenrong toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanZhenrong toBean() {
		_xdb_verify_unsafe_();
		return new HuoBanZhenrong(this); // same as copy()
	}

	@Override
	public xbean.HuoBanZhenrong toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.HuoBanZhenrong toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getCurrent() { // 当前阵容编号
		_xdb_verify_unsafe_();
		return current;
	}

	@Override
	public java.util.Map<Integer, xbean.HuoBanZhenrongInfo> getZhenrong() { // key-阵容编号(1,2,3)
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "zhenrong"), zhenrong);
	}

	@Override
	public java.util.Map<Integer, xbean.HuoBanZhenrongInfo> getZhenrongAsData() { // key-阵容编号(1,2,3)
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.HuoBanZhenrongInfo> zhenrong;
		HuoBanZhenrong _o_ = this;
		zhenrong = new java.util.HashMap<Integer, xbean.HuoBanZhenrongInfo>();
		for (java.util.Map.Entry<Integer, xbean.HuoBanZhenrongInfo> _e_ : _o_.zhenrong.entrySet())
			zhenrong.put(_e_.getKey(), new HuoBanZhenrongInfo.Data(_e_.getValue()));
		return zhenrong;
	}

	@Override
	public void setCurrent(int _v_) { // 当前阵容编号
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "current") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, current) {
					public void rollback() { current = _xdb_saved; }
				};}});
		current = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		HuoBanZhenrong _o_ = null;
		if ( _o1_ instanceof HuoBanZhenrong ) _o_ = (HuoBanZhenrong)_o1_;
		else if ( _o1_ instanceof HuoBanZhenrong.Const ) _o_ = ((HuoBanZhenrong.Const)_o1_).nThis();
		else return false;
		if (current != _o_.current) return false;
		if (!zhenrong.equals(_o_.zhenrong)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += current;
		_h_ += zhenrong.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(current);
		_sb_.append(",");
		_sb_.append(zhenrong);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("current"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("zhenrong"));
		return lb;
	}

	private class Const implements xbean.HuoBanZhenrong {
		HuoBanZhenrong nThis() {
			return HuoBanZhenrong.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.HuoBanZhenrong copy() {
			return HuoBanZhenrong.this.copy();
		}

		@Override
		public xbean.HuoBanZhenrong toData() {
			return HuoBanZhenrong.this.toData();
		}

		public xbean.HuoBanZhenrong toBean() {
			return HuoBanZhenrong.this.toBean();
		}

		@Override
		public xbean.HuoBanZhenrong toDataIf() {
			return HuoBanZhenrong.this.toDataIf();
		}

		public xbean.HuoBanZhenrong toBeanIf() {
			return HuoBanZhenrong.this.toBeanIf();
		}

		@Override
		public int getCurrent() { // 当前阵容编号
			_xdb_verify_unsafe_();
			return current;
		}

		@Override
		public java.util.Map<Integer, xbean.HuoBanZhenrongInfo> getZhenrong() { // key-阵容编号(1,2,3)
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(zhenrong);
		}

		@Override
		public java.util.Map<Integer, xbean.HuoBanZhenrongInfo> getZhenrongAsData() { // key-阵容编号(1,2,3)
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.HuoBanZhenrongInfo> zhenrong;
			HuoBanZhenrong _o_ = HuoBanZhenrong.this;
			zhenrong = new java.util.HashMap<Integer, xbean.HuoBanZhenrongInfo>();
			for (java.util.Map.Entry<Integer, xbean.HuoBanZhenrongInfo> _e_ : _o_.zhenrong.entrySet())
				zhenrong.put(_e_.getKey(), new HuoBanZhenrongInfo.Data(_e_.getValue()));
			return zhenrong;
		}

		@Override
		public void setCurrent(int _v_) { // 当前阵容编号
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
			return HuoBanZhenrong.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return HuoBanZhenrong.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return HuoBanZhenrong.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return HuoBanZhenrong.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return HuoBanZhenrong.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return HuoBanZhenrong.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return HuoBanZhenrong.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return HuoBanZhenrong.this.hashCode();
		}

		@Override
		public String toString() {
			return HuoBanZhenrong.this.toString();
		}

	}

	public static final class Data implements xbean.HuoBanZhenrong {
		private int current; // 当前阵容编号
		private java.util.HashMap<Integer, xbean.HuoBanZhenrongInfo> zhenrong; // key-阵容编号(1,2,3)

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			zhenrong = new java.util.HashMap<Integer, xbean.HuoBanZhenrongInfo>();
		}

		Data(xbean.HuoBanZhenrong _o1_) {
			if (_o1_ instanceof HuoBanZhenrong) assign((HuoBanZhenrong)_o1_);
			else if (_o1_ instanceof HuoBanZhenrong.Data) assign((HuoBanZhenrong.Data)_o1_);
			else if (_o1_ instanceof HuoBanZhenrong.Const) assign(((HuoBanZhenrong.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(HuoBanZhenrong _o_) {
			current = _o_.current;
			zhenrong = new java.util.HashMap<Integer, xbean.HuoBanZhenrongInfo>();
			for (java.util.Map.Entry<Integer, xbean.HuoBanZhenrongInfo> _e_ : _o_.zhenrong.entrySet())
				zhenrong.put(_e_.getKey(), new HuoBanZhenrongInfo.Data(_e_.getValue()));
		}

		private void assign(HuoBanZhenrong.Data _o_) {
			current = _o_.current;
			zhenrong = new java.util.HashMap<Integer, xbean.HuoBanZhenrongInfo>();
			for (java.util.Map.Entry<Integer, xbean.HuoBanZhenrongInfo> _e_ : _o_.zhenrong.entrySet())
				zhenrong.put(_e_.getKey(), new HuoBanZhenrongInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(current);
			_os_.compact_uint32(zhenrong.size());
			for (java.util.Map.Entry<Integer, xbean.HuoBanZhenrongInfo> _e_ : zhenrong.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			current = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					zhenrong = new java.util.HashMap<Integer, xbean.HuoBanZhenrongInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.HuoBanZhenrongInfo _v_ = xbean.Pod.newHuoBanZhenrongInfoData();
					_v_.unmarshal(_os_);
					zhenrong.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.HuoBanZhenrong copy() {
			return new Data(this);
		}

		@Override
		public xbean.HuoBanZhenrong toData() {
			return new Data(this);
		}

		public xbean.HuoBanZhenrong toBean() {
			return new HuoBanZhenrong(this, null, null);
		}

		@Override
		public xbean.HuoBanZhenrong toDataIf() {
			return this;
		}

		public xbean.HuoBanZhenrong toBeanIf() {
			return new HuoBanZhenrong(this, null, null);
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
		public int getCurrent() { // 当前阵容编号
			return current;
		}

		@Override
		public java.util.Map<Integer, xbean.HuoBanZhenrongInfo> getZhenrong() { // key-阵容编号(1,2,3)
			return zhenrong;
		}

		@Override
		public java.util.Map<Integer, xbean.HuoBanZhenrongInfo> getZhenrongAsData() { // key-阵容编号(1,2,3)
			return zhenrong;
		}

		@Override
		public void setCurrent(int _v_) { // 当前阵容编号
			current = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof HuoBanZhenrong.Data)) return false;
			HuoBanZhenrong.Data _o_ = (HuoBanZhenrong.Data) _o1_;
			if (current != _o_.current) return false;
			if (!zhenrong.equals(_o_.zhenrong)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += current;
			_h_ += zhenrong.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(current);
			_sb_.append(",");
			_sb_.append(zhenrong);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
