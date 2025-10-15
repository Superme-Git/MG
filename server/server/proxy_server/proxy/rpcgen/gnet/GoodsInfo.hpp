
#pragma once


namespace gnet { 

class GoodsInfo : public GNET::Rpc::Data
{
public:
	int goods_id;
	int count;
	int flagmask;
	int timelimit;
	int reserved1;
	GNET::Octets reserved2;

	GoodsInfo() {
		goods_id = 0;
		count = 0;
		flagmask = 0;
		timelimit = 0;
		reserved1 = 0;
	}

	GoodsInfo(int _goods_id_, int _count_, int _flagmask_, int _timelimit_, int _reserved1_, const GNET::Octets & _reserved2_)
		: goods_id(_goods_id_), count(_count_), flagmask(_flagmask_), timelimit(_timelimit_), reserved1(_reserved1_), reserved2(_reserved2_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << goods_id;
		_os_ << count;
		_os_ << flagmask;
		_os_ << timelimit;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> goods_id;
		_os_ >> count;
		_os_ >> flagmask;
		_os_ >> timelimit;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new GoodsInfo(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const GoodsInfo *o = dynamic_cast<const GoodsInfo*>(&d)) return *this = *o;
		return *this;
	}

	GoodsInfo& operator=(const GoodsInfo & d)
	{
		if (&d == this) return *this;
		goods_id = d.goods_id;
		count = d.count;
		flagmask = d.flagmask;
		timelimit = d.timelimit;
		reserved1 = d.reserved1;
		reserved2 = d.reserved2;
		return *this;
	}

};

} 
