
#pragma once


namespace fire { 
namespace pb { 
namespace pet { 

class PetColour : public FireNet::Marshal
{
public:
	enum {
		WHITE = 1,
		GREEN = 2,
		BLUE = 3,
		PURPLE = 4,
		ORANGE = 5,
		DARKGOLD = 6,
	};

	PetColour() {
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

	bool operator<(const PetColour &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const PetColour &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
