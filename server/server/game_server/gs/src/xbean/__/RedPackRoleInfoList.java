
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RedPackRoleInfoList extends mkdb.XBean implements xbean.RedPackRoleInfoList {
	private java.util.HashMap<String, xbean.RedPackInfo> redpackinfolist; // 红包记录 key=redpackid

	@Override
	public void _reset_unsafe_() {
		redpackinfolist.clear();
	}

	RedPackRoleInfoList(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		redpackinfolist = new java.util.HashMap<String, xbean.RedPackInfo>();
	}

	public RedPackRoleInfoList() {
		this(0, null, null);
	}

	public RedPackRoleInfoList(RedPackRoleInfoList _o_) {
		this(_o_, null, null);
	}

	RedPackRoleInfoList(xbean.RedPackRoleInfoList _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RedPackRoleInfoList) assign((RedPackRoleInfoList)_o1_);
		else if (_o1_ instanceof RedPackRoleInfoList.Data) assign((RedPackRoleInfoList.Data)_o1_);
		else if (_o1_ instanceof RedPackRoleInfoList.Const) assign(((RedPackRoleInfoList.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RedPackRoleInfoList _o_) {
		_o_._xdb_verify_unsafe_();
		redpackinfolist = new java.util.HashMap<String, xbean.RedPackInfo>();
		for (java.util.Map.Entry<String, xbean.RedPackInfo> _e_ : _o_.redpackinfolist.entrySet())
			redpackinfolist.put(_e_.getKey(), new RedPackInfo(_e_.getValue(), this, "redpackinfolist"));
	}

	private void assign(RedPackRoleInfoList.Data _o_) {
		redpackinfolist = new java.util.HashMap<String, xbean.RedPackInfo>();
		for (java.util.Map.Entry<String, xbean.RedPackInfo> _e_ : _o_.redpackinfolist.entrySet())
			redpackinfolist.put(_e_.getKey(), new RedPackInfo(_e_.getValue(), this, "redpackinfolist"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(redpackinfolist.size());
		for (java.util.Map.Entry<String, xbean.RedPackInfo> _e_ : redpackinfolist.entrySet())
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
				redpackinfolist = new java.util.HashMap<String, xbean.RedPackInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				String _k_ = "";
				_k_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				xbean.RedPackInfo _v_ = new RedPackInfo(0, this, "redpackinfolist");
				_v_.unmarshal(_os_);
				redpackinfolist.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.RedPackRoleInfoList copy() {
		_xdb_verify_unsafe_();
		return new RedPackRoleInfoList(this);
	}

	@Override
	public xbean.RedPackRoleInfoList toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPackRoleInfoList toBean() {
		_xdb_verify_unsafe_();
		return new RedPackRoleInfoList(this); // same as copy()
	}

	@Override
	public xbean.RedPackRoleInfoList toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RedPackRoleInfoList toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.Map<String, xbean.RedPackInfo> getRedpackinfolist() { // 红包记录 key=redpackid
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "redpackinfolist"), redpackinfolist);
	}

	@Override
	public java.util.Map<String, xbean.RedPackInfo> getRedpackinfolistAsData() { // 红包记录 key=redpackid
		_xdb_verify_unsafe_();
		java.util.Map<String, xbean.RedPackInfo> redpackinfolist;
		RedPackRoleInfoList _o_ = this;
		redpackinfolist = new java.util.HashMap<String, xbean.RedPackInfo>();
		for (java.util.Map.Entry<String, xbean.RedPackInfo> _e_ : _o_.redpackinfolist.entrySet())
			redpackinfolist.put(_e_.getKey(), new RedPackInfo.Data(_e_.getValue()));
		return redpackinfolist;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RedPackRoleInfoList _o_ = null;
		if ( _o1_ instanceof RedPackRoleInfoList ) _o_ = (RedPackRoleInfoList)_o1_;
		else if ( _o1_ instanceof RedPackRoleInfoList.Const ) _o_ = ((RedPackRoleInfoList.Const)_o1_).nThis();
		else return false;
		if (!redpackinfolist.equals(_o_.redpackinfolist)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += redpackinfolist.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(redpackinfolist);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableMap().setVarName("redpackinfolist"));
		return lb;
	}

	private class Const implements xbean.RedPackRoleInfoList {
		RedPackRoleInfoList nThis() {
			return RedPackRoleInfoList.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RedPackRoleInfoList copy() {
			return RedPackRoleInfoList.this.copy();
		}

		@Override
		public xbean.RedPackRoleInfoList toData() {
			return RedPackRoleInfoList.this.toData();
		}

		public xbean.RedPackRoleInfoList toBean() {
			return RedPackRoleInfoList.this.toBean();
		}

		@Override
		public xbean.RedPackRoleInfoList toDataIf() {
			return RedPackRoleInfoList.this.toDataIf();
		}

		public xbean.RedPackRoleInfoList toBeanIf() {
			return RedPackRoleInfoList.this.toBeanIf();
		}

		@Override
		public java.util.Map<String, xbean.RedPackInfo> getRedpackinfolist() { // 红包记录 key=redpackid
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(redpackinfolist);
		}

		@Override
		public java.util.Map<String, xbean.RedPackInfo> getRedpackinfolistAsData() { // 红包记录 key=redpackid
			_xdb_verify_unsafe_();
			java.util.Map<String, xbean.RedPackInfo> redpackinfolist;
			RedPackRoleInfoList _o_ = RedPackRoleInfoList.this;
			redpackinfolist = new java.util.HashMap<String, xbean.RedPackInfo>();
			for (java.util.Map.Entry<String, xbean.RedPackInfo> _e_ : _o_.redpackinfolist.entrySet())
				redpackinfolist.put(_e_.getKey(), new RedPackInfo.Data(_e_.getValue()));
			return redpackinfolist;
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
			return RedPackRoleInfoList.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RedPackRoleInfoList.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RedPackRoleInfoList.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RedPackRoleInfoList.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RedPackRoleInfoList.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RedPackRoleInfoList.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RedPackRoleInfoList.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RedPackRoleInfoList.this.hashCode();
		}

		@Override
		public String toString() {
			return RedPackRoleInfoList.this.toString();
		}

	}

	public static final class Data implements xbean.RedPackRoleInfoList {
		private java.util.HashMap<String, xbean.RedPackInfo> redpackinfolist; // 红包记录 key=redpackid

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			redpackinfolist = new java.util.HashMap<String, xbean.RedPackInfo>();
		}

		Data(xbean.RedPackRoleInfoList _o1_) {
			if (_o1_ instanceof RedPackRoleInfoList) assign((RedPackRoleInfoList)_o1_);
			else if (_o1_ instanceof RedPackRoleInfoList.Data) assign((RedPackRoleInfoList.Data)_o1_);
			else if (_o1_ instanceof RedPackRoleInfoList.Const) assign(((RedPackRoleInfoList.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RedPackRoleInfoList _o_) {
			redpackinfolist = new java.util.HashMap<String, xbean.RedPackInfo>();
			for (java.util.Map.Entry<String, xbean.RedPackInfo> _e_ : _o_.redpackinfolist.entrySet())
				redpackinfolist.put(_e_.getKey(), new RedPackInfo.Data(_e_.getValue()));
		}

		private void assign(RedPackRoleInfoList.Data _o_) {
			redpackinfolist = new java.util.HashMap<String, xbean.RedPackInfo>();
			for (java.util.Map.Entry<String, xbean.RedPackInfo> _e_ : _o_.redpackinfolist.entrySet())
				redpackinfolist.put(_e_.getKey(), new RedPackInfo.Data(_e_.getValue()));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(redpackinfolist.size());
			for (java.util.Map.Entry<String, xbean.RedPackInfo> _e_ : redpackinfolist.entrySet())
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
					redpackinfolist = new java.util.HashMap<String, xbean.RedPackInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					String _k_ = "";
					_k_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
					xbean.RedPackInfo _v_ = xbean.Pod.newRedPackInfoData();
					_v_.unmarshal(_os_);
					redpackinfolist.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.RedPackRoleInfoList copy() {
			return new Data(this);
		}

		@Override
		public xbean.RedPackRoleInfoList toData() {
			return new Data(this);
		}

		public xbean.RedPackRoleInfoList toBean() {
			return new RedPackRoleInfoList(this, null, null);
		}

		@Override
		public xbean.RedPackRoleInfoList toDataIf() {
			return this;
		}

		public xbean.RedPackRoleInfoList toBeanIf() {
			return new RedPackRoleInfoList(this, null, null);
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
		public java.util.Map<String, xbean.RedPackInfo> getRedpackinfolist() { // 红包记录 key=redpackid
			return redpackinfolist;
		}

		@Override
		public java.util.Map<String, xbean.RedPackInfo> getRedpackinfolistAsData() { // 红包记录 key=redpackid
			return redpackinfolist;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RedPackRoleInfoList.Data)) return false;
			RedPackRoleInfoList.Data _o_ = (RedPackRoleInfoList.Data) _o1_;
			if (!redpackinfolist.equals(_o_.redpackinfolist)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += redpackinfolist.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(redpackinfolist);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
