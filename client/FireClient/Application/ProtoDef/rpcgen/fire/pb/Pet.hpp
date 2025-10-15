
#pragma once

#include "fire/pb/BasicFightProperties.hpp"
#include "fire/pb/Petskill.hpp"
#include <list>
#include <map>

namespace fire { 
namespace pb { 

class Pet : public FireNet::Marshal
{
public:
	enum {
		FLAG_LOCK = 1,
		FLAG_BIND = 2,
	};

	int id; // 宠物ID
	int key; // key
	std::wstring name; // 名称
	int level; // 等级
	int uselevel; // 参战等级
	int xuemai; // 宠物当前资质浓度值
	int gengu; // root-bone
	int colour; // 颜色值
	int hp; // 当前生命
	int maxhp; // 最大生命
	int mp; // 当前法力
	int maxmp; // 最大法力
	int attack; // 攻击
	int defend; // 防御
	int speed; // 速度
	int magicattack; // 法术攻击
	int magicdef; // 法术防御
	char scale; // 1,2,3,4分别代表4个size
	fire::pb::BasicFightProperties initbfp; // 初始的基础战斗属性
	fire::pb::BasicFightProperties bfp; // 基础战斗属性
	short point; // 潜能。未分配点数
	char autoaddcons; // 加点方案-升级自动加体质
	char autoaddiq; // 加点方案-升级自动加智力
	char autoaddstr; // 加点方案-升级自动加力量
	char autoaddendu; // 加点方案-升级自动加耐力
	char autoaddagi; // 加点方案-升级自动加敏捷
	short pointresetcount; // 宠物加点的重置次数
	int64_t exp; // 当前经验
	int64_t nexp; // 升级需要经验
	int attackapt; // 攻击资质
	int defendapt; // 防御资质
	int phyforceapt; // 体力资质
	int magicapt; // 法力资质
	int speedapt; // 速度资质
	int dodgeapt; // 躲闪资质
	float growrate; // 成长率
	int life; // 寿命
	int kind; // 宠物类型 野生、宝宝
	std::list<fire::pb::Petskill> skills; // 宠物技能
	std::map<int, int64_t> skillexpires; // 有时限的宠物技能的到期时间。key=技能id，value=到期时间
	char flag; // 宠物标志 1= 锁定，2 = 绑定
	int64_t timeout; // 如果是时间锁，到期时间（毫秒）
	int64_t ownerid; // 主人id
	std::wstring ownername; // 主人名
	int rank; // 排行榜排名
	short starid; // 当前星星等级Id
	short practisetimes; // 当前剩余的训练次数
	std::map<int, int> zizhi; // 训练后得到的未分配的资质
	int changegengu; // 训练后得到的root-bone变化值
	int skill_grids; // 宠物技能格子数
	char aptaddcount; // 资质培养次数
	char growrateaddcount; // 成长率培养次数
	short washcount; // 洗练次数
	int petscore; // 宠物评分
	int petbasescore; // 宠物一级属性评分
	int petdye1; // 宠物染色部位1
	int petdye2; // 宠物染色部位2
	int shenshouinccount; // 神兽养成次数
	int64_t marketfreezeexpire; // 摆摊冻结截止时间,默认0不冻结

	Pet() {
		id = 0;
		key = 0;
		level = 0;
		uselevel = 0;
		xuemai = 0;
		gengu = 0;
		colour = 0;
		hp = 0;
		maxhp = 0;
		mp = 0;
		maxmp = 0;
		attack = 0;
		defend = 0;
		speed = 0;
		magicattack = 0;
		magicdef = 0;
		scale = 0;
		point = 0;
		autoaddcons = 0;
		autoaddiq = 0;
		autoaddstr = 0;
		autoaddendu = 0;
		autoaddagi = 0;
		pointresetcount = 0;
		exp = 0;
		nexp = 0;
		attackapt = 0;
		defendapt = 0;
		phyforceapt = 0;
		magicapt = 0;
		speedapt = 0;
		dodgeapt = 0;
		growrate = 0.0f;
		life = 0;
		kind = 0;
		flag = 0;
		timeout = 0;
		ownerid = 0;
		rank = 0;
		starid = 0;
		practisetimes = 0;
		changegengu = 0;
		skill_grids = 0;
		aptaddcount = 0;
		growrateaddcount = 0;
		washcount = 0;
		petscore = 0;
		petbasescore = 0;
		petdye1 = 0;
		petdye2 = 0;
		shenshouinccount = 0;
		marketfreezeexpire = 0;
	}

