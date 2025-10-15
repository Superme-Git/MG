
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MarshalApprenticeRecord extends mkdb.XBean implements xbean.MarshalApprenticeRecord {
	private long roleid; // roleid
	private String nickname; // 名字
	private int level; // 等级
	private int schoolid; // 职业id
	private int apprenticenum; // 出徒数量
	private int rank; // 排名

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		nickname = "";
		level = 0;
		schoolid = 0;
		apprenticenum = 0;
		rank = 0;
	}

	MarshalApprenticeRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		nickname = "";
	}

	public MarshalApprenticeRecord() {
		this(0, null, null);
	}

	public MarshalApprenticeRecord(MarshalApprenticeRecord _o_) {
		this(_o_, null, null);
	}

	MarshalApprenticeRecord(xbean.MarshalApprenticeRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MarshalApprenticeRecord) assign((MarshalApprenticeRecord)_o1_);
		else if (_o1_ instanceof MarshalApprenticeRecord.Data) assign((MarshalApprenticeRecord.Data)_o1_);
		else if (_o1_ instanceof MarshalApprenticeRecord.Const) assign(((MarshalApprenticeRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MarshalApprenticeRecord _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		nickname = _o_.nickname;
		level = _o_.level;
		schoolid = _o_.schoolid;
		apprenticenum = _o_.apprenticenum;
		rank = _o_.rank;
	}

	private void assign(MarshalApprenticeRecord.Data _o_) {
		roleid = _o_.roleid;
		nickname = _o_.nickname;
		level = _o_.level;
		schoolid = _o_.schoolid;
		apprenticenum = _o_.apprenticenum;
		rank = _o_.rank;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(nickname, mkdb.Const.IO_CHARSET);
		_os_.marshal(level);
		_os_.marshal(schoolid);
		_os_.marshal(apprenticenum);
		_os_.marshal(rank);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		nickname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		level = _os_.unmarshal_int();
		schoolid = _os_.unmarshal_int();
		apprenticenum = _os_.unmarshal_int();
		rank = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.MarshalApprenticeRecord copy() {
		_xdb_verify_unsafe_();
		return new MarshalApprenticeRecord(this);
	}

	@Override
	public xbean.MarshalApprenticeRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarshalApprenticeRecord toBean() {
		_xdb_verify_unsafe_();
		return new MarshalApprenticeRecord(this); // same as copy()
	}

	@Override
	public xbean.MarshalApprenticeRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarshalApprenticeRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // roleid
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public String getNickname() { // 名字
		_xdb_verify_unsafe_();
		return nickname;
	}

	@Override
	public com.locojoy.base.Octets getNicknameOctets() { // 名字
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getNickname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getLevel() { // 等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getSchoolid() { // 职业id
		_xdb_verify_unsafe_();
		return schoolid;
	}

	@Override
	public int getApprenticenum() { // 出徒数量
		_xdb_verify_unsafe_();
		return apprenticenum;
	}

	@Override
	public int getRank() { // 排名
		_xdb_verify_unsafe_();
		return rank;
	}

	@Override
	public void setRoleid(long _v_) { // roleid
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setNickname(String _v_) { // 名字
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "nickname") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, nickname) {
					public void rollback() { nickname = _xdb_saved; }
				};}});
		nickname = _v_;
	}

	@Override
	public void setNicknameOctets(com.locojoy.base.Octets _v_) { // 名字
		_xdb_verify_unsafe_();
		this.setNickname(_v_.getString(mkdb.Const.IO_CHARSET));
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
	public void setSchoolid(int _v_) { // 职业id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "schoolid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, schoolid) {
					public void rollback() { schoolid = _xdb_saved; }
				};}});
		schoolid = _v_;
	}

	@Override
	public void setApprenticenum(int _v_) { // 出徒数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "apprenticenum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, apprenticenum) {
					public void rollback() { apprenticenum = _xdb_saved; }
				};}});
		apprenticenum = _v_;
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
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MarshalApprenticeRecord _o_ = null;
		if ( _o1_ instanceof MarshalApprenticeRecord ) _o_ = (MarshalApprenticeRecord)_o1_;
		else if ( _o1_ instanceof MarshalApprenticeRecord.Const ) _o_ = ((MarshalApprenticeRecord.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (!nickname.equals(_o_.nickname)) return false;
		if (level != _o_.level) return false;
		if (schoolid != _o_.schoolid) return false;
		if (apprenticenum != _o_.apprenticenum) return false;
		if (rank != _o_.rank) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += nickname.hashCode();
		_h_ += level;
		_h_ += schoolid;
		_h_ += apprenticenum;
		_h_ += rank;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append("'").append(nickname).append("'");
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(schoolid);
		_sb_.append(",");
		_sb_.append(apprenticenum);
		_sb_.append(",");
		_sb_.append(rank);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("nickname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("schoolid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("apprenticenum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rank"));
		return lb;
	}

	private class Const implements xbean.MarshalApprenticeRecord {
		MarshalApprenticeRecord nThis() {
			return MarshalApprenticeRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MarshalApprenticeRecord copy() {
			return MarshalApprenticeRecord.this.copy();
		}

		@Override
		public xbean.MarshalApprenticeRecord toData() {
			return MarshalApprenticeRecord.this.toData();
		}

		public xbean.MarshalApprenticeRecord toBean() {
			return MarshalApprenticeRecord.this.toBean();
		}

		@Override
		public xbean.MarshalApprenticeRecord toDataIf() {
			return MarshalApprenticeRecord.this.toDataIf();
		}

		public xbean.MarshalApprenticeRecord toBeanIf() {
			return MarshalApprenticeRecord.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // roleid
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public String getNickname() { // 名字
			_xdb_verify_unsafe_();
			return nickname;
		}

		@Override
		public com.locojoy.base.Octets getNicknameOctets() { // 名字
			_xdb_verify_unsafe_();
			return MarshalApprenticeRecord.this.getNicknameOctets();
		}

		@Override
		public int getLevel() { // 等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getSchoolid() { // 职业id
			_xdb_verify_unsafe_();
			return schoolid;
		}

		@Override
		public int getApprenticenum() { // 出徒数量
			_xdb_verify_unsafe_();
			return apprenticenum;
		}

		@Override
		public int getRank() { // 排名
			_xdb_verify_unsafe_();
			return rank;
		}

		@Override
		public void setRoleid(long _v_) { // roleid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNickname(String _v_) { // 名字
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNicknameOctets(com.locojoy.base.Octets _v_) { // 名字
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSchoolid(int _v_) { // 职业id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setApprenticenum(int _v_) { // 出徒数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRank(int _v_) { // 排名
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
			return MarshalApprenticeRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MarshalApprenticeRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MarshalApprenticeRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MarshalApprenticeRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MarshalApprenticeRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MarshalApprenticeRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MarshalApprenticeRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MarshalApprenticeRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return MarshalApprenticeRecord.this.toString();
		}

	}

	public static final class Data implements xbean.MarshalApprenticeRecord {
		private long roleid; // roleid
		private String nickname; // 名字
		private int level; // 等级
		private int schoolid; // 职业id
		private int apprenticenum; // 出徒数量
		private int rank; // 排名

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			nickname = "";
		}

		Data(xbean.MarshalApprenticeRecord _o1_) {
			if (_o1_ instanceof MarshalApprenticeRecord) assign((MarshalApprenticeRecord)_o1_);
			else if (_o1_ instanceof MarshalApprenticeRecord.Data) assign((MarshalApprenticeRecord.Data)_o1_);
			else if (_o1_ instanceof MarshalApprenticeRecord.Const) assign(((MarshalApprenticeRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MarshalApprenticeRecord _o_) {
			roleid = _o_.roleid;
			nickname = _o_.nickname;
			level = _o_.level;
			schoolid = _o_.schoolid;
			apprenticenum = _o_.apprenticenum;
			rank = _o_.rank;
		}

		private void assign(MarshalApprenticeRecord.Data _o_) {
			roleid = _o_.roleid;
			nickname = _o_.nickname;
			level = _o_.level;
			schoolid = _o_.schoolid;
			apprenticenum = _o_.apprenticenum;
			rank = _o_.rank;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(nickname, mkdb.Const.IO_CHARSET);
			_os_.marshal(level);
			_os_.marshal(schoolid);
			_os_.marshal(apprenticenum);
			_os_.marshal(rank);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			nickname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			level = _os_.unmarshal_int();
			schoolid = _os_.unmarshal_int();
			apprenticenum = _os_.unmarshal_int();
			rank = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.MarshalApprenticeRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.MarshalApprenticeRecord toData() {
			return new Data(this);
		}

		public xbean.MarshalApprenticeRecord toBean() {
			return new MarshalApprenticeRecord(this, null, null);
		}

		@Override
		public xbean.MarshalApprenticeRecord toDataIf() {
			return this;
		}

		public xbean.MarshalApprenticeRecord toBeanIf() {
			return new MarshalApprenticeRecord(this, null, null);
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
		public long getRoleid() { // roleid
			return roleid;
		}

		@Override
		public String getNickname() { // 名字
			return nickname;
		}

		@Override
		public com.locojoy.base.Octets getNicknameOctets() { // 名字
			return com.locojoy.base.Octets.wrap(getNickname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getLevel() { // 等级
			return level;
		}

		@Override
		public int getSchoolid() { // 职业id
			return schoolid;
		}

		@Override
		public int getApprenticenum() { // 出徒数量
			return apprenticenum;
		}

		@Override
		public int getRank() { // 排名
			return rank;
		}

		@Override
		public void setRoleid(long _v_) { // roleid
			roleid = _v_;
		}

		@Override
		public void setNickname(String _v_) { // 名字
			if (null == _v_)
				throw new NullPointerException();
			nickname = _v_;
		}

		@Override
		public void setNicknameOctets(com.locojoy.base.Octets _v_) { // 名字
			this.setNickname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setLevel(int _v_) { // 等级
			level = _v_;
		}

		@Override
		public void setSchoolid(int _v_) { // 职业id
			schoolid = _v_;
		}

		@Override
		public void setApprenticenum(int _v_) { // 出徒数量
			apprenticenum = _v_;
		}

		@Override
		public void setRank(int _v_) { // 排名
			rank = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MarshalApprenticeRecord.Data)) return false;
			MarshalApprenticeRecord.Data _o_ = (MarshalApprenticeRecord.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (!nickname.equals(_o_.nickname)) return false;
			if (level != _o_.level) return false;
			if (schoolid != _o_.schoolid) return false;
			if (apprenticenum != _o_.apprenticenum) return false;
			if (rank != _o_.rank) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += nickname.hashCode();
			_h_ += level;
			_h_ += schoolid;
			_h_ += apprenticenum;
			_h_ += rank;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append("'").append(nickname).append("'");
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(schoolid);
			_sb_.append(",");
			_sb_.append(apprenticenum);
			_sb_.append(",");
			_sb_.append(rank);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
