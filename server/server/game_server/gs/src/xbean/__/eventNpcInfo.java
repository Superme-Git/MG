
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class eventNpcInfo extends mkdb.XBean implements xbean.eventNpcInfo {
	private int npcid; // //npcid 补充怪物时需要用到
	private int npcstatus; // //npckey 对应的战斗状态 0=空闲 1=战斗中 2=已经消失
	private int battletime; // //战斗中的人数
	private int battleendtime; // //战斗结束并胜利的人数
	private long roleid; // //正在战斗的roleid
	private long createtime; // //npc创建时间

	@Override
	public void _reset_unsafe_() {
		npcid = 0;
		npcstatus = 0;
		battletime = 0;
		battleendtime = 0;
		roleid = 0L;
		createtime = 0L;
	}

	eventNpcInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
	}

	public eventNpcInfo() {
		this(0, null, null);
	}

	public eventNpcInfo(eventNpcInfo _o_) {
		this(_o_, null, null);
	}

	eventNpcInfo(xbean.eventNpcInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof eventNpcInfo) assign((eventNpcInfo)_o1_);
		else if (_o1_ instanceof eventNpcInfo.Data) assign((eventNpcInfo.Data)_o1_);
		else if (_o1_ instanceof eventNpcInfo.Const) assign(((eventNpcInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(eventNpcInfo _o_) {
		_o_._xdb_verify_unsafe_();
		npcid = _o_.npcid;
		npcstatus = _o_.npcstatus;
		battletime = _o_.battletime;
		battleendtime = _o_.battleendtime;
		roleid = _o_.roleid;
		createtime = _o_.createtime;
	}

	private void assign(eventNpcInfo.Data _o_) {
		npcid = _o_.npcid;
		npcstatus = _o_.npcstatus;
		battletime = _o_.battletime;
		battleendtime = _o_.battleendtime;
		roleid = _o_.roleid;
		createtime = _o_.createtime;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(npcid);
		_os_.marshal(npcstatus);
		_os_.marshal(battletime);
		_os_.marshal(battleendtime);
		_os_.marshal(roleid);
		_os_.marshal(createtime);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		npcid = _os_.unmarshal_int();
		npcstatus = _os_.unmarshal_int();
		battletime = _os_.unmarshal_int();
		battleendtime = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		createtime = _os_.unmarshal_long();
		return _os_;
	}

	@Override
	public xbean.eventNpcInfo copy() {
		_xdb_verify_unsafe_();
		return new eventNpcInfo(this);
	}

	@Override
	public xbean.eventNpcInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.eventNpcInfo toBean() {
		_xdb_verify_unsafe_();
		return new eventNpcInfo(this); // same as copy()
	}

	@Override
	public xbean.eventNpcInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.eventNpcInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getNpcid() { // //npcid 补充怪物时需要用到
		_xdb_verify_unsafe_();
		return npcid;
	}

	@Override
	public int getNpcstatus() { // //npckey 对应的战斗状态 0=空闲 1=战斗中 2=已经消失
		_xdb_verify_unsafe_();
		return npcstatus;
	}

	@Override
	public int getBattletime() { // //战斗中的人数
		_xdb_verify_unsafe_();
		return battletime;
	}

	@Override
	public int getBattleendtime() { // //战斗结束并胜利的人数
		_xdb_verify_unsafe_();
		return battleendtime;
	}

	@Override
	public long getRoleid() { // //正在战斗的roleid
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public long getCreatetime() { // //npc创建时间
		_xdb_verify_unsafe_();
		return createtime;
	}

	@Override
	public void setNpcid(int _v_) { // //npcid 补充怪物时需要用到
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "npcid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, npcid) {
					public void rollback() { npcid = _xdb_saved; }
				};}});
		npcid = _v_;
	}

	@Override
	public void setNpcstatus(int _v_) { // //npckey 对应的战斗状态 0=空闲 1=战斗中 2=已经消失
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "npcstatus") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, npcstatus) {
					public void rollback() { npcstatus = _xdb_saved; }
				};}});
		npcstatus = _v_;
	}

	@Override
	public void setBattletime(int _v_) { // //战斗中的人数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "battletime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, battletime) {
					public void rollback() { battletime = _xdb_saved; }
				};}});
		battletime = _v_;
	}

	@Override
	public void setBattleendtime(int _v_) { // //战斗结束并胜利的人数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "battleendtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, battleendtime) {
					public void rollback() { battleendtime = _xdb_saved; }
				};}});
		battleendtime = _v_;
	}

	@Override
	public void setRoleid(long _v_) { // //正在战斗的roleid
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setCreatetime(long _v_) { // //npc创建时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "createtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, createtime) {
					public void rollback() { createtime = _xdb_saved; }
				};}});
		createtime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		eventNpcInfo _o_ = null;
		if ( _o1_ instanceof eventNpcInfo ) _o_ = (eventNpcInfo)_o1_;
		else if ( _o1_ instanceof eventNpcInfo.Const ) _o_ = ((eventNpcInfo.Const)_o1_).nThis();
		else return false;
		if (npcid != _o_.npcid) return false;
		if (npcstatus != _o_.npcstatus) return false;
		if (battletime != _o_.battletime) return false;
		if (battleendtime != _o_.battleendtime) return false;
		if (roleid != _o_.roleid) return false;
		if (createtime != _o_.createtime) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += npcid;
		_h_ += npcstatus;
		_h_ += battletime;
		_h_ += battleendtime;
		_h_ += roleid;
		_h_ += createtime;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcid);
		_sb_.append(",");
		_sb_.append(npcstatus);
		_sb_.append(",");
		_sb_.append(battletime);
		_sb_.append(",");
		_sb_.append(battleendtime);
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(createtime);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("npcid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("npcstatus"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battletime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battleendtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("createtime"));
		return lb;
	}

	private class Const implements xbean.eventNpcInfo {
		eventNpcInfo nThis() {
			return eventNpcInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.eventNpcInfo copy() {
			return eventNpcInfo.this.copy();
		}

		@Override
		public xbean.eventNpcInfo toData() {
			return eventNpcInfo.this.toData();
		}

		public xbean.eventNpcInfo toBean() {
			return eventNpcInfo.this.toBean();
		}

		@Override
		public xbean.eventNpcInfo toDataIf() {
			return eventNpcInfo.this.toDataIf();
		}

		public xbean.eventNpcInfo toBeanIf() {
			return eventNpcInfo.this.toBeanIf();
		}

		@Override
		public int getNpcid() { // //npcid 补充怪物时需要用到
			_xdb_verify_unsafe_();
			return npcid;
		}

		@Override
		public int getNpcstatus() { // //npckey 对应的战斗状态 0=空闲 1=战斗中 2=已经消失
			_xdb_verify_unsafe_();
			return npcstatus;
		}

		@Override
		public int getBattletime() { // //战斗中的人数
			_xdb_verify_unsafe_();
			return battletime;
		}

		@Override
		public int getBattleendtime() { // //战斗结束并胜利的人数
			_xdb_verify_unsafe_();
			return battleendtime;
		}

		@Override
		public long getRoleid() { // //正在战斗的roleid
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public long getCreatetime() { // //npc创建时间
			_xdb_verify_unsafe_();
			return createtime;
		}

		@Override
		public void setNpcid(int _v_) { // //npcid 补充怪物时需要用到
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNpcstatus(int _v_) { // //npckey 对应的战斗状态 0=空闲 1=战斗中 2=已经消失
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBattletime(int _v_) { // //战斗中的人数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBattleendtime(int _v_) { // //战斗结束并胜利的人数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // //正在战斗的roleid
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCreatetime(long _v_) { // //npc创建时间
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
			return eventNpcInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return eventNpcInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return eventNpcInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return eventNpcInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return eventNpcInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return eventNpcInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return eventNpcInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return eventNpcInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return eventNpcInfo.this.toString();
		}

	}

	public static final class Data implements xbean.eventNpcInfo {
		private int npcid; // //npcid 补充怪物时需要用到
		private int npcstatus; // //npckey 对应的战斗状态 0=空闲 1=战斗中 2=已经消失
		private int battletime; // //战斗中的人数
		private int battleendtime; // //战斗结束并胜利的人数
		private long roleid; // //正在战斗的roleid
		private long createtime; // //npc创建时间

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
		}

		Data(xbean.eventNpcInfo _o1_) {
			if (_o1_ instanceof eventNpcInfo) assign((eventNpcInfo)_o1_);
			else if (_o1_ instanceof eventNpcInfo.Data) assign((eventNpcInfo.Data)_o1_);
			else if (_o1_ instanceof eventNpcInfo.Const) assign(((eventNpcInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(eventNpcInfo _o_) {
			npcid = _o_.npcid;
			npcstatus = _o_.npcstatus;
			battletime = _o_.battletime;
			battleendtime = _o_.battleendtime;
			roleid = _o_.roleid;
			createtime = _o_.createtime;
		}

		private void assign(eventNpcInfo.Data _o_) {
			npcid = _o_.npcid;
			npcstatus = _o_.npcstatus;
			battletime = _o_.battletime;
			battleendtime = _o_.battleendtime;
			roleid = _o_.roleid;
			createtime = _o_.createtime;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(npcid);
			_os_.marshal(npcstatus);
			_os_.marshal(battletime);
			_os_.marshal(battleendtime);
			_os_.marshal(roleid);
			_os_.marshal(createtime);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			npcid = _os_.unmarshal_int();
			npcstatus = _os_.unmarshal_int();
			battletime = _os_.unmarshal_int();
			battleendtime = _os_.unmarshal_int();
			roleid = _os_.unmarshal_long();
			createtime = _os_.unmarshal_long();
			return _os_;
		}

		@Override
		public xbean.eventNpcInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.eventNpcInfo toData() {
			return new Data(this);
		}

		public xbean.eventNpcInfo toBean() {
			return new eventNpcInfo(this, null, null);
		}

		@Override
		public xbean.eventNpcInfo toDataIf() {
			return this;
		}

		public xbean.eventNpcInfo toBeanIf() {
			return new eventNpcInfo(this, null, null);
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
		public int getNpcid() { // //npcid 补充怪物时需要用到
			return npcid;
		}

		@Override
		public int getNpcstatus() { // //npckey 对应的战斗状态 0=空闲 1=战斗中 2=已经消失
			return npcstatus;
		}

		@Override
		public int getBattletime() { // //战斗中的人数
			return battletime;
		}

		@Override
		public int getBattleendtime() { // //战斗结束并胜利的人数
			return battleendtime;
		}

		@Override
		public long getRoleid() { // //正在战斗的roleid
			return roleid;
		}

		@Override
		public long getCreatetime() { // //npc创建时间
			return createtime;
		}

		@Override
		public void setNpcid(int _v_) { // //npcid 补充怪物时需要用到
			npcid = _v_;
		}

		@Override
		public void setNpcstatus(int _v_) { // //npckey 对应的战斗状态 0=空闲 1=战斗中 2=已经消失
			npcstatus = _v_;
		}

		@Override
		public void setBattletime(int _v_) { // //战斗中的人数
			battletime = _v_;
		}

		@Override
		public void setBattleendtime(int _v_) { // //战斗结束并胜利的人数
			battleendtime = _v_;
		}

		@Override
		public void setRoleid(long _v_) { // //正在战斗的roleid
			roleid = _v_;
		}

		@Override
		public void setCreatetime(long _v_) { // //npc创建时间
			createtime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof eventNpcInfo.Data)) return false;
			eventNpcInfo.Data _o_ = (eventNpcInfo.Data) _o1_;
			if (npcid != _o_.npcid) return false;
			if (npcstatus != _o_.npcstatus) return false;
			if (battletime != _o_.battletime) return false;
			if (battleendtime != _o_.battleendtime) return false;
			if (roleid != _o_.roleid) return false;
			if (createtime != _o_.createtime) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += npcid;
			_h_ += npcstatus;
			_h_ += battletime;
			_h_ += battleendtime;
			_h_ += roleid;
			_h_ += createtime;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(npcid);
			_sb_.append(",");
			_sb_.append(npcstatus);
			_sb_.append(",");
			_sb_.append(battletime);
			_sb_.append(",");
			_sb_.append(battleendtime);
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(createtime);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
