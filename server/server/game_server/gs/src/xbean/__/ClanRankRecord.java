
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanRankRecord extends mkdb.XBean implements xbean.ClanRankRecord {
	private int rank; // 排名
	private long clankey; // 公会表唯一键值
	private String clanname; // 公会名称
	private int level; // 公会等级
	private int membernum; // 公会成员数
	private int hotel; // 旅馆等级 by changhao
	private long triggertime; // 触发时间 by changhao

	@Override
	public void _reset_unsafe_() {
		rank = 0;
		clankey = 0L;
		clanname = "";
		level = 0;
		membernum = 0;
		hotel = 0;
		triggertime = 0L;
	}

	ClanRankRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		clanname = "";
	}

	public ClanRankRecord() {
		this(0, null, null);
	}

	public ClanRankRecord(ClanRankRecord _o_) {
		this(_o_, null, null);
	}

	ClanRankRecord(xbean.ClanRankRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanRankRecord) assign((ClanRankRecord)_o1_);
		else if (_o1_ instanceof ClanRankRecord.Data) assign((ClanRankRecord.Data)_o1_);
		else if (_o1_ instanceof ClanRankRecord.Const) assign(((ClanRankRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanRankRecord _o_) {
		_o_._xdb_verify_unsafe_();
		rank = _o_.rank;
		clankey = _o_.clankey;
		clanname = _o_.clanname;
		level = _o_.level;
		membernum = _o_.membernum;
		hotel = _o_.hotel;
		triggertime = _o_.triggertime;
	}

	private void assign(ClanRankRecord.Data _o_) {
		rank = _o_.rank;
		clankey = _o_.clankey;
		clanname = _o_.clanname;
		level = _o_.level;
		membernum = _o_.membernum;
		hotel = _o_.hotel;
		triggertime = _o_.triggertime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(rank);
		_os_.marshal(clankey);
		_os_.marshal(clanname, mkdb.Const.IO_CHARSET);
		_os_.marshal(level);
		_os_.marshal(membernum);
		_os_.marshal(hotel);
		_os_.marshal(triggertime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		rank = _os_.unmarshal_int();
		clankey = _os_.unmarshal_long();
		clanname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		level = _os_.unmarshal_int();
		membernum = _os_.unmarshal_int();
		hotel = _os_.unmarshal_int();
		triggertime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ClanRankRecord copy() {
		_xdb_verify_unsafe_();
		return new ClanRankRecord(this);
	}

	@Override
	public xbean.ClanRankRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanRankRecord toBean() {
		_xdb_verify_unsafe_();
		return new ClanRankRecord(this); // same as copy()
	}

	@Override
	public xbean.ClanRankRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanRankRecord toBeanIf() {
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
	public long getClankey() { // 公会表唯一键值
		_xdb_verify_unsafe_();
		return clankey;
	}

	@Override
	public String getClanname() { // 公会名称
		_xdb_verify_unsafe_();
		return clanname;
	}

	@Override
	public com.locojoy.base.Octets getClannameOctets() { // 公会名称
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getClanname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getLevel() { // 公会等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getMembernum() { // 公会成员数
		_xdb_verify_unsafe_();
		return membernum;
	}

	@Override
	public int getHotel() { // 旅馆等级 by changhao
		_xdb_verify_unsafe_();
		return hotel;
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
	public void setClankey(long _v_) { // 公会表唯一键值
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clankey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clankey) {
					public void rollback() { clankey = _xdb_saved; }
				};}});
		clankey = _v_;
	}

	@Override
	public void setClanname(String _v_) { // 公会名称
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanname") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, clanname) {
					public void rollback() { clanname = _xdb_saved; }
				};}});
		clanname = _v_;
	}

	@Override
	public void setClannameOctets(com.locojoy.base.Octets _v_) { // 公会名称
		_xdb_verify_unsafe_();
		this.setClanname(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setLevel(int _v_) { // 公会等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setMembernum(int _v_) { // 公会成员数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "membernum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, membernum) {
					public void rollback() { membernum = _xdb_saved; }
				};}});
		membernum = _v_;
	}

	@Override
	public void setHotel(int _v_) { // 旅馆等级 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "hotel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, hotel) {
					public void rollback() { hotel = _xdb_saved; }
				};}});
		hotel = _v_;
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
		ClanRankRecord _o_ = null;
		if ( _o1_ instanceof ClanRankRecord ) _o_ = (ClanRankRecord)_o1_;
		else if ( _o1_ instanceof ClanRankRecord.Const ) _o_ = ((ClanRankRecord.Const)_o1_).nThis();
		else return false;
		if (rank != _o_.rank) return false;
		if (clankey != _o_.clankey) return false;
		if (!clanname.equals(_o_.clanname)) return false;
		if (level != _o_.level) return false;
		if (membernum != _o_.membernum) return false;
		if (hotel != _o_.hotel) return false;
		if (triggertime != _o_.triggertime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += rank;
		_h_ += clankey;
		_h_ += clanname.hashCode();
		_h_ += level;
		_h_ += membernum;
		_h_ += hotel;
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
		_sb_.append(clankey);
		_sb_.append(",");
		_sb_.append("'").append(clanname).append("'");
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(membernum);
		_sb_.append(",");
		_sb_.append(hotel);
		_sb_.append(",");
		_sb_.append(triggertime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rank"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clankey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("membernum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("hotel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("triggertime"));
		return lb;
	}

	private class Const implements xbean.ClanRankRecord {
		ClanRankRecord nThis() {
			return ClanRankRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanRankRecord copy() {
			return ClanRankRecord.this.copy();
		}

		@Override
		public xbean.ClanRankRecord toData() {
			return ClanRankRecord.this.toData();
		}

		public xbean.ClanRankRecord toBean() {
			return ClanRankRecord.this.toBean();
		}

		@Override
		public xbean.ClanRankRecord toDataIf() {
			return ClanRankRecord.this.toDataIf();
		}

		public xbean.ClanRankRecord toBeanIf() {
			return ClanRankRecord.this.toBeanIf();
		}

		@Override
		public int getRank() { // 排名
			_xdb_verify_unsafe_();
			return rank;
		}

		@Override
		public long getClankey() { // 公会表唯一键值
			_xdb_verify_unsafe_();
			return clankey;
		}

		@Override
		public String getClanname() { // 公会名称
			_xdb_verify_unsafe_();
			return clanname;
		}

		@Override
		public com.locojoy.base.Octets getClannameOctets() { // 公会名称
			_xdb_verify_unsafe_();
			return ClanRankRecord.this.getClannameOctets();
		}

		@Override
		public int getLevel() { // 公会等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getMembernum() { // 公会成员数
			_xdb_verify_unsafe_();
			return membernum;
		}

		@Override
		public int getHotel() { // 旅馆等级 by changhao
			_xdb_verify_unsafe_();
			return hotel;
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
		public void setClankey(long _v_) { // 公会表唯一键值
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanname(String _v_) { // 公会名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClannameOctets(com.locojoy.base.Octets _v_) { // 公会名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 公会等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMembernum(int _v_) { // 公会成员数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHotel(int _v_) { // 旅馆等级 by changhao
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
			return ClanRankRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanRankRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanRankRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanRankRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanRankRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanRankRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanRankRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanRankRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanRankRecord.this.toString();
		}

	}

	public static final class Data implements xbean.ClanRankRecord {
		private int rank; // 排名
		private long clankey; // 公会表唯一键值
		private String clanname; // 公会名称
		private int level; // 公会等级
		private int membernum; // 公会成员数
		private int hotel; // 旅馆等级 by changhao
		private long triggertime; // 触发时间 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			clanname = "";
		}

		Data(xbean.ClanRankRecord _o1_) {
			if (_o1_ instanceof ClanRankRecord) assign((ClanRankRecord)_o1_);
			else if (_o1_ instanceof ClanRankRecord.Data) assign((ClanRankRecord.Data)_o1_);
			else if (_o1_ instanceof ClanRankRecord.Const) assign(((ClanRankRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanRankRecord _o_) {
			rank = _o_.rank;
			clankey = _o_.clankey;
			clanname = _o_.clanname;
			level = _o_.level;
			membernum = _o_.membernum;
			hotel = _o_.hotel;
			triggertime = _o_.triggertime;
		}

		private void assign(ClanRankRecord.Data _o_) {
			rank = _o_.rank;
			clankey = _o_.clankey;
			clanname = _o_.clanname;
			level = _o_.level;
			membernum = _o_.membernum;
			hotel = _o_.hotel;
			triggertime = _o_.triggertime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(rank);
			_os_.marshal(clankey);
			_os_.marshal(clanname, mkdb.Const.IO_CHARSET);
			_os_.marshal(level);
			_os_.marshal(membernum);
			_os_.marshal(hotel);
			_os_.marshal(triggertime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			rank = _os_.unmarshal_int();
			clankey = _os_.unmarshal_long();
			clanname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			level = _os_.unmarshal_int();
			membernum = _os_.unmarshal_int();
			hotel = _os_.unmarshal_int();
			triggertime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ClanRankRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanRankRecord toData() {
			return new Data(this);
		}

		public xbean.ClanRankRecord toBean() {
			return new ClanRankRecord(this, null, null);
		}

		@Override
		public xbean.ClanRankRecord toDataIf() {
			return this;
		}

		public xbean.ClanRankRecord toBeanIf() {
			return new ClanRankRecord(this, null, null);
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
		public long getClankey() { // 公会表唯一键值
			return clankey;
		}

		@Override
		public String getClanname() { // 公会名称
			return clanname;
		}

		@Override
		public com.locojoy.base.Octets getClannameOctets() { // 公会名称
			return com.locojoy.base.Octets.wrap(getClanname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getLevel() { // 公会等级
			return level;
		}

		@Override
		public int getMembernum() { // 公会成员数
			return membernum;
		}

		@Override
		public int getHotel() { // 旅馆等级 by changhao
			return hotel;
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
		public void setClankey(long _v_) { // 公会表唯一键值
			clankey = _v_;
		}

		@Override
		public void setClanname(String _v_) { // 公会名称
			if (null == _v_)
				throw new NullPointerException();
			clanname = _v_;
		}

		@Override
		public void setClannameOctets(com.locojoy.base.Octets _v_) { // 公会名称
			this.setClanname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setLevel(int _v_) { // 公会等级
			level = _v_;
		}

		@Override
		public void setMembernum(int _v_) { // 公会成员数
			membernum = _v_;
		}

		@Override
		public void setHotel(int _v_) { // 旅馆等级 by changhao
			hotel = _v_;
		}

		@Override
		public void setTriggertime(long _v_) { // 触发时间 by changhao
			triggertime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanRankRecord.Data)) return false;
			ClanRankRecord.Data _o_ = (ClanRankRecord.Data) _o1_;
			if (rank != _o_.rank) return false;
			if (clankey != _o_.clankey) return false;
			if (!clanname.equals(_o_.clanname)) return false;
			if (level != _o_.level) return false;
			if (membernum != _o_.membernum) return false;
			if (hotel != _o_.hotel) return false;
			if (triggertime != _o_.triggertime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += rank;
			_h_ += clankey;
			_h_ += clanname.hashCode();
			_h_ += level;
			_h_ += membernum;
			_h_ += hotel;
			_h_ += triggertime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(rank);
			_sb_.append(",");
			_sb_.append(clankey);
			_sb_.append(",");
			_sb_.append("'").append(clanname).append("'");
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(membernum);
			_sb_.append(",");
			_sb_.append(hotel);
			_sb_.append(",");
			_sb_.append(triggertime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
