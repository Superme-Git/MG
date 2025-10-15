
#pragma once


namespace fire { 
namespace pb { 
namespace master { 

class RelationType : public FireNet::Marshal
{
public:
	enum {
		MASTER = 0, // Ê¦¸¸
		PRENTICE = 1, // Í½µÜ
	};

	RelationType() {
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

	bool operator<(const RelationType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const RelationType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
