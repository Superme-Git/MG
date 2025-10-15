
#pragma once


namespace fire { 
namespace pb { 
namespace battle { 

class DemoBuff : public FireNet::Marshal
{
public:
	int fighterid;
	int buffid;
	int round; // -1为删除该buff，0为没有回合数的buff，>0为回合数

	DemoBuff() {
		fighterid = 0;
		buffid = 0;
		round = 0;
	}

	DemoBuff(int _fighterid_, int _buffid_, int _round_)
		: fighterid(_fighterid_), buffid(_buffid_), round(_round_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << fighterid;
		_os_ << buffid;
		_os_ << round;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> fighterid;
		_os_ >> buffid;
		_os_ >> round;
		return _os_;
	}

	bool operator<(const DemoBuff &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const DemoBuff &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = fighterid - _o_.fighterid;
		if (0 != _c_) return _c_;
		_c_ = buffid - _o_.buffid;
		if (0 != _c_) return _c_;
		_c_ = round - _o_.round;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
} 
} 
