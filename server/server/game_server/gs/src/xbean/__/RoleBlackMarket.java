
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleBlackMarket extends mkdb.XBean implements xbean.RoleBlackMarket {
	private java.util.HashMap<Long, xbean.GoldOrder> goldordersale; // 出售列表
	private java.util.HashMap<Long, xbean.GoldOrder> goldorderbuy; // 购买列表

	@Override
	public void _reset_unsafe_() {
		goldordersale.clear();
		goldorderbuy.clear();
	}

	RoleBlackMarket(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		goldordersale = new java.util.HashMap<Long, xbean.GoldOrder>();
		goldorderbuy = new java.util.HashMap<Long, xbean.GoldOrder>();
	}

	public RoleBlackMarket() {
		this(0, null, null);
	}

	public RoleBlackMarket(RoleBlackMarket _o_) {
		this(_o_, null, null);
	}

	RoleBlackMarket(xbean.RoleBlackMarket _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleBlackMarket) assign((RoleBlackMarket)_o1_);
		else if (_o1_ instanceof RoleBlackMarket.Data) assign((RoleBlackMarket.Data)_o1_);
		else if (_o1_ instanceof RoleBlackMarket.Const) assign(((RoleBlackMarket.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleBlackMarket _o_) {
		_o_._xdb_verify_unsafe_();
		goldordersale = new java.util.HashMap<Long, xbean.GoldOrder>();
		for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : _o_.goldordersale.entrySet())
			goldordersale.put(_e_.getKey(), new GoldOrder(_e_.getValue(), this, "goldordersale"));
		goldorderbuy = new java.util.HashMap<Long, xbean.GoldOrder>();
		for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : _o_.goldorderbuy.entrySet())
			goldorderbuy.put(_e_.getKey(), new GoldOrder(_e_.getValue(), this, "goldorderbuy"));
	}

	private void assign(RoleBlackMarket.Data _o_) {
		goldordersale = new java.util.HashMap<Long, xbean.GoldOrder>();
		for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : _o_.goldordersale.entrySet())
			goldordersale.put(_e_.getKey(), new GoldOrder(_e_.getValue(), this, "goldordersale"));
		goldorderbuy = new java.util.HashMap<Long, xbean.GoldOrder>();
		for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : _o_.goldorderbuy.entrySet())
			goldorderbuy.put(_e_.getKey(), new GoldOrder(_e_.getValue(), this, "goldorderbuy"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(goldordersale.size());
		for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : goldordersale.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(goldorderbuy.size());
		for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : goldorderbuy.entrySet())
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
				goldordersale = new java.util.HashMap<Long, xbean.GoldOrder>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.GoldOrder _v_ = new GoldOrder(0, this, "goldordersale");
				_v_.unmarshal(_os_);
				goldordersale.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				goldorderbuy = new java.util.HashMap<Long, xbean.GoldOrder>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.GoldOrder _v_ = new GoldOrder(0, this, "goldorderbuy");
				_v_.unmarshal(_os_);
				goldorderbuy.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RoleBlackMarket copy() {
		_xdb_verify_unsafe_();
		return new RoleBlackMarket(this);
	}

	@Override
	public xbean.RoleBlackMarket toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleBlackMarket toBean() {
		_xdb_verify_unsafe_();
		return new RoleBlackMarket(this); // same as copy()
	}

	@Override
	public xbean.RoleBlackMarket toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleBlackMarket toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Long, xbean.GoldOrder> getGoldordersale() { // 出售列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "goldordersale"), goldordersale);
	}

	@Override
	public java.util.Map<Long, xbean.GoldOrder> getGoldordersaleAsData() { // 出售列表
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.GoldOrder> goldordersale;
		RoleBlackMarket _o_ = this;
		goldordersale = new java.util.HashMap<Long, xbean.GoldOrder>();
		for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : _o_.goldordersale.entrySet())
			goldordersale.put(_e_.getKey(), new GoldOrder.Data(_e_.getValue()));
		return goldordersale;
	}

	@Override
	public java.util.Map<Long, xbean.GoldOrder> getGoldorderbuy() { // 购买列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "goldorderbuy"), goldorderbuy);
	}

	@Override
	public java.util.Map<Long, xbean.GoldOrder> getGoldorderbuyAsData() { // 购买列表
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.GoldOrder> goldorderbuy;
		RoleBlackMarket _o_ = this;
		goldorderbuy = new java.util.HashMap<Long, xbean.GoldOrder>();
		for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : _o_.goldorderbuy.entrySet())
			goldorderbuy.put(_e_.getKey(), new GoldOrder.Data(_e_.getValue()));
		return goldorderbuy;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleBlackMarket _o_ = null;
		if ( _o1_ instanceof RoleBlackMarket ) _o_ = (RoleBlackMarket)_o1_;
		else if ( _o1_ instanceof RoleBlackMarket.Const ) _o_ = ((RoleBlackMarket.Const)_o1_).nThis();
		else return false;
		if (!goldordersale.equals(_o_.goldordersale)) return false;
		if (!goldorderbuy.equals(_o_.goldorderbuy)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += goldordersale.hashCode();
		_h_ += goldorderbuy.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(goldordersale);
		_sb_.append(",");
		_sb_.append(goldorderbuy);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("goldordersale"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("goldorderbuy"));
		return lb;
	}

	private class Const implements xbean.RoleBlackMarket {
		RoleBlackMarket nThis() {
			return RoleBlackMarket.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleBlackMarket copy() {
			return RoleBlackMarket.this.copy();
		}

		@Override
		public xbean.RoleBlackMarket toData() {
			return RoleBlackMarket.this.toData();
		}

		public xbean.RoleBlackMarket toBean() {
			return RoleBlackMarket.this.toBean();
		}

		@Override
		public xbean.RoleBlackMarket toDataIf() {
			return RoleBlackMarket.this.toDataIf();
		}

		public xbean.RoleBlackMarket toBeanIf() {
			return RoleBlackMarket.this.toBeanIf();
		}

		@Override
		public java.util.Map<Long, xbean.GoldOrder> getGoldordersale() { // 出售列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(goldordersale);
		}

		@Override
		public java.util.Map<Long, xbean.GoldOrder> getGoldordersaleAsData() { // 出售列表
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.GoldOrder> goldordersale;
			RoleBlackMarket _o_ = RoleBlackMarket.this;
			goldordersale = new java.util.HashMap<Long, xbean.GoldOrder>();
			for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : _o_.goldordersale.entrySet())
				goldordersale.put(_e_.getKey(), new GoldOrder.Data(_e_.getValue()));
			return goldordersale;
		}

		@Override
		public java.util.Map<Long, xbean.GoldOrder> getGoldorderbuy() { // 购买列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(goldorderbuy);
		}

		@Override
		public java.util.Map<Long, xbean.GoldOrder> getGoldorderbuyAsData() { // 购买列表
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.GoldOrder> goldorderbuy;
			RoleBlackMarket _o_ = RoleBlackMarket.this;
			goldorderbuy = new java.util.HashMap<Long, xbean.GoldOrder>();
			for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : _o_.goldorderbuy.entrySet())
				goldorderbuy.put(_e_.getKey(), new GoldOrder.Data(_e_.getValue()));
			return goldorderbuy;
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
			return RoleBlackMarket.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleBlackMarket.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleBlackMarket.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleBlackMarket.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleBlackMarket.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleBlackMarket.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleBlackMarket.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleBlackMarket.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleBlackMarket.this.toString();
		}

	}

	public static final class Data implements xbean.RoleBlackMarket {
		private java.util.HashMap<Long, xbean.GoldOrder> goldordersale; // 出售列表
		private java.util.HashMap<Long, xbean.GoldOrder> goldorderbuy; // 购买列表

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			goldordersale = new java.util.HashMap<Long, xbean.GoldOrder>();
			goldorderbuy = new java.util.HashMap<Long, xbean.GoldOrder>();
		}

		Data(xbean.RoleBlackMarket _o1_) {
			if (_o1_ instanceof RoleBlackMarket) assign((RoleBlackMarket)_o1_);
			else if (_o1_ instanceof RoleBlackMarket.Data) assign((RoleBlackMarket.Data)_o1_);
			else if (_o1_ instanceof RoleBlackMarket.Const) assign(((RoleBlackMarket.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleBlackMarket _o_) {
			goldordersale = new java.util.HashMap<Long, xbean.GoldOrder>();
			for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : _o_.goldordersale.entrySet())
				goldordersale.put(_e_.getKey(), new GoldOrder.Data(_e_.getValue()));
			goldorderbuy = new java.util.HashMap<Long, xbean.GoldOrder>();
			for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : _o_.goldorderbuy.entrySet())
				goldorderbuy.put(_e_.getKey(), new GoldOrder.Data(_e_.getValue()));
		}

		private void assign(RoleBlackMarket.Data _o_) {
			goldordersale = new java.util.HashMap<Long, xbean.GoldOrder>();
			for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : _o_.goldordersale.entrySet())
				goldordersale.put(_e_.getKey(), new GoldOrder.Data(_e_.getValue()));
			goldorderbuy = new java.util.HashMap<Long, xbean.GoldOrder>();
			for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : _o_.goldorderbuy.entrySet())
				goldorderbuy.put(_e_.getKey(), new GoldOrder.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(goldordersale.size());
			for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : goldordersale.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(goldorderbuy.size());
			for (java.util.Map.Entry<Long, xbean.GoldOrder> _e_ : goldorderbuy.entrySet())
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
					goldordersale = new java.util.HashMap<Long, xbean.GoldOrder>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.GoldOrder _v_ = xbean.Pod.newGoldOrderData();
					_v_.unmarshal(_os_);
					goldordersale.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					goldorderbuy = new java.util.HashMap<Long, xbean.GoldOrder>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.GoldOrder _v_ = xbean.Pod.newGoldOrderData();
					_v_.unmarshal(_os_);
					goldorderbuy.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RoleBlackMarket copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleBlackMarket toData() {
			return new Data(this);
		}

		public xbean.RoleBlackMarket toBean() {
			return new RoleBlackMarket(this, null, null);
		}

		@Override
		public xbean.RoleBlackMarket toDataIf() {
			return this;
		}

		public xbean.RoleBlackMarket toBeanIf() {
			return new RoleBlackMarket(this, null, null);
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
		public java.util.Map<Long, xbean.GoldOrder> getGoldordersale() { // 出售列表
			return goldordersale;
		}

		@Override
		public java.util.Map<Long, xbean.GoldOrder> getGoldordersaleAsData() { // 出售列表
			return goldordersale;
		}

		@Override
		public java.util.Map<Long, xbean.GoldOrder> getGoldorderbuy() { // 购买列表
			return goldorderbuy;
		}

		@Override
		public java.util.Map<Long, xbean.GoldOrder> getGoldorderbuyAsData() { // 购买列表
			return goldorderbuy;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleBlackMarket.Data)) return false;
			RoleBlackMarket.Data _o_ = (RoleBlackMarket.Data) _o1_;
			if (!goldordersale.equals(_o_.goldordersale)) return false;
			if (!goldorderbuy.equals(_o_.goldorderbuy)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += goldordersale.hashCode();
			_h_ += goldorderbuy.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(goldordersale);
			_sb_.append(",");
			_sb_.append(goldorderbuy);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
