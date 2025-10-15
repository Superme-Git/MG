
#pragma once


namespace gnet { 

class SetUserNickNameArg : public GNET::Rpc::Data
{
public:
	int userid;
	GNET::Octets nickname;
	int reserved;

	SetUserNickNameArg() {
		userid = 0;
		reserved = 0;
	}

	SetUserNickNameArg(int _userid_, const GNET::Octets & _nickname_, int _reserved_)
		: userid(_userid_), nickname(_nickname_), reserved(_reserved_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << nickname;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> nickname;
		_os_ >> reserved;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new SetUserNickNameArg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const SetUserNickNameArg *o = dynamic_cast<const SetUserNickNameArg*>(&d)) return *this = *o;
		return *this;
	}

	SetUserNickNameArg& operator=(const SetUserNickNameArg & d)
	{
		if (&d == this) return *this;
		userid = d.userid;
		nickname = d.nickname;
		reserved = d.reserved;
		return *this;
	}

};

} 
