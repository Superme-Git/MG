
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanProgressRankRecord extends mkdb.XBean implements xbean.ClanProgressRankRecord {
	private int rank; // 排名 by changhao
	private long clankey; // 
	private String clanname; // 公会名称 by changhao
	private long time; // 时间 by changhao
	private int progress; // 进度 by changhao
	private String clanmastername; // 会长名字 by changhao
	private long triggertime; // 触发时间 by changhao
	private int copyid; // 副本id by changhao
	private String copyname; // 副本名字 by changhao
	private float bosshp; // BOSS血量 by changhao

	@Override
	public void _reset_unsafe_() {
		rank = 0;
		clankey = 0L;
		clanname = "";
		time = 0L;
		progress = 0;
		clanmastername = "";
		triggertime = 0L;
		copyid = 0;
		copyname = "";
		bosshp = 0.0f;
	}

	ClanProgressRankRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		clanname = "";
		clanmastername = "";
		copyname = "";
	}

	public ClanProgressRankRecord() {
		this(0, null, null);
	}

	public ClanProgressRankRecord(ClanProgressRankRecord _o_) {
		this(_o_, null, null);
	}

	ClanProgressRankRecord(xbean.ClanProgressRankRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanProgressRankRecord) assign((ClanProgressRankRecord)_o1_);
		else if (_o1_ instanceof ClanProgressRankRecord.Data) assign((ClanProgressRankRecord.Data)_o1_);
		else if (_o1_ instanceof ClanProgressRankRecord.Const) assign(((ClanProgressRankRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanProgressRankRecord _o_) {
		_o_._xdb_verify_unsafe_();
		rank = _o_.rank;
		clankey = _o_.clankey;
		clanname = _o_.clanname;
		time = _o_.time;
		progress = _o_.progress;
		clanmastername = _o_.clanmastername;
		triggertime = _o_.triggertime;
		copyid = _o_.copyid;
		copyname = _o_.copyname;
		bosshp = _o_.bosshp;
	}

	private void assign(ClanProgressRankRecord.Data _o_) {
		rank = _o_.rank;
		clankey = _o_.clankey;
		clanname = _o_.clanname;
		time = _o_.time;
		progress = _o_.progress;
		clanmastername = _o_.clanmastername;
		triggertime = _o_.triggertime;
		copyid = _o_.copyid;
		copyname = _o_.copyname;
		bosshp = _o_.bosshp;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(rank);
		_os_.marshal(clankey);
		_os_.marshal(clanname, mkdb.Const.IO_CHARSET);
		_os_.marshal(time);
		_os_.marshal(progress);
		_os_.marshal(clanmastername, mkdb.Const.IO_CHARSET);
		_os_.marshal(triggertime);
		_os_.marshal(copyid);
		_os_.marshal(copyname, mkdb.Const.IO_CHARSET);
		_os_.marshal(bosshp);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		rank = _os_.unmarshal_int();
		clankey = _os_.unmarshal_long();
		clanname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		time = _os_.unmarshal_long();
		progress = _os_.unmarshal_int();
		clanmastername = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		triggertime = _os_.unmarshal_long();
		copyid = _os_.unmarshal_int();
		copyname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		bosshp = _os_.unmarshal_float();
		return _os_;
	}

	@Override
	public xbean.ClanProgressRankRecord copy() {
		_xdb_verify_unsafe_();
		return new ClanProgressRankRecord(this);
	}

	@Override
	public xbean.ClanProgressRankRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanProgressRankRecord toBean() {
		_xdb_verify_unsafe_();
		return new ClanProgressRankRecord(this); // same as copy()
	}

	@Override
	public xbean.ClanProgressRankRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanProgressRankRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getRank() { // 排名 by changhao
		_xdb_verify_unsafe_();
		return rank;
	}

	@Override
	public long getClankey() { // 
		_xdb_verify_unsafe_();
		return clankey;
	}

	@Override
	public String getClanname() { // 公会名称 by changhao
		_xdb_verify_unsafe_();
		return clanname;
	}

	@Override
	public com.locojoy.base.Octets getClannameOctets() { // 公会名称 by changhao
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getClanname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getTime() { // 时间 by changhao
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public int getProgress() { // 进度 by changhao
		_xdb_verify_unsafe_();
		return progress;
	}

	@Override
	public String getClanmastername() { // 会长名字 by changhao
		_xdb_verify_unsafe_();
		return clanmastername;
	}

	@Override
	public com.locojoy.base.Octets getClanmasternameOctets() { // 会长名字 by changhao
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getClanmastername(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getTriggertime() { // 触发时间 by changhao
		_xdb_verify_unsafe_();
		return triggertime;
	}

	@Override
	public int getCopyid() { // 副本id by changhao
		_xdb_verify_unsafe_();
		return copyid;
	}

	@Override
	public String getCopyname() { // 副本名字 by changhao
		_xdb_verify_unsafe_();
		return copyname;
	}

	@Override
	public com.locojoy.base.Octets getCopynameOctets() { // 副本名字 by changhao
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getCopyname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public float getBosshp() { // BOSS血量 by changhao
		_xdb_verify_unsafe_();
		return bosshp;
	}

	@Override
	public void setRank(int _v_) { // 排名 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rank") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rank) {
					public void rollback() { rank = _xdb_saved; }
				};}});
		rank = _v_;
	}

	@Override
	public void setClankey(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clankey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clankey) {
					public void rollback() { clankey = _xdb_saved; }
				};}});
		clankey = _v_;
	}

	@Override
	public void setClanname(String _v_) { // 公会名称 by changhao
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
	public void setClannameOctets(com.locojoy.base.Octets _v_) { // 公会名称 by changhao
		_xdb_verify_unsafe_();
		this.setClanname(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setTime(long _v_) { // 时间 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "time") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, time) {
					public void rollback() { time = _xdb_saved; }
				};}});
		time = _v_;
	}

	@Override
	public void setProgress(int _v_) { // 进度 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "progress") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, progress) {
					public void rollback() { progress = _xdb_saved; }
				};}});
		progress = _v_;
	}

	@Override
	public void setClanmastername(String _v_) { // 会长名字 by changhao
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanmastername") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, clanmastername) {
					public void rollback() { clanmastername = _xdb_saved; }
				};}});
		clanmastername = _v_;
	}

	@Override
	public void setClanmasternameOctets(com.locojoy.base.Octets _v_) { // 会长名字 by changhao
		_xdb_verify_unsafe_();
		this.setClanmastername(_v_.getString(mkdb.Const.IO_CHARSET));
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
	public void setCopyid(int _v_) { // 副本id by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "copyid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, copyid) {
					public void rollback() { copyid = _xdb_saved; }
				};}});
		copyid = _v_;
	}

	@Override
	public void setCopyname(String _v_) { // 副本名字 by changhao
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "copyname") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, copyname) {
					public void rollback() { copyname = _xdb_saved; }
				};}});
		copyname = _v_;
	}

	@Override
	public void setCopynameOctets(com.locojoy.base.Octets _v_) { // 副本名字 by changhao
		_xdb_verify_unsafe_();
		this.setCopyname(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setBosshp(float _v_) { // BOSS血量 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bosshp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogFloat(this, bosshp) {
					public void rollback() { bosshp = _xdb_saved; }
				};}});
		bosshp = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanProgressRankRecord _o_ = null;
		if ( _o1_ instanceof ClanProgressRankRecord ) _o_ = (ClanProgressRankRecord)_o1_;
		else if ( _o1_ instanceof ClanProgressRankRecord.Const ) _o_ = ((ClanProgressRankRecord.Const)_o1_).nThis();
		else return false;
		if (rank != _o_.rank) return false;
		if (clankey != _o_.clankey) return false;
		if (!clanname.equals(_o_.clanname)) return false;
		if (time != _o_.time) return false;
		if (progress != _o_.progress) return false;
		if (!clanmastername.equals(_o_.clanmastername)) return false;
		if (triggertime != _o_.triggertime) return false;
		if (copyid != _o_.copyid) return false;
		if (!copyname.equals(_o_.copyname)) return false;
		if (bosshp != _o_.bosshp) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += rank;
		_h_ += clankey;
		_h_ += clanname.hashCode();
		_h_ += time;
		_h_ += progress;
		_h_ += clanmastername.hashCode();
		_h_ += triggertime;
		_h_ += copyid;
		_h_ += copyname.hashCode();
		_h_ += bosshp;
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
		_sb_.append(time);
		_sb_.append(",");
		_sb_.append(progress);
		_sb_.append(",");
		_sb_.append("'").append(clanmastername).append("'");
		_sb_.append(",");
		_sb_.append(triggertime);
		_sb_.append(",");
		_sb_.append(copyid);
		_sb_.append(",");
		_sb_.append("'").append(copyname).append("'");
		_sb_.append(",");
		_sb_.append(bosshp);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rank"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clankey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("progress"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanmastername"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("triggertime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("copyid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("copyname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bosshp"));
		return lb;
	}

	private class Const implements xbean.ClanProgressRankRecord {
		ClanProgressRankRecord nThis() {
			return ClanProgressRankRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanProgressRankRecord copy() {
			return ClanProgressRankRecord.this.copy();
		}

		@Override
		public xbean.ClanProgressRankRecord toData() {
			return ClanProgressRankRecord.this.toData();
		}

		public xbean.ClanProgressRankRecord toBean() {
			return ClanProgressRankRecord.this.toBean();
		}

		@Override
		public xbean.ClanProgressRankRecord toDataIf() {
			return ClanProgressRankRecord.this.toDataIf();
		}

		public xbean.ClanProgressRankRecord toBeanIf() {
			return ClanProgressRankRecord.this.toBeanIf();
		}

		@Override
		public int getRank() { // 排名 by changhao
			_xdb_verify_unsafe_();
			return rank;
		}

		@Override
		public long getClankey() { // 
			_xdb_verify_unsafe_();
			return clankey;
		}

		@Override
		public String getClanname() { // 公会名称 by changhao
			_xdb_verify_unsafe_();
			return clanname;
		}

		@Override
		public com.locojoy.base.Octets getClannameOctets() { // 公会名称 by changhao
			_xdb_verify_unsafe_();
			return ClanProgressRankRecord.this.getClannameOctets();
		}

		@Override
		public long getTime() { // 时间 by changhao
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public int getProgress() { // 进度 by changhao
			_xdb_verify_unsafe_();
			return progress;
		}

		@Override
		public String getClanmastername() { // 会长名字 by changhao
			_xdb_verify_unsafe_();
			return clanmastername;
		}

		@Override
		public com.locojoy.base.Octets getClanmasternameOctets() { // 会长名字 by changhao
			_xdb_verify_unsafe_();
			return ClanProgressRankRecord.this.getClanmasternameOctets();
		}

		@Override
		public long getTriggertime() { // 触发时间 by changhao
			_xdb_verify_unsafe_();
			return triggertime;
		}

		@Override
		public int getCopyid() { // 副本id by changhao
			_xdb_verify_unsafe_();
			return copyid;
		}

		@Override
		public String getCopyname() { // 副本名字 by changhao
			_xdb_verify_unsafe_();
			return copyname;
		}

		@Override
		public com.locojoy.base.Octets getCopynameOctets() { // 副本名字 by changhao
			_xdb_verify_unsafe_();
			return ClanProgressRankRecord.this.getCopynameOctets();
		}

		@Override
		public float getBosshp() { // BOSS血量 by changhao
			_xdb_verify_unsafe_();
			return bosshp;
		}

		@Override
		public void setRank(int _v_) { // 排名 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClankey(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanname(String _v_) { // 公会名称 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClannameOctets(com.locojoy.base.Octets _v_) { // 公会名称 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTime(long _v_) { // 时间 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setProgress(int _v_) { // 进度 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanmastername(String _v_) { // 会长名字 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanmasternameOctets(com.locojoy.base.Octets _v_) { // 会长名字 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTriggertime(long _v_) { // 触发时间 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCopyid(int _v_) { // 副本id by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCopyname(String _v_) { // 副本名字 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCopynameOctets(com.locojoy.base.Octets _v_) { // 副本名字 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBosshp(float _v_) { // BOSS血量 by changhao
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
			return ClanProgressRankRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanProgressRankRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanProgressRankRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanProgressRankRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanProgressRankRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanProgressRankRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanProgressRankRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanProgressRankRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanProgressRankRecord.this.toString();
		}

	}

	public static final class Data implements xbean.ClanProgressRankRecord {
		private int rank; // 排名 by changhao
		private long clankey; // 
		private String clanname; // 公会名称 by changhao
		private long time; // 时间 by changhao
		private int progress; // 进度 by changhao
		private String clanmastername; // 会长名字 by changhao
		private long triggertime; // 触发时间 by changhao
		private int copyid; // 副本id by changhao
		private String copyname; // 副本名字 by changhao
		private float bosshp; // BOSS血量 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			clanname = "";
			clanmastername = "";
			copyname = "";
		}

		Data(xbean.ClanProgressRankRecord _o1_) {
			if (_o1_ instanceof ClanProgressRankRecord) assign((ClanProgressRankRecord)_o1_);
			else if (_o1_ instanceof ClanProgressRankRecord.Data) assign((ClanProgressRankRecord.Data)_o1_);
			else if (_o1_ instanceof ClanProgressRankRecord.Const) assign(((ClanProgressRankRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanProgressRankRecord _o_) {
			rank = _o_.rank;
			clankey = _o_.clankey;
			clanname = _o_.clanname;
			time = _o_.time;
			progress = _o_.progress;
			clanmastername = _o_.clanmastername;
			triggertime = _o_.triggertime;
			copyid = _o_.copyid;
			copyname = _o_.copyname;
			bosshp = _o_.bosshp;
		}

		private void assign(ClanProgressRankRecord.Data _o_) {
			rank = _o_.rank;
			clankey = _o_.clankey;
			clanname = _o_.clanname;
			time = _o_.time;
			progress = _o_.progress;
			clanmastername = _o_.clanmastername;
			triggertime = _o_.triggertime;
			copyid = _o_.copyid;
			copyname = _o_.copyname;
			bosshp = _o_.bosshp;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(rank);
			_os_.marshal(clankey);
			_os_.marshal(clanname, mkdb.Const.IO_CHARSET);
			_os_.marshal(time);
			_os_.marshal(progress);
			_os_.marshal(clanmastername, mkdb.Const.IO_CHARSET);
			_os_.marshal(triggertime);
			_os_.marshal(copyid);
			_os_.marshal(copyname, mkdb.Const.IO_CHARSET);
			_os_.marshal(bosshp);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			rank = _os_.unmarshal_int();
			clankey = _os_.unmarshal_long();
			clanname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			time = _os_.unmarshal_long();
			progress = _os_.unmarshal_int();
			clanmastername = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			triggertime = _os_.unmarshal_long();
			copyid = _os_.unmarshal_int();
			copyname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			bosshp = _os_.unmarshal_float();
			return _os_;
		}

		@Override
		public xbean.ClanProgressRankRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanProgressRankRecord toData() {
			return new Data(this);
		}

		public xbean.ClanProgressRankRecord toBean() {
			return new ClanProgressRankRecord(this, null, null);
		}

		@Override
		public xbean.ClanProgressRankRecord toDataIf() {
			return this;
		}

		public xbean.ClanProgressRankRecord toBeanIf() {
			return new ClanProgressRankRecord(this, null, null);
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
		public int getRank() { // 排名 by changhao
			return rank;
		}

		@Override
		public long getClankey() { // 
			return clankey;
		}

		@Override
		public String getClanname() { // 公会名称 by changhao
			return clanname;
		}

		@Override
		public com.locojoy.base.Octets getClannameOctets() { // 公会名称 by changhao
			return com.locojoy.base.Octets.wrap(getClanname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getTime() { // 时间 by changhao
			return time;
		}

		@Override
		public int getProgress() { // 进度 by changhao
			return progress;
		}

		@Override
		public String getClanmastername() { // 会长名字 by changhao
			return clanmastername;
		}

		@Override
		public com.locojoy.base.Octets getClanmasternameOctets() { // 会长名字 by changhao
			return com.locojoy.base.Octets.wrap(getClanmastername(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getTriggertime() { // 触发时间 by changhao
			return triggertime;
		}

		@Override
		public int getCopyid() { // 副本id by changhao
			return copyid;
		}

		@Override
		public String getCopyname() { // 副本名字 by changhao
			return copyname;
		}

		@Override
		public com.locojoy.base.Octets getCopynameOctets() { // 副本名字 by changhao
			return com.locojoy.base.Octets.wrap(getCopyname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public float getBosshp() { // BOSS血量 by changhao
			return bosshp;
		}

		@Override
		public void setRank(int _v_) { // 排名 by changhao
			rank = _v_;
		}

		@Override
		public void setClankey(long _v_) { // 
			clankey = _v_;
		}

		@Override
		public void setClanname(String _v_) { // 公会名称 by changhao
			if (null == _v_)
				throw new NullPointerException();
			clanname = _v_;
		}

		@Override
		public void setClannameOctets(com.locojoy.base.Octets _v_) { // 公会名称 by changhao
			this.setClanname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setTime(long _v_) { // 时间 by changhao
			time = _v_;
		}

		@Override
		public void setProgress(int _v_) { // 进度 by changhao
			progress = _v_;
		}

		@Override
		public void setClanmastername(String _v_) { // 会长名字 by changhao
			if (null == _v_)
				throw new NullPointerException();
			clanmastername = _v_;
		}

		@Override
		public void setClanmasternameOctets(com.locojoy.base.Octets _v_) { // 会长名字 by changhao
			this.setClanmastername(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setTriggertime(long _v_) { // 触发时间 by changhao
			triggertime = _v_;
		}

		@Override
		public void setCopyid(int _v_) { // 副本id by changhao
			copyid = _v_;
		}

		@Override
		public void setCopyname(String _v_) { // 副本名字 by changhao
			if (null == _v_)
				throw new NullPointerException();
			copyname = _v_;
		}

		@Override
		public void setCopynameOctets(com.locojoy.base.Octets _v_) { // 副本名字 by changhao
			this.setCopyname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setBosshp(float _v_) { // BOSS血量 by changhao
			bosshp = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanProgressRankRecord.Data)) return false;
			ClanProgressRankRecord.Data _o_ = (ClanProgressRankRecord.Data) _o1_;
			if (rank != _o_.rank) return false;
			if (clankey != _o_.clankey) return false;
			if (!clanname.equals(_o_.clanname)) return false;
			if (time != _o_.time) return false;
			if (progress != _o_.progress) return false;
			if (!clanmastername.equals(_o_.clanmastername)) return false;
			if (triggertime != _o_.triggertime) return false;
			if (copyid != _o_.copyid) return false;
			if (!copyname.equals(_o_.copyname)) return false;
			if (bosshp != _o_.bosshp) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += rank;
			_h_ += clankey;
			_h_ += clanname.hashCode();
			_h_ += time;
			_h_ += progress;
			_h_ += clanmastername.hashCode();
			_h_ += triggertime;
			_h_ += copyid;
			_h_ += copyname.hashCode();
			_h_ += bosshp;
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
			_sb_.append(time);
			_sb_.append(",");
			_sb_.append(progress);
			_sb_.append(",");
			_sb_.append("'").append(clanmastername).append("'");
			_sb_.append(",");
			_sb_.append(triggertime);
			_sb_.append(",");
			_sb_.append(copyid);
			_sb_.append(",");
			_sb_.append("'").append(copyname).append("'");
			_sb_.append(",");
			_sb_.append(bosshp);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
