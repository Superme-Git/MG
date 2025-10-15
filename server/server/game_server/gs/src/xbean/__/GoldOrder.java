
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class GoldOrder extends mkdb.XBean implements xbean.GoldOrder {
	private long pid; // 订单编号
	private long number; // 金币数量, 整数
	private long price; // 出售价格, 整数, 单位人民币分
	private int publicity; // 公示时间, 整数, 单位小时.如果不需要公示传0
	private int locktime; // 锁定订单时间,单位秒.锁定时不能下架
	private int state; // 订单状态, 1在售, 2锁定, 3已售, 4待领取, 5领取完成
	private long time; // 订单创建时间,单位毫秒

	@Override
	public void _reset_unsafe_() {
		pid = 0;
		number = 0;
		price = 0;
		publicity = 0;
		locktime = 0;
		state = 0;
		time = 0;
	}

	GoldOrder(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		pid = 0;
		number = 0;
		price = 0;
		publicity = 0;
		locktime = 0;
		state = 0;
		time = 0;
	}

	public GoldOrder() {
		this(0, null, null);
	}

	public GoldOrder(GoldOrder _o_) {
		this(_o_, null, null);
	}

	GoldOrder(xbean.GoldOrder _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof GoldOrder) assign((GoldOrder)_o1_);
		else if (_o1_ instanceof GoldOrder.Data) assign((GoldOrder.Data)_o1_);
		else if (_o1_ instanceof GoldOrder.Const) assign(((GoldOrder.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(GoldOrder _o_) {
		_o_._xdb_verify_unsafe_();
		pid = _o_.pid;
		number = _o_.number;
		price = _o_.price;
		publicity = _o_.publicity;
		locktime = _o_.locktime;
		state = _o_.state;
		time = _o_.time;
	}

	private void assign(GoldOrder.Data _o_) {
		pid = _o_.pid;
		number = _o_.number;
		price = _o_.price;
		publicity = _o_.publicity;
		locktime = _o_.locktime;
		state = _o_.state;
		time = _o_.time;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(pid);
		_os_.marshal(number);
		_os_.marshal(price);
		_os_.marshal(publicity);
		_os_.marshal(locktime);
		_os_.marshal(state);
		_os_.marshal(time);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		pid = _os_.unmarshal_long();
		number = _os_.unmarshal_long();
		price = _os_.unmarshal_long();
		publicity = _os_.unmarshal_int();
		locktime = _os_.unmarshal_int();
		state = _os_.unmarshal_int();
		time = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.GoldOrder copy() {
		_xdb_verify_unsafe_();
		return new GoldOrder(this);
	}

	@Override
	public xbean.GoldOrder toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GoldOrder toBean() {
		_xdb_verify_unsafe_();
		return new GoldOrder(this); // same as copy()
	}

	@Override
	public xbean.GoldOrder toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.GoldOrder toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getPid() { // 订单编号
		_xdb_verify_unsafe_();
		return pid;
	}

	@Override
	public long getNumber() { // 金币数量, 整数
		_xdb_verify_unsafe_();
		return number;
	}

	@Override
	public long getPrice() { // 出售价格, 整数, 单位人民币分
		_xdb_verify_unsafe_();
		return price;
	}

	@Override
	public int getPublicity() { // 公示时间, 整数, 单位小时.如果不需要公示传0
		_xdb_verify_unsafe_();
		return publicity;
	}

	@Override
	public int getLocktime() { // 锁定订单时间,单位秒.锁定时不能下架
		_xdb_verify_unsafe_();
		return locktime;
	}

	@Override
	public int getState() { // 订单状态, 1在售, 2锁定, 3已售, 4待领取, 5领取完成
		_xdb_verify_unsafe_();
		return state;
	}

	@Override
	public long getTime() { // 订单创建时间,单位毫秒
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public void setPid(long _v_) { // 订单编号
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "pid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, pid) {
					public void rollback() { pid = _xdb_saved; }
				};}});
		pid = _v_;
	}

	@Override
	public void setNumber(long _v_) { // 金币数量, 整数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "number") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, number) {
					public void rollback() { number = _xdb_saved; }
				};}});
		number = _v_;
	}

	@Override
	public void setPrice(long _v_) { // 出售价格, 整数, 单位人民币分
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "price") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, price) {
					public void rollback() { price = _xdb_saved; }
				};}});
		price = _v_;
	}

	@Override
	public void setPublicity(int _v_) { // 公示时间, 整数, 单位小时.如果不需要公示传0
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "publicity") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, publicity) {
					public void rollback() { publicity = _xdb_saved; }
				};}});
		publicity = _v_;
	}

	@Override
	public void setLocktime(int _v_) { // 锁定订单时间,单位秒.锁定时不能下架
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "locktime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, locktime) {
					public void rollback() { locktime = _xdb_saved; }
				};}});
		locktime = _v_;
	}

	@Override
	public void setState(int _v_) { // 订单状态, 1在售, 2锁定, 3已售, 4待领取, 5领取完成
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public void setTime(long _v_) { // 订单创建时间,单位毫秒
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "time") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, time) {
					public void rollback() { time = _xdb_saved; }
				};}});
		time = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		GoldOrder _o_ = null;
		if ( _o1_ instanceof GoldOrder ) _o_ = (GoldOrder)_o1_;
		else if ( _o1_ instanceof GoldOrder.Const ) _o_ = ((GoldOrder.Const)_o1_).nThis();
		else return false;
		if (pid != _o_.pid) return false;
		if (number != _o_.number) return false;
		if (price != _o_.price) return false;
		if (publicity != _o_.publicity) return false;
		if (locktime != _o_.locktime) return false;
		if (state != _o_.state) return false;
		if (time != _o_.time) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += pid;
		_h_ += number;
		_h_ += price;
		_h_ += publicity;
		_h_ += locktime;
		_h_ += state;
		_h_ += time;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pid);
		_sb_.append(",");
		_sb_.append(number);
		_sb_.append(",");
		_sb_.append(price);
		_sb_.append(",");
		_sb_.append(publicity);
		_sb_.append(",");
		_sb_.append(locktime);
		_sb_.append(",");
		_sb_.append(state);
		_sb_.append(",");
		_sb_.append(time);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("pid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("number"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("price"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("publicity"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("locktime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		return lb;
	}

	private class Const implements xbean.GoldOrder {
		GoldOrder nThis() {
			return GoldOrder.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.GoldOrder copy() {
			return GoldOrder.this.copy();
		}

		@Override
		public xbean.GoldOrder toData() {
			return GoldOrder.this.toData();
		}

		public xbean.GoldOrder toBean() {
			return GoldOrder.this.toBean();
		}

		@Override
		public xbean.GoldOrder toDataIf() {
			return GoldOrder.this.toDataIf();
		}

		public xbean.GoldOrder toBeanIf() {
			return GoldOrder.this.toBeanIf();
		}

		@Override
		public long getPid() { // 订单编号
			_xdb_verify_unsafe_();
			return pid;
		}

		@Override
		public long getNumber() { // 金币数量, 整数
			_xdb_verify_unsafe_();
			return number;
		}

		@Override
		public long getPrice() { // 出售价格, 整数, 单位人民币分
			_xdb_verify_unsafe_();
			return price;
		}

		@Override
		public int getPublicity() { // 公示时间, 整数, 单位小时.如果不需要公示传0
			_xdb_verify_unsafe_();
			return publicity;
		}

		@Override
		public int getLocktime() { // 锁定订单时间,单位秒.锁定时不能下架
			_xdb_verify_unsafe_();
			return locktime;
		}

		@Override
		public int getState() { // 订单状态, 1在售, 2锁定, 3已售, 4待领取, 5领取完成
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public long getTime() { // 订单创建时间,单位毫秒
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public void setPid(long _v_) { // 订单编号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNumber(long _v_) { // 金币数量, 整数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPrice(long _v_) { // 出售价格, 整数, 单位人民币分
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPublicity(int _v_) { // 公示时间, 整数, 单位小时.如果不需要公示传0
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLocktime(int _v_) { // 锁定订单时间,单位秒.锁定时不能下架
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setState(int _v_) { // 订单状态, 1在售, 2锁定, 3已售, 4待领取, 5领取完成
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTime(long _v_) { // 订单创建时间,单位毫秒
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
			return GoldOrder.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return GoldOrder.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return GoldOrder.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return GoldOrder.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return GoldOrder.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return GoldOrder.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return GoldOrder.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return GoldOrder.this.hashCode();
		}

		@Override
		public String toString() {
			return GoldOrder.this.toString();
		}

	}

	public static final class Data implements xbean.GoldOrder {
		private long pid; // 订单编号
		private long number; // 金币数量, 整数
		private long price; // 出售价格, 整数, 单位人民币分
		private int publicity; // 公示时间, 整数, 单位小时.如果不需要公示传0
		private int locktime; // 锁定订单时间,单位秒.锁定时不能下架
		private int state; // 订单状态, 1在售, 2锁定, 3已售, 4待领取, 5领取完成
		private long time; // 订单创建时间,单位毫秒

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			pid = 0;
			number = 0;
			price = 0;
			publicity = 0;
			locktime = 0;
			state = 0;
			time = 0;
		}

		Data(xbean.GoldOrder _o1_) {
			if (_o1_ instanceof GoldOrder) assign((GoldOrder)_o1_);
			else if (_o1_ instanceof GoldOrder.Data) assign((GoldOrder.Data)_o1_);
			else if (_o1_ instanceof GoldOrder.Const) assign(((GoldOrder.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(GoldOrder _o_) {
			pid = _o_.pid;
			number = _o_.number;
			price = _o_.price;
			publicity = _o_.publicity;
			locktime = _o_.locktime;
			state = _o_.state;
			time = _o_.time;
		}

		private void assign(GoldOrder.Data _o_) {
			pid = _o_.pid;
			number = _o_.number;
			price = _o_.price;
			publicity = _o_.publicity;
			locktime = _o_.locktime;
			state = _o_.state;
			time = _o_.time;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(pid);
			_os_.marshal(number);
			_os_.marshal(price);
			_os_.marshal(publicity);
			_os_.marshal(locktime);
			_os_.marshal(state);
			_os_.marshal(time);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			pid = _os_.unmarshal_long();
			number = _os_.unmarshal_long();
			price = _os_.unmarshal_long();
			publicity = _os_.unmarshal_int();
			locktime = _os_.unmarshal_int();
			state = _os_.unmarshal_int();
			time = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.GoldOrder copy() {
			return new Data(this);
		}

		@Override
		public xbean.GoldOrder toData() {
			return new Data(this);
		}

		public xbean.GoldOrder toBean() {
			return new GoldOrder(this, null, null);
		}

		@Override
		public xbean.GoldOrder toDataIf() {
			return this;
		}

		public xbean.GoldOrder toBeanIf() {
			return new GoldOrder(this, null, null);
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
		public long getPid() { // 订单编号
			return pid;
		}

		@Override
		public long getNumber() { // 金币数量, 整数
			return number;
		}

		@Override
		public long getPrice() { // 出售价格, 整数, 单位人民币分
			return price;
		}

		@Override
		public int getPublicity() { // 公示时间, 整数, 单位小时.如果不需要公示传0
			return publicity;
		}

		@Override
		public int getLocktime() { // 锁定订单时间,单位秒.锁定时不能下架
			return locktime;
		}

		@Override
		public int getState() { // 订单状态, 1在售, 2锁定, 3已售, 4待领取, 5领取完成
			return state;
		}

		@Override
		public long getTime() { // 订单创建时间,单位毫秒
			return time;
		}

		@Override
		public void setPid(long _v_) { // 订单编号
			pid = _v_;
		}

		@Override
		public void setNumber(long _v_) { // 金币数量, 整数
			number = _v_;
		}

		@Override
		public void setPrice(long _v_) { // 出售价格, 整数, 单位人民币分
			price = _v_;
		}

		@Override
		public void setPublicity(int _v_) { // 公示时间, 整数, 单位小时.如果不需要公示传0
			publicity = _v_;
		}

		@Override
		public void setLocktime(int _v_) { // 锁定订单时间,单位秒.锁定时不能下架
			locktime = _v_;
		}

		@Override
		public void setState(int _v_) { // 订单状态, 1在售, 2锁定, 3已售, 4待领取, 5领取完成
			state = _v_;
		}

		@Override
		public void setTime(long _v_) { // 订单创建时间,单位毫秒
			time = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof GoldOrder.Data)) return false;
			GoldOrder.Data _o_ = (GoldOrder.Data) _o1_;
			if (pid != _o_.pid) return false;
			if (number != _o_.number) return false;
			if (price != _o_.price) return false;
			if (publicity != _o_.publicity) return false;
			if (locktime != _o_.locktime) return false;
			if (state != _o_.state) return false;
			if (time != _o_.time) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += pid;
			_h_ += number;
			_h_ += price;
			_h_ += publicity;
			_h_ += locktime;
			_h_ += state;
			_h_ += time;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(pid);
			_sb_.append(",");
			_sb_.append(number);
			_sb_.append(",");
			_sb_.append(price);
			_sb_.append(",");
			_sb_.append(publicity);
			_sb_.append(",");
			_sb_.append(locktime);
			_sb_.append(",");
			_sb_.append(state);
			_sb_.append(",");
			_sb_.append(time);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
