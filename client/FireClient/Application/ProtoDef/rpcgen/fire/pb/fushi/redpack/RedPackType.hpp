
#pragma once


namespace fire { 
namespace pb { 
namespace fushi { 
namespace redpack { 

class RedPackType : public FireNet::Marshal
{
public:
	enum {
		TYPE_WORLD = 1, // 世界红包
		TYPE_CLAN = 2, // 公会红包
		TYPE_TEAM = 3, // 队伍红包
	};

	RedPackType() {
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

	bool operator<(const RedPackType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const RedPackType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
} 
