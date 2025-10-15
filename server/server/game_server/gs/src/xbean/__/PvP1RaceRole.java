
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PvP1RaceRole extends mkdb.XBean implements xbean.PvP1RaceRole {
	private long roleid; // 
	private String name; // 
	private int score; // 积分
	private int battlenum; // 本场比赛的次数
	private int winnum; // 本场比赛赢的次数

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		name = "";
		score = 0;
		battlenum = 0;
		winnum = 0;
	}

	PvP1RaceRole(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		name = "";
	}

	public PvP1RaceRole() {
		this(0, null, null);
	}

	public PvP1RaceRole(PvP1RaceRole _o_) {
		this(_o_, null, null);
	}

	PvP1RaceRole(xbean.PvP1RaceRole _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PvP1RaceRole) assign((PvP1RaceRole)_o1_);
		else if (_o1_ instanceof PvP1RaceRole.Data) assign((PvP1RaceRole.Data)_o1_);
		else if (_o1_ instanceof PvP1RaceRole.Const) assign(((PvP1RaceRole.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PvP1RaceRole _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		name = _o_.name;
		score = _o_.score;
		battlenum = _o_.battlenum;
		winnum = _o_.winnum;
	}

	private void assign(PvP1RaceRole.Data _o_) {
		roleid = _o_.roleid;
		name = _o_.name;
		score = _o_.score;
		battlenum = _o_.battlenum;
		winnum = _o_.winnum;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(name, mkdb.Const.IO_CHARSET);
		_os_.marshal(score);
		_os_.marshal(battlenum);
		_os_.marshal(winnum);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		score = _os_.unmarshal_int();
		battlenum = _os_.unmarshal_int();
		winnum = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.PvP1RaceRole copy() {
		_xdb_verify_unsafe_();
		return new PvP1RaceRole(this);
	}

	@Override
	public xbean.PvP1RaceRole toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PvP1RaceRole toBean() {
		_xdb_verify_unsafe_();
		return new PvP1RaceRole(this); // same as copy()
	}

	@Override
	public xbean.PvP1RaceRole toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PvP1RaceRole toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // 
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public String getName() { // 
		_xdb_verify_unsafe_();
		return name;
	}

	@Override
	public com.locojoy.base.Octets getNameOctets() { // 
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getScore() { // 积分
		_xdb_verify_unsafe_();
		return score;
	}

	@Override
	public int getBattlenum() { // 本场比赛的次数
		_xdb_verify_unsafe_();
		return battlenum;
	}

	@Override
	public int getWinnum() { // 本场比赛赢的次数
		_xdb_verify_unsafe_();
		return winnum;
	}

	@Override
	public void setRoleid(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setName(String _v_) { // 
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "name") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, name) {
					public void rollback() { name = _xdb_saved; }
				};}});
		name = _v_;
	}

	@Override
	public void setNameOctets(com.locojoy.base.Octets _v_) { // 
		_xdb_verify_unsafe_();
		this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setScore(int _v_) { // 积分
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "score") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, score) {
					public void rollback() { score = _xdb_saved; }
				};}});
		score = _v_;
	}

	@Override
	public void setBattlenum(int _v_) { // 本场比赛的次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "battlenum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, battlenum) {
					public void rollback() { battlenum = _xdb_saved; }
				};}});
		battlenum = _v_;
	}

	@Override
	public void setWinnum(int _v_) { // 本场比赛赢的次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "winnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, winnum) {
					public void rollback() { winnum = _xdb_saved; }
				};}});
		winnum = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PvP1RaceRole _o_ = null;
		if ( _o1_ instanceof PvP1RaceRole ) _o_ = (PvP1RaceRole)_o1_;
		else if ( _o1_ instanceof PvP1RaceRole.Const ) _o_ = ((PvP1RaceRole.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (!name.equals(_o_.name)) return false;
		if (score != _o_.score) return false;
		if (battlenum != _o_.battlenum) return false;
		if (winnum != _o_.winnum) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += name.hashCode();
		_h_ += score;
		_h_ += battlenum;
		_h_ += winnum;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append("'").append(name).append("'");
		_sb_.append(",");
		_sb_.append(score);
		_sb_.append(",");
		_sb_.append(battlenum);
		_sb_.append(",");
		_sb_.append(winnum);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("name"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("score"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battlenum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("winnum"));
		return lb;
	}

	private class Const implements xbean.PvP1RaceRole {
		PvP1RaceRole nThis() {
			return PvP1RaceRole.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PvP1RaceRole copy() {
			return PvP1RaceRole.this.copy();
		}

		@Override
		public xbean.PvP1RaceRole toData() {
			return PvP1RaceRole.this.toData();
		}

		public xbean.PvP1RaceRole toBean() {
			return PvP1RaceRole.this.toBean();
		}

		@Override
		public xbean.PvP1RaceRole toDataIf() {
			return PvP1RaceRole.this.toDataIf();
		}

		public xbean.PvP1RaceRole toBeanIf() {
			return PvP1RaceRole.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public String getName() { // 
			_xdb_verify_unsafe_();
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 
			_xdb_verify_unsafe_();
			return PvP1RaceRole.this.getNameOctets();
		}

		@Override
		public int getScore() { // 积分
			_xdb_verify_unsafe_();
			return score;
		}

		@Override
		public int getBattlenum() { // 本场比赛的次数
			_xdb_verify_unsafe_();
			return battlenum;
		}

		@Override
		public int getWinnum() { // 本场比赛赢的次数
			_xdb_verify_unsafe_();
			return winnum;
		}

		@Override
		public void setRoleid(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setName(String _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setScore(int _v_) { // 积分
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBattlenum(int _v_) { // 本场比赛的次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWinnum(int _v_) { // 本场比赛赢的次数
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
			return PvP1RaceRole.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PvP1RaceRole.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PvP1RaceRole.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PvP1RaceRole.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PvP1RaceRole.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PvP1RaceRole.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PvP1RaceRole.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PvP1RaceRole.this.hashCode();
		}

		@Override
		public String toString() {
			return PvP1RaceRole.this.toString();
		}

	}

	public static final class Data implements xbean.PvP1RaceRole {
		private long roleid; // 
		private String name; // 
		private int score; // 积分
		private int battlenum; // 本场比赛的次数
		private int winnum; // 本场比赛赢的次数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			name = "";
		}

		Data(xbean.PvP1RaceRole _o1_) {
			if (_o1_ instanceof PvP1RaceRole) assign((PvP1RaceRole)_o1_);
			else if (_o1_ instanceof PvP1RaceRole.Data) assign((PvP1RaceRole.Data)_o1_);
			else if (_o1_ instanceof PvP1RaceRole.Const) assign(((PvP1RaceRole.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PvP1RaceRole _o_) {
			roleid = _o_.roleid;
			name = _o_.name;
			score = _o_.score;
			battlenum = _o_.battlenum;
			winnum = _o_.winnum;
		}

		private void assign(PvP1RaceRole.Data _o_) {
			roleid = _o_.roleid;
			name = _o_.name;
			score = _o_.score;
			battlenum = _o_.battlenum;
			winnum = _o_.winnum;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(name, mkdb.Const.IO_CHARSET);
			_os_.marshal(score);
			_os_.marshal(battlenum);
			_os_.marshal(winnum);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			score = _os_.unmarshal_int();
			battlenum = _os_.unmarshal_int();
			winnum = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.PvP1RaceRole copy() {
			return new Data(this);
		}

		@Override
		public xbean.PvP1RaceRole toData() {
			return new Data(this);
		}

		public xbean.PvP1RaceRole toBean() {
			return new PvP1RaceRole(this, null, null);
		}

		@Override
		public xbean.PvP1RaceRole toDataIf() {
			return this;
		}

		public xbean.PvP1RaceRole toBeanIf() {
			return new PvP1RaceRole(this, null, null);
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
		public long getRoleid() { // 
			return roleid;
		}

		@Override
		public String getName() { // 
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 
			return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getScore() { // 积分
			return score;
		}

		@Override
		public int getBattlenum() { // 本场比赛的次数
			return battlenum;
		}

		@Override
		public int getWinnum() { // 本场比赛赢的次数
			return winnum;
		}

		@Override
		public void setRoleid(long _v_) { // 
			roleid = _v_;
		}

		@Override
		public void setName(String _v_) { // 
			if (null == _v_)
				throw new NullPointerException();
			name = _v_;
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 
			this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setScore(int _v_) { // 积分
			score = _v_;
		}

		@Override
		public void setBattlenum(int _v_) { // 本场比赛的次数
			battlenum = _v_;
		}

		@Override
		public void setWinnum(int _v_) { // 本场比赛赢的次数
			winnum = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PvP1RaceRole.Data)) return false;
			PvP1RaceRole.Data _o_ = (PvP1RaceRole.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (!name.equals(_o_.name)) return false;
			if (score != _o_.score) return false;
			if (battlenum != _o_.battlenum) return false;
			if (winnum != _o_.winnum) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += name.hashCode();
			_h_ += score;
			_h_ += battlenum;
			_h_ += winnum;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append("'").append(name).append("'");
			_sb_.append(",");
			_sb_.append(score);
			_sb_.append(",");
			_sb_.append(battlenum);
			_sb_.append(",");
			_sb_.append(winnum);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
