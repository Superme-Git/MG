
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ProfessionLeaderTicketInfo extends mkdb.XBean implements xbean.ProfessionLeaderTicketInfo {
	private long roleid; // roleid
	private int tickets; // role的得票
	private String words; // role的宣言

	@Override
	public void _reset_unsafe_() {
		roleid = 0L;
		tickets = 0;
		words = "";
	}

	ProfessionLeaderTicketInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		words = "";
	}

	public ProfessionLeaderTicketInfo() {
		this(0, null, null);
	}

	public ProfessionLeaderTicketInfo(ProfessionLeaderTicketInfo _o_) {
		this(_o_, null, null);
	}

	ProfessionLeaderTicketInfo(xbean.ProfessionLeaderTicketInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ProfessionLeaderTicketInfo) assign((ProfessionLeaderTicketInfo)_o1_);
		else if (_o1_ instanceof ProfessionLeaderTicketInfo.Data) assign((ProfessionLeaderTicketInfo.Data)_o1_);
		else if (_o1_ instanceof ProfessionLeaderTicketInfo.Const) assign(((ProfessionLeaderTicketInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ProfessionLeaderTicketInfo _o_) {
		_o_._xdb_verify_unsafe_();
		roleid = _o_.roleid;
		tickets = _o_.tickets;
		words = _o_.words;
	}

	private void assign(ProfessionLeaderTicketInfo.Data _o_) {
		roleid = _o_.roleid;
		tickets = _o_.tickets;
		words = _o_.words;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(roleid);
		_os_.marshal(tickets);
		_os_.marshal(words, mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		roleid = _os_.unmarshal_long();
		tickets = _os_.unmarshal_int();
		words = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		return _os_;
	}

	@Override
	public xbean.ProfessionLeaderTicketInfo copy() {
		_xdb_verify_unsafe_();
		return new ProfessionLeaderTicketInfo(this);
	}

	@Override
	public xbean.ProfessionLeaderTicketInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ProfessionLeaderTicketInfo toBean() {
		_xdb_verify_unsafe_();
		return new ProfessionLeaderTicketInfo(this); // same as copy()
	}

	@Override
	public xbean.ProfessionLeaderTicketInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ProfessionLeaderTicketInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getRoleid() { // roleid
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public int getTickets() { // role的得票
		_xdb_verify_unsafe_();
		return tickets;
	}

	@Override
	public String getWords() { // role的宣言
		_xdb_verify_unsafe_();
		return words;
	}

	@Override
	public com.locojoy.base.Octets getWordsOctets() { // role的宣言
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getWords(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public void setRoleid(long _v_) { // roleid
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setTickets(int _v_) { // role的得票
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "tickets") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, tickets) {
					public void rollback() { tickets = _xdb_saved; }
				};}});
		tickets = _v_;
	}

	@Override
	public void setWords(String _v_) { // role的宣言
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "words") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, words) {
					public void rollback() { words = _xdb_saved; }
				};}});
		words = _v_;
	}

	@Override
	public void setWordsOctets(com.locojoy.base.Octets _v_) { // role的宣言
		_xdb_verify_unsafe_();
		this.setWords(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ProfessionLeaderTicketInfo _o_ = null;
		if ( _o1_ instanceof ProfessionLeaderTicketInfo ) _o_ = (ProfessionLeaderTicketInfo)_o1_;
		else if ( _o1_ instanceof ProfessionLeaderTicketInfo.Const ) _o_ = ((ProfessionLeaderTicketInfo.Const)_o1_).nThis();
		else return false;
		if (roleid != _o_.roleid) return false;
		if (tickets != _o_.tickets) return false;
		if (!words.equals(_o_.words)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += roleid;
		_h_ += tickets;
		_h_ += words.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(tickets);
		_sb_.append(",");
		_sb_.append("'").append(words).append("'");
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("tickets"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("words"));
		return lb;
	}

	private class Const implements xbean.ProfessionLeaderTicketInfo {
		ProfessionLeaderTicketInfo nThis() {
			return ProfessionLeaderTicketInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ProfessionLeaderTicketInfo copy() {
			return ProfessionLeaderTicketInfo.this.copy();
		}

		@Override
		public xbean.ProfessionLeaderTicketInfo toData() {
			return ProfessionLeaderTicketInfo.this.toData();
		}

		public xbean.ProfessionLeaderTicketInfo toBean() {
			return ProfessionLeaderTicketInfo.this.toBean();
		}

		@Override
		public xbean.ProfessionLeaderTicketInfo toDataIf() {
			return ProfessionLeaderTicketInfo.this.toDataIf();
		}

		public xbean.ProfessionLeaderTicketInfo toBeanIf() {
			return ProfessionLeaderTicketInfo.this.toBeanIf();
		}

		@Override
		public long getRoleid() { // roleid
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getTickets() { // role的得票
			_xdb_verify_unsafe_();
			return tickets;
		}

		@Override
		public String getWords() { // role的宣言
			_xdb_verify_unsafe_();
			return words;
		}

		@Override
		public com.locojoy.base.Octets getWordsOctets() { // role的宣言
			_xdb_verify_unsafe_();
			return ProfessionLeaderTicketInfo.this.getWordsOctets();
		}

		@Override
		public void setRoleid(long _v_) { // roleid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTickets(int _v_) { // role的得票
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWords(String _v_) { // role的宣言
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWordsOctets(com.locojoy.base.Octets _v_) { // role的宣言
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
			return ProfessionLeaderTicketInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ProfessionLeaderTicketInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ProfessionLeaderTicketInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ProfessionLeaderTicketInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ProfessionLeaderTicketInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ProfessionLeaderTicketInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ProfessionLeaderTicketInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ProfessionLeaderTicketInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return ProfessionLeaderTicketInfo.this.toString();
		}

	}

	public static final class Data implements xbean.ProfessionLeaderTicketInfo {
		private long roleid; // roleid
		private int tickets; // role的得票
		private String words; // role的宣言

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			words = "";
		}

		Data(xbean.ProfessionLeaderTicketInfo _o1_) {
			if (_o1_ instanceof ProfessionLeaderTicketInfo) assign((ProfessionLeaderTicketInfo)_o1_);
			else if (_o1_ instanceof ProfessionLeaderTicketInfo.Data) assign((ProfessionLeaderTicketInfo.Data)_o1_);
			else if (_o1_ instanceof ProfessionLeaderTicketInfo.Const) assign(((ProfessionLeaderTicketInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ProfessionLeaderTicketInfo _o_) {
			roleid = _o_.roleid;
			tickets = _o_.tickets;
			words = _o_.words;
		}

		private void assign(ProfessionLeaderTicketInfo.Data _o_) {
			roleid = _o_.roleid;
			tickets = _o_.tickets;
			words = _o_.words;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(roleid);
			_os_.marshal(tickets);
			_os_.marshal(words, mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			roleid = _os_.unmarshal_long();
			tickets = _os_.unmarshal_int();
			words = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			return _os_;
		}

		@Override
		public xbean.ProfessionLeaderTicketInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.ProfessionLeaderTicketInfo toData() {
			return new Data(this);
		}

		public xbean.ProfessionLeaderTicketInfo toBean() {
			return new ProfessionLeaderTicketInfo(this, null, null);
		}

		@Override
		public xbean.ProfessionLeaderTicketInfo toDataIf() {
			return this;
		}

		public xbean.ProfessionLeaderTicketInfo toBeanIf() {
			return new ProfessionLeaderTicketInfo(this, null, null);
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
		public long getRoleid() { // roleid
			return roleid;
		}

		@Override
		public int getTickets() { // role的得票
			return tickets;
		}

		@Override
		public String getWords() { // role的宣言
			return words;
		}

		@Override
		public com.locojoy.base.Octets getWordsOctets() { // role的宣言
			return com.locojoy.base.Octets.wrap(getWords(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public void setRoleid(long _v_) { // roleid
			roleid = _v_;
		}

		@Override
		public void setTickets(int _v_) { // role的得票
			tickets = _v_;
		}

		@Override
		public void setWords(String _v_) { // role的宣言
			if (null == _v_)
				throw new NullPointerException();
			words = _v_;
		}

		@Override
		public void setWordsOctets(com.locojoy.base.Octets _v_) { // role的宣言
			this.setWords(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ProfessionLeaderTicketInfo.Data)) return false;
			ProfessionLeaderTicketInfo.Data _o_ = (ProfessionLeaderTicketInfo.Data) _o1_;
			if (roleid != _o_.roleid) return false;
			if (tickets != _o_.tickets) return false;
			if (!words.equals(_o_.words)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += roleid;
			_h_ += tickets;
			_h_ += words.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(tickets);
			_sb_.append(",");
			_sb_.append("'").append(words).append("'");
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
