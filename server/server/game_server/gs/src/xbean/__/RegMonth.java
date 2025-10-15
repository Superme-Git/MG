
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RegMonth extends mkdb.XBean implements xbean.RegMonth {
	private java.util.HashMap<Integer, xbean.RegDay> daymap; // 
	private int suppregnum; // 补签次数

	@Override
	public void _reset_unsafe_() {
		daymap.clear();
		suppregnum = 0;
	}

	RegMonth(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		daymap = new java.util.HashMap<Integer, xbean.RegDay>();
	}

	public RegMonth() {
		this(0, null, null);
	}

	public RegMonth(RegMonth _o_) {
		this(_o_, null, null);
	}

	RegMonth(xbean.RegMonth _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RegMonth) assign((RegMonth)_o1_);
		else if (_o1_ instanceof RegMonth.Data) assign((RegMonth.Data)_o1_);
		else if (_o1_ instanceof RegMonth.Const) assign(((RegMonth.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RegMonth _o_) {
		_o_._xdb_verify_unsafe_();
		daymap = new java.util.HashMap<Integer, xbean.RegDay>();
		for (java.util.Map.Entry<Integer, xbean.RegDay> _e_ : _o_.daymap.entrySet())
			daymap.put(_e_.getKey(), new RegDay(_e_.getValue(), this, "daymap"));
		suppregnum = _o_.suppregnum;
	}

	private void assign(RegMonth.Data _o_) {
		daymap = new java.util.HashMap<Integer, xbean.RegDay>();
		for (java.util.Map.Entry<Integer, xbean.RegDay> _e_ : _o_.daymap.entrySet())
			daymap.put(_e_.getKey(), new RegDay(_e_.getValue(), this, "daymap"));
		suppregnum = _o_.suppregnum;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(daymap.size());
		for (java.util.Map.Entry<Integer, xbean.RegDay> _e_ : daymap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(suppregnum);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				daymap = new java.util.HashMap<Integer, xbean.RegDay>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.RegDay _v_ = new RegDay(0, this, "daymap");
				_v_.unmarshal(_os_);
				daymap.put(_k_, _v_);
			}
		}
		suppregnum = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RegMonth copy() {
		_xdb_verify_unsafe_();
		return new RegMonth(this);
	}

	@Override
	public xbean.RegMonth toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RegMonth toBean() {
		_xdb_verify_unsafe_();
		return new RegMonth(this); // same as copy()
	}

	@Override
	public xbean.RegMonth toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RegMonth toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, xbean.RegDay> getDaymap() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "daymap"), daymap);
	}

	@Override
	public java.util.Map<Integer, xbean.RegDay> getDaymapAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.RegDay> daymap;
		RegMonth _o_ = this;
		daymap = new java.util.HashMap<Integer, xbean.RegDay>();
		for (java.util.Map.Entry<Integer, xbean.RegDay> _e_ : _o_.daymap.entrySet())
			daymap.put(_e_.getKey(), new RegDay.Data(_e_.getValue()));
		return daymap;
	}

	@Override
	public int getSuppregnum() { // 补签次数
		_xdb_verify_unsafe_();
		return suppregnum;
	}

	@Override
	public void setSuppregnum(int _v_) { // 补签次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "suppregnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, suppregnum) {
					public void rollback() { suppregnum = _xdb_saved; }
				};}});
		suppregnum = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RegMonth _o_ = null;
		if ( _o1_ instanceof RegMonth ) _o_ = (RegMonth)_o1_;
		else if ( _o1_ instanceof RegMonth.Const ) _o_ = ((RegMonth.Const)_o1_).nThis();
		else return false;
		if (!daymap.equals(_o_.daymap)) return false;
		if (suppregnum != _o_.suppregnum) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += daymap.hashCode();
		_h_ += suppregnum;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(daymap);
		_sb_.append(",");
		_sb_.append(suppregnum);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("daymap"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("suppregnum"));
		return lb;
	}

	private class Const implements xbean.RegMonth {
		RegMonth nThis() {
			return RegMonth.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RegMonth copy() {
			return RegMonth.this.copy();
		}

		@Override
		public xbean.RegMonth toData() {
			return RegMonth.this.toData();
		}

		public xbean.RegMonth toBean() {
			return RegMonth.this.toBean();
		}

		@Override
		public xbean.RegMonth toDataIf() {
			return RegMonth.this.toDataIf();
		}

		public xbean.RegMonth toBeanIf() {
			return RegMonth.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, xbean.RegDay> getDaymap() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(daymap);
		}

		@Override
		public java.util.Map<Integer, xbean.RegDay> getDaymapAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.RegDay> daymap;
			RegMonth _o_ = RegMonth.this;
			daymap = new java.util.HashMap<Integer, xbean.RegDay>();
			for (java.util.Map.Entry<Integer, xbean.RegDay> _e_ : _o_.daymap.entrySet())
				daymap.put(_e_.getKey(), new RegDay.Data(_e_.getValue()));
			return daymap;
		}

		@Override
		public int getSuppregnum() { // 补签次数
			_xdb_verify_unsafe_();
			return suppregnum;
		}

		@Override
		public void setSuppregnum(int _v_) { // 补签次数
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
			return RegMonth.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RegMonth.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RegMonth.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RegMonth.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RegMonth.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RegMonth.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RegMonth.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RegMonth.this.hashCode();
		}

		@Override
		public String toString() {
			return RegMonth.this.toString();
		}

	}

	public static final class Data implements xbean.RegMonth {
		private java.util.HashMap<Integer, xbean.RegDay> daymap; // 
		private int suppregnum; // 补签次数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			daymap = new java.util.HashMap<Integer, xbean.RegDay>();
		}

		Data(xbean.RegMonth _o1_) {
			if (_o1_ instanceof RegMonth) assign((RegMonth)_o1_);
			else if (_o1_ instanceof RegMonth.Data) assign((RegMonth.Data)_o1_);
			else if (_o1_ instanceof RegMonth.Const) assign(((RegMonth.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RegMonth _o_) {
			daymap = new java.util.HashMap<Integer, xbean.RegDay>();
			for (java.util.Map.Entry<Integer, xbean.RegDay> _e_ : _o_.daymap.entrySet())
				daymap.put(_e_.getKey(), new RegDay.Data(_e_.getValue()));
			suppregnum = _o_.suppregnum;
		}

		private void assign(RegMonth.Data _o_) {
			daymap = new java.util.HashMap<Integer, xbean.RegDay>();
			for (java.util.Map.Entry<Integer, xbean.RegDay> _e_ : _o_.daymap.entrySet())
				daymap.put(_e_.getKey(), new RegDay.Data(_e_.getValue()));
			suppregnum = _o_.suppregnum;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(daymap.size());
			for (java.util.Map.Entry<Integer, xbean.RegDay> _e_ : daymap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(suppregnum);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					daymap = new java.util.HashMap<Integer, xbean.RegDay>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.RegDay _v_ = xbean.Pod.newRegDayData();
					_v_.unmarshal(_os_);
					daymap.put(_k_, _v_);
				}
			}
			suppregnum = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RegMonth copy() {
			return new Data(this);
		}

		@Override
		public xbean.RegMonth toData() {
			return new Data(this);
		}

		public xbean.RegMonth toBean() {
			return new RegMonth(this, null, null);
		}

		@Override
		public xbean.RegMonth toDataIf() {
			return this;
		}

		public xbean.RegMonth toBeanIf() {
			return new RegMonth(this, null, null);
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
		public java.util.Map<Integer, xbean.RegDay> getDaymap() { // 
			return daymap;
		}

		@Override
		public java.util.Map<Integer, xbean.RegDay> getDaymapAsData() { // 
			return daymap;
		}

		@Override
		public int getSuppregnum() { // 补签次数
			return suppregnum;
		}

		@Override
		public void setSuppregnum(int _v_) { // 补签次数
			suppregnum = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RegMonth.Data)) return false;
			RegMonth.Data _o_ = (RegMonth.Data) _o1_;
			if (!daymap.equals(_o_.daymap)) return false;
			if (suppregnum != _o_.suppregnum) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += daymap.hashCode();
			_h_ += suppregnum;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(daymap);
			_sb_.append(",");
			_sb_.append(suppregnum);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
