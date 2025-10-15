
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleZongheRankRecord extends mkdb.XBean implements xbean.RoleZongheRankRecord {
	private int rank; // 排名
	private long roleid; // 人物ID
	private String rolename; // 人物名称
	private int school; // 职业
	private int level; // 等级
	private int score; // 总评分
	private long triggertime; // 触发时间 by changhao

	@Override
	public void _reset_unsafe_() {
		rank = 0;
		roleid = 0L;
		rolename = "";
		school = 0;
		level = 0;
		score = 0;
		triggertime = 0L;
	}

	RoleZongheRankRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		rolename = "";
	}

	public RoleZongheRankRecord() {
		this(0, null, null);
	}

	public RoleZongheRankRecord(RoleZongheRankRecord _o_) {
		this(_o_, null, null);
	}

	RoleZongheRankRecord(xbean.RoleZongheRankRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleZongheRankRecord) assign((RoleZongheRankRecord)_o1_);
		else if (_o1_ instanceof RoleZongheRankRecord.Data) assign((RoleZongheRankRecord.Data)_o1_);
		else if (_o1_ instanceof RoleZongheRankRecord.Const) assign(((RoleZongheRankRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleZongheRankRecord _o_) {
		_o_._xdb_verify_unsafe_();
		rank = _o_.rank;
		roleid = _o_.roleid;
		rolename = _o_.rolename;
		school = _o_.school;
		level = _o_.level;
		score = _o_.score;
		triggertime = _o_.triggertime;
	}

	private void assign(RoleZongheRankRecord.Data _o_) {
		rank = _o_.rank;
		roleid = _o_.roleid;
		rolename = _o_.rolename;
		school = _o_.school;
		level = _o_.level;
		score = _o_.score;
		triggertime = _o_.triggertime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(rank);
		_os_.marshal(roleid);
		_os_.marshal(rolename, mkdb.Const.IO_CHARSET);
		_os_.marshal(school);
		_os_.marshal(level);
		_os_.marshal(score);
		_os_.marshal(triggertime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		rank = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		school = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		score = _os_.unmarshal_int();
		triggertime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RoleZongheRankRecord copy() {
		_xdb_verify_unsafe_();
		return new RoleZongheRankRecord(this);
	}

	@Override
	public xbean.RoleZongheRankRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleZongheRankRecord toBean() {
		_xdb_verify_unsafe_();
		return new RoleZongheRankRecord(this); // same as copy()
	}

	@Override
	public xbean.RoleZongheRankRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleZongheRankRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getRank() { // 排名
		_xdb_verify_unsafe_();
		return rank;
	}

	@Override
	public long getRoleid() { // 人物ID
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public String getRolename() { // 人物名称
		_xdb_verify_unsafe_();
		return rolename;
	}

	@Override
	public com.locojoy.base.Octets getRolenameOctets() { // 人物名称
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getRolename(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getSchool() { // 职业
		_xdb_verify_unsafe_();
		return school;
	}

	@Override
	public int getLevel() { // 等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getScore() { // 总评分
		_xdb_verify_unsafe_();
		return score;
	}

	@Override
	public long getTriggertime() { // 触发时间 by changhao
		_xdb_verify_unsafe_();
		return triggertime;
	}

	@Override
	public void setRank(int _v_) { // 排名
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rank") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rank) {
					public void rollback() { rank = _xdb_saved; }
				};}});
		rank = _v_;
	}

	@Override
	public void setRoleid(long _v_) { // 人物ID
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setRolename(String _v_) { // 人物名称
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rolename") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, rolename) {
					public void rollback() { rolename = _xdb_saved; }
				};}});
		rolename = _v_;
	}

	@Override
	public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 人物名称
		_xdb_verify_unsafe_();
		this.setRolename(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setSchool(int _v_) { // 职业
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "school") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, school) {
					public void rollback() { school = _xdb_saved; }
				};}});
		school = _v_;
	}

	@Override
	public void setLevel(int _v_) { // 等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setScore(int _v_) { // 总评分
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "score") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, score) {
					public void rollback() { score = _xdb_saved; }
				};}});
		score = _v_;
	}

	@Override
	public void setTriggertime(long _v_) { // 触发时间 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "triggertime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, triggertime) {
					public void rollback() { triggertime = _xdb_saved; }
				};}});
		triggertime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleZongheRankRecord _o_ = null;
		if ( _o1_ instanceof RoleZongheRankRecord ) _o_ = (RoleZongheRankRecord)_o1_;
		else if ( _o1_ instanceof RoleZongheRankRecord.Const ) _o_ = ((RoleZongheRankRecord.Const)_o1_).nThis();
		else return false;
		if (rank != _o_.rank) return false;
		if (roleid != _o_.roleid) return false;
		if (!rolename.equals(_o_.rolename)) return false;
		if (school != _o_.school) return false;
		if (level != _o_.level) return false;
		if (score != _o_.score) return false;
		if (triggertime != _o_.triggertime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += rank;
		_h_ += roleid;
		_h_ += rolename.hashCode();
		_h_ += school;
		_h_ += level;
		_h_ += score;
		_h_ += triggertime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rank);
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append("'").append(rolename).append("'");
		_sb_.append(",");
		_sb_.append(school);
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(score);
		_sb_.append(",");
		_sb_.append(triggertime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rank"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rolename"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("school"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("score"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("triggertime"));
		return lb;
	}

	private class Const implements xbean.RoleZongheRankRecord {
		RoleZongheRankRecord nThis() {
			return RoleZongheRankRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleZongheRankRecord copy() {
			return RoleZongheRankRecord.this.copy();
		}

		@Override
		public xbean.RoleZongheRankRecord toData() {
			return RoleZongheRankRecord.this.toData();
		}

		public xbean.RoleZongheRankRecord toBean() {
			return RoleZongheRankRecord.this.toBean();
		}

		@Override
		public xbean.RoleZongheRankRecord toDataIf() {
			return RoleZongheRankRecord.this.toDataIf();
		}

		public xbean.RoleZongheRankRecord toBeanIf() {
			return RoleZongheRankRecord.this.toBeanIf();
		}

		@Override
		public int getRank() { // 排名
			_xdb_verify_unsafe_();
			return rank;
		}

		@Override
		public long getRoleid() { // 人物ID
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public String getRolename() { // 人物名称
			_xdb_verify_unsafe_();
			return rolename;
		}

		@Override
		public com.locojoy.base.Octets getRolenameOctets() { // 人物名称
			_xdb_verify_unsafe_();
			return RoleZongheRankRecord.this.getRolenameOctets();
		}

		@Override
		public int getSchool() { // 职业
			_xdb_verify_unsafe_();
			return school;
		}

		@Override
		public int getLevel() { // 等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getScore() { // 总评分
			_xdb_verify_unsafe_();
			return score;
		}

		@Override
		public long getTriggertime() { // 触发时间 by changhao
			_xdb_verify_unsafe_();
			return triggertime;
		}

		@Override
		public void setRank(int _v_) { // 排名
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // 人物ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolename(String _v_) { // 人物名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 人物名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSchool(int _v_) { // 职业
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setScore(int _v_) { // 总评分
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTriggertime(long _v_) { // 触发时间 by changhao
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
			return RoleZongheRankRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleZongheRankRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleZongheRankRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleZongheRankRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleZongheRankRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleZongheRankRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleZongheRankRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleZongheRankRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleZongheRankRecord.this.toString();
		}

	}

	public static final class Data implements xbean.RoleZongheRankRecord {
		private int rank; // 排名
		private long roleid; // 人物ID
		private String rolename; // 人物名称
		private int school; // 职业
		private int level; // 等级
		private int score; // 总评分
		private long triggertime; // 触发时间 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			rolename = "";
		}

		Data(xbean.RoleZongheRankRecord _o1_) {
			if (_o1_ instanceof RoleZongheRankRecord) assign((RoleZongheRankRecord)_o1_);
			else if (_o1_ instanceof RoleZongheRankRecord.Data) assign((RoleZongheRankRecord.Data)_o1_);
			else if (_o1_ instanceof RoleZongheRankRecord.Const) assign(((RoleZongheRankRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleZongheRankRecord _o_) {
			rank = _o_.rank;
			roleid = _o_.roleid;
			rolename = _o_.rolename;
			school = _o_.school;
			level = _o_.level;
			score = _o_.score;
			triggertime = _o_.triggertime;
		}

		private void assign(RoleZongheRankRecord.Data _o_) {
			rank = _o_.rank;
			roleid = _o_.roleid;
			rolename = _o_.rolename;
			school = _o_.school;
			level = _o_.level;
			score = _o_.score;
			triggertime = _o_.triggertime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(rank);
			_os_.marshal(roleid);
			_os_.marshal(rolename, mkdb.Const.IO_CHARSET);
			_os_.marshal(school);
			_os_.marshal(level);
			_os_.marshal(score);
			_os_.marshal(triggertime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			rank = _os_.unmarshal_int();
			roleid = _os_.unmarshal_long();
			rolename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			school = _os_.unmarshal_int();
			level = _os_.unmarshal_int();
			score = _os_.unmarshal_int();
			triggertime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RoleZongheRankRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleZongheRankRecord toData() {
			return new Data(this);
		}

		public xbean.RoleZongheRankRecord toBean() {
			return new RoleZongheRankRecord(this, null, null);
		}

		@Override
		public xbean.RoleZongheRankRecord toDataIf() {
			return this;
		}

		public xbean.RoleZongheRankRecord toBeanIf() {
			return new RoleZongheRankRecord(this, null, null);
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
		public int getRank() { // 排名
			return rank;
		}

		@Override
		public long getRoleid() { // 人物ID
			return roleid;
		}

		@Override
		public String getRolename() { // 人物名称
			return rolename;
		}

		@Override
		public com.locojoy.base.Octets getRolenameOctets() { // 人物名称
			return com.locojoy.base.Octets.wrap(getRolename(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getSchool() { // 职业
			return school;
		}

		@Override
		public int getLevel() { // 等级
			return level;
		}

		@Override
		public int getScore() { // 总评分
			return score;
		}

		@Override
		public long getTriggertime() { // 触发时间 by changhao
			return triggertime;
		}

		@Override
		public void setRank(int _v_) { // 排名
			rank = _v_;
		}

		@Override
		public void setRoleid(long _v_) { // 人物ID
			roleid = _v_;
		}

		@Override
		public void setRolename(String _v_) { // 人物名称
			if (null == _v_)
				throw new NullPointerException();
			rolename = _v_;
		}

		@Override
		public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 人物名称
			this.setRolename(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setSchool(int _v_) { // 职业
			school = _v_;
		}

		@Override
		public void setLevel(int _v_) { // 等级
			level = _v_;
		}

		@Override
		public void setScore(int _v_) { // 总评分
			score = _v_;
		}

		@Override
		public void setTriggertime(long _v_) { // 触发时间 by changhao
			triggertime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleZongheRankRecord.Data)) return false;
			RoleZongheRankRecord.Data _o_ = (RoleZongheRankRecord.Data) _o1_;
			if (rank != _o_.rank) return false;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (school != _o_.school) return false;
			if (level != _o_.level) return false;
			if (score != _o_.score) return false;
			if (triggertime != _o_.triggertime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += rank;
			_h_ += roleid;
			_h_ += rolename.hashCode();
			_h_ += school;
			_h_ += level;
			_h_ += score;
			_h_ += triggertime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(rank);
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append("'").append(rolename).append("'");
			_sb_.append(",");
			_sb_.append(school);
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(score);
			_sb_.append(",");
			_sb_.append(triggertime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
