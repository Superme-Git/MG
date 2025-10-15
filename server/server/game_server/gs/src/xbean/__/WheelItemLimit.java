
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class WheelItemLimit extends mkdb.XBean implements xbean.WheelItemLimit {
	private java.util.HashMap<Integer, Integer> limitmap; // 

	@Override
	public void _reset_unsafe_() {
		limitmap.clear();
	}

	WheelItemLimit(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		limitmap = new java.util.HashMap<Integer, Integer>();
	}

	public WheelItemLimit() {
		this(0, null, null);
	}

	public WheelItemLimit(WheelItemLimit _o_) {
		this(_o_, null, null);
	}

	WheelItemLimit(xbean.WheelItemLimit _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof WheelItemLimit) assign((WheelItemLimit)_o1_);
		else if (_o1_ instanceof WheelItemLimit.Data) assign((WheelItemLimit.Data)_o1_);
		else if (_o1_ instanceof WheelItemLimit.Const) assign(((WheelItemLimit.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(WheelItemLimit _o_) {
		_o_._xdb_verify_unsafe_();
		limitmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.limitmap.entrySet())
			limitmap.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(WheelItemLimit.Data _o_) {
		limitmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.limitmap.entrySet())
			limitmap.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(limitmap.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : limitmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				limitmap = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				limitmap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.WheelItemLimit copy() {
		_xdb_verify_unsafe_();
		return new WheelItemLimit(this);
	}

	@Override
	public xbean.WheelItemLimit toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WheelItemLimit toBean() {
		_xdb_verify_unsafe_();
		return new WheelItemLimit(this); // same as copy()
	}

	@Override
	public xbean.WheelItemLimit toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WheelItemLimit toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Integer> getLimitmap() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "limitmap"), limitmap);
	}

	@Override
	public java.util.Map<Integer, Integer> getLimitmapAsData() { // 
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> limitmap;
		WheelItemLimit _o_ = this;
		limitmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.limitmap.entrySet())
			limitmap.put(_e_.getKey(), _e_.getValue());
		return limitmap;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		WheelItemLimit _o_ = null;
		if ( _o1_ instanceof WheelItemLimit ) _o_ = (WheelItemLimit)_o1_;
		else if ( _o1_ instanceof WheelItemLimit.Const ) _o_ = ((WheelItemLimit.Const)_o1_).nThis();
		else return false;
		if (!limitmap.equals(_o_.limitmap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += limitmap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(limitmap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("limitmap"));
		return lb;
	}

	private class Const implements xbean.WheelItemLimit {
		WheelItemLimit nThis() {
			return WheelItemLimit.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.WheelItemLimit copy() {
			return WheelItemLimit.this.copy();
		}

		@Override
		public xbean.WheelItemLimit toData() {
			return WheelItemLimit.this.toData();
		}

		public xbean.WheelItemLimit toBean() {
			return WheelItemLimit.this.toBean();
		}

		@Override
		public xbean.WheelItemLimit toDataIf() {
			return WheelItemLimit.this.toDataIf();
		}

		public xbean.WheelItemLimit toBeanIf() {
			return WheelItemLimit.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Integer> getLimitmap() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(limitmap);
		}

		@Override
		public java.util.Map<Integer, Integer> getLimitmapAsData() { // 
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> limitmap;
			WheelItemLimit _o_ = WheelItemLimit.this;
			limitmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.limitmap.entrySet())
				limitmap.put(_e_.getKey(), _e_.getValue());
			return limitmap;
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
			return WheelItemLimit.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return WheelItemLimit.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return WheelItemLimit.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return WheelItemLimit.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return WheelItemLimit.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return WheelItemLimit.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return WheelItemLimit.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return WheelItemLimit.this.hashCode();
		}

		@Override
		public String toString() {
			return WheelItemLimit.this.toString();
		}

	}

	public static final class Data implements xbean.WheelItemLimit {
		private java.util.HashMap<Integer, Integer> limitmap; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			limitmap = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.WheelItemLimit _o1_) {
			if (_o1_ instanceof WheelItemLimit) assign((WheelItemLimit)_o1_);
			else if (_o1_ instanceof WheelItemLimit.Data) assign((WheelItemLimit.Data)_o1_);
			else if (_o1_ instanceof WheelItemLimit.Const) assign(((WheelItemLimit.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(WheelItemLimit _o_) {
			limitmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.limitmap.entrySet())
				limitmap.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(WheelItemLimit.Data _o_) {
			limitmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.limitmap.entrySet())
				limitmap.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(limitmap.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : limitmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					limitmap = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					limitmap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.WheelItemLimit copy() {
			return new Data(this);
		}

		@Override
		public xbean.WheelItemLimit toData() {
			return new Data(this);
		}

		public xbean.WheelItemLimit toBean() {
			return new WheelItemLimit(this, null, null);
		}

		@Override
		public xbean.WheelItemLimit toDataIf() {
			return this;
		}

		public xbean.WheelItemLimit toBeanIf() {
			return new WheelItemLimit(this, null, null);
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
		public java.util.Map<Integer, Integer> getLimitmap() { // 
			return limitmap;
		}

		@Override
		public java.util.Map<Integer, Integer> getLimitmapAsData() { // 
			return limitmap;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof WheelItemLimit.Data)) return false;
			WheelItemLimit.Data _o_ = (WheelItemLimit.Data) _o1_;
			if (!limitmap.equals(_o_.limitmap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += limitmap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(limitmap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
