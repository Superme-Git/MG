
#pragma once


namespace gnet { 

class SSOUser : public FireNet::Marshal
{
public:
	char isagent;
	int64_t userid;
	FireNet::Octets account;
	FireNet::Octets agentname;
	FireNet::Octets agentaccount;

	SSOUser() {
		isagent = 0;
		userid = 0;
	}

	SSOUser(char _isagent_, int64_t _userid_, const FireNet::Octets & _account_, const FireNet::Octets & _agentname_, const FireNet::Octets & _agentaccount_)
		: isagent(_isagent_), userid(_userid_), account(_account_), agentname(_agentname_), agentaccount(_agentaccount_) {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		_os_ << isagent;
		_os_ << userid;
		_os_ << account;
		_os_ << agentname;
		_os_ << agentaccount;
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		_os_ >> isagent;
		_os_ >> userid;
		_os_ >> account;
		_os_ >> agentname;
		_os_ >> agentaccount;
		return _os_;
	}

};

} 
