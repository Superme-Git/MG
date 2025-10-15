
#pragma once


namespace gnet { 

class MailGoodsInventory : public GNET::Rpc::Data
{
public:
	int goods_id;
	int count;
	int proctype;
	int goods_flag;
	int goods_price;
	int goods_price_before_discount;
	int reserved1;
	GNET::Octets reserved2;

	MailGoodsInventory() {
		goods_id = 0;
		count = 0;
		proctype = 0;
		goods_flag = 0;
		goods_price = 0;
		goods_price_before_discount = 0;
		reserved1 = 0;
	}

	MailGoodsInventory(int _goods_id_, int _count_, int _proctype_, int _goods_flag_, int _goods_price_, int _goods_price_before_discount_, int _reserved1_, const GNET::Octets & _reserved2_)
		: goods_id(_goods_id_), count(_count_), proctype(_proctype_), goods_flag(_goods_flag_), goods_price(_goods_price_), goods_price_before_discount(_goods_price_before_discount_), reserved1(_reserved1_), reserved2(_reserved2_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << goods_id;
		_os_ << count;
		_os_ << proctype;
		_os_ << goods_flag;
		_os_ << goods_price;
		_os_ << goods_price_before_discount;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> goods_id;
		_os_ >> count;
		_os_ >> proctype;
		_os_ >> goods_flag;
		_os_ >> goods_price;
		_os_ >> goods_price_before_discount;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new MailGoodsInventory(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const MailGoodsInventory *o = dynamic_cast<const MailGoodsInventory*>(&d)) return *this = *o;
		return *this;
	}

	MailGoodsInventory& operator=(const MailGoodsInventory & d)
	{
		if (&d == this) return *this;
		goods_id = d.goods_id;
		count = d.count;
		proctype = d.proctype;
		goods_flag = d.goods_flag;
		goods_price = d.goods_price;
		goods_price_before_discount = d.goods_price_before_discount;
		reserved1 = d.reserved1;
		reserved2 = d.reserved2;
		return *this;
	}

};

} 
