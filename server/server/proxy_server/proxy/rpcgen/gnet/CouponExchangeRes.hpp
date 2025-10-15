
#pragma once


namespace gnet { 

class CouponExchangeRes : public GNET::Rpc::Data
{
public:
	int retcode;
	int remain_coupon;
	int today_exchange_coupon;
	int today_remain_exchange_coupon;
	int reserved1;
	int reserved2;

	CouponExchangeRes() {
		retcode = -1;
		remain_coupon = -1;
		today_exchange_coupon = -1;
		today_remain_exchange_coupon = -1;
		reserved1 = 0;
		reserved2 = 0;
	}

	CouponExchangeRes(int _retcode_, int _remain_coupon_, int _today_exchange_coupon_, int _today_remain_exchange_coupon_, int _reserved1_, int _reserved2_)
		: retcode(_retcode_), remain_coupon(_remain_coupon_), today_exchange_coupon(_today_exchange_coupon_), today_remain_exchange_coupon(_today_remain_exchange_coupon_), reserved1(_reserved1_), reserved2(_reserved2_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << retcode;
		_os_ << remain_coupon;
		_os_ << today_exchange_coupon;
		_os_ << today_remain_exchange_coupon;
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> retcode;
		_os_ >> remain_coupon;
		_os_ >> today_exchange_coupon;
		_os_ >> today_remain_exchange_coupon;
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new CouponExchangeRes(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const CouponExchangeRes *o = dynamic_cast<const CouponExchangeRes*>(&d)) return *this = *o;
		return *this;
	}

	CouponExchangeRes& operator=(const CouponExchangeRes & d)
	{
		if (&d == this) return *this;
		retcode = d.retcode;
		remain_coupon = d.remain_coupon;
		today_exchange_coupon = d.today_exchange_coupon;
		today_remain_exchange_coupon = d.today_remain_exchange_coupon;
		reserved1 = d.reserved1;
		reserved2 = d.reserved2;
		return *this;
	}

	bool operator<(const CouponExchangeRes &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const CouponExchangeRes &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = remain_coupon - _o_.remain_coupon;
		if (0 != _c_) return _c_;
		_c_ = today_exchange_coupon - _o_.today_exchange_coupon;
		if (0 != _c_) return _c_;
		_c_ = today_remain_exchange_coupon - _o_.today_remain_exchange_coupon;
		if (0 != _c_) return _c_;
		_c_ = reserved1 - _o_.reserved1;
		if (0 != _c_) return _c_;
		_c_ = reserved2 - _o_.reserved2;
		if (0 != _c_) return _c_;
		return _c_;
	}

};

} 
