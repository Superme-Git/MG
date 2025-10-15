
#pragma once

#include "gnet/DiscountGrade.hpp"
#include <vector>

namespace gnet { 

class MerchantDiscount : public GNET::Rpc::Data
{
public:
	int id;
	GNET::Octets name;
	int reserved;
	std::vector<gnet::DiscountGrade> discount;

	MerchantDiscount() {
		id = 0;
		reserved = 0;
	}

	MerchantDiscount(int _id_, const GNET::Octets & _name_, int _reserved_, const std::vector<gnet::DiscountGrade> & _discount_)
		: id(_id_), name(_name_), reserved(_reserved_), discount(_discount_) {
	}

	bool _validator_() const {
		for (std::vector<gnet::DiscountGrade>::const_iterator _i_ = discount.begin(); _i_ != discount.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << id;
		_os_ << name;
		_os_ << reserved;
		_os_ << GNET::MarshalContainer(discount);
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> id;
		_os_ >> name;
		_os_ >> reserved;
		_os_ >> GNET::MarshalContainer(discount);
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new MerchantDiscount(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const MerchantDiscount *o = dynamic_cast<const MerchantDiscount*>(&d)) return *this = *o;
		return *this;
	}

	MerchantDiscount& operator=(const MerchantDiscount & d)
	{
		if (&d == this) return *this;
		id = d.id;
		name = d.name;
		reserved = d.reserved;
		discount = d.discount;
		return *this;
	}

};

} 
