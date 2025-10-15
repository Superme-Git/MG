
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Fighter extends mkdb.XBean implements xbean.Fighter {
	private long uniqueid; // ID：如果是角色，这里是roleId；如果是宠物或者怪物，这里是baseId
	private String fightername; // 名称
	private int subtype; // 子类型
	private int fightertype; // 战斗单位的类型
	private int battleindex; // 战斗者的战斗单位索引，即fighterId
	private int fighterkey; // 战斗者key,fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
	private boolean ishost; // 是否是战斗的主方
	private int round; // 战斗者进入战斗的回合数
	private int positionx; // 战斗者位置x，位置为阵营内的相对位置，一个阵营的坐标为5*4的位置矩阵
	private int positiony; // 战斗者位置y
	private int inilevel; // 战斗者的初始等级
	private int inihp; // 战斗者的初始hp
	private int inimp; // 战斗者的初始mp
	private int inisp; // 战斗者的初始怒气
	private int inipetkey; // 战斗者的初始宠物key
	private int footlogoid; // 足印效果id
	private int shapeid; // 模型id
	private java.util.HashMap<Integer, Integer> actioncount; // 记录一场战斗的法术物理的攻击和受击次数
	private java.util.HashMap<Integer, Integer> fightedpets; // 当此fighter为角色时，记录其战斗过的宠物，key为petkey,value是result,逃跑,击飞,召回
	private java.util.HashMap<Integer, Float> initattrs; // 记录角色进入战斗时的初始值
	private java.util.HashMap<Integer, Integer> initskills; // 记录角色进入战斗时的技能初始值
	private java.util.HashMap<Integer, Integer> useditems; // 记录在此场战斗中用过的物品

	@Override
	public void _reset_unsafe_() {
		uniqueid = 0L;
		fightername = "";
		subtype = 0;
		fightertype = 0;
		battleindex = 0;
		fighterkey = 0;
		ishost = false;
		round = 0;
		positionx = 0;
		positiony = 0;
		inilevel = 0;
		inihp = 0;
		inimp = 0;
		inisp = 0;
		inipetkey = -1;
		footlogoid = 0;
		shapeid = 0;
		actioncount.clear();
		fightedpets.clear();
		initattrs.clear();
		initskills.clear();
		useditems.clear();
	}

	Fighter(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		fightername = "";
		round = 0;
		inilevel = 0;
		inipetkey = -1;
		actioncount = new java.util.HashMap<Integer, Integer>();
		fightedpets = new java.util.HashMap<Integer, Integer>();
		initattrs = new java.util.HashMap<Integer, Float>();
		initskills = new java.util.HashMap<Integer, Integer>();
		useditems = new java.util.HashMap<Integer, Integer>();
	}

	public Fighter() {
		this(0, null, null);
	}

	public Fighter(Fighter _o_) {
		this(_o_, null, null);
	}

	Fighter(xbean.Fighter _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Fighter) assign((Fighter)_o1_);
		else if (_o1_ instanceof Fighter.Data) assign((Fighter.Data)_o1_);
		else if (_o1_ instanceof Fighter.Const) assign(((Fighter.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Fighter _o_) {
		_o_._xdb_verify_unsafe_();
		uniqueid = _o_.uniqueid;
		fightername = _o_.fightername;
		subtype = _o_.subtype;
		fightertype = _o_.fightertype;
		battleindex = _o_.battleindex;
		fighterkey = _o_.fighterkey;
		ishost = _o_.ishost;
		round = _o_.round;
		positionx = _o_.positionx;
		positiony = _o_.positiony;
		inilevel = _o_.inilevel;
		inihp = _o_.inihp;
		inimp = _o_.inimp;
		inisp = _o_.inisp;
		inipetkey = _o_.inipetkey;
		footlogoid = _o_.footlogoid;
		shapeid = _o_.shapeid;
		actioncount = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.actioncount.entrySet())
			actioncount.put(_e_.getKey(), _e_.getValue());
		fightedpets = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.fightedpets.entrySet())
			fightedpets.put(_e_.getKey(), _e_.getValue());
		initattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.initattrs.entrySet())
			initattrs.put(_e_.getKey(), _e_.getValue());
		initskills = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.initskills.entrySet())
			initskills.put(_e_.getKey(), _e_.getValue());
		useditems = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.useditems.entrySet())
			useditems.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(Fighter.Data _o_) {
		uniqueid = _o_.uniqueid;
		fightername = _o_.fightername;
		subtype = _o_.subtype;
		fightertype = _o_.fightertype;
		battleindex = _o_.battleindex;
		fighterkey = _o_.fighterkey;
		ishost = _o_.ishost;
		round = _o_.round;
		positionx = _o_.positionx;
		positiony = _o_.positiony;
		inilevel = _o_.inilevel;
		inihp = _o_.inihp;
		inimp = _o_.inimp;
		inisp = _o_.inisp;
		inipetkey = _o_.inipetkey;
		footlogoid = _o_.footlogoid;
		shapeid = _o_.shapeid;
		actioncount = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.actioncount.entrySet())
			actioncount.put(_e_.getKey(), _e_.getValue());
		fightedpets = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.fightedpets.entrySet())
			fightedpets.put(_e_.getKey(), _e_.getValue());
		initattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.initattrs.entrySet())
			initattrs.put(_e_.getKey(), _e_.getValue());
		initskills = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.initskills.entrySet())
			initskills.put(_e_.getKey(), _e_.getValue());
		useditems = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.useditems.entrySet())
			useditems.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(uniqueid);
		_os_.marshal(fightername, mkdb.Const.IO_CHARSET);
		_os_.marshal(subtype);
		_os_.marshal(fightertype);
		_os_.marshal(battleindex);
		_os_.marshal(fighterkey);
		_os_.marshal(ishost);
		_os_.marshal(round);
		_os_.marshal(positionx);
		_os_.marshal(positiony);
		_os_.marshal(inilevel);
		_os_.marshal(inihp);
		_os_.marshal(inimp);
		_os_.marshal(inisp);
		_os_.marshal(inipetkey);
		_os_.marshal(footlogoid);
		_os_.marshal(shapeid);
		_os_.compact_uint32(actioncount.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : actioncount.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(fightedpets.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : fightedpets.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(initattrs.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : initattrs.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(initskills.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : initskills.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(useditems.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : useditems.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		uniqueid = _os_.unmarshal_long();
		fightername = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		subtype = _os_.unmarshal_int();
		fightertype = _os_.unmarshal_int();
		battleindex = _os_.unmarshal_int();
		fighterkey = _os_.unmarshal_int();
		ishost = _os_.unmarshal_boolean();
		round = _os_.unmarshal_int();
		positionx = _os_.unmarshal_int();
		positiony = _os_.unmarshal_int();
		inilevel = _os_.unmarshal_int();
		inihp = _os_.unmarshal_int();
		inimp = _os_.unmarshal_int();
		inisp = _os_.unmarshal_int();
		inipetkey = _os_.unmarshal_int();
		footlogoid = _os_.unmarshal_int();
		shapeid = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				actioncount = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				actioncount.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				fightedpets = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				fightedpets.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				initattrs = new java.util.HashMap<Integer, Float>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				float _v_ = 0.0f;
				_v_ = _os_.unmarshal_float();
				initattrs.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				initskills = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				initskills.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				useditems = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				useditems.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.Fighter copy() {
		_xdb_verify_unsafe_();
		return new Fighter(this);
	}

	@Override
	public xbean.Fighter toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Fighter toBean() {
		_xdb_verify_unsafe_();
		return new Fighter(this); // same as copy()
	}

	@Override
	public xbean.Fighter toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Fighter toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getUniqueid() { // ID：如果是角色，这里是roleId；如果是宠物或者怪物，这里是baseId
		_xdb_verify_unsafe_();
		return uniqueid;
	}

	@Override
	public String getFightername() { // 名称
		_xdb_verify_unsafe_();
		return fightername;
	}

	@Override
	public com.locojoy.base.Octets getFighternameOctets() { // 名称
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getFightername(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getSubtype() { // 子类型
		_xdb_verify_unsafe_();
		return subtype;
	}

	@Override
	public int getFightertype() { // 战斗单位的类型
		_xdb_verify_unsafe_();
		return fightertype;
	}

	@Override
	public int getBattleindex() { // 战斗者的战斗单位索引，即fighterId
		_xdb_verify_unsafe_();
		return battleindex;
	}

	@Override
	public int getFighterkey() { // 战斗者key,fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
		_xdb_verify_unsafe_();
		return fighterkey;
	}

	@Override
	public boolean getIshost() { // 是否是战斗的主方
		_xdb_verify_unsafe_();
		return ishost;
	}

	@Override
	public int getRound() { // 战斗者进入战斗的回合数
		_xdb_verify_unsafe_();
		return round;
	}

	@Override
	public int getPositionx() { // 战斗者位置x，位置为阵营内的相对位置，一个阵营的坐标为5*4的位置矩阵
		_xdb_verify_unsafe_();
		return positionx;
	}

	@Override
	public int getPositiony() { // 战斗者位置y
		_xdb_verify_unsafe_();
		return positiony;
	}

	@Override
	public int getInilevel() { // 战斗者的初始等级
		_xdb_verify_unsafe_();
		return inilevel;
	}

	@Override
	public int getInihp() { // 战斗者的初始hp
		_xdb_verify_unsafe_();
		return inihp;
	}

	@Override
	public int getInimp() { // 战斗者的初始mp
		_xdb_verify_unsafe_();
		return inimp;
	}

	@Override
	public int getInisp() { // 战斗者的初始怒气
		_xdb_verify_unsafe_();
		return inisp;
	}

	@Override
	public int getInipetkey() { // 战斗者的初始宠物key
		_xdb_verify_unsafe_();
		return inipetkey;
	}

	@Override
	public int getFootlogoid() { // 足印效果id
		_xdb_verify_unsafe_();
		return footlogoid;
	}

	@Override
	public int getShapeid() { // 模型id
		_xdb_verify_unsafe_();
		return shapeid;
	}

	@Override
	public java.util.Map<Integer, Integer> getActioncount() { // 记录一场战斗的法术物理的攻击和受击次数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "actioncount"), actioncount);
	}

	@Override
	public java.util.Map<Integer, Integer> getActioncountAsData() { // 记录一场战斗的法术物理的攻击和受击次数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> actioncount;
		Fighter _o_ = this;
		actioncount = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.actioncount.entrySet())
			actioncount.put(_e_.getKey(), _e_.getValue());
		return actioncount;
	}

	@Override
	public java.util.Map<Integer, Integer> getFightedpets() { // 当此fighter为角色时，记录其战斗过的宠物，key为petkey,value是result,逃跑,击飞,召回
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "fightedpets"), fightedpets);
	}

	@Override
	public java.util.Map<Integer, Integer> getFightedpetsAsData() { // 当此fighter为角色时，记录其战斗过的宠物，key为petkey,value是result,逃跑,击飞,召回
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> fightedpets;
		Fighter _o_ = this;
		fightedpets = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.fightedpets.entrySet())
			fightedpets.put(_e_.getKey(), _e_.getValue());
		return fightedpets;
	}

	@Override
	public java.util.Map<Integer, Float> getInitattrs() { // 记录角色进入战斗时的初始值
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "initattrs"), initattrs);
	}

	@Override
	public java.util.Map<Integer, Float> getInitattrsAsData() { // 记录角色进入战斗时的初始值
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Float> initattrs;
		Fighter _o_ = this;
		initattrs = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.initattrs.entrySet())
			initattrs.put(_e_.getKey(), _e_.getValue());
		return initattrs;
	}

	@Override
	public java.util.Map<Integer, Integer> getInitskills() { // 记录角色进入战斗时的技能初始值
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "initskills"), initskills);
	}

	@Override
	public java.util.Map<Integer, Integer> getInitskillsAsData() { // 记录角色进入战斗时的技能初始值
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> initskills;
		Fighter _o_ = this;
		initskills = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.initskills.entrySet())
			initskills.put(_e_.getKey(), _e_.getValue());
		return initskills;
	}

	@Override
	public java.util.Map<Integer, Integer> getUseditems() { // 记录在此场战斗中用过的物品
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "useditems"), useditems);
	}

	@Override
	public java.util.Map<Integer, Integer> getUseditemsAsData() { // 记录在此场战斗中用过的物品
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> useditems;
		Fighter _o_ = this;
		useditems = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.useditems.entrySet())
			useditems.put(_e_.getKey(), _e_.getValue());
		return useditems;
	}

	@Override
	public void setUniqueid(long _v_) { // ID：如果是角色，这里是roleId；如果是宠物或者怪物，这里是baseId
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "uniqueid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, uniqueid) {
					public void rollback() { uniqueid = _xdb_saved; }
				};}});
		uniqueid = _v_;
	}

	@Override
	public void setFightername(String _v_) { // 名称
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fightername") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, fightername) {
					public void rollback() { fightername = _xdb_saved; }
				};}});
		fightername = _v_;
	}

	@Override
	public void setFighternameOctets(com.locojoy.base.Octets _v_) { // 名称
		_xdb_verify_unsafe_();
		this.setFightername(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setSubtype(int _v_) { // 子类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "subtype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, subtype) {
					public void rollback() { subtype = _xdb_saved; }
				};}});
		subtype = _v_;
	}

	@Override
	public void setFightertype(int _v_) { // 战斗单位的类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fightertype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, fightertype) {
					public void rollback() { fightertype = _xdb_saved; }
				};}});
		fightertype = _v_;
	}

	@Override
	public void setBattleindex(int _v_) { // 战斗者的战斗单位索引，即fighterId
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "battleindex") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, battleindex) {
					public void rollback() { battleindex = _xdb_saved; }
				};}});
		battleindex = _v_;
	}

	@Override
	public void setFighterkey(int _v_) { // 战斗者key,fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fighterkey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, fighterkey) {
					public void rollback() { fighterkey = _xdb_saved; }
				};}});
		fighterkey = _v_;
	}

	@Override
	public void setIshost(boolean _v_) { // 是否是战斗的主方
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "ishost") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, ishost) {
					public void rollback() { ishost = _xdb_saved; }
				};}});
		ishost = _v_;
	}

	@Override
	public void setRound(int _v_) { // 战斗者进入战斗的回合数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "round") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, round) {
					public void rollback() { round = _xdb_saved; }
				};}});
		round = _v_;
	}

	@Override
	public void setPositionx(int _v_) { // 战斗者位置x，位置为阵营内的相对位置，一个阵营的坐标为5*4的位置矩阵
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "positionx") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, positionx) {
					public void rollback() { positionx = _xdb_saved; }
				};}});
		positionx = _v_;
	}

	@Override
	public void setPositiony(int _v_) { // 战斗者位置y
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "positiony") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, positiony) {
					public void rollback() { positiony = _xdb_saved; }
				};}});
		positiony = _v_;
	}

	@Override
	public void setInilevel(int _v_) { // 战斗者的初始等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "inilevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, inilevel) {
					public void rollback() { inilevel = _xdb_saved; }
				};}});
		inilevel = _v_;
	}

	@Override
	public void setInihp(int _v_) { // 战斗者的初始hp
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "inihp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, inihp) {
					public void rollback() { inihp = _xdb_saved; }
				};}});
		inihp = _v_;
	}

	@Override
	public void setInimp(int _v_) { // 战斗者的初始mp
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "inimp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, inimp) {
					public void rollback() { inimp = _xdb_saved; }
				};}});
		inimp = _v_;
	}

	@Override
	public void setInisp(int _v_) { // 战斗者的初始怒气
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "inisp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, inisp) {
					public void rollback() { inisp = _xdb_saved; }
				};}});
		inisp = _v_;
	}

	@Override
	public void setInipetkey(int _v_) { // 战斗者的初始宠物key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "inipetkey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, inipetkey) {
					public void rollback() { inipetkey = _xdb_saved; }
				};}});
		inipetkey = _v_;
	}

	@Override
	public void setFootlogoid(int _v_) { // 足印效果id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "footlogoid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, footlogoid) {
					public void rollback() { footlogoid = _xdb_saved; }
				};}});
		footlogoid = _v_;
	}

	@Override
	public void setShapeid(int _v_) { // 模型id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "shapeid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, shapeid) {
					public void rollback() { shapeid = _xdb_saved; }
				};}});
		shapeid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Fighter _o_ = null;
		if ( _o1_ instanceof Fighter ) _o_ = (Fighter)_o1_;
		else if ( _o1_ instanceof Fighter.Const ) _o_ = ((Fighter.Const)_o1_).nThis();
		else return false;
		if (uniqueid != _o_.uniqueid) return false;
		if (!fightername.equals(_o_.fightername)) return false;
		if (subtype != _o_.subtype) return false;
		if (fightertype != _o_.fightertype) return false;
		if (battleindex != _o_.battleindex) return false;
		if (fighterkey != _o_.fighterkey) return false;
		if (ishost != _o_.ishost) return false;
		if (round != _o_.round) return false;
		if (positionx != _o_.positionx) return false;
		if (positiony != _o_.positiony) return false;
		if (inilevel != _o_.inilevel) return false;
		if (inihp != _o_.inihp) return false;
		if (inimp != _o_.inimp) return false;
		if (inisp != _o_.inisp) return false;
		if (inipetkey != _o_.inipetkey) return false;
		if (footlogoid != _o_.footlogoid) return false;
		if (shapeid != _o_.shapeid) return false;
		if (!actioncount.equals(_o_.actioncount)) return false;
		if (!fightedpets.equals(_o_.fightedpets)) return false;
		if (!initattrs.equals(_o_.initattrs)) return false;
		if (!initskills.equals(_o_.initskills)) return false;
		if (!useditems.equals(_o_.useditems)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += uniqueid;
		_h_ += fightername.hashCode();
		_h_ += subtype;
		_h_ += fightertype;
		_h_ += battleindex;
		_h_ += fighterkey;
		_h_ += ishost ? 1231 : 1237;
		_h_ += round;
		_h_ += positionx;
		_h_ += positiony;
		_h_ += inilevel;
		_h_ += inihp;
		_h_ += inimp;
		_h_ += inisp;
		_h_ += inipetkey;
		_h_ += footlogoid;
		_h_ += shapeid;
		_h_ += actioncount.hashCode();
		_h_ += fightedpets.hashCode();
		_h_ += initattrs.hashCode();
		_h_ += initskills.hashCode();
		_h_ += useditems.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(uniqueid);
		_sb_.append(",");
		_sb_.append("'").append(fightername).append("'");
		_sb_.append(",");
		_sb_.append(subtype);
		_sb_.append(",");
		_sb_.append(fightertype);
		_sb_.append(",");
		_sb_.append(battleindex);
		_sb_.append(",");
		_sb_.append(fighterkey);
		_sb_.append(",");
		_sb_.append(ishost);
		_sb_.append(",");
		_sb_.append(round);
		_sb_.append(",");
		_sb_.append(positionx);
		_sb_.append(",");
		_sb_.append(positiony);
		_sb_.append(",");
		_sb_.append(inilevel);
		_sb_.append(",");
		_sb_.append(inihp);
		_sb_.append(",");
		_sb_.append(inimp);
		_sb_.append(",");
		_sb_.append(inisp);
		_sb_.append(",");
		_sb_.append(inipetkey);
		_sb_.append(",");
		_sb_.append(footlogoid);
		_sb_.append(",");
		_sb_.append(shapeid);
		_sb_.append(",");
		_sb_.append(actioncount);
		_sb_.append(",");
		_sb_.append(fightedpets);
		_sb_.append(",");
		_sb_.append(initattrs);
		_sb_.append(",");
		_sb_.append(initskills);
		_sb_.append(",");
		_sb_.append(useditems);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("uniqueid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fightername"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("subtype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fightertype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battleindex"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fighterkey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ishost"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("round"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("positionx"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("positiony"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("inilevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("inihp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("inimp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("inisp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("inipetkey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("footlogoid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shapeid"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("actioncount"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("fightedpets"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("initattrs"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("initskills"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("useditems"));
		return lb;
	}

	private class Const implements xbean.Fighter {
		Fighter nThis() {
			return Fighter.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Fighter copy() {
			return Fighter.this.copy();
		}

		@Override
		public xbean.Fighter toData() {
			return Fighter.this.toData();
		}

		public xbean.Fighter toBean() {
			return Fighter.this.toBean();
		}

		@Override
		public xbean.Fighter toDataIf() {
			return Fighter.this.toDataIf();
		}

		public xbean.Fighter toBeanIf() {
			return Fighter.this.toBeanIf();
		}

		@Override
		public long getUniqueid() { // ID：如果是角色，这里是roleId；如果是宠物或者怪物，这里是baseId
			_xdb_verify_unsafe_();
			return uniqueid;
		}

		@Override
		public String getFightername() { // 名称
			_xdb_verify_unsafe_();
			return fightername;
		}

		@Override
		public com.locojoy.base.Octets getFighternameOctets() { // 名称
			_xdb_verify_unsafe_();
			return Fighter.this.getFighternameOctets();
		}

		@Override
		public int getSubtype() { // 子类型
			_xdb_verify_unsafe_();
			return subtype;
		}

		@Override
		public int getFightertype() { // 战斗单位的类型
			_xdb_verify_unsafe_();
			return fightertype;
		}

		@Override
		public int getBattleindex() { // 战斗者的战斗单位索引，即fighterId
			_xdb_verify_unsafe_();
			return battleindex;
		}

		@Override
		public int getFighterkey() { // 战斗者key,fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
			_xdb_verify_unsafe_();
			return fighterkey;
		}

		@Override
		public boolean getIshost() { // 是否是战斗的主方
			_xdb_verify_unsafe_();
			return ishost;
		}

		@Override
		public int getRound() { // 战斗者进入战斗的回合数
			_xdb_verify_unsafe_();
			return round;
		}

		@Override
		public int getPositionx() { // 战斗者位置x，位置为阵营内的相对位置，一个阵营的坐标为5*4的位置矩阵
			_xdb_verify_unsafe_();
			return positionx;
		}

		@Override
		public int getPositiony() { // 战斗者位置y
			_xdb_verify_unsafe_();
			return positiony;
		}

		@Override
		public int getInilevel() { // 战斗者的初始等级
			_xdb_verify_unsafe_();
			return inilevel;
		}

		@Override
		public int getInihp() { // 战斗者的初始hp
			_xdb_verify_unsafe_();
			return inihp;
		}

		@Override
		public int getInimp() { // 战斗者的初始mp
			_xdb_verify_unsafe_();
			return inimp;
		}

		@Override
		public int getInisp() { // 战斗者的初始怒气
			_xdb_verify_unsafe_();
			return inisp;
		}

		@Override
		public int getInipetkey() { // 战斗者的初始宠物key
			_xdb_verify_unsafe_();
			return inipetkey;
		}

		@Override
		public int getFootlogoid() { // 足印效果id
			_xdb_verify_unsafe_();
			return footlogoid;
		}

		@Override
		public int getShapeid() { // 模型id
			_xdb_verify_unsafe_();
			return shapeid;
		}

		@Override
		public java.util.Map<Integer, Integer> getActioncount() { // 记录一场战斗的法术物理的攻击和受击次数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(actioncount);
		}

		@Override
		public java.util.Map<Integer, Integer> getActioncountAsData() { // 记录一场战斗的法术物理的攻击和受击次数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> actioncount;
			Fighter _o_ = Fighter.this;
			actioncount = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.actioncount.entrySet())
				actioncount.put(_e_.getKey(), _e_.getValue());
			return actioncount;
		}

		@Override
		public java.util.Map<Integer, Integer> getFightedpets() { // 当此fighter为角色时，记录其战斗过的宠物，key为petkey,value是result,逃跑,击飞,召回
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(fightedpets);
		}

		@Override
		public java.util.Map<Integer, Integer> getFightedpetsAsData() { // 当此fighter为角色时，记录其战斗过的宠物，key为petkey,value是result,逃跑,击飞,召回
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> fightedpets;
			Fighter _o_ = Fighter.this;
			fightedpets = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.fightedpets.entrySet())
				fightedpets.put(_e_.getKey(), _e_.getValue());
			return fightedpets;
		}

		@Override
		public java.util.Map<Integer, Float> getInitattrs() { // 记录角色进入战斗时的初始值
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(initattrs);
		}

		@Override
		public java.util.Map<Integer, Float> getInitattrsAsData() { // 记录角色进入战斗时的初始值
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Float> initattrs;
			Fighter _o_ = Fighter.this;
			initattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.initattrs.entrySet())
				initattrs.put(_e_.getKey(), _e_.getValue());
			return initattrs;
		}

		@Override
		public java.util.Map<Integer, Integer> getInitskills() { // 记录角色进入战斗时的技能初始值
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(initskills);
		}

		@Override
		public java.util.Map<Integer, Integer> getInitskillsAsData() { // 记录角色进入战斗时的技能初始值
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> initskills;
			Fighter _o_ = Fighter.this;
			initskills = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.initskills.entrySet())
				initskills.put(_e_.getKey(), _e_.getValue());
			return initskills;
		}

		@Override
		public java.util.Map<Integer, Integer> getUseditems() { // 记录在此场战斗中用过的物品
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(useditems);
		}

		@Override
		public java.util.Map<Integer, Integer> getUseditemsAsData() { // 记录在此场战斗中用过的物品
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> useditems;
			Fighter _o_ = Fighter.this;
			useditems = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.useditems.entrySet())
				useditems.put(_e_.getKey(), _e_.getValue());
			return useditems;
		}

		@Override
		public void setUniqueid(long _v_) { // ID：如果是角色，这里是roleId；如果是宠物或者怪物，这里是baseId
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFightername(String _v_) { // 名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFighternameOctets(com.locojoy.base.Octets _v_) { // 名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSubtype(int _v_) { // 子类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFightertype(int _v_) { // 战斗单位的类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBattleindex(int _v_) { // 战斗者的战斗单位索引，即fighterId
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFighterkey(int _v_) { // 战斗者key,fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIshost(boolean _v_) { // 是否是战斗的主方
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRound(int _v_) { // 战斗者进入战斗的回合数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPositionx(int _v_) { // 战斗者位置x，位置为阵营内的相对位置，一个阵营的坐标为5*4的位置矩阵
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPositiony(int _v_) { // 战斗者位置y
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInilevel(int _v_) { // 战斗者的初始等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInihp(int _v_) { // 战斗者的初始hp
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInimp(int _v_) { // 战斗者的初始mp
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInisp(int _v_) { // 战斗者的初始怒气
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setInipetkey(int _v_) { // 战斗者的初始宠物key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFootlogoid(int _v_) { // 足印效果id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShapeid(int _v_) { // 模型id
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
			return Fighter.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Fighter.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Fighter.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Fighter.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Fighter.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Fighter.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Fighter.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Fighter.this.hashCode();
		}

		@Override
		public String toString() {
			return Fighter.this.toString();
		}

	}

	public static final class Data implements xbean.Fighter {
		private long uniqueid; // ID：如果是角色，这里是roleId；如果是宠物或者怪物，这里是baseId
		private String fightername; // 名称
		private int subtype; // 子类型
		private int fightertype; // 战斗单位的类型
		private int battleindex; // 战斗者的战斗单位索引，即fighterId
		private int fighterkey; // 战斗者key,fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
		private boolean ishost; // 是否是战斗的主方
		private int round; // 战斗者进入战斗的回合数
		private int positionx; // 战斗者位置x，位置为阵营内的相对位置，一个阵营的坐标为5*4的位置矩阵
		private int positiony; // 战斗者位置y
		private int inilevel; // 战斗者的初始等级
		private int inihp; // 战斗者的初始hp
		private int inimp; // 战斗者的初始mp
		private int inisp; // 战斗者的初始怒气
		private int inipetkey; // 战斗者的初始宠物key
		private int footlogoid; // 足印效果id
		private int shapeid; // 模型id
		private java.util.HashMap<Integer, Integer> actioncount; // 记录一场战斗的法术物理的攻击和受击次数
		private java.util.HashMap<Integer, Integer> fightedpets; // 当此fighter为角色时，记录其战斗过的宠物，key为petkey,value是result,逃跑,击飞,召回
		private java.util.HashMap<Integer, Float> initattrs; // 记录角色进入战斗时的初始值
		private java.util.HashMap<Integer, Integer> initskills; // 记录角色进入战斗时的技能初始值
		private java.util.HashMap<Integer, Integer> useditems; // 记录在此场战斗中用过的物品

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			fightername = "";
			round = 0;
			inilevel = 0;
			inipetkey = -1;
			actioncount = new java.util.HashMap<Integer, Integer>();
			fightedpets = new java.util.HashMap<Integer, Integer>();
			initattrs = new java.util.HashMap<Integer, Float>();
			initskills = new java.util.HashMap<Integer, Integer>();
			useditems = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.Fighter _o1_) {
			if (_o1_ instanceof Fighter) assign((Fighter)_o1_);
			else if (_o1_ instanceof Fighter.Data) assign((Fighter.Data)_o1_);
			else if (_o1_ instanceof Fighter.Const) assign(((Fighter.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Fighter _o_) {
			uniqueid = _o_.uniqueid;
			fightername = _o_.fightername;
			subtype = _o_.subtype;
			fightertype = _o_.fightertype;
			battleindex = _o_.battleindex;
			fighterkey = _o_.fighterkey;
			ishost = _o_.ishost;
			round = _o_.round;
			positionx = _o_.positionx;
			positiony = _o_.positiony;
			inilevel = _o_.inilevel;
			inihp = _o_.inihp;
			inimp = _o_.inimp;
			inisp = _o_.inisp;
			inipetkey = _o_.inipetkey;
			footlogoid = _o_.footlogoid;
			shapeid = _o_.shapeid;
			actioncount = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.actioncount.entrySet())
				actioncount.put(_e_.getKey(), _e_.getValue());
			fightedpets = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.fightedpets.entrySet())
				fightedpets.put(_e_.getKey(), _e_.getValue());
			initattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.initattrs.entrySet())
				initattrs.put(_e_.getKey(), _e_.getValue());
			initskills = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.initskills.entrySet())
				initskills.put(_e_.getKey(), _e_.getValue());
			useditems = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.useditems.entrySet())
				useditems.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(Fighter.Data _o_) {
			uniqueid = _o_.uniqueid;
			fightername = _o_.fightername;
			subtype = _o_.subtype;
			fightertype = _o_.fightertype;
			battleindex = _o_.battleindex;
			fighterkey = _o_.fighterkey;
			ishost = _o_.ishost;
			round = _o_.round;
			positionx = _o_.positionx;
			positiony = _o_.positiony;
			inilevel = _o_.inilevel;
			inihp = _o_.inihp;
			inimp = _o_.inimp;
			inisp = _o_.inisp;
			inipetkey = _o_.inipetkey;
			footlogoid = _o_.footlogoid;
			shapeid = _o_.shapeid;
			actioncount = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.actioncount.entrySet())
				actioncount.put(_e_.getKey(), _e_.getValue());
			fightedpets = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.fightedpets.entrySet())
				fightedpets.put(_e_.getKey(), _e_.getValue());
			initattrs = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.initattrs.entrySet())
				initattrs.put(_e_.getKey(), _e_.getValue());
			initskills = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.initskills.entrySet())
				initskills.put(_e_.getKey(), _e_.getValue());
			useditems = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.useditems.entrySet())
				useditems.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(uniqueid);
			_os_.marshal(fightername, mkdb.Const.IO_CHARSET);
			_os_.marshal(subtype);
			_os_.marshal(fightertype);
			_os_.marshal(battleindex);
			_os_.marshal(fighterkey);
			_os_.marshal(ishost);
			_os_.marshal(round);
			_os_.marshal(positionx);
			_os_.marshal(positiony);
			_os_.marshal(inilevel);
			_os_.marshal(inihp);
			_os_.marshal(inimp);
			_os_.marshal(inisp);
			_os_.marshal(inipetkey);
			_os_.marshal(footlogoid);
			_os_.marshal(shapeid);
			_os_.compact_uint32(actioncount.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : actioncount.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(fightedpets.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : fightedpets.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(initattrs.size());
			for (java.util.Map.Entry<Integer, Float> _e_ : initattrs.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(initskills.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : initskills.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(useditems.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : useditems.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			uniqueid = _os_.unmarshal_long();
			fightername = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			subtype = _os_.unmarshal_int();
			fightertype = _os_.unmarshal_int();
			battleindex = _os_.unmarshal_int();
			fighterkey = _os_.unmarshal_int();
			ishost = _os_.unmarshal_boolean();
			round = _os_.unmarshal_int();
			positionx = _os_.unmarshal_int();
			positiony = _os_.unmarshal_int();
			inilevel = _os_.unmarshal_int();
			inihp = _os_.unmarshal_int();
			inimp = _os_.unmarshal_int();
			inisp = _os_.unmarshal_int();
			inipetkey = _os_.unmarshal_int();
			footlogoid = _os_.unmarshal_int();
			shapeid = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					actioncount = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					actioncount.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					fightedpets = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					fightedpets.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					initattrs = new java.util.HashMap<Integer, Float>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					float _v_ = 0.0f;
					_v_ = _os_.unmarshal_float();
					initattrs.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					initskills = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					initskills.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					useditems = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					useditems.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.Fighter copy() {
			return new Data(this);
		}

		@Override
		public xbean.Fighter toData() {
			return new Data(this);
		}

		public xbean.Fighter toBean() {
			return new Fighter(this, null, null);
		}

		@Override
		public xbean.Fighter toDataIf() {
			return this;
		}

		public xbean.Fighter toBeanIf() {
			return new Fighter(this, null, null);
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
		public long getUniqueid() { // ID：如果是角色，这里是roleId；如果是宠物或者怪物，这里是baseId
			return uniqueid;
		}

		@Override
		public String getFightername() { // 名称
			return fightername;
		}

		@Override
		public com.locojoy.base.Octets getFighternameOctets() { // 名称
			return com.locojoy.base.Octets.wrap(getFightername(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getSubtype() { // 子类型
			return subtype;
		}

		@Override
		public int getFightertype() { // 战斗单位的类型
			return fightertype;
		}

		@Override
		public int getBattleindex() { // 战斗者的战斗单位索引，即fighterId
			return battleindex;
		}

		@Override
		public int getFighterkey() { // 战斗者key,fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
			return fighterkey;
		}

		@Override
		public boolean getIshost() { // 是否是战斗的主方
			return ishost;
		}

		@Override
		public int getRound() { // 战斗者进入战斗的回合数
			return round;
		}

		@Override
		public int getPositionx() { // 战斗者位置x，位置为阵营内的相对位置，一个阵营的坐标为5*4的位置矩阵
			return positionx;
		}

		@Override
		public int getPositiony() { // 战斗者位置y
			return positiony;
		}

		@Override
		public int getInilevel() { // 战斗者的初始等级
			return inilevel;
		}

		@Override
		public int getInihp() { // 战斗者的初始hp
			return inihp;
		}

		@Override
		public int getInimp() { // 战斗者的初始mp
			return inimp;
		}

		@Override
		public int getInisp() { // 战斗者的初始怒气
			return inisp;
		}

		@Override
		public int getInipetkey() { // 战斗者的初始宠物key
			return inipetkey;
		}

		@Override
		public int getFootlogoid() { // 足印效果id
			return footlogoid;
		}

		@Override
		public int getShapeid() { // 模型id
			return shapeid;
		}

		@Override
		public java.util.Map<Integer, Integer> getActioncount() { // 记录一场战斗的法术物理的攻击和受击次数
			return actioncount;
		}

		@Override
		public java.util.Map<Integer, Integer> getActioncountAsData() { // 记录一场战斗的法术物理的攻击和受击次数
			return actioncount;
		}

		@Override
		public java.util.Map<Integer, Integer> getFightedpets() { // 当此fighter为角色时，记录其战斗过的宠物，key为petkey,value是result,逃跑,击飞,召回
			return fightedpets;
		}

		@Override
		public java.util.Map<Integer, Integer> getFightedpetsAsData() { // 当此fighter为角色时，记录其战斗过的宠物，key为petkey,value是result,逃跑,击飞,召回
			return fightedpets;
		}

		@Override
		public java.util.Map<Integer, Float> getInitattrs() { // 记录角色进入战斗时的初始值
			return initattrs;
		}

		@Override
		public java.util.Map<Integer, Float> getInitattrsAsData() { // 记录角色进入战斗时的初始值
			return initattrs;
		}

		@Override
		public java.util.Map<Integer, Integer> getInitskills() { // 记录角色进入战斗时的技能初始值
			return initskills;
		}

		@Override
		public java.util.Map<Integer, Integer> getInitskillsAsData() { // 记录角色进入战斗时的技能初始值
			return initskills;
		}

		@Override
		public java.util.Map<Integer, Integer> getUseditems() { // 记录在此场战斗中用过的物品
			return useditems;
		}

		@Override
		public java.util.Map<Integer, Integer> getUseditemsAsData() { // 记录在此场战斗中用过的物品
			return useditems;
		}

		@Override
		public void setUniqueid(long _v_) { // ID：如果是角色，这里是roleId；如果是宠物或者怪物，这里是baseId
			uniqueid = _v_;
		}

		@Override
		public void setFightername(String _v_) { // 名称
			if (null == _v_)
				throw new NullPointerException();
			fightername = _v_;
		}

		@Override
		public void setFighternameOctets(com.locojoy.base.Octets _v_) { // 名称
			this.setFightername(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setSubtype(int _v_) { // 子类型
			subtype = _v_;
		}

		@Override
		public void setFightertype(int _v_) { // 战斗单位的类型
			fightertype = _v_;
		}

		@Override
		public void setBattleindex(int _v_) { // 战斗者的战斗单位索引，即fighterId
			battleindex = _v_;
		}

		@Override
		public void setFighterkey(int _v_) { // 战斗者key,fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
			fighterkey = _v_;
		}

		@Override
		public void setIshost(boolean _v_) { // 是否是战斗的主方
			ishost = _v_;
		}

		@Override
		public void setRound(int _v_) { // 战斗者进入战斗的回合数
			round = _v_;
		}

		@Override
		public void setPositionx(int _v_) { // 战斗者位置x，位置为阵营内的相对位置，一个阵营的坐标为5*4的位置矩阵
			positionx = _v_;
		}

		@Override
		public void setPositiony(int _v_) { // 战斗者位置y
			positiony = _v_;
		}

		@Override
		public void setInilevel(int _v_) { // 战斗者的初始等级
			inilevel = _v_;
		}

		@Override
		public void setInihp(int _v_) { // 战斗者的初始hp
			inihp = _v_;
		}

		@Override
		public void setInimp(int _v_) { // 战斗者的初始mp
			inimp = _v_;
		}

		@Override
		public void setInisp(int _v_) { // 战斗者的初始怒气
			inisp = _v_;
		}

		@Override
		public void setInipetkey(int _v_) { // 战斗者的初始宠物key
			inipetkey = _v_;
		}

		@Override
		public void setFootlogoid(int _v_) { // 足印效果id
			footlogoid = _v_;
		}

		@Override
		public void setShapeid(int _v_) { // 模型id
			shapeid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Fighter.Data)) return false;
			Fighter.Data _o_ = (Fighter.Data) _o1_;
			if (uniqueid != _o_.uniqueid) return false;
			if (!fightername.equals(_o_.fightername)) return false;
			if (subtype != _o_.subtype) return false;
			if (fightertype != _o_.fightertype) return false;
			if (battleindex != _o_.battleindex) return false;
			if (fighterkey != _o_.fighterkey) return false;
			if (ishost != _o_.ishost) return false;
			if (round != _o_.round) return false;
			if (positionx != _o_.positionx) return false;
			if (positiony != _o_.positiony) return false;
			if (inilevel != _o_.inilevel) return false;
			if (inihp != _o_.inihp) return false;
			if (inimp != _o_.inimp) return false;
			if (inisp != _o_.inisp) return false;
			if (inipetkey != _o_.inipetkey) return false;
			if (footlogoid != _o_.footlogoid) return false;
			if (shapeid != _o_.shapeid) return false;
			if (!actioncount.equals(_o_.actioncount)) return false;
			if (!fightedpets.equals(_o_.fightedpets)) return false;
			if (!initattrs.equals(_o_.initattrs)) return false;
			if (!initskills.equals(_o_.initskills)) return false;
			if (!useditems.equals(_o_.useditems)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += uniqueid;
			_h_ += fightername.hashCode();
			_h_ += subtype;
			_h_ += fightertype;
			_h_ += battleindex;
			_h_ += fighterkey;
			_h_ += ishost ? 1231 : 1237;
			_h_ += round;
			_h_ += positionx;
			_h_ += positiony;
			_h_ += inilevel;
			_h_ += inihp;
			_h_ += inimp;
			_h_ += inisp;
			_h_ += inipetkey;
			_h_ += footlogoid;
			_h_ += shapeid;
			_h_ += actioncount.hashCode();
			_h_ += fightedpets.hashCode();
			_h_ += initattrs.hashCode();
			_h_ += initskills.hashCode();
			_h_ += useditems.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(uniqueid);
			_sb_.append(",");
			_sb_.append("'").append(fightername).append("'");
			_sb_.append(",");
			_sb_.append(subtype);
			_sb_.append(",");
			_sb_.append(fightertype);
			_sb_.append(",");
			_sb_.append(battleindex);
			_sb_.append(",");
			_sb_.append(fighterkey);
			_sb_.append(",");
			_sb_.append(ishost);
			_sb_.append(",");
			_sb_.append(round);
			_sb_.append(",");
			_sb_.append(positionx);
			_sb_.append(",");
			_sb_.append(positiony);
			_sb_.append(",");
			_sb_.append(inilevel);
			_sb_.append(",");
			_sb_.append(inihp);
			_sb_.append(",");
			_sb_.append(inimp);
			_sb_.append(",");
			_sb_.append(inisp);
			_sb_.append(",");
			_sb_.append(inipetkey);
			_sb_.append(",");
			_sb_.append(footlogoid);
			_sb_.append(",");
			_sb_.append(shapeid);
			_sb_.append(",");
			_sb_.append(actioncount);
			_sb_.append(",");
			_sb_.append(fightedpets);
			_sb_.append(",");
			_sb_.append(initattrs);
			_sb_.append(",");
			_sb_.append(initskills);
			_sb_.append(",");
			_sb_.append(useditems);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
