
#pragma once

#include <list>
#include <map>

namespace fire { 
namespace pb { 
namespace battle { 

class FighterInfo : public FireNet::Marshal
{
public:
	int fightertype; // 战斗单元类型
	int64_t dataid; // 战斗单元的标记 人物为人物的roleid 宠物和怪物则为表中的baseID
	std::wstring fightername; // 战斗单元名称
	std::wstring title; // 战斗单元称谓
	int titleid; // 战斗单元称谓ID
	int awakestate; // 觉醒状态 00000 5个bit位，从低位到高位分别标识95至99状态，0-未觉醒，1-觉醒（具体值是int数值）
	int index; // 战斗单元在战斗中的相对位置
	char bgm; // 是否是GM,0:否 1：是
	int maxhp; // 最大血量
	int uplimithp; // 血量上限
	int hp; // 当前血量
	int ep; // 当前效果点
	int shape; // 造型	造型为short值
	int subtype; // 子类型，宠物为资质，怪物为宝宝野宠等
	std::map<char, int> components; // 角色换装信息key值参考SpriteComponents的枚举，value为0代表脱下某件换装
	std::map<int, int> buffs; // 添加的战斗者拥有的在战斗内显示的buff，value为回合数（为0则没有回合限制）
	int footlogoid; // 足印id
	std::list<int> petkeys; // 已经出战的宠物

	FighterInfo() {
		fightertype = 0;
		dataid = 0;
		titleid = 0;
		awakestate = 0;
		index = 0;
		bgm = 0;
		maxhp = 0;
		uplimithp = 0;
		hp = 0;
		ep = 0;
		shape = 0;
		subtype = 0;
		footlogoid = 0;
	}

	FighterInfo(int _fightertype_, int64_t _dataid_, const std::wstring & _fightername_, const std::wstring & _title_, int _titleid_, int _awakestate_, int _index_, char _bgm_, int _maxhp_, int _uplimithp_, int _hp_, int _ep_, int _shape_, int _subtype_, const std::map<char, int> & _components_, const std::map<int, int> & _buffs_, int _footlogoid_, const std::list<int> & _petkeys_)
		: fightertype(_fightertype_), dataid(_dataid_), fightername(_fightername_), title(_title_), titleid(_titleid_), awakestate(_awakestate_), index(_index_), bgm(_bgm_), maxhp(_maxhp_), uplimithp(_uplimithp_), hp(_hp_), ep(_ep_), shape(_shape_), subtype(_subtype_), components(_components_), buffs(_buffs_), footlogoid(_footlogoid_), petkeys(_petkeys_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << fightertype;
		_os_ << dataid;
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(fightername.length() * 2);
		for(size_t pos =0; pos < fightername.length(); pos++) {
		utf16 ch = (utf16)fightername[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		{
		// utf32 to utf16
		typedef unsigned short utf16;
		FireNet::Octets oct(title.length() * 2);
		for(size_t pos =0; pos < title.length(); pos++) {
		utf16 ch = (utf16)title[pos];
		oct.insert(oct.end(), &ch, 2);
		}
		_os_ << oct; //only for LE
		}
		_os_ << titleid;
		_os_ << awakestate;
		_os_ << index;
		_os_ << bgm;
		_os_ << maxhp;
		_os_ << uplimithp;
		_os_ << hp;
		_os_ << ep;
		_os_ << shape;
		_os_ << subtype;
		_os_ << FireNet::MarshalContainer(components);
		_os_ << FireNet::MarshalContainer(buffs);
		_os_ << footlogoid;
		_os_ << FireNet::MarshalContainer(petkeys);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> fightertype;
		_os_ >> dataid;
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		fightername.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		fightername.insert(fightername.end(), (wchar_t)*ch);
		}
		}
		{
		FireNet::Octets oct;
		_os_ >> oct ;
		if(oct.size() % 2 != 0) {
		throw Marshal::Exception();
		}
		title.reserve(oct.size()/2);
		typedef unsigned short utf16;
		// only for little endian
		for(utf16* ch = (utf16*)oct.begin(); ch != oct.end(); ch++)
		{
		title.insert(title.end(), (wchar_t)*ch);
		}
		}
		_os_ >> titleid;
		_os_ >> awakestate;
		_os_ >> index;
		_os_ >> bgm;
		_os_ >> maxhp;
		_os_ >> uplimithp;
		_os_ >> hp;
		_os_ >> ep;
		_os_ >> shape;
		_os_ >> subtype;
		_os_ >> FireNet::MarshalContainer(components);
		_os_ >> FireNet::MarshalContainer(buffs);
		_os_ >> footlogoid;
		_os_ >> FireNet::MarshalContainer(petkeys);
		return _os_;
	}

};

} 
} 
} 
