
#pragma once

#include <map>

namespace gnet { 

class ServerAttr : public FireNet::Marshal
{
public:
	enum {
		FLAG_DOUBLE_DROP = 4,
		FLAG_DOUBLE_SP = 8,
	};

	int flags; // 当前服务器状态，即上面列出的状态
	char load;
	std::map<int, int> extra;

	ServerAttr() {
		flags = 0;
		load = 0;
	}

	ServerAttr(int _flags_, char _load_, const std::map<int, int> & _extra_)
		: flags(_flags_), load(_load_), extra(_extra_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << flags;
		_os_ << load;
		_os_ << FireNet::MarshalContainer(extra);
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> flags;
		_os_ >> load;
		_os_ >> FireNet::MarshalContainer(extra);
		return _os_;
	}

};

} 
