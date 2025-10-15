
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class EnhancementAttr extends mkdb.XBean implements xbean.EnhancementAttr {
	private int level; // 等级 by changhao
	private java.util.HashMap<Integer, Integer> attrmap; // 附魔增加的属性 by changhao

	@Override
	public void _reset_unsafe_() {
		level = 0;
		attrmap.clear();
	}

	EnhancementAttr(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		attrmap = new java.util.HashMap<Integer, Integer>();
	}

	public EnhancementAttr() {
		this(0, null, null);
	}

	public EnhancementAttr(EnhancementAttr _o_) {
		this(_o_, null, null);
	}

	EnhancementAttr(xbean.EnhancementAttr _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof EnhancementAttr) assign((EnhancementAttr)_o1_);
		else if (_o1_ instanceof EnhancementAttr.Data) assign((EnhancementAttr.Data)_o1_);
		else if (_o1_ instanceof EnhancementAttr.Const) assign(((EnhancementAttr.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(EnhancementAttr _o_) {
		_o_._xdb_verify_unsafe_();
		level = _o_.level;
		attrmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attrmap.entrySet())
			attrmap.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(EnhancementAttr.Data _o_) {
		level = _o_.level;
		attrmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attrmap.entrySet())
			attrmap.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(level);
		_os_.compact_uint32(attrmap.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : attrmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		level = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				attrmap = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				attrmap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.EnhancementAttr copy() {
		_xdb_verify_unsafe_();
		return new EnhancementAttr(this);
	}

	@Override
	public xbean.EnhancementAttr toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EnhancementAttr toBean() {
		_xdb_verify_unsafe_();
		return new EnhancementAttr(this); // same as copy()
	}

	@Override
	public xbean.EnhancementAttr toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EnhancementAttr toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getLevel() { // 等级 by changhao
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public java.util.Map<Integer, Integer> getAttrmap() { // 附魔增加的属性 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "attrmap"), attrmap);
	}

	@Override
	public java.util.Map<Integer, Integer> getAttrmapAsData() { // 附魔增加的属性 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> attrmap;
		EnhancementAttr _o_ = this;
		attrmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attrmap.entrySet())
			attrmap.put(_e_.getKey(), _e_.getValue());
		return attrmap;
	}

	@Override
	public void setLevel(int _v_) { // 等级 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		EnhancementAttr _o_ = null;
		if ( _o1_ instanceof EnhancementAttr ) _o_ = (EnhancementAttr)_o1_;
		else if ( _o1_ instanceof EnhancementAttr.Const ) _o_ = ((EnhancementAttr.Const)_o1_).nThis();
		else return false;
		if (level != _o_.level) return false;
		if (!attrmap.equals(_o_.attrmap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += level;
		_h_ += attrmap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(attrmap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("attrmap"));
		return lb;
	}

	private class Const implements xbean.EnhancementAttr {
		EnhancementAttr nThis() {
			return EnhancementAttr.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.EnhancementAttr copy() {
			return EnhancementAttr.this.copy();
		}

		@Override
		public xbean.EnhancementAttr toData() {
			return EnhancementAttr.this.toData();
		}

		public xbean.EnhancementAttr toBean() {
			return EnhancementAttr.this.toBean();
		}

		@Override
		public xbean.EnhancementAttr toDataIf() {
			return EnhancementAttr.this.toDataIf();
		}

		public xbean.EnhancementAttr toBeanIf() {
			return EnhancementAttr.this.toBeanIf();
		}

		@Override
		public int getLevel() { // 等级 by changhao
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public java.util.Map<Integer, Integer> getAttrmap() { // 附魔增加的属性 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(attrmap);
		}

		@Override
		public java.util.Map<Integer, Integer> getAttrmapAsData() { // 附魔增加的属性 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> attrmap;
			EnhancementAttr _o_ = EnhancementAttr.this;
			attrmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attrmap.entrySet())
				attrmap.put(_e_.getKey(), _e_.getValue());
			return attrmap;
		}

		@Override
		public void setLevel(int _v_) { // 等级 by changhao
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
			return EnhancementAttr.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return EnhancementAttr.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return EnhancementAttr.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return EnhancementAttr.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return EnhancementAttr.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return EnhancementAttr.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return EnhancementAttr.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return EnhancementAttr.this.hashCode();
		}

		@Override
		public String toString() {
			return EnhancementAttr.this.toString();
		}

	}

	public static final class Data implements xbean.EnhancementAttr {
		private int level; // 等级 by changhao
		private java.util.HashMap<Integer, Integer> attrmap; // 附魔增加的属性 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			attrmap = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.EnhancementAttr _o1_) {
			if (_o1_ instanceof EnhancementAttr) assign((EnhancementAttr)_o1_);
			else if (_o1_ instanceof EnhancementAttr.Data) assign((EnhancementAttr.Data)_o1_);
			else if (_o1_ instanceof EnhancementAttr.Const) assign(((EnhancementAttr.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(EnhancementAttr _o_) {
			level = _o_.level;
			attrmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attrmap.entrySet())
				attrmap.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(EnhancementAttr.Data _o_) {
			level = _o_.level;
			attrmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.attrmap.entrySet())
				attrmap.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(level);
			_os_.compact_uint32(attrmap.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : attrmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			level = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					attrmap = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					attrmap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.EnhancementAttr copy() {
			return new Data(this);
		}

		@Override
		public xbean.EnhancementAttr toData() {
			return new Data(this);
		}

		public xbean.EnhancementAttr toBean() {
			return new EnhancementAttr(this, null, null);
		}

		@Override
		public xbean.EnhancementAttr toDataIf() {
			return this;
		}

		public xbean.EnhancementAttr toBeanIf() {
			return new EnhancementAttr(this, null, null);
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
		public int getLevel() { // 等级 by changhao
			return level;
		}

		@Override
		public java.util.Map<Integer, Integer> getAttrmap() { // 附魔增加的属性 by changhao
			return attrmap;
		}

		@Override
		public java.util.Map<Integer, Integer> getAttrmapAsData() { // 附魔增加的属性 by changhao
			return attrmap;
		}

		@Override
		public void setLevel(int _v_) { // 等级 by changhao
			level = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof EnhancementAttr.Data)) return false;
			EnhancementAttr.Data _o_ = (EnhancementAttr.Data) _o1_;
			if (level != _o_.level) return false;
			if (!attrmap.equals(_o_.attrmap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += level;
			_h_ += attrmap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(attrmap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
