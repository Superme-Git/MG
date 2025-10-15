
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class helpcount extends mkdb.XBean implements xbean.helpcount {
	private long expvalue; // 经验(援助战斗次数)
	private int shengwangvalue; // 声望
	private int clanvalue; // 帮贡
	private int clanhishelpnum; // 公会历史援助次数
	private int helpgiveitemnum; // 援助物品次数
	private int helpitemnum; // 求助物品次数

	@Override
	public void _reset_unsafe_() {
		expvalue = 0L;
		shengwangvalue = 0;
		clanvalue = 0;
		clanhishelpnum = 0;
		helpgiveitemnum = 0;
		helpitemnum = 0;
	}

	helpcount(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public helpcount() {
		this(0, null, null);
	}

	public helpcount(helpcount _o_) {
		this(_o_, null, null);
	}

	helpcount(xbean.helpcount _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof helpcount) assign((helpcount)_o1_);
		else if (_o1_ instanceof helpcount.Data) assign((helpcount.Data)_o1_);
		else if (_o1_ instanceof helpcount.Const) assign(((helpcount.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(helpcount _o_) {
		_o_._xdb_verify_unsafe_();
		expvalue = _o_.expvalue;
		shengwangvalue = _o_.shengwangvalue;
		clanvalue = _o_.clanvalue;
		clanhishelpnum = _o_.clanhishelpnum;
		helpgiveitemnum = _o_.helpgiveitemnum;
		helpitemnum = _o_.helpitemnum;
	}

	private void assign(helpcount.Data _o_) {
		expvalue = _o_.expvalue;
		shengwangvalue = _o_.shengwangvalue;
		clanvalue = _o_.clanvalue;
		clanhishelpnum = _o_.clanhishelpnum;
		helpgiveitemnum = _o_.helpgiveitemnum;
		helpitemnum = _o_.helpitemnum;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(expvalue);
		_os_.marshal(shengwangvalue);
		_os_.marshal(clanvalue);
		_os_.marshal(clanhishelpnum);
		_os_.marshal(helpgiveitemnum);
		_os_.marshal(helpitemnum);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		expvalue = _os_.unmarshal_long();
		shengwangvalue = _os_.unmarshal_int();
		clanvalue = _os_.unmarshal_int();
		clanhishelpnum = _os_.unmarshal_int();
		helpgiveitemnum = _os_.unmarshal_int();
		helpitemnum = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.helpcount copy() {
		_xdb_verify_unsafe_();
		return new helpcount(this);
	}

	@Override
	public xbean.helpcount toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.helpcount toBean() {
		_xdb_verify_unsafe_();
		return new helpcount(this); // same as copy()
	}

	@Override
	public xbean.helpcount toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.helpcount toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getExpvalue() { // 经验(援助战斗次数)
		_xdb_verify_unsafe_();
		return expvalue;
	}

	@Override
	public int getShengwangvalue() { // 声望
		_xdb_verify_unsafe_();
		return shengwangvalue;
	}

	@Override
	public int getClanvalue() { // 帮贡
		_xdb_verify_unsafe_();
		return clanvalue;
	}

	@Override
	public int getClanhishelpnum() { // 公会历史援助次数
		_xdb_verify_unsafe_();
		return clanhishelpnum;
	}

	@Override
	public int getHelpgiveitemnum() { // 援助物品次数
		_xdb_verify_unsafe_();
		return helpgiveitemnum;
	}

	@Override
	public int getHelpitemnum() { // 求助物品次数
		_xdb_verify_unsafe_();
		return helpitemnum;
	}

	@Override
	public void setExpvalue(long _v_) { // 经验(援助战斗次数)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "expvalue") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, expvalue) {
					public void rollback() { expvalue = _xdb_saved; }
				};}});
		expvalue = _v_;
	}

	@Override
	public void setShengwangvalue(int _v_) { // 声望
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "shengwangvalue") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, shengwangvalue) {
					public void rollback() { shengwangvalue = _xdb_saved; }
				};}});
		shengwangvalue = _v_;
	}

	@Override
	public void setClanvalue(int _v_) { // 帮贡
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanvalue") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanvalue) {
					public void rollback() { clanvalue = _xdb_saved; }
				};}});
		clanvalue = _v_;
	}

	@Override
	public void setClanhishelpnum(int _v_) { // 公会历史援助次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanhishelpnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanhishelpnum) {
					public void rollback() { clanhishelpnum = _xdb_saved; }
				};}});
		clanhishelpnum = _v_;
	}

	@Override
	public void setHelpgiveitemnum(int _v_) { // 援助物品次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "helpgiveitemnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, helpgiveitemnum) {
					public void rollback() { helpgiveitemnum = _xdb_saved; }
				};}});
		helpgiveitemnum = _v_;
	}

	@Override
	public void setHelpitemnum(int _v_) { // 求助物品次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "helpitemnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, helpitemnum) {
					public void rollback() { helpitemnum = _xdb_saved; }
				};}});
		helpitemnum = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		helpcount _o_ = null;
		if ( _o1_ instanceof helpcount ) _o_ = (helpcount)_o1_;
		else if ( _o1_ instanceof helpcount.Const ) _o_ = ((helpcount.Const)_o1_).nThis();
		else return false;
		if (expvalue != _o_.expvalue) return false;
		if (shengwangvalue != _o_.shengwangvalue) return false;
		if (clanvalue != _o_.clanvalue) return false;
		if (clanhishelpnum != _o_.clanhishelpnum) return false;
		if (helpgiveitemnum != _o_.helpgiveitemnum) return false;
		if (helpitemnum != _o_.helpitemnum) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += expvalue;
		_h_ += shengwangvalue;
		_h_ += clanvalue;
		_h_ += clanhishelpnum;
		_h_ += helpgiveitemnum;
		_h_ += helpitemnum;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(expvalue);
		_sb_.append(",");
		_sb_.append(shengwangvalue);
		_sb_.append(",");
		_sb_.append(clanvalue);
		_sb_.append(",");
		_sb_.append(clanhishelpnum);
		_sb_.append(",");
		_sb_.append(helpgiveitemnum);
		_sb_.append(",");
		_sb_.append(helpitemnum);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("expvalue"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shengwangvalue"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanvalue"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanhishelpnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("helpgiveitemnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("helpitemnum"));
		return lb;
	}

	private class Const implements xbean.helpcount {
		helpcount nThis() {
			return helpcount.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.helpcount copy() {
			return helpcount.this.copy();
		}

		@Override
		public xbean.helpcount toData() {
			return helpcount.this.toData();
		}

		public xbean.helpcount toBean() {
			return helpcount.this.toBean();
		}

		@Override
		public xbean.helpcount toDataIf() {
			return helpcount.this.toDataIf();
		}

		public xbean.helpcount toBeanIf() {
			return helpcount.this.toBeanIf();
		}

		@Override
		public long getExpvalue() { // 经验(援助战斗次数)
			_xdb_verify_unsafe_();
			return expvalue;
		}

		@Override
		public int getShengwangvalue() { // 声望
			_xdb_verify_unsafe_();
			return shengwangvalue;
		}

		@Override
		public int getClanvalue() { // 帮贡
			_xdb_verify_unsafe_();
			return clanvalue;
		}

		@Override
		public int getClanhishelpnum() { // 公会历史援助次数
			_xdb_verify_unsafe_();
			return clanhishelpnum;
		}

		@Override
		public int getHelpgiveitemnum() { // 援助物品次数
			_xdb_verify_unsafe_();
			return helpgiveitemnum;
		}

		@Override
		public int getHelpitemnum() { // 求助物品次数
			_xdb_verify_unsafe_();
			return helpitemnum;
		}

		@Override
		public void setExpvalue(long _v_) { // 经验(援助战斗次数)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShengwangvalue(int _v_) { // 声望
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanvalue(int _v_) { // 帮贡
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanhishelpnum(int _v_) { // 公会历史援助次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHelpgiveitemnum(int _v_) { // 援助物品次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHelpitemnum(int _v_) { // 求助物品次数
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
			return helpcount.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return helpcount.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return helpcount.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return helpcount.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return helpcount.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return helpcount.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return helpcount.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return helpcount.this.hashCode();
		}

		@Override
		public String toString() {
			return helpcount.this.toString();
		}

	}

	public static final class Data implements xbean.helpcount {
		private long expvalue; // 经验(援助战斗次数)
		private int shengwangvalue; // 声望
		private int clanvalue; // 帮贡
		private int clanhishelpnum; // 公会历史援助次数
		private int helpgiveitemnum; // 援助物品次数
		private int helpitemnum; // 求助物品次数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.helpcount _o1_) {
			if (_o1_ instanceof helpcount) assign((helpcount)_o1_);
			else if (_o1_ instanceof helpcount.Data) assign((helpcount.Data)_o1_);
			else if (_o1_ instanceof helpcount.Const) assign(((helpcount.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(helpcount _o_) {
			expvalue = _o_.expvalue;
			shengwangvalue = _o_.shengwangvalue;
			clanvalue = _o_.clanvalue;
			clanhishelpnum = _o_.clanhishelpnum;
			helpgiveitemnum = _o_.helpgiveitemnum;
			helpitemnum = _o_.helpitemnum;
		}

		private void assign(helpcount.Data _o_) {
			expvalue = _o_.expvalue;
			shengwangvalue = _o_.shengwangvalue;
			clanvalue = _o_.clanvalue;
			clanhishelpnum = _o_.clanhishelpnum;
			helpgiveitemnum = _o_.helpgiveitemnum;
			helpitemnum = _o_.helpitemnum;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(expvalue);
			_os_.marshal(shengwangvalue);
			_os_.marshal(clanvalue);
			_os_.marshal(clanhishelpnum);
			_os_.marshal(helpgiveitemnum);
			_os_.marshal(helpitemnum);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			expvalue = _os_.unmarshal_long();
			shengwangvalue = _os_.unmarshal_int();
			clanvalue = _os_.unmarshal_int();
			clanhishelpnum = _os_.unmarshal_int();
			helpgiveitemnum = _os_.unmarshal_int();
			helpitemnum = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.helpcount copy() {
			return new Data(this);
		}

		@Override
		public xbean.helpcount toData() {
			return new Data(this);
		}

		public xbean.helpcount toBean() {
			return new helpcount(this, null, null);
		}

		@Override
		public xbean.helpcount toDataIf() {
			return this;
		}

		public xbean.helpcount toBeanIf() {
			return new helpcount(this, null, null);
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
		public long getExpvalue() { // 经验(援助战斗次数)
			return expvalue;
		}

		@Override
		public int getShengwangvalue() { // 声望
			return shengwangvalue;
		}

		@Override
		public int getClanvalue() { // 帮贡
			return clanvalue;
		}

		@Override
		public int getClanhishelpnum() { // 公会历史援助次数
			return clanhishelpnum;
		}

		@Override
		public int getHelpgiveitemnum() { // 援助物品次数
			return helpgiveitemnum;
		}

		@Override
		public int getHelpitemnum() { // 求助物品次数
			return helpitemnum;
		}

		@Override
		public void setExpvalue(long _v_) { // 经验(援助战斗次数)
			expvalue = _v_;
		}

		@Override
		public void setShengwangvalue(int _v_) { // 声望
			shengwangvalue = _v_;
		}

		@Override
		public void setClanvalue(int _v_) { // 帮贡
			clanvalue = _v_;
		}

		@Override
		public void setClanhishelpnum(int _v_) { // 公会历史援助次数
			clanhishelpnum = _v_;
		}

		@Override
		public void setHelpgiveitemnum(int _v_) { // 援助物品次数
			helpgiveitemnum = _v_;
		}

		@Override
		public void setHelpitemnum(int _v_) { // 求助物品次数
			helpitemnum = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof helpcount.Data)) return false;
			helpcount.Data _o_ = (helpcount.Data) _o1_;
			if (expvalue != _o_.expvalue) return false;
			if (shengwangvalue != _o_.shengwangvalue) return false;
			if (clanvalue != _o_.clanvalue) return false;
			if (clanhishelpnum != _o_.clanhishelpnum) return false;
			if (helpgiveitemnum != _o_.helpgiveitemnum) return false;
			if (helpitemnum != _o_.helpitemnum) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += expvalue;
			_h_ += shengwangvalue;
			_h_ += clanvalue;
			_h_ += clanhishelpnum;
			_h_ += helpgiveitemnum;
			_h_ += helpitemnum;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(expvalue);
			_sb_.append(",");
			_sb_.append(shengwangvalue);
			_sb_.append(",");
			_sb_.append(clanvalue);
			_sb_.append(",");
			_sb_.append(clanhishelpnum);
			_sb_.append(",");
			_sb_.append(helpgiveitemnum);
			_sb_.append(",");
			_sb_.append(helpitemnum);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
