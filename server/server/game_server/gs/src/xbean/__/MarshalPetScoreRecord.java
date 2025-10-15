
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MarshalPetScoreRecord extends mkdb.XBean implements xbean.MarshalPetScoreRecord {
	private long roleid; // roleid
	private long uniquepetid; // 宠物的唯一id
	private String nickname; // 名字
	private String petname; // 宠物名字
	private int petgrade; // 宠物评分
	private int rank; // 排名
	private int colour; // 宠物颜色

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		uniquepetid = 0L;
		nickname = "";
		petname = "";
		petgrade = 0;
		rank = 0;
		colour = 0;
	}

	MarshalPetScoreRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		nickname = "";
		petname = "";
	}

	public MarshalPetScoreRecord() {
		this(0, null, null);
	}

	public MarshalPetScoreRecord(MarshalPetScoreRecord _o_) {
		this(_o_, null, null);
	}

	MarshalPetScoreRecord(xbean.MarshalPetScoreRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MarshalPetScoreRecord) assign((MarshalPetScoreRecord)_o1_);
		else if (_o1_ instanceof MarshalPetScoreRecord.Data) assign((MarshalPetScoreRecord.Data)_o1_);
		else if (_o1_ instanceof MarshalPetScoreRecord.Const) assign(((MarshalPetScoreRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MarshalPetScoreRecord _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		uniquepetid = _o_.uniquepetid;
		nickname = _o_.nickname;
		petname = _o_.petname;
		petgrade = _o_.petgrade;
		rank = _o_.rank;
		colour = _o_.colour;
	}

	private void assign(MarshalPetScoreRecord.Data _o_) {
		roleid = _o_.roleid;
		uniquepetid = _o_.uniquepetid;
		nickname = _o_.nickname;
		petname = _o_.petname;
		petgrade = _o_.petgrade;
		rank = _o_.rank;
		colour = _o_.colour;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(uniquepetid);
		_os_.marshal(nickname, mkdb.Const.IO_CHARSET);
		_os_.marshal(petname, mkdb.Const.IO_CHARSET);
		_os_.marshal(petgrade);
		_os_.marshal(rank);
		_os_.marshal(colour);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		uniquepetid = _os_.unmarshal_long();
		nickname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		petname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		petgrade = _os_.unmarshal_int();
		rank = _os_.unmarshal_int();
		colour = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.MarshalPetScoreRecord copy() {
		_xdb_verify_unsafe_();
		return new MarshalPetScoreRecord(this);
	}

	@Override
	public xbean.MarshalPetScoreRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarshalPetScoreRecord toBean() {
		_xdb_verify_unsafe_();
		return new MarshalPetScoreRecord(this); // same as copy()
	}

	@Override
	public xbean.MarshalPetScoreRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarshalPetScoreRecord toBeanIf() {
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
	public long getUniquepetid() { // 宠物的唯一id
		_xdb_verify_unsafe_();
		return uniquepetid;
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
	public String getPetname() { // 宠物名字
		_xdb_verify_unsafe_();
		return petname;
	}

	@Override
	public com.locojoy.base.Octets getPetnameOctets() { // 宠物名字
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPetname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getPetgrade() { // 宠物评分
		_xdb_verify_unsafe_();
		return petgrade;
	}

	@Override
	public int getRank() { // 排名
		_xdb_verify_unsafe_();
		return rank;
	}

	@Override
	public int getColour() { // 宠物颜色
		_xdb_verify_unsafe_();
		return colour;
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
	public void setUniquepetid(long _v_) { // 宠物的唯一id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "uniquepetid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, uniquepetid) {
					public void rollback() { uniquepetid = _xdb_saved; }
				};}});
		uniquepetid = _v_;
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
	public void setPetname(String _v_) { // 宠物名字
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "petname") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, petname) {
					public void rollback() { petname = _xdb_saved; }
				};}});
		petname = _v_;
	}

	@Override
	public void setPetnameOctets(com.locojoy.base.Octets _v_) { // 宠物名字
		_xdb_verify_unsafe_();
		this.setPetname(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setPetgrade(int _v_) { // 宠物评分
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "petgrade") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, petgrade) {
					public void rollback() { petgrade = _xdb_saved; }
				};}});
		petgrade = _v_;
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
	public void setColour(int _v_) { // 宠物颜色
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "colour") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, colour) {
					public void rollback() { colour = _xdb_saved; }
				};}});
		colour = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MarshalPetScoreRecord _o_ = null;
		if ( _o1_ instanceof MarshalPetScoreRecord ) _o_ = (MarshalPetScoreRecord)_o1_;
		else if ( _o1_ instanceof MarshalPetScoreRecord.Const ) _o_ = ((MarshalPetScoreRecord.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (uniquepetid != _o_.uniquepetid) return false;
		if (!nickname.equals(_o_.nickname)) return false;
		if (!petname.equals(_o_.petname)) return false;
		if (petgrade != _o_.petgrade) return false;
		if (rank != _o_.rank) return false;
		if (colour != _o_.colour) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += uniquepetid;
		_h_ += nickname.hashCode();
		_h_ += petname.hashCode();
		_h_ += petgrade;
		_h_ += rank;
		_h_ += colour;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(uniquepetid);
		_sb_.append(",");
		_sb_.append("'").append(nickname).append("'");
		_sb_.append(",");
		_sb_.append("'").append(petname).append("'");
		_sb_.append(",");
		_sb_.append(petgrade);
		_sb_.append(",");
		_sb_.append(rank);
		_sb_.append(",");
		_sb_.append(colour);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("uniquepetid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("nickname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("petname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("petgrade"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rank"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("colour"));
		return lb;
	}

	private class Const implements xbean.MarshalPetScoreRecord {
		MarshalPetScoreRecord nThis() {
			return MarshalPetScoreRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MarshalPetScoreRecord copy() {
			return MarshalPetScoreRecord.this.copy();
		}

		@Override
		public xbean.MarshalPetScoreRecord toData() {
			return MarshalPetScoreRecord.this.toData();
		}

		public xbean.MarshalPetScoreRecord toBean() {
			return MarshalPetScoreRecord.this.toBean();
		}

		@Override
		public xbean.MarshalPetScoreRecord toDataIf() {
			return MarshalPetScoreRecord.this.toDataIf();
		}

		public xbean.MarshalPetScoreRecord toBeanIf() {
			return MarshalPetScoreRecord.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // roleid
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public long getUniquepetid() { // 宠物的唯一id
			_xdb_verify_unsafe_();
			return uniquepetid;
		}

		@Override
		public String getNickname() { // 名字
			_xdb_verify_unsafe_();
			return nickname;
		}

		@Override
		public com.locojoy.base.Octets getNicknameOctets() { // 名字
			_xdb_verify_unsafe_();
			return MarshalPetScoreRecord.this.getNicknameOctets();
		}

		@Override
		public String getPetname() { // 宠物名字
			_xdb_verify_unsafe_();
			return petname;
		}

		@Override
		public com.locojoy.base.Octets getPetnameOctets() { // 宠物名字
			_xdb_verify_unsafe_();
			return MarshalPetScoreRecord.this.getPetnameOctets();
		}

		@Override
		public int getPetgrade() { // 宠物评分
			_xdb_verify_unsafe_();
			return petgrade;
		}

		@Override
		public int getRank() { // 排名
			_xdb_verify_unsafe_();
			return rank;
		}

		@Override
		public int getColour() { // 宠物颜色
			_xdb_verify_unsafe_();
			return colour;
		}

		@Override
		public void setRoleid(long _v_) { // roleid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUniquepetid(long _v_) { // 宠物的唯一id
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
		public void setPetname(String _v_) { // 宠物名字
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPetnameOctets(com.locojoy.base.Octets _v_) { // 宠物名字
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPetgrade(int _v_) { // 宠物评分
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRank(int _v_) { // 排名
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setColour(int _v_) { // 宠物颜色
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
			return MarshalPetScoreRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MarshalPetScoreRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MarshalPetScoreRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MarshalPetScoreRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MarshalPetScoreRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MarshalPetScoreRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MarshalPetScoreRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MarshalPetScoreRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return MarshalPetScoreRecord.this.toString();
		}

	}

	public static final class Data implements xbean.MarshalPetScoreRecord {
		private long roleid; // roleid
		private long uniquepetid; // 宠物的唯一id
		private String nickname; // 名字
		private String petname; // 宠物名字
		private int petgrade; // 宠物评分
		private int rank; // 排名
		private int colour; // 宠物颜色

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			nickname = "";
			petname = "";
		}

		Data(xbean.MarshalPetScoreRecord _o1_) {
			if (_o1_ instanceof MarshalPetScoreRecord) assign((MarshalPetScoreRecord)_o1_);
			else if (_o1_ instanceof MarshalPetScoreRecord.Data) assign((MarshalPetScoreRecord.Data)_o1_);
			else if (_o1_ instanceof MarshalPetScoreRecord.Const) assign(((MarshalPetScoreRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MarshalPetScoreRecord _o_) {
			roleid = _o_.roleid;
			uniquepetid = _o_.uniquepetid;
			nickname = _o_.nickname;
			petname = _o_.petname;
			petgrade = _o_.petgrade;
			rank = _o_.rank;
			colour = _o_.colour;
		}

		private void assign(MarshalPetScoreRecord.Data _o_) {
			roleid = _o_.roleid;
			uniquepetid = _o_.uniquepetid;
			nickname = _o_.nickname;
			petname = _o_.petname;
			petgrade = _o_.petgrade;
			rank = _o_.rank;
			colour = _o_.colour;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(uniquepetid);
			_os_.marshal(nickname, mkdb.Const.IO_CHARSET);
			_os_.marshal(petname, mkdb.Const.IO_CHARSET);
			_os_.marshal(petgrade);
			_os_.marshal(rank);
			_os_.marshal(colour);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			uniquepetid = _os_.unmarshal_long();
			nickname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			petname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			petgrade = _os_.unmarshal_int();
			rank = _os_.unmarshal_int();
			colour = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.MarshalPetScoreRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.MarshalPetScoreRecord toData() {
			return new Data(this);
		}

		public xbean.MarshalPetScoreRecord toBean() {
			return new MarshalPetScoreRecord(this, null, null);
		}

		@Override
		public xbean.MarshalPetScoreRecord toDataIf() {
			return this;
		}

		public xbean.MarshalPetScoreRecord toBeanIf() {
			return new MarshalPetScoreRecord(this, null, null);
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
		public long getUniquepetid() { // 宠物的唯一id
			return uniquepetid;
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
		public String getPetname() { // 宠物名字
			return petname;
		}

		@Override
		public com.locojoy.base.Octets getPetnameOctets() { // 宠物名字
			return com.locojoy.base.Octets.wrap(getPetname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getPetgrade() { // 宠物评分
			return petgrade;
		}

		@Override
		public int getRank() { // 排名
			return rank;
		}

		@Override
		public int getColour() { // 宠物颜色
			return colour;
		}

		@Override
		public void setRoleid(long _v_) { // roleid
			roleid = _v_;
		}

		@Override
		public void setUniquepetid(long _v_) { // 宠物的唯一id
			uniquepetid = _v_;
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
		public void setPetname(String _v_) { // 宠物名字
			if (null == _v_)
				throw new NullPointerException();
			petname = _v_;
		}

		@Override
		public void setPetnameOctets(com.locojoy.base.Octets _v_) { // 宠物名字
			this.setPetname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setPetgrade(int _v_) { // 宠物评分
			petgrade = _v_;
		}

		@Override
		public void setRank(int _v_) { // 排名
			rank = _v_;
		}

		@Override
		public void setColour(int _v_) { // 宠物颜色
			colour = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MarshalPetScoreRecord.Data)) return false;
			MarshalPetScoreRecord.Data _o_ = (MarshalPetScoreRecord.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (uniquepetid != _o_.uniquepetid) return false;
			if (!nickname.equals(_o_.nickname)) return false;
			if (!petname.equals(_o_.petname)) return false;
			if (petgrade != _o_.petgrade) return false;
			if (rank != _o_.rank) return false;
			if (colour != _o_.colour) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += uniquepetid;
			_h_ += nickname.hashCode();
			_h_ += petname.hashCode();
			_h_ += petgrade;
			_h_ += rank;
			_h_ += colour;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(uniquepetid);
			_sb_.append(",");
			_sb_.append("'").append(nickname).append("'");
			_sb_.append(",");
			_sb_.append("'").append(petname).append("'");
			_sb_.append(",");
			_sb_.append(petgrade);
			_sb_.append(",");
			_sb_.append(rank);
			_sb_.append(",");
			_sb_.append(colour);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
