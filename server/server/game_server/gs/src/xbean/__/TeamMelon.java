
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class TeamMelon extends mkdb.XBean implements xbean.TeamMelon {
	private int itemid; // 道具表id by changhao
	private int itemnum; // 道具数量 by changha
	private xbean.Item itemdata; // 道具数据 by changhao
	private int awardid; // 奖励表id
	private java.util.HashMap<Long, Integer> melonroleids; // 分赃人员(roll点决定) by changhao
	private java.util.HashMap<Long, Integer> opmelonroleids; // 已经操作的分赃人员 by changhao
	private int maxrollpoint; // 最大ROLL点 by changhao
	private int opnum; // 已经ROLL点的人员数量 by changhao
	private long battleid; // 战斗ID by changhao
	private int battleresult; // 战斗结果 by changhao

	@Override
	public void _reset_unsafe_() {
		itemid = 0;
		itemnum = 0;
		itemdata._reset_unsafe_();
		awardid = 0;
		melonroleids.clear();
		opmelonroleids.clear();
		maxrollpoint = 0;
		opnum = 0;
		battleid = 0L;
		battleresult = 0;
	}

	TeamMelon(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		itemdata = new Item(0, this, "itemdata");
		melonroleids = new java.util.HashMap<Long, Integer>();
		opmelonroleids = new java.util.HashMap<Long, Integer>();
	}

	public TeamMelon() {
		this(0, null, null);
	}

	public TeamMelon(TeamMelon _o_) {
		this(_o_, null, null);
	}

	TeamMelon(xbean.TeamMelon _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof TeamMelon) assign((TeamMelon)_o1_);
		else if (_o1_ instanceof TeamMelon.Data) assign((TeamMelon.Data)_o1_);
		else if (_o1_ instanceof TeamMelon.Const) assign(((TeamMelon.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(TeamMelon _o_) {
		_o_._xdb_verify_unsafe_();
		itemid = _o_.itemid;
		itemnum = _o_.itemnum;
		itemdata = new Item(_o_.itemdata, this, "itemdata");
		awardid = _o_.awardid;
		melonroleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.melonroleids.entrySet())
			melonroleids.put(_e_.getKey(), _e_.getValue());
		opmelonroleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.opmelonroleids.entrySet())
			opmelonroleids.put(_e_.getKey(), _e_.getValue());
		maxrollpoint = _o_.maxrollpoint;
		opnum = _o_.opnum;
		battleid = _o_.battleid;
		battleresult = _o_.battleresult;
	}

	private void assign(TeamMelon.Data _o_) {
		itemid = _o_.itemid;
		itemnum = _o_.itemnum;
		itemdata = new Item(_o_.itemdata, this, "itemdata");
		awardid = _o_.awardid;
		melonroleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.melonroleids.entrySet())
			melonroleids.put(_e_.getKey(), _e_.getValue());
		opmelonroleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.opmelonroleids.entrySet())
			opmelonroleids.put(_e_.getKey(), _e_.getValue());
		maxrollpoint = _o_.maxrollpoint;
		opnum = _o_.opnum;
		battleid = _o_.battleid;
		battleresult = _o_.battleresult;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(itemid);
		_os_.marshal(itemnum);
		itemdata.marshal(_os_);
		_os_.marshal(awardid);
		_os_.compact_uint32(melonroleids.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : melonroleids.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(opmelonroleids.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : opmelonroleids.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(maxrollpoint);
		_os_.marshal(opnum);
		_os_.marshal(battleid);
		_os_.marshal(battleresult);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		itemid = _os_.unmarshal_int();
		itemnum = _os_.unmarshal_int();
		itemdata.unmarshal(_os_);
		awardid = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				melonroleids = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				melonroleids.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				opmelonroleids = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				opmelonroleids.put(_k_, _v_);
			}
		}
		maxrollpoint = _os_.unmarshal_int();
		opnum = _os_.unmarshal_int();
		battleid = _os_.unmarshal_long();
		battleresult = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.TeamMelon copy() {
		_xdb_verify_unsafe_();
		return new TeamMelon(this);
	}

	@Override
	public xbean.TeamMelon toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TeamMelon toBean() {
		_xdb_verify_unsafe_();
		return new TeamMelon(this); // same as copy()
	}

	@Override
	public xbean.TeamMelon toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.TeamMelon toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getItemid() { // 道具表id by changhao
		_xdb_verify_unsafe_();
		return itemid;
	}

	@Override
	public int getItemnum() { // 道具数量 by changha
		_xdb_verify_unsafe_();
		return itemnum;
	}

	@Override
	public xbean.Item getItemdata() { // 道具数据 by changhao
		_xdb_verify_unsafe_();
		return itemdata;
	}

	@Override
	public int getAwardid() { // 奖励表id
		_xdb_verify_unsafe_();
		return awardid;
	}

	@Override
	public java.util.Map<Long, Integer> getMelonroleids() { // 分赃人员(roll点决定) by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "melonroleids"), melonroleids);
	}

	@Override
	public java.util.Map<Long, Integer> getMelonroleidsAsData() { // 分赃人员(roll点决定) by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> melonroleids;
		TeamMelon _o_ = this;
		melonroleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.melonroleids.entrySet())
			melonroleids.put(_e_.getKey(), _e_.getValue());
		return melonroleids;
	}

	@Override
	public java.util.Map<Long, Integer> getOpmelonroleids() { // 已经操作的分赃人员 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "opmelonroleids"), opmelonroleids);
	}

	@Override
	public java.util.Map<Long, Integer> getOpmelonroleidsAsData() { // 已经操作的分赃人员 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> opmelonroleids;
		TeamMelon _o_ = this;
		opmelonroleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.opmelonroleids.entrySet())
			opmelonroleids.put(_e_.getKey(), _e_.getValue());
		return opmelonroleids;
	}

	@Override
	public int getMaxrollpoint() { // 最大ROLL点 by changhao
		_xdb_verify_unsafe_();
		return maxrollpoint;
	}

	@Override
	public int getOpnum() { // 已经ROLL点的人员数量 by changhao
		_xdb_verify_unsafe_();
		return opnum;
	}

	@Override
	public long getBattleid() { // 战斗ID by changhao
		_xdb_verify_unsafe_();
		return battleid;
	}

	@Override
	public int getBattleresult() { // 战斗结果 by changhao
		_xdb_verify_unsafe_();
		return battleresult;
	}

	@Override
	public void setItemid(int _v_) { // 道具表id by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemid) {
					public void rollback() { itemid = _xdb_saved; }
				};}});
		itemid = _v_;
	}

	@Override
	public void setItemnum(int _v_) { // 道具数量 by changha
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemnum) {
					public void rollback() { itemnum = _xdb_saved; }
				};}});
		itemnum = _v_;
	}

	@Override
	public void setAwardid(int _v_) { // 奖励表id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "awardid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, awardid) {
					public void rollback() { awardid = _xdb_saved; }
				};}});
		awardid = _v_;
	}

	@Override
	public void setMaxrollpoint(int _v_) { // 最大ROLL点 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "maxrollpoint") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, maxrollpoint) {
					public void rollback() { maxrollpoint = _xdb_saved; }
				};}});
		maxrollpoint = _v_;
	}

	@Override
	public void setOpnum(int _v_) { // 已经ROLL点的人员数量 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "opnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, opnum) {
					public void rollback() { opnum = _xdb_saved; }
				};}});
		opnum = _v_;
	}

	@Override
	public void setBattleid(long _v_) { // 战斗ID by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "battleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, battleid) {
					public void rollback() { battleid = _xdb_saved; }
				};}});
		battleid = _v_;
	}

	@Override
	public void setBattleresult(int _v_) { // 战斗结果 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "battleresult") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, battleresult) {
					public void rollback() { battleresult = _xdb_saved; }
				};}});
		battleresult = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		TeamMelon _o_ = null;
		if ( _o1_ instanceof TeamMelon ) _o_ = (TeamMelon)_o1_;
		else if ( _o1_ instanceof TeamMelon.Const ) _o_ = ((TeamMelon.Const)_o1_).nThis();
		else return false;
		if (itemid != _o_.itemid) return false;
		if (itemnum != _o_.itemnum) return false;
		if (!itemdata.equals(_o_.itemdata)) return false;
		if (awardid != _o_.awardid) return false;
		if (!melonroleids.equals(_o_.melonroleids)) return false;
		if (!opmelonroleids.equals(_o_.opmelonroleids)) return false;
		if (maxrollpoint != _o_.maxrollpoint) return false;
		if (opnum != _o_.opnum) return false;
		if (battleid != _o_.battleid) return false;
		if (battleresult != _o_.battleresult) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += itemid;
		_h_ += itemnum;
		_h_ += itemdata.hashCode();
		_h_ += awardid;
		_h_ += melonroleids.hashCode();
		_h_ += opmelonroleids.hashCode();
		_h_ += maxrollpoint;
		_h_ += opnum;
		_h_ += battleid;
		_h_ += battleresult;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemid);
		_sb_.append(",");
		_sb_.append(itemnum);
		_sb_.append(",");
		_sb_.append(itemdata);
		_sb_.append(",");
		_sb_.append(awardid);
		_sb_.append(",");
		_sb_.append(melonroleids);
		_sb_.append(",");
		_sb_.append(opmelonroleids);
		_sb_.append(",");
		_sb_.append(maxrollpoint);
		_sb_.append(",");
		_sb_.append(opnum);
		_sb_.append(",");
		_sb_.append(battleid);
		_sb_.append(",");
		_sb_.append(battleresult);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemdata"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("awardid"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("melonroleids"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("opmelonroleids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("maxrollpoint"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("opnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battleresult"));
		return lb;
	}

	private class Const implements xbean.TeamMelon {
		TeamMelon nThis() {
			return TeamMelon.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.TeamMelon copy() {
			return TeamMelon.this.copy();
		}

		@Override
		public xbean.TeamMelon toData() {
			return TeamMelon.this.toData();
		}

		public xbean.TeamMelon toBean() {
			return TeamMelon.this.toBean();
		}

		@Override
		public xbean.TeamMelon toDataIf() {
			return TeamMelon.this.toDataIf();
		}

		public xbean.TeamMelon toBeanIf() {
			return TeamMelon.this.toBeanIf();
		}

		@Override
		public int getItemid() { // 道具表id by changhao
			_xdb_verify_unsafe_();
			return itemid;
		}

		@Override
		public int getItemnum() { // 道具数量 by changha
			_xdb_verify_unsafe_();
			return itemnum;
		}

		@Override
		public xbean.Item getItemdata() { // 道具数据 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(itemdata);
		}

		@Override
		public int getAwardid() { // 奖励表id
			_xdb_verify_unsafe_();
			return awardid;
		}

		@Override
		public java.util.Map<Long, Integer> getMelonroleids() { // 分赃人员(roll点决定) by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(melonroleids);
		}

		@Override
		public java.util.Map<Long, Integer> getMelonroleidsAsData() { // 分赃人员(roll点决定) by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> melonroleids;
			TeamMelon _o_ = TeamMelon.this;
			melonroleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.melonroleids.entrySet())
				melonroleids.put(_e_.getKey(), _e_.getValue());
			return melonroleids;
		}

		@Override
		public java.util.Map<Long, Integer> getOpmelonroleids() { // 已经操作的分赃人员 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(opmelonroleids);
		}

		@Override
		public java.util.Map<Long, Integer> getOpmelonroleidsAsData() { // 已经操作的分赃人员 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> opmelonroleids;
			TeamMelon _o_ = TeamMelon.this;
			opmelonroleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.opmelonroleids.entrySet())
				opmelonroleids.put(_e_.getKey(), _e_.getValue());
			return opmelonroleids;
		}

		@Override
		public int getMaxrollpoint() { // 最大ROLL点 by changhao
			_xdb_verify_unsafe_();
			return maxrollpoint;
		}

		@Override
		public int getOpnum() { // 已经ROLL点的人员数量 by changhao
			_xdb_verify_unsafe_();
			return opnum;
		}

		@Override
		public long getBattleid() { // 战斗ID by changhao
			_xdb_verify_unsafe_();
			return battleid;
		}

		@Override
		public int getBattleresult() { // 战斗结果 by changhao
			_xdb_verify_unsafe_();
			return battleresult;
		}

		@Override
		public void setItemid(int _v_) { // 道具表id by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setItemnum(int _v_) { // 道具数量 by changha
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAwardid(int _v_) { // 奖励表id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMaxrollpoint(int _v_) { // 最大ROLL点 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOpnum(int _v_) { // 已经ROLL点的人员数量 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBattleid(long _v_) { // 战斗ID by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBattleresult(int _v_) { // 战斗结果 by changhao
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
			return TeamMelon.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return TeamMelon.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return TeamMelon.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return TeamMelon.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return TeamMelon.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return TeamMelon.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return TeamMelon.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return TeamMelon.this.hashCode();
		}

		@Override
		public String toString() {
			return TeamMelon.this.toString();
		}

	}

	public static final class Data implements xbean.TeamMelon {
		private int itemid; // 道具表id by changhao
		private int itemnum; // 道具数量 by changha
		private xbean.Item itemdata; // 道具数据 by changhao
		private int awardid; // 奖励表id
		private java.util.HashMap<Long, Integer> melonroleids; // 分赃人员(roll点决定) by changhao
		private java.util.HashMap<Long, Integer> opmelonroleids; // 已经操作的分赃人员 by changhao
		private int maxrollpoint; // 最大ROLL点 by changhao
		private int opnum; // 已经ROLL点的人员数量 by changhao
		private long battleid; // 战斗ID by changhao
		private int battleresult; // 战斗结果 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			itemdata = new Item.Data();
			melonroleids = new java.util.HashMap<Long, Integer>();
			opmelonroleids = new java.util.HashMap<Long, Integer>();
		}

		Data(xbean.TeamMelon _o1_) {
			if (_o1_ instanceof TeamMelon) assign((TeamMelon)_o1_);
			else if (_o1_ instanceof TeamMelon.Data) assign((TeamMelon.Data)_o1_);
			else if (_o1_ instanceof TeamMelon.Const) assign(((TeamMelon.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(TeamMelon _o_) {
			itemid = _o_.itemid;
			itemnum = _o_.itemnum;
			itemdata = new Item.Data(_o_.itemdata);
			awardid = _o_.awardid;
			melonroleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.melonroleids.entrySet())
				melonroleids.put(_e_.getKey(), _e_.getValue());
			opmelonroleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.opmelonroleids.entrySet())
				opmelonroleids.put(_e_.getKey(), _e_.getValue());
			maxrollpoint = _o_.maxrollpoint;
			opnum = _o_.opnum;
			battleid = _o_.battleid;
			battleresult = _o_.battleresult;
		}

		private void assign(TeamMelon.Data _o_) {
			itemid = _o_.itemid;
			itemnum = _o_.itemnum;
			itemdata = new Item.Data(_o_.itemdata);
			awardid = _o_.awardid;
			melonroleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.melonroleids.entrySet())
				melonroleids.put(_e_.getKey(), _e_.getValue());
			opmelonroleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.opmelonroleids.entrySet())
				opmelonroleids.put(_e_.getKey(), _e_.getValue());
			maxrollpoint = _o_.maxrollpoint;
			opnum = _o_.opnum;
			battleid = _o_.battleid;
			battleresult = _o_.battleresult;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(itemid);
			_os_.marshal(itemnum);
			itemdata.marshal(_os_);
			_os_.marshal(awardid);
			_os_.compact_uint32(melonroleids.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : melonroleids.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(opmelonroleids.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : opmelonroleids.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(maxrollpoint);
			_os_.marshal(opnum);
			_os_.marshal(battleid);
			_os_.marshal(battleresult);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			itemid = _os_.unmarshal_int();
			itemnum = _os_.unmarshal_int();
			itemdata.unmarshal(_os_);
			awardid = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					melonroleids = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					melonroleids.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					opmelonroleids = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					opmelonroleids.put(_k_, _v_);
				}
			}
			maxrollpoint = _os_.unmarshal_int();
			opnum = _os_.unmarshal_int();
			battleid = _os_.unmarshal_long();
			battleresult = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.TeamMelon copy() {
			return new Data(this);
		}

		@Override
		public xbean.TeamMelon toData() {
			return new Data(this);
		}

		public xbean.TeamMelon toBean() {
			return new TeamMelon(this, null, null);
		}

		@Override
		public xbean.TeamMelon toDataIf() {
			return this;
		}

		public xbean.TeamMelon toBeanIf() {
			return new TeamMelon(this, null, null);
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
		public int getItemid() { // 道具表id by changhao
			return itemid;
		}

		@Override
		public int getItemnum() { // 道具数量 by changha
			return itemnum;
		}

		@Override
		public xbean.Item getItemdata() { // 道具数据 by changhao
			return itemdata;
		}

		@Override
		public int getAwardid() { // 奖励表id
			return awardid;
		}

		@Override
		public java.util.Map<Long, Integer> getMelonroleids() { // 分赃人员(roll点决定) by changhao
			return melonroleids;
		}

		@Override
		public java.util.Map<Long, Integer> getMelonroleidsAsData() { // 分赃人员(roll点决定) by changhao
			return melonroleids;
		}

		@Override
		public java.util.Map<Long, Integer> getOpmelonroleids() { // 已经操作的分赃人员 by changhao
			return opmelonroleids;
		}

		@Override
		public java.util.Map<Long, Integer> getOpmelonroleidsAsData() { // 已经操作的分赃人员 by changhao
			return opmelonroleids;
		}

		@Override
		public int getMaxrollpoint() { // 最大ROLL点 by changhao
			return maxrollpoint;
		}

		@Override
		public int getOpnum() { // 已经ROLL点的人员数量 by changhao
			return opnum;
		}

		@Override
		public long getBattleid() { // 战斗ID by changhao
			return battleid;
		}

		@Override
		public int getBattleresult() { // 战斗结果 by changhao
			return battleresult;
		}

		@Override
		public void setItemid(int _v_) { // 道具表id by changhao
			itemid = _v_;
		}

		@Override
		public void setItemnum(int _v_) { // 道具数量 by changha
			itemnum = _v_;
		}

		@Override
		public void setAwardid(int _v_) { // 奖励表id
			awardid = _v_;
		}

		@Override
		public void setMaxrollpoint(int _v_) { // 最大ROLL点 by changhao
			maxrollpoint = _v_;
		}

		@Override
		public void setOpnum(int _v_) { // 已经ROLL点的人员数量 by changhao
			opnum = _v_;
		}

		@Override
		public void setBattleid(long _v_) { // 战斗ID by changhao
			battleid = _v_;
		}

		@Override
		public void setBattleresult(int _v_) { // 战斗结果 by changhao
			battleresult = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof TeamMelon.Data)) return false;
			TeamMelon.Data _o_ = (TeamMelon.Data) _o1_;
			if (itemid != _o_.itemid) return false;
			if (itemnum != _o_.itemnum) return false;
			if (!itemdata.equals(_o_.itemdata)) return false;
			if (awardid != _o_.awardid) return false;
			if (!melonroleids.equals(_o_.melonroleids)) return false;
			if (!opmelonroleids.equals(_o_.opmelonroleids)) return false;
			if (maxrollpoint != _o_.maxrollpoint) return false;
			if (opnum != _o_.opnum) return false;
			if (battleid != _o_.battleid) return false;
			if (battleresult != _o_.battleresult) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += itemid;
			_h_ += itemnum;
			_h_ += itemdata.hashCode();
			_h_ += awardid;
			_h_ += melonroleids.hashCode();
			_h_ += opmelonroleids.hashCode();
			_h_ += maxrollpoint;
			_h_ += opnum;
			_h_ += battleid;
			_h_ += battleresult;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(itemid);
			_sb_.append(",");
			_sb_.append(itemnum);
			_sb_.append(",");
			_sb_.append(itemdata);
			_sb_.append(",");
			_sb_.append(awardid);
			_sb_.append(",");
			_sb_.append(melonroleids);
			_sb_.append(",");
			_sb_.append(opmelonroleids);
			_sb_.append(",");
			_sb_.append(maxrollpoint);
			_sb_.append(",");
			_sb_.append(opnum);
			_sb_.append(",");
			_sb_.append(battleid);
			_sb_.append(",");
			_sb_.append(battleresult);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
