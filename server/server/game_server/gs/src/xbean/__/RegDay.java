
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RegDay extends mkdb.XBean implements xbean.RegDay {
	private int rewardflag; // 奖励标志(1-过期 2-已领取 3-未领取)
	private int day; // 第几天
	private int suppregflag; // 补签标志(0-正常签到 1-补签)

	@Override
	public void _reset_unsafe_() {
		rewardflag = 0;
		day = 0;
		suppregflag = 0;
	}

	RegDay(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		suppregflag = 0;
	}

	public RegDay() {
		this(0, null, null);
	}

	public RegDay(RegDay _o_) {
		this(_o_, null, null);
	}

	RegDay(xbean.RegDay _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RegDay) assign((RegDay)_o1_);
		else if (_o1_ instanceof RegDay.Data) assign((RegDay.Data)_o1_);
		else if (_o1_ instanceof RegDay.Const) assign(((RegDay.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RegDay _o_) {
		_o_._xdb_verify_unsafe_();
		rewardflag = _o_.rewardflag;
		day = _o_.day;
		suppregflag = _o_.suppregflag;
	}

	private void assign(RegDay.Data _o_) {
		rewardflag = _o_.rewardflag;
		day = _o_.day;
		suppregflag = _o_.suppregflag;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(rewardflag);
		_os_.marshal(day);
		_os_.marshal(suppregflag);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		rewardflag = _os_.unmarshal_int();
		day = _os_.unmarshal_int();
		suppregflag = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.RegDay copy() {
		_xdb_verify_unsafe_();
		return new RegDay(this);
	}

	@Override
	public xbean.RegDay toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RegDay toBean() {
		_xdb_verify_unsafe_();
		return new RegDay(this); // same as copy()
	}

	@Override
	public xbean.RegDay toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RegDay toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getRewardflag() { // 奖励标志(1-过期 2-已领取 3-未领取)
		_xdb_verify_unsafe_();
		return rewardflag;
	}

	@Override
	public int getDay() { // 第几天
		_xdb_verify_unsafe_();
		return day;
	}

	@Override
	public int getSuppregflag() { // 补签标志(0-正常签到 1-补签)
		_xdb_verify_unsafe_();
		return suppregflag;
	}

	@Override
	public void setRewardflag(int _v_) { // 奖励标志(1-过期 2-已领取 3-未领取)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rewardflag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rewardflag) {
					public void rollback() { rewardflag = _xdb_saved; }
				};}});
		rewardflag = _v_;
	}

	@Override
	public void setDay(int _v_) { // 第几天
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "day") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, day) {
					public void rollback() { day = _xdb_saved; }
				};}});
		day = _v_;
	}

	@Override
	public void setSuppregflag(int _v_) { // 补签标志(0-正常签到 1-补签)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "suppregflag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, suppregflag) {
					public void rollback() { suppregflag = _xdb_saved; }
				};}});
		suppregflag = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RegDay _o_ = null;
		if ( _o1_ instanceof RegDay ) _o_ = (RegDay)_o1_;
		else if ( _o1_ instanceof RegDay.Const ) _o_ = ((RegDay.Const)_o1_).nThis();
		else return false;
		if (rewardflag != _o_.rewardflag) return false;
		if (day != _o_.day) return false;
		if (suppregflag != _o_.suppregflag) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += rewardflag;
		_h_ += day;
		_h_ += suppregflag;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rewardflag);
		_sb_.append(",");
		_sb_.append(day);
		_sb_.append(",");
		_sb_.append(suppregflag);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rewardflag"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("day"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("suppregflag"));
		return lb;
	}

	private class Const implements xbean.RegDay {
		RegDay nThis() {
			return RegDay.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RegDay copy() {
			return RegDay.this.copy();
		}

		@Override
		public xbean.RegDay toData() {
			return RegDay.this.toData();
		}

		public xbean.RegDay toBean() {
			return RegDay.this.toBean();
		}

		@Override
		public xbean.RegDay toDataIf() {
			return RegDay.this.toDataIf();
		}

		public xbean.RegDay toBeanIf() {
			return RegDay.this.toBeanIf();
		}

		@Override
		public int getRewardflag() { // 奖励标志(1-过期 2-已领取 3-未领取)
			_xdb_verify_unsafe_();
			return rewardflag;
		}

		@Override
		public int getDay() { // 第几天
			_xdb_verify_unsafe_();
			return day;
		}

		@Override
		public int getSuppregflag() { // 补签标志(0-正常签到 1-补签)
			_xdb_verify_unsafe_();
			return suppregflag;
		}

		@Override
		public void setRewardflag(int _v_) { // 奖励标志(1-过期 2-已领取 3-未领取)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDay(int _v_) { // 第几天
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSuppregflag(int _v_) { // 补签标志(0-正常签到 1-补签)
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
			return RegDay.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RegDay.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RegDay.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RegDay.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RegDay.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RegDay.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RegDay.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RegDay.this.hashCode();
		}

		@Override
		public String toString() {
			return RegDay.this.toString();
		}

	}

	public static final class Data implements xbean.RegDay {
		private int rewardflag; // 奖励标志(1-过期 2-已领取 3-未领取)
		private int day; // 第几天
		private int suppregflag; // 补签标志(0-正常签到 1-补签)

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			suppregflag = 0;
		}

		Data(xbean.RegDay _o1_) {
			if (_o1_ instanceof RegDay) assign((RegDay)_o1_);
			else if (_o1_ instanceof RegDay.Data) assign((RegDay.Data)_o1_);
			else if (_o1_ instanceof RegDay.Const) assign(((RegDay.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RegDay _o_) {
			rewardflag = _o_.rewardflag;
			day = _o_.day;
			suppregflag = _o_.suppregflag;
		}

		private void assign(RegDay.Data _o_) {
			rewardflag = _o_.rewardflag;
			day = _o_.day;
			suppregflag = _o_.suppregflag;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(rewardflag);
			_os_.marshal(day);
			_os_.marshal(suppregflag);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			rewardflag = _os_.unmarshal_int();
			day = _os_.unmarshal_int();
			suppregflag = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.RegDay copy() {
			return new Data(this);
		}

		@Override
		public xbean.RegDay toData() {
			return new Data(this);
		}

		public xbean.RegDay toBean() {
			return new RegDay(this, null, null);
		}

		@Override
		public xbean.RegDay toDataIf() {
			return this;
		}

		public xbean.RegDay toBeanIf() {
			return new RegDay(this, null, null);
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
		public int getRewardflag() { // 奖励标志(1-过期 2-已领取 3-未领取)
			return rewardflag;
		}

		@Override
		public int getDay() { // 第几天
			return day;
		}

		@Override
		public int getSuppregflag() { // 补签标志(0-正常签到 1-补签)
			return suppregflag;
		}

		@Override
		public void setRewardflag(int _v_) { // 奖励标志(1-过期 2-已领取 3-未领取)
			rewardflag = _v_;
		}

		@Override
		public void setDay(int _v_) { // 第几天
			day = _v_;
		}

		@Override
		public void setSuppregflag(int _v_) { // 补签标志(0-正常签到 1-补签)
			suppregflag = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RegDay.Data)) return false;
			RegDay.Data _o_ = (RegDay.Data) _o1_;
			if (rewardflag != _o_.rewardflag) return false;
			if (day != _o_.day) return false;
			if (suppregflag != _o_.suppregflag) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += rewardflag;
			_h_ += day;
			_h_ += suppregflag;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(rewardflag);
			_sb_.append(",");
			_sb_.append(day);
			_sb_.append(",");
			_sb_.append(suppregflag);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
