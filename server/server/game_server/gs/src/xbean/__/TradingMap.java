
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TradingMap extends mkdb.XBean implements xbean.TradingMap {
	private java.util.LinkedList<xbean.TradingPrice> selllist; // 排序好的出售列表
	private java.util.LinkedList<xbean.TradingPrice> buylist; // 排序好的购买列表

	@Override
	public void _reset_unsafe_() {
		selllist.clear();
		buylist.clear();
	}

	TradingMap(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		selllist = new java.util.LinkedList<xbean.TradingPrice>();
		buylist = new java.util.LinkedList<xbean.TradingPrice>();
	}

	public TradingMap() {
		this(0, null, null);
	}

	public TradingMap(TradingMap _o_) {
		this(_o_, null, null);
	}

	TradingMap(xbean.TradingMap _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TradingMap) assign((TradingMap)_o1_);
		else if (_o1_ instanceof TradingMap.Data) assign((TradingMap.Data)_o1_);
		else if (_o1_ instanceof TradingMap.Const) assign(((TradingMap.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TradingMap _o_) {
		_o_._xdb_verify_unsafe_();
		selllist = new java.util.LinkedList<xbean.TradingPrice>();
		for (xbean.TradingPrice _v_ : _o_.selllist)
			selllist.add(new TradingPrice(_v_, this, "selllist"));
		buylist = new java.util.LinkedList<xbean.TradingPrice>();
		for (xbean.TradingPrice _v_ : _o_.buylist)
			buylist.add(new TradingPrice(_v_, this, "buylist"));
	}

	private void assign(TradingMap.Data _o_) {
		selllist = new java.util.LinkedList<xbean.TradingPrice>();
		for (xbean.TradingPrice _v_ : _o_.selllist)
			selllist.add(new TradingPrice(_v_, this, "selllist"));
		buylist = new java.util.LinkedList<xbean.TradingPrice>();
		for (xbean.TradingPrice _v_ : _o_.buylist)
			buylist.add(new TradingPrice(_v_, this, "buylist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(selllist.size());
		for (xbean.TradingPrice _v_ : selllist) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(buylist.size());
		for (xbean.TradingPrice _v_ : buylist) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.TradingPrice _v_ = new TradingPrice(0, this, "selllist");
			_v_.unmarshal(_os_);
			selllist.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.TradingPrice _v_ = new TradingPrice(0, this, "buylist");
			_v_.unmarshal(_os_);
			buylist.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.TradingMap copy() {
		_xdb_verify_unsafe_();
		return new TradingMap(this);
	}

	@Override
	public xbean.TradingMap toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TradingMap toBean() {
		_xdb_verify_unsafe_();
		return new TradingMap(this); // same as copy()
	}

	@Override
	public xbean.TradingMap toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TradingMap toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<xbean.TradingPrice> getSelllist() { // 排序好的出售列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "selllist"), selllist);
	}

	public java.util.List<xbean.TradingPrice> getSelllistAsData() { // 排序好的出售列表
		_xdb_verify_unsafe_();
		java.util.List<xbean.TradingPrice> selllist;
		TradingMap _o_ = this;
		selllist = new java.util.LinkedList<xbean.TradingPrice>();
		for (xbean.TradingPrice _v_ : _o_.selllist)
			selllist.add(new TradingPrice.Data(_v_));
		return selllist;
	}

	@Override
	public java.util.List<xbean.TradingPrice> getBuylist() { // 排序好的购买列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "buylist"), buylist);
	}

	public java.util.List<xbean.TradingPrice> getBuylistAsData() { // 排序好的购买列表
		_xdb_verify_unsafe_();
		java.util.List<xbean.TradingPrice> buylist;
		TradingMap _o_ = this;
		buylist = new java.util.LinkedList<xbean.TradingPrice>();
		for (xbean.TradingPrice _v_ : _o_.buylist)
			buylist.add(new TradingPrice.Data(_v_));
		return buylist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TradingMap _o_ = null;
		if ( _o1_ instanceof TradingMap ) _o_ = (TradingMap)_o1_;
		else if ( _o1_ instanceof TradingMap.Const ) _o_ = ((TradingMap.Const)_o1_).nThis();
		else return false;
		if (!selllist.equals(_o_.selllist)) return false;
		if (!buylist.equals(_o_.buylist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += selllist.hashCode();
		_h_ += buylist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(selllist);
		_sb_.append(",");
		_sb_.append(buylist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("selllist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("buylist"));
		return lb;
	}

	private class Const implements xbean.TradingMap {
		TradingMap nThis() {
			return TradingMap.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TradingMap copy() {
			return TradingMap.this.copy();
		}

		@Override
		public xbean.TradingMap toData() {
			return TradingMap.this.toData();
		}

		public xbean.TradingMap toBean() {
			return TradingMap.this.toBean();
		}

		@Override
		public xbean.TradingMap toDataIf() {
			return TradingMap.this.toDataIf();
		}

		public xbean.TradingMap toBeanIf() {
			return TradingMap.this.toBeanIf();
		}

		@Override
		public java.util.List<xbean.TradingPrice> getSelllist() { // 排序好的出售列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(selllist);
		}

		public java.util.List<xbean.TradingPrice> getSelllistAsData() { // 排序好的出售列表
			_xdb_verify_unsafe_();
			java.util.List<xbean.TradingPrice> selllist;
			TradingMap _o_ = TradingMap.this;
		selllist = new java.util.LinkedList<xbean.TradingPrice>();
		for (xbean.TradingPrice _v_ : _o_.selllist)
			selllist.add(new TradingPrice.Data(_v_));
			return selllist;
		}

		@Override
		public java.util.List<xbean.TradingPrice> getBuylist() { // 排序好的购买列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(buylist);
		}

		public java.util.List<xbean.TradingPrice> getBuylistAsData() { // 排序好的购买列表
			_xdb_verify_unsafe_();
			java.util.List<xbean.TradingPrice> buylist;
			TradingMap _o_ = TradingMap.this;
		buylist = new java.util.LinkedList<xbean.TradingPrice>();
		for (xbean.TradingPrice _v_ : _o_.buylist)
			buylist.add(new TradingPrice.Data(_v_));
			return buylist;
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
			return TradingMap.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TradingMap.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TradingMap.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TradingMap.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TradingMap.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TradingMap.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TradingMap.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TradingMap.this.hashCode();
		}

		@Override
		public String toString() {
			return TradingMap.this.toString();
		}

	}

	public static final class Data implements xbean.TradingMap {
		private java.util.LinkedList<xbean.TradingPrice> selllist; // 排序好的出售列表
		private java.util.LinkedList<xbean.TradingPrice> buylist; // 排序好的购买列表

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			selllist = new java.util.LinkedList<xbean.TradingPrice>();
			buylist = new java.util.LinkedList<xbean.TradingPrice>();
		}

		Data(xbean.TradingMap _o1_) {
			if (_o1_ instanceof TradingMap) assign((TradingMap)_o1_);
			else if (_o1_ instanceof TradingMap.Data) assign((TradingMap.Data)_o1_);
			else if (_o1_ instanceof TradingMap.Const) assign(((TradingMap.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TradingMap _o_) {
			selllist = new java.util.LinkedList<xbean.TradingPrice>();
			for (xbean.TradingPrice _v_ : _o_.selllist)
				selllist.add(new TradingPrice.Data(_v_));
			buylist = new java.util.LinkedList<xbean.TradingPrice>();
			for (xbean.TradingPrice _v_ : _o_.buylist)
				buylist.add(new TradingPrice.Data(_v_));
		}

		private void assign(TradingMap.Data _o_) {
			selllist = new java.util.LinkedList<xbean.TradingPrice>();
			for (xbean.TradingPrice _v_ : _o_.selllist)
				selllist.add(new TradingPrice.Data(_v_));
			buylist = new java.util.LinkedList<xbean.TradingPrice>();
			for (xbean.TradingPrice _v_ : _o_.buylist)
				buylist.add(new TradingPrice.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(selllist.size());
			for (xbean.TradingPrice _v_ : selllist) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(buylist.size());
			for (xbean.TradingPrice _v_ : buylist) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.TradingPrice _v_ = xbean.Pod.newTradingPriceData();
				_v_.unmarshal(_os_);
				selllist.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.TradingPrice _v_ = xbean.Pod.newTradingPriceData();
				_v_.unmarshal(_os_);
				buylist.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.TradingMap copy() {
			return new Data(this);
		}

		@Override
		public xbean.TradingMap toData() {
			return new Data(this);
		}

		public xbean.TradingMap toBean() {
			return new TradingMap(this, null, null);
		}

		@Override
		public xbean.TradingMap toDataIf() {
			return this;
		}

		public xbean.TradingMap toBeanIf() {
			return new TradingMap(this, null, null);
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
		public java.util.List<xbean.TradingPrice> getSelllist() { // 排序好的出售列表
			return selllist;
		}

		@Override
		public java.util.List<xbean.TradingPrice> getSelllistAsData() { // 排序好的出售列表
			return selllist;
		}

		@Override
		public java.util.List<xbean.TradingPrice> getBuylist() { // 排序好的购买列表
			return buylist;
		}

		@Override
		public java.util.List<xbean.TradingPrice> getBuylistAsData() { // 排序好的购买列表
			return buylist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TradingMap.Data)) return false;
			TradingMap.Data _o_ = (TradingMap.Data) _o1_;
			if (!selllist.equals(_o_.selllist)) return false;
			if (!buylist.equals(_o_.buylist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += selllist.hashCode();
			_h_ += buylist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(selllist);
			_sb_.append(",");
			_sb_.append(buylist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
