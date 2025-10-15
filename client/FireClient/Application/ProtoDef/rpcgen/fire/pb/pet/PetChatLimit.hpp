
#pragma once


namespace fire { 
namespace pb { 
namespace pet { 

class PetChatLimit : public FireNet::Marshal
{
public:
	enum {
		Self20percentBegin = 0,
		Self20percentEnd = 1,
		Master20percentBegin = 2,
		Master20percentEnd = 3,
	};

	PetChatLimit() {
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

	bool operator<(const PetChatLimit &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const PetChatLimit &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
