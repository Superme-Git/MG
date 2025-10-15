
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InvitePeopleInfo extends mkdb.XBean implements xbean.InvitePeopleInfo {
	private long inviteme; // 邀请我的人
	private java.util.LinkedList<Long> aminvites; // 我邀请的人
	private java.util.LinkedList<Integer> awardhistory; // 已经领取过的奖励ID 0-填写邀请人奖励

	@Override
	public void _reset_unsafe_() {
		inviteme = 0L;
		aminvites.clear();
		awardhistory.clear();
	}

	InvitePeopleInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		aminvites = new java.util.LinkedList<Long>();
		awardhistory = new java.util.LinkedList<Integer>();
	}

	public InvitePeopleInfo() {
		this(0, null, null);
	}

	public InvitePeopleInfo(InvitePeopleInfo _o_) {
		this(_o_, null, null);
	}

	InvitePeopleInfo(xbean.InvitePeopleInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InvitePeopleInfo) assign((InvitePeopleInfo)_o1_);
		else if (_o1_ instanceof InvitePeopleInfo.Data) assign((InvitePeopleInfo.Data)_o1_);
		else if (_o1_ instanceof InvitePeopleInfo.Const) assign(((InvitePeopleInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InvitePeopleInfo _o_) {
		_o_._xdb_verify_unsafe_();
		inviteme = _o_.inviteme;
		aminvites = new java.util.LinkedList<Long>();
		aminvites.addAll(_o_.aminvites);
		awardhistory = new java.util.LinkedList<Integer>();
		awardhistory.addAll(_o_.awardhistory);
	}

	private void assign(InvitePeopleInfo.Data _o_) {
		inviteme = _o_.inviteme;
		aminvites = new java.util.LinkedList<Long>();
		aminvites.addAll(_o_.aminvites);
		awardhistory = new java.util.LinkedList<Integer>();
		awardhistory.addAll(_o_.awardhistory);
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(inviteme);
		_os_.compact_uint32(aminvites.size());
		for (Long _v_ : aminvites) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(awardhistory.size());
		for (Integer _v_ : awardhistory) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		inviteme = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			aminvites.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			awardhistory.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.InvitePeopleInfo copy() {
		_xdb_verify_unsafe_();
		return new InvitePeopleInfo(this);
	}

	@Override
	public xbean.InvitePeopleInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InvitePeopleInfo toBean() {
		_xdb_verify_unsafe_();
		return new InvitePeopleInfo(this); // same as copy()
	}

	@Override
	public xbean.InvitePeopleInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InvitePeopleInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getInviteme() { // 邀请我的人
		_xdb_verify_unsafe_();
		return inviteme;
	}

	@Override
	public java.util.List<Long> getAminvites() { // 我邀请的人
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "aminvites"), aminvites);
	}

	public java.util.List<Long> getAminvitesAsData() { // 我邀请的人
		_xdb_verify_unsafe_();
		java.util.List<Long> aminvites;
		InvitePeopleInfo _o_ = this;
		aminvites = new java.util.LinkedList<Long>();
		aminvites.addAll(_o_.aminvites);
		return aminvites;
	}

	@Override
	public java.util.List<Integer> getAwardhistory() { // 已经领取过的奖励ID 0-填写邀请人奖励
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "awardhistory"), awardhistory);
	}

	public java.util.List<Integer> getAwardhistoryAsData() { // 已经领取过的奖励ID 0-填写邀请人奖励
		_xdb_verify_unsafe_();
		java.util.List<Integer> awardhistory;
		InvitePeopleInfo _o_ = this;
		awardhistory = new java.util.LinkedList<Integer>();
		awardhistory.addAll(_o_.awardhistory);
		return awardhistory;
	}

	@Override
	public void setInviteme(long _v_) { // 邀请我的人
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "inviteme") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, inviteme) {
					public void rollback() { inviteme = _xdb_saved; }
				};}});
		inviteme = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InvitePeopleInfo _o_ = null;
		if ( _o1_ instanceof InvitePeopleInfo ) _o_ = (InvitePeopleInfo)_o1_;
		else if ( _o1_ instanceof InvitePeopleInfo.Const ) _o_ = ((InvitePeopleInfo.Const)_o1_).nThis();
		else return false;
		if (inviteme != _o_.inviteme) return false;
		if (!aminvites.equals(_o_.aminvites)) return false;
		if (!awardhistory.equals(_o_.awardhistory)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += inviteme;
		_h_ += aminvites.hashCode();
		_h_ += awardhistory.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(inviteme);
		_sb_.append(",");
		_sb_.append(aminvites);
		_sb_.append(",");
		_sb_.append(awardhistory);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("inviteme"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("aminvites"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("awardhistory"));
		return lb;
	}

	private class Const implements xbean.InvitePeopleInfo {
		InvitePeopleInfo nThis() {
			return InvitePeopleInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InvitePeopleInfo copy() {
			return InvitePeopleInfo.this.copy();
		}

		@Override
		public xbean.InvitePeopleInfo toData() {
			return InvitePeopleInfo.this.toData();
		}

		public xbean.InvitePeopleInfo toBean() {
			return InvitePeopleInfo.this.toBean();
		}

		@Override
		public xbean.InvitePeopleInfo toDataIf() {
			return InvitePeopleInfo.this.toDataIf();
		}

		public xbean.InvitePeopleInfo toBeanIf() {
			return InvitePeopleInfo.this.toBeanIf();
		}

		@Override
		public long getInviteme() { // 邀请我的人
			_xdb_verify_unsafe_();
			return inviteme;
		}

		@Override
		public java.util.List<Long> getAminvites() { // 我邀请的人
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(aminvites);
		}

		public java.util.List<Long> getAminvitesAsData() { // 我邀请的人
			_xdb_verify_unsafe_();
			java.util.List<Long> aminvites;
			InvitePeopleInfo _o_ = InvitePeopleInfo.this;
		aminvites = new java.util.LinkedList<Long>();
		aminvites.addAll(_o_.aminvites);
			return aminvites;
		}

		@Override
		public java.util.List<Integer> getAwardhistory() { // 已经领取过的奖励ID 0-填写邀请人奖励
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(awardhistory);
		}

		public java.util.List<Integer> getAwardhistoryAsData() { // 已经领取过的奖励ID 0-填写邀请人奖励
			_xdb_verify_unsafe_();
			java.util.List<Integer> awardhistory;
			InvitePeopleInfo _o_ = InvitePeopleInfo.this;
		awardhistory = new java.util.LinkedList<Integer>();
		awardhistory.addAll(_o_.awardhistory);
			return awardhistory;
		}

		@Override
		public void setInviteme(long _v_) { // 邀请我的人
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
			return InvitePeopleInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InvitePeopleInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InvitePeopleInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InvitePeopleInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InvitePeopleInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InvitePeopleInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InvitePeopleInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InvitePeopleInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return InvitePeopleInfo.this.toString();
		}

	}

	public static final class Data implements xbean.InvitePeopleInfo {
		private long inviteme; // 邀请我的人
		private java.util.LinkedList<Long> aminvites; // 我邀请的人
		private java.util.LinkedList<Integer> awardhistory; // 已经领取过的奖励ID 0-填写邀请人奖励

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			aminvites = new java.util.LinkedList<Long>();
			awardhistory = new java.util.LinkedList<Integer>();
		}

		Data(xbean.InvitePeopleInfo _o1_) {
			if (_o1_ instanceof InvitePeopleInfo) assign((InvitePeopleInfo)_o1_);
			else if (_o1_ instanceof InvitePeopleInfo.Data) assign((InvitePeopleInfo.Data)_o1_);
			else if (_o1_ instanceof InvitePeopleInfo.Const) assign(((InvitePeopleInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InvitePeopleInfo _o_) {
			inviteme = _o_.inviteme;
			aminvites = new java.util.LinkedList<Long>();
			aminvites.addAll(_o_.aminvites);
			awardhistory = new java.util.LinkedList<Integer>();
			awardhistory.addAll(_o_.awardhistory);
		}

		private void assign(InvitePeopleInfo.Data _o_) {
			inviteme = _o_.inviteme;
			aminvites = new java.util.LinkedList<Long>();
			aminvites.addAll(_o_.aminvites);
			awardhistory = new java.util.LinkedList<Integer>();
			awardhistory.addAll(_o_.awardhistory);
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(inviteme);
			_os_.compact_uint32(aminvites.size());
			for (Long _v_ : aminvites) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(awardhistory.size());
			for (Integer _v_ : awardhistory) {
				_os_.marshal(_v_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			inviteme = _os_.unmarshal_long();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				aminvites.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				awardhistory.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.InvitePeopleInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.InvitePeopleInfo toData() {
			return new Data(this);
		}

		public xbean.InvitePeopleInfo toBean() {
			return new InvitePeopleInfo(this, null, null);
		}

		@Override
		public xbean.InvitePeopleInfo toDataIf() {
			return this;
		}

		public xbean.InvitePeopleInfo toBeanIf() {
			return new InvitePeopleInfo(this, null, null);
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
		public long getInviteme() { // 邀请我的人
			return inviteme;
		}

		@Override
		public java.util.List<Long> getAminvites() { // 我邀请的人
			return aminvites;
		}

		@Override
		public java.util.List<Long> getAminvitesAsData() { // 我邀请的人
			return aminvites;
		}

		@Override
		public java.util.List<Integer> getAwardhistory() { // 已经领取过的奖励ID 0-填写邀请人奖励
			return awardhistory;
		}

		@Override
		public java.util.List<Integer> getAwardhistoryAsData() { // 已经领取过的奖励ID 0-填写邀请人奖励
			return awardhistory;
		}

		@Override
		public void setInviteme(long _v_) { // 邀请我的人
			inviteme = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InvitePeopleInfo.Data)) return false;
			InvitePeopleInfo.Data _o_ = (InvitePeopleInfo.Data) _o1_;
			if (inviteme != _o_.inviteme) return false;
			if (!aminvites.equals(_o_.aminvites)) return false;
			if (!awardhistory.equals(_o_.awardhistory)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += inviteme;
			_h_ += aminvites.hashCode();
			_h_ += awardhistory.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(inviteme);
			_sb_.append(",");
			_sb_.append(aminvites);
			_sb_.append(",");
			_sb_.append(awardhistory);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
