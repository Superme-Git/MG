
#pragma once


namespace fire { 
namespace pb { 
namespace master { 

class OnLineState : public FireNet::Marshal
{
public:
	enum {
		NOT_ON_LINE = 0, // 不在线
		ON_LINE = 1, // 在线
	};

	OnLineState() {
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

	bool operator<(const OnLineState &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const OnLineState &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
