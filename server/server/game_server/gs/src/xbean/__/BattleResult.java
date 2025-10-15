
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BattleResult extends mkdb.XBean implements xbean.BattleResult {
	private long battleid; // 战斗ID
	private int configid; // 战斗配置ID，默认为0时不是通过战斗配置new的ID
	private int environment; // 战场环境
	private int phase; // 战斗的阶段
	private int battletype; // 战斗类型
	private int areatype; // 练功区类型
	private int areaconf; // 练功区配置值
	private int hostform; // 主方光环
	private int guestform; // 客方光环
	private int monstertype; // 怪物类型
	private java.util.HashMap<Long, Integer> roleids; // 所有参战的角色ID : 角色的战斗索引
	private java.util.TreeMap<Integer, xbean.Fighter> fighters; // 所有的参战者 战斗索引 ： 战斗者 按索引排序
	private mkdb.util.SetX<Integer> skipeddeadfighters; // 本回合因为死亡而未出手的战斗者，出手结束清除
	private java.util.HashMap<Integer, xbean.Monster> monsters; // 所有在该场战斗中生成的怪物/NPC，key为fighterId
	private java.util.LinkedList<xbean.FailedMonster> failedmonsters; // 失败的怪物（包括己方客方）
	private java.util.TreeMap<Integer, xbean.Fighter> inifighters; // 把参战者的状态在战斗前保存一份,切磋pk会用到
	private java.util.HashMap<Long, Integer> audience; // 观战的roleid battleindex
	private java.util.TreeMap<Integer, xbean.Fighter> watchers; // 
	private long monsterid; // 明雷怪id
	private int round; // 战斗当前的回合数
	private long roundtime; // PRoundStart后是本回合的开始时间;PPlayScript后是播放开始的时间
	private long scriptplaytime; // 脚本播放时间（ms），根据脚本计算出来的播放时间，是脚本播放的最短时间，最短1秒
	private java.util.HashMap<Integer, xbean.Decision> decisions; // 战斗单元决策结果
	private mkdb.util.SetX<Long> feedbacks; // roleid				角色的反馈记录
	private int battleresult; // 战斗是否结束及战斗结果，参考BattleField.BATTLE_XXX,默认为2（BATTLE_NOT_END）
	private java.util.HashMap<Integer, Integer> roundresult; // 战斗单位：战斗结果
	private java.util.HashMap<Integer, xbean.Protectors> protectormap; // 被保护人：保护者列表
	private java.util.HashMap<Integer, Float> battledatas; // 保存战斗相关的一些数据

	@Override
	public void _reset_unsafe_() {
		battleid = 0L;
		configid = 0;
		environment = 0;
		phase = 0;
		battletype = 0;
		areatype = 0;
		areaconf = 0;
		hostform = 0;
		guestform = 0;
		monstertype = 0;
		roleids.clear();
		fighters.clear();
		skipeddeadfighters.clear();
		monsters.clear();
		failedmonsters.clear();
		inifighters.clear();
		audience.clear();
		watchers.clear();
		monsterid = -1;
		round = 1;
		roundtime = 0L;
		scriptplaytime = 1000;
		decisions.clear();
		feedbacks.clear();
		battleresult = 2;
		roundresult.clear();
		protectormap.clear();
		battledatas.clear();
	}

	BattleResult(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		configid = 0;
		roleids = new java.util.HashMap<Long, Integer>();
		fighters = new java.util.TreeMap<Integer, xbean.Fighter>();
		skipeddeadfighters = new mkdb.util.SetX<Integer>();
		monsters = new java.util.HashMap<Integer, xbean.Monster>();
		failedmonsters = new java.util.LinkedList<xbean.FailedMonster>();
		inifighters = new java.util.TreeMap<Integer, xbean.Fighter>();
		audience = new java.util.HashMap<Long, Integer>();
		watchers = new java.util.TreeMap<Integer, xbean.Fighter>();
		monsterid = -1;
		round = 1;
		scriptplaytime = 1000;
		decisions = new java.util.HashMap<Integer, xbean.Decision>();
		feedbacks = new mkdb.util.SetX<Long>();
		battleresult = 2;
		roundresult = new java.util.HashMap<Integer, Integer>();
		protectormap = new java.util.HashMap<Integer, xbean.Protectors>();
		battledatas = new java.util.HashMap<Integer, Float>();
	}

	public BattleResult() {
		this(0, null, null);
	}

	public BattleResult(BattleResult _o_) {
		this(_o_, null, null);
	}

	BattleResult(xbean.BattleResult _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof BattleResult) assign((BattleResult)_o1_);
		else if (_o1_ instanceof BattleResult.Data) assign((BattleResult.Data)_o1_);
		else if (_o1_ instanceof BattleResult.Const) assign(((BattleResult.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(BattleResult _o_) {
		_o_._xdb_verify_unsafe_();
		battleid = _o_.battleid;
		configid = _o_.configid;
		environment = _o_.environment;
		phase = _o_.phase;
		battletype = _o_.battletype;
		areatype = _o_.areatype;
		areaconf = _o_.areaconf;
		hostform = _o_.hostform;
		guestform = _o_.guestform;
		monstertype = _o_.monstertype;
		roleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.roleids.entrySet())
			roleids.put(_e_.getKey(), _e_.getValue());
		fighters = new java.util.TreeMap<Integer, xbean.Fighter>();
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.fighters.entrySet())
			fighters.put(_e_.getKey(), new Fighter(_e_.getValue(), this, "fighters"));
		skipeddeadfighters = new mkdb.util.SetX<Integer>();
		skipeddeadfighters.addAll(_o_.skipeddeadfighters);
		monsters = new java.util.HashMap<Integer, xbean.Monster>();
		for (java.util.Map.Entry<Integer, xbean.Monster> _e_ : _o_.monsters.entrySet())
			monsters.put(_e_.getKey(), new Monster(_e_.getValue(), this, "monsters"));
		failedmonsters = new java.util.LinkedList<xbean.FailedMonster>();
		for (xbean.FailedMonster _v_ : _o_.failedmonsters)
			failedmonsters.add(new FailedMonster(_v_, this, "failedmonsters"));
		inifighters = new java.util.TreeMap<Integer, xbean.Fighter>();
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.inifighters.entrySet())
			inifighters.put(_e_.getKey(), new Fighter(_e_.getValue(), this, "inifighters"));
		audience = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.audience.entrySet())
			audience.put(_e_.getKey(), _e_.getValue());
		watchers = new java.util.TreeMap<Integer, xbean.Fighter>();
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.watchers.entrySet())
			watchers.put(_e_.getKey(), new Fighter(_e_.getValue(), this, "watchers"));
		monsterid = _o_.monsterid;
		round = _o_.round;
		roundtime = _o_.roundtime;
		scriptplaytime = _o_.scriptplaytime;
		decisions = new java.util.HashMap<Integer, xbean.Decision>();
		for (java.util.Map.Entry<Integer, xbean.Decision> _e_ : _o_.decisions.entrySet())
			decisions.put(_e_.getKey(), new Decision(_e_.getValue(), this, "decisions"));
		feedbacks = new mkdb.util.SetX<Long>();
		feedbacks.addAll(_o_.feedbacks);
		battleresult = _o_.battleresult;
		roundresult = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.roundresult.entrySet())
			roundresult.put(_e_.getKey(), _e_.getValue());
		protectormap = new java.util.HashMap<Integer, xbean.Protectors>();
		for (java.util.Map.Entry<Integer, xbean.Protectors> _e_ : _o_.protectormap.entrySet())
			protectormap.put(_e_.getKey(), new Protectors(_e_.getValue(), this, "protectormap"));
		battledatas = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.battledatas.entrySet())
			battledatas.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(BattleResult.Data _o_) {
		battleid = _o_.battleid;
		configid = _o_.configid;
		environment = _o_.environment;
		phase = _o_.phase;
		battletype = _o_.battletype;
		areatype = _o_.areatype;
		areaconf = _o_.areaconf;
		hostform = _o_.hostform;
		guestform = _o_.guestform;
		monstertype = _o_.monstertype;
		roleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.roleids.entrySet())
			roleids.put(_e_.getKey(), _e_.getValue());
		fighters = new java.util.TreeMap<Integer, xbean.Fighter>();
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.fighters.entrySet())
			fighters.put(_e_.getKey(), new Fighter(_e_.getValue(), this, "fighters"));
		skipeddeadfighters = new mkdb.util.SetX<Integer>();
		skipeddeadfighters.addAll(_o_.skipeddeadfighters);
		monsters = new java.util.HashMap<Integer, xbean.Monster>();
		for (java.util.Map.Entry<Integer, xbean.Monster> _e_ : _o_.monsters.entrySet())
			monsters.put(_e_.getKey(), new Monster(_e_.getValue(), this, "monsters"));
		failedmonsters = new java.util.LinkedList<xbean.FailedMonster>();
		for (xbean.FailedMonster _v_ : _o_.failedmonsters)
			failedmonsters.add(new FailedMonster(_v_, this, "failedmonsters"));
		inifighters = new java.util.TreeMap<Integer, xbean.Fighter>();
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.inifighters.entrySet())
			inifighters.put(_e_.getKey(), new Fighter(_e_.getValue(), this, "inifighters"));
		audience = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.audience.entrySet())
			audience.put(_e_.getKey(), _e_.getValue());
		watchers = new java.util.TreeMap<Integer, xbean.Fighter>();
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.watchers.entrySet())
			watchers.put(_e_.getKey(), new Fighter(_e_.getValue(), this, "watchers"));
		monsterid = _o_.monsterid;
		round = _o_.round;
		roundtime = _o_.roundtime;
		scriptplaytime = _o_.scriptplaytime;
		decisions = new java.util.HashMap<Integer, xbean.Decision>();
		for (java.util.Map.Entry<Integer, xbean.Decision> _e_ : _o_.decisions.entrySet())
			decisions.put(_e_.getKey(), new Decision(_e_.getValue(), this, "decisions"));
		feedbacks = new mkdb.util.SetX<Long>();
		feedbacks.addAll(_o_.feedbacks);
		battleresult = _o_.battleresult;
		roundresult = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.roundresult.entrySet())
			roundresult.put(_e_.getKey(), _e_.getValue());
		protectormap = new java.util.HashMap<Integer, xbean.Protectors>();
		for (java.util.Map.Entry<Integer, xbean.Protectors> _e_ : _o_.protectormap.entrySet())
			protectormap.put(_e_.getKey(), new Protectors(_e_.getValue(), this, "protectormap"));
		battledatas = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.battledatas.entrySet())
			battledatas.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(battleid);
		_os_.marshal(configid);
		_os_.marshal(environment);
		_os_.marshal(phase);
		_os_.marshal(battletype);
		_os_.marshal(areatype);
		_os_.marshal(areaconf);
		_os_.marshal(hostform);
		_os_.marshal(guestform);
		_os_.marshal(monstertype);
		_os_.compact_uint32(roleids.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : roleids.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(fighters.size());
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : fighters.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(skipeddeadfighters.size());
		for (Integer _v_ : skipeddeadfighters) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(monsters.size());
		for (java.util.Map.Entry<Integer, xbean.Monster> _e_ : monsters.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(failedmonsters.size());
		for (xbean.FailedMonster _v_ : failedmonsters) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(inifighters.size());
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : inifighters.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(audience.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : audience.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(watchers.size());
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : watchers.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(monsterid);
		_os_.marshal(round);
		_os_.marshal(roundtime);
		_os_.marshal(scriptplaytime);
		_os_.compact_uint32(decisions.size());
		for (java.util.Map.Entry<Integer, xbean.Decision> _e_ : decisions.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(feedbacks.size());
		for (Long _v_ : feedbacks) {
			_os_.marshal(_v_);
		}
		_os_.marshal(battleresult);
		_os_.compact_uint32(roundresult.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : roundresult.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(protectormap.size());
		for (java.util.Map.Entry<Integer, xbean.Protectors> _e_ : protectormap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(battledatas.size());
		for (java.util.Map.Entry<Integer, Float> _e_ : battledatas.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		battleid = _os_.unmarshal_long();
		configid = _os_.unmarshal_int();
		environment = _os_.unmarshal_int();
		phase = _os_.unmarshal_int();
		battletype = _os_.unmarshal_int();
		areatype = _os_.unmarshal_int();
		areaconf = _os_.unmarshal_int();
		hostform = _os_.unmarshal_int();
		guestform = _os_.unmarshal_int();
		monstertype = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				roleids = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				roleids.put(_k_, _v_);
			}
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size)
		{
			int _k_ = 0;
			_k_ = _os_.unmarshal_int();
			xbean.Fighter _v_ = new Fighter(0, this, "fighters");
			_v_.unmarshal(_os_);
			fighters.put(_k_, _v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			skipeddeadfighters.add(_v_);
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				monsters = new java.util.HashMap<Integer, xbean.Monster>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.Monster _v_ = new Monster(0, this, "monsters");
				_v_.unmarshal(_os_);
				monsters.put(_k_, _v_);
			}
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.FailedMonster _v_ = new FailedMonster(0, this, "failedmonsters");
			_v_.unmarshal(_os_);
			failedmonsters.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size)
		{
			int _k_ = 0;
			_k_ = _os_.unmarshal_int();
			xbean.Fighter _v_ = new Fighter(0, this, "inifighters");
			_v_.unmarshal(_os_);
			inifighters.put(_k_, _v_);
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				audience = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				audience.put(_k_, _v_);
			}
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size)
		{
			int _k_ = 0;
			_k_ = _os_.unmarshal_int();
			xbean.Fighter _v_ = new Fighter(0, this, "watchers");
			_v_.unmarshal(_os_);
			watchers.put(_k_, _v_);
		}
		monsterid = _os_.unmarshal_long();
		round = _os_.unmarshal_int();
		roundtime = _os_.unmarshal_long();
		scriptplaytime = _os_.unmarshal_long();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				decisions = new java.util.HashMap<Integer, xbean.Decision>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.Decision _v_ = new Decision(0, this, "decisions");
				_v_.unmarshal(_os_);
				decisions.put(_k_, _v_);
			}
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			feedbacks.add(_v_);
		}
		battleresult = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				roundresult = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				roundresult.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				protectormap = new java.util.HashMap<Integer, xbean.Protectors>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.Protectors _v_ = new Protectors(0, this, "protectormap");
				_v_.unmarshal(_os_);
				protectormap.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				battledatas = new java.util.HashMap<Integer, Float>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				float _v_ = 0.0f;
				_v_ = _os_.unmarshal_float();
				battledatas.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.BattleResult copy() {
		_xdb_verify_unsafe_();
		return new BattleResult(this);
	}

	@Override
	public xbean.BattleResult toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BattleResult toBean() {
		_xdb_verify_unsafe_();
		return new BattleResult(this); // same as copy()
	}

	@Override
	public xbean.BattleResult toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BattleResult toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getBattleid() { // 战斗ID
		_xdb_verify_unsafe_();
		return battleid;
	}

	@Override
	public int getConfigid() { // 战斗配置ID，默认为0时不是通过战斗配置new的ID
		_xdb_verify_unsafe_();
		return configid;
	}

	@Override
	public int getEnvironment() { // 战场环境
		_xdb_verify_unsafe_();
		return environment;
	}

	@Override
	public int getPhase() { // 战斗的阶段
		_xdb_verify_unsafe_();
		return phase;
	}

	@Override
	public int getBattletype() { // 战斗类型
		_xdb_verify_unsafe_();
		return battletype;
	}

	@Override
	public int getAreatype() { // 练功区类型
		_xdb_verify_unsafe_();
		return areatype;
	}

	@Override
	public int getAreaconf() { // 练功区配置值
		_xdb_verify_unsafe_();
		return areaconf;
	}

	@Override
	public int getHostform() { // 主方光环
		_xdb_verify_unsafe_();
		return hostform;
	}

	@Override
	public int getGuestform() { // 客方光环
		_xdb_verify_unsafe_();
		return guestform;
	}

	@Override
	public int getMonstertype() { // 怪物类型
		_xdb_verify_unsafe_();
		return monstertype;
	}

	@Override
	public java.util.Map<Long, Integer> getRoleids() { // 所有参战的角色ID : 角色的战斗索引
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "roleids"), roleids);
	}

	@Override
	public java.util.Map<Long, Integer> getRoleidsAsData() { // 所有参战的角色ID : 角色的战斗索引
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> roleids;
		BattleResult _o_ = this;
		roleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.roleids.entrySet())
			roleids.put(_e_.getKey(), _e_.getValue());
		return roleids;
	}

	@Override
	public java.util.NavigableMap<Integer, xbean.Fighter> getFighters() { // 所有的参战者 战斗索引 ： 战斗者 按索引排序
		_xdb_verify_unsafe_();
		return mkdb.Logs.logNavigableMap(new mkdb.LogKey(this, "fighters"), fighters);
	}

	public java.util.NavigableMap<Integer, xbean.Fighter> getFightersAsData() { // 所有的参战者 战斗索引 ： 战斗者 按索引排序
		_xdb_verify_unsafe_();
		java.util.NavigableMap<Integer, xbean.Fighter> fighters;
		BattleResult _o_ = this;
		fighters = new java.util.TreeMap<Integer, xbean.Fighter>();
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.fighters.entrySet())
			fighters.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
		return fighters;
	}

	@Override
	public java.util.Set<Integer> getSkipeddeadfighters() { // 本回合因为死亡而未出手的战斗者，出手结束清除
		_xdb_verify_unsafe_();
		return mkdb.Logs.logSet(new mkdb.LogKey(this, "skipeddeadfighters"), skipeddeadfighters);
	}

	public java.util.Set<Integer> getSkipeddeadfightersAsData() { // 本回合因为死亡而未出手的战斗者，出手结束清除
		_xdb_verify_unsafe_();
		java.util.Set<Integer> skipeddeadfighters;
		BattleResult _o_ = this;
		skipeddeadfighters = new mkdb.util.SetX<Integer>();
		skipeddeadfighters.addAll(_o_.skipeddeadfighters);
		return skipeddeadfighters;
	}

	@Override
	public java.util.Map<Integer, xbean.Monster> getMonsters() { // 所有在该场战斗中生成的怪物/NPC，key为fighterId
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "monsters"), monsters);
	}

	@Override
	public java.util.Map<Integer, xbean.Monster> getMonstersAsData() { // 所有在该场战斗中生成的怪物/NPC，key为fighterId
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.Monster> monsters;
		BattleResult _o_ = this;
		monsters = new java.util.HashMap<Integer, xbean.Monster>();
		for (java.util.Map.Entry<Integer, xbean.Monster> _e_ : _o_.monsters.entrySet())
			monsters.put(_e_.getKey(), new Monster.Data(_e_.getValue()));
		return monsters;
	}

	@Override
	public java.util.List<xbean.FailedMonster> getFailedmonsters() { // 失败的怪物（包括己方客方）
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "failedmonsters"), failedmonsters);
	}

	public java.util.List<xbean.FailedMonster> getFailedmonstersAsData() { // 失败的怪物（包括己方客方）
		_xdb_verify_unsafe_();
		java.util.List<xbean.FailedMonster> failedmonsters;
		BattleResult _o_ = this;
		failedmonsters = new java.util.LinkedList<xbean.FailedMonster>();
		for (xbean.FailedMonster _v_ : _o_.failedmonsters)
			failedmonsters.add(new FailedMonster.Data(_v_));
		return failedmonsters;
	}

	@Override
	public java.util.NavigableMap<Integer, xbean.Fighter> getInifighters() { // 把参战者的状态在战斗前保存一份,切磋pk会用到
		_xdb_verify_unsafe_();
		return mkdb.Logs.logNavigableMap(new mkdb.LogKey(this, "inifighters"), inifighters);
	}

	public java.util.NavigableMap<Integer, xbean.Fighter> getInifightersAsData() { // 把参战者的状态在战斗前保存一份,切磋pk会用到
		_xdb_verify_unsafe_();
		java.util.NavigableMap<Integer, xbean.Fighter> inifighters;
		BattleResult _o_ = this;
		inifighters = new java.util.TreeMap<Integer, xbean.Fighter>();
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.inifighters.entrySet())
			inifighters.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
		return inifighters;
	}

	@Override
	public java.util.Map<Long, Integer> getAudience() { // 观战的roleid battleindex
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "audience"), audience);
	}

	@Override
	public java.util.Map<Long, Integer> getAudienceAsData() { // 观战的roleid battleindex
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> audience;
		BattleResult _o_ = this;
		audience = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.audience.entrySet())
			audience.put(_e_.getKey(), _e_.getValue());
		return audience;
	}

	@Override
	public java.util.NavigableMap<Integer, xbean.Fighter> getWatchers() { // 
		_xdb_verify_unsafe_();
		return mkdb.Logs.logNavigableMap(new mkdb.LogKey(this, "watchers"), watchers);
	}

	public java.util.NavigableMap<Integer, xbean.Fighter> getWatchersAsData() { // 
		_xdb_verify_unsafe_();
		java.util.NavigableMap<Integer, xbean.Fighter> watchers;
		BattleResult _o_ = this;
		watchers = new java.util.TreeMap<Integer, xbean.Fighter>();
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.watchers.entrySet())
			watchers.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
		return watchers;
	}

	@Override
	public long getMonsterid() { // 明雷怪id
		_xdb_verify_unsafe_();
		return monsterid;
	}

	@Override
	public int getRound() { // 战斗当前的回合数
		_xdb_verify_unsafe_();
		return round;
	}

	@Override
	public long getRoundtime() { // PRoundStart后是本回合的开始时间;PPlayScript后是播放开始的时间
		_xdb_verify_unsafe_();
		return roundtime;
	}

	@Override
	public long getScriptplaytime() { // 脚本播放时间（ms），根据脚本计算出来的播放时间，是脚本播放的最短时间，最短1秒
		_xdb_verify_unsafe_();
		return scriptplaytime;
	}

	@Override
	public java.util.Map<Integer, xbean.Decision> getDecisions() { // 战斗单元决策结果
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "decisions"), decisions);
	}

	@Override
	public java.util.Map<Integer, xbean.Decision> getDecisionsAsData() { // 战斗单元决策结果
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.Decision> decisions;
		BattleResult _o_ = this;
		decisions = new java.util.HashMap<Integer, xbean.Decision>();
		for (java.util.Map.Entry<Integer, xbean.Decision> _e_ : _o_.decisions.entrySet())
			decisions.put(_e_.getKey(), new Decision.Data(_e_.getValue()));
		return decisions;
	}

	@Override
	public java.util.Set<Long> getFeedbacks() { // roleid				角色的反馈记录
		_xdb_verify_unsafe_();
		return mkdb.Logs.logSet(new mkdb.LogKey(this, "feedbacks"), feedbacks);
	}

	public java.util.Set<Long> getFeedbacksAsData() { // roleid				角色的反馈记录
		_xdb_verify_unsafe_();
		java.util.Set<Long> feedbacks;
		BattleResult _o_ = this;
		feedbacks = new mkdb.util.SetX<Long>();
		feedbacks.addAll(_o_.feedbacks);
		return feedbacks;
	}

	@Override
	public int getBattleresult() { // 战斗是否结束及战斗结果，参考BattleField.BATTLE_XXX,默认为2（BATTLE_NOT_END）
		_xdb_verify_unsafe_();
		return battleresult;
	}

	@Override
	public java.util.Map<Integer, Integer> getRoundresult() { // 战斗单位：战斗结果
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "roundresult"), roundresult);
	}

	@Override
	public java.util.Map<Integer, Integer> getRoundresultAsData() { // 战斗单位：战斗结果
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> roundresult;
		BattleResult _o_ = this;
		roundresult = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.roundresult.entrySet())
			roundresult.put(_e_.getKey(), _e_.getValue());
		return roundresult;
	}

	@Override
	public java.util.Map<Integer, xbean.Protectors> getProtectormap() { // 被保护人：保护者列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "protectormap"), protectormap);
	}

	@Override
	public java.util.Map<Integer, xbean.Protectors> getProtectormapAsData() { // 被保护人：保护者列表
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.Protectors> protectormap;
		BattleResult _o_ = this;
		protectormap = new java.util.HashMap<Integer, xbean.Protectors>();
		for (java.util.Map.Entry<Integer, xbean.Protectors> _e_ : _o_.protectormap.entrySet())
			protectormap.put(_e_.getKey(), new Protectors.Data(_e_.getValue()));
		return protectormap;
	}

	@Override
	public java.util.Map<Integer, Float> getBattledatas() { // 保存战斗相关的一些数据
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "battledatas"), battledatas);
	}

	@Override
	public java.util.Map<Integer, Float> getBattledatasAsData() { // 保存战斗相关的一些数据
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Float> battledatas;
		BattleResult _o_ = this;
		battledatas = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.battledatas.entrySet())
			battledatas.put(_e_.getKey(), _e_.getValue());
		return battledatas;
	}

	@Override
	public void setBattleid(long _v_) { // 战斗ID
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "battleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, battleid) {
					public void rollback() { battleid = _xdb_saved; }
				};}});
		battleid = _v_;
	}

	@Override
	public void setConfigid(int _v_) { // 战斗配置ID，默认为0时不是通过战斗配置new的ID
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "configid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, configid) {
					public void rollback() { configid = _xdb_saved; }
				};}});
		configid = _v_;
	}

	@Override
	public void setEnvironment(int _v_) { // 战场环境
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "environment") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, environment) {
					public void rollback() { environment = _xdb_saved; }
				};}});
		environment = _v_;
	}

	@Override
	public void setPhase(int _v_) { // 战斗的阶段
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "phase") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, phase) {
					public void rollback() { phase = _xdb_saved; }
				};}});
		phase = _v_;
	}

	@Override
	public void setBattletype(int _v_) { // 战斗类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "battletype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, battletype) {
					public void rollback() { battletype = _xdb_saved; }
				};}});
		battletype = _v_;
	}

	@Override
	public void setAreatype(int _v_) { // 练功区类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "areatype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, areatype) {
					public void rollback() { areatype = _xdb_saved; }
				};}});
		areatype = _v_;
	}

	@Override
	public void setAreaconf(int _v_) { // 练功区配置值
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "areaconf") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, areaconf) {
					public void rollback() { areaconf = _xdb_saved; }
				};}});
		areaconf = _v_;
	}

	@Override
	public void setHostform(int _v_) { // 主方光环
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "hostform") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, hostform) {
					public void rollback() { hostform = _xdb_saved; }
				};}});
		hostform = _v_;
	}

	@Override
	public void setGuestform(int _v_) { // 客方光环
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "guestform") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, guestform) {
					public void rollback() { guestform = _xdb_saved; }
				};}});
		guestform = _v_;
	}

	@Override
	public void setMonstertype(int _v_) { // 怪物类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "monstertype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, monstertype) {
					public void rollback() { monstertype = _xdb_saved; }
				};}});
		monstertype = _v_;
	}

	@Override
	public void setMonsterid(long _v_) { // 明雷怪id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "monsterid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, monsterid) {
					public void rollback() { monsterid = _xdb_saved; }
				};}});
		monsterid = _v_;
	}

	@Override
	public void setRound(int _v_) { // 战斗当前的回合数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "round") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, round) {
					public void rollback() { round = _xdb_saved; }
				};}});
		round = _v_;
	}

	@Override
	public void setRoundtime(long _v_) { // PRoundStart后是本回合的开始时间;PPlayScript后是播放开始的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roundtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roundtime) {
					public void rollback() { roundtime = _xdb_saved; }
				};}});
		roundtime = _v_;
	}

	@Override
	public void setScriptplaytime(long _v_) { // 脚本播放时间（ms），根据脚本计算出来的播放时间，是脚本播放的最短时间，最短1秒
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "scriptplaytime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, scriptplaytime) {
					public void rollback() { scriptplaytime = _xdb_saved; }
				};}});
		scriptplaytime = _v_;
	}

	@Override
	public void setBattleresult(int _v_) { // 战斗是否结束及战斗结果，参考BattleField.BATTLE_XXX,默认为2（BATTLE_NOT_END）
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
		BattleResult _o_ = null;
		if ( _o1_ instanceof BattleResult ) _o_ = (BattleResult)_o1_;
		else if ( _o1_ instanceof BattleResult.Const ) _o_ = ((BattleResult.Const)_o1_).nThis();
		else return false;
		if (battleid != _o_.battleid) return false;
		if (configid != _o_.configid) return false;
		if (environment != _o_.environment) return false;
		if (phase != _o_.phase) return false;
		if (battletype != _o_.battletype) return false;
		if (areatype != _o_.areatype) return false;
		if (areaconf != _o_.areaconf) return false;
		if (hostform != _o_.hostform) return false;
		if (guestform != _o_.guestform) return false;
		if (monstertype != _o_.monstertype) return false;
		if (!roleids.equals(_o_.roleids)) return false;
		if (!fighters.equals(_o_.fighters)) return false;
		if (!skipeddeadfighters.equals(_o_.skipeddeadfighters)) return false;
		if (!monsters.equals(_o_.monsters)) return false;
		if (!failedmonsters.equals(_o_.failedmonsters)) return false;
		if (!inifighters.equals(_o_.inifighters)) return false;
		if (!audience.equals(_o_.audience)) return false;
		if (!watchers.equals(_o_.watchers)) return false;
		if (monsterid != _o_.monsterid) return false;
		if (round != _o_.round) return false;
		if (roundtime != _o_.roundtime) return false;
		if (scriptplaytime != _o_.scriptplaytime) return false;
		if (!decisions.equals(_o_.decisions)) return false;
		if (!feedbacks.equals(_o_.feedbacks)) return false;
		if (battleresult != _o_.battleresult) return false;
		if (!roundresult.equals(_o_.roundresult)) return false;
		if (!protectormap.equals(_o_.protectormap)) return false;
		if (!battledatas.equals(_o_.battledatas)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += battleid;
		_h_ += configid;
		_h_ += environment;
		_h_ += phase;
		_h_ += battletype;
		_h_ += areatype;
		_h_ += areaconf;
		_h_ += hostform;
		_h_ += guestform;
		_h_ += monstertype;
		_h_ += roleids.hashCode();
		_h_ += fighters.hashCode();
		_h_ += skipeddeadfighters.hashCode();
		_h_ += monsters.hashCode();
		_h_ += failedmonsters.hashCode();
		_h_ += inifighters.hashCode();
		_h_ += audience.hashCode();
		_h_ += watchers.hashCode();
		_h_ += monsterid;
		_h_ += round;
		_h_ += roundtime;
		_h_ += scriptplaytime;
		_h_ += decisions.hashCode();
		_h_ += feedbacks.hashCode();
		_h_ += battleresult;
		_h_ += roundresult.hashCode();
		_h_ += protectormap.hashCode();
		_h_ += battledatas.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(battleid);
		_sb_.append(",");
		_sb_.append(configid);
		_sb_.append(",");
		_sb_.append(environment);
		_sb_.append(",");
		_sb_.append(phase);
		_sb_.append(",");
		_sb_.append(battletype);
		_sb_.append(",");
		_sb_.append(areatype);
		_sb_.append(",");
		_sb_.append(areaconf);
		_sb_.append(",");
		_sb_.append(hostform);
		_sb_.append(",");
		_sb_.append(guestform);
		_sb_.append(",");
		_sb_.append(monstertype);
		_sb_.append(",");
		_sb_.append(roleids);
		_sb_.append(",");
		_sb_.append(fighters);
		_sb_.append(",");
		_sb_.append(skipeddeadfighters);
		_sb_.append(",");
		_sb_.append(monsters);
		_sb_.append(",");
		_sb_.append(failedmonsters);
		_sb_.append(",");
		_sb_.append(inifighters);
		_sb_.append(",");
		_sb_.append(audience);
		_sb_.append(",");
		_sb_.append(watchers);
		_sb_.append(",");
		_sb_.append(monsterid);
		_sb_.append(",");
		_sb_.append(round);
		_sb_.append(",");
		_sb_.append(roundtime);
		_sb_.append(",");
		_sb_.append(scriptplaytime);
		_sb_.append(",");
		_sb_.append(decisions);
		_sb_.append(",");
		_sb_.append(feedbacks);
		_sb_.append(",");
		_sb_.append(battleresult);
		_sb_.append(",");
		_sb_.append(roundresult);
		_sb_.append(",");
		_sb_.append(protectormap);
		_sb_.append(",");
		_sb_.append(battledatas);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("configid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("environment"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("phase"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battletype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("areatype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("areaconf"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("hostform"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("guestform"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("monstertype"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("roleids"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("fighters"));
		lb.add(new mkdb.logs.ListenableSet().setVarName("skipeddeadfighters"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("monsters"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("failedmonsters"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("inifighters"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("audience"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("watchers"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("monsterid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("round"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roundtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("scriptplaytime"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("decisions"));
		lb.add(new mkdb.logs.ListenableSet().setVarName("feedbacks"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battleresult"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("roundresult"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("protectormap"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("battledatas"));
		return lb;
	}

	private class Const implements xbean.BattleResult {
		BattleResult nThis() {
			return BattleResult.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BattleResult copy() {
			return BattleResult.this.copy();
		}

		@Override
		public xbean.BattleResult toData() {
			return BattleResult.this.toData();
		}

		public xbean.BattleResult toBean() {
			return BattleResult.this.toBean();
		}

		@Override
		public xbean.BattleResult toDataIf() {
			return BattleResult.this.toDataIf();
		}

		public xbean.BattleResult toBeanIf() {
			return BattleResult.this.toBeanIf();
		}

		@Override
		public long getBattleid() { // 战斗ID
			_xdb_verify_unsafe_();
			return battleid;
		}

		@Override
		public int getConfigid() { // 战斗配置ID，默认为0时不是通过战斗配置new的ID
			_xdb_verify_unsafe_();
			return configid;
		}

		@Override
		public int getEnvironment() { // 战场环境
			_xdb_verify_unsafe_();
			return environment;
		}

		@Override
		public int getPhase() { // 战斗的阶段
			_xdb_verify_unsafe_();
			return phase;
		}

		@Override
		public int getBattletype() { // 战斗类型
			_xdb_verify_unsafe_();
			return battletype;
		}

		@Override
		public int getAreatype() { // 练功区类型
			_xdb_verify_unsafe_();
			return areatype;
		}

		@Override
		public int getAreaconf() { // 练功区配置值
			_xdb_verify_unsafe_();
			return areaconf;
		}

		@Override
		public int getHostform() { // 主方光环
			_xdb_verify_unsafe_();
			return hostform;
		}

		@Override
		public int getGuestform() { // 客方光环
			_xdb_verify_unsafe_();
			return guestform;
		}

		@Override
		public int getMonstertype() { // 怪物类型
			_xdb_verify_unsafe_();
			return monstertype;
		}

		@Override
		public java.util.Map<Long, Integer> getRoleids() { // 所有参战的角色ID : 角色的战斗索引
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(roleids);
		}

		@Override
		public java.util.Map<Long, Integer> getRoleidsAsData() { // 所有参战的角色ID : 角色的战斗索引
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> roleids;
			BattleResult _o_ = BattleResult.this;
			roleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.roleids.entrySet())
				roleids.put(_e_.getKey(), _e_.getValue());
			return roleids;
		}

		@Override
		public java.util.NavigableMap<Integer, xbean.Fighter> getFighters() { // 所有的参战者 战斗索引 ： 战斗者 按索引排序
			_xdb_verify_unsafe_();
			return mkdb.Consts.constNavigableMap(fighters);
		}

		@Override
		public java.util.NavigableMap<Integer, xbean.Fighter> getFightersAsData() { // 所有的参战者 战斗索引 ： 战斗者 按索引排序
			_xdb_verify_unsafe_();
			java.util.NavigableMap<Integer, xbean.Fighter> fighters;
			BattleResult _o_ = BattleResult.this;
			fighters = new java.util.TreeMap<Integer, xbean.Fighter>();
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.fighters.entrySet())
				fighters.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
			return fighters;
		}

		@Override
		public java.util.Set<Integer> getSkipeddeadfighters() { // 本回合因为死亡而未出手的战斗者，出手结束清除
			_xdb_verify_unsafe_();
			return mkdb.Consts.constSet(skipeddeadfighters);
		}

		public java.util.Set<Integer> getSkipeddeadfightersAsData() { // 本回合因为死亡而未出手的战斗者，出手结束清除
			_xdb_verify_unsafe_();
			java.util.Set<Integer> skipeddeadfighters;
			BattleResult _o_ = BattleResult.this;
		skipeddeadfighters = new mkdb.util.SetX<Integer>();
		skipeddeadfighters.addAll(_o_.skipeddeadfighters);
			return skipeddeadfighters;
		}

		@Override
		public java.util.Map<Integer, xbean.Monster> getMonsters() { // 所有在该场战斗中生成的怪物/NPC，key为fighterId
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(monsters);
		}

		@Override
		public java.util.Map<Integer, xbean.Monster> getMonstersAsData() { // 所有在该场战斗中生成的怪物/NPC，key为fighterId
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.Monster> monsters;
			BattleResult _o_ = BattleResult.this;
			monsters = new java.util.HashMap<Integer, xbean.Monster>();
			for (java.util.Map.Entry<Integer, xbean.Monster> _e_ : _o_.monsters.entrySet())
				monsters.put(_e_.getKey(), new Monster.Data(_e_.getValue()));
			return monsters;
		}

		@Override
		public java.util.List<xbean.FailedMonster> getFailedmonsters() { // 失败的怪物（包括己方客方）
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(failedmonsters);
		}

		public java.util.List<xbean.FailedMonster> getFailedmonstersAsData() { // 失败的怪物（包括己方客方）
			_xdb_verify_unsafe_();
			java.util.List<xbean.FailedMonster> failedmonsters;
			BattleResult _o_ = BattleResult.this;
		failedmonsters = new java.util.LinkedList<xbean.FailedMonster>();
		for (xbean.FailedMonster _v_ : _o_.failedmonsters)
			failedmonsters.add(new FailedMonster.Data(_v_));
			return failedmonsters;
		}

		@Override
		public java.util.NavigableMap<Integer, xbean.Fighter> getInifighters() { // 把参战者的状态在战斗前保存一份,切磋pk会用到
			_xdb_verify_unsafe_();
			return mkdb.Consts.constNavigableMap(inifighters);
		}

		@Override
		public java.util.NavigableMap<Integer, xbean.Fighter> getInifightersAsData() { // 把参战者的状态在战斗前保存一份,切磋pk会用到
			_xdb_verify_unsafe_();
			java.util.NavigableMap<Integer, xbean.Fighter> inifighters;
			BattleResult _o_ = BattleResult.this;
			inifighters = new java.util.TreeMap<Integer, xbean.Fighter>();
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.inifighters.entrySet())
				inifighters.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
			return inifighters;
		}

		@Override
		public java.util.Map<Long, Integer> getAudience() { // 观战的roleid battleindex
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(audience);
		}

		@Override
		public java.util.Map<Long, Integer> getAudienceAsData() { // 观战的roleid battleindex
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> audience;
			BattleResult _o_ = BattleResult.this;
			audience = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.audience.entrySet())
				audience.put(_e_.getKey(), _e_.getValue());
			return audience;
		}

		@Override
		public java.util.NavigableMap<Integer, xbean.Fighter> getWatchers() { // 
			_xdb_verify_unsafe_();
			return mkdb.Consts.constNavigableMap(watchers);
		}

		@Override
		public java.util.NavigableMap<Integer, xbean.Fighter> getWatchersAsData() { // 
			_xdb_verify_unsafe_();
			java.util.NavigableMap<Integer, xbean.Fighter> watchers;
			BattleResult _o_ = BattleResult.this;
			watchers = new java.util.TreeMap<Integer, xbean.Fighter>();
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.watchers.entrySet())
				watchers.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
			return watchers;
		}

		@Override
		public long getMonsterid() { // 明雷怪id
			_xdb_verify_unsafe_();
			return monsterid;
		}

		@Override
		public int getRound() { // 战斗当前的回合数
			_xdb_verify_unsafe_();
			return round;
		}

		@Override
		public long getRoundtime() { // PRoundStart后是本回合的开始时间;PPlayScript后是播放开始的时间
			_xdb_verify_unsafe_();
			return roundtime;
		}

		@Override
		public long getScriptplaytime() { // 脚本播放时间（ms），根据脚本计算出来的播放时间，是脚本播放的最短时间，最短1秒
			_xdb_verify_unsafe_();
			return scriptplaytime;
		}

		@Override
		public java.util.Map<Integer, xbean.Decision> getDecisions() { // 战斗单元决策结果
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(decisions);
		}

		@Override
		public java.util.Map<Integer, xbean.Decision> getDecisionsAsData() { // 战斗单元决策结果
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.Decision> decisions;
			BattleResult _o_ = BattleResult.this;
			decisions = new java.util.HashMap<Integer, xbean.Decision>();
			for (java.util.Map.Entry<Integer, xbean.Decision> _e_ : _o_.decisions.entrySet())
				decisions.put(_e_.getKey(), new Decision.Data(_e_.getValue()));
			return decisions;
		}

		@Override
		public java.util.Set<Long> getFeedbacks() { // roleid				角色的反馈记录
			_xdb_verify_unsafe_();
			return mkdb.Consts.constSet(feedbacks);
		}

		public java.util.Set<Long> getFeedbacksAsData() { // roleid				角色的反馈记录
			_xdb_verify_unsafe_();
			java.util.Set<Long> feedbacks;
			BattleResult _o_ = BattleResult.this;
		feedbacks = new mkdb.util.SetX<Long>();
		feedbacks.addAll(_o_.feedbacks);
			return feedbacks;
		}

		@Override
		public int getBattleresult() { // 战斗是否结束及战斗结果，参考BattleField.BATTLE_XXX,默认为2（BATTLE_NOT_END）
			_xdb_verify_unsafe_();
			return battleresult;
		}

		@Override
		public java.util.Map<Integer, Integer> getRoundresult() { // 战斗单位：战斗结果
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(roundresult);
		}

		@Override
		public java.util.Map<Integer, Integer> getRoundresultAsData() { // 战斗单位：战斗结果
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> roundresult;
			BattleResult _o_ = BattleResult.this;
			roundresult = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.roundresult.entrySet())
				roundresult.put(_e_.getKey(), _e_.getValue());
			return roundresult;
		}

		@Override
		public java.util.Map<Integer, xbean.Protectors> getProtectormap() { // 被保护人：保护者列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(protectormap);
		}

		@Override
		public java.util.Map<Integer, xbean.Protectors> getProtectormapAsData() { // 被保护人：保护者列表
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.Protectors> protectormap;
			BattleResult _o_ = BattleResult.this;
			protectormap = new java.util.HashMap<Integer, xbean.Protectors>();
			for (java.util.Map.Entry<Integer, xbean.Protectors> _e_ : _o_.protectormap.entrySet())
				protectormap.put(_e_.getKey(), new Protectors.Data(_e_.getValue()));
			return protectormap;
		}

		@Override
		public java.util.Map<Integer, Float> getBattledatas() { // 保存战斗相关的一些数据
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(battledatas);
		}

		@Override
		public java.util.Map<Integer, Float> getBattledatasAsData() { // 保存战斗相关的一些数据
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Float> battledatas;
			BattleResult _o_ = BattleResult.this;
			battledatas = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.battledatas.entrySet())
				battledatas.put(_e_.getKey(), _e_.getValue());
			return battledatas;
		}

		@Override
		public void setBattleid(long _v_) { // 战斗ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setConfigid(int _v_) { // 战斗配置ID，默认为0时不是通过战斗配置new的ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEnvironment(int _v_) { // 战场环境
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPhase(int _v_) { // 战斗的阶段
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBattletype(int _v_) { // 战斗类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAreatype(int _v_) { // 练功区类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAreaconf(int _v_) { // 练功区配置值
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHostform(int _v_) { // 主方光环
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGuestform(int _v_) { // 客方光环
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMonstertype(int _v_) { // 怪物类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMonsterid(long _v_) { // 明雷怪id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRound(int _v_) { // 战斗当前的回合数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoundtime(long _v_) { // PRoundStart后是本回合的开始时间;PPlayScript后是播放开始的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setScriptplaytime(long _v_) { // 脚本播放时间（ms），根据脚本计算出来的播放时间，是脚本播放的最短时间，最短1秒
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBattleresult(int _v_) { // 战斗是否结束及战斗结果，参考BattleField.BATTLE_XXX,默认为2（BATTLE_NOT_END）
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
			return BattleResult.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BattleResult.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BattleResult.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BattleResult.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BattleResult.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BattleResult.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BattleResult.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BattleResult.this.hashCode();
		}

		@Override
		public String toString() {
			return BattleResult.this.toString();
		}

	}

	public static final class Data implements xbean.BattleResult {
		private long battleid; // 战斗ID
		private int configid; // 战斗配置ID，默认为0时不是通过战斗配置new的ID
		private int environment; // 战场环境
		private int phase; // 战斗的阶段
		private int battletype; // 战斗类型
		private int areatype; // 练功区类型
		private int areaconf; // 练功区配置值
		private int hostform; // 主方光环
		private int guestform; // 客方光环
		private int monstertype; // 怪物类型
		private java.util.HashMap<Long, Integer> roleids; // 所有参战的角色ID : 角色的战斗索引
		private java.util.TreeMap<Integer, xbean.Fighter> fighters; // 所有的参战者 战斗索引 ： 战斗者 按索引排序
		private java.util.HashSet<Integer> skipeddeadfighters; // 本回合因为死亡而未出手的战斗者，出手结束清除
		private java.util.HashMap<Integer, xbean.Monster> monsters; // 所有在该场战斗中生成的怪物/NPC，key为fighterId
		private java.util.LinkedList<xbean.FailedMonster> failedmonsters; // 失败的怪物（包括己方客方）
		private java.util.TreeMap<Integer, xbean.Fighter> inifighters; // 把参战者的状态在战斗前保存一份,切磋pk会用到
		private java.util.HashMap<Long, Integer> audience; // 观战的roleid battleindex
		private java.util.TreeMap<Integer, xbean.Fighter> watchers; // 
		private long monsterid; // 明雷怪id
		private int round; // 战斗当前的回合数
		private long roundtime; // PRoundStart后是本回合的开始时间;PPlayScript后是播放开始的时间
		private long scriptplaytime; // 脚本播放时间（ms），根据脚本计算出来的播放时间，是脚本播放的最短时间，最短1秒
		private java.util.HashMap<Integer, xbean.Decision> decisions; // 战斗单元决策结果
		private java.util.HashSet<Long> feedbacks; // roleid				角色的反馈记录
		private int battleresult; // 战斗是否结束及战斗结果，参考BattleField.BATTLE_XXX,默认为2（BATTLE_NOT_END）
		private java.util.HashMap<Integer, Integer> roundresult; // 战斗单位：战斗结果
		private java.util.HashMap<Integer, xbean.Protectors> protectormap; // 被保护人：保护者列表
		private java.util.HashMap<Integer, Float> battledatas; // 保存战斗相关的一些数据

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			configid = 0;
			roleids = new java.util.HashMap<Long, Integer>();
			fighters = new java.util.TreeMap<Integer, xbean.Fighter>();
			skipeddeadfighters = new java.util.HashSet<Integer>();
			monsters = new java.util.HashMap<Integer, xbean.Monster>();
			failedmonsters = new java.util.LinkedList<xbean.FailedMonster>();
			inifighters = new java.util.TreeMap<Integer, xbean.Fighter>();
			audience = new java.util.HashMap<Long, Integer>();
			watchers = new java.util.TreeMap<Integer, xbean.Fighter>();
			monsterid = -1;
			round = 1;
			scriptplaytime = 1000;
			decisions = new java.util.HashMap<Integer, xbean.Decision>();
			feedbacks = new java.util.HashSet<Long>();
			battleresult = 2;
			roundresult = new java.util.HashMap<Integer, Integer>();
			protectormap = new java.util.HashMap<Integer, xbean.Protectors>();
			battledatas = new java.util.HashMap<Integer, Float>();
		}

		Data(xbean.BattleResult _o1_) {
			if (_o1_ instanceof BattleResult) assign((BattleResult)_o1_);
			else if (_o1_ instanceof BattleResult.Data) assign((BattleResult.Data)_o1_);
			else if (_o1_ instanceof BattleResult.Const) assign(((BattleResult.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(BattleResult _o_) {
			battleid = _o_.battleid;
			configid = _o_.configid;
			environment = _o_.environment;
			phase = _o_.phase;
			battletype = _o_.battletype;
			areatype = _o_.areatype;
			areaconf = _o_.areaconf;
			hostform = _o_.hostform;
			guestform = _o_.guestform;
			monstertype = _o_.monstertype;
			roleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.roleids.entrySet())
				roleids.put(_e_.getKey(), _e_.getValue());
			fighters = new java.util.TreeMap<Integer, xbean.Fighter>();
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.fighters.entrySet())
				fighters.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
			skipeddeadfighters = new java.util.HashSet<Integer>();
			skipeddeadfighters.addAll(_o_.skipeddeadfighters);
			monsters = new java.util.HashMap<Integer, xbean.Monster>();
			for (java.util.Map.Entry<Integer, xbean.Monster> _e_ : _o_.monsters.entrySet())
				monsters.put(_e_.getKey(), new Monster.Data(_e_.getValue()));
			failedmonsters = new java.util.LinkedList<xbean.FailedMonster>();
			for (xbean.FailedMonster _v_ : _o_.failedmonsters)
				failedmonsters.add(new FailedMonster.Data(_v_));
			inifighters = new java.util.TreeMap<Integer, xbean.Fighter>();
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.inifighters.entrySet())
				inifighters.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
			audience = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.audience.entrySet())
				audience.put(_e_.getKey(), _e_.getValue());
			watchers = new java.util.TreeMap<Integer, xbean.Fighter>();
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.watchers.entrySet())
				watchers.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
			monsterid = _o_.monsterid;
			round = _o_.round;
			roundtime = _o_.roundtime;
			scriptplaytime = _o_.scriptplaytime;
			decisions = new java.util.HashMap<Integer, xbean.Decision>();
			for (java.util.Map.Entry<Integer, xbean.Decision> _e_ : _o_.decisions.entrySet())
				decisions.put(_e_.getKey(), new Decision.Data(_e_.getValue()));
			feedbacks = new java.util.HashSet<Long>();
			feedbacks.addAll(_o_.feedbacks);
			battleresult = _o_.battleresult;
			roundresult = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.roundresult.entrySet())
				roundresult.put(_e_.getKey(), _e_.getValue());
			protectormap = new java.util.HashMap<Integer, xbean.Protectors>();
			for (java.util.Map.Entry<Integer, xbean.Protectors> _e_ : _o_.protectormap.entrySet())
				protectormap.put(_e_.getKey(), new Protectors.Data(_e_.getValue()));
			battledatas = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.battledatas.entrySet())
				battledatas.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(BattleResult.Data _o_) {
			battleid = _o_.battleid;
			configid = _o_.configid;
			environment = _o_.environment;
			phase = _o_.phase;
			battletype = _o_.battletype;
			areatype = _o_.areatype;
			areaconf = _o_.areaconf;
			hostform = _o_.hostform;
			guestform = _o_.guestform;
			monstertype = _o_.monstertype;
			roleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.roleids.entrySet())
				roleids.put(_e_.getKey(), _e_.getValue());
			fighters = new java.util.TreeMap<Integer, xbean.Fighter>();
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.fighters.entrySet())
				fighters.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
			skipeddeadfighters = new java.util.HashSet<Integer>();
			skipeddeadfighters.addAll(_o_.skipeddeadfighters);
			monsters = new java.util.HashMap<Integer, xbean.Monster>();
			for (java.util.Map.Entry<Integer, xbean.Monster> _e_ : _o_.monsters.entrySet())
				monsters.put(_e_.getKey(), new Monster.Data(_e_.getValue()));
			failedmonsters = new java.util.LinkedList<xbean.FailedMonster>();
			for (xbean.FailedMonster _v_ : _o_.failedmonsters)
				failedmonsters.add(new FailedMonster.Data(_v_));
			inifighters = new java.util.TreeMap<Integer, xbean.Fighter>();
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.inifighters.entrySet())
				inifighters.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
			audience = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.audience.entrySet())
				audience.put(_e_.getKey(), _e_.getValue());
			watchers = new java.util.TreeMap<Integer, xbean.Fighter>();
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.watchers.entrySet())
				watchers.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
			monsterid = _o_.monsterid;
			round = _o_.round;
			roundtime = _o_.roundtime;
			scriptplaytime = _o_.scriptplaytime;
			decisions = new java.util.HashMap<Integer, xbean.Decision>();
			for (java.util.Map.Entry<Integer, xbean.Decision> _e_ : _o_.decisions.entrySet())
				decisions.put(_e_.getKey(), new Decision.Data(_e_.getValue()));
			feedbacks = new java.util.HashSet<Long>();
			feedbacks.addAll(_o_.feedbacks);
			battleresult = _o_.battleresult;
			roundresult = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.roundresult.entrySet())
				roundresult.put(_e_.getKey(), _e_.getValue());
			protectormap = new java.util.HashMap<Integer, xbean.Protectors>();
			for (java.util.Map.Entry<Integer, xbean.Protectors> _e_ : _o_.protectormap.entrySet())
				protectormap.put(_e_.getKey(), new Protectors.Data(_e_.getValue()));
			battledatas = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.battledatas.entrySet())
				battledatas.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(battleid);
			_os_.marshal(configid);
			_os_.marshal(environment);
			_os_.marshal(phase);
			_os_.marshal(battletype);
			_os_.marshal(areatype);
			_os_.marshal(areaconf);
			_os_.marshal(hostform);
			_os_.marshal(guestform);
			_os_.marshal(monstertype);
			_os_.compact_uint32(roleids.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : roleids.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(fighters.size());
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : fighters.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(skipeddeadfighters.size());
			for (Integer _v_ : skipeddeadfighters) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(monsters.size());
			for (java.util.Map.Entry<Integer, xbean.Monster> _e_ : monsters.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(failedmonsters.size());
			for (xbean.FailedMonster _v_ : failedmonsters) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(inifighters.size());
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : inifighters.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(audience.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : audience.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(watchers.size());
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : watchers.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(monsterid);
			_os_.marshal(round);
			_os_.marshal(roundtime);
			_os_.marshal(scriptplaytime);
			_os_.compact_uint32(decisions.size());
			for (java.util.Map.Entry<Integer, xbean.Decision> _e_ : decisions.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(feedbacks.size());
			for (Long _v_ : feedbacks) {
				_os_.marshal(_v_);
			}
			_os_.marshal(battleresult);
			_os_.compact_uint32(roundresult.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : roundresult.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(protectormap.size());
			for (java.util.Map.Entry<Integer, xbean.Protectors> _e_ : protectormap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(battledatas.size());
			for (java.util.Map.Entry<Integer, Float> _e_ : battledatas.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			battleid = _os_.unmarshal_long();
			configid = _os_.unmarshal_int();
			environment = _os_.unmarshal_int();
			phase = _os_.unmarshal_int();
			battletype = _os_.unmarshal_int();
			areatype = _os_.unmarshal_int();
			areaconf = _os_.unmarshal_int();
			hostform = _os_.unmarshal_int();
			guestform = _os_.unmarshal_int();
			monstertype = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					roleids = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					roleids.put(_k_, _v_);
				}
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.Fighter _v_ = xbean.Pod.newFighterData();
				_v_.unmarshal(_os_);
				fighters.put(_k_, _v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				skipeddeadfighters.add(_v_);
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					monsters = new java.util.HashMap<Integer, xbean.Monster>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.Monster _v_ = xbean.Pod.newMonsterData();
					_v_.unmarshal(_os_);
					monsters.put(_k_, _v_);
				}
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.FailedMonster _v_ = xbean.Pod.newFailedMonsterData();
				_v_.unmarshal(_os_);
				failedmonsters.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.Fighter _v_ = xbean.Pod.newFighterData();
				_v_.unmarshal(_os_);
				inifighters.put(_k_, _v_);
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					audience = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					audience.put(_k_, _v_);
				}
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.Fighter _v_ = xbean.Pod.newFighterData();
				_v_.unmarshal(_os_);
				watchers.put(_k_, _v_);
			}
			monsterid = _os_.unmarshal_long();
			round = _os_.unmarshal_int();
			roundtime = _os_.unmarshal_long();
			scriptplaytime = _os_.unmarshal_long();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					decisions = new java.util.HashMap<Integer, xbean.Decision>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.Decision _v_ = xbean.Pod.newDecisionData();
					_v_.unmarshal(_os_);
					decisions.put(_k_, _v_);
				}
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				feedbacks.add(_v_);
			}
			battleresult = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					roundresult = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					roundresult.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					protectormap = new java.util.HashMap<Integer, xbean.Protectors>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.Protectors _v_ = xbean.Pod.newProtectorsData();
					_v_.unmarshal(_os_);
					protectormap.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					battledatas = new java.util.HashMap<Integer, Float>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					float _v_ = 0.0f;
					_v_ = _os_.unmarshal_float();
					battledatas.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.BattleResult copy() {
			return new Data(this);
		}

		@Override
		public xbean.BattleResult toData() {
			return new Data(this);
		}

		public xbean.BattleResult toBean() {
			return new BattleResult(this, null, null);
		}

		@Override
		public xbean.BattleResult toDataIf() {
			return this;
		}

		public xbean.BattleResult toBeanIf() {
			return new BattleResult(this, null, null);
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
		public long getBattleid() { // 战斗ID
			return battleid;
		}

		@Override
		public int getConfigid() { // 战斗配置ID，默认为0时不是通过战斗配置new的ID
			return configid;
		}

		@Override
		public int getEnvironment() { // 战场环境
			return environment;
		}

		@Override
		public int getPhase() { // 战斗的阶段
			return phase;
		}

		@Override
		public int getBattletype() { // 战斗类型
			return battletype;
		}

		@Override
		public int getAreatype() { // 练功区类型
			return areatype;
		}

		@Override
		public int getAreaconf() { // 练功区配置值
			return areaconf;
		}

		@Override
		public int getHostform() { // 主方光环
			return hostform;
		}

		@Override
		public int getGuestform() { // 客方光环
			return guestform;
		}

		@Override
		public int getMonstertype() { // 怪物类型
			return monstertype;
		}

		@Override
		public java.util.Map<Long, Integer> getRoleids() { // 所有参战的角色ID : 角色的战斗索引
			return roleids;
		}

		@Override
		public java.util.Map<Long, Integer> getRoleidsAsData() { // 所有参战的角色ID : 角色的战斗索引
			return roleids;
		}

		@Override
		public java.util.NavigableMap<Integer, xbean.Fighter> getFighters() { // 所有的参战者 战斗索引 ： 战斗者 按索引排序
			return fighters;
		}

		@Override
		public java.util.NavigableMap<Integer, xbean.Fighter> getFightersAsData() { // 所有的参战者 战斗索引 ： 战斗者 按索引排序
			return fighters;
		}

		@Override
		public java.util.Set<Integer> getSkipeddeadfighters() { // 本回合因为死亡而未出手的战斗者，出手结束清除
			return skipeddeadfighters;
		}

		@Override
		public java.util.Set<Integer> getSkipeddeadfightersAsData() { // 本回合因为死亡而未出手的战斗者，出手结束清除
			return skipeddeadfighters;
		}

		@Override
		public java.util.Map<Integer, xbean.Monster> getMonsters() { // 所有在该场战斗中生成的怪物/NPC，key为fighterId
			return monsters;
		}

		@Override
		public java.util.Map<Integer, xbean.Monster> getMonstersAsData() { // 所有在该场战斗中生成的怪物/NPC，key为fighterId
			return monsters;
		}

		@Override
		public java.util.List<xbean.FailedMonster> getFailedmonsters() { // 失败的怪物（包括己方客方）
			return failedmonsters;
		}

		@Override
		public java.util.List<xbean.FailedMonster> getFailedmonstersAsData() { // 失败的怪物（包括己方客方）
			return failedmonsters;
		}

		@Override
		public java.util.NavigableMap<Integer, xbean.Fighter> getInifighters() { // 把参战者的状态在战斗前保存一份,切磋pk会用到
			return inifighters;
		}

		@Override
		public java.util.NavigableMap<Integer, xbean.Fighter> getInifightersAsData() { // 把参战者的状态在战斗前保存一份,切磋pk会用到
			return inifighters;
		}

		@Override
		public java.util.Map<Long, Integer> getAudience() { // 观战的roleid battleindex
			return audience;
		}

		@Override
		public java.util.Map<Long, Integer> getAudienceAsData() { // 观战的roleid battleindex
			return audience;
		}

		@Override
		public java.util.NavigableMap<Integer, xbean.Fighter> getWatchers() { // 
			return watchers;
		}

		@Override
		public java.util.NavigableMap<Integer, xbean.Fighter> getWatchersAsData() { // 
			return watchers;
		}

		@Override
		public long getMonsterid() { // 明雷怪id
			return monsterid;
		}

		@Override
		public int getRound() { // 战斗当前的回合数
			return round;
		}

		@Override
		public long getRoundtime() { // PRoundStart后是本回合的开始时间;PPlayScript后是播放开始的时间
			return roundtime;
		}

		@Override
		public long getScriptplaytime() { // 脚本播放时间（ms），根据脚本计算出来的播放时间，是脚本播放的最短时间，最短1秒
			return scriptplaytime;
		}

		@Override
		public java.util.Map<Integer, xbean.Decision> getDecisions() { // 战斗单元决策结果
			return decisions;
		}

		@Override
		public java.util.Map<Integer, xbean.Decision> getDecisionsAsData() { // 战斗单元决策结果
			return decisions;
		}

		@Override
		public java.util.Set<Long> getFeedbacks() { // roleid				角色的反馈记录
			return feedbacks;
		}

		@Override
		public java.util.Set<Long> getFeedbacksAsData() { // roleid				角色的反馈记录
			return feedbacks;
		}

		@Override
		public int getBattleresult() { // 战斗是否结束及战斗结果，参考BattleField.BATTLE_XXX,默认为2（BATTLE_NOT_END）
			return battleresult;
		}

		@Override
		public java.util.Map<Integer, Integer> getRoundresult() { // 战斗单位：战斗结果
			return roundresult;
		}

		@Override
		public java.util.Map<Integer, Integer> getRoundresultAsData() { // 战斗单位：战斗结果
			return roundresult;
		}

		@Override
		public java.util.Map<Integer, xbean.Protectors> getProtectormap() { // 被保护人：保护者列表
			return protectormap;
		}

		@Override
		public java.util.Map<Integer, xbean.Protectors> getProtectormapAsData() { // 被保护人：保护者列表
			return protectormap;
		}

		@Override
		public java.util.Map<Integer, Float> getBattledatas() { // 保存战斗相关的一些数据
			return battledatas;
		}

		@Override
		public java.util.Map<Integer, Float> getBattledatasAsData() { // 保存战斗相关的一些数据
			return battledatas;
		}

		@Override
		public void setBattleid(long _v_) { // 战斗ID
			battleid = _v_;
		}

		@Override
		public void setConfigid(int _v_) { // 战斗配置ID，默认为0时不是通过战斗配置new的ID
			configid = _v_;
		}

		@Override
		public void setEnvironment(int _v_) { // 战场环境
			environment = _v_;
		}

		@Override
		public void setPhase(int _v_) { // 战斗的阶段
			phase = _v_;
		}

		@Override
		public void setBattletype(int _v_) { // 战斗类型
			battletype = _v_;
		}

		@Override
		public void setAreatype(int _v_) { // 练功区类型
			areatype = _v_;
		}

		@Override
		public void setAreaconf(int _v_) { // 练功区配置值
			areaconf = _v_;
		}

		@Override
		public void setHostform(int _v_) { // 主方光环
			hostform = _v_;
		}

		@Override
		public void setGuestform(int _v_) { // 客方光环
			guestform = _v_;
		}

		@Override
		public void setMonstertype(int _v_) { // 怪物类型
			monstertype = _v_;
		}

		@Override
		public void setMonsterid(long _v_) { // 明雷怪id
			monsterid = _v_;
		}

		@Override
		public void setRound(int _v_) { // 战斗当前的回合数
			round = _v_;
		}

		@Override
		public void setRoundtime(long _v_) { // PRoundStart后是本回合的开始时间;PPlayScript后是播放开始的时间
			roundtime = _v_;
		}

		@Override
		public void setScriptplaytime(long _v_) { // 脚本播放时间（ms），根据脚本计算出来的播放时间，是脚本播放的最短时间，最短1秒
			scriptplaytime = _v_;
		}

		@Override
		public void setBattleresult(int _v_) { // 战斗是否结束及战斗结果，参考BattleField.BATTLE_XXX,默认为2（BATTLE_NOT_END）
			battleresult = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BattleResult.Data)) return false;
			BattleResult.Data _o_ = (BattleResult.Data) _o1_;
			if (battleid != _o_.battleid) return false;
			if (configid != _o_.configid) return false;
			if (environment != _o_.environment) return false;
			if (phase != _o_.phase) return false;
			if (battletype != _o_.battletype) return false;
			if (areatype != _o_.areatype) return false;
			if (areaconf != _o_.areaconf) return false;
			if (hostform != _o_.hostform) return false;
			if (guestform != _o_.guestform) return false;
			if (monstertype != _o_.monstertype) return false;
			if (!roleids.equals(_o_.roleids)) return false;
			if (!fighters.equals(_o_.fighters)) return false;
			if (!skipeddeadfighters.equals(_o_.skipeddeadfighters)) return false;
			if (!monsters.equals(_o_.monsters)) return false;
			if (!failedmonsters.equals(_o_.failedmonsters)) return false;
			if (!inifighters.equals(_o_.inifighters)) return false;
			if (!audience.equals(_o_.audience)) return false;
			if (!watchers.equals(_o_.watchers)) return false;
			if (monsterid != _o_.monsterid) return false;
			if (round != _o_.round) return false;
			if (roundtime != _o_.roundtime) return false;
			if (scriptplaytime != _o_.scriptplaytime) return false;
			if (!decisions.equals(_o_.decisions)) return false;
			if (!feedbacks.equals(_o_.feedbacks)) return false;
			if (battleresult != _o_.battleresult) return false;
			if (!roundresult.equals(_o_.roundresult)) return false;
			if (!protectormap.equals(_o_.protectormap)) return false;
			if (!battledatas.equals(_o_.battledatas)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += battleid;
			_h_ += configid;
			_h_ += environment;
			_h_ += phase;
			_h_ += battletype;
			_h_ += areatype;
			_h_ += areaconf;
			_h_ += hostform;
			_h_ += guestform;
			_h_ += monstertype;
			_h_ += roleids.hashCode();
			_h_ += fighters.hashCode();
			_h_ += skipeddeadfighters.hashCode();
			_h_ += monsters.hashCode();
			_h_ += failedmonsters.hashCode();
			_h_ += inifighters.hashCode();
			_h_ += audience.hashCode();
			_h_ += watchers.hashCode();
			_h_ += monsterid;
			_h_ += round;
			_h_ += roundtime;
			_h_ += scriptplaytime;
			_h_ += decisions.hashCode();
			_h_ += feedbacks.hashCode();
			_h_ += battleresult;
			_h_ += roundresult.hashCode();
			_h_ += protectormap.hashCode();
			_h_ += battledatas.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(battleid);
			_sb_.append(",");
			_sb_.append(configid);
			_sb_.append(",");
			_sb_.append(environment);
			_sb_.append(",");
			_sb_.append(phase);
			_sb_.append(",");
			_sb_.append(battletype);
			_sb_.append(",");
			_sb_.append(areatype);
			_sb_.append(",");
			_sb_.append(areaconf);
			_sb_.append(",");
			_sb_.append(hostform);
			_sb_.append(",");
			_sb_.append(guestform);
			_sb_.append(",");
			_sb_.append(monstertype);
			_sb_.append(",");
			_sb_.append(roleids);
			_sb_.append(",");
			_sb_.append(fighters);
			_sb_.append(",");
			_sb_.append(skipeddeadfighters);
			_sb_.append(",");
			_sb_.append(monsters);
			_sb_.append(",");
			_sb_.append(failedmonsters);
			_sb_.append(",");
			_sb_.append(inifighters);
			_sb_.append(",");
			_sb_.append(audience);
			_sb_.append(",");
			_sb_.append(watchers);
			_sb_.append(",");
			_sb_.append(monsterid);
			_sb_.append(",");
			_sb_.append(round);
			_sb_.append(",");
			_sb_.append(roundtime);
			_sb_.append(",");
			_sb_.append(scriptplaytime);
			_sb_.append(",");
			_sb_.append(decisions);
			_sb_.append(",");
			_sb_.append(feedbacks);
			_sb_.append(",");
			_sb_.append(battleresult);
			_sb_.append(",");
			_sb_.append(roundresult);
			_sb_.append(",");
			_sb_.append(protectormap);
			_sb_.append(",");
			_sb_.append(battledatas);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
