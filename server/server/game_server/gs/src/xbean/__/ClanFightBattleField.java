
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanFightBattleField extends mkdb.XBean implements xbean.ClanFightBattleField {
	private int battlefieldid; // excel表里的ID by changhao
	private long clanfightid; // 公会战id by changhao
	private int state; // 战场状态 by changhao
	private long clanid1; // 公会1 by changhao
	private long clanid2; // 公会2 by changhao
	private String clanname1; // 公会名字1 by changhao
	private String clanname2; // 公会名字2 by changhao
	private int clanscore1; // 公会1积分 by changhao
	private int clanscore2; // 公会2积分 by changhao
	private java.util.HashMap<Long, Integer> clan1roleids; // 进入公会战场的 公会1玩家 by changhao
	private java.util.HashMap<Long, Integer> clan2roleids; // 进入公会战场的 公会2玩家 by changhao
	private java.util.HashMap<Long, String> enterroleids; // 进入过战场的角色id by changhao
	private java.util.HashMap<Long, Integer> clan1enterroleids; // 公会1战场进入过的玩家 by changhao
	private java.util.HashMap<Long, Integer> clan2enterroleids; // 公会2战场进入过的玩家 by changhao
	private java.util.LinkedList<xbean.BattleFieldScroeRank> clan1roleranklist; // 当前战场排名1 by changhao
	private java.util.LinkedList<xbean.BattleFieldScroeRank> clan2roleranklist; // 当前战场排名2 by changhao
	private java.util.HashMap<Long, Integer> clan1rolerankmap; // key是角色id value是排名(从0开始) by changhao
	private java.util.HashMap<Long, Integer> clan2rolerankmap; // key是角色id value是排名(从0开始) by changhao
	private long lastclan1rolesortstamp; // 上次排序时间戳 by changhao
	private long lastclan2rolesortstamp; // 上次排序时间戳 by changhao
	private java.util.LinkedList<Long> victoryboxids; // 胜利宝箱链表 by changhao
	private java.util.LinkedList<Long> celebrateboxids; // 庆祝宝箱链表 by changhao
	private long datetime; // 周几那场比赛时间 by changhao
	private long activitystarttime; // 公会战活动开始时间 by changhao
	private long activitypreparetime; // 公会战准备结束时间 by changhao
	private long activitynoentertime; // 公会战不能在进入时间 by changhao
	private long activityendtime; // 公会战活动结束时间 by changhao
	private int whichwin; // -1没结果0是第一个公会1是第二个公会 by changhao

	@Override
	public void _reset_unsafe_() {
		battlefieldid = 0;
		clanfightid = 0L;
		state = 0;
		clanid1 = 0L;
		clanid2 = 0L;
		clanname1 = "";
		clanname2 = "";
		clanscore1 = 0;
		clanscore2 = 0;
		clan1roleids.clear();
		clan2roleids.clear();
		enterroleids.clear();
		clan1enterroleids.clear();
		clan2enterroleids.clear();
		clan1roleranklist.clear();
		clan2roleranklist.clear();
		clan1rolerankmap.clear();
		clan2rolerankmap.clear();
		lastclan1rolesortstamp = 0L;
		lastclan2rolesortstamp = 0L;
		victoryboxids.clear();
		celebrateboxids.clear();
		datetime = 0L;
		activitystarttime = 0L;
		activitypreparetime = 0L;
		activitynoentertime = 0L;
		activityendtime = 0L;
		whichwin = 0;
	}

	ClanFightBattleField(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		clanname1 = "";
		clanname2 = "";
		clan1roleids = new java.util.HashMap<Long, Integer>();
		clan2roleids = new java.util.HashMap<Long, Integer>();
		enterroleids = new java.util.HashMap<Long, String>();
		clan1enterroleids = new java.util.HashMap<Long, Integer>();
		clan2enterroleids = new java.util.HashMap<Long, Integer>();
		clan1roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
		clan2roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
		clan1rolerankmap = new java.util.HashMap<Long, Integer>();
		clan2rolerankmap = new java.util.HashMap<Long, Integer>();
		victoryboxids = new java.util.LinkedList<Long>();
		celebrateboxids = new java.util.LinkedList<Long>();
	}

	public ClanFightBattleField() {
		this(0, null, null);
	}

	public ClanFightBattleField(ClanFightBattleField _o_) {
		this(_o_, null, null);
	}

	ClanFightBattleField(xbean.ClanFightBattleField _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanFightBattleField) assign((ClanFightBattleField)_o1_);
		else if (_o1_ instanceof ClanFightBattleField.Data) assign((ClanFightBattleField.Data)_o1_);
		else if (_o1_ instanceof ClanFightBattleField.Const) assign(((ClanFightBattleField.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanFightBattleField _o_) {
		_o_._xdb_verify_unsafe_();
		battlefieldid = _o_.battlefieldid;
		clanfightid = _o_.clanfightid;
		state = _o_.state;
		clanid1 = _o_.clanid1;
		clanid2 = _o_.clanid2;
		clanname1 = _o_.clanname1;
		clanname2 = _o_.clanname2;
		clanscore1 = _o_.clanscore1;
		clanscore2 = _o_.clanscore2;
		clan1roleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1roleids.entrySet())
			clan1roleids.put(_e_.getKey(), _e_.getValue());
		clan2roleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2roleids.entrySet())
			clan2roleids.put(_e_.getKey(), _e_.getValue());
		enterroleids = new java.util.HashMap<Long, String>();
		for (java.util.Map.Entry<Long, String> _e_ : _o_.enterroleids.entrySet())
			enterroleids.put(_e_.getKey(), _e_.getValue());
		clan1enterroleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1enterroleids.entrySet())
			clan1enterroleids.put(_e_.getKey(), _e_.getValue());
		clan2enterroleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2enterroleids.entrySet())
			clan2enterroleids.put(_e_.getKey(), _e_.getValue());
		clan1roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
		for (xbean.BattleFieldScroeRank _v_ : _o_.clan1roleranklist)
			clan1roleranklist.add(new BattleFieldScroeRank(_v_, this, "clan1roleranklist"));
		clan2roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
		for (xbean.BattleFieldScroeRank _v_ : _o_.clan2roleranklist)
			clan2roleranklist.add(new BattleFieldScroeRank(_v_, this, "clan2roleranklist"));
		clan1rolerankmap = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1rolerankmap.entrySet())
			clan1rolerankmap.put(_e_.getKey(), _e_.getValue());
		clan2rolerankmap = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2rolerankmap.entrySet())
			clan2rolerankmap.put(_e_.getKey(), _e_.getValue());
		lastclan1rolesortstamp = _o_.lastclan1rolesortstamp;
		lastclan2rolesortstamp = _o_.lastclan2rolesortstamp;
		victoryboxids = new java.util.LinkedList<Long>();
		victoryboxids.addAll(_o_.victoryboxids);
		celebrateboxids = new java.util.LinkedList<Long>();
		celebrateboxids.addAll(_o_.celebrateboxids);
		datetime = _o_.datetime;
		activitystarttime = _o_.activitystarttime;
		activitypreparetime = _o_.activitypreparetime;
		activitynoentertime = _o_.activitynoentertime;
		activityendtime = _o_.activityendtime;
		whichwin = _o_.whichwin;
	}

	private void assign(ClanFightBattleField.Data _o_) {
		battlefieldid = _o_.battlefieldid;
		clanfightid = _o_.clanfightid;
		state = _o_.state;
		clanid1 = _o_.clanid1;
		clanid2 = _o_.clanid2;
		clanname1 = _o_.clanname1;
		clanname2 = _o_.clanname2;
		clanscore1 = _o_.clanscore1;
		clanscore2 = _o_.clanscore2;
		clan1roleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1roleids.entrySet())
			clan1roleids.put(_e_.getKey(), _e_.getValue());
		clan2roleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2roleids.entrySet())
			clan2roleids.put(_e_.getKey(), _e_.getValue());
		enterroleids = new java.util.HashMap<Long, String>();
		for (java.util.Map.Entry<Long, String> _e_ : _o_.enterroleids.entrySet())
			enterroleids.put(_e_.getKey(), _e_.getValue());
		clan1enterroleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1enterroleids.entrySet())
			clan1enterroleids.put(_e_.getKey(), _e_.getValue());
		clan2enterroleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2enterroleids.entrySet())
			clan2enterroleids.put(_e_.getKey(), _e_.getValue());
		clan1roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
		for (xbean.BattleFieldScroeRank _v_ : _o_.clan1roleranklist)
			clan1roleranklist.add(new BattleFieldScroeRank(_v_, this, "clan1roleranklist"));
		clan2roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
		for (xbean.BattleFieldScroeRank _v_ : _o_.clan2roleranklist)
			clan2roleranklist.add(new BattleFieldScroeRank(_v_, this, "clan2roleranklist"));
		clan1rolerankmap = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1rolerankmap.entrySet())
			clan1rolerankmap.put(_e_.getKey(), _e_.getValue());
		clan2rolerankmap = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2rolerankmap.entrySet())
			clan2rolerankmap.put(_e_.getKey(), _e_.getValue());
		lastclan1rolesortstamp = _o_.lastclan1rolesortstamp;
		lastclan2rolesortstamp = _o_.lastclan2rolesortstamp;
		victoryboxids = new java.util.LinkedList<Long>();
		victoryboxids.addAll(_o_.victoryboxids);
		celebrateboxids = new java.util.LinkedList<Long>();
		celebrateboxids.addAll(_o_.celebrateboxids);
		datetime = _o_.datetime;
		activitystarttime = _o_.activitystarttime;
		activitypreparetime = _o_.activitypreparetime;
		activitynoentertime = _o_.activitynoentertime;
		activityendtime = _o_.activityendtime;
		whichwin = _o_.whichwin;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(battlefieldid);
		_os_.marshal(clanfightid);
		_os_.marshal(state);
		_os_.marshal(clanid1);
		_os_.marshal(clanid2);
		_os_.marshal(clanname1, mkdb.Const.IO_CHARSET);
		_os_.marshal(clanname2, mkdb.Const.IO_CHARSET);
		_os_.marshal(clanscore1);
		_os_.marshal(clanscore2);
		_os_.compact_uint32(clan1roleids.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : clan1roleids.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(clan2roleids.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : clan2roleids.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(enterroleids.size());
		for (java.util.Map.Entry<Long, String> _e_ : enterroleids.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue(), mkdb.Const.IO_CHARSET);
		}
		_os_.compact_uint32(clan1enterroleids.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : clan1enterroleids.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(clan2enterroleids.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : clan2enterroleids.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(clan1roleranklist.size());
		for (xbean.BattleFieldScroeRank _v_ : clan1roleranklist) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(clan2roleranklist.size());
		for (xbean.BattleFieldScroeRank _v_ : clan2roleranklist) {
			_v_.marshal(_os_);
		}
		_os_.compact_uint32(clan1rolerankmap.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : clan1rolerankmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(clan2rolerankmap.size());
		for (java.util.Map.Entry<Long, Integer> _e_ : clan2rolerankmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(lastclan1rolesortstamp);
		_os_.marshal(lastclan2rolesortstamp);
		_os_.compact_uint32(victoryboxids.size());
		for (Long _v_ : victoryboxids) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(celebrateboxids.size());
		for (Long _v_ : celebrateboxids) {
			_os_.marshal(_v_);
		}
		_os_.marshal(datetime);
		_os_.marshal(activitystarttime);
		_os_.marshal(activitypreparetime);
		_os_.marshal(activitynoentertime);
		_os_.marshal(activityendtime);
		_os_.marshal(whichwin);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		battlefieldid = _os_.unmarshal_int();
		clanfightid = _os_.unmarshal_long();
		state = _os_.unmarshal_int();
		clanid1 = _os_.unmarshal_long();
		clanid2 = _os_.unmarshal_long();
		clanname1 = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		clanname2 = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		clanscore1 = _os_.unmarshal_int();
		clanscore2 = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				clan1roleids = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				clan1roleids.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				clan2roleids = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				clan2roleids.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				enterroleids = new java.util.HashMap<Long, String>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				String _v_ = "";
				_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				enterroleids.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				clan1enterroleids = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				clan1enterroleids.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				clan2enterroleids = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				clan2enterroleids.put(_k_, _v_);
			}
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.BattleFieldScroeRank _v_ = new BattleFieldScroeRank(0, this, "clan1roleranklist");
			_v_.unmarshal(_os_);
			clan1roleranklist.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.BattleFieldScroeRank _v_ = new BattleFieldScroeRank(0, this, "clan2roleranklist");
			_v_.unmarshal(_os_);
			clan2roleranklist.add(_v_);
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				clan1rolerankmap = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				clan1rolerankmap.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				clan2rolerankmap = new java.util.HashMap<Long, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				clan2rolerankmap.put(_k_, _v_);
			}
		}
		lastclan1rolesortstamp = _os_.unmarshal_long();
		lastclan2rolesortstamp = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			victoryboxids.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			celebrateboxids.add(_v_);
		}
		datetime = _os_.unmarshal_long();
		activitystarttime = _os_.unmarshal_long();
		activitypreparetime = _os_.unmarshal_long();
		activitynoentertime = _os_.unmarshal_long();
		activityendtime = _os_.unmarshal_long();
		whichwin = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ClanFightBattleField copy() {
		_xdb_verify_unsafe_();
		return new ClanFightBattleField(this);
	}

	@Override
	public xbean.ClanFightBattleField toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanFightBattleField toBean() {
		_xdb_verify_unsafe_();
		return new ClanFightBattleField(this); // same as copy()
	}

	@Override
	public xbean.ClanFightBattleField toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanFightBattleField toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getBattlefieldid() { // excel表里的ID by changhao
		_xdb_verify_unsafe_();
		return battlefieldid;
	}

	@Override
	public long getClanfightid() { // 公会战id by changhao
		_xdb_verify_unsafe_();
		return clanfightid;
	}

	@Override
	public int getState() { // 战场状态 by changhao
		_xdb_verify_unsafe_();
		return state;
	}

	@Override
	public long getClanid1() { // 公会1 by changhao
		_xdb_verify_unsafe_();
		return clanid1;
	}

	@Override
	public long getClanid2() { // 公会2 by changhao
		_xdb_verify_unsafe_();
		return clanid2;
	}

	@Override
	public String getClanname1() { // 公会名字1 by changhao
		_xdb_verify_unsafe_();
		return clanname1;
	}

	@Override
	public com.locojoy.base.Octets getClanname1Octets() { // 公会名字1 by changhao
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getClanname1(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getClanname2() { // 公会名字2 by changhao
		_xdb_verify_unsafe_();
		return clanname2;
	}

	@Override
	public com.locojoy.base.Octets getClanname2Octets() { // 公会名字2 by changhao
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getClanname2(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getClanscore1() { // 公会1积分 by changhao
		_xdb_verify_unsafe_();
		return clanscore1;
	}

	@Override
	public int getClanscore2() { // 公会2积分 by changhao
		_xdb_verify_unsafe_();
		return clanscore2;
	}

	@Override
	public java.util.Map<Long, Integer> getClan1roleids() { // 进入公会战场的 公会1玩家 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "clan1roleids"), clan1roleids);
	}

	@Override
	public java.util.Map<Long, Integer> getClan1roleidsAsData() { // 进入公会战场的 公会1玩家 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> clan1roleids;
		ClanFightBattleField _o_ = this;
		clan1roleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1roleids.entrySet())
			clan1roleids.put(_e_.getKey(), _e_.getValue());
		return clan1roleids;
	}

	@Override
	public java.util.Map<Long, Integer> getClan2roleids() { // 进入公会战场的 公会2玩家 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "clan2roleids"), clan2roleids);
	}

	@Override
	public java.util.Map<Long, Integer> getClan2roleidsAsData() { // 进入公会战场的 公会2玩家 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> clan2roleids;
		ClanFightBattleField _o_ = this;
		clan2roleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2roleids.entrySet())
			clan2roleids.put(_e_.getKey(), _e_.getValue());
		return clan2roleids;
	}

	@Override
	public java.util.Map<Long, String> getEnterroleids() { // 进入过战场的角色id by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "enterroleids"), enterroleids);
	}

	@Override
	public java.util.Map<Long, String> getEnterroleidsAsData() { // 进入过战场的角色id by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, String> enterroleids;
		ClanFightBattleField _o_ = this;
		enterroleids = new java.util.HashMap<Long, String>();
		for (java.util.Map.Entry<Long, String> _e_ : _o_.enterroleids.entrySet())
			enterroleids.put(_e_.getKey(), _e_.getValue());
		return enterroleids;
	}

	@Override
	public java.util.Map<Long, Integer> getClan1enterroleids() { // 公会1战场进入过的玩家 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "clan1enterroleids"), clan1enterroleids);
	}

	@Override
	public java.util.Map<Long, Integer> getClan1enterroleidsAsData() { // 公会1战场进入过的玩家 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> clan1enterroleids;
		ClanFightBattleField _o_ = this;
		clan1enterroleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1enterroleids.entrySet())
			clan1enterroleids.put(_e_.getKey(), _e_.getValue());
		return clan1enterroleids;
	}

	@Override
	public java.util.Map<Long, Integer> getClan2enterroleids() { // 公会2战场进入过的玩家 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "clan2enterroleids"), clan2enterroleids);
	}

	@Override
	public java.util.Map<Long, Integer> getClan2enterroleidsAsData() { // 公会2战场进入过的玩家 by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> clan2enterroleids;
		ClanFightBattleField _o_ = this;
		clan2enterroleids = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2enterroleids.entrySet())
			clan2enterroleids.put(_e_.getKey(), _e_.getValue());
		return clan2enterroleids;
	}

	@Override
	public java.util.List<xbean.BattleFieldScroeRank> getClan1roleranklist() { // 当前战场排名1 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "clan1roleranklist"), clan1roleranklist);
	}

	public java.util.List<xbean.BattleFieldScroeRank> getClan1roleranklistAsData() { // 当前战场排名1 by changhao
		_xdb_verify_unsafe_();
		java.util.List<xbean.BattleFieldScroeRank> clan1roleranklist;
		ClanFightBattleField _o_ = this;
		clan1roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
		for (xbean.BattleFieldScroeRank _v_ : _o_.clan1roleranklist)
			clan1roleranklist.add(new BattleFieldScroeRank.Data(_v_));
		return clan1roleranklist;
	}

	@Override
	public java.util.List<xbean.BattleFieldScroeRank> getClan2roleranklist() { // 当前战场排名2 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "clan2roleranklist"), clan2roleranklist);
	}

	public java.util.List<xbean.BattleFieldScroeRank> getClan2roleranklistAsData() { // 当前战场排名2 by changhao
		_xdb_verify_unsafe_();
		java.util.List<xbean.BattleFieldScroeRank> clan2roleranklist;
		ClanFightBattleField _o_ = this;
		clan2roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
		for (xbean.BattleFieldScroeRank _v_ : _o_.clan2roleranklist)
			clan2roleranklist.add(new BattleFieldScroeRank.Data(_v_));
		return clan2roleranklist;
	}

	@Override
	public java.util.Map<Long, Integer> getClan1rolerankmap() { // key是角色id value是排名(从0开始) by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "clan1rolerankmap"), clan1rolerankmap);
	}

	@Override
	public java.util.Map<Long, Integer> getClan1rolerankmapAsData() { // key是角色id value是排名(从0开始) by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> clan1rolerankmap;
		ClanFightBattleField _o_ = this;
		clan1rolerankmap = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1rolerankmap.entrySet())
			clan1rolerankmap.put(_e_.getKey(), _e_.getValue());
		return clan1rolerankmap;
	}

	@Override
	public java.util.Map<Long, Integer> getClan2rolerankmap() { // key是角色id value是排名(从0开始) by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "clan2rolerankmap"), clan2rolerankmap);
	}

	@Override
	public java.util.Map<Long, Integer> getClan2rolerankmapAsData() { // key是角色id value是排名(从0开始) by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Long, Integer> clan2rolerankmap;
		ClanFightBattleField _o_ = this;
		clan2rolerankmap = new java.util.HashMap<Long, Integer>();
		for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2rolerankmap.entrySet())
			clan2rolerankmap.put(_e_.getKey(), _e_.getValue());
		return clan2rolerankmap;
	}

	@Override
	public long getLastclan1rolesortstamp() { // 上次排序时间戳 by changhao
		_xdb_verify_unsafe_();
		return lastclan1rolesortstamp;
	}

	@Override
	public long getLastclan2rolesortstamp() { // 上次排序时间戳 by changhao
		_xdb_verify_unsafe_();
		return lastclan2rolesortstamp;
	}

	@Override
	public java.util.List<Long> getVictoryboxids() { // 胜利宝箱链表 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "victoryboxids"), victoryboxids);
	}

	public java.util.List<Long> getVictoryboxidsAsData() { // 胜利宝箱链表 by changhao
		_xdb_verify_unsafe_();
		java.util.List<Long> victoryboxids;
		ClanFightBattleField _o_ = this;
		victoryboxids = new java.util.LinkedList<Long>();
		victoryboxids.addAll(_o_.victoryboxids);
		return victoryboxids;
	}

	@Override
	public java.util.List<Long> getCelebrateboxids() { // 庆祝宝箱链表 by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "celebrateboxids"), celebrateboxids);
	}

	public java.util.List<Long> getCelebrateboxidsAsData() { // 庆祝宝箱链表 by changhao
		_xdb_verify_unsafe_();
		java.util.List<Long> celebrateboxids;
		ClanFightBattleField _o_ = this;
		celebrateboxids = new java.util.LinkedList<Long>();
		celebrateboxids.addAll(_o_.celebrateboxids);
		return celebrateboxids;
	}

	@Override
	public long getDatetime() { // 周几那场比赛时间 by changhao
		_xdb_verify_unsafe_();
		return datetime;
	}

	@Override
	public long getActivitystarttime() { // 公会战活动开始时间 by changhao
		_xdb_verify_unsafe_();
		return activitystarttime;
	}

	@Override
	public long getActivitypreparetime() { // 公会战准备结束时间 by changhao
		_xdb_verify_unsafe_();
		return activitypreparetime;
	}

	@Override
	public long getActivitynoentertime() { // 公会战不能在进入时间 by changhao
		_xdb_verify_unsafe_();
		return activitynoentertime;
	}

	@Override
	public long getActivityendtime() { // 公会战活动结束时间 by changhao
		_xdb_verify_unsafe_();
		return activityendtime;
	}

	@Override
	public int getWhichwin() { // -1没结果0是第一个公会1是第二个公会 by changhao
		_xdb_verify_unsafe_();
		return whichwin;
	}

	@Override
	public void setBattlefieldid(int _v_) { // excel表里的ID by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "battlefieldid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, battlefieldid) {
					public void rollback() { battlefieldid = _xdb_saved; }
				};}});
		battlefieldid = _v_;
	}

	@Override
	public void setClanfightid(long _v_) { // 公会战id by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanfightid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clanfightid) {
					public void rollback() { clanfightid = _xdb_saved; }
				};}});
		clanfightid = _v_;
	}

	@Override
	public void setState(int _v_) { // 战场状态 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "state") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, state) {
					public void rollback() { state = _xdb_saved; }
				};}});
		state = _v_;
	}

	@Override
	public void setClanid1(long _v_) { // 公会1 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanid1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clanid1) {
					public void rollback() { clanid1 = _xdb_saved; }
				};}});
		clanid1 = _v_;
	}

	@Override
	public void setClanid2(long _v_) { // 公会2 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanid2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clanid2) {
					public void rollback() { clanid2 = _xdb_saved; }
				};}});
		clanid2 = _v_;
	}

	@Override
	public void setClanname1(String _v_) { // 公会名字1 by changhao
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanname1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, clanname1) {
					public void rollback() { clanname1 = _xdb_saved; }
				};}});
		clanname1 = _v_;
	}

	@Override
	public void setClanname1Octets(com.locojoy.base.Octets _v_) { // 公会名字1 by changhao
		_xdb_verify_unsafe_();
		this.setClanname1(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setClanname2(String _v_) { // 公会名字2 by changhao
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanname2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, clanname2) {
					public void rollback() { clanname2 = _xdb_saved; }
				};}});
		clanname2 = _v_;
	}

	@Override
	public void setClanname2Octets(com.locojoy.base.Octets _v_) { // 公会名字2 by changhao
		_xdb_verify_unsafe_();
		this.setClanname2(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setClanscore1(int _v_) { // 公会1积分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanscore1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanscore1) {
					public void rollback() { clanscore1 = _xdb_saved; }
				};}});
		clanscore1 = _v_;
	}

	@Override
	public void setClanscore2(int _v_) { // 公会2积分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanscore2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanscore2) {
					public void rollback() { clanscore2 = _xdb_saved; }
				};}});
		clanscore2 = _v_;
	}

	@Override
	public void setLastclan1rolesortstamp(long _v_) { // 上次排序时间戳 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastclan1rolesortstamp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastclan1rolesortstamp) {
					public void rollback() { lastclan1rolesortstamp = _xdb_saved; }
				};}});
		lastclan1rolesortstamp = _v_;
	}

	@Override
	public void setLastclan2rolesortstamp(long _v_) { // 上次排序时间戳 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastclan2rolesortstamp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastclan2rolesortstamp) {
					public void rollback() { lastclan2rolesortstamp = _xdb_saved; }
				};}});
		lastclan2rolesortstamp = _v_;
	}

	@Override
	public void setDatetime(long _v_) { // 周几那场比赛时间 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "datetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, datetime) {
					public void rollback() { datetime = _xdb_saved; }
				};}});
		datetime = _v_;
	}

	@Override
	public void setActivitystarttime(long _v_) { // 公会战活动开始时间 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "activitystarttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, activitystarttime) {
					public void rollback() { activitystarttime = _xdb_saved; }
				};}});
		activitystarttime = _v_;
	}

	@Override
	public void setActivitypreparetime(long _v_) { // 公会战准备结束时间 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "activitypreparetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, activitypreparetime) {
					public void rollback() { activitypreparetime = _xdb_saved; }
				};}});
		activitypreparetime = _v_;
	}

	@Override
	public void setActivitynoentertime(long _v_) { // 公会战不能在进入时间 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "activitynoentertime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, activitynoentertime) {
					public void rollback() { activitynoentertime = _xdb_saved; }
				};}});
		activitynoentertime = _v_;
	}

	@Override
	public void setActivityendtime(long _v_) { // 公会战活动结束时间 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "activityendtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, activityendtime) {
					public void rollback() { activityendtime = _xdb_saved; }
				};}});
		activityendtime = _v_;
	}

	@Override
	public void setWhichwin(int _v_) { // -1没结果0是第一个公会1是第二个公会 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "whichwin") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, whichwin) {
					public void rollback() { whichwin = _xdb_saved; }
				};}});
		whichwin = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanFightBattleField _o_ = null;
		if ( _o1_ instanceof ClanFightBattleField ) _o_ = (ClanFightBattleField)_o1_;
		else if ( _o1_ instanceof ClanFightBattleField.Const ) _o_ = ((ClanFightBattleField.Const)_o1_).nThis();
		else return false;
		if (battlefieldid != _o_.battlefieldid) return false;
		if (clanfightid != _o_.clanfightid) return false;
		if (state != _o_.state) return false;
		if (clanid1 != _o_.clanid1) return false;
		if (clanid2 != _o_.clanid2) return false;
		if (!clanname1.equals(_o_.clanname1)) return false;
		if (!clanname2.equals(_o_.clanname2)) return false;
		if (clanscore1 != _o_.clanscore1) return false;
		if (clanscore2 != _o_.clanscore2) return false;
		if (!clan1roleids.equals(_o_.clan1roleids)) return false;
		if (!clan2roleids.equals(_o_.clan2roleids)) return false;
		if (!enterroleids.equals(_o_.enterroleids)) return false;
		if (!clan1enterroleids.equals(_o_.clan1enterroleids)) return false;
		if (!clan2enterroleids.equals(_o_.clan2enterroleids)) return false;
		if (!clan1roleranklist.equals(_o_.clan1roleranklist)) return false;
		if (!clan2roleranklist.equals(_o_.clan2roleranklist)) return false;
		if (!clan1rolerankmap.equals(_o_.clan1rolerankmap)) return false;
		if (!clan2rolerankmap.equals(_o_.clan2rolerankmap)) return false;
		if (lastclan1rolesortstamp != _o_.lastclan1rolesortstamp) return false;
		if (lastclan2rolesortstamp != _o_.lastclan2rolesortstamp) return false;
		if (!victoryboxids.equals(_o_.victoryboxids)) return false;
		if (!celebrateboxids.equals(_o_.celebrateboxids)) return false;
		if (datetime != _o_.datetime) return false;
		if (activitystarttime != _o_.activitystarttime) return false;
		if (activitypreparetime != _o_.activitypreparetime) return false;
		if (activitynoentertime != _o_.activitynoentertime) return false;
		if (activityendtime != _o_.activityendtime) return false;
		if (whichwin != _o_.whichwin) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += battlefieldid;
		_h_ += clanfightid;
		_h_ += state;
		_h_ += clanid1;
		_h_ += clanid2;
		_h_ += clanname1.hashCode();
		_h_ += clanname2.hashCode();
		_h_ += clanscore1;
		_h_ += clanscore2;
		_h_ += clan1roleids.hashCode();
		_h_ += clan2roleids.hashCode();
		_h_ += enterroleids.hashCode();
		_h_ += clan1enterroleids.hashCode();
		_h_ += clan2enterroleids.hashCode();
		_h_ += clan1roleranklist.hashCode();
		_h_ += clan2roleranklist.hashCode();
		_h_ += clan1rolerankmap.hashCode();
		_h_ += clan2rolerankmap.hashCode();
		_h_ += lastclan1rolesortstamp;
		_h_ += lastclan2rolesortstamp;
		_h_ += victoryboxids.hashCode();
		_h_ += celebrateboxids.hashCode();
		_h_ += datetime;
		_h_ += activitystarttime;
		_h_ += activitypreparetime;
		_h_ += activitynoentertime;
		_h_ += activityendtime;
		_h_ += whichwin;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(battlefieldid);
		_sb_.append(",");
		_sb_.append(clanfightid);
		_sb_.append(",");
		_sb_.append(state);
		_sb_.append(",");
		_sb_.append(clanid1);
		_sb_.append(",");
		_sb_.append(clanid2);
		_sb_.append(",");
		_sb_.append("'").append(clanname1).append("'");
		_sb_.append(",");
		_sb_.append("'").append(clanname2).append("'");
		_sb_.append(",");
		_sb_.append(clanscore1);
		_sb_.append(",");
		_sb_.append(clanscore2);
		_sb_.append(",");
		_sb_.append(clan1roleids);
		_sb_.append(",");
		_sb_.append(clan2roleids);
		_sb_.append(",");
		_sb_.append(enterroleids);
		_sb_.append(",");
		_sb_.append(clan1enterroleids);
		_sb_.append(",");
		_sb_.append(clan2enterroleids);
		_sb_.append(",");
		_sb_.append(clan1roleranklist);
		_sb_.append(",");
		_sb_.append(clan2roleranklist);
		_sb_.append(",");
		_sb_.append(clan1rolerankmap);
		_sb_.append(",");
		_sb_.append(clan2rolerankmap);
		_sb_.append(",");
		_sb_.append(lastclan1rolesortstamp);
		_sb_.append(",");
		_sb_.append(lastclan2rolesortstamp);
		_sb_.append(",");
		_sb_.append(victoryboxids);
		_sb_.append(",");
		_sb_.append(celebrateboxids);
		_sb_.append(",");
		_sb_.append(datetime);
		_sb_.append(",");
		_sb_.append(activitystarttime);
		_sb_.append(",");
		_sb_.append(activitypreparetime);
		_sb_.append(",");
		_sb_.append(activitynoentertime);
		_sb_.append(",");
		_sb_.append(activityendtime);
		_sb_.append(",");
		_sb_.append(whichwin);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battlefieldid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanfightid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("state"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanid1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanid2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanname1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanname2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanscore1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanscore2"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("clan1roleids"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("clan2roleids"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("enterroleids"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("clan1enterroleids"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("clan2enterroleids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clan1roleranklist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clan2roleranklist"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("clan1rolerankmap"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("clan2rolerankmap"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastclan1rolesortstamp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastclan2rolesortstamp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("victoryboxids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("celebrateboxids"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("datetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("activitystarttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("activitypreparetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("activitynoentertime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("activityendtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("whichwin"));
		return lb;
	}

	private class Const implements xbean.ClanFightBattleField {
		ClanFightBattleField nThis() {
			return ClanFightBattleField.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanFightBattleField copy() {
			return ClanFightBattleField.this.copy();
		}

		@Override
		public xbean.ClanFightBattleField toData() {
			return ClanFightBattleField.this.toData();
		}

		public xbean.ClanFightBattleField toBean() {
			return ClanFightBattleField.this.toBean();
		}

		@Override
		public xbean.ClanFightBattleField toDataIf() {
			return ClanFightBattleField.this.toDataIf();
		}

		public xbean.ClanFightBattleField toBeanIf() {
			return ClanFightBattleField.this.toBeanIf();
		}

		@Override
		public int getBattlefieldid() { // excel表里的ID by changhao
			_xdb_verify_unsafe_();
			return battlefieldid;
		}

		@Override
		public long getClanfightid() { // 公会战id by changhao
			_xdb_verify_unsafe_();
			return clanfightid;
		}

		@Override
		public int getState() { // 战场状态 by changhao
			_xdb_verify_unsafe_();
			return state;
		}

		@Override
		public long getClanid1() { // 公会1 by changhao
			_xdb_verify_unsafe_();
			return clanid1;
		}

		@Override
		public long getClanid2() { // 公会2 by changhao
			_xdb_verify_unsafe_();
			return clanid2;
		}

		@Override
		public String getClanname1() { // 公会名字1 by changhao
			_xdb_verify_unsafe_();
			return clanname1;
		}

		@Override
		public com.locojoy.base.Octets getClanname1Octets() { // 公会名字1 by changhao
			_xdb_verify_unsafe_();
			return ClanFightBattleField.this.getClanname1Octets();
		}

		@Override
		public String getClanname2() { // 公会名字2 by changhao
			_xdb_verify_unsafe_();
			return clanname2;
		}

		@Override
		public com.locojoy.base.Octets getClanname2Octets() { // 公会名字2 by changhao
			_xdb_verify_unsafe_();
			return ClanFightBattleField.this.getClanname2Octets();
		}

		@Override
		public int getClanscore1() { // 公会1积分 by changhao
			_xdb_verify_unsafe_();
			return clanscore1;
		}

		@Override
		public int getClanscore2() { // 公会2积分 by changhao
			_xdb_verify_unsafe_();
			return clanscore2;
		}

		@Override
		public java.util.Map<Long, Integer> getClan1roleids() { // 进入公会战场的 公会1玩家 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(clan1roleids);
		}

		@Override
		public java.util.Map<Long, Integer> getClan1roleidsAsData() { // 进入公会战场的 公会1玩家 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> clan1roleids;
			ClanFightBattleField _o_ = ClanFightBattleField.this;
			clan1roleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1roleids.entrySet())
				clan1roleids.put(_e_.getKey(), _e_.getValue());
			return clan1roleids;
		}

		@Override
		public java.util.Map<Long, Integer> getClan2roleids() { // 进入公会战场的 公会2玩家 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(clan2roleids);
		}

		@Override
		public java.util.Map<Long, Integer> getClan2roleidsAsData() { // 进入公会战场的 公会2玩家 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> clan2roleids;
			ClanFightBattleField _o_ = ClanFightBattleField.this;
			clan2roleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2roleids.entrySet())
				clan2roleids.put(_e_.getKey(), _e_.getValue());
			return clan2roleids;
		}

		@Override
		public java.util.Map<Long, String> getEnterroleids() { // 进入过战场的角色id by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(enterroleids);
		}

		@Override
		public java.util.Map<Long, String> getEnterroleidsAsData() { // 进入过战场的角色id by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, String> enterroleids;
			ClanFightBattleField _o_ = ClanFightBattleField.this;
			enterroleids = new java.util.HashMap<Long, String>();
			for (java.util.Map.Entry<Long, String> _e_ : _o_.enterroleids.entrySet())
				enterroleids.put(_e_.getKey(), _e_.getValue());
			return enterroleids;
		}

		@Override
		public java.util.Map<Long, Integer> getClan1enterroleids() { // 公会1战场进入过的玩家 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(clan1enterroleids);
		}

		@Override
		public java.util.Map<Long, Integer> getClan1enterroleidsAsData() { // 公会1战场进入过的玩家 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> clan1enterroleids;
			ClanFightBattleField _o_ = ClanFightBattleField.this;
			clan1enterroleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1enterroleids.entrySet())
				clan1enterroleids.put(_e_.getKey(), _e_.getValue());
			return clan1enterroleids;
		}

		@Override
		public java.util.Map<Long, Integer> getClan2enterroleids() { // 公会2战场进入过的玩家 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(clan2enterroleids);
		}

		@Override
		public java.util.Map<Long, Integer> getClan2enterroleidsAsData() { // 公会2战场进入过的玩家 by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> clan2enterroleids;
			ClanFightBattleField _o_ = ClanFightBattleField.this;
			clan2enterroleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2enterroleids.entrySet())
				clan2enterroleids.put(_e_.getKey(), _e_.getValue());
			return clan2enterroleids;
		}

		@Override
		public java.util.List<xbean.BattleFieldScroeRank> getClan1roleranklist() { // 当前战场排名1 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(clan1roleranklist);
		}

		public java.util.List<xbean.BattleFieldScroeRank> getClan1roleranklistAsData() { // 当前战场排名1 by changhao
			_xdb_verify_unsafe_();
			java.util.List<xbean.BattleFieldScroeRank> clan1roleranklist;
			ClanFightBattleField _o_ = ClanFightBattleField.this;
		clan1roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
		for (xbean.BattleFieldScroeRank _v_ : _o_.clan1roleranklist)
			clan1roleranklist.add(new BattleFieldScroeRank.Data(_v_));
			return clan1roleranklist;
		}

		@Override
		public java.util.List<xbean.BattleFieldScroeRank> getClan2roleranklist() { // 当前战场排名2 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(clan2roleranklist);
		}

		public java.util.List<xbean.BattleFieldScroeRank> getClan2roleranklistAsData() { // 当前战场排名2 by changhao
			_xdb_verify_unsafe_();
			java.util.List<xbean.BattleFieldScroeRank> clan2roleranklist;
			ClanFightBattleField _o_ = ClanFightBattleField.this;
		clan2roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
		for (xbean.BattleFieldScroeRank _v_ : _o_.clan2roleranklist)
			clan2roleranklist.add(new BattleFieldScroeRank.Data(_v_));
			return clan2roleranklist;
		}

		@Override
		public java.util.Map<Long, Integer> getClan1rolerankmap() { // key是角色id value是排名(从0开始) by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(clan1rolerankmap);
		}

		@Override
		public java.util.Map<Long, Integer> getClan1rolerankmapAsData() { // key是角色id value是排名(从0开始) by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> clan1rolerankmap;
			ClanFightBattleField _o_ = ClanFightBattleField.this;
			clan1rolerankmap = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1rolerankmap.entrySet())
				clan1rolerankmap.put(_e_.getKey(), _e_.getValue());
			return clan1rolerankmap;
		}

		@Override
		public java.util.Map<Long, Integer> getClan2rolerankmap() { // key是角色id value是排名(从0开始) by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(clan2rolerankmap);
		}

		@Override
		public java.util.Map<Long, Integer> getClan2rolerankmapAsData() { // key是角色id value是排名(从0开始) by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Long, Integer> clan2rolerankmap;
			ClanFightBattleField _o_ = ClanFightBattleField.this;
			clan2rolerankmap = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2rolerankmap.entrySet())
				clan2rolerankmap.put(_e_.getKey(), _e_.getValue());
			return clan2rolerankmap;
		}

		@Override
		public long getLastclan1rolesortstamp() { // 上次排序时间戳 by changhao
			_xdb_verify_unsafe_();
			return lastclan1rolesortstamp;
		}

		@Override
		public long getLastclan2rolesortstamp() { // 上次排序时间戳 by changhao
			_xdb_verify_unsafe_();
			return lastclan2rolesortstamp;
		}

		@Override
		public java.util.List<Long> getVictoryboxids() { // 胜利宝箱链表 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(victoryboxids);
		}

		public java.util.List<Long> getVictoryboxidsAsData() { // 胜利宝箱链表 by changhao
			_xdb_verify_unsafe_();
			java.util.List<Long> victoryboxids;
			ClanFightBattleField _o_ = ClanFightBattleField.this;
		victoryboxids = new java.util.LinkedList<Long>();
		victoryboxids.addAll(_o_.victoryboxids);
			return victoryboxids;
		}

		@Override
		public java.util.List<Long> getCelebrateboxids() { // 庆祝宝箱链表 by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(celebrateboxids);
		}

		public java.util.List<Long> getCelebrateboxidsAsData() { // 庆祝宝箱链表 by changhao
			_xdb_verify_unsafe_();
			java.util.List<Long> celebrateboxids;
			ClanFightBattleField _o_ = ClanFightBattleField.this;
		celebrateboxids = new java.util.LinkedList<Long>();
		celebrateboxids.addAll(_o_.celebrateboxids);
			return celebrateboxids;
		}

		@Override
		public long getDatetime() { // 周几那场比赛时间 by changhao
			_xdb_verify_unsafe_();
			return datetime;
		}

		@Override
		public long getActivitystarttime() { // 公会战活动开始时间 by changhao
			_xdb_verify_unsafe_();
			return activitystarttime;
		}

		@Override
		public long getActivitypreparetime() { // 公会战准备结束时间 by changhao
			_xdb_verify_unsafe_();
			return activitypreparetime;
		}

		@Override
		public long getActivitynoentertime() { // 公会战不能在进入时间 by changhao
			_xdb_verify_unsafe_();
			return activitynoentertime;
		}

		@Override
		public long getActivityendtime() { // 公会战活动结束时间 by changhao
			_xdb_verify_unsafe_();
			return activityendtime;
		}

		@Override
		public int getWhichwin() { // -1没结果0是第一个公会1是第二个公会 by changhao
			_xdb_verify_unsafe_();
			return whichwin;
		}

		@Override
		public void setBattlefieldid(int _v_) { // excel表里的ID by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanfightid(long _v_) { // 公会战id by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setState(int _v_) { // 战场状态 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanid1(long _v_) { // 公会1 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanid2(long _v_) { // 公会2 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanname1(String _v_) { // 公会名字1 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanname1Octets(com.locojoy.base.Octets _v_) { // 公会名字1 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanname2(String _v_) { // 公会名字2 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanname2Octets(com.locojoy.base.Octets _v_) { // 公会名字2 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanscore1(int _v_) { // 公会1积分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanscore2(int _v_) { // 公会2积分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastclan1rolesortstamp(long _v_) { // 上次排序时间戳 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastclan2rolesortstamp(long _v_) { // 上次排序时间戳 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDatetime(long _v_) { // 周几那场比赛时间 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setActivitystarttime(long _v_) { // 公会战活动开始时间 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setActivitypreparetime(long _v_) { // 公会战准备结束时间 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setActivitynoentertime(long _v_) { // 公会战不能在进入时间 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setActivityendtime(long _v_) { // 公会战活动结束时间 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWhichwin(int _v_) { // -1没结果0是第一个公会1是第二个公会 by changhao
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
			return ClanFightBattleField.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanFightBattleField.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanFightBattleField.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanFightBattleField.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanFightBattleField.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanFightBattleField.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanFightBattleField.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanFightBattleField.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanFightBattleField.this.toString();
		}

	}

	public static final class Data implements xbean.ClanFightBattleField {
		private int battlefieldid; // excel表里的ID by changhao
		private long clanfightid; // 公会战id by changhao
		private int state; // 战场状态 by changhao
		private long clanid1; // 公会1 by changhao
		private long clanid2; // 公会2 by changhao
		private String clanname1; // 公会名字1 by changhao
		private String clanname2; // 公会名字2 by changhao
		private int clanscore1; // 公会1积分 by changhao
		private int clanscore2; // 公会2积分 by changhao
		private java.util.HashMap<Long, Integer> clan1roleids; // 进入公会战场的 公会1玩家 by changhao
		private java.util.HashMap<Long, Integer> clan2roleids; // 进入公会战场的 公会2玩家 by changhao
		private java.util.HashMap<Long, String> enterroleids; // 进入过战场的角色id by changhao
		private java.util.HashMap<Long, Integer> clan1enterroleids; // 公会1战场进入过的玩家 by changhao
		private java.util.HashMap<Long, Integer> clan2enterroleids; // 公会2战场进入过的玩家 by changhao
		private java.util.LinkedList<xbean.BattleFieldScroeRank> clan1roleranklist; // 当前战场排名1 by changhao
		private java.util.LinkedList<xbean.BattleFieldScroeRank> clan2roleranklist; // 当前战场排名2 by changhao
		private java.util.HashMap<Long, Integer> clan1rolerankmap; // key是角色id value是排名(从0开始) by changhao
		private java.util.HashMap<Long, Integer> clan2rolerankmap; // key是角色id value是排名(从0开始) by changhao
		private long lastclan1rolesortstamp; // 上次排序时间戳 by changhao
		private long lastclan2rolesortstamp; // 上次排序时间戳 by changhao
		private java.util.LinkedList<Long> victoryboxids; // 胜利宝箱链表 by changhao
		private java.util.LinkedList<Long> celebrateboxids; // 庆祝宝箱链表 by changhao
		private long datetime; // 周几那场比赛时间 by changhao
		private long activitystarttime; // 公会战活动开始时间 by changhao
		private long activitypreparetime; // 公会战准备结束时间 by changhao
		private long activitynoentertime; // 公会战不能在进入时间 by changhao
		private long activityendtime; // 公会战活动结束时间 by changhao
		private int whichwin; // -1没结果0是第一个公会1是第二个公会 by changhao

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			clanname1 = "";
			clanname2 = "";
			clan1roleids = new java.util.HashMap<Long, Integer>();
			clan2roleids = new java.util.HashMap<Long, Integer>();
			enterroleids = new java.util.HashMap<Long, String>();
			clan1enterroleids = new java.util.HashMap<Long, Integer>();
			clan2enterroleids = new java.util.HashMap<Long, Integer>();
			clan1roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
			clan2roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
			clan1rolerankmap = new java.util.HashMap<Long, Integer>();
			clan2rolerankmap = new java.util.HashMap<Long, Integer>();
			victoryboxids = new java.util.LinkedList<Long>();
			celebrateboxids = new java.util.LinkedList<Long>();
		}

		Data(xbean.ClanFightBattleField _o1_) {
			if (_o1_ instanceof ClanFightBattleField) assign((ClanFightBattleField)_o1_);
			else if (_o1_ instanceof ClanFightBattleField.Data) assign((ClanFightBattleField.Data)_o1_);
			else if (_o1_ instanceof ClanFightBattleField.Const) assign(((ClanFightBattleField.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanFightBattleField _o_) {
			battlefieldid = _o_.battlefieldid;
			clanfightid = _o_.clanfightid;
			state = _o_.state;
			clanid1 = _o_.clanid1;
			clanid2 = _o_.clanid2;
			clanname1 = _o_.clanname1;
			clanname2 = _o_.clanname2;
			clanscore1 = _o_.clanscore1;
			clanscore2 = _o_.clanscore2;
			clan1roleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1roleids.entrySet())
				clan1roleids.put(_e_.getKey(), _e_.getValue());
			clan2roleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2roleids.entrySet())
				clan2roleids.put(_e_.getKey(), _e_.getValue());
			enterroleids = new java.util.HashMap<Long, String>();
			for (java.util.Map.Entry<Long, String> _e_ : _o_.enterroleids.entrySet())
				enterroleids.put(_e_.getKey(), _e_.getValue());
			clan1enterroleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1enterroleids.entrySet())
				clan1enterroleids.put(_e_.getKey(), _e_.getValue());
			clan2enterroleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2enterroleids.entrySet())
				clan2enterroleids.put(_e_.getKey(), _e_.getValue());
			clan1roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
			for (xbean.BattleFieldScroeRank _v_ : _o_.clan1roleranklist)
				clan1roleranklist.add(new BattleFieldScroeRank.Data(_v_));
			clan2roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
			for (xbean.BattleFieldScroeRank _v_ : _o_.clan2roleranklist)
				clan2roleranklist.add(new BattleFieldScroeRank.Data(_v_));
			clan1rolerankmap = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1rolerankmap.entrySet())
				clan1rolerankmap.put(_e_.getKey(), _e_.getValue());
			clan2rolerankmap = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2rolerankmap.entrySet())
				clan2rolerankmap.put(_e_.getKey(), _e_.getValue());
			lastclan1rolesortstamp = _o_.lastclan1rolesortstamp;
			lastclan2rolesortstamp = _o_.lastclan2rolesortstamp;
			victoryboxids = new java.util.LinkedList<Long>();
			victoryboxids.addAll(_o_.victoryboxids);
			celebrateboxids = new java.util.LinkedList<Long>();
			celebrateboxids.addAll(_o_.celebrateboxids);
			datetime = _o_.datetime;
			activitystarttime = _o_.activitystarttime;
			activitypreparetime = _o_.activitypreparetime;
			activitynoentertime = _o_.activitynoentertime;
			activityendtime = _o_.activityendtime;
			whichwin = _o_.whichwin;
		}

		private void assign(ClanFightBattleField.Data _o_) {
			battlefieldid = _o_.battlefieldid;
			clanfightid = _o_.clanfightid;
			state = _o_.state;
			clanid1 = _o_.clanid1;
			clanid2 = _o_.clanid2;
			clanname1 = _o_.clanname1;
			clanname2 = _o_.clanname2;
			clanscore1 = _o_.clanscore1;
			clanscore2 = _o_.clanscore2;
			clan1roleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1roleids.entrySet())
				clan1roleids.put(_e_.getKey(), _e_.getValue());
			clan2roleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2roleids.entrySet())
				clan2roleids.put(_e_.getKey(), _e_.getValue());
			enterroleids = new java.util.HashMap<Long, String>();
			for (java.util.Map.Entry<Long, String> _e_ : _o_.enterroleids.entrySet())
				enterroleids.put(_e_.getKey(), _e_.getValue());
			clan1enterroleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1enterroleids.entrySet())
				clan1enterroleids.put(_e_.getKey(), _e_.getValue());
			clan2enterroleids = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2enterroleids.entrySet())
				clan2enterroleids.put(_e_.getKey(), _e_.getValue());
			clan1roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
			for (xbean.BattleFieldScroeRank _v_ : _o_.clan1roleranklist)
				clan1roleranklist.add(new BattleFieldScroeRank.Data(_v_));
			clan2roleranklist = new java.util.LinkedList<xbean.BattleFieldScroeRank>();
			for (xbean.BattleFieldScroeRank _v_ : _o_.clan2roleranklist)
				clan2roleranklist.add(new BattleFieldScroeRank.Data(_v_));
			clan1rolerankmap = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan1rolerankmap.entrySet())
				clan1rolerankmap.put(_e_.getKey(), _e_.getValue());
			clan2rolerankmap = new java.util.HashMap<Long, Integer>();
			for (java.util.Map.Entry<Long, Integer> _e_ : _o_.clan2rolerankmap.entrySet())
				clan2rolerankmap.put(_e_.getKey(), _e_.getValue());
			lastclan1rolesortstamp = _o_.lastclan1rolesortstamp;
			lastclan2rolesortstamp = _o_.lastclan2rolesortstamp;
			victoryboxids = new java.util.LinkedList<Long>();
			victoryboxids.addAll(_o_.victoryboxids);
			celebrateboxids = new java.util.LinkedList<Long>();
			celebrateboxids.addAll(_o_.celebrateboxids);
			datetime = _o_.datetime;
			activitystarttime = _o_.activitystarttime;
			activitypreparetime = _o_.activitypreparetime;
			activitynoentertime = _o_.activitynoentertime;
			activityendtime = _o_.activityendtime;
			whichwin = _o_.whichwin;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(battlefieldid);
			_os_.marshal(clanfightid);
			_os_.marshal(state);
			_os_.marshal(clanid1);
			_os_.marshal(clanid2);
			_os_.marshal(clanname1, mkdb.Const.IO_CHARSET);
			_os_.marshal(clanname2, mkdb.Const.IO_CHARSET);
			_os_.marshal(clanscore1);
			_os_.marshal(clanscore2);
			_os_.compact_uint32(clan1roleids.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : clan1roleids.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(clan2roleids.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : clan2roleids.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(enterroleids.size());
			for (java.util.Map.Entry<Long, String> _e_ : enterroleids.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue(), mkdb.Const.IO_CHARSET);
			}
			_os_.compact_uint32(clan1enterroleids.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : clan1enterroleids.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(clan2enterroleids.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : clan2enterroleids.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(clan1roleranklist.size());
			for (xbean.BattleFieldScroeRank _v_ : clan1roleranklist) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(clan2roleranklist.size());
			for (xbean.BattleFieldScroeRank _v_ : clan2roleranklist) {
				_v_.marshal(_os_);
			}
			_os_.compact_uint32(clan1rolerankmap.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : clan1rolerankmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(clan2rolerankmap.size());
			for (java.util.Map.Entry<Long, Integer> _e_ : clan2rolerankmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(lastclan1rolesortstamp);
			_os_.marshal(lastclan2rolesortstamp);
			_os_.compact_uint32(victoryboxids.size());
			for (Long _v_ : victoryboxids) {
				_os_.marshal(_v_);
			}
			_os_.compact_uint32(celebrateboxids.size());
			for (Long _v_ : celebrateboxids) {
				_os_.marshal(_v_);
			}
			_os_.marshal(datetime);
			_os_.marshal(activitystarttime);
			_os_.marshal(activitypreparetime);
			_os_.marshal(activitynoentertime);
			_os_.marshal(activityendtime);
			_os_.marshal(whichwin);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			battlefieldid = _os_.unmarshal_int();
			clanfightid = _os_.unmarshal_long();
			state = _os_.unmarshal_int();
			clanid1 = _os_.unmarshal_long();
			clanid2 = _os_.unmarshal_long();
			clanname1 = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			clanname2 = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			clanscore1 = _os_.unmarshal_int();
			clanscore2 = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					clan1roleids = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					clan1roleids.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					clan2roleids = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					clan2roleids.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					enterroleids = new java.util.HashMap<Long, String>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					String _v_ = "";
					_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
					enterroleids.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					clan1enterroleids = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					clan1enterroleids.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					clan2enterroleids = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					clan2enterroleids.put(_k_, _v_);
				}
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.BattleFieldScroeRank _v_ = xbean.Pod.newBattleFieldScroeRankData();
				_v_.unmarshal(_os_);
				clan1roleranklist.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.BattleFieldScroeRank _v_ = xbean.Pod.newBattleFieldScroeRankData();
				_v_.unmarshal(_os_);
				clan2roleranklist.add(_v_);
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					clan1rolerankmap = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					clan1rolerankmap.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					clan2rolerankmap = new java.util.HashMap<Long, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					clan2rolerankmap.put(_k_, _v_);
				}
			}
			lastclan1rolesortstamp = _os_.unmarshal_long();
			lastclan2rolesortstamp = _os_.unmarshal_long();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				victoryboxids.add(_v_);
			}
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				celebrateboxids.add(_v_);
			}
			datetime = _os_.unmarshal_long();
			activitystarttime = _os_.unmarshal_long();
			activitypreparetime = _os_.unmarshal_long();
			activitynoentertime = _os_.unmarshal_long();
			activityendtime = _os_.unmarshal_long();
			whichwin = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ClanFightBattleField copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanFightBattleField toData() {
			return new Data(this);
		}

		public xbean.ClanFightBattleField toBean() {
			return new ClanFightBattleField(this, null, null);
		}

		@Override
		public xbean.ClanFightBattleField toDataIf() {
			return this;
		}

		public xbean.ClanFightBattleField toBeanIf() {
			return new ClanFightBattleField(this, null, null);
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
		public int getBattlefieldid() { // excel表里的ID by changhao
			return battlefieldid;
		}

		@Override
		public long getClanfightid() { // 公会战id by changhao
			return clanfightid;
		}

		@Override
		public int getState() { // 战场状态 by changhao
			return state;
		}

		@Override
		public long getClanid1() { // 公会1 by changhao
			return clanid1;
		}

		@Override
		public long getClanid2() { // 公会2 by changhao
			return clanid2;
		}

		@Override
		public String getClanname1() { // 公会名字1 by changhao
			return clanname1;
		}

		@Override
		public com.locojoy.base.Octets getClanname1Octets() { // 公会名字1 by changhao
			return com.locojoy.base.Octets.wrap(getClanname1(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getClanname2() { // 公会名字2 by changhao
			return clanname2;
		}

		@Override
		public com.locojoy.base.Octets getClanname2Octets() { // 公会名字2 by changhao
			return com.locojoy.base.Octets.wrap(getClanname2(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getClanscore1() { // 公会1积分 by changhao
			return clanscore1;
		}

		@Override
		public int getClanscore2() { // 公会2积分 by changhao
			return clanscore2;
		}

		@Override
		public java.util.Map<Long, Integer> getClan1roleids() { // 进入公会战场的 公会1玩家 by changhao
			return clan1roleids;
		}

		@Override
		public java.util.Map<Long, Integer> getClan1roleidsAsData() { // 进入公会战场的 公会1玩家 by changhao
			return clan1roleids;
		}

		@Override
		public java.util.Map<Long, Integer> getClan2roleids() { // 进入公会战场的 公会2玩家 by changhao
			return clan2roleids;
		}

		@Override
		public java.util.Map<Long, Integer> getClan2roleidsAsData() { // 进入公会战场的 公会2玩家 by changhao
			return clan2roleids;
		}

		@Override
		public java.util.Map<Long, String> getEnterroleids() { // 进入过战场的角色id by changhao
			return enterroleids;
		}

		@Override
		public java.util.Map<Long, String> getEnterroleidsAsData() { // 进入过战场的角色id by changhao
			return enterroleids;
		}

		@Override
		public java.util.Map<Long, Integer> getClan1enterroleids() { // 公会1战场进入过的玩家 by changhao
			return clan1enterroleids;
		}

		@Override
		public java.util.Map<Long, Integer> getClan1enterroleidsAsData() { // 公会1战场进入过的玩家 by changhao
			return clan1enterroleids;
		}

		@Override
		public java.util.Map<Long, Integer> getClan2enterroleids() { // 公会2战场进入过的玩家 by changhao
			return clan2enterroleids;
		}

		@Override
		public java.util.Map<Long, Integer> getClan2enterroleidsAsData() { // 公会2战场进入过的玩家 by changhao
			return clan2enterroleids;
		}

		@Override
		public java.util.List<xbean.BattleFieldScroeRank> getClan1roleranklist() { // 当前战场排名1 by changhao
			return clan1roleranklist;
		}

		@Override
		public java.util.List<xbean.BattleFieldScroeRank> getClan1roleranklistAsData() { // 当前战场排名1 by changhao
			return clan1roleranklist;
		}

		@Override
		public java.util.List<xbean.BattleFieldScroeRank> getClan2roleranklist() { // 当前战场排名2 by changhao
			return clan2roleranklist;
		}

		@Override
		public java.util.List<xbean.BattleFieldScroeRank> getClan2roleranklistAsData() { // 当前战场排名2 by changhao
			return clan2roleranklist;
		}

		@Override
		public java.util.Map<Long, Integer> getClan1rolerankmap() { // key是角色id value是排名(从0开始) by changhao
			return clan1rolerankmap;
		}

		@Override
		public java.util.Map<Long, Integer> getClan1rolerankmapAsData() { // key是角色id value是排名(从0开始) by changhao
			return clan1rolerankmap;
		}

		@Override
		public java.util.Map<Long, Integer> getClan2rolerankmap() { // key是角色id value是排名(从0开始) by changhao
			return clan2rolerankmap;
		}

		@Override
		public java.util.Map<Long, Integer> getClan2rolerankmapAsData() { // key是角色id value是排名(从0开始) by changhao
			return clan2rolerankmap;
		}

		@Override
		public long getLastclan1rolesortstamp() { // 上次排序时间戳 by changhao
			return lastclan1rolesortstamp;
		}

		@Override
		public long getLastclan2rolesortstamp() { // 上次排序时间戳 by changhao
			return lastclan2rolesortstamp;
		}

		@Override
		public java.util.List<Long> getVictoryboxids() { // 胜利宝箱链表 by changhao
			return victoryboxids;
		}

		@Override
		public java.util.List<Long> getVictoryboxidsAsData() { // 胜利宝箱链表 by changhao
			return victoryboxids;
		}

		@Override
		public java.util.List<Long> getCelebrateboxids() { // 庆祝宝箱链表 by changhao
			return celebrateboxids;
		}

		@Override
		public java.util.List<Long> getCelebrateboxidsAsData() { // 庆祝宝箱链表 by changhao
			return celebrateboxids;
		}

		@Override
		public long getDatetime() { // 周几那场比赛时间 by changhao
			return datetime;
		}

		@Override
		public long getActivitystarttime() { // 公会战活动开始时间 by changhao
			return activitystarttime;
		}

		@Override
		public long getActivitypreparetime() { // 公会战准备结束时间 by changhao
			return activitypreparetime;
		}

		@Override
		public long getActivitynoentertime() { // 公会战不能在进入时间 by changhao
			return activitynoentertime;
		}

		@Override
		public long getActivityendtime() { // 公会战活动结束时间 by changhao
			return activityendtime;
		}

		@Override
		public int getWhichwin() { // -1没结果0是第一个公会1是第二个公会 by changhao
			return whichwin;
		}

		@Override
		public void setBattlefieldid(int _v_) { // excel表里的ID by changhao
			battlefieldid = _v_;
		}

		@Override
		public void setClanfightid(long _v_) { // 公会战id by changhao
			clanfightid = _v_;
		}

		@Override
		public void setState(int _v_) { // 战场状态 by changhao
			state = _v_;
		}

		@Override
		public void setClanid1(long _v_) { // 公会1 by changhao
			clanid1 = _v_;
		}

		@Override
		public void setClanid2(long _v_) { // 公会2 by changhao
			clanid2 = _v_;
		}

		@Override
		public void setClanname1(String _v_) { // 公会名字1 by changhao
			if (null == _v_)
				throw new NullPointerException();
			clanname1 = _v_;
		}

		@Override
		public void setClanname1Octets(com.locojoy.base.Octets _v_) { // 公会名字1 by changhao
			this.setClanname1(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setClanname2(String _v_) { // 公会名字2 by changhao
			if (null == _v_)
				throw new NullPointerException();
			clanname2 = _v_;
		}

		@Override
		public void setClanname2Octets(com.locojoy.base.Octets _v_) { // 公会名字2 by changhao
			this.setClanname2(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setClanscore1(int _v_) { // 公会1积分 by changhao
			clanscore1 = _v_;
		}

		@Override
		public void setClanscore2(int _v_) { // 公会2积分 by changhao
			clanscore2 = _v_;
		}

		@Override
		public void setLastclan1rolesortstamp(long _v_) { // 上次排序时间戳 by changhao
			lastclan1rolesortstamp = _v_;
		}

		@Override
		public void setLastclan2rolesortstamp(long _v_) { // 上次排序时间戳 by changhao
			lastclan2rolesortstamp = _v_;
		}

		@Override
		public void setDatetime(long _v_) { // 周几那场比赛时间 by changhao
			datetime = _v_;
		}

		@Override
		public void setActivitystarttime(long _v_) { // 公会战活动开始时间 by changhao
			activitystarttime = _v_;
		}

		@Override
		public void setActivitypreparetime(long _v_) { // 公会战准备结束时间 by changhao
			activitypreparetime = _v_;
		}

		@Override
		public void setActivitynoentertime(long _v_) { // 公会战不能在进入时间 by changhao
			activitynoentertime = _v_;
		}

		@Override
		public void setActivityendtime(long _v_) { // 公会战活动结束时间 by changhao
			activityendtime = _v_;
		}

		@Override
		public void setWhichwin(int _v_) { // -1没结果0是第一个公会1是第二个公会 by changhao
			whichwin = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanFightBattleField.Data)) return false;
			ClanFightBattleField.Data _o_ = (ClanFightBattleField.Data) _o1_;
			if (battlefieldid != _o_.battlefieldid) return false;
			if (clanfightid != _o_.clanfightid) return false;
			if (state != _o_.state) return false;
			if (clanid1 != _o_.clanid1) return false;
			if (clanid2 != _o_.clanid2) return false;
			if (!clanname1.equals(_o_.clanname1)) return false;
			if (!clanname2.equals(_o_.clanname2)) return false;
			if (clanscore1 != _o_.clanscore1) return false;
			if (clanscore2 != _o_.clanscore2) return false;
			if (!clan1roleids.equals(_o_.clan1roleids)) return false;
			if (!clan2roleids.equals(_o_.clan2roleids)) return false;
			if (!enterroleids.equals(_o_.enterroleids)) return false;
			if (!clan1enterroleids.equals(_o_.clan1enterroleids)) return false;
			if (!clan2enterroleids.equals(_o_.clan2enterroleids)) return false;
			if (!clan1roleranklist.equals(_o_.clan1roleranklist)) return false;
			if (!clan2roleranklist.equals(_o_.clan2roleranklist)) return false;
			if (!clan1rolerankmap.equals(_o_.clan1rolerankmap)) return false;
			if (!clan2rolerankmap.equals(_o_.clan2rolerankmap)) return false;
			if (lastclan1rolesortstamp != _o_.lastclan1rolesortstamp) return false;
			if (lastclan2rolesortstamp != _o_.lastclan2rolesortstamp) return false;
			if (!victoryboxids.equals(_o_.victoryboxids)) return false;
			if (!celebrateboxids.equals(_o_.celebrateboxids)) return false;
			if (datetime != _o_.datetime) return false;
			if (activitystarttime != _o_.activitystarttime) return false;
			if (activitypreparetime != _o_.activitypreparetime) return false;
			if (activitynoentertime != _o_.activitynoentertime) return false;
			if (activityendtime != _o_.activityendtime) return false;
			if (whichwin != _o_.whichwin) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += battlefieldid;
			_h_ += clanfightid;
			_h_ += state;
			_h_ += clanid1;
			_h_ += clanid2;
			_h_ += clanname1.hashCode();
			_h_ += clanname2.hashCode();
			_h_ += clanscore1;
			_h_ += clanscore2;
			_h_ += clan1roleids.hashCode();
			_h_ += clan2roleids.hashCode();
			_h_ += enterroleids.hashCode();
			_h_ += clan1enterroleids.hashCode();
			_h_ += clan2enterroleids.hashCode();
			_h_ += clan1roleranklist.hashCode();
			_h_ += clan2roleranklist.hashCode();
			_h_ += clan1rolerankmap.hashCode();
			_h_ += clan2rolerankmap.hashCode();
			_h_ += lastclan1rolesortstamp;
			_h_ += lastclan2rolesortstamp;
			_h_ += victoryboxids.hashCode();
			_h_ += celebrateboxids.hashCode();
			_h_ += datetime;
			_h_ += activitystarttime;
			_h_ += activitypreparetime;
			_h_ += activitynoentertime;
			_h_ += activityendtime;
			_h_ += whichwin;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(battlefieldid);
			_sb_.append(",");
			_sb_.append(clanfightid);
			_sb_.append(",");
			_sb_.append(state);
			_sb_.append(",");
			_sb_.append(clanid1);
			_sb_.append(",");
			_sb_.append(clanid2);
			_sb_.append(",");
			_sb_.append("'").append(clanname1).append("'");
			_sb_.append(",");
			_sb_.append("'").append(clanname2).append("'");
			_sb_.append(",");
			_sb_.append(clanscore1);
			_sb_.append(",");
			_sb_.append(clanscore2);
			_sb_.append(",");
			_sb_.append(clan1roleids);
			_sb_.append(",");
			_sb_.append(clan2roleids);
			_sb_.append(",");
			_sb_.append(enterroleids);
			_sb_.append(",");
			_sb_.append(clan1enterroleids);
			_sb_.append(",");
			_sb_.append(clan2enterroleids);
			_sb_.append(",");
			_sb_.append(clan1roleranklist);
			_sb_.append(",");
			_sb_.append(clan2roleranklist);
			_sb_.append(",");
			_sb_.append(clan1rolerankmap);
			_sb_.append(",");
			_sb_.append(clan2rolerankmap);
			_sb_.append(",");
			_sb_.append(lastclan1rolesortstamp);
			_sb_.append(",");
			_sb_.append(lastclan2rolesortstamp);
			_sb_.append(",");
			_sb_.append(victoryboxids);
			_sb_.append(",");
			_sb_.append(celebrateboxids);
			_sb_.append(",");
			_sb_.append(datetime);
			_sb_.append(",");
			_sb_.append(activitystarttime);
			_sb_.append(",");
			_sb_.append(activitypreparetime);
			_sb_.append(",");
			_sb_.append(activitynoentertime);
			_sb_.append(",");
			_sb_.append(activityendtime);
			_sb_.append(",");
			_sb_.append(whichwin);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
