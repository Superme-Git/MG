
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class NpcFollowInfo extends mkdb.XBean implements xbean.NpcFollowInfo {
	private int npcid; // npc跟随的id
	private int questid; // 
	private long validdate; // npc跟随的时限

	@Override
	public void _reset_unsafe_() {
		npcid = 0;
		questid = 0;
		validdate = 0;
	}

	NpcFollowInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		npcid = 0;
		questid = 0;
		validdate = 0;
	}

	public NpcFollowInfo() {
		this(0, null, null);
	}

	public NpcFollowInfo(NpcFollowInfo _o_) {
		this(_o_, null, null);
	}

	NpcFollowInfo(xbean.NpcFollowInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof NpcFollowInfo) assign((NpcFollowInfo)_o1_);
		else if (_o1_ instanceof NpcFollowInfo.Data) assign((NpcFollowInfo.Data)_o1_);
		else if (_o1_ instanceof NpcFollowInfo.Const) assign(((NpcFollowInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(NpcFollowInfo _o_) {
		_o_._xdb_verify_unsafe_();
		npcid = _o_.npcid;
		questid = _o_.questid;
		validdate = _o_.validdate;
	}

	private void assign(NpcFollowInfo.Data _o_) {
		npcid = _o_.npcid;
		questid = _o_.questid;
		validdate = _o_.validdate;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(npcid);
		_os_.marshal(questid);
		_os_.marshal(validdate);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		npcid = _os_.unmarshal_int();
		questid = _os_.unmarshal_int();
		validdate = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.NpcFollowInfo copy() {
		_xdb_verify_unsafe_();
		return new NpcFollowInfo(this);
	}

	@Override
	public xbean.NpcFollowInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NpcFollowInfo toBean() {
		_xdb_verify_unsafe_();
		return new NpcFollowInfo(this); // same as copy()
	}

	@Override
	public xbean.NpcFollowInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.NpcFollowInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getNpcid() { // npc跟随的id
		_xdb_verify_unsafe_();
		return npcid;
	}

	@Override
	public int getQuestid() { // 
		_xdb_verify_unsafe_();
		return questid;
	}

	@Override
	public long getValiddate() { // npc跟随的时限
		_xdb_verify_unsafe_();
		return validdate;
	}

	@Override
	public void setNpcid(int _v_) { // npc跟随的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "npcid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, npcid) {
					public void rollback() { npcid = _xdb_saved; }
				};}});
		npcid = _v_;
	}

	@Override
	public void setQuestid(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "questid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, questid) {
					public void rollback() { questid = _xdb_saved; }
				};}});
		questid = _v_;
	}

	@Override
	public void setValiddate(long _v_) { // npc跟随的时限
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "validdate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, validdate) {
					public void rollback() { validdate = _xdb_saved; }
				};}});
		validdate = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		NpcFollowInfo _o_ = null;
		if ( _o1_ instanceof NpcFollowInfo ) _o_ = (NpcFollowInfo)_o1_;
		else if ( _o1_ instanceof NpcFollowInfo.Const ) _o_ = ((NpcFollowInfo.Const)_o1_).nThis();
		else return false;
		if (npcid != _o_.npcid) return false;
		if (questid != _o_.questid) return false;
		if (validdate != _o_.validdate) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += npcid;
		_h_ += questid;
		_h_ += validdate;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcid);
		_sb_.append(",");
		_sb_.append(questid);
		_sb_.append(",");
		_sb_.append(validdate);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("npcid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("questid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("validdate"));
		return lb;
	}

	private class Const implements xbean.NpcFollowInfo {
		NpcFollowInfo nThis() {
			return NpcFollowInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.NpcFollowInfo copy() {
			return NpcFollowInfo.this.copy();
		}

		@Override
		public xbean.NpcFollowInfo toData() {
			return NpcFollowInfo.this.toData();
		}

		public xbean.NpcFollowInfo toBean() {
			return NpcFollowInfo.this.toBean();
		}

		@Override
		public xbean.NpcFollowInfo toDataIf() {
			return NpcFollowInfo.this.toDataIf();
		}

		public xbean.NpcFollowInfo toBeanIf() {
			return NpcFollowInfo.this.toBeanIf();
		}

		@Override
		public int getNpcid() { // npc跟随的id
			_xdb_verify_unsafe_();
			return npcid;
		}

		@Override
		public int getQuestid() { // 
			_xdb_verify_unsafe_();
			return questid;
		}

		@Override
		public long getValiddate() { // npc跟随的时限
			_xdb_verify_unsafe_();
			return validdate;
		}

		@Override
		public void setNpcid(int _v_) { // npc跟随的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setQuestid(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setValiddate(long _v_) { // npc跟随的时限
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
			return NpcFollowInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return NpcFollowInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return NpcFollowInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return NpcFollowInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return NpcFollowInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return NpcFollowInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return NpcFollowInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return NpcFollowInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return NpcFollowInfo.this.toString();
		}

	}

	public static final class Data implements xbean.NpcFollowInfo {
		private int npcid; // npc跟随的id
		private int questid; // 
		private long validdate; // npc跟随的时限

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			npcid = 0;
			questid = 0;
			validdate = 0;
		}

		Data(xbean.NpcFollowInfo _o1_) {
			if (_o1_ instanceof NpcFollowInfo) assign((NpcFollowInfo)_o1_);
			else if (_o1_ instanceof NpcFollowInfo.Data) assign((NpcFollowInfo.Data)_o1_);
			else if (_o1_ instanceof NpcFollowInfo.Const) assign(((NpcFollowInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(NpcFollowInfo _o_) {
			npcid = _o_.npcid;
			questid = _o_.questid;
			validdate = _o_.validdate;
		}

		private void assign(NpcFollowInfo.Data _o_) {
			npcid = _o_.npcid;
			questid = _o_.questid;
			validdate = _o_.validdate;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(npcid);
			_os_.marshal(questid);
			_os_.marshal(validdate);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			npcid = _os_.unmarshal_int();
			questid = _os_.unmarshal_int();
			validdate = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.NpcFollowInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.NpcFollowInfo toData() {
			return new Data(this);
		}

		public xbean.NpcFollowInfo toBean() {
			return new NpcFollowInfo(this, null, null);
		}

		@Override
		public xbean.NpcFollowInfo toDataIf() {
			return this;
		}

		public xbean.NpcFollowInfo toBeanIf() {
			return new NpcFollowInfo(this, null, null);
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
		public int getNpcid() { // npc跟随的id
			return npcid;
		}

		@Override
		public int getQuestid() { // 
			return questid;
		}

		@Override
		public long getValiddate() { // npc跟随的时限
			return validdate;
		}

		@Override
		public void setNpcid(int _v_) { // npc跟随的id
			npcid = _v_;
		}

		@Override
		public void setQuestid(int _v_) { // 
			questid = _v_;
		}

		@Override
		public void setValiddate(long _v_) { // npc跟随的时限
			validdate = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof NpcFollowInfo.Data)) return false;
			NpcFollowInfo.Data _o_ = (NpcFollowInfo.Data) _o1_;
			if (npcid != _o_.npcid) return false;
			if (questid != _o_.questid) return false;
			if (validdate != _o_.validdate) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += npcid;
			_h_ += questid;
			_h_ += validdate;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(npcid);
			_sb_.append(",");
			_sb_.append(questid);
			_sb_.append(",");
			_sb_.append(validdate);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
