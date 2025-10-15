
#pragma once


namespace gnet { 

class InstantAddCashArg : public GNET::Rpc::Data
{
public:
	int userid;
	int loginip;
	GNET::Octets cardnum; // ≥‰÷µø®∫≈
	GNET::Octets cardpasswd; // ≥‰÷µø®√‹¬Î
	int reserved;

	InstantAddCashArg() {
		userid = 0;
		loginip = 0;
		reserved = 0;
	}

	InstantAddCashArg(int _userid_, int _loginip_, const GNET::Octets & _cardnum_, const GNET::Octets & _cardpasswd_, int _reserved_)
		: userid(_userid_), loginip(_loginip_), cardnum(_cardnum_), cardpasswd(_cardpasswd_), reserved(_reserved_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << userid;
		_os_ << loginip;
		_os_ << cardnum;
		_os_ << cardpasswd;
		_os_ << reserved;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> userid;
		_os_ >> loginip;
		_os_ >> cardnum;
		_os_ >> cardpasswd;
		_os_ >> reserved;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new InstantAddCashArg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const InstantAddCashArg *o = dynamic_cast<const InstantAddCashArg*>(&d)) return *this = *o;
		return *this;
	}

	InstantAddCashArg& operator=(const InstantAddCashArg & d)
	{
		if (&d == this) return *this;
		userid = d.userid;
		loginip = d.loginip;
		cardnum = d.cardnum;
		cardpasswd = d.cardpasswd;
		reserved = d.reserved;
		return *this;
	}

};

} 
