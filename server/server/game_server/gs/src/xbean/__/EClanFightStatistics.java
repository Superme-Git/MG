
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class EClanFightStatistics extends mkdb.XBean implements xbean.EClanFightStatistics {
	private int act; // 行动力 by changhao
	private int score; // 积分 by changhao
	private long entertime; // 进入战场时间 by changhao
	private int winneritemnum; // 获取胜利宝箱的数量 by changhao
	private int clancelebrateitemnum; // 获取公会庆祝宝箱的数量 by changhao
	private int winnum; // 连续胜利的常数 by changhao
	private long lastcbattlefieldrankliststamp; // 上次请求战场积分时间戳 by changhao
	private long lastlosestamp; // 上次战败时间戳 by changhao

	@Override
	public void _reset_unsafe_() {
		act = 0;
		score = 0;
		entertime = 0L;
		winneritemnum = 0;
		clancelebrateitemnum = 0;
		winnum = 0;
		lastcbattlefieldrankliststamp = 0L;
		lastlosestamp = 0L;
	}

	EClanFightStatistics(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public EClanFightStatistics() {
		this(0, null, null);
	}

	public EClanFightStatistics(EClanFightStatistics _o_) {
		this(_o_, null, null);
	}

	EClanFightStatistics(xbean.EClanFightStatistics _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof EClanFightStatistics) assign((EClanFightStatistics)_o1_);
		else if (_o1_ instanceof EClanFightStatistics.Data) assign((EClanFightStatistics.Data)_o1_);
		else if (_o1_ instanceof EClanFightStatistics.Const) assign(((EClanFightStatistics.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(EClanFightStatistics _o_) {
		_o_._xdb_verify_unsafe_();
		act = _o_.act;
		score = _o_.score;
		entertime = _o_.entertime;
		winneritemnum = _o_.winneritemnum;
		clancelebrateitemnum = _o_.clancelebrateitemnum;
		winnum = _o_.winnum;
		lastcbattlefieldrankliststamp = _o_.lastcbattlefieldrankliststamp;
		lastlosestamp = _o_.lastlosestamp;
	}

	private void assign(EClanFightStatistics.Data _o_) {
		act = _o_.act;
		score = _o_.score;
		entertime = _o_.entertime;
		winneritemnum = _o_.winneritemnum;
		clancelebrateitemnum = _o_.clancelebrateitemnum;
		winnum = _o_.winnum;
		lastcbattlefieldrankliststamp = _o_.lastcbattlefieldrankliststamp;
		lastlosestamp = _o_.lastlosestamp;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(act);
		_os_.marshal(score);
		_os_.marshal(entertime);
		_os_.marshal(winneritemnum);
		_os_.marshal(clancelebrateitemnum);
		_os_.marshal(winnum);
		_os_.marshal(lastcbattlefieldrankliststamp);
		_os_.marshal(lastlosestamp);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		act = _os_.unmarshal_int();
		score = _os_.unmarshal_int();
		entertime = _os_.unmarshal_long();
		winneritemnum = _os_.unmarshal_int();
		clancelebrateitemnum = _os_.unmarshal_int();
		winnum = _os_.unmarshal_int();
		lastcbattlefieldrankliststamp = _os_.unmarshal_long();
		lastlosestamp = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.EClanFightStatistics copy() {
		_xdb_verify_unsafe_();
		return new EClanFightStatistics(this);
	}

	@Override
	public xbean.EClanFightStatistics toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EClanFightStatistics toBean() {
		_xdb_verify_unsafe_();
		return new EClanFightStatistics(this); // same as copy()
	}

	@Override
	public xbean.EClanFightStatistics toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.EClanFightStatistics toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getAct() { // 行动力 by changhao
		_xdb_verify_unsafe_();
		return act;
	}

	@Override
	public int getScore() { // 积分 by changhao
		_xdb_verify_unsafe_();
		return score;
	}

	@Override
	public long getEntertime() { // 进入战场时间 by changhao
		_xdb_verify_unsafe_();
		return entertime;
	}

	@Override
	public int getWinneritemnum() { // 获取胜利宝箱的数量 by changhao
		_xdb_verify_unsafe_();
		return winneritemnum;
	}

	@Override
	public int getClancelebrateitemnum() { // 获取公会庆祝宝箱的数量 by changhao
		_xdb_verify_unsafe_();
		return clancelebrateitemnum;
	}

	@Override
	public int getWinnum() { // 连续胜利的常数 by changhao
		_xdb_verify_unsafe_();
		return winnum;
	}

	@Override
	public long getLastcbattlefieldrankliststamp() { // 上次请求战场积分时间戳 by changhao
		_xdb_verify_unsafe_();
		return lastcbattlefieldrankliststamp;
	}

	@Override
	public long getLastlosestamp() { // 上次战败时间戳 by changhao
		_xdb_verify_unsafe_();
		return lastlosestamp;
	}

	@Override
	public void setAct(int _v_) { // 行动力 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "act") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, act) {
					public void rollback() { act = _xdb_saved; }
				};}});
		act = _v_;
	}

	@Override
	public void setScore(int _v_) { // 积分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "score") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, score) {
					public void rollback() { score = _xdb_saved; }
				};}});
		score = _v_;
	}

	@Override
	public void setEntertime(long _v_) { // 进入战场时间 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "entertime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, entertime) {
					public void rollback() { entertime = _xdb_saved; }
				};}});
		entertime = _v_;
	}

	@Override
	public void setWinneritemnum(int _v_) { // 获取胜利宝箱的数量 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "winneritemnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, winneritemnum) {
					public void rollback() { winneritemnum = _xdb_saved; }
				};}});
		winneritemnum = _v_;
	}

	@Override
	public void setClancelebrateitemnum(int _v_) { // 获取公会庆祝宝箱的数量 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clancelebrateitemnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clancelebrateitemnum) {
					public void rollback() { clancelebrateitemnum = _xdb_saved; }
				};}});
		clancelebrateitemnum = _v_;
	}

	@Override
	public void setWinnum(int _v_) { // 连续胜利的常数 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "winnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, winnum) {
					public void rollback() { winnum = _xdb_saved; }
				};}});
		winnum = _v_;
	}

	@Override
	public void setLastcbattlefieldrankliststamp(long _v_) { // 上次请求战场积分时间戳 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastcbattlefieldrankliststamp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastcbattlefieldrankliststamp) {
					public void rollback() { lastcbattlefieldrankliststamp = _xdb_saved; }
				};}});
		lastcbattlefieldrankliststamp = _v_;
	}

	@Override
	public void setLastlosestamp(long _v_) { // 上次战败时间戳 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastlosestamp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastlosestamp) {
					public void rollback() { lastlosestamp = _xdb_saved; }
				};}});
		lastlosestamp = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		EClanFightStatistics _o_ = null;
		if ( _o1_ instanceof EClanFightStatistics ) _o_ = (EClanFightStatistics)_o1_;
		else if ( _o1_ instanceof EClanFightStatistics.Const ) _o_ = ((EClanFightStatistics.Const)_o1_).nThis();
		else return false;
		if (act != _o_.act) return false;
		if (score != _o_.score) return false;
		if (entertime != _o_.entertime) return false;
		if (winneritemnum != _o_.winneritemnum) return false;
		if (clancelebrateitemnum != _o_.clancelebrateitemnum) return false;
		if (winnum != _o_.winnum) return false;
		if (lastcbattlefieldrankliststamp != _o_.lastcbattlefieldrankliststamp) return false;
		if (lastlosestamp != _o_.lastlosestamp) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += act;
		_h_ += score;
		_h_ += entertime;
		_h_ += winneritemnum;
		_h_ += clancelebrateitemnum;
		_h_ += winnum;
		_h_ += lastcbattlefieldrankliststamp;
		_h_ += lastlosestamp;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(act);
		_sb_.append(",");
		_sb_.append(score);
		_sb_.append(",");
		_sb_.append(entertime);
		_sb_.append(",");
		_sb_.append(winneritemnum);
		_sb_.append(",");
		_sb_.append(clancelebrateitemnum);
		_sb_.append(",");
		_sb_.append(winnum);
		_sb_.append(",");
		_sb_.append(lastcbattlefieldrankliststamp);
		_sb_.append(",");
		_sb_.append(lastlosestamp);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("act"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("score"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("entertime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("winneritemnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clancelebrateitemnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("winnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastcbattlefieldrankliststamp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastlosestamp"));
		return lb;
	}

	private class Const implements xbean.EClanFightStatistics {
		EClanFightStatistics nThis() {
			return EClanFightStatistics.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.EClanFightStatistics copy() {
			return EClanFightStatistics.this.copy();
		}

		@Override
		public xbean.EClanFightStatistics toData() {
			return EClanFightStatistics.this.toData();
		}

		public xbean.EClanFightStatistics toBean() {
			return EClanFightStatistics.this.toBean();
		}

		@Override
		public xbean.EClanFightStatistics toDataIf() {
			return EClanFightStatistics.this.toDataIf();
		}

		public xbean.EClanFightStatistics toBeanIf() {
			return EClanFightStatistics.this.toBeanIf();
		}

		@Override
		public int getAct() { // 行动力 by changhao
			_xdb_verify_unsafe_();
			return act;
		}

		@Override
		public int getScore() { // 积分 by changhao
			_xdb_verify_unsafe_();
			return score;
		}

		@Override
		public long getEntertime() { // 进入战场时间 by changhao
			_xdb_verify_unsafe_();
			return entertime;
		}

		@Override
		public int getWinneritemnum() { // 获取胜利宝箱的数量 by changhao
			_xdb_verify_unsafe_();
			return winneritemnum;
		}

		@Override
		public int getClancelebrateitemnum() { // 获取公会庆祝宝箱的数量 by changhao
			_xdb_verify_unsafe_();
			return clancelebrateitemnum;
		}

		@Override
		public int getWinnum() { // 连续胜利的常数 by changhao
			_xdb_verify_unsafe_();
			return winnum;
		}

		@Override
		public long getLastcbattlefieldrankliststamp() { // 上次请求战场积分时间戳 by changhao
			_xdb_verify_unsafe_();
			return lastcbattlefieldrankliststamp;
		}

		@Override
		public long getLastlosestamp() { // 上次战败时间戳 by changhao
			_xdb_verify_unsafe_();
			return lastlosestamp;
		}

		@Override
		public void setAct(int _v_) { // 行动力 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setScore(int _v_) { // 积分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEntertime(long _v_) { // 进入战场时间 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWinneritemnum(int _v_) { // 获取胜利宝箱的数量 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClancelebrateitemnum(int _v_) { // 获取公会庆祝宝箱的数量 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWinnum(int _v_) { // 连续胜利的常数 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastcbattlefieldrankliststamp(long _v_) { // 上次请求战场积分时间戳 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastlosestamp(long _v_) { // 上次战败时间戳 by changhao
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
			return EClanFightStatistics.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return EClanFightStatistics.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return EClanFightStatistics.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return EClanFightStatistics.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return EClanFightStatistics.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return EClanFightStatistics.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return EClanFightStatistics.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return EClanFightStatistics.this.hashCode();
		}

		@Override
		public String toString() {
			return EClanFightStatistics.this.toString();
		}

	}

	public static final class Data implements xbean.EClanFightStatistics {
		private int act; // 行动力 by changhao
		private int score; // 积分 by changhao
		private long entertime; // 进入战场时间 by changhao
		private int winneritemnum; // 获取胜利宝箱的数量 by changhao
		private int clancelebrateitemnum; // 获取公会庆祝宝箱的数量 by changhao
		private int winnum; // 连续胜利的常数 by changhao
		private long lastcbattlefieldrankliststamp; // 上次请求战场积分时间戳 by changhao
		private long lastlosestamp; // 上次战败时间戳 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.EClanFightStatistics _o1_) {
			if (_o1_ instanceof EClanFightStatistics) assign((EClanFightStatistics)_o1_);
			else if (_o1_ instanceof EClanFightStatistics.Data) assign((EClanFightStatistics.Data)_o1_);
			else if (_o1_ instanceof EClanFightStatistics.Const) assign(((EClanFightStatistics.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(EClanFightStatistics _o_) {
			act = _o_.act;
			score = _o_.score;
			entertime = _o_.entertime;
			winneritemnum = _o_.winneritemnum;
			clancelebrateitemnum = _o_.clancelebrateitemnum;
			winnum = _o_.winnum;
			lastcbattlefieldrankliststamp = _o_.lastcbattlefieldrankliststamp;
			lastlosestamp = _o_.lastlosestamp;
		}

		private void assign(EClanFightStatistics.Data _o_) {
			act = _o_.act;
			score = _o_.score;
			entertime = _o_.entertime;
			winneritemnum = _o_.winneritemnum;
			clancelebrateitemnum = _o_.clancelebrateitemnum;
			winnum = _o_.winnum;
			lastcbattlefieldrankliststamp = _o_.lastcbattlefieldrankliststamp;
			lastlosestamp = _o_.lastlosestamp;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(act);
			_os_.marshal(score);
			_os_.marshal(entertime);
			_os_.marshal(winneritemnum);
			_os_.marshal(clancelebrateitemnum);
			_os_.marshal(winnum);
			_os_.marshal(lastcbattlefieldrankliststamp);
			_os_.marshal(lastlosestamp);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			act = _os_.unmarshal_int();
			score = _os_.unmarshal_int();
			entertime = _os_.unmarshal_long();
			winneritemnum = _os_.unmarshal_int();
			clancelebrateitemnum = _os_.unmarshal_int();
			winnum = _os_.unmarshal_int();
			lastcbattlefieldrankliststamp = _os_.unmarshal_long();
			lastlosestamp = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.EClanFightStatistics copy() {
			return new Data(this);
		}

		@Override
		public xbean.EClanFightStatistics toData() {
			return new Data(this);
		}

		public xbean.EClanFightStatistics toBean() {
			return new EClanFightStatistics(this, null, null);
		}

		@Override
		public xbean.EClanFightStatistics toDataIf() {
			return this;
		}

		public xbean.EClanFightStatistics toBeanIf() {
			return new EClanFightStatistics(this, null, null);
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
		public int getAct() { // 行动力 by changhao
			return act;
		}

		@Override
		public int getScore() { // 积分 by changhao
			return score;
		}

		@Override
		public long getEntertime() { // 进入战场时间 by changhao
			return entertime;
		}

		@Override
		public int getWinneritemnum() { // 获取胜利宝箱的数量 by changhao
			return winneritemnum;
		}

		@Override
		public int getClancelebrateitemnum() { // 获取公会庆祝宝箱的数量 by changhao
			return clancelebrateitemnum;
		}

		@Override
		public int getWinnum() { // 连续胜利的常数 by changhao
			return winnum;
		}

		@Override
		public long getLastcbattlefieldrankliststamp() { // 上次请求战场积分时间戳 by changhao
			return lastcbattlefieldrankliststamp;
		}

		@Override
		public long getLastlosestamp() { // 上次战败时间戳 by changhao
			return lastlosestamp;
		}

		@Override
		public void setAct(int _v_) { // 行动力 by changhao
			act = _v_;
		}

		@Override
		public void setScore(int _v_) { // 积分 by changhao
			score = _v_;
		}

		@Override
		public void setEntertime(long _v_) { // 进入战场时间 by changhao
			entertime = _v_;
		}

		@Override
		public void setWinneritemnum(int _v_) { // 获取胜利宝箱的数量 by changhao
			winneritemnum = _v_;
		}

		@Override
		public void setClancelebrateitemnum(int _v_) { // 获取公会庆祝宝箱的数量 by changhao
			clancelebrateitemnum = _v_;
		}

		@Override
		public void setWinnum(int _v_) { // 连续胜利的常数 by changhao
			winnum = _v_;
		}

		@Override
		public void setLastcbattlefieldrankliststamp(long _v_) { // 上次请求战场积分时间戳 by changhao
			lastcbattlefieldrankliststamp = _v_;
		}

		@Override
		public void setLastlosestamp(long _v_) { // 上次战败时间戳 by changhao
			lastlosestamp = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof EClanFightStatistics.Data)) return false;
			EClanFightStatistics.Data _o_ = (EClanFightStatistics.Data) _o1_;
			if (act != _o_.act) return false;
			if (score != _o_.score) return false;
			if (entertime != _o_.entertime) return false;
			if (winneritemnum != _o_.winneritemnum) return false;
			if (clancelebrateitemnum != _o_.clancelebrateitemnum) return false;
			if (winnum != _o_.winnum) return false;
			if (lastcbattlefieldrankliststamp != _o_.lastcbattlefieldrankliststamp) return false;
			if (lastlosestamp != _o_.lastlosestamp) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += act;
			_h_ += score;
			_h_ += entertime;
			_h_ += winneritemnum;
			_h_ += clancelebrateitemnum;
			_h_ += winnum;
			_h_ += lastcbattlefieldrankliststamp;
			_h_ += lastlosestamp;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(act);
			_sb_.append(",");
			_sb_.append(score);
			_sb_.append(",");
			_sb_.append(entertime);
			_sb_.append(",");
			_sb_.append(winneritemnum);
			_sb_.append(",");
			_sb_.append(clancelebrateitemnum);
			_sb_.append(",");
			_sb_.append(winnum);
			_sb_.append(",");
			_sb_.append(lastcbattlefieldrankliststamp);
			_sb_.append(",");
			_sb_.append(lastlosestamp);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
