
#pragma once

#include "gnet/RoleForbid.hpp"
#include <vector>

namespace gnet { 

class MatrixPasswd2Res : public GNET::Rpc::Data
{
public:
	int retcode;
	int userid;
	int algorithm;
	GNET::Octets response;
	GNET::Octets matrix;
	GNET::Octets seed;
	GNET::Octets pin;
	int rtime;
	int currenttime;
	std::vector<gnet::RoleForbid> forbid;
	GNET::Octets last_used_elec_number;

	MatrixPasswd2Res() {
		retcode = -1;
		userid = -1;
		algorithm = 0;
		rtime = 0;
		currenttime = 0;
	}

	MatrixPasswd2Res(int _retcode_, int _userid_, int _algorithm_, const GNET::Octets & _response_, const GNET::Octets & _matrix_, const GNET::Octets & _seed_, const GNET::Octets & _pin_, int _rtime_, int _currenttime_, const std::vector<gnet::RoleForbid> & _forbid_, const GNET::Octets & _last_used_elec_number_)
		: retcode(_retcode_), userid(_userid_), algorithm(_algorithm_), response(_response_), matrix(_matrix_), seed(_seed_), pin(_pin_), rtime(_rtime_), currenttime(_currenttime_), forbid(_forbid_), last_used_elec_number(_last_used_elec_number_) {
	}

	bool _validator_() const {
		for (std::vector<gnet::RoleForbid>::const_iterator _i_ = forbid.begin(); _i_ != forbid.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << userid;
		_os_ << algorithm;
		_os_ << response;
		_os_ << matrix;
		_os_ << seed;
		_os_ << pin;
		_os_ << rtime;
		_os_ << currenttime;
		_os_ << GNET::MarshalContainer(forbid);
		_os_ << last_used_elec_number;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> userid;
		_os_ >> algorithm;
		_os_ >> response;
		_os_ >> matrix;
		_os_ >> seed;
		_os_ >> pin;
		_os_ >> rtime;
		_os_ >> currenttime;
		_os_ >> GNET::MarshalContainer(forbid);
		_os_ >> last_used_elec_number;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new MatrixPasswd2Res(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const MatrixPasswd2Res *o = dynamic_cast<const MatrixPasswd2Res*>(&d)) return *this = *o;
		return *this;
	}

	MatrixPasswd2Res& operator=(const MatrixPasswd2Res & d)
	{
		if (&d == this) return *this;
		retcode = d.retcode;
		userid = d.userid;
		algorithm = d.algorithm;
		response = d.response;
		matrix = d.matrix;
		seed = d.seed;
		pin = d.pin;
		rtime = d.rtime;
		currenttime = d.currenttime;
		forbid = d.forbid;
		last_used_elec_number = d.last_used_elec_number;
		return *this;
	}

};

} 
