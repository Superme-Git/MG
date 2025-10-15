
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class MarshalClanFightHistroyRankRecord extends mkdb.XBean implements xbean.MarshalClanFightHistroyRankRecord {
	private long clanid; // 公会id by changhao
	private String clanname; // 公会名字 by changhao
	private int clanlevel; // 公会等级 by changhao
	private int fightcount; // 战斗次数 by changhao
	private int wincount; // 胜利次数 by changhao
	private int totalscroe; // 公会战积分 by changhao

	@Override
	public void _reset_unsafe_() {
		clanid = 0L;
		clanname = "";
		clanlevel = 0;
		fightcount = 0;
		wincount = 0;
		totalscroe = 0;
	}

	MarshalClanFightHistroyRankRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		clanname = "";
	}

	public MarshalClanFightHistroyRankRecord() {
		this(0, null, null);
	}

	public MarshalClanFightHistroyRankRecord(MarshalClanFightHistroyRankRecord _o_) {
		this(_o_, null, null);
	}

	MarshalClanFightHistroyRankRecord(xbean.MarshalClanFightHistroyRankRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof MarshalClanFightHistroyRankRecord) assign((MarshalClanFightHistroyRankRecord)_o1_);
		else if (_o1_ instanceof MarshalClanFightHistroyRankRecord.Data) assign((MarshalClanFightHistroyRankRecord.Data)_o1_);
		else if (_o1_ instanceof MarshalClanFightHistroyRankRecord.Const) assign(((MarshalClanFightHistroyRankRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(MarshalClanFightHistroyRankRecord _o_) {
		_o_._xdb_verify_unsafe_();
		clanid = _o_.clanid;
		clanname = _o_.clanname;
		clanlevel = _o_.clanlevel;
		fightcount = _o_.fightcount;
		wincount = _o_.wincount;
		totalscroe = _o_.totalscroe;
	}

	private void assign(MarshalClanFightHistroyRankRecord.Data _o_) {
		clanid = _o_.clanid;
		clanname = _o_.clanname;
		clanlevel = _o_.clanlevel;
		fightcount = _o_.fightcount;
		wincount = _o_.wincount;
		totalscroe = _o_.totalscroe;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(clanid);
		_os_.marshal(clanname, mkdb.Const.IO_CHARSET);
		_os_.marshal(clanlevel);
		_os_.marshal(fightcount);
		_os_.marshal(wincount);
		_os_.marshal(totalscroe);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		clanid = _os_.unmarshal_long();
		clanname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		clanlevel = _os_.unmarshal_int();
		fightcount = _os_.unmarshal_int();
		wincount = _os_.unmarshal_int();
		totalscroe = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.MarshalClanFightHistroyRankRecord copy() {
		_xdb_verify_unsafe_();
		return new MarshalClanFightHistroyRankRecord(this);
	}

	@Override
	public xbean.MarshalClanFightHistroyRankRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarshalClanFightHistroyRankRecord toBean() {
		_xdb_verify_unsafe_();
		return new MarshalClanFightHistroyRankRecord(this); // same as copy()
	}

	@Override
	public xbean.MarshalClanFightHistroyRankRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.MarshalClanFightHistroyRankRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getClanid() { // 公会id by changhao
		_xdb_verify_unsafe_();
		return clanid;
	}

	@Override
	public String getClanname() { // 公会名字 by changhao
		_xdb_verify_unsafe_();
		return clanname;
	}

	@Override
	public com.locojoy.base.Octets getClannameOctets() { // 公会名字 by changhao
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getClanname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getClanlevel() { // 公会等级 by changhao
		_xdb_verify_unsafe_();
		return clanlevel;
	}

	@Override
	public int getFightcount() { // 战斗次数 by changhao
		_xdb_verify_unsafe_();
		return fightcount;
	}

	@Override
	public int getWincount() { // 胜利次数 by changhao
		_xdb_verify_unsafe_();
		return wincount;
	}

	@Override
	public int getTotalscroe() { // 公会战积分 by changhao
		_xdb_verify_unsafe_();
		return totalscroe;
	}

	@Override
	public void setClanid(long _v_) { // 公会id by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clanid) {
					public void rollback() { clanid = _xdb_saved; }
				};}});
		clanid = _v_;
	}

	@Override
	public void setClanname(String _v_) { // 公会名字 by changhao
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
	public void setClannameOctets(com.locojoy.base.Octets _v_) { // 公会名字 by changhao
		_xdb_verify_unsafe_();
		this.setClanname(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setClanlevel(int _v_) { // 公会等级 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanlevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanlevel) {
					public void rollback() { clanlevel = _xdb_saved; }
				};}});
		clanlevel = _v_;
	}

	@Override
	public void setFightcount(int _v_) { // 战斗次数 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fightcount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, fightcount) {
					public void rollback() { fightcount = _xdb_saved; }
				};}});
		fightcount = _v_;
	}

	@Override
	public void setWincount(int _v_) { // 胜利次数 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "wincount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, wincount) {
					public void rollback() { wincount = _xdb_saved; }
				};}});
		wincount = _v_;
	}

	@Override
	public void setTotalscroe(int _v_) { // 公会战积分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "totalscroe") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, totalscroe) {
					public void rollback() { totalscroe = _xdb_saved; }
				};}});
		totalscroe = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		MarshalClanFightHistroyRankRecord _o_ = null;
		if ( _o1_ instanceof MarshalClanFightHistroyRankRecord ) _o_ = (MarshalClanFightHistroyRankRecord)_o1_;
		else if ( _o1_ instanceof MarshalClanFightHistroyRankRecord.Const ) _o_ = ((MarshalClanFightHistroyRankRecord.Const)_o1_).nThis();
		else return false;
		if (clanid != _o_.clanid) return false;
		if (!clanname.equals(_o_.clanname)) return false;
		if (clanlevel != _o_.clanlevel) return false;
		if (fightcount != _o_.fightcount) return false;
		if (wincount != _o_.wincount) return false;
		if (totalscroe != _o_.totalscroe) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += clanid;
		_h_ += clanname.hashCode();
		_h_ += clanlevel;
		_h_ += fightcount;
		_h_ += wincount;
		_h_ += totalscroe;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(clanid);
		_sb_.append(",");
		_sb_.append("'").append(clanname).append("'");
		_sb_.append(",");
		_sb_.append(clanlevel);
		_sb_.append(",");
		_sb_.append(fightcount);
		_sb_.append(",");
		_sb_.append(wincount);
		_sb_.append(",");
		_sb_.append(totalscroe);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanlevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fightcount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("wincount"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("totalscroe"));
		return lb;
	}

	private class Const implements xbean.MarshalClanFightHistroyRankRecord {
		MarshalClanFightHistroyRankRecord nThis() {
			return MarshalClanFightHistroyRankRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.MarshalClanFightHistroyRankRecord copy() {
			return MarshalClanFightHistroyRankRecord.this.copy();
		}

		@Override
		public xbean.MarshalClanFightHistroyRankRecord toData() {
			return MarshalClanFightHistroyRankRecord.this.toData();
		}

		public xbean.MarshalClanFightHistroyRankRecord toBean() {
			return MarshalClanFightHistroyRankRecord.this.toBean();
		}

		@Override
		public xbean.MarshalClanFightHistroyRankRecord toDataIf() {
			return MarshalClanFightHistroyRankRecord.this.toDataIf();
		}

		public xbean.MarshalClanFightHistroyRankRecord toBeanIf() {
			return MarshalClanFightHistroyRankRecord.this.toBeanIf();
		}

		@Override
		public long getClanid() { // 公会id by changhao
			_xdb_verify_unsafe_();
			return clanid;
		}

		@Override
		public String getClanname() { // 公会名字 by changhao
			_xdb_verify_unsafe_();
			return clanname;
		}

		@Override
		public com.locojoy.base.Octets getClannameOctets() { // 公会名字 by changhao
			_xdb_verify_unsafe_();
			return MarshalClanFightHistroyRankRecord.this.getClannameOctets();
		}

		@Override
		public int getClanlevel() { // 公会等级 by changhao
			_xdb_verify_unsafe_();
			return clanlevel;
		}

		@Override
		public int getFightcount() { // 战斗次数 by changhao
			_xdb_verify_unsafe_();
			return fightcount;
		}

		@Override
		public int getWincount() { // 胜利次数 by changhao
			_xdb_verify_unsafe_();
			return wincount;
		}

		@Override
		public int getTotalscroe() { // 公会战积分 by changhao
			_xdb_verify_unsafe_();
			return totalscroe;
		}

		@Override
		public void setClanid(long _v_) { // 公会id by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanname(String _v_) { // 公会名字 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClannameOctets(com.locojoy.base.Octets _v_) { // 公会名字 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanlevel(int _v_) { // 公会等级 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFightcount(int _v_) { // 战斗次数 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWincount(int _v_) { // 胜利次数 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTotalscroe(int _v_) { // 公会战积分 by changhao
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
			return MarshalClanFightHistroyRankRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return MarshalClanFightHistroyRankRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return MarshalClanFightHistroyRankRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return MarshalClanFightHistroyRankRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return MarshalClanFightHistroyRankRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return MarshalClanFightHistroyRankRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return MarshalClanFightHistroyRankRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return MarshalClanFightHistroyRankRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return MarshalClanFightHistroyRankRecord.this.toString();
		}

	}

	public static final class Data implements xbean.MarshalClanFightHistroyRankRecord {
		private long clanid; // 公会id by changhao
		private String clanname; // 公会名字 by changhao
		private int clanlevel; // 公会等级 by changhao
		private int fightcount; // 战斗次数 by changhao
		private int wincount; // 胜利次数 by changhao
		private int totalscroe; // 公会战积分 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			clanname = "";
		}

		Data(xbean.MarshalClanFightHistroyRankRecord _o1_) {
			if (_o1_ instanceof MarshalClanFightHistroyRankRecord) assign((MarshalClanFightHistroyRankRecord)_o1_);
			else if (_o1_ instanceof MarshalClanFightHistroyRankRecord.Data) assign((MarshalClanFightHistroyRankRecord.Data)_o1_);
			else if (_o1_ instanceof MarshalClanFightHistroyRankRecord.Const) assign(((MarshalClanFightHistroyRankRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(MarshalClanFightHistroyRankRecord _o_) {
			clanid = _o_.clanid;
			clanname = _o_.clanname;
			clanlevel = _o_.clanlevel;
			fightcount = _o_.fightcount;
			wincount = _o_.wincount;
			totalscroe = _o_.totalscroe;
		}

		private void assign(MarshalClanFightHistroyRankRecord.Data _o_) {
			clanid = _o_.clanid;
			clanname = _o_.clanname;
			clanlevel = _o_.clanlevel;
			fightcount = _o_.fightcount;
			wincount = _o_.wincount;
			totalscroe = _o_.totalscroe;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(clanid);
			_os_.marshal(clanname, mkdb.Const.IO_CHARSET);
			_os_.marshal(clanlevel);
			_os_.marshal(fightcount);
			_os_.marshal(wincount);
			_os_.marshal(totalscroe);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			clanid = _os_.unmarshal_long();
			clanname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			clanlevel = _os_.unmarshal_int();
			fightcount = _os_.unmarshal_int();
			wincount = _os_.unmarshal_int();
			totalscroe = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.MarshalClanFightHistroyRankRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.MarshalClanFightHistroyRankRecord toData() {
			return new Data(this);
		}

		public xbean.MarshalClanFightHistroyRankRecord toBean() {
			return new MarshalClanFightHistroyRankRecord(this, null, null);
		}

		@Override
		public xbean.MarshalClanFightHistroyRankRecord toDataIf() {
			return this;
		}

		public xbean.MarshalClanFightHistroyRankRecord toBeanIf() {
			return new MarshalClanFightHistroyRankRecord(this, null, null);
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
		public long getClanid() { // 公会id by changhao
			return clanid;
		}

		@Override
		public String getClanname() { // 公会名字 by changhao
			return clanname;
		}

		@Override
		public com.locojoy.base.Octets getClannameOctets() { // 公会名字 by changhao
			return com.locojoy.base.Octets.wrap(getClanname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getClanlevel() { // 公会等级 by changhao
			return clanlevel;
		}

		@Override
		public int getFightcount() { // 战斗次数 by changhao
			return fightcount;
		}

		@Override
		public int getWincount() { // 胜利次数 by changhao
			return wincount;
		}

		@Override
		public int getTotalscroe() { // 公会战积分 by changhao
			return totalscroe;
		}

		@Override
		public void setClanid(long _v_) { // 公会id by changhao
			clanid = _v_;
		}

		@Override
		public void setClanname(String _v_) { // 公会名字 by changhao
			if (null == _v_)
				throw new NullPointerException();
			clanname = _v_;
		}

		@Override
		public void setClannameOctets(com.locojoy.base.Octets _v_) { // 公会名字 by changhao
			this.setClanname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setClanlevel(int _v_) { // 公会等级 by changhao
			clanlevel = _v_;
		}

		@Override
		public void setFightcount(int _v_) { // 战斗次数 by changhao
			fightcount = _v_;
		}

		@Override
		public void setWincount(int _v_) { // 胜利次数 by changhao
			wincount = _v_;
		}

		@Override
		public void setTotalscroe(int _v_) { // 公会战积分 by changhao
			totalscroe = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof MarshalClanFightHistroyRankRecord.Data)) return false;
			MarshalClanFightHistroyRankRecord.Data _o_ = (MarshalClanFightHistroyRankRecord.Data) _o1_;
			if (clanid != _o_.clanid) return false;
			if (!clanname.equals(_o_.clanname)) return false;
			if (clanlevel != _o_.clanlevel) return false;
			if (fightcount != _o_.fightcount) return false;
			if (wincount != _o_.wincount) return false;
			if (totalscroe != _o_.totalscroe) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += clanid;
			_h_ += clanname.hashCode();
			_h_ += clanlevel;
			_h_ += fightcount;
			_h_ += wincount;
			_h_ += totalscroe;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(clanid);
			_sb_.append(",");
			_sb_.append("'").append(clanname).append("'");
			_sb_.append(",");
			_sb_.append(clanlevel);
			_sb_.append(",");
			_sb_.append(fightcount);
			_sb_.append(",");
			_sb_.append(wincount);
			_sb_.append(",");
			_sb_.append(totalscroe);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
