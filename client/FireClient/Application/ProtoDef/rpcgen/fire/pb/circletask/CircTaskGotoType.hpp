
#pragma once


namespace fire { 
namespace pb { 
namespace circletask { 

class CircTaskGotoType : public FireNet::Marshal
{
public:
	enum {
		CircTask_GotoInit = 1, // 跳转到初始点
		CircTask_GotoRand = 2, // 跳转到随机点
		CircTask_GotoGuild = 3, // 跳转到公会地图随机点
	};

	CircTaskGotoType() {
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

	bool operator<(const CircTaskGotoType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CircTaskGotoType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
