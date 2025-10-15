
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class WinnerRoleRecord extends mkdb.XBean implements xbean.WinnerRoleRecord {
	private long roleid; // 
	private int score; // 记录个人的成绩
	private long time; // 记录成绩变化的时间
	private int awardflag; // 是否领取过奖励的标记,0为没有领取   1为领取过了

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		score = 0;
		time = 0L;
		awardflag = 0;
	}

	WinnerRoleRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public WinnerRoleRecord() {
		this(0, null, null);
	}

	public WinnerRoleRecord(WinnerRoleRecord _o_) {
		this(_o_, null, null);
	}

	WinnerRoleRecord(xbean.WinnerRoleRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof WinnerRoleRecord) assign((WinnerRoleRecord)_o1_);
		else if (_o1_ instanceof WinnerRoleRecord.Data) assign((WinnerRoleRecord.Data)_o1_);
		else if (_o1_ instanceof WinnerRoleRecord.Const) assign(((WinnerRoleRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(WinnerRoleRecord _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		score = _o_.score;
		time = _o_.time;
		awardflag = _o_.awardflag;
	}

	private void assign(WinnerRoleRecord.Data _o_) {
		roleid = _o_.roleid;
		score = _o_.score;
		time = _o_.time;
		awardflag = _o_.awardflag;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(score);
		_os_.marshal(time);
		_os_.marshal(awardflag);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		score = _os_.unmarshal_int();
		time = _os_.unmarshal_long();
		awardflag = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.WinnerRoleRecord copy() {
		_xdb_verify_unsafe_();
		return new WinnerRoleRecord(this);
	}

	@Override
	public xbean.WinnerRoleRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WinnerRoleRecord toBean() {
		_xdb_verify_unsafe_();
		return new WinnerRoleRecord(this); // same as copy()
	}

	@Override
	public xbean.WinnerRoleRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.WinnerRoleRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // 
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public int getScore() { // 记录个人的成绩
		_xdb_verify_unsafe_();
		return score;
	}

	@Override
	public long getTime() { // 记录成绩变化的时间
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public int getAwardflag() { // 是否领取过奖励的标记,0为没有领取   1为领取过了
		_xdb_verify_unsafe_();
		return awardflag;
	}

	@Override
	public void setRoleid(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setScore(int _v_) { // 记录个人的成绩
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "score") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, score) {
					public void rollback() { score = _xdb_saved; }
				};}});
		score = _v_;
	}

	@Override
	public void setTime(long _v_) { // 记录成绩变化的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "time") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, time) {
					public void rollback() { time = _xdb_saved; }
				};}});
		time = _v_;
	}

	@Override
	public void setAwardflag(int _v_) { // 是否领取过奖励的标记,0为没有领取   1为领取过了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "awardflag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, awardflag) {
					public void rollback() { awardflag = _xdb_saved; }
				};}});
		awardflag = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		WinnerRoleRecord _o_ = null;
		if ( _o1_ instanceof WinnerRoleRecord ) _o_ = (WinnerRoleRecord)_o1_;
		else if ( _o1_ instanceof WinnerRoleRecord.Const ) _o_ = ((WinnerRoleRecord.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (score != _o_.score) return false;
		if (time != _o_.time) return false;
		if (awardflag != _o_.awardflag) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += score;
		_h_ += time;
		_h_ += awardflag;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(score);
		_sb_.append(",");
		_sb_.append(time);
		_sb_.append(",");
		_sb_.append(awardflag);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("score"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("awardflag"));
		return lb;
	}

	private class Const implements xbean.WinnerRoleRecord {
		WinnerRoleRecord nThis() {
			return WinnerRoleRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.WinnerRoleRecord copy() {
			return WinnerRoleRecord.this.copy();
		}

		@Override
		public xbean.WinnerRoleRecord toData() {
			return WinnerRoleRecord.this.toData();
		}

		public xbean.WinnerRoleRecord toBean() {
			return WinnerRoleRecord.this.toBean();
		}

		@Override
		public xbean.WinnerRoleRecord toDataIf() {
			return WinnerRoleRecord.this.toDataIf();
		}

		public xbean.WinnerRoleRecord toBeanIf() {
			return WinnerRoleRecord.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // 
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getScore() { // 记录个人的成绩
			_xdb_verify_unsafe_();
			return score;
		}

		@Override
		public long getTime() { // 记录成绩变化的时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public int getAwardflag() { // 是否领取过奖励的标记,0为没有领取   1为领取过了
			_xdb_verify_unsafe_();
			return awardflag;
		}

		@Override
		public void setRoleid(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setScore(int _v_) { // 记录个人的成绩
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTime(long _v_) { // 记录成绩变化的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAwardflag(int _v_) { // 是否领取过奖励的标记,0为没有领取   1为领取过了
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
			return WinnerRoleRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return WinnerRoleRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return WinnerRoleRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return WinnerRoleRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return WinnerRoleRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return WinnerRoleRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return WinnerRoleRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return WinnerRoleRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return WinnerRoleRecord.this.toString();
		}

	}

	public static final class Data implements xbean.WinnerRoleRecord {
		private long roleid; // 
		private int score; // 记录个人的成绩
		private long time; // 记录成绩变化的时间
		private int awardflag; // 是否领取过奖励的标记,0为没有领取   1为领取过了

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.WinnerRoleRecord _o1_) {
			if (_o1_ instanceof WinnerRoleRecord) assign((WinnerRoleRecord)_o1_);
			else if (_o1_ instanceof WinnerRoleRecord.Data) assign((WinnerRoleRecord.Data)_o1_);
			else if (_o1_ instanceof WinnerRoleRecord.Const) assign(((WinnerRoleRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(WinnerRoleRecord _o_) {
			roleid = _o_.roleid;
			score = _o_.score;
			time = _o_.time;
			awardflag = _o_.awardflag;
		}

		private void assign(WinnerRoleRecord.Data _o_) {
			roleid = _o_.roleid;
			score = _o_.score;
			time = _o_.time;
			awardflag = _o_.awardflag;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(score);
			_os_.marshal(time);
			_os_.marshal(awardflag);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			score = _os_.unmarshal_int();
			time = _os_.unmarshal_long();
			awardflag = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.WinnerRoleRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.WinnerRoleRecord toData() {
			return new Data(this);
		}

		public xbean.WinnerRoleRecord toBean() {
			return new WinnerRoleRecord(this, null, null);
		}

		@Override
		public xbean.WinnerRoleRecord toDataIf() {
			return this;
		}

		public xbean.WinnerRoleRecord toBeanIf() {
			return new WinnerRoleRecord(this, null, null);
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
		public long getRoleid() { // 
			return roleid;
		}

		@Override
		public int getScore() { // 记录个人的成绩
			return score;
		}

		@Override
		public long getTime() { // 记录成绩变化的时间
			return time;
		}

		@Override
		public int getAwardflag() { // 是否领取过奖励的标记,0为没有领取   1为领取过了
			return awardflag;
		}

		@Override
		public void setRoleid(long _v_) { // 
			roleid = _v_;
		}

		@Override
		public void setScore(int _v_) { // 记录个人的成绩
			score = _v_;
		}

		@Override
		public void setTime(long _v_) { // 记录成绩变化的时间
			time = _v_;
		}

		@Override
		public void setAwardflag(int _v_) { // 是否领取过奖励的标记,0为没有领取   1为领取过了
			awardflag = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof WinnerRoleRecord.Data)) return false;
			WinnerRoleRecord.Data _o_ = (WinnerRoleRecord.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (score != _o_.score) return false;
			if (time != _o_.time) return false;
			if (awardflag != _o_.awardflag) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += score;
			_h_ += time;
			_h_ += awardflag;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(score);
			_sb_.append(",");
			_sb_.append(time);
			_sb_.append(",");
			_sb_.append(awardflag);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
