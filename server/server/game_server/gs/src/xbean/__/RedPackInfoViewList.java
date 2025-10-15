
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RedPackInfoViewList extends mkdb.XBean implements xbean.RedPackInfoViewList {
	private java.util.HashMap<String, xbean.RedPackBaseInfo> redpackinfoviewlist; // 红包记录 key=redpackid

	@Override
	public void _reset_unsafe_() {
		redpackinfoviewlist.clear();
	}

	RedPackInfoViewList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		redpackinfoviewlist = new java.util.HashMap<String, xbean.RedPackBaseInfo>();
	}

	public RedPackInfoViewList() {
		this(0, null, null);
	}

	public RedPackInfoViewList(RedPackInfoViewList _o_) {
		this(_o_, null, null);
	}

	RedPackInfoViewList(xbean.RedPackInfoViewList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RedPackInfoViewList) assign((RedPackInfoViewList)_o1_);
		else if (_o1_ instanceof RedPackInfoViewList.Data) assign((RedPackInfoViewList.Data)_o1_);
		else if (_o1_ instanceof RedPackInfoViewList.Const) assign(((RedPackInfoViewList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RedPackInfoViewList _o_) {
		_o_._xdb_verify_unsafe_();
		redpackinfoviewlist = new java.util.HashMap<String, xbean.RedPackBaseInfo>();
		for (java.util.Map.Entry<String, xbean.RedPackBaseInfo> _e_ : _o_.redpackinfoviewlist.entrySet())
			redpackinfoviewlist.put(_e_.getKey(), new RedPackBaseInfo(_e_.getValue(), this, "redpackinfoviewlist"));
	}

	private void assign(RedPackInfoViewList.Data _o_) {
		redpackinfoviewlist = new java.util.HashMap<String, xbean.RedPackBaseInfo>();
		for (java.util.Map.Entry<String, xbean.RedPackBaseInfo> _e_ : _o_.redpackinfoviewlist.entrySet())
			redpackinfoviewlist.put(_e_.getKey(), new RedPackBaseInfo(_e_.getValue(), this, "redpackinfoviewlist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(redpackinfoviewlist.size());
		for (java.util.Map.Entry<String, xbean.RedPackBaseInfo> _e_ : redpackinfoviewlist.entrySet())
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
				redpackinfoviewlist = new java.util.HashMap<String, xbean.RedPackBaseInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				String _k_ = "";
				_k_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				xbean.RedPackBaseInfo _v_ = new RedPackBaseInfo(0, this, "redpackinfoviewlist");
				_v_.unmarshal(_os_);
				redpackinfoviewlist.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RedPackInfoViewList copy() {
		_xdb_verify_unsafe_();
		return new RedPackInfoViewList(this);
	}

	@Override
	public xbean.RedPackInfoViewList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPackInfoViewList toBean() {
		_xdb_verify_unsafe_();
		return new RedPackInfoViewList(this); // same as copy()
	}

	@Override
	public xbean.RedPackInfoViewList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPackInfoViewList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<String, xbean.RedPackBaseInfo> getRedpackinfoviewlist() { // 红包记录 key=redpackid
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "redpackinfoviewlist"), redpackinfoviewlist);
	}

	@Override
	public java.util.Map<String, xbean.RedPackBaseInfo> getRedpackinfoviewlistAsData() { // 红包记录 key=redpackid
		_xdb_verify_unsafe_();
		java.util.Map<String, xbean.RedPackBaseInfo> redpackinfoviewlist;
		RedPackInfoViewList _o_ = this;
		redpackinfoviewlist = new java.util.HashMap<String, xbean.RedPackBaseInfo>();
		for (java.util.Map.Entry<String, xbean.RedPackBaseInfo> _e_ : _o_.redpackinfoviewlist.entrySet())
			redpackinfoviewlist.put(_e_.getKey(), new RedPackBaseInfo.Data(_e_.getValue()));
		return redpackinfoviewlist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RedPackInfoViewList _o_ = null;
		if ( _o1_ instanceof RedPackInfoViewList ) _o_ = (RedPackInfoViewList)_o1_;
		else if ( _o1_ instanceof RedPackInfoViewList.Const ) _o_ = ((RedPackInfoViewList.Const)_o1_).nThis();
		else return false;
		if (!redpackinfoviewlist.equals(_o_.redpackinfoviewlist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += redpackinfoviewlist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(redpackinfoviewlist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("redpackinfoviewlist"));
		return lb;
	}

	private class Const implements xbean.RedPackInfoViewList {
		RedPackInfoViewList nThis() {
			return RedPackInfoViewList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RedPackInfoViewList copy() {
			return RedPackInfoViewList.this.copy();
		}

		@Override
		public xbean.RedPackInfoViewList toData() {
			return RedPackInfoViewList.this.toData();
		}

		public xbean.RedPackInfoViewList toBean() {
			return RedPackInfoViewList.this.toBean();
		}

		@Override
		public xbean.RedPackInfoViewList toDataIf() {
			return RedPackInfoViewList.this.toDataIf();
		}

		public xbean.RedPackInfoViewList toBeanIf() {
			return RedPackInfoViewList.this.toBeanIf();
		}

		@Override
		public java.util.Map<String, xbean.RedPackBaseInfo> getRedpackinfoviewlist() { // 红包记录 key=redpackid
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(redpackinfoviewlist);
		}

		@Override
		public java.util.Map<String, xbean.RedPackBaseInfo> getRedpackinfoviewlistAsData() { // 红包记录 key=redpackid
			_xdb_verify_unsafe_();
			java.util.Map<String, xbean.RedPackBaseInfo> redpackinfoviewlist;
			RedPackInfoViewList _o_ = RedPackInfoViewList.this;
			redpackinfoviewlist = new java.util.HashMap<String, xbean.RedPackBaseInfo>();
			for (java.util.Map.Entry<String, xbean.RedPackBaseInfo> _e_ : _o_.redpackinfoviewlist.entrySet())
				redpackinfoviewlist.put(_e_.getKey(), new RedPackBaseInfo.Data(_e_.getValue()));
			return redpackinfoviewlist;
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
			return RedPackInfoViewList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RedPackInfoViewList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RedPackInfoViewList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RedPackInfoViewList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RedPackInfoViewList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RedPackInfoViewList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RedPackInfoViewList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RedPackInfoViewList.this.hashCode();
		}

		@Override
		public String toString() {
			return RedPackInfoViewList.this.toString();
		}

	}

	public static final class Data implements xbean.RedPackInfoViewList {
		private java.util.HashMap<String, xbean.RedPackBaseInfo> redpackinfoviewlist; // 红包记录 key=redpackid

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			redpackinfoviewlist = new java.util.HashMap<String, xbean.RedPackBaseInfo>();
		}

		Data(xbean.RedPackInfoViewList _o1_) {
			if (_o1_ instanceof RedPackInfoViewList) assign((RedPackInfoViewList)_o1_);
			else if (_o1_ instanceof RedPackInfoViewList.Data) assign((RedPackInfoViewList.Data)_o1_);
			else if (_o1_ instanceof RedPackInfoViewList.Const) assign(((RedPackInfoViewList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RedPackInfoViewList _o_) {
			redpackinfoviewlist = new java.util.HashMap<String, xbean.RedPackBaseInfo>();
			for (java.util.Map.Entry<String, xbean.RedPackBaseInfo> _e_ : _o_.redpackinfoviewlist.entrySet())
				redpackinfoviewlist.put(_e_.getKey(), new RedPackBaseInfo.Data(_e_.getValue()));
		}

		private void assign(RedPackInfoViewList.Data _o_) {
			redpackinfoviewlist = new java.util.HashMap<String, xbean.RedPackBaseInfo>();
			for (java.util.Map.Entry<String, xbean.RedPackBaseInfo> _e_ : _o_.redpackinfoviewlist.entrySet())
				redpackinfoviewlist.put(_e_.getKey(), new RedPackBaseInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(redpackinfoviewlist.size());
			for (java.util.Map.Entry<String, xbean.RedPackBaseInfo> _e_ : redpackinfoviewlist.entrySet())
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
					redpackinfoviewlist = new java.util.HashMap<String, xbean.RedPackBaseInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					String _k_ = "";
					_k_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
					xbean.RedPackBaseInfo _v_ = xbean.Pod.newRedPackBaseInfoData();
					_v_.unmarshal(_os_);
					redpackinfoviewlist.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RedPackInfoViewList copy() {
			return new Data(this);
		}

		@Override
		public xbean.RedPackInfoViewList toData() {
			return new Data(this);
		}

		public xbean.RedPackInfoViewList toBean() {
			return new RedPackInfoViewList(this, null, null);
		}

		@Override
		public xbean.RedPackInfoViewList toDataIf() {
			return this;
		}

		public xbean.RedPackInfoViewList toBeanIf() {
			return new RedPackInfoViewList(this, null, null);
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
		public java.util.Map<String, xbean.RedPackBaseInfo> getRedpackinfoviewlist() { // 红包记录 key=redpackid
			return redpackinfoviewlist;
		}

		@Override
		public java.util.Map<String, xbean.RedPackBaseInfo> getRedpackinfoviewlistAsData() { // 红包记录 key=redpackid
			return redpackinfoviewlist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RedPackInfoViewList.Data)) return false;
			RedPackInfoViewList.Data _o_ = (RedPackInfoViewList.Data) _o1_;
			if (!redpackinfoviewlist.equals(_o_.redpackinfoviewlist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += redpackinfoviewlist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(redpackinfoviewlist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
