
#pragma once


namespace fire { 
namespace pb { 
namespace clan { 

class ClanSkill : public FireNet::Marshal
{
public:
	int clanskillid; // 技能id
	int clanskilllevel; // 技能当前等级
	int clanskillmaxlevel; // 技能等级上限
	int clanskillcurexp; // 技能当前经验
	int clanskilllevelupexp; // 技能升级所需经验

	ClanSkill() {
		clanskillid = 0;
		clanskilllevel = 0;
		clanskillmaxlevel = 0;
		clanskillcurexp = 0;
		clanskilllevelupexp = 0;
	}

	ClanSkill(int _clanskillid_, int _clanskilllevel_, int _clanskillmaxlevel_, int _clanskillcurexp_, int _clanskilllevelupexp_)
		: clanskillid(_clanskillid_), clanskilllevel(_clanskilllevel_), clanskillmaxlevel(_clanskillmaxlevel_), clanskillcurexp(_clanskillcurexp_), clanskilllevelupexp(_clanskilllevelupexp_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << clanskillid;
		_os_ << clanskilllevel;
		_os_ << clanskillmaxlevel;
		_os_ << clanskillcurexp;
		_os_ << clanskilllevelupexp;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> clanskillid;
		_os_ >> clanskilllevel;
		_os_ >> clanskillmaxlevel;
		_os_ >> clanskillcurexp;
		_os_ >> clanskilllevelupexp;
		return _os_;
	}

	bool operator<(const ClanSkill &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const ClanSkill &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = clanskillid - _o_.clanskillid;
		if (0 != _c_) return _c_;
		_c_ = clanskilllevel - _o_.clanskilllevel;
		if (0 != _c_) return _c_;
		_c_ = clanskillmaxlevel - _o_.clanskillmaxlevel;
		if (0 != _c_) return _c_;
		_c_ = clanskillcurexp - _o_.clanskillcurexp;
		if (0 != _c_) return _c_;
		_c_ = clanskilllevelupexp - _o_.clanskilllevelupexp;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
} 
