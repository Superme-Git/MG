
#pragma once


namespace gnet { 

class GetUserNickNameRes : public GNET::Rpc::Data
{
public:
	int retcode;
	GNET::Octets nickname;
	int reserved;

	GetUserNickNameRes() {
		retcode = 0;
		reserved = 0;
	}

	GetUserNickNameRes(int _retcode_, const GNET::Octets & _nickname_, int _reserved_)
		: retcode(_retcode_), nickname(_nickname_), reserved(_reserved_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << nickname;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> nickname;
		_os_ >> reserved;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new GetUserNickNameRes(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const GetUserNickNameRes *o = dynamic_cast<const GetUserNickNameRes*>(&d)) return *this = *o;
		return *this;
	}

	GetUserNickNameRes& operator=(const GetUserNickNameRes & d)
	{
		if (&d == this) return *this;
		retcode = d.retcode;
		nickname = d.nickname;
		reserved = d.reserved;
		return *this;
	}

};

} 
