
#pragma once

#include <vector>

namespace gnet { 

class UserLogin2Res : public GNET::Rpc::Data
{
public:
	int retcode;
	int remain_playtime;
	int func;
	int funcparm;
	char blisgm;
	int free_time_left;
	int free_time_end;
	int creatime;
	int adduppoint;
	int soldpoint;
	std::vector<char> auth;
	char gender;
	int remain_coupon;
	int today_exchange_coupon;
	int today_remain_exchange_coupon;
	GNET::Octets nickname;

	UserLogin2Res() {
		retcode = -1;
		remain_playtime = 0;
		func = 0;
		funcparm = 0;
		blisgm = 0;
		free_time_left = 0;
		free_time_end = 0;
		creatime = 0;
		adduppoint = 0;
		soldpoint = 0;
		gender = 0;
		remain_coupon = 0;
		today_exchange_coupon = 0;
		today_remain_exchange_coupon = 0;
	}

	UserLogin2Res(int _retcode_, int _remain_playtime_, int _func_, int _funcparm_, char _blisgm_, int _free_time_left_, int _free_time_end_, int _creatime_, int _adduppoint_, int _soldpoint_, const std::vector<char> & _auth_, char _gender_, int _remain_coupon_, int _today_exchange_coupon_, int _today_remain_exchange_coupon_, const GNET::Octets & _nickname_)
		: retcode(_retcode_), remain_playtime(_remain_playtime_), func(_func_), funcparm(_funcparm_), blisgm(_blisgm_), free_time_left(_free_time_left_), free_time_end(_free_time_end_), creatime(_creatime_), adduppoint(_adduppoint_), soldpoint(_soldpoint_), auth(_auth_), gender(_gender_), remain_coupon(_remain_coupon_), today_exchange_coupon(_today_exchange_coupon_), today_remain_exchange_coupon(_today_remain_exchange_coupon_), nickname(_nickname_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << remain_playtime;
		_os_ << func;
		_os_ << funcparm;
		_os_ << blisgm;
		_os_ << free_time_left;
		_os_ << free_time_end;
		_os_ << creatime;
		_os_ << adduppoint;
		_os_ << soldpoint;
		_os_ << GNET::MarshalContainer(auth);
		_os_ << gender;
		_os_ << remain_coupon;
		_os_ << today_exchange_coupon;
		_os_ << today_remain_exchange_coupon;
		_os_ << nickname;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> remain_playtime;
		_os_ >> func;
		_os_ >> funcparm;
		_os_ >> blisgm;
		_os_ >> free_time_left;
		_os_ >> free_time_end;
		_os_ >> creatime;
		_os_ >> adduppoint;
		_os_ >> soldpoint;
		_os_ >> GNET::MarshalContainer(auth);
		_os_ >> gender;
		_os_ >> remain_coupon;
		_os_ >> today_exchange_coupon;
		_os_ >> today_remain_exchange_coupon;
		_os_ >> nickname;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new UserLogin2Res(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const UserLogin2Res *o = dynamic_cast<const UserLogin2Res*>(&d)) return *this = *o;
		return *this;
	}

	UserLogin2Res& operator=(const UserLogin2Res & d)
	{
		if (&d == this) return *this;
		retcode = d.retcode;
		remain_playtime = d.remain_playtime;
		func = d.func;
		funcparm = d.funcparm;
		blisgm = d.blisgm;
		free_time_left = d.free_time_left;
		free_time_end = d.free_time_end;
		creatime = d.creatime;
		adduppoint = d.adduppoint;
		soldpoint = d.soldpoint;
		auth = d.auth;
		gender = d.gender;
		remain_coupon = d.remain_coupon;
		today_exchange_coupon = d.today_exchange_coupon;
		today_remain_exchange_coupon = d.today_remain_exchange_coupon;
		nickname = d.nickname;
		return *this;
	}

};

} 
