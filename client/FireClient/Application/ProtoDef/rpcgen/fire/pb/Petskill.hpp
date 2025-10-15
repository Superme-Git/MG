
#pragma once


namespace fire { 
namespace pb { 

class Petskill : public FireNet::Marshal
{
public:
	int skillid;
	int skillexp;
	char skilltype; // 0=先天技能  1=通过打书学习的技能
	char certification; // 0=未认证 1=认证

	Petskill() {
		skillid = 0;
		skillexp = 0;
		skilltype = 0;
		certification = 0;
	}

	Petskill(int _skillid_, int _skillexp_, char _skilltype_, char _certification_)
		: skillid(_skillid_), skillexp(_skillexp_), skilltype(_skilltype_), certification(_certification_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << skillid;
		_os_ << skillexp;
		_os_ << skilltype;
		_os_ << certification;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> skillid;
		_os_ >> skillexp;
		_os_ >> skilltype;
		_os_ >> certification;
		return _os_;
	}

	bool operator<(const Petskill &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const Petskill &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = skillid - _o_.skillid;
		if (0 != _c_) return _c_;
		_c_ = skillexp - _o_.skillexp;
		if (0 != _c_) return _c_;
		_c_ = skilltype - _o_.skilltype;
		if (0 != _c_) return _c_;
		_c_ = certification - _o_.certification;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
