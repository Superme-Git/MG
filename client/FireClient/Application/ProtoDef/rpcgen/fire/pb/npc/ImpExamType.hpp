
#pragma once


namespace fire { 
namespace pb { 
namespace npc { 

class ImpExamType : public FireNet::Marshal
{
public:
	enum {
		IMPEXAM_VILL = 1,
		IMPEXAM_PROV = 2,
		IMPEXAM_STATE = 3,
	};

	ImpExamType() {
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

	bool operator<(const ImpExamType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ImpExamType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
