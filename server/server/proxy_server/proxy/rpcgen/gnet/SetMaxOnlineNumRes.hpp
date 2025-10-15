
#pragma once


namespace gnet { 

class SetMaxOnlineNumRes : public GNET::Rpc::Data
{
public:
	int retcode;

	SetMaxOnlineNumRes() {
		retcode = 0;
	}

	SetMaxOnlineNumRes(int _retcode_)
		: retcode(_retcode_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new SetMaxOnlineNumRes(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const SetMaxOnlineNumRes *o = dynamic_cast<const SetMaxOnlineNumRes*>(&d)) return *this = *o;
		return *this;
	}

	SetMaxOnlineNumRes& operator=(const SetMaxOnlineNumRes & d)
	{
		if (&d == this) return *this;
		retcode = d.retcode;
		return *this;
	}

	bool operator<(const SetMaxOnlineNumRes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SetMaxOnlineNumRes &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
