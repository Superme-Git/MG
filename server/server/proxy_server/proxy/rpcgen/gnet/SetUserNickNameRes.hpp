
#pragma once


namespace gnet { 

class SetUserNickNameRes : public GNET::Rpc::Data
{
public:
	int retcode;
	int reserved;

	SetUserNickNameRes() {
		retcode = 0;
		reserved = 0;
	}

	SetUserNickNameRes(int _retcode_, int _reserved_)
		: retcode(_retcode_), reserved(_reserved_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> reserved;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new SetUserNickNameRes(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const SetUserNickNameRes *o = dynamic_cast<const SetUserNickNameRes*>(&d)) return *this = *o;
		return *this;
	}

	SetUserNickNameRes& operator=(const SetUserNickNameRes & d)
	{
		if (&d == this) return *this;
		retcode = d.retcode;
		reserved = d.reserved;
		return *this;
	}

	bool operator<(const SetUserNickNameRes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SetUserNickNameRes &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = reserved - _o_.reserved;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
