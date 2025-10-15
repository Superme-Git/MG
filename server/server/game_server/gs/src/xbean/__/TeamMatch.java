
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TeamMatch extends mkdb.XBean implements xbean.TeamMatch {
	private long roleid; // 角色ID by changhao
	private int matchtype; // 类型0是个人1是队伍 by changhao
	private int targetid; // 目标ID by changhao
	private int levelmin; // 需要最小等级 个人匹配这个忽略 by changhao
	private int levelmax; // 需要最大等级 个人匹配这个忽略 by changhao
	private long onekeytimestamp; // 一键喊话时间戳 by changhao
	private long timestamp; // 匹配后的时间戳 by changhao

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		matchtype = 0;
		targetid = 0;
		levelmin = 0;
		levelmax = 0;
		onekeytimestamp = 0L;
		timestamp = 0L;
	}

	TeamMatch(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public TeamMatch() {
		this(0, null, null);
	}

	public TeamMatch(TeamMatch _o_) {
		this(_o_, null, null);
	}

	TeamMatch(xbean.TeamMatch _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TeamMatch) assign((TeamMatch)_o1_);
		else if (_o1_ instanceof TeamMatch.Data) assign((TeamMatch.Data)_o1_);
		else if (_o1_ instanceof TeamMatch.Const) assign(((TeamMatch.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TeamMatch _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		matchtype = _o_.matchtype;
		targetid = _o_.targetid;
		levelmin = _o_.levelmin;
		levelmax = _o_.levelmax;
		onekeytimestamp = _o_.onekeytimestamp;
		timestamp = _o_.timestamp;
	}

	private void assign(TeamMatch.Data _o_) {
		roleid = _o_.roleid;
		matchtype = _o_.matchtype;
		targetid = _o_.targetid;
		levelmin = _o_.levelmin;
		levelmax = _o_.levelmax;
		onekeytimestamp = _o_.onekeytimestamp;
		timestamp = _o_.timestamp;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(matchtype);
		_os_.marshal(targetid);
		_os_.marshal(levelmin);
		_os_.marshal(levelmax);
		_os_.marshal(onekeytimestamp);
		_os_.marshal(timestamp);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		matchtype = _os_.unmarshal_int();
		targetid = _os_.unmarshal_int();
		levelmin = _os_.unmarshal_int();
		levelmax = _os_.unmarshal_int();
		onekeytimestamp = _os_.unmarshal_long();
		timestamp = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.TeamMatch copy() {
		_xdb_verify_unsafe_();
		return new TeamMatch(this);
	}

	@Override
	public xbean.TeamMatch toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TeamMatch toBean() {
		_xdb_verify_unsafe_();
		return new TeamMatch(this); // same as copy()
	}

	@Override
	public xbean.TeamMatch toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TeamMatch toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // 角色ID by changhao
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public int getMatchtype() { // 类型0是个人1是队伍 by changhao
		_xdb_verify_unsafe_();
		return matchtype;
	}

	@Override
	public int getTargetid() { // 目标ID by changhao
		_xdb_verify_unsafe_();
		return targetid;
	}

	@Override
	public int getLevelmin() { // 需要最小等级 个人匹配这个忽略 by changhao
		_xdb_verify_unsafe_();
		return levelmin;
	}

	@Override
	public int getLevelmax() { // 需要最大等级 个人匹配这个忽略 by changhao
		_xdb_verify_unsafe_();
		return levelmax;
	}

	@Override
	public long getOnekeytimestamp() { // 一键喊话时间戳 by changhao
		_xdb_verify_unsafe_();
		return onekeytimestamp;
	}

	@Override
	public long getTimestamp() { // 匹配后的时间戳 by changhao
		_xdb_verify_unsafe_();
		return timestamp;
	}

	@Override
	public void setRoleid(long _v_) { // 角色ID by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setMatchtype(int _v_) { // 类型0是个人1是队伍 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "matchtype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, matchtype) {
					public void rollback() { matchtype = _xdb_saved; }
				};}});
		matchtype = _v_;
	}

	@Override
	public void setTargetid(int _v_) { // 目标ID by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "targetid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, targetid) {
					public void rollback() { targetid = _xdb_saved; }
				};}});
		targetid = _v_;
	}

	@Override
	public void setLevelmin(int _v_) { // 需要最小等级 个人匹配这个忽略 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "levelmin") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, levelmin) {
					public void rollback() { levelmin = _xdb_saved; }
				};}});
		levelmin = _v_;
	}

	@Override
	public void setLevelmax(int _v_) { // 需要最大等级 个人匹配这个忽略 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "levelmax") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, levelmax) {
					public void rollback() { levelmax = _xdb_saved; }
				};}});
		levelmax = _v_;
	}

	@Override
	public void setOnekeytimestamp(long _v_) { // 一键喊话时间戳 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "onekeytimestamp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, onekeytimestamp) {
					public void rollback() { onekeytimestamp = _xdb_saved; }
				};}});
		onekeytimestamp = _v_;
	}

	@Override
	public void setTimestamp(long _v_) { // 匹配后的时间戳 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "timestamp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, timestamp) {
					public void rollback() { timestamp = _xdb_saved; }
				};}});
		timestamp = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TeamMatch _o_ = null;
		if ( _o1_ instanceof TeamMatch ) _o_ = (TeamMatch)_o1_;
		else if ( _o1_ instanceof TeamMatch.Const ) _o_ = ((TeamMatch.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (matchtype != _o_.matchtype) return false;
		if (targetid != _o_.targetid) return false;
		if (levelmin != _o_.levelmin) return false;
		if (levelmax != _o_.levelmax) return false;
		if (onekeytimestamp != _o_.onekeytimestamp) return false;
		if (timestamp != _o_.timestamp) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += matchtype;
		_h_ += targetid;
		_h_ += levelmin;
		_h_ += levelmax;
		_h_ += onekeytimestamp;
		_h_ += timestamp;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(matchtype);
		_sb_.append(",");
		_sb_.append(targetid);
		_sb_.append(",");
		_sb_.append(levelmin);
		_sb_.append(",");
		_sb_.append(levelmax);
		_sb_.append(",");
		_sb_.append(onekeytimestamp);
		_sb_.append(",");
		_sb_.append(timestamp);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("matchtype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("targetid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("levelmin"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("levelmax"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("onekeytimestamp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("timestamp"));
		return lb;
	}

	private class Const implements xbean.TeamMatch {
		TeamMatch nThis() {
			return TeamMatch.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TeamMatch copy() {
			return TeamMatch.this.copy();
		}

		@Override
		public xbean.TeamMatch toData() {
			return TeamMatch.this.toData();
		}

		public xbean.TeamMatch toBean() {
			return TeamMatch.this.toBean();
		}

		@Override
		public xbean.TeamMatch toDataIf() {
			return TeamMatch.this.toDataIf();
		}

		public xbean.TeamMatch toBeanIf() {
			return TeamMatch.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 角色ID by changhao
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getMatchtype() { // 类型0是个人1是队伍 by changhao
			_xdb_verify_unsafe_();
			return matchtype;
		}

		@Override
		public int getTargetid() { // 目标ID by changhao
			_xdb_verify_unsafe_();
			return targetid;
		}

		@Override
		public int getLevelmin() { // 需要最小等级 个人匹配这个忽略 by changhao
			_xdb_verify_unsafe_();
			return levelmin;
		}

		@Override
		public int getLevelmax() { // 需要最大等级 个人匹配这个忽略 by changhao
			_xdb_verify_unsafe_();
			return levelmax;
		}

		@Override
		public long getOnekeytimestamp() { // 一键喊话时间戳 by changhao
			_xdb_verify_unsafe_();
			return onekeytimestamp;
		}

		@Override
		public long getTimestamp() { // 匹配后的时间戳 by changhao
			_xdb_verify_unsafe_();
			return timestamp;
		}

		@Override
		public void setRoleid(long _v_) { // 角色ID by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMatchtype(int _v_) { // 类型0是个人1是队伍 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTargetid(int _v_) { // 目标ID by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevelmin(int _v_) { // 需要最小等级 个人匹配这个忽略 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevelmax(int _v_) { // 需要最大等级 个人匹配这个忽略 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOnekeytimestamp(long _v_) { // 一键喊话时间戳 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTimestamp(long _v_) { // 匹配后的时间戳 by changhao
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
			return TeamMatch.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TeamMatch.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TeamMatch.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TeamMatch.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TeamMatch.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TeamMatch.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TeamMatch.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TeamMatch.this.hashCode();
		}

		@Override
		public String toString() {
			return TeamMatch.this.toString();
		}

	}

	public static final class Data implements xbean.TeamMatch {
		private long roleid; // 角色ID by changhao
		private int matchtype; // 类型0是个人1是队伍 by changhao
		private int targetid; // 目标ID by changhao
		private int levelmin; // 需要最小等级 个人匹配这个忽略 by changhao
		private int levelmax; // 需要最大等级 个人匹配这个忽略 by changhao
		private long onekeytimestamp; // 一键喊话时间戳 by changhao
		private long timestamp; // 匹配后的时间戳 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.TeamMatch _o1_) {
			if (_o1_ instanceof TeamMatch) assign((TeamMatch)_o1_);
			else if (_o1_ instanceof TeamMatch.Data) assign((TeamMatch.Data)_o1_);
			else if (_o1_ instanceof TeamMatch.Const) assign(((TeamMatch.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TeamMatch _o_) {
			roleid = _o_.roleid;
			matchtype = _o_.matchtype;
			targetid = _o_.targetid;
			levelmin = _o_.levelmin;
			levelmax = _o_.levelmax;
			onekeytimestamp = _o_.onekeytimestamp;
			timestamp = _o_.timestamp;
		}

		private void assign(TeamMatch.Data _o_) {
			roleid = _o_.roleid;
			matchtype = _o_.matchtype;
			targetid = _o_.targetid;
			levelmin = _o_.levelmin;
			levelmax = _o_.levelmax;
			onekeytimestamp = _o_.onekeytimestamp;
			timestamp = _o_.timestamp;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(matchtype);
			_os_.marshal(targetid);
			_os_.marshal(levelmin);
			_os_.marshal(levelmax);
			_os_.marshal(onekeytimestamp);
			_os_.marshal(timestamp);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			matchtype = _os_.unmarshal_int();
			targetid = _os_.unmarshal_int();
			levelmin = _os_.unmarshal_int();
			levelmax = _os_.unmarshal_int();
			onekeytimestamp = _os_.unmarshal_long();
			timestamp = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.TeamMatch copy() {
			return new Data(this);
		}

		@Override
		public xbean.TeamMatch toData() {
			return new Data(this);
		}

		public xbean.TeamMatch toBean() {
			return new TeamMatch(this, null, null);
		}

		@Override
		public xbean.TeamMatch toDataIf() {
			return this;
		}

		public xbean.TeamMatch toBeanIf() {
			return new TeamMatch(this, null, null);
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
		public long getRoleid() { // 角色ID by changhao
			return roleid;
		}

		@Override
		public int getMatchtype() { // 类型0是个人1是队伍 by changhao
			return matchtype;
		}

		@Override
		public int getTargetid() { // 目标ID by changhao
			return targetid;
		}

		@Override
		public int getLevelmin() { // 需要最小等级 个人匹配这个忽略 by changhao
			return levelmin;
		}

		@Override
		public int getLevelmax() { // 需要最大等级 个人匹配这个忽略 by changhao
			return levelmax;
		}

		@Override
		public long getOnekeytimestamp() { // 一键喊话时间戳 by changhao
			return onekeytimestamp;
		}

		@Override
		public long getTimestamp() { // 匹配后的时间戳 by changhao
			return timestamp;
		}

		@Override
		public void setRoleid(long _v_) { // 角色ID by changhao
			roleid = _v_;
		}

		@Override
		public void setMatchtype(int _v_) { // 类型0是个人1是队伍 by changhao
			matchtype = _v_;
		}

		@Override
		public void setTargetid(int _v_) { // 目标ID by changhao
			targetid = _v_;
		}

		@Override
		public void setLevelmin(int _v_) { // 需要最小等级 个人匹配这个忽略 by changhao
			levelmin = _v_;
		}

		@Override
		public void setLevelmax(int _v_) { // 需要最大等级 个人匹配这个忽略 by changhao
			levelmax = _v_;
		}

		@Override
		public void setOnekeytimestamp(long _v_) { // 一键喊话时间戳 by changhao
			onekeytimestamp = _v_;
		}

		@Override
		public void setTimestamp(long _v_) { // 匹配后的时间戳 by changhao
			timestamp = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TeamMatch.Data)) return false;
			TeamMatch.Data _o_ = (TeamMatch.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (matchtype != _o_.matchtype) return false;
			if (targetid != _o_.targetid) return false;
			if (levelmin != _o_.levelmin) return false;
			if (levelmax != _o_.levelmax) return false;
			if (onekeytimestamp != _o_.onekeytimestamp) return false;
			if (timestamp != _o_.timestamp) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += matchtype;
			_h_ += targetid;
			_h_ += levelmin;
			_h_ += levelmax;
			_h_ += onekeytimestamp;
			_h_ += timestamp;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(matchtype);
			_sb_.append(",");
			_sb_.append(targetid);
			_sb_.append(",");
			_sb_.append(levelmin);
			_sb_.append(",");
			_sb_.append(levelmax);
			_sb_.append(",");
			_sb_.append(onekeytimestamp);
			_sb_.append(",");
			_sb_.append(timestamp);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
