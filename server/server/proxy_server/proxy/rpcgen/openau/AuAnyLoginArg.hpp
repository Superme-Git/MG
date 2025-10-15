
#pragma once


namespace openau { 

class AuAnyLoginArg : public GNET::Rpc::Data
{
public:
	GNET::Octets account;
	GNET::Octets response;
	GNET::Octets challenge;
	GNET::Octets loginipoctets;
	int loginip;
	int plattype;
	int gameid;
	GNET::Octets gamekey;
	GNET::Octets serverid;
	GNET::Octets selfchannel; // 渠道号
	GNET::Octets childchannel; // 子渠道号
	GNET::Octets deviceid; // 设备唯一标识

	AuAnyLoginArg() {
		loginip = 0;
		plattype = 0;
		gameid = 0;
	}

	AuAnyLoginArg(const GNET::Octets & _account_, const GNET::Octets & _response_, const GNET::Octets & _challenge_, const GNET::Octets & _loginipoctets_, int _loginip_, int _plattype_, int _gameid_, const GNET::Octets & _gamekey_, const GNET::Octets & _serverid_, const GNET::Octets & _selfchannel_, const GNET::Octets & _childchannel_, const GNET::Octets & _deviceid_)
		: account(_account_), response(_response_), challenge(_challenge_), loginipoctets(_loginipoctets_), loginip(_loginip_), plattype(_plattype_), gameid(_gameid_), gamekey(_gamekey_), serverid(_serverid_), selfchannel(_selfchannel_), childchannel(_childchannel_), deviceid(_deviceid_) {
	}

	bool _validator_() const {
		return true;
	}

	GNET::Marshal::OctetsStream & marshal(GNET::Marshal::OctetsStream & _os_) const {
		_os_ << account;
		_os_ << response;
		_os_ << challenge;
		_os_ << loginipoctets;
		_os_ << loginip;
		_os_ << plattype;
		_os_ << gameid;
		_os_ << gamekey;
		_os_ << serverid;
		_os_ << selfchannel;
		_os_ << childchannel;
		_os_ << deviceid;
		return _os_;
	}

	const GNET::Marshal::OctetsStream & unmarshal(const GNET::Marshal::OctetsStream & _os_) {
		_os_ >> account;
		_os_ >> response;
		_os_ >> challenge;
		_os_ >> loginipoctets;
		_os_ >> loginip;
		_os_ >> plattype;
		_os_ >> gameid;
		_os_ >> gamekey;
		_os_ >> serverid;
		_os_ >> selfchannel;
		_os_ >> childchannel;
		_os_ >> deviceid;
		return _os_;
	}

	virtual GNET::Rpc::Data * Clone() const 
	{
		return new AuAnyLoginArg(*this);
	}

	virtual GNET::Rpc::Data & operator=(const GNET::Rpc::Data & d)
	{
		if (&d == this) return *this;
		if (const AuAnyLoginArg *o = dynamic_cast<const AuAnyLoginArg*>(&d)) return *this = *o;
		return *this;
	}

	AuAnyLoginArg& operator=(const AuAnyLoginArg & d)
	{
		if (&d == this) return *this;
		account = d.account;
		response = d.response;
		challenge = d.challenge;
		loginipoctets = d.loginipoctets;
		loginip = d.loginip;
		plattype = d.plattype;
		gameid = d.gameid;
		gamekey = d.gamekey;
		serverid = d.serverid;
		selfchannel = d.selfchannel;
		childchannel = d.childchannel;
		deviceid = d.deviceid;
		return *this;
	}

};

} 
