	// Protocol

	enum { PROTOCOL_TYPE = 8064 };

	std::vector<gnet::MerchantDiscount> discount;
	int reserved1;
	GNET::Octets reserved2;

	DiscountAnnounce() {
		type = PROTOCOL_TYPE;
		reserved1 = 0;
	}

	DiscountAnnounce(const std::vector<gnet::MerchantDiscount> & _discount_, int _reserved1_, const GNET::Octets & _reserved2_)
		: discount(_discount_), reserved1(_reserved1_), reserved2(_reserved2_) {
		type = PROTOCOL_TYPE;
	}

	explicit DiscountAnnounce(void *) : Protocol(PROTOCOL_TYPE) { } // define stub

	bool _validator_() const {
		for (std::vector<gnet::MerchantDiscount>::const_iterator _i_ = discount.begin(); _i_ != discount.end(); ++_i_)
			if (!_i_->_validator_()) return false;
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << GNET::MarshalContainer(discount);
		_os_ << reserved1;
		_os_ << reserved2;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> GNET::MarshalContainer(discount);
		_os_ >> reserved1;
		_os_ >> reserved2;
		return _os_;
	}

	GNET::Protocol * Clone() const { return new DiscountAnnounce(*this); }
	int  PriorPolicy( ) const { return 1; }
	bool SizePolicy(size_t size) const { return size <= 512; }
