
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class CardInfo extends mkdb.XBean implements xbean.CardInfo {
	private int userid; // 
	private long roleid; // 
	private long usecardtime; // 用掉新手卡的时间
	private String answer; // 新手卡号
	private int parenttype; // 父类型
	private int type; // 子类型

	@Override
	public void _reset_unsafe_() {
		userid = 0;
		roleid = 0L;
		usecardtime = 0L;
		answer = "";
		parenttype = 0;
		type = 0;
	}

	CardInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		answer = "";
	}

	public CardInfo() {
		this(0, null, null);
	}

	public CardInfo(CardInfo _o_) {
		this(_o_, null, null);
	}

	CardInfo(xbean.CardInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof CardInfo) assign((CardInfo)_o1_);
		else if (_o1_ instanceof CardInfo.Data) assign((CardInfo.Data)_o1_);
		else if (_o1_ instanceof CardInfo.Const) assign(((CardInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(CardInfo _o_) {
		_o_._xdb_verify_unsafe_();
		userid = _o_.userid;
		roleid = _o_.roleid;
		usecardtime = _o_.usecardtime;
		answer = _o_.answer;
		parenttype = _o_.parenttype;
		type = _o_.type;
	}

	private void assign(CardInfo.Data _o_) {
		userid = _o_.userid;
		roleid = _o_.roleid;
		usecardtime = _o_.usecardtime;
		answer = _o_.answer;
		parenttype = _o_.parenttype;
		type = _o_.type;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(userid);
		_os_.marshal(roleid);
		_os_.marshal(usecardtime);
		_os_.marshal(answer, mkdb.Const.IO_CHARSET);
		_os_.marshal(parenttype);
		_os_.marshal(type);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		userid = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		usecardtime = _os_.unmarshal_long();
		answer = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		parenttype = _os_.unmarshal_int();
		type = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.CardInfo copy() {
		_xdb_verify_unsafe_();
		return new CardInfo(this);
	}

	@Override
	public xbean.CardInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CardInfo toBean() {
		_xdb_verify_unsafe_();
		return new CardInfo(this); // same as copy()
	}

	@Override
	public xbean.CardInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CardInfo toBeanIf() {
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
	public long getUsecardtime() { // 用掉新手卡的时间
		_xdb_verify_unsafe_();
		return usecardtime;
	}

	@Override
	public String getAnswer() { // 新手卡号
		_xdb_verify_unsafe_();
		return answer;
	}

	@Override
	public com.locojoy.base.Octets getAnswerOctets() { // 新手卡号
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getAnswer(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getParenttype() { // 父类型
		_xdb_verify_unsafe_();
		return parenttype;
	}

	@Override
	public int getType() { // 子类型
		_xdb_verify_unsafe_();
		return type;
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
	public void setUsecardtime(long _v_) { // 用掉新手卡的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "usecardtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, usecardtime) {
					public void rollback() { usecardtime = _xdb_saved; }
				};}});
		usecardtime = _v_;
	}

	@Override
	public void setAnswer(String _v_) { // 新手卡号
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "answer") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, answer) {
					public void rollback() { answer = _xdb_saved; }
				};}});
		answer = _v_;
	}

	@Override
	public void setAnswerOctets(com.locojoy.base.Octets _v_) { // 新手卡号
		_xdb_verify_unsafe_();
		this.setAnswer(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setParenttype(int _v_) { // 父类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "parenttype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, parenttype) {
					public void rollback() { parenttype = _xdb_saved; }
				};}});
		parenttype = _v_;
	}

	@Override
	public void setType(int _v_) { // 子类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "type") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, type) {
					public void rollback() { type = _xdb_saved; }
				};}});
		type = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		CardInfo _o_ = null;
		if ( _o1_ instanceof CardInfo ) _o_ = (CardInfo)_o1_;
		else if ( _o1_ instanceof CardInfo.Const ) _o_ = ((CardInfo.Const)_o1_).nThis();
		else return false;
		if (userid != _o_.userid) return false;
		if (roleid != _o_.roleid) return false;
		if (usecardtime != _o_.usecardtime) return false;
		if (!answer.equals(_o_.answer)) return false;
		if (parenttype != _o_.parenttype) return false;
		if (type != _o_.type) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += userid;
		_h_ += roleid;
		_h_ += usecardtime;
		_h_ += answer.hashCode();
		_h_ += parenttype;
		_h_ += type;
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
		_sb_.append(usecardtime);
		_sb_.append(",");
		_sb_.append("'").append(answer).append("'");
		_sb_.append(",");
		_sb_.append(parenttype);
		_sb_.append(",");
		_sb_.append(type);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("userid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("usecardtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("answer"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("parenttype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("type"));
		return lb;
	}

	private class Const implements xbean.CardInfo {
		CardInfo nThis() {
			return CardInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.CardInfo copy() {
			return CardInfo.this.copy();
		}

		@Override
		public xbean.CardInfo toData() {
			return CardInfo.this.toData();
		}

		public xbean.CardInfo toBean() {
			return CardInfo.this.toBean();
		}

		@Override
		public xbean.CardInfo toDataIf() {
			return CardInfo.this.toDataIf();
		}

		public xbean.CardInfo toBeanIf() {
			return CardInfo.this.toBeanIf();
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
		public long getUsecardtime() { // 用掉新手卡的时间
			_xdb_verify_unsafe_();
			return usecardtime;
		}

		@Override
		public String getAnswer() { // 新手卡号
			_xdb_verify_unsafe_();
			return answer;
		}

		@Override
		public com.locojoy.base.Octets getAnswerOctets() { // 新手卡号
			_xdb_verify_unsafe_();
			return CardInfo.this.getAnswerOctets();
		}

		@Override
		public int getParenttype() { // 父类型
			_xdb_verify_unsafe_();
			return parenttype;
		}

		@Override
		public int getType() { // 子类型
			_xdb_verify_unsafe_();
			return type;
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
		public void setUsecardtime(long _v_) { // 用掉新手卡的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAnswer(String _v_) { // 新手卡号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAnswerOctets(com.locojoy.base.Octets _v_) { // 新手卡号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setParenttype(int _v_) { // 父类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setType(int _v_) { // 子类型
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
			return CardInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return CardInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return CardInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return CardInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return CardInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return CardInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return CardInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return CardInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return CardInfo.this.toString();
		}

	}

	public static final class Data implements xbean.CardInfo {
		private int userid; // 
		private long roleid; // 
		private long usecardtime; // 用掉新手卡的时间
		private String answer; // 新手卡号
		private int parenttype; // 父类型
		private int type; // 子类型

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			answer = "";
		}

		Data(xbean.CardInfo _o1_) {
			if (_o1_ instanceof CardInfo) assign((CardInfo)_o1_);
			else if (_o1_ instanceof CardInfo.Data) assign((CardInfo.Data)_o1_);
			else if (_o1_ instanceof CardInfo.Const) assign(((CardInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(CardInfo _o_) {
			userid = _o_.userid;
			roleid = _o_.roleid;
			usecardtime = _o_.usecardtime;
			answer = _o_.answer;
			parenttype = _o_.parenttype;
			type = _o_.type;
		}

		private void assign(CardInfo.Data _o_) {
			userid = _o_.userid;
			roleid = _o_.roleid;
			usecardtime = _o_.usecardtime;
			answer = _o_.answer;
			parenttype = _o_.parenttype;
			type = _o_.type;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(userid);
			_os_.marshal(roleid);
			_os_.marshal(usecardtime);
			_os_.marshal(answer, mkdb.Const.IO_CHARSET);
			_os_.marshal(parenttype);
			_os_.marshal(type);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			userid = _os_.unmarshal_int();
			roleid = _os_.unmarshal_long();
			usecardtime = _os_.unmarshal_long();
			answer = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			parenttype = _os_.unmarshal_int();
			type = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.CardInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.CardInfo toData() {
			return new Data(this);
		}

		public xbean.CardInfo toBean() {
			return new CardInfo(this, null, null);
		}

		@Override
		public xbean.CardInfo toDataIf() {
			return this;
		}

		public xbean.CardInfo toBeanIf() {
			return new CardInfo(this, null, null);
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
		public long getUsecardtime() { // 用掉新手卡的时间
			return usecardtime;
		}

		@Override
		public String getAnswer() { // 新手卡号
			return answer;
		}

		@Override
		public com.locojoy.base.Octets getAnswerOctets() { // 新手卡号
			return com.locojoy.base.Octets.wrap(getAnswer(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getParenttype() { // 父类型
			return parenttype;
		}

		@Override
		public int getType() { // 子类型
			return type;
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
		public void setUsecardtime(long _v_) { // 用掉新手卡的时间
			usecardtime = _v_;
		}

		@Override
		public void setAnswer(String _v_) { // 新手卡号
			if (null == _v_)
				throw new NullPointerException();
			answer = _v_;
		}

		@Override
		public void setAnswerOctets(com.locojoy.base.Octets _v_) { // 新手卡号
			this.setAnswer(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setParenttype(int _v_) { // 父类型
			parenttype = _v_;
		}

		@Override
		public void setType(int _v_) { // 子类型
			type = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof CardInfo.Data)) return false;
			CardInfo.Data _o_ = (CardInfo.Data) _o1_;
			if (userid != _o_.userid) return false;
			if (roleid != _o_.roleid) return false;
			if (usecardtime != _o_.usecardtime) return false;
			if (!answer.equals(_o_.answer)) return false;
			if (parenttype != _o_.parenttype) return false;
			if (type != _o_.type) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += userid;
			_h_ += roleid;
			_h_ += usecardtime;
			_h_ += answer.hashCode();
			_h_ += parenttype;
			_h_ += type;
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
			_sb_.append(usecardtime);
			_sb_.append(",");
			_sb_.append("'").append(answer).append("'");
			_sb_.append(",");
			_sb_.append(parenttype);
			_sb_.append(",");
			_sb_.append(type);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
