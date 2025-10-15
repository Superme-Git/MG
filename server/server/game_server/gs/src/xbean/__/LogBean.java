
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class LogBean extends mkdb.XBean implements xbean.LogBean {
	private int itemid; // 
	private int level; // 
	private int number; // 
	private int money; // 

	@Override
	public void _reset_unsafe_() {
		itemid = 0;
		level = 0;
		number = 0;
		money = 0;
	}

	LogBean(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public LogBean() {
		this(0, null, null);
	}

	public LogBean(LogBean _o_) {
		this(_o_, null, null);
	}

	LogBean(xbean.LogBean _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof LogBean) assign((LogBean)_o1_);
		else if (_o1_ instanceof LogBean.Data) assign((LogBean.Data)_o1_);
		else if (_o1_ instanceof LogBean.Const) assign(((LogBean.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(LogBean _o_) {
		_o_._xdb_verify_unsafe_();
		itemid = _o_.itemid;
		level = _o_.level;
		number = _o_.number;
		money = _o_.money;
	}

	private void assign(LogBean.Data _o_) {
		itemid = _o_.itemid;
		level = _o_.level;
		number = _o_.number;
		money = _o_.money;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(itemid);
		_os_.marshal(level);
		_os_.marshal(number);
		_os_.marshal(money);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		itemid = _os_.unmarshal_int();
		level = _os_.unmarshal_int();
		number = _os_.unmarshal_int();
		money = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.LogBean copy() {
		_xdb_verify_unsafe_();
		return new LogBean(this);
	}

	@Override
	public xbean.LogBean toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LogBean toBean() {
		_xdb_verify_unsafe_();
		return new LogBean(this); // same as copy()
	}

	@Override
	public xbean.LogBean toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.LogBean toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getItemid() { // 
		_xdb_verify_unsafe_();
		return itemid;
	}

	@Override
	public int getLevel() { // 
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getNumber() { // 
		_xdb_verify_unsafe_();
		return number;
	}

	@Override
	public int getMoney() { // 
		_xdb_verify_unsafe_();
		return money;
	}

	@Override
	public void setItemid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemid) {
					public void rollback() { itemid = _xdb_saved; }
				};}});
		itemid = _v_;
	}

	@Override
	public void setLevel(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setNumber(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "number") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, number) {
					public void rollback() { number = _xdb_saved; }
				};}});
		number = _v_;
	}

	@Override
	public void setMoney(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "money") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, money) {
					public void rollback() { money = _xdb_saved; }
				};}});
		money = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		LogBean _o_ = null;
		if ( _o1_ instanceof LogBean ) _o_ = (LogBean)_o1_;
		else if ( _o1_ instanceof LogBean.Const ) _o_ = ((LogBean.Const)_o1_).nThis();
		else return false;
		if (itemid != _o_.itemid) return false;
		if (level != _o_.level) return false;
		if (number != _o_.number) return false;
		if (money != _o_.money) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += itemid;
		_h_ += level;
		_h_ += number;
		_h_ += money;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemid);
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(number);
		_sb_.append(",");
		_sb_.append(money);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("number"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("money"));
		return lb;
	}

	private class Const implements xbean.LogBean {
		LogBean nThis() {
			return LogBean.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.LogBean copy() {
			return LogBean.this.copy();
		}

		@Override
		public xbean.LogBean toData() {
			return LogBean.this.toData();
		}

		public xbean.LogBean toBean() {
			return LogBean.this.toBean();
		}

		@Override
		public xbean.LogBean toDataIf() {
			return LogBean.this.toDataIf();
		}

		public xbean.LogBean toBeanIf() {
			return LogBean.this.toBeanIf();
		}

		@Override
		public int getItemid() { // 
			_xdb_verify_unsafe_();
			return itemid;
		}

		@Override
		public int getLevel() { // 
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getNumber() { // 
			_xdb_verify_unsafe_();
			return number;
		}

		@Override
		public int getMoney() { // 
			_xdb_verify_unsafe_();
			return money;
		}

		@Override
		public void setItemid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNumber(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMoney(int _v_) { // 
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
			return LogBean.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return LogBean.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return LogBean.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return LogBean.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return LogBean.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return LogBean.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return LogBean.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return LogBean.this.hashCode();
		}

		@Override
		public String toString() {
			return LogBean.this.toString();
		}

	}

	public static final class Data implements xbean.LogBean {
		private int itemid; // 
		private int level; // 
		private int number; // 
		private int money; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.LogBean _o1_) {
			if (_o1_ instanceof LogBean) assign((LogBean)_o1_);
			else if (_o1_ instanceof LogBean.Data) assign((LogBean.Data)_o1_);
			else if (_o1_ instanceof LogBean.Const) assign(((LogBean.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(LogBean _o_) {
			itemid = _o_.itemid;
			level = _o_.level;
			number = _o_.number;
			money = _o_.money;
		}

		private void assign(LogBean.Data _o_) {
			itemid = _o_.itemid;
			level = _o_.level;
			number = _o_.number;
			money = _o_.money;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(itemid);
			_os_.marshal(level);
			_os_.marshal(number);
			_os_.marshal(money);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			itemid = _os_.unmarshal_int();
			level = _os_.unmarshal_int();
			number = _os_.unmarshal_int();
			money = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.LogBean copy() {
			return new Data(this);
		}

		@Override
		public xbean.LogBean toData() {
			return new Data(this);
		}

		public xbean.LogBean toBean() {
			return new LogBean(this, null, null);
		}

		@Override
		public xbean.LogBean toDataIf() {
			return this;
		}

		public xbean.LogBean toBeanIf() {
			return new LogBean(this, null, null);
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
		public int getItemid() { // 
			return itemid;
		}

		@Override
		public int getLevel() { // 
			return level;
		}

		@Override
		public int getNumber() { // 
			return number;
		}

		@Override
		public int getMoney() { // 
			return money;
		}

		@Override
		public void setItemid(int _v_) { // 
			itemid = _v_;
		}

		@Override
		public void setLevel(int _v_) { // 
			level = _v_;
		}

		@Override
		public void setNumber(int _v_) { // 
			number = _v_;
		}

		@Override
		public void setMoney(int _v_) { // 
			money = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof LogBean.Data)) return false;
			LogBean.Data _o_ = (LogBean.Data) _o1_;
			if (itemid != _o_.itemid) return false;
			if (level != _o_.level) return false;
			if (number != _o_.number) return false;
			if (money != _o_.money) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += itemid;
			_h_ += level;
			_h_ += number;
			_h_ += money;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(itemid);
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(number);
			_sb_.append(",");
			_sb_.append(money);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
