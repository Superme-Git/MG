
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ChatTime extends mkdb.XBean implements xbean.ChatTime {
	private long lastworldchattime; // 上次世界聊天时间
	private long lastcurrentchattime; // 上次当前聊天时间
	private long lastprofessionchattime; // 上次职业聊天时间
	private long lastclanchattime; // 上次公会聊天时间
	private long lastteamapplychattime; // 上次组队申请喊话时间

	@Override
	public void _reset_unsafe_() {
		lastworldchattime = 0L;
		lastcurrentchattime = 0L;
		lastprofessionchattime = 0L;
		lastclanchattime = 0L;
		lastteamapplychattime = 0L;
	}

	ChatTime(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public ChatTime() {
		this(0, null, null);
	}

	public ChatTime(ChatTime _o_) {
		this(_o_, null, null);
	}

	ChatTime(xbean.ChatTime _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ChatTime) assign((ChatTime)_o1_);
		else if (_o1_ instanceof ChatTime.Data) assign((ChatTime.Data)_o1_);
		else if (_o1_ instanceof ChatTime.Const) assign(((ChatTime.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ChatTime _o_) {
		_o_._xdb_verify_unsafe_();
		lastworldchattime = _o_.lastworldchattime;
		lastcurrentchattime = _o_.lastcurrentchattime;
		lastprofessionchattime = _o_.lastprofessionchattime;
		lastclanchattime = _o_.lastclanchattime;
		lastteamapplychattime = _o_.lastteamapplychattime;
	}

	private void assign(ChatTime.Data _o_) {
		lastworldchattime = _o_.lastworldchattime;
		lastcurrentchattime = _o_.lastcurrentchattime;
		lastprofessionchattime = _o_.lastprofessionchattime;
		lastclanchattime = _o_.lastclanchattime;
		lastteamapplychattime = _o_.lastteamapplychattime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(lastworldchattime);
		_os_.marshal(lastcurrentchattime);
		_os_.marshal(lastprofessionchattime);
		_os_.marshal(lastclanchattime);
		_os_.marshal(lastteamapplychattime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		lastworldchattime = _os_.unmarshal_long();
		lastcurrentchattime = _os_.unmarshal_long();
		lastprofessionchattime = _os_.unmarshal_long();
		lastclanchattime = _os_.unmarshal_long();
		lastteamapplychattime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.ChatTime copy() {
		_xdb_verify_unsafe_();
		return new ChatTime(this);
	}

	@Override
	public xbean.ChatTime toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChatTime toBean() {
		_xdb_verify_unsafe_();
		return new ChatTime(this); // same as copy()
	}

	@Override
	public xbean.ChatTime toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ChatTime toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getLastworldchattime() { // 上次世界聊天时间
		_xdb_verify_unsafe_();
		return lastworldchattime;
	}

	@Override
	public long getLastcurrentchattime() { // 上次当前聊天时间
		_xdb_verify_unsafe_();
		return lastcurrentchattime;
	}

	@Override
	public long getLastprofessionchattime() { // 上次职业聊天时间
		_xdb_verify_unsafe_();
		return lastprofessionchattime;
	}

	@Override
	public long getLastclanchattime() { // 上次公会聊天时间
		_xdb_verify_unsafe_();
		return lastclanchattime;
	}

	@Override
	public long getLastteamapplychattime() { // 上次组队申请喊话时间
		_xdb_verify_unsafe_();
		return lastteamapplychattime;
	}

	@Override
	public void setLastworldchattime(long _v_) { // 上次世界聊天时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastworldchattime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastworldchattime) {
					public void rollback() { lastworldchattime = _xdb_saved; }
				};}});
		lastworldchattime = _v_;
	}

	@Override
	public void setLastcurrentchattime(long _v_) { // 上次当前聊天时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastcurrentchattime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastcurrentchattime) {
					public void rollback() { lastcurrentchattime = _xdb_saved; }
				};}});
		lastcurrentchattime = _v_;
	}

	@Override
	public void setLastprofessionchattime(long _v_) { // 上次职业聊天时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastprofessionchattime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastprofessionchattime) {
					public void rollback() { lastprofessionchattime = _xdb_saved; }
				};}});
		lastprofessionchattime = _v_;
	}

	@Override
	public void setLastclanchattime(long _v_) { // 上次公会聊天时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastclanchattime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastclanchattime) {
					public void rollback() { lastclanchattime = _xdb_saved; }
				};}});
		lastclanchattime = _v_;
	}

	@Override
	public void setLastteamapplychattime(long _v_) { // 上次组队申请喊话时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastteamapplychattime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastteamapplychattime) {
					public void rollback() { lastteamapplychattime = _xdb_saved; }
				};}});
		lastteamapplychattime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ChatTime _o_ = null;
		if ( _o1_ instanceof ChatTime ) _o_ = (ChatTime)_o1_;
		else if ( _o1_ instanceof ChatTime.Const ) _o_ = ((ChatTime.Const)_o1_).nThis();
		else return false;
		if (lastworldchattime != _o_.lastworldchattime) return false;
		if (lastcurrentchattime != _o_.lastcurrentchattime) return false;
		if (lastprofessionchattime != _o_.lastprofessionchattime) return false;
		if (lastclanchattime != _o_.lastclanchattime) return false;
		if (lastteamapplychattime != _o_.lastteamapplychattime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += lastworldchattime;
		_h_ += lastcurrentchattime;
		_h_ += lastprofessionchattime;
		_h_ += lastclanchattime;
		_h_ += lastteamapplychattime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(lastworldchattime);
		_sb_.append(",");
		_sb_.append(lastcurrentchattime);
		_sb_.append(",");
		_sb_.append(lastprofessionchattime);
		_sb_.append(",");
		_sb_.append(lastclanchattime);
		_sb_.append(",");
		_sb_.append(lastteamapplychattime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastworldchattime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastcurrentchattime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastprofessionchattime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastclanchattime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastteamapplychattime"));
		return lb;
	}

	private class Const implements xbean.ChatTime {
		ChatTime nThis() {
			return ChatTime.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ChatTime copy() {
			return ChatTime.this.copy();
		}

		@Override
		public xbean.ChatTime toData() {
			return ChatTime.this.toData();
		}

		public xbean.ChatTime toBean() {
			return ChatTime.this.toBean();
		}

		@Override
		public xbean.ChatTime toDataIf() {
			return ChatTime.this.toDataIf();
		}

		public xbean.ChatTime toBeanIf() {
			return ChatTime.this.toBeanIf();
		}

		@Override
		public long getLastworldchattime() { // 上次世界聊天时间
			_xdb_verify_unsafe_();
			return lastworldchattime;
		}

		@Override
		public long getLastcurrentchattime() { // 上次当前聊天时间
			_xdb_verify_unsafe_();
			return lastcurrentchattime;
		}

		@Override
		public long getLastprofessionchattime() { // 上次职业聊天时间
			_xdb_verify_unsafe_();
			return lastprofessionchattime;
		}

		@Override
		public long getLastclanchattime() { // 上次公会聊天时间
			_xdb_verify_unsafe_();
			return lastclanchattime;
		}

		@Override
		public long getLastteamapplychattime() { // 上次组队申请喊话时间
			_xdb_verify_unsafe_();
			return lastteamapplychattime;
		}

		@Override
		public void setLastworldchattime(long _v_) { // 上次世界聊天时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastcurrentchattime(long _v_) { // 上次当前聊天时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastprofessionchattime(long _v_) { // 上次职业聊天时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastclanchattime(long _v_) { // 上次公会聊天时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastteamapplychattime(long _v_) { // 上次组队申请喊话时间
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
			return ChatTime.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ChatTime.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ChatTime.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ChatTime.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ChatTime.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ChatTime.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ChatTime.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ChatTime.this.hashCode();
		}

		@Override
		public String toString() {
			return ChatTime.this.toString();
		}

	}

	public static final class Data implements xbean.ChatTime {
		private long lastworldchattime; // 上次世界聊天时间
		private long lastcurrentchattime; // 上次当前聊天时间
		private long lastprofessionchattime; // 上次职业聊天时间
		private long lastclanchattime; // 上次公会聊天时间
		private long lastteamapplychattime; // 上次组队申请喊话时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.ChatTime _o1_) {
			if (_o1_ instanceof ChatTime) assign((ChatTime)_o1_);
			else if (_o1_ instanceof ChatTime.Data) assign((ChatTime.Data)_o1_);
			else if (_o1_ instanceof ChatTime.Const) assign(((ChatTime.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ChatTime _o_) {
			lastworldchattime = _o_.lastworldchattime;
			lastcurrentchattime = _o_.lastcurrentchattime;
			lastprofessionchattime = _o_.lastprofessionchattime;
			lastclanchattime = _o_.lastclanchattime;
			lastteamapplychattime = _o_.lastteamapplychattime;
		}

		private void assign(ChatTime.Data _o_) {
			lastworldchattime = _o_.lastworldchattime;
			lastcurrentchattime = _o_.lastcurrentchattime;
			lastprofessionchattime = _o_.lastprofessionchattime;
			lastclanchattime = _o_.lastclanchattime;
			lastteamapplychattime = _o_.lastteamapplychattime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(lastworldchattime);
			_os_.marshal(lastcurrentchattime);
			_os_.marshal(lastprofessionchattime);
			_os_.marshal(lastclanchattime);
			_os_.marshal(lastteamapplychattime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			lastworldchattime = _os_.unmarshal_long();
			lastcurrentchattime = _os_.unmarshal_long();
			lastprofessionchattime = _os_.unmarshal_long();
			lastclanchattime = _os_.unmarshal_long();
			lastteamapplychattime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.ChatTime copy() {
			return new Data(this);
		}

		@Override
		public xbean.ChatTime toData() {
			return new Data(this);
		}

		public xbean.ChatTime toBean() {
			return new ChatTime(this, null, null);
		}

		@Override
		public xbean.ChatTime toDataIf() {
			return this;
		}

		public xbean.ChatTime toBeanIf() {
			return new ChatTime(this, null, null);
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
		public long getLastworldchattime() { // 上次世界聊天时间
			return lastworldchattime;
		}

		@Override
		public long getLastcurrentchattime() { // 上次当前聊天时间
			return lastcurrentchattime;
		}

		@Override
		public long getLastprofessionchattime() { // 上次职业聊天时间
			return lastprofessionchattime;
		}

		@Override
		public long getLastclanchattime() { // 上次公会聊天时间
			return lastclanchattime;
		}

		@Override
		public long getLastteamapplychattime() { // 上次组队申请喊话时间
			return lastteamapplychattime;
		}

		@Override
		public void setLastworldchattime(long _v_) { // 上次世界聊天时间
			lastworldchattime = _v_;
		}

		@Override
		public void setLastcurrentchattime(long _v_) { // 上次当前聊天时间
			lastcurrentchattime = _v_;
		}

		@Override
		public void setLastprofessionchattime(long _v_) { // 上次职业聊天时间
			lastprofessionchattime = _v_;
		}

		@Override
		public void setLastclanchattime(long _v_) { // 上次公会聊天时间
			lastclanchattime = _v_;
		}

		@Override
		public void setLastteamapplychattime(long _v_) { // 上次组队申请喊话时间
			lastteamapplychattime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ChatTime.Data)) return false;
			ChatTime.Data _o_ = (ChatTime.Data) _o1_;
			if (lastworldchattime != _o_.lastworldchattime) return false;
			if (lastcurrentchattime != _o_.lastcurrentchattime) return false;
			if (lastprofessionchattime != _o_.lastprofessionchattime) return false;
			if (lastclanchattime != _o_.lastclanchattime) return false;
			if (lastteamapplychattime != _o_.lastteamapplychattime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += lastworldchattime;
			_h_ += lastcurrentchattime;
			_h_ += lastprofessionchattime;
			_h_ += lastclanchattime;
			_h_ += lastteamapplychattime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(lastworldchattime);
			_sb_.append(",");
			_sb_.append(lastcurrentchattime);
			_sb_.append(",");
			_sb_.append(lastprofessionchattime);
			_sb_.append(",");
			_sb_.append(lastclanchattime);
			_sb_.append(",");
			_sb_.append(lastteamapplychattime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
