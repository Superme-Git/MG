
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PvP5Role extends mkdb.XBean implements xbean.PvP5Role {
	private int grade; // 此记录所属的赛级
	private int camp; // 所属阵营
	private int excellent; // 是否优胜
	private int score; // 积分
	private int battlenum; // 本场比赛的次数
	private int winnum; // 本场比赛赢的次数
	private int continuewinnum; // 连胜次数
	private int firstwinaward; // 首胜奖励
	private int fivebattleaward; // 五战奖励
	private long lastbattletime; // 上次战斗结束的时间
	private long lastentertime; // 上次进入赛场时间

	@Override
	public void _reset_unsafe_() {
		grade = 0;
		camp = 0;
		excellent = 0;
		score = 0;
		battlenum = 0;
		winnum = 0;
		continuewinnum = 0;
		firstwinaward = 0;
		fivebattleaward = 0;
		lastbattletime = 0L;
		lastentertime = 0L;
	}

	PvP5Role(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public PvP5Role() {
		this(0, null, null);
	}

	public PvP5Role(PvP5Role _o_) {
		this(_o_, null, null);
	}

	PvP5Role(xbean.PvP5Role _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PvP5Role) assign((PvP5Role)_o1_);
		else if (_o1_ instanceof PvP5Role.Data) assign((PvP5Role.Data)_o1_);
		else if (_o1_ instanceof PvP5Role.Const) assign(((PvP5Role.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PvP5Role _o_) {
		_o_._xdb_verify_unsafe_();
		grade = _o_.grade;
		camp = _o_.camp;
		excellent = _o_.excellent;
		score = _o_.score;
		battlenum = _o_.battlenum;
		winnum = _o_.winnum;
		continuewinnum = _o_.continuewinnum;
		firstwinaward = _o_.firstwinaward;
		fivebattleaward = _o_.fivebattleaward;
		lastbattletime = _o_.lastbattletime;
		lastentertime = _o_.lastentertime;
	}

	private void assign(PvP5Role.Data _o_) {
		grade = _o_.grade;
		camp = _o_.camp;
		excellent = _o_.excellent;
		score = _o_.score;
		battlenum = _o_.battlenum;
		winnum = _o_.winnum;
		continuewinnum = _o_.continuewinnum;
		firstwinaward = _o_.firstwinaward;
		fivebattleaward = _o_.fivebattleaward;
		lastbattletime = _o_.lastbattletime;
		lastentertime = _o_.lastentertime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(grade);
		_os_.marshal(camp);
		_os_.marshal(excellent);
		_os_.marshal(score);
		_os_.marshal(battlenum);
		_os_.marshal(winnum);
		_os_.marshal(continuewinnum);
		_os_.marshal(firstwinaward);
		_os_.marshal(fivebattleaward);
		_os_.marshal(lastbattletime);
		_os_.marshal(lastentertime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		grade = _os_.unmarshal_int();
		camp = _os_.unmarshal_int();
		excellent = _os_.unmarshal_int();
		score = _os_.unmarshal_int();
		battlenum = _os_.unmarshal_int();
		winnum = _os_.unmarshal_int();
		continuewinnum = _os_.unmarshal_int();
		firstwinaward = _os_.unmarshal_int();
		fivebattleaward = _os_.unmarshal_int();
		lastbattletime = _os_.unmarshal_long();
		lastentertime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.PvP5Role copy() {
		_xdb_verify_unsafe_();
		return new PvP5Role(this);
	}

	@Override
	public xbean.PvP5Role toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PvP5Role toBean() {
		_xdb_verify_unsafe_();
		return new PvP5Role(this); // same as copy()
	}

	@Override
	public xbean.PvP5Role toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PvP5Role toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getGrade() { // 此记录所属的赛级
		_xdb_verify_unsafe_();
		return grade;
	}

	@Override
	public int getCamp() { // 所属阵营
		_xdb_verify_unsafe_();
		return camp;
	}

	@Override
	public int getExcellent() { // 是否优胜
		_xdb_verify_unsafe_();
		return excellent;
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
	public int getContinuewinnum() { // 连胜次数
		_xdb_verify_unsafe_();
		return continuewinnum;
	}

	@Override
	public int getFirstwinaward() { // 首胜奖励
		_xdb_verify_unsafe_();
		return firstwinaward;
	}

	@Override
	public int getFivebattleaward() { // 五战奖励
		_xdb_verify_unsafe_();
		return fivebattleaward;
	}

	@Override
	public long getLastbattletime() { // 上次战斗结束的时间
		_xdb_verify_unsafe_();
		return lastbattletime;
	}

	@Override
	public long getLastentertime() { // 上次进入赛场时间
		_xdb_verify_unsafe_();
		return lastentertime;
	}

	@Override
	public void setGrade(int _v_) { // 此记录所属的赛级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "grade") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, grade) {
					public void rollback() { grade = _xdb_saved; }
				};}});
		grade = _v_;
	}

	@Override
	public void setCamp(int _v_) { // 所属阵营
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "camp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, camp) {
					public void rollback() { camp = _xdb_saved; }
				};}});
		camp = _v_;
	}

	@Override
	public void setExcellent(int _v_) { // 是否优胜
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "excellent") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, excellent) {
					public void rollback() { excellent = _xdb_saved; }
				};}});
		excellent = _v_;
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
	public void setContinuewinnum(int _v_) { // 连胜次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "continuewinnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, continuewinnum) {
					public void rollback() { continuewinnum = _xdb_saved; }
				};}});
		continuewinnum = _v_;
	}

	@Override
	public void setFirstwinaward(int _v_) { // 首胜奖励
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firstwinaward") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, firstwinaward) {
					public void rollback() { firstwinaward = _xdb_saved; }
				};}});
		firstwinaward = _v_;
	}

	@Override
	public void setFivebattleaward(int _v_) { // 五战奖励
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fivebattleaward") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, fivebattleaward) {
					public void rollback() { fivebattleaward = _xdb_saved; }
				};}});
		fivebattleaward = _v_;
	}

	@Override
	public void setLastbattletime(long _v_) { // 上次战斗结束的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastbattletime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastbattletime) {
					public void rollback() { lastbattletime = _xdb_saved; }
				};}});
		lastbattletime = _v_;
	}

	@Override
	public void setLastentertime(long _v_) { // 上次进入赛场时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastentertime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastentertime) {
					public void rollback() { lastentertime = _xdb_saved; }
				};}});
		lastentertime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PvP5Role _o_ = null;
		if ( _o1_ instanceof PvP5Role ) _o_ = (PvP5Role)_o1_;
		else if ( _o1_ instanceof PvP5Role.Const ) _o_ = ((PvP5Role.Const)_o1_).nThis();
		else return false;
		if (grade != _o_.grade) return false;
		if (camp != _o_.camp) return false;
		if (excellent != _o_.excellent) return false;
		if (score != _o_.score) return false;
		if (battlenum != _o_.battlenum) return false;
		if (winnum != _o_.winnum) return false;
		if (continuewinnum != _o_.continuewinnum) return false;
		if (firstwinaward != _o_.firstwinaward) return false;
		if (fivebattleaward != _o_.fivebattleaward) return false;
		if (lastbattletime != _o_.lastbattletime) return false;
		if (lastentertime != _o_.lastentertime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += grade;
		_h_ += camp;
		_h_ += excellent;
		_h_ += score;
		_h_ += battlenum;
		_h_ += winnum;
		_h_ += continuewinnum;
		_h_ += firstwinaward;
		_h_ += fivebattleaward;
		_h_ += lastbattletime;
		_h_ += lastentertime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(grade);
		_sb_.append(",");
		_sb_.append(camp);
		_sb_.append(",");
		_sb_.append(excellent);
		_sb_.append(",");
		_sb_.append(score);
		_sb_.append(",");
		_sb_.append(battlenum);
		_sb_.append(",");
		_sb_.append(winnum);
		_sb_.append(",");
		_sb_.append(continuewinnum);
		_sb_.append(",");
		_sb_.append(firstwinaward);
		_sb_.append(",");
		_sb_.append(fivebattleaward);
		_sb_.append(",");
		_sb_.append(lastbattletime);
		_sb_.append(",");
		_sb_.append(lastentertime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("grade"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("camp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("excellent"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("score"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battlenum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("winnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("continuewinnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firstwinaward"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fivebattleaward"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastbattletime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastentertime"));
		return lb;
	}

	private class Const implements xbean.PvP5Role {
		PvP5Role nThis() {
			return PvP5Role.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PvP5Role copy() {
			return PvP5Role.this.copy();
		}

		@Override
		public xbean.PvP5Role toData() {
			return PvP5Role.this.toData();
		}

		public xbean.PvP5Role toBean() {
			return PvP5Role.this.toBean();
		}

		@Override
		public xbean.PvP5Role toDataIf() {
			return PvP5Role.this.toDataIf();
		}

		public xbean.PvP5Role toBeanIf() {
			return PvP5Role.this.toBeanIf();
		}

		@Override
		public int getGrade() { // 此记录所属的赛级
			_xdb_verify_unsafe_();
			return grade;
		}

		@Override
		public int getCamp() { // 所属阵营
			_xdb_verify_unsafe_();
			return camp;
		}

		@Override
		public int getExcellent() { // 是否优胜
			_xdb_verify_unsafe_();
			return excellent;
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
		public int getContinuewinnum() { // 连胜次数
			_xdb_verify_unsafe_();
			return continuewinnum;
		}

		@Override
		public int getFirstwinaward() { // 首胜奖励
			_xdb_verify_unsafe_();
			return firstwinaward;
		}

		@Override
		public int getFivebattleaward() { // 五战奖励
			_xdb_verify_unsafe_();
			return fivebattleaward;
		}

		@Override
		public long getLastbattletime() { // 上次战斗结束的时间
			_xdb_verify_unsafe_();
			return lastbattletime;
		}

		@Override
		public long getLastentertime() { // 上次进入赛场时间
			_xdb_verify_unsafe_();
			return lastentertime;
		}

		@Override
		public void setGrade(int _v_) { // 此记录所属的赛级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCamp(int _v_) { // 所属阵营
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExcellent(int _v_) { // 是否优胜
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
		public void setContinuewinnum(int _v_) { // 连胜次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirstwinaward(int _v_) { // 首胜奖励
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFivebattleaward(int _v_) { // 五战奖励
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastbattletime(long _v_) { // 上次战斗结束的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastentertime(long _v_) { // 上次进入赛场时间
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
			return PvP5Role.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PvP5Role.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PvP5Role.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PvP5Role.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PvP5Role.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PvP5Role.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PvP5Role.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PvP5Role.this.hashCode();
		}

		@Override
		public String toString() {
			return PvP5Role.this.toString();
		}

	}

	public static final class Data implements xbean.PvP5Role {
		private int grade; // 此记录所属的赛级
		private int camp; // 所属阵营
		private int excellent; // 是否优胜
		private int score; // 积分
		private int battlenum; // 本场比赛的次数
		private int winnum; // 本场比赛赢的次数
		private int continuewinnum; // 连胜次数
		private int firstwinaward; // 首胜奖励
		private int fivebattleaward; // 五战奖励
		private long lastbattletime; // 上次战斗结束的时间
		private long lastentertime; // 上次进入赛场时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.PvP5Role _o1_) {
			if (_o1_ instanceof PvP5Role) assign((PvP5Role)_o1_);
			else if (_o1_ instanceof PvP5Role.Data) assign((PvP5Role.Data)_o1_);
			else if (_o1_ instanceof PvP5Role.Const) assign(((PvP5Role.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PvP5Role _o_) {
			grade = _o_.grade;
			camp = _o_.camp;
			excellent = _o_.excellent;
			score = _o_.score;
			battlenum = _o_.battlenum;
			winnum = _o_.winnum;
			continuewinnum = _o_.continuewinnum;
			firstwinaward = _o_.firstwinaward;
			fivebattleaward = _o_.fivebattleaward;
			lastbattletime = _o_.lastbattletime;
			lastentertime = _o_.lastentertime;
		}

		private void assign(PvP5Role.Data _o_) {
			grade = _o_.grade;
			camp = _o_.camp;
			excellent = _o_.excellent;
			score = _o_.score;
			battlenum = _o_.battlenum;
			winnum = _o_.winnum;
			continuewinnum = _o_.continuewinnum;
			firstwinaward = _o_.firstwinaward;
			fivebattleaward = _o_.fivebattleaward;
			lastbattletime = _o_.lastbattletime;
			lastentertime = _o_.lastentertime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(grade);
			_os_.marshal(camp);
			_os_.marshal(excellent);
			_os_.marshal(score);
			_os_.marshal(battlenum);
			_os_.marshal(winnum);
			_os_.marshal(continuewinnum);
			_os_.marshal(firstwinaward);
			_os_.marshal(fivebattleaward);
			_os_.marshal(lastbattletime);
			_os_.marshal(lastentertime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			grade = _os_.unmarshal_int();
			camp = _os_.unmarshal_int();
			excellent = _os_.unmarshal_int();
			score = _os_.unmarshal_int();
			battlenum = _os_.unmarshal_int();
			winnum = _os_.unmarshal_int();
			continuewinnum = _os_.unmarshal_int();
			firstwinaward = _os_.unmarshal_int();
			fivebattleaward = _os_.unmarshal_int();
			lastbattletime = _os_.unmarshal_long();
			lastentertime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.PvP5Role copy() {
			return new Data(this);
		}

		@Override
		public xbean.PvP5Role toData() {
			return new Data(this);
		}

		public xbean.PvP5Role toBean() {
			return new PvP5Role(this, null, null);
		}

		@Override
		public xbean.PvP5Role toDataIf() {
			return this;
		}

		public xbean.PvP5Role toBeanIf() {
			return new PvP5Role(this, null, null);
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
		public int getGrade() { // 此记录所属的赛级
			return grade;
		}

		@Override
		public int getCamp() { // 所属阵营
			return camp;
		}

		@Override
		public int getExcellent() { // 是否优胜
			return excellent;
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
		public int getContinuewinnum() { // 连胜次数
			return continuewinnum;
		}

		@Override
		public int getFirstwinaward() { // 首胜奖励
			return firstwinaward;
		}

		@Override
		public int getFivebattleaward() { // 五战奖励
			return fivebattleaward;
		}

		@Override
		public long getLastbattletime() { // 上次战斗结束的时间
			return lastbattletime;
		}

		@Override
		public long getLastentertime() { // 上次进入赛场时间
			return lastentertime;
		}

		@Override
		public void setGrade(int _v_) { // 此记录所属的赛级
			grade = _v_;
		}

		@Override
		public void setCamp(int _v_) { // 所属阵营
			camp = _v_;
		}

		@Override
		public void setExcellent(int _v_) { // 是否优胜
			excellent = _v_;
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
		public void setContinuewinnum(int _v_) { // 连胜次数
			continuewinnum = _v_;
		}

		@Override
		public void setFirstwinaward(int _v_) { // 首胜奖励
			firstwinaward = _v_;
		}

		@Override
		public void setFivebattleaward(int _v_) { // 五战奖励
			fivebattleaward = _v_;
		}

		@Override
		public void setLastbattletime(long _v_) { // 上次战斗结束的时间
			lastbattletime = _v_;
		}

		@Override
		public void setLastentertime(long _v_) { // 上次进入赛场时间
			lastentertime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PvP5Role.Data)) return false;
			PvP5Role.Data _o_ = (PvP5Role.Data) _o1_;
			if (grade != _o_.grade) return false;
			if (camp != _o_.camp) return false;
			if (excellent != _o_.excellent) return false;
			if (score != _o_.score) return false;
			if (battlenum != _o_.battlenum) return false;
			if (winnum != _o_.winnum) return false;
			if (continuewinnum != _o_.continuewinnum) return false;
			if (firstwinaward != _o_.firstwinaward) return false;
			if (fivebattleaward != _o_.fivebattleaward) return false;
			if (lastbattletime != _o_.lastbattletime) return false;
			if (lastentertime != _o_.lastentertime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += grade;
			_h_ += camp;
			_h_ += excellent;
			_h_ += score;
			_h_ += battlenum;
			_h_ += winnum;
			_h_ += continuewinnum;
			_h_ += firstwinaward;
			_h_ += fivebattleaward;
			_h_ += lastbattletime;
			_h_ += lastentertime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(grade);
			_sb_.append(",");
			_sb_.append(camp);
			_sb_.append(",");
			_sb_.append(excellent);
			_sb_.append(",");
			_sb_.append(score);
			_sb_.append(",");
			_sb_.append(battlenum);
			_sb_.append(",");
			_sb_.append(winnum);
			_sb_.append(",");
			_sb_.append(continuewinnum);
			_sb_.append(",");
			_sb_.append(firstwinaward);
			_sb_.append(",");
			_sb_.append(fivebattleaward);
			_sb_.append(",");
			_sb_.append(lastbattletime);
			_sb_.append(",");
			_sb_.append(lastentertime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
