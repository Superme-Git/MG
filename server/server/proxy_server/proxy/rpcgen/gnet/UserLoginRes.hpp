
#pragma once


namespace gnet { 

class UserLoginRes : public GNET::Rpc::Data
{
public:
	char retcode;
	int remain_playtime;
	int func;
	int funcparm;
	char blisgm;
	int free_time_left;
	int free_time_end;
	int creatime;

	UserLoginRes() {
		retcode = -1;
		remain_playtime = 0;
		func = 0;
		funcparm = 0;
		blisgm = 0;
		free_time_left = 0;
		free_time_end = 0;
		creatime = 0;
	}

	UserLoginRes(char _retcode_, int _remain_playtime_, int _func_, int _funcparm_, char _blisgm_, int _free_time_left_, int _free_time_end_, int _creatime_)
		: retcode(_retcode_), remain_playtime(_remain_playtime_), func(_func_), funcparm(_funcparm_), blisgm(_blisgm_), free_time_left(_free_time_left_), free_time_end(_free_time_end_), creatime(_creatime_) {
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
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new UserLoginRes(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const UserLoginRes *o = dynamic_cast<const UserLoginRes*>(&d)) return *this = *o;
		return *this;
	}

	UserLoginRes& operator=(const UserLoginRes & d)
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
		return *this;
	}

	bool operator<(const UserLoginRes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const UserLoginRes &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = remain_playtime - _o_.remain_playtime;
		if (0 != _c_) return _c_;
		_c_ = func - _o_.func;
		if (0 != _c_) return _c_;
		_c_ = funcparm - _o_.funcparm;
		if (0 != _c_) return _c_;
		_c_ = blisgm - _o_.blisgm;
		if (0 != _c_) return _c_;
		_c_ = free_time_left - _o_.free_time_left;
		if (0 != _c_) return _c_;
		_c_ = free_time_end - _o_.free_time_end;
		if (0 != _c_) return _c_;
		_c_ = creatime - _o_.creatime;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
