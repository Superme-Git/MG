
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BattleFlags extends mkdb.XBean implements xbean.BattleFlags {
	private java.util.LinkedList<String> friendflag; // 
	private java.util.LinkedList<String> enemyflag; // 

	@Override
	public void _reset_unsafe_() {
		friendflag.clear();
		enemyflag.clear();
	}

	BattleFlags(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		friendflag = new java.util.LinkedList<String>();
		enemyflag = new java.util.LinkedList<String>();
	}

	public BattleFlags() {
		this(0, null, null);
	}

	public BattleFlags(BattleFlags _o_) {
		this(_o_, null, null);
	}

	BattleFlags(xbean.BattleFlags _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BattleFlags) assign((BattleFlags)_o1_);
		else if (_o1_ instanceof BattleFlags.Data) assign((BattleFlags.Data)_o1_);
		else if (_o1_ instanceof BattleFlags.Const) assign(((BattleFlags.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BattleFlags _o_) {
		_o_._xdb_verify_unsafe_();
		friendflag = new java.util.LinkedList<String>();
		friendflag.addAll(_o_.friendflag);
		enemyflag = new java.util.LinkedList<String>();
		enemyflag.addAll(_o_.enemyflag);
	}

	private void assign(BattleFlags.Data _o_) {
		friendflag = new java.util.LinkedList<String>();
		friendflag.addAll(_o_.friendflag);
		enemyflag = new java.util.LinkedList<String>();
		enemyflag.addAll(_o_.enemyflag);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.compact_uint32(friendflag.size());
		for (String _v_ : friendflag) {
			_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
		}
		_os_.compact_uint32(enemyflag.size());
		for (String _v_ : enemyflag) {
			_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			String _v_ = "";
			_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			friendflag.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			String _v_ = "";
			_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			enemyflag.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.BattleFlags copy() {
		_xdb_verify_unsafe_();
		return new BattleFlags(this);
	}

	@Override
	public xbean.BattleFlags toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BattleFlags toBean() {
		_xdb_verify_unsafe_();
		return new BattleFlags(this); // same as copy()
	}

	@Override
	public xbean.BattleFlags toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BattleFlags toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public java.util.List<String> getFriendflag() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "friendflag"), friendflag);
	}

	public java.util.List<String> getFriendflagAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<String> friendflag;
		BattleFlags _o_ = this;
		friendflag = new java.util.LinkedList<String>();
		friendflag.addAll(_o_.friendflag);
		return friendflag;
	}

	@Override
	public java.util.List<String> getEnemyflag() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "enemyflag"), enemyflag);
	}

	public java.util.List<String> getEnemyflagAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<String> enemyflag;
		BattleFlags _o_ = this;
		enemyflag = new java.util.LinkedList<String>();
		enemyflag.addAll(_o_.enemyflag);
		return enemyflag;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BattleFlags _o_ = null;
		if ( _o1_ instanceof BattleFlags ) _o_ = (BattleFlags)_o1_;
		else if ( _o1_ instanceof BattleFlags.Const ) _o_ = ((BattleFlags.Const)_o1_).nThis();
		else return false;
		if (!friendflag.equals(_o_.friendflag)) return false;
		if (!enemyflag.equals(_o_.enemyflag)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += friendflag.hashCode();
		_h_ += enemyflag.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(friendflag);
		_sb_.append(",");
		_sb_.append(enemyflag);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("friendflag"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("enemyflag"));
		return lb;
	}

	private class Const implements xbean.BattleFlags {
		BattleFlags nThis() {
			return BattleFlags.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BattleFlags copy() {
			return BattleFlags.this.copy();
		}

		@Override
		public xbean.BattleFlags toData() {
			return BattleFlags.this.toData();
		}

		public xbean.BattleFlags toBean() {
			return BattleFlags.this.toBean();
		}

		@Override
		public xbean.BattleFlags toDataIf() {
			return BattleFlags.this.toDataIf();
		}

		public xbean.BattleFlags toBeanIf() {
			return BattleFlags.this.toBeanIf();
		}

		@Override
		public java.util.List<String> getFriendflag() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(friendflag);
		}

		public java.util.List<String> getFriendflagAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<String> friendflag;
			BattleFlags _o_ = BattleFlags.this;
		friendflag = new java.util.LinkedList<String>();
		friendflag.addAll(_o_.friendflag);
			return friendflag;
		}

		@Override
		public java.util.List<String> getEnemyflag() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(enemyflag);
		}

		public java.util.List<String> getEnemyflagAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<String> enemyflag;
			BattleFlags _o_ = BattleFlags.this;
		enemyflag = new java.util.LinkedList<String>();
		enemyflag.addAll(_o_.enemyflag);
			return enemyflag;
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
			return BattleFlags.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BattleFlags.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BattleFlags.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BattleFlags.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BattleFlags.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BattleFlags.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BattleFlags.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BattleFlags.this.hashCode();
		}

		@Override
		public String toString() {
			return BattleFlags.this.toString();
		}

	}

	public static final class Data implements xbean.BattleFlags {
		private java.util.LinkedList<String> friendflag; // 
		private java.util.LinkedList<String> enemyflag; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			friendflag = new java.util.LinkedList<String>();
			enemyflag = new java.util.LinkedList<String>();
		}

		Data(xbean.BattleFlags _o1_) {
			if (_o1_ instanceof BattleFlags) assign((BattleFlags)_o1_);
			else if (_o1_ instanceof BattleFlags.Data) assign((BattleFlags.Data)_o1_);
			else if (_o1_ instanceof BattleFlags.Const) assign(((BattleFlags.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BattleFlags _o_) {
			friendflag = new java.util.LinkedList<String>();
			friendflag.addAll(_o_.friendflag);
			enemyflag = new java.util.LinkedList<String>();
			enemyflag.addAll(_o_.enemyflag);
		}

		private void assign(BattleFlags.Data _o_) {
			friendflag = new java.util.LinkedList<String>();
			friendflag.addAll(_o_.friendflag);
			enemyflag = new java.util.LinkedList<String>();
			enemyflag.addAll(_o_.enemyflag);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.compact_uint32(friendflag.size());
			for (String _v_ : friendflag) {
				_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
			}
			_os_.compact_uint32(enemyflag.size());
			for (String _v_ : enemyflag) {
				_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				String _v_ = "";
				_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				friendflag.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				String _v_ = "";
				_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				enemyflag.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.BattleFlags copy() {
			return new Data(this);
		}

		@Override
		public xbean.BattleFlags toData() {
			return new Data(this);
		}

		public xbean.BattleFlags toBean() {
			return new BattleFlags(this, null, null);
		}

		@Override
		public xbean.BattleFlags toDataIf() {
			return this;
		}

		public xbean.BattleFlags toBeanIf() {
			return new BattleFlags(this, null, null);
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
		public java.util.List<String> getFriendflag() { // 
			return friendflag;
		}

		@Override
		public java.util.List<String> getFriendflagAsData() { // 
			return friendflag;
		}

		@Override
		public java.util.List<String> getEnemyflag() { // 
			return enemyflag;
		}

		@Override
		public java.util.List<String> getEnemyflagAsData() { // 
			return enemyflag;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BattleFlags.Data)) return false;
			BattleFlags.Data _o_ = (BattleFlags.Data) _o1_;
			if (!friendflag.equals(_o_.friendflag)) return false;
			if (!enemyflag.equals(_o_.enemyflag)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += friendflag.hashCode();
			_h_ += enemyflag.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(friendflag);
			_sb_.append(",");
			_sb_.append(enemyflag);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
