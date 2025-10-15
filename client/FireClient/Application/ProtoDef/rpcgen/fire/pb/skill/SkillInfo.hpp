
#pragma once


namespace fire { 
namespace pb { 
namespace skill { 

class SkillInfo : public FireNet::Marshal
{
public:
	int id; // 技能id
	int level; // 级别

	SkillInfo() {
		id = 0;
		level = 0;
	}

	SkillInfo(int _id_, int _level_)
		: id(_id_), level(_level_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << id;
		_os_ << level;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> id;
		_os_ >> level;
		return _os_;
	}

	bool operator<(const SkillInfo &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SkillInfo &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = id - _o_.id;
		if (0 != _c_) return _c_;
		_c_ = level - _o_.level;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
} 
