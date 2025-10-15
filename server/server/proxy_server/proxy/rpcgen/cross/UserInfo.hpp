
#pragma once

#include <vector>

namespace cross { 

class UserInfo : public GNET::Rpc::Data
{
public:
	int userid;
	int func;
	int funcparm;
	int loginip;
	char blisgm;
	std::vector<int> auth;
	int algorithm; // 账号安全级别
	char gender; // 0-female,1-male,2-unknown
	GNET::Octets nickname;

	UserInfo() {
		userid = 0;
		func = 0;
		funcparm = 0;
		loginip = 0;
		blisgm = 0;
		algorithm = 0;
		gender = 0;
	}

	UserInfo(int _userid_, int _func_, int _funcparm_, int _loginip_, char _blisgm_, const std::vector<int> & _auth_, int _algorithm_, char _gender_, const GNET::Octets & _nickname_)
		: userid(_userid_), func(_func_), funcparm(_funcparm_), loginip(_loginip_), blisgm(_blisgm_), auth(_auth_), algorithm(_algorithm_), gender(_gender_), nickname(_nickname_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << func;
		_os_ << funcparm;
		_os_ << loginip;
		_os_ << blisgm;
		_os_ << GNET::MarshalContainer(auth);
		_os_ << algorithm;
		_os_ << gender;
		_os_ << nickname;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> func;
		_os_ >> funcparm;
		_os_ >> loginip;
		_os_ >> blisgm;
		_os_ >> GNET::MarshalContainer(auth);
		_os_ >> algorithm;
		_os_ >> gender;
		_os_ >> nickname;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new UserInfo(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const UserInfo *o = dynamic_cast<const UserInfo*>(&d)) return *this = *o;
		return *this;
	}

	UserInfo& operator=(const UserInfo & d)
	{
		if (&d == this) return *this;
		userid = d.userid;
		func = d.func;
		funcparm = d.funcparm;
		loginip = d.loginip;
		blisgm = d.blisgm;
		auth = d.auth;
		algorithm = d.algorithm;
		gender = d.gender;
		nickname = d.nickname;
		return *this;
	}

};

} 
