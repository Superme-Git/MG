
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MarketTradeLog extends mkdb.XBean implements xbean.MarketTradeLog {
	private java.util.ArrayList<xbean.LogBean> buylog; // 买记录
	private java.util.ArrayList<xbean.LogBean> salelog; // 卖记录

	@Override
	public void _reset_unsafe_() {
		buylog.clear();
		salelog.clear();
	}

	MarketTradeLog(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		buylog = new java.util.ArrayList<xbean.LogBean>();
		salelog = new java.util.ArrayList<xbean.LogBean>();
	}

	public MarketTradeLog() {
		this(0, null, null);
	}

	public MarketTradeLog(MarketTradeLog _o_) {
		this(_o_, null, null);
	}

	MarketTradeLog(xbean.MarketTradeLog _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MarketTradeLog) assign((MarketTradeLog)_o1_);
		else if (_o1_ instanceof MarketTradeLog.Data) assign((MarketTradeLog.Data)_o1_);
		else if (_o1_ instanceof MarketTradeLog.Const) assign(((MarketTradeLog.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MarketTradeLog _o_) {
		_o_._xdb_verify_unsafe_();
		buylog = new java.util.ArrayList<xbean.LogBean>();
		for (xbean.LogBean _v_ : _o_.buylog)
			buylog.add(new LogBean(_v_, this, "buylog"));
		salelog = new java.util.ArrayList<xbean.LogBean>();
		for (xbean.LogBean _v_ : _o_.salelog)
			salelog.add(new LogBean(_v_, this, "salelog"));
	}

	private void assign(MarketTradeLog.Data _o_) {
		buylog = new java.util.ArrayList<xbean.LogBean>();
		for (xbean.LogBean _v_ : _o_.buylog)
			buylog.add(new LogBean(_v_, this, "buylog"));
		salelog = new java.util.ArrayList<xbean.LogBean>();
		for (xbean.LogBean _v_ : _o_.salelog)
			salelog.add(new LogBean(_v_, this, "salelog"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(buylog.size());
		for (xbean.LogBean _v_ : buylog) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(salelog.size());
		for (xbean.LogBean _v_ : salelog) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.LogBean _v_ = new LogBean(0, this, "buylog");
			_v_.unmarshal(_os_);
			buylog.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.LogBean _v_ = new LogBean(0, this, "salelog");
			_v_.unmarshal(_os_);
			salelog.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.MarketTradeLog copy() {
		_xdb_verify_unsafe_();
		return new MarketTradeLog(this);
	}

	@Override
	public xbean.MarketTradeLog toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarketTradeLog toBean() {
		_xdb_verify_unsafe_();
		return new MarketTradeLog(this); // same as copy()
	}

	@Override
	public xbean.MarketTradeLog toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarketTradeLog toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.LogBean> getBuylog() { // 买记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "buylog"), buylog);
	}

	public java.util.List<xbean.LogBean> getBuylogAsData() { // 买记录
		_xdb_verify_unsafe_();
		java.util.List<xbean.LogBean> buylog;
		MarketTradeLog _o_ = this;
		buylog = new java.util.ArrayList<xbean.LogBean>();
		for (xbean.LogBean _v_ : _o_.buylog)
			buylog.add(new LogBean.Data(_v_));
		return buylog;
	}

	@Override
	public java.util.List<xbean.LogBean> getSalelog() { // 卖记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "salelog"), salelog);
	}

	public java.util.List<xbean.LogBean> getSalelogAsData() { // 卖记录
		_xdb_verify_unsafe_();
		java.util.List<xbean.LogBean> salelog;
		MarketTradeLog _o_ = this;
		salelog = new java.util.ArrayList<xbean.LogBean>();
		for (xbean.LogBean _v_ : _o_.salelog)
			salelog.add(new LogBean.Data(_v_));
		return salelog;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MarketTradeLog _o_ = null;
		if ( _o1_ instanceof MarketTradeLog ) _o_ = (MarketTradeLog)_o1_;
		else if ( _o1_ instanceof MarketTradeLog.Const ) _o_ = ((MarketTradeLog.Const)_o1_).nThis();
		else return false;
		if (!buylog.equals(_o_.buylog)) return false;
		if (!salelog.equals(_o_.salelog)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += buylog.hashCode();
		_h_ += salelog.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(buylog);
		_sb_.append(",");
		_sb_.append(salelog);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("buylog"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("salelog"));
		return lb;
	}

	private class Const implements xbean.MarketTradeLog {
		MarketTradeLog nThis() {
			return MarketTradeLog.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MarketTradeLog copy() {
			return MarketTradeLog.this.copy();
		}

		@Override
		public xbean.MarketTradeLog toData() {
			return MarketTradeLog.this.toData();
		}

		public xbean.MarketTradeLog toBean() {
			return MarketTradeLog.this.toBean();
		}

		@Override
		public xbean.MarketTradeLog toDataIf() {
			return MarketTradeLog.this.toDataIf();
		}

		public xbean.MarketTradeLog toBeanIf() {
			return MarketTradeLog.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.LogBean> getBuylog() { // 买记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(buylog);
		}

		public java.util.List<xbean.LogBean> getBuylogAsData() { // 买记录
			_xdb_verify_unsafe_();
			java.util.List<xbean.LogBean> buylog;
			MarketTradeLog _o_ = MarketTradeLog.this;
		buylog = new java.util.ArrayList<xbean.LogBean>();
		for (xbean.LogBean _v_ : _o_.buylog)
			buylog.add(new LogBean.Data(_v_));
			return buylog;
		}

		@Override
		public java.util.List<xbean.LogBean> getSalelog() { // 卖记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(salelog);
		}

		public java.util.List<xbean.LogBean> getSalelogAsData() { // 卖记录
			_xdb_verify_unsafe_();
			java.util.List<xbean.LogBean> salelog;
			MarketTradeLog _o_ = MarketTradeLog.this;
		salelog = new java.util.ArrayList<xbean.LogBean>();
		for (xbean.LogBean _v_ : _o_.salelog)
			salelog.add(new LogBean.Data(_v_));
			return salelog;
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
			return MarketTradeLog.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MarketTradeLog.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MarketTradeLog.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MarketTradeLog.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MarketTradeLog.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MarketTradeLog.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MarketTradeLog.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MarketTradeLog.this.hashCode();
		}

		@Override
		public String toString() {
			return MarketTradeLog.this.toString();
		}

	}

	public static final class Data implements xbean.MarketTradeLog {
		private java.util.ArrayList<xbean.LogBean> buylog; // 买记录
		private java.util.ArrayList<xbean.LogBean> salelog; // 卖记录

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			buylog = new java.util.ArrayList<xbean.LogBean>();
			salelog = new java.util.ArrayList<xbean.LogBean>();
		}

		Data(xbean.MarketTradeLog _o1_) {
			if (_o1_ instanceof MarketTradeLog) assign((MarketTradeLog)_o1_);
			else if (_o1_ instanceof MarketTradeLog.Data) assign((MarketTradeLog.Data)_o1_);
			else if (_o1_ instanceof MarketTradeLog.Const) assign(((MarketTradeLog.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MarketTradeLog _o_) {
			buylog = new java.util.ArrayList<xbean.LogBean>();
			for (xbean.LogBean _v_ : _o_.buylog)
				buylog.add(new LogBean.Data(_v_));
			salelog = new java.util.ArrayList<xbean.LogBean>();
			for (xbean.LogBean _v_ : _o_.salelog)
				salelog.add(new LogBean.Data(_v_));
		}

		private void assign(MarketTradeLog.Data _o_) {
			buylog = new java.util.ArrayList<xbean.LogBean>();
			for (xbean.LogBean _v_ : _o_.buylog)
				buylog.add(new LogBean.Data(_v_));
			salelog = new java.util.ArrayList<xbean.LogBean>();
			for (xbean.LogBean _v_ : _o_.salelog)
				salelog.add(new LogBean.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(buylog.size());
			for (xbean.LogBean _v_ : buylog) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(salelog.size());
			for (xbean.LogBean _v_ : salelog) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.LogBean _v_ = xbean.Pod.newLogBeanData();
				_v_.unmarshal(_os_);
				buylog.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.LogBean _v_ = xbean.Pod.newLogBeanData();
				_v_.unmarshal(_os_);
				salelog.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.MarketTradeLog copy() {
			return new Data(this);
		}

		@Override
		public xbean.MarketTradeLog toData() {
			return new Data(this);
		}

		public xbean.MarketTradeLog toBean() {
			return new MarketTradeLog(this, null, null);
		}

		@Override
		public xbean.MarketTradeLog toDataIf() {
			return this;
		}

		public xbean.MarketTradeLog toBeanIf() {
			return new MarketTradeLog(this, null, null);
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
		public java.util.List<xbean.LogBean> getBuylog() { // 买记录
			return buylog;
		}

		@Override
		public java.util.List<xbean.LogBean> getBuylogAsData() { // 买记录
			return buylog;
		}

		@Override
		public java.util.List<xbean.LogBean> getSalelog() { // 卖记录
			return salelog;
		}

		@Override
		public java.util.List<xbean.LogBean> getSalelogAsData() { // 卖记录
			return salelog;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MarketTradeLog.Data)) return false;
			MarketTradeLog.Data _o_ = (MarketTradeLog.Data) _o1_;
			if (!buylog.equals(_o_.buylog)) return false;
			if (!salelog.equals(_o_.salelog)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += buylog.hashCode();
			_h_ += salelog.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(buylog);
			_sb_.append(",");
			_sb_.append(salelog);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
