
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class CircleTaskInfo extends mkdb.XBean implements xbean.CircleTaskInfo {
	private int id; // x循环任务总表里面的id,标识一个唯一的任务,在第一环时确定该id,直到第十环结束如果再有循环任务,则重新确定该id
	private int questid; // 任务id,代表 职业 公会 捉鬼之类
	private int queststate; // 任务状态
	private int round; // 当前任务的环数
	private int sumnum; // 累积次数. 根据不同的任务，拥有不同的含义
	private int smallaward; // 小 奖励 累计次数
	private int bigaward; // 大 奖励 累积次数
	private int questtype; // 任务类型(这里是代表具体循环任务的id)
	private int dstmapid; // 目的地图id
	private int givetasknpcid; // 接任务npc的id
	private long dstnpckey; // 目的npc的key
	private int dstnpcid; // 目的npc的id
	private int dstitemid; // 目的道具的id
	private int dstitemnum; // 目的道具的数量
	private int dstx; // 目的坐标
	private int dsty; // 目的坐标
	private long lastgiveuptime; // 本次任务放弃，下次任务的可接时间:已为放弃时间+delay. 0 为没有放弃,
	private long takequesttime; // 接任务的时间,单位毫秒
	private long acceptquesttime; // 接任务的时间,单位毫秒 任何情况下都不变
	private int sametimes; // 相同任务类型的次数
	private int lasttype; // 上次的任务类型
	private int battlenum; // 战斗次数(传说战斗次数)
	private long firsttasktime; // 今天第一次职业的时间,成就系统用.
	private int doneparam; // 任务完成参数
	private String npcname; // npc名称
	private int questclass; // 任务类型--类似送信，使用物品之类的任务
	private int questgroup; // 组类型--类似需要在任务中分种族的任务
	private int autocomp; // 是否自动完成,0:不自动完成,1:是自动完成
	private int submitnpc; // 非自动完成任务的提交npc
	private int openawardnum; // 转盘奖励计数
	private int catchitsingletime; // 单倍捉鬼次数
	private int catchitdoubletime; // 双倍捉鬼次数
	private int subitemtableid; // 上交物品的表id

	@Override
	public void _reset_unsafe_() {
		id = 0;
		questid = 0;
		queststate = 0;
		round = 0;
		sumnum = 0;
		smallaward = 0;
		bigaward = 0;
		questtype = 0;
		dstmapid = 0;
		givetasknpcid = 0;
		dstnpckey = 0L;
		dstnpcid = 0;
		dstitemid = 0;
		dstitemnum = 0;
		dstx = 0;
		dsty = 0;
		lastgiveuptime = 0L;
		takequesttime = 0L;
		acceptquesttime = 0L;
		sametimes = 0;
		lasttype = 0;
		battlenum = 0;
		firsttasktime = 0L;
		doneparam = 0;
		npcname = "";
		questclass = 0;
		questgroup = 0;
		autocomp = 1;
		submitnpc = 0;
		openawardnum = 0;
		catchitsingletime = 0;
		catchitdoubletime = 0;
		subitemtableid = 0;
	}

	CircleTaskInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		doneparam = 0;
		npcname = "";
		autocomp = 1;
	}

	public CircleTaskInfo() {
		this(0, null, null);
	}

	public CircleTaskInfo(CircleTaskInfo _o_) {
		this(_o_, null, null);
	}

	CircleTaskInfo(xbean.CircleTaskInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof CircleTaskInfo) assign((CircleTaskInfo)_o1_);
		else if (_o1_ instanceof CircleTaskInfo.Data) assign((CircleTaskInfo.Data)_o1_);
		else if (_o1_ instanceof CircleTaskInfo.Const) assign(((CircleTaskInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(CircleTaskInfo _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		questid = _o_.questid;
		queststate = _o_.queststate;
		round = _o_.round;
		sumnum = _o_.sumnum;
		smallaward = _o_.smallaward;
		bigaward = _o_.bigaward;
		questtype = _o_.questtype;
		dstmapid = _o_.dstmapid;
		givetasknpcid = _o_.givetasknpcid;
		dstnpckey = _o_.dstnpckey;
		dstnpcid = _o_.dstnpcid;
		dstitemid = _o_.dstitemid;
		dstitemnum = _o_.dstitemnum;
		dstx = _o_.dstx;
		dsty = _o_.dsty;
		lastgiveuptime = _o_.lastgiveuptime;
		takequesttime = _o_.takequesttime;
		acceptquesttime = _o_.acceptquesttime;
		sametimes = _o_.sametimes;
		lasttype = _o_.lasttype;
		battlenum = _o_.battlenum;
		firsttasktime = _o_.firsttasktime;
		doneparam = _o_.doneparam;
		npcname = _o_.npcname;
		questclass = _o_.questclass;
		questgroup = _o_.questgroup;
		autocomp = _o_.autocomp;
		submitnpc = _o_.submitnpc;
		openawardnum = _o_.openawardnum;
		catchitsingletime = _o_.catchitsingletime;
		catchitdoubletime = _o_.catchitdoubletime;
		subitemtableid = _o_.subitemtableid;
	}

	private void assign(CircleTaskInfo.Data _o_) {
		id = _o_.id;
		questid = _o_.questid;
		queststate = _o_.queststate;
		round = _o_.round;
		sumnum = _o_.sumnum;
		smallaward = _o_.smallaward;
		bigaward = _o_.bigaward;
		questtype = _o_.questtype;
		dstmapid = _o_.dstmapid;
		givetasknpcid = _o_.givetasknpcid;
		dstnpckey = _o_.dstnpckey;
		dstnpcid = _o_.dstnpcid;
		dstitemid = _o_.dstitemid;
		dstitemnum = _o_.dstitemnum;
		dstx = _o_.dstx;
		dsty = _o_.dsty;
		lastgiveuptime = _o_.lastgiveuptime;
		takequesttime = _o_.takequesttime;
		acceptquesttime = _o_.acceptquesttime;
		sametimes = _o_.sametimes;
		lasttype = _o_.lasttype;
		battlenum = _o_.battlenum;
		firsttasktime = _o_.firsttasktime;
		doneparam = _o_.doneparam;
		npcname = _o_.npcname;
		questclass = _o_.questclass;
		questgroup = _o_.questgroup;
		autocomp = _o_.autocomp;
		submitnpc = _o_.submitnpc;
		openawardnum = _o_.openawardnum;
		catchitsingletime = _o_.catchitsingletime;
		catchitdoubletime = _o_.catchitdoubletime;
		subitemtableid = _o_.subitemtableid;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(questid);
		_os_.marshal(queststate);
		_os_.marshal(round);
		_os_.marshal(sumnum);
		_os_.marshal(smallaward);
		_os_.marshal(bigaward);
		_os_.marshal(questtype);
		_os_.marshal(dstmapid);
		_os_.marshal(givetasknpcid);
		_os_.marshal(dstnpckey);
		_os_.marshal(dstnpcid);
		_os_.marshal(dstitemid);
		_os_.marshal(dstitemnum);
		_os_.marshal(dstx);
		_os_.marshal(dsty);
		_os_.marshal(lastgiveuptime);
		_os_.marshal(takequesttime);
		_os_.marshal(acceptquesttime);
		_os_.marshal(sametimes);
		_os_.marshal(lasttype);
		_os_.marshal(battlenum);
		_os_.marshal(firsttasktime);
		_os_.marshal(doneparam);
		_os_.marshal(npcname, mkdb.Const.IO_CHARSET);
		_os_.marshal(questclass);
		_os_.marshal(questgroup);
		_os_.marshal(autocomp);
		_os_.marshal(submitnpc);
		_os_.marshal(openawardnum);
		_os_.marshal(catchitsingletime);
		_os_.marshal(catchitdoubletime);
		_os_.marshal(subitemtableid);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_int();
		questid = _os_.unmarshal_int();
		queststate = _os_.unmarshal_int();
		round = _os_.unmarshal_int();
		sumnum = _os_.unmarshal_int();
		smallaward = _os_.unmarshal_int();
		bigaward = _os_.unmarshal_int();
		questtype = _os_.unmarshal_int();
		dstmapid = _os_.unmarshal_int();
		givetasknpcid = _os_.unmarshal_int();
		dstnpckey = _os_.unmarshal_long();
		dstnpcid = _os_.unmarshal_int();
		dstitemid = _os_.unmarshal_int();
		dstitemnum = _os_.unmarshal_int();
		dstx = _os_.unmarshal_int();
		dsty = _os_.unmarshal_int();
		lastgiveuptime = _os_.unmarshal_long();
		takequesttime = _os_.unmarshal_long();
		acceptquesttime = _os_.unmarshal_long();
		sametimes = _os_.unmarshal_int();
		lasttype = _os_.unmarshal_int();
		battlenum = _os_.unmarshal_int();
		firsttasktime = _os_.unmarshal_long();
		doneparam = _os_.unmarshal_int();
		npcname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		questclass = _os_.unmarshal_int();
		questgroup = _os_.unmarshal_int();
		autocomp = _os_.unmarshal_int();
		submitnpc = _os_.unmarshal_int();
		openawardnum = _os_.unmarshal_int();
		catchitsingletime = _os_.unmarshal_int();
		catchitdoubletime = _os_.unmarshal_int();
		subitemtableid = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.CircleTaskInfo copy() {
		_xdb_verify_unsafe_();
		return new CircleTaskInfo(this);
	}

	@Override
	public xbean.CircleTaskInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CircleTaskInfo toBean() {
		_xdb_verify_unsafe_();
		return new CircleTaskInfo(this); // same as copy()
	}

	@Override
	public xbean.CircleTaskInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.CircleTaskInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getId() { // x循环任务总表里面的id,标识一个唯一的任务,在第一环时确定该id,直到第十环结束如果再有循环任务,则重新确定该id
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public int getQuestid() { // 任务id,代表 职业 公会 捉鬼之类
		_xdb_verify_unsafe_();
		return questid;
	}

	@Override
	public int getQueststate() { // 任务状态
		_xdb_verify_unsafe_();
		return queststate;
	}

	@Override
	public int getRound() { // 当前任务的环数
		_xdb_verify_unsafe_();
		return round;
	}

	@Override
	public int getSumnum() { // 累积次数. 根据不同的任务，拥有不同的含义
		_xdb_verify_unsafe_();
		return sumnum;
	}

	@Override
	public int getSmallaward() { // 小 奖励 累计次数
		_xdb_verify_unsafe_();
		return smallaward;
	}

	@Override
	public int getBigaward() { // 大 奖励 累积次数
		_xdb_verify_unsafe_();
		return bigaward;
	}

	@Override
	public int getQuesttype() { // 任务类型(这里是代表具体循环任务的id)
		_xdb_verify_unsafe_();
		return questtype;
	}

	@Override
	public int getDstmapid() { // 目的地图id
		_xdb_verify_unsafe_();
		return dstmapid;
	}

	@Override
	public int getGivetasknpcid() { // 接任务npc的id
		_xdb_verify_unsafe_();
		return givetasknpcid;
	}

	@Override
	public long getDstnpckey() { // 目的npc的key
		_xdb_verify_unsafe_();
		return dstnpckey;
	}

	@Override
	public int getDstnpcid() { // 目的npc的id
		_xdb_verify_unsafe_();
		return dstnpcid;
	}

	@Override
	public int getDstitemid() { // 目的道具的id
		_xdb_verify_unsafe_();
		return dstitemid;
	}

	@Override
	public int getDstitemnum() { // 目的道具的数量
		_xdb_verify_unsafe_();
		return dstitemnum;
	}

	@Override
	public int getDstx() { // 目的坐标
		_xdb_verify_unsafe_();
		return dstx;
	}

	@Override
	public int getDsty() { // 目的坐标
		_xdb_verify_unsafe_();
		return dsty;
	}

	@Override
	public long getLastgiveuptime() { // 本次任务放弃，下次任务的可接时间:已为放弃时间+delay. 0 为没有放弃,
		_xdb_verify_unsafe_();
		return lastgiveuptime;
	}

	@Override
	public long getTakequesttime() { // 接任务的时间,单位毫秒
		_xdb_verify_unsafe_();
		return takequesttime;
	}

	@Override
	public long getAcceptquesttime() { // 接任务的时间,单位毫秒 任何情况下都不变
		_xdb_verify_unsafe_();
		return acceptquesttime;
	}

	@Override
	public int getSametimes() { // 相同任务类型的次数
		_xdb_verify_unsafe_();
		return sametimes;
	}

	@Override
	public int getLasttype() { // 上次的任务类型
		_xdb_verify_unsafe_();
		return lasttype;
	}

	@Override
	public int getBattlenum() { // 战斗次数(传说战斗次数)
		_xdb_verify_unsafe_();
		return battlenum;
	}

	@Override
	public long getFirsttasktime() { // 今天第一次职业的时间,成就系统用.
		_xdb_verify_unsafe_();
		return firsttasktime;
	}

	@Override
	public int getDoneparam() { // 任务完成参数
		_xdb_verify_unsafe_();
		return doneparam;
	}

	@Override
	public String getNpcname() { // npc名称
		_xdb_verify_unsafe_();
		return npcname;
	}

	@Override
	public com.locojoy.base.Octets getNpcnameOctets() { // npc名称
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getNpcname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getQuestclass() { // 任务类型--类似送信，使用物品之类的任务
		_xdb_verify_unsafe_();
		return questclass;
	}

	@Override
	public int getQuestgroup() { // 组类型--类似需要在任务中分种族的任务
		_xdb_verify_unsafe_();
		return questgroup;
	}

	@Override
	public int getAutocomp() { // 是否自动完成,0:不自动完成,1:是自动完成
		_xdb_verify_unsafe_();
		return autocomp;
	}

	@Override
	public int getSubmitnpc() { // 非自动完成任务的提交npc
		_xdb_verify_unsafe_();
		return submitnpc;
	}

	@Override
	public int getOpenawardnum() { // 转盘奖励计数
		_xdb_verify_unsafe_();
		return openawardnum;
	}

	@Override
	public int getCatchitsingletime() { // 单倍捉鬼次数
		_xdb_verify_unsafe_();
		return catchitsingletime;
	}

	@Override
	public int getCatchitdoubletime() { // 双倍捉鬼次数
		_xdb_verify_unsafe_();
		return catchitdoubletime;
	}

	@Override
	public int getSubitemtableid() { // 上交物品的表id
		_xdb_verify_unsafe_();
		return subitemtableid;
	}

	@Override
	public void setId(int _v_) { // x循环任务总表里面的id,标识一个唯一的任务,在第一环时确定该id,直到第十环结束如果再有循环任务,则重新确定该id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setQuestid(int _v_) { // 任务id,代表 职业 公会 捉鬼之类
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "questid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, questid) {
					public void rollback() { questid = _xdb_saved; }
				};}});
		questid = _v_;
	}

	@Override
	public void setQueststate(int _v_) { // 任务状态
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "queststate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, queststate) {
					public void rollback() { queststate = _xdb_saved; }
				};}});
		queststate = _v_;
	}

	@Override
	public void setRound(int _v_) { // 当前任务的环数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "round") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, round) {
					public void rollback() { round = _xdb_saved; }
				};}});
		round = _v_;
	}

	@Override
	public void setSumnum(int _v_) { // 累积次数. 根据不同的任务，拥有不同的含义
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sumnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, sumnum) {
					public void rollback() { sumnum = _xdb_saved; }
				};}});
		sumnum = _v_;
	}

	@Override
	public void setSmallaward(int _v_) { // 小 奖励 累计次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "smallaward") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, smallaward) {
					public void rollback() { smallaward = _xdb_saved; }
				};}});
		smallaward = _v_;
	}

	@Override
	public void setBigaward(int _v_) { // 大 奖励 累积次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bigaward") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bigaward) {
					public void rollback() { bigaward = _xdb_saved; }
				};}});
		bigaward = _v_;
	}

	@Override
	public void setQuesttype(int _v_) { // 任务类型(这里是代表具体循环任务的id)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "questtype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, questtype) {
					public void rollback() { questtype = _xdb_saved; }
				};}});
		questtype = _v_;
	}

	@Override
	public void setDstmapid(int _v_) { // 目的地图id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstmapid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dstmapid) {
					public void rollback() { dstmapid = _xdb_saved; }
				};}});
		dstmapid = _v_;
	}

	@Override
	public void setGivetasknpcid(int _v_) { // 接任务npc的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "givetasknpcid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, givetasknpcid) {
					public void rollback() { givetasknpcid = _xdb_saved; }
				};}});
		givetasknpcid = _v_;
	}

	@Override
	public void setDstnpckey(long _v_) { // 目的npc的key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstnpckey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, dstnpckey) {
					public void rollback() { dstnpckey = _xdb_saved; }
				};}});
		dstnpckey = _v_;
	}

	@Override
	public void setDstnpcid(int _v_) { // 目的npc的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstnpcid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dstnpcid) {
					public void rollback() { dstnpcid = _xdb_saved; }
				};}});
		dstnpcid = _v_;
	}

	@Override
	public void setDstitemid(int _v_) { // 目的道具的id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstitemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dstitemid) {
					public void rollback() { dstitemid = _xdb_saved; }
				};}});
		dstitemid = _v_;
	}

	@Override
	public void setDstitemnum(int _v_) { // 目的道具的数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstitemnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dstitemnum) {
					public void rollback() { dstitemnum = _xdb_saved; }
				};}});
		dstitemnum = _v_;
	}

	@Override
	public void setDstx(int _v_) { // 目的坐标
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dstx") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dstx) {
					public void rollback() { dstx = _xdb_saved; }
				};}});
		dstx = _v_;
	}

	@Override
	public void setDsty(int _v_) { // 目的坐标
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dsty") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dsty) {
					public void rollback() { dsty = _xdb_saved; }
				};}});
		dsty = _v_;
	}

	@Override
	public void setLastgiveuptime(long _v_) { // 本次任务放弃，下次任务的可接时间:已为放弃时间+delay. 0 为没有放弃,
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastgiveuptime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastgiveuptime) {
					public void rollback() { lastgiveuptime = _xdb_saved; }
				};}});
		lastgiveuptime = _v_;
	}

	@Override
	public void setTakequesttime(long _v_) { // 接任务的时间,单位毫秒
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "takequesttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, takequesttime) {
					public void rollback() { takequesttime = _xdb_saved; }
				};}});
		takequesttime = _v_;
	}

	@Override
	public void setAcceptquesttime(long _v_) { // 接任务的时间,单位毫秒 任何情况下都不变
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "acceptquesttime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, acceptquesttime) {
					public void rollback() { acceptquesttime = _xdb_saved; }
				};}});
		acceptquesttime = _v_;
	}

	@Override
	public void setSametimes(int _v_) { // 相同任务类型的次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sametimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, sametimes) {
					public void rollback() { sametimes = _xdb_saved; }
				};}});
		sametimes = _v_;
	}

	@Override
	public void setLasttype(int _v_) { // 上次的任务类型
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lasttype") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lasttype) {
					public void rollback() { lasttype = _xdb_saved; }
				};}});
		lasttype = _v_;
	}

	@Override
	public void setBattlenum(int _v_) { // 战斗次数(传说战斗次数)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "battlenum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, battlenum) {
					public void rollback() { battlenum = _xdb_saved; }
				};}});
		battlenum = _v_;
	}

	@Override
	public void setFirsttasktime(long _v_) { // 今天第一次职业的时间,成就系统用.
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firsttasktime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, firsttasktime) {
					public void rollback() { firsttasktime = _xdb_saved; }
				};}});
		firsttasktime = _v_;
	}

	@Override
	public void setDoneparam(int _v_) { // 任务完成参数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "doneparam") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, doneparam) {
					public void rollback() { doneparam = _xdb_saved; }
				};}});
		doneparam = _v_;
	}

	@Override
	public void setNpcname(String _v_) { // npc名称
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "npcname") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, npcname) {
					public void rollback() { npcname = _xdb_saved; }
				};}});
		npcname = _v_;
	}

	@Override
	public void setNpcnameOctets(com.locojoy.base.Octets _v_) { // npc名称
		_xdb_verify_unsafe_();
		this.setNpcname(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setQuestclass(int _v_) { // 任务类型--类似送信，使用物品之类的任务
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "questclass") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, questclass) {
					public void rollback() { questclass = _xdb_saved; }
				};}});
		questclass = _v_;
	}

	@Override
	public void setQuestgroup(int _v_) { // 组类型--类似需要在任务中分种族的任务
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "questgroup") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, questgroup) {
					public void rollback() { questgroup = _xdb_saved; }
				};}});
		questgroup = _v_;
	}

	@Override
	public void setAutocomp(int _v_) { // 是否自动完成,0:不自动完成,1:是自动完成
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "autocomp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, autocomp) {
					public void rollback() { autocomp = _xdb_saved; }
				};}});
		autocomp = _v_;
	}

	@Override
	public void setSubmitnpc(int _v_) { // 非自动完成任务的提交npc
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "submitnpc") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, submitnpc) {
					public void rollback() { submitnpc = _xdb_saved; }
				};}});
		submitnpc = _v_;
	}

	@Override
	public void setOpenawardnum(int _v_) { // 转盘奖励计数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "openawardnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, openawardnum) {
					public void rollback() { openawardnum = _xdb_saved; }
				};}});
		openawardnum = _v_;
	}

	@Override
	public void setCatchitsingletime(int _v_) { // 单倍捉鬼次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "catchitsingletime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, catchitsingletime) {
					public void rollback() { catchitsingletime = _xdb_saved; }
				};}});
		catchitsingletime = _v_;
	}

	@Override
	public void setCatchitdoubletime(int _v_) { // 双倍捉鬼次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "catchitdoubletime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, catchitdoubletime) {
					public void rollback() { catchitdoubletime = _xdb_saved; }
				};}});
		catchitdoubletime = _v_;
	}

	@Override
	public void setSubitemtableid(int _v_) { // 上交物品的表id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "subitemtableid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, subitemtableid) {
					public void rollback() { subitemtableid = _xdb_saved; }
				};}});
		subitemtableid = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		CircleTaskInfo _o_ = null;
		if ( _o1_ instanceof CircleTaskInfo ) _o_ = (CircleTaskInfo)_o1_;
		else if ( _o1_ instanceof CircleTaskInfo.Const ) _o_ = ((CircleTaskInfo.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (questid != _o_.questid) return false;
		if (queststate != _o_.queststate) return false;
		if (round != _o_.round) return false;
		if (sumnum != _o_.sumnum) return false;
		if (smallaward != _o_.smallaward) return false;
		if (bigaward != _o_.bigaward) return false;
		if (questtype != _o_.questtype) return false;
		if (dstmapid != _o_.dstmapid) return false;
		if (givetasknpcid != _o_.givetasknpcid) return false;
		if (dstnpckey != _o_.dstnpckey) return false;
		if (dstnpcid != _o_.dstnpcid) return false;
		if (dstitemid != _o_.dstitemid) return false;
		if (dstitemnum != _o_.dstitemnum) return false;
		if (dstx != _o_.dstx) return false;
		if (dsty != _o_.dsty) return false;
		if (lastgiveuptime != _o_.lastgiveuptime) return false;
		if (takequesttime != _o_.takequesttime) return false;
		if (acceptquesttime != _o_.acceptquesttime) return false;
		if (sametimes != _o_.sametimes) return false;
		if (lasttype != _o_.lasttype) return false;
		if (battlenum != _o_.battlenum) return false;
		if (firsttasktime != _o_.firsttasktime) return false;
		if (doneparam != _o_.doneparam) return false;
		if (!npcname.equals(_o_.npcname)) return false;
		if (questclass != _o_.questclass) return false;
		if (questgroup != _o_.questgroup) return false;
		if (autocomp != _o_.autocomp) return false;
		if (submitnpc != _o_.submitnpc) return false;
		if (openawardnum != _o_.openawardnum) return false;
		if (catchitsingletime != _o_.catchitsingletime) return false;
		if (catchitdoubletime != _o_.catchitdoubletime) return false;
		if (subitemtableid != _o_.subitemtableid) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += questid;
		_h_ += queststate;
		_h_ += round;
		_h_ += sumnum;
		_h_ += smallaward;
		_h_ += bigaward;
		_h_ += questtype;
		_h_ += dstmapid;
		_h_ += givetasknpcid;
		_h_ += dstnpckey;
		_h_ += dstnpcid;
		_h_ += dstitemid;
		_h_ += dstitemnum;
		_h_ += dstx;
		_h_ += dsty;
		_h_ += lastgiveuptime;
		_h_ += takequesttime;
		_h_ += acceptquesttime;
		_h_ += sametimes;
		_h_ += lasttype;
		_h_ += battlenum;
		_h_ += firsttasktime;
		_h_ += doneparam;
		_h_ += npcname.hashCode();
		_h_ += questclass;
		_h_ += questgroup;
		_h_ += autocomp;
		_h_ += submitnpc;
		_h_ += openawardnum;
		_h_ += catchitsingletime;
		_h_ += catchitdoubletime;
		_h_ += subitemtableid;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(questid);
		_sb_.append(",");
		_sb_.append(queststate);
		_sb_.append(",");
		_sb_.append(round);
		_sb_.append(",");
		_sb_.append(sumnum);
		_sb_.append(",");
		_sb_.append(smallaward);
		_sb_.append(",");
		_sb_.append(bigaward);
		_sb_.append(",");
		_sb_.append(questtype);
		_sb_.append(",");
		_sb_.append(dstmapid);
		_sb_.append(",");
		_sb_.append(givetasknpcid);
		_sb_.append(",");
		_sb_.append(dstnpckey);
		_sb_.append(",");
		_sb_.append(dstnpcid);
		_sb_.append(",");
		_sb_.append(dstitemid);
		_sb_.append(",");
		_sb_.append(dstitemnum);
		_sb_.append(",");
		_sb_.append(dstx);
		_sb_.append(",");
		_sb_.append(dsty);
		_sb_.append(",");
		_sb_.append(lastgiveuptime);
		_sb_.append(",");
		_sb_.append(takequesttime);
		_sb_.append(",");
		_sb_.append(acceptquesttime);
		_sb_.append(",");
		_sb_.append(sametimes);
		_sb_.append(",");
		_sb_.append(lasttype);
		_sb_.append(",");
		_sb_.append(battlenum);
		_sb_.append(",");
		_sb_.append(firsttasktime);
		_sb_.append(",");
		_sb_.append(doneparam);
		_sb_.append(",");
		_sb_.append("'").append(npcname).append("'");
		_sb_.append(",");
		_sb_.append(questclass);
		_sb_.append(",");
		_sb_.append(questgroup);
		_sb_.append(",");
		_sb_.append(autocomp);
		_sb_.append(",");
		_sb_.append(submitnpc);
		_sb_.append(",");
		_sb_.append(openawardnum);
		_sb_.append(",");
		_sb_.append(catchitsingletime);
		_sb_.append(",");
		_sb_.append(catchitdoubletime);
		_sb_.append(",");
		_sb_.append(subitemtableid);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("questid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("queststate"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("round"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sumnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("smallaward"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bigaward"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("questtype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstmapid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("givetasknpcid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstnpckey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstnpcid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstitemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstitemnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dstx"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dsty"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastgiveuptime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("takequesttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("acceptquesttime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sametimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lasttype"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("battlenum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firsttasktime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("doneparam"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("npcname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("questclass"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("questgroup"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("autocomp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("submitnpc"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("openawardnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("catchitsingletime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("catchitdoubletime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("subitemtableid"));
		return lb;
	}

	private class Const implements xbean.CircleTaskInfo {
		CircleTaskInfo nThis() {
			return CircleTaskInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.CircleTaskInfo copy() {
			return CircleTaskInfo.this.copy();
		}

		@Override
		public xbean.CircleTaskInfo toData() {
			return CircleTaskInfo.this.toData();
		}

		public xbean.CircleTaskInfo toBean() {
			return CircleTaskInfo.this.toBean();
		}

		@Override
		public xbean.CircleTaskInfo toDataIf() {
			return CircleTaskInfo.this.toDataIf();
		}

		public xbean.CircleTaskInfo toBeanIf() {
			return CircleTaskInfo.this.toBeanIf();
		}

		@Override
		public int getId() { // x循环任务总表里面的id,标识一个唯一的任务,在第一环时确定该id,直到第十环结束如果再有循环任务,则重新确定该id
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getQuestid() { // 任务id,代表 职业 公会 捉鬼之类
			_xdb_verify_unsafe_();
			return questid;
		}

		@Override
		public int getQueststate() { // 任务状态
			_xdb_verify_unsafe_();
			return queststate;
		}

		@Override
		public int getRound() { // 当前任务的环数
			_xdb_verify_unsafe_();
			return round;
		}

		@Override
		public int getSumnum() { // 累积次数. 根据不同的任务，拥有不同的含义
			_xdb_verify_unsafe_();
			return sumnum;
		}

		@Override
		public int getSmallaward() { // 小 奖励 累计次数
			_xdb_verify_unsafe_();
			return smallaward;
		}

		@Override
		public int getBigaward() { // 大 奖励 累积次数
			_xdb_verify_unsafe_();
			return bigaward;
		}

		@Override
		public int getQuesttype() { // 任务类型(这里是代表具体循环任务的id)
			_xdb_verify_unsafe_();
			return questtype;
		}

		@Override
		public int getDstmapid() { // 目的地图id
			_xdb_verify_unsafe_();
			return dstmapid;
		}

		@Override
		public int getGivetasknpcid() { // 接任务npc的id
			_xdb_verify_unsafe_();
			return givetasknpcid;
		}

		@Override
		public long getDstnpckey() { // 目的npc的key
			_xdb_verify_unsafe_();
			return dstnpckey;
		}

		@Override
		public int getDstnpcid() { // 目的npc的id
			_xdb_verify_unsafe_();
			return dstnpcid;
		}

		@Override
		public int getDstitemid() { // 目的道具的id
			_xdb_verify_unsafe_();
			return dstitemid;
		}

		@Override
		public int getDstitemnum() { // 目的道具的数量
			_xdb_verify_unsafe_();
			return dstitemnum;
		}

		@Override
		public int getDstx() { // 目的坐标
			_xdb_verify_unsafe_();
			return dstx;
		}

		@Override
		public int getDsty() { // 目的坐标
			_xdb_verify_unsafe_();
			return dsty;
		}

		@Override
		public long getLastgiveuptime() { // 本次任务放弃，下次任务的可接时间:已为放弃时间+delay. 0 为没有放弃,
			_xdb_verify_unsafe_();
			return lastgiveuptime;
		}

		@Override
		public long getTakequesttime() { // 接任务的时间,单位毫秒
			_xdb_verify_unsafe_();
			return takequesttime;
		}

		@Override
		public long getAcceptquesttime() { // 接任务的时间,单位毫秒 任何情况下都不变
			_xdb_verify_unsafe_();
			return acceptquesttime;
		}

		@Override
		public int getSametimes() { // 相同任务类型的次数
			_xdb_verify_unsafe_();
			return sametimes;
		}

		@Override
		public int getLasttype() { // 上次的任务类型
			_xdb_verify_unsafe_();
			return lasttype;
		}

		@Override
		public int getBattlenum() { // 战斗次数(传说战斗次数)
			_xdb_verify_unsafe_();
			return battlenum;
		}

		@Override
		public long getFirsttasktime() { // 今天第一次职业的时间,成就系统用.
			_xdb_verify_unsafe_();
			return firsttasktime;
		}

		@Override
		public int getDoneparam() { // 任务完成参数
			_xdb_verify_unsafe_();
			return doneparam;
		}

		@Override
		public String getNpcname() { // npc名称
			_xdb_verify_unsafe_();
			return npcname;
		}

		@Override
		public com.locojoy.base.Octets getNpcnameOctets() { // npc名称
			_xdb_verify_unsafe_();
			return CircleTaskInfo.this.getNpcnameOctets();
		}

		@Override
		public int getQuestclass() { // 任务类型--类似送信，使用物品之类的任务
			_xdb_verify_unsafe_();
			return questclass;
		}

		@Override
		public int getQuestgroup() { // 组类型--类似需要在任务中分种族的任务
			_xdb_verify_unsafe_();
			return questgroup;
		}

		@Override
		public int getAutocomp() { // 是否自动完成,0:不自动完成,1:是自动完成
			_xdb_verify_unsafe_();
			return autocomp;
		}

		@Override
		public int getSubmitnpc() { // 非自动完成任务的提交npc
			_xdb_verify_unsafe_();
			return submitnpc;
		}

		@Override
		public int getOpenawardnum() { // 转盘奖励计数
			_xdb_verify_unsafe_();
			return openawardnum;
		}

		@Override
		public int getCatchitsingletime() { // 单倍捉鬼次数
			_xdb_verify_unsafe_();
			return catchitsingletime;
		}

		@Override
		public int getCatchitdoubletime() { // 双倍捉鬼次数
			_xdb_verify_unsafe_();
			return catchitdoubletime;
		}

		@Override
		public int getSubitemtableid() { // 上交物品的表id
			_xdb_verify_unsafe_();
			return subitemtableid;
		}

		@Override
		public void setId(int _v_) { // x循环任务总表里面的id,标识一个唯一的任务,在第一环时确定该id,直到第十环结束如果再有循环任务,则重新确定该id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setQuestid(int _v_) { // 任务id,代表 职业 公会 捉鬼之类
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setQueststate(int _v_) { // 任务状态
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRound(int _v_) { // 当前任务的环数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSumnum(int _v_) { // 累积次数. 根据不同的任务，拥有不同的含义
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSmallaward(int _v_) { // 小 奖励 累计次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBigaward(int _v_) { // 大 奖励 累积次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setQuesttype(int _v_) { // 任务类型(这里是代表具体循环任务的id)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstmapid(int _v_) { // 目的地图id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGivetasknpcid(int _v_) { // 接任务npc的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstnpckey(long _v_) { // 目的npc的key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstnpcid(int _v_) { // 目的npc的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstitemid(int _v_) { // 目的道具的id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstitemnum(int _v_) { // 目的道具的数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDstx(int _v_) { // 目的坐标
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDsty(int _v_) { // 目的坐标
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastgiveuptime(long _v_) { // 本次任务放弃，下次任务的可接时间:已为放弃时间+delay. 0 为没有放弃,
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTakequesttime(long _v_) { // 接任务的时间,单位毫秒
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAcceptquesttime(long _v_) { // 接任务的时间,单位毫秒 任何情况下都不变
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSametimes(int _v_) { // 相同任务类型的次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLasttype(int _v_) { // 上次的任务类型
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBattlenum(int _v_) { // 战斗次数(传说战斗次数)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirsttasktime(long _v_) { // 今天第一次职业的时间,成就系统用.
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDoneparam(int _v_) { // 任务完成参数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNpcname(String _v_) { // npc名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNpcnameOctets(com.locojoy.base.Octets _v_) { // npc名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setQuestclass(int _v_) { // 任务类型--类似送信，使用物品之类的任务
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setQuestgroup(int _v_) { // 组类型--类似需要在任务中分种族的任务
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAutocomp(int _v_) { // 是否自动完成,0:不自动完成,1:是自动完成
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSubmitnpc(int _v_) { // 非自动完成任务的提交npc
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOpenawardnum(int _v_) { // 转盘奖励计数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCatchitsingletime(int _v_) { // 单倍捉鬼次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCatchitdoubletime(int _v_) { // 双倍捉鬼次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSubitemtableid(int _v_) { // 上交物品的表id
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
			return CircleTaskInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return CircleTaskInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return CircleTaskInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return CircleTaskInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return CircleTaskInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return CircleTaskInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return CircleTaskInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return CircleTaskInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return CircleTaskInfo.this.toString();
		}

	}

	public static final class Data implements xbean.CircleTaskInfo {
		private int id; // x循环任务总表里面的id,标识一个唯一的任务,在第一环时确定该id,直到第十环结束如果再有循环任务,则重新确定该id
		private int questid; // 任务id,代表 职业 公会 捉鬼之类
		private int queststate; // 任务状态
		private int round; // 当前任务的环数
		private int sumnum; // 累积次数. 根据不同的任务，拥有不同的含义
		private int smallaward; // 小 奖励 累计次数
		private int bigaward; // 大 奖励 累积次数
		private int questtype; // 任务类型(这里是代表具体循环任务的id)
		private int dstmapid; // 目的地图id
		private int givetasknpcid; // 接任务npc的id
		private long dstnpckey; // 目的npc的key
		private int dstnpcid; // 目的npc的id
		private int dstitemid; // 目的道具的id
		private int dstitemnum; // 目的道具的数量
		private int dstx; // 目的坐标
		private int dsty; // 目的坐标
		private long lastgiveuptime; // 本次任务放弃，下次任务的可接时间:已为放弃时间+delay. 0 为没有放弃,
		private long takequesttime; // 接任务的时间,单位毫秒
		private long acceptquesttime; // 接任务的时间,单位毫秒 任何情况下都不变
		private int sametimes; // 相同任务类型的次数
		private int lasttype; // 上次的任务类型
		private int battlenum; // 战斗次数(传说战斗次数)
		private long firsttasktime; // 今天第一次职业的时间,成就系统用.
		private int doneparam; // 任务完成参数
		private String npcname; // npc名称
		private int questclass; // 任务类型--类似送信，使用物品之类的任务
		private int questgroup; // 组类型--类似需要在任务中分种族的任务
		private int autocomp; // 是否自动完成,0:不自动完成,1:是自动完成
		private int submitnpc; // 非自动完成任务的提交npc
		private int openawardnum; // 转盘奖励计数
		private int catchitsingletime; // 单倍捉鬼次数
		private int catchitdoubletime; // 双倍捉鬼次数
		private int subitemtableid; // 上交物品的表id

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			doneparam = 0;
			npcname = "";
			autocomp = 1;
		}

		Data(xbean.CircleTaskInfo _o1_) {
			if (_o1_ instanceof CircleTaskInfo) assign((CircleTaskInfo)_o1_);
			else if (_o1_ instanceof CircleTaskInfo.Data) assign((CircleTaskInfo.Data)_o1_);
			else if (_o1_ instanceof CircleTaskInfo.Const) assign(((CircleTaskInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(CircleTaskInfo _o_) {
			id = _o_.id;
			questid = _o_.questid;
			queststate = _o_.queststate;
			round = _o_.round;
			sumnum = _o_.sumnum;
			smallaward = _o_.smallaward;
			bigaward = _o_.bigaward;
			questtype = _o_.questtype;
			dstmapid = _o_.dstmapid;
			givetasknpcid = _o_.givetasknpcid;
			dstnpckey = _o_.dstnpckey;
			dstnpcid = _o_.dstnpcid;
			dstitemid = _o_.dstitemid;
			dstitemnum = _o_.dstitemnum;
			dstx = _o_.dstx;
			dsty = _o_.dsty;
			lastgiveuptime = _o_.lastgiveuptime;
			takequesttime = _o_.takequesttime;
			acceptquesttime = _o_.acceptquesttime;
			sametimes = _o_.sametimes;
			lasttype = _o_.lasttype;
			battlenum = _o_.battlenum;
			firsttasktime = _o_.firsttasktime;
			doneparam = _o_.doneparam;
			npcname = _o_.npcname;
			questclass = _o_.questclass;
			questgroup = _o_.questgroup;
			autocomp = _o_.autocomp;
			submitnpc = _o_.submitnpc;
			openawardnum = _o_.openawardnum;
			catchitsingletime = _o_.catchitsingletime;
			catchitdoubletime = _o_.catchitdoubletime;
			subitemtableid = _o_.subitemtableid;
		}

		private void assign(CircleTaskInfo.Data _o_) {
			id = _o_.id;
			questid = _o_.questid;
			queststate = _o_.queststate;
			round = _o_.round;
			sumnum = _o_.sumnum;
			smallaward = _o_.smallaward;
			bigaward = _o_.bigaward;
			questtype = _o_.questtype;
			dstmapid = _o_.dstmapid;
			givetasknpcid = _o_.givetasknpcid;
			dstnpckey = _o_.dstnpckey;
			dstnpcid = _o_.dstnpcid;
			dstitemid = _o_.dstitemid;
			dstitemnum = _o_.dstitemnum;
			dstx = _o_.dstx;
			dsty = _o_.dsty;
			lastgiveuptime = _o_.lastgiveuptime;
			takequesttime = _o_.takequesttime;
			acceptquesttime = _o_.acceptquesttime;
			sametimes = _o_.sametimes;
			lasttype = _o_.lasttype;
			battlenum = _o_.battlenum;
			firsttasktime = _o_.firsttasktime;
			doneparam = _o_.doneparam;
			npcname = _o_.npcname;
			questclass = _o_.questclass;
			questgroup = _o_.questgroup;
			autocomp = _o_.autocomp;
			submitnpc = _o_.submitnpc;
			openawardnum = _o_.openawardnum;
			catchitsingletime = _o_.catchitsingletime;
			catchitdoubletime = _o_.catchitdoubletime;
			subitemtableid = _o_.subitemtableid;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(questid);
			_os_.marshal(queststate);
			_os_.marshal(round);
			_os_.marshal(sumnum);
			_os_.marshal(smallaward);
			_os_.marshal(bigaward);
			_os_.marshal(questtype);
			_os_.marshal(dstmapid);
			_os_.marshal(givetasknpcid);
			_os_.marshal(dstnpckey);
			_os_.marshal(dstnpcid);
			_os_.marshal(dstitemid);
			_os_.marshal(dstitemnum);
			_os_.marshal(dstx);
			_os_.marshal(dsty);
			_os_.marshal(lastgiveuptime);
			_os_.marshal(takequesttime);
			_os_.marshal(acceptquesttime);
			_os_.marshal(sametimes);
			_os_.marshal(lasttype);
			_os_.marshal(battlenum);
			_os_.marshal(firsttasktime);
			_os_.marshal(doneparam);
			_os_.marshal(npcname, mkdb.Const.IO_CHARSET);
			_os_.marshal(questclass);
			_os_.marshal(questgroup);
			_os_.marshal(autocomp);
			_os_.marshal(submitnpc);
			_os_.marshal(openawardnum);
			_os_.marshal(catchitsingletime);
			_os_.marshal(catchitdoubletime);
			_os_.marshal(subitemtableid);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_int();
			questid = _os_.unmarshal_int();
			queststate = _os_.unmarshal_int();
			round = _os_.unmarshal_int();
			sumnum = _os_.unmarshal_int();
			smallaward = _os_.unmarshal_int();
			bigaward = _os_.unmarshal_int();
			questtype = _os_.unmarshal_int();
			dstmapid = _os_.unmarshal_int();
			givetasknpcid = _os_.unmarshal_int();
			dstnpckey = _os_.unmarshal_long();
			dstnpcid = _os_.unmarshal_int();
			dstitemid = _os_.unmarshal_int();
			dstitemnum = _os_.unmarshal_int();
			dstx = _os_.unmarshal_int();
			dsty = _os_.unmarshal_int();
			lastgiveuptime = _os_.unmarshal_long();
			takequesttime = _os_.unmarshal_long();
			acceptquesttime = _os_.unmarshal_long();
			sametimes = _os_.unmarshal_int();
			lasttype = _os_.unmarshal_int();
			battlenum = _os_.unmarshal_int();
			firsttasktime = _os_.unmarshal_long();
			doneparam = _os_.unmarshal_int();
			npcname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			questclass = _os_.unmarshal_int();
			questgroup = _os_.unmarshal_int();
			autocomp = _os_.unmarshal_int();
			submitnpc = _os_.unmarshal_int();
			openawardnum = _os_.unmarshal_int();
			catchitsingletime = _os_.unmarshal_int();
			catchitdoubletime = _os_.unmarshal_int();
			subitemtableid = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.CircleTaskInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.CircleTaskInfo toData() {
			return new Data(this);
		}

		public xbean.CircleTaskInfo toBean() {
			return new CircleTaskInfo(this, null, null);
		}

		@Override
		public xbean.CircleTaskInfo toDataIf() {
			return this;
		}

		public xbean.CircleTaskInfo toBeanIf() {
			return new CircleTaskInfo(this, null, null);
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
		public int getId() { // x循环任务总表里面的id,标识一个唯一的任务,在第一环时确定该id,直到第十环结束如果再有循环任务,则重新确定该id
			return id;
		}

		@Override
		public int getQuestid() { // 任务id,代表 职业 公会 捉鬼之类
			return questid;
		}

		@Override
		public int getQueststate() { // 任务状态
			return queststate;
		}

		@Override
		public int getRound() { // 当前任务的环数
			return round;
		}

		@Override
		public int getSumnum() { // 累积次数. 根据不同的任务，拥有不同的含义
			return sumnum;
		}

		@Override
		public int getSmallaward() { // 小 奖励 累计次数
			return smallaward;
		}

		@Override
		public int getBigaward() { // 大 奖励 累积次数
			return bigaward;
		}

		@Override
		public int getQuesttype() { // 任务类型(这里是代表具体循环任务的id)
			return questtype;
		}

		@Override
		public int getDstmapid() { // 目的地图id
			return dstmapid;
		}

		@Override
		public int getGivetasknpcid() { // 接任务npc的id
			return givetasknpcid;
		}

		@Override
		public long getDstnpckey() { // 目的npc的key
			return dstnpckey;
		}

		@Override
		public int getDstnpcid() { // 目的npc的id
			return dstnpcid;
		}

		@Override
		public int getDstitemid() { // 目的道具的id
			return dstitemid;
		}

		@Override
		public int getDstitemnum() { // 目的道具的数量
			return dstitemnum;
		}

		@Override
		public int getDstx() { // 目的坐标
			return dstx;
		}

		@Override
		public int getDsty() { // 目的坐标
			return dsty;
		}

		@Override
		public long getLastgiveuptime() { // 本次任务放弃，下次任务的可接时间:已为放弃时间+delay. 0 为没有放弃,
			return lastgiveuptime;
		}

		@Override
		public long getTakequesttime() { // 接任务的时间,单位毫秒
			return takequesttime;
		}

		@Override
		public long getAcceptquesttime() { // 接任务的时间,单位毫秒 任何情况下都不变
			return acceptquesttime;
		}

		@Override
		public int getSametimes() { // 相同任务类型的次数
			return sametimes;
		}

		@Override
		public int getLasttype() { // 上次的任务类型
			return lasttype;
		}

		@Override
		public int getBattlenum() { // 战斗次数(传说战斗次数)
			return battlenum;
		}

		@Override
		public long getFirsttasktime() { // 今天第一次职业的时间,成就系统用.
			return firsttasktime;
		}

		@Override
		public int getDoneparam() { // 任务完成参数
			return doneparam;
		}

		@Override
		public String getNpcname() { // npc名称
			return npcname;
		}

		@Override
		public com.locojoy.base.Octets getNpcnameOctets() { // npc名称
			return com.locojoy.base.Octets.wrap(getNpcname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getQuestclass() { // 任务类型--类似送信，使用物品之类的任务
			return questclass;
		}

		@Override
		public int getQuestgroup() { // 组类型--类似需要在任务中分种族的任务
			return questgroup;
		}

		@Override
		public int getAutocomp() { // 是否自动完成,0:不自动完成,1:是自动完成
			return autocomp;
		}

		@Override
		public int getSubmitnpc() { // 非自动完成任务的提交npc
			return submitnpc;
		}

		@Override
		public int getOpenawardnum() { // 转盘奖励计数
			return openawardnum;
		}

		@Override
		public int getCatchitsingletime() { // 单倍捉鬼次数
			return catchitsingletime;
		}

		@Override
		public int getCatchitdoubletime() { // 双倍捉鬼次数
			return catchitdoubletime;
		}

		@Override
		public int getSubitemtableid() { // 上交物品的表id
			return subitemtableid;
		}

		@Override
		public void setId(int _v_) { // x循环任务总表里面的id,标识一个唯一的任务,在第一环时确定该id,直到第十环结束如果再有循环任务,则重新确定该id
			id = _v_;
		}

		@Override
		public void setQuestid(int _v_) { // 任务id,代表 职业 公会 捉鬼之类
			questid = _v_;
		}

		@Override
		public void setQueststate(int _v_) { // 任务状态
			queststate = _v_;
		}

		@Override
		public void setRound(int _v_) { // 当前任务的环数
			round = _v_;
		}

		@Override
		public void setSumnum(int _v_) { // 累积次数. 根据不同的任务，拥有不同的含义
			sumnum = _v_;
		}

		@Override
		public void setSmallaward(int _v_) { // 小 奖励 累计次数
			smallaward = _v_;
		}

		@Override
		public void setBigaward(int _v_) { // 大 奖励 累积次数
			bigaward = _v_;
		}

		@Override
		public void setQuesttype(int _v_) { // 任务类型(这里是代表具体循环任务的id)
			questtype = _v_;
		}

		@Override
		public void setDstmapid(int _v_) { // 目的地图id
			dstmapid = _v_;
		}

		@Override
		public void setGivetasknpcid(int _v_) { // 接任务npc的id
			givetasknpcid = _v_;
		}

		@Override
		public void setDstnpckey(long _v_) { // 目的npc的key
			dstnpckey = _v_;
		}

		@Override
		public void setDstnpcid(int _v_) { // 目的npc的id
			dstnpcid = _v_;
		}

		@Override
		public void setDstitemid(int _v_) { // 目的道具的id
			dstitemid = _v_;
		}

		@Override
		public void setDstitemnum(int _v_) { // 目的道具的数量
			dstitemnum = _v_;
		}

		@Override
		public void setDstx(int _v_) { // 目的坐标
			dstx = _v_;
		}

		@Override
		public void setDsty(int _v_) { // 目的坐标
			dsty = _v_;
		}

		@Override
		public void setLastgiveuptime(long _v_) { // 本次任务放弃，下次任务的可接时间:已为放弃时间+delay. 0 为没有放弃,
			lastgiveuptime = _v_;
		}

		@Override
		public void setTakequesttime(long _v_) { // 接任务的时间,单位毫秒
			takequesttime = _v_;
		}

		@Override
		public void setAcceptquesttime(long _v_) { // 接任务的时间,单位毫秒 任何情况下都不变
			acceptquesttime = _v_;
		}

		@Override
		public void setSametimes(int _v_) { // 相同任务类型的次数
			sametimes = _v_;
		}

		@Override
		public void setLasttype(int _v_) { // 上次的任务类型
			lasttype = _v_;
		}

		@Override
		public void setBattlenum(int _v_) { // 战斗次数(传说战斗次数)
			battlenum = _v_;
		}

		@Override
		public void setFirsttasktime(long _v_) { // 今天第一次职业的时间,成就系统用.
			firsttasktime = _v_;
		}

		@Override
		public void setDoneparam(int _v_) { // 任务完成参数
			doneparam = _v_;
		}

		@Override
		public void setNpcname(String _v_) { // npc名称
			if (null == _v_)
				throw new NullPointerException();
			npcname = _v_;
		}

		@Override
		public void setNpcnameOctets(com.locojoy.base.Octets _v_) { // npc名称
			this.setNpcname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setQuestclass(int _v_) { // 任务类型--类似送信，使用物品之类的任务
			questclass = _v_;
		}

		@Override
		public void setQuestgroup(int _v_) { // 组类型--类似需要在任务中分种族的任务
			questgroup = _v_;
		}

		@Override
		public void setAutocomp(int _v_) { // 是否自动完成,0:不自动完成,1:是自动完成
			autocomp = _v_;
		}

		@Override
		public void setSubmitnpc(int _v_) { // 非自动完成任务的提交npc
			submitnpc = _v_;
		}

		@Override
		public void setOpenawardnum(int _v_) { // 转盘奖励计数
			openawardnum = _v_;
		}

		@Override
		public void setCatchitsingletime(int _v_) { // 单倍捉鬼次数
			catchitsingletime = _v_;
		}

		@Override
		public void setCatchitdoubletime(int _v_) { // 双倍捉鬼次数
			catchitdoubletime = _v_;
		}

		@Override
		public void setSubitemtableid(int _v_) { // 上交物品的表id
			subitemtableid = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof CircleTaskInfo.Data)) return false;
			CircleTaskInfo.Data _o_ = (CircleTaskInfo.Data) _o1_;
			if (id != _o_.id) return false;
			if (questid != _o_.questid) return false;
			if (queststate != _o_.queststate) return false;
			if (round != _o_.round) return false;
			if (sumnum != _o_.sumnum) return false;
			if (smallaward != _o_.smallaward) return false;
			if (bigaward != _o_.bigaward) return false;
			if (questtype != _o_.questtype) return false;
			if (dstmapid != _o_.dstmapid) return false;
			if (givetasknpcid != _o_.givetasknpcid) return false;
			if (dstnpckey != _o_.dstnpckey) return false;
			if (dstnpcid != _o_.dstnpcid) return false;
			if (dstitemid != _o_.dstitemid) return false;
			if (dstitemnum != _o_.dstitemnum) return false;
			if (dstx != _o_.dstx) return false;
			if (dsty != _o_.dsty) return false;
			if (lastgiveuptime != _o_.lastgiveuptime) return false;
			if (takequesttime != _o_.takequesttime) return false;
			if (acceptquesttime != _o_.acceptquesttime) return false;
			if (sametimes != _o_.sametimes) return false;
			if (lasttype != _o_.lasttype) return false;
			if (battlenum != _o_.battlenum) return false;
			if (firsttasktime != _o_.firsttasktime) return false;
			if (doneparam != _o_.doneparam) return false;
			if (!npcname.equals(_o_.npcname)) return false;
			if (questclass != _o_.questclass) return false;
			if (questgroup != _o_.questgroup) return false;
			if (autocomp != _o_.autocomp) return false;
			if (submitnpc != _o_.submitnpc) return false;
			if (openawardnum != _o_.openawardnum) return false;
			if (catchitsingletime != _o_.catchitsingletime) return false;
			if (catchitdoubletime != _o_.catchitdoubletime) return false;
			if (subitemtableid != _o_.subitemtableid) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += questid;
			_h_ += queststate;
			_h_ += round;
			_h_ += sumnum;
			_h_ += smallaward;
			_h_ += bigaward;
			_h_ += questtype;
			_h_ += dstmapid;
			_h_ += givetasknpcid;
			_h_ += dstnpckey;
			_h_ += dstnpcid;
			_h_ += dstitemid;
			_h_ += dstitemnum;
			_h_ += dstx;
			_h_ += dsty;
			_h_ += lastgiveuptime;
			_h_ += takequesttime;
			_h_ += acceptquesttime;
			_h_ += sametimes;
			_h_ += lasttype;
			_h_ += battlenum;
			_h_ += firsttasktime;
			_h_ += doneparam;
			_h_ += npcname.hashCode();
			_h_ += questclass;
			_h_ += questgroup;
			_h_ += autocomp;
			_h_ += submitnpc;
			_h_ += openawardnum;
			_h_ += catchitsingletime;
			_h_ += catchitdoubletime;
			_h_ += subitemtableid;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(questid);
			_sb_.append(",");
			_sb_.append(queststate);
			_sb_.append(",");
			_sb_.append(round);
			_sb_.append(",");
			_sb_.append(sumnum);
			_sb_.append(",");
			_sb_.append(smallaward);
			_sb_.append(",");
			_sb_.append(bigaward);
			_sb_.append(",");
			_sb_.append(questtype);
			_sb_.append(",");
			_sb_.append(dstmapid);
			_sb_.append(",");
			_sb_.append(givetasknpcid);
			_sb_.append(",");
			_sb_.append(dstnpckey);
			_sb_.append(",");
			_sb_.append(dstnpcid);
			_sb_.append(",");
			_sb_.append(dstitemid);
			_sb_.append(",");
			_sb_.append(dstitemnum);
			_sb_.append(",");
			_sb_.append(dstx);
			_sb_.append(",");
			_sb_.append(dsty);
			_sb_.append(",");
			_sb_.append(lastgiveuptime);
			_sb_.append(",");
			_sb_.append(takequesttime);
			_sb_.append(",");
			_sb_.append(acceptquesttime);
			_sb_.append(",");
			_sb_.append(sametimes);
			_sb_.append(",");
			_sb_.append(lasttype);
			_sb_.append(",");
			_sb_.append(battlenum);
			_sb_.append(",");
			_sb_.append(firsttasktime);
			_sb_.append(",");
			_sb_.append(doneparam);
			_sb_.append(",");
			_sb_.append("'").append(npcname).append("'");
			_sb_.append(",");
			_sb_.append(questclass);
			_sb_.append(",");
			_sb_.append(questgroup);
			_sb_.append(",");
			_sb_.append(autocomp);
			_sb_.append(",");
			_sb_.append(submitnpc);
			_sb_.append(",");
			_sb_.append(openawardnum);
			_sb_.append(",");
			_sb_.append(catchitsingletime);
			_sb_.append(",");
			_sb_.append(catchitdoubletime);
			_sb_.append(",");
			_sb_.append(subitemtableid);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
