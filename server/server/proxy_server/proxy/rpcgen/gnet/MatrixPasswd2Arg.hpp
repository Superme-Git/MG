
#pragma once


namespace gnet { 

class MatrixPasswd2Arg : public GNET::Rpc::Data
{
public:
	GNET::Octets account;
	GNET::Octets challenge;
	int loginip;

	MatrixPasswd2Arg() {
		loginip = 0;
	}

	MatrixPasswd2Arg(const GNET::Octets & _account_, const GNET::Octets & _challenge_, int _loginip_)
		: account(_account_), challenge(_challenge_), loginip(_loginip_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << account;
		_os_ << challenge;
		_os_ << loginip;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> account;
		_os_ >> challenge;
		_os_ >> loginip;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new MatrixPasswd2Arg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const MatrixPasswd2Arg *o = dynamic_cast<const MatrixPasswd2Arg*>(&d)) return *this = *o;
		return *this;
	}

	MatrixPasswd2Arg& operator=(const MatrixPasswd2Arg & d)
	{
		if (&d == this) return *this;
		account = d.account;
		challenge = d.challenge;
		loginip = d.loginip;
		return *this;
	}

};

} 
