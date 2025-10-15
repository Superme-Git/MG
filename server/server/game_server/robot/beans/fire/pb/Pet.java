
package fire.pb;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class Pet implements Marshal {
	public final static int FLAG_LOCK = 1;
	public final static int FLAG_BIND = 2;

	public int id; // 宠物ID
	public int key; // key
	public java.lang.String name; // 名称
	public int level; // 等级
	public int uselevel; // 参战等级
	public int xuemai; // 宠物当前资质浓度值
	public int gengu; // root-bone
	public int colour; // 颜色值
	public int hp; // 当前生命
	public int maxhp; // 最大生命
	public int mp; // 当前法力
	public int maxmp; // 最大法力
	public int attack; // 攻击
	public int defend; // 防御
	public int speed; // 速度
	public int magicattack; // 法术攻击
	public int magicdef; // 法术防御
	public byte scale; // 1,2,3,4分别代表4个size
	public fire.pb.BasicFightProperties initbfp; // 初始的基础战斗属性
	public fire.pb.BasicFightProperties bfp; // 基础战斗属性
	public short point; // 潜能。未分配点数
	public byte autoaddcons; // 加点方案-升级自动加体质
	public byte autoaddiq; // 加点方案-升级自动加智力
	public byte autoaddstr; // 加点方案-升级自动加力量
	public byte autoaddendu; // 加点方案-升级自动加耐力
	public byte autoaddagi; // 加点方案-升级自动加敏捷
	public short pointresetcount; // 宠物加点的重置次数
	public long exp; // 当前经验
	public long nexp; // 升级需要经验
	public int attackapt; // 攻击资质
	public int defendapt; // 防御资质
	public int phyforceapt; // 体力资质
	public int magicapt; // 法力资质
	public int speedapt; // 速度资质
	public int dodgeapt; // 躲闪资质
	public float growrate; // 成长率
	public int life; // 寿命
	public int kind; // 宠物类型 野生、宝宝
	public java.util.LinkedList<fire.pb.Petskill> skills; // 宠物技能
	public java.util.HashMap<Integer,Long> skillexpires; // 有时限的宠物技能的到期时间。key=技能id，value=到期时间
	public byte flag; // 宠物标志 1= 锁定，2 = 绑定
	public long timeout; // 如果是时间锁，到期时间（毫秒）
	public long ownerid; // 主人id
	public java.lang.String ownername; // 主人名
	public int rank; // 排行榜排名
	public short starid; // 当前星星等级Id
	public short practisetimes; // 当前剩余的训练次数
	public java.util.HashMap<Integer,Integer> zizhi; // 训练后得到的未分配的资质
	public int changegengu; // 训练后得到的root-bone变化值
	public int skill_grids; // 宠物技能格子数
	public byte aptaddcount; // 资质培养次数
	public byte growrateaddcount; // 成长率培养次数
	public short washcount; // 洗练次数
	public int petscore; // 宠物评分
	public int petbasescore; // 宠物一级属性评分
	public int petdye1; // 宠物染色部位1
	public int petdye2; // 宠物染色部位2
	public int shenshouinccount; // 神兽养成次数
	public long marketfreezeexpire; // 摆摊冻结截止时间,默认0不冻结

	public Pet() {
		name = "";
		initbfp = new fire.pb.BasicFightProperties();
		bfp = new fire.pb.BasicFightProperties();
		skills = new java.util.LinkedList<fire.pb.Petskill>();
		skillexpires = new java.util.HashMap<Integer,Long>();
		ownername = "";
		zizhi = new java.util.HashMap<Integer,Integer>();
	}

	public Pet(int _id_, int _key_, java.lang.String _name_, int _level_, int _uselevel_, int _xuemai_, int _gengu_, int _colour_, int _hp_, int _maxhp_, int _mp_, int _maxmp_, int _attack_, int _defend_, int _speed_, int _magicattack_, int _magicdef_, byte _scale_, fire.pb.BasicFightProperties _initbfp_, fire.pb.BasicFightProperties _bfp_, short _point_, byte _autoaddcons_, byte _autoaddiq_, byte _autoaddstr_, byte _autoaddendu_, byte _autoaddagi_, short _pointresetcount_, long _exp_, long _nexp_, int _attackapt_, int _defendapt_, int _phyforceapt_, int _magicapt_, int _speedapt_, int _dodgeapt_, float _growrate_, int _life_, int _kind_, java.util.LinkedList<fire.pb.Petskill> _skills_, java.util.HashMap<Integer,Long> _skillexpires_, byte _flag_, long _timeout_, long _ownerid_, java.lang.String _ownername_, int _rank_, short _starid_, short _practisetimes_, java.util.HashMap<Integer,Integer> _zizhi_, int _changegengu_, int _skill_grids_, byte _aptaddcount_, byte _growrateaddcount_, short _washcount_, int _petscore_, int _petbasescore_, int _petdye1_, int _petdye2_, int _shenshouinccount_, long _marketfreezeexpire_) {
		this.id = _id_;
		this.key = _key_;
		this.name = _name_;
		this.level = _level_;
		this.uselevel = _uselevel_;
		this.xuemai = _xuemai_;
		this.gengu = _gengu_;
		this.colour = _colour_;
		this.hp = _hp_;
		this.maxhp = _maxhp_;
		this.mp = _mp_;
		this.maxmp = _maxmp_;
		this.attack = _attack_;
		this.defend = _defend_;
		this.speed = _speed_;
		this.magicattack = _magicattack_;
		this.magicdef = _magicdef_;
		this.scale = _scale_;
		this.initbfp = _initbfp_;
		this.bfp = _bfp_;
		this.point = _point_;
		this.autoaddcons = _autoaddcons_;
		this.autoaddiq = _autoaddiq_;
		this.autoaddstr = _autoaddstr_;
		this.autoaddendu = _autoaddendu_;
		this.autoaddagi = _autoaddagi_;
		this.pointresetcount = _pointresetcount_;
		this.exp = _exp_;
		this.nexp = _nexp_;
		this.attackapt = _attackapt_;
		this.defendapt = _defendapt_;
		this.phyforceapt = _phyforceapt_;
		this.magicapt = _magicapt_;
		this.speedapt = _speedapt_;
		this.dodgeapt = _dodgeapt_;
		this.growrate = _growrate_;
		this.life = _life_;
		this.kind = _kind_;
		this.skills = _skills_;
		this.skillexpires = _skillexpires_;
		this.flag = _flag_;
		this.timeout = _timeout_;
		this.ownerid = _ownerid_;
		this.ownername = _ownername_;
		this.rank = _rank_;
		this.starid = _starid_;
		this.practisetimes = _practisetimes_;
		this.zizhi = _zizhi_;
		this.changegengu = _changegengu_;
		this.skill_grids = _skill_grids_;
		this.aptaddcount = _aptaddcount_;
		this.growrateaddcount = _growrateaddcount_;
		this.washcount = _washcount_;
		this.petscore = _petscore_;
		this.petbasescore = _petbasescore_;
		this.petdye1 = _petdye1_;
		this.petdye2 = _petdye2_;
		this.shenshouinccount = _shenshouinccount_;
		this.marketfreezeexpire = _marketfreezeexpire_;
	}

	public final boolean _validator_() {
		if (id < 1) return false;
		if (!initbfp._validator_()) return false;
		if (!bfp._validator_()) return false;
		if (autoaddcons < 0) return false;
		if (autoaddiq < 0) return false;
		if (autoaddstr < 0) return false;
		if (autoaddendu < 0) return false;
		if (autoaddagi < 0) return false;
		for (fire.pb.Petskill _v_ : skills)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(id);
		_os_.marshal(key);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(level);
		_os_.marshal(uselevel);
		_os_.marshal(xuemai);
		_os_.marshal(gengu);
		_os_.marshal(colour);
		_os_.marshal(hp);
		_os_.marshal(maxhp);
		_os_.marshal(mp);
		_os_.marshal(maxmp);
		_os_.marshal(attack);
		_os_.marshal(defend);
		_os_.marshal(speed);
		_os_.marshal(magicattack);
		_os_.marshal(magicdef);
		_os_.marshal(scale);
		_os_.marshal(initbfp);
		_os_.marshal(bfp);
		_os_.marshal(point);
		_os_.marshal(autoaddcons);
		_os_.marshal(autoaddiq);
		_os_.marshal(autoaddstr);
		_os_.marshal(autoaddendu);
		_os_.marshal(autoaddagi);
		_os_.marshal(pointresetcount);
		_os_.marshal(exp);
		_os_.marshal(nexp);
		_os_.marshal(attackapt);
		_os_.marshal(defendapt);
		_os_.marshal(phyforceapt);
		_os_.marshal(magicapt);
		_os_.marshal(speedapt);
		_os_.marshal(dodgeapt);
		_os_.marshal(growrate);
		_os_.marshal(life);
		_os_.marshal(kind);
		_os_.compact_uint32(skills.size());
		for (fire.pb.Petskill _v_ : skills) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(skillexpires.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : skillexpires.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(flag);
		_os_.marshal(timeout);
		_os_.marshal(ownerid);
		_os_.marshal(ownername, "UTF-16LE");
		_os_.marshal(rank);
		_os_.marshal(starid);
		_os_.marshal(practisetimes);
		_os_.compact_uint32(zizhi.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : zizhi.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(changegengu);
		_os_.marshal(skill_grids);
		_os_.marshal(aptaddcount);
		_os_.marshal(growrateaddcount);
		_os_.marshal(washcount);
		_os_.marshal(petscore);
		_os_.marshal(petbasescore);
		_os_.marshal(petdye1);
		_os_.marshal(petdye2);
		_os_.marshal(shenshouinccount);
		_os_.marshal(marketfreezeexpire);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		id = _os_.unmarshal_int();
		key = _os_.unmarshal_int();
		name = _os_.unmarshal_String("UTF-16LE");
		level = _os_.unmarshal_int();
		uselevel = _os_.unmarshal_int();
		xuemai = _os_.unmarshal_int();
		gengu = _os_.unmarshal_int();
		colour = _os_.unmarshal_int();
		hp = _os_.unmarshal_int();
		maxhp = _os_.unmarshal_int();
		mp = _os_.unmarshal_int();
		maxmp = _os_.unmarshal_int();
		attack = _os_.unmarshal_int();
		defend = _os_.unmarshal_int();
		speed = _os_.unmarshal_int();
		magicattack = _os_.unmarshal_int();
		magicdef = _os_.unmarshal_int();
		scale = _os_.unmarshal_byte();
		initbfp.unmarshal(_os_);
		bfp.unmarshal(_os_);
		point = _os_.unmarshal_short();
		autoaddcons = _os_.unmarshal_byte();
		autoaddiq = _os_.unmarshal_byte();
		autoaddstr = _os_.unmarshal_byte();
		autoaddendu = _os_.unmarshal_byte();
		autoaddagi = _os_.unmarshal_byte();
		pointresetcount = _os_.unmarshal_short();
		exp = _os_.unmarshal_long();
		nexp = _os_.unmarshal_long();
		attackapt = _os_.unmarshal_int();
		defendapt = _os_.unmarshal_int();
		phyforceapt = _os_.unmarshal_int();
		magicapt = _os_.unmarshal_int();
		speedapt = _os_.unmarshal_int();
		dodgeapt = _os_.unmarshal_int();
		growrate = _os_.unmarshal_float();
		life = _os_.unmarshal_int();
		kind = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.Petskill _v_ = new fire.pb.Petskill();
			_v_.unmarshal(_os_);
			skills.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			long _v_;
			_v_ = _os_.unmarshal_long();
			skillexpires.put(_k_, _v_);
		}
		flag = _os_.unmarshal_byte();
		timeout = _os_.unmarshal_long();
		ownerid = _os_.unmarshal_long();
		ownername = _os_.unmarshal_String("UTF-16LE");
		rank = _os_.unmarshal_int();
		starid = _os_.unmarshal_short();
		practisetimes = _os_.unmarshal_short();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			zizhi.put(_k_, _v_);
		}
		changegengu = _os_.unmarshal_int();
		skill_grids = _os_.unmarshal_int();
		aptaddcount = _os_.unmarshal_byte();
		growrateaddcount = _os_.unmarshal_byte();
		washcount = _os_.unmarshal_short();
		petscore = _os_.unmarshal_int();
		petbasescore = _os_.unmarshal_int();
		petdye1 = _os_.unmarshal_int();
		petdye2 = _os_.unmarshal_int();
		shenshouinccount = _os_.unmarshal_int();
		marketfreezeexpire = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof Pet) {
			Pet _o_ = (Pet)_o1_;
			if (id != _o_.id) return false;
			if (key != _o_.key) return false;
			if (!name.equals(_o_.name)) return false;
			if (level != _o_.level) return false;
			if (uselevel != _o_.uselevel) return false;
			if (xuemai != _o_.xuemai) return false;
			if (gengu != _o_.gengu) return false;
			if (colour != _o_.colour) return false;
			if (hp != _o_.hp) return false;
			if (maxhp != _o_.maxhp) return false;
			if (mp != _o_.mp) return false;
			if (maxmp != _o_.maxmp) return false;
			if (attack != _o_.attack) return false;
			if (defend != _o_.defend) return false;
			if (speed != _o_.speed) return false;
			if (magicattack != _o_.magicattack) return false;
			if (magicdef != _o_.magicdef) return false;
			if (scale != _o_.scale) return false;
			if (!initbfp.equals(_o_.initbfp)) return false;
			if (!bfp.equals(_o_.bfp)) return false;
			if (point != _o_.point) return false;
			if (autoaddcons != _o_.autoaddcons) return false;
			if (autoaddiq != _o_.autoaddiq) return false;
			if (autoaddstr != _o_.autoaddstr) return false;
			if (autoaddendu != _o_.autoaddendu) return false;
			if (autoaddagi != _o_.autoaddagi) return false;
			if (pointresetcount != _o_.pointresetcount) return false;
			if (exp != _o_.exp) return false;
			if (nexp != _o_.nexp) return false;
			if (attackapt != _o_.attackapt) return false;
			if (defendapt != _o_.defendapt) return false;
			if (phyforceapt != _o_.phyforceapt) return false;
			if (magicapt != _o_.magicapt) return false;
			if (speedapt != _o_.speedapt) return false;
			if (dodgeapt != _o_.dodgeapt) return false;
			if (growrate != _o_.growrate) return false;
			if (life != _o_.life) return false;
			if (kind != _o_.kind) return false;
			if (!skills.equals(_o_.skills)) return false;
			if (!skillexpires.equals(_o_.skillexpires)) return false;
			if (flag != _o_.flag) return false;
			if (timeout != _o_.timeout) return false;
			if (ownerid != _o_.ownerid) return false;
			if (!ownername.equals(_o_.ownername)) return false;
			if (rank != _o_.rank) return false;
			if (starid != _o_.starid) return false;
			if (practisetimes != _o_.practisetimes) return false;
			if (!zizhi.equals(_o_.zizhi)) return false;
			if (changegengu != _o_.changegengu) return false;
			if (skill_grids != _o_.skill_grids) return false;
			if (aptaddcount != _o_.aptaddcount) return false;
			if (growrateaddcount != _o_.growrateaddcount) return false;
			if (washcount != _o_.washcount) return false;
			if (petscore != _o_.petscore) return false;
			if (petbasescore != _o_.petbasescore) return false;
			if (petdye1 != _o_.petdye1) return false;
			if (petdye2 != _o_.petdye2) return false;
			if (shenshouinccount != _o_.shenshouinccount) return false;
			if (marketfreezeexpire != _o_.marketfreezeexpire) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += id;
		_h_ += key;
		_h_ += name.hashCode();
		_h_ += level;
		_h_ += uselevel;
		_h_ += xuemai;
		_h_ += gengu;
		_h_ += colour;
		_h_ += hp;
		_h_ += maxhp;
		_h_ += mp;
		_h_ += maxmp;
		_h_ += attack;
		_h_ += defend;
		_h_ += speed;
		_h_ += magicattack;
		_h_ += magicdef;
		_h_ += (int)scale;
		_h_ += initbfp.hashCode();
		_h_ += bfp.hashCode();
		_h_ += point;
		_h_ += (int)autoaddcons;
		_h_ += (int)autoaddiq;
		_h_ += (int)autoaddstr;
		_h_ += (int)autoaddendu;
		_h_ += (int)autoaddagi;
		_h_ += pointresetcount;
		_h_ += (int)exp;
		_h_ += (int)nexp;
		_h_ += attackapt;
		_h_ += defendapt;
		_h_ += phyforceapt;
		_h_ += magicapt;
		_h_ += speedapt;
		_h_ += dodgeapt;
		_h_ += Float.floatToIntBits(growrate);
		_h_ += life;
		_h_ += kind;
		_h_ += skills.hashCode();
		_h_ += skillexpires.hashCode();
		_h_ += (int)flag;
		_h_ += (int)timeout;
		_h_ += (int)ownerid;
		_h_ += ownername.hashCode();
		_h_ += rank;
		_h_ += starid;
		_h_ += practisetimes;
		_h_ += zizhi.hashCode();
		_h_ += changegengu;
		_h_ += skill_grids;
		_h_ += (int)aptaddcount;
		_h_ += (int)growrateaddcount;
		_h_ += washcount;
		_h_ += petscore;
		_h_ += petbasescore;
		_h_ += petdye1;
		_h_ += petdye2;
		_h_ += shenshouinccount;
		_h_ += (int)marketfreezeexpire;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(id).append(",");
		_sb_.append(key).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(level).append(",");
		_sb_.append(uselevel).append(",");
		_sb_.append(xuemai).append(",");
		_sb_.append(gengu).append(",");
		_sb_.append(colour).append(",");
		_sb_.append(hp).append(",");
		_sb_.append(maxhp).append(",");
		_sb_.append(mp).append(",");
		_sb_.append(maxmp).append(",");
		_sb_.append(attack).append(",");
		_sb_.append(defend).append(",");
		_sb_.append(speed).append(",");
		_sb_.append(magicattack).append(",");
		_sb_.append(magicdef).append(",");
		_sb_.append(scale).append(",");
		_sb_.append(initbfp).append(",");
		_sb_.append(bfp).append(",");
		_sb_.append(point).append(",");
		_sb_.append(autoaddcons).append(",");
		_sb_.append(autoaddiq).append(",");
		_sb_.append(autoaddstr).append(",");
		_sb_.append(autoaddendu).append(",");
		_sb_.append(autoaddagi).append(",");
		_sb_.append(pointresetcount).append(",");
		_sb_.append(exp).append(",");
		_sb_.append(nexp).append(",");
		_sb_.append(attackapt).append(",");
		_sb_.append(defendapt).append(",");
		_sb_.append(phyforceapt).append(",");
		_sb_.append(magicapt).append(",");
		_sb_.append(speedapt).append(",");
		_sb_.append(dodgeapt).append(",");
		_sb_.append(growrate).append(",");
		_sb_.append(life).append(",");
		_sb_.append(kind).append(",");
		_sb_.append(skills).append(",");
		_sb_.append(skillexpires).append(",");
		_sb_.append(flag).append(",");
		_sb_.append(timeout).append(",");
		_sb_.append(ownerid).append(",");
		_sb_.append("T").append(ownername.length()).append(",");
		_sb_.append(rank).append(",");
		_sb_.append(starid).append(",");
		_sb_.append(practisetimes).append(",");
		_sb_.append(zizhi).append(",");
		_sb_.append(changegengu).append(",");
		_sb_.append(skill_grids).append(",");
		_sb_.append(aptaddcount).append(",");
		_sb_.append(growrateaddcount).append(",");
		_sb_.append(washcount).append(",");
		_sb_.append(petscore).append(",");
		_sb_.append(petbasescore).append(",");
		_sb_.append(petdye1).append(",");
		_sb_.append(petdye2).append(",");
		_sb_.append(shenshouinccount).append(",");
		_sb_.append(marketfreezeexpire).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

