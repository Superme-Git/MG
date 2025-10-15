
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class InviteInfo extends mkdb.XBean implements xbean.InviteInfo {
	private boolean beinginvited; // 
	private xbean.TeamInvite inviting; // 
	private java.util.LinkedList<xbean.TeamInvite> invited; // 

	@Override
	public void _reset_unsafe_() {
		beinginvited = false;
		inviting._reset_unsafe_();
		invited.clear();
	}

	InviteInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		inviting = new TeamInvite(0, this, "inviting");
		invited = new java.util.LinkedList<xbean.TeamInvite>();
	}

	public InviteInfo() {
		this(0, null, null);
	}

	public InviteInfo(InviteInfo _o_) {
		this(_o_, null, null);
	}

	InviteInfo(xbean.InviteInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof InviteInfo) assign((InviteInfo)_o1_);
		else if (_o1_ instanceof InviteInfo.Data) assign((InviteInfo.Data)_o1_);
		else if (_o1_ instanceof InviteInfo.Const) assign(((InviteInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(InviteInfo _o_) {
		_o_._xdb_verify_unsafe_();
		beinginvited = _o_.beinginvited;
		inviting = new TeamInvite(_o_.inviting, this, "inviting");
		invited = new java.util.LinkedList<xbean.TeamInvite>();
		for (xbean.TeamInvite _v_ : _o_.invited)
			invited.add(new TeamInvite(_v_, this, "invited"));
	}

	private void assign(InviteInfo.Data _o_) {
		beinginvited = _o_.beinginvited;
		inviting = new TeamInvite(_o_.inviting, this, "inviting");
		invited = new java.util.LinkedList<xbean.TeamInvite>();
		for (xbean.TeamInvite _v_ : _o_.invited)
			invited.add(new TeamInvite(_v_, this, "invited"));
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(beinginvited);
		inviting.marshal(_os_);
		_os_.compact_uint32(invited.size());
		for (xbean.TeamInvite _v_ : invited) {
			_v_.marshal(_os_);
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		beinginvited = _os_.unmarshal_boolean();
		inviting.unmarshal(_os_);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.TeamInvite _v_ = new TeamInvite(0, this, "invited");
			_v_.unmarshal(_os_);
			invited.add(_v_);
		}
		return _os_;
	}

	@Override
	public xbean.InviteInfo copy() {
		_xdb_verify_unsafe_();
		return new InviteInfo(this);
	}

	@Override
	public xbean.InviteInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InviteInfo toBean() {
		_xdb_verify_unsafe_();
		return new InviteInfo(this); // same as copy()
	}

	@Override
	public xbean.InviteInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.InviteInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public boolean getBeinginvited() { // 
		_xdb_verify_unsafe_();
		return beinginvited;
	}

	@Override
	public xbean.TeamInvite getInviting() { // 
		_xdb_verify_unsafe_();
		return inviting;
	}

	@Override
	public java.util.List<xbean.TeamInvite> getInvited() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "invited"), invited);
	}

	public java.util.List<xbean.TeamInvite> getInvitedAsData() { // 
		_xdb_verify_unsafe_();
		java.util.List<xbean.TeamInvite> invited;
		InviteInfo _o_ = this;
		invited = new java.util.LinkedList<xbean.TeamInvite>();
		for (xbean.TeamInvite _v_ : _o_.invited)
			invited.add(new TeamInvite.Data(_v_));
		return invited;
	}

	@Override
	public void setBeinginvited(boolean _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "beinginvited") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, beinginvited) {
					public void rollback() { beinginvited = _xdb_saved; }
				};}});
		beinginvited = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		InviteInfo _o_ = null;
		if ( _o1_ instanceof InviteInfo ) _o_ = (InviteInfo)_o1_;
		else if ( _o1_ instanceof InviteInfo.Const ) _o_ = ((InviteInfo.Const)_o1_).nThis();
		else return false;
		if (beinginvited != _o_.beinginvited) return false;
		if (!inviting.equals(_o_.inviting)) return false;
		if (!invited.equals(_o_.invited)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += beinginvited ? 1231 : 1237;
		_h_ += inviting.hashCode();
		_h_ += invited.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(beinginvited);
		_sb_.append(",");
		_sb_.append(inviting);
		_sb_.append(",");
		_sb_.append(invited);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("beinginvited"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("inviting"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("invited"));
		return lb;
	}

	private class Const implements xbean.InviteInfo {
		InviteInfo nThis() {
			return InviteInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.InviteInfo copy() {
			return InviteInfo.this.copy();
		}

		@Override
		public xbean.InviteInfo toData() {
			return InviteInfo.this.toData();
		}

		public xbean.InviteInfo toBean() {
			return InviteInfo.this.toBean();
		}

		@Override
		public xbean.InviteInfo toDataIf() {
			return InviteInfo.this.toDataIf();
		}

		public xbean.InviteInfo toBeanIf() {
			return InviteInfo.this.toBeanIf();
		}

		@Override
		public boolean getBeinginvited() { // 
			_xdb_verify_unsafe_();
			return beinginvited;
		}

		@Override
		public xbean.TeamInvite getInviting() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(inviting);
		}

		@Override
		public java.util.List<xbean.TeamInvite> getInvited() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(invited);
		}

		public java.util.List<xbean.TeamInvite> getInvitedAsData() { // 
			_xdb_verify_unsafe_();
			java.util.List<xbean.TeamInvite> invited;
			InviteInfo _o_ = InviteInfo.this;
		invited = new java.util.LinkedList<xbean.TeamInvite>();
		for (xbean.TeamInvite _v_ : _o_.invited)
			invited.add(new TeamInvite.Data(_v_));
			return invited;
		}

		@Override
		public void setBeinginvited(boolean _v_) { // 
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
			return InviteInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return InviteInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return InviteInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return InviteInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return InviteInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return InviteInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return InviteInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return InviteInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return InviteInfo.this.toString();
		}

	}

	public static final class Data implements xbean.InviteInfo {
		private boolean beinginvited; // 
		private xbean.TeamInvite inviting; // 
		private java.util.LinkedList<xbean.TeamInvite> invited; // 

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			inviting = new TeamInvite.Data();
			invited = new java.util.LinkedList<xbean.TeamInvite>();
		}

		Data(xbean.InviteInfo _o1_) {
			if (_o1_ instanceof InviteInfo) assign((InviteInfo)_o1_);
			else if (_o1_ instanceof InviteInfo.Data) assign((InviteInfo.Data)_o1_);
			else if (_o1_ instanceof InviteInfo.Const) assign(((InviteInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(InviteInfo _o_) {
			beinginvited = _o_.beinginvited;
			inviting = new TeamInvite.Data(_o_.inviting);
			invited = new java.util.LinkedList<xbean.TeamInvite>();
			for (xbean.TeamInvite _v_ : _o_.invited)
				invited.add(new TeamInvite.Data(_v_));
		}

		private void assign(InviteInfo.Data _o_) {
			beinginvited = _o_.beinginvited;
			inviting = new TeamInvite.Data(_o_.inviting);
			invited = new java.util.LinkedList<xbean.TeamInvite>();
			for (xbean.TeamInvite _v_ : _o_.invited)
				invited.add(new TeamInvite.Data(_v_));
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(beinginvited);
			inviting.marshal(_os_);
			_os_.compact_uint32(invited.size());
			for (xbean.TeamInvite _v_ : invited) {
				_v_.marshal(_os_);
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			beinginvited = _os_.unmarshal_boolean();
			inviting.unmarshal(_os_);
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.TeamInvite _v_ = xbean.Pod.newTeamInviteData();
				_v_.unmarshal(_os_);
				invited.add(_v_);
			}
			return _os_;
		}

		@Override
		public xbean.InviteInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.InviteInfo toData() {
			return new Data(this);
		}

		public xbean.InviteInfo toBean() {
			return new InviteInfo(this, null, null);
		}

		@Override
		public xbean.InviteInfo toDataIf() {
			return this;
		}

		public xbean.InviteInfo toBeanIf() {
			return new InviteInfo(this, null, null);
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
		public boolean getBeinginvited() { // 
			return beinginvited;
		}

		@Override
		public xbean.TeamInvite getInviting() { // 
			return inviting;
		}

		@Override
		public java.util.List<xbean.TeamInvite> getInvited() { // 
			return invited;
		}

		@Override
		public java.util.List<xbean.TeamInvite> getInvitedAsData() { // 
			return invited;
		}

		@Override
		public void setBeinginvited(boolean _v_) { // 
			beinginvited = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof InviteInfo.Data)) return false;
			InviteInfo.Data _o_ = (InviteInfo.Data) _o1_;
			if (beinginvited != _o_.beinginvited) return false;
			if (!inviting.equals(_o_.inviting)) return false;
			if (!invited.equals(_o_.invited)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += beinginvited ? 1231 : 1237;
			_h_ += inviting.hashCode();
			_h_ += invited.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(beinginvited);
			_sb_.append(",");
			_sb_.append(inviting);
			_sb_.append(",");
			_sb_.append(invited);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
