
#pragma once


namespace fire { 
namespace pb { 
namespace move { 

class SpriteComponents : public FireNet::Marshal
{
public:
	enum {
		SPRITE_WEAPON = 1, // 武器
		SPRITE_HEADDRESS = 2, // 头饰
		SPRITE_BACKDRESS = 3, // 背饰
		SPRITE_FACEDRESS1 = 4, // 面饰1
		SPRITE_FACEDRESS2 = 5, // 面饰2
		SPRITE_HORSEDRESS = 6, // 坐骑
		SPRITE_WEAPONCOLOR = 7, // 武器颜色	武器颜色	byte 1为白色，2为绿色。。4为紫色 5为橙色
		SPRITE_FASHION = 8, // 时装
		ROLE_COLOR1 = 50, // 染色部位1
		ROLE_COLOR2 = 51, // 染色部位2
		SPRITE_EQUIP_EFFECT = 60, // 装备特效
	};

	SpriteComponents() {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		return _os_;
	}

	bool operator<(const SpriteComponents &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SpriteComponents &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
