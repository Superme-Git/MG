
#pragma once


namespace gnet { 

class GetUserNickNameArg : public GNET::Rpc::Data
{
public:
	int userid;

	GetUserNickNameArg() {
		userid = 0;
	}

	GetUserNickNameArg(int _userid_)
		: userid(_userid_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new GetUserNickNameArg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const GetUserNickNameArg *o = dynamic_cast<const GetUserNickNameArg*>(&d)) return *this = *o;
		return *this;
	}

	GetUserNickNameArg& operator=(const GetUserNickNameArg & d)
	{
		if (&d == this) return *this;
		userid = d.userid;
		return *this;
	}

	bool operator<(const GetUserNickNameArg &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GetUserNickNameArg &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
