
#pragma once

#include "fire/pb/move/Pos.hpp"
#include <map>

namespace fire { 
namespace pb { 
namespace move { 

class NpcBasic : public FireNet::Marshal
{
public:
	int64_t npckey; // npckey :npc的唯一ID
	int id; // npcid: npc的基本ID
	std::wstring name;
	fire::pb::move::Pos pos;
	char posz;
	fire::pb::move::Pos destpos; // 运动中的NPC的目的点
	int speed; // 移动NPC速度
	int dir;
	int shape; // npc 造型
	int scenestate; // npc 状态
	std::map<char, int> components; // 角色换装信息key值参考SpriteComponents的枚举

	NpcBasic() {
		npckey = 0;
		id = 0;
		posz = 0;
		speed = 0;
		dir = 0;
		shape = 0;
		scenestate = 0;
	}

	NpcBasic(int64_t _npckey_, int _id_, const std::wstring & _name_, const fire::pb::move::Pos & _pos_, char _posz_, const fire::pb::move::Pos & _destpos_, int _speed_, int _dir_, int _shape_, int _scenestate_, const std::map<char, int> & _components_)
		: npckey(_npckey_), id(_id_), name(_name_), pos(_pos_), posz(_posz_), destpos(_destpos_), speed(_speed_), dir(_dir_), shape(_shape_), scenestate(_scenestate_), components(_components_) {
	}

	bool _validator_() const {
		if (!pos._validator_()) return false;
		if (!destpos._validator_()) return false;
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << npckey;
		_os_ << id;
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
		_os_ << pos;
		_os_ << posz;
		_os_ << destpos;
		_os_ << speed;
		_os_ << dir;
		_os_ << shape;
		_os_ << scenestate;
		_os_ << FireNet::MarshalContainer(components);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> npckey;
		_os_ >> id;
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
		_os_ >> pos;
		_os_ >> posz;
		_os_ >> destpos;
		_os_ >> speed;
		_os_ >> dir;
		_os_ >> shape;
		_os_ >> scenestate;
		_os_ >> FireNet::MarshalContainer(components);
		return _os_;
	}

};

} 
} 
} 
