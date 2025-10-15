
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleTradingInfoList extends mkdb.XBean implements xbean.RoleTradingInfoList {
	private java.util.HashMap<String, String> tradinglist; // 

	@Override
	public void _reset_unsafe_() {
		tradinglist.clear();
	}

	RoleTradingInfoList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		tradinglist = new java.util.HashMap<String, String>();
	}

	public RoleTradingInfoList() {
		this(0, null, null);
	}

	public RoleTradingInfoList(RoleTradingInfoList _o_) {
		this(_o_, null, null);
	}

	RoleTradingInfoList(xbean.RoleTradingInfoList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleTradingInfoList) assign((RoleTradingInfoList)_o1_);
		else if (_o1_ instanceof RoleTradingInfoList.Data) assign((RoleTradingInfoList.Data)_o1_);
		else if (_o1_ instanceof RoleTradingInfoList.Const) assign(((RoleTradingInfoList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleTradingInfoList _o_) {
		_o_._xdb_verify_unsafe_();
		tradinglist = new java.util.HashMap<String, String>();
		for (java.util.Map.Entry<String, String> _e_ : _o_.tradinglist.entrySet())
			tradinglist.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(RoleTradingInfoList.Data _o_) {
		tradinglist = new java.util.HashMap<String, String>();
		for (java.util.Map.Entry<String, String> _e_ : _o_.tradinglist.entrySet())
			tradinglist.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(tradinglist.size());
		for (java.util.Map.Entry<String, String> _e_ : tradinglist.entrySet())
		{
			_os_.marshal(_e_.getKey(), mkdb.Const.IO_CHARSET);
			_os_.marshal(_e_.getValue(), mkdb.Const.IO_CHARSET);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				tradinglist = new java.util.HashMap<String, String>(size * 2);
			}
			for (; size > 0; --size)
			{
				String _k_ = "";
				_k_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				String _v_ = "";
				_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				tradinglist.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RoleTradingInfoList copy() {
		_xdb_verify_unsafe_();
		return new RoleTradingInfoList(this);
	}

	@Override
	public xbean.RoleTradingInfoList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleTradingInfoList toBean() {
		_xdb_verify_unsafe_();
		return new RoleTradingInfoList(this); // same as copy()
	}

	@Override
	public xbean.RoleTradingInfoList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleTradingInfoList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<String, String> getTradinglist() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "tradinglist"), tradinglist);
	}

	@Override
	public java.util.Map<String, String> getTradinglistAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<String, String> tradinglist;
		RoleTradingInfoList _o_ = this;
		tradinglist = new java.util.HashMap<String, String>();
		for (java.util.Map.Entry<String, String> _e_ : _o_.tradinglist.entrySet())
			tradinglist.put(_e_.getKey(), _e_.getValue());
		return tradinglist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleTradingInfoList _o_ = null;
		if ( _o1_ instanceof RoleTradingInfoList ) _o_ = (RoleTradingInfoList)_o1_;
		else if ( _o1_ instanceof RoleTradingInfoList.Const ) _o_ = ((RoleTradingInfoList.Const)_o1_).nThis();
		else return false;
		if (!tradinglist.equals(_o_.tradinglist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += tradinglist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(tradinglist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("tradinglist"));
		return lb;
	}

	private class Const implements xbean.RoleTradingInfoList {
		RoleTradingInfoList nThis() {
			return RoleTradingInfoList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleTradingInfoList copy() {
			return RoleTradingInfoList.this.copy();
		}

		@Override
		public xbean.RoleTradingInfoList toData() {
			return RoleTradingInfoList.this.toData();
		}

		public xbean.RoleTradingInfoList toBean() {
			return RoleTradingInfoList.this.toBean();
		}

		@Override
		public xbean.RoleTradingInfoList toDataIf() {
			return RoleTradingInfoList.this.toDataIf();
		}

		public xbean.RoleTradingInfoList toBeanIf() {
			return RoleTradingInfoList.this.toBeanIf();
		}

		@Override
		public java.util.Map<String, String> getTradinglist() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(tradinglist);
		}

		@Override
		public java.util.Map<String, String> getTradinglistAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<String, String> tradinglist;
			RoleTradingInfoList _o_ = RoleTradingInfoList.this;
			tradinglist = new java.util.HashMap<String, String>();
			for (java.util.Map.Entry<String, String> _e_ : _o_.tradinglist.entrySet())
				tradinglist.put(_e_.getKey(), _e_.getValue());
			return tradinglist;
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
			return RoleTradingInfoList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleTradingInfoList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleTradingInfoList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleTradingInfoList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleTradingInfoList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleTradingInfoList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleTradingInfoList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleTradingInfoList.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleTradingInfoList.this.toString();
		}

	}

	public static final class Data implements xbean.RoleTradingInfoList {
		private java.util.HashMap<String, String> tradinglist; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			tradinglist = new java.util.HashMap<String, String>();
		}

		Data(xbean.RoleTradingInfoList _o1_) {
			if (_o1_ instanceof RoleTradingInfoList) assign((RoleTradingInfoList)_o1_);
			else if (_o1_ instanceof RoleTradingInfoList.Data) assign((RoleTradingInfoList.Data)_o1_);
			else if (_o1_ instanceof RoleTradingInfoList.Const) assign(((RoleTradingInfoList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleTradingInfoList _o_) {
			tradinglist = new java.util.HashMap<String, String>();
			for (java.util.Map.Entry<String, String> _e_ : _o_.tradinglist.entrySet())
				tradinglist.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(RoleTradingInfoList.Data _o_) {
			tradinglist = new java.util.HashMap<String, String>();
			for (java.util.Map.Entry<String, String> _e_ : _o_.tradinglist.entrySet())
				tradinglist.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(tradinglist.size());
			for (java.util.Map.Entry<String, String> _e_ : tradinglist.entrySet())
			{
				_os_.marshal(_e_.getKey(), mkdb.Const.IO_CHARSET);
				_os_.marshal(_e_.getValue(), mkdb.Const.IO_CHARSET);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					tradinglist = new java.util.HashMap<String, String>(size * 2);
				}
				for (; size > 0; --size)
				{
					String _k_ = "";
					_k_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
					String _v_ = "";
					_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
					tradinglist.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RoleTradingInfoList copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleTradingInfoList toData() {
			return new Data(this);
		}

		public xbean.RoleTradingInfoList toBean() {
			return new RoleTradingInfoList(this, null, null);
		}

		@Override
		public xbean.RoleTradingInfoList toDataIf() {
			return this;
		}

		public xbean.RoleTradingInfoList toBeanIf() {
			return new RoleTradingInfoList(this, null, null);
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
		public java.util.Map<String, String> getTradinglist() { // 
			return tradinglist;
		}

		@Override
		public java.util.Map<String, String> getTradinglistAsData() { // 
			return tradinglist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleTradingInfoList.Data)) return false;
			RoleTradingInfoList.Data _o_ = (RoleTradingInfoList.Data) _o1_;
			if (!tradinglist.equals(_o_.tradinglist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += tradinglist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(tradinglist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
