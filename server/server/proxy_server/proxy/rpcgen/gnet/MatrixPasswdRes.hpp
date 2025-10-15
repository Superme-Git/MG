
#pragma once


namespace gnet { 

class MatrixPasswdRes : public GNET::Rpc::Data
{
public:
	int retcode;
	int userid;
	int algorithm;
	GNET::Octets response;
	GNET::Octets matrix;

	MatrixPasswdRes() {
		retcode = -1;
		userid = -1;
		algorithm = 0;
	}

	MatrixPasswdRes(int _retcode_, int _userid_, int _algorithm_, const GNET::Octets & _response_, const GNET::Octets & _matrix_)
		: retcode(_retcode_), userid(_userid_), algorithm(_algorithm_), response(_response_), matrix(_matrix_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << userid;
		_os_ << algorithm;
		_os_ << response;
		_os_ << matrix;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> userid;
		_os_ >> algorithm;
		_os_ >> response;
		_os_ >> matrix;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new MatrixPasswdRes(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const MatrixPasswdRes *o = dynamic_cast<const MatrixPasswdRes*>(&d)) return *this = *o;
		return *this;
	}

	MatrixPasswdRes& operator=(const MatrixPasswdRes & d)
	{
		if (&d == this) return *this;
		retcode = d.retcode;
		userid = d.userid;
		algorithm = d.algorithm;
		response = d.response;
		matrix = d.matrix;
		return *this;
	}

};

} 
