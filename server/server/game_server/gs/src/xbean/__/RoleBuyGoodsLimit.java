
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleBuyGoodsLimit extends mkdb.XBean implements xbean.RoleBuyGoodsLimit {
	private java.util.HashMap<Integer, xbean.NumberAndTime> daylimit; // key=goodsid日限购
	private java.util.HashMap<Integer, xbean.NumberAndTime> weeklimit; // 周限购
	private java.util.HashMap<Integer, xbean.NumberAndTime> monthlimit; // 月限购

	@Override
	public void _reset_unsafe_() {
		daylimit.clear();
		weeklimit.clear();
		monthlimit.clear();
	}

	RoleBuyGoodsLimit(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		daylimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
		weeklimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
		monthlimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
	}

	public RoleBuyGoodsLimit() {
		this(0, null, null);
	}

	public RoleBuyGoodsLimit(RoleBuyGoodsLimit _o_) {
		this(_o_, null, null);
	}

	RoleBuyGoodsLimit(xbean.RoleBuyGoodsLimit _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleBuyGoodsLimit) assign((RoleBuyGoodsLimit)_o1_);
		else if (_o1_ instanceof RoleBuyGoodsLimit.Data) assign((RoleBuyGoodsLimit.Data)_o1_);
		else if (_o1_ instanceof RoleBuyGoodsLimit.Const) assign(((RoleBuyGoodsLimit.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleBuyGoodsLimit _o_) {
		_o_._xdb_verify_unsafe_();
		daylimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
		for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.daylimit.entrySet())
			daylimit.put(_e_.getKey(), new NumberAndTime(_e_.getValue(), this, "daylimit"));
		weeklimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
		for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.weeklimit.entrySet())
			weeklimit.put(_e_.getKey(), new NumberAndTime(_e_.getValue(), this, "weeklimit"));
		monthlimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
		for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.monthlimit.entrySet())
			monthlimit.put(_e_.getKey(), new NumberAndTime(_e_.getValue(), this, "monthlimit"));
	}

	private void assign(RoleBuyGoodsLimit.Data _o_) {
		daylimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
		for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.daylimit.entrySet())
			daylimit.put(_e_.getKey(), new NumberAndTime(_e_.getValue(), this, "daylimit"));
		weeklimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
		for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.weeklimit.entrySet())
			weeklimit.put(_e_.getKey(), new NumberAndTime(_e_.getValue(), this, "weeklimit"));
		monthlimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
		for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.monthlimit.entrySet())
			monthlimit.put(_e_.getKey(), new NumberAndTime(_e_.getValue(), this, "monthlimit"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(daylimit.size());
		for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : daylimit.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(weeklimit.size());
		for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : weeklimit.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(monthlimit.size());
		for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : monthlimit.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				daylimit = new java.util.HashMap<Integer, xbean.NumberAndTime>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.NumberAndTime _v_ = new NumberAndTime(0, this, "daylimit");
				_v_.unmarshal(_os_);
				daylimit.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				weeklimit = new java.util.HashMap<Integer, xbean.NumberAndTime>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.NumberAndTime _v_ = new NumberAndTime(0, this, "weeklimit");
				_v_.unmarshal(_os_);
				weeklimit.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				monthlimit = new java.util.HashMap<Integer, xbean.NumberAndTime>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.NumberAndTime _v_ = new NumberAndTime(0, this, "monthlimit");
				_v_.unmarshal(_os_);
				monthlimit.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RoleBuyGoodsLimit copy() {
		_xdb_verify_unsafe_();
		return new RoleBuyGoodsLimit(this);
	}

	@Override
	public xbean.RoleBuyGoodsLimit toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleBuyGoodsLimit toBean() {
		_xdb_verify_unsafe_();
		return new RoleBuyGoodsLimit(this); // same as copy()
	}

	@Override
	public xbean.RoleBuyGoodsLimit toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleBuyGoodsLimit toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.NumberAndTime> getDaylimit() { // key=goodsid日限购
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "daylimit"), daylimit);
	}

	@Override
	public java.util.Map<Integer, xbean.NumberAndTime> getDaylimitAsData() { // key=goodsid日限购
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.NumberAndTime> daylimit;
		RoleBuyGoodsLimit _o_ = this;
		daylimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
		for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.daylimit.entrySet())
			daylimit.put(_e_.getKey(), new NumberAndTime.Data(_e_.getValue()));
		return daylimit;
	}

	@Override
	public java.util.Map<Integer, xbean.NumberAndTime> getWeeklimit() { // 周限购
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "weeklimit"), weeklimit);
	}

	@Override
	public java.util.Map<Integer, xbean.NumberAndTime> getWeeklimitAsData() { // 周限购
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.NumberAndTime> weeklimit;
		RoleBuyGoodsLimit _o_ = this;
		weeklimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
		for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.weeklimit.entrySet())
			weeklimit.put(_e_.getKey(), new NumberAndTime.Data(_e_.getValue()));
		return weeklimit;
	}

	@Override
	public java.util.Map<Integer, xbean.NumberAndTime> getMonthlimit() { // 月限购
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "monthlimit"), monthlimit);
	}

	@Override
	public java.util.Map<Integer, xbean.NumberAndTime> getMonthlimitAsData() { // 月限购
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.NumberAndTime> monthlimit;
		RoleBuyGoodsLimit _o_ = this;
		monthlimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
		for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.monthlimit.entrySet())
			monthlimit.put(_e_.getKey(), new NumberAndTime.Data(_e_.getValue()));
		return monthlimit;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleBuyGoodsLimit _o_ = null;
		if ( _o1_ instanceof RoleBuyGoodsLimit ) _o_ = (RoleBuyGoodsLimit)_o1_;
		else if ( _o1_ instanceof RoleBuyGoodsLimit.Const ) _o_ = ((RoleBuyGoodsLimit.Const)_o1_).nThis();
		else return false;
		if (!daylimit.equals(_o_.daylimit)) return false;
		if (!weeklimit.equals(_o_.weeklimit)) return false;
		if (!monthlimit.equals(_o_.monthlimit)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += daylimit.hashCode();
		_h_ += weeklimit.hashCode();
		_h_ += monthlimit.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(daylimit);
		_sb_.append(",");
		_sb_.append(weeklimit);
		_sb_.append(",");
		_sb_.append(monthlimit);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("daylimit"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("weeklimit"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("monthlimit"));
		return lb;
	}

	private class Const implements xbean.RoleBuyGoodsLimit {
		RoleBuyGoodsLimit nThis() {
			return RoleBuyGoodsLimit.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleBuyGoodsLimit copy() {
			return RoleBuyGoodsLimit.this.copy();
		}

		@Override
		public xbean.RoleBuyGoodsLimit toData() {
			return RoleBuyGoodsLimit.this.toData();
		}

		public xbean.RoleBuyGoodsLimit toBean() {
			return RoleBuyGoodsLimit.this.toBean();
		}

		@Override
		public xbean.RoleBuyGoodsLimit toDataIf() {
			return RoleBuyGoodsLimit.this.toDataIf();
		}

		public xbean.RoleBuyGoodsLimit toBeanIf() {
			return RoleBuyGoodsLimit.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.NumberAndTime> getDaylimit() { // key=goodsid日限购
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(daylimit);
		}

		@Override
		public java.util.Map<Integer, xbean.NumberAndTime> getDaylimitAsData() { // key=goodsid日限购
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.NumberAndTime> daylimit;
			RoleBuyGoodsLimit _o_ = RoleBuyGoodsLimit.this;
			daylimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
			for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.daylimit.entrySet())
				daylimit.put(_e_.getKey(), new NumberAndTime.Data(_e_.getValue()));
			return daylimit;
		}

		@Override
		public java.util.Map<Integer, xbean.NumberAndTime> getWeeklimit() { // 周限购
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(weeklimit);
		}

		@Override
		public java.util.Map<Integer, xbean.NumberAndTime> getWeeklimitAsData() { // 周限购
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.NumberAndTime> weeklimit;
			RoleBuyGoodsLimit _o_ = RoleBuyGoodsLimit.this;
			weeklimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
			for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.weeklimit.entrySet())
				weeklimit.put(_e_.getKey(), new NumberAndTime.Data(_e_.getValue()));
			return weeklimit;
		}

		@Override
		public java.util.Map<Integer, xbean.NumberAndTime> getMonthlimit() { // 月限购
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(monthlimit);
		}

		@Override
		public java.util.Map<Integer, xbean.NumberAndTime> getMonthlimitAsData() { // 月限购
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.NumberAndTime> monthlimit;
			RoleBuyGoodsLimit _o_ = RoleBuyGoodsLimit.this;
			monthlimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
			for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.monthlimit.entrySet())
				monthlimit.put(_e_.getKey(), new NumberAndTime.Data(_e_.getValue()));
			return monthlimit;
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
			return RoleBuyGoodsLimit.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleBuyGoodsLimit.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleBuyGoodsLimit.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleBuyGoodsLimit.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleBuyGoodsLimit.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleBuyGoodsLimit.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleBuyGoodsLimit.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleBuyGoodsLimit.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleBuyGoodsLimit.this.toString();
		}

	}

	public static final class Data implements xbean.RoleBuyGoodsLimit {
		private java.util.HashMap<Integer, xbean.NumberAndTime> daylimit; // key=goodsid日限购
		private java.util.HashMap<Integer, xbean.NumberAndTime> weeklimit; // 周限购
		private java.util.HashMap<Integer, xbean.NumberAndTime> monthlimit; // 月限购

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			daylimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
			weeklimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
			monthlimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
		}

		Data(xbean.RoleBuyGoodsLimit _o1_) {
			if (_o1_ instanceof RoleBuyGoodsLimit) assign((RoleBuyGoodsLimit)_o1_);
			else if (_o1_ instanceof RoleBuyGoodsLimit.Data) assign((RoleBuyGoodsLimit.Data)_o1_);
			else if (_o1_ instanceof RoleBuyGoodsLimit.Const) assign(((RoleBuyGoodsLimit.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleBuyGoodsLimit _o_) {
			daylimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
			for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.daylimit.entrySet())
				daylimit.put(_e_.getKey(), new NumberAndTime.Data(_e_.getValue()));
			weeklimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
			for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.weeklimit.entrySet())
				weeklimit.put(_e_.getKey(), new NumberAndTime.Data(_e_.getValue()));
			monthlimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
			for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.monthlimit.entrySet())
				monthlimit.put(_e_.getKey(), new NumberAndTime.Data(_e_.getValue()));
		}

		private void assign(RoleBuyGoodsLimit.Data _o_) {
			daylimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
			for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.daylimit.entrySet())
				daylimit.put(_e_.getKey(), new NumberAndTime.Data(_e_.getValue()));
			weeklimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
			for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.weeklimit.entrySet())
				weeklimit.put(_e_.getKey(), new NumberAndTime.Data(_e_.getValue()));
			monthlimit = new java.util.HashMap<Integer, xbean.NumberAndTime>();
			for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : _o_.monthlimit.entrySet())
				monthlimit.put(_e_.getKey(), new NumberAndTime.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(daylimit.size());
			for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : daylimit.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(weeklimit.size());
			for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : weeklimit.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(monthlimit.size());
			for (java.util.Map.Entry<Integer, xbean.NumberAndTime> _e_ : monthlimit.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					daylimit = new java.util.HashMap<Integer, xbean.NumberAndTime>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.NumberAndTime _v_ = xbean.Pod.newNumberAndTimeData();
					_v_.unmarshal(_os_);
					daylimit.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					weeklimit = new java.util.HashMap<Integer, xbean.NumberAndTime>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.NumberAndTime _v_ = xbean.Pod.newNumberAndTimeData();
					_v_.unmarshal(_os_);
					weeklimit.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					monthlimit = new java.util.HashMap<Integer, xbean.NumberAndTime>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.NumberAndTime _v_ = xbean.Pod.newNumberAndTimeData();
					_v_.unmarshal(_os_);
					monthlimit.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RoleBuyGoodsLimit copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleBuyGoodsLimit toData() {
			return new Data(this);
		}

		public xbean.RoleBuyGoodsLimit toBean() {
			return new RoleBuyGoodsLimit(this, null, null);
		}

		@Override
		public xbean.RoleBuyGoodsLimit toDataIf() {
			return this;
		}

		public xbean.RoleBuyGoodsLimit toBeanIf() {
			return new RoleBuyGoodsLimit(this, null, null);
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
		public java.util.Map<Integer, xbean.NumberAndTime> getDaylimit() { // key=goodsid日限购
			return daylimit;
		}

		@Override
		public java.util.Map<Integer, xbean.NumberAndTime> getDaylimitAsData() { // key=goodsid日限购
			return daylimit;
		}

		@Override
		public java.util.Map<Integer, xbean.NumberAndTime> getWeeklimit() { // 周限购
			return weeklimit;
		}

		@Override
		public java.util.Map<Integer, xbean.NumberAndTime> getWeeklimitAsData() { // 周限购
			return weeklimit;
		}

		@Override
		public java.util.Map<Integer, xbean.NumberAndTime> getMonthlimit() { // 月限购
			return monthlimit;
		}

		@Override
		public java.util.Map<Integer, xbean.NumberAndTime> getMonthlimitAsData() { // 月限购
			return monthlimit;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleBuyGoodsLimit.Data)) return false;
			RoleBuyGoodsLimit.Data _o_ = (RoleBuyGoodsLimit.Data) _o1_;
			if (!daylimit.equals(_o_.daylimit)) return false;
			if (!weeklimit.equals(_o_.weeklimit)) return false;
			if (!monthlimit.equals(_o_.monthlimit)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += daylimit.hashCode();
			_h_ += weeklimit.hashCode();
			_h_ += monthlimit.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(daylimit);
			_sb_.append(",");
			_sb_.append(weeklimit);
			_sb_.append(",");
			_sb_.append(monthlimit);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
