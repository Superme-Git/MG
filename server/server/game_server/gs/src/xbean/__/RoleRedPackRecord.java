
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class RoleRedPackRecord extends mkdb.XBean implements xbean.RoleRedPackRecord {
	private long sendnum; // 发红包数量
	private long sendgold; // 发红包金币数量
	private long sendfushi; // 发红包符石数量
	private long receivenum; // 收红包数量
	private long receivegold; // 收红包金币数量
	private long receivefushi; // 收红包符石数量

	@Override
	public void _reset_unsafe_() {
		sendnum = 0L;
		sendgold = 0L;
		sendfushi = 0L;
		receivenum = 0L;
		receivegold = 0L;
		receivefushi = 0L;
	}

	RoleRedPackRecord(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public RoleRedPackRecord() {
		this(0, null, null);
	}

	public RoleRedPackRecord(RoleRedPackRecord _o_) {
		this(_o_, null, null);
	}

	RoleRedPackRecord(xbean.RoleRedPackRecord _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof RoleRedPackRecord) assign((RoleRedPackRecord)_o1_);
		else if (_o1_ instanceof RoleRedPackRecord.Data) assign((RoleRedPackRecord.Data)_o1_);
		else if (_o1_ instanceof RoleRedPackRecord.Const) assign(((RoleRedPackRecord.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(RoleRedPackRecord _o_) {
		_o_._xdb_verify_unsafe_();
		sendnum = _o_.sendnum;
		sendgold = _o_.sendgold;
		sendfushi = _o_.sendfushi;
		receivenum = _o_.receivenum;
		receivegold = _o_.receivegold;
		receivefushi = _o_.receivefushi;
	}

	private void assign(RoleRedPackRecord.Data _o_) {
		sendnum = _o_.sendnum;
		sendgold = _o_.sendgold;
		sendfushi = _o_.sendfushi;
		receivenum = _o_.receivenum;
		receivegold = _o_.receivegold;
		receivefushi = _o_.receivefushi;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(sendnum);
		_os_.marshal(sendgold);
		_os_.marshal(sendfushi);
		_os_.marshal(receivenum);
		_os_.marshal(receivegold);
		_os_.marshal(receivefushi);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		sendnum = _os_.unmarshal_long();
		sendgold = _os_.unmarshal_long();
		sendfushi = _os_.unmarshal_long();
		receivenum = _os_.unmarshal_long();
		receivegold = _os_.unmarshal_long();
		receivefushi = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.RoleRedPackRecord copy() {
		_xdb_verify_unsafe_();
		return new RoleRedPackRecord(this);
	}

	@Override
	public xbean.RoleRedPackRecord toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleRedPackRecord toBean() {
		_xdb_verify_unsafe_();
		return new RoleRedPackRecord(this); // same as copy()
	}

	@Override
	public xbean.RoleRedPackRecord toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.RoleRedPackRecord toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getSendnum() { // 发红包数量
		_xdb_verify_unsafe_();
		return sendnum;
	}

	@Override
	public long getSendgold() { // 发红包金币数量
		_xdb_verify_unsafe_();
		return sendgold;
	}

	@Override
	public long getSendfushi() { // 发红包符石数量
		_xdb_verify_unsafe_();
		return sendfushi;
	}

	@Override
	public long getReceivenum() { // 收红包数量
		_xdb_verify_unsafe_();
		return receivenum;
	}

	@Override
	public long getReceivegold() { // 收红包金币数量
		_xdb_verify_unsafe_();
		return receivegold;
	}

	@Override
	public long getReceivefushi() { // 收红包符石数量
		_xdb_verify_unsafe_();
		return receivefushi;
	}

	@Override
	public void setSendnum(long _v_) { // 发红包数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sendnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, sendnum) {
					public void rollback() { sendnum = _xdb_saved; }
				};}});
		sendnum = _v_;
	}

	@Override
	public void setSendgold(long _v_) { // 发红包金币数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sendgold") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, sendgold) {
					public void rollback() { sendgold = _xdb_saved; }
				};}});
		sendgold = _v_;
	}

	@Override
	public void setSendfushi(long _v_) { // 发红包符石数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sendfushi") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, sendfushi) {
					public void rollback() { sendfushi = _xdb_saved; }
				};}});
		sendfushi = _v_;
	}

	@Override
	public void setReceivenum(long _v_) { // 收红包数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "receivenum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, receivenum) {
					public void rollback() { receivenum = _xdb_saved; }
				};}});
		receivenum = _v_;
	}

	@Override
	public void setReceivegold(long _v_) { // 收红包金币数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "receivegold") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, receivegold) {
					public void rollback() { receivegold = _xdb_saved; }
				};}});
		receivegold = _v_;
	}

	@Override
	public void setReceivefushi(long _v_) { // 收红包符石数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "receivefushi") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, receivefushi) {
					public void rollback() { receivefushi = _xdb_saved; }
				};}});
		receivefushi = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		RoleRedPackRecord _o_ = null;
		if ( _o1_ instanceof RoleRedPackRecord ) _o_ = (RoleRedPackRecord)_o1_;
		else if ( _o1_ instanceof RoleRedPackRecord.Const ) _o_ = ((RoleRedPackRecord.Const)_o1_).nThis();
		else return false;
		if (sendnum != _o_.sendnum) return false;
		if (sendgold != _o_.sendgold) return false;
		if (sendfushi != _o_.sendfushi) return false;
		if (receivenum != _o_.receivenum) return false;
		if (receivegold != _o_.receivegold) return false;
		if (receivefushi != _o_.receivefushi) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += sendnum;
		_h_ += sendgold;
		_h_ += sendfushi;
		_h_ += receivenum;
		_h_ += receivegold;
		_h_ += receivefushi;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(sendnum);
		_sb_.append(",");
		_sb_.append(sendgold);
		_sb_.append(",");
		_sb_.append(sendfushi);
		_sb_.append(",");
		_sb_.append(receivenum);
		_sb_.append(",");
		_sb_.append(receivegold);
		_sb_.append(",");
		_sb_.append(receivefushi);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sendnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sendgold"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sendfushi"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("receivenum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("receivegold"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("receivefushi"));
		return lb;
	}

	private class Const implements xbean.RoleRedPackRecord {
		RoleRedPackRecord nThis() {
			return RoleRedPackRecord.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.RoleRedPackRecord copy() {
			return RoleRedPackRecord.this.copy();
		}

		@Override
		public xbean.RoleRedPackRecord toData() {
			return RoleRedPackRecord.this.toData();
		}

		public xbean.RoleRedPackRecord toBean() {
			return RoleRedPackRecord.this.toBean();
		}

		@Override
		public xbean.RoleRedPackRecord toDataIf() {
			return RoleRedPackRecord.this.toDataIf();
		}

		public xbean.RoleRedPackRecord toBeanIf() {
			return RoleRedPackRecord.this.toBeanIf();
		}

		@Override
		public long getSendnum() { // 发红包数量
			_xdb_verify_unsafe_();
			return sendnum;
		}

		@Override
		public long getSendgold() { // 发红包金币数量
			_xdb_verify_unsafe_();
			return sendgold;
		}

		@Override
		public long getSendfushi() { // 发红包符石数量
			_xdb_verify_unsafe_();
			return sendfushi;
		}

		@Override
		public long getReceivenum() { // 收红包数量
			_xdb_verify_unsafe_();
			return receivenum;
		}

		@Override
		public long getReceivegold() { // 收红包金币数量
			_xdb_verify_unsafe_();
			return receivegold;
		}

		@Override
		public long getReceivefushi() { // 收红包符石数量
			_xdb_verify_unsafe_();
			return receivefushi;
		}

		@Override
		public void setSendnum(long _v_) { // 发红包数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSendgold(long _v_) { // 发红包金币数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSendfushi(long _v_) { // 发红包符石数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReceivenum(long _v_) { // 收红包数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReceivegold(long _v_) { // 收红包金币数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setReceivefushi(long _v_) { // 收红包符石数量
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
			return RoleRedPackRecord.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return RoleRedPackRecord.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return RoleRedPackRecord.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return RoleRedPackRecord.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return RoleRedPackRecord.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return RoleRedPackRecord.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return RoleRedPackRecord.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return RoleRedPackRecord.this.hashCode();
		}

		@Override
		public String toString() {
			return RoleRedPackRecord.this.toString();
		}

	}

	public static final class Data implements xbean.RoleRedPackRecord {
		private long sendnum; // 发红包数量
		private long sendgold; // 发红包金币数量
		private long sendfushi; // 发红包符石数量
		private long receivenum; // 收红包数量
		private long receivegold; // 收红包金币数量
		private long receivefushi; // 收红包符石数量

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.RoleRedPackRecord _o1_) {
			if (_o1_ instanceof RoleRedPackRecord) assign((RoleRedPackRecord)_o1_);
			else if (_o1_ instanceof RoleRedPackRecord.Data) assign((RoleRedPackRecord.Data)_o1_);
			else if (_o1_ instanceof RoleRedPackRecord.Const) assign(((RoleRedPackRecord.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(RoleRedPackRecord _o_) {
			sendnum = _o_.sendnum;
			sendgold = _o_.sendgold;
			sendfushi = _o_.sendfushi;
			receivenum = _o_.receivenum;
			receivegold = _o_.receivegold;
			receivefushi = _o_.receivefushi;
		}

		private void assign(RoleRedPackRecord.Data _o_) {
			sendnum = _o_.sendnum;
			sendgold = _o_.sendgold;
			sendfushi = _o_.sendfushi;
			receivenum = _o_.receivenum;
			receivegold = _o_.receivegold;
			receivefushi = _o_.receivefushi;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(sendnum);
			_os_.marshal(sendgold);
			_os_.marshal(sendfushi);
			_os_.marshal(receivenum);
			_os_.marshal(receivegold);
			_os_.marshal(receivefushi);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			sendnum = _os_.unmarshal_long();
			sendgold = _os_.unmarshal_long();
			sendfushi = _os_.unmarshal_long();
			receivenum = _os_.unmarshal_long();
			receivegold = _os_.unmarshal_long();
			receivefushi = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.RoleRedPackRecord copy() {
			return new Data(this);
		}

		@Override
		public xbean.RoleRedPackRecord toData() {
			return new Data(this);
		}

		public xbean.RoleRedPackRecord toBean() {
			return new RoleRedPackRecord(this, null, null);
		}

		@Override
		public xbean.RoleRedPackRecord toDataIf() {
			return this;
		}

		public xbean.RoleRedPackRecord toBeanIf() {
			return new RoleRedPackRecord(this, null, null);
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
		public long getSendnum() { // 发红包数量
			return sendnum;
		}

		@Override
		public long getSendgold() { // 发红包金币数量
			return sendgold;
		}

		@Override
		public long getSendfushi() { // 发红包符石数量
			return sendfushi;
		}

		@Override
		public long getReceivenum() { // 收红包数量
			return receivenum;
		}

		@Override
		public long getReceivegold() { // 收红包金币数量
			return receivegold;
		}

		@Override
		public long getReceivefushi() { // 收红包符石数量
			return receivefushi;
		}

		@Override
		public void setSendnum(long _v_) { // 发红包数量
			sendnum = _v_;
		}

		@Override
		public void setSendgold(long _v_) { // 发红包金币数量
			sendgold = _v_;
		}

		@Override
		public void setSendfushi(long _v_) { // 发红包符石数量
			sendfushi = _v_;
		}

		@Override
		public void setReceivenum(long _v_) { // 收红包数量
			receivenum = _v_;
		}

		@Override
		public void setReceivegold(long _v_) { // 收红包金币数量
			receivegold = _v_;
		}

		@Override
		public void setReceivefushi(long _v_) { // 收红包符石数量
			receivefushi = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof RoleRedPackRecord.Data)) return false;
			RoleRedPackRecord.Data _o_ = (RoleRedPackRecord.Data) _o1_;
			if (sendnum != _o_.sendnum) return false;
			if (sendgold != _o_.sendgold) return false;
			if (sendfushi != _o_.sendfushi) return false;
			if (receivenum != _o_.receivenum) return false;
			if (receivegold != _o_.receivegold) return false;
			if (receivefushi != _o_.receivefushi) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += sendnum;
			_h_ += sendgold;
			_h_ += sendfushi;
			_h_ += receivenum;
			_h_ += receivegold;
			_h_ += receivefushi;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(sendnum);
			_sb_.append(",");
			_sb_.append(sendgold);
			_sb_.append(",");
			_sb_.append(sendfushi);
			_sb_.append(",");
			_sb_.append(receivenum);
			_sb_.append(",");
			_sb_.append(receivegold);
			_sb_.append(",");
			_sb_.append(receivefushi);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
