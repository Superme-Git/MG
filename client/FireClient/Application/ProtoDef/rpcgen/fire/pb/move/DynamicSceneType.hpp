
#pragma once


namespace fire { 
namespace pb { 
namespace move { 

class DynamicSceneType : public FireNet::Marshal
{
public:
	enum {
		NORMAL_SCENE = 0, // 普通动态场景
		FACTION_SCENE = 1, // 公会场景
		FAMILY_SCENE = 2, // 场景
		DREAMLAND_SCENE = 4, // 虚幻境场景
	};

	DynamicSceneType() {
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

	bool operator<(const DynamicSceneType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const DynamicSceneType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
