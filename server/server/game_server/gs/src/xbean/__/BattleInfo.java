
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class BattleInfo extends mkdb.XBean implements xbean.BattleInfo {
	private long battleid; // 战斗ID
	private int configid; // 战斗配置ID，默认为0时不是通过战斗配置new的ID
	private int background; // 战斗地图
	private int bgm; // 背景音乐
	private int environment; // 战场环境
	private int gmactiontimetimes; // 时间倍率应用除以10.0，用于调试，gm使用。
	private int envchangefrom; // 环境改变来源（0 = 场外， 1 = 场内）
	private int phase; // 战斗的阶段
	private int round; // 战斗当前的回合数
	private boolean canrunaway; // 本场战斗是否能逃跑
	private int autokey; // 战斗者key，fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
	private int battletype; // 战斗类型
	private int areatype; // 练功区类型
	private int areaconf; // 练功区配置值
	private int babyflag; // 是否是含有宝宝的战斗 0不是     1是含有宝宝的战斗
	private int babybagfull; // 宠物栏是否满  0不满    1满
	private long hostroleid; // 发起战斗的主角或者队长的id
	private long monsterid; // 明雷怪npckey
	private int monstertype; // 怪物类型
	private java.util.ArrayList<fire.pb.battle.BattleEndHandler> battleendhandlers; // 处理战斗结束的handler
	private int hostform; // 主方光环
	private int guestform; // 客方光环
	private int hostformlevel; // 主方光环等级
	private int guestformlevel; // 客方光环等级
	private float hostformeffcet; // 主对客方光环压制效果
	private java.util.LinkedList<fire.pb.battle.FighterInfo> figterinfoonbegin; // 出战斗时的战斗者信息
	private java.util.HashMap<Long, Integer> roleids; // 所有参战的角色ID : 角色的战斗索引
	private java.util.TreeMap<Integer, xbean.Fighter> fighters; // 所有的参战者 战斗索引 ： 战斗者 按索引排序
	private java.util.HashMap<Integer, fire.pb.battle.Fighter> fighterobjects; // 包装好的Fighter类，战斗开始时直接创建完，不用每次用的时候再new,注意使用前要锁rolelock
	private mkdb.util.SetX<Integer> skipeddeadfighters; // 本回合因为死亡而未出手的战斗者，出手结束清除
	private java.util.HashMap<Integer, xbean.Monster> monsters; // 所有在该场战斗中生成的怪物/NPC，key为fighterId
	private java.util.LinkedList<xbean.FailedMonster> failedmonsters; // 失败的怪物（包括己方客方）
	private java.util.TreeMap<Integer, xbean.Fighter> inifighters; // 把参战者的状态在战斗前保存一份,切磋pk会用到
	private java.util.HashMap<Long, Integer> audience; // 观战的roleid battleindex
	private java.util.TreeMap<Integer, xbean.Fighter> watchers; // 
	private java.util.LinkedList<Long> offlineroleids; // 战斗中掉线的角色，不能发送协议
	private long battlebegintime; // 战斗开始的时间
	private long roundtime; // PRoundStart后是本回合的开始时间;PPlayScript后是播放开始的时间
	private long scriptplaytime; // 脚本播放时间（ms），根据脚本计算出来的播放时间，是脚本播放的最短时间，最短1秒
	private java.util.HashMap<Integer, xbean.Decision> decisions; // 战斗单元决策结果
	private mkdb.util.SetX<Long> feedbacks; // roleid				角色的反馈记录
	private int battleresult; // 战斗是否结束及战斗结果，参考BattleField.BATTLE_XXX,默认为2（BATTLE_NOT_END）
	private java.util.HashMap<Integer, Integer> roundresult; // 战斗单位：战斗结果
	private java.util.HashMap<Integer, xbean.Protectors> protectormap; // 被保护人：保护者列表
	private java.util.concurrent.ScheduledFuture<?> timeout; // 超时检测
	private java.util.LinkedList<xbean.BattleAI> battleais; // 挂在战场上的AI
	private fire.script.FightJSEngine engine; // 用于本场战斗的JS引擎
	private java.util.HashMap<Integer, fire.pb.battle.ActionChangedAttrs> fighterchangedattrs; // 战斗者每个回合改变的属性,key=fighterId
	private java.util.HashMap<Integer, Float> battledatas; // 保存战斗相关的一些数据
	private fire.pb.battle.Fighter fieldfighter; // 战场虚拟的Fighter，为战场技能而用，虚拟数据的来源是此战斗中的一个怪
	private java.util.LinkedList<xbean.BattleEndProcedure> endprocedures; // 战斗者出战斗时执行的Procedure
	private java.util.HashMap<Integer, xbean.RoundResultItems> roundresultitem; // 整场战斗的demo
	private java.util.LinkedList<Integer> itemtimelist; // 整场战斗的demo时间
	private java.util.ArrayList<fire.pb.battle.operate.Operate> reliveacts; // 复活后要出手的行动
	private java.util.HashMap<Long, Integer> battledeads; // 第几回合死掉的角色
	private int battlemaxround; // 战斗的最大回合数 如果不设置默认为200回合
	private java.util.HashMap<Integer, Integer> params; // 与战斗有关的一些参数
	private java.util.LinkedList<Long> quitteamroleids; // 战斗中有退队角色,战后自动退队 by changhao
	private java.util.LinkedList<Long> absentteamroleids; // 战斗中有暂离角色,战后自动暂离 by changhao
	private java.util.LinkedList<Long> teamidlist; // 战斗里所有队伍id by changhao
	private int livedieteam; // 是否是组队生死战0不是    1是

	@Override
	public void _reset_unsafe_() {
		battleid = 0L;
		configid = 0;
		background = 0;
		bgm = 0;
		environment = 0;
		gmactiontimetimes = 10;
		envchangefrom = 0;
		phase = 0;
		round = 0;
		canrunaway = true;
		autokey = 0;
		battletype = 0;
		areatype = 0;
		areaconf = 0;
		babyflag = 0;
		babybagfull = 0;
		hostroleid = 0L;
		monsterid = -1;
		monstertype = -1;
		battleendhandlers.clear();
		hostform = 0;
		guestform = 0;
		hostformlevel = 0;
		guestformlevel = 0;
		hostformeffcet = 0.0f;
		figterinfoonbegin.clear();
		roleids.clear();
		fighters.clear();
		fighterobjects.clear();
		skipeddeadfighters.clear();
		monsters.clear();
		failedmonsters.clear();
		inifighters.clear();
		audience.clear();
		watchers.clear();
		offlineroleids.clear();
		battlebegintime = 0L;
		roundtime = 0L;
		scriptplaytime = 1000;
		decisions.clear();
		feedbacks.clear();
		battleresult = 2;
		roundresult.clear();
		protectormap.clear();
		timeout = null;
		battleais.clear();
		engine = null;
		fighterchangedattrs.clear();
		battledatas.clear();
		fieldfighter = null;
		endprocedures.clear();
		roundresultitem.clear();
		itemtimelist.clear();
		reliveacts.clear();
		battledeads.clear();
		battlemaxround = 0;
		params.clear();
		quitteamroleids.clear();
		absentteamroleids.clear();
		teamidlist.clear();
		livedieteam = 0;
	}

	BattleInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		configid = 0;
		background = 0;
		bgm = 0;
		gmactiontimetimes = 10;
		round = 0;
		canrunaway = true;
		autokey = 0;
		babyflag = 0;
		babybagfull = 0;
		monsterid = -1;
		monstertype = -1;
		battleendhandlers = new java.util.ArrayList<fire.pb.battle.BattleEndHandler>();
		figterinfoonbegin = new java.util.LinkedList<fire.pb.battle.FighterInfo>();
		roleids = new java.util.HashMap<Long, Integer>();
		fighters = new java.util.TreeMap<Integer, xbean.Fighter>();
		fighterobjects = new java.util.HashMap<Integer, fire.pb.battle.Fighter>();
		skipeddeadfighters = new mkdb.util.SetX<Integer>();
		monsters = new java.util.HashMap<Integer, xbean.Monster>();
		failedmonsters = new java.util.LinkedList<xbean.FailedMonster>();
		inifighters = new java.util.TreeMap<Integer, xbean.Fighter>();
		audience = new java.util.HashMap<Long, Integer>();
		watchers = new java.util.TreeMap<Integer, xbean.Fighter>();
		offlineroleids = new java.util.LinkedList<Long>();
		scriptplaytime = 1000;
		decisions = new java.util.HashMap<Integer, xbean.Decision>();
		feedbacks = new mkdb.util.SetX<Long>();
		battleresult = 2;
		roundresult = new java.util.HashMap<Integer, Integer>();
		protectormap = new java.util.HashMap<Integer, xbean.Protectors>();
		timeout = null;
		battleais = new java.util.LinkedList<xbean.BattleAI>();
		engine = null;
		fighterchangedattrs = new java.util.HashMap<Integer, fire.pb.battle.ActionChangedAttrs>();
		battledatas = new java.util.HashMap<Integer, Float>();
		fieldfighter = null;
		endprocedures = new java.util.LinkedList<xbean.BattleEndProcedure>();
		roundresultitem = new java.util.HashMap<Integer, xbean.RoundResultItems>();
		itemtimelist = new java.util.LinkedList<Integer>();
		reliveacts = new java.util.ArrayList<fire.pb.battle.operate.Operate>();
		battledeads = new java.util.HashMap<Long, Integer>();
		params = new java.util.HashMap<Integer, Integer>();
		quitteamroleids = new java.util.LinkedList<Long>();
		absentteamroleids = new java.util.LinkedList<Long>();
		teamidlist = new java.util.LinkedList<Long>();
	}

	public BattleInfo() {
		this(0, null, null);
	}

	public BattleInfo(BattleInfo _o_) {
		this(_o_, null, null);
	}

	BattleInfo(xbean.BattleInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		throw new UnsupportedOperationException();
	}

	@Override
	public xbean.BattleInfo copy() {
		_xdb_verify_unsafe_();
		return new BattleInfo(this);
	}

	@Override
	public xbean.BattleInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BattleInfo toBean() {
		_xdb_verify_unsafe_();
		return new BattleInfo(this); // same as copy()
	}

	@Override
	public xbean.BattleInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.BattleInfo toBeanIf() {
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
	public int getBackground() { // 战斗地图
		_xdb_verify_unsafe_();
		return background;
	}

	@Override
	public int getBgm() { // 背景音乐
		_xdb_verify_unsafe_();
		return bgm;
	}

	@Override
	public int getEnvironment() { // 战场环境
		_xdb_verify_unsafe_();
		return environment;
	}

	@Override
	public int getGmactiontimetimes() { // 时间倍率应用除以10.0，用于调试，gm使用。
		_xdb_verify_unsafe_();
		return gmactiontimetimes;
	}

	@Override
	public int getEnvchangefrom() { // 环境改变来源（0 = 场外， 1 = 场内）
		_xdb_verify_unsafe_();
		return envchangefrom;
	}

	@Override
	public int getPhase() { // 战斗的阶段
		_xdb_verify_unsafe_();
		return phase;
	}

	@Override
	public int getRound() { // 战斗当前的回合数
		_xdb_verify_unsafe_();
		return round;
	}

	@Override
	public boolean getCanrunaway() { // 本场战斗是否能逃跑
		_xdb_verify_unsafe_();
		return canrunaway;
	}

	@Override
	public int getAutokey() { // 战斗者key，fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
		_xdb_verify_unsafe_();
		return autokey;
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
	public int getBabyflag() { // 是否是含有宝宝的战斗 0不是     1是含有宝宝的战斗
		_xdb_verify_unsafe_();
		return babyflag;
	}

	@Override
	public int getBabybagfull() { // 宠物栏是否满  0不满    1满
		_xdb_verify_unsafe_();
		return babybagfull;
	}

	@Override
	public long getHostroleid() { // 发起战斗的主角或者队长的id
		_xdb_verify_unsafe_();
		return hostroleid;
	}

	@Override
	public long getMonsterid() { // 明雷怪npckey
		_xdb_verify_unsafe_();
		return monsterid;
	}

	@Override
	public int getMonstertype() { // 怪物类型
		_xdb_verify_unsafe_();
		return monstertype;
	}

	@Override
	public java.util.List<fire.pb.battle.BattleEndHandler> getBattleendhandlers() { // 处理战斗结束的handler
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "battleendhandlers"), battleendhandlers);
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
	public int getHostformlevel() { // 主方光环等级
		_xdb_verify_unsafe_();
		return hostformlevel;
	}

	@Override
	public int getGuestformlevel() { // 客方光环等级
		_xdb_verify_unsafe_();
		return guestformlevel;
	}

	@Override
	public float getHostformeffcet() { // 主对客方光环压制效果
		_xdb_verify_unsafe_();
		return hostformeffcet;
	}

	@Override
	public java.util.List<fire.pb.battle.FighterInfo> getFigterinfoonbegin() { // 出战斗时的战斗者信息
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "figterinfoonbegin"), figterinfoonbegin);
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
		BattleInfo _o_ = this;
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
		BattleInfo _o_ = this;
		fighters = new java.util.TreeMap<Integer, xbean.Fighter>();
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.fighters.entrySet())
			fighters.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
		return fighters;
	}

	@Override
	public java.util.Map<Integer, fire.pb.battle.Fighter> getFighterobjects() { // 包装好的Fighter类，战斗开始时直接创建完，不用每次用的时候再new,注意使用前要锁rolelock
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "fighterobjects"), fighterobjects);
	}

	@Override
	public java.util.Set<Integer> getSkipeddeadfighters() { // 本回合因为死亡而未出手的战斗者，出手结束清除
		_xdb_verify_unsafe_();
		return mkdb.Logs.logSet(new mkdb.LogKey(this, "skipeddeadfighters"), skipeddeadfighters);
	}

	public java.util.Set<Integer> getSkipeddeadfightersAsData() { // 本回合因为死亡而未出手的战斗者，出手结束清除
		_xdb_verify_unsafe_();
		java.util.Set<Integer> skipeddeadfighters;
		BattleInfo _o_ = this;
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
		BattleInfo _o_ = this;
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
		BattleInfo _o_ = this;
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
		BattleInfo _o_ = this;
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
		BattleInfo _o_ = this;
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
		BattleInfo _o_ = this;
		watchers = new java.util.TreeMap<Integer, xbean.Fighter>();
		for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.watchers.entrySet())
			watchers.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
		return watchers;
	}

	@Override
	public java.util.List<Long> getOfflineroleids() { // 战斗中掉线的角色，不能发送协议
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "offlineroleids"), offlineroleids);
	}

	public java.util.List<Long> getOfflineroleidsAsData() { // 战斗中掉线的角色，不能发送协议
		_xdb_verify_unsafe_();
		java.util.List<Long> offlineroleids;
		BattleInfo _o_ = this;
		offlineroleids = new java.util.LinkedList<Long>();
		offlineroleids.addAll(_o_.offlineroleids);
		return offlineroleids;
	}

	@Override
	public long getBattlebegintime() { // 战斗开始的时间
		_xdb_verify_unsafe_();
		return battlebegintime;
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
		BattleInfo _o_ = this;
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
		BattleInfo _o_ = this;
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
		BattleInfo _o_ = this;
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
		BattleInfo _o_ = this;
		protectormap = new java.util.HashMap<Integer, xbean.Protectors>();
		for (java.util.Map.Entry<Integer, xbean.Protectors> _e_ : _o_.protectormap.entrySet())
			protectormap.put(_e_.getKey(), new Protectors.Data(_e_.getValue()));
		return protectormap;
	}

	@Override
	public java.util.concurrent.ScheduledFuture<?> getTimeout() { // 超时检测
		_xdb_verify_unsafe_();
		return timeout;
	}

	@Override
	public java.util.List<xbean.BattleAI> getBattleais() { // 挂在战场上的AI
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "battleais"), battleais);
	}

	public java.util.List<xbean.BattleAI> getBattleaisAsData() { // 挂在战场上的AI
		_xdb_verify_unsafe_();
		java.util.List<xbean.BattleAI> battleais;
		BattleInfo _o_ = this;
		battleais = new java.util.LinkedList<xbean.BattleAI>();
		for (xbean.BattleAI _v_ : _o_.battleais)
			battleais.add(new BattleAI.Data(_v_));
		return battleais;
	}

	@Override
	public fire.script.FightJSEngine getEngine() { // 用于本场战斗的JS引擎
		_xdb_verify_unsafe_();
		return engine;
	}

	@Override
	public java.util.Map<Integer, fire.pb.battle.ActionChangedAttrs> getFighterchangedattrs() { // 战斗者每个回合改变的属性,key=fighterId
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "fighterchangedattrs"), fighterchangedattrs);
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
		BattleInfo _o_ = this;
		battledatas = new java.util.HashMap<Integer, Float>();
		for (java.util.Map.Entry<Integer, Float> _e_ : _o_.battledatas.entrySet())
			battledatas.put(_e_.getKey(), _e_.getValue());
		return battledatas;
	}

	@Override
	public fire.pb.battle.Fighter getFieldfighter() { // 战场虚拟的Fighter，为战场技能而用，虚拟数据的来源是此战斗中的一个怪
		_xdb_verify_unsafe_();
		return fieldfighter;
	}

	@Override
	public java.util.List<xbean.BattleEndProcedure> getEndprocedures() { // 战斗者出战斗时执行的Procedure
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "endprocedures"), endprocedures);
	}

	@Override
	public java.util.Map<Integer, xbean.RoundResultItems> getRoundresultitem() { // 整场战斗的demo
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "roundresultitem"), roundresultitem);
	}

	@Override
	public java.util.List<Integer> getItemtimelist() { // 整场战斗的demo时间
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "itemtimelist"), itemtimelist);
	}

	public java.util.List<Integer> getItemtimelistAsData() { // 整场战斗的demo时间
		_xdb_verify_unsafe_();
		java.util.List<Integer> itemtimelist;
		BattleInfo _o_ = this;
		itemtimelist = new java.util.LinkedList<Integer>();
		itemtimelist.addAll(_o_.itemtimelist);
		return itemtimelist;
	}

	@Override
	public java.util.List<fire.pb.battle.operate.Operate> getReliveacts() { // 复活后要出手的行动
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "reliveacts"), reliveacts);
	}

	@Override
	public java.util.Map<Long, Integer> getBattledeads() { // 第几回合死掉的角色
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "battledeads"), battledeads);
	}

	@Override
	public java.util.Map<Long, Integer> getBattledeadsAsData() { // 第几回合死掉的角色
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> battledeads;
		BattleInfo _o_ = this;
		battledeads = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.battledeads.entrySet())
			battledeads.put(_e_.getKey(), _e_.getValue());
		return battledeads;
	}

	@Override
	public int getBattlemaxround() { // 战斗的最大回合数 如果不设置默认为200回合
		_xdb_verify_unsafe_();
		return battlemaxround;
	}

	@Override
	public java.util.Map<Integer, Integer> getParams() { // 与战斗有关的一些参数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "params"), params);
	}

	@Override
	public java.util.Map<Integer, Integer> getParamsAsData() { // 与战斗有关的一些参数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> params;
		BattleInfo _o_ = this;
		params = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.params.entrySet())
			params.put(_e_.getKey(), _e_.getValue());
		return params;
	}

	@Override
	public java.util.List<Long> getQuitteamroleids() { // 战斗中有退队角色,战后自动退队 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "quitteamroleids"), quitteamroleids);
	}

	public java.util.List<Long> getQuitteamroleidsAsData() { // 战斗中有退队角色,战后自动退队 by changhao
		_xdb_verify_unsafe_();
		java.util.List<Long> quitteamroleids;
		BattleInfo _o_ = this;
		quitteamroleids = new java.util.LinkedList<Long>();
		quitteamroleids.addAll(_o_.quitteamroleids);
		return quitteamroleids;
	}

	@Override
	public java.util.List<Long> getAbsentteamroleids() { // 战斗中有暂离角色,战后自动暂离 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "absentteamroleids"), absentteamroleids);
	}

	public java.util.List<Long> getAbsentteamroleidsAsData() { // 战斗中有暂离角色,战后自动暂离 by changhao
		_xdb_verify_unsafe_();
		java.util.List<Long> absentteamroleids;
		BattleInfo _o_ = this;
		absentteamroleids = new java.util.LinkedList<Long>();
		absentteamroleids.addAll(_o_.absentteamroleids);
		return absentteamroleids;
	}

	@Override
	public java.util.List<Long> getTeamidlist() { // 战斗里所有队伍id by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "teamidlist"), teamidlist);
	}

	public java.util.List<Long> getTeamidlistAsData() { // 战斗里所有队伍id by changhao
		_xdb_verify_unsafe_();
		java.util.List<Long> teamidlist;
		BattleInfo _o_ = this;
		teamidlist = new java.util.LinkedList<Long>();
		teamidlist.addAll(_o_.teamidlist);
		return teamidlist;
	}

	@Override
	public int getLivedieteam() { // 是否是组队生死战0不是    1是
		_xdb_verify_unsafe_();
		return livedieteam;
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
	public void setBackground(int _v_) { // 战斗地图
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "background") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, background) {
					public void rollback() { background = _xdb_saved; }
				};}});
		background = _v_;
	}

	@Override
	public void setBgm(int _v_) { // 背景音乐
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bgm") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bgm) {
					public void rollback() { bgm = _xdb_saved; }
				};}});
		bgm = _v_;
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
	public void setGmactiontimetimes(int _v_) { // 时间倍率应用除以10.0，用于调试，gm使用。
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "gmactiontimetimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, gmactiontimetimes) {
					public void rollback() { gmactiontimetimes = _xdb_saved; }
				};}});
		gmactiontimetimes = _v_;
	}

	@Override
	public void setEnvchangefrom(int _v_) { // 环境改变来源（0 = 场外， 1 = 场内）
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "envchangefrom") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, envchangefrom) {
					public void rollback() { envchangefrom = _xdb_saved; }
				};}});
		envchangefrom = _v_;
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
	public void setCanrunaway(boolean _v_) { // 本场战斗是否能逃跑
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "canrunaway") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, canrunaway) {
					public void rollback() { canrunaway = _xdb_saved; }
				};}});
		canrunaway = _v_;
	}

	@Override
	public void setAutokey(int _v_) { // 战斗者key，fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "autokey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, autokey) {
					public void rollback() { autokey = _xdb_saved; }
				};}});
		autokey = _v_;
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
	public void setBabyflag(int _v_) { // 是否是含有宝宝的战斗 0不是     1是含有宝宝的战斗
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "babyflag") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, babyflag) {
					public void rollback() { babyflag = _xdb_saved; }
				};}});
		babyflag = _v_;
	}

	@Override
	public void setBabybagfull(int _v_) { // 宠物栏是否满  0不满    1满
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "babybagfull") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, babybagfull) {
					public void rollback() { babybagfull = _xdb_saved; }
				};}});
		babybagfull = _v_;
	}

	@Override
	public void setHostroleid(long _v_) { // 发起战斗的主角或者队长的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "hostroleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, hostroleid) {
					public void rollback() { hostroleid = _xdb_saved; }
				};}});
		hostroleid = _v_;
	}

	@Override
	public void setMonsterid(long _v_) { // 明雷怪npckey
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "monsterid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, monsterid) {
					public void rollback() { monsterid = _xdb_saved; }
				};}});
		monsterid = _v_;
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
	public void setHostformlevel(int _v_) { // 主方光环等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "hostformlevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, hostformlevel) {
					public void rollback() { hostformlevel = _xdb_saved; }
				};}});
		hostformlevel = _v_;
	}

	@Override
	public void setGuestformlevel(int _v_) { // 客方光环等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "guestformlevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, guestformlevel) {
					public void rollback() { guestformlevel = _xdb_saved; }
				};}});
		guestformlevel = _v_;
	}

	@Override
	public void setHostformeffcet(float _v_) { // 主对客方光环压制效果
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "hostformeffcet") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogFloat(this, hostformeffcet) {
					public void rollback() { hostformeffcet = _xdb_saved; }
				};}});
		hostformeffcet = _v_;
	}

	@Override
	public void setBattlebegintime(long _v_) { // 战斗开始的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "battlebegintime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, battlebegintime) {
					public void rollback() { battlebegintime = _xdb_saved; }
				};}});
		battlebegintime = _v_;
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
	public void setTimeout(java.util.concurrent.ScheduledFuture<?> _v_) { // 超时检测
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "timeout") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<java.util.concurrent.ScheduledFuture<?>>(this, timeout) {
					public void rollback() { timeout = _xdb_saved; }
			}; }});
		timeout = _v_;
	}

	@Override
	public void setEngine(fire.script.FightJSEngine _v_) { // 用于本场战斗的JS引擎
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "engine") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<fire.script.FightJSEngine>(this, engine) {
					public void rollback() { engine = _xdb_saved; }
			}; }});
		engine = _v_;
	}

	@Override
	public void setFieldfighter(fire.pb.battle.Fighter _v_) { // 战场虚拟的Fighter，为战场技能而用，虚拟数据的来源是此战斗中的一个怪
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fieldfighter") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<fire.pb.battle.Fighter>(this, fieldfighter) {
					public void rollback() { fieldfighter = _xdb_saved; }
			}; }});
		fieldfighter = _v_;
	}

	@Override
	public void setBattlemaxround(int _v_) { // 战斗的最大回合数 如果不设置默认为200回合
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "battlemaxround") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, battlemaxround) {
					public void rollback() { battlemaxround = _xdb_saved; }
				};}});
		battlemaxround = _v_;
	}

	@Override
	public void setLivedieteam(int _v_) { // 是否是组队生死战0不是    1是
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "livedieteam") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, livedieteam) {
					public void rollback() { livedieteam = _xdb_saved; }
				};}});
		livedieteam = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		BattleInfo _o_ = null;
		if ( _o1_ instanceof BattleInfo ) _o_ = (BattleInfo)_o1_;
		else if ( _o1_ instanceof BattleInfo.Const ) _o_ = ((BattleInfo.Const)_o1_).nThis();
		else return false;
		if (battleid != _o_.battleid) return false;
		if (configid != _o_.configid) return false;
		if (background != _o_.background) return false;
		if (bgm != _o_.bgm) return false;
		if (environment != _o_.environment) return false;
		if (gmactiontimetimes != _o_.gmactiontimetimes) return false;
		if (envchangefrom != _o_.envchangefrom) return false;
		if (phase != _o_.phase) return false;
		if (round != _o_.round) return false;
		if (canrunaway != _o_.canrunaway) return false;
		if (autokey != _o_.autokey) return false;
		if (battletype != _o_.battletype) return false;
		if (areatype != _o_.areatype) return false;
		if (areaconf != _o_.areaconf) return false;
		if (babyflag != _o_.babyflag) return false;
		if (babybagfull != _o_.babybagfull) return false;
		if (hostroleid != _o_.hostroleid) return false;
		if (monsterid != _o_.monsterid) return false;
		if (monstertype != _o_.monstertype) return false;
		if (!battleendhandlers.equals(_o_.battleendhandlers)) return false;
		if (hostform != _o_.hostform) return false;
		if (guestform != _o_.guestform) return false;
		if (hostformlevel != _o_.hostformlevel) return false;
		if (guestformlevel != _o_.guestformlevel) return false;
		if (hostformeffcet != _o_.hostformeffcet) return false;
		if (!figterinfoonbegin.equals(_o_.figterinfoonbegin)) return false;
		if (!roleids.equals(_o_.roleids)) return false;
		if (!fighters.equals(_o_.fighters)) return false;
		if (!fighterobjects.equals(_o_.fighterobjects)) return false;
		if (!skipeddeadfighters.equals(_o_.skipeddeadfighters)) return false;
		if (!monsters.equals(_o_.monsters)) return false;
		if (!failedmonsters.equals(_o_.failedmonsters)) return false;
		if (!inifighters.equals(_o_.inifighters)) return false;
		if (!audience.equals(_o_.audience)) return false;
		if (!watchers.equals(_o_.watchers)) return false;
		if (!offlineroleids.equals(_o_.offlineroleids)) return false;
		if (battlebegintime != _o_.battlebegintime) return false;
		if (roundtime != _o_.roundtime) return false;
		if (scriptplaytime != _o_.scriptplaytime) return false;
		if (!decisions.equals(_o_.decisions)) return false;
		if (!feedbacks.equals(_o_.feedbacks)) return false;
		if (battleresult != _o_.battleresult) return false;
		if (!roundresult.equals(_o_.roundresult)) return false;
		if (!protectormap.equals(_o_.protectormap)) return false;
		if ((null == timeout && null != _o_.timeout) || (null != timeout && null == _o_.timeout) || (null != timeout && null != _o_.timeout && !timeout.equals(_o_.timeout))) return false;
		if (!battleais.equals(_o_.battleais)) return false;
		if ((null == engine && null != _o_.engine) || (null != engine && null == _o_.engine) || (null != engine && null != _o_.engine && !engine.equals(_o_.engine))) return false;
		if (!fighterchangedattrs.equals(_o_.fighterchangedattrs)) return false;
		if (!battledatas.equals(_o_.battledatas)) return false;
		if ((null == fieldfighter && null != _o_.fieldfighter) || (null != fieldfighter && null == _o_.fieldfighter) || (null != fieldfighter && null != _o_.fieldfighter && !fieldfighter.equals(_o_.fieldfighter))) return false;
		if (!endprocedures.equals(_o_.endprocedures)) return false;
		if (!roundresultitem.equals(_o_.roundresultitem)) return false;
		if (!itemtimelist.equals(_o_.itemtimelist)) return false;
		if (!reliveacts.equals(_o_.reliveacts)) return false;
		if (!battledeads.equals(_o_.battledeads)) return false;
		if (battlemaxround != _o_.battlemaxround) return false;
		if (!params.equals(_o_.params)) return false;
		if (!quitteamroleids.equals(_o_.quitteamroleids)) return false;
		if (!absentteamroleids.equals(_o_.absentteamroleids)) return false;
		if (!teamidlist.equals(_o_.teamidlist)) return false;
		if (livedieteam != _o_.livedieteam) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += battleid;
		_h_ += configid;
		_h_ += background;
		_h_ += bgm;
		_h_ += environment;
		_h_ += gmactiontimetimes;
		_h_ += envchangefrom;
		_h_ += phase;
		_h_ += round;
		_h_ += canrunaway ? 1231 : 1237;
		_h_ += autokey;
		_h_ += battletype;
		_h_ += areatype;
		_h_ += areaconf;
		_h_ += babyflag;
		_h_ += babybagfull;
		_h_ += hostroleid;
		_h_ += monsterid;
		_h_ += monstertype;
		_h_ += battleendhandlers.hashCode();
		_h_ += hostform;
		_h_ += guestform;
		_h_ += hostformlevel;
		_h_ += guestformlevel;
		_h_ += hostformeffcet;
		_h_ += figterinfoonbegin.hashCode();
		_h_ += roleids.hashCode();
		_h_ += fighters.hashCode();
		_h_ += fighterobjects.hashCode();
		_h_ += skipeddeadfighters.hashCode();
		_h_ += monsters.hashCode();
		_h_ += failedmonsters.hashCode();
		_h_ += inifighters.hashCode();
		_h_ += audience.hashCode();
		_h_ += watchers.hashCode();
		_h_ += offlineroleids.hashCode();
		_h_ += battlebegintime;
		_h_ += roundtime;
		_h_ += scriptplaytime;
		_h_ += decisions.hashCode();
		_h_ += feedbacks.hashCode();
		_h_ += battleresult;
		_h_ += roundresult.hashCode();
		_h_ += protectormap.hashCode();
		_h_ += (timeout == null ? 0 : timeout.hashCode());
		_h_ += battleais.hashCode();
		_h_ += (engine == null ? 0 : engine.hashCode());
		_h_ += fighterchangedattrs.hashCode();
		_h_ += battledatas.hashCode();
		_h_ += (fieldfighter == null ? 0 : fieldfighter.hashCode());
		_h_ += endprocedures.hashCode();
		_h_ += roundresultitem.hashCode();
		_h_ += itemtimelist.hashCode();
		_h_ += reliveacts.hashCode();
		_h_ += battledeads.hashCode();
		_h_ += battlemaxround;
		_h_ += params.hashCode();
		_h_ += quitteamroleids.hashCode();
		_h_ += absentteamroleids.hashCode();
		_h_ += teamidlist.hashCode();
		_h_ += livedieteam;
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
		_sb_.append(background);
		_sb_.append(",");
		_sb_.append(bgm);
		_sb_.append(",");
		_sb_.append(environment);
		_sb_.append(",");
		_sb_.append(gmactiontimetimes);
		_sb_.append(",");
		_sb_.append(envchangefrom);
		_sb_.append(",");
		_sb_.append(phase);
		_sb_.append(",");
		_sb_.append(round);
		_sb_.append(",");
		_sb_.append(canrunaway);
		_sb_.append(",");
		_sb_.append(autokey);
		_sb_.append(",");
		_sb_.append(battletype);
		_sb_.append(",");
		_sb_.append(areatype);
		_sb_.append(",");
		_sb_.append(areaconf);
		_sb_.append(",");
		_sb_.append(babyflag);
		_sb_.append(",");
		_sb_.append(babybagfull);
		_sb_.append(",");
		_sb_.append(hostroleid);
		_sb_.append(",");
		_sb_.append(monsterid);
		_sb_.append(",");
		_sb_.append(monstertype);
		_sb_.append(",");
		_sb_.append(battleendhandlers);
		_sb_.append(",");
		_sb_.append(hostform);
		_sb_.append(",");
		_sb_.append(guestform);
		_sb_.append(",");
		_sb_.append(hostformlevel);
		_sb_.append(",");
		_sb_.append(guestformlevel);
		_sb_.append(",");
		_sb_.append(hostformeffcet);
		_sb_.append(",");
		_sb_.append(figterinfoonbegin);
		_sb_.append(",");
		_sb_.append(roleids);
		_sb_.append(",");
		_sb_.append(fighters);
		_sb_.append(",");
		_sb_.append(fighterobjects);
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
		_sb_.append(offlineroleids);
		_sb_.append(",");
		_sb_.append(battlebegintime);
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
		_sb_.append(timeout);
		_sb_.append(",");
		_sb_.append(battleais);
		_sb_.append(",");
		_sb_.append(engine);
		_sb_.append(",");
		_sb_.append(fighterchangedattrs);
		_sb_.append(",");
		_sb_.append(battledatas);
		_sb_.append(",");
		_sb_.append(fieldfighter);
		_sb_.append(",");
		_sb_.append(endprocedures);
		_sb_.append(",");
		_sb_.append(roundresultitem);
		_sb_.append(",");
		_sb_.append(itemtimelist);
		_sb_.append(",");
		_sb_.append(reliveacts);
		_sb_.append(",");
		_sb_.append(battledeads);
		_sb_.append(",");
		_sb_.append(battlemaxround);
		_sb_.append(",");
		_sb_.append(params);
		_sb_.append(",");
		_sb_.append(quitteamroleids);
		_sb_.append(",");
		_sb_.append(absentteamroleids);
		_sb_.append(",");
		_sb_.append(teamidlist);
		_sb_.append(",");
		_sb_.append(livedieteam);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("configid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("background"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bgm"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("environment"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("gmactiontimetimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("envchangefrom"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("phase"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("round"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("canrunaway"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("autokey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battletype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("areatype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("areaconf"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("babyflag"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("babybagfull"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("hostroleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("monsterid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("monstertype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battleendhandlers"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("hostform"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("guestform"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("hostformlevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("guestformlevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("hostformeffcet"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("figterinfoonbegin"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("roleids"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("fighters"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("fighterobjects"));
		lb.add(new mkdb.logs.ListenableSet().setVarName("skipeddeadfighters"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("monsters"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("failedmonsters"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("inifighters"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("audience"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("watchers"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("offlineroleids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battlebegintime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roundtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("scriptplaytime"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("decisions"));
		lb.add(new mkdb.logs.ListenableSet().setVarName("feedbacks"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battleresult"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("roundresult"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("protectormap"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("timeout"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battleais"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("engine"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("fighterchangedattrs"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("battledatas"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fieldfighter"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("endprocedures"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("roundresultitem"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemtimelist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("reliveacts"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("battledeads"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battlemaxround"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("params"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("quitteamroleids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("absentteamroleids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("teamidlist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("livedieteam"));
		return lb;
	}

	private class Const implements xbean.BattleInfo {
		BattleInfo nThis() {
			return BattleInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BattleInfo copy() {
			return BattleInfo.this.copy();
		}

		@Override
		public xbean.BattleInfo toData() {
			return BattleInfo.this.toData();
		}

		public xbean.BattleInfo toBean() {
			return BattleInfo.this.toBean();
		}

		@Override
		public xbean.BattleInfo toDataIf() {
			return BattleInfo.this.toDataIf();
		}

		public xbean.BattleInfo toBeanIf() {
			return BattleInfo.this.toBeanIf();
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
		public int getBackground() { // 战斗地图
			_xdb_verify_unsafe_();
			return background;
		}

		@Override
		public int getBgm() { // 背景音乐
			_xdb_verify_unsafe_();
			return bgm;
		}

		@Override
		public int getEnvironment() { // 战场环境
			_xdb_verify_unsafe_();
			return environment;
		}

		@Override
		public int getGmactiontimetimes() { // 时间倍率应用除以10.0，用于调试，gm使用。
			_xdb_verify_unsafe_();
			return gmactiontimetimes;
		}

		@Override
		public int getEnvchangefrom() { // 环境改变来源（0 = 场外， 1 = 场内）
			_xdb_verify_unsafe_();
			return envchangefrom;
		}

		@Override
		public int getPhase() { // 战斗的阶段
			_xdb_verify_unsafe_();
			return phase;
		}

		@Override
		public int getRound() { // 战斗当前的回合数
			_xdb_verify_unsafe_();
			return round;
		}

		@Override
		public boolean getCanrunaway() { // 本场战斗是否能逃跑
			_xdb_verify_unsafe_();
			return canrunaway;
		}

		@Override
		public int getAutokey() { // 战斗者key，fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
			_xdb_verify_unsafe_();
			return autokey;
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
		public int getBabyflag() { // 是否是含有宝宝的战斗 0不是     1是含有宝宝的战斗
			_xdb_verify_unsafe_();
			return babyflag;
		}

		@Override
		public int getBabybagfull() { // 宠物栏是否满  0不满    1满
			_xdb_verify_unsafe_();
			return babybagfull;
		}

		@Override
		public long getHostroleid() { // 发起战斗的主角或者队长的id
			_xdb_verify_unsafe_();
			return hostroleid;
		}

		@Override
		public long getMonsterid() { // 明雷怪npckey
			_xdb_verify_unsafe_();
			return monsterid;
		}

		@Override
		public int getMonstertype() { // 怪物类型
			_xdb_verify_unsafe_();
			return monstertype;
		}

		@Override
		public java.util.List<fire.pb.battle.BattleEndHandler> getBattleendhandlers() { // 处理战斗结束的handler
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(battleendhandlers);
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
		public int getHostformlevel() { // 主方光环等级
			_xdb_verify_unsafe_();
			return hostformlevel;
		}

		@Override
		public int getGuestformlevel() { // 客方光环等级
			_xdb_verify_unsafe_();
			return guestformlevel;
		}

		@Override
		public float getHostformeffcet() { // 主对客方光环压制效果
			_xdb_verify_unsafe_();
			return hostformeffcet;
		}

		@Override
		public java.util.List<fire.pb.battle.FighterInfo> getFigterinfoonbegin() { // 出战斗时的战斗者信息
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(figterinfoonbegin);
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
			BattleInfo _o_ = BattleInfo.this;
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
			BattleInfo _o_ = BattleInfo.this;
			fighters = new java.util.TreeMap<Integer, xbean.Fighter>();
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.fighters.entrySet())
				fighters.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
			return fighters;
		}

		@Override
		public java.util.Map<Integer, fire.pb.battle.Fighter> getFighterobjects() { // 包装好的Fighter类，战斗开始时直接创建完，不用每次用的时候再new,注意使用前要锁rolelock
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(fighterobjects);
		}

		@Override
		public java.util.Set<Integer> getSkipeddeadfighters() { // 本回合因为死亡而未出手的战斗者，出手结束清除
			_xdb_verify_unsafe_();
			return mkdb.Consts.constSet(skipeddeadfighters);
		}

		public java.util.Set<Integer> getSkipeddeadfightersAsData() { // 本回合因为死亡而未出手的战斗者，出手结束清除
			_xdb_verify_unsafe_();
			java.util.Set<Integer> skipeddeadfighters;
			BattleInfo _o_ = BattleInfo.this;
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
			BattleInfo _o_ = BattleInfo.this;
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
			BattleInfo _o_ = BattleInfo.this;
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
			BattleInfo _o_ = BattleInfo.this;
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
			BattleInfo _o_ = BattleInfo.this;
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
			BattleInfo _o_ = BattleInfo.this;
			watchers = new java.util.TreeMap<Integer, xbean.Fighter>();
			for (java.util.Map.Entry<Integer, xbean.Fighter> _e_ : _o_.watchers.entrySet())
				watchers.put(_e_.getKey(), new Fighter.Data(_e_.getValue()));
			return watchers;
		}

		@Override
		public java.util.List<Long> getOfflineroleids() { // 战斗中掉线的角色，不能发送协议
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(offlineroleids);
		}

		public java.util.List<Long> getOfflineroleidsAsData() { // 战斗中掉线的角色，不能发送协议
			_xdb_verify_unsafe_();
			java.util.List<Long> offlineroleids;
			BattleInfo _o_ = BattleInfo.this;
		offlineroleids = new java.util.LinkedList<Long>();
		offlineroleids.addAll(_o_.offlineroleids);
			return offlineroleids;
		}

		@Override
		public long getBattlebegintime() { // 战斗开始的时间
			_xdb_verify_unsafe_();
			return battlebegintime;
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
			BattleInfo _o_ = BattleInfo.this;
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
			BattleInfo _o_ = BattleInfo.this;
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
			BattleInfo _o_ = BattleInfo.this;
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
			BattleInfo _o_ = BattleInfo.this;
			protectormap = new java.util.HashMap<Integer, xbean.Protectors>();
			for (java.util.Map.Entry<Integer, xbean.Protectors> _e_ : _o_.protectormap.entrySet())
				protectormap.put(_e_.getKey(), new Protectors.Data(_e_.getValue()));
			return protectormap;
		}

		@Override
		public java.util.concurrent.ScheduledFuture<?> getTimeout() { // 超时检测
			_xdb_verify_unsafe_();
			return timeout;
		}

		@Override
		public java.util.List<xbean.BattleAI> getBattleais() { // 挂在战场上的AI
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(battleais);
		}

		public java.util.List<xbean.BattleAI> getBattleaisAsData() { // 挂在战场上的AI
			_xdb_verify_unsafe_();
			java.util.List<xbean.BattleAI> battleais;
			BattleInfo _o_ = BattleInfo.this;
		battleais = new java.util.LinkedList<xbean.BattleAI>();
		for (xbean.BattleAI _v_ : _o_.battleais)
			battleais.add(new BattleAI.Data(_v_));
			return battleais;
		}

		@Override
		public fire.script.FightJSEngine getEngine() { // 用于本场战斗的JS引擎
			_xdb_verify_unsafe_();
			return engine;
		}

		@Override
		public java.util.Map<Integer, fire.pb.battle.ActionChangedAttrs> getFighterchangedattrs() { // 战斗者每个回合改变的属性,key=fighterId
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(fighterchangedattrs);
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
			BattleInfo _o_ = BattleInfo.this;
			battledatas = new java.util.HashMap<Integer, Float>();
			for (java.util.Map.Entry<Integer, Float> _e_ : _o_.battledatas.entrySet())
				battledatas.put(_e_.getKey(), _e_.getValue());
			return battledatas;
		}

		@Override
		public fire.pb.battle.Fighter getFieldfighter() { // 战场虚拟的Fighter，为战场技能而用，虚拟数据的来源是此战斗中的一个怪
			_xdb_verify_unsafe_();
			return fieldfighter;
		}

		@Override
		public java.util.List<xbean.BattleEndProcedure> getEndprocedures() { // 战斗者出战斗时执行的Procedure
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(endprocedures);
		}

		@Override
		public java.util.Map<Integer, xbean.RoundResultItems> getRoundresultitem() { // 整场战斗的demo
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(roundresultitem);
		}

		@Override
		public java.util.List<Integer> getItemtimelist() { // 整场战斗的demo时间
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(itemtimelist);
		}

		public java.util.List<Integer> getItemtimelistAsData() { // 整场战斗的demo时间
			_xdb_verify_unsafe_();
			java.util.List<Integer> itemtimelist;
			BattleInfo _o_ = BattleInfo.this;
		itemtimelist = new java.util.LinkedList<Integer>();
		itemtimelist.addAll(_o_.itemtimelist);
			return itemtimelist;
		}

		@Override
		public java.util.List<fire.pb.battle.operate.Operate> getReliveacts() { // 复活后要出手的行动
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(reliveacts);
		}

		@Override
		public java.util.Map<Long, Integer> getBattledeads() { // 第几回合死掉的角色
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(battledeads);
		}

		@Override
		public java.util.Map<Long, Integer> getBattledeadsAsData() { // 第几回合死掉的角色
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> battledeads;
			BattleInfo _o_ = BattleInfo.this;
			battledeads = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.battledeads.entrySet())
				battledeads.put(_e_.getKey(), _e_.getValue());
			return battledeads;
		}

		@Override
		public int getBattlemaxround() { // 战斗的最大回合数 如果不设置默认为200回合
			_xdb_verify_unsafe_();
			return battlemaxround;
		}

		@Override
		public java.util.Map<Integer, Integer> getParams() { // 与战斗有关的一些参数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(params);
		}

		@Override
		public java.util.Map<Integer, Integer> getParamsAsData() { // 与战斗有关的一些参数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> params;
			BattleInfo _o_ = BattleInfo.this;
			params = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.params.entrySet())
				params.put(_e_.getKey(), _e_.getValue());
			return params;
		}

		@Override
		public java.util.List<Long> getQuitteamroleids() { // 战斗中有退队角色,战后自动退队 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(quitteamroleids);
		}

		public java.util.List<Long> getQuitteamroleidsAsData() { // 战斗中有退队角色,战后自动退队 by changhao
			_xdb_verify_unsafe_();
			java.util.List<Long> quitteamroleids;
			BattleInfo _o_ = BattleInfo.this;
		quitteamroleids = new java.util.LinkedList<Long>();
		quitteamroleids.addAll(_o_.quitteamroleids);
			return quitteamroleids;
		}

		@Override
		public java.util.List<Long> getAbsentteamroleids() { // 战斗中有暂离角色,战后自动暂离 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(absentteamroleids);
		}

		public java.util.List<Long> getAbsentteamroleidsAsData() { // 战斗中有暂离角色,战后自动暂离 by changhao
			_xdb_verify_unsafe_();
			java.util.List<Long> absentteamroleids;
			BattleInfo _o_ = BattleInfo.this;
		absentteamroleids = new java.util.LinkedList<Long>();
		absentteamroleids.addAll(_o_.absentteamroleids);
			return absentteamroleids;
		}

		@Override
		public java.util.List<Long> getTeamidlist() { // 战斗里所有队伍id by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(teamidlist);
		}

		public java.util.List<Long> getTeamidlistAsData() { // 战斗里所有队伍id by changhao
			_xdb_verify_unsafe_();
			java.util.List<Long> teamidlist;
			BattleInfo _o_ = BattleInfo.this;
		teamidlist = new java.util.LinkedList<Long>();
		teamidlist.addAll(_o_.teamidlist);
			return teamidlist;
		}

		@Override
		public int getLivedieteam() { // 是否是组队生死战0不是    1是
			_xdb_verify_unsafe_();
			return livedieteam;
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
		public void setBackground(int _v_) { // 战斗地图
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBgm(int _v_) { // 背景音乐
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEnvironment(int _v_) { // 战场环境
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGmactiontimetimes(int _v_) { // 时间倍率应用除以10.0，用于调试，gm使用。
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEnvchangefrom(int _v_) { // 环境改变来源（0 = 场外， 1 = 场内）
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPhase(int _v_) { // 战斗的阶段
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRound(int _v_) { // 战斗当前的回合数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCanrunaway(boolean _v_) { // 本场战斗是否能逃跑
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAutokey(int _v_) { // 战斗者key，fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
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
		public void setBabyflag(int _v_) { // 是否是含有宝宝的战斗 0不是     1是含有宝宝的战斗
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBabybagfull(int _v_) { // 宠物栏是否满  0不满    1满
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHostroleid(long _v_) { // 发起战斗的主角或者队长的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMonsterid(long _v_) { // 明雷怪npckey
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMonstertype(int _v_) { // 怪物类型
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
		public void setHostformlevel(int _v_) { // 主方光环等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGuestformlevel(int _v_) { // 客方光环等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHostformeffcet(float _v_) { // 主对客方光环压制效果
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBattlebegintime(long _v_) { // 战斗开始的时间
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
		public void setTimeout(java.util.concurrent.ScheduledFuture<?> _v_) { // 超时检测
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEngine(fire.script.FightJSEngine _v_) { // 用于本场战斗的JS引擎
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFieldfighter(fire.pb.battle.Fighter _v_) { // 战场虚拟的Fighter，为战场技能而用，虚拟数据的来源是此战斗中的一个怪
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBattlemaxround(int _v_) { // 战斗的最大回合数 如果不设置默认为200回合
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLivedieteam(int _v_) { // 是否是组队生死战0不是    1是
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
			return BattleInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return BattleInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return BattleInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return BattleInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return BattleInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return BattleInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return BattleInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return BattleInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return BattleInfo.this.toString();
		}

	}

	public static final class Data implements xbean.BattleInfo {
		private long battleid; // 战斗ID
		private int configid; // 战斗配置ID，默认为0时不是通过战斗配置new的ID
		private int background; // 战斗地图
		private int bgm; // 背景音乐
		private int environment; // 战场环境
		private int gmactiontimetimes; // 时间倍率应用除以10.0，用于调试，gm使用。
		private int envchangefrom; // 环境改变来源（0 = 场外， 1 = 场内）
		private int phase; // 战斗的阶段
		private int round; // 战斗当前的回合数
		private boolean canrunaway; // 本场战斗是否能逃跑
		private int autokey; // 战斗者key，fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
		private int battletype; // 战斗类型
		private int areatype; // 练功区类型
		private int areaconf; // 练功区配置值
		private int babyflag; // 是否是含有宝宝的战斗 0不是     1是含有宝宝的战斗
		private int babybagfull; // 宠物栏是否满  0不满    1满
		private long hostroleid; // 发起战斗的主角或者队长的id
		private long monsterid; // 明雷怪npckey
		private int monstertype; // 怪物类型
		private java.util.ArrayList<fire.pb.battle.BattleEndHandler> battleendhandlers; // 处理战斗结束的handler
		private int hostform; // 主方光环
		private int guestform; // 客方光环
		private int hostformlevel; // 主方光环等级
		private int guestformlevel; // 客方光环等级
		private float hostformeffcet; // 主对客方光环压制效果
		private java.util.LinkedList<fire.pb.battle.FighterInfo> figterinfoonbegin; // 出战斗时的战斗者信息
		private java.util.HashMap<Long, Integer> roleids; // 所有参战的角色ID : 角色的战斗索引
		private java.util.TreeMap<Integer, xbean.Fighter> fighters; // 所有的参战者 战斗索引 ： 战斗者 按索引排序
		private java.util.HashMap<Integer, fire.pb.battle.Fighter> fighterobjects; // 包装好的Fighter类，战斗开始时直接创建完，不用每次用的时候再new,注意使用前要锁rolelock
		private java.util.HashSet<Integer> skipeddeadfighters; // 本回合因为死亡而未出手的战斗者，出手结束清除
		private java.util.HashMap<Integer, xbean.Monster> monsters; // 所有在该场战斗中生成的怪物/NPC，key为fighterId
		private java.util.LinkedList<xbean.FailedMonster> failedmonsters; // 失败的怪物（包括己方客方）
		private java.util.TreeMap<Integer, xbean.Fighter> inifighters; // 把参战者的状态在战斗前保存一份,切磋pk会用到
		private java.util.HashMap<Long, Integer> audience; // 观战的roleid battleindex
		private java.util.TreeMap<Integer, xbean.Fighter> watchers; // 
		private java.util.LinkedList<Long> offlineroleids; // 战斗中掉线的角色，不能发送协议
		private long battlebegintime; // 战斗开始的时间
		private long roundtime; // PRoundStart后是本回合的开始时间;PPlayScript后是播放开始的时间
		private long scriptplaytime; // 脚本播放时间（ms），根据脚本计算出来的播放时间，是脚本播放的最短时间，最短1秒
		private java.util.HashMap<Integer, xbean.Decision> decisions; // 战斗单元决策结果
		private java.util.HashSet<Long> feedbacks; // roleid				角色的反馈记录
		private int battleresult; // 战斗是否结束及战斗结果，参考BattleField.BATTLE_XXX,默认为2（BATTLE_NOT_END）
		private java.util.HashMap<Integer, Integer> roundresult; // 战斗单位：战斗结果
		private java.util.HashMap<Integer, xbean.Protectors> protectormap; // 被保护人：保护者列表
		private java.util.concurrent.ScheduledFuture<?> timeout; // 超时检测
		private java.util.LinkedList<xbean.BattleAI> battleais; // 挂在战场上的AI
		private fire.script.FightJSEngine engine; // 用于本场战斗的JS引擎
		private java.util.HashMap<Integer, fire.pb.battle.ActionChangedAttrs> fighterchangedattrs; // 战斗者每个回合改变的属性,key=fighterId
		private java.util.HashMap<Integer, Float> battledatas; // 保存战斗相关的一些数据
		private fire.pb.battle.Fighter fieldfighter; // 战场虚拟的Fighter，为战场技能而用，虚拟数据的来源是此战斗中的一个怪
		private java.util.LinkedList<xbean.BattleEndProcedure> endprocedures; // 战斗者出战斗时执行的Procedure
		private java.util.HashMap<Integer, xbean.RoundResultItems> roundresultitem; // 整场战斗的demo
		private java.util.LinkedList<Integer> itemtimelist; // 整场战斗的demo时间
		private java.util.ArrayList<fire.pb.battle.operate.Operate> reliveacts; // 复活后要出手的行动
		private java.util.HashMap<Long, Integer> battledeads; // 第几回合死掉的角色
		private int battlemaxround; // 战斗的最大回合数 如果不设置默认为200回合
		private java.util.HashMap<Integer, Integer> params; // 与战斗有关的一些参数
		private java.util.LinkedList<Long> quitteamroleids; // 战斗中有退队角色,战后自动退队 by changhao
		private java.util.LinkedList<Long> absentteamroleids; // 战斗中有暂离角色,战后自动暂离 by changhao
		private java.util.LinkedList<Long> teamidlist; // 战斗里所有队伍id by changhao
		private int livedieteam; // 是否是组队生死战0不是    1是

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			configid = 0;
			background = 0;
			bgm = 0;
			gmactiontimetimes = 10;
			round = 0;
			canrunaway = true;
			autokey = 0;
			babyflag = 0;
			babybagfull = 0;
			monsterid = -1;
			monstertype = -1;
			battleendhandlers = new java.util.ArrayList<fire.pb.battle.BattleEndHandler>();
			figterinfoonbegin = new java.util.LinkedList<fire.pb.battle.FighterInfo>();
			roleids = new java.util.HashMap<Long, Integer>();
			fighters = new java.util.TreeMap<Integer, xbean.Fighter>();
			fighterobjects = new java.util.HashMap<Integer, fire.pb.battle.Fighter>();
			skipeddeadfighters = new java.util.HashSet<Integer>();
			monsters = new java.util.HashMap<Integer, xbean.Monster>();
			failedmonsters = new java.util.LinkedList<xbean.FailedMonster>();
			inifighters = new java.util.TreeMap<Integer, xbean.Fighter>();
			audience = new java.util.HashMap<Long, Integer>();
			watchers = new java.util.TreeMap<Integer, xbean.Fighter>();
			offlineroleids = new java.util.LinkedList<Long>();
			scriptplaytime = 1000;
			decisions = new java.util.HashMap<Integer, xbean.Decision>();
			feedbacks = new java.util.HashSet<Long>();
			battleresult = 2;
			roundresult = new java.util.HashMap<Integer, Integer>();
			protectormap = new java.util.HashMap<Integer, xbean.Protectors>();
			timeout = null;
			battleais = new java.util.LinkedList<xbean.BattleAI>();
			engine = null;
			fighterchangedattrs = new java.util.HashMap<Integer, fire.pb.battle.ActionChangedAttrs>();
			battledatas = new java.util.HashMap<Integer, Float>();
			fieldfighter = null;
			endprocedures = new java.util.LinkedList<xbean.BattleEndProcedure>();
			roundresultitem = new java.util.HashMap<Integer, xbean.RoundResultItems>();
			itemtimelist = new java.util.LinkedList<Integer>();
			reliveacts = new java.util.ArrayList<fire.pb.battle.operate.Operate>();
			battledeads = new java.util.HashMap<Long, Integer>();
			params = new java.util.HashMap<Integer, Integer>();
			quitteamroleids = new java.util.LinkedList<Long>();
			absentteamroleids = new java.util.LinkedList<Long>();
			teamidlist = new java.util.LinkedList<Long>();
		}

		Data(xbean.BattleInfo _o1_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			throw new UnsupportedOperationException();
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.BattleInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.BattleInfo toData() {
			return new Data(this);
		}

		public xbean.BattleInfo toBean() {
			return new BattleInfo(this, null, null);
		}

		@Override
		public xbean.BattleInfo toDataIf() {
			return this;
		}

		public xbean.BattleInfo toBeanIf() {
			return new BattleInfo(this, null, null);
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
		public int getBackground() { // 战斗地图
			return background;
		}

		@Override
		public int getBgm() { // 背景音乐
			return bgm;
		}

		@Override
		public int getEnvironment() { // 战场环境
			return environment;
		}

		@Override
		public int getGmactiontimetimes() { // 时间倍率应用除以10.0，用于调试，gm使用。
			return gmactiontimetimes;
		}

		@Override
		public int getEnvchangefrom() { // 环境改变来源（0 = 场外， 1 = 场内）
			return envchangefrom;
		}

		@Override
		public int getPhase() { // 战斗的阶段
			return phase;
		}

		@Override
		public int getRound() { // 战斗当前的回合数
			return round;
		}

		@Override
		public boolean getCanrunaway() { // 本场战斗是否能逃跑
			return canrunaway;
		}

		@Override
		public int getAutokey() { // 战斗者key，fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
			return autokey;
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
		public int getBabyflag() { // 是否是含有宝宝的战斗 0不是     1是含有宝宝的战斗
			return babyflag;
		}

		@Override
		public int getBabybagfull() { // 宠物栏是否满  0不满    1满
			return babybagfull;
		}

		@Override
		public long getHostroleid() { // 发起战斗的主角或者队长的id
			return hostroleid;
		}

		@Override
		public long getMonsterid() { // 明雷怪npckey
			return monsterid;
		}

		@Override
		public int getMonstertype() { // 怪物类型
			return monstertype;
		}

		@Override
		public java.util.List<fire.pb.battle.BattleEndHandler> getBattleendhandlers() { // 处理战斗结束的handler
			return battleendhandlers;
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
		public int getHostformlevel() { // 主方光环等级
			return hostformlevel;
		}

		@Override
		public int getGuestformlevel() { // 客方光环等级
			return guestformlevel;
		}

		@Override
		public float getHostformeffcet() { // 主对客方光环压制效果
			return hostformeffcet;
		}

		@Override
		public java.util.List<fire.pb.battle.FighterInfo> getFigterinfoonbegin() { // 出战斗时的战斗者信息
			return figterinfoonbegin;
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
		public java.util.Map<Integer, fire.pb.battle.Fighter> getFighterobjects() { // 包装好的Fighter类，战斗开始时直接创建完，不用每次用的时候再new,注意使用前要锁rolelock
			return fighterobjects;
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
		public java.util.List<Long> getOfflineroleids() { // 战斗中掉线的角色，不能发送协议
			return offlineroleids;
		}

		@Override
		public java.util.List<Long> getOfflineroleidsAsData() { // 战斗中掉线的角色，不能发送协议
			return offlineroleids;
		}

		@Override
		public long getBattlebegintime() { // 战斗开始的时间
			return battlebegintime;
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
		public java.util.concurrent.ScheduledFuture<?> getTimeout() { // 超时检测
			return timeout;
		}

		@Override
		public java.util.List<xbean.BattleAI> getBattleais() { // 挂在战场上的AI
			return battleais;
		}

		@Override
		public java.util.List<xbean.BattleAI> getBattleaisAsData() { // 挂在战场上的AI
			return battleais;
		}

		@Override
		public fire.script.FightJSEngine getEngine() { // 用于本场战斗的JS引擎
			return engine;
		}

		@Override
		public java.util.Map<Integer, fire.pb.battle.ActionChangedAttrs> getFighterchangedattrs() { // 战斗者每个回合改变的属性,key=fighterId
			return fighterchangedattrs;
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
		public fire.pb.battle.Fighter getFieldfighter() { // 战场虚拟的Fighter，为战场技能而用，虚拟数据的来源是此战斗中的一个怪
			return fieldfighter;
		}

		@Override
		public java.util.List<xbean.BattleEndProcedure> getEndprocedures() { // 战斗者出战斗时执行的Procedure
			return endprocedures;
		}

		@Override
		public java.util.Map<Integer, xbean.RoundResultItems> getRoundresultitem() { // 整场战斗的demo
			return roundresultitem;
		}

		@Override
		public java.util.List<Integer> getItemtimelist() { // 整场战斗的demo时间
			return itemtimelist;
		}

		@Override
		public java.util.List<Integer> getItemtimelistAsData() { // 整场战斗的demo时间
			return itemtimelist;
		}

		@Override
		public java.util.List<fire.pb.battle.operate.Operate> getReliveacts() { // 复活后要出手的行动
			return reliveacts;
		}

		@Override
		public java.util.Map<Long, Integer> getBattledeads() { // 第几回合死掉的角色
			return battledeads;
		}

		@Override
		public java.util.Map<Long, Integer> getBattledeadsAsData() { // 第几回合死掉的角色
			return battledeads;
		}

		@Override
		public int getBattlemaxround() { // 战斗的最大回合数 如果不设置默认为200回合
			return battlemaxround;
		}

		@Override
		public java.util.Map<Integer, Integer> getParams() { // 与战斗有关的一些参数
			return params;
		}

		@Override
		public java.util.Map<Integer, Integer> getParamsAsData() { // 与战斗有关的一些参数
			return params;
		}

		@Override
		public java.util.List<Long> getQuitteamroleids() { // 战斗中有退队角色,战后自动退队 by changhao
			return quitteamroleids;
		}

		@Override
		public java.util.List<Long> getQuitteamroleidsAsData() { // 战斗中有退队角色,战后自动退队 by changhao
			return quitteamroleids;
		}

		@Override
		public java.util.List<Long> getAbsentteamroleids() { // 战斗中有暂离角色,战后自动暂离 by changhao
			return absentteamroleids;
		}

		@Override
		public java.util.List<Long> getAbsentteamroleidsAsData() { // 战斗中有暂离角色,战后自动暂离 by changhao
			return absentteamroleids;
		}

		@Override
		public java.util.List<Long> getTeamidlist() { // 战斗里所有队伍id by changhao
			return teamidlist;
		}

		@Override
		public java.util.List<Long> getTeamidlistAsData() { // 战斗里所有队伍id by changhao
			return teamidlist;
		}

		@Override
		public int getLivedieteam() { // 是否是组队生死战0不是    1是
			return livedieteam;
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
		public void setBackground(int _v_) { // 战斗地图
			background = _v_;
		}

		@Override
		public void setBgm(int _v_) { // 背景音乐
			bgm = _v_;
		}

		@Override
		public void setEnvironment(int _v_) { // 战场环境
			environment = _v_;
		}

		@Override
		public void setGmactiontimetimes(int _v_) { // 时间倍率应用除以10.0，用于调试，gm使用。
			gmactiontimetimes = _v_;
		}

		@Override
		public void setEnvchangefrom(int _v_) { // 环境改变来源（0 = 场外， 1 = 场内）
			envchangefrom = _v_;
		}

		@Override
		public void setPhase(int _v_) { // 战斗的阶段
			phase = _v_;
		}

		@Override
		public void setRound(int _v_) { // 战斗当前的回合数
			round = _v_;
		}

		@Override
		public void setCanrunaway(boolean _v_) { // 本场战斗是否能逃跑
			canrunaway = _v_;
		}

		@Override
		public void setAutokey(int _v_) { // 战斗者key，fighterId可能会重复，不能唯一确定一个战斗者，此自增的key不会重复
			autokey = _v_;
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
		public void setBabyflag(int _v_) { // 是否是含有宝宝的战斗 0不是     1是含有宝宝的战斗
			babyflag = _v_;
		}

		@Override
		public void setBabybagfull(int _v_) { // 宠物栏是否满  0不满    1满
			babybagfull = _v_;
		}

		@Override
		public void setHostroleid(long _v_) { // 发起战斗的主角或者队长的id
			hostroleid = _v_;
		}

		@Override
		public void setMonsterid(long _v_) { // 明雷怪npckey
			monsterid = _v_;
		}

		@Override
		public void setMonstertype(int _v_) { // 怪物类型
			monstertype = _v_;
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
		public void setHostformlevel(int _v_) { // 主方光环等级
			hostformlevel = _v_;
		}

		@Override
		public void setGuestformlevel(int _v_) { // 客方光环等级
			guestformlevel = _v_;
		}

		@Override
		public void setHostformeffcet(float _v_) { // 主对客方光环压制效果
			hostformeffcet = _v_;
		}

		@Override
		public void setBattlebegintime(long _v_) { // 战斗开始的时间
			battlebegintime = _v_;
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
		public void setTimeout(java.util.concurrent.ScheduledFuture<?> _v_) { // 超时检测
			timeout = _v_;
		}

		@Override
		public void setEngine(fire.script.FightJSEngine _v_) { // 用于本场战斗的JS引擎
			engine = _v_;
		}

		@Override
		public void setFieldfighter(fire.pb.battle.Fighter _v_) { // 战场虚拟的Fighter，为战场技能而用，虚拟数据的来源是此战斗中的一个怪
			fieldfighter = _v_;
		}

		@Override
		public void setBattlemaxround(int _v_) { // 战斗的最大回合数 如果不设置默认为200回合
			battlemaxround = _v_;
		}

		@Override
		public void setLivedieteam(int _v_) { // 是否是组队生死战0不是    1是
			livedieteam = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof BattleInfo.Data)) return false;
			BattleInfo.Data _o_ = (BattleInfo.Data) _o1_;
			if (battleid != _o_.battleid) return false;
			if (configid != _o_.configid) return false;
			if (background != _o_.background) return false;
			if (bgm != _o_.bgm) return false;
			if (environment != _o_.environment) return false;
			if (gmactiontimetimes != _o_.gmactiontimetimes) return false;
			if (envchangefrom != _o_.envchangefrom) return false;
			if (phase != _o_.phase) return false;
			if (round != _o_.round) return false;
			if (canrunaway != _o_.canrunaway) return false;
			if (autokey != _o_.autokey) return false;
			if (battletype != _o_.battletype) return false;
			if (areatype != _o_.areatype) return false;
			if (areaconf != _o_.areaconf) return false;
			if (babyflag != _o_.babyflag) return false;
			if (babybagfull != _o_.babybagfull) return false;
			if (hostroleid != _o_.hostroleid) return false;
			if (monsterid != _o_.monsterid) return false;
			if (monstertype != _o_.monstertype) return false;
			if (!battleendhandlers.equals(_o_.battleendhandlers)) return false;
			if (hostform != _o_.hostform) return false;
			if (guestform != _o_.guestform) return false;
			if (hostformlevel != _o_.hostformlevel) return false;
			if (guestformlevel != _o_.guestformlevel) return false;
			if (hostformeffcet != _o_.hostformeffcet) return false;
			if (!figterinfoonbegin.equals(_o_.figterinfoonbegin)) return false;
			if (!roleids.equals(_o_.roleids)) return false;
			if (!fighters.equals(_o_.fighters)) return false;
			if (!fighterobjects.equals(_o_.fighterobjects)) return false;
			if (!skipeddeadfighters.equals(_o_.skipeddeadfighters)) return false;
			if (!monsters.equals(_o_.monsters)) return false;
			if (!failedmonsters.equals(_o_.failedmonsters)) return false;
			if (!inifighters.equals(_o_.inifighters)) return false;
			if (!audience.equals(_o_.audience)) return false;
			if (!watchers.equals(_o_.watchers)) return false;
			if (!offlineroleids.equals(_o_.offlineroleids)) return false;
			if (battlebegintime != _o_.battlebegintime) return false;
			if (roundtime != _o_.roundtime) return false;
			if (scriptplaytime != _o_.scriptplaytime) return false;
			if (!decisions.equals(_o_.decisions)) return false;
			if (!feedbacks.equals(_o_.feedbacks)) return false;
			if (battleresult != _o_.battleresult) return false;
			if (!roundresult.equals(_o_.roundresult)) return false;
			if (!protectormap.equals(_o_.protectormap)) return false;
			if ((null == timeout && null != _o_.timeout) || (null != timeout && null == _o_.timeout) || (null != timeout && null != _o_.timeout && !timeout.equals(_o_.timeout))) return false;
			if (!battleais.equals(_o_.battleais)) return false;
			if ((null == engine && null != _o_.engine) || (null != engine && null == _o_.engine) || (null != engine && null != _o_.engine && !engine.equals(_o_.engine))) return false;
			if (!fighterchangedattrs.equals(_o_.fighterchangedattrs)) return false;
			if (!battledatas.equals(_o_.battledatas)) return false;
			if ((null == fieldfighter && null != _o_.fieldfighter) || (null != fieldfighter && null == _o_.fieldfighter) || (null != fieldfighter && null != _o_.fieldfighter && !fieldfighter.equals(_o_.fieldfighter))) return false;
			if (!endprocedures.equals(_o_.endprocedures)) return false;
			if (!roundresultitem.equals(_o_.roundresultitem)) return false;
			if (!itemtimelist.equals(_o_.itemtimelist)) return false;
			if (!reliveacts.equals(_o_.reliveacts)) return false;
			if (!battledeads.equals(_o_.battledeads)) return false;
			if (battlemaxround != _o_.battlemaxround) return false;
			if (!params.equals(_o_.params)) return false;
			if (!quitteamroleids.equals(_o_.quitteamroleids)) return false;
			if (!absentteamroleids.equals(_o_.absentteamroleids)) return false;
			if (!teamidlist.equals(_o_.teamidlist)) return false;
			if (livedieteam != _o_.livedieteam) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += battleid;
			_h_ += configid;
			_h_ += background;
			_h_ += bgm;
			_h_ += environment;
			_h_ += gmactiontimetimes;
			_h_ += envchangefrom;
			_h_ += phase;
			_h_ += round;
			_h_ += canrunaway ? 1231 : 1237;
			_h_ += autokey;
			_h_ += battletype;
			_h_ += areatype;
			_h_ += areaconf;
			_h_ += babyflag;
			_h_ += babybagfull;
			_h_ += hostroleid;
			_h_ += monsterid;
			_h_ += monstertype;
			_h_ += battleendhandlers.hashCode();
			_h_ += hostform;
			_h_ += guestform;
			_h_ += hostformlevel;
			_h_ += guestformlevel;
			_h_ += hostformeffcet;
			_h_ += figterinfoonbegin.hashCode();
			_h_ += roleids.hashCode();
			_h_ += fighters.hashCode();
			_h_ += fighterobjects.hashCode();
			_h_ += skipeddeadfighters.hashCode();
			_h_ += monsters.hashCode();
			_h_ += failedmonsters.hashCode();
			_h_ += inifighters.hashCode();
			_h_ += audience.hashCode();
			_h_ += watchers.hashCode();
			_h_ += offlineroleids.hashCode();
			_h_ += battlebegintime;
			_h_ += roundtime;
			_h_ += scriptplaytime;
			_h_ += decisions.hashCode();
			_h_ += feedbacks.hashCode();
			_h_ += battleresult;
			_h_ += roundresult.hashCode();
			_h_ += protectormap.hashCode();
			_h_ += (timeout == null ? 0 : timeout.hashCode());
			_h_ += battleais.hashCode();
			_h_ += (engine == null ? 0 : engine.hashCode());
			_h_ += fighterchangedattrs.hashCode();
			_h_ += battledatas.hashCode();
			_h_ += (fieldfighter == null ? 0 : fieldfighter.hashCode());
			_h_ += endprocedures.hashCode();
			_h_ += roundresultitem.hashCode();
			_h_ += itemtimelist.hashCode();
			_h_ += reliveacts.hashCode();
			_h_ += battledeads.hashCode();
			_h_ += battlemaxround;
			_h_ += params.hashCode();
			_h_ += quitteamroleids.hashCode();
			_h_ += absentteamroleids.hashCode();
			_h_ += teamidlist.hashCode();
			_h_ += livedieteam;
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
			_sb_.append(background);
			_sb_.append(",");
			_sb_.append(bgm);
			_sb_.append(",");
			_sb_.append(environment);
			_sb_.append(",");
			_sb_.append(gmactiontimetimes);
			_sb_.append(",");
			_sb_.append(envchangefrom);
			_sb_.append(",");
			_sb_.append(phase);
			_sb_.append(",");
			_sb_.append(round);
			_sb_.append(",");
			_sb_.append(canrunaway);
			_sb_.append(",");
			_sb_.append(autokey);
			_sb_.append(",");
			_sb_.append(battletype);
			_sb_.append(",");
			_sb_.append(areatype);
			_sb_.append(",");
			_sb_.append(areaconf);
			_sb_.append(",");
			_sb_.append(babyflag);
			_sb_.append(",");
			_sb_.append(babybagfull);
			_sb_.append(",");
			_sb_.append(hostroleid);
			_sb_.append(",");
			_sb_.append(monsterid);
			_sb_.append(",");
			_sb_.append(monstertype);
			_sb_.append(",");
			_sb_.append(battleendhandlers);
			_sb_.append(",");
			_sb_.append(hostform);
			_sb_.append(",");
			_sb_.append(guestform);
			_sb_.append(",");
			_sb_.append(hostformlevel);
			_sb_.append(",");
			_sb_.append(guestformlevel);
			_sb_.append(",");
			_sb_.append(hostformeffcet);
			_sb_.append(",");
			_sb_.append(figterinfoonbegin);
			_sb_.append(",");
			_sb_.append(roleids);
			_sb_.append(",");
			_sb_.append(fighters);
			_sb_.append(",");
			_sb_.append(fighterobjects);
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
			_sb_.append(offlineroleids);
			_sb_.append(",");
			_sb_.append(battlebegintime);
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
			_sb_.append(timeout);
			_sb_.append(",");
			_sb_.append(battleais);
			_sb_.append(",");
			_sb_.append(engine);
			_sb_.append(",");
			_sb_.append(fighterchangedattrs);
			_sb_.append(",");
			_sb_.append(battledatas);
			_sb_.append(",");
			_sb_.append(fieldfighter);
			_sb_.append(",");
			_sb_.append(endprocedures);
			_sb_.append(",");
			_sb_.append(roundresultitem);
			_sb_.append(",");
			_sb_.append(itemtimelist);
			_sb_.append(",");
			_sb_.append(reliveacts);
			_sb_.append(",");
			_sb_.append(battledeads);
			_sb_.append(",");
			_sb_.append(battlemaxround);
			_sb_.append(",");
			_sb_.append(params);
			_sb_.append(",");
			_sb_.append(quitteamroleids);
			_sb_.append(",");
			_sb_.append(absentteamroleids);
			_sb_.append(",");
			_sb_.append(teamidlist);
			_sb_.append(",");
			_sb_.append(livedieteam);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
