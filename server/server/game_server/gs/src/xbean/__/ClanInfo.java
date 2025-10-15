
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class ClanInfo extends mkdb.XBean implements xbean.ClanInfo {
	private int index; // 公会序号
	private long key; // 公会表里的key
	private String clanname; // 公会名字
	private String oldclanname; // 公会曾用名
	private long creator; // 公会创始人
	private int popularity; // 公会人气度
	private int clanlevel; // 公会级别
	private int clanlevelmax; // 公会增加到达的最大等级 by changhao
	private String clanaim; // 公会宗旨
	private long clanmaster; // 会长
	private long clanvicemaster; // 副会长
	private int clanrank; // 公会排名
	private java.util.HashMap<Integer, xbean.ClanSkill> clanskillmap; // 公会技能信息
	private java.util.HashMap<Long, xbean.ClanMemberInfo> members; // 公会成员
	private java.util.HashMap<Long, Long> applicants; // 申请加入公会人员
	private long createtime; // 公会创建时间
	private int money; // 公会资金 by changhao
	private java.util.HashMap<Integer, Integer> house; // 2金库3药房4旅馆  对应等级  by changhao
	private int bonus; // 公会分红 by changhao
	private int autostate; // 公会自动接收申请人入会的状态：0关闭 1开启
	private int totalscore; // 公会总评分 by changhao
	private int firemembernum; // 每日踢出次数
	private int claninstservice; // 公会选择的副本服务编号
	private java.util.ArrayList<xbean.ClanEventRecord> claneventrecordlist; // 公会事件
	private short requestlevel; // 公会自动接收申请人入会的等级
	private xbean.ImpeachMent impeachment; // 弹劾
	private int clandkp; // 本周公会dkp
	private int lastclandkp; // 上周公会dkp

	@Override
	public void _reset_unsafe_() {
		index = 0;
		key = 0L;
		clanname = "";
		oldclanname = "";
		creator = 0L;
		popularity = 0;
		clanlevel = 1;
		clanlevelmax = 1;
		clanaim = "";
		clanmaster = 0L;
		clanvicemaster = -1;
		clanrank = 0;
		clanskillmap.clear();
		members.clear();
		applicants.clear();
		createtime = 0L;
		money = 0;
		house.clear();
		bonus = 0;
		autostate = 0;
		totalscore = 0;
		firemembernum = 0;
		claninstservice = 0;
		claneventrecordlist.clear();
		requestlevel = 1;
		impeachment._reset_unsafe_();
		clandkp = 0;
		lastclandkp = 0;
	}

	ClanInfo(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		clanname = "";
		oldclanname = "";
		clanlevel = 1;
		clanlevelmax = 1;
		clanaim = "";
		clanvicemaster = -1;
		clanskillmap = new java.util.HashMap<Integer, xbean.ClanSkill>();
		members = new java.util.HashMap<Long, xbean.ClanMemberInfo>();
		applicants = new java.util.HashMap<Long, Long>();
		money = 0;
		house = new java.util.HashMap<Integer, Integer>();
		autostate = 0;
		claneventrecordlist = new java.util.ArrayList<xbean.ClanEventRecord>();
		requestlevel = 1;
		impeachment = new ImpeachMent(0, this, "impeachment");
		clandkp = 0;
		lastclandkp = 0;
	}

	public ClanInfo() {
		this(0, null, null);
	}

	public ClanInfo(ClanInfo _o_) {
		this(_o_, null, null);
	}

	ClanInfo(xbean.ClanInfo _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof ClanInfo) assign((ClanInfo)_o1_);
		else if (_o1_ instanceof ClanInfo.Data) assign((ClanInfo.Data)_o1_);
		else if (_o1_ instanceof ClanInfo.Const) assign(((ClanInfo.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(ClanInfo _o_) {
		_o_._xdb_verify_unsafe_();
		index = _o_.index;
		key = _o_.key;
		clanname = _o_.clanname;
		oldclanname = _o_.oldclanname;
		creator = _o_.creator;
		popularity = _o_.popularity;
		clanlevel = _o_.clanlevel;
		clanlevelmax = _o_.clanlevelmax;
		clanaim = _o_.clanaim;
		clanmaster = _o_.clanmaster;
		clanvicemaster = _o_.clanvicemaster;
		clanrank = _o_.clanrank;
		clanskillmap = new java.util.HashMap<Integer, xbean.ClanSkill>();
		for (java.util.Map.Entry<Integer, xbean.ClanSkill> _e_ : _o_.clanskillmap.entrySet())
			clanskillmap.put(_e_.getKey(), new ClanSkill(_e_.getValue(), this, "clanskillmap"));
		members = new java.util.HashMap<Long, xbean.ClanMemberInfo>();
		for (java.util.Map.Entry<Long, xbean.ClanMemberInfo> _e_ : _o_.members.entrySet())
			members.put(_e_.getKey(), new ClanMemberInfo(_e_.getValue(), this, "members"));
		applicants = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.applicants.entrySet())
			applicants.put(_e_.getKey(), _e_.getValue());
		createtime = _o_.createtime;
		money = _o_.money;
		house = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.house.entrySet())
			house.put(_e_.getKey(), _e_.getValue());
		bonus = _o_.bonus;
		autostate = _o_.autostate;
		totalscore = _o_.totalscore;
		firemembernum = _o_.firemembernum;
		claninstservice = _o_.claninstservice;
		claneventrecordlist = new java.util.ArrayList<xbean.ClanEventRecord>();
		for (xbean.ClanEventRecord _v_ : _o_.claneventrecordlist)
			claneventrecordlist.add(new ClanEventRecord(_v_, this, "claneventrecordlist"));
		requestlevel = _o_.requestlevel;
		impeachment = new ImpeachMent(_o_.impeachment, this, "impeachment");
		clandkp = _o_.clandkp;
		lastclandkp = _o_.lastclandkp;
	}

	private void assign(ClanInfo.Data _o_) {
		index = _o_.index;
		key = _o_.key;
		clanname = _o_.clanname;
		oldclanname = _o_.oldclanname;
		creator = _o_.creator;
		popularity = _o_.popularity;
		clanlevel = _o_.clanlevel;
		clanlevelmax = _o_.clanlevelmax;
		clanaim = _o_.clanaim;
		clanmaster = _o_.clanmaster;
		clanvicemaster = _o_.clanvicemaster;
		clanrank = _o_.clanrank;
		clanskillmap = new java.util.HashMap<Integer, xbean.ClanSkill>();
		for (java.util.Map.Entry<Integer, xbean.ClanSkill> _e_ : _o_.clanskillmap.entrySet())
			clanskillmap.put(_e_.getKey(), new ClanSkill(_e_.getValue(), this, "clanskillmap"));
		members = new java.util.HashMap<Long, xbean.ClanMemberInfo>();
		for (java.util.Map.Entry<Long, xbean.ClanMemberInfo> _e_ : _o_.members.entrySet())
			members.put(_e_.getKey(), new ClanMemberInfo(_e_.getValue(), this, "members"));
		applicants = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.applicants.entrySet())
			applicants.put(_e_.getKey(), _e_.getValue());
		createtime = _o_.createtime;
		money = _o_.money;
		house = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.house.entrySet())
			house.put(_e_.getKey(), _e_.getValue());
		bonus = _o_.bonus;
		autostate = _o_.autostate;
		totalscore = _o_.totalscore;
		firemembernum = _o_.firemembernum;
		claninstservice = _o_.claninstservice;
		claneventrecordlist = new java.util.ArrayList<xbean.ClanEventRecord>();
		for (xbean.ClanEventRecord _v_ : _o_.claneventrecordlist)
			claneventrecordlist.add(new ClanEventRecord(_v_, this, "claneventrecordlist"));
		requestlevel = _o_.requestlevel;
		impeachment = new ImpeachMent(_o_.impeachment, this, "impeachment");
		clandkp = _o_.clandkp;
		lastclandkp = _o_.lastclandkp;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(index);
		_os_.marshal(key);
		_os_.marshal(clanname, mkdb.Const.IO_CHARSET);
		_os_.marshal(oldclanname, mkdb.Const.IO_CHARSET);
		_os_.marshal(creator);
		_os_.marshal(popularity);
		_os_.marshal(clanlevel);
		_os_.marshal(clanlevelmax);
		_os_.marshal(clanaim, mkdb.Const.IO_CHARSET);
		_os_.marshal(clanmaster);
		_os_.marshal(clanvicemaster);
		_os_.marshal(clanrank);
		_os_.compact_uint32(clanskillmap.size());
		for (java.util.Map.Entry<Integer, xbean.ClanSkill> _e_ : clanskillmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(members.size());
		for (java.util.Map.Entry<Long, xbean.ClanMemberInfo> _e_ : members.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.compact_uint32(applicants.size());
		for (java.util.Map.Entry<Long, Long> _e_ : applicants.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(createtime);
		_os_.marshal(money);
		_os_.compact_uint32(house.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : house.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(bonus);
		_os_.marshal(autostate);
		_os_.marshal(totalscore);
		_os_.marshal(firemembernum);
		_os_.marshal(claninstservice);
		_os_.compact_uint32(claneventrecordlist.size());
		for (xbean.ClanEventRecord _v_ : claneventrecordlist) {
			_v_.marshal(_os_);
		}
		_os_.marshal(requestlevel);
		impeachment.marshal(_os_);
		_os_.marshal(clandkp);
		_os_.marshal(lastclandkp);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		index = _os_.unmarshal_int();
		key = _os_.unmarshal_long();
		clanname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		oldclanname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		creator = _os_.unmarshal_long();
		popularity = _os_.unmarshal_int();
		clanlevel = _os_.unmarshal_int();
		clanlevelmax = _os_.unmarshal_int();
		clanaim = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		clanmaster = _os_.unmarshal_long();
		clanvicemaster = _os_.unmarshal_long();
		clanrank = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				clanskillmap = new java.util.HashMap<Integer, xbean.ClanSkill>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.ClanSkill _v_ = new ClanSkill(0, this, "clanskillmap");
				_v_.unmarshal(_os_);
				clanskillmap.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				members = new java.util.HashMap<Long, xbean.ClanMemberInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				xbean.ClanMemberInfo _v_ = new ClanMemberInfo(0, this, "members");
				_v_.unmarshal(_os_);
				members.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				applicants = new java.util.HashMap<Long, Long>(size * 2);
			}
			for (; size > 0; --size)
			{
				long _k_ = 0;
				_k_ = _os_.unmarshal_long();
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				applicants.put(_k_, _v_);
			}
		}
		createtime = _os_.unmarshal_long();
		money = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				house = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				house.put(_k_, _v_);
			}
		}
		bonus = _os_.unmarshal_int();
		autostate = _os_.unmarshal_int();
		totalscore = _os_.unmarshal_int();
		firemembernum = _os_.unmarshal_int();
		claninstservice = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.ClanEventRecord _v_ = new ClanEventRecord(0, this, "claneventrecordlist");
			_v_.unmarshal(_os_);
			claneventrecordlist.add(_v_);
		}
		requestlevel = _os_.unmarshal_short();
		impeachment.unmarshal(_os_);
		clandkp = _os_.unmarshal_int();
		lastclandkp = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.ClanInfo copy() {
		_xdb_verify_unsafe_();
		return new ClanInfo(this);
	}

	@Override
	public xbean.ClanInfo toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanInfo toBean() {
		_xdb_verify_unsafe_();
		return new ClanInfo(this); // same as copy()
	}

	@Override
	public xbean.ClanInfo toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.ClanInfo toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public int getIndex() { // 公会序号
		_xdb_verify_unsafe_();
		return index;
	}

	@Override
	public long getKey() { // 公会表里的key
		_xdb_verify_unsafe_();
		return key;
	}

	@Override
	public String getClanname() { // 公会名字
		_xdb_verify_unsafe_();
		return clanname;
	}

	@Override
	public com.locojoy.base.Octets getClannameOctets() { // 公会名字
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getClanname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public String getOldclanname() { // 公会曾用名
		_xdb_verify_unsafe_();
		return oldclanname;
	}

	@Override
	public com.locojoy.base.Octets getOldclannameOctets() { // 公会曾用名
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getOldclanname(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getCreator() { // 公会创始人
		_xdb_verify_unsafe_();
		return creator;
	}

	@Override
	public int getPopularity() { // 公会人气度
		_xdb_verify_unsafe_();
		return popularity;
	}

	@Override
	public int getClanlevel() { // 公会级别
		_xdb_verify_unsafe_();
		return clanlevel;
	}

	@Override
	public int getClanlevelmax() { // 公会增加到达的最大等级 by changhao
		_xdb_verify_unsafe_();
		return clanlevelmax;
	}

	@Override
	public String getClanaim() { // 公会宗旨
		_xdb_verify_unsafe_();
		return clanaim;
	}

	@Override
	public com.locojoy.base.Octets getClanaimOctets() { // 公会宗旨
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getClanaim(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getClanmaster() { // 会长
		_xdb_verify_unsafe_();
		return clanmaster;
	}

	@Override
	public long getClanvicemaster() { // 副会长
		_xdb_verify_unsafe_();
		return clanvicemaster;
	}

	@Override
	public int getClanrank() { // 公会排名
		_xdb_verify_unsafe_();
		return clanrank;
	}

	@Override
	public java.util.Map<Integer, xbean.ClanSkill> getClanskillmap() { // 公会技能信息
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "clanskillmap"), clanskillmap);
	}

	@Override
	public java.util.Map<Integer, xbean.ClanSkill> getClanskillmapAsData() { // 公会技能信息
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.ClanSkill> clanskillmap;
		ClanInfo _o_ = this;
		clanskillmap = new java.util.HashMap<Integer, xbean.ClanSkill>();
		for (java.util.Map.Entry<Integer, xbean.ClanSkill> _e_ : _o_.clanskillmap.entrySet())
			clanskillmap.put(_e_.getKey(), new ClanSkill.Data(_e_.getValue()));
		return clanskillmap;
	}

	@Override
	public java.util.Map<Long, xbean.ClanMemberInfo> getMembers() { // 公会成员
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "members"), members);
	}

	@Override
	public java.util.Map<Long, xbean.ClanMemberInfo> getMembersAsData() { // 公会成员
		_xdb_verify_unsafe_();
		java.util.Map<Long, xbean.ClanMemberInfo> members;
		ClanInfo _o_ = this;
		members = new java.util.HashMap<Long, xbean.ClanMemberInfo>();
		for (java.util.Map.Entry<Long, xbean.ClanMemberInfo> _e_ : _o_.members.entrySet())
			members.put(_e_.getKey(), new ClanMemberInfo.Data(_e_.getValue()));
		return members;
	}

	@Override
	public java.util.Map<Long, Long> getApplicants() { // 申请加入公会人员
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "applicants"), applicants);
	}

	@Override
	public java.util.Map<Long, Long> getApplicantsAsData() { // 申请加入公会人员
		_xdb_verify_unsafe_();
		java.util.Map<Long, Long> applicants;
		ClanInfo _o_ = this;
		applicants = new java.util.HashMap<Long, Long>();
		for (java.util.Map.Entry<Long, Long> _e_ : _o_.applicants.entrySet())
			applicants.put(_e_.getKey(), _e_.getValue());
		return applicants;
	}

	@Override
	public long getCreatetime() { // 公会创建时间
		_xdb_verify_unsafe_();
		return createtime;
	}

	@Override
	public int getMoney() { // 公会资金 by changhao
		_xdb_verify_unsafe_();
		return money;
	}

	@Override
	public java.util.Map<Integer, Integer> getHouse() { // 2金库3药房4旅馆  对应等级  by changhao
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "house"), house);
	}

	@Override
	public java.util.Map<Integer, Integer> getHouseAsData() { // 2金库3药房4旅馆  对应等级  by changhao
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> house;
		ClanInfo _o_ = this;
		house = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.house.entrySet())
			house.put(_e_.getKey(), _e_.getValue());
		return house;
	}

	@Override
	public int getBonus() { // 公会分红 by changhao
		_xdb_verify_unsafe_();
		return bonus;
	}

	@Override
	public int getAutostate() { // 公会自动接收申请人入会的状态：0关闭 1开启
		_xdb_verify_unsafe_();
		return autostate;
	}

	@Override
	public int getTotalscore() { // 公会总评分 by changhao
		_xdb_verify_unsafe_();
		return totalscore;
	}

	@Override
	public int getFiremembernum() { // 每日踢出次数
		_xdb_verify_unsafe_();
		return firemembernum;
	}

	@Override
	public int getClaninstservice() { // 公会选择的副本服务编号
		_xdb_verify_unsafe_();
		return claninstservice;
	}

	@Override
	public java.util.List<xbean.ClanEventRecord> getClaneventrecordlist() { // 公会事件
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "claneventrecordlist"), claneventrecordlist);
	}

	public java.util.List<xbean.ClanEventRecord> getClaneventrecordlistAsData() { // 公会事件
		_xdb_verify_unsafe_();
		java.util.List<xbean.ClanEventRecord> claneventrecordlist;
		ClanInfo _o_ = this;
		claneventrecordlist = new java.util.ArrayList<xbean.ClanEventRecord>();
		for (xbean.ClanEventRecord _v_ : _o_.claneventrecordlist)
			claneventrecordlist.add(new ClanEventRecord.Data(_v_));
		return claneventrecordlist;
	}

	@Override
	public short getRequestlevel() { // 公会自动接收申请人入会的等级
		_xdb_verify_unsafe_();
		return requestlevel;
	}

	@Override
	public xbean.ImpeachMent getImpeachment() { // 弹劾
		_xdb_verify_unsafe_();
		return impeachment;
	}

	@Override
	public int getClandkp() { // 本周公会dkp
		_xdb_verify_unsafe_();
		return clandkp;
	}

	@Override
	public int getLastclandkp() { // 上周公会dkp
		_xdb_verify_unsafe_();
		return lastclandkp;
	}

	@Override
	public void setIndex(int _v_) { // 公会序号
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "index") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, index) {
					public void rollback() { index = _xdb_saved; }
				};}});
		index = _v_;
	}

	@Override
	public void setKey(long _v_) { // 公会表里的key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "key") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, key) {
					public void rollback() { key = _xdb_saved; }
				};}});
		key = _v_;
	}

	@Override
	public void setClanname(String _v_) { // 公会名字
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanname") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, clanname) {
					public void rollback() { clanname = _xdb_saved; }
				};}});
		clanname = _v_;
	}

	@Override
	public void setClannameOctets(com.locojoy.base.Octets _v_) { // 公会名字
		_xdb_verify_unsafe_();
		this.setClanname(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setOldclanname(String _v_) { // 公会曾用名
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "oldclanname") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, oldclanname) {
					public void rollback() { oldclanname = _xdb_saved; }
				};}});
		oldclanname = _v_;
	}

	@Override
	public void setOldclannameOctets(com.locojoy.base.Octets _v_) { // 公会曾用名
		_xdb_verify_unsafe_();
		this.setOldclanname(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setCreator(long _v_) { // 公会创始人
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "creator") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, creator) {
					public void rollback() { creator = _xdb_saved; }
				};}});
		creator = _v_;
	}

	@Override
	public void setPopularity(int _v_) { // 公会人气度
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "popularity") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, popularity) {
					public void rollback() { popularity = _xdb_saved; }
				};}});
		popularity = _v_;
	}

	@Override
	public void setClanlevel(int _v_) { // 公会级别
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanlevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanlevel) {
					public void rollback() { clanlevel = _xdb_saved; }
				};}});
		clanlevel = _v_;
	}

	@Override
	public void setClanlevelmax(int _v_) { // 公会增加到达的最大等级 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanlevelmax") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanlevelmax) {
					public void rollback() { clanlevelmax = _xdb_saved; }
				};}});
		clanlevelmax = _v_;
	}

	@Override
	public void setClanaim(String _v_) { // 公会宗旨
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanaim") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, clanaim) {
					public void rollback() { clanaim = _xdb_saved; }
				};}});
		clanaim = _v_;
	}

	@Override
	public void setClanaimOctets(com.locojoy.base.Octets _v_) { // 公会宗旨
		_xdb_verify_unsafe_();
		this.setClanaim(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setClanmaster(long _v_) { // 会长
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanmaster") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clanmaster) {
					public void rollback() { clanmaster = _xdb_saved; }
				};}});
		clanmaster = _v_;
	}

	@Override
	public void setClanvicemaster(long _v_) { // 副会长
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanvicemaster") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clanvicemaster) {
					public void rollback() { clanvicemaster = _xdb_saved; }
				};}});
		clanvicemaster = _v_;
	}

	@Override
	public void setClanrank(int _v_) { // 公会排名
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clanrank") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clanrank) {
					public void rollback() { clanrank = _xdb_saved; }
				};}});
		clanrank = _v_;
	}

	@Override
	public void setCreatetime(long _v_) { // 公会创建时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "createtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, createtime) {
					public void rollback() { createtime = _xdb_saved; }
				};}});
		createtime = _v_;
	}

	@Override
	public void setMoney(int _v_) { // 公会资金 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "money") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, money) {
					public void rollback() { money = _xdb_saved; }
				};}});
		money = _v_;
	}

	@Override
	public void setBonus(int _v_) { // 公会分红 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bonus") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bonus) {
					public void rollback() { bonus = _xdb_saved; }
				};}});
		bonus = _v_;
	}

	@Override
	public void setAutostate(int _v_) { // 公会自动接收申请人入会的状态：0关闭 1开启
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "autostate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, autostate) {
					public void rollback() { autostate = _xdb_saved; }
				};}});
		autostate = _v_;
	}

	@Override
	public void setTotalscore(int _v_) { // 公会总评分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "totalscore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, totalscore) {
					public void rollback() { totalscore = _xdb_saved; }
				};}});
		totalscore = _v_;
	}

	@Override
	public void setFiremembernum(int _v_) { // 每日踢出次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firemembernum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, firemembernum) {
					public void rollback() { firemembernum = _xdb_saved; }
				};}});
		firemembernum = _v_;
	}

	@Override
	public void setClaninstservice(int _v_) { // 公会选择的副本服务编号
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "claninstservice") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, claninstservice) {
					public void rollback() { claninstservice = _xdb_saved; }
				};}});
		claninstservice = _v_;
	}

	@Override
	public void setRequestlevel(short _v_) { // 公会自动接收申请人入会的等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "requestlevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogShort(this, requestlevel) {
					public void rollback() { requestlevel = _xdb_saved; }
				};}});
		requestlevel = _v_;
	}

	@Override
	public void setClandkp(int _v_) { // 本周公会dkp
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clandkp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, clandkp) {
					public void rollback() { clandkp = _xdb_saved; }
				};}});
		clandkp = _v_;
	}

	@Override
	public void setLastclandkp(int _v_) { // 上周公会dkp
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastclandkp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lastclandkp) {
					public void rollback() { lastclandkp = _xdb_saved; }
				};}});
		lastclandkp = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		ClanInfo _o_ = null;
		if ( _o1_ instanceof ClanInfo ) _o_ = (ClanInfo)_o1_;
		else if ( _o1_ instanceof ClanInfo.Const ) _o_ = ((ClanInfo.Const)_o1_).nThis();
		else return false;
		if (index != _o_.index) return false;
		if (key != _o_.key) return false;
		if (!clanname.equals(_o_.clanname)) return false;
		if (!oldclanname.equals(_o_.oldclanname)) return false;
		if (creator != _o_.creator) return false;
		if (popularity != _o_.popularity) return false;
		if (clanlevel != _o_.clanlevel) return false;
		if (clanlevelmax != _o_.clanlevelmax) return false;
		if (!clanaim.equals(_o_.clanaim)) return false;
		if (clanmaster != _o_.clanmaster) return false;
		if (clanvicemaster != _o_.clanvicemaster) return false;
		if (clanrank != _o_.clanrank) return false;
		if (!clanskillmap.equals(_o_.clanskillmap)) return false;
		if (!members.equals(_o_.members)) return false;
		if (!applicants.equals(_o_.applicants)) return false;
		if (createtime != _o_.createtime) return false;
		if (money != _o_.money) return false;
		if (!house.equals(_o_.house)) return false;
		if (bonus != _o_.bonus) return false;
		if (autostate != _o_.autostate) return false;
		if (totalscore != _o_.totalscore) return false;
		if (firemembernum != _o_.firemembernum) return false;
		if (claninstservice != _o_.claninstservice) return false;
		if (!claneventrecordlist.equals(_o_.claneventrecordlist)) return false;
		if (requestlevel != _o_.requestlevel) return false;
		if (!impeachment.equals(_o_.impeachment)) return false;
		if (clandkp != _o_.clandkp) return false;
		if (lastclandkp != _o_.lastclandkp) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += index;
		_h_ += key;
		_h_ += clanname.hashCode();
		_h_ += oldclanname.hashCode();
		_h_ += creator;
		_h_ += popularity;
		_h_ += clanlevel;
		_h_ += clanlevelmax;
		_h_ += clanaim.hashCode();
		_h_ += clanmaster;
		_h_ += clanvicemaster;
		_h_ += clanrank;
		_h_ += clanskillmap.hashCode();
		_h_ += members.hashCode();
		_h_ += applicants.hashCode();
		_h_ += createtime;
		_h_ += money;
		_h_ += house.hashCode();
		_h_ += bonus;
		_h_ += autostate;
		_h_ += totalscore;
		_h_ += firemembernum;
		_h_ += claninstservice;
		_h_ += claneventrecordlist.hashCode();
		_h_ += requestlevel;
		_h_ += impeachment.hashCode();
		_h_ += clandkp;
		_h_ += lastclandkp;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(index);
		_sb_.append(",");
		_sb_.append(key);
		_sb_.append(",");
		_sb_.append("'").append(clanname).append("'");
		_sb_.append(",");
		_sb_.append("'").append(oldclanname).append("'");
		_sb_.append(",");
		_sb_.append(creator);
		_sb_.append(",");
		_sb_.append(popularity);
		_sb_.append(",");
		_sb_.append(clanlevel);
		_sb_.append(",");
		_sb_.append(clanlevelmax);
		_sb_.append(",");
		_sb_.append("'").append(clanaim).append("'");
		_sb_.append(",");
		_sb_.append(clanmaster);
		_sb_.append(",");
		_sb_.append(clanvicemaster);
		_sb_.append(",");
		_sb_.append(clanrank);
		_sb_.append(",");
		_sb_.append(clanskillmap);
		_sb_.append(",");
		_sb_.append(members);
		_sb_.append(",");
		_sb_.append(applicants);
		_sb_.append(",");
		_sb_.append(createtime);
		_sb_.append(",");
		_sb_.append(money);
		_sb_.append(",");
		_sb_.append(house);
		_sb_.append(",");
		_sb_.append(bonus);
		_sb_.append(",");
		_sb_.append(autostate);
		_sb_.append(",");
		_sb_.append(totalscore);
		_sb_.append(",");
		_sb_.append(firemembernum);
		_sb_.append(",");
		_sb_.append(claninstservice);
		_sb_.append(",");
		_sb_.append(claneventrecordlist);
		_sb_.append(",");
		_sb_.append(requestlevel);
		_sb_.append(",");
		_sb_.append(impeachment);
		_sb_.append(",");
		_sb_.append(clandkp);
		_sb_.append(",");
		_sb_.append(lastclandkp);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("index"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("key"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("oldclanname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("creator"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("popularity"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanlevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanlevelmax"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanaim"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanmaster"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanvicemaster"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clanrank"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("clanskillmap"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("members"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("applicants"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("createtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("money"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("house"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bonus"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("autostate"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("totalscore"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firemembernum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("claninstservice"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("claneventrecordlist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("requestlevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("impeachment"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clandkp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastclandkp"));
		return lb;
	}

	private class Const implements xbean.ClanInfo {
		ClanInfo nThis() {
			return ClanInfo.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.ClanInfo copy() {
			return ClanInfo.this.copy();
		}

		@Override
		public xbean.ClanInfo toData() {
			return ClanInfo.this.toData();
		}

		public xbean.ClanInfo toBean() {
			return ClanInfo.this.toBean();
		}

		@Override
		public xbean.ClanInfo toDataIf() {
			return ClanInfo.this.toDataIf();
		}

		public xbean.ClanInfo toBeanIf() {
			return ClanInfo.this.toBeanIf();
		}

		@Override
		public int getIndex() { // 公会序号
			_xdb_verify_unsafe_();
			return index;
		}

		@Override
		public long getKey() { // 公会表里的key
			_xdb_verify_unsafe_();
			return key;
		}

		@Override
		public String getClanname() { // 公会名字
			_xdb_verify_unsafe_();
			return clanname;
		}

		@Override
		public com.locojoy.base.Octets getClannameOctets() { // 公会名字
			_xdb_verify_unsafe_();
			return ClanInfo.this.getClannameOctets();
		}

		@Override
		public String getOldclanname() { // 公会曾用名
			_xdb_verify_unsafe_();
			return oldclanname;
		}

		@Override
		public com.locojoy.base.Octets getOldclannameOctets() { // 公会曾用名
			_xdb_verify_unsafe_();
			return ClanInfo.this.getOldclannameOctets();
		}

		@Override
		public long getCreator() { // 公会创始人
			_xdb_verify_unsafe_();
			return creator;
		}

		@Override
		public int getPopularity() { // 公会人气度
			_xdb_verify_unsafe_();
			return popularity;
		}

		@Override
		public int getClanlevel() { // 公会级别
			_xdb_verify_unsafe_();
			return clanlevel;
		}

		@Override
		public int getClanlevelmax() { // 公会增加到达的最大等级 by changhao
			_xdb_verify_unsafe_();
			return clanlevelmax;
		}

		@Override
		public String getClanaim() { // 公会宗旨
			_xdb_verify_unsafe_();
			return clanaim;
		}

		@Override
		public com.locojoy.base.Octets getClanaimOctets() { // 公会宗旨
			_xdb_verify_unsafe_();
			return ClanInfo.this.getClanaimOctets();
		}

		@Override
		public long getClanmaster() { // 会长
			_xdb_verify_unsafe_();
			return clanmaster;
		}

		@Override
		public long getClanvicemaster() { // 副会长
			_xdb_verify_unsafe_();
			return clanvicemaster;
		}

		@Override
		public int getClanrank() { // 公会排名
			_xdb_verify_unsafe_();
			return clanrank;
		}

		@Override
		public java.util.Map<Integer, xbean.ClanSkill> getClanskillmap() { // 公会技能信息
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(clanskillmap);
		}

		@Override
		public java.util.Map<Integer, xbean.ClanSkill> getClanskillmapAsData() { // 公会技能信息
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.ClanSkill> clanskillmap;
			ClanInfo _o_ = ClanInfo.this;
			clanskillmap = new java.util.HashMap<Integer, xbean.ClanSkill>();
			for (java.util.Map.Entry<Integer, xbean.ClanSkill> _e_ : _o_.clanskillmap.entrySet())
				clanskillmap.put(_e_.getKey(), new ClanSkill.Data(_e_.getValue()));
			return clanskillmap;
		}

		@Override
		public java.util.Map<Long, xbean.ClanMemberInfo> getMembers() { // 公会成员
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(members);
		}

		@Override
		public java.util.Map<Long, xbean.ClanMemberInfo> getMembersAsData() { // 公会成员
			_xdb_verify_unsafe_();
			java.util.Map<Long, xbean.ClanMemberInfo> members;
			ClanInfo _o_ = ClanInfo.this;
			members = new java.util.HashMap<Long, xbean.ClanMemberInfo>();
			for (java.util.Map.Entry<Long, xbean.ClanMemberInfo> _e_ : _o_.members.entrySet())
				members.put(_e_.getKey(), new ClanMemberInfo.Data(_e_.getValue()));
			return members;
		}

		@Override
		public java.util.Map<Long, Long> getApplicants() { // 申请加入公会人员
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(applicants);
		}

		@Override
		public java.util.Map<Long, Long> getApplicantsAsData() { // 申请加入公会人员
			_xdb_verify_unsafe_();
			java.util.Map<Long, Long> applicants;
			ClanInfo _o_ = ClanInfo.this;
			applicants = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.applicants.entrySet())
				applicants.put(_e_.getKey(), _e_.getValue());
			return applicants;
		}

		@Override
		public long getCreatetime() { // 公会创建时间
			_xdb_verify_unsafe_();
			return createtime;
		}

		@Override
		public int getMoney() { // 公会资金 by changhao
			_xdb_verify_unsafe_();
			return money;
		}

		@Override
		public java.util.Map<Integer, Integer> getHouse() { // 2金库3药房4旅馆  对应等级  by changhao
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(house);
		}

		@Override
		public java.util.Map<Integer, Integer> getHouseAsData() { // 2金库3药房4旅馆  对应等级  by changhao
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> house;
			ClanInfo _o_ = ClanInfo.this;
			house = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.house.entrySet())
				house.put(_e_.getKey(), _e_.getValue());
			return house;
		}

		@Override
		public int getBonus() { // 公会分红 by changhao
			_xdb_verify_unsafe_();
			return bonus;
		}

		@Override
		public int getAutostate() { // 公会自动接收申请人入会的状态：0关闭 1开启
			_xdb_verify_unsafe_();
			return autostate;
		}

		@Override
		public int getTotalscore() { // 公会总评分 by changhao
			_xdb_verify_unsafe_();
			return totalscore;
		}

		@Override
		public int getFiremembernum() { // 每日踢出次数
			_xdb_verify_unsafe_();
			return firemembernum;
		}

		@Override
		public int getClaninstservice() { // 公会选择的副本服务编号
			_xdb_verify_unsafe_();
			return claninstservice;
		}

		@Override
		public java.util.List<xbean.ClanEventRecord> getClaneventrecordlist() { // 公会事件
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(claneventrecordlist);
		}

		public java.util.List<xbean.ClanEventRecord> getClaneventrecordlistAsData() { // 公会事件
			_xdb_verify_unsafe_();
			java.util.List<xbean.ClanEventRecord> claneventrecordlist;
			ClanInfo _o_ = ClanInfo.this;
		claneventrecordlist = new java.util.ArrayList<xbean.ClanEventRecord>();
		for (xbean.ClanEventRecord _v_ : _o_.claneventrecordlist)
			claneventrecordlist.add(new ClanEventRecord.Data(_v_));
			return claneventrecordlist;
		}

		@Override
		public short getRequestlevel() { // 公会自动接收申请人入会的等级
			_xdb_verify_unsafe_();
			return requestlevel;
		}

		@Override
		public xbean.ImpeachMent getImpeachment() { // 弹劾
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(impeachment);
		}

		@Override
		public int getClandkp() { // 本周公会dkp
			_xdb_verify_unsafe_();
			return clandkp;
		}

		@Override
		public int getLastclandkp() { // 上周公会dkp
			_xdb_verify_unsafe_();
			return lastclandkp;
		}

		@Override
		public void setIndex(int _v_) { // 公会序号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setKey(long _v_) { // 公会表里的key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanname(String _v_) { // 公会名字
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClannameOctets(com.locojoy.base.Octets _v_) { // 公会名字
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOldclanname(String _v_) { // 公会曾用名
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOldclannameOctets(com.locojoy.base.Octets _v_) { // 公会曾用名
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCreator(long _v_) { // 公会创始人
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPopularity(int _v_) { // 公会人气度
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanlevel(int _v_) { // 公会级别
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanlevelmax(int _v_) { // 公会增加到达的最大等级 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanaim(String _v_) { // 公会宗旨
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanaimOctets(com.locojoy.base.Octets _v_) { // 公会宗旨
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanmaster(long _v_) { // 会长
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanvicemaster(long _v_) { // 副会长
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClanrank(int _v_) { // 公会排名
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCreatetime(long _v_) { // 公会创建时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMoney(int _v_) { // 公会资金 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBonus(int _v_) { // 公会分红 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAutostate(int _v_) { // 公会自动接收申请人入会的状态：0关闭 1开启
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTotalscore(int _v_) { // 公会总评分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFiremembernum(int _v_) { // 每日踢出次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClaninstservice(int _v_) { // 公会选择的副本服务编号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRequestlevel(short _v_) { // 公会自动接收申请人入会的等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClandkp(int _v_) { // 本周公会dkp
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastclandkp(int _v_) { // 上周公会dkp
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
			return ClanInfo.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return ClanInfo.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return ClanInfo.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return ClanInfo.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return ClanInfo.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return ClanInfo.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return ClanInfo.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return ClanInfo.this.hashCode();
		}

		@Override
		public String toString() {
			return ClanInfo.this.toString();
		}

	}

	public static final class Data implements xbean.ClanInfo {
		private int index; // 公会序号
		private long key; // 公会表里的key
		private String clanname; // 公会名字
		private String oldclanname; // 公会曾用名
		private long creator; // 公会创始人
		private int popularity; // 公会人气度
		private int clanlevel; // 公会级别
		private int clanlevelmax; // 公会增加到达的最大等级 by changhao
		private String clanaim; // 公会宗旨
		private long clanmaster; // 会长
		private long clanvicemaster; // 副会长
		private int clanrank; // 公会排名
		private java.util.HashMap<Integer, xbean.ClanSkill> clanskillmap; // 公会技能信息
		private java.util.HashMap<Long, xbean.ClanMemberInfo> members; // 公会成员
		private java.util.HashMap<Long, Long> applicants; // 申请加入公会人员
		private long createtime; // 公会创建时间
		private int money; // 公会资金 by changhao
		private java.util.HashMap<Integer, Integer> house; // 2金库3药房4旅馆  对应等级  by changhao
		private int bonus; // 公会分红 by changhao
		private int autostate; // 公会自动接收申请人入会的状态：0关闭 1开启
		private int totalscore; // 公会总评分 by changhao
		private int firemembernum; // 每日踢出次数
		private int claninstservice; // 公会选择的副本服务编号
		private java.util.ArrayList<xbean.ClanEventRecord> claneventrecordlist; // 公会事件
		private short requestlevel; // 公会自动接收申请人入会的等级
		private xbean.ImpeachMent impeachment; // 弹劾
		private int clandkp; // 本周公会dkp
		private int lastclandkp; // 上周公会dkp

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			clanname = "";
			oldclanname = "";
			clanlevel = 1;
			clanlevelmax = 1;
			clanaim = "";
			clanvicemaster = -1;
			clanskillmap = new java.util.HashMap<Integer, xbean.ClanSkill>();
			members = new java.util.HashMap<Long, xbean.ClanMemberInfo>();
			applicants = new java.util.HashMap<Long, Long>();
			money = 0;
			house = new java.util.HashMap<Integer, Integer>();
			autostate = 0;
			claneventrecordlist = new java.util.ArrayList<xbean.ClanEventRecord>();
			requestlevel = 1;
			impeachment = new ImpeachMent.Data();
			clandkp = 0;
			lastclandkp = 0;
		}

		Data(xbean.ClanInfo _o1_) {
			if (_o1_ instanceof ClanInfo) assign((ClanInfo)_o1_);
			else if (_o1_ instanceof ClanInfo.Data) assign((ClanInfo.Data)_o1_);
			else if (_o1_ instanceof ClanInfo.Const) assign(((ClanInfo.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(ClanInfo _o_) {
			index = _o_.index;
			key = _o_.key;
			clanname = _o_.clanname;
			oldclanname = _o_.oldclanname;
			creator = _o_.creator;
			popularity = _o_.popularity;
			clanlevel = _o_.clanlevel;
			clanlevelmax = _o_.clanlevelmax;
			clanaim = _o_.clanaim;
			clanmaster = _o_.clanmaster;
			clanvicemaster = _o_.clanvicemaster;
			clanrank = _o_.clanrank;
			clanskillmap = new java.util.HashMap<Integer, xbean.ClanSkill>();
			for (java.util.Map.Entry<Integer, xbean.ClanSkill> _e_ : _o_.clanskillmap.entrySet())
				clanskillmap.put(_e_.getKey(), new ClanSkill.Data(_e_.getValue()));
			members = new java.util.HashMap<Long, xbean.ClanMemberInfo>();
			for (java.util.Map.Entry<Long, xbean.ClanMemberInfo> _e_ : _o_.members.entrySet())
				members.put(_e_.getKey(), new ClanMemberInfo.Data(_e_.getValue()));
			applicants = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.applicants.entrySet())
				applicants.put(_e_.getKey(), _e_.getValue());
			createtime = _o_.createtime;
			money = _o_.money;
			house = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.house.entrySet())
				house.put(_e_.getKey(), _e_.getValue());
			bonus = _o_.bonus;
			autostate = _o_.autostate;
			totalscore = _o_.totalscore;
			firemembernum = _o_.firemembernum;
			claninstservice = _o_.claninstservice;
			claneventrecordlist = new java.util.ArrayList<xbean.ClanEventRecord>();
			for (xbean.ClanEventRecord _v_ : _o_.claneventrecordlist)
				claneventrecordlist.add(new ClanEventRecord.Data(_v_));
			requestlevel = _o_.requestlevel;
			impeachment = new ImpeachMent.Data(_o_.impeachment);
			clandkp = _o_.clandkp;
			lastclandkp = _o_.lastclandkp;
		}

		private void assign(ClanInfo.Data _o_) {
			index = _o_.index;
			key = _o_.key;
			clanname = _o_.clanname;
			oldclanname = _o_.oldclanname;
			creator = _o_.creator;
			popularity = _o_.popularity;
			clanlevel = _o_.clanlevel;
			clanlevelmax = _o_.clanlevelmax;
			clanaim = _o_.clanaim;
			clanmaster = _o_.clanmaster;
			clanvicemaster = _o_.clanvicemaster;
			clanrank = _o_.clanrank;
			clanskillmap = new java.util.HashMap<Integer, xbean.ClanSkill>();
			for (java.util.Map.Entry<Integer, xbean.ClanSkill> _e_ : _o_.clanskillmap.entrySet())
				clanskillmap.put(_e_.getKey(), new ClanSkill.Data(_e_.getValue()));
			members = new java.util.HashMap<Long, xbean.ClanMemberInfo>();
			for (java.util.Map.Entry<Long, xbean.ClanMemberInfo> _e_ : _o_.members.entrySet())
				members.put(_e_.getKey(), new ClanMemberInfo.Data(_e_.getValue()));
			applicants = new java.util.HashMap<Long, Long>();
			for (java.util.Map.Entry<Long, Long> _e_ : _o_.applicants.entrySet())
				applicants.put(_e_.getKey(), _e_.getValue());
			createtime = _o_.createtime;
			money = _o_.money;
			house = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.house.entrySet())
				house.put(_e_.getKey(), _e_.getValue());
			bonus = _o_.bonus;
			autostate = _o_.autostate;
			totalscore = _o_.totalscore;
			firemembernum = _o_.firemembernum;
			claninstservice = _o_.claninstservice;
			claneventrecordlist = new java.util.ArrayList<xbean.ClanEventRecord>();
			for (xbean.ClanEventRecord _v_ : _o_.claneventrecordlist)
				claneventrecordlist.add(new ClanEventRecord.Data(_v_));
			requestlevel = _o_.requestlevel;
			impeachment = new ImpeachMent.Data(_o_.impeachment);
			clandkp = _o_.clandkp;
			lastclandkp = _o_.lastclandkp;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(index);
			_os_.marshal(key);
			_os_.marshal(clanname, mkdb.Const.IO_CHARSET);
			_os_.marshal(oldclanname, mkdb.Const.IO_CHARSET);
			_os_.marshal(creator);
			_os_.marshal(popularity);
			_os_.marshal(clanlevel);
			_os_.marshal(clanlevelmax);
			_os_.marshal(clanaim, mkdb.Const.IO_CHARSET);
			_os_.marshal(clanmaster);
			_os_.marshal(clanvicemaster);
			_os_.marshal(clanrank);
			_os_.compact_uint32(clanskillmap.size());
			for (java.util.Map.Entry<Integer, xbean.ClanSkill> _e_ : clanskillmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(members.size());
			for (java.util.Map.Entry<Long, xbean.ClanMemberInfo> _e_ : members.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.compact_uint32(applicants.size());
			for (java.util.Map.Entry<Long, Long> _e_ : applicants.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(createtime);
			_os_.marshal(money);
			_os_.compact_uint32(house.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : house.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(bonus);
			_os_.marshal(autostate);
			_os_.marshal(totalscore);
			_os_.marshal(firemembernum);
			_os_.marshal(claninstservice);
			_os_.compact_uint32(claneventrecordlist.size());
			for (xbean.ClanEventRecord _v_ : claneventrecordlist) {
				_v_.marshal(_os_);
			}
			_os_.marshal(requestlevel);
			impeachment.marshal(_os_);
			_os_.marshal(clandkp);
			_os_.marshal(lastclandkp);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			index = _os_.unmarshal_int();
			key = _os_.unmarshal_long();
			clanname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			oldclanname = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			creator = _os_.unmarshal_long();
			popularity = _os_.unmarshal_int();
			clanlevel = _os_.unmarshal_int();
			clanlevelmax = _os_.unmarshal_int();
			clanaim = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			clanmaster = _os_.unmarshal_long();
			clanvicemaster = _os_.unmarshal_long();
			clanrank = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					clanskillmap = new java.util.HashMap<Integer, xbean.ClanSkill>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.ClanSkill _v_ = xbean.Pod.newClanSkillData();
					_v_.unmarshal(_os_);
					clanskillmap.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					members = new java.util.HashMap<Long, xbean.ClanMemberInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					xbean.ClanMemberInfo _v_ = xbean.Pod.newClanMemberInfoData();
					_v_.unmarshal(_os_);
					members.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					applicants = new java.util.HashMap<Long, Long>(size * 2);
				}
				for (; size > 0; --size)
				{
					long _k_ = 0;
					_k_ = _os_.unmarshal_long();
					long _v_ = 0;
					_v_ = _os_.unmarshal_long();
					applicants.put(_k_, _v_);
				}
			}
			createtime = _os_.unmarshal_long();
			money = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					house = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					house.put(_k_, _v_);
				}
			}
			bonus = _os_.unmarshal_int();
			autostate = _os_.unmarshal_int();
			totalscore = _os_.unmarshal_int();
			firemembernum = _os_.unmarshal_int();
			claninstservice = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.ClanEventRecord _v_ = xbean.Pod.newClanEventRecordData();
				_v_.unmarshal(_os_);
				claneventrecordlist.add(_v_);
			}
			requestlevel = _os_.unmarshal_short();
			impeachment.unmarshal(_os_);
			clandkp = _os_.unmarshal_int();
			lastclandkp = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.ClanInfo copy() {
			return new Data(this);
		}

		@Override
		public xbean.ClanInfo toData() {
			return new Data(this);
		}

		public xbean.ClanInfo toBean() {
			return new ClanInfo(this, null, null);
		}

		@Override
		public xbean.ClanInfo toDataIf() {
			return this;
		}

		public xbean.ClanInfo toBeanIf() {
			return new ClanInfo(this, null, null);
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
		public int getIndex() { // 公会序号
			return index;
		}

		@Override
		public long getKey() { // 公会表里的key
			return key;
		}

		@Override
		public String getClanname() { // 公会名字
			return clanname;
		}

		@Override
		public com.locojoy.base.Octets getClannameOctets() { // 公会名字
			return com.locojoy.base.Octets.wrap(getClanname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public String getOldclanname() { // 公会曾用名
			return oldclanname;
		}

		@Override
		public com.locojoy.base.Octets getOldclannameOctets() { // 公会曾用名
			return com.locojoy.base.Octets.wrap(getOldclanname(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getCreator() { // 公会创始人
			return creator;
		}

		@Override
		public int getPopularity() { // 公会人气度
			return popularity;
		}

		@Override
		public int getClanlevel() { // 公会级别
			return clanlevel;
		}

		@Override
		public int getClanlevelmax() { // 公会增加到达的最大等级 by changhao
			return clanlevelmax;
		}

		@Override
		public String getClanaim() { // 公会宗旨
			return clanaim;
		}

		@Override
		public com.locojoy.base.Octets getClanaimOctets() { // 公会宗旨
			return com.locojoy.base.Octets.wrap(getClanaim(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getClanmaster() { // 会长
			return clanmaster;
		}

		@Override
		public long getClanvicemaster() { // 副会长
			return clanvicemaster;
		}

		@Override
		public int getClanrank() { // 公会排名
			return clanrank;
		}

		@Override
		public java.util.Map<Integer, xbean.ClanSkill> getClanskillmap() { // 公会技能信息
			return clanskillmap;
		}

		@Override
		public java.util.Map<Integer, xbean.ClanSkill> getClanskillmapAsData() { // 公会技能信息
			return clanskillmap;
		}

		@Override
		public java.util.Map<Long, xbean.ClanMemberInfo> getMembers() { // 公会成员
			return members;
		}

		@Override
		public java.util.Map<Long, xbean.ClanMemberInfo> getMembersAsData() { // 公会成员
			return members;
		}

		@Override
		public java.util.Map<Long, Long> getApplicants() { // 申请加入公会人员
			return applicants;
		}

		@Override
		public java.util.Map<Long, Long> getApplicantsAsData() { // 申请加入公会人员
			return applicants;
		}

		@Override
		public long getCreatetime() { // 公会创建时间
			return createtime;
		}

		@Override
		public int getMoney() { // 公会资金 by changhao
			return money;
		}

		@Override
		public java.util.Map<Integer, Integer> getHouse() { // 2金库3药房4旅馆  对应等级  by changhao
			return house;
		}

		@Override
		public java.util.Map<Integer, Integer> getHouseAsData() { // 2金库3药房4旅馆  对应等级  by changhao
			return house;
		}

		@Override
		public int getBonus() { // 公会分红 by changhao
			return bonus;
		}

		@Override
		public int getAutostate() { // 公会自动接收申请人入会的状态：0关闭 1开启
			return autostate;
		}

		@Override
		public int getTotalscore() { // 公会总评分 by changhao
			return totalscore;
		}

		@Override
		public int getFiremembernum() { // 每日踢出次数
			return firemembernum;
		}

		@Override
		public int getClaninstservice() { // 公会选择的副本服务编号
			return claninstservice;
		}

		@Override
		public java.util.List<xbean.ClanEventRecord> getClaneventrecordlist() { // 公会事件
			return claneventrecordlist;
		}

		@Override
		public java.util.List<xbean.ClanEventRecord> getClaneventrecordlistAsData() { // 公会事件
			return claneventrecordlist;
		}

		@Override
		public short getRequestlevel() { // 公会自动接收申请人入会的等级
			return requestlevel;
		}

		@Override
		public xbean.ImpeachMent getImpeachment() { // 弹劾
			return impeachment;
		}

		@Override
		public int getClandkp() { // 本周公会dkp
			return clandkp;
		}

		@Override
		public int getLastclandkp() { // 上周公会dkp
			return lastclandkp;
		}

		@Override
		public void setIndex(int _v_) { // 公会序号
			index = _v_;
		}

		@Override
		public void setKey(long _v_) { // 公会表里的key
			key = _v_;
		}

		@Override
		public void setClanname(String _v_) { // 公会名字
			if (null == _v_)
				throw new NullPointerException();
			clanname = _v_;
		}

		@Override
		public void setClannameOctets(com.locojoy.base.Octets _v_) { // 公会名字
			this.setClanname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setOldclanname(String _v_) { // 公会曾用名
			if (null == _v_)
				throw new NullPointerException();
			oldclanname = _v_;
		}

		@Override
		public void setOldclannameOctets(com.locojoy.base.Octets _v_) { // 公会曾用名
			this.setOldclanname(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setCreator(long _v_) { // 公会创始人
			creator = _v_;
		}

		@Override
		public void setPopularity(int _v_) { // 公会人气度
			popularity = _v_;
		}

		@Override
		public void setClanlevel(int _v_) { // 公会级别
			clanlevel = _v_;
		}

		@Override
		public void setClanlevelmax(int _v_) { // 公会增加到达的最大等级 by changhao
			clanlevelmax = _v_;
		}

		@Override
		public void setClanaim(String _v_) { // 公会宗旨
			if (null == _v_)
				throw new NullPointerException();
			clanaim = _v_;
		}

		@Override
		public void setClanaimOctets(com.locojoy.base.Octets _v_) { // 公会宗旨
			this.setClanaim(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setClanmaster(long _v_) { // 会长
			clanmaster = _v_;
		}

		@Override
		public void setClanvicemaster(long _v_) { // 副会长
			clanvicemaster = _v_;
		}

		@Override
		public void setClanrank(int _v_) { // 公会排名
			clanrank = _v_;
		}

		@Override
		public void setCreatetime(long _v_) { // 公会创建时间
			createtime = _v_;
		}

		@Override
		public void setMoney(int _v_) { // 公会资金 by changhao
			money = _v_;
		}

		@Override
		public void setBonus(int _v_) { // 公会分红 by changhao
			bonus = _v_;
		}

		@Override
		public void setAutostate(int _v_) { // 公会自动接收申请人入会的状态：0关闭 1开启
			autostate = _v_;
		}

		@Override
		public void setTotalscore(int _v_) { // 公会总评分 by changhao
			totalscore = _v_;
		}

		@Override
		public void setFiremembernum(int _v_) { // 每日踢出次数
			firemembernum = _v_;
		}

		@Override
		public void setClaninstservice(int _v_) { // 公会选择的副本服务编号
			claninstservice = _v_;
		}

		@Override
		public void setRequestlevel(short _v_) { // 公会自动接收申请人入会的等级
			requestlevel = _v_;
		}

		@Override
		public void setClandkp(int _v_) { // 本周公会dkp
			clandkp = _v_;
		}

		@Override
		public void setLastclandkp(int _v_) { // 上周公会dkp
			lastclandkp = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof ClanInfo.Data)) return false;
			ClanInfo.Data _o_ = (ClanInfo.Data) _o1_;
			if (index != _o_.index) return false;
			if (key != _o_.key) return false;
			if (!clanname.equals(_o_.clanname)) return false;
			if (!oldclanname.equals(_o_.oldclanname)) return false;
			if (creator != _o_.creator) return false;
			if (popularity != _o_.popularity) return false;
			if (clanlevel != _o_.clanlevel) return false;
			if (clanlevelmax != _o_.clanlevelmax) return false;
			if (!clanaim.equals(_o_.clanaim)) return false;
			if (clanmaster != _o_.clanmaster) return false;
			if (clanvicemaster != _o_.clanvicemaster) return false;
			if (clanrank != _o_.clanrank) return false;
			if (!clanskillmap.equals(_o_.clanskillmap)) return false;
			if (!members.equals(_o_.members)) return false;
			if (!applicants.equals(_o_.applicants)) return false;
			if (createtime != _o_.createtime) return false;
			if (money != _o_.money) return false;
			if (!house.equals(_o_.house)) return false;
			if (bonus != _o_.bonus) return false;
			if (autostate != _o_.autostate) return false;
			if (totalscore != _o_.totalscore) return false;
			if (firemembernum != _o_.firemembernum) return false;
			if (claninstservice != _o_.claninstservice) return false;
			if (!claneventrecordlist.equals(_o_.claneventrecordlist)) return false;
			if (requestlevel != _o_.requestlevel) return false;
			if (!impeachment.equals(_o_.impeachment)) return false;
			if (clandkp != _o_.clandkp) return false;
			if (lastclandkp != _o_.lastclandkp) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += index;
			_h_ += key;
			_h_ += clanname.hashCode();
			_h_ += oldclanname.hashCode();
			_h_ += creator;
			_h_ += popularity;
			_h_ += clanlevel;
			_h_ += clanlevelmax;
			_h_ += clanaim.hashCode();
			_h_ += clanmaster;
			_h_ += clanvicemaster;
			_h_ += clanrank;
			_h_ += clanskillmap.hashCode();
			_h_ += members.hashCode();
			_h_ += applicants.hashCode();
			_h_ += createtime;
			_h_ += money;
			_h_ += house.hashCode();
			_h_ += bonus;
			_h_ += autostate;
			_h_ += totalscore;
			_h_ += firemembernum;
			_h_ += claninstservice;
			_h_ += claneventrecordlist.hashCode();
			_h_ += requestlevel;
			_h_ += impeachment.hashCode();
			_h_ += clandkp;
			_h_ += lastclandkp;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(index);
			_sb_.append(",");
			_sb_.append(key);
			_sb_.append(",");
			_sb_.append("'").append(clanname).append("'");
			_sb_.append(",");
			_sb_.append("'").append(oldclanname).append("'");
			_sb_.append(",");
			_sb_.append(creator);
			_sb_.append(",");
			_sb_.append(popularity);
			_sb_.append(",");
			_sb_.append(clanlevel);
			_sb_.append(",");
			_sb_.append(clanlevelmax);
			_sb_.append(",");
			_sb_.append("'").append(clanaim).append("'");
			_sb_.append(",");
			_sb_.append(clanmaster);
			_sb_.append(",");
			_sb_.append(clanvicemaster);
			_sb_.append(",");
			_sb_.append(clanrank);
			_sb_.append(",");
			_sb_.append(clanskillmap);
			_sb_.append(",");
			_sb_.append(members);
			_sb_.append(",");
			_sb_.append(applicants);
			_sb_.append(",");
			_sb_.append(createtime);
			_sb_.append(",");
			_sb_.append(money);
			_sb_.append(",");
			_sb_.append(house);
			_sb_.append(",");
			_sb_.append(bonus);
			_sb_.append(",");
			_sb_.append(autostate);
			_sb_.append(",");
			_sb_.append(totalscore);
			_sb_.append(",");
			_sb_.append(firemembernum);
			_sb_.append(",");
			_sb_.append(claninstservice);
			_sb_.append(",");
			_sb_.append(claneventrecordlist);
			_sb_.append(",");
			_sb_.append(requestlevel);
			_sb_.append(",");
			_sb_.append(impeachment);
			_sb_.append(",");
			_sb_.append(clandkp);
			_sb_.append(",");
			_sb_.append(lastclandkp);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
