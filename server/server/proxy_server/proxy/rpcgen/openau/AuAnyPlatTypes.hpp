
#pragma once


namespace openau { 

class AuAnyPlatTypes : public GNET::Rpc::Data
{
public:
	enum {
		unknow = -1,
	};

	AuAnyPlatTypes() {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new AuAnyPlatTypes(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const AuAnyPlatTypes *o = dynamic_cast<const AuAnyPlatTypes*>(&d)) return *this = *o;
		return *this;
	}

	AuAnyPlatTypes& operator=(const AuAnyPlatTypes & d)
	{
		if (&d == this) return *this;
		return *this;
	}

	bool operator<(const AuAnyPlatTypes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const AuAnyPlatTypes &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
