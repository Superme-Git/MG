
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class NumberAndTime extends mkdb.XBean implements xbean.NumberAndTime {
	private int number; // 购买或售卖次数
	private long time; // 最后购买或售卖时间

	@Override
	public void _reset_unsafe_() {
		number = 0;
		time = 0L;
	}

	NumberAndTime(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public NumberAndTime() {
		this(0, null, null);
	}

	public NumberAndTime(NumberAndTime _o_) {
		this(_o_, null, null);
	}

	NumberAndTime(xbean.NumberAndTime _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof NumberAndTime) assign((NumberAndTime)_o1_);
		else if (_o1_ instanceof NumberAndTime.Data) assign((NumberAndTime.Data)_o1_);
		else if (_o1_ instanceof NumberAndTime.Const) assign(((NumberAndTime.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(NumberAndTime _o_) {
		_o_._xdb_verify_unsafe_();
		number = _o_.number;
		time = _o_.time;
	}

	private void assign(NumberAndTime.Data _o_) {
		number = _o_.number;
		time = _o_.time;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(number);
		_os_.marshal(time);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		number = _os_.unmarshal_int();
		time = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.NumberAndTime copy() {
		_xdb_verify_unsafe_();
		return new NumberAndTime(this);
	}

	@Override
	public xbean.NumberAndTime toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NumberAndTime toBean() {
		_xdb_verify_unsafe_();
		return new NumberAndTime(this); // same as copy()
	}

	@Override
	public xbean.NumberAndTime toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NumberAndTime toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getNumber() { // 购买或售卖次数
		_xdb_verify_unsafe_();
		return number;
	}

	@Override
	public long getTime() { // 最后购买或售卖时间
		_xdb_verify_unsafe_();
		return time;
	}

	@Override
	public void setNumber(int _v_) { // 购买或售卖次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "number") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, number) {
					public void rollback() { number = _xdb_saved; }
				};}});
		number = _v_;
	}

	@Override
	public void setTime(long _v_) { // 最后购买或售卖时间
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
		NumberAndTime _o_ = null;
		if ( _o1_ instanceof NumberAndTime ) _o_ = (NumberAndTime)_o1_;
		else if ( _o1_ instanceof NumberAndTime.Const ) _o_ = ((NumberAndTime.Const)_o1_).nThis();
		else return false;
		if (number != _o_.number) return false;
		if (time != _o_.time) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += number;
		_h_ += time;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(number);
		_sb_.append(",");
		_sb_.append(time);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("number"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("time"));
		return lb;
	}

	private class Const implements xbean.NumberAndTime {
		NumberAndTime nThis() {
			return NumberAndTime.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.NumberAndTime copy() {
			return NumberAndTime.this.copy();
		}

		@Override
		public xbean.NumberAndTime toData() {
			return NumberAndTime.this.toData();
		}

		public xbean.NumberAndTime toBean() {
			return NumberAndTime.this.toBean();
		}

		@Override
		public xbean.NumberAndTime toDataIf() {
			return NumberAndTime.this.toDataIf();
		}

		public xbean.NumberAndTime toBeanIf() {
			return NumberAndTime.this.toBeanIf();
		}

		@Override
		public int getNumber() { // 购买或售卖次数
			_xdb_verify_unsafe_();
			return number;
		}

		@Override
		public long getTime() { // 最后购买或售卖时间
			_xdb_verify_unsafe_();
			return time;
		}

		@Override
		public void setNumber(int _v_) { // 购买或售卖次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTime(long _v_) { // 最后购买或售卖时间
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
			return NumberAndTime.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return NumberAndTime.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return NumberAndTime.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return NumberAndTime.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return NumberAndTime.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return NumberAndTime.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return NumberAndTime.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return NumberAndTime.this.hashCode();
		}

		@Override
		public String toString() {
			return NumberAndTime.this.toString();
		}

	}

	public static final class Data implements xbean.NumberAndTime {
		private int number; // 购买或售卖次数
		private long time; // 最后购买或售卖时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.NumberAndTime _o1_) {
			if (_o1_ instanceof NumberAndTime) assign((NumberAndTime)_o1_);
			else if (_o1_ instanceof NumberAndTime.Data) assign((NumberAndTime.Data)_o1_);
			else if (_o1_ instanceof NumberAndTime.Const) assign(((NumberAndTime.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(NumberAndTime _o_) {
			number = _o_.number;
			time = _o_.time;
		}

		private void assign(NumberAndTime.Data _o_) {
			number = _o_.number;
			time = _o_.time;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(number);
			_os_.marshal(time);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			number = _os_.unmarshal_int();
			time = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.NumberAndTime copy() {
			return new Data(this);
		}

		@Override
		public xbean.NumberAndTime toData() {
			return new Data(this);
		}

		public xbean.NumberAndTime toBean() {
			return new NumberAndTime(this, null, null);
		}

		@Override
		public xbean.NumberAndTime toDataIf() {
			return this;
		}

		public xbean.NumberAndTime toBeanIf() {
			return new NumberAndTime(this, null, null);
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
		public int getNumber() { // 购买或售卖次数
			return number;
		}

		@Override
		public long getTime() { // 最后购买或售卖时间
			return time;
		}

		@Override
		public void setNumber(int _v_) { // 购买或售卖次数
			number = _v_;
		}

		@Override
		public void setTime(long _v_) { // 最后购买或售卖时间
			time = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof NumberAndTime.Data)) return false;
			NumberAndTime.Data _o_ = (NumberAndTime.Data) _o1_;
			if (number != _o_.number) return false;
			if (time != _o_.time) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += number;
			_h_ += time;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(number);
			_sb_.append(",");
			_sb_.append(time);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
