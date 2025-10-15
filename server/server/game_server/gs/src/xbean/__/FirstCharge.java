
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class FirstCharge extends mkdb.XBean implements xbean.FirstCharge {
	private long firstchargetime; // 首冲的时间,可以被刷新的
	private long firstchargeclearpresenttime; // 
	private long chargestatus; // 记录玩家首冲数据，0--未充过值 1-－充值了，未领取礼包 2，领取礼包了

	@Override
	public void _reset_unsafe_() {
		firstchargetime = 0L;
		firstchargeclearpresenttime = 0L;
		chargestatus = 0L;
	}

	FirstCharge(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public FirstCharge() {
		this(0, null, null);
	}

	public FirstCharge(FirstCharge _o_) {
		this(_o_, null, null);
	}

	FirstCharge(xbean.FirstCharge _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof FirstCharge) assign((FirstCharge)_o1_);
		else if (_o1_ instanceof FirstCharge.Data) assign((FirstCharge.Data)_o1_);
		else if (_o1_ instanceof FirstCharge.Const) assign(((FirstCharge.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(FirstCharge _o_) {
		_o_._xdb_verify_unsafe_();
		firstchargetime = _o_.firstchargetime;
		firstchargeclearpresenttime = _o_.firstchargeclearpresenttime;
		chargestatus = _o_.chargestatus;
	}

	private void assign(FirstCharge.Data _o_) {
		firstchargetime = _o_.firstchargetime;
		firstchargeclearpresenttime = _o_.firstchargeclearpresenttime;
		chargestatus = _o_.chargestatus;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(firstchargetime);
		_os_.marshal(firstchargeclearpresenttime);
		_os_.marshal(chargestatus);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		firstchargetime = _os_.unmarshal_long();
		firstchargeclearpresenttime = _os_.unmarshal_long();
		chargestatus = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.FirstCharge copy() {
		_xdb_verify_unsafe_();
		return new FirstCharge(this);
	}

	@Override
	public xbean.FirstCharge toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FirstCharge toBean() {
		_xdb_verify_unsafe_();
		return new FirstCharge(this); // same as copy()
	}

	@Override
	public xbean.FirstCharge toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.FirstCharge toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getFirstchargetime() { // 首冲的时间,可以被刷新的
		_xdb_verify_unsafe_();
		return firstchargetime;
	}

	@Override
	public long getFirstchargeclearpresenttime() { // 
		_xdb_verify_unsafe_();
		return firstchargeclearpresenttime;
	}

	@Override
	public long getChargestatus() { // 记录玩家首冲数据，0--未充过值 1-－充值了，未领取礼包 2，领取礼包了
		_xdb_verify_unsafe_();
		return chargestatus;
	}

	@Override
	public void setFirstchargetime(long _v_) { // 首冲的时间,可以被刷新的
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firstchargetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, firstchargetime) {
					public void rollback() { firstchargetime = _xdb_saved; }
				};}});
		firstchargetime = _v_;
	}

	@Override
	public void setFirstchargeclearpresenttime(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firstchargeclearpresenttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, firstchargeclearpresenttime) {
					public void rollback() { firstchargeclearpresenttime = _xdb_saved; }
				};}});
		firstchargeclearpresenttime = _v_;
	}

	@Override
	public void setChargestatus(long _v_) { // 记录玩家首冲数据，0--未充过值 1-－充值了，未领取礼包 2，领取礼包了
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "chargestatus") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, chargestatus) {
					public void rollback() { chargestatus = _xdb_saved; }
				};}});
		chargestatus = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		FirstCharge _o_ = null;
		if ( _o1_ instanceof FirstCharge ) _o_ = (FirstCharge)_o1_;
		else if ( _o1_ instanceof FirstCharge.Const ) _o_ = ((FirstCharge.Const)_o1_).nThis();
		else return false;
		if (firstchargetime != _o_.firstchargetime) return false;
		if (firstchargeclearpresenttime != _o_.firstchargeclearpresenttime) return false;
		if (chargestatus != _o_.chargestatus) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += firstchargetime;
		_h_ += firstchargeclearpresenttime;
		_h_ += chargestatus;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(firstchargetime);
		_sb_.append(",");
		_sb_.append(firstchargeclearpresenttime);
		_sb_.append(",");
		_sb_.append(chargestatus);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firstchargetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firstchargeclearpresenttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("chargestatus"));
		return lb;
	}

	private class Const implements xbean.FirstCharge {
		FirstCharge nThis() {
			return FirstCharge.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.FirstCharge copy() {
			return FirstCharge.this.copy();
		}

		@Override
		public xbean.FirstCharge toData() {
			return FirstCharge.this.toData();
		}

		public xbean.FirstCharge toBean() {
			return FirstCharge.this.toBean();
		}

		@Override
		public xbean.FirstCharge toDataIf() {
			return FirstCharge.this.toDataIf();
		}

		public xbean.FirstCharge toBeanIf() {
			return FirstCharge.this.toBeanIf();
		}

		@Override
		public long getFirstchargetime() { // 首冲的时间,可以被刷新的
			_xdb_verify_unsafe_();
			return firstchargetime;
		}

		@Override
		public long getFirstchargeclearpresenttime() { // 
			_xdb_verify_unsafe_();
			return firstchargeclearpresenttime;
		}

		@Override
		public long getChargestatus() { // 记录玩家首冲数据，0--未充过值 1-－充值了，未领取礼包 2，领取礼包了
			_xdb_verify_unsafe_();
			return chargestatus;
		}

		@Override
		public void setFirstchargetime(long _v_) { // 首冲的时间,可以被刷新的
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirstchargeclearpresenttime(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setChargestatus(long _v_) { // 记录玩家首冲数据，0--未充过值 1-－充值了，未领取礼包 2，领取礼包了
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
			return FirstCharge.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return FirstCharge.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return FirstCharge.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return FirstCharge.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return FirstCharge.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return FirstCharge.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return FirstCharge.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return FirstCharge.this.hashCode();
		}

		@Override
		public String toString() {
			return FirstCharge.this.toString();
		}

	}

	public static final class Data implements xbean.FirstCharge {
		private long firstchargetime; // 首冲的时间,可以被刷新的
		private long firstchargeclearpresenttime; // 
		private long chargestatus; // 记录玩家首冲数据，0--未充过值 1-－充值了，未领取礼包 2，领取礼包了

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.FirstCharge _o1_) {
			if (_o1_ instanceof FirstCharge) assign((FirstCharge)_o1_);
			else if (_o1_ instanceof FirstCharge.Data) assign((FirstCharge.Data)_o1_);
			else if (_o1_ instanceof FirstCharge.Const) assign(((FirstCharge.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(FirstCharge _o_) {
			firstchargetime = _o_.firstchargetime;
			firstchargeclearpresenttime = _o_.firstchargeclearpresenttime;
			chargestatus = _o_.chargestatus;
		}

		private void assign(FirstCharge.Data _o_) {
			firstchargetime = _o_.firstchargetime;
			firstchargeclearpresenttime = _o_.firstchargeclearpresenttime;
			chargestatus = _o_.chargestatus;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(firstchargetime);
			_os_.marshal(firstchargeclearpresenttime);
			_os_.marshal(chargestatus);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			firstchargetime = _os_.unmarshal_long();
			firstchargeclearpresenttime = _os_.unmarshal_long();
			chargestatus = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.FirstCharge copy() {
			return new Data(this);
		}

		@Override
		public xbean.FirstCharge toData() {
			return new Data(this);
		}

		public xbean.FirstCharge toBean() {
			return new FirstCharge(this, null, null);
		}

		@Override
		public xbean.FirstCharge toDataIf() {
			return this;
		}

		public xbean.FirstCharge toBeanIf() {
			return new FirstCharge(this, null, null);
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
		public long getFirstchargetime() { // 首冲的时间,可以被刷新的
			return firstchargetime;
		}

		@Override
		public long getFirstchargeclearpresenttime() { // 
			return firstchargeclearpresenttime;
		}

		@Override
		public long getChargestatus() { // 记录玩家首冲数据，0--未充过值 1-－充值了，未领取礼包 2，领取礼包了
			return chargestatus;
		}

		@Override
		public void setFirstchargetime(long _v_) { // 首冲的时间,可以被刷新的
			firstchargetime = _v_;
		}

		@Override
		public void setFirstchargeclearpresenttime(long _v_) { // 
			firstchargeclearpresenttime = _v_;
		}

		@Override
		public void setChargestatus(long _v_) { // 记录玩家首冲数据，0--未充过值 1-－充值了，未领取礼包 2，领取礼包了
			chargestatus = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof FirstCharge.Data)) return false;
			FirstCharge.Data _o_ = (FirstCharge.Data) _o1_;
			if (firstchargetime != _o_.firstchargetime) return false;
			if (firstchargeclearpresenttime != _o_.firstchargeclearpresenttime) return false;
			if (chargestatus != _o_.chargestatus) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += firstchargetime;
			_h_ += firstchargeclearpresenttime;
			_h_ += chargestatus;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(firstchargetime);
			_sb_.append(",");
			_sb_.append(firstchargeclearpresenttime);
			_sb_.append(",");
			_sb_.append(chargestatus);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
