
#pragma once

#include "gnet/GoodsInfo.hpp"
#include <vector>

namespace gnet { 

class FunctionInfo : public GNET::Rpc::Data
{
public:
	int function_id;
	GNET::Octets name;
	int count;
	int price;
	int price_before_discount;
	std::vector<gnet::GoodsInfo> goods;
	int reserved1;
	GNET::Octets reserved2;

	FunctionInfo() {
		function_id = 0;
		count = 0;
		price = 0;
		price_before_discount = 0;
		reserved1 = 0;
	}

	FunctionInfo(int _function_id_, const GNET::Octets & _name_, int _count_, int _price_, int _price_before_discount_, const std::vector<gnet::GoodsInfo> & _goods_, int _reserved1_, const GNET::Octets & _reserved2_)
		: function_id(_function_id_), name(_name_), count(_count_), price(_price_), price_before_discount(_price_before_discount_), goods(_goods_), reserved1(_reserved1_), reserved2(_reserved2_) {
	}

	bool _validator_() const {
		for (std::vector<gnet::GoodsInfo>::const_iterator _i_ = goods.begin(); _i_ != goods.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << function_id;
		_os_ << name;
		_os_ << count;
		_os_ << price;
		_os_ << price_before_discount;
		_os_ << GNET::MarshalContainer(goods);
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> function_id;
		_os_ >> name;
		_os_ >> count;
		_os_ >> price;
		_os_ >> price_before_discount;
		_os_ >> GNET::MarshalContainer(goods);
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new FunctionInfo(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const FunctionInfo *o = dynamic_cast<const FunctionInfo*>(&d)) return *this = *o;
		return *this;
	}

	FunctionInfo& operator=(const FunctionInfo & d)
	{
		if (&d == this) return *this;
		function_id = d.function_id;
		name = d.name;
		count = d.count;
		price = d.price;
		price_before_discount = d.price_before_discount;
		goods = d.goods;
		reserved1 = d.reserved1;
		reserved2 = d.reserved2;
		return *this;
	}

};

} 
