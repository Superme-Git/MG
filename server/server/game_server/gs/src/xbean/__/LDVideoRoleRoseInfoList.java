
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class LDVideoRoleRoseInfoList extends mkdb.XBean implements xbean.LDVideoRoleRoseInfoList {
	private java.util.HashMap<String, xbean.LDVideoRoleRoseInfo> ldvideoroleroseinfolist; // 点赞记录 key=videoid

	@Override
	public void _reset_unsafe_() {
		ldvideoroleroseinfolist.clear();
	}

	LDVideoRoleRoseInfoList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		ldvideoroleroseinfolist = new java.util.HashMap<String, xbean.LDVideoRoleRoseInfo>();
	}

	public LDVideoRoleRoseInfoList() {
		this(0, null, null);
	}

	public LDVideoRoleRoseInfoList(LDVideoRoleRoseInfoList _o_) {
		this(_o_, null, null);
	}

	LDVideoRoleRoseInfoList(xbean.LDVideoRoleRoseInfoList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LDVideoRoleRoseInfoList) assign((LDVideoRoleRoseInfoList)_o1_);
		else if (_o1_ instanceof LDVideoRoleRoseInfoList.Data) assign((LDVideoRoleRoseInfoList.Data)_o1_);
		else if (_o1_ instanceof LDVideoRoleRoseInfoList.Const) assign(((LDVideoRoleRoseInfoList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LDVideoRoleRoseInfoList _o_) {
		_o_._xdb_verify_unsafe_();
		ldvideoroleroseinfolist = new java.util.HashMap<String, xbean.LDVideoRoleRoseInfo>();
		for (java.util.Map.Entry<String, xbean.LDVideoRoleRoseInfo> _e_ : _o_.ldvideoroleroseinfolist.entrySet())
			ldvideoroleroseinfolist.put(_e_.getKey(), new LDVideoRoleRoseInfo(_e_.getValue(), this, "ldvideoroleroseinfolist"));
	}

	private void assign(LDVideoRoleRoseInfoList.Data _o_) {
		ldvideoroleroseinfolist = new java.util.HashMap<String, xbean.LDVideoRoleRoseInfo>();
		for (java.util.Map.Entry<String, xbean.LDVideoRoleRoseInfo> _e_ : _o_.ldvideoroleroseinfolist.entrySet())
			ldvideoroleroseinfolist.put(_e_.getKey(), new LDVideoRoleRoseInfo(_e_.getValue(), this, "ldvideoroleroseinfolist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(ldvideoroleroseinfolist.size());
		for (java.util.Map.Entry<String, xbean.LDVideoRoleRoseInfo> _e_ : ldvideoroleroseinfolist.entrySet())
		{
			_os_.marshal(_e_.getKey(), mkdb.Const.IO_CHARSET);
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
				ldvideoroleroseinfolist = new java.util.HashMap<String, xbean.LDVideoRoleRoseInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				String _k_ = "";
				_k_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				xbean.LDVideoRoleRoseInfo _v_ = new LDVideoRoleRoseInfo(0, this, "ldvideoroleroseinfolist");
				_v_.unmarshal(_os_);
				ldvideoroleroseinfolist.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.LDVideoRoleRoseInfoList copy() {
		_xdb_verify_unsafe_();
		return new LDVideoRoleRoseInfoList(this);
	}

	@Override
	public xbean.LDVideoRoleRoseInfoList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LDVideoRoleRoseInfoList toBean() {
		_xdb_verify_unsafe_();
		return new LDVideoRoleRoseInfoList(this); // same as copy()
	}

	@Override
	public xbean.LDVideoRoleRoseInfoList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LDVideoRoleRoseInfoList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<String, xbean.LDVideoRoleRoseInfo> getLdvideoroleroseinfolist() { // 点赞记录 key=videoid
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "ldvideoroleroseinfolist"), ldvideoroleroseinfolist);
	}

	@Override
	public java.util.Map<String, xbean.LDVideoRoleRoseInfo> getLdvideoroleroseinfolistAsData() { // 点赞记录 key=videoid
		_xdb_verify_unsafe_();
		java.util.Map<String, xbean.LDVideoRoleRoseInfo> ldvideoroleroseinfolist;
		LDVideoRoleRoseInfoList _o_ = this;
		ldvideoroleroseinfolist = new java.util.HashMap<String, xbean.LDVideoRoleRoseInfo>();
		for (java.util.Map.Entry<String, xbean.LDVideoRoleRoseInfo> _e_ : _o_.ldvideoroleroseinfolist.entrySet())
			ldvideoroleroseinfolist.put(_e_.getKey(), new LDVideoRoleRoseInfo.Data(_e_.getValue()));
		return ldvideoroleroseinfolist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LDVideoRoleRoseInfoList _o_ = null;
		if ( _o1_ instanceof LDVideoRoleRoseInfoList ) _o_ = (LDVideoRoleRoseInfoList)_o1_;
		else if ( _o1_ instanceof LDVideoRoleRoseInfoList.Const ) _o_ = ((LDVideoRoleRoseInfoList.Const)_o1_).nThis();
		else return false;
		if (!ldvideoroleroseinfolist.equals(_o_.ldvideoroleroseinfolist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += ldvideoroleroseinfolist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ldvideoroleroseinfolist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("ldvideoroleroseinfolist"));
		return lb;
	}

	private class Const implements xbean.LDVideoRoleRoseInfoList {
		LDVideoRoleRoseInfoList nThis() {
			return LDVideoRoleRoseInfoList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LDVideoRoleRoseInfoList copy() {
			return LDVideoRoleRoseInfoList.this.copy();
		}

		@Override
		public xbean.LDVideoRoleRoseInfoList toData() {
			return LDVideoRoleRoseInfoList.this.toData();
		}

		public xbean.LDVideoRoleRoseInfoList toBean() {
			return LDVideoRoleRoseInfoList.this.toBean();
		}

		@Override
		public xbean.LDVideoRoleRoseInfoList toDataIf() {
			return LDVideoRoleRoseInfoList.this.toDataIf();
		}

		public xbean.LDVideoRoleRoseInfoList toBeanIf() {
			return LDVideoRoleRoseInfoList.this.toBeanIf();
		}

		@Override
		public java.util.Map<String, xbean.LDVideoRoleRoseInfo> getLdvideoroleroseinfolist() { // 点赞记录 key=videoid
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(ldvideoroleroseinfolist);
		}

		@Override
		public java.util.Map<String, xbean.LDVideoRoleRoseInfo> getLdvideoroleroseinfolistAsData() { // 点赞记录 key=videoid
			_xdb_verify_unsafe_();
			java.util.Map<String, xbean.LDVideoRoleRoseInfo> ldvideoroleroseinfolist;
			LDVideoRoleRoseInfoList _o_ = LDVideoRoleRoseInfoList.this;
			ldvideoroleroseinfolist = new java.util.HashMap<String, xbean.LDVideoRoleRoseInfo>();
			for (java.util.Map.Entry<String, xbean.LDVideoRoleRoseInfo> _e_ : _o_.ldvideoroleroseinfolist.entrySet())
				ldvideoroleroseinfolist.put(_e_.getKey(), new LDVideoRoleRoseInfo.Data(_e_.getValue()));
			return ldvideoroleroseinfolist;
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
			return LDVideoRoleRoseInfoList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LDVideoRoleRoseInfoList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return LDVideoRoleRoseInfoList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LDVideoRoleRoseInfoList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LDVideoRoleRoseInfoList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LDVideoRoleRoseInfoList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LDVideoRoleRoseInfoList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LDVideoRoleRoseInfoList.this.hashCode();
		}

		@Override
		public String toString() {
			return LDVideoRoleRoseInfoList.this.toString();
		}

	}

	public static final class Data implements xbean.LDVideoRoleRoseInfoList {
		private java.util.HashMap<String, xbean.LDVideoRoleRoseInfo> ldvideoroleroseinfolist; // 点赞记录 key=videoid

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			ldvideoroleroseinfolist = new java.util.HashMap<String, xbean.LDVideoRoleRoseInfo>();
		}

		Data(xbean.LDVideoRoleRoseInfoList _o1_) {
			if (_o1_ instanceof LDVideoRoleRoseInfoList) assign((LDVideoRoleRoseInfoList)_o1_);
			else if (_o1_ instanceof LDVideoRoleRoseInfoList.Data) assign((LDVideoRoleRoseInfoList.Data)_o1_);
			else if (_o1_ instanceof LDVideoRoleRoseInfoList.Const) assign(((LDVideoRoleRoseInfoList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LDVideoRoleRoseInfoList _o_) {
			ldvideoroleroseinfolist = new java.util.HashMap<String, xbean.LDVideoRoleRoseInfo>();
			for (java.util.Map.Entry<String, xbean.LDVideoRoleRoseInfo> _e_ : _o_.ldvideoroleroseinfolist.entrySet())
				ldvideoroleroseinfolist.put(_e_.getKey(), new LDVideoRoleRoseInfo.Data(_e_.getValue()));
		}

		private void assign(LDVideoRoleRoseInfoList.Data _o_) {
			ldvideoroleroseinfolist = new java.util.HashMap<String, xbean.LDVideoRoleRoseInfo>();
			for (java.util.Map.Entry<String, xbean.LDVideoRoleRoseInfo> _e_ : _o_.ldvideoroleroseinfolist.entrySet())
				ldvideoroleroseinfolist.put(_e_.getKey(), new LDVideoRoleRoseInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(ldvideoroleroseinfolist.size());
			for (java.util.Map.Entry<String, xbean.LDVideoRoleRoseInfo> _e_ : ldvideoroleroseinfolist.entrySet())
			{
				_os_.marshal(_e_.getKey(), mkdb.Const.IO_CHARSET);
				_e_.getValue().marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					ldvideoroleroseinfolist = new java.util.HashMap<String, xbean.LDVideoRoleRoseInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					String _k_ = "";
					_k_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
					xbean.LDVideoRoleRoseInfo _v_ = xbean.Pod.newLDVideoRoleRoseInfoData();
					_v_.unmarshal(_os_);
					ldvideoroleroseinfolist.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.LDVideoRoleRoseInfoList copy() {
			return new Data(this);
		}

		@Override
		public xbean.LDVideoRoleRoseInfoList toData() {
			return new Data(this);
		}

		public xbean.LDVideoRoleRoseInfoList toBean() {
			return new LDVideoRoleRoseInfoList(this, null, null);
		}

		@Override
		public xbean.LDVideoRoleRoseInfoList toDataIf() {
			return this;
		}

		public xbean.LDVideoRoleRoseInfoList toBeanIf() {
			return new LDVideoRoleRoseInfoList(this, null, null);
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
		public java.util.Map<String, xbean.LDVideoRoleRoseInfo> getLdvideoroleroseinfolist() { // 点赞记录 key=videoid
			return ldvideoroleroseinfolist;
		}

		@Override
		public java.util.Map<String, xbean.LDVideoRoleRoseInfo> getLdvideoroleroseinfolistAsData() { // 点赞记录 key=videoid
			return ldvideoroleroseinfolist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LDVideoRoleRoseInfoList.Data)) return false;
			LDVideoRoleRoseInfoList.Data _o_ = (LDVideoRoleRoseInfoList.Data) _o1_;
			if (!ldvideoroleroseinfolist.equals(_o_.ldvideoroleroseinfolist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += ldvideoroleroseinfolist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(ldvideoroleroseinfolist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
