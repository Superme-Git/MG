
#pragma once


namespace gnet { 

class SSOUser : public GNET::Rpc::Data
{
public:
	char isagent;
	int64_t userid;
	GNET::Octets account;
	GNET::Octets agentname;
	GNET::Octets agentaccount;

	SSOUser() {
		isagent = 0;
		userid = 0;
	}

	SSOUser(char _isagent_, int64_t _userid_, const GNET::Octets & _account_, const GNET::Octets & _agentname_, const GNET::Octets & _agentaccount_)
		: isagent(_isagent_), userid(_userid_), account(_account_), agentname(_agentname_), agentaccount(_agentaccount_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << isagent;
		_os_ << userid;
		_os_ << account;
		_os_ << agentname;
		_os_ << agentaccount;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> isagent;
		_os_ >> userid;
		_os_ >> account;
		_os_ >> agentname;
		_os_ >> agentaccount;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new SSOUser(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const SSOUser *o = dynamic_cast<const SSOUser*>(&d)) return *this = *o;
		return *this;
	}

	SSOUser& operator=(const SSOUser & d)
	{
		if (&d == this) return *this;
		isagent = d.isagent;
		userid = d.userid;
		account = d.account;
		agentname = d.agentname;
		agentaccount = d.agentaccount;
		return *this;
	}

};

} 