	Pet(int _id_, int _key_, const std::wstring & _name_, int _level_, int _uselevel_, int _xuemai_, int _gengu_, int _colour_, int _hp_, int _maxhp_, int _mp_, int _maxmp_, int _attack_, int _defend_, int _speed_, int _magicattack_, int _magicdef_, char _scale_, const fire::pb::BasicFightProperties & _initbfp_, const fire::pb::BasicFightProperties & _bfp_, short _point_, char _autoaddcons_, char _autoaddiq_, char _autoaddstr_, char _autoaddendu_, char _autoaddagi_, short _pointresetcount_, int64_t _exp_, int64_t _nexp_, int _attackapt_, int _defendapt_, int _phyforceapt_, int _magicapt_, int _speedapt_, int _dodgeapt_, float _growrate_, int _life_, int _kind_, const std::list<fire::pb::Petskill> & _skills_, const std::map<int, int64_t> & _skillexpires_, char _flag_, int64_t _timeout_, int64_t _ownerid_, const std::wstring & _ownername_, int _rank_, short _starid_, short _practisetimes_, const std::map<int, int> & _zizhi_, int _changegengu_, int _skill_grids_, char _aptaddcount_, char _growrateaddcount_, short _washcount_, int _petscore_, int _petbasescore_, int _petdye1_, int _petdye2_, int _shenshouinccount_, int64_t _marketfreezeexpire_)
		: id(_id_), key(_key_), name(_name_), level(_level_), uselevel(_uselevel_), xuemai(_xuemai_), gengu(_gengu_), colour(_colour_), hp(_hp_), maxhp(_maxhp_), mp(_mp_), maxmp(_maxmp_), attack(_attack_), defend(_defend_), speed(_speed_), magicattack(_magicattack_), magicdef(_magicdef_), scale(_scale_), initbfp(_initbfp_), bfp(_bfp_), point(_point_), autoaddcons(_autoaddcons_), autoaddiq(_autoaddiq_), autoaddstr(_autoaddstr_), autoaddendu(_autoaddendu_), autoaddagi(_autoaddagi_), pointresetcount(_pointresetcount_), exp(_exp_), nexp(_nexp_), attackapt(_attackapt_), defendapt(_defendapt_), phyforceapt(_phyforceapt_), magicapt(_magicapt_), speedapt(_speedapt_), dodgeapt(_dodgeapt_), growrate(_growrate_), life(_life_), kind(_kind_), skills(_skills_), skillexpires(_skillexpires_), flag(_flag_), timeout(_timeout_), ownerid(_ownerid_), ownername(_ownername_), rank(_rank_), starid(_starid_), practisetimes(_practisetimes_), zizhi(_zizhi_), changegengu(_changegengu_), skill_grids(_skill_grids_), aptaddcount(_aptaddcount_), growrateaddcount(_growrateaddcount_), washcount(_washcount_), petscore(_petscore_), petbasescore(_petbasescore_), petdye1(_petdye1_), petdye2(_petdye2_), shenshouinccount(_shenshouinccount_), marketfreezeexpire(_marketfreezeexpire_) {
	}

