
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class EnhancementData extends mkdb.XBean implements xbean.EnhancementData {
	private java.util.HashMap<Integer, Integer> enhancementattr; // 附魔属性 by changhao
	private long enhancementtime; // 附魔时间 by changhao

	@Override
	public void _reset_unsafe_() {
		enhancementattr.clear();
		enhancementtime = 0L;
	}

	EnhancementData(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		enhancementattr = new java.util.HashMap<Integer, Integer>();
	}

	public EnhancementData() {
		this(0, null, null);
	}

	public EnhancementData(EnhancementData _o_) {
		this(_o_, null, null);
	}

	EnhancementData(xbean.EnhancementData _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof EnhancementData) assign((EnhancementData)_o1_);
		else if (_o1_ instanceof EnhancementData.Data) assign((EnhancementData.Data)_o1_);
		else if (_o1_ instanceof EnhancementData.Const) assign(((EnhancementData.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(EnhancementData _o_) {
		_o_._xdb_verify_unsafe_();
		enhancementattr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.enhancementattr.entrySet())
			enhancementattr.put(_e_.getKey(), _e_.getValue());
		enhancementtime = _o_.enhancementtime;
	}

	private void assign(EnhancementData.Data _o_) {
		enhancementattr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.enhancementattr.entrySet())
			enhancementattr.put(_e_.getKey(), _e_.getValue());
		enhancementtime = _o_.enhancementtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(enhancementattr.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : enhancementattr.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(enhancementtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				enhancementattr = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				enhancementattr.put(_k_, _v_);
			}
		}
		enhancementtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.EnhancementData copy() {
		_xdb_verify_unsafe_();
		return new EnhancementData(this);
	}

	@Override
	public xbean.EnhancementData toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EnhancementData toBean() {
		_xdb_verify_unsafe_();
		return new EnhancementData(this); // same as copy()
	}

	@Override
	public xbean.EnhancementData toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EnhancementData toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<Integer, Integer> getEnhancementattr() { // 附魔属性 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "enhancementattr"), enhancementattr);
	}

	@Override
	public java.util.Map<Integer, Integer> getEnhancementattrAsData() { // 附魔属性 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> enhancementattr;
		EnhancementData _o_ = this;
		enhancementattr = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.enhancementattr.entrySet())
			enhancementattr.put(_e_.getKey(), _e_.getValue());
		return enhancementattr;
	}

	@Override
	public long getEnhancementtime() { // 附魔时间 by changhao
		_xdb_verify_unsafe_();
		return enhancementtime;
	}

	@Override
	public void setEnhancementtime(long _v_) { // 附魔时间 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "enhancementtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, enhancementtime) {
					public void rollback() { enhancementtime = _xdb_saved; }
				};}});
		enhancementtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		EnhancementData _o_ = null;
		if ( _o1_ instanceof EnhancementData ) _o_ = (EnhancementData)_o1_;
		else if ( _o1_ instanceof EnhancementData.Const ) _o_ = ((EnhancementData.Const)_o1_).nThis();
		else return false;
		if (!enhancementattr.equals(_o_.enhancementattr)) return false;
		if (enhancementtime != _o_.enhancementtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += enhancementattr.hashCode();
		_h_ += enhancementtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(enhancementattr);
		_sb_.append(",");
		_sb_.append(enhancementtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("enhancementattr"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("enhancementtime"));
		return lb;
	}

	private class Const implements xbean.EnhancementData {
		EnhancementData nThis() {
			return EnhancementData.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.EnhancementData copy() {
			return EnhancementData.this.copy();
		}

		@Override
		public xbean.EnhancementData toData() {
			return EnhancementData.this.toData();
		}

		public xbean.EnhancementData toBean() {
			return EnhancementData.this.toBean();
		}

		@Override
		public xbean.EnhancementData toDataIf() {
			return EnhancementData.this.toDataIf();
		}

		public xbean.EnhancementData toBeanIf() {
			return EnhancementData.this.toBeanIf();
		}

		@Override
		public java.util.Map<Integer, Integer> getEnhancementattr() { // 附魔属性 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(enhancementattr);
		}

		@Override
		public java.util.Map<Integer, Integer> getEnhancementattrAsData() { // 附魔属性 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> enhancementattr;
			EnhancementData _o_ = EnhancementData.this;
			enhancementattr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.enhancementattr.entrySet())
				enhancementattr.put(_e_.getKey(), _e_.getValue());
			return enhancementattr;
		}

		@Override
		public long getEnhancementtime() { // 附魔时间 by changhao
			_xdb_verify_unsafe_();
			return enhancementtime;
		}

		@Override
		public void setEnhancementtime(long _v_) { // 附魔时间 by changhao
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
			return EnhancementData.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return EnhancementData.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return EnhancementData.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return EnhancementData.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return EnhancementData.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return EnhancementData.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return EnhancementData.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return EnhancementData.this.hashCode();
		}

		@Override
		public String toString() {
			return EnhancementData.this.toString();
		}

	}

	public static final class Data implements xbean.EnhancementData {
		private java.util.HashMap<Integer, Integer> enhancementattr; // 附魔属性 by changhao
		private long enhancementtime; // 附魔时间 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			enhancementattr = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.EnhancementData _o1_) {
			if (_o1_ instanceof EnhancementData) assign((EnhancementData)_o1_);
			else if (_o1_ instanceof EnhancementData.Data) assign((EnhancementData.Data)_o1_);
			else if (_o1_ instanceof EnhancementData.Const) assign(((EnhancementData.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(EnhancementData _o_) {
			enhancementattr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.enhancementattr.entrySet())
				enhancementattr.put(_e_.getKey(), _e_.getValue());
			enhancementtime = _o_.enhancementtime;
		}

		private void assign(EnhancementData.Data _o_) {
			enhancementattr = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.enhancementattr.entrySet())
				enhancementattr.put(_e_.getKey(), _e_.getValue());
			enhancementtime = _o_.enhancementtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(enhancementattr.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : enhancementattr.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(enhancementtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					enhancementattr = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					enhancementattr.put(_k_, _v_);
				}
			}
			enhancementtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.EnhancementData copy() {
			return new Data(this);
		}

		@Override
		public xbean.EnhancementData toData() {
			return new Data(this);
		}

		public xbean.EnhancementData toBean() {
			return new EnhancementData(this, null, null);
		}

		@Override
		public xbean.EnhancementData toDataIf() {
			return this;
		}

		public xbean.EnhancementData toBeanIf() {
			return new EnhancementData(this, null, null);
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
		public java.util.Map<Integer, Integer> getEnhancementattr() { // 附魔属性 by changhao
			return enhancementattr;
		}

		@Override
		public java.util.Map<Integer, Integer> getEnhancementattrAsData() { // 附魔属性 by changhao
			return enhancementattr;
		}

		@Override
		public long getEnhancementtime() { // 附魔时间 by changhao
			return enhancementtime;
		}

		@Override
		public void setEnhancementtime(long _v_) { // 附魔时间 by changhao
			enhancementtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof EnhancementData.Data)) return false;
			EnhancementData.Data _o_ = (EnhancementData.Data) _o1_;
			if (!enhancementattr.equals(_o_.enhancementattr)) return false;
			if (enhancementtime != _o_.enhancementtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += enhancementattr.hashCode();
			_h_ += enhancementtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(enhancementattr);
			_sb_.append(",");
			_sb_.append(enhancementtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
