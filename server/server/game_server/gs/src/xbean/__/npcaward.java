
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class npcaward extends mkdb.XBean implements xbean.npcaward {
	private long lasttime; // 上次领取时间
	private int count; // 已经领取次数
	private int totalcount; // 领取总次数

	@Override
	public void _reset_unsafe_() {
		lasttime = 0L;
		count = 0;
		totalcount = 0;
	}

	npcaward(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public npcaward() {
		this(0, null, null);
	}

	public npcaward(npcaward _o_) {
		this(_o_, null, null);
	}

	npcaward(xbean.npcaward _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof npcaward) assign((npcaward)_o1_);
		else if (_o1_ instanceof npcaward.Data) assign((npcaward.Data)_o1_);
		else if (_o1_ instanceof npcaward.Const) assign(((npcaward.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(npcaward _o_) {
		_o_._xdb_verify_unsafe_();
		lasttime = _o_.lasttime;
		count = _o_.count;
		totalcount = _o_.totalcount;
	}

	private void assign(npcaward.Data _o_) {
		lasttime = _o_.lasttime;
		count = _o_.count;
		totalcount = _o_.totalcount;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(lasttime);
		_os_.marshal(count);
		_os_.marshal(totalcount);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		lasttime = _os_.unmarshal_long();
		count = _os_.unmarshal_int();
		totalcount = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.npcaward copy() {
		_xdb_verify_unsafe_();
		return new npcaward(this);
	}

	@Override
	public xbean.npcaward toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.npcaward toBean() {
		_xdb_verify_unsafe_();
		return new npcaward(this); // same as copy()
	}

	@Override
	public xbean.npcaward toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.npcaward toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getLasttime() { // 上次领取时间
		_xdb_verify_unsafe_();
		return lasttime;
	}

	@Override
	public int getCount() { // 已经领取次数
		_xdb_verify_unsafe_();
		return count;
	}

	@Override
	public int getTotalcount() { // 领取总次数
		_xdb_verify_unsafe_();
		return totalcount;
	}

	@Override
	public void setLasttime(long _v_) { // 上次领取时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lasttime) {
					public void rollback() { lasttime = _xdb_saved; }
				};}});
		lasttime = _v_;
	}

	@Override
	public void setCount(int _v_) { // 已经领取次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "count") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, count) {
					public void rollback() { count = _xdb_saved; }
				};}});
		count = _v_;
	}

	@Override
	public void setTotalcount(int _v_) { // 领取总次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "totalcount") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, totalcount) {
					public void rollback() { totalcount = _xdb_saved; }
				};}});
		totalcount = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		npcaward _o_ = null;
		if ( _o1_ instanceof npcaward ) _o_ = (npcaward)_o1_;
		else if ( _o1_ instanceof npcaward.Const ) _o_ = ((npcaward.Const)_o1_).nThis();
		else return false;
		if (lasttime != _o_.lasttime) return false;
		if (count != _o_.count) return false;
		if (totalcount != _o_.totalcount) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += lasttime;
		_h_ += count;
		_h_ += totalcount;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(lasttime);
		_sb_.append(",");
		_sb_.append(count);
		_sb_.append(",");
		_sb_.append(totalcount);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("count"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("totalcount"));
		return lb;
	}

	private class Const implements xbean.npcaward {
		npcaward nThis() {
			return npcaward.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.npcaward copy() {
			return npcaward.this.copy();
		}

		@Override
		public xbean.npcaward toData() {
			return npcaward.this.toData();
		}

		public xbean.npcaward toBean() {
			return npcaward.this.toBean();
		}

		@Override
		public xbean.npcaward toDataIf() {
			return npcaward.this.toDataIf();
		}

		public xbean.npcaward toBeanIf() {
			return npcaward.this.toBeanIf();
		}

		@Override
		public long getLasttime() { // 上次领取时间
			_xdb_verify_unsafe_();
			return lasttime;
		}

		@Override
		public int getCount() { // 已经领取次数
			_xdb_verify_unsafe_();
			return count;
		}

		@Override
		public int getTotalcount() { // 领取总次数
			_xdb_verify_unsafe_();
			return totalcount;
		}

		@Override
		public void setLasttime(long _v_) { // 上次领取时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCount(int _v_) { // 已经领取次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTotalcount(int _v_) { // 领取总次数
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
			return npcaward.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return npcaward.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return npcaward.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return npcaward.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return npcaward.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return npcaward.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return npcaward.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return npcaward.this.hashCode();
		}

		@Override
		public String toString() {
			return npcaward.this.toString();
		}

	}

	public static final class Data implements xbean.npcaward {
		private long lasttime; // 上次领取时间
		private int count; // 已经领取次数
		private int totalcount; // 领取总次数

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.npcaward _o1_) {
			if (_o1_ instanceof npcaward) assign((npcaward)_o1_);
			else if (_o1_ instanceof npcaward.Data) assign((npcaward.Data)_o1_);
			else if (_o1_ instanceof npcaward.Const) assign(((npcaward.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(npcaward _o_) {
			lasttime = _o_.lasttime;
			count = _o_.count;
			totalcount = _o_.totalcount;
		}

		private void assign(npcaward.Data _o_) {
			lasttime = _o_.lasttime;
			count = _o_.count;
			totalcount = _o_.totalcount;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(lasttime);
			_os_.marshal(count);
			_os_.marshal(totalcount);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			lasttime = _os_.unmarshal_long();
			count = _os_.unmarshal_int();
			totalcount = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.npcaward copy() {
			return new Data(this);
		}

		@Override
		public xbean.npcaward toData() {
			return new Data(this);
		}

		public xbean.npcaward toBean() {
			return new npcaward(this, null, null);
		}

		@Override
		public xbean.npcaward toDataIf() {
			return this;
		}

		public xbean.npcaward toBeanIf() {
			return new npcaward(this, null, null);
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
		public long getLasttime() { // 上次领取时间
			return lasttime;
		}

		@Override
		public int getCount() { // 已经领取次数
			return count;
		}

		@Override
		public int getTotalcount() { // 领取总次数
			return totalcount;
		}

		@Override
		public void setLasttime(long _v_) { // 上次领取时间
			lasttime = _v_;
		}

		@Override
		public void setCount(int _v_) { // 已经领取次数
			count = _v_;
		}

		@Override
		public void setTotalcount(int _v_) { // 领取总次数
			totalcount = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof npcaward.Data)) return false;
			npcaward.Data _o_ = (npcaward.Data) _o1_;
			if (lasttime != _o_.lasttime) return false;
			if (count != _o_.count) return false;
			if (totalcount != _o_.totalcount) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += lasttime;
			_h_ += count;
			_h_ += totalcount;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(lasttime);
			_sb_.append(",");
			_sb_.append(count);
			_sb_.append(",");
			_sb_.append(totalcount);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
