
#pragma once


namespace gnet { 

class CouponExchangeArg : public GNET::Rpc::Data
{
public:
	int userid;
	int coupon_number;
	int cash_number;
	int64_t timestamp;
	int reserved1;
	int reserved2;

	CouponExchangeArg() {
		userid = -1;
		coupon_number = -1;
		cash_number = -1;
		timestamp = -1;
		reserved1 = 0;
		reserved2 = 0;
	}

	CouponExchangeArg(int _userid_, int _coupon_number_, int _cash_number_, int64_t _timestamp_, int _reserved1_, int _reserved2_)
		: userid(_userid_), coupon_number(_coupon_number_), cash_number(_cash_number_), timestamp(_timestamp_), reserved1(_reserved1_), reserved2(_reserved2_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << coupon_number;
		_os_ << cash_number;
		_os_ << timestamp;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> coupon_number;
		_os_ >> cash_number;
		_os_ >> timestamp;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new CouponExchangeArg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const CouponExchangeArg *o = dynamic_cast<const CouponExchangeArg*>(&d)) return *this = *o;
		return *this;
	}

	CouponExchangeArg& operator=(const CouponExchangeArg & d)
	{
		if (&d == this) return *this;
		userid = d.userid;
		coupon_number = d.coupon_number;
		cash_number = d.cash_number;
		timestamp = d.timestamp;
		reserved1 = d.reserved1;
		reserved2 = d.reserved2;
		return *this;
	}

	bool operator<(const CouponExchangeArg &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CouponExchangeArg &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = coupon_number - _o_.coupon_number;
		if (0 != _c_) return _c_;
		_c_ = cash_number - _o_.cash_number;
		if (0 != _c_) return _c_;
		_c_ = GNET::signum(timestamp - _o_.timestamp);
		if (0 != _c_) return _c_;
		_c_ = reserved1 - _o_.reserved1;
		if (0 != _c_) return _c_;
		_c_ = reserved2 - _o_.reserved2;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