	bool _validator_() const {
		if (id < 1) return false;
		if (!initbfp._validator_()) return false;
		if (!bfp._validator_()) return false;
		if (autoaddcons < 0) return false;
		if (autoaddiq < 0) return false;
		if (autoaddstr < 0) return false;
		if (autoaddendu < 0) return false;
		if (autoaddagi < 0) return false;
		for (std::list<fire::pb::Petskill>::const_iterator _i_ = skills.begin(); _i_ != skills.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << id;
		_os_ << key;
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(name.length() * 2);
		for(size_t pos =0; pos < name.length(); pos++) {
		utf16 ch = (utf16)name[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		_os_ << level;
		_os_ << uselevel;
		_os_ << xuemai;
		_os_ << gengu;
		_os_ << colour;
		_os_ << hp;
		_os_ << maxhp;
		_os_ << mp;
		_os_ << maxmp;
		_os_ << attack;
		_os_ << defend;
		_os_ << speed;
		_os_ << magicattack;
		_os_ << magicdef;
		_os_ << scale;
		_os_ << initbfp;
		_os_ << bfp;
		_os_ << point;
		_os_ << autoaddcons;
		_os_ << autoaddiq;
		_os_ << autoaddstr;
		_os_ << autoaddendu;
		_os_ << autoaddagi;
		_os_ << pointresetcount;
		_os_ << exp;
		_os_ << nexp;
		_os_ << attackapt;
		_os_ << defendapt;
		_os_ << phyforceapt;
		_os_ << magicapt;
		_os_ << speedapt;
		_os_ << dodgeapt;
		_os_ << growrate;
		_os_ << life;
		_os_ << kind;
		_os_ << FireNet::MarshalContainer(skills);
		_os_ << FireNet::MarshalContainer(skillexpires);
		_os_ << flag;
		_os_ << timeout;
		_os_ << ownerid;
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(ownername.length() * 2);
		for(size_t pos =0; pos < ownername.length(); pos++) {
		utf16 ch = (utf16)ownername[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		_os_ << rank;
		_os_ << starid;
		_os_ << practisetimes;
		_os_ << FireNet::MarshalContainer(zizhi);
		_os_ << changegengu;
		_os_ << skill_grids;
		_os_ << aptaddcount;
		_os_ << growrateaddcount;
		_os_ << washcount;
		_os_ << petscore;
		_os_ << petbasescore;
		_os_ << petdye1;
		_os_ << petdye2;
		_os_ << shenshouinccount;
		_os_ << marketfreezeexpire;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> id;
		_os_ >> key;
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		name.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		name.insert(name.end(), (wchar_t)*ch);
		}
		}
		_os_ >> level;
		_os_ >> uselevel;
		_os_ >> xuemai;
		_os_ >> gengu;
		_os_ >> colour;
		_os_ >> hp;
		_os_ >> maxhp;
		_os_ >> mp;
		_os_ >> maxmp;
		_os_ >> attack;
		_os_ >> defend;
		_os_ >> speed;
		_os_ >> magicattack;
		_os_ >> magicdef;
		_os_ >> scale;
		_os_ >> initbfp;
		_os_ >> bfp;
		_os_ >> point;
		_os_ >> autoaddcons;
		_os_ >> autoaddiq;
		_os_ >> autoaddstr;
		_os_ >> autoaddendu;
		_os_ >> autoaddagi;
		_os_ >> pointresetcount;
		_os_ >> exp;
		_os_ >> nexp;
		_os_ >> attackapt;
		_os_ >> defendapt;
		_os_ >> phyforceapt;
		_os_ >> magicapt;
		_os_ >> speedapt;
		_os_ >> dodgeapt;
		_os_ >> growrate;
		_os_ >> life;
		_os_ >> kind;
		_os_ >> FireNet::MarshalContainer(skills);
		_os_ >> FireNet::MarshalContainer(skillexpires);
		_os_ >> flag;
		_os_ >> timeout;
		_os_ >> ownerid;
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		ownername.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		ownername.insert(ownername.end(), (wchar_t)*ch);
		}
		}
		_os_ >> rank;
		_os_ >> starid;
		_os_ >> practisetimes;
		_os_ >> FireNet::MarshalContainer(zizhi);
		_os_ >> changegengu;
		_os_ >> skill_grids;
		_os_ >> aptaddcount;
		_os_ >> growrateaddcount;
		_os_ >> washcount;
		_os_ >> petscore;
		_os_ >> petbasescore;
		_os_ >> petdye1;
		_os_ >> petdye2;
		_os_ >> shenshouinccount;
		_os_ >> marketfreezeexpire;
		return _os_;
	}

};

} 
} 
