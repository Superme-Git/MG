
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MarshalBingFengRecord extends mkdb.XBean implements xbean.MarshalBingFengRecord {
	private int shool; // 职业
	private int rank; // 排名
	private long roleid; // roleid
	private String rolename; // 玩家的名字
	private int stage; // 关数
	private int rounds; // 总回合数
	private int times; // 耗时

	@Override
	public void _reset_unsafe_() {
		shool = 0;
		rank = 0;
		roleid = 0L;
		rolename = "";
		stage = 0;
		rounds = 0;
		times = 0;
	}

	MarshalBingFengRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		rolename = "";
	}

	public MarshalBingFengRecord() {
		this(0, null, null);
	}

	public MarshalBingFengRecord(MarshalBingFengRecord _o_) {
		this(_o_, null, null);
	}

	MarshalBingFengRecord(xbean.MarshalBingFengRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MarshalBingFengRecord) assign((MarshalBingFengRecord)_o1_);
		else if (_o1_ instanceof MarshalBingFengRecord.Data) assign((MarshalBingFengRecord.Data)_o1_);
		else if (_o1_ instanceof MarshalBingFengRecord.Const) assign(((MarshalBingFengRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MarshalBingFengRecord _o_) {
		_o_._xdb_verify_unsafe_();
		shool = _o_.shool;
		rank = _o_.rank;
		roleid = _o_.roleid;
		rolename = _o_.rolename;
		stage = _o_.stage;
		rounds = _o_.rounds;
		times = _o_.times;
	}

	private void assign(MarshalBingFengRecord.Data _o_) {
		shool = _o_.shool;
		rank = _o_.rank;
		roleid = _o_.roleid;
		rolename = _o_.rolename;
		stage = _o_.stage;
		rounds = _o_.rounds;
		times = _o_.times;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(shool);
		_os_.marshal(rank);
		_os_.marshal(roleid);
		_os_.marshal(rolename, mkdb.Const.IO_CHARSET);
		_os_.marshal(stage);
		_os_.marshal(rounds);
		_os_.marshal(times);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		shool = _os_.unmarshal_int();
		rank = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		stage = _os_.unmarshal_int();
		rounds = _os_.unmarshal_int();
		times = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.MarshalBingFengRecord copy() {
		_xdb_verify_unsafe_();
		return new MarshalBingFengRecord(this);
	}

	@Override
	public xbean.MarshalBingFengRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarshalBingFengRecord toBean() {
		_xdb_verify_unsafe_();
		return new MarshalBingFengRecord(this); // same as copy()
	}

	@Override
	public xbean.MarshalBingFengRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarshalBingFengRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getShool() { // 职业
		_xdb_verify_unsafe_();
		return shool;
	}

	@Override
	public int getRank() { // 排名
		_xdb_verify_unsafe_();
		return rank;
	}

	@Override
	public long getRoleid() { // roleid
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public String getRolename() { // 玩家的名字
		_xdb_verify_unsafe_();
		return rolename;
	}

	@Override
	public com.locojoy.base.Octets getRolenameOctets() { // 玩家的名字
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getRolename(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getStage() { // 关数
		_xdb_verify_unsafe_();
		return stage;
	}

	@Override
	public int getRounds() { // 总回合数
		_xdb_verify_unsafe_();
		return rounds;
	}

	@Override
	public int getTimes() { // 耗时
		_xdb_verify_unsafe_();
		return times;
	}

	@Override
	public void setShool(int _v_) { // 职业
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "shool") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, shool) {
					public void rollback() { shool = _xdb_saved; }
				};}});
		shool = _v_;
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
	public void setRolename(String _v_) { // 玩家的名字
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
	public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 玩家的名字
		_xdb_verify_unsafe_();
		this.setRolename(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setStage(int _v_) { // 关数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "stage") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, stage) {
					public void rollback() { stage = _xdb_saved; }
				};}});
		stage = _v_;
	}

	@Override
	public void setRounds(int _v_) { // 总回合数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rounds") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rounds) {
					public void rollback() { rounds = _xdb_saved; }
				};}});
		rounds = _v_;
	}

	@Override
	public void setTimes(int _v_) { // 耗时
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "times") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, times) {
					public void rollback() { times = _xdb_saved; }
				};}});
		times = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MarshalBingFengRecord _o_ = null;
		if ( _o1_ instanceof MarshalBingFengRecord ) _o_ = (MarshalBingFengRecord)_o1_;
		else if ( _o1_ instanceof MarshalBingFengRecord.Const ) _o_ = ((MarshalBingFengRecord.Const)_o1_).nThis();
		else return false;
		if (shool != _o_.shool) return false;
		if (rank != _o_.rank) return false;
		if (roleid != _o_.roleid) return false;
		if (!rolename.equals(_o_.rolename)) return false;
		if (stage != _o_.stage) return false;
		if (rounds != _o_.rounds) return false;
		if (times != _o_.times) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += shool;
		_h_ += rank;
		_h_ += roleid;
		_h_ += rolename.hashCode();
		_h_ += stage;
		_h_ += rounds;
		_h_ += times;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(shool);
		_sb_.append(",");
		_sb_.append(rank);
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append("'").append(rolename).append("'");
		_sb_.append(",");
		_sb_.append(stage);
		_sb_.append(",");
		_sb_.append(rounds);
		_sb_.append(",");
		_sb_.append(times);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shool"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rank"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rolename"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("stage"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rounds"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("times"));
		return lb;
	}

	private class Const implements xbean.MarshalBingFengRecord {
		MarshalBingFengRecord nThis() {
			return MarshalBingFengRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MarshalBingFengRecord copy() {
			return MarshalBingFengRecord.this.copy();
		}

		@Override
		public xbean.MarshalBingFengRecord toData() {
			return MarshalBingFengRecord.this.toData();
		}

		public xbean.MarshalBingFengRecord toBean() {
			return MarshalBingFengRecord.this.toBean();
		}

		@Override
		public xbean.MarshalBingFengRecord toDataIf() {
			return MarshalBingFengRecord.this.toDataIf();
		}

		public xbean.MarshalBingFengRecord toBeanIf() {
			return MarshalBingFengRecord.this.toBeanIf();
		}

		@Override
		public int getShool() { // 职业
			_xdb_verify_unsafe_();
			return shool;
		}

		@Override
		public int getRank() { // 排名
			_xdb_verify_unsafe_();
			return rank;
		}

		@Override
		public long getRoleid() { // roleid
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public String getRolename() { // 玩家的名字
			_xdb_verify_unsafe_();
			return rolename;
		}

		@Override
		public com.locojoy.base.Octets getRolenameOctets() { // 玩家的名字
			_xdb_verify_unsafe_();
			return MarshalBingFengRecord.this.getRolenameOctets();
		}

		@Override
		public int getStage() { // 关数
			_xdb_verify_unsafe_();
			return stage;
		}

		@Override
		public int getRounds() { // 总回合数
			_xdb_verify_unsafe_();
			return rounds;
		}

		@Override
		public int getTimes() { // 耗时
			_xdb_verify_unsafe_();
			return times;
		}

		@Override
		public void setShool(int _v_) { // 职业
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRank(int _v_) { // 排名
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // roleid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolename(String _v_) { // 玩家的名字
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 玩家的名字
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setStage(int _v_) { // 关数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRounds(int _v_) { // 总回合数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTimes(int _v_) { // 耗时
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
			return MarshalBingFengRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MarshalBingFengRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MarshalBingFengRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MarshalBingFengRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MarshalBingFengRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MarshalBingFengRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MarshalBingFengRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MarshalBingFengRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return MarshalBingFengRecord.this.toString();
		}

	}

	public static final class Data implements xbean.MarshalBingFengRecord {
		private int shool; // 职业
		private int rank; // 排名
		private long roleid; // roleid
		private String rolename; // 玩家的名字
		private int stage; // 关数
		private int rounds; // 总回合数
		private int times; // 耗时

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			rolename = "";
		}

		Data(xbean.MarshalBingFengRecord _o1_) {
			if (_o1_ instanceof MarshalBingFengRecord) assign((MarshalBingFengRecord)_o1_);
			else if (_o1_ instanceof MarshalBingFengRecord.Data) assign((MarshalBingFengRecord.Data)_o1_);
			else if (_o1_ instanceof MarshalBingFengRecord.Const) assign(((MarshalBingFengRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MarshalBingFengRecord _o_) {
			shool = _o_.shool;
			rank = _o_.rank;
			roleid = _o_.roleid;
			rolename = _o_.rolename;
			stage = _o_.stage;
			rounds = _o_.rounds;
			times = _o_.times;
		}

		private void assign(MarshalBingFengRecord.Data _o_) {
			shool = _o_.shool;
			rank = _o_.rank;
			roleid = _o_.roleid;
			rolename = _o_.rolename;
			stage = _o_.stage;
			rounds = _o_.rounds;
			times = _o_.times;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(shool);
			_os_.marshal(rank);
			_os_.marshal(roleid);
			_os_.marshal(rolename, mkdb.Const.IO_CHARSET);
			_os_.marshal(stage);
			_os_.marshal(rounds);
			_os_.marshal(times);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			shool = _os_.unmarshal_int();
			rank = _os_.unmarshal_int();
			roleid = _os_.unmarshal_long();
			rolename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			stage = _os_.unmarshal_int();
			rounds = _os_.unmarshal_int();
			times = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.MarshalBingFengRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.MarshalBingFengRecord toData() {
			return new Data(this);
		}

		public xbean.MarshalBingFengRecord toBean() {
			return new MarshalBingFengRecord(this, null, null);
		}

		@Override
		public xbean.MarshalBingFengRecord toDataIf() {
			return this;
		}

		public xbean.MarshalBingFengRecord toBeanIf() {
			return new MarshalBingFengRecord(this, null, null);
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
		public int getShool() { // 职业
			return shool;
		}

		@Override
		public int getRank() { // 排名
			return rank;
		}

		@Override
		public long getRoleid() { // roleid
			return roleid;
		}

		@Override
		public String getRolename() { // 玩家的名字
			return rolename;
		}

		@Override
		public com.locojoy.base.Octets getRolenameOctets() { // 玩家的名字
			return com.locojoy.base.Octets.wrap(getRolename(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getStage() { // 关数
			return stage;
		}

		@Override
		public int getRounds() { // 总回合数
			return rounds;
		}

		@Override
		public int getTimes() { // 耗时
			return times;
		}

		@Override
		public void setShool(int _v_) { // 职业
			shool = _v_;
		}

		@Override
		public void setRank(int _v_) { // 排名
			rank = _v_;
		}

		@Override
		public void setRoleid(long _v_) { // roleid
			roleid = _v_;
		}

		@Override
		public void setRolename(String _v_) { // 玩家的名字
			if (null == _v_)
				throw new NullPointerException();
			rolename = _v_;
		}

		@Override
		public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 玩家的名字
			this.setRolename(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setStage(int _v_) { // 关数
			stage = _v_;
		}

		@Override
		public void setRounds(int _v_) { // 总回合数
			rounds = _v_;
		}

		@Override
		public void setTimes(int _v_) { // 耗时
			times = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MarshalBingFengRecord.Data)) return false;
			MarshalBingFengRecord.Data _o_ = (MarshalBingFengRecord.Data) _o1_;
			if (shool != _o_.shool) return false;
			if (rank != _o_.rank) return false;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (stage != _o_.stage) return false;
			if (rounds != _o_.rounds) return false;
			if (times != _o_.times) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += shool;
			_h_ += rank;
			_h_ += roleid;
			_h_ += rolename.hashCode();
			_h_ += stage;
			_h_ += rounds;
			_h_ += times;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(shool);
			_sb_.append(",");
			_sb_.append(rank);
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append("'").append(rolename).append("'");
			_sb_.append(",");
			_sb_.append(stage);
			_sb_.append(",");
			_sb_.append(rounds);
			_sb_.append(",");
			_sb_.append(times);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
