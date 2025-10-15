
#pragma once


namespace fire { 
namespace pb { 
namespace pet { 

class PetTypeEnum : public FireNet::Marshal
{
public:
	enum {
		WILD = 1, // “∞…˙
		BABY = 2, // ±¶±¶
		VARIATION = 3, // ±‰“Ï
		SACREDANIMAL = 4, // …Ò ﬁ
	};

	PetTypeEnum() {
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

	bool operator<(const PetTypeEnum &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const PetTypeEnum &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
