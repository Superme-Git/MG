
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class NpcSaleBusiness extends mkdb.XBean implements xbean.NpcSaleBusiness {
	private java.util.HashMap<Integer, xbean.GoodsBusiness> goodsmap; // key=goodsid
	private short isactive; // 是否统计,默认为0不启动,1是启动
	private long time; // 最后统计时间

	@Override
	public void _reset_unsafe_() {
		goodsmap.clear();
		isactive = 0;
		time = 0L;
	}

	NpcSaleBusiness(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		goodsmap = new java.util.HashMap<Integer, xbean.GoodsBusiness>();
	}

	public NpcSaleBusiness() {
		this(0, null, null);
	}

	public NpcSaleBusiness(NpcSaleBusiness _o_) {
		this(_o_, null, null);
	}

	NpcSaleBusiness(xbean.NpcSaleBusiness _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof NpcSaleBusiness) assign((NpcSaleBusiness)_o1_);
		else if (_o1_ instanceof NpcSaleBusiness.Data) assign((NpcSaleBusiness.Data)_o1_);
		else if (_o1_ instanceof NpcSaleBusiness.Const) assign(((NpcSaleBusiness.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(NpcSaleBusiness _o_) {
		_o_._xdb_verify_unsafe_();
		goodsmap = new java.util.HashMap<Integer, xbean.GoodsBusiness>();
		for (java.util.Map.Entry<Integer, xbean.GoodsBusiness> _e_ : _o_.goodsmap.entrySet())
			goodsmap.put(_e_.getKey(), new GoodsBusiness(_e_.getValue(), this, "goodsmap"));
		isactive = _o_.isactive;
		time = _o_.time;
	}

	private void assign(NpcSaleBusiness.Data _o_) {
		goodsmap = new java.util.HashMap<Integer, xbean.GoodsBusiness>();
		for (java.util.Map.Entry<Integer, xbean.GoodsBusiness> _e_ : _o_.goodsmap.entrySet())
			goodsmap.put(_e_.getKey(), new GoodsBusiness(_e_.getValue(), this, "goodsmap"));
		isactive = _o_.isactive;
		time = _o_.time;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(goodsmap.size());
		for (java.util.Map.Entry<Integer, xbean.GoodsBusiness> _e_ : goodsmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(isactive);
		_os_.marshal(time);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				goodsmap = new java.util.HashMap<Integer, xbean.GoodsBusiness>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.GoodsBusiness _v_ = new GoodsBusiness(0, this, "goodsmap");
				_v_.unmarshal(_os_);
				goodsmap.put(_k_, _v_);
			}
		}
		isactive = _os_.unmarshal_short();
		time = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.NpcSaleBusiness copy() {
		_xdb_verify_unsafe_();
		return new NpcSaleBusiness(this);
	}

	@Override
	public xbean.NpcSaleBusiness toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NpcSaleBusiness toBean() {
		_xdb_verify_unsafe_();
		return new NpcSaleBusiness(this); // same as copy()
	}

	@Override
	public xbean.NpcSaleBusiness toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NpcSaleBusiness toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.GoodsBusiness> getGoodsmap() { // key=goodsid
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "goodsmap"), goodsmap);
	}

	@Override
	public java.util.Map<Integer, xbean.GoodsBusiness> getGoodsmapAsData() { // key=goodsid
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.GoodsBusiness> goodsmap;
		NpcSaleBusiness _o_ = this;
		goodsmap = new java.util.HashMap<Integer, xbean.GoodsBusiness>();
		for (java.util.Map.Entry<Integer, xbean.GoodsBusiness> _e_ : _o_.goodsmap.entrySet())
			goodsmap.put(_e_.getKey(), new GoodsBusiness.Data(_e_.getValue()));
		return goodsmap;
	}

	@Override
	public short getIsactive() { // 是否统计,默认为0不启动,1是启动
		_xdb_verify_unsafe_();
		return isactive;
	}

	@Override
	public long getTime() { // 最后统计时间
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public void setIsactive(short _v_) { // 是否统计,默认为0不启动,1是启动
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "isactive") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogShort(this, isactive) {
					public void rollback() { isactive = _xdb_saved; }
				};}});
		isactive = _v_;
	}

	@Override
	public void setTime(long _v_) { // 最后统计时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "time") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, time) {
					public void rollback() { time = _xdb_saved; }
				};}});
		time = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		NpcSaleBusiness _o_ = null;
		if ( _o1_ instanceof NpcSaleBusiness ) _o_ = (NpcSaleBusiness)_o1_;
		else if ( _o1_ instanceof NpcSaleBusiness.Const ) _o_ = ((NpcSaleBusiness.Const)_o1_).nThis();
		else return false;
		if (!goodsmap.equals(_o_.goodsmap)) return false;
		if (isactive != _o_.isactive) return false;
		if (time != _o_.time) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += goodsmap.hashCode();
		_h_ += isactive;
		_h_ += time;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(goodsmap);
		_sb_.append(",");
		_sb_.append(isactive);
		_sb_.append(",");
		_sb_.append(time);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("goodsmap"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("isactive"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		return lb;
	}

	private class Const implements xbean.NpcSaleBusiness {
		NpcSaleBusiness nThis() {
			return NpcSaleBusiness.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.NpcSaleBusiness copy() {
			return NpcSaleBusiness.this.copy();
		}

		@Override
		public xbean.NpcSaleBusiness toData() {
			return NpcSaleBusiness.this.toData();
		}

		public xbean.NpcSaleBusiness toBean() {
			return NpcSaleBusiness.this.toBean();
		}

		@Override
		public xbean.NpcSaleBusiness toDataIf() {
			return NpcSaleBusiness.this.toDataIf();
		}

		public xbean.NpcSaleBusiness toBeanIf() {
			return NpcSaleBusiness.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.GoodsBusiness> getGoodsmap() { // key=goodsid
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(goodsmap);
		}

		@Override
		public java.util.Map<Integer, xbean.GoodsBusiness> getGoodsmapAsData() { // key=goodsid
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.GoodsBusiness> goodsmap;
			NpcSaleBusiness _o_ = NpcSaleBusiness.this;
			goodsmap = new java.util.HashMap<Integer, xbean.GoodsBusiness>();
			for (java.util.Map.Entry<Integer, xbean.GoodsBusiness> _e_ : _o_.goodsmap.entrySet())
				goodsmap.put(_e_.getKey(), new GoodsBusiness.Data(_e_.getValue()));
			return goodsmap;
		}

		@Override
		public short getIsactive() { // 是否统计,默认为0不启动,1是启动
			_xdb_verify_unsafe_();
			return isactive;
		}

		@Override
		public long getTime() { // 最后统计时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public void setIsactive(short _v_) { // 是否统计,默认为0不启动,1是启动
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTime(long _v_) { // 最后统计时间
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
			return NpcSaleBusiness.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return NpcSaleBusiness.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return NpcSaleBusiness.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return NpcSaleBusiness.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return NpcSaleBusiness.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return NpcSaleBusiness.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return NpcSaleBusiness.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return NpcSaleBusiness.this.hashCode();
		}

		@Override
		public String toString() {
			return NpcSaleBusiness.this.toString();
		}

	}

	public static final class Data implements xbean.NpcSaleBusiness {
		private java.util.HashMap<Integer, xbean.GoodsBusiness> goodsmap; // key=goodsid
		private short isactive; // 是否统计,默认为0不启动,1是启动
		private long time; // 最后统计时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			goodsmap = new java.util.HashMap<Integer, xbean.GoodsBusiness>();
		}

		Data(xbean.NpcSaleBusiness _o1_) {
			if (_o1_ instanceof NpcSaleBusiness) assign((NpcSaleBusiness)_o1_);
			else if (_o1_ instanceof NpcSaleBusiness.Data) assign((NpcSaleBusiness.Data)_o1_);
			else if (_o1_ instanceof NpcSaleBusiness.Const) assign(((NpcSaleBusiness.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(NpcSaleBusiness _o_) {
			goodsmap = new java.util.HashMap<Integer, xbean.GoodsBusiness>();
			for (java.util.Map.Entry<Integer, xbean.GoodsBusiness> _e_ : _o_.goodsmap.entrySet())
				goodsmap.put(_e_.getKey(), new GoodsBusiness.Data(_e_.getValue()));
			isactive = _o_.isactive;
			time = _o_.time;
		}

		private void assign(NpcSaleBusiness.Data _o_) {
			goodsmap = new java.util.HashMap<Integer, xbean.GoodsBusiness>();
			for (java.util.Map.Entry<Integer, xbean.GoodsBusiness> _e_ : _o_.goodsmap.entrySet())
				goodsmap.put(_e_.getKey(), new GoodsBusiness.Data(_e_.getValue()));
			isactive = _o_.isactive;
			time = _o_.time;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(goodsmap.size());
			for (java.util.Map.Entry<Integer, xbean.GoodsBusiness> _e_ : goodsmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(isactive);
			_os_.marshal(time);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					goodsmap = new java.util.HashMap<Integer, xbean.GoodsBusiness>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.GoodsBusiness _v_ = xbean.Pod.newGoodsBusinessData();
					_v_.unmarshal(_os_);
					goodsmap.put(_k_, _v_);
				}
			}
			isactive = _os_.unmarshal_short();
			time = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.NpcSaleBusiness copy() {
			return new Data(this);
		}

		@Override
		public xbean.NpcSaleBusiness toData() {
			return new Data(this);
		}

		public xbean.NpcSaleBusiness toBean() {
			return new NpcSaleBusiness(this, null, null);
		}

		@Override
		public xbean.NpcSaleBusiness toDataIf() {
			return this;
		}

		public xbean.NpcSaleBusiness toBeanIf() {
			return new NpcSaleBusiness(this, null, null);
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
		public java.util.Map<Integer, xbean.GoodsBusiness> getGoodsmap() { // key=goodsid
			return goodsmap;
		}

		@Override
		public java.util.Map<Integer, xbean.GoodsBusiness> getGoodsmapAsData() { // key=goodsid
			return goodsmap;
		}

		@Override
		public short getIsactive() { // 是否统计,默认为0不启动,1是启动
			return isactive;
		}

		@Override
		public long getTime() { // 最后统计时间
			return time;
		}

		@Override
		public void setIsactive(short _v_) { // 是否统计,默认为0不启动,1是启动
			isactive = _v_;
		}

		@Override
		public void setTime(long _v_) { // 最后统计时间
			time = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof NpcSaleBusiness.Data)) return false;
			NpcSaleBusiness.Data _o_ = (NpcSaleBusiness.Data) _o1_;
			if (!goodsmap.equals(_o_.goodsmap)) return false;
			if (isactive != _o_.isactive) return false;
			if (time != _o_.time) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += goodsmap.hashCode();
			_h_ += isactive;
			_h_ += time;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(goodsmap);
			_sb_.append(",");
			_sb_.append(isactive);
			_sb_.append(",");
			_sb_.append(time);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
