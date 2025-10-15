
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanBossInfo extends mkdb.XBean implements xbean.ClanBossInfo {
	private int bossmonsterid; // 
	private long maxhp; // 
	private long hp; // 
	private int fighteindx; // 
	private java.util.LinkedList<Long> winroleids; // 最后一击的角色
	private long bossnpckey; // 

	@Override
	public void _reset_unsafe_() {
		bossmonsterid = 0;
		maxhp = 0L;
		hp = 0L;
		fighteindx = 0;
		winroleids.clear();
		bossnpckey = 0L;
	}

	ClanBossInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		winroleids = new java.util.LinkedList<Long>();
	}

	public ClanBossInfo() {
		this(0, null, null);
	}

	public ClanBossInfo(ClanBossInfo _o_) {
		this(_o_, null, null);
	}

	ClanBossInfo(xbean.ClanBossInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanBossInfo) assign((ClanBossInfo)_o1_);
		else if (_o1_ instanceof ClanBossInfo.Data) assign((ClanBossInfo.Data)_o1_);
		else if (_o1_ instanceof ClanBossInfo.Const) assign(((ClanBossInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanBossInfo _o_) {
		_o_._xdb_verify_unsafe_();
		bossmonsterid = _o_.bossmonsterid;
		maxhp = _o_.maxhp;
		hp = _o_.hp;
		fighteindx = _o_.fighteindx;
		winroleids = new java.util.LinkedList<Long>();
		winroleids.addAll(_o_.winroleids);
		bossnpckey = _o_.bossnpckey;
	}

	private void assign(ClanBossInfo.Data _o_) {
		bossmonsterid = _o_.bossmonsterid;
		maxhp = _o_.maxhp;
		hp = _o_.hp;
		fighteindx = _o_.fighteindx;
		winroleids = new java.util.LinkedList<Long>();
		winroleids.addAll(_o_.winroleids);
		bossnpckey = _o_.bossnpckey;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(bossmonsterid);
		_os_.marshal(maxhp);
		_os_.marshal(hp);
		_os_.marshal(fighteindx);
		_os_.compact_uint32(winroleids.size());
		for (Long _v_ : winroleids) {
			_os_.marshal(_v_);
		}
		_os_.marshal(bossnpckey);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		bossmonsterid = _os_.unmarshal_int();
		maxhp = _os_.unmarshal_long();
		hp = _os_.unmarshal_long();
		fighteindx = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			winroleids.add(_v_);
		}
		bossnpckey = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ClanBossInfo copy() {
		_xdb_verify_unsafe_();
		return new ClanBossInfo(this);
	}

	@Override
	public xbean.ClanBossInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanBossInfo toBean() {
		_xdb_verify_unsafe_();
		return new ClanBossInfo(this); // same as copy()
	}

	@Override
	public xbean.ClanBossInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanBossInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getBossmonsterid() { // 
		_xdb_verify_unsafe_();
		return bossmonsterid;
	}

	@Override
	public long getMaxhp() { // 
		_xdb_verify_unsafe_();
		return maxhp;
	}

	@Override
	public long getHp() { // 
		_xdb_verify_unsafe_();
		return hp;
	}

	@Override
	public int getFighteindx() { // 
		_xdb_verify_unsafe_();
		return fighteindx;
	}

	@Override
	public java.util.List<Long> getWinroleids() { // 最后一击的角色
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "winroleids"), winroleids);
	}

	public java.util.List<Long> getWinroleidsAsData() { // 最后一击的角色
		_xdb_verify_unsafe_();
		java.util.List<Long> winroleids;
		ClanBossInfo _o_ = this;
		winroleids = new java.util.LinkedList<Long>();
		winroleids.addAll(_o_.winroleids);
		return winroleids;
	}

	@Override
	public long getBossnpckey() { // 
		_xdb_verify_unsafe_();
		return bossnpckey;
	}

	@Override
	public void setBossmonsterid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bossmonsterid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bossmonsterid) {
					public void rollback() { bossmonsterid = _xdb_saved; }
				};}});
		bossmonsterid = _v_;
	}

	@Override
	public void setMaxhp(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "maxhp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, maxhp) {
					public void rollback() { maxhp = _xdb_saved; }
				};}});
		maxhp = _v_;
	}

	@Override
	public void setHp(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "hp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, hp) {
					public void rollback() { hp = _xdb_saved; }
				};}});
		hp = _v_;
	}

	@Override
	public void setFighteindx(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fighteindx") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, fighteindx) {
					public void rollback() { fighteindx = _xdb_saved; }
				};}});
		fighteindx = _v_;
	}

	@Override
	public void setBossnpckey(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bossnpckey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, bossnpckey) {
					public void rollback() { bossnpckey = _xdb_saved; }
				};}});
		bossnpckey = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanBossInfo _o_ = null;
		if ( _o1_ instanceof ClanBossInfo ) _o_ = (ClanBossInfo)_o1_;
		else if ( _o1_ instanceof ClanBossInfo.Const ) _o_ = ((ClanBossInfo.Const)_o1_).nThis();
		else return false;
		if (bossmonsterid != _o_.bossmonsterid) return false;
		if (maxhp != _o_.maxhp) return false;
		if (hp != _o_.hp) return false;
		if (fighteindx != _o_.fighteindx) return false;
		if (!winroleids.equals(_o_.winroleids)) return false;
		if (bossnpckey != _o_.bossnpckey) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += bossmonsterid;
		_h_ += maxhp;
		_h_ += hp;
		_h_ += fighteindx;
		_h_ += winroleids.hashCode();
		_h_ += bossnpckey;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(bossmonsterid);
		_sb_.append(",");
		_sb_.append(maxhp);
		_sb_.append(",");
		_sb_.append(hp);
		_sb_.append(",");
		_sb_.append(fighteindx);
		_sb_.append(",");
		_sb_.append(winroleids);
		_sb_.append(",");
		_sb_.append(bossnpckey);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bossmonsterid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("maxhp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("hp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fighteindx"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("winroleids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bossnpckey"));
		return lb;
	}

	private class Const implements xbean.ClanBossInfo {
		ClanBossInfo nThis() {
			return ClanBossInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanBossInfo copy() {
			return ClanBossInfo.this.copy();
		}

		@Override
		public xbean.ClanBossInfo toData() {
			return ClanBossInfo.this.toData();
		}

		public xbean.ClanBossInfo toBean() {
			return ClanBossInfo.this.toBean();
		}

		@Override
		public xbean.ClanBossInfo toDataIf() {
			return ClanBossInfo.this.toDataIf();
		}

		public xbean.ClanBossInfo toBeanIf() {
			return ClanBossInfo.this.toBeanIf();
		}

		@Override
		public int getBossmonsterid() { // 
			_xdb_verify_unsafe_();
			return bossmonsterid;
		}

		@Override
		public long getMaxhp() { // 
			_xdb_verify_unsafe_();
			return maxhp;
		}

		@Override
		public long getHp() { // 
			_xdb_verify_unsafe_();
			return hp;
		}

		@Override
		public int getFighteindx() { // 
			_xdb_verify_unsafe_();
			return fighteindx;
		}

		@Override
		public java.util.List<Long> getWinroleids() { // 最后一击的角色
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(winroleids);
		}

		public java.util.List<Long> getWinroleidsAsData() { // 最后一击的角色
			_xdb_verify_unsafe_();
			java.util.List<Long> winroleids;
			ClanBossInfo _o_ = ClanBossInfo.this;
		winroleids = new java.util.LinkedList<Long>();
		winroleids.addAll(_o_.winroleids);
			return winroleids;
		}

		@Override
		public long getBossnpckey() { // 
			_xdb_verify_unsafe_();
			return bossnpckey;
		}

		@Override
		public void setBossmonsterid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMaxhp(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHp(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFighteindx(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBossnpckey(long _v_) { // 
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
			return ClanBossInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanBossInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanBossInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanBossInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanBossInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanBossInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanBossInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanBossInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanBossInfo.this.toString();
		}

	}

	public static final class Data implements xbean.ClanBossInfo {
		private int bossmonsterid; // 
		private long maxhp; // 
		private long hp; // 
		private int fighteindx; // 
		private java.util.LinkedList<Long> winroleids; // 最后一击的角色
		private long bossnpckey; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			winroleids = new java.util.LinkedList<Long>();
		}

		Data(xbean.ClanBossInfo _o1_) {
			if (_o1_ instanceof ClanBossInfo) assign((ClanBossInfo)_o1_);
			else if (_o1_ instanceof ClanBossInfo.Data) assign((ClanBossInfo.Data)_o1_);
			else if (_o1_ instanceof ClanBossInfo.Const) assign(((ClanBossInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanBossInfo _o_) {
			bossmonsterid = _o_.bossmonsterid;
			maxhp = _o_.maxhp;
			hp = _o_.hp;
			fighteindx = _o_.fighteindx;
			winroleids = new java.util.LinkedList<Long>();
			winroleids.addAll(_o_.winroleids);
			bossnpckey = _o_.bossnpckey;
		}

		private void assign(ClanBossInfo.Data _o_) {
			bossmonsterid = _o_.bossmonsterid;
			maxhp = _o_.maxhp;
			hp = _o_.hp;
			fighteindx = _o_.fighteindx;
			winroleids = new java.util.LinkedList<Long>();
			winroleids.addAll(_o_.winroleids);
			bossnpckey = _o_.bossnpckey;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(bossmonsterid);
			_os_.marshal(maxhp);
			_os_.marshal(hp);
			_os_.marshal(fighteindx);
			_os_.compact_uint32(winroleids.size());
			for (Long _v_ : winroleids) {
				_os_.marshal(_v_);
			}
			_os_.marshal(bossnpckey);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			bossmonsterid = _os_.unmarshal_int();
			maxhp = _os_.unmarshal_long();
			hp = _os_.unmarshal_long();
			fighteindx = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				winroleids.add(_v_);
			}
			bossnpckey = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ClanBossInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanBossInfo toData() {
			return new Data(this);
		}

		public xbean.ClanBossInfo toBean() {
			return new ClanBossInfo(this, null, null);
		}

		@Override
		public xbean.ClanBossInfo toDataIf() {
			return this;
		}

		public xbean.ClanBossInfo toBeanIf() {
			return new ClanBossInfo(this, null, null);
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
		public int getBossmonsterid() { // 
			return bossmonsterid;
		}

		@Override
		public long getMaxhp() { // 
			return maxhp;
		}

		@Override
		public long getHp() { // 
			return hp;
		}

		@Override
		public int getFighteindx() { // 
			return fighteindx;
		}

		@Override
		public java.util.List<Long> getWinroleids() { // 最后一击的角色
			return winroleids;
		}

		@Override
		public java.util.List<Long> getWinroleidsAsData() { // 最后一击的角色
			return winroleids;
		}

		@Override
		public long getBossnpckey() { // 
			return bossnpckey;
		}

		@Override
		public void setBossmonsterid(int _v_) { // 
			bossmonsterid = _v_;
		}

		@Override
		public void setMaxhp(long _v_) { // 
			maxhp = _v_;
		}

		@Override
		public void setHp(long _v_) { // 
			hp = _v_;
		}

		@Override
		public void setFighteindx(int _v_) { // 
			fighteindx = _v_;
		}

		@Override
		public void setBossnpckey(long _v_) { // 
			bossnpckey = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanBossInfo.Data)) return false;
			ClanBossInfo.Data _o_ = (ClanBossInfo.Data) _o1_;
			if (bossmonsterid != _o_.bossmonsterid) return false;
			if (maxhp != _o_.maxhp) return false;
			if (hp != _o_.hp) return false;
			if (fighteindx != _o_.fighteindx) return false;
			if (!winroleids.equals(_o_.winroleids)) return false;
			if (bossnpckey != _o_.bossnpckey) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += bossmonsterid;
			_h_ += maxhp;
			_h_ += hp;
			_h_ += fighteindx;
			_h_ += winroleids.hashCode();
			_h_ += bossnpckey;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(bossmonsterid);
			_sb_.append(",");
			_sb_.append(maxhp);
			_sb_.append(",");
			_sb_.append(hp);
			_sb_.append(",");
			_sb_.append(fighteindx);
			_sb_.append(",");
			_sb_.append(winroleids);
			_sb_.append(",");
			_sb_.append(bossnpckey);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
