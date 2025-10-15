
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class Properties extends mkdb.XBean implements xbean.Properties {
	private String rolename; // 角色名
	private java.util.LinkedList<String> usedname; // 曾用名
	private int level; // 等级
	private int shape; // 主角ID(是指游戏中设定的10个角色id)
	private int sex; // 人物性别
	private long exp; // 当前经验
	private int race; // 种族
	private int school; // 职业
	private int color; // 配色方案
	private int title; // 称谓
	private int hp; // 气血
	private int uplimithp; // 当前气血上限
	private int wound; // 伤，当前血上限=最大血上限-伤，伤：[0,MaxHp-1]
	private int mp; // 法力
	private int sp; // 怒气
	private xbean.BasicFightProperties bfp; // 存储基础的点数
	private xbean.RoleAddPointProperties addpointfp; // 人物加点方案
	private java.util.HashMap<Integer, Integer> point; // 潜能。未分配点数
	private int scheme; // 人物加点方案
	private int schemechanges; // 人物加点方案切换次数
	private long schemechangetimes; // 人物加点方案切换最后时间
	private long allexp; // 玩家出生后获得所有经验
	private long sceneid; // 角色所在场景ID
	private int posx; // 坐标x
	private int posy; // 坐标y
	private int posz; // 坐标z
	private long laststaticsceneid; // 最后一次玩家所在的静态场景ID
	private int laststaticposx; // 最后一次玩家所在的静态场景x
	private int laststaticposy; // 最后一次玩家所在的静态场景y
	private int laststaticposz; // 最后一次玩家所在的静态场景z
	private int fightpetkey; // 当前战斗宠物
	private int showpetkey; // 当前展示宠物
	private java.util.HashMap<Integer, Integer> sysconfigmap; // 系统设置
	private java.util.HashMap<Integer, xbean.TitleInfo> titles; // 拥有的称谓列表
	private int defultform; // 默认光环
	private java.util.HashMap<Integer, xbean.FormBean> formationsmap; // key=光环id  value=光环等级
	private long clankey; // 角色对应公会的key
	private long oldclankey; // 角色对应的上次公会key
	private int exitstate; // 角色退出公会方式，0主动退出    1被踢出    2解散          key by hzl
	private long onlinetime; // 角色上次登录时间
	private long offlinetime; // 角色上次离线时间
	private long createtime; // 角色创建时间
	private long bindtel; // 绑定的手机号
	private int logintimesofday; // 当天登录次数
	private int isgetbindtelaward; // 是否得到绑定手机奖励（0不能领取 1 已经领取过了2 可以领取了）
	private long bindteltime; // 绑定手机的时间
	private int isbindtelagain; // 是否需要重新绑定
	private int bindtelagainnum; // 重新绑定次数
	private java.util.LinkedList<Long> unbindteltime; // 解除绑定的时间点
	private String password; // 安全锁密码
	private long forcedelpdtime; // 强制删除密码时间
	private long forcedelendtime; // 强制删除密码结束时间
	private int errortimes; // 密码剩余输入错误次数
	private long lockendtime; // 锁定结束时间点
	private int isopensafelock; // 是否开启道具安全锁
	private long sumonlinetime; // 累积在线时长,ms,这个时长只有在下线的时候才会改变，其他情况还需要通过上次登录时间进行计算
	private int userid; // 帐号角色id
	private boolean addranklist; // 角色是否参与排行榜
	private long deletetime; // 角色被删除的时间,如果角色没有被删除,这个值为0
	private int lastloginip; // 
	private String platformuid; // userid在平台的唯一id
	private int viplevel; // vip等级
	private int energy; // 活力 by changhao
	private int playtimetimes; // 战斗播放时长倍数 10为1倍速 liuxinhua
	private int rolezonghemaxscore; // 人物综合实力历史最高评分 by changhao
	private int skillscore; // 技能评分 by changhao
	private int petscore; // 最强单宠物评分 by changhao
	private int manypetscore; // 多宠物评分 by changhao
	private int levelscore; // 等级评分 by changhao
	private int xiulianscroe; // 修炼评分 by changhao
	private int rolescore; // 人物评分 by changhao
	private int equipscore; // 装备评分 by changhao
	private long historymaxprofessioncontribute; // 历史最大职业贡献度 by changhao
	private int ride; // 当前坐骑 by changhao
	private long bestpetid; // 
	private int rolecolor1; // 染色方案1
	private int rolecolor2; // 染色方案2
	private java.util.LinkedList<xbean.RoleColorType> colorroomlist; // 衣橱
	private int cruise; // 巡游状态，默认0没有巡游，否则为巡游id
	private int cruise2; // 巡游2 by changhao
	private int cruise3; // 巡游3 by changhao
	private java.util.LinkedList<xbean.BattleAI> fighteai; // 主角ai
	private int equipeffect; // 当前装备特效
	private int pilot; // 新手引导项
	private java.util.HashMap<Integer, Integer> tupotips; // 突破任务是否提示过
	private java.util.HashMap<Integer, Integer> tupofinish; // 突破任务是否完成
	private int yingfuxishu; // 盈福转换系数
	private java.util.HashMap<Integer, String> depotnameinfo; // key=仓库id  value=名称
	private long expiretime; // 点卡结束时间(点卡服专用) by changhao
	private int firstprompt; // 还没提示过就是0(点卡服专用) by changhao
	private long lastchargetime; // 最后一次充值时间
	private java.util.HashMap<Integer, Integer> lineconfigmap; // 精英副本自动进入设置

	@Override
	public void _reset_unsafe_() {
		rolename = "";
		usedname.clear();
		level = 0;
		shape = 0;
		sex = 0;
		exp = 0L;
		race = 0;
		school = 0;
		color = 0;
		title = -1;
		hp = 0;
		uplimithp = 0;
		wound = 0;
		mp = 0;
		sp = 0;
		bfp._reset_unsafe_();
		addpointfp._reset_unsafe_();
		point.clear();
		scheme = 1;
		schemechanges = 0;
		schemechangetimes = 0L;
		allexp = 0L;
		sceneid = 0L;
		posx = 0;
		posy = 0;
		posz = 0;
		laststaticsceneid = 0L;
		laststaticposx = 0;
		laststaticposy = 0;
		laststaticposz = 0;
		fightpetkey = -1;
		showpetkey = 0;
		sysconfigmap.clear();
		titles.clear();
		defultform = 0;
		formationsmap.clear();
		clankey = -1;
		oldclankey = -1;
		exitstate = -1;
		onlinetime = -1;
		offlinetime = -1;
		createtime = 0L;
		bindtel = 0L;
		logintimesofday = 0;
		isgetbindtelaward = 0;
		bindteltime = 0;
		isbindtelagain = 0;
		bindtelagainnum = 0;
		unbindteltime.clear();
		password = "";
		forcedelpdtime = 0;
		forcedelendtime = 0;
		errortimes = 5;
		lockendtime = 0L;
		isopensafelock = 0;
		sumonlinetime = 0L;
		userid = 0;
		addranklist = true;
		deletetime = 0L;
		lastloginip = 0;
		platformuid = "";
		viplevel = 0;
		energy = 0;
		playtimetimes = 10;
		rolezonghemaxscore = 0;
		skillscore = 0;
		petscore = 0;
		manypetscore = 0;
		levelscore = 0;
		xiulianscroe = 0;
		rolescore = 0;
		equipscore = 0;
		historymaxprofessioncontribute = 0;
		ride = 0;
		bestpetid = 0L;
		rolecolor1 = 101;
		rolecolor2 = 201;
		colorroomlist.clear();
		cruise = 0;
		cruise2 = 0;
		cruise3 = 0;
		fighteai.clear();
		equipeffect = 0;
		pilot = 2;
		tupotips.clear();
		tupofinish.clear();
		yingfuxishu = 0;
		depotnameinfo.clear();
		expiretime = 0L;
		firstprompt = 0;
		lastchargetime = 0L;
		lineconfigmap.clear();
	}

	Properties(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		rolename = "";
		usedname = new java.util.LinkedList<String>();
		title = -1;
		sp = 0;
		bfp = new BasicFightProperties(0, this, "bfp");
		addpointfp = new RoleAddPointProperties(0, this, "addpointfp");
		point = new java.util.HashMap<Integer, Integer>();
		scheme = 1;
		schemechanges = 0;
		fightpetkey = -1;
		sysconfigmap = new java.util.HashMap<Integer, Integer>();
		titles = new java.util.HashMap<Integer, xbean.TitleInfo>();
		formationsmap = new java.util.HashMap<Integer, xbean.FormBean>();
		clankey = -1;
		oldclankey = -1;
		exitstate = -1;
		onlinetime = -1;
		offlinetime = -1;
		logintimesofday = 0;
		isgetbindtelaward = 0;
		bindteltime = 0;
		isbindtelagain = 0;
		bindtelagainnum = 0;
		unbindteltime = new java.util.LinkedList<Long>();
		password = "";
		forcedelpdtime = 0;
		forcedelendtime = 0;
		errortimes = 5;
		isopensafelock = 0;
		addranklist = true;
		platformuid = "";
		playtimetimes = 10;
		rolezonghemaxscore = 0;
		skillscore = 0;
		petscore = 0;
		manypetscore = 0;
		levelscore = 0;
		xiulianscroe = 0;
		rolescore = 0;
		equipscore = 0;
		historymaxprofessioncontribute = 0;
		ride = 0;
		rolecolor1 = 101;
		rolecolor2 = 201;
		colorroomlist = new java.util.LinkedList<xbean.RoleColorType>();
		cruise = 0;
		cruise2 = 0;
		cruise3 = 0;
		fighteai = new java.util.LinkedList<xbean.BattleAI>();
		equipeffect = 0;
		pilot = 2;
		tupotips = new java.util.HashMap<Integer, Integer>();
		tupofinish = new java.util.HashMap<Integer, Integer>();
		yingfuxishu = 0;
		depotnameinfo = new java.util.HashMap<Integer, String>();
		lineconfigmap = new java.util.HashMap<Integer, Integer>();
	}

	public Properties() {
		this(0, null, null);
	}

	public Properties(Properties _o_) {
		this(_o_, null, null);
	}

	Properties(xbean.Properties _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof Properties) assign((Properties)_o1_);
		else if (_o1_ instanceof Properties.Data) assign((Properties.Data)_o1_);
		else if (_o1_ instanceof Properties.Const) assign(((Properties.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(Properties _o_) {
		_o_._xdb_verify_unsafe_();
		rolename = _o_.rolename;
		usedname = new java.util.LinkedList<String>();
		usedname.addAll(_o_.usedname);
		level = _o_.level;
		shape = _o_.shape;
		sex = _o_.sex;
		exp = _o_.exp;
		race = _o_.race;
		school = _o_.school;
		color = _o_.color;
		title = _o_.title;
		hp = _o_.hp;
		uplimithp = _o_.uplimithp;
		wound = _o_.wound;
		mp = _o_.mp;
		sp = _o_.sp;
		bfp = new BasicFightProperties(_o_.bfp, this, "bfp");
		addpointfp = new RoleAddPointProperties(_o_.addpointfp, this, "addpointfp");
		point = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.point.entrySet())
			point.put(_e_.getKey(), _e_.getValue());
		scheme = _o_.scheme;
		schemechanges = _o_.schemechanges;
		schemechangetimes = _o_.schemechangetimes;
		allexp = _o_.allexp;
		sceneid = _o_.sceneid;
		posx = _o_.posx;
		posy = _o_.posy;
		posz = _o_.posz;
		laststaticsceneid = _o_.laststaticsceneid;
		laststaticposx = _o_.laststaticposx;
		laststaticposy = _o_.laststaticposy;
		laststaticposz = _o_.laststaticposz;
		fightpetkey = _o_.fightpetkey;
		showpetkey = _o_.showpetkey;
		sysconfigmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.sysconfigmap.entrySet())
			sysconfigmap.put(_e_.getKey(), _e_.getValue());
		titles = new java.util.HashMap<Integer, xbean.TitleInfo>();
		for (java.util.Map.Entry<Integer, xbean.TitleInfo> _e_ : _o_.titles.entrySet())
			titles.put(_e_.getKey(), new TitleInfo(_e_.getValue(), this, "titles"));
		defultform = _o_.defultform;
		formationsmap = new java.util.HashMap<Integer, xbean.FormBean>();
		for (java.util.Map.Entry<Integer, xbean.FormBean> _e_ : _o_.formationsmap.entrySet())
			formationsmap.put(_e_.getKey(), new FormBean(_e_.getValue(), this, "formationsmap"));
		clankey = _o_.clankey;
		oldclankey = _o_.oldclankey;
		exitstate = _o_.exitstate;
		onlinetime = _o_.onlinetime;
		offlinetime = _o_.offlinetime;
		createtime = _o_.createtime;
		bindtel = _o_.bindtel;
		logintimesofday = _o_.logintimesofday;
		isgetbindtelaward = _o_.isgetbindtelaward;
		bindteltime = _o_.bindteltime;
		isbindtelagain = _o_.isbindtelagain;
		bindtelagainnum = _o_.bindtelagainnum;
		unbindteltime = new java.util.LinkedList<Long>();
		unbindteltime.addAll(_o_.unbindteltime);
		password = _o_.password;
		forcedelpdtime = _o_.forcedelpdtime;
		forcedelendtime = _o_.forcedelendtime;
		errortimes = _o_.errortimes;
		lockendtime = _o_.lockendtime;
		isopensafelock = _o_.isopensafelock;
		sumonlinetime = _o_.sumonlinetime;
		userid = _o_.userid;
		addranklist = _o_.addranklist;
		deletetime = _o_.deletetime;
		lastloginip = _o_.lastloginip;
		platformuid = _o_.platformuid;
		viplevel = _o_.viplevel;
		energy = _o_.energy;
		playtimetimes = _o_.playtimetimes;
		rolezonghemaxscore = _o_.rolezonghemaxscore;
		skillscore = _o_.skillscore;
		petscore = _o_.petscore;
		manypetscore = _o_.manypetscore;
		levelscore = _o_.levelscore;
		xiulianscroe = _o_.xiulianscroe;
		rolescore = _o_.rolescore;
		equipscore = _o_.equipscore;
		historymaxprofessioncontribute = _o_.historymaxprofessioncontribute;
		ride = _o_.ride;
		bestpetid = _o_.bestpetid;
		rolecolor1 = _o_.rolecolor1;
		rolecolor2 = _o_.rolecolor2;
		colorroomlist = new java.util.LinkedList<xbean.RoleColorType>();
		for (xbean.RoleColorType _v_ : _o_.colorroomlist)
			colorroomlist.add(new RoleColorType(_v_, this, "colorroomlist"));
		cruise = _o_.cruise;
		cruise2 = _o_.cruise2;
		cruise3 = _o_.cruise3;
		fighteai = new java.util.LinkedList<xbean.BattleAI>();
		for (xbean.BattleAI _v_ : _o_.fighteai)
			fighteai.add(new BattleAI(_v_, this, "fighteai"));
		equipeffect = _o_.equipeffect;
		pilot = _o_.pilot;
		tupotips = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tupotips.entrySet())
			tupotips.put(_e_.getKey(), _e_.getValue());
		tupofinish = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tupofinish.entrySet())
			tupofinish.put(_e_.getKey(), _e_.getValue());
		yingfuxishu = _o_.yingfuxishu;
		depotnameinfo = new java.util.HashMap<Integer, String>();
		for (java.util.Map.Entry<Integer, String> _e_ : _o_.depotnameinfo.entrySet())
			depotnameinfo.put(_e_.getKey(), _e_.getValue());
		expiretime = _o_.expiretime;
		firstprompt = _o_.firstprompt;
		lastchargetime = _o_.lastchargetime;
		lineconfigmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.lineconfigmap.entrySet())
			lineconfigmap.put(_e_.getKey(), _e_.getValue());
	}

	private void assign(Properties.Data _o_) {
		rolename = _o_.rolename;
		usedname = new java.util.LinkedList<String>();
		usedname.addAll(_o_.usedname);
		level = _o_.level;
		shape = _o_.shape;
		sex = _o_.sex;
		exp = _o_.exp;
		race = _o_.race;
		school = _o_.school;
		color = _o_.color;
		title = _o_.title;
		hp = _o_.hp;
		uplimithp = _o_.uplimithp;
		wound = _o_.wound;
		mp = _o_.mp;
		sp = _o_.sp;
		bfp = new BasicFightProperties(_o_.bfp, this, "bfp");
		addpointfp = new RoleAddPointProperties(_o_.addpointfp, this, "addpointfp");
		point = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.point.entrySet())
			point.put(_e_.getKey(), _e_.getValue());
		scheme = _o_.scheme;
		schemechanges = _o_.schemechanges;
		schemechangetimes = _o_.schemechangetimes;
		allexp = _o_.allexp;
		sceneid = _o_.sceneid;
		posx = _o_.posx;
		posy = _o_.posy;
		posz = _o_.posz;
		laststaticsceneid = _o_.laststaticsceneid;
		laststaticposx = _o_.laststaticposx;
		laststaticposy = _o_.laststaticposy;
		laststaticposz = _o_.laststaticposz;
		fightpetkey = _o_.fightpetkey;
		showpetkey = _o_.showpetkey;
		sysconfigmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.sysconfigmap.entrySet())
			sysconfigmap.put(_e_.getKey(), _e_.getValue());
		titles = new java.util.HashMap<Integer, xbean.TitleInfo>();
		for (java.util.Map.Entry<Integer, xbean.TitleInfo> _e_ : _o_.titles.entrySet())
			titles.put(_e_.getKey(), new TitleInfo(_e_.getValue(), this, "titles"));
		defultform = _o_.defultform;
		formationsmap = new java.util.HashMap<Integer, xbean.FormBean>();
		for (java.util.Map.Entry<Integer, xbean.FormBean> _e_ : _o_.formationsmap.entrySet())
			formationsmap.put(_e_.getKey(), new FormBean(_e_.getValue(), this, "formationsmap"));
		clankey = _o_.clankey;
		oldclankey = _o_.oldclankey;
		exitstate = _o_.exitstate;
		onlinetime = _o_.onlinetime;
		offlinetime = _o_.offlinetime;
		createtime = _o_.createtime;
		bindtel = _o_.bindtel;
		logintimesofday = _o_.logintimesofday;
		isgetbindtelaward = _o_.isgetbindtelaward;
		bindteltime = _o_.bindteltime;
		isbindtelagain = _o_.isbindtelagain;
		bindtelagainnum = _o_.bindtelagainnum;
		unbindteltime = new java.util.LinkedList<Long>();
		unbindteltime.addAll(_o_.unbindteltime);
		password = _o_.password;
		forcedelpdtime = _o_.forcedelpdtime;
		forcedelendtime = _o_.forcedelendtime;
		errortimes = _o_.errortimes;
		lockendtime = _o_.lockendtime;
		isopensafelock = _o_.isopensafelock;
		sumonlinetime = _o_.sumonlinetime;
		userid = _o_.userid;
		addranklist = _o_.addranklist;
		deletetime = _o_.deletetime;
		lastloginip = _o_.lastloginip;
		platformuid = _o_.platformuid;
		viplevel = _o_.viplevel;
		energy = _o_.energy;
		playtimetimes = _o_.playtimetimes;
		rolezonghemaxscore = _o_.rolezonghemaxscore;
		skillscore = _o_.skillscore;
		petscore = _o_.petscore;
		manypetscore = _o_.manypetscore;
		levelscore = _o_.levelscore;
		xiulianscroe = _o_.xiulianscroe;
		rolescore = _o_.rolescore;
		equipscore = _o_.equipscore;
		historymaxprofessioncontribute = _o_.historymaxprofessioncontribute;
		ride = _o_.ride;
		bestpetid = _o_.bestpetid;
		rolecolor1 = _o_.rolecolor1;
		rolecolor2 = _o_.rolecolor2;
		colorroomlist = new java.util.LinkedList<xbean.RoleColorType>();
		for (xbean.RoleColorType _v_ : _o_.colorroomlist)
			colorroomlist.add(new RoleColorType(_v_, this, "colorroomlist"));
		cruise = _o_.cruise;
		cruise2 = _o_.cruise2;
		cruise3 = _o_.cruise3;
		fighteai = new java.util.LinkedList<xbean.BattleAI>();
		for (xbean.BattleAI _v_ : _o_.fighteai)
			fighteai.add(new BattleAI(_v_, this, "fighteai"));
		equipeffect = _o_.equipeffect;
		pilot = _o_.pilot;
		tupotips = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tupotips.entrySet())
			tupotips.put(_e_.getKey(), _e_.getValue());
		tupofinish = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tupofinish.entrySet())
			tupofinish.put(_e_.getKey(), _e_.getValue());
		yingfuxishu = _o_.yingfuxishu;
		depotnameinfo = new java.util.HashMap<Integer, String>();
		for (java.util.Map.Entry<Integer, String> _e_ : _o_.depotnameinfo.entrySet())
			depotnameinfo.put(_e_.getKey(), _e_.getValue());
		expiretime = _o_.expiretime;
		firstprompt = _o_.firstprompt;
		lastchargetime = _o_.lastchargetime;
		lineconfigmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.lineconfigmap.entrySet())
			lineconfigmap.put(_e_.getKey(), _e_.getValue());
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(rolename, mkdb.Const.IO_CHARSET);
		_os_.compact_uint32(usedname.size());
		for (String _v_ : usedname) {
			_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
		}
		_os_.marshal(level);
		_os_.marshal(shape);
		_os_.marshal(sex);
		_os_.marshal(exp);
		_os_.marshal(race);
		_os_.marshal(school);
		_os_.marshal(color);
		_os_.marshal(title);
		_os_.marshal(hp);
		_os_.marshal(uplimithp);
		_os_.marshal(wound);
		_os_.marshal(mp);
		_os_.marshal(sp);
		bfp.marshal(_os_);
		addpointfp.marshal(_os_);
		_os_.compact_uint32(point.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : point.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(scheme);
		_os_.marshal(schemechanges);
		_os_.marshal(schemechangetimes);
		_os_.marshal(allexp);
		_os_.marshal(sceneid);
		_os_.marshal(posx);
		_os_.marshal(posy);
		_os_.marshal(posz);
		_os_.marshal(laststaticsceneid);
		_os_.marshal(laststaticposx);
		_os_.marshal(laststaticposy);
		_os_.marshal(laststaticposz);
		_os_.marshal(fightpetkey);
		_os_.marshal(showpetkey);
		_os_.compact_uint32(sysconfigmap.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : sysconfigmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(titles.size());
		for (java.util.Map.Entry<Integer, xbean.TitleInfo> _e_ : titles.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(defultform);
		_os_.compact_uint32(formationsmap.size());
		for (java.util.Map.Entry<Integer, xbean.FormBean> _e_ : formationsmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_e_.getValue().marshal(_os_);
		}
		_os_.marshal(clankey);
		_os_.marshal(oldclankey);
		_os_.marshal(exitstate);
		_os_.marshal(onlinetime);
		_os_.marshal(offlinetime);
		_os_.marshal(createtime);
		_os_.marshal(bindtel);
		_os_.marshal(logintimesofday);
		_os_.marshal(isgetbindtelaward);
		_os_.marshal(bindteltime);
		_os_.marshal(isbindtelagain);
		_os_.marshal(bindtelagainnum);
		_os_.compact_uint32(unbindteltime.size());
		for (Long _v_ : unbindteltime) {
			_os_.marshal(_v_);
		}
		_os_.marshal(password, mkdb.Const.IO_CHARSET);
		_os_.marshal(forcedelpdtime);
		_os_.marshal(forcedelendtime);
		_os_.marshal(errortimes);
		_os_.marshal(lockendtime);
		_os_.marshal(isopensafelock);
		_os_.marshal(sumonlinetime);
		_os_.marshal(userid);
		_os_.marshal(addranklist);
		_os_.marshal(deletetime);
		_os_.marshal(lastloginip);
		_os_.marshal(platformuid, mkdb.Const.IO_CHARSET);
		_os_.marshal(viplevel);
		_os_.marshal(energy);
		_os_.marshal(playtimetimes);
		_os_.marshal(rolezonghemaxscore);
		_os_.marshal(skillscore);
		_os_.marshal(petscore);
		_os_.marshal(manypetscore);
		_os_.marshal(levelscore);
		_os_.marshal(xiulianscroe);
		_os_.marshal(rolescore);
		_os_.marshal(equipscore);
		_os_.marshal(historymaxprofessioncontribute);
		_os_.marshal(ride);
		_os_.marshal(bestpetid);
		_os_.marshal(rolecolor1);
		_os_.marshal(rolecolor2);
		_os_.compact_uint32(colorroomlist.size());
		for (xbean.RoleColorType _v_ : colorroomlist) {
			_v_.marshal(_os_);
		}
		_os_.marshal(cruise);
		_os_.marshal(cruise2);
		_os_.marshal(cruise3);
		_os_.compact_uint32(fighteai.size());
		for (xbean.BattleAI _v_ : fighteai) {
			_v_.marshal(_os_);
		}
		_os_.marshal(equipeffect);
		_os_.marshal(pilot);
		_os_.compact_uint32(tupotips.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : tupotips.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.compact_uint32(tupofinish.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : tupofinish.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(yingfuxishu);
		_os_.compact_uint32(depotnameinfo.size());
		for (java.util.Map.Entry<Integer, String> _e_ : depotnameinfo.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue(), mkdb.Const.IO_CHARSET);
		}
		_os_.marshal(expiretime);
		_os_.marshal(firstprompt);
		_os_.marshal(lastchargetime);
		_os_.compact_uint32(lineconfigmap.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : lineconfigmap.entrySet())
		{
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		rolename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			String _v_ = "";
			_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			usedname.add(_v_);
		}
		level = _os_.unmarshal_int();
		shape = _os_.unmarshal_int();
		sex = _os_.unmarshal_int();
		exp = _os_.unmarshal_long();
		race = _os_.unmarshal_int();
		school = _os_.unmarshal_int();
		color = _os_.unmarshal_int();
		title = _os_.unmarshal_int();
		hp = _os_.unmarshal_int();
		uplimithp = _os_.unmarshal_int();
		wound = _os_.unmarshal_int();
		mp = _os_.unmarshal_int();
		sp = _os_.unmarshal_int();
		bfp.unmarshal(_os_);
		addpointfp.unmarshal(_os_);
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				point = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				point.put(_k_, _v_);
			}
		}
		scheme = _os_.unmarshal_int();
		schemechanges = _os_.unmarshal_int();
		schemechangetimes = _os_.unmarshal_long();
		allexp = _os_.unmarshal_long();
		sceneid = _os_.unmarshal_long();
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		posz = _os_.unmarshal_int();
		laststaticsceneid = _os_.unmarshal_long();
		laststaticposx = _os_.unmarshal_int();
		laststaticposy = _os_.unmarshal_int();
		laststaticposz = _os_.unmarshal_int();
		fightpetkey = _os_.unmarshal_int();
		showpetkey = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				sysconfigmap = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				sysconfigmap.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				titles = new java.util.HashMap<Integer, xbean.TitleInfo>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.TitleInfo _v_ = new TitleInfo(0, this, "titles");
				_v_.unmarshal(_os_);
				titles.put(_k_, _v_);
			}
		}
		defultform = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				formationsmap = new java.util.HashMap<Integer, xbean.FormBean>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				xbean.FormBean _v_ = new FormBean(0, this, "formationsmap");
				_v_.unmarshal(_os_);
				formationsmap.put(_k_, _v_);
			}
		}
		clankey = _os_.unmarshal_long();
		oldclankey = _os_.unmarshal_long();
		exitstate = _os_.unmarshal_int();
		onlinetime = _os_.unmarshal_long();
		offlinetime = _os_.unmarshal_long();
		createtime = _os_.unmarshal_long();
		bindtel = _os_.unmarshal_long();
		logintimesofday = _os_.unmarshal_int();
		isgetbindtelaward = _os_.unmarshal_int();
		bindteltime = _os_.unmarshal_long();
		isbindtelagain = _os_.unmarshal_int();
		bindtelagainnum = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			long _v_ = 0;
			_v_ = _os_.unmarshal_long();
			unbindteltime.add(_v_);
		}
		password = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		forcedelpdtime = _os_.unmarshal_long();
		forcedelendtime = _os_.unmarshal_long();
		errortimes = _os_.unmarshal_int();
		lockendtime = _os_.unmarshal_long();
		isopensafelock = _os_.unmarshal_int();
		sumonlinetime = _os_.unmarshal_long();
		userid = _os_.unmarshal_int();
		addranklist = _os_.unmarshal_boolean();
		deletetime = _os_.unmarshal_long();
		lastloginip = _os_.unmarshal_int();
		platformuid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		viplevel = _os_.unmarshal_int();
		energy = _os_.unmarshal_int();
		playtimetimes = _os_.unmarshal_int();
		rolezonghemaxscore = _os_.unmarshal_int();
		skillscore = _os_.unmarshal_int();
		petscore = _os_.unmarshal_int();
		manypetscore = _os_.unmarshal_int();
		levelscore = _os_.unmarshal_int();
		xiulianscroe = _os_.unmarshal_int();
		rolescore = _os_.unmarshal_int();
		equipscore = _os_.unmarshal_int();
		historymaxprofessioncontribute = _os_.unmarshal_long();
		ride = _os_.unmarshal_int();
		bestpetid = _os_.unmarshal_long();
		rolecolor1 = _os_.unmarshal_int();
		rolecolor2 = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.RoleColorType _v_ = new RoleColorType(0, this, "colorroomlist");
			_v_.unmarshal(_os_);
			colorroomlist.add(_v_);
		}
		cruise = _os_.unmarshal_int();
		cruise2 = _os_.unmarshal_int();
		cruise3 = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			xbean.BattleAI _v_ = new BattleAI(0, this, "fighteai");
			_v_.unmarshal(_os_);
			fighteai.add(_v_);
		}
		equipeffect = _os_.unmarshal_int();
		pilot = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				tupotips = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				tupotips.put(_k_, _v_);
			}
		}
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				tupofinish = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				tupofinish.put(_k_, _v_);
			}
		}
		yingfuxishu = _os_.unmarshal_int();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				depotnameinfo = new java.util.HashMap<Integer, String>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				String _v_ = "";
				_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				depotnameinfo.put(_k_, _v_);
			}
		}
		expiretime = _os_.unmarshal_long();
		firstprompt = _os_.unmarshal_int();
		lastchargetime = _os_.unmarshal_long();
		{
			int size = _os_.uncompact_uint32();
			if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
				lineconfigmap = new java.util.HashMap<Integer, Integer>(size * 2);
			}
			for (; size > 0; --size)
			{
				int _k_ = 0;
				_k_ = _os_.unmarshal_int();
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				lineconfigmap.put(_k_, _v_);
			}
		}
		return _os_;
	}

	@Override
	public xbean.Properties copy() {
		_xdb_verify_unsafe_();
		return new Properties(this);
	}

	@Override
	public xbean.Properties toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Properties toBean() {
		_xdb_verify_unsafe_();
		return new Properties(this); // same as copy()
	}

	@Override
	public xbean.Properties toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.Properties toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public String getRolename() { // 角色名
		_xdb_verify_unsafe_();
		return rolename;
	}

	@Override
	public com.locojoy.base.Octets getRolenameOctets() { // 角色名
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getRolename(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public java.util.List<String> getUsedname() { // 曾用名
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "usedname"), usedname);
	}

	public java.util.List<String> getUsednameAsData() { // 曾用名
		_xdb_verify_unsafe_();
		java.util.List<String> usedname;
		Properties _o_ = this;
		usedname = new java.util.LinkedList<String>();
		usedname.addAll(_o_.usedname);
		return usedname;
	}

	@Override
	public int getLevel() { // 等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getShape() { // 主角ID(是指游戏中设定的10个角色id)
		_xdb_verify_unsafe_();
		return shape;
	}

	@Override
	public int getSex() { // 人物性别
		_xdb_verify_unsafe_();
		return sex;
	}

	@Override
	public long getExp() { // 当前经验
		_xdb_verify_unsafe_();
		return exp;
	}

	@Override
	public int getRace() { // 种族
		_xdb_verify_unsafe_();
		return race;
	}

	@Override
	public int getSchool() { // 职业
		_xdb_verify_unsafe_();
		return school;
	}

	@Override
	public int getColor() { // 配色方案
		_xdb_verify_unsafe_();
		return color;
	}

	@Override
	public int getTitle() { // 称谓
		_xdb_verify_unsafe_();
		return title;
	}

	@Override
	public int getHp() { // 气血
		_xdb_verify_unsafe_();
		return hp;
	}

	@Override
	public int getUplimithp() { // 当前气血上限
		_xdb_verify_unsafe_();
		return uplimithp;
	}

	@Override
	public int getWound() { // 伤，当前血上限=最大血上限-伤，伤：[0,MaxHp-1]
		_xdb_verify_unsafe_();
		return wound;
	}

	@Override
	public int getMp() { // 法力
		_xdb_verify_unsafe_();
		return mp;
	}

	@Override
	public int getSp() { // 怒气
		_xdb_verify_unsafe_();
		return sp;
	}

	@Override
	public xbean.BasicFightProperties getBfp() { // 存储基础的点数
		_xdb_verify_unsafe_();
		return bfp;
	}

	@Override
	public xbean.RoleAddPointProperties getAddpointfp() { // 人物加点方案
		_xdb_verify_unsafe_();
		return addpointfp;
	}

	@Override
	public java.util.Map<Integer, Integer> getPoint() { // 潜能。未分配点数
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "point"), point);
	}

	@Override
	public java.util.Map<Integer, Integer> getPointAsData() { // 潜能。未分配点数
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> point;
		Properties _o_ = this;
		point = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.point.entrySet())
			point.put(_e_.getKey(), _e_.getValue());
		return point;
	}

	@Override
	public int getScheme() { // 人物加点方案
		_xdb_verify_unsafe_();
		return scheme;
	}

	@Override
	public int getSchemechanges() { // 人物加点方案切换次数
		_xdb_verify_unsafe_();
		return schemechanges;
	}

	@Override
	public long getSchemechangetimes() { // 人物加点方案切换最后时间
		_xdb_verify_unsafe_();
		return schemechangetimes;
	}

	@Override
	public long getAllexp() { // 玩家出生后获得所有经验
		_xdb_verify_unsafe_();
		return allexp;
	}

	@Override
	public long getSceneid() { // 角色所在场景ID
		_xdb_verify_unsafe_();
		return sceneid;
	}

	@Override
	public int getPosx() { // 坐标x
		_xdb_verify_unsafe_();
		return posx;
	}

	@Override
	public int getPosy() { // 坐标y
		_xdb_verify_unsafe_();
		return posy;
	}

	@Override
	public int getPosz() { // 坐标z
		_xdb_verify_unsafe_();
		return posz;
	}

	@Override
	public long getLaststaticsceneid() { // 最后一次玩家所在的静态场景ID
		_xdb_verify_unsafe_();
		return laststaticsceneid;
	}

	@Override
	public int getLaststaticposx() { // 最后一次玩家所在的静态场景x
		_xdb_verify_unsafe_();
		return laststaticposx;
	}

	@Override
	public int getLaststaticposy() { // 最后一次玩家所在的静态场景y
		_xdb_verify_unsafe_();
		return laststaticposy;
	}

	@Override
	public int getLaststaticposz() { // 最后一次玩家所在的静态场景z
		_xdb_verify_unsafe_();
		return laststaticposz;
	}

	@Override
	public int getFightpetkey() { // 当前战斗宠物
		_xdb_verify_unsafe_();
		return fightpetkey;
	}

	@Override
	public int getShowpetkey() { // 当前展示宠物
		_xdb_verify_unsafe_();
		return showpetkey;
	}

	@Override
	public java.util.Map<Integer, Integer> getSysconfigmap() { // 系统设置
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "sysconfigmap"), sysconfigmap);
	}

	@Override
	public java.util.Map<Integer, Integer> getSysconfigmapAsData() { // 系统设置
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> sysconfigmap;
		Properties _o_ = this;
		sysconfigmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.sysconfigmap.entrySet())
			sysconfigmap.put(_e_.getKey(), _e_.getValue());
		return sysconfigmap;
	}

	@Override
	public java.util.Map<Integer, xbean.TitleInfo> getTitles() { // 拥有的称谓列表
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "titles"), titles);
	}

	@Override
	public java.util.Map<Integer, xbean.TitleInfo> getTitlesAsData() { // 拥有的称谓列表
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.TitleInfo> titles;
		Properties _o_ = this;
		titles = new java.util.HashMap<Integer, xbean.TitleInfo>();
		for (java.util.Map.Entry<Integer, xbean.TitleInfo> _e_ : _o_.titles.entrySet())
			titles.put(_e_.getKey(), new TitleInfo.Data(_e_.getValue()));
		return titles;
	}

	@Override
	public int getDefultform() { // 默认光环
		_xdb_verify_unsafe_();
		return defultform;
	}

	@Override
	public java.util.Map<Integer, xbean.FormBean> getFormationsmap() { // key=光环id  value=光环等级
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "formationsmap"), formationsmap);
	}

	@Override
	public java.util.Map<Integer, xbean.FormBean> getFormationsmapAsData() { // key=光环id  value=光环等级
		_xdb_verify_unsafe_();
		java.util.Map<Integer, xbean.FormBean> formationsmap;
		Properties _o_ = this;
		formationsmap = new java.util.HashMap<Integer, xbean.FormBean>();
		for (java.util.Map.Entry<Integer, xbean.FormBean> _e_ : _o_.formationsmap.entrySet())
			formationsmap.put(_e_.getKey(), new FormBean.Data(_e_.getValue()));
		return formationsmap;
	}

	@Override
	public long getClankey() { // 角色对应公会的key
		_xdb_verify_unsafe_();
		return clankey;
	}

	@Override
	public long getOldclankey() { // 角色对应的上次公会key
		_xdb_verify_unsafe_();
		return oldclankey;
	}

	@Override
	public int getExitstate() { // 角色退出公会方式，0主动退出    1被踢出    2解散          key by hzl
		_xdb_verify_unsafe_();
		return exitstate;
	}

	@Override
	public long getOnlinetime() { // 角色上次登录时间
		_xdb_verify_unsafe_();
		return onlinetime;
	}

	@Override
	public long getOfflinetime() { // 角色上次离线时间
		_xdb_verify_unsafe_();
		return offlinetime;
	}

	@Override
	public long getCreatetime() { // 角色创建时间
		_xdb_verify_unsafe_();
		return createtime;
	}

	@Override
	public long getBindtel() { // 绑定的手机号
		_xdb_verify_unsafe_();
		return bindtel;
	}

	@Override
	public int getLogintimesofday() { // 当天登录次数
		_xdb_verify_unsafe_();
		return logintimesofday;
	}

	@Override
	public int getIsgetbindtelaward() { // 是否得到绑定手机奖励（0不能领取 1 已经领取过了2 可以领取了）
		_xdb_verify_unsafe_();
		return isgetbindtelaward;
	}

	@Override
	public long getBindteltime() { // 绑定手机的时间
		_xdb_verify_unsafe_();
		return bindteltime;
	}

	@Override
	public int getIsbindtelagain() { // 是否需要重新绑定
		_xdb_verify_unsafe_();
		return isbindtelagain;
	}

	@Override
	public int getBindtelagainnum() { // 重新绑定次数
		_xdb_verify_unsafe_();
		return bindtelagainnum;
	}

	@Override
	public java.util.List<Long> getUnbindteltime() { // 解除绑定的时间点
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "unbindteltime"), unbindteltime);
	}

	public java.util.List<Long> getUnbindteltimeAsData() { // 解除绑定的时间点
		_xdb_verify_unsafe_();
		java.util.List<Long> unbindteltime;
		Properties _o_ = this;
		unbindteltime = new java.util.LinkedList<Long>();
		unbindteltime.addAll(_o_.unbindteltime);
		return unbindteltime;
	}

	@Override
	public String getPassword() { // 安全锁密码
		_xdb_verify_unsafe_();
		return password;
	}

	@Override
	public com.locojoy.base.Octets getPasswordOctets() { // 安全锁密码
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPassword(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getForcedelpdtime() { // 强制删除密码时间
		_xdb_verify_unsafe_();
		return forcedelpdtime;
	}

	@Override
	public long getForcedelendtime() { // 强制删除密码结束时间
		_xdb_verify_unsafe_();
		return forcedelendtime;
	}

	@Override
	public int getErrortimes() { // 密码剩余输入错误次数
		_xdb_verify_unsafe_();
		return errortimes;
	}

	@Override
	public long getLockendtime() { // 锁定结束时间点
		_xdb_verify_unsafe_();
		return lockendtime;
	}

	@Override
	public int getIsopensafelock() { // 是否开启道具安全锁
		_xdb_verify_unsafe_();
		return isopensafelock;
	}

	@Override
	public long getSumonlinetime() { // 累积在线时长,ms,这个时长只有在下线的时候才会改变，其他情况还需要通过上次登录时间进行计算
		_xdb_verify_unsafe_();
		return sumonlinetime;
	}

	@Override
	public int getUserid() { // 帐号角色id
		_xdb_verify_unsafe_();
		return userid;
	}

	@Override
	public boolean getAddranklist() { // 角色是否参与排行榜
		_xdb_verify_unsafe_();
		return addranklist;
	}

	@Override
	public long getDeletetime() { // 角色被删除的时间,如果角色没有被删除,这个值为0
		_xdb_verify_unsafe_();
		return deletetime;
	}

	@Override
	public int getLastloginip() { // 
		_xdb_verify_unsafe_();
		return lastloginip;
	}

	@Override
	public String getPlatformuid() { // userid在平台的唯一id
		_xdb_verify_unsafe_();
		return platformuid;
	}

	@Override
	public com.locojoy.base.Octets getPlatformuidOctets() { // userid在平台的唯一id
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getPlatformuid(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public int getViplevel() { // vip等级
		_xdb_verify_unsafe_();
		return viplevel;
	}

	@Override
	public int getEnergy() { // 活力 by changhao
		_xdb_verify_unsafe_();
		return energy;
	}

	@Override
	public int getPlaytimetimes() { // 战斗播放时长倍数 10为1倍速 liuxinhua
		_xdb_verify_unsafe_();
		return playtimetimes;
	}

	@Override
	public int getRolezonghemaxscore() { // 人物综合实力历史最高评分 by changhao
		_xdb_verify_unsafe_();
		return rolezonghemaxscore;
	}

	@Override
	public int getSkillscore() { // 技能评分 by changhao
		_xdb_verify_unsafe_();
		return skillscore;
	}

	@Override
	public int getPetscore() { // 最强单宠物评分 by changhao
		_xdb_verify_unsafe_();
		return petscore;
	}

	@Override
	public int getManypetscore() { // 多宠物评分 by changhao
		_xdb_verify_unsafe_();
		return manypetscore;
	}

	@Override
	public int getLevelscore() { // 等级评分 by changhao
		_xdb_verify_unsafe_();
		return levelscore;
	}

	@Override
	public int getXiulianscroe() { // 修炼评分 by changhao
		_xdb_verify_unsafe_();
		return xiulianscroe;
	}

	@Override
	public int getRolescore() { // 人物评分 by changhao
		_xdb_verify_unsafe_();
		return rolescore;
	}

	@Override
	public int getEquipscore() { // 装备评分 by changhao
		_xdb_verify_unsafe_();
		return equipscore;
	}

	@Override
	public long getHistorymaxprofessioncontribute() { // 历史最大职业贡献度 by changhao
		_xdb_verify_unsafe_();
		return historymaxprofessioncontribute;
	}

	@Override
	public int getRide() { // 当前坐骑 by changhao
		_xdb_verify_unsafe_();
		return ride;
	}

	@Override
	public long getBestpetid() { // 
		_xdb_verify_unsafe_();
		return bestpetid;
	}

	@Override
	public int getRolecolor1() { // 染色方案1
		_xdb_verify_unsafe_();
		return rolecolor1;
	}

	@Override
	public int getRolecolor2() { // 染色方案2
		_xdb_verify_unsafe_();
		return rolecolor2;
	}

	@Override
	public java.util.List<xbean.RoleColorType> getColorroomlist() { // 衣橱
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "colorroomlist"), colorroomlist);
	}

	public java.util.List<xbean.RoleColorType> getColorroomlistAsData() { // 衣橱
		_xdb_verify_unsafe_();
		java.util.List<xbean.RoleColorType> colorroomlist;
		Properties _o_ = this;
		colorroomlist = new java.util.LinkedList<xbean.RoleColorType>();
		for (xbean.RoleColorType _v_ : _o_.colorroomlist)
			colorroomlist.add(new RoleColorType.Data(_v_));
		return colorroomlist;
	}

	@Override
	public int getCruise() { // 巡游状态，默认0没有巡游，否则为巡游id
		_xdb_verify_unsafe_();
		return cruise;
	}

	@Override
	public int getCruise2() { // 巡游2 by changhao
		_xdb_verify_unsafe_();
		return cruise2;
	}

	@Override
	public int getCruise3() { // 巡游3 by changhao
		_xdb_verify_unsafe_();
		return cruise3;
	}

	@Override
	public java.util.List<xbean.BattleAI> getFighteai() { // 主角ai
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "fighteai"), fighteai);
	}

	public java.util.List<xbean.BattleAI> getFighteaiAsData() { // 主角ai
		_xdb_verify_unsafe_();
		java.util.List<xbean.BattleAI> fighteai;
		Properties _o_ = this;
		fighteai = new java.util.LinkedList<xbean.BattleAI>();
		for (xbean.BattleAI _v_ : _o_.fighteai)
			fighteai.add(new BattleAI.Data(_v_));
		return fighteai;
	}

	@Override
	public int getEquipeffect() { // 当前装备特效
		_xdb_verify_unsafe_();
		return equipeffect;
	}

	@Override
	public int getPilot() { // 新手引导项
		_xdb_verify_unsafe_();
		return pilot;
	}

	@Override
	public java.util.Map<Integer, Integer> getTupotips() { // 突破任务是否提示过
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "tupotips"), tupotips);
	}

	@Override
	public java.util.Map<Integer, Integer> getTupotipsAsData() { // 突破任务是否提示过
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> tupotips;
		Properties _o_ = this;
		tupotips = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tupotips.entrySet())
			tupotips.put(_e_.getKey(), _e_.getValue());
		return tupotips;
	}

	@Override
	public java.util.Map<Integer, Integer> getTupofinish() { // 突破任务是否完成
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "tupofinish"), tupofinish);
	}

	@Override
	public java.util.Map<Integer, Integer> getTupofinishAsData() { // 突破任务是否完成
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> tupofinish;
		Properties _o_ = this;
		tupofinish = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tupofinish.entrySet())
			tupofinish.put(_e_.getKey(), _e_.getValue());
		return tupofinish;
	}

	@Override
	public int getYingfuxishu() { // 盈福转换系数
		_xdb_verify_unsafe_();
		return yingfuxishu;
	}

	@Override
	public java.util.Map<Integer, String> getDepotnameinfo() { // key=仓库id  value=名称
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "depotnameinfo"), depotnameinfo);
	}

	@Override
	public java.util.Map<Integer, String> getDepotnameinfoAsData() { // key=仓库id  value=名称
		_xdb_verify_unsafe_();
		java.util.Map<Integer, String> depotnameinfo;
		Properties _o_ = this;
		depotnameinfo = new java.util.HashMap<Integer, String>();
		for (java.util.Map.Entry<Integer, String> _e_ : _o_.depotnameinfo.entrySet())
			depotnameinfo.put(_e_.getKey(), _e_.getValue());
		return depotnameinfo;
	}

	@Override
	public long getExpiretime() { // 点卡结束时间(点卡服专用) by changhao
		_xdb_verify_unsafe_();
		return expiretime;
	}

	@Override
	public int getFirstprompt() { // 还没提示过就是0(点卡服专用) by changhao
		_xdb_verify_unsafe_();
		return firstprompt;
	}

	@Override
	public long getLastchargetime() { // 最后一次充值时间
		_xdb_verify_unsafe_();
		return lastchargetime;
	}

	@Override
	public java.util.Map<Integer, Integer> getLineconfigmap() { // 精英副本自动进入设置
		_xdb_verify_unsafe_();
		return mkdb.Logs.logMap(new mkdb.LogKey(this, "lineconfigmap"), lineconfigmap);
	}

	@Override
	public java.util.Map<Integer, Integer> getLineconfigmapAsData() { // 精英副本自动进入设置
		_xdb_verify_unsafe_();
		java.util.Map<Integer, Integer> lineconfigmap;
		Properties _o_ = this;
		lineconfigmap = new java.util.HashMap<Integer, Integer>();
		for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.lineconfigmap.entrySet())
			lineconfigmap.put(_e_.getKey(), _e_.getValue());
		return lineconfigmap;
	}

	@Override
	public void setRolename(String _v_) { // 角色名
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rolename") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, rolename) {
					public void rollback() { rolename = _xdb_saved; }
				};}});
		rolename = _v_;
	}

	@Override
	public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 角色名
		_xdb_verify_unsafe_();
		this.setRolename(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setLevel(int _v_) { // 等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setShape(int _v_) { // 主角ID(是指游戏中设定的10个角色id)
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "shape") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, shape) {
					public void rollback() { shape = _xdb_saved; }
				};}});
		shape = _v_;
	}

	@Override
	public void setSex(int _v_) { // 人物性别
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sex") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, sex) {
					public void rollback() { sex = _xdb_saved; }
				};}});
		sex = _v_;
	}

	@Override
	public void setExp(long _v_) { // 当前经验
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "exp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, exp) {
					public void rollback() { exp = _xdb_saved; }
				};}});
		exp = _v_;
	}

	@Override
	public void setRace(int _v_) { // 种族
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "race") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, race) {
					public void rollback() { race = _xdb_saved; }
				};}});
		race = _v_;
	}

	@Override
	public void setSchool(int _v_) { // 职业
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "school") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, school) {
					public void rollback() { school = _xdb_saved; }
				};}});
		school = _v_;
	}

	@Override
	public void setColor(int _v_) { // 配色方案
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "color") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, color) {
					public void rollback() { color = _xdb_saved; }
				};}});
		color = _v_;
	}

	@Override
	public void setTitle(int _v_) { // 称谓
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "title") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, title) {
					public void rollback() { title = _xdb_saved; }
				};}});
		title = _v_;
	}

	@Override
	public void setHp(int _v_) { // 气血
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "hp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, hp) {
					public void rollback() { hp = _xdb_saved; }
				};}});
		hp = _v_;
	}

	@Override
	public void setUplimithp(int _v_) { // 当前气血上限
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "uplimithp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, uplimithp) {
					public void rollback() { uplimithp = _xdb_saved; }
				};}});
		uplimithp = _v_;
	}

	@Override
	public void setWound(int _v_) { // 伤，当前血上限=最大血上限-伤，伤：[0,MaxHp-1]
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "wound") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, wound) {
					public void rollback() { wound = _xdb_saved; }
				};}});
		wound = _v_;
	}

	@Override
	public void setMp(int _v_) { // 法力
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "mp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, mp) {
					public void rollback() { mp = _xdb_saved; }
				};}});
		mp = _v_;
	}

	@Override
	public void setSp(int _v_) { // 怒气
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, sp) {
					public void rollback() { sp = _xdb_saved; }
				};}});
		sp = _v_;
	}

	@Override
	public void setScheme(int _v_) { // 人物加点方案
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "scheme") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, scheme) {
					public void rollback() { scheme = _xdb_saved; }
				};}});
		scheme = _v_;
	}

	@Override
	public void setSchemechanges(int _v_) { // 人物加点方案切换次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "schemechanges") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, schemechanges) {
					public void rollback() { schemechanges = _xdb_saved; }
				};}});
		schemechanges = _v_;
	}

	@Override
	public void setSchemechangetimes(long _v_) { // 人物加点方案切换最后时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "schemechangetimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, schemechangetimes) {
					public void rollback() { schemechangetimes = _xdb_saved; }
				};}});
		schemechangetimes = _v_;
	}

	@Override
	public void setAllexp(long _v_) { // 玩家出生后获得所有经验
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "allexp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, allexp) {
					public void rollback() { allexp = _xdb_saved; }
				};}});
		allexp = _v_;
	}

	@Override
	public void setSceneid(long _v_) { // 角色所在场景ID
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sceneid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, sceneid) {
					public void rollback() { sceneid = _xdb_saved; }
				};}});
		sceneid = _v_;
	}

	@Override
	public void setPosx(int _v_) { // 坐标x
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "posx") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, posx) {
					public void rollback() { posx = _xdb_saved; }
				};}});
		posx = _v_;
	}

	@Override
	public void setPosy(int _v_) { // 坐标y
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "posy") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, posy) {
					public void rollback() { posy = _xdb_saved; }
				};}});
		posy = _v_;
	}

	@Override
	public void setPosz(int _v_) { // 坐标z
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "posz") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, posz) {
					public void rollback() { posz = _xdb_saved; }
				};}});
		posz = _v_;
	}

	@Override
	public void setLaststaticsceneid(long _v_) { // 最后一次玩家所在的静态场景ID
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "laststaticsceneid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, laststaticsceneid) {
					public void rollback() { laststaticsceneid = _xdb_saved; }
				};}});
		laststaticsceneid = _v_;
	}

	@Override
	public void setLaststaticposx(int _v_) { // 最后一次玩家所在的静态场景x
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "laststaticposx") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, laststaticposx) {
					public void rollback() { laststaticposx = _xdb_saved; }
				};}});
		laststaticposx = _v_;
	}

	@Override
	public void setLaststaticposy(int _v_) { // 最后一次玩家所在的静态场景y
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "laststaticposy") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, laststaticposy) {
					public void rollback() { laststaticposy = _xdb_saved; }
				};}});
		laststaticposy = _v_;
	}

	@Override
	public void setLaststaticposz(int _v_) { // 最后一次玩家所在的静态场景z
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "laststaticposz") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, laststaticposz) {
					public void rollback() { laststaticposz = _xdb_saved; }
				};}});
		laststaticposz = _v_;
	}

	@Override
	public void setFightpetkey(int _v_) { // 当前战斗宠物
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "fightpetkey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, fightpetkey) {
					public void rollback() { fightpetkey = _xdb_saved; }
				};}});
		fightpetkey = _v_;
	}

	@Override
	public void setShowpetkey(int _v_) { // 当前展示宠物
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "showpetkey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, showpetkey) {
					public void rollback() { showpetkey = _xdb_saved; }
				};}});
		showpetkey = _v_;
	}

	@Override
	public void setDefultform(int _v_) { // 默认光环
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "defultform") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, defultform) {
					public void rollback() { defultform = _xdb_saved; }
				};}});
		defultform = _v_;
	}

	@Override
	public void setClankey(long _v_) { // 角色对应公会的key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "clankey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, clankey) {
					public void rollback() { clankey = _xdb_saved; }
				};}});
		clankey = _v_;
	}

	@Override
	public void setOldclankey(long _v_) { // 角色对应的上次公会key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "oldclankey") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, oldclankey) {
					public void rollback() { oldclankey = _xdb_saved; }
				};}});
		oldclankey = _v_;
	}

	@Override
	public void setExitstate(int _v_) { // 角色退出公会方式，0主动退出    1被踢出    2解散          key by hzl
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "exitstate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, exitstate) {
					public void rollback() { exitstate = _xdb_saved; }
				};}});
		exitstate = _v_;
	}

	@Override
	public void setOnlinetime(long _v_) { // 角色上次登录时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "onlinetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, onlinetime) {
					public void rollback() { onlinetime = _xdb_saved; }
				};}});
		onlinetime = _v_;
	}

	@Override
	public void setOfflinetime(long _v_) { // 角色上次离线时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "offlinetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, offlinetime) {
					public void rollback() { offlinetime = _xdb_saved; }
				};}});
		offlinetime = _v_;
	}

	@Override
	public void setCreatetime(long _v_) { // 角色创建时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "createtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, createtime) {
					public void rollback() { createtime = _xdb_saved; }
				};}});
		createtime = _v_;
	}

	@Override
	public void setBindtel(long _v_) { // 绑定的手机号
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bindtel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, bindtel) {
					public void rollback() { bindtel = _xdb_saved; }
				};}});
		bindtel = _v_;
	}

	@Override
	public void setLogintimesofday(int _v_) { // 当天登录次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "logintimesofday") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, logintimesofday) {
					public void rollback() { logintimesofday = _xdb_saved; }
				};}});
		logintimesofday = _v_;
	}

	@Override
	public void setIsgetbindtelaward(int _v_) { // 是否得到绑定手机奖励（0不能领取 1 已经领取过了2 可以领取了）
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "isgetbindtelaward") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, isgetbindtelaward) {
					public void rollback() { isgetbindtelaward = _xdb_saved; }
				};}});
		isgetbindtelaward = _v_;
	}

	@Override
	public void setBindteltime(long _v_) { // 绑定手机的时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bindteltime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, bindteltime) {
					public void rollback() { bindteltime = _xdb_saved; }
				};}});
		bindteltime = _v_;
	}

	@Override
	public void setIsbindtelagain(int _v_) { // 是否需要重新绑定
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "isbindtelagain") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, isbindtelagain) {
					public void rollback() { isbindtelagain = _xdb_saved; }
				};}});
		isbindtelagain = _v_;
	}

	@Override
	public void setBindtelagainnum(int _v_) { // 重新绑定次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bindtelagainnum") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, bindtelagainnum) {
					public void rollback() { bindtelagainnum = _xdb_saved; }
				};}});
		bindtelagainnum = _v_;
	}

	@Override
	public void setPassword(String _v_) { // 安全锁密码
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "password") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, password) {
					public void rollback() { password = _xdb_saved; }
				};}});
		password = _v_;
	}

	@Override
	public void setPasswordOctets(com.locojoy.base.Octets _v_) { // 安全锁密码
		_xdb_verify_unsafe_();
		this.setPassword(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setForcedelpdtime(long _v_) { // 强制删除密码时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "forcedelpdtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, forcedelpdtime) {
					public void rollback() { forcedelpdtime = _xdb_saved; }
				};}});
		forcedelpdtime = _v_;
	}

	@Override
	public void setForcedelendtime(long _v_) { // 强制删除密码结束时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "forcedelendtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, forcedelendtime) {
					public void rollback() { forcedelendtime = _xdb_saved; }
				};}});
		forcedelendtime = _v_;
	}

	@Override
	public void setErrortimes(int _v_) { // 密码剩余输入错误次数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "errortimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, errortimes) {
					public void rollback() { errortimes = _xdb_saved; }
				};}});
		errortimes = _v_;
	}

	@Override
	public void setLockendtime(long _v_) { // 锁定结束时间点
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lockendtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lockendtime) {
					public void rollback() { lockendtime = _xdb_saved; }
				};}});
		lockendtime = _v_;
	}

	@Override
	public void setIsopensafelock(int _v_) { // 是否开启道具安全锁
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "isopensafelock") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, isopensafelock) {
					public void rollback() { isopensafelock = _xdb_saved; }
				};}});
		isopensafelock = _v_;
	}

	@Override
	public void setSumonlinetime(long _v_) { // 累积在线时长,ms,这个时长只有在下线的时候才会改变，其他情况还需要通过上次登录时间进行计算
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "sumonlinetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, sumonlinetime) {
					public void rollback() { sumonlinetime = _xdb_saved; }
				};}});
		sumonlinetime = _v_;
	}

	@Override
	public void setUserid(int _v_) { // 帐号角色id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "userid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, userid) {
					public void rollback() { userid = _xdb_saved; }
				};}});
		userid = _v_;
	}

	@Override
	public void setAddranklist(boolean _v_) { // 角色是否参与排行榜
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "addranklist") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogObject<Boolean>(this, addranklist) {
					public void rollback() { addranklist = _xdb_saved; }
				};}});
		addranklist = _v_;
	}

	@Override
	public void setDeletetime(long _v_) { // 角色被删除的时间,如果角色没有被删除,这个值为0
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "deletetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, deletetime) {
					public void rollback() { deletetime = _xdb_saved; }
				};}});
		deletetime = _v_;
	}

	@Override
	public void setLastloginip(int _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastloginip") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, lastloginip) {
					public void rollback() { lastloginip = _xdb_saved; }
				};}});
		lastloginip = _v_;
	}

	@Override
	public void setPlatformuid(String _v_) { // userid在平台的唯一id
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "platformuid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, platformuid) {
					public void rollback() { platformuid = _xdb_saved; }
				};}});
		platformuid = _v_;
	}

	@Override
	public void setPlatformuidOctets(com.locojoy.base.Octets _v_) { // userid在平台的唯一id
		_xdb_verify_unsafe_();
		this.setPlatformuid(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setViplevel(int _v_) { // vip等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "viplevel") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, viplevel) {
					public void rollback() { viplevel = _xdb_saved; }
				};}});
		viplevel = _v_;
	}

	@Override
	public void setEnergy(int _v_) { // 活力 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "energy") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, energy) {
					public void rollback() { energy = _xdb_saved; }
				};}});
		energy = _v_;
	}

	@Override
	public void setPlaytimetimes(int _v_) { // 战斗播放时长倍数 10为1倍速 liuxinhua
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "playtimetimes") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, playtimetimes) {
					public void rollback() { playtimetimes = _xdb_saved; }
				};}});
		playtimetimes = _v_;
	}

	@Override
	public void setRolezonghemaxscore(int _v_) { // 人物综合实力历史最高评分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rolezonghemaxscore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rolezonghemaxscore) {
					public void rollback() { rolezonghemaxscore = _xdb_saved; }
				};}});
		rolezonghemaxscore = _v_;
	}

	@Override
	public void setSkillscore(int _v_) { // 技能评分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "skillscore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, skillscore) {
					public void rollback() { skillscore = _xdb_saved; }
				};}});
		skillscore = _v_;
	}

	@Override
	public void setPetscore(int _v_) { // 最强单宠物评分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "petscore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, petscore) {
					public void rollback() { petscore = _xdb_saved; }
				};}});
		petscore = _v_;
	}

	@Override
	public void setManypetscore(int _v_) { // 多宠物评分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "manypetscore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, manypetscore) {
					public void rollback() { manypetscore = _xdb_saved; }
				};}});
		manypetscore = _v_;
	}

	@Override
	public void setLevelscore(int _v_) { // 等级评分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "levelscore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, levelscore) {
					public void rollback() { levelscore = _xdb_saved; }
				};}});
		levelscore = _v_;
	}

	@Override
	public void setXiulianscroe(int _v_) { // 修炼评分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "xiulianscroe") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, xiulianscroe) {
					public void rollback() { xiulianscroe = _xdb_saved; }
				};}});
		xiulianscroe = _v_;
	}

	@Override
	public void setRolescore(int _v_) { // 人物评分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rolescore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rolescore) {
					public void rollback() { rolescore = _xdb_saved; }
				};}});
		rolescore = _v_;
	}

	@Override
	public void setEquipscore(int _v_) { // 装备评分 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "equipscore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, equipscore) {
					public void rollback() { equipscore = _xdb_saved; }
				};}});
		equipscore = _v_;
	}

	@Override
	public void setHistorymaxprofessioncontribute(long _v_) { // 历史最大职业贡献度 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "historymaxprofessioncontribute") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, historymaxprofessioncontribute) {
					public void rollback() { historymaxprofessioncontribute = _xdb_saved; }
				};}});
		historymaxprofessioncontribute = _v_;
	}

	@Override
	public void setRide(int _v_) { // 当前坐骑 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "ride") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, ride) {
					public void rollback() { ride = _xdb_saved; }
				};}});
		ride = _v_;
	}

	@Override
	public void setBestpetid(long _v_) { // 
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "bestpetid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, bestpetid) {
					public void rollback() { bestpetid = _xdb_saved; }
				};}});
		bestpetid = _v_;
	}

	@Override
	public void setRolecolor1(int _v_) { // 染色方案1
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rolecolor1") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rolecolor1) {
					public void rollback() { rolecolor1 = _xdb_saved; }
				};}});
		rolecolor1 = _v_;
	}

	@Override
	public void setRolecolor2(int _v_) { // 染色方案2
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "rolecolor2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, rolecolor2) {
					public void rollback() { rolecolor2 = _xdb_saved; }
				};}});
		rolecolor2 = _v_;
	}

	@Override
	public void setCruise(int _v_) { // 巡游状态，默认0没有巡游，否则为巡游id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "cruise") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, cruise) {
					public void rollback() { cruise = _xdb_saved; }
				};}});
		cruise = _v_;
	}

	@Override
	public void setCruise2(int _v_) { // 巡游2 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "cruise2") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, cruise2) {
					public void rollback() { cruise2 = _xdb_saved; }
				};}});
		cruise2 = _v_;
	}

	@Override
	public void setCruise3(int _v_) { // 巡游3 by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "cruise3") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, cruise3) {
					public void rollback() { cruise3 = _xdb_saved; }
				};}});
		cruise3 = _v_;
	}

	@Override
	public void setEquipeffect(int _v_) { // 当前装备特效
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "equipeffect") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, equipeffect) {
					public void rollback() { equipeffect = _xdb_saved; }
				};}});
		equipeffect = _v_;
	}

	@Override
	public void setPilot(int _v_) { // 新手引导项
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "pilot") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, pilot) {
					public void rollback() { pilot = _xdb_saved; }
				};}});
		pilot = _v_;
	}

	@Override
	public void setYingfuxishu(int _v_) { // 盈福转换系数
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "yingfuxishu") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, yingfuxishu) {
					public void rollback() { yingfuxishu = _xdb_saved; }
				};}});
		yingfuxishu = _v_;
	}

	@Override
	public void setExpiretime(long _v_) { // 点卡结束时间(点卡服专用) by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "expiretime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, expiretime) {
					public void rollback() { expiretime = _xdb_saved; }
				};}});
		expiretime = _v_;
	}

	@Override
	public void setFirstprompt(int _v_) { // 还没提示过就是0(点卡服专用) by changhao
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firstprompt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, firstprompt) {
					public void rollback() { firstprompt = _xdb_saved; }
				};}});
		firstprompt = _v_;
	}

	@Override
	public void setLastchargetime(long _v_) { // 最后一次充值时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "lastchargetime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, lastchargetime) {
					public void rollback() { lastchargetime = _xdb_saved; }
				};}});
		lastchargetime = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		Properties _o_ = null;
		if ( _o1_ instanceof Properties ) _o_ = (Properties)_o1_;
		else if ( _o1_ instanceof Properties.Const ) _o_ = ((Properties.Const)_o1_).nThis();
		else return false;
		if (!rolename.equals(_o_.rolename)) return false;
		if (!usedname.equals(_o_.usedname)) return false;
		if (level != _o_.level) return false;
		if (shape != _o_.shape) return false;
		if (sex != _o_.sex) return false;
		if (exp != _o_.exp) return false;
		if (race != _o_.race) return false;
		if (school != _o_.school) return false;
		if (color != _o_.color) return false;
		if (title != _o_.title) return false;
		if (hp != _o_.hp) return false;
		if (uplimithp != _o_.uplimithp) return false;
		if (wound != _o_.wound) return false;
		if (mp != _o_.mp) return false;
		if (sp != _o_.sp) return false;
		if (!bfp.equals(_o_.bfp)) return false;
		if (!addpointfp.equals(_o_.addpointfp)) return false;
		if (!point.equals(_o_.point)) return false;
		if (scheme != _o_.scheme) return false;
		if (schemechanges != _o_.schemechanges) return false;
		if (schemechangetimes != _o_.schemechangetimes) return false;
		if (allexp != _o_.allexp) return false;
		if (sceneid != _o_.sceneid) return false;
		if (posx != _o_.posx) return false;
		if (posy != _o_.posy) return false;
		if (posz != _o_.posz) return false;
		if (laststaticsceneid != _o_.laststaticsceneid) return false;
		if (laststaticposx != _o_.laststaticposx) return false;
		if (laststaticposy != _o_.laststaticposy) return false;
		if (laststaticposz != _o_.laststaticposz) return false;
		if (fightpetkey != _o_.fightpetkey) return false;
		if (showpetkey != _o_.showpetkey) return false;
		if (!sysconfigmap.equals(_o_.sysconfigmap)) return false;
		if (!titles.equals(_o_.titles)) return false;
		if (defultform != _o_.defultform) return false;
		if (!formationsmap.equals(_o_.formationsmap)) return false;
		if (clankey != _o_.clankey) return false;
		if (oldclankey != _o_.oldclankey) return false;
		if (exitstate != _o_.exitstate) return false;
		if (onlinetime != _o_.onlinetime) return false;
		if (offlinetime != _o_.offlinetime) return false;
		if (createtime != _o_.createtime) return false;
		if (bindtel != _o_.bindtel) return false;
		if (logintimesofday != _o_.logintimesofday) return false;
		if (isgetbindtelaward != _o_.isgetbindtelaward) return false;
		if (bindteltime != _o_.bindteltime) return false;
		if (isbindtelagain != _o_.isbindtelagain) return false;
		if (bindtelagainnum != _o_.bindtelagainnum) return false;
		if (!unbindteltime.equals(_o_.unbindteltime)) return false;
		if (!password.equals(_o_.password)) return false;
		if (forcedelpdtime != _o_.forcedelpdtime) return false;
		if (forcedelendtime != _o_.forcedelendtime) return false;
		if (errortimes != _o_.errortimes) return false;
		if (lockendtime != _o_.lockendtime) return false;
		if (isopensafelock != _o_.isopensafelock) return false;
		if (sumonlinetime != _o_.sumonlinetime) return false;
		if (userid != _o_.userid) return false;
		if (addranklist != _o_.addranklist) return false;
		if (deletetime != _o_.deletetime) return false;
		if (lastloginip != _o_.lastloginip) return false;
		if (!platformuid.equals(_o_.platformuid)) return false;
		if (viplevel != _o_.viplevel) return false;
		if (energy != _o_.energy) return false;
		if (playtimetimes != _o_.playtimetimes) return false;
		if (rolezonghemaxscore != _o_.rolezonghemaxscore) return false;
		if (skillscore != _o_.skillscore) return false;
		if (petscore != _o_.petscore) return false;
		if (manypetscore != _o_.manypetscore) return false;
		if (levelscore != _o_.levelscore) return false;
		if (xiulianscroe != _o_.xiulianscroe) return false;
		if (rolescore != _o_.rolescore) return false;
		if (equipscore != _o_.equipscore) return false;
		if (historymaxprofessioncontribute != _o_.historymaxprofessioncontribute) return false;
		if (ride != _o_.ride) return false;
		if (bestpetid != _o_.bestpetid) return false;
		if (rolecolor1 != _o_.rolecolor1) return false;
		if (rolecolor2 != _o_.rolecolor2) return false;
		if (!colorroomlist.equals(_o_.colorroomlist)) return false;
		if (cruise != _o_.cruise) return false;
		if (cruise2 != _o_.cruise2) return false;
		if (cruise3 != _o_.cruise3) return false;
		if (!fighteai.equals(_o_.fighteai)) return false;
		if (equipeffect != _o_.equipeffect) return false;
		if (pilot != _o_.pilot) return false;
		if (!tupotips.equals(_o_.tupotips)) return false;
		if (!tupofinish.equals(_o_.tupofinish)) return false;
		if (yingfuxishu != _o_.yingfuxishu) return false;
		if (!depotnameinfo.equals(_o_.depotnameinfo)) return false;
		if (expiretime != _o_.expiretime) return false;
		if (firstprompt != _o_.firstprompt) return false;
		if (lastchargetime != _o_.lastchargetime) return false;
		if (!lineconfigmap.equals(_o_.lineconfigmap)) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += rolename.hashCode();
		_h_ += usedname.hashCode();
		_h_ += level;
		_h_ += shape;
		_h_ += sex;
		_h_ += exp;
		_h_ += race;
		_h_ += school;
		_h_ += color;
		_h_ += title;
		_h_ += hp;
		_h_ += uplimithp;
		_h_ += wound;
		_h_ += mp;
		_h_ += sp;
		_h_ += bfp.hashCode();
		_h_ += addpointfp.hashCode();
		_h_ += point.hashCode();
		_h_ += scheme;
		_h_ += schemechanges;
		_h_ += schemechangetimes;
		_h_ += allexp;
		_h_ += sceneid;
		_h_ += posx;
		_h_ += posy;
		_h_ += posz;
		_h_ += laststaticsceneid;
		_h_ += laststaticposx;
		_h_ += laststaticposy;
		_h_ += laststaticposz;
		_h_ += fightpetkey;
		_h_ += showpetkey;
		_h_ += sysconfigmap.hashCode();
		_h_ += titles.hashCode();
		_h_ += defultform;
		_h_ += formationsmap.hashCode();
		_h_ += clankey;
		_h_ += oldclankey;
		_h_ += exitstate;
		_h_ += onlinetime;
		_h_ += offlinetime;
		_h_ += createtime;
		_h_ += bindtel;
		_h_ += logintimesofday;
		_h_ += isgetbindtelaward;
		_h_ += bindteltime;
		_h_ += isbindtelagain;
		_h_ += bindtelagainnum;
		_h_ += unbindteltime.hashCode();
		_h_ += password.hashCode();
		_h_ += forcedelpdtime;
		_h_ += forcedelendtime;
		_h_ += errortimes;
		_h_ += lockendtime;
		_h_ += isopensafelock;
		_h_ += sumonlinetime;
		_h_ += userid;
		_h_ += addranklist ? 1231 : 1237;
		_h_ += deletetime;
		_h_ += lastloginip;
		_h_ += platformuid.hashCode();
		_h_ += viplevel;
		_h_ += energy;
		_h_ += playtimetimes;
		_h_ += rolezonghemaxscore;
		_h_ += skillscore;
		_h_ += petscore;
		_h_ += manypetscore;
		_h_ += levelscore;
		_h_ += xiulianscroe;
		_h_ += rolescore;
		_h_ += equipscore;
		_h_ += historymaxprofessioncontribute;
		_h_ += ride;
		_h_ += bestpetid;
		_h_ += rolecolor1;
		_h_ += rolecolor2;
		_h_ += colorroomlist.hashCode();
		_h_ += cruise;
		_h_ += cruise2;
		_h_ += cruise3;
		_h_ += fighteai.hashCode();
		_h_ += equipeffect;
		_h_ += pilot;
		_h_ += tupotips.hashCode();
		_h_ += tupofinish.hashCode();
		_h_ += yingfuxishu;
		_h_ += depotnameinfo.hashCode();
		_h_ += expiretime;
		_h_ += firstprompt;
		_h_ += lastchargetime;
		_h_ += lineconfigmap.hashCode();
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("'").append(rolename).append("'");
		_sb_.append(",");
		_sb_.append(usedname);
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(shape);
		_sb_.append(",");
		_sb_.append(sex);
		_sb_.append(",");
		_sb_.append(exp);
		_sb_.append(",");
		_sb_.append(race);
		_sb_.append(",");
		_sb_.append(school);
		_sb_.append(",");
		_sb_.append(color);
		_sb_.append(",");
		_sb_.append(title);
		_sb_.append(",");
		_sb_.append(hp);
		_sb_.append(",");
		_sb_.append(uplimithp);
		_sb_.append(",");
		_sb_.append(wound);
		_sb_.append(",");
		_sb_.append(mp);
		_sb_.append(",");
		_sb_.append(sp);
		_sb_.append(",");
		_sb_.append(bfp);
		_sb_.append(",");
		_sb_.append(addpointfp);
		_sb_.append(",");
		_sb_.append(point);
		_sb_.append(",");
		_sb_.append(scheme);
		_sb_.append(",");
		_sb_.append(schemechanges);
		_sb_.append(",");
		_sb_.append(schemechangetimes);
		_sb_.append(",");
		_sb_.append(allexp);
		_sb_.append(",");
		_sb_.append(sceneid);
		_sb_.append(",");
		_sb_.append(posx);
		_sb_.append(",");
		_sb_.append(posy);
		_sb_.append(",");
		_sb_.append(posz);
		_sb_.append(",");
		_sb_.append(laststaticsceneid);
		_sb_.append(",");
		_sb_.append(laststaticposx);
		_sb_.append(",");
		_sb_.append(laststaticposy);
		_sb_.append(",");
		_sb_.append(laststaticposz);
		_sb_.append(",");
		_sb_.append(fightpetkey);
		_sb_.append(",");
		_sb_.append(showpetkey);
		_sb_.append(",");
		_sb_.append(sysconfigmap);
		_sb_.append(",");
		_sb_.append(titles);
		_sb_.append(",");
		_sb_.append(defultform);
		_sb_.append(",");
		_sb_.append(formationsmap);
		_sb_.append(",");
		_sb_.append(clankey);
		_sb_.append(",");
		_sb_.append(oldclankey);
		_sb_.append(",");
		_sb_.append(exitstate);
		_sb_.append(",");
		_sb_.append(onlinetime);
		_sb_.append(",");
		_sb_.append(offlinetime);
		_sb_.append(",");
		_sb_.append(createtime);
		_sb_.append(",");
		_sb_.append(bindtel);
		_sb_.append(",");
		_sb_.append(logintimesofday);
		_sb_.append(",");
		_sb_.append(isgetbindtelaward);
		_sb_.append(",");
		_sb_.append(bindteltime);
		_sb_.append(",");
		_sb_.append(isbindtelagain);
		_sb_.append(",");
		_sb_.append(bindtelagainnum);
		_sb_.append(",");
		_sb_.append(unbindteltime);
		_sb_.append(",");
		_sb_.append("'").append(password).append("'");
		_sb_.append(",");
		_sb_.append(forcedelpdtime);
		_sb_.append(",");
		_sb_.append(forcedelendtime);
		_sb_.append(",");
		_sb_.append(errortimes);
		_sb_.append(",");
		_sb_.append(lockendtime);
		_sb_.append(",");
		_sb_.append(isopensafelock);
		_sb_.append(",");
		_sb_.append(sumonlinetime);
		_sb_.append(",");
		_sb_.append(userid);
		_sb_.append(",");
		_sb_.append(addranklist);
		_sb_.append(",");
		_sb_.append(deletetime);
		_sb_.append(",");
		_sb_.append(lastloginip);
		_sb_.append(",");
		_sb_.append("'").append(platformuid).append("'");
		_sb_.append(",");
		_sb_.append(viplevel);
		_sb_.append(",");
		_sb_.append(energy);
		_sb_.append(",");
		_sb_.append(playtimetimes);
		_sb_.append(",");
		_sb_.append(rolezonghemaxscore);
		_sb_.append(",");
		_sb_.append(skillscore);
		_sb_.append(",");
		_sb_.append(petscore);
		_sb_.append(",");
		_sb_.append(manypetscore);
		_sb_.append(",");
		_sb_.append(levelscore);
		_sb_.append(",");
		_sb_.append(xiulianscroe);
		_sb_.append(",");
		_sb_.append(rolescore);
		_sb_.append(",");
		_sb_.append(equipscore);
		_sb_.append(",");
		_sb_.append(historymaxprofessioncontribute);
		_sb_.append(",");
		_sb_.append(ride);
		_sb_.append(",");
		_sb_.append(bestpetid);
		_sb_.append(",");
		_sb_.append(rolecolor1);
		_sb_.append(",");
		_sb_.append(rolecolor2);
		_sb_.append(",");
		_sb_.append(colorroomlist);
		_sb_.append(",");
		_sb_.append(cruise);
		_sb_.append(",");
		_sb_.append(cruise2);
		_sb_.append(",");
		_sb_.append(cruise3);
		_sb_.append(",");
		_sb_.append(fighteai);
		_sb_.append(",");
		_sb_.append(equipeffect);
		_sb_.append(",");
		_sb_.append(pilot);
		_sb_.append(",");
		_sb_.append(tupotips);
		_sb_.append(",");
		_sb_.append(tupofinish);
		_sb_.append(",");
		_sb_.append(yingfuxishu);
		_sb_.append(",");
		_sb_.append(depotnameinfo);
		_sb_.append(",");
		_sb_.append(expiretime);
		_sb_.append(",");
		_sb_.append(firstprompt);
		_sb_.append(",");
		_sb_.append(lastchargetime);
		_sb_.append(",");
		_sb_.append(lineconfigmap);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rolename"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("usedname"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("shape"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sex"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("exp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("race"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("school"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("color"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("title"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("hp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("uplimithp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("wound"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("mp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bfp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("addpointfp"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("point"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("scheme"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("schemechanges"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("schemechangetimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("allexp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sceneid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("posx"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("posy"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("posz"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("laststaticsceneid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("laststaticposx"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("laststaticposy"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("laststaticposz"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fightpetkey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("showpetkey"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("sysconfigmap"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("titles"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("defultform"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("formationsmap"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("clankey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("oldclankey"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("exitstate"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("onlinetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("offlinetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("createtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bindtel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("logintimesofday"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("isgetbindtelaward"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bindteltime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("isbindtelagain"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bindtelagainnum"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("unbindteltime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("password"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("forcedelpdtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("forcedelendtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("errortimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lockendtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("isopensafelock"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("sumonlinetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("userid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("addranklist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("deletetime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastloginip"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("platformuid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("viplevel"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("energy"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("playtimetimes"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rolezonghemaxscore"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("skillscore"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("petscore"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("manypetscore"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("levelscore"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("xiulianscroe"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rolescore"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("equipscore"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("historymaxprofessioncontribute"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("ride"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("bestpetid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rolecolor1"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("rolecolor2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("colorroomlist"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("cruise"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("cruise2"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("cruise3"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("fighteai"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("equipeffect"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("pilot"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("tupotips"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("tupofinish"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("yingfuxishu"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("depotnameinfo"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("expiretime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firstprompt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("lastchargetime"));
		lb.add(new mkdb.logs.ListenableMap().setVarName("lineconfigmap"));
		return lb;
	}

	private class Const implements xbean.Properties {
		Properties nThis() {
			return Properties.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.Properties copy() {
			return Properties.this.copy();
		}

		@Override
		public xbean.Properties toData() {
			return Properties.this.toData();
		}

		public xbean.Properties toBean() {
			return Properties.this.toBean();
		}

		@Override
		public xbean.Properties toDataIf() {
			return Properties.this.toDataIf();
		}

		public xbean.Properties toBeanIf() {
			return Properties.this.toBeanIf();
		}

		@Override
		public String getRolename() { // 角色名
			_xdb_verify_unsafe_();
			return rolename;
		}

		@Override
		public com.locojoy.base.Octets getRolenameOctets() { // 角色名
			_xdb_verify_unsafe_();
			return Properties.this.getRolenameOctets();
		}

		@Override
		public java.util.List<String> getUsedname() { // 曾用名
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(usedname);
		}

		public java.util.List<String> getUsednameAsData() { // 曾用名
			_xdb_verify_unsafe_();
			java.util.List<String> usedname;
			Properties _o_ = Properties.this;
		usedname = new java.util.LinkedList<String>();
		usedname.addAll(_o_.usedname);
			return usedname;
		}

		@Override
		public int getLevel() { // 等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getShape() { // 主角ID(是指游戏中设定的10个角色id)
			_xdb_verify_unsafe_();
			return shape;
		}

		@Override
		public int getSex() { // 人物性别
			_xdb_verify_unsafe_();
			return sex;
		}

		@Override
		public long getExp() { // 当前经验
			_xdb_verify_unsafe_();
			return exp;
		}

		@Override
		public int getRace() { // 种族
			_xdb_verify_unsafe_();
			return race;
		}

		@Override
		public int getSchool() { // 职业
			_xdb_verify_unsafe_();
			return school;
		}

		@Override
		public int getColor() { // 配色方案
			_xdb_verify_unsafe_();
			return color;
		}

		@Override
		public int getTitle() { // 称谓
			_xdb_verify_unsafe_();
			return title;
		}

		@Override
		public int getHp() { // 气血
			_xdb_verify_unsafe_();
			return hp;
		}

		@Override
		public int getUplimithp() { // 当前气血上限
			_xdb_verify_unsafe_();
			return uplimithp;
		}

		@Override
		public int getWound() { // 伤，当前血上限=最大血上限-伤，伤：[0,MaxHp-1]
			_xdb_verify_unsafe_();
			return wound;
		}

		@Override
		public int getMp() { // 法力
			_xdb_verify_unsafe_();
			return mp;
		}

		@Override
		public int getSp() { // 怒气
			_xdb_verify_unsafe_();
			return sp;
		}

		@Override
		public xbean.BasicFightProperties getBfp() { // 存储基础的点数
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(bfp);
		}

		@Override
		public xbean.RoleAddPointProperties getAddpointfp() { // 人物加点方案
			_xdb_verify_unsafe_();
			return mkdb.Consts.toConst(addpointfp);
		}

		@Override
		public java.util.Map<Integer, Integer> getPoint() { // 潜能。未分配点数
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(point);
		}

		@Override
		public java.util.Map<Integer, Integer> getPointAsData() { // 潜能。未分配点数
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> point;
			Properties _o_ = Properties.this;
			point = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.point.entrySet())
				point.put(_e_.getKey(), _e_.getValue());
			return point;
		}

		@Override
		public int getScheme() { // 人物加点方案
			_xdb_verify_unsafe_();
			return scheme;
		}

		@Override
		public int getSchemechanges() { // 人物加点方案切换次数
			_xdb_verify_unsafe_();
			return schemechanges;
		}

		@Override
		public long getSchemechangetimes() { // 人物加点方案切换最后时间
			_xdb_verify_unsafe_();
			return schemechangetimes;
		}

		@Override
		public long getAllexp() { // 玩家出生后获得所有经验
			_xdb_verify_unsafe_();
			return allexp;
		}

		@Override
		public long getSceneid() { // 角色所在场景ID
			_xdb_verify_unsafe_();
			return sceneid;
		}

		@Override
		public int getPosx() { // 坐标x
			_xdb_verify_unsafe_();
			return posx;
		}

		@Override
		public int getPosy() { // 坐标y
			_xdb_verify_unsafe_();
			return posy;
		}

		@Override
		public int getPosz() { // 坐标z
			_xdb_verify_unsafe_();
			return posz;
		}

		@Override
		public long getLaststaticsceneid() { // 最后一次玩家所在的静态场景ID
			_xdb_verify_unsafe_();
			return laststaticsceneid;
		}

		@Override
		public int getLaststaticposx() { // 最后一次玩家所在的静态场景x
			_xdb_verify_unsafe_();
			return laststaticposx;
		}

		@Override
		public int getLaststaticposy() { // 最后一次玩家所在的静态场景y
			_xdb_verify_unsafe_();
			return laststaticposy;
		}

		@Override
		public int getLaststaticposz() { // 最后一次玩家所在的静态场景z
			_xdb_verify_unsafe_();
			return laststaticposz;
		}

		@Override
		public int getFightpetkey() { // 当前战斗宠物
			_xdb_verify_unsafe_();
			return fightpetkey;
		}

		@Override
		public int getShowpetkey() { // 当前展示宠物
			_xdb_verify_unsafe_();
			return showpetkey;
		}

		@Override
		public java.util.Map<Integer, Integer> getSysconfigmap() { // 系统设置
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(sysconfigmap);
		}

		@Override
		public java.util.Map<Integer, Integer> getSysconfigmapAsData() { // 系统设置
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> sysconfigmap;
			Properties _o_ = Properties.this;
			sysconfigmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.sysconfigmap.entrySet())
				sysconfigmap.put(_e_.getKey(), _e_.getValue());
			return sysconfigmap;
		}

		@Override
		public java.util.Map<Integer, xbean.TitleInfo> getTitles() { // 拥有的称谓列表
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(titles);
		}

		@Override
		public java.util.Map<Integer, xbean.TitleInfo> getTitlesAsData() { // 拥有的称谓列表
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.TitleInfo> titles;
			Properties _o_ = Properties.this;
			titles = new java.util.HashMap<Integer, xbean.TitleInfo>();
			for (java.util.Map.Entry<Integer, xbean.TitleInfo> _e_ : _o_.titles.entrySet())
				titles.put(_e_.getKey(), new TitleInfo.Data(_e_.getValue()));
			return titles;
		}

		@Override
		public int getDefultform() { // 默认光环
			_xdb_verify_unsafe_();
			return defultform;
		}

		@Override
		public java.util.Map<Integer, xbean.FormBean> getFormationsmap() { // key=光环id  value=光环等级
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(formationsmap);
		}

		@Override
		public java.util.Map<Integer, xbean.FormBean> getFormationsmapAsData() { // key=光环id  value=光环等级
			_xdb_verify_unsafe_();
			java.util.Map<Integer, xbean.FormBean> formationsmap;
			Properties _o_ = Properties.this;
			formationsmap = new java.util.HashMap<Integer, xbean.FormBean>();
			for (java.util.Map.Entry<Integer, xbean.FormBean> _e_ : _o_.formationsmap.entrySet())
				formationsmap.put(_e_.getKey(), new FormBean.Data(_e_.getValue()));
			return formationsmap;
		}

		@Override
		public long getClankey() { // 角色对应公会的key
			_xdb_verify_unsafe_();
			return clankey;
		}

		@Override
		public long getOldclankey() { // 角色对应的上次公会key
			_xdb_verify_unsafe_();
			return oldclankey;
		}

		@Override
		public int getExitstate() { // 角色退出公会方式，0主动退出    1被踢出    2解散          key by hzl
			_xdb_verify_unsafe_();
			return exitstate;
		}

		@Override
		public long getOnlinetime() { // 角色上次登录时间
			_xdb_verify_unsafe_();
			return onlinetime;
		}

		@Override
		public long getOfflinetime() { // 角色上次离线时间
			_xdb_verify_unsafe_();
			return offlinetime;
		}

		@Override
		public long getCreatetime() { // 角色创建时间
			_xdb_verify_unsafe_();
			return createtime;
		}

		@Override
		public long getBindtel() { // 绑定的手机号
			_xdb_verify_unsafe_();
			return bindtel;
		}

		@Override
		public int getLogintimesofday() { // 当天登录次数
			_xdb_verify_unsafe_();
			return logintimesofday;
		}

		@Override
		public int getIsgetbindtelaward() { // 是否得到绑定手机奖励（0不能领取 1 已经领取过了2 可以领取了）
			_xdb_verify_unsafe_();
			return isgetbindtelaward;
		}

		@Override
		public long getBindteltime() { // 绑定手机的时间
			_xdb_verify_unsafe_();
			return bindteltime;
		}

		@Override
		public int getIsbindtelagain() { // 是否需要重新绑定
			_xdb_verify_unsafe_();
			return isbindtelagain;
		}

		@Override
		public int getBindtelagainnum() { // 重新绑定次数
			_xdb_verify_unsafe_();
			return bindtelagainnum;
		}

		@Override
		public java.util.List<Long> getUnbindteltime() { // 解除绑定的时间点
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(unbindteltime);
		}

		public java.util.List<Long> getUnbindteltimeAsData() { // 解除绑定的时间点
			_xdb_verify_unsafe_();
			java.util.List<Long> unbindteltime;
			Properties _o_ = Properties.this;
		unbindteltime = new java.util.LinkedList<Long>();
		unbindteltime.addAll(_o_.unbindteltime);
			return unbindteltime;
		}

		@Override
		public String getPassword() { // 安全锁密码
			_xdb_verify_unsafe_();
			return password;
		}

		@Override
		public com.locojoy.base.Octets getPasswordOctets() { // 安全锁密码
			_xdb_verify_unsafe_();
			return Properties.this.getPasswordOctets();
		}

		@Override
		public long getForcedelpdtime() { // 强制删除密码时间
			_xdb_verify_unsafe_();
			return forcedelpdtime;
		}

		@Override
		public long getForcedelendtime() { // 强制删除密码结束时间
			_xdb_verify_unsafe_();
			return forcedelendtime;
		}

		@Override
		public int getErrortimes() { // 密码剩余输入错误次数
			_xdb_verify_unsafe_();
			return errortimes;
		}

		@Override
		public long getLockendtime() { // 锁定结束时间点
			_xdb_verify_unsafe_();
			return lockendtime;
		}

		@Override
		public int getIsopensafelock() { // 是否开启道具安全锁
			_xdb_verify_unsafe_();
			return isopensafelock;
		}

		@Override
		public long getSumonlinetime() { // 累积在线时长,ms,这个时长只有在下线的时候才会改变，其他情况还需要通过上次登录时间进行计算
			_xdb_verify_unsafe_();
			return sumonlinetime;
		}

		@Override
		public int getUserid() { // 帐号角色id
			_xdb_verify_unsafe_();
			return userid;
		}

		@Override
		public boolean getAddranklist() { // 角色是否参与排行榜
			_xdb_verify_unsafe_();
			return addranklist;
		}

		@Override
		public long getDeletetime() { // 角色被删除的时间,如果角色没有被删除,这个值为0
			_xdb_verify_unsafe_();
			return deletetime;
		}

		@Override
		public int getLastloginip() { // 
			_xdb_verify_unsafe_();
			return lastloginip;
		}

		@Override
		public String getPlatformuid() { // userid在平台的唯一id
			_xdb_verify_unsafe_();
			return platformuid;
		}

		@Override
		public com.locojoy.base.Octets getPlatformuidOctets() { // userid在平台的唯一id
			_xdb_verify_unsafe_();
			return Properties.this.getPlatformuidOctets();
		}

		@Override
		public int getViplevel() { // vip等级
			_xdb_verify_unsafe_();
			return viplevel;
		}

		@Override
		public int getEnergy() { // 活力 by changhao
			_xdb_verify_unsafe_();
			return energy;
		}

		@Override
		public int getPlaytimetimes() { // 战斗播放时长倍数 10为1倍速 liuxinhua
			_xdb_verify_unsafe_();
			return playtimetimes;
		}

		@Override
		public int getRolezonghemaxscore() { // 人物综合实力历史最高评分 by changhao
			_xdb_verify_unsafe_();
			return rolezonghemaxscore;
		}

		@Override
		public int getSkillscore() { // 技能评分 by changhao
			_xdb_verify_unsafe_();
			return skillscore;
		}

		@Override
		public int getPetscore() { // 最强单宠物评分 by changhao
			_xdb_verify_unsafe_();
			return petscore;
		}

		@Override
		public int getManypetscore() { // 多宠物评分 by changhao
			_xdb_verify_unsafe_();
			return manypetscore;
		}

		@Override
		public int getLevelscore() { // 等级评分 by changhao
			_xdb_verify_unsafe_();
			return levelscore;
		}

		@Override
		public int getXiulianscroe() { // 修炼评分 by changhao
			_xdb_verify_unsafe_();
			return xiulianscroe;
		}

		@Override
		public int getRolescore() { // 人物评分 by changhao
			_xdb_verify_unsafe_();
			return rolescore;
		}

		@Override
		public int getEquipscore() { // 装备评分 by changhao
			_xdb_verify_unsafe_();
			return equipscore;
		}

		@Override
		public long getHistorymaxprofessioncontribute() { // 历史最大职业贡献度 by changhao
			_xdb_verify_unsafe_();
			return historymaxprofessioncontribute;
		}

		@Override
		public int getRide() { // 当前坐骑 by changhao
			_xdb_verify_unsafe_();
			return ride;
		}

		@Override
		public long getBestpetid() { // 
			_xdb_verify_unsafe_();
			return bestpetid;
		}

		@Override
		public int getRolecolor1() { // 染色方案1
			_xdb_verify_unsafe_();
			return rolecolor1;
		}

		@Override
		public int getRolecolor2() { // 染色方案2
			_xdb_verify_unsafe_();
			return rolecolor2;
		}

		@Override
		public java.util.List<xbean.RoleColorType> getColorroomlist() { // 衣橱
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(colorroomlist);
		}

		public java.util.List<xbean.RoleColorType> getColorroomlistAsData() { // 衣橱
			_xdb_verify_unsafe_();
			java.util.List<xbean.RoleColorType> colorroomlist;
			Properties _o_ = Properties.this;
		colorroomlist = new java.util.LinkedList<xbean.RoleColorType>();
		for (xbean.RoleColorType _v_ : _o_.colorroomlist)
			colorroomlist.add(new RoleColorType.Data(_v_));
			return colorroomlist;
		}

		@Override
		public int getCruise() { // 巡游状态，默认0没有巡游，否则为巡游id
			_xdb_verify_unsafe_();
			return cruise;
		}

		@Override
		public int getCruise2() { // 巡游2 by changhao
			_xdb_verify_unsafe_();
			return cruise2;
		}

		@Override
		public int getCruise3() { // 巡游3 by changhao
			_xdb_verify_unsafe_();
			return cruise3;
		}

		@Override
		public java.util.List<xbean.BattleAI> getFighteai() { // 主角ai
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(fighteai);
		}

		public java.util.List<xbean.BattleAI> getFighteaiAsData() { // 主角ai
			_xdb_verify_unsafe_();
			java.util.List<xbean.BattleAI> fighteai;
			Properties _o_ = Properties.this;
		fighteai = new java.util.LinkedList<xbean.BattleAI>();
		for (xbean.BattleAI _v_ : _o_.fighteai)
			fighteai.add(new BattleAI.Data(_v_));
			return fighteai;
		}

		@Override
		public int getEquipeffect() { // 当前装备特效
			_xdb_verify_unsafe_();
			return equipeffect;
		}

		@Override
		public int getPilot() { // 新手引导项
			_xdb_verify_unsafe_();
			return pilot;
		}

		@Override
		public java.util.Map<Integer, Integer> getTupotips() { // 突破任务是否提示过
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(tupotips);
		}

		@Override
		public java.util.Map<Integer, Integer> getTupotipsAsData() { // 突破任务是否提示过
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> tupotips;
			Properties _o_ = Properties.this;
			tupotips = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tupotips.entrySet())
				tupotips.put(_e_.getKey(), _e_.getValue());
			return tupotips;
		}

		@Override
		public java.util.Map<Integer, Integer> getTupofinish() { // 突破任务是否完成
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(tupofinish);
		}

		@Override
		public java.util.Map<Integer, Integer> getTupofinishAsData() { // 突破任务是否完成
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> tupofinish;
			Properties _o_ = Properties.this;
			tupofinish = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tupofinish.entrySet())
				tupofinish.put(_e_.getKey(), _e_.getValue());
			return tupofinish;
		}

		@Override
		public int getYingfuxishu() { // 盈福转换系数
			_xdb_verify_unsafe_();
			return yingfuxishu;
		}

		@Override
		public java.util.Map<Integer, String> getDepotnameinfo() { // key=仓库id  value=名称
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(depotnameinfo);
		}

		@Override
		public java.util.Map<Integer, String> getDepotnameinfoAsData() { // key=仓库id  value=名称
			_xdb_verify_unsafe_();
			java.util.Map<Integer, String> depotnameinfo;
			Properties _o_ = Properties.this;
			depotnameinfo = new java.util.HashMap<Integer, String>();
			for (java.util.Map.Entry<Integer, String> _e_ : _o_.depotnameinfo.entrySet())
				depotnameinfo.put(_e_.getKey(), _e_.getValue());
			return depotnameinfo;
		}

		@Override
		public long getExpiretime() { // 点卡结束时间(点卡服专用) by changhao
			_xdb_verify_unsafe_();
			return expiretime;
		}

		@Override
		public int getFirstprompt() { // 还没提示过就是0(点卡服专用) by changhao
			_xdb_verify_unsafe_();
			return firstprompt;
		}

		@Override
		public long getLastchargetime() { // 最后一次充值时间
			_xdb_verify_unsafe_();
			return lastchargetime;
		}

		@Override
		public java.util.Map<Integer, Integer> getLineconfigmap() { // 精英副本自动进入设置
			_xdb_verify_unsafe_();
			return mkdb.Consts.constMap(lineconfigmap);
		}

		@Override
		public java.util.Map<Integer, Integer> getLineconfigmapAsData() { // 精英副本自动进入设置
			_xdb_verify_unsafe_();
			java.util.Map<Integer, Integer> lineconfigmap;
			Properties _o_ = Properties.this;
			lineconfigmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.lineconfigmap.entrySet())
				lineconfigmap.put(_e_.getKey(), _e_.getValue());
			return lineconfigmap;
		}

		@Override
		public void setRolename(String _v_) { // 角色名
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 角色名
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShape(int _v_) { // 主角ID(是指游戏中设定的10个角色id)
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSex(int _v_) { // 人物性别
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExp(long _v_) { // 当前经验
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRace(int _v_) { // 种族
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSchool(int _v_) { // 职业
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setColor(int _v_) { // 配色方案
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTitle(int _v_) { // 称谓
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHp(int _v_) { // 气血
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUplimithp(int _v_) { // 当前气血上限
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setWound(int _v_) { // 伤，当前血上限=最大血上限-伤，伤：[0,MaxHp-1]
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMp(int _v_) { // 法力
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSp(int _v_) { // 怒气
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setScheme(int _v_) { // 人物加点方案
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSchemechanges(int _v_) { // 人物加点方案切换次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSchemechangetimes(long _v_) { // 人物加点方案切换最后时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAllexp(long _v_) { // 玩家出生后获得所有经验
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSceneid(long _v_) { // 角色所在场景ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPosx(int _v_) { // 坐标x
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPosy(int _v_) { // 坐标y
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPosz(int _v_) { // 坐标z
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLaststaticsceneid(long _v_) { // 最后一次玩家所在的静态场景ID
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLaststaticposx(int _v_) { // 最后一次玩家所在的静态场景x
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLaststaticposy(int _v_) { // 最后一次玩家所在的静态场景y
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLaststaticposz(int _v_) { // 最后一次玩家所在的静态场景z
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFightpetkey(int _v_) { // 当前战斗宠物
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShowpetkey(int _v_) { // 当前展示宠物
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDefultform(int _v_) { // 默认光环
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setClankey(long _v_) { // 角色对应公会的key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOldclankey(long _v_) { // 角色对应的上次公会key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExitstate(int _v_) { // 角色退出公会方式，0主动退出    1被踢出    2解散          key by hzl
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOnlinetime(long _v_) { // 角色上次登录时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setOfflinetime(long _v_) { // 角色上次离线时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCreatetime(long _v_) { // 角色创建时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBindtel(long _v_) { // 绑定的手机号
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLogintimesofday(int _v_) { // 当天登录次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIsgetbindtelaward(int _v_) { // 是否得到绑定手机奖励（0不能领取 1 已经领取过了2 可以领取了）
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBindteltime(long _v_) { // 绑定手机的时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIsbindtelagain(int _v_) { // 是否需要重新绑定
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBindtelagainnum(int _v_) { // 重新绑定次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPassword(String _v_) { // 安全锁密码
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPasswordOctets(com.locojoy.base.Octets _v_) { // 安全锁密码
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setForcedelpdtime(long _v_) { // 强制删除密码时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setForcedelendtime(long _v_) { // 强制删除密码结束时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setErrortimes(int _v_) { // 密码剩余输入错误次数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLockendtime(long _v_) { // 锁定结束时间点
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setIsopensafelock(int _v_) { // 是否开启道具安全锁
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSumonlinetime(long _v_) { // 累积在线时长,ms,这个时长只有在下线的时候才会改变，其他情况还需要通过上次登录时间进行计算
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUserid(int _v_) { // 帐号角色id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAddranklist(boolean _v_) { // 角色是否参与排行榜
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDeletetime(long _v_) { // 角色被删除的时间,如果角色没有被删除,这个值为0
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastloginip(int _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatformuid(String _v_) { // userid在平台的唯一id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlatformuidOctets(com.locojoy.base.Octets _v_) { // userid在平台的唯一id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setViplevel(int _v_) { // vip等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEnergy(int _v_) { // 活力 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPlaytimetimes(int _v_) { // 战斗播放时长倍数 10为1倍速 liuxinhua
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolezonghemaxscore(int _v_) { // 人物综合实力历史最高评分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSkillscore(int _v_) { // 技能评分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPetscore(int _v_) { // 最强单宠物评分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setManypetscore(int _v_) { // 多宠物评分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevelscore(int _v_) { // 等级评分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setXiulianscroe(int _v_) { // 修炼评分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolescore(int _v_) { // 人物评分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEquipscore(int _v_) { // 装备评分 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setHistorymaxprofessioncontribute(long _v_) { // 历史最大职业贡献度 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRide(int _v_) { // 当前坐骑 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setBestpetid(long _v_) { // 
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolecolor1(int _v_) { // 染色方案1
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRolecolor2(int _v_) { // 染色方案2
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCruise(int _v_) { // 巡游状态，默认0没有巡游，否则为巡游id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCruise2(int _v_) { // 巡游2 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCruise3(int _v_) { // 巡游3 by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setEquipeffect(int _v_) { // 当前装备特效
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPilot(int _v_) { // 新手引导项
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setYingfuxishu(int _v_) { // 盈福转换系数
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExpiretime(long _v_) { // 点卡结束时间(点卡服专用) by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirstprompt(int _v_) { // 还没提示过就是0(点卡服专用) by changhao
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLastchargetime(long _v_) { // 最后一次充值时间
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
			return Properties.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return Properties.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return Properties.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return Properties.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return Properties.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return Properties.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return Properties.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return Properties.this.hashCode();
		}

		@Override
		public String toString() {
			return Properties.this.toString();
		}

	}

	public static final class Data implements xbean.Properties {
		private String rolename; // 角色名
		private java.util.LinkedList<String> usedname; // 曾用名
		private int level; // 等级
		private int shape; // 主角ID(是指游戏中设定的10个角色id)
		private int sex; // 人物性别
		private long exp; // 当前经验
		private int race; // 种族
		private int school; // 职业
		private int color; // 配色方案
		private int title; // 称谓
		private int hp; // 气血
		private int uplimithp; // 当前气血上限
		private int wound; // 伤，当前血上限=最大血上限-伤，伤：[0,MaxHp-1]
		private int mp; // 法力
		private int sp; // 怒气
		private xbean.BasicFightProperties bfp; // 存储基础的点数
		private xbean.RoleAddPointProperties addpointfp; // 人物加点方案
		private java.util.HashMap<Integer, Integer> point; // 潜能。未分配点数
		private int scheme; // 人物加点方案
		private int schemechanges; // 人物加点方案切换次数
		private long schemechangetimes; // 人物加点方案切换最后时间
		private long allexp; // 玩家出生后获得所有经验
		private long sceneid; // 角色所在场景ID
		private int posx; // 坐标x
		private int posy; // 坐标y
		private int posz; // 坐标z
		private long laststaticsceneid; // 最后一次玩家所在的静态场景ID
		private int laststaticposx; // 最后一次玩家所在的静态场景x
		private int laststaticposy; // 最后一次玩家所在的静态场景y
		private int laststaticposz; // 最后一次玩家所在的静态场景z
		private int fightpetkey; // 当前战斗宠物
		private int showpetkey; // 当前展示宠物
		private java.util.HashMap<Integer, Integer> sysconfigmap; // 系统设置
		private java.util.HashMap<Integer, xbean.TitleInfo> titles; // 拥有的称谓列表
		private int defultform; // 默认光环
		private java.util.HashMap<Integer, xbean.FormBean> formationsmap; // key=光环id  value=光环等级
		private long clankey; // 角色对应公会的key
		private long oldclankey; // 角色对应的上次公会key
		private int exitstate; // 角色退出公会方式，0主动退出    1被踢出    2解散          key by hzl
		private long onlinetime; // 角色上次登录时间
		private long offlinetime; // 角色上次离线时间
		private long createtime; // 角色创建时间
		private long bindtel; // 绑定的手机号
		private int logintimesofday; // 当天登录次数
		private int isgetbindtelaward; // 是否得到绑定手机奖励（0不能领取 1 已经领取过了2 可以领取了）
		private long bindteltime; // 绑定手机的时间
		private int isbindtelagain; // 是否需要重新绑定
		private int bindtelagainnum; // 重新绑定次数
		private java.util.LinkedList<Long> unbindteltime; // 解除绑定的时间点
		private String password; // 安全锁密码
		private long forcedelpdtime; // 强制删除密码时间
		private long forcedelendtime; // 强制删除密码结束时间
		private int errortimes; // 密码剩余输入错误次数
		private long lockendtime; // 锁定结束时间点
		private int isopensafelock; // 是否开启道具安全锁
		private long sumonlinetime; // 累积在线时长,ms,这个时长只有在下线的时候才会改变，其他情况还需要通过上次登录时间进行计算
		private int userid; // 帐号角色id
		private boolean addranklist; // 角色是否参与排行榜
		private long deletetime; // 角色被删除的时间,如果角色没有被删除,这个值为0
		private int lastloginip; // 
		private String platformuid; // userid在平台的唯一id
		private int viplevel; // vip等级
		private int energy; // 活力 by changhao
		private int playtimetimes; // 战斗播放时长倍数 10为1倍速 liuxinhua
		private int rolezonghemaxscore; // 人物综合实力历史最高评分 by changhao
		private int skillscore; // 技能评分 by changhao
		private int petscore; // 最强单宠物评分 by changhao
		private int manypetscore; // 多宠物评分 by changhao
		private int levelscore; // 等级评分 by changhao
		private int xiulianscroe; // 修炼评分 by changhao
		private int rolescore; // 人物评分 by changhao
		private int equipscore; // 装备评分 by changhao
		private long historymaxprofessioncontribute; // 历史最大职业贡献度 by changhao
		private int ride; // 当前坐骑 by changhao
		private long bestpetid; // 
		private int rolecolor1; // 染色方案1
		private int rolecolor2; // 染色方案2
		private java.util.LinkedList<xbean.RoleColorType> colorroomlist; // 衣橱
		private int cruise; // 巡游状态，默认0没有巡游，否则为巡游id
		private int cruise2; // 巡游2 by changhao
		private int cruise3; // 巡游3 by changhao
		private java.util.LinkedList<xbean.BattleAI> fighteai; // 主角ai
		private int equipeffect; // 当前装备特效
		private int pilot; // 新手引导项
		private java.util.HashMap<Integer, Integer> tupotips; // 突破任务是否提示过
		private java.util.HashMap<Integer, Integer> tupofinish; // 突破任务是否完成
		private int yingfuxishu; // 盈福转换系数
		private java.util.HashMap<Integer, String> depotnameinfo; // key=仓库id  value=名称
		private long expiretime; // 点卡结束时间(点卡服专用) by changhao
		private int firstprompt; // 还没提示过就是0(点卡服专用) by changhao
		private long lastchargetime; // 最后一次充值时间
		private java.util.HashMap<Integer, Integer> lineconfigmap; // 精英副本自动进入设置

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			rolename = "";
			usedname = new java.util.LinkedList<String>();
			title = -1;
			sp = 0;
			bfp = new BasicFightProperties.Data();
			addpointfp = new RoleAddPointProperties.Data();
			point = new java.util.HashMap<Integer, Integer>();
			scheme = 1;
			schemechanges = 0;
			fightpetkey = -1;
			sysconfigmap = new java.util.HashMap<Integer, Integer>();
			titles = new java.util.HashMap<Integer, xbean.TitleInfo>();
			formationsmap = new java.util.HashMap<Integer, xbean.FormBean>();
			clankey = -1;
			oldclankey = -1;
			exitstate = -1;
			onlinetime = -1;
			offlinetime = -1;
			logintimesofday = 0;
			isgetbindtelaward = 0;
			bindteltime = 0;
			isbindtelagain = 0;
			bindtelagainnum = 0;
			unbindteltime = new java.util.LinkedList<Long>();
			password = "";
			forcedelpdtime = 0;
			forcedelendtime = 0;
			errortimes = 5;
			isopensafelock = 0;
			addranklist = true;
			platformuid = "";
			playtimetimes = 10;
			rolezonghemaxscore = 0;
			skillscore = 0;
			petscore = 0;
			manypetscore = 0;
			levelscore = 0;
			xiulianscroe = 0;
			rolescore = 0;
			equipscore = 0;
			historymaxprofessioncontribute = 0;
			ride = 0;
			rolecolor1 = 101;
			rolecolor2 = 201;
			colorroomlist = new java.util.LinkedList<xbean.RoleColorType>();
			cruise = 0;
			cruise2 = 0;
			cruise3 = 0;
			fighteai = new java.util.LinkedList<xbean.BattleAI>();
			equipeffect = 0;
			pilot = 2;
			tupotips = new java.util.HashMap<Integer, Integer>();
			tupofinish = new java.util.HashMap<Integer, Integer>();
			yingfuxishu = 0;
			depotnameinfo = new java.util.HashMap<Integer, String>();
			lineconfigmap = new java.util.HashMap<Integer, Integer>();
		}

		Data(xbean.Properties _o1_) {
			if (_o1_ instanceof Properties) assign((Properties)_o1_);
			else if (_o1_ instanceof Properties.Data) assign((Properties.Data)_o1_);
			else if (_o1_ instanceof Properties.Const) assign(((Properties.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(Properties _o_) {
			rolename = _o_.rolename;
			usedname = new java.util.LinkedList<String>();
			usedname.addAll(_o_.usedname);
			level = _o_.level;
			shape = _o_.shape;
			sex = _o_.sex;
			exp = _o_.exp;
			race = _o_.race;
			school = _o_.school;
			color = _o_.color;
			title = _o_.title;
			hp = _o_.hp;
			uplimithp = _o_.uplimithp;
			wound = _o_.wound;
			mp = _o_.mp;
			sp = _o_.sp;
			bfp = new BasicFightProperties.Data(_o_.bfp);
			addpointfp = new RoleAddPointProperties.Data(_o_.addpointfp);
			point = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.point.entrySet())
				point.put(_e_.getKey(), _e_.getValue());
			scheme = _o_.scheme;
			schemechanges = _o_.schemechanges;
			schemechangetimes = _o_.schemechangetimes;
			allexp = _o_.allexp;
			sceneid = _o_.sceneid;
			posx = _o_.posx;
			posy = _o_.posy;
			posz = _o_.posz;
			laststaticsceneid = _o_.laststaticsceneid;
			laststaticposx = _o_.laststaticposx;
			laststaticposy = _o_.laststaticposy;
			laststaticposz = _o_.laststaticposz;
			fightpetkey = _o_.fightpetkey;
			showpetkey = _o_.showpetkey;
			sysconfigmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.sysconfigmap.entrySet())
				sysconfigmap.put(_e_.getKey(), _e_.getValue());
			titles = new java.util.HashMap<Integer, xbean.TitleInfo>();
			for (java.util.Map.Entry<Integer, xbean.TitleInfo> _e_ : _o_.titles.entrySet())
				titles.put(_e_.getKey(), new TitleInfo.Data(_e_.getValue()));
			defultform = _o_.defultform;
			formationsmap = new java.util.HashMap<Integer, xbean.FormBean>();
			for (java.util.Map.Entry<Integer, xbean.FormBean> _e_ : _o_.formationsmap.entrySet())
				formationsmap.put(_e_.getKey(), new FormBean.Data(_e_.getValue()));
			clankey = _o_.clankey;
			oldclankey = _o_.oldclankey;
			exitstate = _o_.exitstate;
			onlinetime = _o_.onlinetime;
			offlinetime = _o_.offlinetime;
			createtime = _o_.createtime;
			bindtel = _o_.bindtel;
			logintimesofday = _o_.logintimesofday;
			isgetbindtelaward = _o_.isgetbindtelaward;
			bindteltime = _o_.bindteltime;
			isbindtelagain = _o_.isbindtelagain;
			bindtelagainnum = _o_.bindtelagainnum;
			unbindteltime = new java.util.LinkedList<Long>();
			unbindteltime.addAll(_o_.unbindteltime);
			password = _o_.password;
			forcedelpdtime = _o_.forcedelpdtime;
			forcedelendtime = _o_.forcedelendtime;
			errortimes = _o_.errortimes;
			lockendtime = _o_.lockendtime;
			isopensafelock = _o_.isopensafelock;
			sumonlinetime = _o_.sumonlinetime;
			userid = _o_.userid;
			addranklist = _o_.addranklist;
			deletetime = _o_.deletetime;
			lastloginip = _o_.lastloginip;
			platformuid = _o_.platformuid;
			viplevel = _o_.viplevel;
			energy = _o_.energy;
			playtimetimes = _o_.playtimetimes;
			rolezonghemaxscore = _o_.rolezonghemaxscore;
			skillscore = _o_.skillscore;
			petscore = _o_.petscore;
			manypetscore = _o_.manypetscore;
			levelscore = _o_.levelscore;
			xiulianscroe = _o_.xiulianscroe;
			rolescore = _o_.rolescore;
			equipscore = _o_.equipscore;
			historymaxprofessioncontribute = _o_.historymaxprofessioncontribute;
			ride = _o_.ride;
			bestpetid = _o_.bestpetid;
			rolecolor1 = _o_.rolecolor1;
			rolecolor2 = _o_.rolecolor2;
			colorroomlist = new java.util.LinkedList<xbean.RoleColorType>();
			for (xbean.RoleColorType _v_ : _o_.colorroomlist)
				colorroomlist.add(new RoleColorType.Data(_v_));
			cruise = _o_.cruise;
			cruise2 = _o_.cruise2;
			cruise3 = _o_.cruise3;
			fighteai = new java.util.LinkedList<xbean.BattleAI>();
			for (xbean.BattleAI _v_ : _o_.fighteai)
				fighteai.add(new BattleAI.Data(_v_));
			equipeffect = _o_.equipeffect;
			pilot = _o_.pilot;
			tupotips = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tupotips.entrySet())
				tupotips.put(_e_.getKey(), _e_.getValue());
			tupofinish = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tupofinish.entrySet())
				tupofinish.put(_e_.getKey(), _e_.getValue());
			yingfuxishu = _o_.yingfuxishu;
			depotnameinfo = new java.util.HashMap<Integer, String>();
			for (java.util.Map.Entry<Integer, String> _e_ : _o_.depotnameinfo.entrySet())
				depotnameinfo.put(_e_.getKey(), _e_.getValue());
			expiretime = _o_.expiretime;
			firstprompt = _o_.firstprompt;
			lastchargetime = _o_.lastchargetime;
			lineconfigmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.lineconfigmap.entrySet())
				lineconfigmap.put(_e_.getKey(), _e_.getValue());
		}

		private void assign(Properties.Data _o_) {
			rolename = _o_.rolename;
			usedname = new java.util.LinkedList<String>();
			usedname.addAll(_o_.usedname);
			level = _o_.level;
			shape = _o_.shape;
			sex = _o_.sex;
			exp = _o_.exp;
			race = _o_.race;
			school = _o_.school;
			color = _o_.color;
			title = _o_.title;
			hp = _o_.hp;
			uplimithp = _o_.uplimithp;
			wound = _o_.wound;
			mp = _o_.mp;
			sp = _o_.sp;
			bfp = new BasicFightProperties.Data(_o_.bfp);
			addpointfp = new RoleAddPointProperties.Data(_o_.addpointfp);
			point = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.point.entrySet())
				point.put(_e_.getKey(), _e_.getValue());
			scheme = _o_.scheme;
			schemechanges = _o_.schemechanges;
			schemechangetimes = _o_.schemechangetimes;
			allexp = _o_.allexp;
			sceneid = _o_.sceneid;
			posx = _o_.posx;
			posy = _o_.posy;
			posz = _o_.posz;
			laststaticsceneid = _o_.laststaticsceneid;
			laststaticposx = _o_.laststaticposx;
			laststaticposy = _o_.laststaticposy;
			laststaticposz = _o_.laststaticposz;
			fightpetkey = _o_.fightpetkey;
			showpetkey = _o_.showpetkey;
			sysconfigmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.sysconfigmap.entrySet())
				sysconfigmap.put(_e_.getKey(), _e_.getValue());
			titles = new java.util.HashMap<Integer, xbean.TitleInfo>();
			for (java.util.Map.Entry<Integer, xbean.TitleInfo> _e_ : _o_.titles.entrySet())
				titles.put(_e_.getKey(), new TitleInfo.Data(_e_.getValue()));
			defultform = _o_.defultform;
			formationsmap = new java.util.HashMap<Integer, xbean.FormBean>();
			for (java.util.Map.Entry<Integer, xbean.FormBean> _e_ : _o_.formationsmap.entrySet())
				formationsmap.put(_e_.getKey(), new FormBean.Data(_e_.getValue()));
			clankey = _o_.clankey;
			oldclankey = _o_.oldclankey;
			exitstate = _o_.exitstate;
			onlinetime = _o_.onlinetime;
			offlinetime = _o_.offlinetime;
			createtime = _o_.createtime;
			bindtel = _o_.bindtel;
			logintimesofday = _o_.logintimesofday;
			isgetbindtelaward = _o_.isgetbindtelaward;
			bindteltime = _o_.bindteltime;
			isbindtelagain = _o_.isbindtelagain;
			bindtelagainnum = _o_.bindtelagainnum;
			unbindteltime = new java.util.LinkedList<Long>();
			unbindteltime.addAll(_o_.unbindteltime);
			password = _o_.password;
			forcedelpdtime = _o_.forcedelpdtime;
			forcedelendtime = _o_.forcedelendtime;
			errortimes = _o_.errortimes;
			lockendtime = _o_.lockendtime;
			isopensafelock = _o_.isopensafelock;
			sumonlinetime = _o_.sumonlinetime;
			userid = _o_.userid;
			addranklist = _o_.addranklist;
			deletetime = _o_.deletetime;
			lastloginip = _o_.lastloginip;
			platformuid = _o_.platformuid;
			viplevel = _o_.viplevel;
			energy = _o_.energy;
			playtimetimes = _o_.playtimetimes;
			rolezonghemaxscore = _o_.rolezonghemaxscore;
			skillscore = _o_.skillscore;
			petscore = _o_.petscore;
			manypetscore = _o_.manypetscore;
			levelscore = _o_.levelscore;
			xiulianscroe = _o_.xiulianscroe;
			rolescore = _o_.rolescore;
			equipscore = _o_.equipscore;
			historymaxprofessioncontribute = _o_.historymaxprofessioncontribute;
			ride = _o_.ride;
			bestpetid = _o_.bestpetid;
			rolecolor1 = _o_.rolecolor1;
			rolecolor2 = _o_.rolecolor2;
			colorroomlist = new java.util.LinkedList<xbean.RoleColorType>();
			for (xbean.RoleColorType _v_ : _o_.colorroomlist)
				colorroomlist.add(new RoleColorType.Data(_v_));
			cruise = _o_.cruise;
			cruise2 = _o_.cruise2;
			cruise3 = _o_.cruise3;
			fighteai = new java.util.LinkedList<xbean.BattleAI>();
			for (xbean.BattleAI _v_ : _o_.fighteai)
				fighteai.add(new BattleAI.Data(_v_));
			equipeffect = _o_.equipeffect;
			pilot = _o_.pilot;
			tupotips = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tupotips.entrySet())
				tupotips.put(_e_.getKey(), _e_.getValue());
			tupofinish = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.tupofinish.entrySet())
				tupofinish.put(_e_.getKey(), _e_.getValue());
			yingfuxishu = _o_.yingfuxishu;
			depotnameinfo = new java.util.HashMap<Integer, String>();
			for (java.util.Map.Entry<Integer, String> _e_ : _o_.depotnameinfo.entrySet())
				depotnameinfo.put(_e_.getKey(), _e_.getValue());
			expiretime = _o_.expiretime;
			firstprompt = _o_.firstprompt;
			lastchargetime = _o_.lastchargetime;
			lineconfigmap = new java.util.HashMap<Integer, Integer>();
			for (java.util.Map.Entry<Integer, Integer> _e_ : _o_.lineconfigmap.entrySet())
				lineconfigmap.put(_e_.getKey(), _e_.getValue());
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(rolename, mkdb.Const.IO_CHARSET);
			_os_.compact_uint32(usedname.size());
			for (String _v_ : usedname) {
				_os_.marshal(_v_, mkdb.Const.IO_CHARSET);
			}
			_os_.marshal(level);
			_os_.marshal(shape);
			_os_.marshal(sex);
			_os_.marshal(exp);
			_os_.marshal(race);
			_os_.marshal(school);
			_os_.marshal(color);
			_os_.marshal(title);
			_os_.marshal(hp);
			_os_.marshal(uplimithp);
			_os_.marshal(wound);
			_os_.marshal(mp);
			_os_.marshal(sp);
			bfp.marshal(_os_);
			addpointfp.marshal(_os_);
			_os_.compact_uint32(point.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : point.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(scheme);
			_os_.marshal(schemechanges);
			_os_.marshal(schemechangetimes);
			_os_.marshal(allexp);
			_os_.marshal(sceneid);
			_os_.marshal(posx);
			_os_.marshal(posy);
			_os_.marshal(posz);
			_os_.marshal(laststaticsceneid);
			_os_.marshal(laststaticposx);
			_os_.marshal(laststaticposy);
			_os_.marshal(laststaticposz);
			_os_.marshal(fightpetkey);
			_os_.marshal(showpetkey);
			_os_.compact_uint32(sysconfigmap.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : sysconfigmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(titles.size());
			for (java.util.Map.Entry<Integer, xbean.TitleInfo> _e_ : titles.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(defultform);
			_os_.compact_uint32(formationsmap.size());
			for (java.util.Map.Entry<Integer, xbean.FormBean> _e_ : formationsmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_e_.getValue().marshal(_os_);
			}
			_os_.marshal(clankey);
			_os_.marshal(oldclankey);
			_os_.marshal(exitstate);
			_os_.marshal(onlinetime);
			_os_.marshal(offlinetime);
			_os_.marshal(createtime);
			_os_.marshal(bindtel);
			_os_.marshal(logintimesofday);
			_os_.marshal(isgetbindtelaward);
			_os_.marshal(bindteltime);
			_os_.marshal(isbindtelagain);
			_os_.marshal(bindtelagainnum);
			_os_.compact_uint32(unbindteltime.size());
			for (Long _v_ : unbindteltime) {
				_os_.marshal(_v_);
			}
			_os_.marshal(password, mkdb.Const.IO_CHARSET);
			_os_.marshal(forcedelpdtime);
			_os_.marshal(forcedelendtime);
			_os_.marshal(errortimes);
			_os_.marshal(lockendtime);
			_os_.marshal(isopensafelock);
			_os_.marshal(sumonlinetime);
			_os_.marshal(userid);
			_os_.marshal(addranklist);
			_os_.marshal(deletetime);
			_os_.marshal(lastloginip);
			_os_.marshal(platformuid, mkdb.Const.IO_CHARSET);
			_os_.marshal(viplevel);
			_os_.marshal(energy);
			_os_.marshal(playtimetimes);
			_os_.marshal(rolezonghemaxscore);
			_os_.marshal(skillscore);
			_os_.marshal(petscore);
			_os_.marshal(manypetscore);
			_os_.marshal(levelscore);
			_os_.marshal(xiulianscroe);
			_os_.marshal(rolescore);
			_os_.marshal(equipscore);
			_os_.marshal(historymaxprofessioncontribute);
			_os_.marshal(ride);
			_os_.marshal(bestpetid);
			_os_.marshal(rolecolor1);
			_os_.marshal(rolecolor2);
			_os_.compact_uint32(colorroomlist.size());
			for (xbean.RoleColorType _v_ : colorroomlist) {
				_v_.marshal(_os_);
			}
			_os_.marshal(cruise);
			_os_.marshal(cruise2);
			_os_.marshal(cruise3);
			_os_.compact_uint32(fighteai.size());
			for (xbean.BattleAI _v_ : fighteai) {
				_v_.marshal(_os_);
			}
			_os_.marshal(equipeffect);
			_os_.marshal(pilot);
			_os_.compact_uint32(tupotips.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : tupotips.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.compact_uint32(tupofinish.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : tupofinish.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			_os_.marshal(yingfuxishu);
			_os_.compact_uint32(depotnameinfo.size());
			for (java.util.Map.Entry<Integer, String> _e_ : depotnameinfo.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue(), mkdb.Const.IO_CHARSET);
			}
			_os_.marshal(expiretime);
			_os_.marshal(firstprompt);
			_os_.marshal(lastchargetime);
			_os_.compact_uint32(lineconfigmap.size());
			for (java.util.Map.Entry<Integer, Integer> _e_ : lineconfigmap.entrySet())
			{
				_os_.marshal(_e_.getKey());
				_os_.marshal(_e_.getValue());
			}
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			rolename = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				String _v_ = "";
				_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
				usedname.add(_v_);
			}
			level = _os_.unmarshal_int();
			shape = _os_.unmarshal_int();
			sex = _os_.unmarshal_int();
			exp = _os_.unmarshal_long();
			race = _os_.unmarshal_int();
			school = _os_.unmarshal_int();
			color = _os_.unmarshal_int();
			title = _os_.unmarshal_int();
			hp = _os_.unmarshal_int();
			uplimithp = _os_.unmarshal_int();
			wound = _os_.unmarshal_int();
			mp = _os_.unmarshal_int();
			sp = _os_.unmarshal_int();
			bfp.unmarshal(_os_);
			addpointfp.unmarshal(_os_);
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					point = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					point.put(_k_, _v_);
				}
			}
			scheme = _os_.unmarshal_int();
			schemechanges = _os_.unmarshal_int();
			schemechangetimes = _os_.unmarshal_long();
			allexp = _os_.unmarshal_long();
			sceneid = _os_.unmarshal_long();
			posx = _os_.unmarshal_int();
			posy = _os_.unmarshal_int();
			posz = _os_.unmarshal_int();
			laststaticsceneid = _os_.unmarshal_long();
			laststaticposx = _os_.unmarshal_int();
			laststaticposy = _os_.unmarshal_int();
			laststaticposz = _os_.unmarshal_int();
			fightpetkey = _os_.unmarshal_int();
			showpetkey = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					sysconfigmap = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					sysconfigmap.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					titles = new java.util.HashMap<Integer, xbean.TitleInfo>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.TitleInfo _v_ = xbean.Pod.newTitleInfoData();
					_v_.unmarshal(_os_);
					titles.put(_k_, _v_);
				}
			}
			defultform = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					formationsmap = new java.util.HashMap<Integer, xbean.FormBean>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					xbean.FormBean _v_ = xbean.Pod.newFormBeanData();
					_v_.unmarshal(_os_);
					formationsmap.put(_k_, _v_);
				}
			}
			clankey = _os_.unmarshal_long();
			oldclankey = _os_.unmarshal_long();
			exitstate = _os_.unmarshal_int();
			onlinetime = _os_.unmarshal_long();
			offlinetime = _os_.unmarshal_long();
			createtime = _os_.unmarshal_long();
			bindtel = _os_.unmarshal_long();
			logintimesofday = _os_.unmarshal_int();
			isgetbindtelaward = _os_.unmarshal_int();
			bindteltime = _os_.unmarshal_long();
			isbindtelagain = _os_.unmarshal_int();
			bindtelagainnum = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				long _v_ = 0;
				_v_ = _os_.unmarshal_long();
				unbindteltime.add(_v_);
			}
			password = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			forcedelpdtime = _os_.unmarshal_long();
			forcedelendtime = _os_.unmarshal_long();
			errortimes = _os_.unmarshal_int();
			lockendtime = _os_.unmarshal_long();
			isopensafelock = _os_.unmarshal_int();
			sumonlinetime = _os_.unmarshal_long();
			userid = _os_.unmarshal_int();
			addranklist = _os_.unmarshal_boolean();
			deletetime = _os_.unmarshal_long();
			lastloginip = _os_.unmarshal_int();
			platformuid = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			viplevel = _os_.unmarshal_int();
			energy = _os_.unmarshal_int();
			playtimetimes = _os_.unmarshal_int();
			rolezonghemaxscore = _os_.unmarshal_int();
			skillscore = _os_.unmarshal_int();
			petscore = _os_.unmarshal_int();
			manypetscore = _os_.unmarshal_int();
			levelscore = _os_.unmarshal_int();
			xiulianscroe = _os_.unmarshal_int();
			rolescore = _os_.unmarshal_int();
			equipscore = _os_.unmarshal_int();
			historymaxprofessioncontribute = _os_.unmarshal_long();
			ride = _os_.unmarshal_int();
			bestpetid = _os_.unmarshal_long();
			rolecolor1 = _os_.unmarshal_int();
			rolecolor2 = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.RoleColorType _v_ = xbean.Pod.newRoleColorTypeData();
				_v_.unmarshal(_os_);
				colorroomlist.add(_v_);
			}
			cruise = _os_.unmarshal_int();
			cruise2 = _os_.unmarshal_int();
			cruise3 = _os_.unmarshal_int();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				xbean.BattleAI _v_ = xbean.Pod.newBattleAIData();
				_v_.unmarshal(_os_);
				fighteai.add(_v_);
			}
			equipeffect = _os_.unmarshal_int();
			pilot = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					tupotips = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					tupotips.put(_k_, _v_);
				}
			}
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					tupofinish = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					tupofinish.put(_k_, _v_);
				}
			}
			yingfuxishu = _os_.unmarshal_int();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					depotnameinfo = new java.util.HashMap<Integer, String>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					String _v_ = "";
					_v_ = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
					depotnameinfo.put(_k_, _v_);
				}
			}
			expiretime = _os_.unmarshal_long();
			firstprompt = _os_.unmarshal_int();
			lastchargetime = _os_.unmarshal_long();
			{
				int size = _os_.uncompact_uint32();
				if (size >= 12) { // {java.util.HashMap} 16 * 0.75 = 12
					lineconfigmap = new java.util.HashMap<Integer, Integer>(size * 2);
				}
				for (; size > 0; --size)
				{
					int _k_ = 0;
					_k_ = _os_.unmarshal_int();
					int _v_ = 0;
					_v_ = _os_.unmarshal_int();
					lineconfigmap.put(_k_, _v_);
				}
			}
			return _os_;
		}

		@Override
		public xbean.Properties copy() {
			return new Data(this);
		}

		@Override
		public xbean.Properties toData() {
			return new Data(this);
		}

		public xbean.Properties toBean() {
			return new Properties(this, null, null);
		}

		@Override
		public xbean.Properties toDataIf() {
			return this;
		}

		public xbean.Properties toBeanIf() {
			return new Properties(this, null, null);
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
		public String getRolename() { // 角色名
			return rolename;
		}

		@Override
		public com.locojoy.base.Octets getRolenameOctets() { // 角色名
			return com.locojoy.base.Octets.wrap(getRolename(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public java.util.List<String> getUsedname() { // 曾用名
			return usedname;
		}

		@Override
		public java.util.List<String> getUsednameAsData() { // 曾用名
			return usedname;
		}

		@Override
		public int getLevel() { // 等级
			return level;
		}

		@Override
		public int getShape() { // 主角ID(是指游戏中设定的10个角色id)
			return shape;
		}

		@Override
		public int getSex() { // 人物性别
			return sex;
		}

		@Override
		public long getExp() { // 当前经验
			return exp;
		}

		@Override
		public int getRace() { // 种族
			return race;
		}

		@Override
		public int getSchool() { // 职业
			return school;
		}

		@Override
		public int getColor() { // 配色方案
			return color;
		}

		@Override
		public int getTitle() { // 称谓
			return title;
		}

		@Override
		public int getHp() { // 气血
			return hp;
		}

		@Override
		public int getUplimithp() { // 当前气血上限
			return uplimithp;
		}

		@Override
		public int getWound() { // 伤，当前血上限=最大血上限-伤，伤：[0,MaxHp-1]
			return wound;
		}

		@Override
		public int getMp() { // 法力
			return mp;
		}

		@Override
		public int getSp() { // 怒气
			return sp;
		}

		@Override
		public xbean.BasicFightProperties getBfp() { // 存储基础的点数
			return bfp;
		}

		@Override
		public xbean.RoleAddPointProperties getAddpointfp() { // 人物加点方案
			return addpointfp;
		}

		@Override
		public java.util.Map<Integer, Integer> getPoint() { // 潜能。未分配点数
			return point;
		}

		@Override
		public java.util.Map<Integer, Integer> getPointAsData() { // 潜能。未分配点数
			return point;
		}

		@Override
		public int getScheme() { // 人物加点方案
			return scheme;
		}

		@Override
		public int getSchemechanges() { // 人物加点方案切换次数
			return schemechanges;
		}

		@Override
		public long getSchemechangetimes() { // 人物加点方案切换最后时间
			return schemechangetimes;
		}

		@Override
		public long getAllexp() { // 玩家出生后获得所有经验
			return allexp;
		}

		@Override
		public long getSceneid() { // 角色所在场景ID
			return sceneid;
		}

		@Override
		public int getPosx() { // 坐标x
			return posx;
		}

		@Override
		public int getPosy() { // 坐标y
			return posy;
		}

		@Override
		public int getPosz() { // 坐标z
			return posz;
		}

		@Override
		public long getLaststaticsceneid() { // 最后一次玩家所在的静态场景ID
			return laststaticsceneid;
		}

		@Override
		public int getLaststaticposx() { // 最后一次玩家所在的静态场景x
			return laststaticposx;
		}

		@Override
		public int getLaststaticposy() { // 最后一次玩家所在的静态场景y
			return laststaticposy;
		}

		@Override
		public int getLaststaticposz() { // 最后一次玩家所在的静态场景z
			return laststaticposz;
		}

		@Override
		public int getFightpetkey() { // 当前战斗宠物
			return fightpetkey;
		}

		@Override
		public int getShowpetkey() { // 当前展示宠物
			return showpetkey;
		}

		@Override
		public java.util.Map<Integer, Integer> getSysconfigmap() { // 系统设置
			return sysconfigmap;
		}

		@Override
		public java.util.Map<Integer, Integer> getSysconfigmapAsData() { // 系统设置
			return sysconfigmap;
		}

		@Override
		public java.util.Map<Integer, xbean.TitleInfo> getTitles() { // 拥有的称谓列表
			return titles;
		}

		@Override
		public java.util.Map<Integer, xbean.TitleInfo> getTitlesAsData() { // 拥有的称谓列表
			return titles;
		}

		@Override
		public int getDefultform() { // 默认光环
			return defultform;
		}

		@Override
		public java.util.Map<Integer, xbean.FormBean> getFormationsmap() { // key=光环id  value=光环等级
			return formationsmap;
		}

		@Override
		public java.util.Map<Integer, xbean.FormBean> getFormationsmapAsData() { // key=光环id  value=光环等级
			return formationsmap;
		}

		@Override
		public long getClankey() { // 角色对应公会的key
			return clankey;
		}

		@Override
		public long getOldclankey() { // 角色对应的上次公会key
			return oldclankey;
		}

		@Override
		public int getExitstate() { // 角色退出公会方式，0主动退出    1被踢出    2解散          key by hzl
			return exitstate;
		}

		@Override
		public long getOnlinetime() { // 角色上次登录时间
			return onlinetime;
		}

		@Override
		public long getOfflinetime() { // 角色上次离线时间
			return offlinetime;
		}

		@Override
		public long getCreatetime() { // 角色创建时间
			return createtime;
		}

		@Override
		public long getBindtel() { // 绑定的手机号
			return bindtel;
		}

		@Override
		public int getLogintimesofday() { // 当天登录次数
			return logintimesofday;
		}

		@Override
		public int getIsgetbindtelaward() { // 是否得到绑定手机奖励（0不能领取 1 已经领取过了2 可以领取了）
			return isgetbindtelaward;
		}

		@Override
		public long getBindteltime() { // 绑定手机的时间
			return bindteltime;
		}

		@Override
		public int getIsbindtelagain() { // 是否需要重新绑定
			return isbindtelagain;
		}

		@Override
		public int getBindtelagainnum() { // 重新绑定次数
			return bindtelagainnum;
		}

		@Override
		public java.util.List<Long> getUnbindteltime() { // 解除绑定的时间点
			return unbindteltime;
		}

		@Override
		public java.util.List<Long> getUnbindteltimeAsData() { // 解除绑定的时间点
			return unbindteltime;
		}

		@Override
		public String getPassword() { // 安全锁密码
			return password;
		}

		@Override
		public com.locojoy.base.Octets getPasswordOctets() { // 安全锁密码
			return com.locojoy.base.Octets.wrap(getPassword(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getForcedelpdtime() { // 强制删除密码时间
			return forcedelpdtime;
		}

		@Override
		public long getForcedelendtime() { // 强制删除密码结束时间
			return forcedelendtime;
		}

		@Override
		public int getErrortimes() { // 密码剩余输入错误次数
			return errortimes;
		}

		@Override
		public long getLockendtime() { // 锁定结束时间点
			return lockendtime;
		}

		@Override
		public int getIsopensafelock() { // 是否开启道具安全锁
			return isopensafelock;
		}

		@Override
		public long getSumonlinetime() { // 累积在线时长,ms,这个时长只有在下线的时候才会改变，其他情况还需要通过上次登录时间进行计算
			return sumonlinetime;
		}

		@Override
		public int getUserid() { // 帐号角色id
			return userid;
		}

		@Override
		public boolean getAddranklist() { // 角色是否参与排行榜
			return addranklist;
		}

		@Override
		public long getDeletetime() { // 角色被删除的时间,如果角色没有被删除,这个值为0
			return deletetime;
		}

		@Override
		public int getLastloginip() { // 
			return lastloginip;
		}

		@Override
		public String getPlatformuid() { // userid在平台的唯一id
			return platformuid;
		}

		@Override
		public com.locojoy.base.Octets getPlatformuidOctets() { // userid在平台的唯一id
			return com.locojoy.base.Octets.wrap(getPlatformuid(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public int getViplevel() { // vip等级
			return viplevel;
		}

		@Override
		public int getEnergy() { // 活力 by changhao
			return energy;
		}

		@Override
		public int getPlaytimetimes() { // 战斗播放时长倍数 10为1倍速 liuxinhua
			return playtimetimes;
		}

		@Override
		public int getRolezonghemaxscore() { // 人物综合实力历史最高评分 by changhao
			return rolezonghemaxscore;
		}

		@Override
		public int getSkillscore() { // 技能评分 by changhao
			return skillscore;
		}

		@Override
		public int getPetscore() { // 最强单宠物评分 by changhao
			return petscore;
		}

		@Override
		public int getManypetscore() { // 多宠物评分 by changhao
			return manypetscore;
		}

		@Override
		public int getLevelscore() { // 等级评分 by changhao
			return levelscore;
		}

		@Override
		public int getXiulianscroe() { // 修炼评分 by changhao
			return xiulianscroe;
		}

		@Override
		public int getRolescore() { // 人物评分 by changhao
			return rolescore;
		}

		@Override
		public int getEquipscore() { // 装备评分 by changhao
			return equipscore;
		}

		@Override
		public long getHistorymaxprofessioncontribute() { // 历史最大职业贡献度 by changhao
			return historymaxprofessioncontribute;
		}

		@Override
		public int getRide() { // 当前坐骑 by changhao
			return ride;
		}

		@Override
		public long getBestpetid() { // 
			return bestpetid;
		}

		@Override
		public int getRolecolor1() { // 染色方案1
			return rolecolor1;
		}

		@Override
		public int getRolecolor2() { // 染色方案2
			return rolecolor2;
		}

		@Override
		public java.util.List<xbean.RoleColorType> getColorroomlist() { // 衣橱
			return colorroomlist;
		}

		@Override
		public java.util.List<xbean.RoleColorType> getColorroomlistAsData() { // 衣橱
			return colorroomlist;
		}

		@Override
		public int getCruise() { // 巡游状态，默认0没有巡游，否则为巡游id
			return cruise;
		}

		@Override
		public int getCruise2() { // 巡游2 by changhao
			return cruise2;
		}

		@Override
		public int getCruise3() { // 巡游3 by changhao
			return cruise3;
		}

		@Override
		public java.util.List<xbean.BattleAI> getFighteai() { // 主角ai
			return fighteai;
		}

		@Override
		public java.util.List<xbean.BattleAI> getFighteaiAsData() { // 主角ai
			return fighteai;
		}

		@Override
		public int getEquipeffect() { // 当前装备特效
			return equipeffect;
		}

		@Override
		public int getPilot() { // 新手引导项
			return pilot;
		}

		@Override
		public java.util.Map<Integer, Integer> getTupotips() { // 突破任务是否提示过
			return tupotips;
		}

		@Override
		public java.util.Map<Integer, Integer> getTupotipsAsData() { // 突破任务是否提示过
			return tupotips;
		}

		@Override
		public java.util.Map<Integer, Integer> getTupofinish() { // 突破任务是否完成
			return tupofinish;
		}

		@Override
		public java.util.Map<Integer, Integer> getTupofinishAsData() { // 突破任务是否完成
			return tupofinish;
		}

		@Override
		public int getYingfuxishu() { // 盈福转换系数
			return yingfuxishu;
		}

		@Override
		public java.util.Map<Integer, String> getDepotnameinfo() { // key=仓库id  value=名称
			return depotnameinfo;
		}

		@Override
		public java.util.Map<Integer, String> getDepotnameinfoAsData() { // key=仓库id  value=名称
			return depotnameinfo;
		}

		@Override
		public long getExpiretime() { // 点卡结束时间(点卡服专用) by changhao
			return expiretime;
		}

		@Override
		public int getFirstprompt() { // 还没提示过就是0(点卡服专用) by changhao
			return firstprompt;
		}

		@Override
		public long getLastchargetime() { // 最后一次充值时间
			return lastchargetime;
		}

		@Override
		public java.util.Map<Integer, Integer> getLineconfigmap() { // 精英副本自动进入设置
			return lineconfigmap;
		}

		@Override
		public java.util.Map<Integer, Integer> getLineconfigmapAsData() { // 精英副本自动进入设置
			return lineconfigmap;
		}

		@Override
		public void setRolename(String _v_) { // 角色名
			if (null == _v_)
				throw new NullPointerException();
			rolename = _v_;
		}

		@Override
		public void setRolenameOctets(com.locojoy.base.Octets _v_) { // 角色名
			this.setRolename(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setLevel(int _v_) { // 等级
			level = _v_;
		}

		@Override
		public void setShape(int _v_) { // 主角ID(是指游戏中设定的10个角色id)
			shape = _v_;
		}

		@Override
		public void setSex(int _v_) { // 人物性别
			sex = _v_;
		}

		@Override
		public void setExp(long _v_) { // 当前经验
			exp = _v_;
		}

		@Override
		public void setRace(int _v_) { // 种族
			race = _v_;
		}

		@Override
		public void setSchool(int _v_) { // 职业
			school = _v_;
		}

		@Override
		public void setColor(int _v_) { // 配色方案
			color = _v_;
		}

		@Override
		public void setTitle(int _v_) { // 称谓
			title = _v_;
		}

		@Override
		public void setHp(int _v_) { // 气血
			hp = _v_;
		}

		@Override
		public void setUplimithp(int _v_) { // 当前气血上限
			uplimithp = _v_;
		}

		@Override
		public void setWound(int _v_) { // 伤，当前血上限=最大血上限-伤，伤：[0,MaxHp-1]
			wound = _v_;
		}

		@Override
		public void setMp(int _v_) { // 法力
			mp = _v_;
		}

		@Override
		public void setSp(int _v_) { // 怒气
			sp = _v_;
		}

		@Override
		public void setScheme(int _v_) { // 人物加点方案
			scheme = _v_;
		}

		@Override
		public void setSchemechanges(int _v_) { // 人物加点方案切换次数
			schemechanges = _v_;
		}

		@Override
		public void setSchemechangetimes(long _v_) { // 人物加点方案切换最后时间
			schemechangetimes = _v_;
		}

		@Override
		public void setAllexp(long _v_) { // 玩家出生后获得所有经验
			allexp = _v_;
		}

		@Override
		public void setSceneid(long _v_) { // 角色所在场景ID
			sceneid = _v_;
		}

		@Override
		public void setPosx(int _v_) { // 坐标x
			posx = _v_;
		}

		@Override
		public void setPosy(int _v_) { // 坐标y
			posy = _v_;
		}

		@Override
		public void setPosz(int _v_) { // 坐标z
			posz = _v_;
		}

		@Override
		public void setLaststaticsceneid(long _v_) { // 最后一次玩家所在的静态场景ID
			laststaticsceneid = _v_;
		}

		@Override
		public void setLaststaticposx(int _v_) { // 最后一次玩家所在的静态场景x
			laststaticposx = _v_;
		}

		@Override
		public void setLaststaticposy(int _v_) { // 最后一次玩家所在的静态场景y
			laststaticposy = _v_;
		}

		@Override
		public void setLaststaticposz(int _v_) { // 最后一次玩家所在的静态场景z
			laststaticposz = _v_;
		}

		@Override
		public void setFightpetkey(int _v_) { // 当前战斗宠物
			fightpetkey = _v_;
		}

		@Override
		public void setShowpetkey(int _v_) { // 当前展示宠物
			showpetkey = _v_;
		}

		@Override
		public void setDefultform(int _v_) { // 默认光环
			defultform = _v_;
		}

		@Override
		public void setClankey(long _v_) { // 角色对应公会的key
			clankey = _v_;
		}

		@Override
		public void setOldclankey(long _v_) { // 角色对应的上次公会key
			oldclankey = _v_;
		}

		@Override
		public void setExitstate(int _v_) { // 角色退出公会方式，0主动退出    1被踢出    2解散          key by hzl
			exitstate = _v_;
		}

		@Override
		public void setOnlinetime(long _v_) { // 角色上次登录时间
			onlinetime = _v_;
		}

		@Override
		public void setOfflinetime(long _v_) { // 角色上次离线时间
			offlinetime = _v_;
		}

		@Override
		public void setCreatetime(long _v_) { // 角色创建时间
			createtime = _v_;
		}

		@Override
		public void setBindtel(long _v_) { // 绑定的手机号
			bindtel = _v_;
		}

		@Override
		public void setLogintimesofday(int _v_) { // 当天登录次数
			logintimesofday = _v_;
		}

		@Override
		public void setIsgetbindtelaward(int _v_) { // 是否得到绑定手机奖励（0不能领取 1 已经领取过了2 可以领取了）
			isgetbindtelaward = _v_;
		}

		@Override
		public void setBindteltime(long _v_) { // 绑定手机的时间
			bindteltime = _v_;
		}

		@Override
		public void setIsbindtelagain(int _v_) { // 是否需要重新绑定
			isbindtelagain = _v_;
		}

		@Override
		public void setBindtelagainnum(int _v_) { // 重新绑定次数
			bindtelagainnum = _v_;
		}

		@Override
		public void setPassword(String _v_) { // 安全锁密码
			if (null == _v_)
				throw new NullPointerException();
			password = _v_;
		}

		@Override
		public void setPasswordOctets(com.locojoy.base.Octets _v_) { // 安全锁密码
			this.setPassword(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setForcedelpdtime(long _v_) { // 强制删除密码时间
			forcedelpdtime = _v_;
		}

		@Override
		public void setForcedelendtime(long _v_) { // 强制删除密码结束时间
			forcedelendtime = _v_;
		}

		@Override
		public void setErrortimes(int _v_) { // 密码剩余输入错误次数
			errortimes = _v_;
		}

		@Override
		public void setLockendtime(long _v_) { // 锁定结束时间点
			lockendtime = _v_;
		}

		@Override
		public void setIsopensafelock(int _v_) { // 是否开启道具安全锁
			isopensafelock = _v_;
		}

		@Override
		public void setSumonlinetime(long _v_) { // 累积在线时长,ms,这个时长只有在下线的时候才会改变，其他情况还需要通过上次登录时间进行计算
			sumonlinetime = _v_;
		}

		@Override
		public void setUserid(int _v_) { // 帐号角色id
			userid = _v_;
		}

		@Override
		public void setAddranklist(boolean _v_) { // 角色是否参与排行榜
			addranklist = _v_;
		}

		@Override
		public void setDeletetime(long _v_) { // 角色被删除的时间,如果角色没有被删除,这个值为0
			deletetime = _v_;
		}

		@Override
		public void setLastloginip(int _v_) { // 
			lastloginip = _v_;
		}

		@Override
		public void setPlatformuid(String _v_) { // userid在平台的唯一id
			if (null == _v_)
				throw new NullPointerException();
			platformuid = _v_;
		}

		@Override
		public void setPlatformuidOctets(com.locojoy.base.Octets _v_) { // userid在平台的唯一id
			this.setPlatformuid(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setViplevel(int _v_) { // vip等级
			viplevel = _v_;
		}

		@Override
		public void setEnergy(int _v_) { // 活力 by changhao
			energy = _v_;
		}

		@Override
		public void setPlaytimetimes(int _v_) { // 战斗播放时长倍数 10为1倍速 liuxinhua
			playtimetimes = _v_;
		}

		@Override
		public void setRolezonghemaxscore(int _v_) { // 人物综合实力历史最高评分 by changhao
			rolezonghemaxscore = _v_;
		}

		@Override
		public void setSkillscore(int _v_) { // 技能评分 by changhao
			skillscore = _v_;
		}

		@Override
		public void setPetscore(int _v_) { // 最强单宠物评分 by changhao
			petscore = _v_;
		}

		@Override
		public void setManypetscore(int _v_) { // 多宠物评分 by changhao
			manypetscore = _v_;
		}

		@Override
		public void setLevelscore(int _v_) { // 等级评分 by changhao
			levelscore = _v_;
		}

		@Override
		public void setXiulianscroe(int _v_) { // 修炼评分 by changhao
			xiulianscroe = _v_;
		}

		@Override
		public void setRolescore(int _v_) { // 人物评分 by changhao
			rolescore = _v_;
		}

		@Override
		public void setEquipscore(int _v_) { // 装备评分 by changhao
			equipscore = _v_;
		}

		@Override
		public void setHistorymaxprofessioncontribute(long _v_) { // 历史最大职业贡献度 by changhao
			historymaxprofessioncontribute = _v_;
		}

		@Override
		public void setRide(int _v_) { // 当前坐骑 by changhao
			ride = _v_;
		}

		@Override
		public void setBestpetid(long _v_) { // 
			bestpetid = _v_;
		}

		@Override
		public void setRolecolor1(int _v_) { // 染色方案1
			rolecolor1 = _v_;
		}

		@Override
		public void setRolecolor2(int _v_) { // 染色方案2
			rolecolor2 = _v_;
		}

		@Override
		public void setCruise(int _v_) { // 巡游状态，默认0没有巡游，否则为巡游id
			cruise = _v_;
		}

		@Override
		public void setCruise2(int _v_) { // 巡游2 by changhao
			cruise2 = _v_;
		}

		@Override
		public void setCruise3(int _v_) { // 巡游3 by changhao
			cruise3 = _v_;
		}

		@Override
		public void setEquipeffect(int _v_) { // 当前装备特效
			equipeffect = _v_;
		}

		@Override
		public void setPilot(int _v_) { // 新手引导项
			pilot = _v_;
		}

		@Override
		public void setYingfuxishu(int _v_) { // 盈福转换系数
			yingfuxishu = _v_;
		}

		@Override
		public void setExpiretime(long _v_) { // 点卡结束时间(点卡服专用) by changhao
			expiretime = _v_;
		}

		@Override
		public void setFirstprompt(int _v_) { // 还没提示过就是0(点卡服专用) by changhao
			firstprompt = _v_;
		}

		@Override
		public void setLastchargetime(long _v_) { // 最后一次充值时间
			lastchargetime = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof Properties.Data)) return false;
			Properties.Data _o_ = (Properties.Data) _o1_;
			if (!rolename.equals(_o_.rolename)) return false;
			if (!usedname.equals(_o_.usedname)) return false;
			if (level != _o_.level) return false;
			if (shape != _o_.shape) return false;
			if (sex != _o_.sex) return false;
			if (exp != _o_.exp) return false;
			if (race != _o_.race) return false;
			if (school != _o_.school) return false;
			if (color != _o_.color) return false;
			if (title != _o_.title) return false;
			if (hp != _o_.hp) return false;
			if (uplimithp != _o_.uplimithp) return false;
			if (wound != _o_.wound) return false;
			if (mp != _o_.mp) return false;
			if (sp != _o_.sp) return false;
			if (!bfp.equals(_o_.bfp)) return false;
			if (!addpointfp.equals(_o_.addpointfp)) return false;
			if (!point.equals(_o_.point)) return false;
			if (scheme != _o_.scheme) return false;
			if (schemechanges != _o_.schemechanges) return false;
			if (schemechangetimes != _o_.schemechangetimes) return false;
			if (allexp != _o_.allexp) return false;
			if (sceneid != _o_.sceneid) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			if (posz != _o_.posz) return false;
			if (laststaticsceneid != _o_.laststaticsceneid) return false;
			if (laststaticposx != _o_.laststaticposx) return false;
			if (laststaticposy != _o_.laststaticposy) return false;
			if (laststaticposz != _o_.laststaticposz) return false;
			if (fightpetkey != _o_.fightpetkey) return false;
			if (showpetkey != _o_.showpetkey) return false;
			if (!sysconfigmap.equals(_o_.sysconfigmap)) return false;
			if (!titles.equals(_o_.titles)) return false;
			if (defultform != _o_.defultform) return false;
			if (!formationsmap.equals(_o_.formationsmap)) return false;
			if (clankey != _o_.clankey) return false;
			if (oldclankey != _o_.oldclankey) return false;
			if (exitstate != _o_.exitstate) return false;
			if (onlinetime != _o_.onlinetime) return false;
			if (offlinetime != _o_.offlinetime) return false;
			if (createtime != _o_.createtime) return false;
			if (bindtel != _o_.bindtel) return false;
			if (logintimesofday != _o_.logintimesofday) return false;
			if (isgetbindtelaward != _o_.isgetbindtelaward) return false;
			if (bindteltime != _o_.bindteltime) return false;
			if (isbindtelagain != _o_.isbindtelagain) return false;
			if (bindtelagainnum != _o_.bindtelagainnum) return false;
			if (!unbindteltime.equals(_o_.unbindteltime)) return false;
			if (!password.equals(_o_.password)) return false;
			if (forcedelpdtime != _o_.forcedelpdtime) return false;
			if (forcedelendtime != _o_.forcedelendtime) return false;
			if (errortimes != _o_.errortimes) return false;
			if (lockendtime != _o_.lockendtime) return false;
			if (isopensafelock != _o_.isopensafelock) return false;
			if (sumonlinetime != _o_.sumonlinetime) return false;
			if (userid != _o_.userid) return false;
			if (addranklist != _o_.addranklist) return false;
			if (deletetime != _o_.deletetime) return false;
			if (lastloginip != _o_.lastloginip) return false;
			if (!platformuid.equals(_o_.platformuid)) return false;
			if (viplevel != _o_.viplevel) return false;
			if (energy != _o_.energy) return false;
			if (playtimetimes != _o_.playtimetimes) return false;
			if (rolezonghemaxscore != _o_.rolezonghemaxscore) return false;
			if (skillscore != _o_.skillscore) return false;
			if (petscore != _o_.petscore) return false;
			if (manypetscore != _o_.manypetscore) return false;
			if (levelscore != _o_.levelscore) return false;
			if (xiulianscroe != _o_.xiulianscroe) return false;
			if (rolescore != _o_.rolescore) return false;
			if (equipscore != _o_.equipscore) return false;
			if (historymaxprofessioncontribute != _o_.historymaxprofessioncontribute) return false;
			if (ride != _o_.ride) return false;
			if (bestpetid != _o_.bestpetid) return false;
			if (rolecolor1 != _o_.rolecolor1) return false;
			if (rolecolor2 != _o_.rolecolor2) return false;
			if (!colorroomlist.equals(_o_.colorroomlist)) return false;
			if (cruise != _o_.cruise) return false;
			if (cruise2 != _o_.cruise2) return false;
			if (cruise3 != _o_.cruise3) return false;
			if (!fighteai.equals(_o_.fighteai)) return false;
			if (equipeffect != _o_.equipeffect) return false;
			if (pilot != _o_.pilot) return false;
			if (!tupotips.equals(_o_.tupotips)) return false;
			if (!tupofinish.equals(_o_.tupofinish)) return false;
			if (yingfuxishu != _o_.yingfuxishu) return false;
			if (!depotnameinfo.equals(_o_.depotnameinfo)) return false;
			if (expiretime != _o_.expiretime) return false;
			if (firstprompt != _o_.firstprompt) return false;
			if (lastchargetime != _o_.lastchargetime) return false;
			if (!lineconfigmap.equals(_o_.lineconfigmap)) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += rolename.hashCode();
			_h_ += usedname.hashCode();
			_h_ += level;
			_h_ += shape;
			_h_ += sex;
			_h_ += exp;
			_h_ += race;
			_h_ += school;
			_h_ += color;
			_h_ += title;
			_h_ += hp;
			_h_ += uplimithp;
			_h_ += wound;
			_h_ += mp;
			_h_ += sp;
			_h_ += bfp.hashCode();
			_h_ += addpointfp.hashCode();
			_h_ += point.hashCode();
			_h_ += scheme;
			_h_ += schemechanges;
			_h_ += schemechangetimes;
			_h_ += allexp;
			_h_ += sceneid;
			_h_ += posx;
			_h_ += posy;
			_h_ += posz;
			_h_ += laststaticsceneid;
			_h_ += laststaticposx;
			_h_ += laststaticposy;
			_h_ += laststaticposz;
			_h_ += fightpetkey;
			_h_ += showpetkey;
			_h_ += sysconfigmap.hashCode();
			_h_ += titles.hashCode();
			_h_ += defultform;
			_h_ += formationsmap.hashCode();
			_h_ += clankey;
			_h_ += oldclankey;
			_h_ += exitstate;
			_h_ += onlinetime;
			_h_ += offlinetime;
			_h_ += createtime;
			_h_ += bindtel;
			_h_ += logintimesofday;
			_h_ += isgetbindtelaward;
			_h_ += bindteltime;
			_h_ += isbindtelagain;
			_h_ += bindtelagainnum;
			_h_ += unbindteltime.hashCode();
			_h_ += password.hashCode();
			_h_ += forcedelpdtime;
			_h_ += forcedelendtime;
			_h_ += errortimes;
			_h_ += lockendtime;
			_h_ += isopensafelock;
			_h_ += sumonlinetime;
			_h_ += userid;
			_h_ += addranklist ? 1231 : 1237;
			_h_ += deletetime;
			_h_ += lastloginip;
			_h_ += platformuid.hashCode();
			_h_ += viplevel;
			_h_ += energy;
			_h_ += playtimetimes;
			_h_ += rolezonghemaxscore;
			_h_ += skillscore;
			_h_ += petscore;
			_h_ += manypetscore;
			_h_ += levelscore;
			_h_ += xiulianscroe;
			_h_ += rolescore;
			_h_ += equipscore;
			_h_ += historymaxprofessioncontribute;
			_h_ += ride;
			_h_ += bestpetid;
			_h_ += rolecolor1;
			_h_ += rolecolor2;
			_h_ += colorroomlist.hashCode();
			_h_ += cruise;
			_h_ += cruise2;
			_h_ += cruise3;
			_h_ += fighteai.hashCode();
			_h_ += equipeffect;
			_h_ += pilot;
			_h_ += tupotips.hashCode();
			_h_ += tupofinish.hashCode();
			_h_ += yingfuxishu;
			_h_ += depotnameinfo.hashCode();
			_h_ += expiretime;
			_h_ += firstprompt;
			_h_ += lastchargetime;
			_h_ += lineconfigmap.hashCode();
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append("'").append(rolename).append("'");
			_sb_.append(",");
			_sb_.append(usedname);
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(shape);
			_sb_.append(",");
			_sb_.append(sex);
			_sb_.append(",");
			_sb_.append(exp);
			_sb_.append(",");
			_sb_.append(race);
			_sb_.append(",");
			_sb_.append(school);
			_sb_.append(",");
			_sb_.append(color);
			_sb_.append(",");
			_sb_.append(title);
			_sb_.append(",");
			_sb_.append(hp);
			_sb_.append(",");
			_sb_.append(uplimithp);
			_sb_.append(",");
			_sb_.append(wound);
			_sb_.append(",");
			_sb_.append(mp);
			_sb_.append(",");
			_sb_.append(sp);
			_sb_.append(",");
			_sb_.append(bfp);
			_sb_.append(",");
			_sb_.append(addpointfp);
			_sb_.append(",");
			_sb_.append(point);
			_sb_.append(",");
			_sb_.append(scheme);
			_sb_.append(",");
			_sb_.append(schemechanges);
			_sb_.append(",");
			_sb_.append(schemechangetimes);
			_sb_.append(",");
			_sb_.append(allexp);
			_sb_.append(",");
			_sb_.append(sceneid);
			_sb_.append(",");
			_sb_.append(posx);
			_sb_.append(",");
			_sb_.append(posy);
			_sb_.append(",");
			_sb_.append(posz);
			_sb_.append(",");
			_sb_.append(laststaticsceneid);
			_sb_.append(",");
			_sb_.append(laststaticposx);
			_sb_.append(",");
			_sb_.append(laststaticposy);
			_sb_.append(",");
			_sb_.append(laststaticposz);
			_sb_.append(",");
			_sb_.append(fightpetkey);
			_sb_.append(",");
			_sb_.append(showpetkey);
			_sb_.append(",");
			_sb_.append(sysconfigmap);
			_sb_.append(",");
			_sb_.append(titles);
			_sb_.append(",");
			_sb_.append(defultform);
			_sb_.append(",");
			_sb_.append(formationsmap);
			_sb_.append(",");
			_sb_.append(clankey);
			_sb_.append(",");
			_sb_.append(oldclankey);
			_sb_.append(",");
			_sb_.append(exitstate);
			_sb_.append(",");
			_sb_.append(onlinetime);
			_sb_.append(",");
			_sb_.append(offlinetime);
			_sb_.append(",");
			_sb_.append(createtime);
			_sb_.append(",");
			_sb_.append(bindtel);
			_sb_.append(",");
			_sb_.append(logintimesofday);
			_sb_.append(",");
			_sb_.append(isgetbindtelaward);
			_sb_.append(",");
			_sb_.append(bindteltime);
			_sb_.append(",");
			_sb_.append(isbindtelagain);
			_sb_.append(",");
			_sb_.append(bindtelagainnum);
			_sb_.append(",");
			_sb_.append(unbindteltime);
			_sb_.append(",");
			_sb_.append("'").append(password).append("'");
			_sb_.append(",");
			_sb_.append(forcedelpdtime);
			_sb_.append(",");
			_sb_.append(forcedelendtime);
			_sb_.append(",");
			_sb_.append(errortimes);
			_sb_.append(",");
			_sb_.append(lockendtime);
			_sb_.append(",");
			_sb_.append(isopensafelock);
			_sb_.append(",");
			_sb_.append(sumonlinetime);
			_sb_.append(",");
			_sb_.append(userid);
			_sb_.append(",");
			_sb_.append(addranklist);
			_sb_.append(",");
			_sb_.append(deletetime);
			_sb_.append(",");
			_sb_.append(lastloginip);
			_sb_.append(",");
			_sb_.append("'").append(platformuid).append("'");
			_sb_.append(",");
			_sb_.append(viplevel);
			_sb_.append(",");
			_sb_.append(energy);
			_sb_.append(",");
			_sb_.append(playtimetimes);
			_sb_.append(",");
			_sb_.append(rolezonghemaxscore);
			_sb_.append(",");
			_sb_.append(skillscore);
			_sb_.append(",");
			_sb_.append(petscore);
			_sb_.append(",");
			_sb_.append(manypetscore);
			_sb_.append(",");
			_sb_.append(levelscore);
			_sb_.append(",");
			_sb_.append(xiulianscroe);
			_sb_.append(",");
			_sb_.append(rolescore);
			_sb_.append(",");
			_sb_.append(equipscore);
			_sb_.append(",");
			_sb_.append(historymaxprofessioncontribute);
			_sb_.append(",");
			_sb_.append(ride);
			_sb_.append(",");
			_sb_.append(bestpetid);
			_sb_.append(",");
			_sb_.append(rolecolor1);
			_sb_.append(",");
			_sb_.append(rolecolor2);
			_sb_.append(",");
			_sb_.append(colorroomlist);
			_sb_.append(",");
			_sb_.append(cruise);
			_sb_.append(",");
			_sb_.append(cruise2);
			_sb_.append(",");
			_sb_.append(cruise3);
			_sb_.append(",");
			_sb_.append(fighteai);
			_sb_.append(",");
			_sb_.append(equipeffect);
			_sb_.append(",");
			_sb_.append(pilot);
			_sb_.append(",");
			_sb_.append(tupotips);
			_sb_.append(",");
			_sb_.append(tupofinish);
			_sb_.append(",");
			_sb_.append(yingfuxishu);
			_sb_.append(",");
			_sb_.append(depotnameinfo);
			_sb_.append(",");
			_sb_.append(expiretime);
			_sb_.append(",");
			_sb_.append(firstprompt);
			_sb_.append(",");
			_sb_.append(lastchargetime);
			_sb_.append(",");
			_sb_.append(lineconfigmap);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
