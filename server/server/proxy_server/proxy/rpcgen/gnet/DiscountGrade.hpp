
#pragma once


namespace gnet { 

class DiscountGrade : public GNET::Rpc::Data
{
public:
	int amount_begin;
	int discount;
	int reserved1;
	GNET::Octets reserved2;

	DiscountGrade() {
		amount_begin = 0;
		discount = 0;
		reserved1 = 0;
	}

	DiscountGrade(int _amount_begin_, int _discount_, int _reserved1_, const GNET::Octets & _reserved2_)
		: amount_begin(_amount_begin_), discount(_discount_), reserved1(_reserved1_), reserved2(_reserved2_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << amount_begin;
		_os_ << discount;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> amount_begin;
		_os_ >> discount;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new DiscountGrade(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const DiscountGrade *o = dynamic_cast<const DiscountGrade*>(&d)) return *this = *o;
		return *this;
	}

	DiscountGrade& operator=(const DiscountGrade & d)
	{
		if (&d == this) return *this;
		amount_begin = d.amount_begin;
		discount = d.discount;
		reserved1 = d.reserved1;
		reserved2 = d.reserved2;
		return *this;
	}

};

} 
