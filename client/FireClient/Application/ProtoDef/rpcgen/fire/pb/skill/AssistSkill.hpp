
#pragma once


namespace fire { 
namespace pb { 
namespace skill { 

class AssistSkill : public FireNet::Marshal
{
public:
	int id; // 辅助技能ID
	int level; // 级别
	int exp; // 熟练度

	AssistSkill() {
		id = 0;
		level = 0;
		exp = 0;
	}

	AssistSkill(int _id_, int _level_, int _exp_)
		: id(_id_), level(_level_), exp(_exp_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << id;
		_os_ << level;
		_os_ << exp;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> id;
		_os_ >> level;
		_os_ >> exp;
		return _os_;
	}

	bool operator<(const AssistSkill &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const AssistSkill &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		_c_ = level - _o_.level;
		if (0 != _c_) return _c_;
		_c_ = exp - _o_.exp;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
} 
