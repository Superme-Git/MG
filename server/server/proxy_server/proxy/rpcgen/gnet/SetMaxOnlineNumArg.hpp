
#pragma once


namespace gnet { 

class SetMaxOnlineNumArg : public GNET::Rpc::Data
{
public:
	int maxnum;
	int fake_maxnum;

	SetMaxOnlineNumArg() {
		maxnum = 0;
		fake_maxnum = 0;
	}

	SetMaxOnlineNumArg(int _maxnum_, int _fake_maxnum_)
		: maxnum(_maxnum_), fake_maxnum(_fake_maxnum_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << maxnum;
		_os_ << fake_maxnum;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> maxnum;
		_os_ >> fake_maxnum;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new SetMaxOnlineNumArg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const SetMaxOnlineNumArg *o = dynamic_cast<const SetMaxOnlineNumArg*>(&d)) return *this = *o;
		return *this;
	}

	SetMaxOnlineNumArg& operator=(const SetMaxOnlineNumArg & d)
	{
		if (&d == this) return *this;
		maxnum = d.maxnum;
		fake_maxnum = d.fake_maxnum;
		return *this;
	}

	bool operator<(const SetMaxOnlineNumArg &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const SetMaxOnlineNumArg &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = maxnum - _o_.maxnum;
		if (0 != _c_) return _c_;
		_c_ = fake_maxnum - _o_.fake_maxnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
