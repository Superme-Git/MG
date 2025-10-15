
package xbean.__;

import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public final class PetItem extends mkdb.XBean implements xbean.PetItem {
	private long id; // 主键id
	private int firstno; // 一级菜单
	private int twono; // 二级菜单
	private int threeno; // 三级菜单
	private long uniquid; // 道具唯一id
	private int key; // 背包中key
	private int itemid; // 道具id
	private long extid; // 道具属性扩展id
	private String name; // 道具名称
	private long roleid; // 拥有者id
	private int level; // 道具等级
	private int number; // 道具数量
	private int price; // 道具价格
	private int attentionnumber; // 关注数量
	private long showtime; // 公示时间
	private long expiretime; // 到期时间
	private int attack; // 攻击
	private int defend; // 防御
	private int speed; // 速度
	private int magicattack; // 法术攻击
	private int magicdef; // 法术防御
	private int maxhp; // 最大生命
	private int attackapt; // 攻击资质
	private int defendapt; // 防御资质
	private int phyforceapt; // 体力资质
	private int magicapt; // 法力资质
	private int speedapt; // 速度资质
	private int dodgeapt; // 躲闪资质
	private float growrate; // 成长率
	private java.util.ArrayList<Integer> skills; // 技能
	private int skillnumber; // 技能数量
	private int petscore; // 宠物评分

	@Override
	public void _reset_unsafe_() {
		id = 0;
		firstno = 0;
		twono = 0;
		threeno = 0;
		uniquid = 0;
		key = 0;
		itemid = 0;
		extid = 0;
		name = "";
		roleid = 0;
		level = 0;
		number = 0;
		price = 0;
		attentionnumber = 0;
		showtime = 0;
		expiretime = 0;
		attack = 0;
		defend = 0;
		speed = 0;
		magicattack = 0;
		magicdef = 0;
		maxhp = 0;
		attackapt = 0;
		defendapt = 0;
		phyforceapt = 0;
		magicapt = 0;
		speedapt = 0;
		dodgeapt = 0;
		growrate = 0;
		skills.clear();
		skillnumber = 0;
		petscore = 0;
	}

	PetItem(int __, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		id = 0;
		firstno = 0;
		twono = 0;
		threeno = 0;
		uniquid = 0;
		key = 0;
		itemid = 0;
		extid = 0;
		name = "";
		roleid = 0;
		level = 0;
		number = 0;
		price = 0;
		attentionnumber = 0;
		showtime = 0;
		expiretime = 0;
		attack = 0;
		defend = 0;
		speed = 0;
		magicattack = 0;
		magicdef = 0;
		maxhp = 0;
		attackapt = 0;
		defendapt = 0;
		phyforceapt = 0;
		magicapt = 0;
		speedapt = 0;
		dodgeapt = 0;
		growrate = 0;
		skills = new java.util.ArrayList<Integer>();
		skillnumber = 0;
		petscore = 0;
	}

	public PetItem() {
		this(0, null, null);
	}

	public PetItem(PetItem _o_) {
		this(_o_, null, null);
	}

	PetItem(xbean.PetItem _o1_, mkdb.XBean _xp_, String _vn_) {
		super(_xp_, _vn_);
		if (_o1_ instanceof PetItem) assign((PetItem)_o1_);
		else if (_o1_ instanceof PetItem.Data) assign((PetItem.Data)_o1_);
		else if (_o1_ instanceof PetItem.Const) assign(((PetItem.Const)_o1_).nThis());
		else throw new UnsupportedOperationException();
	}

	private void assign(PetItem _o_) {
		_o_._xdb_verify_unsafe_();
		id = _o_.id;
		firstno = _o_.firstno;
		twono = _o_.twono;
		threeno = _o_.threeno;
		uniquid = _o_.uniquid;
		key = _o_.key;
		itemid = _o_.itemid;
		extid = _o_.extid;
		name = _o_.name;
		roleid = _o_.roleid;
		level = _o_.level;
		number = _o_.number;
		price = _o_.price;
		attentionnumber = _o_.attentionnumber;
		showtime = _o_.showtime;
		expiretime = _o_.expiretime;
		attack = _o_.attack;
		defend = _o_.defend;
		speed = _o_.speed;
		magicattack = _o_.magicattack;
		magicdef = _o_.magicdef;
		maxhp = _o_.maxhp;
		attackapt = _o_.attackapt;
		defendapt = _o_.defendapt;
		phyforceapt = _o_.phyforceapt;
		magicapt = _o_.magicapt;
		speedapt = _o_.speedapt;
		dodgeapt = _o_.dodgeapt;
		growrate = _o_.growrate;
		skills = new java.util.ArrayList<Integer>();
		skills.addAll(_o_.skills);
		skillnumber = _o_.skillnumber;
		petscore = _o_.petscore;
	}

	private void assign(PetItem.Data _o_) {
		id = _o_.id;
		firstno = _o_.firstno;
		twono = _o_.twono;
		threeno = _o_.threeno;
		uniquid = _o_.uniquid;
		key = _o_.key;
		itemid = _o_.itemid;
		extid = _o_.extid;
		name = _o_.name;
		roleid = _o_.roleid;
		level = _o_.level;
		number = _o_.number;
		price = _o_.price;
		attentionnumber = _o_.attentionnumber;
		showtime = _o_.showtime;
		expiretime = _o_.expiretime;
		attack = _o_.attack;
		defend = _o_.defend;
		speed = _o_.speed;
		magicattack = _o_.magicattack;
		magicdef = _o_.magicdef;
		maxhp = _o_.maxhp;
		attackapt = _o_.attackapt;
		defendapt = _o_.defendapt;
		phyforceapt = _o_.phyforceapt;
		magicapt = _o_.magicapt;
		speedapt = _o_.speedapt;
		dodgeapt = _o_.dodgeapt;
		growrate = _o_.growrate;
		skills = new java.util.ArrayList<Integer>();
		skills.addAll(_o_.skills);
		skillnumber = _o_.skillnumber;
		petscore = _o_.petscore;
	}

	@Override
	public final OctetsStream marshal(OctetsStream _os_) {
		_xdb_verify_unsafe_();
		_os_.marshal(id);
		_os_.marshal(firstno);
		_os_.marshal(twono);
		_os_.marshal(threeno);
		_os_.marshal(uniquid);
		_os_.marshal(key);
		_os_.marshal(itemid);
		_os_.marshal(extid);
		_os_.marshal(name, mkdb.Const.IO_CHARSET);
		_os_.marshal(roleid);
		_os_.marshal(level);
		_os_.marshal(number);
		_os_.marshal(price);
		_os_.marshal(attentionnumber);
		_os_.marshal(showtime);
		_os_.marshal(expiretime);
		_os_.marshal(attack);
		_os_.marshal(defend);
		_os_.marshal(speed);
		_os_.marshal(magicattack);
		_os_.marshal(magicdef);
		_os_.marshal(maxhp);
		_os_.marshal(attackapt);
		_os_.marshal(defendapt);
		_os_.marshal(phyforceapt);
		_os_.marshal(magicapt);
		_os_.marshal(speedapt);
		_os_.marshal(dodgeapt);
		_os_.marshal(growrate);
		_os_.compact_uint32(skills.size());
		for (Integer _v_ : skills) {
			_os_.marshal(_v_);
		}
		_os_.marshal(skillnumber);
		_os_.marshal(petscore);
		return _os_;
	}

	@Override
	public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		_xdb_verify_unsafe_();
		id = _os_.unmarshal_long();
		firstno = _os_.unmarshal_int();
		twono = _os_.unmarshal_int();
		threeno = _os_.unmarshal_int();
		uniquid = _os_.unmarshal_long();
		key = _os_.unmarshal_int();
		itemid = _os_.unmarshal_int();
		extid = _os_.unmarshal_long();
		name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
		roleid = _os_.unmarshal_long();
		level = _os_.unmarshal_int();
		number = _os_.unmarshal_int();
		price = _os_.unmarshal_int();
		attentionnumber = _os_.unmarshal_int();
		showtime = _os_.unmarshal_long();
		expiretime = _os_.unmarshal_long();
		attack = _os_.unmarshal_int();
		defend = _os_.unmarshal_int();
		speed = _os_.unmarshal_int();
		magicattack = _os_.unmarshal_int();
		magicdef = _os_.unmarshal_int();
		maxhp = _os_.unmarshal_int();
		attackapt = _os_.unmarshal_int();
		defendapt = _os_.unmarshal_int();
		phyforceapt = _os_.unmarshal_int();
		magicapt = _os_.unmarshal_int();
		speedapt = _os_.unmarshal_int();
		dodgeapt = _os_.unmarshal_int();
		growrate = _os_.unmarshal_float();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _v_ = 0;
			_v_ = _os_.unmarshal_int();
			skills.add(_v_);
		}
		skillnumber = _os_.unmarshal_int();
		petscore = _os_.unmarshal_int();
		return _os_;
	}

	@Override
	public xbean.PetItem copy() {
		_xdb_verify_unsafe_();
		return new PetItem(this);
	}

	@Override
	public xbean.PetItem toData() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PetItem toBean() {
		_xdb_verify_unsafe_();
		return new PetItem(this); // same as copy()
	}

	@Override
	public xbean.PetItem toDataIf() {
		_xdb_verify_unsafe_();
		return new Data(this);
	}

	public xbean.PetItem toBeanIf() {
		_xdb_verify_unsafe_();
		return this;
	}

	@Override
	public mkdb.Bean toConst() {
		_xdb_verify_unsafe_();
		return new Const();
	}

	@Override
	public long getId() { // 主键id
		_xdb_verify_unsafe_();
		return id;
	}

	@Override
	public int getFirstno() { // 一级菜单
		_xdb_verify_unsafe_();
		return firstno;
	}

	@Override
	public int getTwono() { // 二级菜单
		_xdb_verify_unsafe_();
		return twono;
	}

	@Override
	public int getThreeno() { // 三级菜单
		_xdb_verify_unsafe_();
		return threeno;
	}

	@Override
	public long getUniquid() { // 道具唯一id
		_xdb_verify_unsafe_();
		return uniquid;
	}

	@Override
	public int getKey() { // 背包中key
		_xdb_verify_unsafe_();
		return key;
	}

	@Override
	public int getItemid() { // 道具id
		_xdb_verify_unsafe_();
		return itemid;
	}

	@Override
	public long getExtid() { // 道具属性扩展id
		_xdb_verify_unsafe_();
		return extid;
	}

	@Override
	public String getName() { // 道具名称
		_xdb_verify_unsafe_();
		return name;
	}

	@Override
	public com.locojoy.base.Octets getNameOctets() { // 道具名称
		_xdb_verify_unsafe_();
		return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
	}

	@Override
	public long getRoleid() { // 拥有者id
		_xdb_verify_unsafe_();
		return roleid;
	}

	@Override
	public int getLevel() { // 道具等级
		_xdb_verify_unsafe_();
		return level;
	}

	@Override
	public int getNumber() { // 道具数量
		_xdb_verify_unsafe_();
		return number;
	}

	@Override
	public int getPrice() { // 道具价格
		_xdb_verify_unsafe_();
		return price;
	}

	@Override
	public int getAttentionnumber() { // 关注数量
		_xdb_verify_unsafe_();
		return attentionnumber;
	}

	@Override
	public long getShowtime() { // 公示时间
		_xdb_verify_unsafe_();
		return showtime;
	}

	@Override
	public long getExpiretime() { // 到期时间
		_xdb_verify_unsafe_();
		return expiretime;
	}

	@Override
	public int getAttack() { // 攻击
		_xdb_verify_unsafe_();
		return attack;
	}

	@Override
	public int getDefend() { // 防御
		_xdb_verify_unsafe_();
		return defend;
	}

	@Override
	public int getSpeed() { // 速度
		_xdb_verify_unsafe_();
		return speed;
	}

	@Override
	public int getMagicattack() { // 法术攻击
		_xdb_verify_unsafe_();
		return magicattack;
	}

	@Override
	public int getMagicdef() { // 法术防御
		_xdb_verify_unsafe_();
		return magicdef;
	}

	@Override
	public int getMaxhp() { // 最大生命
		_xdb_verify_unsafe_();
		return maxhp;
	}

	@Override
	public int getAttackapt() { // 攻击资质
		_xdb_verify_unsafe_();
		return attackapt;
	}

	@Override
	public int getDefendapt() { // 防御资质
		_xdb_verify_unsafe_();
		return defendapt;
	}

	@Override
	public int getPhyforceapt() { // 体力资质
		_xdb_verify_unsafe_();
		return phyforceapt;
	}

	@Override
	public int getMagicapt() { // 法力资质
		_xdb_verify_unsafe_();
		return magicapt;
	}

	@Override
	public int getSpeedapt() { // 速度资质
		_xdb_verify_unsafe_();
		return speedapt;
	}

	@Override
	public int getDodgeapt() { // 躲闪资质
		_xdb_verify_unsafe_();
		return dodgeapt;
	}

	@Override
	public float getGrowrate() { // 成长率
		_xdb_verify_unsafe_();
		return growrate;
	}

	@Override
	public java.util.List<Integer> getSkills() { // 技能
		_xdb_verify_unsafe_();
		return mkdb.Logs.logList(new mkdb.LogKey(this, "skills"), skills);
	}

	public java.util.List<Integer> getSkillsAsData() { // 技能
		_xdb_verify_unsafe_();
		java.util.List<Integer> skills;
		PetItem _o_ = this;
		skills = new java.util.ArrayList<Integer>();
		skills.addAll(_o_.skills);
		return skills;
	}

	@Override
	public int getSkillnumber() { // 技能数量
		_xdb_verify_unsafe_();
		return skillnumber;
	}

	@Override
	public int getPetscore() { // 宠物评分
		_xdb_verify_unsafe_();
		return petscore;
	}

	@Override
	public void setId(long _v_) { // 主键id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "id") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, id) {
					public void rollback() { id = _xdb_saved; }
				};}});
		id = _v_;
	}

	@Override
	public void setFirstno(int _v_) { // 一级菜单
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "firstno") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, firstno) {
					public void rollback() { firstno = _xdb_saved; }
				};}});
		firstno = _v_;
	}

	@Override
	public void setTwono(int _v_) { // 二级菜单
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "twono") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, twono) {
					public void rollback() { twono = _xdb_saved; }
				};}});
		twono = _v_;
	}

	@Override
	public void setThreeno(int _v_) { // 三级菜单
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "threeno") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, threeno) {
					public void rollback() { threeno = _xdb_saved; }
				};}});
		threeno = _v_;
	}

	@Override
	public void setUniquid(long _v_) { // 道具唯一id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "uniquid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, uniquid) {
					public void rollback() { uniquid = _xdb_saved; }
				};}});
		uniquid = _v_;
	}

	@Override
	public void setKey(int _v_) { // 背包中key
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "key") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, key) {
					public void rollback() { key = _xdb_saved; }
				};}});
		key = _v_;
	}

	@Override
	public void setItemid(int _v_) { // 道具id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "itemid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, itemid) {
					public void rollback() { itemid = _xdb_saved; }
				};}});
		itemid = _v_;
	}

	@Override
	public void setExtid(long _v_) { // 道具属性扩展id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "extid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, extid) {
					public void rollback() { extid = _xdb_saved; }
				};}});
		extid = _v_;
	}

	@Override
	public void setName(String _v_) { // 道具名称
		_xdb_verify_unsafe_();
		if (null == _v_)
			throw new NullPointerException();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "name") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogString(this, name) {
					public void rollback() { name = _xdb_saved; }
				};}});
		name = _v_;
	}

	@Override
	public void setNameOctets(com.locojoy.base.Octets _v_) { // 道具名称
		_xdb_verify_unsafe_();
		this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
	}

	@Override
	public void setRoleid(long _v_) { // 拥有者id
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "roleid") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, roleid) {
					public void rollback() { roleid = _xdb_saved; }
				};}});
		roleid = _v_;
	}

	@Override
	public void setLevel(int _v_) { // 道具等级
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "level") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, level) {
					public void rollback() { level = _xdb_saved; }
				};}});
		level = _v_;
	}

	@Override
	public void setNumber(int _v_) { // 道具数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "number") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, number) {
					public void rollback() { number = _xdb_saved; }
				};}});
		number = _v_;
	}

	@Override
	public void setPrice(int _v_) { // 道具价格
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "price") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, price) {
					public void rollback() { price = _xdb_saved; }
				};}});
		price = _v_;
	}

	@Override
	public void setAttentionnumber(int _v_) { // 关注数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "attentionnumber") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, attentionnumber) {
					public void rollback() { attentionnumber = _xdb_saved; }
				};}});
		attentionnumber = _v_;
	}

	@Override
	public void setShowtime(long _v_) { // 公示时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "showtime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, showtime) {
					public void rollback() { showtime = _xdb_saved; }
				};}});
		showtime = _v_;
	}

	@Override
	public void setExpiretime(long _v_) { // 到期时间
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "expiretime") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogLong(this, expiretime) {
					public void rollback() { expiretime = _xdb_saved; }
				};}});
		expiretime = _v_;
	}

	@Override
	public void setAttack(int _v_) { // 攻击
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "attack") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, attack) {
					public void rollback() { attack = _xdb_saved; }
				};}});
		attack = _v_;
	}

	@Override
	public void setDefend(int _v_) { // 防御
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "defend") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, defend) {
					public void rollback() { defend = _xdb_saved; }
				};}});
		defend = _v_;
	}

	@Override
	public void setSpeed(int _v_) { // 速度
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "speed") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, speed) {
					public void rollback() { speed = _xdb_saved; }
				};}});
		speed = _v_;
	}

	@Override
	public void setMagicattack(int _v_) { // 法术攻击
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "magicattack") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, magicattack) {
					public void rollback() { magicattack = _xdb_saved; }
				};}});
		magicattack = _v_;
	}

	@Override
	public void setMagicdef(int _v_) { // 法术防御
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "magicdef") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, magicdef) {
					public void rollback() { magicdef = _xdb_saved; }
				};}});
		magicdef = _v_;
	}

	@Override
	public void setMaxhp(int _v_) { // 最大生命
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "maxhp") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, maxhp) {
					public void rollback() { maxhp = _xdb_saved; }
				};}});
		maxhp = _v_;
	}

	@Override
	public void setAttackapt(int _v_) { // 攻击资质
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "attackapt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, attackapt) {
					public void rollback() { attackapt = _xdb_saved; }
				};}});
		attackapt = _v_;
	}

	@Override
	public void setDefendapt(int _v_) { // 防御资质
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "defendapt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, defendapt) {
					public void rollback() { defendapt = _xdb_saved; }
				};}});
		defendapt = _v_;
	}

	@Override
	public void setPhyforceapt(int _v_) { // 体力资质
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "phyforceapt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, phyforceapt) {
					public void rollback() { phyforceapt = _xdb_saved; }
				};}});
		phyforceapt = _v_;
	}

	@Override
	public void setMagicapt(int _v_) { // 法力资质
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "magicapt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, magicapt) {
					public void rollback() { magicapt = _xdb_saved; }
				};}});
		magicapt = _v_;
	}

	@Override
	public void setSpeedapt(int _v_) { // 速度资质
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "speedapt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, speedapt) {
					public void rollback() { speedapt = _xdb_saved; }
				};}});
		speedapt = _v_;
	}

	@Override
	public void setDodgeapt(int _v_) { // 躲闪资质
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "dodgeapt") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, dodgeapt) {
					public void rollback() { dodgeapt = _xdb_saved; }
				};}});
		dodgeapt = _v_;
	}

	@Override
	public void setGrowrate(float _v_) { // 成长率
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "growrate") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogFloat(this, growrate) {
					public void rollback() { growrate = _xdb_saved; }
				};}});
		growrate = _v_;
	}

	@Override
	public void setSkillnumber(int _v_) { // 技能数量
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "skillnumber") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, skillnumber) {
					public void rollback() { skillnumber = _xdb_saved; }
				};}});
		skillnumber = _v_;
	}

	@Override
	public void setPetscore(int _v_) { // 宠物评分
		_xdb_verify_unsafe_();
		mkdb.Logs.logIf(new mkdb.LogKey(this, "petscore") {
			protected mkdb.Log create() {
				return new mkdb.logs.LogInt(this, petscore) {
					public void rollback() { petscore = _xdb_saved; }
				};}});
		petscore = _v_;
	}

	@Override
	public final boolean equals(Object _o1_) {
		_xdb_verify_unsafe_();
		PetItem _o_ = null;
		if ( _o1_ instanceof PetItem ) _o_ = (PetItem)_o1_;
		else if ( _o1_ instanceof PetItem.Const ) _o_ = ((PetItem.Const)_o1_).nThis();
		else return false;
		if (id != _o_.id) return false;
		if (firstno != _o_.firstno) return false;
		if (twono != _o_.twono) return false;
		if (threeno != _o_.threeno) return false;
		if (uniquid != _o_.uniquid) return false;
		if (key != _o_.key) return false;
		if (itemid != _o_.itemid) return false;
		if (extid != _o_.extid) return false;
		if (!name.equals(_o_.name)) return false;
		if (roleid != _o_.roleid) return false;
		if (level != _o_.level) return false;
		if (number != _o_.number) return false;
		if (price != _o_.price) return false;
		if (attentionnumber != _o_.attentionnumber) return false;
		if (showtime != _o_.showtime) return false;
		if (expiretime != _o_.expiretime) return false;
		if (attack != _o_.attack) return false;
		if (defend != _o_.defend) return false;
		if (speed != _o_.speed) return false;
		if (magicattack != _o_.magicattack) return false;
		if (magicdef != _o_.magicdef) return false;
		if (maxhp != _o_.maxhp) return false;
		if (attackapt != _o_.attackapt) return false;
		if (defendapt != _o_.defendapt) return false;
		if (phyforceapt != _o_.phyforceapt) return false;
		if (magicapt != _o_.magicapt) return false;
		if (speedapt != _o_.speedapt) return false;
		if (dodgeapt != _o_.dodgeapt) return false;
		if (growrate != _o_.growrate) return false;
		if (!skills.equals(_o_.skills)) return false;
		if (skillnumber != _o_.skillnumber) return false;
		if (petscore != _o_.petscore) return false;
		return true;
	}

	@Override
	public final int hashCode() {
		_xdb_verify_unsafe_();
		int _h_ = 0;
		_h_ += id;
		_h_ += firstno;
		_h_ += twono;
		_h_ += threeno;
		_h_ += uniquid;
		_h_ += key;
		_h_ += itemid;
		_h_ += extid;
		_h_ += name.hashCode();
		_h_ += roleid;
		_h_ += level;
		_h_ += number;
		_h_ += price;
		_h_ += attentionnumber;
		_h_ += showtime;
		_h_ += expiretime;
		_h_ += attack;
		_h_ += defend;
		_h_ += speed;
		_h_ += magicattack;
		_h_ += magicdef;
		_h_ += maxhp;
		_h_ += attackapt;
		_h_ += defendapt;
		_h_ += phyforceapt;
		_h_ += magicapt;
		_h_ += speedapt;
		_h_ += dodgeapt;
		_h_ += growrate;
		_h_ += skills.hashCode();
		_h_ += skillnumber;
		_h_ += petscore;
		return _h_;
	}

	@Override
	public String toString() {
		_xdb_verify_unsafe_();
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id);
		_sb_.append(",");
		_sb_.append(firstno);
		_sb_.append(",");
		_sb_.append(twono);
		_sb_.append(",");
		_sb_.append(threeno);
		_sb_.append(",");
		_sb_.append(uniquid);
		_sb_.append(",");
		_sb_.append(key);
		_sb_.append(",");
		_sb_.append(itemid);
		_sb_.append(",");
		_sb_.append(extid);
		_sb_.append(",");
		_sb_.append("'").append(name).append("'");
		_sb_.append(",");
		_sb_.append(roleid);
		_sb_.append(",");
		_sb_.append(level);
		_sb_.append(",");
		_sb_.append(number);
		_sb_.append(",");
		_sb_.append(price);
		_sb_.append(",");
		_sb_.append(attentionnumber);
		_sb_.append(",");
		_sb_.append(showtime);
		_sb_.append(",");
		_sb_.append(expiretime);
		_sb_.append(",");
		_sb_.append(attack);
		_sb_.append(",");
		_sb_.append(defend);
		_sb_.append(",");
		_sb_.append(speed);
		_sb_.append(",");
		_sb_.append(magicattack);
		_sb_.append(",");
		_sb_.append(magicdef);
		_sb_.append(",");
		_sb_.append(maxhp);
		_sb_.append(",");
		_sb_.append(attackapt);
		_sb_.append(",");
		_sb_.append(defendapt);
		_sb_.append(",");
		_sb_.append(phyforceapt);
		_sb_.append(",");
		_sb_.append(magicapt);
		_sb_.append(",");
		_sb_.append(speedapt);
		_sb_.append(",");
		_sb_.append(dodgeapt);
		_sb_.append(",");
		_sb_.append(growrate);
		_sb_.append(",");
		_sb_.append(skills);
		_sb_.append(",");
		_sb_.append(skillnumber);
		_sb_.append(",");
		_sb_.append(petscore);
		_sb_.append(")");
		return _sb_.toString();
	}

	@Override
	public mkdb.logs.Listenable newListenable() {
		mkdb.logs.ListenableBean lb = new mkdb.logs.ListenableBean();
		lb.add(new mkdb.logs.ListenableChanged().setVarName("id"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("firstno"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("twono"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("threeno"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("uniquid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("key"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("itemid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("extid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("name"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("roleid"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("level"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("number"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("price"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("attentionnumber"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("showtime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("expiretime"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("attack"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("defend"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("speed"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("magicattack"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("magicdef"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("maxhp"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("attackapt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("defendapt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("phyforceapt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("magicapt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("speedapt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("dodgeapt"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("growrate"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("skills"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("skillnumber"));
		lb.add(new mkdb.logs.ListenableChanged().setVarName("petscore"));
		return lb;
	}

	private class Const implements xbean.PetItem {
		PetItem nThis() {
			return PetItem.this;
		}

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		@Override
		public xbean.PetItem copy() {
			return PetItem.this.copy();
		}

		@Override
		public xbean.PetItem toData() {
			return PetItem.this.toData();
		}

		public xbean.PetItem toBean() {
			return PetItem.this.toBean();
		}

		@Override
		public xbean.PetItem toDataIf() {
			return PetItem.this.toDataIf();
		}

		public xbean.PetItem toBeanIf() {
			return PetItem.this.toBeanIf();
		}

		@Override
		public long getId() { // 主键id
			_xdb_verify_unsafe_();
			return id;
		}

		@Override
		public int getFirstno() { // 一级菜单
			_xdb_verify_unsafe_();
			return firstno;
		}

		@Override
		public int getTwono() { // 二级菜单
			_xdb_verify_unsafe_();
			return twono;
		}

		@Override
		public int getThreeno() { // 三级菜单
			_xdb_verify_unsafe_();
			return threeno;
		}

		@Override
		public long getUniquid() { // 道具唯一id
			_xdb_verify_unsafe_();
			return uniquid;
		}

		@Override
		public int getKey() { // 背包中key
			_xdb_verify_unsafe_();
			return key;
		}

		@Override
		public int getItemid() { // 道具id
			_xdb_verify_unsafe_();
			return itemid;
		}

		@Override
		public long getExtid() { // 道具属性扩展id
			_xdb_verify_unsafe_();
			return extid;
		}

		@Override
		public String getName() { // 道具名称
			_xdb_verify_unsafe_();
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 道具名称
			_xdb_verify_unsafe_();
			return PetItem.this.getNameOctets();
		}

		@Override
		public long getRoleid() { // 拥有者id
			_xdb_verify_unsafe_();
			return roleid;
		}

		@Override
		public int getLevel() { // 道具等级
			_xdb_verify_unsafe_();
			return level;
		}

		@Override
		public int getNumber() { // 道具数量
			_xdb_verify_unsafe_();
			return number;
		}

		@Override
		public int getPrice() { // 道具价格
			_xdb_verify_unsafe_();
			return price;
		}

		@Override
		public int getAttentionnumber() { // 关注数量
			_xdb_verify_unsafe_();
			return attentionnumber;
		}

		@Override
		public long getShowtime() { // 公示时间
			_xdb_verify_unsafe_();
			return showtime;
		}

		@Override
		public long getExpiretime() { // 到期时间
			_xdb_verify_unsafe_();
			return expiretime;
		}

		@Override
		public int getAttack() { // 攻击
			_xdb_verify_unsafe_();
			return attack;
		}

		@Override
		public int getDefend() { // 防御
			_xdb_verify_unsafe_();
			return defend;
		}

		@Override
		public int getSpeed() { // 速度
			_xdb_verify_unsafe_();
			return speed;
		}

		@Override
		public int getMagicattack() { // 法术攻击
			_xdb_verify_unsafe_();
			return magicattack;
		}

		@Override
		public int getMagicdef() { // 法术防御
			_xdb_verify_unsafe_();
			return magicdef;
		}

		@Override
		public int getMaxhp() { // 最大生命
			_xdb_verify_unsafe_();
			return maxhp;
		}

		@Override
		public int getAttackapt() { // 攻击资质
			_xdb_verify_unsafe_();
			return attackapt;
		}

		@Override
		public int getDefendapt() { // 防御资质
			_xdb_verify_unsafe_();
			return defendapt;
		}

		@Override
		public int getPhyforceapt() { // 体力资质
			_xdb_verify_unsafe_();
			return phyforceapt;
		}

		@Override
		public int getMagicapt() { // 法力资质
			_xdb_verify_unsafe_();
			return magicapt;
		}

		@Override
		public int getSpeedapt() { // 速度资质
			_xdb_verify_unsafe_();
			return speedapt;
		}

		@Override
		public int getDodgeapt() { // 躲闪资质
			_xdb_verify_unsafe_();
			return dodgeapt;
		}

		@Override
		public float getGrowrate() { // 成长率
			_xdb_verify_unsafe_();
			return growrate;
		}

		@Override
		public java.util.List<Integer> getSkills() { // 技能
			_xdb_verify_unsafe_();
			return mkdb.Consts.constList(skills);
		}

		public java.util.List<Integer> getSkillsAsData() { // 技能
			_xdb_verify_unsafe_();
			java.util.List<Integer> skills;
			PetItem _o_ = PetItem.this;
		skills = new java.util.ArrayList<Integer>();
		skills.addAll(_o_.skills);
			return skills;
		}

		@Override
		public int getSkillnumber() { // 技能数量
			_xdb_verify_unsafe_();
			return skillnumber;
		}

		@Override
		public int getPetscore() { // 宠物评分
			_xdb_verify_unsafe_();
			return petscore;
		}

		@Override
		public void setId(long _v_) { // 主键id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setFirstno(int _v_) { // 一级菜单
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setTwono(int _v_) { // 二级菜单
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setThreeno(int _v_) { // 三级菜单
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setUniquid(long _v_) { // 道具唯一id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setKey(int _v_) { // 背包中key
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setItemid(int _v_) { // 道具id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExtid(long _v_) { // 道具属性扩展id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setName(String _v_) { // 道具名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 道具名称
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setRoleid(long _v_) { // 拥有者id
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setLevel(int _v_) { // 道具等级
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setNumber(int _v_) { // 道具数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPrice(int _v_) { // 道具价格
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAttentionnumber(int _v_) { // 关注数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setShowtime(long _v_) { // 公示时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExpiretime(long _v_) { // 到期时间
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAttack(int _v_) { // 攻击
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDefend(int _v_) { // 防御
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSpeed(int _v_) { // 速度
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMagicattack(int _v_) { // 法术攻击
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMagicdef(int _v_) { // 法术防御
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMaxhp(int _v_) { // 最大生命
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAttackapt(int _v_) { // 攻击资质
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDefendapt(int _v_) { // 防御资质
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPhyforceapt(int _v_) { // 体力资质
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMagicapt(int _v_) { // 法力资质
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSpeedapt(int _v_) { // 速度资质
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setDodgeapt(int _v_) { // 躲闪资质
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setGrowrate(float _v_) { // 成长率
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setSkillnumber(int _v_) { // 技能数量
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPetscore(int _v_) { // 宠物评分
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
			return PetItem.this.isData();
		}

		@Override
		public OctetsStream marshal(OctetsStream _os_) {
			return PetItem.this.marshal(_os_);
		}

		@Override
		public OctetsStream unmarshal(OctetsStream arg0) throws MarshalException {
			_xdb_verify_unsafe_();
			throw new UnsupportedOperationException();
		}

		@Override
		public mkdb.Bean xdbParent() {
			return PetItem.this.xdbParent();
		}

		@Override
		public boolean xdbManaged() {
			return PetItem.this.xdbManaged();
		}

		@Override
		public String xdbVarname() {
			return PetItem.this.xdbVarname();
		}

		@Override
		public Long xdbObjId() {
			return PetItem.this.xdbObjId();
		}

		@Override
		public boolean equals(Object obj) {
			return PetItem.this.equals(obj);
		}

		@Override
		public int hashCode() {
			return PetItem.this.hashCode();
		}

		@Override
		public String toString() {
			return PetItem.this.toString();
		}

	}

	public static final class Data implements xbean.PetItem {
		private long id; // 主键id
		private int firstno; // 一级菜单
		private int twono; // 二级菜单
		private int threeno; // 三级菜单
		private long uniquid; // 道具唯一id
		private int key; // 背包中key
		private int itemid; // 道具id
		private long extid; // 道具属性扩展id
		private String name; // 道具名称
		private long roleid; // 拥有者id
		private int level; // 道具等级
		private int number; // 道具数量
		private int price; // 道具价格
		private int attentionnumber; // 关注数量
		private long showtime; // 公示时间
		private long expiretime; // 到期时间
		private int attack; // 攻击
		private int defend; // 防御
		private int speed; // 速度
		private int magicattack; // 法术攻击
		private int magicdef; // 法术防御
		private int maxhp; // 最大生命
		private int attackapt; // 攻击资质
		private int defendapt; // 防御资质
		private int phyforceapt; // 体力资质
		private int magicapt; // 法力资质
		private int speedapt; // 速度资质
		private int dodgeapt; // 躲闪资质
		private float growrate; // 成长率
		private java.util.ArrayList<Integer> skills; // 技能
		private int skillnumber; // 技能数量
		private int petscore; // 宠物评分

		@Override
		public void _reset_unsafe_() {
			throw new UnsupportedOperationException();
		}

		public Data() {
			id = 0;
			firstno = 0;
			twono = 0;
			threeno = 0;
			uniquid = 0;
			key = 0;
			itemid = 0;
			extid = 0;
			name = "";
			roleid = 0;
			level = 0;
			number = 0;
			price = 0;
			attentionnumber = 0;
			showtime = 0;
			expiretime = 0;
			attack = 0;
			defend = 0;
			speed = 0;
			magicattack = 0;
			magicdef = 0;
			maxhp = 0;
			attackapt = 0;
			defendapt = 0;
			phyforceapt = 0;
			magicapt = 0;
			speedapt = 0;
			dodgeapt = 0;
			growrate = 0;
			skills = new java.util.ArrayList<Integer>();
			skillnumber = 0;
			petscore = 0;
		}

		Data(xbean.PetItem _o1_) {
			if (_o1_ instanceof PetItem) assign((PetItem)_o1_);
			else if (_o1_ instanceof PetItem.Data) assign((PetItem.Data)_o1_);
			else if (_o1_ instanceof PetItem.Const) assign(((PetItem.Const)_o1_).nThis());
			else throw new UnsupportedOperationException();
		}

		private void assign(PetItem _o_) {
			id = _o_.id;
			firstno = _o_.firstno;
			twono = _o_.twono;
			threeno = _o_.threeno;
			uniquid = _o_.uniquid;
			key = _o_.key;
			itemid = _o_.itemid;
			extid = _o_.extid;
			name = _o_.name;
			roleid = _o_.roleid;
			level = _o_.level;
			number = _o_.number;
			price = _o_.price;
			attentionnumber = _o_.attentionnumber;
			showtime = _o_.showtime;
			expiretime = _o_.expiretime;
			attack = _o_.attack;
			defend = _o_.defend;
			speed = _o_.speed;
			magicattack = _o_.magicattack;
			magicdef = _o_.magicdef;
			maxhp = _o_.maxhp;
			attackapt = _o_.attackapt;
			defendapt = _o_.defendapt;
			phyforceapt = _o_.phyforceapt;
			magicapt = _o_.magicapt;
			speedapt = _o_.speedapt;
			dodgeapt = _o_.dodgeapt;
			growrate = _o_.growrate;
			skills = new java.util.ArrayList<Integer>();
			skills.addAll(_o_.skills);
			skillnumber = _o_.skillnumber;
			petscore = _o_.petscore;
		}

		private void assign(PetItem.Data _o_) {
			id = _o_.id;
			firstno = _o_.firstno;
			twono = _o_.twono;
			threeno = _o_.threeno;
			uniquid = _o_.uniquid;
			key = _o_.key;
			itemid = _o_.itemid;
			extid = _o_.extid;
			name = _o_.name;
			roleid = _o_.roleid;
			level = _o_.level;
			number = _o_.number;
			price = _o_.price;
			attentionnumber = _o_.attentionnumber;
			showtime = _o_.showtime;
			expiretime = _o_.expiretime;
			attack = _o_.attack;
			defend = _o_.defend;
			speed = _o_.speed;
			magicattack = _o_.magicattack;
			magicdef = _o_.magicdef;
			maxhp = _o_.maxhp;
			attackapt = _o_.attackapt;
			defendapt = _o_.defendapt;
			phyforceapt = _o_.phyforceapt;
			magicapt = _o_.magicapt;
			speedapt = _o_.speedapt;
			dodgeapt = _o_.dodgeapt;
			growrate = _o_.growrate;
			skills = new java.util.ArrayList<Integer>();
			skills.addAll(_o_.skills);
			skillnumber = _o_.skillnumber;
			petscore = _o_.petscore;
		}

		@Override
		public final OctetsStream marshal(OctetsStream _os_) {
			_os_.marshal(id);
			_os_.marshal(firstno);
			_os_.marshal(twono);
			_os_.marshal(threeno);
			_os_.marshal(uniquid);
			_os_.marshal(key);
			_os_.marshal(itemid);
			_os_.marshal(extid);
			_os_.marshal(name, mkdb.Const.IO_CHARSET);
			_os_.marshal(roleid);
			_os_.marshal(level);
			_os_.marshal(number);
			_os_.marshal(price);
			_os_.marshal(attentionnumber);
			_os_.marshal(showtime);
			_os_.marshal(expiretime);
			_os_.marshal(attack);
			_os_.marshal(defend);
			_os_.marshal(speed);
			_os_.marshal(magicattack);
			_os_.marshal(magicdef);
			_os_.marshal(maxhp);
			_os_.marshal(attackapt);
			_os_.marshal(defendapt);
			_os_.marshal(phyforceapt);
			_os_.marshal(magicapt);
			_os_.marshal(speedapt);
			_os_.marshal(dodgeapt);
			_os_.marshal(growrate);
			_os_.compact_uint32(skills.size());
			for (Integer _v_ : skills) {
				_os_.marshal(_v_);
			}
			_os_.marshal(skillnumber);
			_os_.marshal(petscore);
			return _os_;
		}

		@Override
		public final OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
			id = _os_.unmarshal_long();
			firstno = _os_.unmarshal_int();
			twono = _os_.unmarshal_int();
			threeno = _os_.unmarshal_int();
			uniquid = _os_.unmarshal_long();
			key = _os_.unmarshal_int();
			itemid = _os_.unmarshal_int();
			extid = _os_.unmarshal_long();
			name = _os_.unmarshal_String(mkdb.Const.IO_CHARSET);
			roleid = _os_.unmarshal_long();
			level = _os_.unmarshal_int();
			number = _os_.unmarshal_int();
			price = _os_.unmarshal_int();
			attentionnumber = _os_.unmarshal_int();
			showtime = _os_.unmarshal_long();
			expiretime = _os_.unmarshal_long();
			attack = _os_.unmarshal_int();
			defend = _os_.unmarshal_int();
			speed = _os_.unmarshal_int();
			magicattack = _os_.unmarshal_int();
			magicdef = _os_.unmarshal_int();
			maxhp = _os_.unmarshal_int();
			attackapt = _os_.unmarshal_int();
			defendapt = _os_.unmarshal_int();
			phyforceapt = _os_.unmarshal_int();
			magicapt = _os_.unmarshal_int();
			speedapt = _os_.unmarshal_int();
			dodgeapt = _os_.unmarshal_int();
			growrate = _os_.unmarshal_float();
			for (int size = _os_.uncompact_uint32(); size > 0; --size) {
				int _v_ = 0;
				_v_ = _os_.unmarshal_int();
				skills.add(_v_);
			}
			skillnumber = _os_.unmarshal_int();
			petscore = _os_.unmarshal_int();
			return _os_;
		}

		@Override
		public xbean.PetItem copy() {
			return new Data(this);
		}

		@Override
		public xbean.PetItem toData() {
			return new Data(this);
		}

		public xbean.PetItem toBean() {
			return new PetItem(this, null, null);
		}

		@Override
		public xbean.PetItem toDataIf() {
			return this;
		}

		public xbean.PetItem toBeanIf() {
			return new PetItem(this, null, null);
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
		public long getId() { // 主键id
			return id;
		}

		@Override
		public int getFirstno() { // 一级菜单
			return firstno;
		}

		@Override
		public int getTwono() { // 二级菜单
			return twono;
		}

		@Override
		public int getThreeno() { // 三级菜单
			return threeno;
		}

		@Override
		public long getUniquid() { // 道具唯一id
			return uniquid;
		}

		@Override
		public int getKey() { // 背包中key
			return key;
		}

		@Override
		public int getItemid() { // 道具id
			return itemid;
		}

		@Override
		public long getExtid() { // 道具属性扩展id
			return extid;
		}

		@Override
		public String getName() { // 道具名称
			return name;
		}

		@Override
		public com.locojoy.base.Octets getNameOctets() { // 道具名称
			return com.locojoy.base.Octets.wrap(getName(), mkdb.Const.IO_CHARSET);
		}

		@Override
		public long getRoleid() { // 拥有者id
			return roleid;
		}

		@Override
		public int getLevel() { // 道具等级
			return level;
		}

		@Override
		public int getNumber() { // 道具数量
			return number;
		}

		@Override
		public int getPrice() { // 道具价格
			return price;
		}

		@Override
		public int getAttentionnumber() { // 关注数量
			return attentionnumber;
		}

		@Override
		public long getShowtime() { // 公示时间
			return showtime;
		}

		@Override
		public long getExpiretime() { // 到期时间
			return expiretime;
		}

		@Override
		public int getAttack() { // 攻击
			return attack;
		}

		@Override
		public int getDefend() { // 防御
			return defend;
		}

		@Override
		public int getSpeed() { // 速度
			return speed;
		}

		@Override
		public int getMagicattack() { // 法术攻击
			return magicattack;
		}

		@Override
		public int getMagicdef() { // 法术防御
			return magicdef;
		}

		@Override
		public int getMaxhp() { // 最大生命
			return maxhp;
		}

		@Override
		public int getAttackapt() { // 攻击资质
			return attackapt;
		}

		@Override
		public int getDefendapt() { // 防御资质
			return defendapt;
		}

		@Override
		public int getPhyforceapt() { // 体力资质
			return phyforceapt;
		}

		@Override
		public int getMagicapt() { // 法力资质
			return magicapt;
		}

		@Override
		public int getSpeedapt() { // 速度资质
			return speedapt;
		}

		@Override
		public int getDodgeapt() { // 躲闪资质
			return dodgeapt;
		}

		@Override
		public float getGrowrate() { // 成长率
			return growrate;
		}

		@Override
		public java.util.List<Integer> getSkills() { // 技能
			return skills;
		}

		@Override
		public java.util.List<Integer> getSkillsAsData() { // 技能
			return skills;
		}

		@Override
		public int getSkillnumber() { // 技能数量
			return skillnumber;
		}

		@Override
		public int getPetscore() { // 宠物评分
			return petscore;
		}

		@Override
		public void setId(long _v_) { // 主键id
			id = _v_;
		}

		@Override
		public void setFirstno(int _v_) { // 一级菜单
			firstno = _v_;
		}

		@Override
		public void setTwono(int _v_) { // 二级菜单
			twono = _v_;
		}

		@Override
		public void setThreeno(int _v_) { // 三级菜单
			threeno = _v_;
		}

		@Override
		public void setUniquid(long _v_) { // 道具唯一id
			uniquid = _v_;
		}

		@Override
		public void setKey(int _v_) { // 背包中key
			key = _v_;
		}

		@Override
		public void setItemid(int _v_) { // 道具id
			itemid = _v_;
		}

		@Override
		public void setExtid(long _v_) { // 道具属性扩展id
			extid = _v_;
		}

		@Override
		public void setName(String _v_) { // 道具名称
			if (null == _v_)
				throw new NullPointerException();
			name = _v_;
		}

		@Override
		public void setNameOctets(com.locojoy.base.Octets _v_) { // 道具名称
			this.setName(_v_.getString(mkdb.Const.IO_CHARSET));
		}

		@Override
		public void setRoleid(long _v_) { // 拥有者id
			roleid = _v_;
		}

		@Override
		public void setLevel(int _v_) { // 道具等级
			level = _v_;
		}

		@Override
		public void setNumber(int _v_) { // 道具数量
			number = _v_;
		}

		@Override
		public void setPrice(int _v_) { // 道具价格
			price = _v_;
		}

		@Override
		public void setAttentionnumber(int _v_) { // 关注数量
			attentionnumber = _v_;
		}

		@Override
		public void setShowtime(long _v_) { // 公示时间
			showtime = _v_;
		}

		@Override
		public void setExpiretime(long _v_) { // 到期时间
			expiretime = _v_;
		}

		@Override
		public void setAttack(int _v_) { // 攻击
			attack = _v_;
		}

		@Override
		public void setDefend(int _v_) { // 防御
			defend = _v_;
		}

		@Override
		public void setSpeed(int _v_) { // 速度
			speed = _v_;
		}

		@Override
		public void setMagicattack(int _v_) { // 法术攻击
			magicattack = _v_;
		}

		@Override
		public void setMagicdef(int _v_) { // 法术防御
			magicdef = _v_;
		}

		@Override
		public void setMaxhp(int _v_) { // 最大生命
			maxhp = _v_;
		}

		@Override
		public void setAttackapt(int _v_) { // 攻击资质
			attackapt = _v_;
		}

		@Override
		public void setDefendapt(int _v_) { // 防御资质
			defendapt = _v_;
		}

		@Override
		public void setPhyforceapt(int _v_) { // 体力资质
			phyforceapt = _v_;
		}

		@Override
		public void setMagicapt(int _v_) { // 法力资质
			magicapt = _v_;
		}

		@Override
		public void setSpeedapt(int _v_) { // 速度资质
			speedapt = _v_;
		}

		@Override
		public void setDodgeapt(int _v_) { // 躲闪资质
			dodgeapt = _v_;
		}

		@Override
		public void setGrowrate(float _v_) { // 成长率
			growrate = _v_;
		}

		@Override
		public void setSkillnumber(int _v_) { // 技能数量
			skillnumber = _v_;
		}

		@Override
		public void setPetscore(int _v_) { // 宠物评分
			petscore = _v_;
		}

		@Override
		public final boolean equals(Object _o1_) {
			if (!(_o1_ instanceof PetItem.Data)) return false;
			PetItem.Data _o_ = (PetItem.Data) _o1_;
			if (id != _o_.id) return false;
			if (firstno != _o_.firstno) return false;
			if (twono != _o_.twono) return false;
			if (threeno != _o_.threeno) return false;
			if (uniquid != _o_.uniquid) return false;
			if (key != _o_.key) return false;
			if (itemid != _o_.itemid) return false;
			if (extid != _o_.extid) return false;
			if (!name.equals(_o_.name)) return false;
			if (roleid != _o_.roleid) return false;
			if (level != _o_.level) return false;
			if (number != _o_.number) return false;
			if (price != _o_.price) return false;
			if (attentionnumber != _o_.attentionnumber) return false;
			if (showtime != _o_.showtime) return false;
			if (expiretime != _o_.expiretime) return false;
			if (attack != _o_.attack) return false;
			if (defend != _o_.defend) return false;
			if (speed != _o_.speed) return false;
			if (magicattack != _o_.magicattack) return false;
			if (magicdef != _o_.magicdef) return false;
			if (maxhp != _o_.maxhp) return false;
			if (attackapt != _o_.attackapt) return false;
			if (defendapt != _o_.defendapt) return false;
			if (phyforceapt != _o_.phyforceapt) return false;
			if (magicapt != _o_.magicapt) return false;
			if (speedapt != _o_.speedapt) return false;
			if (dodgeapt != _o_.dodgeapt) return false;
			if (growrate != _o_.growrate) return false;
			if (!skills.equals(_o_.skills)) return false;
			if (skillnumber != _o_.skillnumber) return false;
			if (petscore != _o_.petscore) return false;
			return true;
		}

		@Override
		public final int hashCode() {
			int _h_ = 0;
			_h_ += id;
			_h_ += firstno;
			_h_ += twono;
			_h_ += threeno;
			_h_ += uniquid;
			_h_ += key;
			_h_ += itemid;
			_h_ += extid;
			_h_ += name.hashCode();
			_h_ += roleid;
			_h_ += level;
			_h_ += number;
			_h_ += price;
			_h_ += attentionnumber;
			_h_ += showtime;
			_h_ += expiretime;
			_h_ += attack;
			_h_ += defend;
			_h_ += speed;
			_h_ += magicattack;
			_h_ += magicdef;
			_h_ += maxhp;
			_h_ += attackapt;
			_h_ += defendapt;
			_h_ += phyforceapt;
			_h_ += magicapt;
			_h_ += speedapt;
			_h_ += dodgeapt;
			_h_ += growrate;
			_h_ += skills.hashCode();
			_h_ += skillnumber;
			_h_ += petscore;
			return _h_;
		}

		@Override
		public String toString() {
			StringBuilder _sb_ = new StringBuilder();
			_sb_.append("(");
			_sb_.append(id);
			_sb_.append(",");
			_sb_.append(firstno);
			_sb_.append(",");
			_sb_.append(twono);
			_sb_.append(",");
			_sb_.append(threeno);
			_sb_.append(",");
			_sb_.append(uniquid);
			_sb_.append(",");
			_sb_.append(key);
			_sb_.append(",");
			_sb_.append(itemid);
			_sb_.append(",");
			_sb_.append(extid);
			_sb_.append(",");
			_sb_.append("'").append(name).append("'");
			_sb_.append(",");
			_sb_.append(roleid);
			_sb_.append(",");
			_sb_.append(level);
			_sb_.append(",");
			_sb_.append(number);
			_sb_.append(",");
			_sb_.append(price);
			_sb_.append(",");
			_sb_.append(attentionnumber);
			_sb_.append(",");
			_sb_.append(showtime);
			_sb_.append(",");
			_sb_.append(expiretime);
			_sb_.append(",");
			_sb_.append(attack);
			_sb_.append(",");
			_sb_.append(defend);
			_sb_.append(",");
			_sb_.append(speed);
			_sb_.append(",");
			_sb_.append(magicattack);
			_sb_.append(",");
			_sb_.append(magicdef);
			_sb_.append(",");
			_sb_.append(maxhp);
			_sb_.append(",");
			_sb_.append(attackapt);
			_sb_.append(",");
			_sb_.append(defendapt);
			_sb_.append(",");
			_sb_.append(phyforceapt);
			_sb_.append(",");
			_sb_.append(magicapt);
			_sb_.append(",");
			_sb_.append(speedapt);
			_sb_.append(",");
			_sb_.append(dodgeapt);
			_sb_.append(",");
			_sb_.append(growrate);
			_sb_.append(",");
			_sb_.append(skills);
			_sb_.append(",");
			_sb_.append(skillnumber);
			_sb_.append(",");
			_sb_.append(petscore);
			_sb_.append(")");
			return _sb_.toString();
		}

	}
}
