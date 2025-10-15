
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleSpace extends mkdb.XBean implements xbean.RoleSpace {
	private int gift; // 空间礼物
	private int popularity; // 人气
	private int recvgift; // 收到的礼物
	private int getgiftnum; // 踩空间获得礼物的次数
	private long getgifttime; // 上次踩空间获得礼物的时间

	@Override
	public void _reset_unsafe_() {
		gift = 0;
		popularity = 0;
		recvgift = 0;
		getgiftnum = 0;
		getgifttime = 0L;
	}

	RoleSpace(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RoleSpace() {
		this(0, null, null);
	}

	public RoleSpace(RoleSpace _o_) {
		this(_o_, null, null);
	}

	RoleSpace(xbean.RoleSpace _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleSpace) assign((RoleSpace)_o1_);
		else if (_o1_ instanceof RoleSpace.Data) assign((RoleSpace.Data)_o1_);
		else if (_o1_ instanceof RoleSpace.Const) assign(((RoleSpace.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleSpace _o_) {
		_o_._xdb_verify_unsafe_();
		gift = _o_.gift;
		popularity = _o_.popularity;
		recvgift = _o_.recvgift;
		getgiftnum = _o_.getgiftnum;
		getgifttime = _o_.getgifttime;
	}

	private void assign(RoleSpace.Data _o_) {
		gift = _o_.gift;
		popularity = _o_.popularity;
		recvgift = _o_.recvgift;
		getgiftnum = _o_.getgiftnum;
		getgifttime = _o_.getgifttime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(gift);
		_os_.marshal(popularity);
		_os_.marshal(recvgift);
		_os_.marshal(getgiftnum);
		_os_.marshal(getgifttime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		gift = _os_.unmarshal_int();
		popularity = _os_.unmarshal_int();
		recvgift = _os_.unmarshal_int();
		getgiftnum = _os_.unmarshal_int();
		getgifttime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RoleSpace copy() {
		_xdb_verify_unsafe_();
		return new RoleSpace(this);
	}

	@Override
	public xbean.RoleSpace toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleSpace toBean() {
		_xdb_verify_unsafe_();
		return new RoleSpace(this); // same as copy()
	}

	@Override
	public xbean.RoleSpace toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleSpace toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getGift() { // 空间礼物
		_xdb_verify_unsafe_();
		return gift;
	}

	@Override
	public int getPopularity() { // 人气
		_xdb_verify_unsafe_();
		return popularity;
	}

	@Override
	public int getRecvgift() { // 收到的礼物
		_xdb_verify_unsafe_();
		return recvgift;
	}

	@Override
	public int getGetgiftnum() { // 踩空间获得礼物的次数
		_xdb_verify_unsafe_();
		return getgiftnum;
	}

	@Override
	public long getGetgifttime() { // 上次踩空间获得礼物的时间
		_xdb_verify_unsafe_();
		return getgifttime;
	}

	@Override
	public void setGift(int _v_) { // 空间礼物
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "gift") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, gift) {
					public void rollback() { gift = _xdb_saved; }
				};}});
		gift = _v_;
	}

	@Override
	public void setPopularity(int _v_) { // 人气
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "popularity") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, popularity) {
					public void rollback() { popularity = _xdb_saved; }
				};}});
		popularity = _v_;
	}

	@Override
	public void setRecvgift(int _v_) { // 收到的礼物
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "recvgift") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, recvgift) {
					public void rollback() { recvgift = _xdb_saved; }
				};}});
		recvgift = _v_;
	}

	@Override
	public void setGetgiftnum(int _v_) { // 踩空间获得礼物的次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "getgiftnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, getgiftnum) {
					public void rollback() { getgiftnum = _xdb_saved; }
				};}});
		getgiftnum = _v_;
	}

	@Override
	public void setGetgifttime(long _v_) { // 上次踩空间获得礼物的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "getgifttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, getgifttime) {
					public void rollback() { getgifttime = _xdb_saved; }
				};}});
		getgifttime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleSpace _o_ = null;
		if ( _o1_ instanceof RoleSpace ) _o_ = (RoleSpace)_o1_;
		else if ( _o1_ instanceof RoleSpace.Const ) _o_ = ((RoleSpace.Const)_o1_).nThis();
		else return false;
		if (gift != _o_.gift) return false;
		if (popularity != _o_.popularity) return false;
		if (recvgift != _o_.recvgift) return false;
		if (getgiftnum != _o_.getgiftnum) return false;
		if (getgifttime != _o_.getgifttime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += gift;
		_h_ += popularity;
		_h_ += recvgift;
		_h_ += getgiftnum;
		_h_ += getgifttime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(gift);
		_sb_.append(",");
		_sb_.append(popularity);
		_sb_.append(",");
		_sb_.append(recvgift);
		_sb_.append(",");
		_sb_.append(getgiftnum);
		_sb_.append(",");
		_sb_.append(getgifttime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("gift"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("popularity"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("recvgift"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("getgiftnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("getgifttime"));
		return lb;
	}

	private class Const implements xbean.RoleSpace {
		RoleSpace nThis() {
			return RoleSpace.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleSpace copy() {
			return RoleSpace.this.copy();
		}

		@Override
		public xbean.RoleSpace toData() {
			return RoleSpace.this.toData();
		}

		public xbean.RoleSpace toBean() {
			return RoleSpace.this.toBean();
		}

		@Override
		public xbean.RoleSpace toDataIf() {
			return RoleSpace.this.toDataIf();
		}

		public xbean.RoleSpace toBeanIf() {
			return RoleSpace.this.toBeanIf();
		}

		@Override
		public int getGift() { // 空间礼物
			_xdb_verify_unsafe_();
			return gift;
		}

		@Override
		public int getPopularity() { // 人气
			_xdb_verify_unsafe_();
			return popularity;
		}

		@Override
		public int getRecvgift() { // 收到的礼物
			_xdb_verify_unsafe_();
			return recvgift;
		}

		@Override
		public int getGetgiftnum() { // 踩空间获得礼物的次数
			_xdb_verify_unsafe_();
			return getgiftnum;
		}

		@Override
		public long getGetgifttime() { // 上次踩空间获得礼物的时间
			_xdb_verify_unsafe_();
			return getgifttime;
		}

		@Override
		public void setGift(int _v_) { // 空间礼物
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPopularity(int _v_) { // 人气
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRecvgift(int _v_) { // 收到的礼物
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGetgiftnum(int _v_) { // 踩空间获得礼物的次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGetgifttime(long _v_) { // 上次踩空间获得礼物的时间
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
			return RoleSpace.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleSpace.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleSpace.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleSpace.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleSpace.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleSpace.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleSpace.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleSpace.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleSpace.this.toString();
		}

	}

	public static final class Data implements xbean.RoleSpace {
		private int gift; // 空间礼物
		private int popularity; // 人气
		private int recvgift; // 收到的礼物
		private int getgiftnum; // 踩空间获得礼物的次数
		private long getgifttime; // 上次踩空间获得礼物的时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RoleSpace _o1_) {
			if (_o1_ instanceof RoleSpace) assign((RoleSpace)_o1_);
			else if (_o1_ instanceof RoleSpace.Data) assign((RoleSpace.Data)_o1_);
			else if (_o1_ instanceof RoleSpace.Const) assign(((RoleSpace.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleSpace _o_) {
			gift = _o_.gift;
			popularity = _o_.popularity;
			recvgift = _o_.recvgift;
			getgiftnum = _o_.getgiftnum;
			getgifttime = _o_.getgifttime;
		}

		private void assign(RoleSpace.Data _o_) {
			gift = _o_.gift;
			popularity = _o_.popularity;
			recvgift = _o_.recvgift;
			getgiftnum = _o_.getgiftnum;
			getgifttime = _o_.getgifttime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(gift);
			_os_.marshal(popularity);
			_os_.marshal(recvgift);
			_os_.marshal(getgiftnum);
			_os_.marshal(getgifttime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			gift = _os_.unmarshal_int();
			popularity = _os_.unmarshal_int();
			recvgift = _os_.unmarshal_int();
			getgiftnum = _os_.unmarshal_int();
			getgifttime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RoleSpace copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleSpace toData() {
			return new Data(this);
		}

		public xbean.RoleSpace toBean() {
			return new RoleSpace(this, null, null);
		}

		@Override
		public xbean.RoleSpace toDataIf() {
			return this;
		}

		public xbean.RoleSpace toBeanIf() {
			return new RoleSpace(this, null, null);
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
		public int getGift() { // 空间礼物
			return gift;
		}

		@Override
		public int getPopularity() { // 人气
			return popularity;
		}

		@Override
		public int getRecvgift() { // 收到的礼物
			return recvgift;
		}

		@Override
		public int getGetgiftnum() { // 踩空间获得礼物的次数
			return getgiftnum;
		}

		@Override
		public long getGetgifttime() { // 上次踩空间获得礼物的时间
			return getgifttime;
		}

		@Override
		public void setGift(int _v_) { // 空间礼物
			gift = _v_;
		}

		@Override
		public void setPopularity(int _v_) { // 人气
			popularity = _v_;
		}

		@Override
		public void setRecvgift(int _v_) { // 收到的礼物
			recvgift = _v_;
		}

		@Override
		public void setGetgiftnum(int _v_) { // 踩空间获得礼物的次数
			getgiftnum = _v_;
		}

		@Override
		public void setGetgifttime(long _v_) { // 上次踩空间获得礼物的时间
			getgifttime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleSpace.Data)) return false;
			RoleSpace.Data _o_ = (RoleSpace.Data) _o1_;
			if (gift != _o_.gift) return false;
			if (popularity != _o_.popularity) return false;
			if (recvgift != _o_.recvgift) return false;
			if (getgiftnum != _o_.getgiftnum) return false;
			if (getgifttime != _o_.getgifttime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += gift;
			_h_ += popularity;
			_h_ += recvgift;
			_h_ += getgiftnum;
			_h_ += getgifttime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(gift);
			_sb_.append(",");
			_sb_.append(popularity);
			_sb_.append(",");
			_sb_.append(recvgift);
			_sb_.append(",");
			_sb_.append(getgiftnum);
			_sb_.append(",");
			_sb_.append(getgifttime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
