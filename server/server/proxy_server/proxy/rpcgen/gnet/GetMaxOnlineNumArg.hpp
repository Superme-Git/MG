
#pragma once


namespace gnet { 

class GetMaxOnlineNumArg : public GNET::Rpc::Data
{
public:
	int padding;

	GetMaxOnlineNumArg() {
		padding = 0;
	}

	GetMaxOnlineNumArg(int _padding_)
		: padding(_padding_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << padding;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> padding;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new GetMaxOnlineNumArg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const GetMaxOnlineNumArg *o = dynamic_cast<const GetMaxOnlineNumArg*>(&d)) return *this = *o;
		return *this;
	}

	GetMaxOnlineNumArg& operator=(const GetMaxOnlineNumArg & d)
	{
		if (&d == this) return *this;
		padding = d.padding;
		return *this;
	}

	bool operator<(const GetMaxOnlineNumArg &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const GetMaxOnlineNumArg &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = padding - _o_.padding;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
