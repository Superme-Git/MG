
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class YbNum extends mkdb.XBean implements xbean.YbNum {
	private int num; // 现金充值符石数
	private int sysnum; // 系统赠送符石数
	private int nopresentnum; // 没有包含赠送的实际充值总符石
	private java.util.ArrayList<Long> snlist; // 玩家充值的序列号
	private int bindorsysnumtoday; // 该玩家今天获得的符石数量(不包括充值的)
	private long bindorsysnumtodaytime; // 这个time跟下面time不一样,这个time只有绑定或系统符石增加的时候才会变
	private long time; // 最近一次符石数量发生变化的时间
	private long fushiall; // 累计符石总数，包括所有产出途径的符石数

	@Override
	public void _reset_unsafe_() {
		num = 0;
		sysnum = 0;
		nopresentnum = 0;
		snlist.clear();
		bindorsysnumtoday = 0;
		bindorsysnumtodaytime = 0L;
		time = 0L;
		fushiall = 0L;
	}

	YbNum(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		snlist = new java.util.ArrayList<Long>();
	}

	public YbNum() {
		this(0, null, null);
	}

	public YbNum(YbNum _o_) {
		this(_o_, null, null);
	}

	YbNum(xbean.YbNum _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof YbNum) assign((YbNum)_o1_);
		else if (_o1_ instanceof YbNum.Data) assign((YbNum.Data)_o1_);
		else if (_o1_ instanceof YbNum.Const) assign(((YbNum.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(YbNum _o_) {
		_o_._xdb_verify_unsafe_();
		num = _o_.num;
		sysnum = _o_.sysnum;
		nopresentnum = _o_.nopresentnum;
		snlist = new java.util.ArrayList<Long>();
		snlist.addAll(_o_.snlist);
		bindorsysnumtoday = _o_.bindorsysnumtoday;
		bindorsysnumtodaytime = _o_.bindorsysnumtodaytime;
		time = _o_.time;
		fushiall = _o_.fushiall;
	}

	private void assign(YbNum.Data _o_) {
		num = _o_.num;
		sysnum = _o_.sysnum;
		nopresentnum = _o_.nopresentnum;
		snlist = new java.util.ArrayList<Long>();
		snlist.addAll(_o_.snlist);
		bindorsysnumtoday = _o_.bindorsysnumtoday;
		bindorsysnumtodaytime = _o_.bindorsysnumtodaytime;
		time = _o_.time;
		fushiall = _o_.fushiall;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(num);
		_os_.marshal(sysnum);
		_os_.marshal(nopresentnum);
		_os_.compact_uint32(snlist.size());
		for (Long _v_ : snlist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(bindorsysnumtoday);
		_os_.marshal(bindorsysnumtodaytime);
		_os_.marshal(time);
		_os_.marshal(fushiall);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		num = _os_.unmarshal_int();
		sysnum = _os_.unmarshal_int();
		nopresentnum = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			snlist.add(_v_);
		}
		bindorsysnumtoday = _os_.unmarshal_int();
		bindorsysnumtodaytime = _os_.unmarshal_long();
		time = _os_.unmarshal_long();
		fushiall = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.YbNum copy() {
		_xdb_verify_unsafe_();
		return new YbNum(this);
	}

	@Override
	public xbean.YbNum toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.YbNum toBean() {
		_xdb_verify_unsafe_();
		return new YbNum(this); // same as copy()
	}

	@Override
	public xbean.YbNum toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.YbNum toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getNum() { // 现金充值符石数
		_xdb_verify_unsafe_();
		return num;
	}

	@Override
	public int getSysnum() { // 系统赠送符石数
		_xdb_verify_unsafe_();
		return sysnum;
	}

	@Override
	public int getNopresentnum() { // 没有包含赠送的实际充值总符石
		_xdb_verify_unsafe_();
		return nopresentnum;
	}

	@Override
	public java.util.List<Long> getSnlist() { // 玩家充值的序列号
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "snlist"), snlist);
	}

	public java.util.List<Long> getSnlistAsData() { // 玩家充值的序列号
		_xdb_verify_unsafe_();
		java.util.List<Long> snlist;
		YbNum _o_ = this;
		snlist = new java.util.ArrayList<Long>();
		snlist.addAll(_o_.snlist);
		return snlist;
	}

	@Override
	public int getBindorsysnumtoday() { // 该玩家今天获得的符石数量(不包括充值的)
		_xdb_verify_unsafe_();
		return bindorsysnumtoday;
	}

	@Override
	public long getBindorsysnumtodaytime() { // 这个time跟下面time不一样,这个time只有绑定或系统符石增加的时候才会变
		_xdb_verify_unsafe_();
		return bindorsysnumtodaytime;
	}

	@Override
	public long getTime() { // 最近一次符石数量发生变化的时间
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public long getFushiall() { // 累计符石总数，包括所有产出途径的符石数
		_xdb_verify_unsafe_();
		return fushiall;
	}

	@Override
	public void setNum(int _v_) { // 现金充值符石数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "num") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, num) {
					public void rollback() { num = _xdb_saved; }
				};}});
		num = _v_;
	}

	@Override
	public void setSysnum(int _v_) { // 系统赠送符石数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sysnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, sysnum) {
					public void rollback() { sysnum = _xdb_saved; }
				};}});
		sysnum = _v_;
	}

	@Override
	public void setNopresentnum(int _v_) { // 没有包含赠送的实际充值总符石
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "nopresentnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, nopresentnum) {
					public void rollback() { nopresentnum = _xdb_saved; }
				};}});
		nopresentnum = _v_;
	}

	@Override
	public void setBindorsysnumtoday(int _v_) { // 该玩家今天获得的符石数量(不包括充值的)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bindorsysnumtoday") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bindorsysnumtoday) {
					public void rollback() { bindorsysnumtoday = _xdb_saved; }
				};}});
		bindorsysnumtoday = _v_;
	}

	@Override
	public void setBindorsysnumtodaytime(long _v_) { // 这个time跟下面time不一样,这个time只有绑定或系统符石增加的时候才会变
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bindorsysnumtodaytime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, bindorsysnumtodaytime) {
					public void rollback() { bindorsysnumtodaytime = _xdb_saved; }
				};}});
		bindorsysnumtodaytime = _v_;
	}

	@Override
	public void setTime(long _v_) { // 最近一次符石数量发生变化的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "time") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, time) {
					public void rollback() { time = _xdb_saved; }
				};}});
		time = _v_;
	}

	@Override
	public void setFushiall(long _v_) { // 累计符石总数，包括所有产出途径的符石数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fushiall") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, fushiall) {
					public void rollback() { fushiall = _xdb_saved; }
				};}});
		fushiall = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		YbNum _o_ = null;
		if ( _o1_ instanceof YbNum ) _o_ = (YbNum)_o1_;
		else if ( _o1_ instanceof YbNum.Const ) _o_ = ((YbNum.Const)_o1_).nThis();
		else return false;
		if (num != _o_.num) return false;
		if (sysnum != _o_.sysnum) return false;
		if (nopresentnum != _o_.nopresentnum) return false;
		if (!snlist.equals(_o_.snlist)) return false;
		if (bindorsysnumtoday != _o_.bindorsysnumtoday) return false;
		if (bindorsysnumtodaytime != _o_.bindorsysnumtodaytime) return false;
		if (time != _o_.time) return false;
		if (fushiall != _o_.fushiall) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += num;
		_h_ += sysnum;
		_h_ += nopresentnum;
		_h_ += snlist.hashCode();
		_h_ += bindorsysnumtoday;
		_h_ += bindorsysnumtodaytime;
		_h_ += time;
		_h_ += fushiall;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(num);
		_sb_.append(",");
		_sb_.append(sysnum);
		_sb_.append(",");
		_sb_.append(nopresentnum);
		_sb_.append(",");
		_sb_.append(snlist);
		_sb_.append(",");
		_sb_.append(bindorsysnumtoday);
		_sb_.append(",");
		_sb_.append(bindorsysnumtodaytime);
		_sb_.append(",");
		_sb_.append(time);
		_sb_.append(",");
		_sb_.append(fushiall);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("num"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sysnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("nopresentnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("snlist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bindorsysnumtoday"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bindorsysnumtodaytime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fushiall"));
		return lb;
	}

	private class Const implements xbean.YbNum {
		YbNum nThis() {
			return YbNum.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.YbNum copy() {
			return YbNum.this.copy();
		}

		@Override
		public xbean.YbNum toData() {
			return YbNum.this.toData();
		}

		public xbean.YbNum toBean() {
			return YbNum.this.toBean();
		}

		@Override
		public xbean.YbNum toDataIf() {
			return YbNum.this.toDataIf();
		}

		public xbean.YbNum toBeanIf() {
			return YbNum.this.toBeanIf();
		}

		@Override
		public int getNum() { // 现金充值符石数
			_xdb_verify_unsafe_();
			return num;
		}

		@Override
		public int getSysnum() { // 系统赠送符石数
			_xdb_verify_unsafe_();
			return sysnum;
		}

		@Override
		public int getNopresentnum() { // 没有包含赠送的实际充值总符石
			_xdb_verify_unsafe_();
			return nopresentnum;
		}

		@Override
		public java.util.List<Long> getSnlist() { // 玩家充值的序列号
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(snlist);
		}

		public java.util.List<Long> getSnlistAsData() { // 玩家充值的序列号
			_xdb_verify_unsafe_();
			java.util.List<Long> snlist;
			YbNum _o_ = YbNum.this;
		snlist = new java.util.ArrayList<Long>();
		snlist.addAll(_o_.snlist);
			return snlist;
		}

		@Override
		public int getBindorsysnumtoday() { // 该玩家今天获得的符石数量(不包括充值的)
			_xdb_verify_unsafe_();
			return bindorsysnumtoday;
		}

		@Override
		public long getBindorsysnumtodaytime() { // 这个time跟下面time不一样,这个time只有绑定或系统符石增加的时候才会变
			_xdb_verify_unsafe_();
			return bindorsysnumtodaytime;
		}

		@Override
		public long getTime() { // 最近一次符石数量发生变化的时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public long getFushiall() { // 累计符石总数，包括所有产出途径的符石数
			_xdb_verify_unsafe_();
			return fushiall;
		}

		@Override
		public void setNum(int _v_) { // 现金充值符石数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSysnum(int _v_) { // 系统赠送符石数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNopresentnum(int _v_) { // 没有包含赠送的实际充值总符石
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBindorsysnumtoday(int _v_) { // 该玩家今天获得的符石数量(不包括充值的)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBindorsysnumtodaytime(long _v_) { // 这个time跟下面time不一样,这个time只有绑定或系统符石增加的时候才会变
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTime(long _v_) { // 最近一次符石数量发生变化的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFushiall(long _v_) { // 累计符石总数，包括所有产出途径的符石数
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
			return YbNum.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return YbNum.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return YbNum.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return YbNum.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return YbNum.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return YbNum.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return YbNum.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return YbNum.this.hashCode();
		}

		@Override
		public String toString() {
			return YbNum.this.toString();
		}

	}

	public static final class Data implements xbean.YbNum {
		private int num; // 现金充值符石数
		private int sysnum; // 系统赠送符石数
		private int nopresentnum; // 没有包含赠送的实际充值总符石
		private java.util.ArrayList<Long> snlist; // 玩家充值的序列号
		private int bindorsysnumtoday; // 该玩家今天获得的符石数量(不包括充值的)
		private long bindorsysnumtodaytime; // 这个time跟下面time不一样,这个time只有绑定或系统符石增加的时候才会变
		private long time; // 最近一次符石数量发生变化的时间
		private long fushiall; // 累计符石总数，包括所有产出途径的符石数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			snlist = new java.util.ArrayList<Long>();
		}

		Data(xbean.YbNum _o1_) {
			if (_o1_ instanceof YbNum) assign((YbNum)_o1_);
			else if (_o1_ instanceof YbNum.Data) assign((YbNum.Data)_o1_);
			else if (_o1_ instanceof YbNum.Const) assign(((YbNum.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(YbNum _o_) {
			num = _o_.num;
			sysnum = _o_.sysnum;
			nopresentnum = _o_.nopresentnum;
			snlist = new java.util.ArrayList<Long>();
			snlist.addAll(_o_.snlist);
			bindorsysnumtoday = _o_.bindorsysnumtoday;
			bindorsysnumtodaytime = _o_.bindorsysnumtodaytime;
			time = _o_.time;
			fushiall = _o_.fushiall;
		}

		private void assign(YbNum.Data _o_) {
			num = _o_.num;
			sysnum = _o_.sysnum;
			nopresentnum = _o_.nopresentnum;
			snlist = new java.util.ArrayList<Long>();
			snlist.addAll(_o_.snlist);
			bindorsysnumtoday = _o_.bindorsysnumtoday;
			bindorsysnumtodaytime = _o_.bindorsysnumtodaytime;
			time = _o_.time;
			fushiall = _o_.fushiall;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(num);
			_os_.marshal(sysnum);
			_os_.marshal(nopresentnum);
			_os_.compact_uint32(snlist.size());
			for (Long _v_ : snlist) {
				_os_.marshal(_v_);
			}
			_os_.marshal(bindorsysnumtoday);
			_os_.marshal(bindorsysnumtodaytime);
			_os_.marshal(time);
			_os_.marshal(fushiall);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			num = _os_.unmarshal_int();
			sysnum = _os_.unmarshal_int();
			nopresentnum = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				snlist.add(_v_);
			}
			bindorsysnumtoday = _os_.unmarshal_int();
			bindorsysnumtodaytime = _os_.unmarshal_long();
			time = _os_.unmarshal_long();
			fushiall = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.YbNum copy() {
			return new Data(this);
		}

		@Override
		public xbean.YbNum toData() {
			return new Data(this);
		}

		public xbean.YbNum toBean() {
			return new YbNum(this, null, null);
		}

		@Override
		public xbean.YbNum toDataIf() {
			return this;
		}

		public xbean.YbNum toBeanIf() {
			return new YbNum(this, null, null);
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
		public int getNum() { // 现金充值符石数
			return num;
		}

		@Override
		public int getSysnum() { // 系统赠送符石数
			return sysnum;
		}

		@Override
		public int getNopresentnum() { // 没有包含赠送的实际充值总符石
			return nopresentnum;
		}

		@Override
		public java.util.List<Long> getSnlist() { // 玩家充值的序列号
			return snlist;
		}

		@Override
		public java.util.List<Long> getSnlistAsData() { // 玩家充值的序列号
			return snlist;
		}

		@Override
		public int getBindorsysnumtoday() { // 该玩家今天获得的符石数量(不包括充值的)
			return bindorsysnumtoday;
		}

		@Override
		public long getBindorsysnumtodaytime() { // 这个time跟下面time不一样,这个time只有绑定或系统符石增加的时候才会变
			return bindorsysnumtodaytime;
		}

		@Override
		public long getTime() { // 最近一次符石数量发生变化的时间
			return time;
		}

		@Override
		public long getFushiall() { // 累计符石总数，包括所有产出途径的符石数
			return fushiall;
		}

		@Override
		public void setNum(int _v_) { // 现金充值符石数
			num = _v_;
		}

		@Override
		public void setSysnum(int _v_) { // 系统赠送符石数
			sysnum = _v_;
		}

		@Override
		public void setNopresentnum(int _v_) { // 没有包含赠送的实际充值总符石
			nopresentnum = _v_;
		}

		@Override
		public void setBindorsysnumtoday(int _v_) { // 该玩家今天获得的符石数量(不包括充值的)
			bindorsysnumtoday = _v_;
		}

		@Override
		public void setBindorsysnumtodaytime(long _v_) { // 这个time跟下面time不一样,这个time只有绑定或系统符石增加的时候才会变
			bindorsysnumtodaytime = _v_;
		}

		@Override
		public void setTime(long _v_) { // 最近一次符石数量发生变化的时间
			time = _v_;
		}

		@Override
		public void setFushiall(long _v_) { // 累计符石总数，包括所有产出途径的符石数
			fushiall = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof YbNum.Data)) return false;
			YbNum.Data _o_ = (YbNum.Data) _o1_;
			if (num != _o_.num) return false;
			if (sysnum != _o_.sysnum) return false;
			if (nopresentnum != _o_.nopresentnum) return false;
			if (!snlist.equals(_o_.snlist)) return false;
			if (bindorsysnumtoday != _o_.bindorsysnumtoday) return false;
			if (bindorsysnumtodaytime != _o_.bindorsysnumtodaytime) return false;
			if (time != _o_.time) return false;
			if (fushiall != _o_.fushiall) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += num;
			_h_ += sysnum;
			_h_ += nopresentnum;
			_h_ += snlist.hashCode();
			_h_ += bindorsysnumtoday;
			_h_ += bindorsysnumtodaytime;
			_h_ += time;
			_h_ += fushiall;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(num);
			_sb_.append(",");
			_sb_.append(sysnum);
			_sb_.append(",");
			_sb_.append(nopresentnum);
			_sb_.append(",");
			_sb_.append(snlist);
			_sb_.append(",");
			_sb_.append(bindorsysnumtoday);
			_sb_.append(",");
			_sb_.append(bindorsysnumtodaytime);
			_sb_.append(",");
			_sb_.append(time);
			_sb_.append(",");
			_sb_.append(fushiall);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
