
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RenXingData extends mkdb.XBean implements xbean.RenXingData {
	private java.util.HashMap<Integer, Integer> renxinmap; // key 为循环类型

	@Override
	public void _reset_unsafe_() {
		renxinmap.clear();
	}

	RenXingData(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		renxinmap = new java.util.HashMap<Integer, Integer>();
	}

	public RenXingData() {
		this(0, null, null);
	}

	public RenXingData(RenXingData _o_) {
		this(_o_, null, null);
	}

	RenXingData(xbean.RenXingData _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RenXingData) assign((RenXingData)_o1_);
		else if (_o1_ instanceof RenXingData.Data) assign((RenXingData.Data)_o1_);
		else if (_o1_ instanceof RenXingData.Const) assign(((RenXingData.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RenXingData _o_) {
		_o_._xdb_verify_unsafe_();
		renxinmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.renxinmap.entrySet())
			renxinmap.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(RenXingData.Data _o_) {
		renxinmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.renxinmap.entrySet())
			renxinmap.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(renxinmap.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : renxinmap.entrySet())
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
				renxinmap = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				renxinmap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RenXingData copy() {
		_xdb_verify_unsafe_();
		return new RenXingData(this);
	}

	@Override
	public xbean.RenXingData toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RenXingData toBean() {
		_xdb_verify_unsafe_();
		return new RenXingData(this); // same as copy()
	}

	@Override
	public xbean.RenXingData toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RenXingData toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Integer> getRenxinmap() { // key 为循环类型
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "renxinmap"), renxinmap);
	}

	@Override
	public java.util.Map<Integer, Integer> getRenxinmapAsData() { // key 为循环类型
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> renxinmap;
		RenXingData _o_ = this;
		renxinmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.renxinmap.entrySet())
			renxinmap.put(_e_.getKey(), _e_.getValue());
		return renxinmap;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RenXingData _o_ = null;
		if ( _o1_ instanceof RenXingData ) _o_ = (RenXingData)_o1_;
		else if ( _o1_ instanceof RenXingData.Const ) _o_ = ((RenXingData.Const)_o1_).nThis();
		else return false;
		if (!renxinmap.equals(_o_.renxinmap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += renxinmap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(renxinmap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("renxinmap"));
		return lb;
	}

	private class Const implements xbean.RenXingData {
		RenXingData nThis() {
			return RenXingData.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RenXingData copy() {
			return RenXingData.this.copy();
		}

		@Override
		public xbean.RenXingData toData() {
			return RenXingData.this.toData();
		}

		public xbean.RenXingData toBean() {
			return RenXingData.this.toBean();
		}

		@Override
		public xbean.RenXingData toDataIf() {
			return RenXingData.this.toDataIf();
		}

		public xbean.RenXingData toBeanIf() {
			return RenXingData.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Integer> getRenxinmap() { // key 为循环类型
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(renxinmap);
		}

		@Override
		public java.util.Map<Integer, Integer> getRenxinmapAsData() { // key 为循环类型
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> renxinmap;
			RenXingData _o_ = RenXingData.this;
			renxinmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.renxinmap.entrySet())
				renxinmap.put(_e_.getKey(), _e_.getValue());
			return renxinmap;
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
			return RenXingData.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RenXingData.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RenXingData.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RenXingData.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RenXingData.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RenXingData.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RenXingData.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RenXingData.this.hashCode();
		}

		@Override
		public String toString() {
			return RenXingData.this.toString();
		}

	}

	public static final class Data implements xbean.RenXingData {
		private java.util.HashMap<Integer, Integer> renxinmap; // key 为循环类型

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			renxinmap = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.RenXingData _o1_) {
			if (_o1_ instanceof RenXingData) assign((RenXingData)_o1_);
			else if (_o1_ instanceof RenXingData.Data) assign((RenXingData.Data)_o1_);
			else if (_o1_ instanceof RenXingData.Const) assign(((RenXingData.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RenXingData _o_) {
			renxinmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.renxinmap.entrySet())
				renxinmap.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(RenXingData.Data _o_) {
			renxinmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.renxinmap.entrySet())
				renxinmap.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(renxinmap.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : renxinmap.entrySet())
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
					renxinmap = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					renxinmap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RenXingData copy() {
			return new Data(this);
		}

		@Override
		public xbean.RenXingData toData() {
			return new Data(this);
		}

		public xbean.RenXingData toBean() {
			return new RenXingData(this, null, null);
		}

		@Override
		public xbean.RenXingData toDataIf() {
			return this;
		}

		public xbean.RenXingData toBeanIf() {
			return new RenXingData(this, null, null);
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
		public java.util.Map<Integer, Integer> getRenxinmap() { // key 为循环类型
			return renxinmap;
		}

		@Override
		public java.util.Map<Integer, Integer> getRenxinmapAsData() { // key 为循环类型
			return renxinmap;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RenXingData.Data)) return false;
			RenXingData.Data _o_ = (RenXingData.Data) _o1_;
			if (!renxinmap.equals(_o_.renxinmap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += renxinmap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(renxinmap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
