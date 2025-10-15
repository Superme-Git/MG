
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class DayPay extends mkdb.XBean implements xbean.DayPay {
	private int userid; // 
	private long roleid; // 
	private long expiretime; // 结束时间 by changhao
	private int firstprompt; // 还没提示过就是0 by changhao

	@Override
	public void _reset_unsafe_() {
		userid = 0;
		roleid = 0L;
		expiretime = 0L;
		firstprompt = 0;
	}

	DayPay(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public DayPay() {
		this(0, null, null);
	}

	public DayPay(DayPay _o_) {
		this(_o_, null, null);
	}

	DayPay(xbean.DayPay _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof DayPay) assign((DayPay)_o1_);
		else if (_o1_ instanceof DayPay.Data) assign((DayPay.Data)_o1_);
		else if (_o1_ instanceof DayPay.Const) assign(((DayPay.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(DayPay _o_) {
		_o_._xdb_verify_unsafe_();
		userid = _o_.userid;
		roleid = _o_.roleid;
		expiretime = _o_.expiretime;
		firstprompt = _o_.firstprompt;
	}

	private void assign(DayPay.Data _o_) {
		userid = _o_.userid;
		roleid = _o_.roleid;
		expiretime = _o_.expiretime;
		firstprompt = _o_.firstprompt;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(userid);
		_os_.marshal(roleid);
		_os_.marshal(expiretime);
		_os_.marshal(firstprompt);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		userid = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		expiretime = _os_.unmarshal_long();
		firstprompt = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.DayPay copy() {
		_xdb_verify_unsafe_();
		return new DayPay(this);
	}

	@Override
	public xbean.DayPay toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.DayPay toBean() {
		_xdb_verify_unsafe_();
		return new DayPay(this); // same as copy()
	}

	@Override
	public xbean.DayPay toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.DayPay toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getUserid() { // 
		_xdb_verify_unsafe_();
		return userid;
	}

	@Override
	public long getRoleid() { // 
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public long getExpiretime() { // 结束时间 by changhao
		_xdb_verify_unsafe_();
		return expiretime;
	}

	@Override
	public int getFirstprompt() { // 还没提示过就是0 by changhao
		_xdb_verify_unsafe_();
		return firstprompt;
	}

	@Override
	public void setUserid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "userid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, userid) {
					public void rollback() { userid = _xdb_saved; }
				};}});
		userid = _v_;
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
	public void setExpiretime(long _v_) { // 结束时间 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "expiretime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, expiretime) {
					public void rollback() { expiretime = _xdb_saved; }
				};}});
		expiretime = _v_;
	}

	@Override
	public void setFirstprompt(int _v_) { // 还没提示过就是0 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firstprompt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, firstprompt) {
					public void rollback() { firstprompt = _xdb_saved; }
				};}});
		firstprompt = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		DayPay _o_ = null;
		if ( _o1_ instanceof DayPay ) _o_ = (DayPay)_o1_;
		else if ( _o1_ instanceof DayPay.Const ) _o_ = ((DayPay.Const)_o1_).nThis();
		else return false;
		if (userid != _o_.userid) return false;
		if (roleid != _o_.roleid) return false;
		if (expiretime != _o_.expiretime) return false;
		if (firstprompt != _o_.firstprompt) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += userid;
		_h_ += roleid;
		_h_ += expiretime;
		_h_ += firstprompt;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid);
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(expiretime);
		_sb_.append(",");
		_sb_.append(firstprompt);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("userid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("expiretime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firstprompt"));
		return lb;
	}

	private class Const implements xbean.DayPay {
		DayPay nThis() {
			return DayPay.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.DayPay copy() {
			return DayPay.this.copy();
		}

		@Override
		public xbean.DayPay toData() {
			return DayPay.this.toData();
		}

		public xbean.DayPay toBean() {
			return DayPay.this.toBean();
		}

		@Override
		public xbean.DayPay toDataIf() {
			return DayPay.this.toDataIf();
		}

		public xbean.DayPay toBeanIf() {
			return DayPay.this.toBeanIf();
		}

		@Override
		public int getUserid() { // 
			_xdb_verify_unsafe_();
			return userid;
		}

		@Override
		public long getRoleid() { // 
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public long getExpiretime() { // 结束时间 by changhao
			_xdb_verify_unsafe_();
			return expiretime;
		}

		@Override
		public int getFirstprompt() { // 还没提示过就是0 by changhao
			_xdb_verify_unsafe_();
			return firstprompt;
		}

		@Override
		public void setUserid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExpiretime(long _v_) { // 结束时间 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirstprompt(int _v_) { // 还没提示过就是0 by changhao
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
			return DayPay.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return DayPay.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return DayPay.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return DayPay.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return DayPay.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return DayPay.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return DayPay.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return DayPay.this.hashCode();
		}

		@Override
		public String toString() {
			return DayPay.this.toString();
		}

	}

	public static final class Data implements xbean.DayPay {
		private int userid; // 
		private long roleid; // 
		private long expiretime; // 结束时间 by changhao
		private int firstprompt; // 还没提示过就是0 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.DayPay _o1_) {
			if (_o1_ instanceof DayPay) assign((DayPay)_o1_);
			else if (_o1_ instanceof DayPay.Data) assign((DayPay.Data)_o1_);
			else if (_o1_ instanceof DayPay.Const) assign(((DayPay.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(DayPay _o_) {
			userid = _o_.userid;
			roleid = _o_.roleid;
			expiretime = _o_.expiretime;
			firstprompt = _o_.firstprompt;
		}

		private void assign(DayPay.Data _o_) {
			userid = _o_.userid;
			roleid = _o_.roleid;
			expiretime = _o_.expiretime;
			firstprompt = _o_.firstprompt;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(userid);
			_os_.marshal(roleid);
			_os_.marshal(expiretime);
			_os_.marshal(firstprompt);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			userid = _os_.unmarshal_int();
			roleid = _os_.unmarshal_long();
			expiretime = _os_.unmarshal_long();
			firstprompt = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.DayPay copy() {
			return new Data(this);
		}

		@Override
		public xbean.DayPay toData() {
			return new Data(this);
		}

		public xbean.DayPay toBean() {
			return new DayPay(this, null, null);
		}

		@Override
		public xbean.DayPay toDataIf() {
			return this;
		}

		public xbean.DayPay toBeanIf() {
			return new DayPay(this, null, null);
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
		public int getUserid() { // 
			return userid;
		}

		@Override
		public long getRoleid() { // 
			return roleid;
		}

		@Override
		public long getExpiretime() { // 结束时间 by changhao
			return expiretime;
		}

		@Override
		public int getFirstprompt() { // 还没提示过就是0 by changhao
			return firstprompt;
		}

		@Override
		public void setUserid(int _v_) { // 
			userid = _v_;
		}

		@Override
		public void setRoleid(long _v_) { // 
			roleid = _v_;
		}

		@Override
		public void setExpiretime(long _v_) { // 结束时间 by changhao
			expiretime = _v_;
		}

		@Override
		public void setFirstprompt(int _v_) { // 还没提示过就是0 by changhao
			firstprompt = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof DayPay.Data)) return false;
			DayPay.Data _o_ = (DayPay.Data) _o1_;
			if (userid != _o_.userid) return false;
			if (roleid != _o_.roleid) return false;
			if (expiretime != _o_.expiretime) return false;
			if (firstprompt != _o_.firstprompt) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += userid;
			_h_ += roleid;
			_h_ += expiretime;
			_h_ += firstprompt;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(userid);
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(expiretime);
			_sb_.append(",");
			_sb_.append(firstprompt);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
